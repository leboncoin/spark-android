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

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.SparkButtonTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
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
}
