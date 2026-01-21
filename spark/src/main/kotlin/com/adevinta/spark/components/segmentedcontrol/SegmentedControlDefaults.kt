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

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default values used by [SegmentedControl] components.
 */
public object SegmentedControlDefaults {
    /**
     * Maximum number of segments allowed in a horizontal layout.
     */
    public const val MaxHorizontalSegments: Int = 4

    /**
     * Maximum number of segments allowed in a vertical layout.
     */
    public const val MaxVerticalSegments: Int = 8

    /**
     * Minimum number of segments required.
     */
    public const val MinSegments: Int = 2

    /**
     * Border width for the segmented control container.
     */
    public val BorderWidth: Dp = 1.dp

    /**
     * Divider width between segments.
     */
    public val DividerWidth: Dp = 1.dp

    /**
     * Corner radius percentage for fully rounded corners (at edges).
     */
    public const val FullCornerRadiusPercent: Int = 50

    /**
     * Corner radius percentage for less rounded corners (in middle).
     */
    public const val MediumCornerRadiusPercent: Int = 15

    /**
     * Minimum touch target size for segments.
     */
    public val MinTouchTargetSize: Dp = 48.dp
}
