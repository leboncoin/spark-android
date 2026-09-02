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
package com.adevinta.spark.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.tags.Highlight
import com.adevinta.spark.components.tags.HighlightBadge
import com.adevinta.spark.components.tags.Tag
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.tags.TagOutlined
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.FireFill
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class TagDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun tagFilled() = paparazzi.sparkDocSnapshot {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagFilled(text = "Main", leadingIcon = LeboncoinIcons.FireFill)
            TagFilled(text = "Main")
        }
    }

    @Test
    fun tagTinted() = paparazzi.sparkDocSnapshot {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagTinted(text = "Main", leadingIcon = LeboncoinIcons.FireFill)
            TagTinted(text = "Main")
        }
    }

    @Test
    fun tagOutlined() = paparazzi.sparkDocSnapshot({ SparkTheme.colors.backgroundVariant }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagOutlined(text = "Main", leadingIcon = LeboncoinIcons.FireFill)
            TagOutlined(text = "Main")
        }
    }

    @Test
    fun highlight() = paparazzi.sparkDocSnapshot {
        Tag.Highlight()
    }

    @Test
    fun highlightBadge() = paparazzi.sparkDocSnapshot({ SparkTheme.colors.backgroundVariant }) {
        Tag.HighlightBadge()
    }

    @Test
    fun tagAi() = paparazzi.sparkDocSnapshot {
        Tag.Ai {
            Text("À la une")
        }
    }
}
