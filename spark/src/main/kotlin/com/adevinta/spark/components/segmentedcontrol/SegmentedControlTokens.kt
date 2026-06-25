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
package com.adevinta.spark.components.segmentedcontrol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.disabled
import com.adevinta.spark.tokens.highlight

/**
 * Default values and constants for [SegmentedControl].
 *
 * Callers rarely need to reference these directly; they are exposed so that custom
 * [SegmentedControl.Horizontal] and [SegmentedControl.Vertical] wrappers can reuse the same
 * layout limits.
 */
public object SegmentedControlTokens {

    /** Minimum number of segments accepted by [SegmentedControl.Horizontal]. */
    public const val MinSegments: Int = 2

    /** Minimum number of segments accepted by [SegmentedControl.Vertical]. */
    public const val MinVerticalSegments: Int = 4

    /** Maximum number of segments accepted by [SegmentedControl.Horizontal]. */
    public const val MaxHorizontalSegments: Int = 5

    /** Maximum number of segments accepted by [SegmentedControl.Vertical]. */
    public const val MaxVerticalSegments: Int = 8

    /** Width of the outer border drawn around the control track. */
    public val BorderWidth: Dp = 1.dp
    public val BorderColor: Color
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.colors.outline

    public val ContainerHorizontalShape: Shape
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.shapes.full

    public val ContainerVerticalShape: Shape
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.shapes.large
    public val IndicatorBorderWidth: Dp = 2.dp

    /** Width of the divider line drawn between adjacent segments. */
    public val DividerWidth: Dp = 1.dp

    /** Height of the divider line drawn between adjacent segments. */
    public val DividerHeight: Dp = 24.dp
    public val DividerColor: Color
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.colors.outline

    /** Minimum width and height applied to the control track to satisfy touch-target guidelines. */
    public val MinTouchTargetSize: Dp = 48.dp
    public val MinHeight: Dp = 52.dp

    public val SegmentTextStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.typography.body2

    public val SegmentTextStyleSelected: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.typography.body2.highlight

    public val SegmentTextColor: Color
        @Composable
        get() = SparkTheme.colors.onSurface.dim1

    public val SegmentTextColorSelected: Color
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.colors.onSurface

    public val SegmentTextColorDisabled: Color
        @Composable
        get() = SparkTheme.colors.onSurface.disabled

    public val SegmentIconColor: Color
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.colors.support

    public val SegmentIconSelectedColor: Color
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.colors.supportVariant

    public val SegmentIconDisabledColor: Color
        @Composable
        get() = SparkTheme.colors.support.disabled

    public val SegmentIconDisabledSelectedColor: Color
        @Composable
        get() = SparkTheme.colors.supportVariant.disabled

    public val SegmentIconSize: IconSize = IconSize.Medium
}
