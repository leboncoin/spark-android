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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.TextFieldCharacterCounter
import com.adevinta.spark.components.textfields.TextFieldDefault
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * CompositionLocal that propagates the current [FormFieldStatus] down to slot children so they
 * can tint themselves accordingly without explicit wiring.
 *
 * The default value is `null` (no status).
 */
public val LocalFormFieldStatus: ProvidableCompositionLocal<FormFieldStatus?> =
    compositionLocalOf { null }

/**
 * @param label composable rendered as the visual label
 * @param description composable rendered as the visual description
 * @param supportLabel composable rendered as the visual support label
 * @param statusMessage composable rendered as the visual status message
 * @param counter optional character counter displayed right-aligned alongside the support label row
 * @param content the required interactive control; retains its own full semantics
 */
@InternalSparkApi
@Composable
internal fun SparkFormField(
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
    val statusMessageColor = when (status) {
        FormFieldStatus.Success -> SparkTheme.colors.success
        FormFieldStatus.Alert -> SparkTheme.colors.alert
        FormFieldStatus.Error -> SparkTheme.colors.error
        null -> SparkTheme.colors.onSurface
    }

    if (required) {
        stringResource(id = R.string.spark_textfield_mandatory_content_description)
    } else {
        null
    }

    val stateIcon = TextFieldDefault.getStatusIcon(status)

    CompositionLocalProvider(LocalFormFieldStatus provides status) {
        Column(
            modifier = modifier
                .semantics(mergeDescendants = true) {}
                .sparkUsageOverlay(),
        ) {
            if (label != null) {
                Box(modifier = Modifier.invisibleSemantic()) {
                    ProvideTextStyle(SparkTheme.typography.body2) {
                        label()
                    }
                }
            }

            if (description != null) {
                Box(modifier = Modifier.invisibleSemantic()) {
                    ProvideTextStyle(SparkTheme.typography.body1) {
                        CompositionLocalProvider(LocalContentColor provides SparkTheme.colors.onSurface) {
                            description()
                        }
                    }
                }
            }

            // Content is NOT wrapped — it keeps its own full semantics (e.g. text field role,
            // checked state, slider actions) untouched.
            content()

            if (supportLabel != null || counter != null) {
                Box(modifier = Modifier.invisibleSemantic()) {
                    ProvideTextStyle(SparkTheme.typography.caption) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (supportLabel != null) {
                                Box(modifier = Modifier.weight(1f, fill = true)) {
                                    CompositionLocalProvider(LocalContentColor provides SparkTheme.colors.onSurface) {
                                        supportLabel()
                                    }
                                }
                            }
                            if (counter != null) {
                                val counterContentDescription = pluralStringResource(
                                    id = R.plurals.spark_textfield_counter_content_description,
                                    count = counter.count,
                                    counter.count,
                                    counter.maxCharacter,
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .semantics { contentDescription = counterContentDescription },
                                    text = "${counter.count}/${counter.maxCharacter}",
                                )
                            }
                        }
                    }
                }
            }

            if (statusMessage != null) {
                Box(modifier = Modifier.invisibleSemantic()) {
                    ProvideTextStyle(SparkTheme.typography.caption) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            stateIcon?.invoke(Modifier.padding(end = 4.dp))
                            CompositionLocalProvider(LocalContentColor provides statusMessageColor) {
                                Box(modifier = Modifier.weight(1f, fill = true)) {
                                    statusMessage()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
