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
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.menu.DropdownMenu
import com.adevinta.spark.components.popover.PlainTooltip
import com.adevinta.spark.components.popover.TooltipBox
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ArrowHorizontalDown
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.SparkTypography
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlinx.coroutines.launch

/**
 * Outlined text input to get an input value from a list of elements selectable through a dropdown by the user.
 * @param value the input [TextFieldValue] to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates values in
 * [TextFieldValue]. An updated [TextFieldValue] comes as a parameter of the callback
 * @param expanded Whether Dropdown Menu should be expanded or not
 * @param onExpandedChange Executes when the user clicks on the ExposedDropdownMenuBox
 * @param onDismissRequest Called when the user requests to dismiss the menu, such as by
 * tapping outside the menu's bounds
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
 * @param properties PopupProperties for further customization of this popup's behavior.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 * @param dropdownContent The content to be displayed inside ExposedDropdownMenuBox.
 *
 * @see TextField
 * @see MultilineTextField
 */
@OptIn(ExperimentalMaterial3Api::class)
@Deprecated(
    message = "Use the Dropdown component instead for read only usecases.",
    replaceWith = ReplaceWith(
        """
        Dropdown(value=value, expanded=expanded, onExpandedChange=onExpandedChange, onDismissRequest=onDismissRequest,
         modifier=modifier, enabled=enabled, required=required, label=label, placeholder=placeholder, helper=helper,
          leadingContent=leadingContent, state=state, stateMessage=stateMessage,
           visualTransformation=visualTransformation, interactionSource=interactionSource, dropdownContent)""",
    ),
)
@Composable
public fun SelectTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    properties: PopupProperties = PopupProperties(focusable = readOnly, dismissOnClickOutside = readOnly),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    dropdownContent: @Composable ColumnScope.() -> Unit,
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            enabled = enabled,
            readOnly = readOnly,
            required = required,
            label = label,
            placeholder = placeholder,
            helper = helper,
            leadingContent = leadingContent,
            trailingContent = {
                SparkSelectTrailingIcon(
                    expanded = expanded,
                )
            },
            state = state,
            stateMessage = stateMessage,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
        )
        // Use Exposed when b/205589613 is fixed
//        ExposedDropdownMenu(
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.exposedDropdownSize(),
            properties = properties,
            content = dropdownContent,
        )
    }
}

