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
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.IntentColors
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Composable
@Suppress("MaterialComposableHasSparkReplacement") // This composable wraps the material component
@InternalSparkApi
internal fun SparkCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    intent: IntentColors = IntentColors.Support,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    TriStateCheckbox(
        state = state,
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier.sparkUsageOverlay(),
        colors = intent.toCheckboxDefaultsColors(checked = state == ToggleableState.On),
    )
}

/**
 *
 * Checkboxes allows users to select one or more items from a set. Checkboxes can turn an option on or off.
 *
 *  - Toggle a single item on or off.
 *  - Require another action to activate or deactivate something.
 *
 * ![Checkbox States](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.toggles_CheckboxDocumentationScreenshots_checkboxStates.png)
 *
 * @param state whether TriStateCheckbox is checked, unchecked or in indeterminate state
 * @param onClick callback to be invoked when checkbox is being clicked,
 * therefore the change of checked state in requested.  If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param enabled whether the component is enabled or grayed out
 * @param error whether the checkbox is in an error state
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Checkbox. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Checkbox in different [Interaction]s.
 */
@Composable
public fun Checkbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkCheckbox(
        state = state,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        intent = if (error) IntentColors.Danger else IntentColors.Support,
        interactionSource = interactionSource,
    )
}

/**
 * Spark checkbox variants.
 *
 * Call [Checkbox] directly for the bare control.
 */
public object Checkbox {

    /**
     * Checkboxes allow users to select one or more items from a set. A checkbox turns an option on or off.
     *
     *  - Toggle a single item on or off.
     *  - Require another action to activate or deactivate something.
     *
     * ![Checkbox States](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.toggles_CheckboxDocumentationScreenshots_checkboxStates.png)
     *
     * @param state whether the checkbox is checked, unchecked or indeterminate.
     * @param onClick called when the user clicks the checkbox to request a change of state.
     * When null, the checkbox is passive and a parent component owns the click and the state.
     * @param modifier the [Modifier] applied to the checkbox.
     * @param enabled whether the checkbox reacts to clicks. A disabled checkbox is dimmed.
     * @param error whether the checkbox shows the error colours.
     * @param interactionSource the [MutableInteractionSource] that emits the [Interaction]s of this checkbox.
     * Pass your own remembered instance to observe them or to change the appearance for an [Interaction].
     */
    @Composable
    public fun Standalone(
        state: ToggleableState,
        onClick: (() -> Unit)?,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        error: Boolean = false,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    ) {
        SparkCheckbox(
            state = state,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            intent = if (error) IntentColors.Danger else IntentColors.Support,
            interactionSource = interactionSource,
        )
    }

