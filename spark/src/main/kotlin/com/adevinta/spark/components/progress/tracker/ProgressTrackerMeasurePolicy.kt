/*
 * Copyright (c) 2023 Adevinta
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
package com.adevinta.spark.components.progress.tracker

import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.MultiContentMeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapIndexed
import androidx.compose.ui.util.fastMaxOfOrNull
import androidx.compose.ui.util.fastSumBy
import com.adevinta.spark.components.textfields.hasWidthThenDefault
import com.adevinta.spark.components.textfields.heightOrZero

internal data class ProgressTrackerMeasurePolicy(
    private val orientation: LayoutOrientation,
    private val arrangementSpacing: Dp,
    private val indicatorSize: Dp,
) : MultiContentMeasurePolicy {

    override fun MeasureScope.measure(
        measurables: List<List<Measurable>>,
        constraints: Constraints,
    ): MeasureResult {
        val (trackMeasurables, labelMeasurables, indicatorMeasurables) = measurables

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val stepsCount = indicatorMeasurables.size
        // Each indicator  have a fixed size coming from the size object
        val indicatorSize = indicatorSize.roundToPx()
        // Calculate the width of each step, taking into account the padding between steps
        val stepsWidth = (constraints.maxWidth / stepsCount)
        // Measure each label to fit within the calculated step width
        val labelPlaceables = labelMeasurables.fastMap {
            val labelConstraint = if (orientation == LayoutOrientation.Horizontal) {
                constraints.copy(minWidth = stepsWidth, maxWidth = stepsWidth)
            } else {
                val indicatorWithSpacing = indicatorSize + arrangementSpacing.roundToPx()
                looseConstraints.copy(
                    minHeight = indicatorWithSpacing,
                    maxWidth = if (constraints.hasBoundedWidth) {
                        (constraints.maxWidth - indicatorWithSpacing).coerceAtLeast(0)
                    } else {
                        Constraints.Infinity
                    },
                )
            }
            it.measure(labelConstraint)
        }

        val indicatorPlaceables = indicatorMeasurables.fastMap {
            it.measure(Constraints.fixed(indicatorSize, indicatorSize))
        }

        val trackPlaceables = trackMeasurables.fastMapIndexed { index, trackMeasurable ->
            val trackConstraint = if (orientation == LayoutOrientation.Horizontal) {
                // Calculate the width of the track, taking into account the width of the indicators and padding
                val trackWidth = stepsWidth - (indicatorPlaceables.first().width + arrangementSpacing.roundToPx())
                constraints.copy(minWidth = trackWidth, maxWidth = trackWidth)
            } else {
                val extraSizeOfLabelBaseline = 4.sp.roundToPx()
                val trackHeight = heightOrZero(labelPlaceables.getOrNull(index)) +
                    extraSizeOfLabelBaseline -
                    (arrangementSpacing.roundToPx() / 2) -
                    indicatorSize
                looseConstraints.copy(maxHeight = trackHeight.coerceAtLeast(arrangementSpacing.roundToPx() / 2))
            }
            trackMeasurable.measure(trackConstraint)
        }

        val labelTopPadding = arrangementSpacing.roundToPx()

        val layoutWidth: Int
        val layoutHeight: Int

        if (orientation == LayoutOrientation.Horizontal) {
            layoutWidth = labelPlaceables.fastSumBy(Placeable::width)
            // Calculate height of the layout by taking into account the height maximum height of all labels, indicator
            // and padding
            val maxLabelHeight = labelPlaceables.fastMaxOfOrNull(Placeable::height) ?: 0
            val maxLabelHeightWithPadding = maxLabelHeight.takeIf {
                it > 0 // Don't add the padding if there are no labels
            }?.let {
                it + arrangementSpacing.roundToPx()
            } ?: 0
            layoutHeight = indicatorPlaceables.first().height + maxLabelHeightWithPadding
        } else {
            val maxLabelWidth = labelPlaceables.maxByOrNull(Placeable::width)?.width ?: 0
            val maxLabelWidthWithPadding = maxLabelWidth.takeIf {
                it > 0 // Don't add the padding if there are no labels
            }?.let {
                it + arrangementSpacing.roundToPx()
            } ?: 0
            layoutWidth = indicatorPlaceables.first().width + maxLabelWidthWithPadding
            layoutHeight = labelPlaceables.fastSumBy {
                maxOf(it.height, indicatorPlaceables.first().height) + arrangementSpacing.roundToPx()
            }
        }

        return layout(
            width = layoutWidth,
            height = layoutHeight,
        ) {
            if (orientation == LayoutOrientation.Horizontal) {
                placeHorizontally(
                    this@measure,
                    indicatorPlaceables,
                    stepsWidth,
                    labelPlaceables,
                    labelTopPadding,
                    trackPlaceables,
                )
            } else {
                placeVertically(
                    this@measure,
                    indicatorPlaceables,
                    labelPlaceables,
                    trackPlaceables,
                )
            }
        }
    }

    private fun Placeable.PlacementScope.placeHorizontally(
        measureScope: MeasureScope,
        indicatorPlaceables: List<Placeable>,
        stepsWidth: Int,
        labelPlaceables: List<Placeable>,
        labelTopPadding: Int,
        trackPlaceables: List<Placeable>,
    ) {
        with(measureScope) {
            indicatorPlaceables.fastForEachIndexed { index, indicatorPlaceable ->
                // Center the indicator horizontally and place it at the top of the layout
                val indicatorX = (stepsWidth * index) + stepsWidth / 2 - indicatorPlaceable.width / 2
                indicatorPlaceable.placeRelative(
                    x = indicatorX,
                    y = 0,
                )
                labelPlaceables[index].placeRelative(
                    x = stepsWidth * index,
                    y = indicatorPlaceable.height + labelTopPadding, // centered
                )
                if (index < indicatorPlaceables.size - 1) {
                    trackPlaceables[index].placeRelative(
                        x = indicatorX + indicatorPlaceable.width + arrangementSpacing.roundToPx() / 2,
                        y = indicatorPlaceable.width / 2,
                    )
                }
            }
        }
    }

    private fun Placeable.PlacementScope.placeVertically(
        measureScope: MeasureScope,
        indicatorPlaceables: List<Placeable>,
        labelPlaceables: List<Placeable>,
        trackPlaceables: List<Placeable>,
    ) {
        with(measureScope) {
            var previousLabelY = 0

            indicatorPlaceables.fastForEachIndexed { index, indicatorPlaceable ->
                val labelPlaceable = labelPlaceables[index]
                val labelFirstBaseline = labelPlaceable[FirstBaseline]

                labelPlaceable.placeRelative(
                    x = indicatorPlaceable.width + labelPlaceable.hasWidthThenDefault(arrangementSpacing.roundToPx()),
                    y = previousLabelY +
                        (
                            indicatorPlaceable.height /
                                2 -
                                labelFirstBaseline +
                                5.sp.roundToPx() // Magic number to center the text to the indicator text baseline
                            ),
                )
                indicatorPlaceable.placeRelative(x = 0, y = previousLabelY)
                if (index < indicatorPlaceables.size - 1) {
                    trackPlaceables[index].placeRelative(
                        x = indicatorPlaceable.width / 2,
                        y = previousLabelY + indicatorPlaceable.height + arrangementSpacing.roundToPx() / 2,
                    )
                }
                previousLabelY += maxOf(
                    labelPlaceable.height,
                    indicatorSize.roundToPx(),
                ) +
                    arrangementSpacing.roundToPx()
            }
        }
    }
}
