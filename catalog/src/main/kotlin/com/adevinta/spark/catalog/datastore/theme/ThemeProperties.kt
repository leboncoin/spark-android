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
package com.adevinta.spark.catalog.datastore.theme

import com.adevinta.spark.catalog.themes.ColorMode
import com.adevinta.spark.catalog.themes.FontScaleMode
import com.adevinta.spark.catalog.themes.NavigationMode
import com.adevinta.spark.catalog.themes.TextDirection
import com.adevinta.spark.catalog.themes.ThemeMode
import com.adevinta.spark.catalog.themes.UserMode
import com.adevinta.spark.catalog.ui.shaders.colorblindness.ColorBlindNessType
import kotlinx.serialization.Serializable

@Serializable
internal data class ThemeProperties(
    val fontScale: Float,
    val userMode: UserMode,
    val themeMode: ThemeMode,
    val colorMode: ColorMode,
    val fontScaleMode: FontScaleMode,
    val colorBlindNessType: ColorBlindNessType,
    val colorBlindNessSeverity: Float,
    val navigationMode: NavigationMode,
    val textDirection: TextDirection,
    val highlightSparkComponents: Boolean,
    val highlightSparkTokens: Boolean,
) {
    companion object {
        val DEFAULT = ThemeProperties(
            fontScale = 1.0f,
            userMode = UserMode.Part,
            themeMode = ThemeMode.System,
            colorMode = ColorMode.Baseline,
            textDirection = TextDirection.System,
            fontScaleMode = FontScaleMode.System,
            colorBlindNessType = ColorBlindNessType.None,
            colorBlindNessSeverity = 0.5f,
            navigationMode = NavigationMode.Default,
            highlightSparkComponents = false,
            highlightSparkTokens = false,
        )
    }
}
