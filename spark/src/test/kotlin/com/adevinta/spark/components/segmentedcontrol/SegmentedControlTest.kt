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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SegmentedControlTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    @Test
    fun selection_changes_correctly() {
        var selectedIndex by mutableIntStateOf(0)
        var callbackInvoked = false
        var callbackIndex = -1

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = selectedIndex,
                    onSegmentSelect = {
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

        val segments = composeTestRule.onAllNodes(withRole(Role.Tab))
        assert(segments.fetchSemanticsNodes().size == 3)

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
                    onSegmentSelect = {
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

        val segments = composeTestRule.onAllNodes(withRole(Role.Tab))
        assert(segments.fetchSemanticsNodes().size == 5)

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
                    onSegmentSelect = { selectedIndex = it },
                    enabled = false,
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Option 1")
                    SingleLine("Option 2")
                }
            }
        }

        val segments = composeTestRule.onAllNodes(withRole(Role.Tab))
        val count = segments.fetchSemanticsNodes().size
        for (i in 0 until count) {
            segments[i].assertIsNotEnabled()
        }
    }

    @Test
    fun segments_have_tab_role() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = 1,
                    onSegmentSelect = { },
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Option 1")
                    SingleLine("Option 2")
                    SingleLine("Option 3")
                }
            }
        }

        val tabs = composeTestRule.onAllNodes(withRole(Role.Tab))
        assert(tabs.fetchSemanticsNodes().size == 3)

        val radioButtons = composeTestRule.onAllNodes(withRole(Role.RadioButton))
        assert(radioButtons.fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun horizontal_max_segments_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Horizontal(
                        selectedIndex = 0,
                        onSegmentSelect = { },
                    ) {
                        repeat(6) { SingleLine("Option $it") }
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("5") == true)
        }
    }

    @Test
    fun vertical_max_segments_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Vertical(
                        selectedIndex = 0,
                        onSegmentSelect = { },
                    ) {
                        repeat(9) { SingleLine("Option $it") }
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
                        onSegmentSelect = { },
                    ) {
                        SingleLine("Only one")
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("2") == true)
        }
    }

    @Test
    fun vertical_min_segments_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Vertical(
                        selectedIndex = 0,
                        onSegmentSelect = { },
                    ) {
                        SingleLine("1")
                        SingleLine("2")
                        SingleLine("3")
                    }
                }
            }
            assert(false) { "Should have thrown IllegalArgumentException" }
        } catch (e: IllegalArgumentException) {
            assert(e.message?.contains("4") == true)
        }
    }

    @Test
    fun invalid_selected_index_validation() {
        try {
            composeTestRule.setContent {
                PreviewTheme {
                    SegmentedControl.Horizontal(
                        selectedIndex = 5,
                        onSegmentSelect = { },
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
                    onSegmentSelect = { },
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Text")
                    TwoLine("Title", "Subtitle")
                    Icon(LeboncoinIcons.ShoppingCartOutline)
                    IconText(LeboncoinIcons.ShoppingCartOutline, "Cart")
                }
            }
        }

        val segments = composeTestRule.onAllNodes(withRole(Role.Tab))
        assert(segments.fetchSemanticsNodes().size == 4)
        for (i in 0 until 4) {
            segments[i].assertIsDisplayed()
            segments[i].assertIsEnabled()
        }
    }

    @Test
    fun vertical_cross_row_selection_fires_correct_index() {
        var selectedIndex by mutableIntStateOf(0)
        var lastCallbackIndex = -1

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Vertical(
                    selectedIndex = selectedIndex,
                    onSegmentSelect = {
                        lastCallbackIndex = it
                        selectedIndex = it
                    },
                ) {
                    SingleLine("1")
                    SingleLine("2")
                    SingleLine("3")
                    SingleLine("4")
                    SingleLine("5")
                    SingleLine("6")
                }
            }
        }

        val segments = composeTestRule.onAllNodes(withRole(Role.Tab))
        segments[5].performClick()

        assert(lastCallbackIndex == 5)
        assert(selectedIndex == 5)
    }
}
