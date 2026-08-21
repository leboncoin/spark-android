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
