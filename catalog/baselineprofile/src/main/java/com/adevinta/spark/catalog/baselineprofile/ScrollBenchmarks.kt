package com.adevinta.spark.catalog.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
internal class ScrollBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun scrollComponentList() = rule.measureRepeated(
        packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
        metrics = listOf(
            FrameTimingMetric(),
            MemoryUsageMetric(MemoryUsageMetric.Mode.Last),
            PowerMetric(PowerMetric.Type.Battery()),
            TraceSectionMetric("%Spark%", TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.scrollable(true)), 5_000)
        },
        measureBlock = {
            val list = device.findObject(By.scrollable(true)) ?: return@measureRepeated
            list.setGestureMargin(device.displayWidth / 4)
            repeat(3) {
                list.scroll(Direction.UP, 2f)
                device.waitForIdle()
            }
            repeat(3) {
                list.scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
        },
    )
}
