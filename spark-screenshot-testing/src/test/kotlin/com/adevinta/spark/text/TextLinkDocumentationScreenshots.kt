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
package com.adevinta.spark.text

import androidx.compose.foundation.layout.Column
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
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class TextLinkDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun textLink() = paparazzi.sparkDocSnapshot {
        TextLink(
            style = SparkTheme.typography.subhead,
            text = annotatedStringResource(id = R.string.spark_text_link_short_example),
            onClickLabel = "textLink",
            onClick = {},
        )
    }

    @Test
    fun textLinkButton() = paparazzi.sparkDocSnapshot {
        Column {
            TextLinkButton(
                text = "Click me",
                icon = LeboncoinIcons.InfoOutline,
                intent = ButtonIntent.Accent,
                iconSide = IconSide.START,
                onClick = {},
            )
            TextLinkButton(
                text = "Click me",
                icon = LeboncoinIcons.InfoOutline,
                intent = ButtonIntent.Accent,
                iconSide = IconSide.END,
                onClick = {},
            )
        }
    }
}
