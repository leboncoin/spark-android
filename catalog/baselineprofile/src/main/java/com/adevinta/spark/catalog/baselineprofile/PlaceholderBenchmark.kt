package com.adevinta.spark.catalog.baselineprofile

import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@LargeTest
internal class PlaceholderBenchmark : ScenarioBenchmark() {

    /** Toggles placeholder visibility. Exercises the Modifier.Node skeleton animation. */
    @Test
    fun toggleVisibility() = measureScenario("placeholder")
}
