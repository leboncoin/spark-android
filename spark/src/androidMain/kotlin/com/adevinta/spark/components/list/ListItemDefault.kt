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
package com.adevinta.spark.components.list

import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

public object ListItemDefault {
    private const val ListItemDisabledLabelTextOpacity = 0.3f
    private const val ListItemDisabledLeadingIconOpacity = 0.38f
    private const val ListItemDisabledTrailingIconOpacity = 0.38f

    public val Elevation: Dp = 0.0.dp

    @Composable
    public fun colors(): ListItemColors = ListItemDefaults.colors(
        containerColor = SparkTheme.colors.surface,
        headlineColor = SparkTheme.colors.onSurface,
        leadingIconColor = SparkTheme.colors.neutral,
        overlineColor = SparkTheme.colors.neutral,
        supportingColor = SparkTheme.colors.neutral,
        trailingIconColor = SparkTheme.colors.neutral,
        disabledHeadlineColor = SparkTheme.colors.onSurface.copy(alpha = ListItemDisabledLabelTextOpacity),
        disabledLeadingIconColor = SparkTheme.colors.onSurface.copy(alpha = ListItemDisabledLeadingIconOpacity),
        disabledTrailingIconColor = SparkTheme.colors.onSurface.copy(alpha = ListItemDisabledTrailingIconOpacity),
    )
}
