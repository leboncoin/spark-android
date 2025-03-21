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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.popover.PlainTooltip
import com.adevinta.spark.components.popover.TooltipBox
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * A rating input component that allows the user to select a rating from 0 to 5.
 *
 * @param value The current rating value [Int].
 * @param onRatingChanged The callback that is called when the rating is changed.
 * @param modifier The modifier to be applied to the layout.
 * @param enabled Whether the rating input is enabled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun RatingInput(
    @IntRange(from = 0, to = 5) value: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    if (value !in 0..5) return
    Row(
        modifier = modifier.sparkUsageOverlay(),
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
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text("$starRatingValue")
                    }
                },
                state = tooltipState,
            ) {
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(SparkTheme.shapes.full)
                        .size(48.dp)
                        .clickable(
                            onClick = {
                                onRatingChanged(starRatingValue)
                            },
                            enabled = enabled,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                bounded = false,
                                radius = 48.dp / 2,
                            ),
                        )
                        .padding(4.dp),
                ) {
                    RatingStar(
                        enabled = enabled,
                        state = RatingStarState(starRating),
                        size = 40.dp,
                    )
                }
            }
        }
    }
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
        RatingInput(value = 0, onRatingChanged = {})
        RatingInput(value = 1, onRatingChanged = {})
        RatingInput(value = 2, onRatingChanged = {})
        RatingInput(value = 3, onRatingChanged = {})
        RatingInput(value = 3, onRatingChanged = {}, enabled = false)
        RatingInput(value = 4, onRatingChanged = {})
        RatingInput(value = 5, onRatingChanged = {})
    }
}
