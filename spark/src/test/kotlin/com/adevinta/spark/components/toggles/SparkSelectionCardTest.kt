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

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.InputModeManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.test.requestFocus
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.text.Text
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class SparkSelectionCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun withRole(role: Role) = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    private fun withToggleableState(state: ToggleableState) =
        SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, state)

    private val hasSelected = SemanticsMatcher.keyIsDefined(SemanticsProperties.Selected)
    private val hasNoSelected = SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected)
    private val hasToggleableState = SemanticsMatcher.keyIsDefined(SemanticsProperties.ToggleableState)
    private val hasNoToggleableState = SemanticsMatcher.keyNotDefined(SemanticsProperties.ToggleableState)
    private val isFocusable = SemanticsMatcher.keyIsDefined(SemanticsProperties.Focused)

    private lateinit var inputModeManager: InputModeManager

    // Keyboard users reach the card in keyboard mode. Only the card takes focus, the passive toggle does not.
    private fun focusCardWithKeyboard() {
        composeTestRule.runOnIdle { inputModeManager.requestInputMode(InputMode.Keyboard) }
        composeTestRule.onNodeWithTag(CardTag).requestFocus().assertIsFocused()
        composeTestRule.onAllNodes(isFocusable, useUnmergedTree = true).assertCountEquals(1)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun pressOnCard(key: Key) {
        composeTestRule.onNodeWithTag(CardTag).performKeyInput { pressKey(key) }
    }

    private fun setTriStateCard(
        state: ToggleableState,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            inputModeManager = LocalInputModeManager.current
            PreviewTheme {
                SparkSelectionCard(
                    state = state,
                    onClick = onClick,
                    modifier = Modifier.testTag(CardTag),
                    enabled = enabled,
                    toggle = { SparkCheckbox(state = state, onClick = null) },
                ) {
                    Text(Title)
                }
            }
        }
    }

    private fun setToggleableCard(
        checked: Boolean,
        enabled: Boolean = true,
        onCheckedChange: (Boolean) -> Unit = {},
    ) {
        composeTestRule.setContent {
            inputModeManager = LocalInputModeManager.current
            PreviewTheme {
                SparkSelectionCard(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.testTag(CardTag),
                    enabled = enabled,
                    toggle = { SparkSwitch(checked = checked, onCheckedChange = null) },
                ) {
                    Text(Title)
                }
            }
        }
    }

    private fun setSelectableCard(
        selected: Boolean,
        enabled: Boolean = true,
        onClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            inputModeManager = LocalInputModeManager.current
            PreviewTheme {
                SparkSelectionCard(
                    selected = selected,
                    onClick = onClick,
                    modifier = Modifier.testTag(CardTag),
                    enabled = enabled,
                    toggle = { SparkRadioButton(selected = selected, onClick = null) },
                ) {
                    Text(Title)
                }
            }
        }
    }

    // The toggle and the content merge into the card, so only one node is clickable.
    private fun assertSingleMergedNode() {
        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
        composeTestRule.onNodeWithTag(CardTag).assert(hasText(Title)).assert(hasClickAction())
    }

    @Test
    fun triStateCard_exposesToggleableStateAndNotSelected() {
        setTriStateCard(state = ToggleableState.On)

        assertSingleMergedNode()
        composeTestRule.onNodeWithTag(CardTag)
            .assert(withRole(Role.Checkbox))
            .assert(withToggleableState(ToggleableState.On))
            .assert(hasNoSelected)
        composeTestRule.onAllNodes(hasSelected, useUnmergedTree = true).assertCountEquals(0)
        composeTestRule.onAllNodes(hasToggleableState, useUnmergedTree = true).assertCountEquals(1)
    }

    @Test
    fun triStateCard_exposesIndeterminateState() {
        setTriStateCard(state = ToggleableState.Indeterminate)

        composeTestRule.onNodeWithTag(CardTag)
            .assert(withToggleableState(ToggleableState.Indeterminate))
            .assert(hasNoSelected)
    }

    @Test
    fun triStateCard_clickFiresOnClick() {
        var clicks = 0
        setTriStateCard(state = ToggleableState.Off, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).assertIsEnabled().performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun triStateCard_disabledIsNotClickable() {
        var clicks = 0
        setTriStateCard(state = ToggleableState.Off, enabled = false, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).assertIsNotEnabled().performClick()

        assertEquals(0, clicks)
    }

    @Test
    fun toggleableCard_exposesToggleableStateAndNotSelected() {
        setToggleableCard(checked = true)

        assertSingleMergedNode()
        composeTestRule.onNodeWithTag(CardTag)
            .assert(withRole(Role.Switch))
            .assert(withToggleableState(ToggleableState.On))
            .assert(hasNoSelected)
        composeTestRule.onAllNodes(hasSelected, useUnmergedTree = true).assertCountEquals(0)
        composeTestRule.onAllNodes(hasToggleableState, useUnmergedTree = true).assertCountEquals(1)
    }

    @Test
    fun toggleableCard_clickRequestsTheOppositeValue() {
        var requested: Boolean? = null
        setToggleableCard(checked = false, onCheckedChange = { requested = it })

        composeTestRule.onNodeWithTag(CardTag).performClick()

        assertEquals(true, requested)
    }

    @Test
    fun toggleableCard_disabledIsNotClickable() {
        var requested: Boolean? = null
        setToggleableCard(checked = false, enabled = false, onCheckedChange = { requested = it })

        composeTestRule.onNodeWithTag(CardTag).assertIsNotEnabled().performClick()

        assertNull(requested)
    }

    @Test
    fun selectableCard_exposesSelectedAndNotToggleableState() {
        setSelectableCard(selected = true)

        assertSingleMergedNode()
        composeTestRule.onNodeWithTag(CardTag)
            .assert(withRole(Role.RadioButton))
            .assertIsSelected()
            .assert(hasNoToggleableState)
        composeTestRule.onAllNodes(hasSelected, useUnmergedTree = true).assertCountEquals(1)
        composeTestRule.onAllNodes(hasToggleableState, useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    fun selectableCard_clickFiresOnClick() {
        var clicks = 0
        setSelectableCard(selected = false, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun selectableCard_disabledIsNotClickable() {
        var clicks = 0
        setSelectableCard(selected = false, enabled = false, onClick = { clicks++ })

        composeTestRule.onNodeWithTag(CardTag).assertIsNotEnabled().performClick()

        assertEquals(0, clicks)
    }

    @Test
    fun triStateCard_keyboardFocusesTheCardAndEnterOrSpaceClicksIt() {
        var clicks = 0
        setTriStateCard(state = ToggleableState.Off, onClick = { clicks++ })

        focusCardWithKeyboard()
        pressOnCard(Key.Enter)
        pressOnCard(Key.Spacebar)

        assertEquals(2, clicks)
    }

    @Test
    fun toggleableCard_keyboardFocusesTheCardAndEnterOrSpaceToggles() {
        val requested = mutableListOf<Boolean>()
        setToggleableCard(checked = false, onCheckedChange = { requested += it })

        focusCardWithKeyboard()
        pressOnCard(Key.Enter)
        pressOnCard(Key.Spacebar)

        assertEquals(listOf(true, true), requested)
    }

    @Test
    fun selectableCard_keyboardFocusesTheCardAndEnterOrSpaceClicksIt() {
        var clicks = 0
        setSelectableCard(selected = false, onClick = { clicks++ })

        focusCardWithKeyboard()
        pressOnCard(Key.Enter)
        pressOnCard(Key.Spacebar)

        assertEquals(2, clicks)
    }

    @Test
    fun disabledCard_isNotFocusable() {
        setTriStateCard(state = ToggleableState.Off, enabled = false)

        composeTestRule.runOnIdle { inputModeManager.requestInputMode(InputMode.Keyboard) }

        composeTestRule.onAllNodes(isFocusable, useUnmergedTree = true).assertCountEquals(0)
    }

    private companion object {
        const val CardTag = "selection-card"
        const val Title = "Card title"
    }
}
