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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.tools.modifiers.minimumTouchTargetSize
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Composable
@InternalSparkApi
internal fun SparkRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    intent: ToggleIntent = ToggleIntent.Basic,
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
 * @see [SparkRadioButton] if you require color customization between states. Be aware that this is still an internal
 * composable so if you need such state contact the Spark team
 *
 * @param selected whether this radio button is selected or not
 * @param onClick callback to be invoked when the RadioButton is clicked. If null, then this RadioButton will not
 * handle input events, and only act as a visual indicator of selected state
 * @param modifier Modifier to be applied to the layout of the radiobutton
 * @param intent The [ToggleIntent] to use to draw the radio button
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this RadioButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this RadioButton in different [Interaction]s.
 */
@Deprecated(
    message = "Intent usages for RadioButton as been deprecated in favour of just an error parameter",
    replaceWith = ReplaceWith("RadioButton(selected = selected, onClick = onClick, modifier = modifier, enabled = enabled, error = false, interactionSource = interactionSource)"),
)
@Composable
public fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    intent: ToggleIntent, // no default values to not break the api
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkRadioButton(
        intent = intent,
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier,
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
        intent = if (error) ToggleIntent.Danger else ToggleIntent.Basic,
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier,
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
 * @see [SparkRadioButton] if you require color customization between states. Be aware that this is still an internal
 * composable so if you need such state contact the Spark team
 *
 * @param selected whether this radio button is selected or not
 * @param onClick callback to be invoked when the RadioButton is clicked. If null, then this RadioButton will not
 * handle input events, and only act as a visual indicator of selected state
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param intent The [ToggleIntent] to use to draw the radio button
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this RadioButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this RadioButton in different [Interaction]s.
 * @param contentSide The side where we want to show the label, default to [ContentSide.End].
 * @param content The content displayed after the radio button, usually a Text composable shown at the end.
 */
@Deprecated(
    message = "Intent usages for RadioButtonLabelled as been deprecated in favour of just an error parameter",
    replaceWith = ReplaceWith("RadioButtonLabelled(selected = selected, onClick = onClick, modifier = modifier, enabled = enabled, error = false, interactionSource = interactionSource)"),
)
@Composable
public fun RadioButtonLabelled(
    selected: Boolean,
    intent: ToggleIntent, // no default values to not break the api
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentSide: ContentSide = ContentSide.End,
    content: @Composable RowScope.() -> Unit,
) {
    SparkToggleLabelledContainer(
        state = ToggleableState(selected),
        toggle = {
            RadioButton(
                modifier = it.minimumTouchTargetSize(),
                intent = intent,
                selected = selected,
                onClick = null,
                interactionSource = interactionSource,
                enabled = enabled,
            )
        },
        role = Role.RadioButton,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentSide = contentSide,
        content = content,
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
 * @see [SparkRadioButton] if you require color customization between states. Be aware that this is still an internal
 * composable so if you need such state contact the Spark team
 *
 * @param selected whether this radio button is selected or not
 * @param onClick callback to be invoked when the RadioButton is clicked. If null, then this RadioButton will not
 * handle input events, and only act as a visual indicator of selected state
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param error whether the radio button is in an error state
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this RadioButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this RadioButton in different [Interaction]s.
 * @param contentSide The side where we want to show the label, default to [ContentSide.End].
 * @param content The content displayed after the radio button, usually a Text composable shown at the end.
 */
@Composable
public fun RadioButtonLabelled(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentSide: ContentSide = ContentSide.End,
    content: @Composable RowScope.() -> Unit,
) {
    SparkToggleLabelledContainer(
        state = ToggleableState(selected),
        toggle = {
            RadioButton(
                modifier = it.minimumTouchTargetSize(),
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
        contentSide = contentSide,
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
}
