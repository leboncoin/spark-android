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
package com.adevinta.spark

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.Paparazzi
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.darkHighContrastSparkColors
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightHighContrastSparkColors
import com.adevinta.spark.tokens.lightSparkColors

internal fun Paparazzi.sparkSnapshot(
    name: String? = null,
    drawBackground: Boolean = true,
    isDark: Boolean = false,
    composable: @Composable () -> Unit,
): Unit = sparkSnapshotWithColors(
    name = name,
    drawBackground = drawBackground,
    colors = if (isDark) darkSparkColors() else lightSparkColors(),
    composable = composable,
)

internal fun Paparazzi.gifView(
    drawBackground: Boolean = true,
    isDark: Boolean = false,
    composable: @Composable () -> Unit,
): View {
    val view = ComposeView(context)
    view.setContent {
        SparkThemeContent(
            colors = if (isDark) darkSparkColors() else lightSparkColors(),
            drawBackground = drawBackground,
            composable = composable,
        )
    }
    return view
}

internal fun Paparazzi.sparkSnapshotWithColors(
    name: String? = null,
    drawBackground: Boolean = true,
    colors: SparkColors,
    composable: @Composable () -> Unit,
): Unit = snapshot(name) {
    SparkThemeContent(
        colors = colors,
        drawBackground = drawBackground,
        composable = composable,
    )
}

@Composable
private fun SparkThemeContent(
    colors: SparkColors,
    drawBackground: Boolean,
    composable: @Composable () -> Unit,
) {
    // Behave like in Android Studio Preview renderer
    CompositionLocalProvider(LocalInspectionMode provides true) {
        SparkTheme(
            colors = colors,
        ) {
            // The first box acts as a shield from ComposeView which forces the first layout node
            // to match it's size. This allows the content below to wrap as needed.
            Box {
                // The second box adds a border and background so we can easily see layout bounds in screenshots
                Box(
                    Modifier.background(if (drawBackground) SparkTheme.colors.surface else Color.Transparent),
                ) {
                    composable()
                }
            }
        }
    }
}

/**
 * Generate 3 screenshots for each device: phone, tablet and foldable
 */
internal fun Paparazzi.sparkSnapshotDevices(
    name: String? = null,
    drawBackground: Boolean = true,
    isDark: Boolean = false,
    composable: @Composable () -> Unit,
) {
    DefaultTestDevices.devices.forEach { deviceConfig ->
        unsafeUpdateConfig(
            deviceConfig = deviceConfig,
        )
        sparkSnapshot(name.orEmpty() + "_${deviceConfig.screenWidth}", drawBackground, isDark, composable)
    }
}

/**
 * Generate 2 screenshots for each theme: light and dark
 */
internal fun Paparazzi.sparkSnapshotNightMode(
    name: String? = null,
    drawBackground: Boolean = true,
    composable: @Composable () -> Unit,
) {
    var exception: Throwable? = null
    ThemeVariant.entries.forEach {
        try {
            sparkSnapshot(
                name = name.orEmpty() + it.name,
                drawBackground = drawBackground,
                isDark = it == ThemeVariant.Dark,
                composable = composable,
            )
        } catch (e: Throwable) {
            // Keep track of the last exception to rethrow after running all tests
            exception = e
        }
    }
    exception?.let { throw it }
}

/**
 * Generate 2 screenshots for high contrast themes: light and dark high contrast
 */
internal fun Paparazzi.sparkSnapshotHighContrast(
    name: String? = null,
    drawBackground: Boolean = true,
    composable: @Composable () -> Unit,
) {
    var exception: Throwable? = null
    HighContrastThemeVariant.entries.forEach {
        try {
            sparkSnapshotWithColors(
                name = name.orEmpty() + it.name,
                drawBackground = drawBackground,
                colors = when (it) {
                    HighContrastThemeVariant.LightHighContrast -> lightHighContrastSparkColors()
                    HighContrastThemeVariant.DarkHighContrast -> darkHighContrastSparkColors()
                },
                composable = composable,
            )
        } catch (e: Throwable) {
            // Keep track of the last exception to rethrow after running all tests
            exception = e
        }
    }
    exception?.let { throw it }
}

enum class ThemeVariant { Light, Dark }

enum class HighContrastThemeVariant { LightHighContrast, DarkHighContrast }
