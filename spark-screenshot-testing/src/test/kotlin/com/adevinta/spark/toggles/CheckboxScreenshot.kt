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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.CheckboxLabelled
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import org.junit.Rule
import org.junit.Test

class CheckboxScreenshot {
    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Foldable,
    )

    @Test
    fun all_states() {
        paparazzi.sparkSnapshotNightMode {
            Row {
                CheckboxStates(isError = false)
                CheckboxStates(isError = true)
            }
        }
    }

    @Test
    fun vertical_alignment() {
        paparazzi.sparkSnapshotNightMode {
            val longLabel = "First line\nSecond line\nThird line"
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CheckboxLabelled(
                    state = ToggleableState.On,
                    onClick = {},
                    verticalAlignment = Alignment.Top,
                ) { Text(longLabel) }
                CheckboxLabelled(
                    state = ToggleableState.On,
                    onClick = {},
                    verticalAlignment = Alignment.CenterVertically,
                ) { Text(longLabel) }
                CheckboxLabelled(
                    state = ToggleableState.On,
                    onClick = {},
                    verticalAlignment = Alignment.Bottom,
                ) { Text(longLabel) }
            }
        }
    }
}

@Composable
private fun CheckboxStates(isError: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.On,
            onClick = {},
            error = isError,
        ) { Text("Paris") }
        CheckboxLabelled(
            enabled = false,
            state = ToggleableState.On,
            onClick = {},
            error = isError,
        ) { Text("Lyon") }
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.Indeterminate,
            onClick = {},
            error = isError,
        ) { Text("Briançon") }
        CheckboxLabelled(
            enabled = false,
            state = ToggleableState.Indeterminate,
            onClick = {},
            error = isError,
        ) { Text("Montgenèvre") }
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.Off,
            onClick = {},
            error = isError,
        ) { Text("La Clusaz") }
        CheckboxLabelled(
            enabled = false,
            state = ToggleableState.Off,
            onClick = {},
            error = isError,
        ) { Text("Grenoble") }
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.On,
            onClick = {},
            error = isError,
        ) {
            Text(
                "This is an example of a multi-line text which is very long and in which the user should read all the information.",
            )
        }
    }
}
