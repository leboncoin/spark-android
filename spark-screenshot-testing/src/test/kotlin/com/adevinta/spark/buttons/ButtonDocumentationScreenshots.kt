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
package com.adevinta.spark.buttons

import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonContrast
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

/**
 * Documentation screenshots for Button component, following Material Design's approach of showing
 * each variant with light and dark theme side by side.
 */
internal class ButtonDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun buttonFilled() = paparazzi.sparkDocSnapshot {
        ButtonFilled(
            text = "Filled button",
            onClick = {},
        )
    }

    @Test
    fun buttonOutlined() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        ButtonOutlined(
            text = "Outlined button",
            onClick = {},
        )
    }

    @Test
    fun buttonTinted() = paparazzi.sparkDocSnapshot {
        ButtonTinted(
            text = "Tinted button",
            onClick = {},
        )
    }

    @Test
    fun buttonGhost() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        ButtonGhost(
            text = "Ghost button",
            onClick = {},
        )
    }

    @Test
    fun buttonContrast() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        ButtonContrast(
            text = "Contrast button",
            onClick = {},
        )
    }
}
