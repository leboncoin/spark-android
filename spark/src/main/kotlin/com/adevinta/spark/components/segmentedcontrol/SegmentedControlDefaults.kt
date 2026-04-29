/*
 * Copyright (c) 2025 Adevinta
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

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

public object SegmentedControlDefaults {
    /** Minimum segments for both variants. */
    public const val MinSegments: Int = 2

    /** Minimum segments required for the Vertical variant. */
    public const val MinVerticalSegments: Int = 4

    /** Maximum segments for the Horizontal (single-row) variant. */
    public const val MaxHorizontalSegments: Int = 5

    /** Maximum segments for the Vertical (two-row) variant. */
    public const val MaxVerticalSegments: Int = 8

    public val BorderWidth: Dp = 1.dp
    public val DividerWidth: Dp = 1.dp
    public val MinTouchTargetSize: Dp = 48.dp
}

public enum class SegmentedControlShape {

    Rounded {
        override val shape: CornerBasedShape
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.shapes.large
    },
    Pill {
        override val shape: CornerBasedShape
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.shapes.full
    };

    public abstract val shape: CornerBasedShape
        @Composable @ReadOnlyComposable get
}
