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
package com.adevinta.spark.catalog.examples.samples.toggles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.toggles.ContentSide
import com.adevinta.spark.components.toggles.Switch
import com.adevinta.spark.components.toggles.SwitchDefaults
import com.adevinta.spark.components.toggles.SwitchIcons
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.AlarmOffFill
import com.adevinta.spark.icons.AlarmOnFill
import com.adevinta.spark.icons.SparkIcons

private const val SwitchExampleDescription = "Switch examples"
private const val SwitchExampleSourceUrl = "$SampleSourceUrl/SwitchSamples.kt"
public val SwitchExamples: List<Example> = listOf(
    Example(
        id = "standalone",
        name = "Standalone switch",
        description = SwitchExampleDescription,
        sourceUrl = SwitchExampleSourceUrl,
    ) {
        Column {
            var switchState by remember { mutableStateOf(true) }
            val onCheckedChange = { _: Boolean -> switchState = !switchState }
            SwitchPair(
                checked = switchState,
                onCheckedChange = onCheckedChange,
            )
            SwitchPair(
                icons = SwitchDefaults.icons,
                checked = switchState,
                onCheckedChange = onCheckedChange,
            )
            SwitchPair(
                icons = SwitchIcons(
                    checked = SparkIcons.AlarmOnFill,
                    unchecked = SparkIcons.AlarmOffFill,
                ),
                checked = switchState,
                onCheckedChange = onCheckedChange,
            )
        }
    },
    Example(
        id = "labeled",
        name = "Labeled switch content side End",
        description = SwitchExampleDescription,
        sourceUrl = SwitchExampleSourceUrl,
    ) {
        LabeledSwitchGroupExample(ContentSide.End)
    },
    Example(
        id = "labeled-start",
        name = "Labeled switch content side Start",
        description = SwitchExampleDescription,
        sourceUrl = SwitchExampleSourceUrl,
    ) {
        LabeledSwitchGroupExample(ContentSide.Start)
    },
)

@Composable
private fun LabeledSwitchGroupExample(
    contentSide: ContentSide,
) {
    val labels = listOf(
        stringResource(id = R.string.component_checkbox_group_example_option1_label),
        stringResource(id = R.string.component_checkbox_group_example_option2_label),
        stringResource(id = R.string.component_checkbox_content_side_example_label),

    )
    Column {
        labels.forEach { label ->
            var checked by remember { mutableStateOf(false) }
            SwitchLabelled(
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                checked = checked,
                onCheckedChange = { checked = !checked },
            ) {
                Text(
                    text = label,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SwitchPair(
    checked: Boolean,
    icons: SwitchIcons? = null,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = true,
        icons = icons,
    )
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = false,
        icons = icons,
    )
}
