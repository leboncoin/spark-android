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

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import org.junit.Rule
import org.junit.Test

internal class TypographyScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
    )

    private val typography
        @Composable
        get() = with(SparkTheme.typography) {
            listOf(
                "display1" to display1,
                "display2" to display2,
                "display3" to display3,
                "headline1" to headline1,
                "headline2" to headline2,
                "subhead" to subhead,
                "body1" to body1,
                "body1.highlight" to body1.highlight,
                "body2" to body2,
                "body2.highlight" to body2.highlight,
                "caption" to caption,
                "caption.highlight" to caption.highlight,
                "small" to small,
                "small.highlight" to small.highlight,
                "callout" to callout,
            )
        }

    @Test
    fun typography() {
        paparazzi.sparkSnapshotNightMode {
            Typography()
        }
    }

    @Composable
    private fun Typography() {
        Column {
            typography.forEach { (name, style) ->
                TypographyItem(name, style)
            }
        }
    }

    @Composable
    private fun TypographyItem(name: String, style: TextStyle) {
        Text(
            text = "This is font family text $name",
            style = style,
        )
    }
}
