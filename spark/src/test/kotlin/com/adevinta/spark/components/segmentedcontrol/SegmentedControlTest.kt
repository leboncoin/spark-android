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
package com.adevinta.spark.components.segmentedcontrol

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasRole
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithRole
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SegmentedControlTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun horizontal_selection_changes_correctly() {
        var selectedIndex by mutableIntStateOf(0)
        var callbackInvoked = false
        var callbackIndex = -1

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = selectedIndex,
                    onSegmentSelected = {
                        callbackInvoked = true
                        callbackIndex = it
                        selectedIndex = it
                    },
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Option 1")
                    SingleLine("Option 2")
                    SingleLine("Option 3")
                }
            }
        }

        val segments = composeTestRule.onAllNodesWithRole(Role.RadioButton)
        assert(segments.fetchSemanticsNodes().size == 3)

        // Click second segment
        segments[1].performClick()

        assert(callbackInvoked)
        assert(callbackIndex == 1)
        assert(selectedIndex == 1)
    }

    @Test
    fun vertical_selection_changes_correctly() {
        var selectedIndex by mutableIntStateOf(0)
        var callbackInvoked = false

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Vertical(
                    selectedIndex = selectedIndex,
                    onSegmentSelected = {
                        callbackInvoked = true
                        selectedIndex = it
                    },
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Option 1")
                    SingleLine("Option 2")
                    SingleLine("Option 3")
                    SingleLine("Option 4")
                    SingleLine("Option 5")
                }
            }
        }

        val segments = composeTestRule.onAllNodesWithRole(Role.RadioButton)
        assert(segments.fetchSemanticsNodes().size == 5)

        // Click third segment
        segments[2].performClick()

        assert(callbackInvoked)
        assert(selectedIndex == 2)
    }

    @Test
    fun disabled_segments_are_not_clickable() {
        var selectedIndex by mutableIntStateOf(0)

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = selectedIndex,
                    onSegmentSelected = { selectedIndex = it },
                    enabled = false,
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Option 1")
                    SingleLine("Option 2")
                }
            }
        }

        val segments = composeTestRule.onAllNodesWithRole(Role.RadioButton)
        segments.forEach { segment ->
            segment.assertIsNotEnabled()
        }
    }

    @Test
    fun segments_have_correct_semantics() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = 1,
                    onSegmentSelected = { },
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Option 1")
                    SingleLine("Option 2")
                    SingleLine("Option 3")
                }
            }
        }

        val segments = composeTestRule.onAllNodesWithRole(Role.RadioButton)
        assert(segments.fetchSemanticsNodes().size == 3)

        // Check that segments have radio button role
        segments.forEach { segment ->
            segment.assert(hasRole(Role.RadioButton))
        }
    }

    @Test
    fun horizontal_max_segments_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Horizontal(
                        selectedIndex = 0,
                        onSegmentSelected = { },
                    ) {
                        SingleLine("1")
                        SingleLine("2")
                        SingleLine("3")
                        SingleLine("4")
                        SingleLine("5") // Should fail - max 4
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("4") == true)
        }
    }

    @Test
    fun vertical_max_segments_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Vertical(
                        selectedIndex = 0,
                        onSegmentSelected = { },
                    ) {
                        repeat(9) { // Should fail - max 8
                            SingleLine("Option $it")
                        }
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("8") == true)
        }
    }

    @Test
    fun min_segments_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Horizontal(
                        selectedIndex = 0,
                        onSegmentSelected = { },
                    ) {
                        SingleLine("Only one") // Should fail - min 2
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("2") == true)
        }
    }

    @Test
    fun invalid_selected_index_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Horizontal(
                        selectedIndex = 5, // Invalid - only 3 segments
                        onSegmentSelected = { },
                    ) {
                        SingleLine("1")
                        SingleLine("2")
                        SingleLine("3")
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("5") == true)
        }
    }

    @Test
    fun all_segment_types_render() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = 0,
                    onSegmentSelected = { },
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Text")
                    TwoLine("Title", "Subtitle")
                    Icon(com.adevinta.spark.icons.SparkIcons.ShoppingBagOutline)
                    IconText(com.adevinta.spark.icons.SparkIcons.ShoppingBagOutline, "Cart")
                }
            }
        }

        val segments = composeTestRule.onAllNodesWithRole(Role.RadioButton)
        assert(segments.fetchSemanticsNodes().size == 4)
        segments.forEach { segment ->
            segment.assertIsDisplayed()
            segment.assertIsEnabled()
        }
    }
}
