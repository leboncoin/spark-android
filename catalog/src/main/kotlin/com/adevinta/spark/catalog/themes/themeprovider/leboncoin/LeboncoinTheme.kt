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
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.catalog.themes.themeprovider.ThemeProvider
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.SparkShapes
import com.adevinta.spark.tokens.SparkTypography
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors

public object LeboncoinTheme : ThemeProvider {
    @Composable
    override fun colors(useDarkColors: Boolean, isPro: Boolean, contrastLevel: Float): SparkColors =
        if (contrastLevel in 0.0f..0.33f) {
            basicTheme(useDarkColors, isPro)
        } else {
            highContrastTheme(useDarkColors)
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

    @Composable
    private fun highContrastTheme(useDarkColors: Boolean): SparkColors {
        val DarkBold = Color(0xff1a1a1a)
        val DarkSemiBold = Color(0xff2c2c2c)
        val DarkRegular = Color(0xff484848)
        val DarkMid = Color(0xff4f4f4f)
        val DarkMedium = Color(0xffE6E6E6)
        val DarkLight = Color(0xffF9F9F9)
        return if (useDarkColors) {
            val LightBold = Color(0xff1a1a1a)
            val LightRegular = Color(0xff484848)
            val LightMid = Color(0xff4f4f4f)
            val LightGray = Color(0xffbcbcbc)
            val LightLight = Color(0xffF9F9F9)
            darkSparkColors(
                accent = LightGray,
                onAccent = DarkBold,
                accentContainer = LightMid,
                onAccentContainer = LightLight,
                accentVariant = DarkMedium,
                onAccentVariant = DarkBold,
                basic = LightGray,
                onBasic = DarkBold,
                basicContainer = LightMid,
                onBasicContainer = LightLight,
                main = LightGray,
                onMain = DarkBold,
                mainContainer = LightMid,
                onMainContainer = LightLight,
                mainVariant = LightGray,
                onMainVariant = DarkBold,
                support = DarkMedium,
                onSupport = DarkBold,
                supportContainer = LightMid,
                onSupportContainer = LightLight,
                supportVariant = LightGray,
                onSupportVariant = DarkBold,
                success = LightGray,
                onSuccess = DarkBold,
                successContainer = LightMid,
                onSuccessContainer = LightLight,
                alert = LightGray,
                onAlert = DarkBold,
                alertContainer = LightMid,
                onAlertContainer = LightLight,
                error = LightGray,
                onError = DarkBold,
                errorContainer = LightMid,
                onErrorContainer = LightLight,
                info = LightGray,
                onInfo = DarkBold,
                infoContainer = LightMid,
                onInfoContainer = LightLight,
                neutral = LightGray,
                onNeutral = DarkBold,
                neutralContainer = LightMid,
                onNeutralContainer = LightLight,
                background = DarkSemiBold,
                onBackground = LightLight,
                backgroundVariant = Color(0xff3c3c3c),
                onBackgroundVariant = LightLight,
                surface = DarkSemiBold,
                onSurface = LightLight,
                surfaceInverse = LightLight,
                onSurfaceInverse = DarkBold,
                outline = Color(0xffc1c1c1),
                outlineHigh = LightLight,
            )
        } else {
            lightSparkColors(
                accent = DarkRegular,
                onAccent = DarkLight,
                accentContainer = DarkMedium,
                onAccentContainer = DarkBold,
                accentVariant = DarkMedium,
                onAccentVariant = DarkLight,
                basic = DarkRegular,
                onBasic = DarkLight,
                basicContainer = DarkMedium,
                onBasicContainer = DarkBold,
                main = DarkRegular,
                onMain = DarkLight,
                mainContainer = DarkMedium,
                onMainContainer = DarkBold,
                mainVariant = DarkRegular,
                onMainVariant = DarkLight,
                support = DarkRegular,
                onSupport = DarkLight,
                supportContainer = DarkMedium,
                onSupportContainer = DarkBold,
                supportVariant = DarkRegular,
                onSupportVariant = DarkLight,
                success = DarkRegular,
                onSuccess = DarkLight,
                successContainer = DarkMedium,
                onSuccessContainer = DarkBold,
                alert = DarkRegular,
                onAlert = DarkLight,
                alertContainer = DarkMedium,
                onAlertContainer = DarkBold,
                error = DarkRegular,
                onError = DarkLight,
                errorContainer = DarkMedium,
                onErrorContainer = DarkBold,
                info = DarkRegular,
                onInfo = DarkLight,
                infoContainer = DarkMedium,
                onInfoContainer = DarkBold,
                neutral = DarkRegular,
                onNeutral = DarkLight,
                neutralContainer = DarkMedium,
                onNeutralContainer = DarkBold,
                background = DarkLight,
                onBackground = DarkBold,
                backgroundVariant = DarkMedium,
                onBackgroundVariant = DarkBold,
                surface = DarkLight,
                onSurface = DarkBold,
                surfaceInverse = DarkBold,
                onSurfaceInverse = DarkLight,
                outline = DarkMid,
                outlineHigh = Color.Black,
            )
        }
    }
}
