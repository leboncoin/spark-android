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
package com.adevinta.spark.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonContrast
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

internal class ButtonScreenshot {

    private val intents = ButtonIntent.entries

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = V_SCROLL,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun medium() {
        paparazzi.sparkSnapshotNightMode {
            FlowColumn {
                intents.forEach { intent ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ButtonsWithIcons(size = ButtonSize.Medium, intent = intent, enabled = true)
                        ButtonsWithIcons(size = ButtonSize.Medium, intent = intent, enabled = false)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun large() {
        paparazzi.sparkSnapshotNightMode {
            FlowColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                intents.forEach { intent ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ButtonsWithIcons(size = ButtonSize.Large, intent = intent, enabled = true)
                        ButtonsWithIcons(size = ButtonSize.Large, intent = intent, enabled = false)
                    }
                }
            }
        }
    }

    @Composable
    private fun ButtonsWithIcons(
        size: ButtonSize,
        enabled: Boolean,
        intent: ButtonIntent = ButtonIntent.Main,
    ) {
        Surface(
            color = if (intent == ButtonIntent.Surface) {
                SparkTheme.colors.surfaceInverse
            } else {
                SparkTheme.colors.surface
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ButtonFilled(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.START,
                    )
                    ButtonOutlined(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.START,
                    )
                    ButtonTinted(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.START,
                    )
                    ButtonContrast(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.START,
                    )
                    ButtonGhost(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.START,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ButtonFilled(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.END,
                    )
                    ButtonOutlined(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.END,
                    )
                    ButtonTinted(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.END,
                    )
                    ButtonContrast(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.END,
                    )
                    ButtonGhost(
                        onClick = {},
                        text = "Button",
                        size = size,
                        intent = intent,
                        enabled = enabled,
                        icon = LeboncoinIcons.UserOutline,
                        iconSide = IconSide.END,
                    )
                }
            }
        }
    }
}
