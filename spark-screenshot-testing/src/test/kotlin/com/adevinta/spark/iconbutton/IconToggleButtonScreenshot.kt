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
package com.adevinta.spark.iconbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonShape
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonContrast
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonFilled
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonGhost
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonOutlined
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonTinted
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.icons.Userfill
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

internal class IconToggleButtonScreenshot {

    private val icons: IconToggleButtonIcons = IconToggleButtonIcons(
        SparkIcons.UserOutline,
        SparkIcons.Userfill,
    )

    private val shapes = ButtonShape.entries

    private val sizes = IconButtonSize.entries

    private val intents = IconButtonIntent.entries

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SessionParams.RenderingMode.H_SCROLL,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun shape() {
        paparazzi.sparkSnapshot {
            FlowColumn {
                shapes.forEach { shape ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        sizes.forEach { size ->
                            IconToggleButtons(
                                checked = true,
                                size = size,
                                shape = shape,
                                enabled = true,
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun enabled() {
        paparazzi.sparkSnapshot {
            FlowColumn {
                intents.forEach { intent ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        sizes.forEach { size ->
                            IconToggleButtons(
                                checked = true,
                                size = size,
                                intent = intent,
                                enabled = true,
                            )
                            IconToggleButtons(
                                checked = false,
                                size = size,
                                intent = intent,
                                enabled = true,
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun disabled() {
        paparazzi.sparkSnapshot {
            FlowColumn {
                intents.forEach { intent ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        sizes.forEach { size ->
                            IconToggleButtons(
                                checked = true,
                                size = size,
                                intent = intent,
                                enabled = false,
                            )
                            IconToggleButtons(
                                checked = false,
                                size = size,
                                intent = intent,
                                enabled = false,
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalSparkApi::class)
    @Composable
    private fun IconToggleButtons(
        checked: Boolean,
        size: IconButtonSize,
        shape: ButtonShape = ButtonShape.Rounded,
        intent: IconButtonIntent = IconButtonIntent.Main,
        enabled: Boolean,
    ) {
        Surface(
            color = if (intent == IconButtonIntent.Surface) {
                SparkTheme.colors.surfaceInverse
            } else {
                SparkTheme.colors.surface
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                IconToggleButtonFilled(
                    checked = checked,
                    onCheckedChange = {},
                    icons = icons,
                    shape = shape,
                    size = size,
                    intent = intent,
                    enabled = enabled,
                )
                IconToggleButtonOutlined(
                    checked = checked,
                    onCheckedChange = {},
                    icons = icons,
                    shape = shape,
                    size = size,
                    intent = intent,
                    enabled = enabled,
                )
                IconToggleButtonTinted(
                    checked = checked,
                    onCheckedChange = {},
                    icons = icons,
                    shape = shape,
                    size = size,
                    intent = intent,
                    enabled = enabled,
                )
                IconToggleButtonContrast(
                    checked = checked,
                    onCheckedChange = {},
                    icons = icons,
                    shape = shape,
                    size = size,
                    intent = intent,
                    enabled = enabled,
                )
                IconToggleButtonGhost(
                    checked = checked,
                    onCheckedChange = {},
                    icons = icons,
                    shape = shape,
                    size = size,
                    intent = intent,
                    enabled = enabled,
                )
            }
        }
    }
}
