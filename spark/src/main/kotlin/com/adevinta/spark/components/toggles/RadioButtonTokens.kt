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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Component tokens for radio button components. Centralises token resolution so that radio button
 * composables read from a single source of truth instead of inlining the lookup at every call site.
 */
public object RadioButtonTokens {

    /**
     * Component tokens for [RadioButton.Card].
     */
    public object Card {

        /**
         * The container shape of the card.
         */
        public val shape: Shape
            @Composable
            @ReadOnlyComposable
            get() = SelectionCardTokens.shape

        /**
         * The space between the border of the card and its toggle and content.
         */
        public val contentPadding: PaddingValues = SelectionCardTokens.contentPadding

        /**
         * The space between the toggle at the start and the content in the horizontal layout.
         */
        public val horizontalStartSpacing: Dp = SelectionCardTokens.horizontalStartSpacing

        /**
         * The space between the toggle on top and the content in the vertical layout.
         */
        public val verticalTopSpacing: Dp = SelectionCardTokens.verticalTopSpacing

        /**
         * The border width of a card that is not selected.
         */
        public val borderWidth: Dp = SelectionCardTokens.borderWidth

        /**
         * The border width of a selected card.
         */
        public val selectedBorderWidth: Dp = SelectionCardTokens.selectedBorderWidth

        /**
         * The alpha that dims a disabled card as a whole.
         */
        public val disabledAlpha: Float
            @Composable
            @ReadOnlyComposable
            get() = SelectionCardTokens.disabledAlpha

        internal val style: SelectionCardStyle
            @Composable
            @ReadOnlyComposable
            get() = SelectionCardStyle(
                shape = shape,
                contentPadding = contentPadding,
                horizontalStartSpacing = horizontalStartSpacing,
                verticalTopSpacing = verticalTopSpacing,
                borderWidth = borderWidth,
                selectedBorderWidth = selectedBorderWidth,
                disabledAlpha = disabledAlpha,
            )
    }
}
