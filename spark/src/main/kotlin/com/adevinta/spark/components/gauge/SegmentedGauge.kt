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
package com.adevinta.spark.components.gauge

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.transparent
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Composable
private fun SparkSegmentedGauge(
    segments: GaugeSegments,
    size: GaugeSize,
    type: GaugeType?,
    color: Color,
    showIndicator: Boolean,
    modifier: Modifier = Modifier,
    testTag: String? = null,
    description: @Composable FlowRowScope.() -> Unit,
) {
    val (cellWidth, indicatorSize, spacingSize) = with(LocalDensity.current) {
        listOf(size.width.dp.roundToPx(), size.indicatorSize.dp.roundToPx(), GaugeDefaults.SegmentSpacing.roundToPx())
    }
    val index = type?.index ?: 0

    val indicatorOffset = remember(index, cellWidth, indicatorSize) {
        IntOffset(
            x = cellWidth * index + cellWidth / 2 - indicatorSize / 2 + index * spacingSize,
            y = 0,
        )
    }

    // Coordinated animation state
    val transition = updateTransition(
        targetState = GaugeAnimationState(
            type = type,
            index = index,
            customColor = color,
            segments = segments,
            cellWidth = cellWidth,
            indicatorSize = indicatorSize,
            spacingSize = spacingSize,
            indicatorOffset = indicatorOffset,
        ),
        label = "gauge",
    )
    FlowRow(
        modifier = modifier
            .sparkUsageOverlay()
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.invisibleSemantic(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(GaugeDefaults.SegmentSpacing),
            ) {
                repeat(segments.count) { segmentIndex ->
                    Cell(
                        size = size,
                        type = type?.takeIf { it.index >= segmentIndex },
                        customColor = color,
                        segmentIndex = segmentIndex,
                        transition = transition,
                        testTag = testTag?.let { "${it}_cell" },
                    )
                }
            }
            Indicator(
                size = size.indicatorSize.dp,
                type = type,
                offset = { indicatorOffset },
                visible = showIndicator,
                customColor = color,
                transition = transition,
                testTag = testTag?.let { "${it}_indicator" },
            )
        }
        ProvideTextStyle(SparkTheme.typography.body2) {
            description()
        }
    }
}

/**
 * Internal state for coordinated gauge animations.
 */
private data class GaugeAnimationState(
    val type: GaugeType?,
    val index: Int,
    val customColor: Color,
    val segments: GaugeSegments,
    val cellWidth: Int,
    val indicatorSize: Int,
    val spacingSize: Int,
    val indicatorOffset: IntOffset,
)

@Composable
private fun Indicator(
    type: GaugeType?,
    customColor: Color,
    size: Dp,
    visible: Boolean,
    offset: () -> IntOffset,
    transition: Transition<GaugeAnimationState>,
    modifier: Modifier = Modifier,
    testTag: String? = null,
) {
    val animatedIndicatorOffset by transition.animateIntOffset(
        transitionSpec = { GaugeDefaults.IndicatorAnimationSpec },
        label = "indicator offset",
    ) { it.indicatorOffset }

    val indicatorColor by transition.animateColor(
        transitionSpec = { spring() },
        label = "indicator color",
    ) {
        when {
            it.type == null || !visible -> SparkTheme.colors.surface
            customColor != Color.Unspecified -> customColor
            else -> it.type.color
        }
    }
    val indicatorAlpha by transition.animateFloat(
        transitionSpec = { spring() },
        label = "indicator color",
    ) {
        if (it.type == null || !visible) 0f else 1f
    }

    Spacer(
        modifier = modifier
            .size(size)
            // We use alpha instead of visibility because if we hide the indicator the overall size of
            // The gauge would change and make the siblings move.
            .graphicsLayer { alpha = indicatorAlpha }
            .offset { animatedIndicatorOffset }
            .border(
                border = BorderStroke(size / 4, indicatorColor),
                shape = SparkTheme.shapes.full,
            )
            .padding(1.dp) // to avoid the background from overflowing du to the clipping aliasing
            .clip(SparkTheme.shapes.full)
            .background(SparkTheme.colors.surface)
            .ifNotNull(testTag) { testTag(it) },
    )
}

