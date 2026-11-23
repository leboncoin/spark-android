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
@file:Suppress("DEPRECATION")

package com.adevinta.spark.components.toggles

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.IntentColors
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Suppress("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@Composable
@InternalSparkApi
internal fun SparkRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    intent: IntentColors = IntentColors.Support,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    androidx.compose.material3.RadioButton(
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        colors = intent.toRadioButtonDefaultsColors(),
        modifier = modifier.sparkUsageOverlay(),
    )
}

/**
 *
 * Radio buttons allow users to select one option from a set.
 *
 *  - Use radio buttons to select a single option from a list
 *  - It should be visible at a glance if a radio button has been selected, and selected items should be more visually
 *  prominent than unselected items.
 *  - Present a list showing all available options. If available options can be collapsed, consider using a dropdown
 *  menu because it uses less space.
 *
 * ![Radio Button States](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.toggles_RadioButtonDocumentationScreenshots_radioButtonStates.png)
 *
 * @see [SparkRadioButton] if you require color customization between states. Be aware that this is still an internal
 * composable so if you need such state contact the Spark team
 *
 * @param selected whether this radio button is selected or not
 * @param onClick callback to be invoked when the RadioButton is clicked. If null, then this RadioButton will not
 * handle input events, and only act as a visual indicator of selected state
 * @param modifier Modifier to be applied to the layout of the radiobutton
 * @param error whether the radio button is in an error state
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this RadioButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this RadioButton in different [Interaction]s.
 */
@Composable
public fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkRadioButton(
        intent = if (error) IntentColors.Danger else IntentColors.Support,
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier,
    )
}

/**
 * Spark radio button variants.
 *
 * Call [RadioButton] directly for the bare control.
 */
public object RadioButton {

    /**
     * Radio buttons allow users to select one option from a set.
     *
     *  - Use radio buttons to select a single option from a list.
     *  - Make the selected option visible at a glance and more prominent than the unselected options.
     *  - Show all the available options. When the options can be collapsed, a dropdown menu uses less space.
     *
     * ![Radio Button States](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.toggles_RadioButtonDocumentationScreenshots_radioButtonStates.png)
     *
     * @param selected whether the radio button is selected.
     * @param onClick called when the user clicks the radio button. When null, the radio button does not handle
     * input events and only shows the selected state.
     * @param modifier the [Modifier] applied to the radio button.
     * @param enabled whether the radio button reacts to clicks. A disabled radio button is dimmed.
     * @param error whether the radio button shows the error colours.
     * @param interactionSource the [MutableInteractionSource] that emits the [Interaction]s of this radio button.
     * Pass your own remembered instance to observe them or to change the appearance for an [Interaction].
     */
    @Composable
    public fun Standalone(
        selected: Boolean,
        onClick: (() -> Unit)?,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        error: Boolean = false,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    ) {
        SparkRadioButton(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            intent = if (error) IntentColors.Danger else IntentColors.Support,
            enabled = enabled,
            interactionSource = interactionSource,
        )
    }

    /**
     * A card that the user selects as one option from a set.
     * The whole card is one target with [Role.RadioButton], and it exposes the selected state.
     * Put the cards of one group in a parent with `Modifier.selectableGroup()`, so that screen readers announce
     * them as one group.
     *
     * Pass `toggle = { null }` to hide the control. Then add helper text that says whether the user can pick one
     * option or several.
     *
     * ![RadioButton.Card](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_radio_group.png)
     *
     * @param selected whether the card is selected.
     * @param onClick called when the user clicks the card.
     * @param modifier the [Modifier] applied to the card.
     * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
     * @param error the error message that screen readers read aloud, or null when the card has no error.
     * Use a short, specific and non-blank message, and hide any visible copy of it from accessibility services.
     * @param layout the position of the [toggle] relative to the [content].
     * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
     * @param toggle the passive control. It must end with a call on [RadioButtonCardScope], such as
     * [RadioButtonCardScope.radioButton] or [RadioButtonCardScope.custom], or with `null`. It defaults to a radio
     * button that follows [selected] and [error].
     * @param content the content of the card.
     */
    @Composable
    public fun Card(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        error: String? = null,
        layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
        interactionSource: MutableInteractionSource? = null,
        toggle: @Composable RadioButtonCardScope.() -> SelectionCardItem? = { radioButton() },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        require(error == null || error.isNotBlank()) {
            "error must not be blank. Pass null when the card has no error."
        }
        SparkSelectionCard(
            selected = selected,
            onClick = onClick,
            style = RadioButtonTokens.Card.style,
            modifier = modifier,
            enabled = enabled,
            error = error,
            layout = layout,
            interactionSource = interactionSource,
            toggle = toggle,
            content = content,
        )
    }
}

