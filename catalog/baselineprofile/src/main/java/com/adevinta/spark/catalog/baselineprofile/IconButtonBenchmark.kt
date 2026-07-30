package com.adevinta.spark.catalog.baselineprofile

import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@LargeTest
internal class IconButtonBenchmark : ScenarioBenchmark() {

    /** Infinite bellShake animation. Stresses per-frame recomposition (see trace-analysis-report.md). */
    @Test
    fun bellShakeAnimation() = measureScenario("iconbutton-bellshake")
}
