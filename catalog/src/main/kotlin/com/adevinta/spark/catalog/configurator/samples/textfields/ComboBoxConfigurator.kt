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
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.AddonScope
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.components.toggles.SwitchLabelled

public val ComboBoxConfigurator: Configurator = Configurator(
    id = "combobox",
    name = "ComboBox",
    description = "ComboBox configuration",
    sourceUrl = "$SampleSourceUrl/ComboBoxSamples.kt",
) {
    ComboBoxSample()
}

@Composable
private fun ColumnScope.ComboBoxSample() {
    var isEnabled by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var isRequired by remember { mutableStateOf(true) }
    var state: TextFieldState? by remember { mutableStateOf(null) }
    var labelText by remember { mutableStateOf("Label") }
    var valueText by remember { mutableStateOf("Value") }
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
        repeat(5) {
            DropdownMenuItem(
                text = { Text("Item $it") },
                onClick = { expanded = false },
                selected = false,
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

@Preview
@Composable
private fun ComboBoxSamplePreview() {
    PreviewTheme { ComboBoxSample() }
}
