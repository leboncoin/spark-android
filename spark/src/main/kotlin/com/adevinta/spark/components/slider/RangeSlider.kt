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
package com.adevinta.spark.components.slider

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.RangeSlider as MaterialRangeSlider

@OptIn(ExperimentalMaterial3Api::class)
@InternalSparkApi
@Composable
internal fun SparkRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    intent: SliderIntent,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    rounded: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    startInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    endInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    startHandle: @Composable (RangeSliderState) -> Unit = remember(startInteractionSource, enabled, intent) {
        {
            Handle(
                interactionSource = startInteractionSource,
                intent = intent,
                enabled = enabled,
            )
        }
    },
    endHandle: @Composable (RangeSliderState) -> Unit = remember(endInteractionSource, enabled, intent) {
        {
            Handle(
                interactionSource = endInteractionSource,
                intent = intent,
                enabled = enabled,
            )
        }
    },
    track: @Composable (RangeSliderState) -> Unit = remember(enabled, rounded, intent) {
        { rangeSliderState ->
            Track(
                intent = intent,
                enabled = enabled,
                rounded = rounded,
                rangeSliderState = rangeSliderState,
            )
        }
    },
    steps: Int = 0,
) {
    MaterialRangeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.sparkUsageOverlay(),
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        startThumb = startHandle,
        endThumb = endHandle,
        startInteractionSource = startInteractionSource,
        endInteractionSource = endInteractionSource,
        track = track,
    )
}

/**
 * Spark Range slider.
 * Range Sliders expand upon Slider using the same concepts but allow the user to select 2 values.
 * The two values are still bounded by the value range but they also cannot cross each other.
 * Use continuous Range Sliders to allow users to make meaningful selections that don’t require a specific values:
 * @param value current values of the RangeSlider. If either value is outside of valueRange provided, it will be coerced to this range.
 * @param onValueChange lambda in which values should be updated
 * @param modifier modifiers for the Range Slider layout
 * @param intent The intent color for the Slider.
 * @param enabled whether or not component is enabled and can we interacted with or not
 * @param valueRange range of values that Range Slider values can take. Passed value will be coerced to this range
 * @param steps if greater than 0, specifies the amounts of discrete values, evenly distributed between across the whole value range.
 * If 0, range slider will behave as a continuous slider and allow to choose any value from the range specified. Must not be negative.
 * @param onValueChangeFinished lambda to be invoked when value change has ended.
 * This callback shouldn't be used to update the range slider values (use onValueChange for that),
 * but rather to know when the user has completed selecting a new value by ending a drag or a click.
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
public fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    intent: SliderIntent = SliderIntent.Support,
    enabled: Boolean = true,
    rounded: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    SparkRangeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        rounded = rounded,
        valueRange = valueRange,
        steps = steps,
        intent = intent,
        onValueChangeFinished = onValueChangeFinished,
    )
}

@Preview(
    group = "RangeSlider",
    name = "RangeSlider",
)
@Composable
private fun RangeSliderPreview() {
    var progress by remember { mutableStateOf(0.1f..0.5f) }

    PreviewTheme {
        RangeSlider(
            value = progress,
            onValueChange = { progress = it },
            enabled = true,
            steps = 3,
        )

        RangeSlider(
            value = 0.1f..0.5f,
            onValueChange = {},
            enabled = false,
        )
    }
}
