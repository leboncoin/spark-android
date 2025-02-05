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
package com.adevinta.spark.catalog.themes.themeprovider.leboncoin

import androidx.compose.runtime.Composable
import com.adevinta.spark.catalog.themes.themeprovider.ThemeProvider
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.SparkShapes
import com.adevinta.spark.tokens.SparkTypography
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors

public object LeboncoinTheme : ThemeProvider {
    @Composable
    override fun colors(useDarkColors: Boolean, isPro: Boolean, contrastLevel: Float): SparkColors =
        when (contrastLevel) {
            in 0.0f..0.33f -> basicTheme(useDarkColors, isPro)

            in 0.34f..0.66f -> if (useDarkColors) {
                if (isPro) LeboncoinColorProDark else darkSparkColors(contrastLevel = contrastLevel)
            } else {
                if (isPro) LeboncoinColorProLight else lightSparkColors(contrastLevel = contrastLevel)
            }

            in 0.67f..1.0f -> if (useDarkColors) {
                if (isPro) LeboncoinColorProDark else darkSparkColors(contrastLevel = contrastLevel)
            } else {
                if (isPro) LeboncoinColorProLight else lightSparkColors(contrastLevel = contrastLevel)
            }

            else -> basicTheme(useDarkColors, isPro)
        }


    @Composable
    override fun shapes(): SparkShapes = LeboncoinShapes

    @Composable
    override fun typography(): SparkTypography = LeboncoinTypo

    @Composable
    private fun basicTheme(useDarkColors: Boolean, isPro: Boolean): SparkColors = if (useDarkColors) {
        if (isPro) LeboncoinColorProDark else darkSparkColors()
    } else {
        if (isPro) LeboncoinColorProLight else lightSparkColors()
    }
}
