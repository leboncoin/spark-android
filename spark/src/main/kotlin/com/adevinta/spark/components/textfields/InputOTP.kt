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
package com.adevinta.spark.components.textfields

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text

/**
 * InputOTP is an accessible component for entering one-time passwords or verification codes.
 *
 * ## Specifications
 * - **Single default size**: Width 40px, Height 50px per slot
 * - **Numeric by default**: Only numeric characters accepted, can be configured for alphanumeric/alphabetic
 * - **Pasting**: Fills cells from left to right automatically
 * - **Horizontal alignment only**: Slots are arranged horizontally
 * - **4 to 6 digits**: Length must be between 4 and 6 digits
 *
 * ## Focus Behavior
 * - Focus is automatically set on the first cell
 * - Entering a character moves focus to the next cell (left to right)
 * - If all cells are filled, automatic validation can be triggered
 *
 * ## Delete Behavior
 * - If the current cell has a value, clear it and keep focus
 * - If it's empty, move to the previous cell and clear it
 *
 * ## Haptic Feedback (Mobile only)
 * - Haptic feedback when code is successfully entered
 * - No repeated vibration while typing
 *
 * ## Note on Label and Helper Text
 * The label and helper text are NOT included in this component, as they are optional.
 * Use the FormField component wrapper to add labels and helper text if needed.
 * Error feedback should only be shown after backend validation.
 *
 * @param value the current OTP value
 * @param onValueChange callback invoked when the OTP value changes
 * @param length the number of OTP slots (must be between 4 and 6)
 * @param modifier a [Modifier] for this component
 * @param enabled whether the input is enabled
 * @param inputType the type of characters accepted (Numeric, Alphanumeric, Alphabetic)
 * @param state the validation state (Success, Alert, Error) - typically set after backend validation
 * @param showSeparator whether to show a separator between groups of 3 digits (only for 6 digits)
 * @param onComplete optional callback invoked when all slots are filled (triggers haptic feedback on mobile)
 */
