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
package com.adevinta.spark.stepper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.stepper.Stepper
import com.adevinta.spark.components.stepper.StepperInputState
import com.adevinta.spark.components.stepper.rememberStepperInputState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StepperInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Input controlled - text field exposes editable text`() {
        val testTag = "inputStepper"

        composeTestRule.setContent {
            PreviewTheme {
                Stepper.Input(
                    value = 5,
                    onValueChange = {},
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    suffix = " kg",
                    testTag = testTag,
                )
            }
        }

        // The value is edited through the text field, which exposes a set-text action.
        composeTestRule.onNode(hasSetTextAction())
            .assertExists()
    }

    @Test
    fun `Input controlled - no custom actions on the node`() {
        val testTag = "inputStepper"

        composeTestRule.setContent {
            PreviewTheme {
                Stepper.Input(
                    value = 5,
                    onValueChange = {},
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    testTag = testTag,
                )
            }
        }

        // Field-only variant: increment and decrement are not exposed as accessibility actions.
        composeTestRule.onNodeWithTag(testTag)
            .assert(SemanticsMatcher.keyNotDefined(SemanticsActions.CustomActions))
    }

    @Test
    fun `Input controlled - disabled field is not editable`() {
        val testTag = "inputStepper"

        composeTestRule.setContent {
            PreviewTheme {
                Stepper.Input(
                    value = 5,
                    onValueChange = {},
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    enabled = false,
                    testTag = testTag,
                )
            }
        }

        // A disabled text field exposes no set-text action, so it cannot be edited.
        composeTestRule.onNode(hasSetTextAction())
            .assertDoesNotExist()
    }

    @Test
    fun `Input controlled - increment button updates value`() {
        val testTag = "inputStepper"
        var lastValue: Int? = null

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableStateOf<Int?>(5) }
                Stepper.Input(
                    value = value,
                    onValueChange = {
                        lastValue = it
                        value = it
                    },
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    testTag = testTag,
                )
            }
        }

        composeTestRule.onNode(hasTestTag("${testTag}Increment"))
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(6, lastValue)
    }

    @Test
    fun `Input controlled - decrement button updates value`() {
        val testTag = "inputStepper"
        var lastValue: Int? = null

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableStateOf<Int?>(5) }
                Stepper.Input(
                    value = value,
                    onValueChange = {
                        lastValue = it
                        value = it
                    },
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    testTag = testTag,
                )
            }
        }

        composeTestRule.onNode(hasTestTag("${testTag}Decrement"))
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(4, lastValue)
    }

    @Test
    fun `Input controlled - increment from null snaps to range first`() {
        val testTag = "inputStepper"
        var lastValue: Int? = null

        composeTestRule.setContent {
            PreviewTheme {
                var value by remember { mutableStateOf<Int?>(null) }
                Stepper.Input(
                    value = value,
                    onValueChange = {
                        lastValue = it
                        value = it
                    },
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    testTag = testTag,
                )
            }
        }

        composeTestRule.onNode(hasTestTag("${testTag}Increment"))
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(0, lastValue)
    }

    @Test
    fun `Input uncontrolled - state increment updates value`() {
        val testTag = "inputStepper"
        lateinit var state: StepperInputState

        composeTestRule.setContent {
            PreviewTheme {
                state = rememberStepperInputState(initialValue = 5)
                Stepper.Input(
                    state = state,
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    testTag = testTag,
                )
            }
        }

        composeTestRule.onNode(hasTestTag("${testTag}Increment"))
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(6, state.value)
    }

    @Test
    fun `Input uncontrolled - state decrement clamps at min`() {
        val testTag = "inputStepper"
        lateinit var state: StepperInputState

        composeTestRule.setContent {
            PreviewTheme {
                state = rememberStepperInputState(initialValue = 0)
                Stepper.Input(
                    state = state,
                    modifier = Modifier.testTag(testTag),
                    range = 0..10,
                    step = 1,
                    testTag = testTag,
                )
            }
        }

        composeTestRule.onNode(hasTestTag("${testTag}Decrement"))
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(0, state.value)
    }
}
