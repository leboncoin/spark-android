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
package com.adevinta.spark.components.stepper.internal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.stepper.canDecrement
import com.adevinta.spark.components.stepper.canIncrement
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.AnimationDuration
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.Minus
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tokens.dim5
import com.adevinta.spark.tokens.transparent
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlinx.coroutines.flow.drop

@Composable
internal fun SparkStepperInput(
    textFieldState: TextFieldState,
    value: Int?,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onCommit: () -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..10,
    placeholder: String = "-",
    suffix: String = "",
    step: Int = 1,
    enabled: Boolean = true,
    status: FormFieldStatus? = null,
    flexible: Boolean = false,
    testTag: String? = null,
) {
    stepperInputValidator(step, range)

    val canDecrement = enabled && canDecrement(value, range)
    val canIncrement = enabled && canIncrement(value, range)

    val textFieldInteractionSource = remember { MutableInteractionSource() }
    val isFocused by textFieldInteractionSource.collectIsFocusedAsState()
    val currentOnCommit by rememberUpdatedState(onCommit)

    LaunchedEffect(textFieldInteractionSource) {
        snapshotFlow { isFocused }.drop(1).collect { focused ->
            if (!focused) currentOnCommit()
        }
    }

    val containerBgColor by animateColorAsState(
        targetValue = if (enabled) {
            SparkTheme.colors.onSurface.transparent
        } else {
            SparkTheme.colors.onSurface.dim5
        },
        animationSpec = tween(durationMillis = AnimationDuration),
        label = "containerBg",
    )
    val innerBorderColor by animateColorAsState(
        targetValue = when {
            !enabled -> SparkTheme.colors.outline
            status != null -> status.color()
            isFocused -> SparkTheme.colors.outlineHigh
            else -> SparkTheme.colors.outline
        },
        animationSpec = tween(durationMillis = AnimationDuration),
        label = "innerBorder",
    )
    val innerBorderThickness by animateDpAsState(
        targetValue = if (isFocused || status != null) {
            OutlinedTextFieldDefaults.FocusedBorderThickness
        } else {
            OutlinedTextFieldDefaults.UnfocusedBorderThickness
        },
        animationSpec = tween(durationMillis = 150),
        label = "innerThickness",
    )

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clip(SparkTheme.shapes.full)
            .sparkUsageOverlay(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InputStepperButton(
            modifier = Modifier
                .fillMaxHeight()
                .offset(x = 1.dp)
                .border(
                    BorderStroke(StepperTokens.borderThickness, StepperTokens.borderColor),
                    StepperTokens.Input.decrementShape,
                )
                .zIndex(1f)
                // The text field is the accessible control; the buttons are visual only.
                .invisibleSemantic()
                .generateStepperTestTag(testTag, "Decrement"),
            sparkIcon = LeboncoinIcons.Minus,
            contentDescription = "",
            enabled = canDecrement,
            containerColor = containerBgColor,
            onClick = onDecrement,
        )

        val outputTransformation = remember { IntegerOutputTransformation() }
        Box(
            modifier = Modifier
                .then(
                    if (flexible) {
                        Modifier.weight(1f)
                    } else {
                        Modifier.widthIn(min = StepperTokens.MinWidth)
                    },
                )
                .fillMaxHeight()
                .border(
                    BorderStroke(innerBorderThickness, innerBorderColor),
                    StepperTokens.Input.inputShape,
                )
                .background(containerBgColor)
                .zIndex(2f)
                .generateStepperTestTag(testTag, "Input"),
            contentAlignment = Alignment.Center,
        ) {
            BasicTextField(
                state = textFieldState,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .width(IntrinsicSize.Min),
                enabled = enabled,
                textStyle = SparkTheme.typography.body1.copy(
                    textAlign = TextAlign.Center,
                    color = if (enabled) {
                        SparkTheme.colors.onSurface
                    } else {
                        SparkTheme.colors.onSurface.dim3
                    },
                ),
                inputTransformation = IntegerInputTransformation(allowNegative = range.first < 0),
                outputTransformation = if (!isFocused) outputTransformation else null,
                interactionSource = textFieldInteractionSource,
                cursorBrush = SolidColor(LocalContentColor.current),
                lineLimits = TextFieldLineLimits.SingleLine,
                decorator = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (textFieldState.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = SparkTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                color = SparkTheme.colors.onSurface.dim3,
                            )
                        }
                        innerTextField()
                        if (suffix.isNotEmpty() && textFieldState.text.isNotEmpty()) {
                            Text(
                                text = suffix,
                                style = SparkTheme.typography.body1,
                                // The field already reads its value; avoid a duplicate suffix announcement.
                                modifier = Modifier.padding(start = 4.dp).invisibleSemantic(),
                            )
                        }
                    }
                },
            )
        }

        InputStepperButton(
            modifier = Modifier
                .fillMaxHeight()
                .offset(x = -1.dp)
                .border(
                    BorderStroke(StepperTokens.borderThickness, StepperTokens.borderColor),
                    StepperTokens.Input.incrementShape,
                )
                .zIndex(1f)
                // The text field is the accessible control; the buttons are visual only.
                .invisibleSemantic()
                .generateStepperTestTag(testTag, "Increment"),
            sparkIcon = LeboncoinIcons.Plus,
            contentDescription = "",
            enabled = canIncrement,
            containerColor = containerBgColor,
            onClick = onIncrement,
        )
    }
}

@Composable
private fun InputStepperButton(
    sparkIcon: SparkIcon,
    contentDescription: String,
    enabled: Boolean,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    RepeatOnLongPress(
        interactionSource = interactionSource,
        enabled = enabled,
        onClick = onClick,
    )

    val iconColor by animateColorAsState(
        targetValue = if (enabled) SparkTheme.colors.onSurface else SparkTheme.colors.onSurface.dim3,
        animationSpec = tween(durationMillis = AnimationDuration),
        label = "iconColor",
    )

    Surface(
        onClick = {
            onClick()
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        },
        modifier = modifier
            .sizeIn(minWidth = 44.dp, minHeight = 44.dp)
            .focusProperties { canFocus = false },
        enabled = enabled,
        shape = RectangleShape,
        color = containerColor,
        contentColor = iconColor,
        interactionSource = interactionSource,
    ) {
        Icon(
            sparkIcon = sparkIcon,
            contentDescription = contentDescription,
            modifier = Modifier.requiredSize(16.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewSparkStepperInput() {
    PreviewTheme {
        SparkStepperInput(
            textFieldState = remember { TextFieldState("5") },
            value = 5,
            onIncrement = {},
            onDecrement = {},
            onCommit = {},
        )
        SparkStepperInput(
            textFieldState = remember { TextFieldState("") },
            value = null,
            onIncrement = {},
            onDecrement = {},
            onCommit = {},
        )
        SparkStepperInput(
            textFieldState = remember { TextFieldState("5") },
            value = 5,
            onIncrement = {},
            onDecrement = {},
            onCommit = {},
            enabled = false,
        )
        SparkStepperInput(
            textFieldState = remember { TextFieldState("5") },
            value = 5,
            onIncrement = {},
            onDecrement = {},
            onCommit = {},
            status = FormFieldStatus.Error,
        )
        SparkStepperInput(
            textFieldState = remember { TextFieldState("5") },
            value = 5,
            onIncrement = {},
            onDecrement = {},
            onCommit = {},
            status = FormFieldStatus.Success,
        )
    }
}
