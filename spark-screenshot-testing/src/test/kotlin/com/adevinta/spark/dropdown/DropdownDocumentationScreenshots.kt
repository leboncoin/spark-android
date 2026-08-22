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
package com.adevinta.spark.dropdown

import androidx.compose.foundation.layout.Column
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.menu.DropdownMenuItemWrapper
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.PenFill
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

// DropdownMenu renders its items in a popup window that Paparazzi cannot capture.
// This test renders the item list directly inside a Surface to show the menu item states.
@OptIn(InternalSparkApi::class)
internal class DropdownDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun dropdownMenuItems() = paparazzi.sparkDocSnapshot {
        Surface(shape = SparkTheme.shapes.small) {
            Column {
                with(DropdownMenuItemWrapper(this)) {
                    // Item with a leading icon
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {},
                        leadingIcon = {
                            Icon(
                                sparkIcon = LeboncoinIcons.PenFill,
                                contentDescription = null,
                            )
                        },
                    )
                    // Text-only item
                    DropdownMenuItem(
                        text = { Text("Save") },
                        onClick = {},
                    )
                    // Disabled item
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = {},
                        enabled = false,
                    )
                }
            }
        }
    }
}
