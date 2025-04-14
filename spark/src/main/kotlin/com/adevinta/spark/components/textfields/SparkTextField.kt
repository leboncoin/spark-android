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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text2.input.InputTransformation
import androidx.compose.foundation.text2.input.OutputTransformation
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.TextLayoutResult
import androidx.compose.foundation.text2.input.TextFieldDecorator
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.tokens.EmphasizeDim3
import com.adevinta.spark.tools.modifiers.SlotArea
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

// FIXME: Duplicate of Material OutlinedTextField while the counter is not supported on their end
// b/236761202
@InternalSparkApi
@Composable
internal fun SparkTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    enabled: Boolean,
    readOnly: Boolean,
    required: Boolean,
    label: String?,
    placeholder: String?,
    helper: String?,
    counter: TextFieldCharacterCounter?,
    leadingContent: @Composable (AddonScope.() -> Unit)?,
    trailingContent: @Composable (AddonScope.() -> Unit)?,
    state: TextFieldState?,
    stateMessage: String?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = sparkOutlinedTextFieldColors()
    val density = LocalDensity.current
    val labelContentDescriptionModifier = if (label != null) {
        val labelContentDescription = computeLabelContentDescription(
            label = label,
            required = required,
        )
        Modifier
            // Merge semantics at the beginning of the modifier chain to ensure padding is
            // considered part of the text field.
            .semantics(mergeDescendants = true) { contentDescription = labelContentDescription }
            .padding(top = with(density) { (SparkTheme.typography.body2.fontSize / 2).toDp() })
    } else {
        Modifier
    }
    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        @OptIn(ExperimentalMaterial3Api::class)
        BasicTextField(
            value = value,
            modifier = modifier
                .then(other = labelContentDescriptionModifier)
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth,
                    minHeight = TextFieldMinHeight,
                )
                .sparkUsageOverlay(),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = SparkTheme.typography.body1.merge(TextStyle(colors.textColor(enabled).value)),
            cursorBrush = SolidColor(colors.cursorColor(state).value),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = @Composable { innerTextField ->
                val counterComposable = counterText(counter)
                val stateIcon = TextFieldDefault.getStatusIcon(state = state)
                val supportingTextComposable = supportText(
                    helper = helper,
                    state = state,
                    stateMessage = stateMessage,
                    counterComposable = counterComposable,
                    stateIcon = stateIcon,
                )

                SparkDecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = { Label(text = label, required = required) },
                    interactionSource = interactionSource,
                    colors = colors,
                    readOnly = readOnly,
                    placeholder = { PlaceHolder(text = placeholder) },
                    supportingText = supportingTextComposable,
                    leadingIcon = leadingContent,
                    trailingIcon = trailingContent,
                    singleLine = singleLine,
                    enabled = enabled,
                    state = state,
                ) {
                    OutlinedBorderContainerBox(
                        enabled,
                        readOnly,
                        state,
                        interactionSource,
                        colors,
                        SparkTheme.shapes.large,
                    )
                }
            },
            onTextLayout = {},
        )
    }
}

@InternalSparkApi
@Composable
internal fun SparkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    readOnly: Boolean,
    required: Boolean,
    label: String?,
    placeholder: String?,
    helper: String?,
    counter: TextFieldCharacterCounter?,
    leadingIcon: @Composable (AddonScope.() -> Unit)?, // Should we rename it to leadingContent?
    trailingIcon: @Composable (AddonScope.() -> Unit)?, // Should we rename it to trailingContent?
    state: TextFieldState?,
    stateMessage: String?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = sparkOutlinedTextFieldColors()
    val labelContentDescriptionModifier = if (label != null) {
        val labelContentDescription = computeLabelContentDescription(
            label = label,
            required = required,
        )
        Modifier
            // Merge semantics at the beginning of the modifier chain to ensure padding is
            // considered part of the text field.
            .semantics(mergeDescendants = true) { contentDescription = labelContentDescription }
            .padding(top = OutlinedTextFieldTopPadding)
    } else {
        Modifier
    }
    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        @OptIn(ExperimentalMaterial3Api::class)
        BasicTextField(
            value = value,
            modifier = modifier
                .then(other = labelContentDescriptionModifier)
                .defaultMinSize(
                    minWidth = 48.dp,
                    minHeight = TextFieldMinHeight,
                )
                .sparkUsageOverlay(),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = SparkTheme.typography.body1.merge(TextStyle(colors.textColor(enabled).value)),
            cursorBrush = SolidColor(colors.cursorColor(state).value),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox = @Composable { innerTextField ->
                val counterComposable = counterText(counter)
                val stateIcon = TextFieldDefault.getStatusIcon(state = state)
                val supportingTextComposable = supportText(
                    helper = helper,
                    state = state,
                    stateMessage = stateMessage,
                    counterComposable = counterComposable,
                    stateIcon = stateIcon,
                )

                SparkDecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = { Label(text = label, required = required) },
                    interactionSource = interactionSource,
                    colors = colors,
                    readOnly = readOnly,
                    placeholder = { PlaceHolder(text = placeholder) },
                    supportingText = supportingTextComposable,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    singleLine = singleLine,
                    enabled = enabled,
                    state = state,
                ) {
                    OutlinedBorderContainerBox(
                        enabled,
                        readOnly,
                        state,
                        interactionSource,
                        colors,
                        SparkTheme.shapes.large,
                    )
                }
            },
            onTextLayout = {},
        )
    }
}

