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
package com.adevinta.spark.components.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

/** Documentation screenshots for the placeholder modifiers, each shown in light and dark theme side by side. */
internal class PlaceholderDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun placeholder() = paparazzi.sparkDocSnapshot {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .placeholder(visible = true),
        )
    }

    @Test
    fun textPlaceholder() = paparazzi.sparkDocSnapshot {
        Text(
            text = "Text placeholder",
            modifier = Modifier.textPlaceholder(visible = true),
        )
    }

    @Test
    fun illustrationPlaceholder() = paparazzi.sparkDocSnapshot {
        Box(
            modifier = Modifier
                .size(128.dp)
                .illustrationPlaceholder(visible = true, shape = SparkTheme.shapes.extraLarge),
        )
    }
}
