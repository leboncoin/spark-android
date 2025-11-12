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
package com.adevinta.spark.textfields

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.AddonScope
import com.adevinta.spark.components.textfields.Dropdown
import com.adevinta.spark.components.textfields.MultilineTextField
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldCharacterCounter
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.H_SCROLL
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

internal class TextFieldDocScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = H_SCROLL,
    )

    @Test
    fun textFieldShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Row {
                Textfields(
                    state = null,
                    stateMessage = "Helper text",
                )
                Textfields(
                    state = TextFieldState.Error,
                    stateMessage = "Error text",
                )
                Textfields(
                    state = TextFieldState.Alert,
                    stateMessage = "Alert text",
                )
                Textfields(
                    state = TextFieldState.Success,
                    stateMessage = "Success text",
                )
            }
        }
    }

    @Test
    fun multilineTextFieldShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Row {
                MultilineTextfields(
                    state = null,
                    stateMessage = "Helper text",
                )
                MultilineTextfields(
                    state = TextFieldState.Error,
                    stateMessage = "Error text",
                )
                MultilineTextfields(
                    state = TextFieldState.Alert,
                    stateMessage = "Alert text",
                )
                MultilineTextfields(
                    state = TextFieldState.Success,
                    stateMessage = "Success text",
                )
            }
        }
    }

    @Test
    fun selectTextFieldShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Row {
                SelectTextfields(
                    state = null,
                    stateMessage = "Helper text",
                )
                SelectTextfields(
                    state = TextFieldState.Error,
                    stateMessage = "Error text",
                )
                SelectTextfields(
                    state = TextFieldState.Alert,
                    stateMessage = "Alert text",
                )
                SelectTextfields(
                    state = TextFieldState.Success,
                    stateMessage = "Success text",
                )
            }
        }
    }

    @Composable
    private fun Textfields(
        state: TextFieldState?,
        stateMessage: String?,
        enabled: Boolean = true,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val icon: @Composable (AddonScope.() -> Unit) = @Composable {
                Icon(
                    sparkIcon = SparkIcons.LikeFill,
                    contentDescription = null,
                    size = IconSize.Medium,
                )
            }

            Text("Unfocused with value")

            TextField(
                value = "Input",
                onValueChange = {},
                enabled = enabled,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                trailingContent = icon,
            )

            Text("Focused without value")

            TextField(
                value = "",
                onValueChange = {},
                enabled = enabled,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                trailingContent = icon,
                interactionSource = object : DefaultMutableInteractionSource() {
                    override val interactions =
                        flowOf(FocusInteraction.Focus(), PressInteraction.Press(Offset.Zero))
                },
            )

            Text("Unfocused without value")

            TextField(
                value = "",
                onValueChange = {},
                enabled = enabled,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                trailingContent = icon,
            )
        }
    }

    @Composable
    private fun MultilineTextfields(
        state: TextFieldState?,
        stateMessage: String?,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val icon: @Composable (AddonScope.() -> Unit) = @Composable {
                Icon(
                    sparkIcon = SparkIcons.LikeFill,
                    contentDescription = null,
                    size = IconSize.Medium,
                )
            }

            Text("Unfocused with value")

            MultilineTextField(
                value = "Input",
                onValueChange = {},
                onCancelClick = {},
                enabled = true,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                counter = TextFieldCharacterCounter(12, 24),
                minLines = 3,
                helper = "Helper text",
                leadingContent = icon,
            )

            Text("Focused without value")

            MultilineTextField(
                value = "",
                onValueChange = {},
                onCancelClick = {},
                enabled = true,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                minLines = 3,
                interactionSource = object : DefaultMutableInteractionSource() {
                    override val interactions =
                        flowOf(FocusInteraction.Focus(), PressInteraction.Press(Offset.Zero))
                },
            )

            Text("Unfocused without value")

            MultilineTextField(
                value = "",
                onValueChange = {},
                onCancelClick = {},
                enabled = true,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                minLines = 3,
            )
        }
    }

    @OptIn(ExperimentalSparkApi::class)
    @Composable
    private fun SelectTextfields(
        state: TextFieldState?,
        stateMessage: String?,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val icon: @Composable (AddonScope.() -> Unit) = @Composable {
                Icon(
                    sparkIcon = SparkIcons.LikeFill,
                    contentDescription = null,
                    size = IconSize.Medium,
                )
            }

            Text("Unfocused with value")

            Dropdown(
                value = "Input",
                expanded = false,
                onExpandedChange = {},
                onDismissRequest = {},
                enabled = true,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                dropdownContent = {},
            )

            Text("Focused without value")

            Dropdown(
                value = "",
                expanded = false,
                onExpandedChange = {},
                onDismissRequest = {},
                enabled = true,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                interactionSource = object : DefaultMutableInteractionSource() {
                    override val interactions =
                        flowOf(FocusInteraction.Focus(), PressInteraction.Press(Offset.Zero))
                },
                dropdownContent = {},
            )

            Text("Unfocused without value")

            Dropdown(
                value = "",
                expanded = false,
                onExpandedChange = {},
                onDismissRequest = {},
                enabled = true,
                state = state,
                stateMessage = stateMessage,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                leadingContent = icon,
                dropdownContent = {},
            )
        }
    }

    internal abstract class DefaultMutableInteractionSource : MutableInteractionSource {
        override suspend fun emit(interaction: Interaction) {
        }

        override fun tryEmit(interaction: Interaction): Boolean = true
    }
}
