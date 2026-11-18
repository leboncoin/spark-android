/*
 * Copyright (c) 2026 Adevinta
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.image.Image
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.Checkbox
import com.adevinta.spark.components.toggles.RadioButton
import com.adevinta.spark.components.toggles.SelectionCardLayout
import com.adevinta.spark.components.toggles.Switch
import com.adevinta.spark.icons.Building
import com.adevinta.spark.icons.House
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.PaperMapFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val SelectionCardExampleDescription = "Selection card examples"
private const val SelectionCardExampleSourceUrl = "$SampleSourceUrl/SelectionCardSamples.kt"

internal val SelectionCardExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "horizontal-radio-group",
        name = "Horizontal radio card group",
        description = SelectionCardExampleDescription,
        sourceUrl = SelectionCardExampleSourceUrl,
    ) {
        HorizontalRadioGroupExample()
    },
    Example(
        id = "vertical-checkbox-group",
        name = "Vertical checkbox card group",
        description = SelectionCardExampleDescription,
        sourceUrl = SelectionCardExampleSourceUrl,
    ) {
        VerticalCheckboxGroupExample()
    },
    Example(
        id = "switch-list",
        name = "Switch card list",
        description = SelectionCardExampleDescription,
        sourceUrl = SelectionCardExampleSourceUrl,
    ) {
        SwitchListExample()
    },
    Example(
        id = "image-content",
        name = "Image in the content",
        description = SelectionCardExampleDescription,
        sourceUrl = SelectionCardExampleSourceUrl,
    ) {
        ImageContentExample()
    },
    Example(
        id = "empty-toggle",
        name = "Card without a toggle",
        description = SelectionCardExampleDescription,
        sourceUrl = SelectionCardExampleSourceUrl,
    ) {
        EmptyToggleExample()
    },
)

private data class CardOption(@StringRes val title: Int, @StringRes val helper: Int)

private data class IconOption(val icon: SparkIcon, @StringRes val label: Int)

@Composable
private fun HorizontalRadioGroupExample() {
    val options = remember {
        persistentListOf(
            CardOption(
                title = R.string.spark_catalog_component_selection_card_example_delivery_hand_title,
                helper = R.string.spark_catalog_component_selection_card_example_delivery_hand_helper,
            ),
            CardOption(
                title = R.string.spark_catalog_component_selection_card_example_delivery_parcel_title,
                helper = R.string.spark_catalog_component_selection_card_example_delivery_parcel_helper,
            ),
            CardOption(
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

@Composable
private fun VerticalCheckboxGroupExample() {
    val options = remember {
        persistentListOf(
            IconOption(LeboncoinIcons.House, R.string.spark_catalog_component_selection_card_example_property_house),
            IconOption(LeboncoinIcons.Building, R.string.spark_catalog_component_selection_card_example_property_flat),
            IconOption(
                LeboncoinIcons.PaperMapFill,
                R.string.spark_catalog_component_selection_card_example_property_land,
            ),
        )
    }
    var states by remember { mutableStateOf(List(options.size) { ToggleableState.Off }) }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { index, option ->
            Checkbox.Card(
                state = states[index],
                onClick = {
                    states = states.mapIndexed { i, state ->
                        if (i == index) ToggleableState(state != ToggleableState.On) else state
                    }
                },
                modifier = Modifier.weight(1f),
                layout = SelectionCardLayout.Vertical,
            ) {
                // The card label already names the option, so the icon is decorative.
                Icon(sparkIcon = option.icon, contentDescription = null, size = IconSize.Large)
                Text(
                    text = stringResource(option.label),
                    style = SparkTheme.typography.body1.highlight,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SwitchListExample() {
    val options = remember {
        persistentListOf(
            CardOption(
                title = R.string.spark_catalog_component_selection_card_example_notification_push_title,
                helper = R.string.spark_catalog_component_selection_card_example_notification_push_helper,
            ),
            CardOption(
                title = R.string.spark_catalog_component_selection_card_example_notification_email_title,
                helper = R.string.spark_catalog_component_selection_card_example_notification_email_helper,
            ),
            CardOption(
                title = R.string.spark_catalog_component_selection_card_example_notification_sms_title,
                helper = R.string.spark_catalog_component_selection_card_example_notification_sms_helper,
            ),
        )
    }
    var checkedStates by remember { mutableStateOf(List(options.size) { it == 0 }) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { index, option ->
            Switch.Card(
                checked = checkedStates[index],
                onCheckedChange = { checked ->
                    checkedStates = checkedStates.mapIndexed { i, state -> if (i == index) checked else state }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                CardTexts(title = stringResource(option.title), helper = stringResource(option.helper))
            }
        }
    }
}

@Composable
private fun ImageContentExample() {
    var state by remember { mutableStateOf(ToggleableState.Off) }
    Checkbox.Card(
        state = state,
        onClick = { state = ToggleableState(state != ToggleableState.On) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        // The title next to the image describes the option, so the image is decorative.
        Image(
            model = R.drawable.spark_catalog_img_wide_image_configurator,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(SparkTheme.shapes.small),
            contentScale = ContentScale.Crop,
        )
        CardTexts(
            title = stringResource(R.string.spark_catalog_component_selection_card_example_boost_title),
            helper = stringResource(R.string.spark_catalog_component_selection_card_example_boost_helper),
        )
    }
}

@Composable
private fun EmptyToggleExample() {
    val options = remember {
        persistentListOf(
            CardOption(
                title = R.string.spark_catalog_component_selection_card_example_plan_standard_title,
                helper = R.string.spark_catalog_component_selection_card_example_plan_standard_helper,
            ),
            CardOption(
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
                toggle = {},
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
private fun SelectionCardExamplesPreview() {
    PreviewTheme {
        HorizontalRadioGroupExample()
        VerticalCheckboxGroupExample()
        SwitchListExample()
        ImageContentExample()
        EmptyToggleExample()
    }
}
