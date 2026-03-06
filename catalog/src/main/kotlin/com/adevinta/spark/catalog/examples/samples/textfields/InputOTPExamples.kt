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
package com.adevinta.spark.catalog.examples.samples.textfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.InputOTP
import com.adevinta.spark.components.textfields.InputOTPType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val InputOTPExampleSourceUrl = "$SampleSourceUrl/InputOTPExamples.kt"

public val InputOTPExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "input-otp-basic",
        name = "Basic InputOTP",
        description = "Basic 4-digit and 6-digit OTP input (6 is the maximum length)",
        sourceUrl = InputOTPExampleSourceUrl,
    ) {
        BasicInputOTP()
    },
    Example(
        id = "input-otp-with-wrapper",
        name = "InputOTP with Label/Helper",
        description = "InputOTP wrapped with label and helper text (recommended pattern)",
        sourceUrl = InputOTPExampleSourceUrl,
    ) {
        InputOTPWithWrapper()
    },
    Example(
        id = "input-otp-states",
        name = "InputOTP States",
        description = "InputOTP with different validation states (after backend validation)",
        sourceUrl = InputOTPExampleSourceUrl,
    ) {
        InputOTPStates()
    },
    Example(
        id = "input-otp-types",
        name = "Input Types",
        description = "Numeric, Alphanumeric, and Alphabetic input types",
        sourceUrl = InputOTPExampleSourceUrl,
    ) {
        InputOTPTypes()
    },
    Example(
        id = "input-otp-interactive",
        name = "Interactive InputOTP",
        description = "Fully interactive OTP input with backend validation simulation",
        sourceUrl = InputOTPExampleSourceUrl,
    ) {
        InteractiveInputOTP()
    },
)

@Composable
private fun ColumnScope.BasicInputOTP() {
    var otp4Value by remember { mutableStateOf("") }
    var otp6Value by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("4-digit OTP (40px x 50px per slot, \"-\" placeholder)")
            InputOTP(
                value = otp4Value,
                onValueChange = { otp4Value = it },
                length = 4,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("6-digit OTP (maximum supported length)")
            InputOTP(
                value = otp6Value,
                onValueChange = { otp6Value = it },
                length = 6,
            )
        }
    }
}

@Composable
private fun ColumnScope.InputOTPWithWrapper() {
    var otpValue by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Recommended pattern: Wrap InputOTP with label/helper")

        // Label (external to InputOTP)
        Text(
            text = "Verification Code",
            style = SparkTheme.typography.body2,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // InputOTP component (no label/helper included)
        InputOTP(
            value = otpValue,
            onValueChange = { otpValue = it },
            length = 4,
        )

        // Helper text (external to InputOTP)
        Text(
            text = "Enter the 4-digit code sent to your phone",
            style = SparkTheme.typography.caption,
            color = SparkTheme.colors.onSurface.copy(alpha = 0.6f),
        )
    }
}

@Composable
private fun ColumnScope.InputOTPStates() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Empty State")
            InputOTP(
                value = "",
                onValueChange = {},
                length = 4,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Partially Filled")
            InputOTP(
                value = "12",
                onValueChange = {},
                length = 4,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Filled")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Error State (after backend validation)")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
                state = FormFieldStatus.Error,
            )
            Text(
                text = "Invalid code. Please try again.",
                style = SparkTheme.typography.caption,
                color = SparkTheme.colors.error,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Success State")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
                state = FormFieldStatus.Success,
            )
            Text(
                text = "Code verified successfully",
                style = SparkTheme.typography.caption,
                color = SparkTheme.colors.success,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Disabled")
            InputOTP(
                value = "1234",
                onValueChange = {},
                length = 4,
                enabled = false,
            )
        }
    }
}

@Composable
private fun ColumnScope.InputOTPTypes() {
    var numericValue by remember { mutableStateOf("") }
    var alphanumericValue by remember { mutableStateOf("") }
    var alphabeticValue by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Numeric (default) - Only 0-9")
            InputOTP(
                value = numericValue,
                onValueChange = { numericValue = it },
                length = 4,
                inputType = InputOTPType.Numeric,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Alphanumeric - A-Z, a-z, 0-9")
            InputOTP(
                value = alphanumericValue,
                onValueChange = { alphanumericValue = it },
                length = 6,
                inputType = InputOTPType.Alphanumeric,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Alphabetic - A-Z, a-z only")
            InputOTP(
                value = alphabeticValue,
                onValueChange = { alphabeticValue = it },
                length = 4,
                inputType = InputOTPType.Alphabetic,
            )
        }
    }
}

@Composable
private fun ColumnScope.InteractiveInputOTP() {
    var otpValue by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    val correctCode = "1234"

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Try entering: $correctCode (validates immediately for demo)")

        // Label
        Text(
            text = "Verification Code",
            style = SparkTheme.typography.body2,
        )

        val state = when {
            isSuccess -> FormFieldStatus.Success
            isError -> FormFieldStatus.Error
            else -> null
        }

        // InputOTP component
        InputOTP(
            value = otpValue,
            onValueChange = { newValue ->
                otpValue = newValue
                isError = false
                isSuccess = false

                // Validate when complete (in real app, call backend here)
                if (newValue.length == 4) {
                    if (newValue == correctCode) {
                        isSuccess = true
                    } else {
                        isError = true
                    }
                }
            },
            length = 4,
            state = state,
            onComplete = {
                // In real app: trigger backend validation here
                // For demo: validation happens immediately in onValueChange
            },
        )

        // Helper/Error message
        val message = when {
            isSuccess -> "Code verified successfully!"
            isError -> "Invalid code. Please try again."
            else -> "Enter the 4-digit code sent to your phone"
        }

        val messageColor = when {
            isSuccess -> SparkTheme.colors.success
            isError -> SparkTheme.colors.error
            else -> SparkTheme.colors.onSurface.copy(alpha = 0.6f)
        }

        Text(
            text = message,
            style = SparkTheme.typography.caption,
            color = messageColor,
        )

        // Accessibility notes
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Accessibility Features:",
            style = SparkTheme.typography.body2,
        )
        Text(
            text = "✓ Screen reader announces 'Digit X of 4' for each slot",
            style = SparkTheme.typography.caption,
        )
        Text(
            text = "✓ Announces 'Code complete' when all digits are filled",
            style = SparkTheme.typography.caption,
        )
        Text(
            text = "✓ Paste support: fills cells from left to right",
            style = SparkTheme.typography.caption,
        )
        Text(
            text = "✓ Haptic feedback on completion (mobile devices)",
            style = SparkTheme.typography.caption,
        )
    }
}
