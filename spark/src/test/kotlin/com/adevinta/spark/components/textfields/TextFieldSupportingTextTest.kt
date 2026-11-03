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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Constraints
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "xhdpi")
class TextFieldSupportingTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun message_stays_visible_when_max_height_equals_natural_height() {
        var naturalHeight = 0
        var maxHeight by mutableStateOf(Constraints.Infinity)
        composeTestRule.setContent {
            PreviewTheme {
                Row {
                    MultilineTextField(
                        value = "",
                        onValueChange = {},
                        onCancelClick = {},
                        required = true,
                        label = "Label",
                        placeholder = "Placeholder",
                        state = FormFieldStatus.Error,
                        stateMessage = "Error text",
                        minLines = 3,
                        modifier = Modifier
                            .testTag("field")
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints.copy(maxHeight = maxHeight))
                                if (maxHeight == Constraints.Infinity) naturalHeight = placeable.height
                                layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                            },
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        val naturalMessageBounds = composeTestRule.onNodeWithText("Error text", useUnmergedTree = true)
            .getBoundsInRoot()

        maxHeight = naturalHeight
        composeTestRule.waitForIdle()

        val fieldHeight = composeTestRule.onNodeWithTag("field").fetchSemanticsNode().size.height
        assertEquals(naturalHeight, fieldHeight)
        val messageBounds = composeTestRule.onNodeWithText("Error text", useUnmergedTree = true)
            .getBoundsInRoot()
        assertEquals(naturalMessageBounds, messageBounds)
        assertTrue(messageBounds.bottom > messageBounds.top)
    }

    @Test
    fun wrapped_message_stays_visible_when_max_width_is_unbounded() {
        val message = "This error message is long enough to wrap over several lines at the field width"
        var naturalHeight = 0
        var widthWasUnbounded = false
        var maxHeight by mutableStateOf(Constraints.Infinity)
        composeTestRule.setContent {
            PreviewTheme {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    MultilineTextField(
                        value = "",
                        onValueChange = {},
                        onCancelClick = {},
                        required = true,
                        label = "Label",
                        placeholder = "Placeholder",
                        state = FormFieldStatus.Error,
                        stateMessage = message,
                        minLines = 3,
                        modifier = Modifier
                            .testTag("field")
                            .layout { measurable, constraints ->
                                widthWasUnbounded = !constraints.hasBoundedWidth
                                val placeable = measurable.measure(constraints.copy(maxHeight = maxHeight))
                                if (maxHeight == Constraints.Infinity) naturalHeight = placeable.height
                                layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                            },
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        assertTrue(widthWasUnbounded)
        val messageNode = composeTestRule.onNodeWithText(message, useUnmergedTree = true)
        assertTrue(messageNode.lineCount() > 1)
        val naturalMessageBounds = messageNode.getBoundsInRoot()
        val naturalChildBounds = fieldChildBounds()

        maxHeight = naturalHeight
        composeTestRule.waitForIdle()

        val fieldHeight = composeTestRule.onNodeWithTag("field").fetchSemanticsNode().size.height
        assertEquals(naturalHeight, fieldHeight)
        assertEquals(naturalMessageBounds, messageNode.getBoundsInRoot())
        assertEquals(naturalChildBounds, fieldChildBounds())
    }

    private fun fieldChildBounds(): List<Rect> = composeTestRule
        .onNodeWithTag("field", useUnmergedTree = true)
        .onChildren()
        .fetchSemanticsNodes()
        .map { it.boundsInRoot }

    private fun SemanticsNodeInteraction.lineCount(): Int {
        val results = mutableListOf<TextLayoutResult>()
        fetchSemanticsNode().config[SemanticsActions.GetTextLayoutResult].action?.invoke(results)
        return results.single().lineCount
    }
}
