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
package com.adevinta.spark.stepper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions.SetProgress
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isNotFocusable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.withKeyDown
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.stepper.Stepper
import com.ibm.icu.impl.SimpleFormatterImpl.IterInternal.step
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StepperTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Stepper semantics suffix`() {
        val minRange = 0
        val maxRange = 10
        val step = 2
        val initialValue = 4
        val stepperTestTag = "myStepper"

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    range = minRange..maxRange,
                    step = step,
                    suffix = " €",
                    testTag = stepperTestTag,
                )
            }
        }
        // Verify that the suffix is read by accessibility services
        composeTestRule.onNodeWithTag(stepperTestTag)
            .assert(
                SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "4 €"),
            )
    }

    @Test
    fun `Stepper semantics range`() {
        val minRange = 0
        val maxRange = 2
        val rangeFloat = minRange.toFloat()..maxRange.toFloat()
        val step = 1
        val steps = (maxRange - minRange) / step
        val initialValue = 1
        val stepperTestTag = "myStepper"

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    range = minRange..maxRange,
                    step = step,
                    suffix = " €",
                    testTag = stepperTestTag,
                )
            }
        }

        // Check initial value and range
        composeTestRule.onNodeWithTag(stepperTestTag)
            .assert(
                SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo),
            )
            .performSemanticsAction(SetProgress) { setProgress -> setProgress(3f) }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 2f, range = rangeFloat, steps),
                ),
            )
    }

    @Test
    fun `Stepper semantics progress`() {
        val minRange = 0
        val maxRange = 10
        val range = minRange..maxRange
        val rangeFloat = minRange.toFloat()..maxRange.toFloat()
        val step = 2
        val steps = (maxRange - minRange) / step
        val initialValue = 4
        val stepperTestTag = "myStepper"

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    range = range,
                    step = step,
                    suffix = " €",
                    testTag = stepperTestTag,
                )
            }
        }

        // Check initial value
        composeTestRule.onNodeWithTag(stepperTestTag)
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 4f, range = rangeFloat, steps = steps),
                ),
            )
            .assertTextEquals("4", " €")

        // Increment
        composeTestRule.onNodeWithTag(stepperTestTag)
            .performSemanticsAction(SetProgress) { setProgress -> setProgress(6f) }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 6f, range = rangeFloat, steps),
                ),
            )
            .assertTextEquals("6", " €")

        // Decrement
        composeTestRule.onNodeWithTag(stepperTestTag)
            .performSemanticsAction(SetProgress) { setProgress -> setProgress(4f) }
            .performSemanticsAction(SetProgress) { setProgress -> setProgress(2f) }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 2f, range = rangeFloat, steps),
                ),
            )
            .assertTextEquals("2", " €")
    }

    @Test
    fun `Stepper semantics disabled`() {
        val minRange = 0
        val maxRange = 10
        val range = minRange..maxRange
        val step = 2
        val initialValue = 4
        val stepperTestTag = "myStepper"
        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    testTag = stepperTestTag,
                    range = range,
                    step = step,
                    enabled = false,
                )
            }
        }

        composeTestRule.onNodeWithTag(stepperTestTag)
            .assert(isNotFocusable())
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Stepper keyboard control`() {
        val minRange = 0
        val maxRange = 10
        val range = minRange..maxRange
        val rangeFloat = minRange.toFloat()..maxRange.toFloat()
        val step = 2
        val steps = (maxRange - minRange) / step
        val initialValue = 4
        val stepperTestTag = "myStepper"

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    range = range,
                    step = step,
                    suffix = " €",
                    testTag = stepperTestTag,
                )
            }
        }
        composeTestRule.onNodeWithTag(stepperTestTag)
            .requestFocus()
            .performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionUp)
                }
            }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 6f, range = rangeFloat, steps),
                ),
            ).assertTextEquals("6", " €")

        composeTestRule.onNodeWithTag(stepperTestTag)
            .performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionDown)
                }
            }.performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionDown)
                }
            }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 2f, range = rangeFloat, steps),
                ),
            ).assertTextEquals("2", " €")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Stepper not focused keyboard control`() {
        val minRange = 0
        val maxRange = 10
        val range = minRange..maxRange
        val rangeFloat = minRange.toFloat()..maxRange.toFloat()
        val step = 2
        val steps = (maxRange - minRange) / step
        val initialValue = 4
        val stepperTestTag = "myStepper"

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    range = range,
                    step = step,
                    suffix = " €",
                    testTag = stepperTestTag,
                )
            }
        }
        composeTestRule.onNodeWithTag(stepperTestTag)
            .performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionUp)
                }
            }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 4f, range = rangeFloat, steps),
                ),
            ).assertTextEquals("4", " €")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `Stepper keyboard control range`() {
        val minRange = 0
        val maxRange = 2
        val rangeFloat = minRange.toFloat()..maxRange.toFloat()
        val step = 1
        val steps = (maxRange - minRange) / step
        val initialValue = 1
        val stepperTestTag = "myStepper"

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableIntStateOf(initialValue) }
                Stepper(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.testTag(stepperTestTag),
                    range = minRange..maxRange,
                    step = step,
                    suffix = " €",
                    testTag = stepperTestTag,
                )
            }
        }

        // Check initial value and range
        composeTestRule.onNodeWithTag(stepperTestTag)
            .requestFocus()
            .performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionUp)
                }
            }
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ProgressBarRangeInfo,
                    ProgressBarRangeInfo(current = 2f, range = rangeFloat, steps),
                ),
            )
    }
}
