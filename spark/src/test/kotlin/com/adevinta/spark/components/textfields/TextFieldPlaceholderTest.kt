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
package com.adevinta.spark.components.textfields

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "xhdpi")
class TextFieldPlaceholderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun placeholder_is_displayed_when_unfocused_and_has_no_label() {
        composeTestRule.setContent {
            PreviewTheme {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Placeholder",
                )
            }
        }

        composeTestRule.onNodeWithText("Placeholder").assertIsDisplayed()
    }

    @Test
    fun placeholder_is_not_displayed_when_unfocused_and_has_label() {
        composeTestRule.setContent {
            PreviewTheme {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = "Label",
                    placeholder = "Placeholder",
                )
            }
        }

        composeTestRule.onNodeWithText("Placeholder").assertDoesNotExist()
    }

    @Test
    fun multiline_placeholder_is_displayed_when_unfocused_and_has_no_label() {
        composeTestRule.setContent {
            PreviewTheme {
                MultilineTextField(
                    value = "",
                    onValueChange = {},
                    onCancelClick = {},
                    placeholder = "Write a message…",
                )
            }
        }

        composeTestRule.onNodeWithText("Write a message…").assertIsDisplayed()
    }
}
