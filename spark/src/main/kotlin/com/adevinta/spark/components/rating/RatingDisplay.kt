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

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * A display for a rating value.
 *
 * @param value The rating value to display. Must be between 0 and 5.
 * @param modifier The modifier to be applied to the layout.
 * @param size The size of the stars. Default is [RatingDefault.SmallStarSize]

 */
@Composable
public fun RatingDisplay(
    @FloatRange(from = 0.0, to = 5.0)
    value: Float,
    modifier: Modifier = Modifier,
    size: Dp = RatingDefault.SmallStarSize,
) {
    if (value !in .5f..5f) return
    Row(
        modifier = modifier.sparkUsageOverlay(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        var remainingValue = remember(value) {
            value
        }
        repeat(5) {
            val starRating = when {
                remainingValue == 0f -> 0f
                remainingValue >= 1 -> {
                    remainingValue -= 1
                    1f
                }

                else -> {
                    val fraction = remainingValue / 1
                    remainingValue = 0f
                    fraction
                }
            }

            RatingStar(
                enabled = true,
                modifier = Modifier,
                state = RatingStarState(starRating),
                size = size,
            )
        }
    }
}

@Composable
@Preview(
    group = "Ratings",
    name = "RatingDisplay",
)
internal fun RatingDisplayPreview() {
    PreviewTheme {
        RatingDisplay(value = 0.5f)
        RatingDisplay(value = 1f)
        RatingDisplay(value = 1.5f)
        RatingDisplay(value = 2.1f)
        RatingDisplay(value = 2.5f)
        RatingDisplay(value = 3.2f)
        RatingDisplay(value = 3.666f)
        RatingDisplay(value = 4.1f)
        RatingDisplay(value = 4.26f)
        RatingDisplay(value = 5f)
    }
}
