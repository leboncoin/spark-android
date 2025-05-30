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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tokens.dim4
import com.adevinta.spark.tokens.ripple
import com.adevinta.spark.tokens.transparent
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.Slider as MaterialSlider

@OptIn(ExperimentalMaterial3Api::class)
@InternalSparkApi
@Composable
internal fun SparkSlider(
    value: Float,
    intent: SliderIntent,
    steps: Int,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    rounded: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    allowSemantics: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    handle: @Composable (SliderState) -> Unit = remember(interactionSource, enabled, rounded, intent, steps) {
        {
            Handle(
                interactionSource = interactionSource,
                intent = intent,
                enabled = enabled,
            )
        }
    },
    track: @Composable (SliderState) -> Unit = remember(interactionSource, enabled, rounded, intent, steps) {
        {
            Track(
                intent = intent,
                enabled = enabled,
                rounded = rounded,
                sliderState = it,
            )
        }
    },

) {
    MaterialSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
//            .ifTrue(allowSemantics) {
//                stepperSemantics(
//                    value = coerced,
//                    onValueChange = ::setValue,
//                    range = range,
//                    step = steps,
//                    suffix = suffix,
//                    enabled = enabled,
//                )
//            }
            .sparkUsageOverlay(),
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        thumb = handle,
        track = track,
    )
}

private val HandleDefaultOutline = 1.dp
private val DefaultHandleSizeInnerWidth = 24.dp
private val DefaultHandleSizeWidth = 32.dp
private val DefaultHandleSize = DpSize(DefaultHandleSizeWidth, DefaultHandleSizeWidth)

@Composable
internal fun Handle(
    intent: SliderIntent = remember { SliderIntent.Basic },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    handleSize: DpSize = DefaultHandleSize,
) {
    val interactions = remember { mutableStateListOf<Interaction>() }
    val intentColor = intent.colors()
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }

    val handleColor = if (enabled) intentColor.color else intentColor.color.dim3
    val handleColorBg by animateColorAsState(
        label = "handleColorBg",
        targetValue = if (interactions.isNotEmpty()) {
            intentColor.containerColor
        } else {
            intentColor.containerColor.transparent
        },
    )
    val handleColorBorder by animateColorAsState(
        label = "handleBorder",
        targetValue = if (interactions.isNotEmpty()) {
            intentColor.color
        } else {
            intentColor.color.transparent
        },
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(DefaultHandleSizeWidth)
            .indication(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = false,
                    radius = 24.dp,
                ),
            )
            .background(handleColorBg, CircleShape)
            .border(
                width = HandleDefaultOutline,
                color = handleColorBorder,
                shape = CircleShape,
            ),
    ) {
        Box(
            modifier = Modifier
                .size(DefaultHandleSizeInnerWidth)
                .background(SparkTheme.colors.basicContainer, CircleShape),
        )
        Box(
            modifier = Modifier
                .size(DefaultHandleSizeInnerWidth)
                .background(handleColor, CircleShape)
                .hoverable(interactionSource = interactionSource),
        )
        Box(
            Modifier
                .size(handleSize)
                .hoverable(interactionSource = interactionSource),

        )
    }
}

private fun stepsToTickFractions(steps: Int): FloatArray = if (steps == 0) {
    floatArrayOf()
} else {
    FloatArray(steps + 2) {
        it.toFloat() / (steps + 1)
    }
}

internal val TrackHeight = 4.dp
private val TickSize = 2.dp

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
internal fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

internal fun getCoercedValueAsFraction(valueRange: ClosedFloatingPointRange<Float>, value: Float): Float = calcFraction(
    valueRange.start,
    valueRange.endInclusive,
    value.coerceIn(valueRange.start, valueRange.endInclusive),
)

internal fun getCoercedActiveRangeStartAsFraction(
    valueRange: ClosedFloatingPointRange<Float>,
    activeRangeStart: Float,
): Float = calcFraction(
    valueRange.start,
    valueRange.endInclusive,
    activeRangeStart,
)

internal fun getCoercedActiveRangeEndAsFraction(
    valueRange: ClosedFloatingPointRange<Float>,
    activeRangeEnd: Float,
): Float = calcFraction(
    valueRange.start,
    valueRange.endInclusive,
    activeRangeEnd,
)

/**
 * The Default track for [Slider] & [RangeSlider]
 *
 * @param sliderState [SliderState] which is used to obtain the current active track.
 * @param modifier the [Modifier] to be applied to the track.
 * @param intent The intent color for the Track.
 * @param enabled controls the enabled state of this slider. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to
 * accessibility services.
 */