@Composable
private fun Cell(
    size: GaugeSize,
    type: GaugeType?,
    customColor: Color,
    segmentIndex: Int,
    transition: Transition<GaugeAnimationState>,
    modifier: Modifier = Modifier,
    testTag: String? = null,
) {
    val animatedColor by animateColorAsState(
        animationSpec = spring(),
        label = "cell color $segmentIndex",
        targetValue = when {
            type == null -> SparkTheme.colors.surface
            customColor != Color.Unspecified -> customColor
            else -> type.color
        },
    )

    val animatedBorderColor by animateColorAsState(
        animationSpec = spring(),
        label = "cell border color $segmentIndex",
        targetValue = if (type == null) {
            SparkTheme.colors.outline
        } else {
            SparkTheme.colors.outline.transparent
        },
    )

    val animatedBorderSize by animateDpAsState(
        animationSpec = spring(),
        label = "cell border size $segmentIndex",
        targetValue = if (type == null) 1.dp else 0.dp,
    )

    Spacer(
        modifier = Modifier
            .width(size.width.dp)
            .requiredHeight(size.height.dp)
            .border(width = animatedBorderSize, color = animatedBorderColor, shape = SparkTheme.shapes.full)
            .background(color = animatedColor, shape = SparkTheme.shapes.full)
            .ifNotNull(testTag) { testTag(it) },
    )
}

/**
 * A compact, three-segment read-only gauge used to represent qualitative levels (for example:
 * "Very low", "Low", "Very high").
 *
 * @param modifier Modifier applied to the gauge.
 * @param size Size of the gauge; typically [GaugeSize.Small] or [GaugeSize.Medium].
 * @param type Optional gauge type that determines which segments are filled and the semantic color.
 * When `null`, the gauge renders unfilled segments (neutral state).
 * @param customColor Optional color override for filled segments. When [Color.Unspecified], the semantic color
 * from [type] is used.
 * @param showIndicator toggle the visibility of the indicator.
 * @param description Description of the gauge. if not enough space is available horizontally it'll be placed bellow
 * the gauge.
 *
 * @see SegmentedGauge
 */
@ExperimentalSparkApi
@Composable
public fun SegmentedGaugeShort(
    modifier: Modifier = Modifier,
    size: GaugeSize = GaugeDefaults.Size,
    type: GaugeTypeShort? = null,
    customColor: Color = GaugeDefaults.Color,
    showIndicator: Boolean = true,
    description: @Composable FlowRowScope.() -> Unit,
) {
    SparkSegmentedGauge(
        modifier = modifier,
        size = size,
        type = type,
        color = customColor,
        segments = GaugeSegments.Short,
        showIndicator = showIndicator,
        description = description,
        testTag = null,
    )
}

/**
 * A five-segment read-only gauge used to represent qualitative levels (for example: Very low →
 * Very high).
 *
 * @param modifier Modifier applied to the gauge.
 * @param size Size of the gauge; typically [GaugeSize.Small] or [GaugeSize.Medium].
 * @param type Optional gauge type that determines which segments are filled and the semantic color.
 * When `null`, the gauge renders unfilled segments (neutral state).
 * @param customColor Optional color override for filled segments. When [Color.Unspecified], the semantic color
 * from [type] is used.
 * @param showIndicator toggle the visibility of the indicator.
 * @param description Description of the gauge. if not enough space is available horizontally it'll be placed bellow
 * the gauge.
 *
 * @see SegmentedGaugeShort
 *
 */
@ExperimentalSparkApi
@Composable
public fun SegmentedGauge(
    modifier: Modifier = Modifier,
    size: GaugeSize = GaugeDefaults.Size,
    type: GaugeTypeNormal? = null,
    customColor: Color = GaugeDefaults.Color,
    showIndicator: Boolean = true,
    description: @Composable FlowRowScope.() -> Unit,
) {
    SparkSegmentedGauge(
        modifier = modifier,
        size = size,
        type = type,
        color = customColor,
        segments = GaugeSegments.Normal,
        showIndicator = showIndicator,
        description = description,
        testTag = null,
    )
}

@Preview
@Composable
private fun PreviewSegmentedGauge() {
    PreviewTheme {
        SegmentedGauge(
            type = GaugeTypeNormal.VeryHigh,
            customColor = SparkTheme.colors.accent,
        ) { Text("VeryHigh custom color") }
        SegmentedGauge(type = null, customColor = SparkTheme.colors.accentContainer) { Text("No data custom color") }
        SegmentedGauge(type = GaugeTypeNormal.VeryHigh) { Text("VeryHigh") }
        SegmentedGauge(type = GaugeTypeNormal.High) { Text("High") }
        SegmentedGauge(type = GaugeTypeNormal.Medium) { Text("Medium") }
        SegmentedGauge(type = GaugeTypeNormal.Low) { Text("Low") }
        SegmentedGauge(type = GaugeTypeNormal.VeryLow) { Text("VeryLow") }
        SegmentedGauge(type = null) { Text("No data") }
    }
}

@Preview
@Composable
private fun PreviewSegmentedGaugeShort() {
    PreviewTheme {
        SegmentedGaugeShort(type = GaugeTypeShort.VeryHigh) { Text("VeryHigh") }
        SegmentedGaugeShort(type = GaugeTypeShort.Low) { Text("Low") }
        SegmentedGaugeShort(type = GaugeTypeShort.VeryLow) { Text("VeryLow") }
        SegmentedGaugeShort(type = null) { Text("No data") }
    }
}
