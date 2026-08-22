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

import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.Dropdown
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.PenFill
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.NORMAL
import com.android.resources.ScreenOrientation
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class DropdownDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = NORMAL,
        deviceConfig = DefaultTestDevices.DocPhone.copy(
            screenWidth = 700,
            screenHeight = 860,
            orientation = ScreenOrientation.PORTRAIT,
        ),
    )

    @Test
    fun dropdown() = paparazzi.sparkSnapshot {
        Dropdown(
            value = "Edit",
            expanded = true,
            onExpandedChange = {},
            onDismissRequest = {},
            label = "Action",
        ) {
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
            DropdownMenuItem(
                text = { Text("Save") },
                onClick = {},
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                onClick = {},
                enabled = false,
            )
        }
    }
}
