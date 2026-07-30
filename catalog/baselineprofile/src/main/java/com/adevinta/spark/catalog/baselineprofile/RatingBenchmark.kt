package com.adevinta.spark.catalog.baselineprofile

import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@LargeTest
internal class RatingBenchmark : ScenarioBenchmark() {

    /** Static rating grid. Stresses the per-star icon painter cost (see trace-analysis-report.md). */
    @Test
    fun display() = measureScenario("ratings")
}
