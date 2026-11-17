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

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Covers [Switch.Standalone], which adds a minimum touch target only when it has a callback. */
@RunWith(RobolectricTestRunner::class)
class SwitchStandaloneTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setSwitch(onCheckedChange: ((Boolean) -> Unit)?, enabled: Boolean = true) {
        composeTestRule.setContent {
            PreviewTheme {
                // The size tests measure this box, because it holds the space that the switch takes in the layout.
                Box(Modifier.testTag(CONTAINER_TAG)) {
                    Switch.Standalone(
                        checked = false,
                        onCheckedChange = onCheckedChange,
                        enabled = enabled,
                        modifier = Modifier.testTag(TAG),
                    )
                }
            }
        }
    }

    @Test
    fun withCallback_exposesRoleAndState_andClickCallsCallback() {
        val values = mutableListOf<Boolean>()
        setSwitch(onCheckedChange = { values += it })

        composeTestRule.onNodeWithTag(TAG)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .assertIsOff()
            .performClick()

        composeTestRule.runOnIdle { assertEquals(listOf(true), values) }
    }

    @Test
    fun withNullCallback_exposesNoClickAction() {
        setSwitch(onCheckedChange = null)

        composeTestRule.onNodeWithTag(TAG).assert(!hasClickAction())
    }

    @Test
    fun disabled_isNotClickable() {
        val values = mutableListOf<Boolean>()
        setSwitch(onCheckedChange = { values += it }, enabled = false)

        composeTestRule.onNodeWithTag(TAG).assertIsNotEnabled().performClick()

        composeTestRule.runOnIdle { assertTrue(values.isEmpty()) }
    }

    @Test
    fun withCallback_hasMinimumTouchTarget() {
        setSwitch(onCheckedChange = {})

        composeTestRule.onNodeWithTag(CONTAINER_TAG).assertHeightIsAtLeast(44.dp)
    }

    @Test
    fun withNullCallback_hasTrackHeightOnly() {
        setSwitch(onCheckedChange = null)

        // The Material switch track is 32 dp high.
        composeTestRule.onNodeWithTag(CONTAINER_TAG).assertHeightIsEqualTo(32.dp)
    }

    private companion object {
        const val TAG = "switch"
        const val CONTAINER_TAG = "container"
    }
}
