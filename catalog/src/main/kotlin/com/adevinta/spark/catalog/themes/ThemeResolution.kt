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
package com.adevinta.spark.catalog.themes

import android.content.res.Configuration
import androidx.compose.material3.ColorScheme
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.asSparkColors

internal fun isSystemDark(configuration: Configuration): Boolean =
    configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

internal fun useDarkTheme(themeMode: ThemeMode, isSystemDark: Boolean): Boolean =
    themeMode == ThemeMode.Dark || (themeMode == ThemeMode.System && isSystemDark)

internal fun resolveSparkColors(
    themeMode: ThemeMode,
    colorMode: ColorMode,
    isSystemDark: Boolean,
    dynamicColorScheme: ((useDark: Boolean) -> ColorScheme)?,
    baselineColors: SparkColors,
): SparkColors {
    val useDark = useDarkTheme(themeMode, isSystemDark)
    return if (colorMode == ColorMode.Dynamic && dynamicColorScheme != null) {
        dynamicColorScheme(useDark).asSparkColors(useDark = useDark)
    } else {
        baselineColors
    }
}
