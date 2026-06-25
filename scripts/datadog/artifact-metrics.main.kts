#!/usr/bin/env kotlin
/*
 * Copyright (c) 2026 Adevinta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:Import("../utils/clikt.main.kts")
@file:Import("../utils/datadog.main.kts")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import java.text.NumberFormat
import java.util.Locale
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText

private val SERVICE_NAME = "artifact-metrics"

// Regex to extract the size table block (APK/AAR section before DEX/JAR stats).
// Diffuse output has a size table with "compressed │ uncompressed" header, then a DEX/JAR stats table.
// We capture only the size table by matching from the header through the total row.
private val DIFFUSE_SIZE_TABLE =
    Regex("""compressed.*?total[^\n]*\n""", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

// Regex to parse a size row: section │ compressed │ uncompressed
// Matches: "      dex │    2,9 MiB │      6,5 MiB" or "  jar │  2,400,000 │  2,500,000"
private val DIFFUSE_SIZE_ROW =
    Regex("""^\s*(\w+)\s*[│|]\s*([\d,. ]+(?:KiB|MiB|B)?)\s*[│|]\s*([\d,. ]+(?:KiB|MiB|B)?)""", RegexOption.MULTILINE)

// Regex to parse count rows from DEX/JAR stats tables.
// AAR JAR: " classes │   779"         (two columns - captures first count)
// APK DEX: " classes │  6794 │  6794" (three columns - captures raw count, ignores unique)
private val DIFFUSE_COUNT_ROW = Regex("""^\s*(classes|methods|fields)\s*[│|]\s*([\d,]+)""", RegexOption.MULTILINE)

/**
 * Submits artifact size metrics to Datadog from pre-generated diffuse output files.
 *
 * Inputs are passed as `--input path` where the filename (without extension) is the module name:
 *   - `apk.txt`         → catalog APK diffuse output
 *   - `spark.txt`       → spark AAR diffuse output
 *   - `spark-icons.txt` → spark-icons AAR diffuse output
 *
 * **APK metrics** (module:catalog):
 *   spark.health.apk.size  — section:dex/res/native/total, kind:compressed/uncompressed
 *   spark.health.apk.dex   — stat:classes/methods/fields
 *
 * **AAR metrics** (module:spark, module:spark-icons):
 *   spark.health.aar.size  — section:jar/res/total, kind:compressed/uncompressed
 *   spark.health.aar.jar   — stat:classes/methods/fields
 */
class ArtifactMetrics : CliktCommand("artifact-metrics.main.kts") {

    override fun help(context: Context) =
        "Submits artifact size and composition metrics to Datadog from diffuse output files."

    private val dryRun by dryRun()
    private val verbose by verbose()
    private val inputFiles by inputs()

    override fun run() {
        val inputs = inputFiles.associateBy { it.nameWithoutExtension }
        val metrics = mutableListOf<SparkMetric>()

        inputs["apk"]?.let { path ->
            if (verbose) echo("Parsing APK diffuse output: $path")
            metrics += parseApkMetrics(path.readText())
        }

        for ((module, path) in inputs.filterKeys { it != "apk" }) {
            if (verbose) echo("Parsing $module AAR diffuse output: $path")
            metrics += parseAarMetrics(path.readText(), module)
        }

        submitMetrics(metrics, SERVICE_NAME, dryRun, verbose)
    }

    private fun parseApkMetrics(output: String): List<SparkMetric> {
        val metrics = mutableListOf<SparkMetric>()
        val sizes = parseSizeTable(output)

        for (section in listOf("dex", "res", "native", "total")) {
            val row = sizes[section] ?: continue
            metrics += sizeMetrics("spark.health.apk.size", listOf("module:catalog", "section:$section"), row)
        }

        for ((stat, count) in parseCountTable(output)) {
            metrics += SparkMetric("spark.health.apk.dex", count, listOf("module:catalog", "stat:$stat"))
        }

        return metrics
    }

    private fun parseAarMetrics(output: String, module: String): List<SparkMetric> {
        val metrics = mutableListOf<SparkMetric>()
        val sizes = parseSizeTable(output)

        for (section in listOf("jar", "res", "total")) {
            val row = sizes[section] ?: continue
            metrics += sizeMetrics("spark.health.aar.size", listOf("module:$module", "section:$section"), row)
        }

        for ((stat, count) in parseCountTable(output)) {
            metrics += SparkMetric("spark.health.aar.jar", count, listOf("module:$module", "stat:$stat"))
        }

        return metrics
    }

    private data class SizeRow(val compressed: Double, val uncompressed: Double)

    private fun sizeMetrics(metricName: String, tags: List<String>, row: SizeRow): List<SparkMetric> = listOf(
        SparkMetric(metricName, row.compressed, tags + "kind:compressed", unit = "byte"),
        SparkMetric(metricName, row.uncompressed, tags + "kind:uncompressed", unit = "byte"),
    )

    /**
     * Parses the size table from diffuse output into a map of section -> (compressed, uncompressed) bytes.
     * Scopes parsing to the size table block only (before DEX/JAR stats tables).
     */
    private fun parseSizeTable(output: String): Map<String, SizeRow> {
        val tableBlock = DIFFUSE_SIZE_TABLE.find(output)?.value
            ?: error("Could not find size table in diffuse output - unexpected format:\n$output")
        return DIFFUSE_SIZE_ROW.findAll(tableBlock).associate { match ->
            val section = match.groupValues[1]
            val compressed = parseSize(match.groupValues[2].trim())
            val uncompressed = parseSize(match.groupValues[3].trim())
            section to SizeRow(compressed, uncompressed)
        }
    }

    /**
     * Parses count rows (classes, methods, fields) from diffuse output.
     */
    private fun parseCountTable(
        output: String,
    ): Map<String, Double> = DIFFUSE_COUNT_ROW.findAll(output).associate { match ->
        val stat = match.groupValues[1]
        val count = match.groupValues[2].replace(",", "").toDouble()
        stat to count
    }

    /**
     * Converts a diffuse size string to bytes.
     *
     * Diffuse runs on GitHub-hosted Linux (en_US.UTF-8), so output is always English format:
     * comma as thousands separator, period as decimal (e.g. "1,024.5 KiB", "2,400,000").
     */
    private fun parseSize(value: String): Double {
        val fmt = NumberFormat.getInstance(Locale.ENGLISH)
        fun parseNumber(s: String) = fmt.parse(s)!!.toDouble()

        return when {
            value.endsWith("MiB") -> (parseNumber(value.removeSuffix("MiB").trim()) * 1024 * 1024).toLong().toDouble()
            value.endsWith("KiB") -> (parseNumber(value.removeSuffix("KiB").trim()) * 1024).toLong().toDouble()
            value.endsWith("B") -> parseNumber(value.removeSuffix("B").trim())
            else -> parseNumber(value)
        }
    }
}

ArtifactMetrics().main(args)
