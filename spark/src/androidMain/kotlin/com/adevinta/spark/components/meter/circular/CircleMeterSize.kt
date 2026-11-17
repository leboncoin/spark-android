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
package com.adevinta.spark.components.meter.circular

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.highlight

public enum class CircleMeterSize(public val diameter: Dp, public val strokeWidth: Dp, public val iconSize: Dp) {
    /**
     * Used preferably in small spaces like list items, table cells or sticky bottom
     */
    @InternalSparkApi
    Small(diameter = 24.dp, strokeWidth = 3.dp, iconSize = 0.dp),

    /**
     * Used preferably in KPI cards
     */
    Medium(diameter = 64.dp, strokeWidth = 5.dp, iconSize = 24.dp) {
        override val labelTextStyle: TextStyle
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.typography.small
    },

    /**
     * Used preferably in dashboards or for hero KPI
     */
    Large(diameter = 96.dp, strokeWidth = 8.dp, iconSize = 32.dp) {
        override val labelTextStyle: TextStyle
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.typography.caption
    },

    /**
     * Used preferably in large dashboards or for hero KPI if the space is available (like on tablet)
     */
    XLarge(diameter = 128.dp, strokeWidth = 10.dp, iconSize = 40.dp) {
        override val valueTextStyle: TextStyle
            @Composable
            @ReadOnlyComposable
            get() = SparkTheme.typography.display3
    }, ;

    public open val valueTextStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.typography.body2.highlight

    public open val labelTextStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = SparkTheme.typography.body2
}
