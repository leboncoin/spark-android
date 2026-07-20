/*
 * Copyright (c) 2026 Adevinta
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
import com.adevinta.spark.components.buttons.Ai
import com.adevinta.spark.components.buttons.Boost
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.buttons.Contrast
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.Primary
import com.adevinta.spark.components.buttons.Secondary
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.components.buttons.Tertiary
import com.adevinta.spark.components.buttons.Text
import com.adevinta.spark.components.buttons.Underlined
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class NewButtonDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun buttonPrimary() = paparazzi.sparkDocSnapshot {
        Button.Primary(
            text = "Primary button",
            onClick = {},
        )
    }

    @Test
    fun buttonSecondary() = paparazzi.sparkDocSnapshot {
        Button.Secondary(
            text = "Secondary button",
            onClick = {},
        )
    }

    @Test
    fun buttonTertiary() = paparazzi.sparkDocSnapshot {
        Button.Tertiary(
            text = "Tertiary button",
            onClick = {},
        )
    }

    @Test
    fun buttonBoost() = paparazzi.sparkDocSnapshot {
        Button.Boost(
            text = "Boost button",
            onClick = {},
        )
    }

    @Test
    fun buttonAi() = paparazzi.sparkDocSnapshot {
        Button.Ai(
            text = "AI button",
            onClick = {},
        )
    }

    @Test
    fun buttonDanger() = paparazzi.sparkDocSnapshot {
        Button.Danger(
            text = "Danger button",
            onClick = {},
        )
    }

    @Test
    fun buttonSuccess() = paparazzi.sparkDocSnapshot {
        Button.Success(
            text = "Success button",
            onClick = {},
        )
    }

    @Test
    fun buttonContrast() = paparazzi.sparkDocSnapshot {
        Button.Contrast(
            text = "Contrast button",
            onClick = {},
        )
    }

    @Test
    fun buttonText() = paparazzi.sparkDocSnapshot {
        Button.Text(
            text = "Text button",
            onClick = {},
        )
    }

    @Test
    fun buttonUnderlined() = paparazzi.sparkDocSnapshot {
        Button.Underlined(
            text = "Underlined button",
            onClick = {},
        )
    }

    @Test
    fun buttonGhost() = paparazzi.sparkDocSnapshot {
        ButtonGhost(
            text = "Ghost button",
            onClick = {},
        )
    }
}
