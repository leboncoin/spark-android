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
package com.adevinta.spark.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.CardDefaults.OutlinedCardTokens.DisabledContainerElevation
import com.adevinta.spark.components.card.CardDefaults.OutlinedCardTokens.DisabledOutlineOpacity
import com.adevinta.spark.components.card.CardDefaults.OutlinedCardTokens.OutlineWidth
import com.adevinta.spark.tokens.DisabledAlpha
import com.adevinta.spark.tokens.ElevationTokens
import com.adevinta.spark.tokens.contentColorFor
import com.adevinta.spark.tokens.surfaceColorAtElevation

public object CardDefaults {
    @Composable
    public fun cardColors(
        containerColor: Color = SparkTheme.colors.backgroundVariant,
        disabledContainerColor: Color = SparkTheme.colors.neutralContainer
            .copy(alpha = DisabledAlpha)
            .compositeOver(SparkTheme.colors.surface),
        disabledContentColor: Color = SparkTheme.colors.contentColorFor(containerColor).copy(DisabledAlpha),
    ): CardColors = CardColors(
        containerColor = containerColor,
        contentColor = SparkTheme.colors.contentColorFor(containerColor),
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    public fun elevatedCardColors(
        containerColor: Color = SparkTheme.colors.backgroundVariant,
        contentColor: Color = SparkTheme.colors.contentColorFor(containerColor),
        disabledContainerColor: Color =
            SparkTheme.colors.surface
                .copy(alpha = DisabledAlpha)
                .compositeOver(
                    SparkTheme.colors.surfaceColorAtElevation(ElevationTokens.Level1),
                ),
        disabledContentColor: Color = contentColor.copy(DisabledAlpha),
    ): CardColors =
        CardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )

    @Composable
    public fun outlinedCardColors(
        containerColor: Color = SparkTheme.colors.backgroundVariant,
        contentColor: Color = SparkTheme.colors.contentColorFor(containerColor),
        disabledContainerColor: Color = containerColor,
        disabledContentColor: Color = contentColor.copy(DisabledAlpha),
    ): CardColors =
        CardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        )

    @Composable
    public fun outlinedCardBorder(enabled: Boolean = true): BorderStroke {
        val color = if (enabled) {
            SparkTheme.colors.outline
        } else {
            SparkTheme.colors.outline
                .copy(alpha = DisabledOutlineOpacity)
                .compositeOver(
                    SparkTheme.colors.surfaceColorAtElevation(
                        DisabledContainerElevation,
                    ),
                )
        }
        return remember(color) { BorderStroke(OutlineWidth, color) }
    }

    /**
     * Creates a [CardElevation] that will animate between the provided values according to the
     * Material specification for a [Card].
     *
     * @param defaultElevation the elevation used when the [Card] is has no other [Interaction]s.
     * @param pressedElevation the elevation used when the [Card] is pressed.
     * @param focusedElevation the elevation used when the [Card] is focused.
     * @param hoveredElevation the elevation used when the [Card] is hovered.
     * @param draggedElevation the elevation used when the [Card] is dragged.
     */
    @Composable
    public fun cardElevation(
        defaultElevation: Dp = ElevationTokens.Level0,
        pressedElevation: Dp = ElevationTokens.Level0,
        focusedElevation: Dp = ElevationTokens.Level0,
        hoveredElevation: Dp = ElevationTokens.Level1,
        draggedElevation: Dp = ElevationTokens.Level3,
        disabledElevation: Dp = ElevationTokens.Level0,
    ): CardElevation = CardElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        draggedElevation = draggedElevation,
        disabledElevation = disabledElevation,
    )

    /**
     * Creates a [CardElevation] that will animate between the provided values according to the
     * Material specification for an [ElevatedCard].
     *
     * @param defaultElevation the elevation used when the [ElevatedCard] is has no other
     * [Interaction]s.
     * @param pressedElevation the elevation used when the [ElevatedCard] is pressed.
     * @param focusedElevation the elevation used when the [ElevatedCard] is focused.
     * @param hoveredElevation the elevation used when the [ElevatedCard] is hovered.
     * @param draggedElevation the elevation used when the [ElevatedCard] is dragged.
     */
    @Composable
    public fun elevatedCardElevation(
        defaultElevation: Dp = ElevationTokens.Level2,
        pressedElevation: Dp = ElevationTokens.Level3,
        focusedElevation: Dp = ElevationTokens.Level3,
        hoveredElevation: Dp = ElevationTokens.Level3,
        draggedElevation: Dp = ElevationTokens.Level4,
        disabledElevation: Dp = ElevationTokens.Level2,
    ): CardElevation = CardElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        draggedElevation = draggedElevation,
        disabledElevation = disabledElevation,
    )

    /**
     * Creates a [CardElevation] that will animate between the provided values according to the
     * Material specification for an [OutlinedCard].
     *
     * @param defaultElevation the elevation used when the [OutlinedCard] is has no other
     * [Interaction]s.
     * @param pressedElevation the elevation used when the [OutlinedCard] is pressed.
     * @param focusedElevation the elevation used when the [OutlinedCard] is focused.
     * @param hoveredElevation the elevation used when the [OutlinedCard] is hovered.
     * @param draggedElevation the elevation used when the [OutlinedCard] is dragged.
     */
    @Composable
    public fun outlinedCardElevation(
        defaultElevation: Dp = ElevationTokens.Level0,
        pressedElevation: Dp = defaultElevation,
        focusedElevation: Dp = defaultElevation,
        hoveredElevation: Dp = defaultElevation,
        draggedElevation: Dp = ElevationTokens.Level3,
        disabledElevation: Dp = ElevationTokens.Level0,
    ): CardElevation = CardElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        draggedElevation = draggedElevation,
        disabledElevation = disabledElevation,
    )

    @Composable
    public fun paddingValues(hasHeading: Boolean): PaddingValues = if (hasHeading) {
        PaddingValues(
            top = 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
        )
    } else {
        PaddingValues(16.dp)
    }

    internal object OutlinedCardTokens {
        const val DisabledOutlineOpacity = 0.12f
        val DisabledContainerElevation = ElevationTokens.Level0
        val OutlineWidth = 1.0.dp
    }
}
