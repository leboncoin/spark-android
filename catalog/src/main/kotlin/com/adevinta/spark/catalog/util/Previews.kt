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
package com.adevinta.spark.catalog.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewWrapper
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors
import com.adevinta.spark.tokens.sparkShapes
import com.adevinta.spark.tokens.sparkTypography

@OptIn(ExperimentalSharedTransitionApi::class)
@Suppress("ComposeModifierMissing") // It's okay since it’s a base theme
@Composable
internal fun PreviewTheme(
    useDarkColors: Boolean = LocalInspectionMode.current && isSystemInDarkTheme(),
    padding: PaddingValues = PaddingValues(16.dp),
    contentPadding: Dp = 16.dp,
    color: @Composable () -> Color = { SparkTheme.colors.background },
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkTenantTheme(
        // We don't want to automatically support dark theme in the app but still want it in the previews
        useDarkColors = useDarkColors,
    ) {
        SharedTransitionLayout {
            AnimatedVisibility(true, label = "LocalSharedTransitionScope") {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalAnimatedVisibilityScope provides this,
                ) {
                    PreviewWrapper(
                        padding = padding,
                        contentPadding = contentPadding,
                        color = color,
                        content = content,
                    )
                }
            }
        }
    }
}

@Composable
internal fun SparkTenantTheme(
    // We don't want to automatically support dark theme in the app but still want it in the previews
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkColors) {
        darkSparkColors()
    } else {
        lightSparkColors()
    }
    SparkTheme(
        colors = colors,
        shapes = sparkShapes(),
        typography = sparkTypography(),
        content = content,
    )
}
