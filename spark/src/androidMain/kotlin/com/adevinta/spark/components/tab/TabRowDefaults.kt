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
package com.adevinta.spark.components.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

public object TabRowDefaults {

    /** Default container color of a tab row. */

    public val containerColor: Color
        @Composable get() = MainNavigationTabTokens.ContainerColor

    /** Default content color of a tab row. */
    public val contentColor: Color
        @Composable get() =
            MainNavigationTabTokens.ActiveTabColor

    /**
     * Default indicator, which will be positioned at the bottom of the [TabRow], on top of the
     * divider.
     *
     * @param modifier modifier for the indicator's layout
     * @param height height of the indicator
     * @param color color of the indicator
     */
    @Composable
    public fun Indicator(
        modifier: Modifier = Modifier,
        height: Dp = MainNavigationTabTokens.ActiveIndicatorHeight,
        color: Color = MainNavigationTabTokens.ActiveIndicatorColor,
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(height)
                .background(color = color),
        )
    }
}

internal object MainNavigationTabTokens {
    val ContainerColor
        @Composable get() = SparkTheme.colors.surface
    val ActiveTabColor
        @Composable get() = SparkTheme.colors.main
    val ActiveIndicatorHeight = 2.0.dp
    val ActiveIndicatorColor
        @Composable get() = ActiveTabColor
}
