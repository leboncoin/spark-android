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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
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
import com.adevinta.spark.tokens.highlight

/**
 * SegmentedControl allows users to select a single option from a small set of choices,
 * while maintaining an immediate overview of the available alternatives.
 *
 * The component supports two layout variants:
 * - [Horizontal] - Single row layout, supports up to 4 segments
 * - [Vertical] - Multi-row layout, supports up to 8 segments
 *
 * Each segment can contain different content types:
 * - Single line of text
 * - Two lines (title and subtitle)
 * - Icon only
 * - Icon with text
 * - Number
 * - Custom content with customizable selected background color
 */
public object SegmentedControl {
    /**
     * Horizontal segmented control that displays segments in a single row.
     * Supports a maximum of 4 segments.
     *
     * @param selectedIndex The index of the currently selected segment
     * @param onSegmentSelect Callback invoked when a segment is selected
     * @param modifier Optional modifier to apply to the control
     * @param title Optional title displayed above the control
     * @param linkText Optional link text displayed next to the title
     * @param onLinkClick Callback invoked when the link is clicked
     * @param customSelectedColors Optional list of colors for value scales (e.g., energy ratings)
     * @param enabled Whether the control is enabled
     * @param content Lambda that provides [SegmentedControlScope] to add segments
     */
    @Composable
    public fun Horizontal(
        selectedIndex: Int,
        onSegmentSelect: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        role: Role = Role.RadioButton,
        customSelectedColors: List<Color>? = null,
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
            customSelectedColors = customSelectedColors,
            enabled = enabled,
            isHorizontal = true,
            maxSegments = SegmentedControlDefaults.MaxHorizontalSegments,
            content = content,
        )
    }

    /**
     * Vertical segmented control that displays segments in a multi-row layout.
     * Supports a maximum of 8 segments.
     *
     * @param selectedIndex The index of the currently selected segment
     * @param onSegmentSelect Callback invoked when a segment is selected
     * @param modifier Optional modifier to apply to the control
     * @param title Optional title displayed above the control
     * @param linkText Optional link text displayed next to the title
     * @param onLinkClick Callback invoked when the link is clicked
     * @param customSelectedColors Optional list of colors for value scales (e.g., energy ratings)
     * @param enabled Whether the control is enabled
     * @param content Lambda that provides [SegmentedControlScope] to add segments
     */
    @Composable
    public fun Vertical(
        selectedIndex: Int,
        onSegmentSelect: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        customSelectedColors: List<Color>? = null,
        role: Role = Role.RadioButton,
        enabled: Boolean = true,
        content: @Composable SegmentedControlScope.() -> Unit,
    ) {
        androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
        SegmentedControlImpl(
            selectedIndex = selectedIndex,
            onSegmentSelect = onSegmentSelect,
            modifier = modifier,
            title = title,
            linkText = linkText,
            onLinkClick = onLinkClick,
            customSelectedColors = customSelectedColors,
            enabled = enabled,
            isHorizontal = false,
            maxSegments = SegmentedControlDefaults.MaxVerticalSegments,
            content = content,
        )
    }
}

/**
 * Internal layout IDs for the segmented control components.
 */
private enum class SegmentedControlLayoutId {
    Segment,
    Background,
    Divider,
}

/**
 * Internal data class representing a segment.
 */
@Immutable
private data class SegmentData(
    val type: SegmentType,
    val customBackgroundColor: Color? = null,
)

/**
 * Internal enum representing segment content types.
 */
private enum class SegmentType {
    SingleLine,
    TwoLine,
    Icon,
    IconText,
    Number,
    Custom,
}

/**
 * Internal scope implementation that collects segments as they're composed.
 */
