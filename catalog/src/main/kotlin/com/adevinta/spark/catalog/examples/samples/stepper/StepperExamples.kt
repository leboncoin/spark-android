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
package com.adevinta.spark.catalog.examples.samples.stepper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.stepper.Stepper
import com.adevinta.spark.components.stepper.rememberStepperInputState
import com.adevinta.spark.components.stepper.stepperSemantics
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public val StepperExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "nudger",
        name = "Nudger",
        description = "Base Nudger stepper with decrease/increase buttons.",
        sourceUrl = "$SampleSourceUrl/StepperExamples.kt",
    ) {
        var value by rememberSaveable { mutableStateOf<Int?>(0) }
        Stepper.Nudger(
            value = value,
            onValueChange = { value = it },
        )
        Stepper.NudgerForm(
            value = value,
            onValueChange = { value = it },
            label = "Label",
            required = true,
            helper = "Exemple de message d'aide",
        )
    },
    Example(
        id = "nudger-states",
        name = "Nudger States",
        description = "Disabled and status states for the Nudger variant.",
        sourceUrl = "$SampleSourceUrl/StepperExamples.kt",
    ) {
        Stepper.NudgerForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
            enabled = false,
        )
        Stepper.NudgerForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
        )
        Stepper.NudgerForm(
            value = -1,
            onValueChange = {},
            status = FormFieldStatus.Alert,
            label = "Label",
            helper = "helper message",
        )
        Stepper.NudgerForm(
            value = -1234,
            onValueChange = {},
            status = FormFieldStatus.Success,
            label = "Label",
            helper = "helper message",
        )
    },
    Example(
        id = "input",
        name = "Input",
        description = "Input stepper with editable text field.",
        sourceUrl = "$SampleSourceUrl/StepperExamples.kt",
    ) {
        var value by rememberSaveable { mutableStateOf<Int?>(3) }
        Stepper.Input(
            value = value,
            onValueChange = { value = it },
            range = 0..100,
        )
        Stepper.InputForm(
            value = value,
            onValueChange = { value = it },
            label = "Quantity",
            helper = "Enter a value between 0 and 100",
            range = 0..100,
            required = true,
        )
    },
    Example(
        id = "input-states",
        name = "Input States",
        description = "Status states for the Input variant.",
        sourceUrl = "$SampleSourceUrl/StepperExamples.kt",
    ) {
        Stepper.InputForm(
            value = 3,
            onValueChange = {},
            status = FormFieldStatus.Error,
            statusMessage = "Value is invalid",
            label = "Label",
            helper = "helper message",
        )
        Stepper.InputForm(
            value = 3,
            onValueChange = {},
            status = FormFieldStatus.Alert,
            statusMessage = "Check this value",
            label = "Label",
            helper = "helper message",
        )
        Stepper.InputForm(
            value = 3,
            onValueChange = {},
            status = FormFieldStatus.Success,
            statusMessage = "Looks good",
            label = "Label",
            helper = "helper message",
        )
    },
    Example(
        id = "input-uncontrolled",
        name = "Input (Uncontrolled)",
        description = "Input stepper driven by StepperInputState.",
        sourceUrl = "$SampleSourceUrl/StepperExamples.kt",
    ) {
        val state = rememberStepperInputState(initialValue = 5)
        Stepper.Input(
            state = state,
            range = 0..100,
            step = 1,
        )
        Text(text = "Current value: ${state.value ?: "empty"}")
    },
    Example(
        id = "custom-form",
        name = "Custom Stepper form",
        description = "Stepper.Nudger with allowSemantics = false in a custom layout.",
        sourceUrl = "$SampleSourceUrl/StepperExamples.kt",
    ) {
        CustomStepper(
            value = 1,
            onValueChange = {},
            title = "Adultes",
            subtitle = "18 ans et plus",
        )
        CustomStepper(
            value = 0,
            onValueChange = {},
            title = "Enfants",
            subtitle = "De 3 à 17 ans",
        )
        CustomStepper(
            value = 0,
            onValueChange = {},
            title = "Bébés",
            subtitle = "Moins de 3 ans",
        )
        CustomStepper(
            value = 0,
            onValueChange = {},
            title = "Animaux",
            subtitle = "Non acceptés",
            enabled = false,
        )
    },
)

@Composable
private fun CustomStepper(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    title: String,
    subtitle: String,
    range: IntRange = 0..10,
    suffix: String = "",
    step: Int = 1,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .stepperSemantics(
                value = value,
                onValueChange = onValueChange,
                range = range,
                step = step,
                suffix = title,
                enabled = enabled,
            )
            .semantics {
                text = listOfNotNull(title, subtitle)
                    .joinToString(separator = " ")
                    .let(::AnnotatedString)
            },
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            Text(text = subtitle)
        }
        Stepper.Nudger(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            range = range,
            suffix = suffix,
            step = step,
            allowSemantics = false,
        )
    }
}

@Preview
@Composable
private fun PreviewCustomStepper() {
    PreviewTheme {
        CustomStepper(
            value = 1,
            onValueChange = {},
            title = "Adultes",
            subtitle = "18 ans et plus",
        )
    }
}