@Composable
@ExperimentalMaterial3Api
internal fun Track(
    sliderState: SliderState,
    modifier: Modifier = Modifier,
    intent: SliderIntent = SliderIntent.Basic,
    enabled: Boolean = true,
    rounded: Boolean = false,
) {
    val indicatorColor = rememberUpdatedState(
        if (enabled) {
            intent.colors().color
        } else {
            intent.colors().color.dim3
        },
    ).value

    val trackColor = SparkTheme.colors.onBackground.dim4

    val tickFractions = stepsToTickFractions(sliderState.steps)

    Canvas(
        modifier
            .fillMaxWidth()
            .height(TrackHeight),
    ) {
        drawTrack(
            tickFractions,
            0f,
            getCoercedValueAsFraction(sliderState.valueRange, sliderState.value),
            trackColor,
            indicatorColor,
            Color.Transparent,
            Color.Transparent,
            rounded = rounded,
        )
    }
}

private fun DrawScope.drawTrack(
    tickFractions: FloatArray,
    activeRangeStart: Float,
    activeRangeEnd: Float,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    inactiveTickColor: Color,
    activeTickColor: Color,
    rounded: Boolean,
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val tickSize = TickSize.toPx()
    val trackStrokeWidth = TrackHeight.toPx()
    drawLine(
        inactiveTrackColor,
        sliderStart,
        sliderEnd,
        trackStrokeWidth,
        if (rounded) StrokeCap.Round else StrokeCap.Square,
    )
    val sliderValueEnd = Offset(
        sliderStart.x + (sliderEnd.x - sliderStart.x) * activeRangeEnd,
        center.y,
    )

    val sliderValueStart = Offset(
        sliderStart.x + (sliderEnd.x - sliderStart.x) * activeRangeStart,
        center.y,
    )

    drawLine(
        activeTrackColor,
        sliderValueStart,
        sliderValueEnd,
        trackStrokeWidth,
        if (rounded) StrokeCap.Round else StrokeCap.Square,
    )

    for (tick in tickFractions) {
        val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
        drawCircle(
            color = if (outsideFraction) inactiveTickColor else activeTickColor,
            center = Offset(lerp(sliderStart, sliderEnd, tick).x, center.y),
            radius = tickSize / 2f,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Track(
    rangeSliderState: RangeSliderState,
    modifier: Modifier = Modifier,
    intent: SliderIntent = SliderIntent.Basic,
    enabled: Boolean = true,
    rounded: Boolean = false,
) {
    val indicatorColor = rememberUpdatedState(
        if (enabled) {
            intent.colors().color
        } else {
            intent.colors().color.dim3
        },
    ).value

    val trackColor = SparkTheme.colors.onBackground.dim4

    val tickFractions = stepsToTickFractions(rangeSliderState.steps)

    Canvas(
        modifier
            .fillMaxWidth()
            .height(TrackHeight),
    ) {
        drawTrack(
            tickFractions,
            getCoercedActiveRangeStartAsFraction(rangeSliderState.valueRange, rangeSliderState.activeRangeStart),
            getCoercedActiveRangeEndAsFraction(rangeSliderState.valueRange, rangeSliderState.activeRangeEnd),
            trackColor,
            indicatorColor,
            Color.Transparent,
            Color.Transparent,
            rounded = rounded,
        )
    }
}

/**
 * Spark slider.
 * Sliders allow users to make selections from a range of values.
 * Sliders reflect a range of values along a bar, from which users may select a single value.
 * They are ideal for adjusting settings such as volume, brightness, or applying image filters.
 *
 * @param value current value of the slider. If outside of valueRange provided,
 * value will be coerced to this range.
 * @param onValueChange callback in which value should be updated
 * @param modifier the Modifier to be applied to this slider
 * @param valueRange range of values that this slider can take. The passed value will be coerced to this range.
 * @param onValueChangeFinished called when value change has ended. This should not be used to update the slider value
 * (use onValueChange instead), but rather to know when the user has completed selecting a new value by ending a drag or a click.
 * @param enabled controls the enabled state of this slider. When false, this component will not respond to user input,
 * and it will appear visually disabled and disabled to accessibility services.
 * @param steps if greater than 0, specifies the amount of discrete allowable values, evenly distributed across the whole value range.
 * If 0, the slider will behave continuously and allow any value from the range specified. Must not be negative.
 * @param intent The intent color for the Slider.
 * @param interactionSource the MutableInteractionSource representing the stream of Interactions for this slider.
 * You can create and pass in your own remembered instance to observe Interactions
 * and customize the appearance / behavior of this slider in different states.
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
public fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    enabled: Boolean = true,
    rounded: Boolean = false,
    steps: Int = 0,
    intent: SliderIntent = SliderIntent.Basic,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        intent = intent,
        enabled = enabled,
        rounded = rounded,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,
        steps = steps,
        interactionSource = interactionSource,
    )
}

@Preview(
    group = "Slider",
    name = "Slider",
)
@Composable
private fun SliderPreview() {
    var progress by remember { mutableFloatStateOf(0.75f) }

    PreviewTheme {
        Slider(
            value = progress,
            intent = SliderIntent.Error,
            onValueChange = { progress = it },
            enabled = true,
            valueRange = 0f..1f,
            steps = 4,
        )
        Slider(
            value = 0.5f,
            intent = SliderIntent.Success,
            onValueChange = {},
            enabled = false,
        )
    }
}
