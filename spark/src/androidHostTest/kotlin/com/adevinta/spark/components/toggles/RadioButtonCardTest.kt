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
package com.adevinta.spark.components.toggles

import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.text.Text
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Covers the semantics, clicks, error, group behaviour and screen reader output of [RadioButton.Card]. */
@RunWith(RobolectricTestRunner::class)
class RadioButtonCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    private val hasNoToggleableState = SemanticsMatcher.keyNotDefined(SemanticsProperties.ToggleableState)
    private val hasNoError = SemanticsMatcher.keyNotDefined(SemanticsProperties.Error)

    private lateinit var view: View

    // TalkBack reads the platform node, so read the same node that the screen reader gets.
    private fun accessibilityNodeInfo(tag: String): AccessibilityNodeInfo {
        val id = composeTestRule.onNodeWithTag(tag).fetchSemanticsNode().id
        return composeTestRule.runOnIdle {
            requireNotNull(view.accessibilityNodeProvider?.createAccessibilityNodeInfo(id))
        }
    }

    private fun setCard(
        selected: Boolean,
        enabled: Boolean = true,
        error: String? = null,
        onClick: () -> Unit = {},
        toggle: (@Composable RadioButtonCardScope.() -> SelectionCardItem?)? = null,
    ) {
        composeTestRule.setContent {
            view = LocalView.current
            PreviewTheme {
                if (toggle == null) {
                    RadioButton.Card(
                        selected = selected,
                        onClick = onClick,
                        modifier = Modifier.testTag(CardTag),
                        enabled = enabled,
                        error = error,
                    ) { Text(Title) }
                } else {
                    RadioButton.Card(
                        selected = selected,
                        onClick = onClick,
                        modifier = Modifier.testTag(CardTag),
                        enabled = enabled,
                        error = error,
                        toggle = toggle,
                    ) { Text(Title) }
                }
            }
        }
    }

    @Test
    fun card_exposesRoleAndSelectedButNotToggleableState() {
        setCard(selected = true)

        composeTestRule.onNodeWithTag(CardTag)
            .assert(withRole(Role.RadioButton))
            .assertIsSelected()
            .assert(hasNoToggleableState)
    }

    @Test
    fun errorCard_exposesErrorAndKeepsRoleAndSelected() {
        setCard(selected = true, error = ErrorText)

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Error, ErrorText))
            .assert(withRole(Role.RadioButton))
            .assertIsSelected()
            .assert(hasNoToggleableState)
    }

    @Test
    fun card_withoutErrorExposesNoError() {
        setCard(selected = true)

        composeTestRule.onNodeWithTag(CardTag).assert(hasNoError)
    }

    @Test
    fun errorCard_isReportedAsInvalidToTheScreenReader() {
        setCard(selected = true, error = ErrorText)

        val card = accessibilityNodeInfo(CardTag)
        assertTrue(card.isContentInvalid)
        assertEquals(ErrorText, card.error?.toString())
        assertTrue(card.isCheckable)
    }

    @Test
    fun card_withBlankErrorFails() {
        // The blank message fails during composition, so the wait for idle stays inside the block.
        assertFailsWith<IllegalArgumentException> {
            setCard(selected = true, error = " ")
            composeTestRule.waitForIdle()
        }
    }

    @Test
    fun card_withoutErrorIsNotReportedAsInvalid() {
        setCard(selected = true)

        val card = accessibilityNodeInfo(CardTag)
        assertFalse(card.isContentInvalid)
        assertNull(card.error)
    }

    @Test
    fun card_isOneMergedNode() {
        setCard(selected = false)

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag).assert(hasText(Title)).assert(hasClickAction())
    }

    @Test
    fun card_clickCallsOnClick() {
        var clicks = 0
        setCard(selected = false, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun card_disabledIsNotClickable() {
        var clicks = 0
        setCard(selected = false, enabled = false, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).assertIsNotEnabled().performClick()

        assertEquals(0, clicks)
    }

    @Test
    fun group_keepsSingleSelection() {
        var selectedIndex by mutableIntStateOf(0)
        val count = 3

        composeTestRule.setContent {
            view = LocalView.current
            PreviewTheme {
                Column(modifier = Modifier.selectableGroup()) {
                    repeat(count) { index ->
                        RadioButton.Card(
                            selected = index == selectedIndex,
                            onClick = { selectedIndex = index },
                            modifier = Modifier.testTag("$CardTag-$index"),
                        ) {
                            Text("Option $index")
                        }
                    }
                }
            }
        }

        composeTestRule.onAllNodes(withRole(Role.RadioButton)).assertCountEquals(count)
        listOf(1, 2, 0).forEach { clicked ->
            composeTestRule.onNodeWithTag("$CardTag-$clicked").performClick()

            assertEquals(clicked, selectedIndex)
            composeTestRule.onAllNodes(isSelected()).assertCountEquals(1)
            repeat(count) { index ->
                val node = composeTestRule.onNodeWithTag("$CardTag-$index")
                if (index == clicked) node.assertIsSelected() else node.assertIsNotSelected()
            }
        }
    }

    @Test
    fun group_exposesCollectionInfo() {
        val count = 3
        composeTestRule.setContent {
            view = LocalView.current
            PreviewTheme {
                Column(modifier = Modifier.testTag(GroupTag).selectableGroup()) {
                    repeat(count) { index ->
                        RadioButton.Card(
                            selected = index == 1,
                            onClick = {},
                            modifier = Modifier.testTag("$CardTag-$index"),
                        ) {
                            Text("Option $index")
                        }
                    }
                }
            }
        }

        val collection = assertNotNull(accessibilityNodeInfo(GroupTag).collectionInfo)
        assertEquals(count, collection.rowCount)
        assertEquals(1, collection.columnCount)
        repeat(count) { index ->
            val card = accessibilityNodeInfo("$CardTag-$index")
            val item = assertNotNull(card.collectionItemInfo)
            assertEquals(index, item.rowIndex)
            assertEquals(index == 1, item.isSelected)
            assertEquals(if (index == 1) "Selected" else "Not selected", card.stateDescription?.toString())
        }
    }

    @Test
    fun card_emptyToggleKeepsRoleAndState() {
        setCard(selected = true, toggle = { null })

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag)
            .assert(hasText(Title))
            .assert(withRole(Role.RadioButton))
            .assertIsSelected()
            .assert(hasNoToggleableState)
    }

    @Test
    fun card_isOneCheckableScreenReaderStop() {
        setCard(selected = false)

        val card = accessibilityNodeInfo(CardTag)
        assertTrue(card.isScreenReaderFocusable)
        assertTrue(card.isCheckable)
        assertTrue(card.isClickable)
    }

    private companion object {
        const val GroupTag = "radio-group"
        const val CardTag = "radio-card"
        const val Title = "Card title"
        const val ErrorText = "Select another option"
    }
}
