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
package com.adevinta.spark.components.progress.tracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalSparkApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
public class ProgressTrackerTest {

    @get:Rule
    public val composeTestRule: androidx.compose.ui.test.junit4.ComposeContentTestRule = createComposeRule()

    @Test
    public fun progressTrackerRow_displaysBothLabelsWhenSublabelProvided() {
        composeTestRule.setContent {
            PreviewTheme {
                ProgressTrackerRow(
                    items = persistentListOf(
                        ProgressStep("Step 1", true, sublabel = "10:00 AM"),
                        ProgressStep("Step 2", true, sublabel = "In progress"),
                        ProgressStep("Step 3", false),
                    ),
                    selectedStep = 1,
                )
            }
        }

        composeTestRule.onNodeWithText("Step 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("10:00 AM").assertIsDisplayed()
        composeTestRule.onNodeWithText("Step 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("In progress").assertIsDisplayed()
        composeTestRule.onNodeWithText("Step 3").assertIsDisplayed()
    }

    @Test
    public fun progressTrackerColumn_displaysBothLabelsWhenSublabelProvided() {
        composeTestRule.setContent {
            PreviewTheme {
                ProgressTrackerColumn(
                    items = persistentListOf(
                        ProgressStep("Order placed", true, sublabel = "Oct 27"),
                        ProgressStep("Shipped", true, sublabel = "Oct 28"),
                        ProgressStep("Delivered", false),
                    ),
                    selectedStep = 0,
                )
            }
        }

        composeTestRule.onNodeWithText("Order placed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Oct 27").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shipped").assertIsDisplayed()
        composeTestRule.onNodeWithText("Oct 28").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delivered").assertIsDisplayed()
    }

    @Test
    public fun progressTracker_stepClick_worksWithSublabel() {
        var clickedIndex: Int? = null
        composeTestRule.setContent {
            PreviewTheme {
                ProgressTrackerRow(
                    items = persistentListOf(
                        ProgressStep("Step 1", true, sublabel = "Sub 1"),
                        ProgressStep("Step 2", true, sublabel = "Sub 2"),
                    ),
                    onStepClick = { clickedIndex = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Step 2").performClick()
        assertEquals(1, clickedIndex)
    }
}