private class SegmentedControlScopeImpl(
    private val segments: MutableList<SegmentData>,
    private val segmentContents: MutableList<@Composable () -> Unit>,
) : SegmentedControlScope {
    @Composable
    override fun SingleLine(text: String, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.SingleLine))
        segmentContents.add {
            Text(
                text = text,
                modifier = modifier,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = SparkTheme.typography.body2.highlight,
            )
        }

    }

    @Composable
    override fun TwoLine(title: String, subtitle: String, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.TwoLine))
        segmentContents.add {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = SparkTheme.typography.body2.highlight,
                )
                Text(
                    text = subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = SparkTheme.typography.caption,
                )
            }
        }
    }

    @Composable
    override fun Icon(icon: SparkIcon, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.Icon))
        segmentContents.add {
            Icon(
                sparkIcon = icon,
                contentDescription = null,
                modifier = modifier,
                size = IconSize.Medium,
            )
        }
    }

    @Composable
    override fun IconText(icon: SparkIcon, text: String, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.IconText))
        segmentContents.add {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    sparkIcon = icon,
                    contentDescription = null,
                    size = IconSize.Medium,
                )
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = SparkTheme.typography.body2.highlight,
                )
            }
        }
    }

    @Composable
    override fun Number(number: Int, modifier: Modifier) {
        segments.add(SegmentData(SegmentType.Number))
        segmentContents.add {
            Text(
                text = number.toString(),
                modifier = modifier,
                style = SparkTheme.typography.body2.highlight,
            )
        }
    }

    @Composable
    override fun Custom(
        selectedBackgroundColor: Color,
        modifier: Modifier,
        content: @Composable () -> Unit,
    ) {
        segments.add(SegmentData(SegmentType.Custom, selectedBackgroundColor))
        segmentContents.add {
            Box(modifier = modifier) {
                content()
            }
        }
    }
}

/**
 * Internal implementation of the segmented control.
 */
@Composable
private fun SegmentedControlImpl(
    selectedIndex: Int,
    onSegmentSelect: (Int) -> Unit,
    title: String?,
    linkText: String?,
    onLinkClick: (() -> Unit)?,
    customSelectedColors: List<Color>?,
    role: Role,
    enabled: Boolean,
    isHorizontal: Boolean,
    maxSegments: Int,
    modifier: Modifier = Modifier,
    content: @Composable SegmentedControlScope.() -> Unit,
) {
    val segments = remember { mutableListOf<SegmentData>() }
    val segmentContents = remember { mutableListOf<@Composable () -> Unit>() }

    // Collect segments
    segments.clear()
    segmentContents.clear()
    val scope = SegmentedControlScopeImpl(segments, segmentContents)
    scope.content()

    require(segments.size >= SegmentedControlDefaults.MinSegments) {
        "SegmentedControl requires at least ${SegmentedControlDefaults.MinSegments} segments"
    }
    require(segments.size <= maxSegments) {
        "SegmentedControl ${if (isHorizontal) "Horizontal" else "Vertical"} supports at most $maxSegments segments"
    }
    require(selectedIndex in segments.indices) {
        "Selected index $selectedIndex is out of bounds for ${segments.size} segments"
    }

    Column(modifier = modifier) {
        // Title and link row
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
                        style = SparkTheme.typography.body2.copy(
                            textDecoration = TextDecoration.Underline,
                        ),
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .selectable(selected = false) { onLinkClick() },
                    )
                }
            }
        }

        // Segmented control
        SegmentedControlContent(
            segments = segments,
            segmentContents = segmentContents,
            selectedIndex = selectedIndex,
            onSegmentSelect = onSegmentSelect,
            customSelectedColors = customSelectedColors,
            role = role,
            enabled = enabled,
            isHorizontal = isHorizontal,
        )
    }
}

/**
 * Internal composable that renders the segmented control content with layout.
 */