@Preview(group = "Toggles", name = "RadioButton Card")
@Composable
private fun RadioButtonCardPreview() {
    PreviewTheme {
        SelectionCardLayout.entries.forEach { layout ->
            Column(modifier = Modifier.selectableGroup()) {
                RadioButton.Card(selected = true, onClick = {}, layout = layout) { Text("Selected") }
                RadioButton.Card(selected = false, onClick = {}, layout = layout) { Text("Unselected") }
                RadioButton.Card(
                    selected = true,
                    onClick = {},
                    error = "Select another option",
                    layout = layout,
                ) { Text("Error") }
                RadioButton.Card(selected = false, onClick = {}, enabled = false, layout = layout) {
                    Text("Disabled")
                }
            }
        }
    }
}

/**
 *
 * Radio buttons allow users to select one option from a set.
 *
 *  - Use radio buttons to select a single option from a list
 *  - It should be visible at a glance if a radio button has been selected, and selected items should be more visually
 *  prominent than unselected items.
 *  - Present a list showing all available options. If available options can be collapsed, consider using a dropdown
 *  menu because it uses less space.
 *
 * @see [SparkRadioButton] if you require color customization between states. Be aware that this is still an internal
 * composable so if you need such state contact the Spark team
 *
 * @param selected whether this radio button is selected or not
 * @param onClick callback to be invoked when the RadioButton is clicked. If null, then this RadioButton will not
 * handle input events, and only act as a visual indicator of selected state
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param verticalAlignment how the radio button widget aligns vertically when the label content is taller than the
 * radio button, e.g. with multi-line text. Defaults to [Alignment.Top].
 * @param error whether the radio button is in an error state
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this RadioButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this RadioButton in different [Interaction]s.
 * @param content The content displayed after the radio button, usually a Text composable shown at the end.
 */
@Composable
public fun RadioButtonLabelled(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    SparkToggleLabelledContainer(
        state = ToggleableState(selected),
        toggle = {
            RadioButton(
                modifier = it.align(
                    verticalAlignment,
                ),
                selected = selected,
                onClick = null,
                enabled = enabled,
                error = error,
                interactionSource = interactionSource,
            )
        },
        role = Role.RadioButton,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content,
    )
}

@Preview(
    group = "Toggles",
    name = "RadioButtonLabelled",
    device = Devices.FOLDABLE,
)
@Composable
private fun PreviewAllStatesRadioButtonLabelled() {
    PreviewTheme {
        RadioButtonStates(isError = false)
        RadioButtonStates(isError = true)
    }
}

@Composable
private fun ColumnScope.RadioButtonStates(isError: Boolean) {
    RadioButtonLabelled(
        enabled = true,
        selected = true,
        onClick = {},
        error = isError,
    ) { Text("RadioButton On") }
    RadioButtonLabelled(
        enabled = false,
        selected = true,
        onClick = {},
        error = isError,
    ) { Text("RadioButton On") }
    RadioButtonLabelled(
        enabled = true,
        selected = false,
        onClick = {},
        error = isError,
    ) { Text("RadioButton Off") }
    RadioButtonLabelled(
        enabled = false,
        selected = false,
        onClick = {},
        error = isError,
    ) { Text("RadioButton Off") }
    RadioButtonLabelled(
        enabled = true,
        selected = true,
        onClick = {},
        verticalAlignment = Alignment.Top,
        error = isError,
    ) { Text("RadioButton On\nSecond line of label") }
}
