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
package com.adevinta.spark.components.form.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.form.LocalFormFieldStatus
import com.adevinta.spark.components.form.formFieldSemantics
import com.adevinta.spark.components.textfields.FormFieldStatus

internal sealed interface FormFieldScope {
    val status: FormFieldStatus?
}

private class FormFieldScopeImpl(override val status: FormFieldStatus?) : FormFieldScope

@Composable
internal fun SparkFormField(
    modifier: Modifier = Modifier,
    status: FormFieldStatus? = null,
    label: (@Composable FormFieldScope.() -> Unit)? = null,
    supportingText: (@Composable FormFieldScope.() -> Unit)? = null,
    characterCounter: @Composable (() -> Unit)? = null,
    content: @Composable FormFieldScope.() -> Unit,
) {
    val statusDescription = when (status) {
        FormFieldStatus.Success -> "Success"
        FormFieldStatus.Alert -> "Alert"
        FormFieldStatus.Error -> "Error"
        null -> null
    }
    val enabled = true // Could be parameterized if needed
    val scope = FormFieldScopeImpl(status)
    CompositionLocalProvider(LocalFormFieldStatus provides (status ?: FormFieldStatus.Success)) {
        Column(
            modifier = modifier.formFieldSemantics(
                label = null, // For best a11y, use the string overload
                status = statusDescription,
                supportingText = null,
                enabled = enabled,
            ),
        ) {
            label?.invoke(scope)
            scope.content()
            if (supportingText != null || characterCounter != null || status != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (status != null) {
//                        Icon(
//                            imageVector = status.icon.imageVector,
//                            contentDescription = statusDescription,
//                            tint = when (status) {
//                                FormFieldStatus.Success -> SparkTheme.colors.success
//                                FormFieldStatus.Alert -> SparkTheme.colors.alert
//                                FormFieldStatus.Error -> SparkTheme.colors.error
//                            },
//                        )
                    }
                    if (supportingText != null) {
                        val color = when (status) {
                            FormFieldStatus.Success -> SparkTheme.colors.success
                            FormFieldStatus.Alert -> SparkTheme.colors.alert
                            FormFieldStatus.Error -> SparkTheme.colors.error
                            null -> SparkTheme.colors.onSurface
                        }
                        Box(Modifier.weight(1f, fill = true)) {
                            CompositionLocalProvider(LocalContentColor provides color) {
                                supportingText.invoke(scope)
                            }
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                    characterCounter?.invoke()
                }
            }
        }
    }
}