@Composable
private fun SegmentedControlContent(
    segments: List<SegmentData>,
    segmentContents: List<@Composable () -> Unit>,
    selectedIndex: Int,
    onSegmentSelect: (Int) -> Unit,
    customSelectedColors: List<Color>?,
    role: Role,
    enabled: Boolean,
    isHorizontal: Boolean,
) {
    val backgroundProgress by animateFloatAsState(
        label = "Background Position Progress",
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
        ),
        targetValue = selectedIndex.toFloat(),
    )

    // Determine background color
    val selectedSegment = segments[selectedIndex]
    val backgroundColor = when {
        selectedSegment.customBackgroundColor != null -> selectedSegment.customBackgroundColor
        customSelectedColors != null && selectedIndex < customSelectedColors.size ->
            customSelectedColors[selectedIndex]

        else -> SparkTheme.colors.mainVariant
    }

    val backgroundColorState by animateColorAsState(
        label = "Background Color",
        targetValue = backgroundColor,
    )

    // Animate corner radius
    val startCornerRadius by animateIntAsState(
        label = "Start Corner Radius",
        targetValue = if (selectedIndex == 0) {
            SegmentedControlDefaults.FullCornerRadiusPercent
        } else {
            SegmentedControlDefaults.MediumCornerRadiusPercent
        },
    )
    val endCornerRadius by animateIntAsState(
        label = "End Corner Radius",
        targetValue = if (selectedIndex == segments.lastIndex) {
            SegmentedControlDefaults.FullCornerRadiusPercent
        } else {
            SegmentedControlDefaults.MediumCornerRadiusPercent
        },
    )

    Layout(
        modifier = Modifier
            .sizeIn(
                minWidth = SegmentedControlDefaults.MinTouchTargetSize,
                minHeight = SegmentedControlDefaults.MinTouchTargetSize,
            )
            .clip(SparkTheme.shapes.full)
            .selectableGroup()
            .border(
                width = SegmentedControlDefaults.BorderWidth,
                color = SparkTheme.colors.outlineHigh,
                shape = SparkTheme.shapes.full,
            ),
        content = {
            // Segments
            segments.forEachIndexed { index, _ ->
                val selected = index == selectedIndex
                val textColor by animateColorAsState(
                    label = "Text Color",
                    targetValue = if (selected) {
                        SparkTheme.colors.onMainVariant
                    } else {
                        LocalContentColor.current
                    },
                )

                val segmentShape = RoundedCornerShape(
                    topStartPercent = if (index == 0) {
                        SegmentedControlDefaults.FullCornerRadiusPercent
                    } else {
                        SegmentedControlDefaults.MediumCornerRadiusPercent
                    },
                    bottomStartPercent = if (index == 0) {
                        SegmentedControlDefaults.FullCornerRadiusPercent
                    } else {
                        SegmentedControlDefaults.MediumCornerRadiusPercent
                    },
                    topEndPercent = if (index == segments.lastIndex) {
                        SegmentedControlDefaults.FullCornerRadiusPercent
                    } else {
                        SegmentedControlDefaults.MediumCornerRadiusPercent
                    },
                    bottomEndPercent = if (index == segments.lastIndex) {
                        SegmentedControlDefaults.FullCornerRadiusPercent
                    } else {
                        SegmentedControlDefaults.MediumCornerRadiusPercent
                    },
                )

                Box(
                    modifier = Modifier
                        .layoutId(SegmentedControlLayoutId.Segment)
                        .clip(segmentShape)
                        .selectable(selected = selected, enabled = enabled) {
                            onSegmentSelect(index)
                        }
                        .semantics { role = Role.RadioButton },
                    contentAlignment = Alignment.Center,
                ) {
                    CompositionLocalProvider(LocalContentColor provides textColor) {
                        segmentContents[index]()
                    }
                }
            }

            // Background
            Box(
                modifier = Modifier
                    .layoutId(SegmentedControlLayoutId.Background)
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = startCornerRadius,
                            bottomStartPercent = startCornerRadius,
                            topEndPercent = endCornerRadius,
                            bottomEndPercent = endCornerRadius,
                        ),
                    )
                    .background(backgroundColorState),
            )

            // Dividers
            repeat(segments.size - 1) { dividerIndex ->
                // Hide dividers at horizontal edges of selected segment
                // Divider at index i is between segment i and i+1
                // So if segment 1 is selected, hide divider 0 (left) and divider 1 (right)
                val isHidden = dividerIndex == selectedIndex - 1 || dividerIndex == selectedIndex
                val dividerAlpha by animateFloatAsState(
                    label = "Divider Alpha",
                    targetValue = if (isHidden) 0f else 1f,
                )

                Box(
                    modifier = Modifier
                        .layoutId(SegmentedControlLayoutId.Divider)
                        .width(SegmentedControlDefaults.DividerWidth)
                        .fillMaxHeight()
                        .background(
                            SparkTheme.colors.outlineHigh.copy(alpha = dividerAlpha),
                        ),
                )
            }
        },
    ) { measurables, constraints ->
        val segmentCount = segments.size
        val dividerCount = segmentCount - 1

        if (isHorizontal) {
            // Horizontal layout: single row
            val dividerWidth = if (dividerCount > 0) {
                SegmentedControlDefaults.DividerWidth.roundToPx()
            } else {
                0
            }
            val availableWidth = constraints.maxWidth - (dividerWidth * dividerCount)
            val segmentWidth = availableWidth / segmentCount
            val segmentConstraints = Constraints.fixed(
                width = segmentWidth,
                height = constraints.minHeight,
            )

            val segmentPlaceables = measurables
                .filter { it.layoutId == SegmentedControlLayoutId.Segment }
                .map { it.measure(segmentConstraints) }

            val backgroundPlaceable = measurables
                .firstOrNull { it.layoutId == SegmentedControlLayoutId.Background }
                ?.measure(segmentConstraints)

            val dividerPlaceables = measurables
                .filter { it.layoutId == SegmentedControlLayoutId.Divider }
                .map { it.measure(Constraints.fixed(dividerWidth, constraints.minHeight)) }

            val maxHeight = maxOf(
                segmentPlaceables.maxOfOrNull { it.height } ?: 0,
                backgroundPlaceable?.height ?: 0,
                dividerPlaceables.maxOfOrNull { it.height } ?: 0,
            )

            layout(
                width = constraints.maxWidth,
                height = maxHeight,
            ) {
                backgroundPlaceable?.placeRelative(
                    x = (backgroundProgress * (segmentWidth + dividerWidth)).toInt(),
                    y = 0,
                )

                var xPosition = 0
                segmentPlaceables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(x = xPosition, y = 0)
                    xPosition += segmentWidth

                    if (index < dividerPlaceables.size) {
                        dividerPlaceables[index].placeRelative(
                            x = xPosition,
                            y = 0,
                        )
                        xPosition += dividerWidth
                    }
                }
            }
        } else {
            // Vertical layout: multi-row (typically 2 rows of 4)
            val segmentsPerRow = (segmentCount + 1) / 2 // Round up
            val dividerWidth = SegmentedControlDefaults.DividerWidth.roundToInt()
            val availableWidth = constraints.maxWidth - (dividerWidth * (segmentsPerRow - 1))
            val segmentWidth = availableWidth / segmentsPerRow
            val segmentConstraints = Constraints.fixed(
                width = segmentWidth,
                height = constraints.minHeight,
            )

            val segmentPlaceables = measurables
                .filter { it.layoutId == SegmentedControlLayoutId.Segment }
                .map { it.measure(segmentConstraints) }

            val backgroundPlaceable = measurables
                .firstOrNull { it.layoutId == SegmentedControlLayoutId.Background }
                ?.measure(segmentConstraints)

            val dividerPlaceables = measurables
                .filter { it.layoutId == SegmentedControlLayoutId.Divider }
                .map { it.measure(Constraints.fixed(dividerWidth, constraints.minHeight)) }

            val rowHeight = maxOf(
                segmentPlaceables.maxOfOrNull { it.height } ?: 0,
                backgroundPlaceable?.height ?: 0,
            )

            val selectedRow = selectedIndex / segmentsPerRow
            val selectedCol = selectedIndex % segmentsPerRow

            layout(
                width = constraints.maxWidth,
                height = rowHeight * 2,
            ) {
                // Place background
                backgroundPlaceable?.placeRelative(
                    x = selectedCol * (segmentWidth + dividerWidth),
                    y = selectedRow * rowHeight,
                )

                // Place segments and dividers
                var segmentIndex = 0
                var dividerIndex = 0
                repeat(2) { row ->
                    var xPosition = 0
                    repeat(segmentsPerRow) { col ->
                        if (segmentIndex < segmentPlaceables.size) {
                            segmentPlaceables[segmentIndex].placeRelative(
                                x = xPosition,
                                y = row * rowHeight,
                            )
                            xPosition += segmentWidth

                            // Place divider if not last in row and not last segment overall
                            if (col < segmentsPerRow - 1 && segmentIndex < segmentCount - 1) {
                                if (dividerIndex < dividerPlaceables.size) {
                                    dividerPlaceables[dividerIndex].placeRelative(
                                        x = xPosition,
                                        y = row * rowHeight,
                                    )
                                    xPosition += dividerWidth
                                    dividerIndex++
                                }
                            }
                            segmentIndex++
                        }
                    }
                }
            }
        }
    }
}
