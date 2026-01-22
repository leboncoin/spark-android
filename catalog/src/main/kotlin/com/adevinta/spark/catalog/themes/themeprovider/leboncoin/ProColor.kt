/*
 * Copyright (c) 2025 Adevinta
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

import androidx.compose.ui.graphics.Color
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors

// region Iris colors
internal val Iris50	= Color(0xFFEBEFFD)
internal val Iris100 = Color(0xFFD7DFFC)
internal val Iris200 = Color(0xFFB0C0F9)
internal val Iris300 = Color(0xFF88A0F6)
internal val Iris400 = Color(0xFF6181F3)
internal val Iris500 = Color(0xFF3961F0)
internal val Iris600 = Color(0xFF2E4EC0)
internal val Iris700 = Color(0xFF223A90)
internal val Iris800 = Color(0xFF172760)
internal val Iris900 = Color(0xFF0B1330)
// endregion

// region Violet colors
internal val Violet50 = Color(0xFFF3F1FC)
internal val Violet100 = Color(0xFFE6E3F9)
internal val Violet200 = Color(0xFFCEC8F3)
internal val Violet300 = Color(0xFFB5ACEC)
internal val Violet400 = Color(0xFF9D91E6)
internal val Violet500 = Color(0xFF8475E0)
internal val Violet600 = Color(0xFF6E61C1)
internal val Violet700 = Color(0xFF584DA2)
internal val Violet800 = Color(0xFF433884)
internal val Violet900 = Color(0xFF221A55)
// endregion

// region Scilla colors
internal val Scilla50 = Color(0xFFEDF2F8)
internal val Scilla100 = Color(0xFFDBE4F0)
internal val Scilla200 = Color(0xFFB6C9E2)
internal val Scilla300 = Color(0xFF92AED3)
internal val Scilla400 = Color(0xFF6E93C4)
internal val Scilla500 = Color(0xFF4572AB)
internal val Scilla600 = Color(0xFF3B6091)
internal val Scilla700 = Color(0xFF284162)
internal val Scilla800 = Color(0xFF18273A)
internal val Scilla900 = Color(0xFF0F1824)
// endregion

// region Euphorbia colors
internal val Euphorbia50 = Color(0xFFF5FBF8)
internal val Euphorbia100 = Color(0xFFE0F2E9)
internal val Euphorbia200 = Color(0xFFB7DFCB)
internal val Euphorbia300 = Color(0xFF8ECDAE)
internal val Euphorbia400 = Color(0xFF64BC90)
internal val Euphorbia500 = Color(0xFF31A56B)
internal val Euphorbia600 = Color(0xFF278456)
internal val Euphorbia700 = Color(0xFF1D6340)
internal val Euphorbia800 = Color(0xFF14422B)
internal val Euphorbia900 = Color(0xFF0C291B)
// endregion

// region Poppy colors
internal val Poppy50 = Color(0xFFFBE9ED)
internal val Poppy100 = Color(0xFFF7D4DA)
internal val Poppy200 = Color(0xFFEFA9B6)
internal val Poppy300 = Color(0xFFE87D91)
internal val Poppy400 = Color(0xFFE0526C)
internal val Poppy500 = Color(0xFFDA3150)
internal val Poppy600 = Color(0xFFAD1F39)
internal val Poppy700 = Color(0xFF82172B)
internal val Poppy800 = Color(0xFF56101D)
internal val Poppy900 = Color(0xFF2B080E)
// endregion

// region Sunflower colors
internal val Sunflower50 = Color(0xFFFFF8EF)
internal val Sunflower100 = Color(0xFFFFF1DF)
internal val Sunflower200 = Color(0xFFFFE2BE)
internal val Sunflower300 = Color(0xFFFED49E)
internal val Sunflower400 = Color(0xFFFEC57D)
internal val Sunflower500 = Color(0xFFFEB75D)
internal val Sunflower600 = Color(0xFFDD9D4B)
internal val Sunflower700 = Color(0xFFBD8238)
internal val Sunflower800 = Color(0xFF8A5F29)
internal val Sunflower900 = Color(0xFF543713)
// endregion

// region Halcyon colors
internal val Halcyon50 = Color(0xFFE2F7F9)
internal val Halcyon100 = Color(0xFFCDF1F5)
internal val Halcyon200 = Color(0xFFA3E5EC)
internal val Halcyon300 = Color(0xFF7AD8E2)
internal val Halcyon400 = Color(0xFF50CCD9)
internal val Halcyon500 = Color(0xFF26C0D0)
internal val Halcyon600 = Color(0xFF1E9AA6)
internal val Halcyon700 = Color(0xFF17737D)
internal val Halcyon800 = Color(0xFF0F4D53)
internal val Halcyon900 = Color(0xFF08262A)
// endregion

// region Obsidia colors
internal val Obsidia50 = Color(0xFFF8F9FA)
internal val Obsidia100 = Color(0xFFF0F3F4)
internal val Obsidia200 = Color(0xFFE9ECEF)
internal val Obsidia300 = Color(0xFFDEE2E6)
internal val Obsidia400 = Color(0xFFCED4DA)
internal val Obsidia500 = Color(0xFFADB5BD)
internal val Obsidia600 = Color(0xFF6C757D)
internal val Obsidia700 = Color(0xFF495057)
internal val Obsidia800 = Color(0xFF343A40)
internal val Obsidia900 = Color(0xFF212529)
// endregion

internal val LeboncoinColorProLight: SparkColors = lightSparkColors(
    main = Iris500,
    mainContainer = Iris100,
    onMainContainer = Iris700,
    mainVariant = Iris500,
    support = Scilla800,
    supportContainer = Scilla100,
    onSupportContainer = Scilla700,
    supportVariant = Scilla800,
    basic = Scilla800,
    basicContainer = Scilla100,
    onBasicContainer = Scilla700,
    accent = Violet600,
    accentContainer = Violet100,
    onAccentContainer = Violet800,
    accentVariant = Violet600,
    success = Euphorbia600,
    successContainer = Euphorbia100,
    onSuccessContainer = Euphorbia700,
    alert = Sunflower500,
    onAlert = Sunflower900,
    alertContainer = Sunflower100,
    onAlertContainer = Sunflower800,
    error = Poppy500,
    errorContainer = Poppy50,
    onErrorContainer = Poppy600,
    info = Halcyon500,
    onInfo = Halcyon900,
    infoContainer = Halcyon50,
    onInfoContainer = Halcyon700,
    neutral = Obsidia700,
    neutralContainer = Obsidia100,
    onNeutralContainer = Obsidia800,
    background = Scilla50,
    onBackground = Scilla900,
    backgroundVariant = Color.White,
    onBackgroundVariant = Scilla900,
    onSurface = Scilla900,
    surfaceInverse = Iris900,
    onSurfaceInverse = Iris50,
    surfaceDark = Iris900,
    onSurfaceDark = Iris50,
    scrim = Iris900,
    outline = Obsidia500,
    outlineHigh = Obsidia900,
)

internal val LeboncoinColorProDark: SparkColors = darkSparkColors(
    main = Iris300,
    onMain = Iris900,
    mainContainer = Iris700,
    onMainContainer = Iris100,
    mainVariant = Iris300,
    onMainVariant = Iris900,
    support = Scilla100,
    onSupport = Scilla900,
    supportContainer = Scilla700,
    onSupportContainer = Scilla100,
    supportVariant = Scilla100,
    onSupportVariant = Scilla900,
    basic = Scilla100,
    onBasic = Scilla900,
    basicContainer = Scilla700,
    onBasicContainer = Scilla100,
    accent = Violet300,
    onAccent = Violet900,
    accentContainer = Violet700,
    onAccentContainer = Violet100,
    accentVariant = Violet300,
    onAccentVariant = Violet900,
    success = Euphorbia300,
    onSuccess = Euphorbia900,
    successContainer = Euphorbia700,
    onSuccessContainer = Euphorbia100,
    alert = Sunflower400,
    onAlert = Sunflower900,
    alertContainer = Sunflower800,
    onAlertContainer = Sunflower100,
    error = Poppy300,
    onError = Poppy900,
    errorContainer = Poppy700,
    onErrorContainer = Poppy100,
    info = Halcyon300,
    onInfo = Halcyon900,
    infoContainer = Halcyon800,
    onInfoContainer = Halcyon50,
    neutral = Obsidia500,
    onNeutral = Obsidia900,
    neutralContainer = Obsidia700,
    onNeutralContainer = Obsidia100,
    background = Scilla900,
    onBackground = Scilla50,
    backgroundVariant = Scilla900,
    onBackgroundVariant = Scilla50,
    surface = Scilla800,
    onSurface = Scilla50,
    surfaceInverse = Iris800,
    onSurfaceInverse = Iris50,
    surfaceDark = Iris800,
    onSurfaceDark = Iris50,
    scrim = Iris900,
    outline = Obsidia600,
    outlineHigh = Obsidia100,
)
