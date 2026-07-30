@file:OptIn(ExperimentalMetricApi::class)

package com.adevinta.spark.catalog.baselineprofile

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceMetric
import androidx.benchmark.traceprocessor.TraceProcessor
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

internal class CompositionMetric(private val composable: String) : TraceMetric() {
    override fun getMeasurements(
        captureInfo: CaptureInfo,
        traceSession: TraceProcessor.Session,
    ): List<Measurement> {
        val shortName = composable.substringAfterLast(".")

        val durationsNs = traceSession.query(
            """
                SELECT slice.dur FROM slice
                    INNER JOIN thread_track on slice.track_id = thread_track.id
                    INNER JOIN thread USING(utid)
                    INNER JOIN process USING(upid)
                WHERE process.name LIKE "${captureInfo.targetPackageName}"
                    AND slice.name LIKE "$composable"
            """.trimIndent(),
        ).map { it.long("dur") }.toList()

        return listOf(
            Measurement(
                "${shortName}RecomposeDurMs",
                durationsNs.sumOf { it }.nanoseconds.toDouble(DurationUnit.MILLISECONDS),
            ),
            Measurement("${shortName}RecomposeCount", durationsNs.count().toDouble()),
        )
    }
}
