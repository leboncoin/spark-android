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
import androidx.compose.ui.text.font.FontFamily

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

/**
 * Utility class to handle the change of font family for the SparkTheme.
 */
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
 * The default Nunito Sans font family used by Spark.
 * Platform-specific implementations provide the actual fonts.
 */
@Composable
internal expect fun nunitoFontFamily(): FontFamily
