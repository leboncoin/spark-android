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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.toggles.ContentSide
import com.adevinta.spark.components.toggles.RadioButton
import com.adevinta.spark.components.toggles.RadioButtonLabelled

private const val RadioButtonExampleSourceUrl = "$SampleSourceUrl/RadioButtonSamples.kt"
public val RadioButtonExamples: List<Example> = listOf(
    Example(
        id = "standalone",
        name = "Standalone radio button",
        description = R.string.radio_button_example_description,
        sourceUrl = RadioButtonExampleSourceUrl,
    ) {
        Column {
            var selected by remember { mutableStateOf(true) }
            val onClick = { selected = !selected }
            RadioButton(
                selected = selected,
                onClick = onClick,
                enabled = true,
            )
            RadioButton(
                selected = selected,
                onClick = onClick,
                enabled = false,
            )
        }
    },
    Example(
        id = "labeled",
        name = "Labeled radio button content side",
        description = R.string.radio_button_example_description,
        sourceUrl = RadioButtonExampleSourceUrl,
    ) {
        RadioButtonContentSideExample()
    },
    Example(
        id = "group",
        name = "Labeled radio button group",
        description = R.string.radio_button_example_description,
        sourceUrl = RadioButtonExampleSourceUrl,
    ) {
        LabeledRadioButtonGroupExample()
    },
)

@Composable
private fun RadioButtonContentSideExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val label = stringResource(id = R.string.component_checkbox_content_side_example_label)
        ContentSide.values().forEach { contentSide ->
            var selected by remember { mutableStateOf(false) }
            RadioButtonLabelled(
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                selected = selected,
                contentSide = contentSide,
                onClick = { selected = !selected },
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
private fun LabeledRadioButtonGroupExample() {
    val labels = listOf(
        stringResource(id = R.string.component_checkbox_group_example_option1_label),
        stringResource(id = R.string.component_checkbox_group_example_option2_label),
    )
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        LabeledRadioButtonGroupHorizontalExample(labels)
        LabeledRadioButtonVerticalGroupExample(labels)
    }
}

@Composable
private fun LabeledRadioButtonVerticalGroupExample(
    labels: List<String>,
) {
    var childrenStates by remember {
        mutableStateOf(List(labels.size) { false })
    }
    Column {
        Text(
            text = stringResource(id = R.string.component_toggle_vertical_group_title),
            style = SparkTheme.typography.headline2,
        )
        VerticalSpacer(space = 8.dp)
        labels.forEachIndexed { index, label ->
            val selected = childrenStates.getOrElse(index) { false }
            val onClick = {
                childrenStates = childrenStates.mapIndexed { i, selected ->
                    if (i == index && selected.not()) {
                        true
                    } else if (i != index && selected) {
                        false
                    } else {
                        selected
                    }
                }
            }
            RadioButtonLabelled(
                enabled = true,
                selected = selected,
                contentSide = ContentSide.End,
                onClick = onClick,
            ) {
                Text(text = label)
            }
        }
    }
}

@Composable
private fun LabeledRadioButtonGroupHorizontalExample(labels: List<String>) {
    Column {
        Text(
            text = stringResource(id = R.string.component_toggle_horizontal_group_title),
            style = SparkTheme.typography.headline2,
        )
        VerticalSpacer(space = 8.dp)
        var childrenStates by remember {
            mutableStateOf(List(labels.size) { false })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            labels.forEachIndexed { index, label ->
                val selected = childrenStates.getOrElse(index) { false }
                val onClick = {
                    childrenStates = childrenStates.mapIndexed { i, selected ->
                        if (i == index && selected.not()) {
                            true
                        } else if (i != index && selected) {
                            false
                        } else {
                            selected
                        }
                    }
                }
                RadioButtonLabelled(
                    enabled = true,
                    selected = selected,
                    onClick = onClick,
                ) { Text(label) }
            }
        }
    }
}
