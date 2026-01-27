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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuGroupItem
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.AddonScope
import com.adevinta.spark.components.textfields.SingleChoiceDropdown
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.components.toggles.SwitchLabelled

@OptIn(ExperimentalSparkApi::class)
public val SingleChoiceDropdownConfigurator: Configurator = Configurator(
    id = "single-choice-dropdown",
    name = "SingleChoiceDropdown",
    description = "SingleChoiceDropdown configuration",
    sourceUrl = "$SampleSourceUrl/SingleChoiceDropdownConfigurator.kt",
) {
    SingleChoiceDropdownSample()
}

@Composable
private fun ColumnScope.SingleChoiceDropdownSample() {
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var hideOnSelect by rememberSaveable { mutableStateOf(true) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isRequired by rememberSaveable { mutableStateOf(true) }
    var state: TextFieldState? by rememberSaveable { mutableStateOf(null) }
    var labelText by rememberSaveable { mutableStateOf("Label") }
    var valueText by rememberSaveable { mutableStateOf("") }
    var placeHolderText by rememberSaveable { mutableStateOf("Placeholder") }
    var helperText by rememberSaveable { mutableStateOf("Helper message") }
    var stateMessageText by rememberSaveable { mutableStateOf("State Message") }
    var addonText: String? by rememberSaveable { mutableStateOf(null) }

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

    SingleChoiceDropdown(
        value = valueText,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled,
        required = isRequired,
        label = labelText,
        placeholder = placeHolderText,
        helper = helperText,
        leadingContent = leadingContent,
        state = state,
        stateMessage = stateMessageText,
    ) {
        DropdownStubData.forEach { (groupName, books) ->
            DropdownMenuGroupItem(
                title = {
                    Text(groupName)
                },
            ) {
                books.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book) },
                        onClick = {
                            valueText = book
                            if (hideOnSelect) expanded = false
                        },
                        selected = valueText == book,
                    )
                }
            }
        }
    }

    SwitchLabelled(
        checked = hideOnSelect,
        onCheckedChange = { hideOnSelect = it },
    ) {
        Text(
            text = "Hide the menu on selection",
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
        onValueChange = {
            labelText = it
        },
        label = "Label text",
        placeholder = "Label of the Dropdown",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = placeHolderText,
        onValueChange = {
            placeHolderText = it
        },
        label = "Placeholder text",
        placeholder = "Placeholder of the Dropdown",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = {
            helperText = it
        },
        label = "Helper text",
        placeholder = "Helper of the Dropdown",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = stateMessageText,
        onValueChange = {
            stateMessageText = it
        },
        label = "State message",
        placeholder = "State message of the Dropdown",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = addonText ?: "",
        onValueChange = {
            addonText = it.ifBlank { null }
        },
        label = "Prefix",
        placeholder = "Prefix text for the Dropdown",
    )
}

@Preview
@Composable
private fun SingleChoiceDropdownSamplePreview() {
    PreviewTheme { SingleChoiceDropdownSample() }
}
