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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.RadioButton
import com.adevinta.spark.components.toggles.RadioButtonLabelled
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val RadioButtonExampleDescription = "RadioButton examples"
private const val RadioButtonExampleSourceUrl = "$SampleSourceUrl/RadioButtonSamples.kt"
private const val RadioButtonCardExampleSourceUrl = "$SampleSourceUrl/toggles/RadioButtonExamples.kt"
internal val RadioButtonExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "standalone",
        name = "Standalone radio button",
        description = RadioButtonExampleDescription,
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
        description = RadioButtonExampleDescription,
        sourceUrl = RadioButtonExampleSourceUrl,
    ) {
        RadioButtonContentSideExample()
    },
    Example(
        id = "group",
        name = "Labeled radio button group",
        description = RadioButtonExampleDescription,
        sourceUrl = RadioButtonExampleSourceUrl,
    ) {
        LabeledRadioButtonGroupExample()
    },
    Example(
        id = "horizontal-radio-button-card-group",
        name = "Horizontal radio button card group",
        description = RadioButtonExampleDescription,
        sourceUrl = RadioButtonCardExampleSourceUrl,
    ) {
        HorizontalRadioButtonCardGroupExample()
    },
    Example(
        id = "radio-button-card-without-toggle",
        name = "Radio button card without a toggle",
        description = RadioButtonExampleDescription,
        sourceUrl = RadioButtonCardExampleSourceUrl,
    ) {
        RadioButtonCardWithoutToggleExample()
    },
)

private data class RadioButtonCardOption(@StringRes val title: Int, @StringRes val helper: Int)

/** A single choice between delivery options, with radio button cards in the horizontal layout. */
@Composable
private fun HorizontalRadioButtonCardGroupExample() {
    val options = remember {
        persistentListOf(
            RadioButtonCardOption(
                title = R.string.spark_catalog_component_selection_card_example_delivery_hand_title,
                helper = R.string.spark_catalog_component_selection_card_example_delivery_hand_helper,
            ),
            RadioButtonCardOption(
                title = R.string.spark_catalog_component_selection_card_example_delivery_parcel_title,
                helper = R.string.spark_catalog_component_selection_card_example_delivery_parcel_helper,
            ),
            RadioButtonCardOption(
                title = R.string.spark_catalog_component_selection_card_example_delivery_pickup_title,
                helper = R.string.spark_catalog_component_selection_card_example_delivery_pickup_helper,
            ),
        )
    }
    var selectedIndex by remember { mutableIntStateOf(0) }
    // selectableGroup() lets TalkBack announce the cards as one group of choices.
    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEachIndexed { index, option ->
            RadioButton.Card(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                modifier = Modifier.fillMaxWidth(),
            ) {
                CardTexts(title = stringResource(option.title), helper = stringResource(option.helper))
            }
        }
    }
}

/** A single choice between plans, with radio button cards that pass `toggle = { null }` to hide the toggle. */
@Composable
private fun RadioButtonCardWithoutToggleExample() {
    val options = remember {
        persistentListOf(
            RadioButtonCardOption(
                title = R.string.spark_catalog_component_selection_card_example_plan_standard_title,
                helper = R.string.spark_catalog_component_selection_card_example_plan_standard_helper,
            ),
            RadioButtonCardOption(
                title = R.string.spark_catalog_component_selection_card_example_plan_urgent_title,
                helper = R.string.spark_catalog_component_selection_card_example_plan_urgent_helper,
            ),
        )
    }
    var selectedIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEachIndexed { index, option ->
            // Without a toggle, the border alone shows the selection, so the helper text must explain the option.
            RadioButton.Card(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                modifier = Modifier.fillMaxWidth(),
                toggle = { null },
            ) {
                CardTexts(title = stringResource(option.title), helper = stringResource(option.helper))
            }
        }
    }
}

@Composable
private fun CardTexts(title: String, helper: String) {
    Text(text = title, style = SparkTheme.typography.body1.highlight)
    Text(
        text = helper,
        style = SparkTheme.typography.body2,
        color = SparkTheme.colors.onSurface.dim1,
    )
}

@Preview
@Composable
private fun RadioButtonCardExamplesPreview() {
    PreviewTheme {
        HorizontalRadioButtonCardGroupExample()
        RadioButtonCardWithoutToggleExample()
    }
}

@Composable
private fun RadioButtonContentSideExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val label = stringResource(id = R.string.spark_catalog_component_checkbox_content_side_example_label)
        var selected by remember { mutableStateOf(false) }
        RadioButtonLabelled(
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            selected = selected,
            onClick = { selected = !selected },
        ) {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun LabeledRadioButtonGroupExample() {
    val labels = listOf(
        stringResource(id = R.string.spark_catalog_component_checkbox_group_example_option1_label),
        stringResource(id = R.string.spark_catalog_component_checkbox_group_example_option2_label),
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
            text = stringResource(id = R.string.spark_catalog_component_toggle_vertical_group_title),
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
            text = stringResource(id = R.string.spark_catalog_component_toggle_horizontal_group_title),
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
