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

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.disabled

/**
 * Default values and constants for [SegmentedControl].
 *
 * Callers rarely need to reference these directly; they are exposed so that custom
 * [SegmentedControl.Horizontal] and [SegmentedControl.Vertical] wrappers can reuse the same
 * layout limits.
 */
public object SegmentedControlDefaults {

    /** Minimum width and height applied to the control track to satisfy touch-target guidelines. */
    public val MinTouchTargetSize: Dp = 48.dp

    public val SemanticRole: Role = Role.RadioButton

    /**
     * Default selection indicator: a box clipped to [shape] with an animated background and border.
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
        enabled: Boolean,
        modifier: Modifier = Modifier,
    ) {
        val background = animateColorAsState(
            if (enabled) {
                SparkTheme.colors.neutralContainer
            } else {
                SparkTheme.colors.surface
            },
        )
        val borderColor = animateColorAsState(
            if (enabled) {
                SparkTheme.colors.outlineHigh
            } else {
                SparkTheme.colors.outlineHigh.disabled
            },
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(shape)
                .border(SegmentedControlTokens.IndicatorBorderWidth, borderColor.value, shape)
                .drawBehind { drawRect(background.value ) },
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
    },
    ;

    /** Resolved [CornerBasedShape] from the current [SparkTheme]. */
    public abstract val shape: CornerBasedShape
        @Composable @ReadOnlyComposable
        get
}
