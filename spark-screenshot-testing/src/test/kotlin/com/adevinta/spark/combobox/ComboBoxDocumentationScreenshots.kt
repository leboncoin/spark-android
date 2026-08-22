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
package com.adevinta.spark.combobox

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.remember
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.NORMAL
import com.android.resources.ScreenOrientation
import org.junit.Rule
import org.junit.Test

internal class ComboBoxDocumentationScreenshots {

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
    fun singleChoiceComboBox() = paparazzi.sparkSnapshot {
        val state = remember { TextFieldState(initialText = "land") }
        SingleChoiceComboBox(
            state = state,
            expanded = true,
            onExpandedChange = {},
            onDismissRequest = {},
            label = "Country",
            placeholder = "Select a country",
        ) {
            DropdownMenuItem(
                text = { Text("Iceland") },
                selected = false,
                onClick = {},
            )
            DropdownMenuItem(
                text = { Text("Ireland") },
                selected = true,
                onClick = {},
            )
            DropdownMenuItem(
                text = { Text("Finland") },
                selected = false,
                onClick = {},
            )
        }
    }

    @Test
    fun multiChoiceComboBox() = paparazzi.sparkSnapshot {
        val state = remember { TextFieldState(initialText = "ing") }
        MultiChoiceComboBox(
            state = state,
            expanded = true,
            onExpandedChange = {},
            onDismissRequest = {},
            label = "Tags",
            placeholder = "Select tags",
        ) {
            DropdownMenuItem(
                text = { Text("Gaming") },
                checked = true,
                onCheckedChange = {},
            )
            DropdownMenuItem(
                text = { Text("Cooking") },
                checked = false,
                onCheckedChange = {},
            )
            DropdownMenuItem(
                text = { Text("Running") },
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}