@Composable
public fun InputOTP(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    inputType: InputOTPType = InputOTPType.Numeric,
    state: FormFieldStatus? = null,
    showSeparator: Boolean = false,
    onComplete: (() -> Unit)? = null,
) {
    require(length in 4..6) {
        "InputOTP length must be between 4 and 6, but was $length"
    }

    val actualLength = length
    val focusRequesters = remember { List(actualLength) { FocusRequester() } }
    var focusedIndex by remember { mutableIntStateOf(0) }
    var pendingFocusIndex by remember { mutableIntStateOf(-1) }
    val view = LocalView.current

    // Track if we just completed the code to trigger haptic feedback once
    var wasCompleted by remember { mutableStateOf(false) }

    // Announce completion for screen readers
    val isComplete = value.length == actualLength

    // Trigger haptic feedback on completion (mobile only)
    LaunchedEffect(isComplete) {
        if (isComplete && !wasCompleted && enabled) {
            val feedbackConstant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                HapticFeedbackConstants.CONFIRM
            } else {
                HapticFeedbackConstants.KEYBOARD_TAP
            }
            view.performHapticFeedback(feedbackConstant)
            onComplete?.invoke()
            wasCompleted = true
        } else if (!isComplete) {
            wasCompleted = false
        }
    }

    // Build accessibility description
    val accessibilityDescription = buildString {
        append(pluralStringResource(R.plurals.spark_inputotp_content_description, actualLength, actualLength))
        append(". ")
        if (isComplete) {
            append(stringResource(R.string.spark_inputotp_complete_description))
            append(". ")
        } else {
            append(
                pluralStringResource(
                    R.plurals.spark_inputotp_progress_description,
                    value.length,
                    value.length,
                    actualLength,
                ),
            )
            append(". ")
        }
    }

    val stateDescriptionText = when (state) {
        FormFieldStatus.Error -> stringResource(R.string.spark_inputotp_state_error)
        FormFieldStatus.Success -> stringResource(R.string.spark_inputotp_state_success)
        FormFieldStatus.Alert -> stringResource(R.string.spark_inputotp_state_alert)
        null -> null
    }

    Row(
        modifier = modifier.semantics {
            contentDescription = accessibilityDescription
            if (stateDescriptionText != null) {
                stateDescription = stateDescriptionText
            }
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(actualLength) { index ->
            // Add spacing before each cell (except first)
            if (index > 0) {
                // Add separator after every 3rd cell for 6 digits if enabled
                if (showSeparator && index % 3 == 0 && actualLength == 6) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(4.dp)
                            .background(
                                color = SparkTheme.colors.outline,
                                shape = SparkTheme.shapes.full,
                            ),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            InputOTPCell(
                value = value.getOrNull(index)?.toString() ?: "",
                onValueChange = { newChar ->
                    handleCellValueChange(
                        index = index,
                        newChar = newChar,
                        currentValue = value,
                        length = actualLength,
                        inputType = inputType,
                        onValueChange = onValueChange,
                        focusRequesters = focusRequesters,
                    )
                },
                onPaste = { pastedText ->
                    handlePaste(
                        index = index,
                        pastedText = pastedText,
                        currentValue = value,
                        length = actualLength,
                        inputType = inputType,
                        onValueChange = onValueChange,
                        focusRequesters = focusRequesters,
                    )
                },
                onDelete = {
                    val targetIndex = handleDelete(
                        index = index,
                        cellValue = value.getOrNull(index)?.toString() ?: "",
                        currentValue = value,
                        onValueChange = onValueChange,
                    )
                    if (targetIndex != null) {
                        pendingFocusIndex = targetIndex
                    }
                },
                enabled = enabled,
                state = state,
                focusRequester = focusRequesters[index],
                inputType = inputType,
                position = index + 1,
                total = actualLength,
                onFocusChanged = { hasFocus ->
                    if (hasFocus) focusedIndex = index
                },
            )
        }
    }

    // Auto-focus on first cell on mount
    LaunchedEffect(Unit) {
        if (enabled) {
            focusRequesters[0].requestFocus()
        }
    }

    // Handle pending focus change after delete
    LaunchedEffect(pendingFocusIndex) {
        if (pendingFocusIndex >= 0 && pendingFocusIndex < actualLength) {
            focusRequesters[pendingFocusIndex].requestFocus()
            pendingFocusIndex = -1
        }
    }
}

/**
 * Handles value change in a cell.
 * When a character is entered, move focus to the next cell.
 *
 * @param index The index of the current cell
 * @param newChar The new character entered
 * @param currentValue The current OTP value
 * @param length The total number of cells
 * @param inputType The type of input validation
 * @param onValueChange Callback to update the OTP value
 * @param focusRequesters List of focus requesters for all cells
 */
private fun handleCellValueChange(
    index: Int,
    newChar: String,
    currentValue: String,
    length: Int,
    inputType: InputOTPType,
    onValueChange: (String) -> Unit,
    focusRequesters: List<FocusRequester>,
) {
    if (newChar.isEmpty() || !inputType.isValid(newChar.first())) return

    // Build new value by replacing character at index
    val chars = currentValue.padEnd(length, ' ').toCharArray()
    chars[index] = newChar.first()
    val newValue = String(chars).trimEnd()

    onValueChange(newValue)

    // Move focus to next cell if not at the end
    if (index < length - 1) {
        focusRequesters[index + 1].requestFocus()
    }
}

/**
 * Handles paste operation - fills cells from left to right.
 *
 * @param index The index where the paste operation starts
 * @param pastedText The text that was pasted
 * @param currentValue The current OTP value
 * @param length The total number of cells
 * @param inputType The type of input validation
 * @param onValueChange Callback to update the OTP value
 * @param focusRequesters List of focus requesters for all cells
 */
private fun handlePaste(
    index: Int,
    pastedText: String,
    currentValue: String,
    length: Int,
    inputType: InputOTPType,
    onValueChange: (String) -> Unit,
    focusRequesters: List<FocusRequester>,
) {
    // Filter pasted text to valid characters
    val validChars = pastedText.filter { inputType.isValid(it) }

    // Fill from current index
    val chars = currentValue.padEnd(length, ' ').toCharArray()
    var targetIndex = index

    for (char in validChars) {
        if (targetIndex >= length) break
        chars[targetIndex] = char
        targetIndex++
    }

    val newValue = String(chars).trimEnd()
    onValueChange(newValue)

    // Move focus to the cell after the last filled one, or stay at last
    val nextFocusIndex = minOf(targetIndex, length - 1)
    if (nextFocusIndex < length) {
        focusRequesters[nextFocusIndex].requestFocus()
    }
}

/**
 * Handles delete/backspace in a cell.
 * - If current cell has value: clear it and keep focus
 * - If empty: move to previous cell and clear it
 *
 * @param index The index of the current cell
 * @param cellValue The current value of the cell
 * @param currentValue The current OTP value
 * @param onValueChange Callback to update the OTP value
 * @return The index to move focus to, or null to keep current focus
 */
private fun handleDelete(
    index: Int,
    cellValue: String,
    currentValue: String,
    onValueChange: (String) -> Unit,
): Int? {
    val currentCellHasValue = cellValue.isNotEmpty()

    return if (currentCellHasValue) {
        // Clear current cell and keep focus
        val newValue = currentValue.substring(0, index) +
            currentValue.substring(minOf(index + 1, currentValue.length))
        onValueChange(newValue)
        null
    } else if (index > 0) {
        // Clear previous cell and move focus there
        val prevIndex = index - 1
        val newValue = currentValue.substring(0, prevIndex) +
            currentValue.substring(minOf(prevIndex + 1, currentValue.length))
        onValueChange(newValue)
        prevIndex
    } else {
        null
    }
}

/**
 * Individual OTP cell with its own TextField.
 * Size: 40px width x 50px height (fixed, per Figma specs).
 *
 * @param value The current value of the cell (single character or empty)
 * @param onValueChange Callback when a new character is entered
 * @param onPaste Callback when text is pasted into the cell
 * @param onDelete Callback when delete/backspace is pressed
 * @param enabled Whether the cell is enabled for input
 * @param state The validation state (Error, Success, Alert, or null)
 * @param focusRequester The focus requester for this cell
 * @param inputType The type of input validation
 * @param position The position of this cell (1-indexed for accessibility)
 * @param total The total number of cells
 * @param onFocusChanged Callback when focus state changes
 */
@Composable
private fun InputOTPCell(
    value: String,
    onValueChange: (String) -> Unit,
    onPaste: (String) -> Unit,
    onDelete: () -> Unit,
    enabled: Boolean,
    state: FormFieldStatus?,
    focusRequester: FocusRequester,
    inputType: InputOTPType,
    position: Int,
    total: Int,
    onFocusChanged: (Boolean) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    var textFieldValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length),
            ),
        )
    }

    val backgroundColor = when {
        !enabled -> SparkTheme.colors.neutralContainer
        state == FormFieldStatus.Error -> SparkTheme.colors.errorContainer
        state == FormFieldStatus.Success -> SparkTheme.colors.successContainer
        state == FormFieldStatus.Alert -> SparkTheme.colors.alertContainer
        isFocused || isHovered -> SparkTheme.colors.backgroundVariant
        value.isNotEmpty() -> SparkTheme.colors.neutralContainer
        else -> SparkTheme.colors.surface
    }

    val borderColor = when {
        !enabled -> SparkTheme.colors.outline
        state != null -> state.color()
        isFocused || isHovered -> SparkTheme.colors.outlineHigh
        value.isNotEmpty() -> SparkTheme.colors.outline
        else -> SparkTheme.colors.outline
    }

    val borderWidth = when {
        !enabled -> 1.dp
        state != null -> 2.dp
        else -> 1.dp
    }

    val cellContentDescription = when (inputType) {
        InputOTPType.Numeric -> stringResource(R.string.spark_inputotp_cell_numeric_description, position, total)

        InputOTPType.Alphanumeric -> stringResource(
            R.string.spark_inputotp_cell_alphanumeric_description,
            position,
            total,
        )

        InputOTPType.Alphabetic -> stringResource(R.string.spark_inputotp_cell_alphabetic_description, position, total)
    }

    val cellStateDescription = if (value.isNotEmpty()) {
        stringResource(R.string.spark_inputotp_cell_filled_state)
    } else {
        null
    }

    // Add focus shadow effect (double border for focused state)
    val focusModifier = if (isFocused && enabled && state == null) {
        Modifier
            .border(4.dp, SparkTheme.colors.main, SparkTheme.shapes.medium) // Blue outer border
            .border(2.dp, SparkTheme.colors.surface, SparkTheme.shapes.medium) // White inner border
    } else {
        Modifier.border(borderWidth, borderColor, SparkTheme.shapes.medium)
    }

    Box(
        modifier = Modifier
            .width(40.dp)
            .height(50.dp)
            .background(backgroundColor, SparkTheme.shapes.medium)
            .then(focusModifier)
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .semantics {
                contentDescription = cellContentDescription
                if (cellStateDescription != null) {
                    stateDescription = cellStateDescription
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = textFieldValue,
            interactionSource = interactionSource,
            onValueChange = { newValue ->
                val newText = newValue.text.filter { inputType.isValid(it) }

                // Detect paste (multiple characters at once)
                if (newText.length > 1) {
                    onPaste(newText)
                    textFieldValue = TextFieldValue(
                        text = newText.take(1),
                        selection = TextRange(1),
                    )
                } else {
                    textFieldValue = newValue.copy(
                        text = newText.take(1),
                        selection = TextRange(newText.length),
                    )
                    if (newText.isNotEmpty()) {
                        onValueChange(newText)
                    }
                }
            },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = inputType.keyboardType,
                imeAction = ImeAction.Next,
            ),
            textStyle = SparkTheme.typography.display3.copy(
                color = if (enabled) {
                    SparkTheme.colors.onSurface
                } else {
                    SparkTheme.colors.onSurface.copy(
                        alpha = SparkTheme.colors.dim3,
                    )
                },
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .width(40.dp)
                .height(50.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    val focused = focusState.isFocused
                    isFocused = focused
                    onFocusChanged(focused)
                }
                .onKeyEvent { keyEvent ->
                    // Check for both KeyDown and KeyUp as virtual keyboards may send KeyUp
                    if ((keyEvent.type == KeyEventType.KeyDown || keyEvent.type == KeyEventType.KeyUp) &&
                        (keyEvent.key == Key.Backspace || keyEvent.key == Key.Delete)
                    ) {
                        onDelete()
                        true
                    } else {
                        false
                    }
                },
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(40.dp)
                        .height(50.dp),
                ) {
                    // Show placeholder "-" if value is empty
                    if (value.isEmpty()) {
                        Text(
                            text = "-",
                            style = SparkTheme.typography.display3.copy(
                                color = SparkTheme.colors.onSurface.copy(alpha = SparkTheme.colors.dim3),
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

/**
 * Type of input accepted by InputOTP.
 */
public enum class InputOTPType {
    /**
     * Only numeric characters (0-9) are accepted.
     * This is the default and recommended for OTP codes.
     */
    Numeric {
        override fun isValid(char: Char): Boolean = char.isDigit()
        override val keyboardType: KeyboardType = KeyboardType.NumberPassword
    },

    /**
     * Alphanumeric characters (A-Z, a-z, 0-9) are accepted.
     */
    Alphanumeric {
        override fun isValid(char: Char): Boolean = char.isLetterOrDigit()
        override val keyboardType: KeyboardType = KeyboardType.Ascii
    },

    /**
     * Only alphabetic characters (A-Z, a-z) are accepted.
     */
    Alphabetic {
        override fun isValid(char: Char): Boolean = char.isLetter()
        override val keyboardType: KeyboardType = KeyboardType.Ascii
    },
    ;

    internal abstract fun isValid(char: Char): Boolean
    internal abstract val keyboardType: KeyboardType
}

@Preview(
    group = "InputOTP",
    name = "InputOTP States",
)
@Composable
private fun InputOTPPreview() {
    PreviewTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Empty (Default)")
            InputOTP(
                value = "",
                onValueChange = {},
                length = 4,
            )

            Text("Partially Filled")
            InputOTP(
                value = "12",
                onValueChange = {},
                length = 4,
            )

            Text("Filled")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
            )

            Text("Error State (after backend validation)")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
                state = FormFieldStatus.Error,
            )

            Text("Success State")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
                state = FormFieldStatus.Success,
            )

            Text("Disabled")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
                enabled = false,
            )

            Text("6 Digits")
            InputOTP(
                value = "123456",
                onValueChange = {},
                length = 6,
            )

            Text("6 Digits with Separator")
            InputOTP(
                value = "123456",
                onValueChange = {},
                length = 6,
                showSeparator = true,
            )

            Text("Alphanumeric")
            InputOTP(
                value = "AB12",
                onValueChange = {},
                length = 4,
                inputType = InputOTPType.Alphanumeric,
            )
        }
    }
}
