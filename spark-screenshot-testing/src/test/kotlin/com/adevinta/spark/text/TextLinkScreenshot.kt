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
package com.adevinta.spark.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.stringResource
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.text.TextLink
import com.adevinta.spark.components.text.TextLinkButton
import com.adevinta.spark.icons.InfoOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.res.annotatedStringResource
import com.adevinta.spark.screenshot.testing.R
import com.adevinta.spark.sparkSnapshotNightMode
import org.junit.Rule
import org.junit.Test
import java.util.Locale

internal class TextLinkScreenshot {

    @get:Rule
    val paparazzi = paparazziRule()

    @Test
    fun testTextLink() {
        paparazzi.sparkSnapshotNightMode {
            Column {
                IconSide.entries.forEach { iconSide ->
                    Row {
                        TextLinkButton(
                            text = "Click me",
                            icon = LeboncoinIcons.InfoOutline,
                            intent = ButtonIntent.Accent,
                            iconSide = iconSide,
                            onClick = {},
                        )
                    }
                }
                TextLink(
                    style = SparkTheme.typography.subhead,
                    text = annotatedStringResource(id = R.string.spark_text_link_short_example),
                    onClickLabel = "textLink",
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun frenchVariant() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DefaultTestDevices.Phone.copy(
                locale = Locale.FRENCH.toLanguageTag(),
            ),
        )
        paparazzi.sparkSnapshotNightMode {
            Column {
                IconSide.entries.forEach { iconSide ->
                    Row {
                        TextLinkButton(
                            text = stringResource(id = R.string.spark_text_link_button_example),
                            icon = LeboncoinIcons.InfoOutline,
                            intent = ButtonIntent.Accent,
                            iconSide = iconSide,
                            onClick = {},
                        )
                    }
                }
                TextLink(
                    style = SparkTheme.typography.subhead,
                    text = annotatedStringResource(id = R.string.spark_text_link_short_example),
                    onClickLabel = "textLink",
                    onClick = {},
                )
            }
        }
    }
}
