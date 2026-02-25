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
package com.adevinta.spark.components.rating

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.withKeyDown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RatingInputTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ratingInput_disabled_doesNotChangeValue() {
        val testTag = "ratingInputTag"
        composeTestRule.setContent {
            var rating by remember { mutableIntStateOf(2) }
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = rating,
                onRatingChanged = { rating = it },
                enabled = false,
                testTag = testTag,
            )
        }
        composeTestRule.onNodeWithTag("$testTag Star 3").assertIsNotEnabled().performClick()
        // Value should remain 2
    }

    @Test
    fun ratingInput_customStateDescription_isApplied() {
        val testTag = "ratingInputTag"
        composeTestRule.setContent {
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = 4,
                onRatingChanged = {},
                stateDescription = { "Custom: $it" },
                testTag = testTag,
            )
        }
        // Check semantics
        composeTestRule.onNodeWithTag(testTag).assert(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Custom: 4"),
        )
    }

    @Test
    fun ratingInput_allowSemantics_false_noSemantics() {
        val testTag = "ratingInputTag"
        composeTestRule.setContent {
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = 2,
                onRatingChanged = {},
                allowSemantics = false,
                testTag = testTag,
            )
        }
        // Should not have progressBarRangeInfo
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 2f,
                    range = 0f..5f,
                    steps = 0,
                ),
            ),
            useUnmergedTree = true,
        ).assertDoesNotExist()
    }

    @Test
    fun ratingInput_semantics_stateDescription_and_range() {
        val min = 0
        val max = 5
        val value = 3
        val testTag = "ratingInputTag"
        val rangeFloat = min.toFloat()..max.toFloat()
        val steps = max - min
        composeTestRule.setContent {
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = value,
                onRatingChanged = {},
                stateDescription = { "$value stars" },
                testTag = testTag,
            )
        }
        composeTestRule.onNodeWithTag(testTag).assert(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = value.toFloat(),
                    range = rangeFloat,
                    steps = steps,
                ),
            ),
        ).assert(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "3 stars"),
        )
    }

    @Test
    fun ratingInput_semantics_range_and_progress() {
        val min = 0
        val max = 5
        val initialValue = 2
        val testTag = "ratingInputTag"
        val rangeFloat = min.toFloat()..max.toFloat()
        val steps = max - min
        composeTestRule.setContent {
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = initialValue,
                onRatingChanged = {},
                testTag = testTag,
            )
        }
        composeTestRule.onNodeWithTag(testTag).assert(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = initialValue.toFloat(),
                    range = rangeFloat,
                    steps = steps,
                ),
            ),
        )
    }

    @Test
    fun ratingInput_semantics_progress_setProgress() {
        val min = 0
        val max = 5
        val initialValue = 2
        val testTag = "ratingInputTag"
        val rangeFloat = min.toFloat()..max.toFloat()
        val steps = max - min
        composeTestRule.setContent {
            var rating by remember { mutableIntStateOf(initialValue) }
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = rating,
                onRatingChanged = { rating = it },
                testTag = testTag,
            )
        }
        composeTestRule.onNodeWithTag(testTag)
            .performSemanticsAction(SemanticsActions.SetProgress) { setProgress -> setProgress(4f) }.assert(
                hasProgressBarRangeInfo(
                    ProgressBarRangeInfo(
                        current = 4f,
                        range = rangeFloat,
                        steps = steps,
                    ),
                ),
            )
    }

    @Test
    fun ratingInput_semantics_disabled_state() {
        val testTag = "ratingInputTag"
        composeTestRule.setContent {
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = 2,
                onRatingChanged = {},
                testTag = testTag,
                enabled = false,
            )
        }
        composeTestRule.onNodeWithTag(testTag).assert(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled),
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun ratingInput_keyboard_control_increments_and_decrements() {
        val min = 0
        val max = 5
        val initialValue = 2
        val testTag = "ratingInputTag"
        val rangeFloat = min.toFloat()..max.toFloat()
        val steps = max - min
        composeTestRule.setContent {
            var rating by remember { mutableIntStateOf(initialValue) }
            RatingInput(
                modifier = Modifier.testTag(testTag),
                value = rating,
                onRatingChanged = { rating = it },
                testTag = testTag,
            )
        }
        composeTestRule.onNodeWithTag(testTag)
            .requestFocus()
            .performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionRight)
                }
            }.assert(
                hasProgressBarRangeInfo(
                    ProgressBarRangeInfo(
                        current = 3f,
                        range = rangeFloat,
                        steps = steps,
                    ),
                ),
            ).performKeyInput {
                withKeyDown(Key.ShiftLeft) {
                    pressKey(Key.DirectionLeft)
                }
            }.assert(
                hasProgressBarRangeInfo(
                    ProgressBarRangeInfo(
                        current = 2f,
                        range = rangeFloat,
                        steps = steps,
                    ),
                ),
            )
    }
}
