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
package com.adevinta.spark.components.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.DeleteOutline
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.SparkTypography
import kotlinx.coroutines.flow.flowOf

/**
 * Outlined text input to get an input value from the user.
 * Which can scroll its content when [value] takes more place than the available width.
 * We can also set the amount of maximum lines it can expand before overscrolling.
 * @param value the input [TextFieldValue] to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates values in
 * [TextFieldValue]. An updated [TextFieldValue] comes as a parameter of the callback
 * @param onCancelClick the callback that is triggered when the user try to cancel his input. Most of the times it would
 * require removing the content from The [MultilineTextField]
 * @param modifier a [Modifier] for this text field
 * @param enabled True controls the enabled state of the [TextField]. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param readOnly controls the editable state of the [TextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param required add an asterisk to the label to indicate that this field is required and read it as "label mandatory"
 * but doesn't do anything else so it's up to the developer to handle the behavior.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [SparkTypography.body2] when the text field is in focus and
 * [SparkTypography.body1] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [SparkTypography.body1]
 * @param helper The optional helper text to be displayed at the bottom outside the text input container that give some
 * informations about expected text
 * @param counter The optional counter to be displayed the the end bottom outside the text input container
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param state indicates the validation state of the text field. The label, outline, leading & trailing content are
 * tinted by the state color.
 * @param stateMessage the optional state text to be displayed at the helper position that give more information about
 * the state, it's displayed only when [state] is not null.
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation]
 * to create a password text field. By default no visual transformation is applied
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 * @param maxLines the maximum height in terms of maximum number of visible lines. It is required
 *  * that 1 <= [minLines] <= [maxLines].
 * @param minLines the minimum height in terms of minimum number of visible lines. It is required
 *  * that 1 <= [minLines] <= [maxLines].
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 *
 * @see TextField
 * @see SelectTextField
 */
@Composable
public fun MultilineTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: TextFieldCharacterCounter? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val trailingIcon: (@Composable AddonScope.() -> Unit)? = if (value.text.isNotBlank()) {
        {
            Icon(
                modifier = Modifier
                    .clickable {
                        onCancelClick()
                    }
                    .semantics {
                        role = Role.Button
                    },
                sparkIcon = SparkIcons.DeleteOutline,
                contentDescription = stringResource(R.string.spark_textfield_delete_a11y),
            )
        }
    } else {
        null
    }

    SparkTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        required = required,
        label = label,
        placeholder = placeholder,
        helper = helper,
        counter = counter,
        leadingContent = leadingContent,
        trailingContent = trailingIcon,
        state = state,
        stateMessage = stateMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = false,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
    )
}

/**
 * Outlined text input to get an input value from the user.
 * Which can scroll its content when [value] takes more place than the available width.
 * We can also set the amount of maximum lines it can expand before overscrolling.
 * @param value the input text to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback
 * @param onCancelClick the callback that is triggered when the user try to cancel his input. Most of the times it would
 * require removing the content from The [MultilineTextField]
 * @param modifier a [Modifier] for this text field
 * @param enabled True controls the enabled state of the [TextField]. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param readOnly controls the editable state of the [TextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param required add an asterisk to the label to indicate that this field is required and read it as "label mandatory"
 * but doesn't do anything else so it's up to the developer to handle the behavior.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [SparkTypography.body2] when the text field is in focus and
 * [SparkTypography.body1] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [SparkTypography.body1]
 * @param helper The optional helper text to be displayed at the bottom outside the text input container that give some
 * informations about expected text
 * @param counter The optional counter to be displayed the the end bottom outside the text input container
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param state indicates the validation state of the text field. The label, outline, leading & trailing content are
 * tinted by the state color.
 * @param stateMessage the optional state text to be displayed at the helper position that give more information about
 * the state, it's displayed only when [state] is not null.
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation]
 * to create a password text field. By default no visual transformation is applied
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 * @param maxLines the maximum height in terms of maximum number of visible lines. It is required
 *  * that 1 <= [minLines] <= [maxLines].
 * @param minLines the minimum height in terms of minimum number of visible lines. It is required
 *  * that 1 <= [minLines] <= [maxLines].
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 *
 * @see TextField
 * @see SelectTextField
 */
@Composable
public fun MultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: TextFieldCharacterCounter? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val trailingIconComposable: (@Composable AddonScope.() -> Unit)? = if (value.isNotBlank()) {
        {
            Icon(
                modifier = Modifier
                    .clickable(onClick = onCancelClick)
                    .semantics {
                        role = Role.Button
                    },
                sparkIcon = SparkIcons.DeleteOutline,
                contentDescription = stringResource(R.string.spark_textfield_delete_a11y),
            )
        }
    } else {
        null
    }

    SparkTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        required = required,
        label = label,
        placeholder = placeholder,
        helper = helper,
        counter = counter,
        leadingIcon = leadingContent,
        trailingIcon = trailingIconComposable,
        state = state,
        stateMessage = stateMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = false,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
    )
}

@Preview(
    group = "TextFields",
    name = "MultilineTextField intents",
)
@Composable
private fun MultilineTextFieldIntentPreview() {
    PreviewTheme {
        PreviewTextFields(
            state = null,
            stateMessage = "Helper text",
        )
    }
}

@Composable
private fun ColumnScope.PreviewTextFields(
    state: TextFieldState?,
    stateMessage: String?,
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
        counter = TextFieldCharacterCounter(242424242, 242424242),
        maxLines = 3,
        helper = "Placeholder Placeholder Placeholder Placeholder",
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
        counter = TextFieldCharacterCounter(0, 300),
        required = true,
        label = "Label",
        placeholder = "Placeholder",
        helper = "Helper text",
        leadingContent = icon,
        interactionSource = object : DefaultMutableInteractionSource() {
            override val interactions = flowOf(FocusInteraction.Focus(), PressInteraction.Press(Offset.Zero))
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
