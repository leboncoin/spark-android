#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.10.0")
@file:Import("../utils/clikt.main.kts")
@file:Import("../utils/datadog.main.kts")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.nio.file.Path
import kotlin.io.path.readText

private val SERVICE_NAME = "ci-metrics"

private val TRACKED_JOBS = setOf(
    "validation",
    "checks",
    "build",
    "test",
    "lint",
    "dokka",
)

private val LEADING_NON_ALPHA = Regex("^[^a-zA-Z]+")

class CiMetrics : CliktCommand("ci-metrics.main.kts") {

    override fun help(context: Context) = "Submits CI metrics (job durations, screenshot count) to Datadog."

    private val dryRun by dryRun()
    private val verbose by verbose()

    private val jobsJson by option("--jobs-json", help = "Path to JSON file with job durations")
        .path(mustExist = true, mustBeReadable = true, canBeDir = false)
        .required()

    private val screenshotCount by option("--screenshot-count", help = "Number of screenshot tests")
        .int()
        .required()

    override fun run() {
        val metrics = mutableListOf<SparkMetric>()

        // Parse job durations
        val jobDurations = parseJobDurations(jobsJson)
        for ((jobName, duration) in jobDurations) {
            val normalizedName = normalizeJobName(jobName)
            if (normalizedName in TRACKED_JOBS) {
                if (verbose) echo("Job '$jobName' -> '$normalizedName': ${duration}s")
                metrics += SparkMetric(
                    name = "spark.health.ci.duration",
                    value = duration,
                    tags = listOf("job:$normalizedName"),
                )
            } else if (verbose) {
                echo("Skipping untracked job: $jobName")
            }
        }

        // Screenshot count
        if (verbose) echo("Screenshot count: $screenshotCount")
        metrics += SparkMetric(
            name = "spark.health.screenshot.count",
            value = screenshotCount.toDouble(),
        )

        submitMetrics(metrics, SERVICE_NAME, dryRun, verbose)
    }

    private fun parseJobDurations(path: Path): Map<String, Double> {
        val json = Json.parseToJsonElement(path.readText())
        return json.jsonArray.associate { job ->
            val obj = job.jsonObject
            val name = obj["name"]?.jsonPrimitive?.content ?: ""
            val duration = obj["duration"]?.jsonPrimitive?.content?.toDoubleOrNull() ?: 0.0
            name to duration
        }
    }

    /**
     * Normalizes job names from GitHub Actions format to metric tag format.
     * E.g. "🛃 Validation" -> "validation", "👷 Build" -> "build"
     */
    private fun normalizeJobName(name: String): String {
        return name
            .replace(LEADING_NON_ALPHA, "")
            .trim()
            .lowercase()
    }
}

CiMetrics().main(args)
