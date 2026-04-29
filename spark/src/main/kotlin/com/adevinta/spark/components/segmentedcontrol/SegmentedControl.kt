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

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
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
import com.adevinta.spark.tokens.transparent

/**
 * SegmentedControl allows users to select a single option from a small set of choices,
 * while maintaining an immediate overview of the available alternatives.
 *
 * Two layout variants:
 * - [Horizontal] — single row, 2–5 segments
 * - [Vertical] — two balanced rows, 4–8 segments
 */
public object SegmentedControl {

    /**
     * Single-row segmented control. Supports 2–5 segments.
     *
     * @param selectedIndex Index of the currently selected segment
     * @param onSegmentSelect Called with the tapped segment index
     * @param modifier Modifier applied to the outer container
     * @param title Optional label above the control
     * @param linkText Optional link shown to the right of the title
     * @param onLinkClick Called when the link is tapped
     * @param enabled Whether the control accepts input
     * @param content Segment declarations via [SegmentedControlScope]
     */
    @Composable
    public fun Horizontal(
        selectedIndex: Int,
        onSegmentSelect: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        content: @Composable SegmentedControlScope.() -> Unit,
    ) {
        SegmentedControlImpl(
            selectedIndex = selectedIndex,
            onSegmentSelect = onSegmentSelect,
            modifier = modifier,
            title = title,
            linkText = linkText,
            onLinkClick = onLinkClick,
            enabled = enabled,
            isHorizontal = true,
            minSegments = SegmentedControlDefaults.MinSegments,
            maxSegments = SegmentedControlDefaults.MaxHorizontalSegments,
            shape = SegmentedControlShape.Pill.shape,
            content = content,
        )
    }

    /**
     * Two-row segmented control. Supports 4–8 segments.
     * Row 0 gets ceil(n/2) segments, row 1 gets floor(n/2).
     *
     * @param selectedIndex Index of the currently selected segment
     * @param onSegmentSelect Called with the tapped segment index
     * @param modifier Modifier applied to the outer container
     * @param title Optional label above the control
     * @param linkText Optional link shown to the right of the title
     * @param onLinkClick Called when the link is tapped
     * @param enabled Whether the control accepts input
     * @param shape Shape of the selection indicator pill
     * @param content Segment declarations via [SegmentedControlScope]
     */
    @Composable
    public fun Vertical(
        selectedIndex: Int,
        onSegmentSelect: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        shape: SegmentedControlShape = SegmentedControlShape.Rounded,
        content: @Composable SegmentedControlScope.() -> Unit,
    ) {
        SegmentedControlImpl(
            selectedIndex = selectedIndex,
            onSegmentSelect = onSegmentSelect,
            modifier = modifier,
            title = title,
            linkText = linkText,
            onLinkClick = onLinkClick,
            enabled = enabled,
            isHorizontal = false,
            minSegments = SegmentedControlDefaults.MinVerticalSegments,
            maxSegments = SegmentedControlDefaults.MaxVerticalSegments,
            shape = shape.shape,
            content = content,
        )
    }
}

private enum class SegmentedControlLayoutId { Segment, Divider, Background }

@Immutable
private data class SegmentData(val type: SegmentType, val customBackgroundColor: Color? = null)

internal enum class SegmentType { SingleLine, TwoLine, Icon, IconText, Number, Custom }

private data class SegmentLabelStyle(val color: Color, val textStyle: TextStyle)

@Composable
private fun segmentLabelStyle(selected: Boolean): SegmentLabelStyle {
    val color by animateColorAsState(
        if (selected) SparkTheme.colors.onSurface else SparkTheme.colors.onSurface.dim1,
    )
    val progress by animateFloatAsState(if (selected) 1f else 0f)
    val textStyle = lerp(SparkTheme.typography.body2, SparkTheme.typography.body2.highlight, progress)
    return SegmentLabelStyle(color, textStyle)
}

