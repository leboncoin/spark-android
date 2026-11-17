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
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Covers the semantics, clicks and screen reader output of [Switch.Card]. */
@RunWith(RobolectricTestRunner::class)
class SwitchCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    private fun withToggleableState(state: ToggleableState) =
        SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, state)

    private val hasSelected = SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)
    private val hasNoSelected = SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected)

    private lateinit var view: View

    // TalkBack reads the platform node, so read the same node that the screen reader gets.
    private fun accessibilityNodeInfo(): AccessibilityNodeInfo {
        val id = composeTestRule.onNodeWithTag(CardTag).fetchSemanticsNode().id
        return composeTestRule.runOnIdle {
            requireNotNull(view.accessibilityNodeProvider?.createAccessibilityNodeInfo(id))
        }
    }

    private fun setCard(
        checked: Boolean,
        enabled: Boolean = true,
        onCheckedChange: (Boolean) -> Unit = {},
        toggle: (@Composable SwitchCardScope.() -> SelectionCardItem?)? = null,
    ) {
        composeTestRule.setContent {
            view = LocalView.current
            PreviewTheme {
                if (toggle == null) {
                    Switch.Card(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier.testTag(CardTag),
                        enabled = enabled,
                    ) { Text(Title) }
                } else {
                    Switch.Card(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        modifier = Modifier.testTag(CardTag),
                        enabled = enabled,
                        toggle = toggle,
                    ) { Text(Title) }
                }
            }
        }
    }

    @Test
    fun on_exposesSwitchRoleAndOnState() {
        setCard(checked = true)

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag)
            .assert(hasText(Title))
            .assert(hasClickAction())
            .assert(withRole(Role.Switch))
            .assert(withToggleableState(ToggleableState.On))
            .assert(hasNoSelected)
        composeTestRule.onAllNodes(hasSelected, useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    fun off_exposesOffState() {
        setCard(checked = false)

        composeTestRule.onNodeWithTag(CardTag)
            .assert(withRole(Role.Switch))
            .assert(withToggleableState(ToggleableState.Off))
            .assert(hasNoSelected)
    }

    @Test
    fun click_requestsTheOppositeValue() {
        var requested: Boolean? = null
        setCard(checked = true, onCheckedChange = { requested = it })

        composeTestRule.onNodeWithTag(CardTag).performClick()

        assertEquals(false, requested)
    }

    @Test
    fun disabled_isNotClickable() {
        var requested: Boolean? = null
        setCard(checked = false, enabled = false, onCheckedChange = { requested = it })

        composeTestRule.onNodeWithTag(CardTag).assertIsNotEnabled().performClick()

        assertNull(requested)
    }

    @Test
    fun emptyToggle_keepsRoleAndState() {
        setCard(checked = true, toggle = { null })

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag)
            .assert(hasText(Title))
            .assert(withRole(Role.Switch))
            .assert(withToggleableState(ToggleableState.On))
            .assert(hasNoSelected)
    }

    @Test
    fun on_isOneCheckableScreenReaderStopThatSaysOn() {
        setCard(checked = true)

        val card = accessibilityNodeInfo()
        assertTrue(card.isScreenReaderFocusable)
        assertTrue(card.isCheckable)
        assertTrue(card.isClickable)
        assertEquals("On", card.stateDescription?.toString())
    }

    private companion object {
        const val CardTag = "switch-card"
        const val Title = "Card title"
    }
}
