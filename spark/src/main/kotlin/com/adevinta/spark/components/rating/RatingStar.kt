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
import androidx.annotation.IntRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.rating.RatingStarState.Empty
import com.adevinta.spark.components.rating.RatingStarState.Full
import com.adevinta.spark.components.rating.RatingStarState.Half
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.StarFill
import com.adevinta.spark.icons.StarOutline
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tokens.dim5

/**
 * RatingStar is the atomic element of rating components
 * @param modifier to be applied
 * @param enabled whether the star should be enabled or disabled
 * @param modifier to be applied
 * @param size of the star, can be any size but preferably, use [RatingDefault.StarSize] or [RatingDefault.SmallStarSize].
 * @param state of the star,
 * can be [RatingStarState.Full], [RatingStarState.Empty] or [RatingStarState.Half]
 * or RatingStarState(1) or RatingStarState(0.5) or RatingStarState(0.0)
 */
@Composable
public fun RatingStar(
    modifier: Modifier = Modifier,
    size: Dp = RatingDefault.SmallStarSize,
    state: RatingStarState = RatingStarState(1),
    enabled: Boolean = true,
) {
    val filledStarColor by animateColorAsState(
        targetValue = if (enabled) SparkTheme.colors.mainVariant else SparkTheme.colors.mainVariant.dim3,
        label = "filled star color",
    )

    val outlinedStarColor by animateColorAsState(
        targetValue = if (enabled) SparkTheme.colors.onSurface.dim3 else SparkTheme.colors.onSurface.dim5,
        label = "outlined star color",
    )

    val icon = if (state == Full) LeboncoinIcons.StarFill else LeboncoinIcons.StarOutline

    when (state) {
        Full,
        -> {
            Icon(
                modifier = modifier.size(size),
                sparkIcon = icon,
                tint = filledStarColor,
                contentDescription = null,
            )
        }

        Empty,
        -> {
            Icon(
                modifier = modifier.size(size),
                sparkIcon = icon,
                tint = outlinedStarColor,
                contentDescription = null,
            )
        }

        Half -> {
            Box(modifier = modifier) {
                Icon(
                    modifier = Modifier
                        .size(size)
                        .clip(
                            FractionalRectangleShape(startFraction = 0f, endFraction = 0.5f),
                        ),
                    sparkIcon = LeboncoinIcons.StarFill,
                    tint = filledStarColor,
                    contentDescription = null,
                )
                Icon(
                    modifier = Modifier
                        .size(size)
                        .clip(
                            FractionalRectangleShape(startFraction = 0.5f, endFraction = 1f),
                        ),
                    sparkIcon = LeboncoinIcons.StarOutline,
                    tint = outlinedStarColor,
                    contentDescription = null,
                )
            }
        }
    }
}

/**
 * Enum that represents possible star states.
 */
public enum class RatingStarState {
    /**
     * State that means the star is fully drawn
     */
    Full,

    /**
     * State that means the star is empty
     */
    Empty,

    /**
     * State that means the star is half drawn
     */
    Half,
}

/**
 * Return corresponding RatingStarState based on a Boolean representation
 * @param isFilled whether the RatingStarState is full or empty
 */
public fun RatingStarState(isFilled: Boolean): RatingStarState = if (isFilled) Full else Empty

/**
 * Return corresponding RatingStarState based on a Int representation
 * @param starValue whether the RatingStarState is 1 (full) or 0 (empty)
 */
public fun RatingStarState(@IntRange(0, 1) starValue: Int): RatingStarState {
    check(starValue in 0..1) { "RatingStarState value must be between 0 and 1" }
    return if (starValue == 1) Full else Empty
}

/**
 * Return corresponding RatingStarState based on a Float representation
 * @param starValue whether the RatingStarState is full, half or empty
 */
public fun RatingStarState(@FloatRange(0.0, 1.0) starValue: Float): RatingStarState {
    check(starValue in 0.0f..1.0f) { "RatingStarState value must be between 0.0 and 1.0" }
    return when (starValue) {
        in 0.0f..0.25f -> Empty
        in 0.25f..0.75f -> Half
        else -> Full
    }
}

/**
 * Return corresponding RatingStarState based on a Double representation
 * @param starValue whether the RatingStarState is full, half or empty
 */
public fun RatingStarState(@FloatRange(0.0, 1.0) starValue: Double): RatingStarState {
    check(starValue in 0.0..1.0) { "RatingStarState value must be between 0.0 and 1.0" }
    return when (starValue) {
        in 0.0..0.25 -> Empty
        in 0.25..0.75 -> Half
        else -> Full
    }
}

public object RatingDefault {
    public val StarSize: Dp = 16.dp
    public val SmallStarSize: Dp = 12.dp
    public val LabelSide: RatingLabelSide = RatingLabelSide.End
}

@Composable
@Preview
private fun RatingStarPreview() {
    PreviewTheme {
        RatingStar(enabled = true, state = RatingStarState(1))
        RatingStar(enabled = false, state = RatingStarState(0.1))
        RatingStar(enabled = false, state = RatingStarState(0.3))
        RatingStar(enabled = true, state = RatingStarState(0.6))
        RatingStar(enabled = false, state = RatingStarState(0.8))
    }
}
