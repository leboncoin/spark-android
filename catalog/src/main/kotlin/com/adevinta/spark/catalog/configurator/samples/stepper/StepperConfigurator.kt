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
package com.adevinta.spark.catalog.configurator.samples.stepper

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.stepper.Stepper
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.components.toggles.SwitchLabelled
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

public val StepperConfigurators: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "nudger",
        name = "Nudger",
        description = "Nudger stepper configuration",
        sourceUrl = "$SampleSourceUrl/StepperSamples.kt",
    ) { snackbarHostState, _ ->
        NudgerSample(snackbarHostState)
    },
    Configurator(
        id = "nudger-form",
        name = "Nudger Form",
        description = "Nudger Form configuration with helper and label",
        sourceUrl = "$SampleSourceUrl/StepperSamples.kt",
    ) { snackbarHostState, _ ->
        NudgerFormSample(snackbarHostState)
    },
    Configurator(
        id = "input",
        name = "Input",
        description = "Input stepper configuration",
        sourceUrl = "$SampleSourceUrl/StepperSamples.kt",
    ) { snackbarHostState, _ ->
        InputSample(snackbarHostState)
    },
    Configurator(
        id = "input-form",
        name = "Input Form",
        description = "Input Form configuration with helper and label",
        sourceUrl = "$SampleSourceUrl/StepperSamples.kt",
    ) { snackbarHostState, _ ->
        InputFormSample(snackbarHostState)
    },
)

