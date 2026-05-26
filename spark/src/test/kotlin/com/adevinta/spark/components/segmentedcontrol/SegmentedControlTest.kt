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

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
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
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    listOf("Option 1", "Option 2", "Option 3").forEachIndexed { index, label ->
                        SingleLine(
                            text = label,
                            selected = index == selectedIndex,
                            onClick = {
                                callbackInvoked = true
                                callbackIndex = index
                                selectedIndex = index
                            },
                        )
                    }
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
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5").forEachIndexed { index, label ->
                        SingleLine(
                            text = label,
                            selected = index == selectedIndex,
                            onClick = {
                                callbackInvoked = true
                                selectedIndex = index
                            },
                        )
                    }
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
                    enabled = false,
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    listOf("Option 1", "Option 2").forEachIndexed { index, label ->
                        SingleLine(
                            text = label,
                            selected = index == selectedIndex,
                            onClick = { selectedIndex = index },
                        )
                    }
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
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    listOf("Option 1", "Option 2", "Option 3").forEachIndexed { index, label ->
                        SingleLine(text = label, selected = index == 1, onClick = {})
                    }
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
                    SegmentedControl.Horizontal(selectedIndex = 0) {
                        repeat(6) { index -> SingleLine("Option $index", selected = index == 0, onClick = {}) }
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
                    SegmentedControl.Vertical(selectedIndex = 0) {
                        repeat(9) { index -> SingleLine("Option $index", selected = index == 0, onClick = {}) }
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
                    SegmentedControl.Horizontal(selectedIndex = 0) {
                        SingleLine("Only one", selected = true, onClick = {})
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
                    SegmentedControl.Vertical(selectedIndex = 0) {
                        SingleLine("1", selected = true, onClick = {})
                        SingleLine("2", selected = false, onClick = {})
                        SingleLine("3", selected = false, onClick = {})
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
                    SegmentedControl.Horizontal(selectedIndex = 5) {
                        SingleLine("1", selected = false, onClick = {})
                        SingleLine("2", selected = false, onClick = {})
                        SingleLine("3", selected = false, onClick = {})
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
                    modifier = Modifier.testTag("segmented-control"),
                ) {
                    SingleLine("Text", selected = true, onClick = {})
                    TwoLine("Title", "Subtitle", selected = false, onClick = {})
                    Icon(LeboncoinIcons.ShoppingCartOutline, selected = false, onClick = {})
                    IconText(LeboncoinIcons.ShoppingCartOutline, "Cart", selected = false, onClick = {})
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
    fun number_segment_displays_correct_value() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(selectedIndex = 0) {
                    Number(42, selected = true, onClick = {})
                    Number(99, selected = false, onClick = {})
                }
            }
        }

        val tabs = composeTestRule.onAllNodes(withRole(Role.Tab))
        assert(tabs.fetchSemanticsNodes().size == 2)
        tabs[0].assertIsEnabled()
        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText("99").assertIsDisplayed()
    }

    @Test
    fun custom_indicator_content_is_composed() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = 0,
                    indicatorContent = { Box(Modifier.testTag("custom-indicator")) },
                ) {
                    SingleLine("A", selected = true, onClick = {})
                    SingleLine("B", selected = false, onClick = {})
                }
            }
        }

        composeTestRule.onNodeWithTag("custom-indicator").assertExists()
    }

    @Test
    fun header_title_and_link_text_are_displayed() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = 0,
                    title = "Sort by",
                    linkText = "Reset",
                    onLinkClick = {},
                ) {
                    SingleLine("A", selected = true, onClick = {})
                    SingleLine("B", selected = false, onClick = {})
                }
            }
        }

        composeTestRule.onNodeWithText("Sort by").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
    }

    @Test
    fun disabled_control_does_not_fire_callback() {
        var clicked = false

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(
                    selectedIndex = 0,
                    enabled = false,
                ) {
                    SingleLine("A", selected = true, onClick = { clicked = true })
                    SingleLine("B", selected = false, onClick = { clicked = true })
                }
            }
        }

        val tabs = composeTestRule.onAllNodes(withRole(Role.Tab))
        val count = tabs.fetchSemanticsNodes().size
        for (i in 0 until count) {
            tabs[i].performClick()
        }

        assert(!clicked) { "Callback must not fire when control is disabled" }
    }

    @Test
    fun selected_segment_has_selected_semantics() {
        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Horizontal(selectedIndex = 1) {
                    listOf("A", "B", "C").forEachIndexed { index, label ->
                        SingleLine(label, selected = index == 1, onClick = {})
                    }
                }
            }
        }

        val tabs = composeTestRule.onAllNodes(withRole(Role.Tab))
        tabs[0].assertIsNotSelected()
        tabs[1].assertIsSelected()
        tabs[2].assertIsNotSelected()
    }

    @Test
    fun vertical_cross_row_selection_fires_correct_index() {
        var selectedIndex by mutableIntStateOf(0)
        var lastCallbackIndex = -1

        composeTestRule.setContent {
            PreviewTheme {
                SegmentedControl.Vertical(selectedIndex = selectedIndex) {
                    listOf("1", "2", "3", "4", "5", "6").forEachIndexed { index, label ->
                        SingleLine(
                            text = label,
                            selected = index == selectedIndex,
                            onClick = {
                                lastCallbackIndex = index
                                selectedIndex = index
                            },
                        )
                    }
                }
            }
        }

        val segments = composeTestRule.onAllNodes(withRole(Role.Tab))
        segments[5].performClick()

        assert(lastCallbackIndex == 5)
        assert(selectedIndex == 5)
    }
}
