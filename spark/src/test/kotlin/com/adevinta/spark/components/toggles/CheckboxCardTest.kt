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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.text.Text
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CheckboxCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    private fun withToggleableState(state: ToggleableState) =
        SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, state)

    private val hasSelected = SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)
    private val hasNoSelected = SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected)

    private lateinit var view: View

    // TalkBack reads the platform node, so read the same node that the screen reader gets.
    private fun accessibilityNodeInfo(id: Int): AccessibilityNodeInfo = composeTestRule.runOnIdle {
        requireNotNull(view.accessibilityNodeProvider?.createAccessibilityNodeInfo(id))
    }

    private fun setCard(
        state: ToggleableState,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
        toggle: (@Composable () -> Unit)? = null,
    ) {
        composeTestRule.setContent {
            view = LocalView.current
            PreviewTheme {
                if (toggle == null) {
                    Checkbox.Card(
                        state = state,
                        onClick = onClick,
                        modifier = Modifier.testTag(CardTag),
                        enabled = enabled,
                    ) { Text(Title) }
                } else {
                    Checkbox.Card(
                        state = state,
                        onClick = onClick,
                        modifier = Modifier.testTag(CardTag),
                        enabled = enabled,
                        toggle = toggle,
                    ) { Text(Title) }
                }
            }
        }
    }

    private fun assertCardState(state: ToggleableState) {
        composeTestRule.onNodeWithTag(CardTag)
            .assert(withRole(Role.Checkbox))
            .assert(withToggleableState(state))
            .assert(hasNoSelected)
        composeTestRule.onAllNodes(hasSelected, useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    fun card_isOneMergedNode() {
        setCard(state = ToggleableState.On)

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag).assert(hasText(Title)).assert(hasClickAction())
    }

    @Test
    fun card_exposesOn() {
        setCard(state = ToggleableState.On)

        assertCardState(ToggleableState.On)
    }

    @Test
    fun card_exposesOff() {
        setCard(state = ToggleableState.Off)

        assertCardState(ToggleableState.Off)
    }

    @Test
    fun card_exposesIndeterminate() {
        setCard(state = ToggleableState.Indeterminate)

        assertCardState(ToggleableState.Indeterminate)
    }

    @Test
    fun card_clickCallsOnClick() {
        var clicks = 0
        setCard(state = ToggleableState.Off, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).assertIsEnabled().performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun card_disabledIsNotClickable() {
        var clicks = 0
        setCard(state = ToggleableState.Off, enabled = false, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).assertIsNotEnabled().performClick()

        assertEquals(0, clicks)
    }

    @Test
    fun card_emptyToggleKeepsRole() {
        setCard(state = ToggleableState.On, toggle = {})

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        assertCardState(ToggleableState.On)
    }

    @Test
    fun card_isTheOnlyScreenReaderStopAndCarriesTheState() {
        setCard(state = ToggleableState.Indeterminate)

        val card = accessibilityNodeInfo(composeTestRule.onNodeWithTag(CardTag).fetchSemanticsNode().id)
        assertTrue(card.isScreenReaderFocusable)
        assertTrue(card.isClickable)
        assertTrue(card.isCheckable)
        assertEquals("Partially checked", card.stateDescription?.toString())

        val titleId = composeTestRule.onNodeWithText(Title, useUnmergedTree = true).fetchSemanticsNode().id
        val title = accessibilityNodeInfo(titleId)
        assertFalse(title.isScreenReaderFocusable)
        assertFalse(title.isFocusable)
        assertFalse(title.isClickable)
    }

    @Test
    fun disabledCard_isReportedAsDisabled() {
        setCard(state = ToggleableState.On, enabled = false)

        val card = accessibilityNodeInfo(composeTestRule.onNodeWithTag(CardTag).fetchSemanticsNode().id)
        assertFalse(card.isEnabled)
        assertTrue(card.isCheckable)
    }

    private companion object {
        const val CardTag = "checkbox-card"
        const val Title = "Card title"
    }
}