/**
 * Outlined text input to get an input value from a list of elements selectable through a dropdown by the user.
 * @param value the input text to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback
 * @param expanded Whether Dropdown Menu should be expanded or not
 * @param onExpandedChange Executes when the user clicks on the ExposedDropdownMenuBox
 * @param onDismissRequest Called when the user requests to dismiss the menu, such as by
 * tapping outside the menu's bounds
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
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param state indicates the validation state of the text field. The label, outline, leading & trailing content are tinted by the state color.
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
 * @param properties commentCountPopupProperties for further customization of this popup's behavior.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 * @param dropdownContent The content to be displayed inside ExposedDropdownMenuBox.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Deprecated(
    message = "Use the Dropdown component instead for read only usecases.",
    replaceWith = ReplaceWith(
        """
        Dropdown(value=value, expanded=expanded, onExpandedChange=onExpandedChange, onDismissRequest=onDismissRequest,
         modifier=modifier, enabled=enabled, required=required, label=label, placeholder=placeholder, helper=helper,
          leadingContent=leadingContent, state=state, stateMessage=stateMessage,
           visualTransformation=visualTransformation, interactionSource=interactionSource, dropdownContent)""",
    ),
)
@Composable
public fun SelectTextField(
    value: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    properties: PopupProperties = PopupProperties(focusable = readOnly, dismissOnClickOutside = readOnly),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    dropdownContent: @Composable ColumnScope.() -> Unit,
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            enabled = enabled,
            readOnly = readOnly,
            required = required,
            label = label,
            placeholder = placeholder,
            helper = helper,
            leadingContent = leadingContent,
            trailingContent = {
                SparkSelectTrailingIcon(
                    expanded = expanded,
                )
            },
            state = state,
            stateMessage = stateMessage,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
        )
        // Use Exposed when b/205589613 is fixed
//        ExposedDropdownMenu(
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .exposedDropdownSize()
                .padding(16.dp),
            properties = properties,
            content = dropdownContent,
        )
    }
}

/**
 * Outlined text input to get an input value from a list of elements selectable through a DropdownMenu by the user.
 * @param value the input text to be shown in the text field
 * @param expanded Whether Dropdown Menu should be expanded or not
 * @param onExpandedChange Executes when the user clicks on the ExposedDropdownMenuBox
 * @param onDismissRequest Called when the user requests to dismiss the menu, such as by
 * tapping outside the menu's bounds
 * @param modifier a [Modifier] for this text field
 * @param enabled True controls the enabled state of the [TextField]. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param required add an asterisk to the label to indicate that this field is required and read it as "label mandatory"
 * but doesn't do anything else so it's up to the developer to handle the behavior.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [SparkTypography.body2] when the text field is in focus and
 * [SparkTypography.body1] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [SparkTypography.body1]
 * @param helper The optional helper text to be displayed at the bottom outside the text input container that give some
 * informations about expected text
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param state indicates the validation state of the text field. The label, outline, leading & trailing content are tinted by the state color.
 * @param stateMessage the optional state text to be displayed at the helper position that give more information about
 * the state, it's displayed only when [state] is not null.
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation]
 * to create a password text field. By default no visual transformation is applied
 * @param properties commentCountPopupProperties for further customization of this popup's behavior.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 * @param dropdownContent The content to be displayed inside ExposedDropdownMenuBox.
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
public fun Dropdown(
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    properties: PopupProperties = PopupProperties(focusable = true, dismissOnClickOutside = true),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    dropdownContent: @Composable ColumnScope.() -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            // Don't expand the menu when the Textfield is disabled
            if (enabled) onExpandedChange(it)
        },
    ) {
        TextField(
            value = value,
            onValueChange = { },
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            enabled = enabled,
            readOnly = true,
            required = required,
            label = label,
            placeholder = placeholder,
            helper = helper,
            leadingContent = leadingContent,
            trailingContent = {
                SparkSelectTrailingIcon(
                    expanded = expanded,
                )
            },
            state = state,
            stateMessage = stateMessage,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
        )
        // Use Exposed when b/205589613 is fixed
//        ExposedDropdownMenu(
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .exposedDropdownSize()
                .padding(16.dp),
            properties = properties,
            content = dropdownContent,
        )
    }
}

/**
 * Outlined text input to get an input value from a list of elements selectable through a custom container (BottomSheet / Dialog / Screen) by the user.
 * @param value the input [TextFieldValue] to be shown in the text field
 * @param expanded Whether Dropdown Menu should be expanded or not
 * @param onClick Called when the user requests to expand the details (in a dropdown, BottomSheet or other container
 * components).
 * @param onClickLabel The label to be used for the [onClick] action. It is used by accessibility services to
 * describe the action to the user. It should describe where the user will be taken to when the action is
 * performed. For example, "Open check-in selection dates" or "Open category selection".
 * @param modifier a [Modifier] for this text field
 * @param enabled True controls the enabled state of the [TextField]. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param required add an asterisk to the label to indicate that this field is required and read it as "label mandatory"
 * but doesn't do anything else so it's up to the developer to handle the behavior.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [SparkTypography.body2] when the text field is in focus and
 * [SparkTypography.body1] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [SparkTypography.body1]
 * @param helper The optional helper text to be displayed at the bottom outside the text input container that give some
 * informations about expected text
 * @param leadingContent the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param state indicates the validation state of the text field. The label, outline, leading & trailing content are
 * tinted by the state color.
 * @param stateMessage the optional state text to be displayed at the helper position that give more information about
 * the state, it's displayed only when [state] is not null.
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation]
 * to create a password text field. By default no visual transformation is applied
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 *
 * @see TextField
 * @see MultilineTextField
 */
@Composable
public fun Dropdown(
    value: String,
    expanded: Boolean,
    onClick: () -> Unit,
    onClickLabel: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    state: TextFieldState? = null,
    stateMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .sparkUsageOverlay(),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { },
            enabled = enabled,
            readOnly = true,
            required = required,
            label = label,
            placeholder = placeholder,
            helper = helper,
            leadingContent = leadingContent,
            trailingContent = {
                SparkSelectTrailingIcon(
                    expanded = expanded,
                )
            },
            state = state,
            stateMessage = stateMessage,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
        )
        // Transparent clickable surface on top of TextField
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(SparkTheme.shapes.large)
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClickLabel = onClickLabel,
                    onClick = onClick,
                ),
            color = Color.Transparent,
        ) { }
    }
}

/**
 * Default trailing icon for Exposed Dropdown Menu.
 *
 * @param expanded Whether [ExposedDropdownMenuBoxScope.ExposedDropdownMenu]
 * is expanded or not. Affects the appearance of the icon.
 */
@ExperimentalSparkApi
@Composable
public fun SparkSelectTrailingIcon(
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    // Clear semantics here as otherwise icon will be a11y focusable but without an
    // action. When there's an API to check if Talkback is on, developer will be able to
    // expand the menu on icon click in a11y mode only esp. if using their own custom
    // trailing icon.
    Icon(
        sparkIcon = SparkIcons.ArrowHorizontalDown,
        size = IconSize.Medium,
        contentDescription = null,
        modifier = modifier.rotate(
            if (expanded) 180f else 0f,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SelectTextFieldTapPreview() {
    PreviewTheme {
        val tooltipState = remember { TooltipState() }
        val coroutineScope = rememberCoroutineScope()
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text("Tapped")
                }
            },
            state = tooltipState,
        ) {
            Dropdown(
                value = "Input",
                onClick = {
                    coroutineScope.launch {
                        tooltipState.show()
                    }
                },
                onClickLabel = "Open Tooltip",
                enabled = true,
                required = true,
                label = "Label",
                placeholder = "Placeholder",
                helper = "Helper text",
                expanded = false,
            )
        }
    }
}
