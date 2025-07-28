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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.toggles.Checkbox
import com.adevinta.spark.components.toggles.CheckboxLabelled

private const val CheckboxExampleDescription = "Checkbox examples"
private const val CheckboxExampleSourceUrl = "$SampleSourceUrl/CheckboxSamples.kt"
public val CheckboxExamples: List<Example> = listOf(
    Example(
        id = "standalone",
        name = "Standalone checkbox",
        description = CheckboxExampleDescription,
        sourceUrl = CheckboxExampleSourceUrl,
    ) {
        var checkboxState by remember { mutableStateOf(ToggleableState.Off) }
        Column {
            Checkbox(
                enabled = true,
                state = checkboxState,
                onClick = {
                    checkboxState = when (checkboxState) {
                        ToggleableState.On -> ToggleableState.Off
                        ToggleableState.Off -> ToggleableState.Indeterminate
                        ToggleableState.Indeterminate -> ToggleableState.On
                    }
                },
            )
            Checkbox(enabled = false, state = checkboxState, onClick = {})
        }
    },
    Example(
        id = "labeled",
        name = "Labeled checkbox group",
        description = CheckboxExampleDescription,
        sourceUrl = CheckboxExampleSourceUrl,
    ) {
        LabeledCheckboxGroupExample()
    },
)

@Composable
private fun LabeledCheckboxGroupExample() {
    val labels = listOf(
        stringResource(id = R.string.component_checkbox_group_example_option1_label),
        stringResource(id = R.string.component_checkbox_group_example_option2_label),
    )
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        LabeledCheckboxGroupHorizontalExample(labels)
        LabeledCheckboxGroupVerticalExample(labels)
    }
}

@Composable
private fun LabeledCheckboxGroupVerticalExample(labels: List<String>) {
    var childrenStates by remember {
        mutableStateOf(List(labels.size) { ToggleableState.Off })
    }
    val groupState by remember {
        derivedStateOf {
            when {
                childrenStates.all { it == ToggleableState.On } -> ToggleableState.On

                childrenStates.all { it == ToggleableState.Off } ->
                    ToggleableState.Off

                else -> ToggleableState.Indeterminate
            }
        }
    }

    Column(modifier = Modifier.selectableGroup()) {
        Text(
            text = stringResource(id = R.string.component_toggle_vertical_group_title),
            style = SparkTheme.typography.headline2,
        )
        VerticalSpacer(space = 8.dp)
        CheckboxLabelled(
            enabled = true,
            state = groupState,
            onClick = {
                childrenStates = childrenStates.map {
                    when (groupState) {
                        ToggleableState.Indeterminate, ToggleableState.Off -> ToggleableState.On
                        ToggleableState.On -> ToggleableState.Off
                    }
                }
            },
        ) { Text(text = stringResource(id = R.string.component_checkbox_group_example_group_label)) }

        Column(
            modifier = Modifier.padding(start = 32.dp),
        ) {
            labels.forEachIndexed { index, label ->
                val checkboxState = childrenStates.getOrElse(index) { ToggleableState.Off }
                CheckboxLabelled(
                    enabled = true,
                    state = checkboxState,
                    onClick = {
                        childrenStates = childrenStates.mapIndexed { i, state ->
                            if (i == index) {
                                when (state) {
                                    ToggleableState.Indeterminate, ToggleableState.Off -> ToggleableState.On
                                    ToggleableState.On -> ToggleableState.Off
                                }
                            } else {
                                state
                            }
                        }
                    },
                ) { Text(text = label) }
            }
        }
    }
}

@Composable
private fun LabeledCheckboxGroupHorizontalExample(labels: List<String>) {
    Column {
        Text(
            text = stringResource(id = R.string.component_toggle_horizontal_group_title),
            style = SparkTheme.typography.headline2,
        )
        VerticalSpacer(space = 8.dp)
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            labels.forEach { label ->
                var checkboxState by remember { mutableStateOf(ToggleableState.Off) }
                CheckboxLabelled(
                    enabled = true,
                    state = checkboxState,
                    onClick = {
                        checkboxState = when (checkboxState) {
                            ToggleableState.Indeterminate, ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Off
                        }
                    },
                ) { Text(label) }
            }
        }
    }
}
