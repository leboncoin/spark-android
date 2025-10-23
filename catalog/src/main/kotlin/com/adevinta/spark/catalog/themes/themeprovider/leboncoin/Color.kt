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

import androidx.compose.ui.graphics.Color
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors

// region Blueberry colors
private val Blueberry900 = Color(0xFF0F1824)
private val Blueberry800 = Color(0xFF1D3049)
private val Blueberry700 = Color(0xFF2C486D)
private val Blueberry600 = Color(0xFF3B6091)
private val Blueberry500 = Color(0xFF1388EC)
private val Blueberry400 = Color(0xFF69B2F3)
private val Blueberry300 = Color(0xFF9FCEF7)
private val Blueberry200 = Color(0xFFC2E0FA)
private val Blueberry100 = Color(0xFFE6F2FD)
private val Blueberry50 = Color(0xFFF4F9FE)
// endregion

// region Sonic colors
private val Sonic900 = Color(0xFF0B1330)
private val Sonic800 = Color(0xFF172760)
private val Sonic700 = Color(0xFF223A90)
private val Sonic600 = Color(0xFF2E4EC0)
private val Sonic500 = Color(0xFF3961F0)
private val Sonic400 = Color(0xFF6181F3)
private val Sonic300 = Color(0xFF88A0F6)
private val Sonic200 = Color(0xFFB0C0F9)
private val Sonic100 = Color(0xFFD7DFFC)
private val Sonic50 = Color(0xFFEBEFFD)
// endregion
//
// region Blue Java colors
private val BlueJava900 = Color(0xFF061013)
private val BlueJava800 = Color(0xFF18404D)
private val BlueJava700 = Color(0xFF256073)
private val BlueJava600 = Color(0xFF31809A)
private val BlueJava500 = Color(0xFF3DA0C0)
private val BlueJava400 = Color(0xFF64B3CD)
private val BlueJava300 = Color(0xFF8BC6D9)
private val BlueJava200 = Color(0xFFB1D9E6)
private val BlueJava100 = Color(0xFFD8ECF2)
private val BlueJava50 = Color(0xFFECF5F9)
// endregion

internal val LeboncoinColorProLight: SparkColors = lightSparkColors(
    main = Sonic500,
    onMain = Color.White,
    mainContainer = Sonic100,
    onMainContainer = Sonic700,
    mainVariant = Sonic700,
    onMainVariant = Color.White,
    support = Blueberry800,
    onSupport = Color.White,
    supportContainer = Blueberry100,
    onSupportContainer = Blueberry700,
    basic = Blueberry800,
    onBasic = Color.White,
    basicContainer = Blueberry100,
    onBasicContainer = Blueberry700,
    info = BlueJava400,
    onInfo = BlueJava900,
    infoContainer = BlueJava50,
    onInfoContainer = BlueJava800,
    background = Blueberry50,
    onBackground = Blueberry900,
    backgroundVariant = Blueberry50,
    onBackgroundVariant = Blueberry900,
    onSurface = Blueberry900,
    surfaceInverse = Sonic900,
    onSurfaceInverse = Sonic50,
    scrim = Sonic900,
)

internal val LeboncoinColorProDark: SparkColors = darkSparkColors(
    main = Sonic300,
    onMain = Sonic900,
    mainContainer = Sonic700,
    onMainContainer = Sonic100,
    mainVariant = Sonic500,
    onMainVariant = Color.White,
    support = Blueberry100,
    onSupport = Blueberry900,
    supportContainer = Blueberry700,
    onSupportContainer = Blueberry100,
    basic = Blueberry100,
    onBasic = Blueberry900,
    basicContainer = Blueberry700,
    onBasicContainer = Blueberry100,
    info = BlueJava300,
    onInfo = BlueJava900,
    infoContainer = BlueJava800,
    onInfoContainer = BlueJava50,
    background = Blueberry900,
    onBackground = Blueberry50,
    backgroundVariant = Blueberry900,
    onBackgroundVariant = Blueberry50,
    surface = Blueberry800,
    onSurface = Blueberry50,
    surfaceInverse = Sonic50,
    onSurfaceInverse = Sonic50,
    scrim = Sonic900,
)
