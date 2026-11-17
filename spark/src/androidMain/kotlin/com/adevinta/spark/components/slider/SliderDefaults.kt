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
package com.adevinta.spark.components.slider

import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.adevinta.spark.SparkTheme
import androidx.compose.material3.SliderDefaults as MaterialSliderDefaults

public object SliderDefaults {
    private const val TickMarksContainerOpacity = 0.38f
    private const val DisabledOpacity = 0.38f
    private const val DisabledInactiveTrackOpacity = 0.12f

    @Composable
    public fun colors(
        thumbColor: Color = SparkTheme.colors.main,
        activeTrackColor: Color = SparkTheme.colors.main,
        activeTickColor: Color = SparkTheme.colors.onMain.copy(alpha = TickMarksContainerOpacity),
        inactiveTrackColor: Color = SparkTheme.colors.neutralContainer,
        inactiveTickColor: Color = SparkTheme.colors.onNeutralContainer.copy(alpha = TickMarksContainerOpacity),
        disabledThumbColor: Color = SparkTheme.colors.onSurface
            .copy(alpha = DisabledOpacity)
            .compositeOver(SparkTheme.colors.surface),
        disabledActiveTrackColor: Color = SparkTheme.colors.onSurface.copy(alpha = DisabledOpacity),
        disabledActiveTickColor: Color = disabledActiveTrackColor,
        disabledInactiveTrackColor: Color = SparkTheme.colors.onSurface.copy(alpha = DisabledInactiveTrackOpacity),

        disabledInactiveTickColor: Color = disabledActiveTrackColor,
    ): SliderColors = MaterialSliderDefaults.colors(
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        activeTickColor = activeTickColor,
        inactiveTrackColor = inactiveTrackColor,
        inactiveTickColor = inactiveTickColor,
        disabledThumbColor = disabledThumbColor,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledActiveTickColor = disabledActiveTickColor,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
        disabledInactiveTickColor = disabledInactiveTickColor,
    )
}
