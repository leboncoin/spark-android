#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:Import("../utils/clikt.main.kts")
@file:Import("../utils/datadog.main.kts")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import java.time.Instant
import java.time.temporal.ChronoUnit

private val SERVICE_NAME = "release-metrics"

/**
 * Submits release cadence metric to Datadog.
 *
 * Calculates days between the previous release and now.
 */
class ReleaseMetrics : CliktCommand("release-metrics.main.kts") {

    override fun help(context: Context) = "Submits release cadence metric to Datadog."

    private val dryRun by dryRun()
    private val verbose by verbose()
    private val previousRelease by option(
        "--previous-release",
        help = "ISO-8601 timestamp of the previous release (e.g. 2024-01-15T10:30:00Z)",
    ).required()

    override fun run() {
        val previous = Instant.parse(previousRelease)
        val now = Instant.now()
        val daysSincePrevious = ChronoUnit.DAYS.between(previous, now)

        if (verbose) {
            echo("Previous release: $previous")
            echo("Now: $now")
            echo("Days since previous: $daysSincePrevious")
        }

        val metrics = listOf(
            SparkMetric(
                name = "spark.health.release.cadence",
                value = daysSincePrevious.toDouble(),
            )
        )

        submitMetrics(metrics, SERVICE_NAME, dryRun, verbose)
    }
}

ReleaseMetrics().main(args)
