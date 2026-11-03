/*
 * Copyright (c) 2025 Adevinta
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.slider.RangeSlider
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class SliderDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun slider() = paparazzi.sparkDocSnapshot {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Slider(
                value = 0.25f,
                intent = SliderIntent.Support,
                onValueChange = {},
            )
            Slider(
                value = 0.5f,
                intent = SliderIntent.Support,
                onValueChange = {},
            )
            Slider(
                value = 0.75f,
                intent = SliderIntent.Support,
                onValueChange = {},
            )
        }
    }

    @Test
    fun rangeSlider() = paparazzi.sparkDocSnapshot {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            RangeSlider(
                value = 0.3f..0.6f,
                onValueChange = {},
            )
            RangeSlider(
                value = 0.2f..0.7f,
                onValueChange = {},
            )
        }
    }
}