/**
 * Composable that draws a default container for [SparkTextField] with a border stroke. You
 * can use it to draw a border stroke in your custom text field based on
 * [OutlinedBorderContainerBox]. The [SparkTextField] applies it automatically.
 *
 * @param enabled whether the text field is enabled
 * @param readOnly whether the text field's value can't be edited
 * @param status whether the text field's current value is in error, success or alert
 * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
 * the text field is in focus or not
 * @param colors [TextFieldColors] used to resolve colors of the text field
 */
@ExperimentalMaterial3Api
@Composable
internal fun OutlinedBorderContainerBox(
    enabled: Boolean,
    readOnly: Boolean,
    status: TextFieldState?,
    interactionSource: InteractionSource,
    colors: DefaultSparkTextFieldColors,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    val borderStroke = animateBorderStrokeAsState(
        enabled,
        readOnly,
        status,
        interactionSource,
        colors,
    )
    val containerColor = animateColorAsState(
        targetValue = colors.containerColor(enabled).value,
        animationSpec = tween(durationMillis = AnimationDuration),
    )
    Box(
        modifier
            .border(borderStroke.value, shape)
            .textFieldBackground(containerColor::value, shape),
    )
}

/**
 * Replacement for Modifier.background which takes color as a State to avoid recomposition while
 * animating.
 */
internal fun Modifier.textFieldBackground(
    color: ColorProducer,
    shape: Shape,
): Modifier =
    this.drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        onDrawBehind { drawOutline(outline, color = color()) }
    }

@Composable
internal fun animateBorderStrokeAsState(
    enabled: Boolean,
    readOnly: Boolean,
    state: TextFieldState?,
    interactionSource: InteractionSource,
    colors: DefaultSparkTextFieldColors,
): State<BorderStroke> {
    val focused by interactionSource.collectIsFocusedAsState()
    val indicatorColor = colors.indicatorColor(enabled, readOnly, state, interactionSource)
    val targetThickness = if ((focused || state != null) && !readOnly) {
        OutlinedTextFieldDefaults.FocusedBorderThickness
    } else {
        OutlinedTextFieldDefaults.UnfocusedBorderThickness
    }
    val animatedThickness = if (enabled && !readOnly) {
        animateDpAsState(targetThickness, tween(durationMillis = 150), label = "borderThickness")
    } else {
        rememberUpdatedState(OutlinedTextFieldDefaults.UnfocusedBorderThickness)
    }
    return rememberUpdatedState(
        BorderStroke(animatedThickness.value, SolidColor(indicatorColor.value)),
    )
}

@Stable
public data class TextFieldCharacterCounter(val count: Int, val maxCharacter: Int)

