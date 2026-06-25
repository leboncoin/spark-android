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
            ),
        )

        submitMetrics(metrics, SERVICE_NAME, dryRun, verbose)
    }
}

ReleaseMetrics().main(args)
