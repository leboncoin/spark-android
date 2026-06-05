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
package com.adevinta.spark.components.meter

import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

internal class MeterScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = V_SCROLL,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun default() {
        paparazzi.sparkSnapshotNightMode {
            SparkTheme {
                Meter.Circular(
                    value = 70f,
                    content = CircularMeterContent.ValueLabel("70%", label = "Label"),
                    intent = MeterIntent.Support,
                    size = CircleMeterSize.Large,
                )
            }
        }
    }

    @Test
    fun allSizes() {
        paparazzi.sparkSnapshotNightMode {
            SparkTheme {
                Meter.CircularSmall(value = 50f, intent = MeterIntent.Main)
                Meter.Circular(
                    value = 50f,
                    content = CircularMeterContent.Value("50%"),
                    intent = MeterIntent.Main,
                    size = CircleMeterSize.Medium,
                )
                Meter.Circular(
                    value = 50f,
                    content = CircularMeterContent.ValueLabel("50%", label = "Label"),
                    intent = MeterIntent.Main,
                    size = CircleMeterSize.Large,
                )
                Meter.Circular(
                    value = 50f,
                    content = CircularMeterContent.ValueLabel("50%", label = "Label"),
                    intent = MeterIntent.Main,
                    size = CircleMeterSize.XLarge,
                )
            }
        }
    }

    @Test
    fun allIntents() {
        paparazzi.sparkSnapshotNightMode {
            SparkTheme {
                MeterIntent.entries.forEach { intent ->
                    Meter.Circular(
                        value = 70f,
                        content = CircularMeterContent.Value("70%"),
                        intent = intent,
                        size = CircleMeterSize.Medium,
                    )
                }
            }
        }
    }
}

internal class MeterDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun circular() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.ValueLabel("70%", label = "Label"),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun circularSmall() = paparazzi.sparkDocSnapshot {
        Meter.CircularSmall(value = 70f, intent = MeterIntent.Support)
    }
}
