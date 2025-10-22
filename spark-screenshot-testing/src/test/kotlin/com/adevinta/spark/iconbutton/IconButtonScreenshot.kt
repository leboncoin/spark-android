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
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonShape
import com.adevinta.spark.components.iconbuttons.IconButtonContrast
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonOutlined
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.IconButtonTinted
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

internal class IconButtonScreenshot {

    private val shapes = ButtonShape.entries

    private val sizes = IconButtonSize.entries

    private val intents = IconButtonIntent.entries

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SessionParams.RenderingMode.SHRINK,
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
                            Buttons(size = size, shape = shape, enabled = true)
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
                            Buttons(size = size, intent = intent, enabled = true)
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
                            Buttons(size = size, intent = intent, enabled = false)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Buttons(
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
                IconButtonFilled(
                    onClick = {},
                    icon = SparkIcons.UserOutline,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = enabled,
                )
                IconButtonOutlined(
                    onClick = {},
                    icon = SparkIcons.UserOutline,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = enabled,
                )
                IconButtonTinted(
                    onClick = {},
                    icon = SparkIcons.UserOutline,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = enabled,
                )
                IconButtonContrast(
                    onClick = {},
                    icon = SparkIcons.UserOutline,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = enabled,
                )
                IconButtonGhost(
                    onClick = {},
                    icon = SparkIcons.UserOutline,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = enabled,
                )
            }
        }
    }
}
