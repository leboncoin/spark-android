package com.adevinta.spark.catalog.baselineprofile

import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@LargeTest
internal class ButtonBenchmark : ScenarioBenchmark() {

    @Test
    fun static() = measureScenario("button-static", measureFrames = false)

    @Test
    fun changeIntent() = measureScenario("button-intent")

    @Test
    fun loading() = measureScenario("button-loading")
}
