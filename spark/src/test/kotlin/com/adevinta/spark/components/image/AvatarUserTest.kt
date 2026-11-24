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
package com.adevinta.spark.components.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AvatarUserTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val isImage = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Image)
    private val hasAnyText = SemanticsMatcher.keyIsDefined(SemanticsProperties.Text)

    @Test
    fun letter_is_hidden_from_accessibility_and_avatar_is_announced() {
        composeTestRule.setContent {
            PreviewTheme { Avatar.User(username = "spark") }
        }

        composeTestRule.onNodeWithContentDescription("Profile photo").assertExists().assert(isImage)
            .assert(hasText("S").not())
        composeTestRule.onNodeWithText("S").assertDoesNotExist()
    }

    @Test
    fun blank_username_shows_no_letter() {
        composeTestRule.setContent {
            PreviewTheme { Avatar.User(username = " \t\n") }
        }

        composeTestRule.onNodeWithContentDescription("Profile photo").assertExists().assert(isImage)
        composeTestRule.onAllNodes(hasAnyText, useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    fun first_letter_is_uppercased() {
        composeTestRule.setContent {
            PreviewTheme { Avatar.User(username = "  élodie") }
        }

        composeTestRule.onAllNodes(hasAnyText, useUnmergedTree = true).assertCountEquals(1)
        composeTestRule.onNodeWithText("É", useUnmergedTree = true).assertExists()
    }

    @Test
    fun null_addon_composes_no_addon() {
        var showAddon by mutableStateOf(false)
        composeTestRule.setContent {
            PreviewTheme {
                Avatar.User(
                    username = "spark",
                    addon = { if (showAddon) custom { Box(Modifier.testTag(ADDON_TAG)) } else null },
                )
            }
        }

        composeTestRule.onNodeWithTag(ADDON_TAG).assertDoesNotExist()
        showAddon = true
        composeTestRule.onNodeWithTag(ADDON_TAG).assertExists()
    }

    @Test
    fun custom_addon_renders_its_content() {
        composeTestRule.setContent {
            PreviewTheme {
                Avatar.User(
                    username = "spark",
                    addon = { custom { Box(Modifier.testTag(ADDON_TAG).size(16.dp)) } },
                )
            }
        }

        composeTestRule.onNodeWithTag(ADDON_TAG).assertExists()
    }

    @Test
    fun icon_button_addon_calls_on_click_once() {
        var clicks = 0
        composeTestRule.setContent {
            PreviewTheme {
                Avatar.User(
                    username = "spark",
                    size = AvatarSize.XL,
                    addon = { iconButton(onClick = { clicks++ }, modifier = Modifier.testTag(ADDON_TAG)) },
                )
            }
        }

        composeTestRule.onNodeWithTag(ADDON_TAG).performClick()
        assertEquals(1, clicks)
    }

    private companion object {
        const val ADDON_TAG = "addon"
    }
}
