/*
 * Copyright (c) 2026 Adevinta
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

package com.adevinta.spark.components.formfield

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.formfield.FormField.Description
import com.adevinta.spark.components.formfield.FormField.Label
import com.adevinta.spark.components.formfield.FormField.StatusMessage
import com.adevinta.spark.components.formfield.FormField.SupportLabel
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.TextFieldCharacterCounter
import com.adevinta.spark.tokens.EmphasizeDim3

public object FormField {

    /**
     * Scaffold for a form field using plain strings for both visual rendering and accessibility.
     *
     * Arranges a label, description, [content], support label and status message in a column.
     * Default slot composables ([Label], [Description], [SupportLabel], [StatusMessage]) are used
     * for rendering. The same strings are baked into `semantics { text }` on the root so TalkBack
     * announces the full field context, while the visual slots are hidden from the accessibility
     * tree with `invisibleSemantic`. The [content] slot is NOT wrapped and retains its own full
     * semantics (role, state, actions).
     *
     * The [status] is propagated via [LocalFormFieldStatus] so [content] can read it without
     * explicit wiring.
     *
     * @param modifier the [Modifier] to be applied to the column container
     * @param status the current validation status; drives the colour of [statusMessage] and is
     * available to [content] via [LocalFormFieldStatus]
     * @param label optional label text; rendered by [Label] and announced by TalkBack
     * @param required when `true`, a localised "mandatory" string is appended to the TalkBack
     * announcement and an asterisk is shown in the label
     * @param description optional description text; rendered by [Description] and announced by
     * TalkBack
     * @param supportLabel optional support label text; rendered by [SupportLabel] and announced
     * by TalkBack
     * @param statusMessage optional status message text; rendered by [StatusMessage], coloured by
     * [status], and announced by TalkBack
     * @param counter optional character counter displayed right-aligned alongside the support label
     * @param content the required interactive control; retains its own full semantics
     */
    @Composable
    public fun Scaffold(
        label: String,
        modifier: Modifier = Modifier,
        status: FormFieldStatus? = null,
        required: Boolean = false,
        description: String? = null,
        supportLabel: String? = null,
        statusMessage: String? = null,
        counter: TextFieldCharacterCounter? = null,
        content: @Composable () -> Unit,
    ) {
        SparkFormField(
            modifier = modifier,
            status = status,
            required = required,
            label = { Label(text = label, required = required) },
            description = description?.let { { Description(text = it) } },
            supportLabel = supportLabel?.let { { SupportLabel(text = it) } },
            statusMessage = statusMessage?.let { { StatusMessage(text = it) } },
            counter = counter,
            content = content,
        )
    }

    /**
     * Scaffold for a form field using composable slots for visual rendering.
     *
     * Arranges [label], [description], [content], [supportLabel] and [statusMessage] in a column.
     * Use this overload when the default slot composables are not sufficient (e.g. a label with an
     * icon, or a rich description).
     *
     * The [status] is propagated via [LocalFormFieldStatus] so [content] can read it without
     * explicit wiring. The composable slots are hidden from the accessibility tree with
     * `invisibleSemantic`. The [content] slot is NOT wrapped and retains its own full semantics.
     *
     * @param modifier the [Modifier] to be applied to the column container
     * @param status the current validation status; drives the colour of [statusMessage] and is
     * available to [content] via [LocalFormFieldStatus]
     * @param required when `true`, a localised "mandatory" string is appended to the TalkBack
     * announcement
     * @param label optional composable rendered as the visual label
     * @param description optional composable rendered as the visual description
     * @param supportLabel optional composable rendered as the visual support label
     * @param statusMessage optional composable rendered as the visual status message
     * @param counter optional character counter displayed right-aligned alongside the support label
     * @param content the required interactive control; retains its own full semantics
     */
    @Composable
    public fun Scaffold(
        modifier: Modifier = Modifier,
        status: FormFieldStatus? = null,
        required: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        description: @Composable (() -> Unit)? = null,
        supportLabel: @Composable (() -> Unit)? = null,
        statusMessage: @Composable (() -> Unit)? = null,
        counter: TextFieldCharacterCounter? = null,
        content: @Composable () -> Unit,
    ) {
        SparkFormField(
            modifier = modifier,
            status = status,
            required = required,
            label = label,
            description = description,
            supportLabel = supportLabel,
            statusMessage = statusMessage,
            counter = counter,
            content = content,
        )
    }

    /**
     * Default label slot for [FormField.Scaffold].
     *
     * Renders [text] in `body2` style. An asterisk is appended when [required] is `true`.
     *
     * @param text the label text
     * @param required when `true`, appends a dimmed asterisk to indicate a mandatory field
     * @param modifier the [Modifier] to be applied
     */
    @Composable
    public fun Label(
        text: String,
        modifier: Modifier = Modifier,
        required: Boolean = false,
    ) {
        Row(modifier = modifier) {
            Text(
                text = text,
                modifier = Modifier.weight(weight = 1f, fill = false),
            )
            if (required) {
                EmphasizeDim3 {
                    Text(
                        text = "*",
                        modifier = Modifier.padding(start = 4.dp),
                        style = SparkTheme.typography.caption,
                    )
                }
            }
        }
    }

    /**
     * Default description slot for [FormField.Scaffold].
     *
     * Renders [text] in `body1` style.
     *
     * @param text the description text
     * @param modifier the [Modifier] to be applied
     */
    @Composable
    public fun Description(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(text = text, modifier = modifier)
    }

    /**
     * Default support label slot for [FormField.Scaffold].
     *
     * Renders [text] in `caption` style.
     *
     * @param text the support label text
     * @param modifier the [Modifier] to be applied
     */
    @Composable
    public fun SupportLabel(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(text = text, modifier = modifier)
    }

    /**
     * Default status message slot for [FormField.Scaffold].
     *
     * Renders [text] in `caption` style, coloured by the [FormFieldStatus] propagated via
     * [LocalFormFieldStatus]. A leading status icon is shown when a status is present.
     *
     * @param text the status message text
     * @param modifier the [Modifier] to be applied
     */
    @Composable
    public fun StatusMessage(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(text = text, modifier = modifier)
    }
}

