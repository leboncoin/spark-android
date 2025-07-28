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
package com.adevinta.spark.toggles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.toggles.RadioButtonLabelled
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import org.junit.Rule
import org.junit.Test

class RadioButtonScreenshot {
    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Foldable,
    )

    @Test
    fun all_states() {
        paparazzi.sparkSnapshotNightMode {
            Row {
                RadioButtonStates(isError = false)
                RadioButtonStates(isError = true)
            }
        }
    }
}

@Composable
private fun RadioButtonStates(isError: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RadioButtonLabelled(
            enabled = true,
            selected = true,
            onClick = {},
            error = isError,
        ) { Text("Nui") }
        RadioButtonLabelled(
            enabled = false,
            selected = true,
            onClick = {},
            error = isError,
        ) { Text("Mako") }
        RadioButtonLabelled(
            enabled = true,
            selected = false,
            onClick = {},
            error = isError,
        ) { Text("Ragyö") }
        RadioButtonLabelled(
            enabled = false,
            selected = false,
            onClick = {},
            error = isError,
        ) { Text("Nonon") }
    }
}
