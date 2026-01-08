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
package com.adevinta.spark.components.textfields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.components.chips.Chip
import com.adevinta.spark.components.chips.ChipDefaults
import com.adevinta.spark.components.chips.ChipIntent
import com.adevinta.spark.components.chips.ChipStyles
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.menu.MultiChoiceDropdownItemColumnScope
import com.adevinta.spark.components.menu.MultipleChoiceExposedDropdownMenu
import com.adevinta.spark.components.menu.SingleChoiceDropdownItemColumnScope
import com.adevinta.spark.components.menu.SingleChoiceExposedDropdownMenu
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.DeleteOutline
import com.adevinta.spark.icons.SparkIcons
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * A ComboBox component that allows users to select a single option from a dropdown list.
 * It combines a text field with a dropdown menu, providing a searchable interface for selecting options.
 *
 * @param state The state of the text field that controls the input value
 * @param expanded Whether the dropdown menu is currently expanded
 * @param onExpandedChange Callback invoked when the expanded state changes
 * @param onDismissRequest Callback invoked when the dropdown menu should be dismissed
 * @param modifier Modifier to be applied to the ComboBox
 * @param enabled Whether the ComboBox is enabled
 * @param required Whether the field is required
 * @param label Optional label text displayed above the ComboBox
 * @param placeholder Optional placeholder text shown when the field is empty
 * @param helper Optional helper text displayed below the ComboBox
 * @param leadingContent Optional composable content displayed at the start of the ComboBox
 * @param trailingContent Optional composable content displayed at the end of the ComboBox, if not provided, a default
 * trailing icon will be used.
 * @param status Optional status of the form field (e.g., error, success)
 * @param statusMessage Optional message associated with the status
 * @param inputTransformation Optional transformation applied to input text
 * @param keyboardOptions Options for the keyboard
 * @param onKeyboardAction Optional handler for keyboard actions
 * @param lineLimits Limits for the number of lines in the text field
 * @param onTextLayout Optional callback for text layout changes
 * @param interactionSource Optional interaction source for the ComboBox
 * @param outputTransformation Optional transformation applied to output text
 * @param scrollState Scroll state for the text field
 * @param dropdownContent Content of the dropdown menu, using [SingleChoiceDropdownItemColumnScope]
 * @see MultiChoiceComboBox for a version that supports multiple selections
 * @sample com.adevinta.spark.components.textfields.ComboBoxPreview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SingleChoiceComboBox(
    state: TextFieldState,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    trailingContent: @Composable (AddonScope.() -> Unit)? = null,
    status: FormFieldStatus? = null,
    statusMessage: String? = null,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    dropdownContent: @Composable SingleChoiceDropdownItemColumnScope.() -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            onExpandedChange(it)
        },
    ) {
        val trailingContentDefault = trailingContent ?: {
            SparkSelectTrailingIcon(
                expanded = expanded,
                modifier = Modifier.menuAnchor(
                    ExposedDropdownMenuAnchorType.SecondaryEditable,
                    enabled = enabled,
                ),
                onClick = { onExpandedChange(!expanded) },
            )
        }
        TextField(
            state = state,
            modifier = modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = enabled),
            enabled = enabled,
            readOnly = false,
            required = required,
            label = label,
            placeholder = placeholder,
            helper = helper,
            leadingContent = leadingContent,
            trailingContent = trailingContentDefault,
            status = status,
            statusMessage = statusMessage,
            inputTransformation = inputTransformation,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            outputTransformation = outputTransformation,
            scrollState = scrollState,
        )
        SingleChoiceExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .exposedDropdownSize()
                .padding(16.dp),
            content = dropdownContent,
        )
    }
}

/**
 * Represents a selected choice in a [MultiChoiceComboBox].
 * These are represented as chips below the text field.
 * @property id The unique identifier for the choice
 * @property label The human-readable label for the choice
 * @see MultiChoiceComboBox for usage examples
 */
@Immutable
public data class SelectedChoice(val id: String, val label: String)

