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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

/** Covers the semantics and clicks of [RadioButton.Standalone], with and without a callback. */
@RunWith(RobolectricTestRunner::class)
class RadioButtonStandaloneTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setRadioButton(onClick: (() -> Unit)?, enabled: Boolean = true) {
        composeTestRule.setContent {
            PreviewTheme {
                RadioButton.Standalone(
                    selected = true,
                    onClick = onClick,
                    enabled = enabled,
                    modifier = Modifier.testTag(TAG),
                )
            }
        }
    }

    @Test
    fun withCallback_exposesRoleAndState_andClickCallsCallback() {
        var clicks = 0
        setRadioButton(onClick = { clicks++ })

        composeTestRule.onNodeWithTag(TAG)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
            .assertIsSelected()
            .performClick()

        composeTestRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun withNullCallback_exposesNoClickAction() {
        setRadioButton(onClick = null)

        composeTestRule.onNodeWithTag(TAG).assert(!hasClickAction())
    }

    @Test
    fun disabled_isNotClickable() {
        var clicks = 0
        setRadioButton(onClick = { clicks++ }, enabled = false)

        composeTestRule.onNodeWithTag(TAG).assertIsNotEnabled().performClick()

        composeTestRule.runOnIdle { assertEquals(0, clicks) }
    }

    private companion object {
        const val TAG = "radioButton"
    }
}
