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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.snackbars.animatedOpacity
import com.adevinta.spark.components.spacer.Spacer
import com.adevinta.spark.tokens.transparent

@Composable
public fun SparkSegmentedGauge(
    modifier: Modifier = Modifier,
    size: GaugeSize = GaugeSize.Small,
    type: GaugeType = GaugeType.NoData,
    color: Color = Color.Unspecified,
) {
    val (cellWidth, indicatorSize, spacingSize) = with(LocalDensity.current) {
        listOf(size.width.dp.roundToPx(), size.indicatorSize.dp.roundToPx(), 4.dp.roundToPx())
    }
    val indicatorOffset by animateIntOffsetAsState(
        IntOffset(
            x = cellWidth * type.index + cellWidth / 2 - indicatorSize / 2 + type.index * spacingSize,
            y = 0,
        ),
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            horizontalArrangement = spacedBy(4.dp),
        ) {
            repeat(5) { index ->
                val inRange = index <= type.index
                Cell(
                    size = size,
                    type = if (inRange) type else GaugeType.NoData,
                    color = when {
                        !inRange -> GaugeType.NoData.color
                        color == Color.Unspecified -> type.color
                        else -> color
                    },
                )
            }
        }
        val indicatorColor by animateColorAsState(
            targetValue = if (type == GaugeType.NoData) type.color.transparent else type.color,
            label = "indicator color",
        )
        val indicatorAlpha by animatedOpacity(
            visible = type != GaugeType.NoData,
            animation = spring(),
        )
        val indicatorSize = size.indicatorSize.dp
        Spacer(
            modifier = Modifier
                .offset { indicatorOffset }
                .size(indicatorSize)
                .graphicsLayer {
                    alpha = indicatorAlpha
                }
                .border(
                    border = BorderStroke(indicatorSize / 4, indicatorColor), shape = SparkTheme.shapes.full,
                )
                .padding(1.dp) // to avoid the background from overflowing du to the clipping aliasing
                .clip(SparkTheme.shapes.full)
                .background(SparkTheme.colors.surface),
        )
    }
}

@Composable
private fun Cell(
    size: GaugeSize,
    type: GaugeType,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(color)
    val borderColor by animateColorAsState(
        if (type == GaugeType.NoData) {
            SparkTheme.colors.outline
        } else {
            SparkTheme.colors.outline.transparent
        },
    )
    val borderSize by animateDpAsState(if (type == GaugeType.NoData) 1.dp else 0.dp)
    Spacer(
        modifier = Modifier
            .width(size.width.dp)
            .requiredHeight(size.height.dp)
            .border(width = borderSize, color = borderColor, shape = SparkTheme.shapes.full)
            .background(color = color, shape = SparkTheme.shapes.full),
    )
}

public enum class GaugeSize(
    internal val width: Int,
    internal val height: Int,
    internal val indicatorSize: Int,
) {
    Medium(34, 12, 16),
    Small(24, 8, 12);
}

public enum class GaugeSegment(
    internal val width: Int,
    internal val height: Int,
    internal val indicatorSize: Int,
) {
    Medium(34, 12, 16),
    Small(24, 8, 12);
}

@ExperimentalSparkApi
@Composable
public fun SegmentedGauge(
    modifier: Modifier = Modifier,
    size: GaugeSize = GaugeSize.Small,
    type: GaugeType = GaugeType.NoData,
    color: Color = Color.Unspecified,
) {
    SparkSegmentedGauge(
        modifier = modifier,
        size = size,
        type = type,
        color = color,
    )
}

@Preview
@Composable
private fun PreviewSegmentedGauge() {
    PreviewTheme() {
        SparkSegmentedGauge(type = GaugeType.VeryHigh)
        SparkSegmentedGauge(type = GaugeType.High)
        SparkSegmentedGauge(type = GaugeType.Medium)
        SparkSegmentedGauge(type = GaugeType.Low)
        SparkSegmentedGauge(type = GaugeType.VeryLow)
        SparkSegmentedGauge(type = GaugeType.NoData)
    }
}

@Preview
@Composable
private fun PreviewCell() {
    PreviewTheme(
        color = { SparkTheme.colors.backgroundVariant },
    ) {
        Row() {
            GaugeSize.entries.forEach { size ->
                Column() {
                    Cell(
                        size = size,
                        type = GaugeType.VeryHigh,
                        color = GaugeType.VeryHigh.color,
                    )
                    Cell(
                        size = size,
                        type = GaugeType.High,
                        color = GaugeType.High.color,
                    )
                    Cell(
                        size = size,
                        type = GaugeType.Medium,
                        color = GaugeType.Medium.color,
                    )
                    Cell(
                        size = size,
                        type = GaugeType.Low,
                        color = GaugeType.Low.color,
                    )
                    Cell(
                        size = size,
                        type = GaugeType.VeryLow,
                        color = GaugeType.VeryLow.color,
                    )
                    Cell(
                        size = size,
                        type = GaugeType.NoData,
                        color = GaugeType.NoData.color,
                    )
                }
            }
        }

    }
}
