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
package com.adevinta.spark.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.badge.Badge
import com.adevinta.spark.components.tab.Tab
import com.adevinta.spark.components.tab.TabGroup
import com.adevinta.spark.components.tab.TabIntent
import com.adevinta.spark.components.tab.TabSize
import com.adevinta.spark.icons.BubbleTextOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

internal class TabsScreenshot {

    private val sizes = TabSize.entries

    private val intents = TabIntent.entries

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SessionParams.RenderingMode.SHRINK,
        deviceConfig = DefaultTestDevices.Phone,
    )

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun enabled() {
        paparazzi.sparkSnapshot {
            sizes.forEach { size ->
                FlowColumn {
                    intents.forEach { intent ->
                        Column {
                            TabGroup(
                                selectedTabIndex = 0,
                                intent = intent,
                            ) {
                                Tab(
                                    selected = true,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    enabled = true,
                                    text = "Home",
                                )
                                Tab(
                                    selected = false,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    text = "Message",
                                    enabled = true,
                                    icon = SparkIcons.BubbleTextOutline,
                                    trailingContent = {
                                        Badge(count = 5)
                                    },
                                )
                            }
                            TabGroup(
                                selectedTabIndex = 0,
                                intent = intent,
                                spacedEvenly = false,
                            ) {
                                Tab(
                                    selected = true,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    enabled = true,
                                    text = "Home",
                                )
                                Tab(
                                    selected = false,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    text = "Message",
                                    enabled = true,
                                    icon = SparkIcons.BubbleTextOutline,
                                    trailingContent = {
                                        Badge(count = 5)
                                    },
                                )
                            }
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
            sizes.forEach { size ->
                FlowColumn(
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    intents.forEach { intent ->
                        Column {
                            TabGroup(
                                selectedTabIndex = 0,
                                intent = intent,
                            ) {
                                Tab(
                                    selected = true,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    text = "Home",
                                    enabled = false,
                                )
                                Tab(
                                    selected = false,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    text = "Message",
                                    icon = SparkIcons.BubbleTextOutline,
                                    enabled = false,
                                    trailingContent = {
                                        Badge(count = 5)
                                    },
                                )
                            }
                            TabGroup(
                                selectedTabIndex = 0,
                                intent = intent,
                                spacedEvenly = false,
                            ) {
                                Tab(
                                    selected = true,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    text = "Home",
                                    enabled = false,
                                )
                                Tab(
                                    selected = false,
                                    onClick = {},
                                    intent = intent,
                                    size = size,
                                    text = "Message",
                                    icon = SparkIcons.BubbleTextOutline,
                                    enabled = false,
                                    trailingContent = {
                                        Badge(count = 5)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
