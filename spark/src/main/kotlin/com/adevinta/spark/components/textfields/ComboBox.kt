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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SingleChoiceComboBox(
    value: String,
    onValueChange: (String) -> Unit,
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
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    dropdownContent: @Composable SingleChoiceDropdownItemColumnScope.() -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            onExpandedChange(it)
        },
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = enabled),
            enabled = enabled,
            readOnly = false,
            required = required,
            label = label,
            placeholder = placeholder,
            helper = helper,
            leadingContent = leadingContent,
            trailingContent = {
                SparkSelectTrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable, enabled = enabled),
                )
            },
            state = state,
            stateMessage = stateMessage,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
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

@Immutable
public data class SelectedChoice(val id: String, val label: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
public fun MultiChoiceComboBox(
    value: String,
    onValueChange: (String) -> Unit,
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
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    dropdownContent: @Composable MultiChoiceDropdownItemColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                onExpandedChange(it)
            },
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = enabled),
                enabled = enabled,
                readOnly = false,
                required = required,
                label = label,
                placeholder = placeholder,
                helper = helper,
                leadingContent = leadingContent,
                trailingContent = {
                    SparkSelectTrailingIcon(
                        expanded = expanded,
                        modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable, enabled = enabled),
                    )
                },
                state = state,
                stateMessage = stateMessage,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
            )
            MultipleChoiceExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = Modifier
                    .exposedDropdownSize()
                    .padding(16.dp),
                content = dropdownContent,
            )
        }
        AnimatedVisibility(selectedChoices.isNotEmpty()) {
            FlowRow {
                selectedChoices.fastForEach { (id, label) ->
                    key(id) {
                        val clickLabel = stringResource(R.string.spark_combobox_multiple_chip_click_a11y, label)
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

@Composable
public fun MultiChoiceComboBox(
    value: String,
    onValueChange: (String) -> Unit,
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
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    dropdownContent: @Composable MultiChoiceDropdownItemColumnScope.() -> Unit,
) {
    MultiChoiceComboBox(
        value = value,
        onValueChange = onValueChange,
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
        state = state,
        stateMessage = stateMessage,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        dropdownContent = dropdownContent,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ComboBoxPreview() {
    PreviewTheme {
        var value by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        SingleChoiceComboBox(
            value = value,
            onValueChange = { value = it },
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
