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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

/**
 * Default values and constants for [SegmentedControl].
 *
 * Callers rarely need to reference these directly; they are exposed so that custom
 * [SegmentedControl.Horizontal] and [SegmentedControl.Vertical] wrappers can reuse the same
 * layout limits.
 */
public object SegmentedControlDefaults {
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

    /** Width of the divider line drawn between adjacent segments. */
    public val DividerWidth: Dp = 1.dp

    /** Minimum width and height applied to the control track to satisfy touch-target guidelines. */
    public val MinTouchTargetSize: Dp = 48.dp

    /**
     * Default selection indicator: a box clipped to [shape] with an animated
     * [SparkTheme.colors.neutralContainer] fill and a 2 dp [SparkTheme.colors.outlineHigh] border.
     *
     * Pass this — or a custom composable with the same signature — to the `indicatorContent`
     * parameter of [SegmentedControl.Horizontal] or [SegmentedControl.Vertical].
     *
     * @param shape Clip shape for both the fill and the border.
     * @param modifier Additional modifier applied to the [Box].
     */
    @Composable
    public fun Indicator(
        shape: Shape,
        modifier: Modifier = Modifier,
    ) {
        val background = SparkTheme.colors.neutralContainer
        val borderColor = SparkTheme.colors.outlineHigh
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(shape)
                .border(2.dp, borderColor, shape)
                .drawBehind { drawRect(background) },
        )
    }
}

/**
 * Shape options for the [SegmentedControl.Vertical] indicator and segment touch targets.
 *
 * [SegmentedControl.Horizontal] always uses [Pill]; [SegmentedControl.Vertical] accepts either
 * variant and defaults to [Rounded].
 */
public enum class SegmentedControlShape {

    /** Large-radius rounded rectangle — [SparkTheme.shapes.large]. */
    Rounded {
        override val shape: CornerBasedShape
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.shapes.large
    },

    /** Fully rounded pill — [SparkTheme.shapes.full]. */
    Pill {
        override val shape: CornerBasedShape
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.shapes.full
    }, ;

    /** Resolved [CornerBasedShape] from the current [SparkTheme]. */
    public abstract val shape: CornerBasedShape
        @Composable @ReadOnlyComposable
        get
}
