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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.components.toggles.SwitchLabelled
import kotlinx.coroutines.launch

public val StepperConfigurators: List<Configurator> = listOf(
    Configurator(
        name = "Stepper",
        description = "Stepper configuration",
        sourceUrl = "$SampleSourceUrl/StepperSamples.kt",
    ) {
        StepperSample(it)
    },
    Configurator(
        name = "Stepper Form",
        description = "Stepper Form configuration with helper and label",
        sourceUrl = "$SampleSourceUrl/StepperSamples.kt",
    ) {
        StepperFormSample(it)
    },
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.StepperSample(snackbarState: SnackbarHostState) {
    var isEnabled by remember { mutableStateOf(true) }
    var min by remember { mutableIntStateOf(-2) }
    var max by remember { mutableIntStateOf(1000000000) }
    var value by remember { mutableIntStateOf(10000000) }
    var isFlexible by remember { mutableStateOf(false) }
    var status: TextFieldState? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    Stepper(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .animateContentSize(),
        value = value,
        onValueChange = {
            value = it
        },
        range = min..max,
        enabled = isEnabled,
        isFlexible = isFlexible,
        status = status,
    )
    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    SwitchLabelled(
        checked = isFlexible,
        onCheckedChange = { isFlexible = it },
    ) {
        Text(
            text = "Flexible",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    val textFieldStates: MutableSet<TextFieldState?> =
        TextFieldState.entries.toMutableSet<TextFieldState?>().apply { add(null) }
    val buttonStylesLabel = textFieldStates.map { it?.run { name } ?: "Default" }
    ButtonGroup(
        title = "Status",
        selectedOption = status?.name ?: "Default",
        onOptionSelect = { status = if (it == "Default") null else TextFieldState.valueOf(it) },
        options = buttonStylesLabel,
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = min.toString(),
            onValueChange = {
                val newMin = it.toIntOrNull()
                when {
                    newMin == null -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for min: $it can't be used.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                    }

                    newMin > max -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for min: $newMin can't be greater than $max.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                        min = max - 1
                    }

                    else -> min = newMin
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = "Min",
            placeholder = "0",
            helper = "Minimal range of the stepper",
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = max.toString(),
            onValueChange = {
                val newMax = it.toIntOrNull()
                when {
                    newMax == null -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for max: $it can't be used.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                    }

                    newMax < min -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for max: $newMax can't be lesser than $min.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                        max = min + 1
                    }

                    else -> max = newMax
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = "Max",
            placeholder = "15",
            helper = "Maximal range of the stepper",
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.StepperFormSample(snackbarState: SnackbarHostState) {
    var isEnabled by remember { mutableStateOf(true) }
    var isRequired by remember { mutableStateOf(true) }
    var isFlexible by remember { mutableStateOf(false) }
    var min by remember { mutableIntStateOf(-2) }
    var max by remember { mutableIntStateOf(10) }
    var value by remember { mutableIntStateOf(0) }
    var status: TextFieldState? by remember { mutableStateOf(null) }
    var labelText by remember { mutableStateOf("Label") }
    var helperText by remember { mutableStateOf("Helper message") }
    val coroutineScope = rememberCoroutineScope()
    Stepper(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .animateContentSize(),
        value = value,
        onValueChange = {
            value = it
        },
        required = isRequired,
        range = min..max,
        enabled = isEnabled,
        isFlexible = isFlexible,
        status = status,
        label = labelText,
        helperText = helperText,
    )
    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    SwitchLabelled(
        checked = isFlexible,
        onCheckedChange = { isFlexible = it },
    ) {
        Text(
            text = "Flexible",
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
    val textFieldStates: MutableSet<TextFieldState?> =
        TextFieldState.entries.toMutableSet<TextFieldState?>().apply { add(null) }
    val buttonStylesLabel = textFieldStates.map { it?.run { name } ?: "Default" }
    ButtonGroup(
        title = "Status",
        selectedOption = status?.name ?: "Default",
        onOptionSelect = { status = if (it == "Default") null else TextFieldState.valueOf(it) },
        options = buttonStylesLabel,
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = min.toString(),
            onValueChange = {
                val newMin = it.toIntOrNull()
                when {
                    newMin == null -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for min: $it can't be used.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                    }

                    newMin > max -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for min: $newMin can't be greater than $max.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                        min = max - 1
                    }

                    else -> min = newMin
                }
            },
            label = "Min",
            placeholder = "0",
            helper = "Minimal range of the stepper",
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = max.toString(),
            onValueChange = {
                val newMax = it.toIntOrNull()
                when {
                    newMax == null -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for max: $it can't be used.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                    }

                    newMax < min -> {
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                message = "The value for max: $newMax can't be lesser than $min.",
                                intent = SnackbarIntent.Error,
                            )
                        }
                        max = min + 1
                    }

                    else -> max = newMax
                }
            },
            label = "Max",
            placeholder = "15",
            helper = "Maximal range of the stepper",
        )
    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = {
            labelText = it
        },
        label = "Label",
        placeholder = "Number of adults",
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = helperText,
        onValueChange = {
            helperText = it
        },
        label = "Helper",
        placeholder = "A helper message aim to guide the user on what the field expect to contain",
    )
}

@Preview
@Composable
private fun PreviewStepperSample() {
    PreviewTheme { StepperSample(SnackbarHostState()) }
}

@Preview
@Composable
private fun PreviewStepperFormSample() {
    PreviewTheme { StepperFormSample(SnackbarHostState()) }
}
