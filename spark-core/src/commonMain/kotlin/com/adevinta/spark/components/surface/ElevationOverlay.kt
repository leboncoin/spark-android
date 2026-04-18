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
package com.adevinta.spark.components.surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.contentColorFor
import kotlin.math.ln

/**
 * CompositionLocal containing the [ElevationOverlay] used by [Surface] components. Provide
 * `null` to turn off [ElevationOverlay]s for the children within this CompositionLocal..
 *
 * @see ElevationOverlay
 */
public val LocalElevationOverlay: ProvidableCompositionLocal<ElevationOverlay?> =
    staticCompositionLocalOf { DefaultElevationOverlay }

/**
 * An ElevationOverlay is an overlay applied to the background color of [Surface] components,
 * used to emphasize elevation in dark theme, where shadows are not as visible. An
 * ElevationOverlay does not replace the shadows drawn by a [Surface], but is used as an
 * additional representation of elevation.
 *
 * The default ElevationOverlay only applies in dark theme, in accordance with
 * the Material specification for
 * [Dark Theme](https://material.io/design/color/dark-theme.html#properties).
 *
 * See [LocalElevationOverlay] to provide your own [ElevationOverlay]. You can provide `null`
 * to have no ElevationOverlay applied.
 */
@Stable
public fun interface ElevationOverlay {
    /**
     * Returns the new background [Color] to use, representing the original background [color]
     * with an overlay corresponding to [elevation] applied. Typically this should only be
     * applied to [com.adevinta.spark.tokens.SparkColors.surface].
     */
    @Composable
    public fun apply(color: Color, elevation: Dp): Color
}

/**
 * The default [ElevationOverlay] implementation.
 */
private object DefaultElevationOverlay : ElevationOverlay {
    @ReadOnlyComposable
    @Composable
    override fun apply(
        color: Color,
        elevation: Dp,
    ): Color = if (elevation > 0.dp && SparkTheme.colors.surface.luminance() <= 0.5) {
        val foregroundColor = calculateForegroundColor(elevation)
        foregroundColor.compositeOver(color)
    } else {
        color
    }
}

/**
 * @return the alpha-modified foreground color to overlay on top of the surface color to produce
 * the resultant color. This color is the [contentColorFor] the `backgroundColor`, with alpha
 * applied depending on the value of [elevation].
 */
@ReadOnlyComposable
@Composable
private fun calculateForegroundColor(elevation: Dp): Color {
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return SparkTheme.colors.surfaceTint.copy(alpha = alpha)
}
