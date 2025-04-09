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
package com.adevinta.spark.catalog.examples.samples.slider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.slider.RangeSlider
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent

private const val SlidersExampleSourceUrl = "$SampleSourceUrl/SliderExamples.kt"

public val SlidersExamples: List<Example> = listOf(
    Example(
        id = "steps",
        name = R.string.slider_example_steps_title,
        description = R.string.slider_example_steps_description,
        sourceUrl = SlidersExampleSourceUrl,
    ) {

        var progress by remember { mutableFloatStateOf(0.75f) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            Slider(
                value = progress,
                intent = SliderIntent.Error,
                onValueChange = { progress = it },
                enabled = true,
                valueRange = 0f..1f,
                steps = 4,
            )
        }
    },
    Example(
        id = "no-steps",
        name = R.string.slider_example_no_steps_title,
        description = R.string.slider_example_no_steps_description,
        sourceUrl = SlidersExampleSourceUrl,
    ) {

        var progress by remember { mutableFloatStateOf(0.75f) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            Slider(
                value = progress,
                intent = SliderIntent.Basic,
                onValueChange = { progress = it },
                enabled = true,
                valueRange = 0f..1f,
            )
        }
    },
    Example(
        id = "range-steps",
        name = R.string.slider_example_range_steps_title,
        description = R.string.slider_example_range_steps_description,
        sourceUrl = SlidersExampleSourceUrl,
    ) {

        var rangeProgress by remember { mutableStateOf(0.1f..0.5f) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            RangeSlider(
                value = rangeProgress,
                intent = SliderIntent.Accent,
                onValueChange = { rangeProgress = it },
                enabled = true,
                valueRange = 0f..1f,
                steps = 4,
            )
        }
    },
    Example(
        id = "range-no-steps",
        name = R.string.slider_example_range_no_steps_title,
        description = R.string.slider_example_range_no_steps_description,
        sourceUrl = SlidersExampleSourceUrl,
    ) {

        var rangeProgress by remember { mutableStateOf(0.1f..0.5f) }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            RangeSlider(
                value = rangeProgress,
                intent = SliderIntent.Success,
                onValueChange = { rangeProgress = it },
                enabled = true,
                valueRange = 0f..1f,
            )
        }
    },
)
