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

import androidx.annotation.OpenForTesting
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.stepper.StepperDefaults
import com.adevinta.spark.components.stepper.stepperSemantics
import com.adevinta.spark.icons.Minus
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.ifTrue
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Composable
internal fun SparkStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..10,
    suffix: String = "",
    step: Int = 1,
    enabled: Boolean = true,
    flexible: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    testTag: String? = null,
    allowSemantics: Boolean = true,
) {
    stepperInputValidator(step, range)
    val colors = StepperDefaults.stepperColors()
    val coerced = value.coerceIn(range)
    fun setValue(value: Int) =
        onValueChange(value.coerceIn(range))

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .ifTrue(allowSemantics) {
                stepperSemantics(
                    value = coerced,
                    onValueChange = ::setValue,
                    range = range,
                    step = step,
                    suffix = suffix,
                    enabled = enabled,
                )
            }
            .focusable(
                enabled = enabled,
                interactionSource = interactionSource,
            )
            .sparkUsageOverlay(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .generateStepperTestTag(testTag, "Decrement"),
            sparkIcon = SparkIcons.Minus,
            contentDescription = "", // handled by semantics modifier
            enabled = enabled && coerced > range.first,
            colors = colors,
            shape = SparkTheme.shapes.large,
            interactionSource = interactionSource,
            onClick = { setValue(coerced - step) },
        )

        MiddleText(
            modifier = Modifier
                .then(
                    if (flexible) {
                        modifier.weight(1.0f)
                    } else {
                        modifier.widthIn(min = 48.dp)
                    },
                )
                .fillMaxHeight()
                .invisibleSemantic(),
            value = coerced,
            suffix = suffix,
            enabled = enabled,
            colors = colors,
        )

        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .generateStepperTestTag(testTag, "Increment"),
            sparkIcon = SparkIcons.Plus,
            contentDescription = "", // handled by semantics modifier
            enabled = enabled && coerced < range.last,
            colors = colors,
            shape = SparkTheme.shapes.large,
            interactionSource = interactionSource,
            onClick = {
                setValue(coerced + step)
            },
        )
    }
}

/**
 * Validates the input parameters for a stepper operation.
 *
 * This function ensures that the provided step value is positive and that both the start and end
 * of the given range are multiples of the step. This is crucial for stepper operations where
 * values are incremented or decremented by a fixed step size.
 *
 * @param step The step value used for incrementing or decrementing. Must be a positive integer.
 * @param range The range of values allowed, represented as an [IntRange]. Both the start and
 *              end of this range must be multiples of the step value.
 * @throws IllegalArgumentException If the step is not positive, or if the start or end of the
 *                                  range are not multiples of the step. The exception message will
 *                                  indicate the specific violation.
 *
 * Example Usage:
 * ```kotlin
 * // Valid input: step of 2, range from 0 to 10 (both multiples of 2)
 * stepperInputValidator(2, 0..10)
 *
 * // Invalid input: step of -1 (not positive)
 * try {
 *     stepperInputValidator(-1, 0..10)
 * } catch (e: IllegalArgumentException) {
 *     println(e.message) // Output: A step can only be a positive integer, but was -1
 * }
 *
 * // Invalid input: range start 1 (not multiple of 2)
 * try {
 *     stepperInputValidator(2, 1..10)
 * } catch (e: IllegalArgumentException) {
 *      println(e.message) // Output: The min range must be a multiple of the step, but has 1  remaining
 * }
 *
 * // Invalid input: range end 9 (not multiple of 2)
 * try {
 *     stepperInputValidator(2, 0..9)
 * } catch (e: IllegalArgumentException) {
 *      println(e.message) // Output: The max range must be a multiple of the step, but has 1  remaining
 * }
 * ```
 */
@OpenForTesting
public fun stepperInputValidator(step: Int, range: IntRange) {
    require(step > 0) { "A step can only be a positive integer, but was $step" }
    require(range.last % step == 0) {
        "The max range must be a multiple of the step, but has ${range.last % step}  remaining"
    }
    require(range.first % step == 0) {
        "The min range must be a multiple of the step, but has ${range.first % step}  remaining"
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.generateStepperTestTag(testTag: String?, action: String): Modifier = testTag?.let {
    semantics {
        contentDescription = "" // handled by semantics modifier
        stateDescription = "" // handled by semantics modifier
        testTagsAsResourceId = true
    }.testTag("${testTag}$action")
} ?: this

@Preview
@Composable
private fun PreviewSparkStepper() {
    PreviewTheme {
        SparkStepper(
            value = 1234,
            onValueChange = {},
        )
        SparkStepper(
            value = 1234,
            onValueChange = {},
            enabled = false,
        )
    }
}
