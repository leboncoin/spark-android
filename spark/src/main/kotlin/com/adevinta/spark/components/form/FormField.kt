/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.components.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.textfields.FormFieldStatus

/**
 * Adds accessibility semantics to a form field, grouping label, status, and supporting text for screen readers.
 *
 * This modifier should be applied to the root of a form field layout to ensure that assistive technologies announce
 * the label, status (e.g., error, success), and supporting text as a single field.
 *
 * @param label The label for the field (announced as the field label).
 * @param status The status of the field (e.g., "Error", "Success", "Alert"), announced as stateDescription.
 * @param supportingText Helper or error text, announced as a hint.
 * @param enabled Whether the field is enabled (announced as disabled if false).
 *
 * Usage:
 * ```kotlin
 * Column(
 *   modifier = Modifier.formFieldSemantics(
 *     label = "Email address",
 *     status = "Error",
 *     supportingText = "Please enter a valid email.",
 *     enabled = true
 *   )
 * ) { ... }
 * ```
 */
public fun Modifier.formFieldSemantics(
    label: String? = null,
    status: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
): Modifier = semantics(mergeDescendants = true) {
//    if (label != null) this.label = label
//    if (status != null) this.stateDescription = status
//    if (supportingText != null) this.hint = supportingText
//    if (!enabled) this.disabled()
//    // Compose a full contentDescription for screen readers
//    val desc = buildString {
//        if (label != null) append(label + ". ")
//        if (status != null) append(status + ". ")
//        if (supportingText != null) append(supportingText + ". ")
//    }.ifBlank { null }
//    if (desc != null) this.contentDescription = desc
}

// @Composable
// public fun FormField(
//    modifier: Modifier = Modifier,
//    status: FormFieldStatus? = null,
//    label: (@Composable FormFieldScope.() -> Unit)? = null,
//    supportingText: (@Composable FormFieldScope.() -> Unit)? = null,
//    characterCounter: @Composable (() -> Unit)? = null,
//    content: @Composable FormFieldScope.() -> Unit,
// ) {
//    SparkFormField(
//        modifier = modifier,
//        status = status,
//        label = label,
//        supportingText = supportingText,
//        characterCounter = characterCounter,
//        content = content,
//    )
// }

/**
 * CompositionLocal used to specify the default shapes for the surfaces.
 */
internal val LocalFormFieldStatus = compositionLocalOf<FormFieldStatus?> { null }

@Preview
@Composable
private fun PreviewFormField() {
    PreviewTheme {
//        FormField(
//            status = FormFieldStatus.Error,
//            label = { Text("Label", style = MaterialTheme.typography.bodyLarge) },
//            supportingText = { Text("This is an error message.") },
//            characterCounter = { Text("12/20") },
//        ) {
//            Text("[Field content goes here]", Modifier.fillMaxWidth())
//        }
    }
}
