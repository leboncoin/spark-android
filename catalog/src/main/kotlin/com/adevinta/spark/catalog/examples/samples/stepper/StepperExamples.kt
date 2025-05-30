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
import androidx.compose.runtime.mutableIntStateOf
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
import com.adevinta.spark.components.stepper.StepperForm
import com.adevinta.spark.components.stepper.stepperSemantics
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus

public val StepperExamples: List<Example> = listOf(
    Example(
        id = "default",
        name = "Base Stepper Example",
        description = "Base interactions on stepper.",
        sourceUrl = "$SampleSourceUrl/RatingDisplaySample.kt",
    ) {
        var value by rememberSaveable { mutableIntStateOf(0) }
        Stepper(
            value = value,
            onValueChange = {
                value = it
            },
        )
        StepperForm(
            value = value,
            onValueChange = {
                value = it
            },
            label = "Label",
            required = true,
            helper = "Exemple de message d'aide",
        )
    },
    Example(
        id = "states",
        name = "Stepper States",
        description = "Disabled and all regular states available for the TestField.",
        sourceUrl = "$SampleSourceUrl/RatingDisplaySample.kt",
    ) {
        StepperForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
            enabled = false,
        )
        StepperForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
        )
        StepperForm(
            value = -1,
            onValueChange = {},
            status = FormFieldStatus.Alert,
            label = "Label",
            helper = "helper message",
        )
        StepperForm(
            value = -1234,
            onValueChange = {},
            status = FormFieldStatus.Success,
            label = "Label",
            helper = "helper message",
        )
    },

    Example(
        id = "states",
        name = "Stepper States",
        description = "Disabled and all regular states available for the TestField.",
        sourceUrl = "$SampleSourceUrl/RatingDisplaySample.kt",
    ) {
        StepperForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
            enabled = false,
        )
    },
    Example(
        id = "custom-form",
        name = "Custom Stepper form",
        description = "Stepper can show a suffix like `%`, `€` or `person•s`.",
        sourceUrl = "$SampleSourceUrl/RatingInputSample.kt",
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
    //  Example(
    //     name = "Stepper Custom Buttons",
    //     description = "Custom implementation allow used defined button for de-increment.",
    //     sourceUrl = "$SampleSourceUrl/RatingFullSample.kt",
    // ) {
    //     WipIllustration()
    // },
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
        Stepper(
            value = value,
            onValueChange = {},
            enabled = enabled,
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