@Preview(group = "FormField", name = "FormField - Error with all strings")
@Composable
private fun FormFieldErrorPreview() {
    PreviewTheme {
        FormField.Scaffold(
            label = "Email address",
            required = true,
            description = "We will only use this to contact you about your account",
            statusMessage = "Please enter a valid email address",
            supportLabel = "e.g. name@example.com",
            status = FormFieldStatus.Error,
        ) { Text("[ TextField goes here ]") }
    }
}

@Preview(group = "FormField", name = "FormField - Success")
@Composable
private fun FormFieldSuccessPreview() {
    PreviewTheme {
        FormField.Scaffold(
            label = "Username",
            statusMessage = "Username is available",
            status = FormFieldStatus.Success,
        ) { Text("[ TextField goes here ]") }
    }
}

@Preview(group = "FormField", name = "FormField - custom slots")
@Composable
private fun FormFieldCustomSlotsPreview() {
    PreviewTheme {
        FormField.Scaffold(
            label = "Quantity",
            required = true,
            description = "Choose how many items you want",
        ) { Text("[ Stepper goes here ]") }
    }
}

@Preview(group = "FormField", name = "FormField - content only")
@Composable
private fun FormFieldContentOnlyPreview() {
    PreviewTheme {
        FormField.Scaffold { Text("[ no surrounding labels ]") }
    }
}

@Preview(group = "FormField", name = "FormField - with counter")
@Composable
private fun FormFieldCounterPreview() {
    PreviewTheme {
        FormField.Scaffold(
            label = "Bio",
            supportLabel = "Tell us about yourself",
            counter = TextFieldCharacterCounter(count = 42, maxCharacter = 200),
        ) { Text("[ TextField goes here ]") }
    }
}