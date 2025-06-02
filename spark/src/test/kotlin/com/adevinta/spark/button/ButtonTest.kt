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
package com.adevinta.spark.button

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.SparkButtonTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenIsLoading_ThenProgressIndicatorShouldBeVisible() {
        composeTestRule.setContent {
            SparkTheme {
                ButtonFilled(
                    text = "buttonText",
                    onClick = { },
                    isLoading = true,
                )
            }
        }

        composeTestRule.onNodeWithTag(SparkButtonTags.TAG_PROGRESS_INDICATOR, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenIsNotLoading_ThenProgressIndicatorShouldBeHidden() {
        composeTestRule.setContent {
            SparkTheme {
                ButtonFilled(
                    text = "buttonText",
                    onClick = { },
                    isLoading = false,
                )
            }
        }

        composeTestRule.onNodeWithTag(SparkButtonTags.TAG_PROGRESS_INDICATOR, useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenClickLabel_ThenSemanticsContainCorrectClickLabel() {
        val buttonText = "Submit"
        val clickLabel = "Submit form"

        composeTestRule.setContent {
            SparkTheme {
                ButtonFilled(
                    text = buttonText,
                    onClick = { },
                    clickLabel = clickLabel,
                )
            }
        }

        composeTestRule.onNodeWithText(buttonText)
            .assert(
                SemanticsMatcher("OnClick label is set correctly") { semanticsNode ->
                    semanticsNode.config.getOrNull(SemanticsActions.OnClick)?.label == clickLabel
                },
            )
    }

    @Test
    fun givenNoClickLabel_ThenSemanticsContainNoClickLabel() {
        val buttonText = "Submit"

        composeTestRule.setContent {
            SparkTheme {
                ButtonFilled(
                    text = buttonText,
                    onClick = { },
                    clickLabel = null,
                )
            }
        }

        composeTestRule.onNodeWithText(buttonText)
            .assert(
                SemanticsMatcher("OnClick label is null when not provided") { semanticsNode ->
                    semanticsNode.config.getOrNull(SemanticsActions.OnClick)?.label == null
                },
            )
    }

    @Test
    fun givenClickLabelAndSemanticsModifier_ThenSemanticsModifierTakesPrecedence() {
        val buttonText = "Submit"
        val clickLabelFromArgument = "Submit form from argument"
        val clickLabelFromModifier = "Submit form from modifier"

        composeTestRule.setContent {
            SparkTheme {
                ButtonFilled(
                    text = buttonText,
                    onClick = { },
                    clickLabel = clickLabelFromArgument,
                    modifier = Modifier.semantics {
                        onClick(label = clickLabelFromModifier, action = null)
                    },
                )
            }
        }

        composeTestRule.onNodeWithText(buttonText)
            .assert(
                SemanticsMatcher(
                    description = "Semantics modifier should override clickLabel argument",
                ) { semanticsNode ->
                    semanticsNode.config.getOrNull(SemanticsActions.OnClick)?.label == clickLabelFromModifier
                },
            )
    }
}
