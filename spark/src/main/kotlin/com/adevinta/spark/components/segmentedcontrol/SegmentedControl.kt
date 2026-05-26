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

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl.Horizontal
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl.Vertical
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tokens.ripple

/**
 * Marker interface returned by every [SegmentedControlScope] segment function.
 *
 * The return type enforces that only recognised segment builders can appear inside a
 * [SegmentedControl] content block. Callers should not implement this interface directly.
 */
public interface SegmentedButtonItem

/**
 * Single-selection segmented control for choosing one option from a compact set.
 *
 * Two layout variants cover the full supported range of 2–8 segments:
 * - [Horizontal] — single row, 2–5 segments
 * - [Vertical] — two balanced rows, 4–8 segments with a configurable indicator shape
 *
 * State is fully caller-controlled: store `selectedIndex` yourself and update it in each
 * segment's `onClick` lambda.
 *
 * ```kotlin
 * var selected by remember { mutableIntStateOf(0) }
 * SegmentedControl.Horizontal(selectedIndex = selected) {
 *     SingleLine("Day",  selected = selected == 0, onClick = { selected = 0 })
 *     SingleLine("Week", selected = selected == 1, onClick = { selected = 1 })
 * }
 * ```
 *
 * The component throws [IllegalArgumentException] at composition time if the segment count falls
 * outside the allowed range for the chosen variant, or if [selectedIndex] is out of bounds.
 */
public object SegmentedControl {

    /**
     * Single-row segmented control for 2–5 segments.
     *
     * All segments share equal width. The animated pill indicator always uses
     * [SegmentedControlShape.Pill] (fully rounded); the shape is not configurable on this variant
     * because the outer container is also pill-shaped.
     *
     * @param selectedIndex Zero-based index of the currently selected segment. Must be in
     *   `0 until segmentCount`; throws [IllegalArgumentException] otherwise.
     * @param modifier Modifier applied to the outermost [Column] that wraps the optional header
     *   and the control track.
     * @param title Optional bold label rendered above the control. When both [title] and
     *   [linkText] are provided they appear on the same row.
     * @param linkText Optional underlined link rendered to the right of [title]. Ignored when
     *   [onLinkClick] is `null`.
     * @param onLinkClick Callback invoked when [linkText] is tapped. When `null` the link text
     *   is not rendered even if [linkText] is non-null.
     * @param enabled When `false` all segments ignore input and their ripple is suppressed.
     * @param indicatorContent Composable that draws the selection indicator. Receives the current
     *   [selectedIndex] so callers can vary the appearance per selection — useful for value scales
     *   such as energy ratings. Defaults to [SegmentedControlDefaults.Indicator].
     * @param content Segment declarations using [SegmentedControlScope]. Every call to a scope
     *   function adds one segment; call them in display order. Requires 2–5 calls.
     *
     */
    @Composable
    public fun Horizontal(
        selectedIndex: Int,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        indicatorContent: @Composable (selectedIndex: Int) -> Unit = DefaultHorizontalIndicator,
        content: @Composable SegmentedControlScope.(SegmentedButtonItem) -> Unit,
    ) {
        SegmentedControlImpl(
            selectedIndex = selectedIndex,
            modifier = modifier,
            title = title,
            linkText = linkText,
            onLinkClick = onLinkClick,
            enabled = enabled,
            isHorizontal = true,
            minSegments = SegmentedControlDefaults.MinSegments,
            maxSegments = SegmentedControlDefaults.MaxHorizontalSegments,
            shape = SegmentedControlShape.Pill.shape,
            indicatorContent = indicatorContent,
            content = content,
        )
    }