// region Nudger

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.NudgerSample(snackbarState: SnackbarHostState) {
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var min by rememberSaveable { mutableIntStateOf(-2) }
    var max by rememberSaveable { mutableIntStateOf(100) }
    var value by rememberSaveable { mutableStateOf<Int?>(99) }
    var step by rememberSaveable { mutableIntStateOf(1) }
    var isFlexible by rememberSaveable { mutableStateOf(false) }
    var suffix by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Stepper.Nudger(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        value = value,
        onValueChange = { value = it },
        step = step,
        range = min..max,
        suffix = suffix,
        enabled = isEnabled,
        flexible = isFlexible,
    )

    SwitchLabelled(checked = isEnabled, onCheckedChange = { isEnabled = it }) {
        Text(text = "Enabled", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(checked = isFlexible, onCheckedChange = { isFlexible = it }) {
        Text(text = "Flexible", modifier = Modifier.fillMaxWidth())
    }
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        RangeFields(
            min = min,
            max = max,
            onMinChange = { min = it },
            onMaxChange = { max = it },
            snackbarState = snackbarState,
            coroutineScope = coroutineScope,
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = suffix,
            onValueChange = { suffix = it },
            label = "Suffix",
            placeholder = "%",
            helper = "Suffix displayed after the value",
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.NudgerFormSample(snackbarState: SnackbarHostState) {
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var isRequired by rememberSaveable { mutableStateOf(true) }
    var isFlexible by rememberSaveable { mutableStateOf(false) }
    var min by rememberSaveable { mutableIntStateOf(-2) }
    var max by rememberSaveable { mutableIntStateOf(10) }
    var value by rememberSaveable { mutableStateOf<Int?>(0) }
    var status: TextFieldState? by rememberSaveable { mutableStateOf(null) }
    var labelText by rememberSaveable { mutableStateOf("Label") }
    var helperText by rememberSaveable { mutableStateOf("Helper message") }
    val coroutineScope = rememberCoroutineScope()

    Stepper.NudgerForm(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .animateContentSize(),
        value = value,
        onValueChange = { value = it },
        required = isRequired,
        range = min..max,
        enabled = isEnabled,
        flexible = isFlexible,
        status = status,
        label = labelText,
        helper = helperText,
    )

    SwitchLabelled(checked = isEnabled, onCheckedChange = { isEnabled = it }) {
        Text(text = "Enabled", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(checked = isFlexible, onCheckedChange = { isFlexible = it }) {
        Text(text = "Flexible", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(checked = isRequired, onCheckedChange = { isRequired = it }) {
        Text(text = "Required", modifier = Modifier.fillMaxWidth())
    }

    StatusButtonGroup(status = status, onStatusChange = { status = it })

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        RangeFields(
            min = min,
            max = max,
            onMinChange = { min = it },
            onMaxChange = { max = it },
            snackbarState = snackbarState,
            coroutineScope = coroutineScope,
        )
    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = { labelText = it },
        label = "Label",
        placeholder = "Number of adults",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = { helperText = it },
        label = "Helper",
        placeholder = "A helper message",
    )
}

// endregion

// region Input

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.InputSample(snackbarState: SnackbarHostState) {
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var min by rememberSaveable { mutableIntStateOf(0) }
    var max by rememberSaveable { mutableIntStateOf(100) }
    var value by rememberSaveable { mutableStateOf<Int?>(5) }
    var step by rememberSaveable { mutableIntStateOf(1) }
    var isFlexible by rememberSaveable { mutableStateOf(false) }
    var status: TextFieldState? by rememberSaveable { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    Stepper.Input(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        value = value,
        onValueChange = { value = it },
        step = step,
        range = min..max,
        enabled = isEnabled,
        status = status,
        flexible = isFlexible,
    )

    SwitchLabelled(checked = isEnabled, onCheckedChange = { isEnabled = it }) {
        Text(text = "Enabled", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(checked = isFlexible, onCheckedChange = { isFlexible = it }) {
        Text(text = "Flexible", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(
        checked = value == null,
        onCheckedChange = { value = if (it) null else min },
    ) {
        Text(text = "Null value", modifier = Modifier.fillMaxWidth())
    }

    StatusButtonGroup(status = status, onStatusChange = { status = it })

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        RangeFields(
            min = min,
            max = max,
            onMinChange = { min = it },
            onMaxChange = { max = it },
            snackbarState = snackbarState,
            coroutineScope = coroutineScope,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.InputFormSample(snackbarState: SnackbarHostState) {
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var isRequired by rememberSaveable { mutableStateOf(true) }
    var isFlexible by rememberSaveable { mutableStateOf(false) }
    var min by rememberSaveable { mutableIntStateOf(0) }
    var max by rememberSaveable { mutableIntStateOf(100) }
    var value by rememberSaveable { mutableStateOf<Int?>(5) }
    var status: TextFieldState? by rememberSaveable { mutableStateOf(null) }
    var labelText by rememberSaveable { mutableStateOf("Quantity") }
    var helperText by rememberSaveable { mutableStateOf("Enter a value") }
    val coroutineScope = rememberCoroutineScope()

    Stepper.InputForm(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .animateContentSize(),
        value = value,
        onValueChange = { value = it },
        required = isRequired,
        range = min..max,
        enabled = isEnabled,
        status = status,
        flexible = isFlexible,
        label = labelText,
        helper = helperText,
    )

    SwitchLabelled(checked = isEnabled, onCheckedChange = { isEnabled = it }) {
        Text(text = "Enabled", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(checked = isFlexible, onCheckedChange = { isFlexible = it }) {
        Text(text = "Flexible", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(checked = isRequired, onCheckedChange = { isRequired = it }) {
        Text(text = "Required", modifier = Modifier.fillMaxWidth())
    }
    SwitchLabelled(
        checked = value == null,
        onCheckedChange = { value = if (it) null else min },
    ) {
        Text(text = "Null value", modifier = Modifier.fillMaxWidth())
    }

    StatusButtonGroup(status = status, onStatusChange = { status = it })

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        RangeFields(
            min = min,
            max = max,
            onMinChange = { min = it },
            onMaxChange = { max = it },
            snackbarState = snackbarState,
            coroutineScope = coroutineScope,
        )
    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = { labelText = it },
        label = "Label",
        placeholder = "Quantity",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = { helperText = it },
        label = "Helper",
        placeholder = "A helper message",
    )
}

// endregion

// region Shared helpers

@Composable
private fun StatusButtonGroup(
    status: TextFieldState?,
    onStatusChange: (TextFieldState?) -> Unit,
) {
    val options = TextFieldState.entries.toMutableSet<TextFieldState?>().apply { add(null) }
    val labels = options.map { it?.name ?: "Default" }.toImmutableList()
    ButtonGroup(
        title = "Status",
        selectedOption = status?.name ?: "Default",
        onOptionSelect = { onStatusChange(if (it == "Default") null else TextFieldState.valueOf(it)) },
        options = labels,
    )
}

@Composable
private fun RangeFields(
    min: Int,
    max: Int,
    onMinChange: (Int) -> Unit,
    onMaxChange: (Int) -> Unit,
    snackbarState: SnackbarHostState,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(0.45f),
        value = min.toString(),
        onValueChange = {
            val newMin = it.toIntOrNull()
            when {
                newMin == null -> coroutineScope.launch {
                    snackbarState.showSnackbar(
                        message = "The value for min: $it can't be used.",
                        intent = SnackbarIntent.Error,
                    )
                }

                newMin > max -> {
                    coroutineScope.launch {
                        snackbarState.showSnackbar(
                            message = "Min ($newMin) can't be greater than max ($max).",
                            intent = SnackbarIntent.Error,
                        )
                    }
                    onMinChange(max - 1)
                }

                else -> onMinChange(newMin)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = "Min",
        placeholder = "0",
        helper = "Minimal range",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(0.45f),
        value = max.toString(),
        onValueChange = {
            val newMax = it.toIntOrNull()
            when {
                newMax == null -> coroutineScope.launch {
                    snackbarState.showSnackbar(
                        message = "The value for max: $it can't be used.",
                        intent = SnackbarIntent.Error,
                    )
                }

                newMax < min -> {
                    coroutineScope.launch {
                        snackbarState.showSnackbar(
                            message = "Max ($newMax) can't be less than min ($min).",
                            intent = SnackbarIntent.Error,
                        )
                    }
                    onMaxChange(min + 1)
                }

                else -> onMaxChange(newMax)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = "Max",
        placeholder = "15",
        helper = "Maximal range",
    )
}

// endregion

@Preview
@Composable
private fun PreviewNudgerSample() {
    PreviewTheme { NudgerSample(SnackbarHostState()) }
}

@Preview
@Composable
private fun PreviewInputSample() {
    PreviewTheme { InputSample(SnackbarHostState()) }
}