private class SegmentedControlScopeImpl(
    private val segments: MutableList<SegmentData>,
    private val segmentContents: MutableList<@Composable () -> Unit>,
) : SegmentedControlScope {

    @Composable
    override fun SingleLine(text: String, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.SingleLine))
        segmentContents.add {
            val (labelColor, textStyle) = segmentLabelStyle(LocalSegmentSelected.current)
            Text(
                text = text,
                modifier = modifier,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = textStyle,
                color = labelColor,
            )
        }
    }

    @Composable
    override fun TwoLine(title: String, subtitle: String, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.TwoLine))
        segmentContents.add {
            val (labelColor, textStyle) = segmentLabelStyle(LocalSegmentSelected.current)
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis, color = labelColor, style = textStyle)
                Text(text = subtitle, maxLines = 1, overflow = TextOverflow.Ellipsis, style = SparkTheme.typography.caption)
            }
        }
    }

    @Composable
    override fun Icon(icon: SparkIcon, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.Icon))
        segmentContents.add {
            val iconColor by animateColorAsState(
                if (LocalSegmentSelected.current) SparkTheme.colors.supportVariant else SparkTheme.colors.support,
            )
            Icon(sparkIcon = icon, contentDescription = null, modifier = modifier, size = IconSize.Medium, tint = iconColor)
        }
    }

    @Composable
    override fun IconText(icon: SparkIcon, text: String, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.IconText))
        segmentContents.add {
            val selected = LocalSegmentSelected.current
            val iconColor by animateColorAsState(
                if (selected) SparkTheme.colors.supportVariant else SparkTheme.colors.support,
            )
            val (labelColor, textStyle) = segmentLabelStyle(selected)
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(sparkIcon = icon, contentDescription = null, size = IconSize.Medium, tint = iconColor)
                Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis, color = labelColor, style = textStyle)
            }
        }
    }

    @Composable
    override fun Number(number: Int, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.Number))
        segmentContents.add {
            val (labelColor, textStyle) = segmentLabelStyle(LocalSegmentSelected.current)
            Text(text = number.toString(), modifier = modifier, style = textStyle, color = labelColor)
        }
    }

    @Composable
    override fun Custom(selectedBackgroundColor: Color, modifier: Modifier, content: @Composable () -> Unit) {
        segments.add(SegmentData(SegmentType.Custom, selectedBackgroundColor))
        segmentContents.add {
            Box(modifier = modifier, contentAlignment = Alignment.Center) { content() }
        }
    }
}

@Composable
private fun SegmentedControlImpl(
    selectedIndex: Int,
    onSegmentSelect: (Int) -> Unit,
    title: String?,
    linkText: String?,
    onLinkClick: (() -> Unit)?,
    enabled: Boolean,
    isHorizontal: Boolean,
    minSegments: Int,
    maxSegments: Int,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable SegmentedControlScope.() -> Unit,
) {
    val segments = remember { mutableListOf<SegmentData>() }
    val segmentContents = remember { mutableListOf<@Composable () -> Unit>() }

    segments.clear()
    segmentContents.clear()
    SegmentedControlScopeImpl(segments, segmentContents).content()

    require(segments.size >= minSegments) {
        "SegmentedControl.${if (isHorizontal) "Horizontal" else "Vertical"} requires at least $minSegments segments, got ${segments.size}"
    }
    require(segments.size <= maxSegments) {
        "SegmentedControl.${if (isHorizontal) "Horizontal" else "Vertical"} supports at most $maxSegments segments, got ${segments.size}"
    }
    require(selectedIndex in segments.indices) {
        "selectedIndex $selectedIndex is out of bounds for ${segments.size} segments"
    }

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

        SegmentedControlContent(
            segments = segments,
            segmentContents = segmentContents,
            selectedIndex = selectedIndex,
            onSegmentSelect = onSegmentSelect,
            enabled = enabled,
            shape = shape,
            isHorizontal = isHorizontal,
            groupLabel = title,
        )
    }
}

