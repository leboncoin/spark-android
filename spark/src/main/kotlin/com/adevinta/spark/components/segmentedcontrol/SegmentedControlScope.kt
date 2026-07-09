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
package com.adevinta.spark.components.segmentedcontrol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.icons.SparkIcon

/**
 * DSL scope for declaring segments inside a [SegmentedControl] content block.
 *
 * Each function adds one segment in declaration order. The return type [SegmentedButtonItem] is a
 * marker; callers discard it. All variants animate label weight and colour between the selected and
 * unselected states; icons additionally animate between [com.adevinta.spark.tokens.SparkColors.support] (unselected)
 * and [com.adevinta.spark.tokens.SparkColors.supportVariant] (selected).
 *
 * Enabled state and indicator shape are inherited from the enclosing [SegmentedControl] and cannot
 * be overridden per segment.
 */
@Stable
public interface SegmentedControlScope {

    /**
     * Segment showing a single line of text, truncated with ellipsis when the segment is too
     * narrow.
     *
     * ![Single line segment](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.segmentedcontrol_SegmentedControlDocumentationScreenshots_singleLine.png)
     *
     * @param text Label displayed in the segment.
     * @param selected Whether this segment is currently selected.
     * @param onClick Called when the user taps this segment.
     * @param modifier Modifier applied to this segment's touch-target box.
     */
    @Composable
    public fun singleLine(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ): SegmentedButtonItem

    /**
     * Segment showing a bold primary line and a smaller caption below it. Both lines are
     * single-line, truncated with ellipsis.
     *
     * ![Two line segment](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.segmentedcontrol_SegmentedControlDocumentationScreenshots_twoLine.png)
     *
     * @param title Primary label, styled as [com.adevinta.spark.tokens.SparkTypography.body2].
     * @param subtitle Secondary label, styled as [com.adevinta.spark.tokens.SparkTypography.caption].
     * @param selected Whether this segment is currently selected.
     * @param onClick Called when the user taps this segment.
     * @param modifier Modifier applied to this segment's touch-target box.
     */
    @Composable
    public fun twoLine(
        title: String,
        subtitle: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ): SegmentedButtonItem

    /**
     * Segment showing a single medium-sized [SparkIcon].
     *
     * ![Icon segment](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.segmentedcontrol_SegmentedControlDocumentationScreenshots_icon.png)
     *
     * The icon colour animates between [com.adevinta.spark.tokens.SparkColors.support] when unselected and
     * [com.adevinta.spark.tokens.SparkColors.supportVariant] when selected.
     *
     * Because the icon carries no visible text, [contentDescription] is required so that screen
     * readers can identify the segment. It is also shown as a plain tooltip on long-press.
     *
     * @param icon Icon to render.
     * @param contentDescription Accessible label for the segment, announced by TalkBack and shown
     *   as a tooltip. Must describe the action or selection represented by the icon
     *   (e.g. "List view", "Map view").
     * @param selected Whether this segment is currently selected.
     * @param onClick Called when the user taps this segment.
     * @param modifier Modifier applied to this segment's touch-target box.
     */
    @Composable
    public fun icon(
        icon: SparkIcon,
        contentDescription: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ): SegmentedButtonItem

    /**
     * Segment showing a medium-sized [SparkIcon] above a single line of text.
     *
     * ![Icon + text segment](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.segmentedcontrol_SegmentedControlDocumentationScreenshots_iconText.png)
     *
     * The icon colour animates between [com.adevinta.spark.tokens.SparkColors.support] when unselected and
     * [com.adevinta.spark.tokens.SparkColors.supportVariant] when selected. The text uses the same animated style as
     * [singleLine].
     *
     * @param icon Icon rendered above or beside the label.
     * @param text Label displayed next to or below the icon, truncated with ellipsis.
     * @param selected Whether this segment is currently selected.
     * @param iconOnTop When `true` the icon is placed above the text. When `false` it is placed to
     *   the start (inline with the text).
     * @param onClick Called when the user taps this segment.
     * @param modifier Modifier applied to this segment's touch-target box.
     */
    @Composable
    public fun iconText(
        icon: SparkIcon,
        text: String,
        selected: Boolean,
        iconOnTop: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ): SegmentedButtonItem

    /**
     * Segment showing an integer label. Equivalent to [singleLine] with [number] converted to
     * a string. Useful for compact numeric scales (e.g. ratings 1–5).
     *
     * ![Number segment](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.segmentedcontrol_SegmentedControlDocumentationScreenshots_number.png)
     *
     * @param number Value to display.
     * @param selected Whether this segment is currently selected.
     * @param onClick Called when the user taps this segment.
     * @param modifier Modifier applied to this segment's touch-target box.
     */
    @Composable
    public fun number(
        number: Int,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ): SegmentedButtonItem

    /**
     * Segment with fully custom content. Use this when no other scope variant fits, for example
     * when segment appearance must vary with the selected value (e.g. a color-coded energy-rating
     * scale).
     *
     * The [content] lambda is centred inside the segment's touch target. Enabled state and indicator
     * shape are still controlled by the enclosing [SegmentedControl].
     *
     * To reuse the same animations as built-in segments, call
     * [SegmentedControlDefaults.segmentLabelStyle] for text color and weight, or
     * [SegmentedControlDefaults.segmentIconColor] for icon tint.
     *
     * **Accessibility:** [content] must produce at least one composable that contributes text
     * semantics (e.g. [com.adevinta.spark.components.text.Text], or a composable with a non-null
     * `contentDescription`). If the content is purely visual, add a label via
     * `modifier = Modifier.semantics { contentDescription = "…" }`.
     *
     * @param selected Whether this segment is currently selected.
     * @param onClick Called when the user taps this segment.
     * @param modifier Modifier applied to this segment's touch-target box.
     * @param rippleColor Colour of the tap ripple. Defaults to [com.adevinta.spark.tokens.SparkColors.outlineHigh];
     *   override to match a segment's custom background.
     * @param content Composable content rendered inside the segment.
     */
    @Composable
    public fun custom(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        rippleColor: Color = SparkTheme.colors.outlineHigh,
        content: @Composable () -> Unit,
    ): SegmentedButtonItem
}
