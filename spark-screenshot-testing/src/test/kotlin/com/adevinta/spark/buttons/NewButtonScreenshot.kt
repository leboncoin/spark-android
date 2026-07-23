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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.buttons.Ai
import com.adevinta.spark.components.buttons.Boost
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.Contrast
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.buttons.Primary
import com.adevinta.spark.components.buttons.Secondary
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.components.buttons.Tertiary
import com.adevinta.spark.components.buttons.Text
import com.adevinta.spark.components.buttons.Underlined
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

internal class NewButtonScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = V_SCROLL,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun allVariants() {
        paparazzi.sparkSnapshotNightMode {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AllNewButtons(size = ButtonSize.Medium, enabled = true)
                AllNewButtons(size = ButtonSize.Medium, enabled = false)
                AllNewButtons(size = ButtonSize.Large, enabled = true)
                AllNewButtons(size = ButtonSize.Large, enabled = false)
            }
        }
    }

    @Composable
    private fun AllNewButtons(size: ButtonSize, enabled: Boolean) {
        val icon = LeboncoinIcons.UserOutline
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button.Primary(
                onClick = {},
                text = "Primary",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Secondary(
                onClick = {},
                text = "Secondary",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Tertiary(
                onClick = {},
                text = "Tertiary",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Boost(
                onClick = {},
                text = "Boost",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Ai(
                onClick = {},
                text = "AI",
                size = size,
                enabled = enabled,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button.Danger(
                onClick = {},
                text = "Danger",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Success(
                onClick = {},
                text = "Success",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Contrast(
                onClick = {},
                text = "Contrast",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Text(
                onClick = {},
                text = "Text",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
            Button.Underlined(
                onClick = {},
                text = "Underlined",
                size = size,
                enabled = enabled,
                icon = icon,
                iconSide = IconSide.START,
            )
        }
    }
}
