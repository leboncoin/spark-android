#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:Import("../utils/clikt.main.kts")
@file:Import("../utils/files.main.kts")
@file:Import("../utils/datadog.main.kts")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.useLines

private val SERVICE_NAME = "library-metrics"

class LibraryMetrics : CliktCommand("library-metrics.main.kts") {

    override fun help(context: Context) = "Submits library metrics (icons, docs) to Datadog."

    private val projectDir by projectDir()
    private val dryRun by dryRun()
    private val verbose by verbose()

    override fun run() {
        val metrics = mutableListOf<SparkMetric>()

        // Icon count
        val iconCount = countIcons(projectDir)
        if (verbose) echo("Icon count: $iconCount")
        metrics += SparkMetric(
            name = "spark.health.icons.count",
            value = iconCount.toDouble(),
            tags = listOf("module:spark-icons"),
        )

        // Docs missing and pages
        val (docsPages, docsMissing) = countDocs(projectDir)
        if (verbose) {
            echo("Docs pages: $docsPages")
            echo("Docs missing: $docsMissing")
        }
        metrics += SparkMetric(
            name = "spark.health.docs.pages",
            value = docsPages.toDouble(),
            tags = listOf("module:spark"),
        )
        metrics += SparkMetric(
            name = "spark.health.docs.missing",
            value = docsMissing.toDouble(),
            tags = listOf("module:spark"),
        )

        submitMetrics(metrics, SERVICE_NAME, dryRun, verbose)
    }

    private fun countIcons(projectDir: Path): Int {
        val iconsFile = projectDir
            .resolve("spark-icons/src/main/kotlin/com/adevinta/spark/icons/LeboncoinIcons.kt")
        return iconsFile.useLines { lines -> lines.count { it.startsWith("public val LeboncoinIcons.") } }
    }

    private fun countDocs(projectDir: Path): Pair<Int, Int> {
        val componentsDir = projectDir
            .resolve("spark/src/main/kotlin/com/adevinta/spark/components")

        val componentDirs = componentsDir.listDirectoryEntries()
            .filter { it.isDirectory() }

        var docsPages = 0
        var docsMissing = 0

        for (dir in componentDirs) {
            val mdFiles = dir.listDirectoryEntries("*.md")
            docsPages += mdFiles.size
            if (mdFiles.none { it.name == "${dir.name}.md" }) {
                if (verbose) echo("Missing doc for component: ${dir.name}")
                docsMissing++
            }
        }

        return docsPages to docsMissing
    }
}

LibraryMetrics().main(args)