@Composable
private fun SegmentedControlContent(
    segments: List<SegmentData>,
    selectedIndex: Int,
    onSegmentSelect: (Int) -> Unit,
    enabled: Boolean,
    isHorizontal: Boolean,
    shape: Shape,
    segmentContents: List<@Composable () -> Unit>,
    groupLabel: String?,
) {
    val segmentCount = segments.size
    val measurePolicy = remember(segmentCount, isHorizontal, selectedIndex) {
        SegmentedControlMeasurePolicy(segmentCount, isHorizontal, selectedIndex)
    }

    val containerShape = if (isHorizontal) SparkTheme.shapes.full else SparkTheme.shapes.large

    LookaheadScope {
        Layout(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minWidth = SegmentedControlDefaults.MinTouchTargetSize, minHeight = 52.dp)
                .clip(containerShape)
                .semantics {
                    collectionInfo = CollectionInfo(rowCount = 1, columnCount = segmentCount)
                    isTraversalGroup = true
                    if (groupLabel != null) contentDescription = groupLabel
                }
                .selectableGroup()
                .border(
                    width = SegmentedControlDefaults.BorderWidth,
                    color = SparkTheme.colors.outline,
                    shape = containerShape,
                ),
            content = {
                val row0Count = if (isHorizontal) segmentCount else (segmentCount + 1) / 2
                segments.forEachIndexed { index, segment ->
                    val selected = index == selectedIndex
                    val rippleColor = if (segment.type == SegmentType.Custom) {
                        segment.customBackgroundColor ?: SparkTheme.colors.outlineHigh
                    } else {
                        SparkTheme.colors.outlineHigh
                    }
                    val itemRow = if (isHorizontal || index < row0Count) 0 else 1
                    val itemCol = if (isHorizontal || index < row0Count) index else index - row0Count
                    Box(
                        modifier = Modifier
                            .layoutId(SegmentedControlLayoutId.Segment)
                            .padding(4.dp)
                            .clip(shape)
                            .semantics {
                                collectionItemInfo = CollectionItemInfo(
                                    rowIndex = itemRow,
                                    rowSpan = 1,
                                    columnIndex = itemCol,
                                    columnSpan = 1,
                                )
                            }
                            .selectable(
                                selected = selected,
                                role = Role.Tab,
                                enabled = enabled,
                                indication = ripple(color = rippleColor),
                                interactionSource = null,
                            ) {
                                onSegmentSelect(index)
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        CompositionLocalProvider(LocalSegmentSelected provides selected) {
                            segmentContents[index]()
                        }
                    }
                }

                val indicatorBackground by animateColorAsState(
                    if (segments[selectedIndex].type == SegmentType.Custom) {
                        segments[selectedIndex].customBackgroundColor ?: SparkTheme.colors.neutralContainer
                    } else {
                        SparkTheme.colors.neutralContainer
                    },
                )
                val indicatorBorderThickness by animateDpAsState(
                    if (segments[selectedIndex].type == SegmentType.Custom) 0.dp else 2.dp,
                )
                val indicatorBorderColor by animateColorAsState(
                    if (segments[selectedIndex].type == SegmentType.Custom) {
                        SparkTheme.colors.outlineHigh.transparent
                    } else {
                        SparkTheme.colors.outlineHigh
                    },
                )

                Box(
                    modifier = Modifier
                        .layoutId(SegmentedControlLayoutId.Background)
                        .animateBounds(
                            lookaheadScope = this@LookaheadScope,
                            boundsTransform = { _, _ ->
                                spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioLowBouncy)
                            },
                        )
                        .padding(4.dp)
                        .clip(shape)
                        .border(indicatorBorderThickness, indicatorBorderColor, shape)
                        .background(indicatorBackground, shape),
                )

                repeat(segmentCount - 1) {
                    Box(
                        modifier = Modifier
                            .layoutId(SegmentedControlLayoutId.Divider)
                            .width(SegmentedControlDefaults.DividerWidth)
                            .requiredHeight(24.dp)
                            .background(SparkTheme.colors.outline),
                    )
                }
            },
            measurePolicy = measurePolicy,
        )
    }
}

