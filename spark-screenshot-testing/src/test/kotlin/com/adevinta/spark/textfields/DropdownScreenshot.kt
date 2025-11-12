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
package com.adevinta.spark.textfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenuGroupItem
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceDropdown
import com.adevinta.spark.components.textfields.SingleChoiceDropdown
import com.adevinta.spark.icons.FlashlightFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.ValidFill
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class DropdownScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Phone,
        renderingMode = SHRINK,
    )

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun expanded() {
        paparazzi.sparkSnapshot {
            FlowColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SingleChoiceDropdown(
                    value = stubBody,
                    enabled = true,
                    onExpandedChange = {},
                    onDismissRequest = {},
                    expanded = false,
                ) {
                    DropdownMenuItem(
                        text = { Text("book") },
                        onClick = { },
                        selected = false,
                    )
                }
                MultiChoiceDropdown(
                    value = stubShortBody,
                    enabled = true,
                    onExpandedChange = {},
                    onDismissRequest = {},
                    expanded = true,
                ) {
                    DropdownMenuItem(
                        text = { Text("book") },
                        onCheckedChange = { },
                        checked = false,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun items() {
        paparazzi.sparkSnapshot {
            FlowColumn(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DropdownMenuItems(enabled = true)
                DropdownMenuItems(enabled = false)
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun groups() {
        paparazzi.sparkSnapshot {
            FlowColumn(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DropdownMenuGroupItem(
                    title = { Text("Software") },
                ) {
                    DropdownMenuItems(enabled = true)
                }
                DropdownMenuGroupItem(
                    title = { Text("Player") },
                ) {
                    DropdownMenuItems(enabled = false)
                }
            }
        }
    }

    @Suppress("DEPRECATION") // Just for test we ant to show them
    @Composable
    private fun DropdownMenuItems(
        enabled: Boolean = true,
    ) {
        DropdownMenuItem(
            enabled = enabled,
            text = { Text("book") },
            leadingIcon = { Icon(sparkIcon = SparkIcons.ValidFill, contentDescription = null) },
            onClick = { },
        )
        DropdownMenuItem(
            enabled = enabled,
            text = { Text("book") },
            trailingIcon = { Icon(sparkIcon = SparkIcons.FlashlightFill, contentDescription = null) },
            onClick = { },
        )
        DropdownMenuItem(
            enabled = enabled,
            text = { Text("book") },
            leadingIcon = { Icon(sparkIcon = SparkIcons.ValidFill, contentDescription = null) },
            trailingIcon = { Icon(sparkIcon = SparkIcons.FlashlightFill, contentDescription = null) },
            onClick = { },
        )
    }

    companion object {
        private const val stubShortBody = "Lorem ipsum"
        private const val stubBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi lacus dolor, " +
            "pulvinar eu nulla sit amet, iaculis interdum."
    }
}