    /**
     * Two-row segmented control for 4–8 segments.
     *
     * Segments are distributed across two rows, segments are indexed sequentially across both rows, so index 0 is
     * the first segment in row 0 and index `ceil(n/2)` is the first segment in row 1.
     *
     * @param selectedIndex Zero-based index of the currently selected segment across both rows.
     *   Must be in `0 until segmentCount`; throws [IllegalArgumentException] otherwise.
     * @param modifier Modifier applied to the outermost [Column] that wraps the optional header
     *   and the control track.
     * @param title Optional bold label rendered above the control. When both [title] and
     *   [linkText] are provided they appear on the same row, left- and right-aligned.
     * @param linkText Optional underlined link rendered to the right of [title]. Ignored when
     *   [onLinkClick] is `null`.
     * @param onLinkClick Callback invoked when [linkText] is tapped. When `null` the link text
     *   is not rendered even if [linkText] is non-null.
     * @param enabled When `false` all segments ignore input and their ripple is suppressed.
     * @param shape Shape applied to each segment's touch target and to the animated indicator.
     *   Defaults to [SegmentedControlShape.Rounded]. Use [SegmentedControlShape.Pill] for a fully
     *   rounded look.
     * @param indicatorContent Composable that draws the selection indicator. Receives the current
     *   [selectedIndex] so callers can vary the appearance per selection — useful for value scales
     *   such as energy ratings. Defaults to [SegmentedControlDefaults.Indicator] using [shape].
     * @param content Segment declarations using [SegmentedControlScope]. Every call to a scope
     *   function adds one segment; call them in display order. Requires 4–8 calls.
     *
     */
    @Composable
    public fun Vertical(
        selectedIndex: Int,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        shape: SegmentedControlShape = SegmentedControlShape.Rounded,
        indicatorContent: @Composable (selectedIndex: Int) -> Unit = {
            SegmentedControlDefaults.Indicator(shape = shape.shape)
        },
        content: @Composable SegmentedControlScope.(SegmentedButtonItem) -> Unit,
    ) {
        SegmentedControlImpl(
            selectedIndex = selectedIndex,
            modifier = modifier,
            title = title,
            linkText = linkText,
            onLinkClick = onLinkClick,
            enabled = enabled,
            isHorizontal = false,
            minSegments = SegmentedControlDefaults.MinVerticalSegments,
            maxSegments = SegmentedControlDefaults.MaxVerticalSegments,
            shape = shape.shape,
            indicatorContent = indicatorContent,
            content = content,
        )
    }
}

@Stable
private data class SegmentLabelStyle(val color: Color, val textStyle: TextStyle)

@Composable
private fun segmentLabelStyle(selected: Boolean): SegmentLabelStyle {
    val transition = updateTransition(selected, label = "segmentLabel")
    val color by transition.animateColor(label = "labelColor") {
        if (it) SparkTheme.colors.onSurface else SparkTheme.colors.onSurface.dim1
    }
    val progress by transition.animateFloat(label = "labelProgress") { if (it) 1f else 0f }
    val textStyle = lerp(SparkTheme.typography.body2, SparkTheme.typography.body2.highlight, progress)
    return SegmentLabelStyle(color, textStyle)
}

private object SegmentedButtonItemImpl : SegmentedButtonItem

private val DefaultHorizontalIndicator: @Composable (Int) -> Unit = {
    SegmentedControlDefaults.Indicator(shape = SegmentedControlShape.Pill.shape)
}

private fun row0Count(segmentCount: Int): Int = (segmentCount + 1) / 2

private enum class SegmentSlot { Segments, Indicator, Dividers }

// We suppress the content emmiter with returning values as we use it to enforce which composable we accept inside the
// segemented control slot
@SuppressLint("ComposeContentEmitterReturningValues", "ComposeNamingLowercase")
private object SegmentedControlScopeImpl : SegmentedControlScope {

