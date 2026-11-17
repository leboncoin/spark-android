/*
 * Copyright (c) 2025-2026 Adevinta
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.testTagsAsResourceId

/**
 * Validates the input parameters for a stepper operation.
 *
 * Checks that the step is positive and that both ends of the range are multiples of the step.
 * A stepper increments and decrements by a fixed step, so the range bounds must land on a step.
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
internal fun Modifier.generateStepperTestTag(testTag: String?, action: String): Modifier = testTag?.let {
    semantics {
        contentDescription = "" // handled by semantics modifier
        stateDescription = "" // handled by semantics modifier
        testTagsAsResourceId = true
    }.testTag("${testTag}$action")
} ?: this
