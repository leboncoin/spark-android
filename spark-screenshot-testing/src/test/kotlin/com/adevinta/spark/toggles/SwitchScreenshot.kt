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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.toggles.SwitchIcons
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.AlarmOffFill
import com.adevinta.spark.icons.AlarmOnFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import org.junit.Rule
import org.junit.Test

class SwitchScreenshot {
    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Foldable,
    )

    @Test
    fun all_states() {
        paparazzi.sparkSnapshotNightMode {
            SwitchStates()
        }
    }
}

@Composable
private fun SwitchStates() {
    val text =
        "This is an example of a multi-line text which is very long and in which the user should read all the information."
    val icons = SwitchIcons(
        checked = SparkIcons.AlarmOnFill,
        unchecked = SparkIcons.AlarmOffFill,
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SwitchLabelled(
            enabled = true,
            checked = true,
            onCheckedChange = {},
            icons = icons,
        ) { Text(text = "Label") }
        SwitchLabelled(
            enabled = true,
            checked = false,
            onCheckedChange = {},
            icons = icons,
        ) { Text("Label") }
        SwitchLabelled(
            enabled = false,
            checked = true,
            onCheckedChange = {},
            icons = icons,
        ) { Text(text) }
        SwitchLabelled(
            enabled = false,
            checked = false,
            onCheckedChange = {},
            icons = icons,
        ) { Text(text) }
    }
}
