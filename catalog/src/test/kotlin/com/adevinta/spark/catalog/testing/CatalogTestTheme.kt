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
package com.adevinta.spark.catalog.testing

import androidx.compose.runtime.Composable
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.themes.BrandMode
import com.adevinta.spark.catalog.themes.VersionMode
import com.adevinta.spark.catalog.themes.themeprovider.ThemeProvider
import com.adevinta.spark.catalog.themes.themeprovider.leboncoin.LeboncoinTheme

@Composable
internal fun CatalogTestTheme(
    isDark: Boolean = false,
    isHighContrast: Boolean = false,
    brandMode: BrandMode = BrandMode.Part,
    versionMode: VersionMode = VersionMode.Current,
    themeProvider: ThemeProvider = LeboncoinTheme,
    content: @Composable () -> Unit,
) {
    SparkTheme(
        colors = themeProvider.colors(
            useDarkColors = isDark,
            brandMode = brandMode,
            versionMode = versionMode,
            contrastLevel = if (isHighContrast) 1f else 0f,
        ),
        shapes = themeProvider.shapes(),
        typography = themeProvider.typography(),
        content = content,
    )
}
