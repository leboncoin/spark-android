/*
 * Copyright (c) 2026 Adevinta
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
package com.adevinta.spark.components.toggles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

/**
 * The values that the selection cards of checkboxes, radio buttons and switches share.
 * Each value is defined here once. [CheckboxTokens.Card], [RadioButtonTokens.Card] and
 * [SwitchTokens.Card] expose them.
 */
internal object SelectionCardTokens {

    val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.shapes.small

    val contentPadding: PaddingValues = PaddingValues(16.dp)

    val horizontalStartSpacing: Dp = 16.dp

    val verticalTopSpacing: Dp = 16.dp

    val borderWidth: Dp = 1.dp

    val selectedBorderWidth: Dp = 2.dp

    val disabledAlpha: Float
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.colors.dim3
}

/**
 * The resolved tokens that the selection card container draws with.
 * Each `Card` token object supplies its own instance.
 */
@Immutable
internal class SelectionCardStyle(
    val shape: Shape,
    val contentPadding: PaddingValues,
    val horizontalStartSpacing: Dp,
    val verticalTopSpacing: Dp,
    val borderWidth: Dp,
    val selectedBorderWidth: Dp,
    val disabledAlpha: Float,
) {

    /**
     * The border of the card. The error colour takes precedence over the selected colour.
     * The disabled state keeps the same border, since the whole card is dimmed once.
     *
     * The width and the colour animate when [selected] or [error] changes.
     * They use the default spring of `animate*AsState`, like the border of the file upload preview.
     * The first composition shows the final border with no animation.
     */
    @Composable
    fun border(selected: Boolean, error: Boolean): BorderStroke {
        val width by animateDpAsState(
            targetValue = if (selected) selectedBorderWidth else borderWidth,
            label = "selection card border width",
        )
        val color by animateColorAsState(
            targetValue = when {
                error -> SparkTheme.colors.error
                selected -> SparkTheme.colors.support
                else -> SparkTheme.colors.outline
            },
            label = "selection card border colour",
        )
        return remember(width, color) { BorderStroke(width, color) }
    }
}