    @Composable
    override fun SingleLine(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
    ): SegmentedButtonItem {
        SegmentWrapper(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
        ) {
            val (labelColor, textStyle) = segmentLabelStyle(selected)
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = textStyle,
                color = labelColor,
            )
        }
        return SegmentedButtonItemImpl
    }

    @Composable
    override fun TwoLine(
        title: String,
        subtitle: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
    ): SegmentedButtonItem {
        SegmentWrapper(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
        ) {
            val (labelColor, textStyle) = segmentLabelStyle(selected)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = labelColor,
                    style = textStyle,
                )
                Text(
                    text = subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = SparkTheme.typography.caption,
                )
            }
        }
        return SegmentedButtonItemImpl
    }

    @Composable
    override fun Icon(
        icon: SparkIcon,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
    ): SegmentedButtonItem {
        SegmentWrapper(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
        ) {
            val iconColor by animateColorAsState(
                if (selected) SparkTheme.colors.supportVariant else SparkTheme.colors.support,
                label = "iconColor",
            )
            Icon(
                sparkIcon = icon,
                contentDescription = null,
                size = IconSize.Medium,
                tint = iconColor,
            )
        }
        return SegmentedButtonItemImpl
    }

    @Composable
    override fun IconText(
        icon: SparkIcon,
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
    ): SegmentedButtonItem {
        SegmentWrapper(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
        ) {
            val iconColor by animateColorAsState(
                if (selected) SparkTheme.colors.supportVariant else SparkTheme.colors.support,
                label = "iconColor",
            )
            val (labelColor, textStyle) = segmentLabelStyle(selected)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(sparkIcon = icon, contentDescription = null, size = IconSize.Medium, tint = iconColor)
                Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis, color = labelColor, style = textStyle)
            }
        }
        return SegmentedButtonItemImpl
    }

    @Composable
    override fun Number(
        number: Int,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
    ): SegmentedButtonItem {
        SegmentWrapper(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
        ) {
            val (labelColor, textStyle) = segmentLabelStyle(selected)
            Text(text = number.toString(), style = textStyle, color = labelColor)
        }
        return SegmentedButtonItemImpl
    }

    @Composable
    override fun Custom(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        rippleColor: Color,
        content: @Composable () -> Unit,
    ): SegmentedButtonItem {
        SegmentWrapper(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
            rippleColor = rippleColor,
        ) {
            Box(contentAlignment = Alignment.Center) { content() }
        }
        return SegmentedButtonItemImpl
    }

    @Composable
    private fun SegmentWrapper(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        rippleColor: Color = SparkTheme.colors.outlineHigh,
        content: @Composable () -> Unit,
    ) {
        val itemInfo = LocalSegmentItemInfo.current
        Box(
            modifier = modifier
                .padding(4.dp)
                .clip(itemInfo.shape)
                .selectable(
                    selected = selected,
                    role = Role.Tab,
                    enabled = itemInfo.enabled,
                    indication = ripple(color = rippleColor),
                    interactionSource = null,
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Stable
private data class SegmentItemInfo(val shape: Shape, val enabled: Boolean)

private val LocalSegmentItemInfo = compositionLocalOf {
    SegmentItemInfo(shape = RectangleShape, enabled = true)
}

@Composable
private fun SegmentedControlImpl(
    selectedIndex: Int,
    title: String?,
    linkText: String?,
    onLinkClick: (() -> Unit)?,
    enabled: Boolean,
    isHorizontal: Boolean,
    minSegments: Int,
    maxSegments: Int,
    shape: Shape,
    indicatorContent: @Composable (selectedIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable SegmentedControlScope.(SegmentedButtonItem) -> Unit,
) {
    val containerShape = if (isHorizontal) SparkTheme.shapes.full else SparkTheme.shapes.large

    val segmentCountState = remember { mutableIntStateOf(0) }

    val animSpec =
        remember { spring<Dp>(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioLowBouncy) }

    val indicatorSlot = remember { movableContentOf<Int> { indicatorContent(it) } }

    Column(modifier = modifier) {
        if (title != null || linkText != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = SparkTheme.typography.body2.highlight,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                if (linkText != null && onLinkClick != null) {
                    Text(
                        text = linkText,
                        style = SparkTheme.typography.body2.copy(textDecoration = TextDecoration.Underline),
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clickable(onClickLabel = linkText, onClick = onLinkClick),
                    )
                }
            }
        }

        SubcomposeLayout(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minWidth = SegmentedControlDefaults.MinTouchTargetSize, minHeight = 52.dp)
                .clip(containerShape)
                .semantics {
                    val count = segmentCountState.intValue
                    collectionInfo = CollectionInfo(
                        rowCount = if (isHorizontal) 1 else 2,
                        columnCount = if (isHorizontal) count else row0Count(count),
                    )
                    isTraversalGroup = true
                    selectableGroup()
                    if (title != null) contentDescription = title
                }
                .border(
                    width = SegmentedControlDefaults.BorderWidth,
                    color = SparkTheme.colors.outline,
                    shape = containerShape,
                ),
        ) { constraints ->
            val dividerWidth = SegmentedControlDefaults.DividerWidth.roundToPx()

            val segMeasurables = subcompose(SegmentSlot.Segments) {
                CompositionLocalProvider(
                    LocalSegmentItemInfo provides remember(shape, enabled) {
                        SegmentItemInfo(
                            shape = shape,
                            enabled = enabled,
                        )
                    },
                ) {
                    SegmentedControlScopeImpl.content(SegmentedButtonItemImpl)
                }
            }
            val segmentCount = segMeasurables.size
            segmentCountState.intValue = segmentCount

            require(segmentCount >= minSegments) {
                "SegmentedControl.${if (isHorizontal) "Horizontal" else "Vertical"} requires at least $minSegments segments, got $segmentCount"
            }
            require(segmentCount <= maxSegments) {
                "SegmentedControl.${if (isHorizontal) "Horizontal" else "Vertical"} supports at most $maxSegments segments, got $segmentCount"
            }
            require(selectedIndex in 0 until segmentCount) {
                "selectedIndex $selectedIndex is out of bounds for $segmentCount segments"
            }

            if (isHorizontal) {
                val segWidth = (constraints.maxWidth - dividerWidth * (segmentCount - 1)) / segmentCount
                val rowHeight = maxOf(
                    segMeasurables.maxOfOrNull { it.maxIntrinsicHeight(segWidth) } ?: 0,
                    constraints.minHeight,
                )
                val segPlaceables = segMeasurables.map { it.measure(Constraints.fixed(segWidth, rowHeight)) }

                val indicatorXDp = (selectedIndex * (segWidth + dividerWidth)).toDp()
                val segWidthDp = segWidth.toDp()
                val rowHeightDp = rowHeight.toDp()
                val indicatorPlaceable = subcompose(SegmentSlot.Indicator) {
                    val animX = animateDpAsState(indicatorXDp, animSpec, label = "indicatorX")
                    val animWidth = animateDpAsState(segWidthDp, animSpec, label = "indicatorWidth")
                    val animHeight = animateDpAsState(rowHeightDp, animSpec, label = "indicatorHeight")
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(animX.value.roundToPx(), 0) }
                            .layout { measurable, _ ->
                                val w = animWidth.value.roundToPx()
                                val h = animHeight.value.roundToPx()
                                val placeable = measurable.measure(Constraints.fixed(w, h))
                                layout(w, h) { placeable.placeRelative(0, 0) }
                            },
                    ) {
                        indicatorSlot(selectedIndex)
                    }
                }.single().measure(Constraints())

                val dividerPlaceables = subcompose(SegmentSlot.Dividers) {
                    repeat(segmentCount - 1) {
                        Box(
                            modifier = Modifier
                                .width(SegmentedControlDefaults.DividerWidth)
                                .requiredHeight(24.dp)
                                .background(SparkTheme.colors.outline),
                        )
                    }
                }.map { it.measure(Constraints.fixed(dividerWidth, rowHeight)) }

                layout(constraints.maxWidth, rowHeight) {
                    indicatorPlaceable.placeRelative(0, 0)
                    var x = 0
                    segPlaceables.forEachIndexed { i, p ->
                        p.placeRelative(x, 0)
                        x += segWidth
                        if (i < dividerPlaceables.size) {
                            dividerPlaceables[i].placeRelative(x, 0)
                            x += dividerWidth
                        }
                    }
                }
            } else {
                val r0Count = row0Count(segmentCount)
                val row1Count = segmentCount / 2
                val row0SegWidth = (constraints.maxWidth - dividerWidth * (r0Count - 1)) / r0Count
                val row1SegWidth = (constraints.maxWidth - dividerWidth * (row1Count - 1)) / row1Count
                val row0Height = segMeasurables.take(r0Count).maxOfOrNull { it.maxIntrinsicHeight(row0SegWidth) } ?: 0
                val row1Height = segMeasurables.drop(r0Count).maxOfOrNull { it.maxIntrinsicHeight(row1SegWidth) } ?: 0
                val rowHeight = maxOf(row0Height, row1Height, constraints.minHeight)

                val selectedRow = if (selectedIndex < r0Count) 0 else 1
                val selectedCol = if (selectedRow == 0) selectedIndex else selectedIndex - r0Count
                val selectedSegWidth = if (selectedRow == 0) row0SegWidth else row1SegWidth

                val segPlaceables = segMeasurables.mapIndexed { i, m ->
                    m.measure(Constraints.fixed(if (i < r0Count) row0SegWidth else row1SegWidth, rowHeight))
                }

                val indicatorXDp = (selectedCol * (selectedSegWidth + dividerWidth)).toDp()
                val indicatorYDp = (selectedRow * rowHeight).toDp()
                val indicatorWidthDp = selectedSegWidth.toDp()
                val rowHeightDp = rowHeight.toDp()
                val indicatorPlaceable = subcompose(SegmentSlot.Indicator) {
                    val animX = animateDpAsState(indicatorXDp, animSpec, label = "indicatorX")
                    val animY = animateDpAsState(indicatorYDp, animSpec, label = "indicatorY")
                    val animWidth = animateDpAsState(indicatorWidthDp, animSpec, label = "indicatorWidth")
                    val animHeight = animateDpAsState(rowHeightDp, animSpec, label = "indicatorHeight")
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(animX.value.roundToPx(), animY.value.roundToPx()) }
                            .layout { measurable, _ ->
                                val w = animWidth.value.roundToPx()
                                val h = animHeight.value.roundToPx()
                                val placeable = measurable.measure(Constraints.fixed(w, h))
                                layout(w, h) { placeable.placeRelative(0, 0) }
                            },
                    ) {
                        indicatorSlot(selectedIndex)
                    }
                }.single().measure(Constraints())

                val dividerPlaceables = subcompose(SegmentSlot.Dividers) {
                    repeat(segmentCount - 1) {
                        Box(
                            modifier = Modifier
                                .width(SegmentedControlDefaults.DividerWidth)
                                .requiredHeight(24.dp)
                                .background(SparkTheme.colors.outline),
                        )
                    }
                }.map { it.measure(Constraints.fixed(dividerWidth, rowHeight)) }

                layout(constraints.maxWidth, rowHeight * 2) {
                    indicatorPlaceable.placeRelative(0, 0)
                    var segIdx = 0
                    var divIdx = 0
                    for (row in 0 until 2) {
                        val countInRow = if (row == 0) r0Count else row1Count
                        var x = 0
                        for (col in 0 until countInRow) {
                            if (segIdx < segPlaceables.size) {
                                segPlaceables[segIdx].placeRelative(x, row * rowHeight)
                                x += if (row == 0) row0SegWidth else row1SegWidth
                                if (col < countInRow - 1) {
                                    dividerPlaceables[divIdx].placeRelative(x, row * rowHeight)
                                    x += dividerWidth
                                    divIdx++
                                }
                                segIdx++
                            }
                        }
                    }
                }
            }
        }
    }
}
