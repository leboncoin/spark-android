package com.adevinta.spark.catalog.baselineprofile

import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@LargeTest
internal class StepperBenchmark : ScenarioBenchmark() {

    /** Increments and decrements to trigger the value text slide animation. */
    @Test
    fun animateValue() = measureScenario("stepper-animate")
}
