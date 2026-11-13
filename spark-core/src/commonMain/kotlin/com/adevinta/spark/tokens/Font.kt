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
package com.adevinta.spark.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.adevinta.spark.core.Res
import com.adevinta.spark.core.nunito_sans_bold
import com.adevinta.spark.core.nunito_sans_bold_italic
import com.adevinta.spark.core.nunito_sans_italic
import com.adevinta.spark.core.nunito_sans_italic_variable
import com.adevinta.spark.core.nunito_sans_regular
import com.adevinta.spark.core.nunito_sans_semi_bold
import com.adevinta.spark.core.nunito_sans_semi_bold_italic
import com.adevinta.spark.core.nunito_sans_variable
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.Font

/**
 * Creates a [SparkFontFamily] with the given [fontFamily] and [useSparkTokensHighlighter].
 * @param fontFamily The [FontFamily] you want to use globally inside your app. If null, uses the default Nunito Sans font.
 * @param useSparkTokensHighlighter If true, the [FontFamily] will be replaced by [FontFamily.Cursive] when the
 * token highlighting is enabled in the SparkTheme.
 */
@Composable
public fun sparkFontFamily(
    fontFamily: FontFamily? = null,
    useSparkTokensHighlighter: Boolean = false,
): SparkFontFamily = SparkFontFamily(
    useSparkTokensHighlighter = useSparkTokensHighlighter,
    fontFamily = fontFamily ?: nunitoFontFamily(),
)

/** Holds the active font family for the SparkTheme, with optional token-highlighter override. */
@Immutable
public class SparkFontFamily(private val useSparkTokensHighlighter: Boolean, private val fontFamily: FontFamily) {
    /**
     * The [FontFamily] used globally inside the app.
     */
    public val default: FontFamily
        @Composable @ReadOnlyComposable
        get() = if (useSparkTokensHighlighter && !LocalInspectionMode.current) FontFamily.Cursive else fontFamily
}

/**
 * Whether the platform renders variable font axes correctly.
 *
 * When this is false, [nunitoFontFamily] uses the static Nunito Sans files instead.
 */
internal expect fun supportsVariableFonts(): Boolean

/** The default Nunito Sans font family used by Spark. */
@Composable
internal fun nunitoFontFamily(): FontFamily = if (supportsVariableFonts()) {
    // Nunito Sans ships the italic as a second variable file, so each style has its own resource.
    FontFamily(
        nunitoVariableFont(Res.font.nunito_sans_variable, weight = 400, style = FontStyle.Normal),
        nunitoVariableFont(Res.font.nunito_sans_variable, weight = 600, style = FontStyle.Normal),
        nunitoVariableFont(Res.font.nunito_sans_variable, weight = 700, style = FontStyle.Normal),
        nunitoVariableFont(Res.font.nunito_sans_italic_variable, weight = 400, style = FontStyle.Italic),
        nunitoVariableFont(Res.font.nunito_sans_italic_variable, weight = 600, style = FontStyle.Italic),
        nunitoVariableFont(Res.font.nunito_sans_italic_variable, weight = 700, style = FontStyle.Italic),
    )
} else {
    FontFamily(
        Font(resource = Res.font.nunito_sans_regular, weight = FontWeight.Normal),
        Font(resource = Res.font.nunito_sans_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_sans_semi_bold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.nunito_sans_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_sans_bold, weight = FontWeight.Bold),
        Font(resource = Res.font.nunito_sans_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    )
}

@Composable
private fun nunitoVariableFont(resource: FontResource, weight: Int, style: FontStyle): Font = Font(
    resource = resource,
    weight = FontWeight(weight),
    style = style,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight),
        FontVariation.width(100f),
        FontVariation.Setting("YTLC", 500f),
    ),
)
