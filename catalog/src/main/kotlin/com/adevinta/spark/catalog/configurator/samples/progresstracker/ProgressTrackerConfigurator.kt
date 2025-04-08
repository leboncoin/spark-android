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
package com.adevinta.spark.catalog.configurator.samples.progresstracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.card.ElevatedCard
import com.adevinta.spark.components.progress.tracker.ProgressSizes
import com.adevinta.spark.components.progress.tracker.ProgressStep
import com.adevinta.spark.components.progress.tracker.ProgressStyles
import com.adevinta.spark.components.progress.tracker.ProgressTrackerColumn
import com.adevinta.spark.components.progress.tracker.ProgressTrackerIntent
import com.adevinta.spark.components.progress.tracker.ProgressTrackerRow
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.Switch
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.tokens.highlight
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

public val ProgressTrackerConfigurator: Configurator = Configurator(
    id = "progress-tracker",
    name = "Progress Tracker",
    description = "Progress Tracker configuration",
    sourceUrl = "$SampleSourceUrl/ProgressTrackerSamples.kt",
) {
    ProgressTrackerSample()
}

@Composable
private fun ColumnScope.ProgressTrackerSample() {
    var intent by remember { mutableStateOf(ProgressTrackerIntent.Basic) }
    var size by remember { mutableStateOf(ProgressSizes.Large) }
    var style by remember { mutableStateOf(ProgressStyles.Outlined) }
    var hasIndicatorContent by remember { mutableStateOf(true) }
    var selectedStep by remember { mutableIntStateOf(1) }
    var items by remember {
        mutableStateOf(
            persistentListOf(
                ProgressStep("Lorem ipsume", true),
                ProgressStep("Lorem ipsume dolar sit amet", true),
                ProgressStep("Lorem ipsume", false),
            ),
        )
    }

    Text(
        text = "Horizontal Progress Tracker",
        style = SparkTheme.typography.headline2,
    )

    ProgressTrackerRow(
        items = items,
        size = size,
        intent = intent,
        style = style,
        hasIndicatorContent = hasIndicatorContent,
        onStepClick = {
            selectedStep = it
        },
        selectedStep = selectedStep,
    )

    Text(
        text = "Vertical Progress Tracker",
        style = SparkTheme.typography.headline2,
    )

    ProgressTrackerColumn(
        items = items,
        size = size,
        intent = intent,
        style = style,
        hasIndicatorContent = hasIndicatorContent,
        onStepClick = {
            selectedStep = it
        },
        selectedStep = selectedStep,
    )

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.configurator_component_screen_intent_label),
        selectedOption = intent,
        onOptionSelect = {
            intent = it
        },
    )

    SwitchLabelled(
        checked = hasIndicatorContent,
        onCheckedChange = { hasIndicatorContent = it },
    ) {
        Text(
            text = "Indicator content",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    ButtonGroup(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
    )

    ButtonGroup(
        title = "Sizes",
        selectedOption = size,
        onOptionSelect = { size = it },
    )

    Column {
        Text(
            text = "Steps",
            modifier = Modifier.padding(bottom = 8.dp),
            style = SparkTheme.typography.body2.highlight,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ButtonTinted(
                text = "Remove Step",
                intent = ButtonIntent.Danger,
                onClick = {
                    if (items.size > 2) {
                        items = items.dropLast(1).toPersistentList()
                    }
                },
                modifier = Modifier.weight(1f),
            )
            ButtonTinted(
                text = "Add Step",
                onClick = {
                    if (items.size < 6) {
                        items = items.add(ProgressStep("New Step", true))
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }
    }

    items.forEachIndexed { index, progressStep ->
        ElevatedCard {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextField(
                    modifier = Modifier.weight(1f, false),
                    value = progressStep.label.toString(),
                    label = "Step $index",
                    onValueChange = {
                        items = items.set(index, progressStep.copy(label = it))
                    },
                )
                Switch(
                    checked = progressStep.enabled,
                    onCheckedChange = {
                        items = items.set(
                            index,
                            progressStep.copy(enabled = it),
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProgressTrackerSamplePreview() {
    PreviewTheme { ProgressTrackerSample() }
}
