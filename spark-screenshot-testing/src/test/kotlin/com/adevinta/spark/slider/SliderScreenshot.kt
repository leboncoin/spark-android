/*
 * Copyright (c) 2023 Adevinta
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
package com.adevinta.spark.slider

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.slider.RangeSlider
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class SliderScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun testSliders() {
        paparazzi.sparkSnapshot {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = spacedBy(16.dp),
            ) {
                Row {
                    Text(text = "Square Shape")
                    Sliders(
                        intent = SliderIntent.Accent,
                        rounded = false,
                    )
                }
                Row {
                    Text(text = "Rounded Shape")
                    Sliders(
                        intent = SliderIntent.Support,
                        rounded = true,
                    )
                }
                Row {
                    Text(text = "Disabled")
                    Sliders(
                        intent = SliderIntent.Support,
                        enabled = false,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun testRangeSliders() {
        paparazzi.sparkSnapshot {
            FlowColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = spacedBy(16.dp),
            ) {
                Row {
                    Text(text = "Square Shape")
                    RangeSliders(
                        intent = SliderIntent.Support,
                        rounded = false,
                    )
                }
                Row {
                    Text(text = "Rounded Shape")
                    RangeSliders(
                        intent = SliderIntent.Error,
                        rounded = true,
                    )
                }
                Row {
                    Text(text = "Disabled")
                    RangeSliders(
                        intent = SliderIntent.Error,
                        enabled = false,
                        rounded = true,
                    )
                }
            }
        }
    }

    @SuppressLint("ComposableNaming")
    @OptIn(ExperimentalSparkApi::class, ExperimentalLayoutApi::class)
    @Composable
    private fun Sliders(
        intent: SliderIntent,
        enabled: Boolean = true,
        rounded: Boolean = true,
    ) {
        FlowColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Slider(
                value = 0.25f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
                valueRange = 0f..1f,
            )
            Slider(
                value = 0.5f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
                valueRange = 0f..1f,
            )
            Slider(
                value = 0.75f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
                valueRange = 0f..1f,
            )
            Slider(
                value = 1f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
                valueRange = 0f..1f,
            )
        }
    }

    @OptIn(ExperimentalSparkApi::class)
    @Composable
    private fun RangeSliders(
        intent: SliderIntent,
        enabled: Boolean = true,
        rounded: Boolean = true,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RangeSlider(
                value = 0.3f..0.6f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
            )
            RangeSlider(
                value = 0.2f..0.7f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
            )
            RangeSlider(
                value = 0.1f..0.8f,
                intent = intent,
                onValueChange = { },
                enabled = enabled,
                rounded = rounded,
            )
        }
    }
}
