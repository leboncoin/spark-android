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
package com.adevinta.spark.components.rating

import androidx.annotation.IntRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.popover.PlainTooltip
import com.adevinta.spark.components.popover.TooltipBox
import com.adevinta.spark.components.rating.RatingDefaults.InputFloatRange
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.ifTrue
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlin.math.roundToInt

/**
 * A rating input component that allows the user to select a rating from 0 to 5.
 *
 * @param value The current rating value [Int].
 * @param onRatingChanged The callback that is called when the rating value changes.
 * @param modifier The modifier to be applied to the layout.
 * @param enabled Whether the rating input is enabled.
 * @param stateDescription Lambda to generate the accessibility state description from the current value.
 *                        Defaults to "$value stars".
 * @param allowSemantics Whether to enable semantic properties for accessibility. Set to false when
 *                      using the component as part of a larger component that handles its own semantics
 *                      to avoid duplicate announcements.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun RatingInput(
    @IntRange(from = 0, to = 5) value: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    stateDescription: (Int) -> String = { "$it stars" },
    allowSemantics: Boolean = true,
    testTag: String? = null,
) {
    if (value !in RatingDefaults.InputIntRange) return

    val haptics = LocalHapticFeedback.current
    var ratingContainerWidth = 0f
    var lastDragRating = value

    Row(
        modifier = modifier
            .sparkUsageOverlay()
            .ifTrue(allowSemantics) {
                ratingSemantics(
                    value = value,
                    onValueChange = onRatingChanged,
                    enabled = enabled,
                    stateDescription = stateDescription,
                )
            }
            .focusable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
            )
            .onSizeChanged { size ->
                ratingContainerWidth = size.width.toFloat()
            }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        lastDragRating = calculateRatingFromPosition(offset.x, ratingContainerWidth)
                    },
                    onDragEnd = {},
                    onDragCancel = {},
                    onHorizontalDrag = { change, _ ->
                        val newRating = calculateRatingFromPosition(change.position.x, ratingContainerWidth)
                        if (newRating != lastDragRating) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onRatingChanged(newRating)
                            lastDragRating = newRating
                        }
                    },
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var remainingValue = value
        repeat(5) { starRatingIndex ->
            val starRating = when {
                remainingValue == 0 -> 0
                else -> {
                    remainingValue -= 1
                    1
                }
            }

            val tooltipState = rememberTooltipState()
            val starRatingValue = starRatingIndex + 1
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(
                            text = "$starRatingValue",
                            // hardcoded value inside material we need to be able to center the tooltip
                            modifier = Modifier.width(24.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                state = tooltipState,
            ) {
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(SparkTheme.shapes.full)
                        .size(RatingDefaults.TouchTargetSize)
                        .clickable(
                            onClick = {
                                onRatingChanged(starRatingValue)
                            },
                            enabled = enabled,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                bounded = false,
                                radius = RatingDefaults.TouchTargetSize / 2,
                            ),
                        )
                        .ifNotNull(testTag) {
                            testTag("$it Star $starRatingIndex")
                        }
                        .padding(4.dp),
                ) {
                    RatingStar(
                        enabled = enabled,
                        state = RatingStarState(starRating),
                        size = RatingDefaults.StarSize,
                    )
                }
            }
        }
    }
}

/**
 * Adds semantics to a rating input component to make it behave like a slider for accessibility purposes.
 *
 * @param value The current rating value [Int] between 0 and 5.
 * @param onValueChange The callback that is called when the rating is changed.
 * @param enabled Whether the rating input is enabled.
 * @param stateDescription Lambda to generate the state description string from the current value.
 */
// TODO-scott.rayapoulle.ext (17-09-2025): Move to spark a11y lib once it's initiated
private fun Modifier.ratingSemantics(
    value: Int,
    onValueChange: (Int) -> Unit,
    enabled: Boolean,
    stateDescription: (Int) -> String = { "$it" },
): Modifier = semantics(mergeDescendants = true) {
    setProgress { targetValue ->
        val newValue = targetValue
            .roundToInt()
            .coerceIn(RatingDefaults.InputIntRange)
        if (newValue != value) {
            onValueChange(newValue)
            true
        } else {
            false
        }
    }

    this.stateDescription = stateDescription(value)
    if (!enabled) disabled()
}
    .semantics {
        progressBarRangeInfo = ProgressBarRangeInfo(
            current = value.toFloat(),
            range = InputFloatRange,
            steps = RatingDefaults.Steps,
        )
    }
    .onKeyEvent {
        if (!enabled) return@onKeyEvent false

        val isRightKey = it.key == Key.DirectionRight
        val isLeftKey = it.key == Key.DirectionLeft
        val isUpKey = it.key == Key.DirectionUp
        val isDownKey = it.key == Key.DirectionDown
        val isShiftOnlyPressed = it.isShiftPressed && !it.isCtrlPressed && !it.isAltPressed && !it.isMetaPressed

        if (it.type == KeyEventType.KeyDown && isShiftOnlyPressed) {
            when {
                isRightKey || isUpKey -> onValueChange((value + 1).coerceIn(RatingDefaults.InputIntRange))
                isLeftKey || isDownKey -> onValueChange((value - 1).coerceIn(RatingDefaults.InputIntRange))
                else -> return@onKeyEvent false
            }
            true
        } else {
            false
        }
    }

/**
 * Calculates the rating value based on the horizontal drag position.
 *
 * @param x The x-coordinate of the drag position
 * @param width The total width of the rating component
 * @return The calculated rating value between 0 and 5
 */
private fun calculateRatingFromPosition(x: Float, width: Float): Int {
    if (width <= 0f) return 0
    val position = x.coerceIn(0f, width)
    val normalized = (position / width) * 5
    return normalized.roundToInt().coerceIn(0, 5)
}

public object RatingDefaults {

    public const val InputMin: Int = 0
    public const val InputMax: Int = 5
    public val InputIntRange: ClosedRange<Int> = InputMin..InputMax
    public val InputFloatRange: ClosedFloatingPointRange<Float> = InputMin.toFloat()..InputMax.toFloat()
    public const val Steps: Int = InputMax - InputMin

    /**
     * The size of each star in the rating.
     */
    public val StarSize: Dp = 40.dp

    /**
     * The size of the clickable area for each star.
     */
    public val TouchTargetSize: Dp = 48.dp
}

@Composable
@Preview(
    group = "Ratings",
    name = "RatingDisplay",
)
internal fun RatingInputPreview() {
    PreviewTheme {
        var rating by remember {
            mutableIntStateOf(2)
        }
        RatingInput(value = rating, onRatingChanged = { rating = it })
        RatingInput(value = rating, enabled = false, onRatingChanged = { rating = it })
        RatingInput(value = 0, onRatingChanged = {})
        RatingInput(value = 1, onRatingChanged = {})
        RatingInput(value = 2, onRatingChanged = {})
        RatingInput(value = 3, onRatingChanged = {})
        RatingInput(value = 3, onRatingChanged = {}, enabled = false)
        RatingInput(value = 4, onRatingChanged = {})
        RatingInput(value = 5, onRatingChanged = {})
    }
}