    /**
     * A card that the user clicks as a whole to check or uncheck an option.
     * The card shows a passive checkbox with its [content] and exposes [Role.Checkbox] with its [state].
     *
     * Pass `toggle = { null }` to hide the control. Then add helper text that says whether the user can pick one
     * or several options.
     *
     * ![Checkbox.Card](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_checkbox_group.png)
     *
     * @param state whether the card is checked, unchecked or indeterminate.
     * @param onClick called when the user clicks the card.
     * @param modifier the [Modifier] applied to the card.
     * @param enabled whether the card reacts to clicks. A disabled card is dimmed as a whole.
     * @param error the error message that screen readers read aloud, or null when the card has no error.
     * Use a short, specific and non-blank message, and hide any visible copy of it from accessibility services.
     * @param layout the position of the [toggle] relative to the [content].
     * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
     * @param toggle the passive control. It must end with a call on [CheckboxCardScope], such as
     * [CheckboxCardScope.checkbox] or [CheckboxCardScope.custom], or with `null`. It defaults to a checkbox that
     * follows [state] and [error].
     * @param content the content of the card.
     */
    @Composable
    public fun Card(
        state: ToggleableState,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        error: String? = null,
        layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
        interactionSource: MutableInteractionSource? = null,
        toggle: @Composable CheckboxCardScope.() -> SelectionCardItem? = { checkbox() },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        require(error == null || error.isNotBlank()) {
            "error must not be blank. Pass null when the card has no error."
        }
        SparkSelectionCard(
            state = state,
            onClick = onClick,
            style = CheckboxTokens.Card.style,
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

/**
 *
 * Checkboxes allows users to select one or more items from a set. Checkboxes can turn an option on or off.
 *
 *  - Toggle a single item on or off.
 *  - Require another action to activate or deactivate something.
 *
 * @see [SparkCheckbox] if you require support for an indeterminate state, or more advanced
 * color customization between states. Be aware that this is still an internal composable so if you need such
 * state contact the Spark team
 *
 * @param state whether TriStateCheckbox is checked, unchecked or in indeterminate state
 * @param onClick callback to be invoked when checkbox is being clicked,
 * therefore the change of checked state in requested.  If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param verticalAlignment how the checkbox widget aligns vertically when the label content is taller than the
 * checkbox, e.g. with multi-line text. Defaults to [Alignment.Top].
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Checkbox. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Checkbox in different [Interaction]s.
 * @param error whether the checkbox is in an error state
 * @param content The content displayed after the checkbox, usually a Text composable shown at the end.
 */
@Composable
public fun CheckboxLabelled(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    SparkToggleLabelledContainer(
        state = state,
        toggle = {
            Checkbox(
                modifier = it.align(
                    verticalAlignment,
                ),
                state = state,
                onClick = null,
                interactionSource = interactionSource,
                enabled = enabled,
                error = error,
            )
        },
        role = Role.Checkbox,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        content = content,
    )
}

@Preview(
    group = "Toggles",
    name = "CheckboxLabelled",
    device = Devices.FOLDABLE,
)
@Composable
private fun AllStatesCheckBoxLabelledPreview() {
    PreviewTheme {
        Row {
            CheckboxStates(isError = false)
            CheckboxStates(isError = true)
        }
    }
}

@Preview(
    group = "Toggles",
    name = "Checkbox.Card",
    device = Devices.FOLDABLE,
)
@Composable
private fun CheckboxCardPreview() {
    PreviewTheme {
        Row(horizontalArrangement = spacedBy(16.dp)) {
            CheckboxCardStates(layout = SelectionCardLayout.Horizontal, modifier = Modifier.weight(1f))
            CheckboxCardStates(layout = SelectionCardLayout.Vertical, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CheckboxCardStates(layout: SelectionCardLayout, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(16.dp),
    ) {
        Checkbox.Card(state = ToggleableState.On, onClick = {}, layout = layout) { Text("Card On") }
        Checkbox.Card(state = ToggleableState.Off, onClick = {}, layout = layout) { Text("Card Off") }
        Checkbox.Card(state = ToggleableState.Indeterminate, onClick = {}, layout = layout) {
            Text("Card Indeterminate")
        }
        Checkbox.Card(
            state = ToggleableState.Off,
            onClick = {},
            error = "Select at least one option",
            layout = layout,
        ) {
            Text("Card error")
        }
        Checkbox.Card(state = ToggleableState.On, onClick = {}, enabled = false, layout = layout) {
            Text("Card disabled")
        }
    }
}

@Composable
private fun CheckboxStates(isError: Boolean) {
    Column(
        verticalArrangement = spacedBy(16.dp),
    ) {
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.On,
            onClick = {},
            error = isError,
        ) { Text("CheckBox On") }
        CheckboxLabelled(
            enabled = false,
            state = ToggleableState.On,
            onClick = {},
            error = isError,
        ) { Text("CheckBox On") }
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.Indeterminate,
            onClick = {},
            error = isError,
        ) { Text("CheckBox Indeterminate") }
        CheckboxLabelled(
            enabled = false,
            state = ToggleableState.Indeterminate,
            onClick = {},
            error = isError,
        ) { Text("CheckBox Indeterminate") }
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.Off,
            onClick = {},
            error = isError,
        ) { Text("CheckBox Off") }
        CheckboxLabelled(
            enabled = false,
            state = ToggleableState.Off,
            onClick = {},
            error = isError,
        ) { Text("CheckBox Off") }
        CheckboxLabelled(
            enabled = true,
            state = ToggleableState.On,
            onClick = {},
            verticalAlignment = Alignment.Top,
            error = isError,
        ) { Text("CheckBox On\nSecond line of label") }
    }
}
