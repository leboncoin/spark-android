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
package com.adevinta.spark.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

/** Documentation screenshots for each typography token, shown in light and dark theme side by side. */
internal class TypographyDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun display1() = snapshot("display1") { SparkTheme.typography.display1 }

    @Test
    fun display2() = snapshot("display2") { SparkTheme.typography.display2 }

    @Test
    fun display3() = snapshot("display3") { SparkTheme.typography.display3 }

    @Test
    fun headline1() = snapshot("headline1") { SparkTheme.typography.headline1 }

    @Test
    fun headline2() = snapshot("headline2") { SparkTheme.typography.headline2 }

    @Test
    fun subhead() = snapshot("subhead") { SparkTheme.typography.subhead }

    @Test
    fun body1() = snapshot("body1") { SparkTheme.typography.body1 }

    @Test
    fun body1Highlight() = snapshot("body1.highlight") { SparkTheme.typography.body1.highlight }

    @Test
    fun body2() = snapshot("body2") { SparkTheme.typography.body2 }

    @Test
    fun body2Highlight() = snapshot("body2.highlight") { SparkTheme.typography.body2.highlight }

    @Test
    fun caption() = snapshot("caption") { SparkTheme.typography.caption }

    @Test
    fun captionHighlight() = snapshot("caption.highlight") { SparkTheme.typography.caption.highlight }

    @Test
    fun small() = snapshot("small") { SparkTheme.typography.small }

    @Test
    fun smallHighlight() = snapshot("small.highlight") { SparkTheme.typography.small.highlight }

    @Test
    fun callout() = snapshot("callout") { SparkTheme.typography.callout }

    private fun snapshot(name: String, style: @Composable () -> TextStyle) =
        paparazzi.sparkDocSnapshot {
            Text(
                text = "This is font family text $name",
                style = style(),
            )
        }
}
