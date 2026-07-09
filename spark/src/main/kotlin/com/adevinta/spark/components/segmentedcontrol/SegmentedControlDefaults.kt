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

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
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

    /** Minimum width and height applied to the control track to satisfy touch-target guidelines. */
    public val MinTouchTargetSize: Dp = 48.dp

    public val SemanticRole: Role = Role.RadioButton

    /**
     * Default selection indicator: a box clipped to [shape] with an animated background and border.
     *
     * Pass this, or a custom composable with the same signature, to the `indicatorContent`
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
                SegmentedControlTokens.IndicatorColor
            } else {
                SegmentedControlTokens.IndicatorDisabledColor
            },
        )
        val borderColor = animateColorAsState(
            if (enabled) {
                SegmentedControlTokens.IndicatorBorderColor
            } else {
                SegmentedControlTokens.IndicatorBorderDisabledColor
            },
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(shape)
                .border(SegmentedControlTokens.IndicatorBorderWidth, borderColor.value, shape)
                .drawBehind { drawRect(background.value) },
        )
    }

    /**
     * Animated label style matching the built-in segment animations.
     *
     * Use this in [SegmentedControlScope.custom] content to get the same text colour
     * and weight transitions that the standard segment types use.
     *
     * @param selected Whether this segment is currently selected.
     * @param enabled Whether the parent control is enabled.
     * @return [SegmentLabelStyle] with animated [Color] and [TextStyle].
     */
    @Composable
    public fun segmentLabelStyle(
        selected: Boolean,
        enabled: Boolean,
    ): SegmentLabelStyle {
        val animationState = remember(selected, enabled) {
            MutableTransitionState(SegmentLabelAnimationState(selected, enabled))
        }
        val transition = rememberTransition(transitionState = animationState, label = "segmentLabel")
        val color by transition.animateColor(label = "labelColor") {
            when {
                it.enabled && it.selected -> SegmentedControlTokens.SegmentTextColorSelected
                it.enabled && !it.selected -> SegmentedControlTokens.SegmentTextColor
                else -> SegmentedControlTokens.SegmentTextColorDisabled
            }
        }
        val progress by transition.animateFloat(label = "labelProgress") { if (it.selected) 1f else 0f }
        val textStyle = lerp(
            start = SegmentedControlTokens.SegmentTextStyle,
            stop = SegmentedControlTokens.SegmentTextStyleSelected,
            fraction = progress,
        )
        return SegmentLabelStyle(color, textStyle)
    }

    /**
     * Animated icon colour matching the built-in segment animations.
     *
     * Use this in [SegmentedControlScope.custom] content to get the same icon tint
     * transitions that the standard icon segment types use.
     *
     * @param selected Whether this segment is currently selected.
     * @param enabled Whether the parent control is enabled.
     * @return Animated [State] of [Color] for icon tinting.
     */
    @Composable
    public fun segmentIconColor(
        selected: Boolean,
        enabled: Boolean,
    ): State<Color> = animateColorAsState(
        when {
            enabled && selected -> SegmentedControlTokens.SegmentIconSelectedColor
            enabled && !selected -> SegmentedControlTokens.SegmentIconColor
            !enabled && selected -> SegmentedControlTokens.SegmentIconDisabledSelectedColor
            else -> SegmentedControlTokens.SegmentIconDisabledColor
        },
        label = "segmentIconColor",
    )
}

/**
 * Animated colour and text style for segment labels.
 *
 * Obtained via [SegmentedControlDefaults.segmentLabelStyle].
 */
@Stable
public data class SegmentLabelStyle(val color: Color, val textStyle: TextStyle)

private data class SegmentLabelAnimationState(val selected: Boolean, val enabled: Boolean)

/**
 * Shape options for the [SegmentedControl.Vertical] indicator and segment touch targets.
 *
 * [SegmentedControl.Horizontal] always uses [Pill]. [SegmentedControl.Vertical] accepts either
 * variant and defaults to [Rounded].
 */
public enum class SegmentedControlShape {

    /** Large-radius rounded rectangle ([com.adevinta.spark.tokens.SparkShapes.large]). */
    Rounded {
        override val shape: CornerBasedShape
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.shapes.large
    },

    /** Fully rounded pill ([com.adevinta.spark.tokens.SparkShapes.full]). */
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
