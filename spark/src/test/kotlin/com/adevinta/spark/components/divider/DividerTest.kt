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
package com.adevinta.spark.components.divider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.times
import androidx.compose.ui.unit.width
import com.adevinta.spark.PreviewTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DividerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun start_line_is_fixed_on_the_left_in_ltr() {
        setHorizontal(LayoutDirection.Ltr, startFixed = true)

        assertBounds(StartTag, left = 0.dp, width = MinLine)
        assertBounds(LabelTag, left = MinLine, width = LabelSize)
        assertBounds(EndTag, left = MinLine + LabelSize, width = Length - MinLine - LabelSize)
    }

    @Test
    fun start_line_is_fixed_on_the_right_in_rtl() {
        setHorizontal(LayoutDirection.Rtl, startFixed = true)

        assertBounds(StartTag, left = Length - MinLine, width = MinLine)
        assertBounds(LabelTag, left = Length - MinLine - LabelSize, width = LabelSize)
        assertBounds(EndTag, left = 0.dp, width = Length - MinLine - LabelSize)
    }

    @Test
    fun end_line_is_fixed_on_the_right_in_ltr() {
        setHorizontal(LayoutDirection.Ltr, endFixed = true)

        assertBounds(StartTag, left = 0.dp, width = Length - MinLine - LabelSize)
        assertBounds(EndTag, left = Length - MinLine, width = MinLine)
    }

    @Test
    fun end_line_is_fixed_on_the_left_in_rtl() {
        setHorizontal(LayoutDirection.Rtl, endFixed = true)

        assertBounds(StartTag, left = MinLine + LabelSize, width = Length - MinLine - LabelSize)
        assertBounds(EndTag, left = 0.dp, width = MinLine)
    }

    @Test
    fun lines_split_the_space_equally_when_both_are_flexible() {
        setHorizontal(LayoutDirection.Ltr)

        assertBounds(StartTag, width = (Length - LabelSize) / 2)
        assertBounds(EndTag, width = (Length - LabelSize) / 2)
    }

    @Test
    fun oversized_label_leaves_the_minimum_line_length() {
        setHorizontal(LayoutDirection.Ltr, labelSize = 400.dp)

        assertBounds(StartTag, width = MinLine)
        assertBounds(LabelTag, width = Length - 2 * MinLine)
        assertBounds(EndTag, width = MinLine)
    }

    @Test
    fun lines_fit_in_the_parent_when_it_is_narrower_than_two_minimum_lines() {
        val narrow = 60.dp
        setHorizontal(LayoutDirection.Ltr, length = narrow)

        val start = bounds(StartTag)
        val end = bounds(EndTag)
        assertEquals(narrow.value, (start.width + end.width + bounds(LabelTag).width).value, Delta)
        assertEquals(0f, start.left.value, Delta)
        assertEquals(narrow.value, end.right.value, Delta)
    }

    @Test
    fun fixed_line_shrinks_last_when_the_parent_is_narrow() {
        setHorizontal(LayoutDirection.Ltr, startFixed = true, length = 60.dp)

        assertBounds(StartTag, left = 0.dp, width = MinLine)
        assertBounds(EndTag, left = MinLine, width = 20.dp)
    }

    @Test
    fun vertical_start_line_is_fixed_at_the_top() {
        composeTestRule.setContent {
            PreviewTheme {
                Box(Modifier.height(Length).testTag(RootTag)) { Probe(isHorizontal = false, startFixed = true) }
            }
        }

        assertEquals(0f, bounds(StartTag).top.value, Delta)
        assertEquals(MinLine.value, bounds(StartTag).height.value, Delta)
        assertEquals(MinLine.value, bounds(LabelTag).top.value, Delta)
        assertEquals((Length - MinLine - LabelSize).value, bounds(EndTag).height.value, Delta)
    }

    @Test
    fun horizontal_divider_supports_intrinsic_width() {
        composeTestRule.setContent {
            PreviewTheme {
                Column(Modifier.width(IntrinsicSize.Min).testTag(RootTag)) {
                    HorizontalDivider(label = { Box(Modifier.size(LabelSize)) })
                }
            }
        }

        assertEquals((2 * MinLine + 2 * LabelPadding + LabelSize).value, bounds(RootTag).width.value, Delta)
    }

    @Test
    fun vertical_divider_supports_intrinsic_height() {
        composeTestRule.setContent {
            PreviewTheme {
                Row(Modifier.height(IntrinsicSize.Min).testTag(RootTag)) {
                    VerticalDivider(label = { Box(Modifier.size(LabelSize)) })
                }
            }
        }

        assertEquals((2 * MinLine + 2 * LabelPadding + LabelSize).value, bounds(RootTag).height.value, Delta)
    }

    private fun setHorizontal(
        direction: LayoutDirection,
        startFixed: Boolean = false,
        endFixed: Boolean = false,
        length: Dp = Length,
        labelSize: Dp = LabelSize,
    ) {
        composeTestRule.setContent {
            PreviewTheme {
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    Box(Modifier.width(length).testTag(RootTag)) {
                        Probe(isHorizontal = true, startFixed = startFixed, endFixed = endFixed, labelSize = labelSize)
                    }
                }
            }
        }
    }

    /** The bounds of [tag], relative to the sized box that holds the divider. */
    private fun bounds(tag: String): DpRect {
        val node = composeTestRule.onNodeWithTag(tag).getBoundsInRoot()
        if (tag == RootTag) return node
        val root = composeTestRule.onNodeWithTag(RootTag).getBoundsInRoot()
        return DpRect(node.left - root.left, node.top - root.top, node.right - root.left, node.bottom - root.top)
    }

    private fun assertBounds(tag: String, left: Dp? = null, width: Dp? = null) {
        val actual = bounds(tag)
        left?.let { assertEquals("$tag left", it.value, actual.left.value, Delta) }
        width?.let { assertEquals("$tag width", it.value, actual.width.value, Delta) }
    }

    private companion object {
        const val RootTag = "root"
        const val StartTag = "start"
        const val LabelTag = "label"
        const val EndTag = "end"
        val Length: Dp = 200.dp
        val MinLine: Dp = 40.dp
        val LabelPadding: Dp = 16.dp
        val LabelSize: Dp = 20.dp
        const val Delta = 0.6f
    }
}

/** Three tagged boxes in place of the two lines and the label, so that the test reads the real line lengths. */
@Composable
private fun Probe(
    isHorizontal: Boolean,
    startFixed: Boolean = false,
    endFixed: Boolean = false,
    labelSize: Dp = 20.dp,
) {
    val line = if (isHorizontal) Modifier.fillMaxWidth().height(1.dp) else Modifier.fillMaxHeight().width(1.dp)
    LabelledDivider(
        isHorizontal = isHorizontal,
        isStartLineFixed = startFixed,
        isEndLineFixed = endFixed,
    ) {
        Box(line.testTag("start"))
        Box(Modifier.size(labelSize).testTag("label"))
        Box(line.testTag("end"))
    }
}