/**
 * A ComboBox component that allows users to select multiple options from a dropdown list.
 * It combines a text field with a dropdown menu, providing a searchable interface for selecting options.
 * The selected choices are displayed as chips below the text field.
 *
 * @param state The state of the text field that controls the input value
 * @param expanded Whether the dropdown menu is currently expanded
 * @param onExpandedChange Callback invoked when the expanded state changes
 * @param onDismissRequest Callback invoked when the dropdown menu should be dismissed
 * @param selectedChoices The list of currently selected choices
 * @param onSelectedClick Callback invoked when a selected choice chip is clicked, typically to remove it
 * @param modifier Modifier to be applied to the ComboBox
 * @param enabled Whether the ComboBox is enabled
 * @param required Whether the field is required
 * @param label Optional label text displayed above the ComboBox
 * @param placeholder Optional placeholder text shown when the field is empty
 * @param helper Optional helper text displayed below the ComboBox
 * @param leadingContent Optional composable content displayed at the start of the ComboBox
 * @param trailingContent Optional composable content displayed at the end of the ComboBox, if not provided, a default
 * trailing icon will be used.
 * @param status Optional status of the form field (e.g., error, success)
 * @param statusMessage Optional message associated with the status
 * @param inputTransformation Optional transformation applied to input text
 * @param keyboardOptions Options for the keyboard
 * @param onKeyboardAction Optional handler for keyboard actions
 * @param lineLimits Limits for the number of lines in the text field
 * @param onTextLayout Optional callback for text layout changes
 * @param interactionSource Optional interaction source for the ComboBox
 * @param outputTransformation Optional transformation applied to output text
 * @param scrollState Scroll state for the text field
 * @param dropdownContent Content of the dropdown menu, using [MultiChoiceDropdownItemColumnScope]
 * @see SingleChoiceComboBox for a version that supports single selection
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
public fun MultiChoiceComboBox(
    state: TextFieldState,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    selectedChoices: ImmutableList<SelectedChoice>,
    onSelectedClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    trailingContent: @Composable (AddonScope.() -> Unit)? = null,
    status: FormFieldStatus? = null,
    statusMessage: String? = null,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    dropdownContent: @Composable MultiChoiceDropdownItemColumnScope.() -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                onExpandedChange(it)
            },
        ) {
            val trailingContentDefault = trailingContent ?: {
                SparkSelectTrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.menuAnchor(
                        ExposedDropdownMenuAnchorType.SecondaryEditable,
                        enabled = enabled,
                    ),
                    onClick = { onExpandedChange(!expanded) },
                )
            }
            TextField(
                state = state,
                modifier = modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = enabled),
                enabled = enabled,
                readOnly = false,
                required = required,
                label = label,
                placeholder = placeholder,
                helper = helper,
                leadingContent = leadingContent,
                trailingContent = trailingContentDefault,
                status = status,
                statusMessage = statusMessage,
                inputTransformation = inputTransformation,
                keyboardOptions = keyboardOptions,
                onKeyboardAction = onKeyboardAction,
                onTextLayout = onTextLayout,
                interactionSource = interactionSource,
                outputTransformation = outputTransformation,
                scrollState = scrollState,
            )
            MultipleChoiceExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = Modifier
                    .exposedDropdownSize(),
                content = dropdownContent,
            )
        }
        AnimatedVisibility(selectedChoices.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = spacedBy(4.dp),
            ) {
                selectedChoices.fastForEach { (id, label) ->
                    key(id) {
                        val clickLabel =
                            stringResource(R.string.spark_combobox_multiple_chip_click_a11y, label)
                        Chip(
                            modifier = Modifier.semantics {
                                onClick(label = clickLabel, action = null)
                            },
                            style = ChipStyles.Tinted,
                            intent = ChipIntent.Neutral,
                            onClick = { onSelectedClick(id) },
                            trailingIcon = {
                                Icon(
                                    sparkIcon = SparkIcons.DeleteOutline,
                                    modifier = Modifier.size(ChipDefaults.LeadingIconSize),
                                    contentDescription = null,
                                )
                            },
                            content = {
                                Text(label)
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A ComboBox component that allows users to select multiple options from a dropdown list.
 * It combines a text field with a dropdown menu, providing a searchable interface for selecting options.
 * This overload does not manage selected choices directly, and is intended for use cases where
 * selection state is handled externally or not needed.
 *
 * @param state The state of the text field that controls the input value
 * @param expanded Whether the dropdown menu is currently expanded
 * @param onExpandedChange Callback invoked when the expanded state changes
 * @param onDismissRequest Callback invoked when the dropdown menu should be dismissed
 * @param modifier Modifier to be applied to the ComboBox
 * @param enabled Whether the ComboBox is enabled
 * @param required Whether the field is required
 * @param label Optional label text displayed above the ComboBox
 * @param placeholder Optional placeholder text shown when the field is empty
 * @param helper Optional helper text displayed below the ComboBox
 * @param leadingContent Optional composable content displayed at the start of the ComboBox
 * @param trailingContent Optional composable content displayed at the end of the ComboBox, if not provided, a default
 * trailing icon will be used.
 * @param status Optional status of the form field (e.g., error, success)
 * @param statusMessage Optional message associated with the status
 * @param inputTransformation Optional transformation applied to input text
 * @param keyboardOptions Options for the keyboard
 * @param onKeyboardAction Optional handler for keyboard actions
 * @param lineLimits Limits for the number of lines in the text field
 * @param onTextLayout Optional callback for text layout changes
 * @param interactionSource Optional interaction source for the ComboBox
 * @param outputTransformation Optional transformation applied to output text
 * @param scrollState Scroll state for the text field
 * @param dropdownContent Content of the dropdown menu, using [MultiChoiceDropdownItemColumnScope]
 * @see SingleChoiceComboBox for a version that supports single selection
 * @see MultiChoiceComboBox for the version that manages selected choices
 */
@Composable
public fun MultiChoiceComboBox(
    state: TextFieldState,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    trailingContent: @Composable (AddonScope.() -> Unit)? = null,
    status: FormFieldStatus? = null,
    statusMessage: String? = null,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    dropdownContent: @Composable MultiChoiceDropdownItemColumnScope.() -> Unit,
) {
    MultiChoiceComboBox(
        state = state,
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        onDismissRequest = onDismissRequest,
        selectedChoices = persistentListOf(),
        onSelectedClick = {},
        modifier = modifier,
        enabled = enabled,
        required = required,
        label = label,
        placeholder = placeholder,
        helper = helper,
        leadingContent = leadingContent,
        status = status,
        statusMessage = statusMessage,
        inputTransformation = inputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        outputTransformation = outputTransformation,
        scrollState = scrollState,
        dropdownContent = dropdownContent,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ComboBoxPreview() {
    PreviewTheme {
        val state = rememberTextFieldState()
        var expanded by remember { mutableStateOf(false) }
        SingleChoiceComboBox(
            state = state,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            onDismissRequest = { expanded = false },
            enabled = true,
            required = true,
            label = "Label",
            placeholder = "Placeholder",
            helper = "Helper text",
        ) {
            for (i in 0..(8)) {
                DropdownMenuItem(
                    text = { Text(text = "Item $i") },
                    selected = false,
                    onClick = { expanded = false },
                )
            }
        }
    }
}
