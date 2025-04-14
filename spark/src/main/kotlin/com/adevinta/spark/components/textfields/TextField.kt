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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text2.input.InputTransformation
import androidx.compose.foundation.text2.input.OutputTransformation
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.TextLayoutResult
import androidx.compose.foundation.text2.input.TextFieldDecorator
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardCapitalization
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.SparkTypography
import kotlinx.coroutines.flow.flowOf

/**
 * Outlined text input to get an input value from the user.
 * @param value the input [TextFieldValue] to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates values in
 * [TextFieldValue]. An updated [TextFieldValue] comes as a parameter of the callback
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
 * information about expected text
 * @param counter The optional counter to be displayed the the end bottom outside the text input container
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param trailingContent the optional trailing icon to be displayed at the end of the text field
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
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 *
 * @see MultilineTextField
 * @see SelectTextField
 */
@Composable
public fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: TextFieldCharacterCounter? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    trailingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
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
        trailingContent = trailingContent,
        state = state,
        stateMessage = stateMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        interactionSource = interactionSource,
    )
}

/**
 * Outlined text input to get an input value from the user.
 * @param value the input text to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback
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
 * information about expected text
 * @param counter The optional counter to be displayed the the end bottom outside the text input container
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container, note that
 * @param trailingContent the optional trailing icon to be displayed at the end of the text field
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
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 *
 * @see MultilineTextField
 * @see SelectTextField
 */
@Composable
public fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: TextFieldCharacterCounter? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    trailingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
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
        trailingIcon = trailingContent,
        state = state,
        stateMessage = stateMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        interactionSource = interactionSource,
    )
}

/**
 * Outlined text input to get an input value from the user using the new TextFieldState API.
 * @param state the [TextFieldState] that manages the text field's state
 * @param modifier a [Modifier] for this text field
 * @param enabled True controls the enabled state of the [TextField]. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param readOnly controls the editable state of the [TextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param inputTransformation transforms the input text before it is displayed
 * @param textStyle the style to be applied to the input text
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 * @param lineLimits the limits for the number of lines in the text field
 * @param onTextLayout callback that is executed when the text layout is calculated
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 * @param cursorBrush the brush to be used for the cursor
 * @param outputTransformation transforms the output text before it is displayed
 * @param decorator the decorator to be used for the text field
 * @param scrollState the scroll state for the text field
 *
 * @see BasicTextField
 */
@Composable
public fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = SparkTheme.typography.body1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(SparkTheme.colors.main),
    outputTransformation: OutputTransformation? = null,
    decorator: TextFieldDecorator? = null,
    scrollState: ScrollState = rememberScrollState(),
) {
    SparkTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        outputTransformation = outputTransformation,
        decorator = decorator,
        scrollState = scrollState,
    )
}

@Preview(
    group = "TextFields",
    name = "TextField intents",
)
@Composable
private fun TextFieldIntentPreview() {
    PreviewTheme {
        PreviewTextFields(
            state = TextFieldState.Success,
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

    TextField(
        value = "Input",
        onValueChange = {},
        enabled = true,
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
        enabled = true,
        state = state,
        stateMessage = stateMessage,
        required = true,
        label = "Label",
        placeholder = "Placeholder",
        helper = "Helper text",
        leadingContent = icon,
        trailingContent = icon,
        interactionSource = object : DefaultMutableInteractionSource() {
            override val interactions = flowOf(FocusInteraction.Focus(), PressInteraction.Press(Offset.Zero))
        },
    )

    Text("Unfocused without value")

    TextField(
        value = "",
        onValueChange = {},
        enabled = true,
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

internal abstract class DefaultMutableInteractionSource : MutableInteractionSource {
    override suspend fun emit(interaction: Interaction) {
    }

    override fun tryEmit(interaction: Interaction): Boolean = true
}