@Composable
private fun Label(text: String?, required: Boolean) {
    if (text != null) {
        Row(modifier = Modifier.clearAndSetSemantics {}) {
            Text(
                text = text,
                modifier = Modifier.weight(weight = 1f, fill = false),
            )
            if (required) {
                EmphasizeDim3 {
                    Text(
                        text = "*",
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceHolder(text: String?) {
    if (text != null) {
        Box {
            Text(text = text)
        }
    }
}

@Composable
private fun counterText(
    charCounter: TextFieldCharacterCounter?,
): (@Composable (Modifier) -> Unit)? = charCounter?.let { counter ->
    { modifier ->
        val contentDescription = pluralStringResource(
            id = R.plurals.spark_textfield_counter_content_description,
            count = counter.count,
            counter.count,
            counter.maxCharacter,
        )
        Text(
            modifier = modifier.semantics { this.contentDescription = contentDescription },
            text = "${counter.count}/${counter.maxCharacter}",
        )
    }
}

@Composable
private fun supportText(
    helper: String?,
    state: TextFieldState?,
    stateMessage: String?,
    counterComposable: @Composable ((Modifier) -> Unit)?,
    stateIcon: @Composable ((Modifier) -> Unit)?,
): (@Composable () -> Unit)? = if (
    (stateMessage != null && state != null) || helper != null || counterComposable != null
) {
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            stateIcon?.invoke(Modifier.padding(end = 4.dp))
            // Prioritize the state message if there's one and fallback to the helper otherwise
            val message = state?.let { stateMessage } ?: helper
            val stateMessageContentDescriptionModifier = if (state != null) {
                val stateMessageContentDescription = computeStateMessageContentDescription(
                    state = state,
                    stateMessage = stateMessage,
                )
                Modifier.semantics { contentDescription = stateMessageContentDescription }
            } else {
                Modifier
            }
            Text(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .then(other = stateMessageContentDescriptionModifier),
                text = message.orEmpty(),
            )
            counterComposable?.invoke(Modifier.padding(start = 8.dp))
        }
    }
} else {
    null
}

@Composable
private fun computeLabelContentDescription(
    label: String,
    required: Boolean,
): String = buildString {
    append(label)
    if (required) {
        appendLine()
        append(stringResource(id = R.string.spark_textfield_mandatory_content_description))
    }
}

@Composable
private fun computeStateMessageContentDescription(
    state: TextFieldState,
    stateMessage: String?,
): String = buildString {
    val stateStatusContentDescription = when (state) {
        TextFieldState.Success -> stringResource(id = R.string.spark_textfield_state_success_content_description)
        TextFieldState.Alert -> stringResource(id = R.string.spark_textfield_state_alert_content_description)
        TextFieldState.Error -> stringResource(id = R.string.spark_textfield_state_error_content_description)
    }
    append(stateStatusContentDescription)
    if (stateMessage != null) {
        appendLine()
        append(stateMessage)
    }
}

internal object TextFieldDefault {

    @Composable
    internal fun getStatusIcon(state: TextFieldState?): (@Composable (Modifier) -> Unit)? {
        state ?: return null

        return { modifier ->
            Icon(
                modifier = modifier.size(18.dp),
                sparkIcon = state.icon,
                contentDescription = null,
            )
        }
    }
}

/*
This padding is used to allow label not overlap with the content above it. This 8.dp will work
for default cases when developers do not override the label's font size. If they do, they will
need to add additional padding themselves
*/
internal val OutlinedTextFieldTopPadding = 8.dp

// The default Material height is 56.dp with 16.dp vertical padding, since we want 12.dp for ours we subtract 8.dp
internal val TextFieldMinHeight = 44.dp

@Preview(
    group = "TextFields",
    name = "TextField Slots",
)
@Composable
internal fun TextFieldSlotsPreview() {
    PreviewTheme {
        val icon: @Composable (AddonScope.() -> Unit) = @Composable {
            SlotArea(color = LocalContentColor.current) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
            }
        }

        TextField(
            value = "din.djarin@adevinta.com",
            onValueChange = {},
            enabled = true,
            required = true,
            label = "Label",
            placeholder = "Placeholder",
            helper = "Helper helper helper helper helper helper Helper helper helper helper helper helper " +
                "helper helper helper helper helper",
            state = TextFieldState.Success,
            counter = TextFieldCharacterCounter(10, 20),
            leadingContent = icon,
        )

        TextField(
            value = "din.djarin@adevinta.com",
            onValueChange = {},
            enabled = false,
            required = true,
            label = "Label",
            placeholder = "Placeholder",
            helper = "Helper helper helper helper helper helper Helper helper helper helper helper helper " +
                "helper helper helper helper helper",
            counter = TextFieldCharacterCounter(10, 20),
            state = TextFieldState.Success,
            leadingContent = icon,
            trailingContent = icon,
        )
    }
}

@InternalSparkApi
@Composable
internal fun SparkTextField(
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
    val colors = sparkOutlinedTextFieldColors()
    val density = LocalDensity.current
    val interactionSourceState = interactionSource ?: remember { MutableInteractionSource() }

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        @OptIn(ExperimentalMaterial3Api::class)
        BasicTextField(
            state = state,
            modifier = modifier
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth,
                    minHeight = TextFieldMinHeight,
                )
                .sparkUsageOverlay(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.merge(TextStyle(colors.textColor(enabled).value)),
            cursorBrush = cursorBrush,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSourceState,
            lineLimits = lineLimits,
            onTextLayout = onTextLayout,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            decorator = decorator,
            scrollState = scrollState,
        )
    }
}
