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
package com.adevinta.spark.components.tags

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.LocalSparkFeatureFlag
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.highlight

/**
 * Component tokens for tag components. Centralises all flag-driven token resolution so that
 * tag composables read from a single source of truth instead of inlining the flag check at
 * every call site. When the rebranding feature flag is eventually removed, only this file changes.
 *
 * Note: tag shapes are inverted relative to buttons — the legacy shape is full/pill and the
 * rebranded shape is extraSmall (nearly square corners).
 */
public object TagTokens {

    /**
     * The resolved container shape for tags.
     */
    public val shape: Shape
        @Composable get() = if (LocalSparkFeatureFlag.current.useRebrandedShapes) {
            SparkTheme.shapes.extraSmall
        } else {
            SparkTheme.shapes.full
        }

    /**
     * The outlined tag's border size
     */
    public val OutlinedBorderSize: Dp = 1.dp

    /**
     * The size of a tag's leading icon
     */
    public val LeadingIconSize: Dp = 12.dp

    /**
     * The size of a tag's leading icon
     */
    public val ContentSpacing: Dp = 4.dp

    public val contentTypo: TextStyle
        @Composable
        get() = SparkTheme.typography.caption.highlight

    public val contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp)

    @ExperimentalSparkApi
    public object Ai {
        public val borderColor: Color
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.colors.ai
        public val containerColor: Color
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.colors.aiContainer
        public val contentColor: Color
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.colors.onAiContainer
        public val iconTint: Color
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.colors.ai
    }
}