private class SegmentedControlMeasurePolicy(
    private val segmentCount: Int,
    private val isHorizontal: Boolean,
    private val selectedIndex: Int,
) : MeasurePolicy {

    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val dividerWidth = SegmentedControlDefaults.DividerWidth.roundToPx()
        val segMeasurables = measurables.filter { it.layoutId == SegmentedControlLayoutId.Segment }
        val bgMeasurable = measurables.firstOrNull { it.layoutId == SegmentedControlLayoutId.Background }
        val divMeasurables = measurables.filter { it.layoutId == SegmentedControlLayoutId.Divider }

        if (isHorizontal) {
            val segWidth = (constraints.maxWidth - dividerWidth * (segmentCount - 1)) / segmentCount
            val rowHeight = maxOf(
                segMeasurables.maxOfOrNull { it.maxIntrinsicHeight(segWidth) } ?: 0,
                constraints.minHeight,
            )
            val segConstraints = Constraints.fixed(segWidth, rowHeight)
            val segPlaceables = segMeasurables.map { it.measure(segConstraints) }
            val bgPlaceable = bgMeasurable?.measure(segConstraints)
            val divPlaceables = divMeasurables.map { it.measure(Constraints.fixed(dividerWidth, rowHeight)) }

            return layout(constraints.maxWidth, rowHeight) {
                bgPlaceable?.placeRelative(x = selectedIndex * (segWidth + dividerWidth), y = 0)
                var x = 0
                segPlaceables.forEachIndexed { i, p ->
                    p.placeRelative(x, 0)
                    x += segWidth
                    if (i < divPlaceables.size) {
                        divPlaceables[i].placeRelative(x, 0)
                        x += dividerWidth
                    }
                }
            }
        } else {
            val row0Count = (segmentCount + 1) / 2
            val row1Count = segmentCount / 2
            val row0SegWidth = (constraints.maxWidth - dividerWidth * (row0Count - 1)) / row0Count
            val row1SegWidth = (constraints.maxWidth - dividerWidth * (row1Count - 1)) / row1Count
            val row0Height = segMeasurables.take(row0Count).maxOfOrNull { it.maxIntrinsicHeight(row0SegWidth) } ?: 0
            val row1Height = segMeasurables.drop(row0Count).maxOfOrNull { it.maxIntrinsicHeight(row1SegWidth) } ?: 0
            val rowHeight = maxOf(row0Height, row1Height, constraints.minHeight)

            val selectedRow = if (selectedIndex < row0Count) 0 else 1
            val selectedCol = if (selectedRow == 0) selectedIndex else selectedIndex - row0Count
            val selectedSegWidth = if (selectedRow == 0) row0SegWidth else row1SegWidth

            val segPlaceables = segMeasurables.mapIndexed { i, m ->
                m.measure(Constraints.fixed(if (i < row0Count) row0SegWidth else row1SegWidth, rowHeight))
            }
            val bgPlaceable = bgMeasurable?.measure(Constraints.fixed(selectedSegWidth, rowHeight))
            val divPlaceables = divMeasurables.map { it.measure(Constraints.fixed(dividerWidth, rowHeight)) }

            return layout(constraints.maxWidth, rowHeight * 2) {
                bgPlaceable?.placeRelative(
                    x = selectedCol * (selectedSegWidth + dividerWidth),
                    y = selectedRow * rowHeight,
                )
                var segIdx = 0
                var divIdx = 0
                for (row in 0 until 2) {
                    val countInRow = if (row == 0) row0Count else row1Count
                    var x = 0
                    for (col in 0 until countInRow) {
                        if (segIdx < segPlaceables.size) {
                            segPlaceables[segIdx].placeRelative(x, row * rowHeight)
                            x += if (row == 0) row0SegWidth else row1SegWidth
                            if (col < countInRow - 1) {
                                divPlaceables[divIdx].placeRelative(x, row * rowHeight)
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

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
        val dividerWidthPx = SegmentedControlDefaults.DividerWidth.roundToPx()
        val segMeasurables = measurables.take(segmentCount)
        return if (isHorizontal) {
            val segWidth = (width - dividerWidthPx * (segmentCount - 1)) / segmentCount
            segMeasurables.maxOfOrNull { it.maxIntrinsicHeight(segWidth) } ?: 0
        } else {
            val row0Count = (segmentCount + 1) / 2
            val row1Count = segmentCount / 2
            val row0SegWidth = (width - dividerWidthPx * (row0Count - 1)) / row0Count
            val row1SegWidth = (width - dividerWidthPx * (row1Count - 1)) / row1Count
            val row0Height = segMeasurables.take(row0Count).maxOfOrNull { it.maxIntrinsicHeight(row0SegWidth) } ?: 0
            val row1Height = segMeasurables.drop(row0Count).maxOfOrNull { it.maxIntrinsicHeight(row1SegWidth) } ?: 0
            maxOf(row0Height, row1Height) * 2
        }
    }
}
