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
package com.adevinta.spark.catalog.baselineprofile

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
@LargeTest
internal class AllComponentsBenchmarks(
    private val componentId: String,
    private val componentName: String,
    private val exampleCount: Int,
) {

    @get:Rule
    val rule = MacrobenchmarkRule()

    private val packageName: String
        get() = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg")

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun benchmark() = rule.measureRepeated(
        packageName = packageName,
        metrics = listOf(
            FrameTimingMetric(),
            MemoryUsageMetric(MemoryUsageMetric.Mode.Last),
            TraceSectionMetric("Spark::$componentName", TraceSectionMetric.Mode.Sum),
            CompositionMetric("Spark::$componentName"),
        ),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.WARM,
        iterations = 5,
        setupBlock = {
            pressHome()
        },
        measureBlock = {
            repeat(exampleCount) { index ->
                startActivityAndWait(
                    Intent().apply {
                        setPackage(packageName)
                        setAction("com.adevinta.spark.catalog.BENCHMARK")
                        putExtra("component_id", componentId)
                        putExtra("example_index", index)
                    },
                )
                device.wait(Until.hasObject(By.res("benchmark_example")), 5_000)
                device.waitForIdle()
            }
        },
    )

    companion object {
        @JvmStatic
        @Parameters(name = "{1}")
        fun components(): List<Array<Any>> = listOf(
            arrayOf("animations", "Animations", 2),
            arrayOf("badges", "Badges", 1),
            arrayOf("bottomsheets", "BottomSheets", 1),
            arrayOf("buttons", "Buttons", 4),
            arrayOf("cards", "Cards", 2),
            arrayOf("comboBox", "ComboBox", 1),
            arrayOf("checkboxes", "Checkboxes", 2),
            arrayOf("chips", "Chips", 2),
            arrayOf("dialogs", "Dialogs", 2),
            arrayOf("dividers", "Dividers", 2),
            arrayOf("dropdowns", "Dropdowns", 1),
            arrayOf("icons", "Icons", 1),
            arrayOf("icon-buttons", "IconButtons", 2),
            arrayOf("icon-toggle-buttons", "IconToggleButtons", 1),
            arrayOf("fileupload", "File upload", 1),
            arrayOf("meter", "Meter", 1),
            arrayOf("popovers", "Popovers", 1),
            arrayOf("progressbars", "Progressbars", 2),
            arrayOf("progress-tracker", "Progress Tracker", 1),
            arrayOf("radio-buttons", "Radio buttons", 2),
            arrayOf("ratings", "Ratings", 2),
            arrayOf("segmented-control", "Segmented Control", 1),
            arrayOf("gauge", "Segmented Gauge", 1),
            arrayOf("skeleton", "Skeletons", 2),
            arrayOf("slider", "Slider", 2),
            arrayOf("snackbars", "Snackbars", 1),
            arrayOf("steppers", "Steppers", 1),
            arrayOf("switches", "Switches", 2),
            arrayOf("tabs", "Tabs", 2),
            arrayOf("tags", "Tags", 2),
            arrayOf("textFields", "TextFields", 2),
            arrayOf("textLinks", "TextLinks", 1),
        )
    }
}
