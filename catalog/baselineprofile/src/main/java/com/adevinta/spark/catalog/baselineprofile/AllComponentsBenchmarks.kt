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
import android.content.Intent
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
internal class AllComponentsBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    private val packageName: String
        get() = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg")

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun scrollAllComponents() = rule.measureRepeated(
        packageName = packageName,
        metrics = listOf(
            FrameTimingMetric(),
            MemoryUsageMetric(MemoryUsageMetric.Mode.Last),
            TraceSectionMetric("Spark::%", TraceSectionMetric.Mode.Sum),
            CompositionMetric("Spark::%"),
        ),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            startActivityAndWait(Intent().apply {
                setPackage(packageName)
                setAction("com.adevinta.spark.catalog.BENCHMARK")
            })
            device.wait(Until.hasObject(By.res("benchmark_list")), 10_000)
        },
        measureBlock = {
            val list = device.findObject(By.res("benchmark_list")) ?: return@measureRepeated
            list.setGestureMargin(device.displayWidth / 4)

            var canScroll = true
            while (canScroll) {
                canScroll = list.scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
        },
    )
}
