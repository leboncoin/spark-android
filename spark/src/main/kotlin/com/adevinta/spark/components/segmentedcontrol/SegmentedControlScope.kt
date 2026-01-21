/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.components.segmentedcontrol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.icons.SparkIcon

/**
 * Scope interface for adding segments to a [SegmentedControl].
 *
 * This scope provides methods to add different types of segments:
 * - [SingleLine] - Single line of text
 * - [TwoLine] - Two lines (title and subtitle)
 * - [Icon] - Icon only
 * - [IconText] - Icon with text
 * - [Number] - Number display
 * - [Custom] - Custom content with customizable selected background color
 */
@Stable
public interface SegmentedControlScope {
    /**
     * Adds a single-line text segment.
     *
     * @param text The text to display in the segment
     * @param modifier Optional modifier to apply to the segment
     */
    @Composable
    public fun SingleLine(
        text: String,
        modifier: Modifier = Modifier,
    )

    /**
     * Adds a two-line text segment with title and subtitle.
     *
     * @param title The title text displayed on the first line
     * @param subtitle The subtitle text displayed on the second line
     * @param modifier Optional modifier to apply to the segment
     */
    @Composable
    public fun TwoLine(
        title: String,
        subtitle: String,
        modifier: Modifier = Modifier,
    )

    /**
     * Adds an icon-only segment.
     *
     * @param icon The icon to display
     * @param modifier Optional modifier to apply to the segment
     */
    @Composable
    public fun Icon(
        icon: SparkIcon,
        modifier: Modifier = Modifier,
    )

    /**
     * Adds a segment with an icon and text.
     *
     * @param icon The icon to display
     * @param text The text to display alongside the icon
     * @param modifier Optional modifier to apply to the segment
     */
    @Composable
    public fun IconText(
        icon: SparkIcon,
        text: String,
        modifier: Modifier = Modifier,
    )

    /**
     * Adds a number segment.
     *
     * @param number The number to display
     * @param modifier Optional modifier to apply to the segment
     */
    @Composable
    public fun Number(
        number: Int,
        modifier: Modifier = Modifier,
    )

    /**
     * Adds a custom segment with a content slot and customizable selected background color.
     *
     * @param selectedBackgroundColor The background color to use when this segment is selected
     * @param modifier Optional modifier to apply to the segment
     * @param content The custom content to display in the segment
     */
    @Composable
    public fun Custom(
        selectedBackgroundColor: Color,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    )
}
