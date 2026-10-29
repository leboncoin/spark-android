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
package com.adevinta.spark.iconbutton

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.iconbuttons.IconButton
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.iconbuttons.Primary
import com.adevinta.spark.icons.GearOutline
import com.adevinta.spark.icons.LeboncoinIcons
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class IconButtonSemanticsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val label = "Settings"

    private val loadingLabel: String
        get() = RuntimeEnvironment.getApplication().getString(R.string.spark_icon_button_loading_a11y)

    // The Material 3 TooltipBox puts its long-press "show tooltip" action on its own wrapper node.
    private val hasTooltipAnchorAction = SemanticsMatcher.keyIsDefined(SemanticsActions.OnLongClick)

    private fun button(): SemanticsNodeInteraction = composeTestRule.onNode(hasClickAction())

    private fun setPrimary(isLoading: Boolean, onClick: () -> Unit = {}) {
        composeTestRule.setContent {
            SparkTheme {
                IconButton.Primary(
                    icon = LeboncoinIcons.GearOutline,
                    onClick = onClick,
                    contentDescription = label,
                    isLoading = isLoading,
                )
            }
        }
    }

    @Test
    fun givenPrimary_ThenNodeHasButtonRole() {
        setPrimary(isLoading = false)

        button().assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
    }

    @Test
    fun givenPrimary_ThenLabelIsReadOnceAndNoStateDescription() {
        setPrimary(isLoading = false)

        button()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ContentDescription, listOf(label)))
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription))
    }

    @Test
    fun givenPrimaryLoading_ThenLabelStaysAndStateDescriptionIsLoading() {
        setPrimary(isLoading = true)

        button()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ContentDescription, listOf(label)))
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, loadingLabel))
    }

    @Test
    fun givenPrimaryLoading_ThenSpinnerIsNotAnAccessibilityNode() {
        setPrimary(isLoading = true)

        composeTestRule
            .onAllNodes(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate), useUnmergedTree = true)
            .assertCountEquals(0)
    }

    @Test
    fun givenPrimary_WhenClicked_ThenOnClickIsCalled() {
        var clicks = 0
        setPrimary(isLoading = false, onClick = { clicks++ })

        button().performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun givenPrimaryLoading_WhenClicked_ThenOnClickIsNotCalled() {
        var clicks = 0
        setPrimary(isLoading = true, onClick = { clicks++ })

        button().performClick()

        assertEquals(0, clicks)
    }

    @Test
    fun givenPrimary_WhenLongClicked_ThenTooltipShowsLabel() {
        setPrimary(isLoading = false)

        composeTestRule.onAllNodes(hasTooltipAnchorAction).assertCountEquals(1)
        button().performTouchInput { longClick() }

        composeTestRule.onAllNodes(isPopup()).assertCountEquals(1)
        composeTestRule.onNode(hasAnyAncestor(isPopup()) and hasText(label)).assertExists()
    }

    @Test
    fun givenLegacyFilledWithoutLabel_ThenNoContentDescriptionAndNoTooltip() {
        composeTestRule.setContent {
            SparkTheme {
                IconButtonFilled(
                    icon = LeboncoinIcons.GearOutline,
                    onClick = {},
                    contentDescription = null,
                )
            }
        }

        button()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentDescription))
        composeTestRule.onAllNodes(hasTooltipAnchorAction).assertCountEquals(0)
        button().performTouchInput { longClick() }

        composeTestRule.onAllNodes(isPopup()).assertCountEquals(0)
    }
}
