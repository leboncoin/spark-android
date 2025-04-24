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
package com.adevinta.spark.catalog.configurator.samples.textfields

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.examples.samples.combobox.comboBoxSampleValues
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.AddonScope
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SelectedChoice
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.components.toggles.SwitchLabelled
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

public val ComboBoxConfigurators: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "combobox",
        name = "ComboBox",
        description = "ComboBox configuration",
        sourceUrl = "$SampleSourceUrl/ComboBoxSamples.kt",
    ) {
        ComboBoxSample()
    },
    Configurator(
        id = "multichoice-combobox",
        name = "MultiChoiceComboBox",
        description = "MultiChoiceComboBox configuration",
        sourceUrl = "$SampleSourceUrl/ComboBoxSamples.kt",
    ) {
        MultiChoiceComboBoxSample()
    },
    Configurator(
        id = "multichoice-combobox-with-selected",
        name = "MultiChoiceComboBox with Selected Choices",
        description = "MultiChoiceComboBox configuration with pre-selected choices",
        sourceUrl = "$SampleSourceUrl/ComboBoxSamples.kt",
    ) {
        MultiChoiceComboBoxWithSelectedSample()
    },
)

@Composable
private fun ColumnScope.ComboBoxSample() {
    var isEnabled by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var isRequired by remember { mutableStateOf(true) }
    var state: TextFieldState? by remember { mutableStateOf(null) }
    var labelText by remember { mutableStateOf("Label") }
    var valueText by remember { mutableStateOf("") }
    var placeHolderText by remember { mutableStateOf("Placeholder") }
    var helperText by remember { mutableStateOf("Helper message") }
    var stateMessageText by remember { mutableStateOf("State Message") }
    var addonText: String? by remember { mutableStateOf(null) }

    val leadingContent: (@Composable AddonScope.() -> Unit)? = addonText?.let {
        @Composable {
            Text(it)
        }
    }

    SwitchLabelled(
        checked = expanded,
        onCheckedChange = { expanded = it },
    ) {
        Text(
            text = "Expanded",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        value = valueText,
        onValueChange = { valueText = it },
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        onDismissRequest = { expanded = false },
        enabled = isEnabled,
        required = isRequired,
        label = labelText,
        placeholder = placeHolderText,
        helper = helperText,
        leadingContent = leadingContent,
        state = state,
        stateMessage = stateMessageText,
    ) {
        comboBoxSampleValues.forEach { book ->
            DropdownMenuItem(
                text = { Text(book.title) },
                onClick = {
                    valueText = book.title
                    expanded = false
                },
                selected = book.title == valueText,
            )
        }
    }

    SwitchLabelled(
        checked = isRequired,
        onCheckedChange = { isRequired = it },
    ) {
        Text(
            text = "Required",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    val textFieldStates: MutableSet<TextFieldState?> =
        TextFieldState.entries.toMutableSet<TextFieldState?>().apply { add(null) }
    val buttonStylesLabel = textFieldStates.map { it?.run { name } ?: "Default" }
    ButtonGroup(
        title = "State",
        selectedOption = state?.name ?: "Default",
        onOptionSelect = { state = if (it == "Default") null else TextFieldState.valueOf(it) },
        options = buttonStylesLabel,
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = {
            labelText = it
        },
        label = "Label text",
        placeholder = "Label of the ComboBox",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = placeHolderText,
        onValueChange = {
            placeHolderText = it
        },
        label = "Placeholder text",
        placeholder = "Placeholder of the ComboBox",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = {
            helperText = it
        },
        label = "Helper text",
        placeholder = "Helper of the ComboBox",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = stateMessageText,
        onValueChange = {
            stateMessageText = it
        },
        label = "State message",
        placeholder = "State message of the ComboBox",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = addonText ?: "",
        onValueChange = {
            addonText = it.ifBlank { null }
        },
        label = "Prefix",
        placeholder = "State message of the ComboBox",
    )
}

@Composable
private fun ColumnScope.MultiChoiceComboBoxSample() {
    var value by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var isRequired by remember { mutableStateOf(true) }
    var state: TextFieldState? by remember { mutableStateOf(null) }
    var labelText by remember { mutableStateOf("Label") }
    var placeHolderText by remember { mutableStateOf("Placeholder") }
    var helperText by remember { mutableStateOf("Helper message") }
    var stateMessageText by remember { mutableStateOf("State Message") }
    var addonText: String? by remember { mutableStateOf(null) }
    var selectedBooks by remember { mutableStateOf(setOf<Int>()) }

    val leadingContent: (@Composable AddonScope.() -> Unit)? = addonText?.let {
        @Composable {
            Text(it)
        }
    }

    MultiChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value = it },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = isEnabled,
        required = isRequired,
        label = labelText,
        placeholder = placeHolderText,
        helper = helperText,
        leadingContent = leadingContent,
        state = state,
        stateMessage = stateMessageText,
        selectedChoices = selectedBooks.mapNotNull { id ->
            comboBoxSampleValues.find { it.id == id }?.let {
                SelectedChoice(id.toString(), it.title)
            }
        }.toPersistentList(),
        onSelectedClick = { id ->
            selectedBooks = selectedBooks - id.toInt()
        },
        dropdownContent = {
            comboBoxSampleValues.forEach { book ->
                DropdownMenuItem(
                    text = { Text(book.title) },
                    onCheckedChange = { checked ->
                        selectedBooks = if (checked) {
                            selectedBooks + book.id
                        } else {
                            selectedBooks - book.id
                        }
                    },
                    checked = book.id in selectedBooks,
                )
            }
        },
    )

    SwitchLabelled(
        checked = expanded,
        onCheckedChange = { expanded = it },
    ) {
        Text(
            text = "Expanded",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = isRequired,
        onCheckedChange = { isRequired = it },
    ) {
        Text(
            text = "Required",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    val buttonStylesLabel by remember {
        val textFieldStates: MutableSet<TextFieldState?> =
            TextFieldState.entries.toMutableSet<TextFieldState?>().apply { add(null) }
        mutableStateOf(textFieldStates.map { it?.run { name } ?: "Default" })
    }
    ButtonGroup(
        title = "State",
        selectedOption = state?.name ?: "Default",
        onOptionSelect = { state = if (it == "Default") null else TextFieldState.valueOf(it) },
        options = buttonStylesLabel,
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = { labelText = it },
        label = "Label text",
        placeholder = "Label of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = placeHolderText,
        onValueChange = { placeHolderText = it },
        label = "Placeholder text",
        placeholder = "Placeholder of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = { helperText = it },
        label = "Helper text",
        placeholder = "Helper of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = stateMessageText,
        onValueChange = { stateMessageText = it },
        label = "State message",
        placeholder = "State message of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = addonText ?: "",
        onValueChange = { addonText = it.ifBlank { null } },
        label = "Prefix",
        placeholder = "Prefix text for the ComboBox",
    )
}

@Composable
private fun ColumnScope.MultiChoiceComboBoxWithSelectedSample() {
    var value by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var isRequired by remember { mutableStateOf(true) }
    var state: TextFieldState? by remember { mutableStateOf(null) }
    var labelText by remember { mutableStateOf("Label") }
    var placeHolderText by remember { mutableStateOf("Placeholder") }
    var helperText by remember { mutableStateOf("Helper message") }
    var stateMessageText by remember { mutableStateOf("State Message") }
    var addonText: String? by remember { mutableStateOf(null) }
    var selectedBooks by remember { mutableStateOf(setOf(1, 2)) } // Pre-select first two books

    val leadingContent: (@Composable AddonScope.() -> Unit)? = addonText?.let {
        @Composable {
            Text(it)
        }
    }

    MultiChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value = it },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = isEnabled,
        required = isRequired,
        label = labelText,
        placeholder = placeHolderText,
        helper = helperText,
        leadingContent = leadingContent,
        state = state,
        stateMessage = stateMessageText,
        selectedChoices = selectedBooks.mapNotNull { id ->
            comboBoxSampleValues.find { it.id == id }?.let {
                SelectedChoice(id.toString(), it.title)
            }
        }.toPersistentList(),
        onSelectedClick = { id ->
            selectedBooks = selectedBooks - id.toInt()
        },
        dropdownContent = {
            comboBoxSampleValues.forEach { book ->
                DropdownMenuItem(
                    text = { Text(book.title) },
                    onCheckedChange = { checked ->
                        selectedBooks = if (checked) {
                            selectedBooks + book.id
                        } else {
                            selectedBooks - book.id
                        }
                    },
                    checked = book.id in selectedBooks,
                )
            }
        },
    )

    SwitchLabelled(
        checked = expanded,
        onCheckedChange = { expanded = it },
    ) {
        Text(
            text = "Expanded",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = isRequired,
        onCheckedChange = { isRequired = it },
    ) {
        Text(
            text = "Required",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    val textFieldStates: MutableSet<TextFieldState?> =
        TextFieldState.entries.toMutableSet<TextFieldState?>().apply { add(null) }
    val buttonStylesLabel = textFieldStates.map { it?.run { name } ?: "Default" }
    ButtonGroup(
        title = "State",
        selectedOption = state?.name ?: "Default",
        onOptionSelect = { state = if (it == "Default") null else TextFieldState.valueOf(it) },
        options = buttonStylesLabel,
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = { labelText = it },
        label = "Label text",
        placeholder = "Label of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = placeHolderText,
        onValueChange = { placeHolderText = it },
        label = "Placeholder text",
        placeholder = "Placeholder of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = { helperText = it },
        label = "Helper text",
        placeholder = "Helper of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = stateMessageText,
        onValueChange = { stateMessageText = it },
        label = "State message",
        placeholder = "State message of the ComboBox",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = addonText ?: "",
        onValueChange = { addonText = it.ifBlank { null } },
        label = "Prefix",
        placeholder = "Prefix text for the ComboBox",
    )
}

@Preview
@Composable
private fun ComboBoxSamplePreview() {
    PreviewTheme { ComboBoxSample() }
}
