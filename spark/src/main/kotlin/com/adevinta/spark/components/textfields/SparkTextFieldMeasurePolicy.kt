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
package com.adevinta.spark.components.textfields

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.lerp
import kotlin.math.max
import kotlin.math.roundToInt

internal class SparkTextFieldMeasurePolicy(
    private val onLabelMeasured: (Size) -> Unit,
    private val singleLine: Boolean,
    private val animationProgress: Float,
    private val paddingValues: PaddingValues,
) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        // used to calculate the constraints for measuring elements that will be placed in a row
        var occupiedSpaceHorizontally = 0
        var occupiedSpaceVertically = 0
        val bottomPadding = paddingValues.calculateBottomPadding().roundToPx()

        // measure leading icon
        val relaxedConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val leadingPlaceable = measurables.fastFirstOrNull {
            it.layoutId == LeadingId
        }?.measure(relaxedConstraints)
        occupiedSpaceHorizontally += widthOrZero(leadingPlaceable)
        occupiedSpaceVertically = max(occupiedSpaceVertically, heightOrZero(leadingPlaceable))

        // measure trailing icon
        val trailingPlaceable = measurables.fastFirstOrNull { it.layoutId == TrailingId }
            ?.measure(relaxedConstraints.offset(horizontal = -occupiedSpaceHorizontally))
        occupiedSpaceHorizontally += widthOrZero(trailingPlaceable)
        occupiedSpaceVertically = max(occupiedSpaceVertically, heightOrZero(trailingPlaceable))

        // measure label
        val labelHorizontalPaddingOffset =
            paddingValues.calculateLeftPadding(layoutDirection).roundToPx() +
                paddingValues.calculateRightPadding(layoutDirection).roundToPx()
        val labelConstraints = relaxedConstraints.offset(
            horizontal = lerp(
                -occupiedSpaceHorizontally -
                    labelHorizontalPaddingOffset, // label in middle
                -labelHorizontalPaddingOffset, // label at top
                animationProgress,
            ),
            vertical = -bottomPadding,
        )
        val labelPlaceable =
            measurables.fastFirstOrNull { it.layoutId == LabelId }?.measure(labelConstraints)
        val labelSize = labelPlaceable?.let { Size(it.width.toFloat(), it.height.toFloat()) } ?: Size.Zero
        onLabelMeasured(labelSize)

        // supporting text must be measured after other elements, but we
        // reserve space for it using its intrinsic height as a heuristic
        val supportingMeasurable =
            measurables.fastFirstOrNull { it.layoutId == SupportingId }
        val supportingIntrinsicHeight =
            supportingMeasurable?.minIntrinsicHeight(constraints.minWidth) ?: 0

        // measure text field
        // On top, we offset either by default padding or by label's half height if its too big.
        // On bottom, we offset to make room for supporting text.
        // minHeight must not be set to 0 due to how foundation TextField treats zero minHeight.
        val topPadding = max(
            heightOrZero(labelPlaceable) / 2,
            paddingValues.calculateTopPadding().roundToPx(),
        )
        val textConstraints = constraints.offset(
            horizontal = -occupiedSpaceHorizontally,
            vertical = -bottomPadding - topPadding - supportingIntrinsicHeight,
        ).copy(minHeight = 0)
        val textFieldPlaceable =
            measurables.fastFirst { it.layoutId == TextFieldId }.measure(textConstraints)

        // measure placeholder
        val placeholderConstraints = textConstraints.copy(minWidth = 0)
        val placeholderPlaceable =
            measurables.fastFirstOrNull { it.layoutId == PlaceholderId }?.measure(placeholderConstraints)

        occupiedSpaceVertically = max(
            occupiedSpaceVertically,
            max(heightOrZero(textFieldPlaceable), heightOrZero(placeholderPlaceable)) +
                topPadding +
                bottomPadding,
        )

        val width = calculateWidth(
            leadingPlaceableWidth = widthOrZero(leadingPlaceable),
            trailingPlaceableWidth = widthOrZero(trailingPlaceable),
            textFieldPlaceableWidth = textFieldPlaceable.width,
            labelPlaceableWidth = widthOrZero(labelPlaceable),
            placeholderPlaceableWidth = widthOrZero(placeholderPlaceable),
            animationProgress = animationProgress,
            constraints = constraints,
            density = density,
            paddingValues = paddingValues,
        )

        // measure supporting text
        val supportingConstraints = relaxedConstraints.offset(
            vertical = -occupiedSpaceVertically,
        ).copy(minHeight = 0, maxWidth = width)

        val supportingPlaceable =
            supportingMeasurable?.measure(supportingConstraints.copy(maxWidth = width))
        val supportingHeight = heightOrZero(supportingPlaceable)

        val totalHeight = calculateHeight(
            leadingPlaceableHeight = heightOrZero(leadingPlaceable),
            trailingPlaceableHeight = heightOrZero(trailingPlaceable),
            textFieldPlaceableHeight = textFieldPlaceable.height,
            labelPlaceableHeight = heightOrZero(labelPlaceable),
            placeholderPlaceableHeight = heightOrZero(placeholderPlaceable),
            supportingPlaceableHeight = supportingHeight,
            animationProgress = animationProgress,
            constraints = constraints,
            density = density,
            paddingValues = paddingValues,
        )
        val height = totalHeight - supportingHeight

        val borderPlaceable =
            measurables
                .fastFirst { it.layoutId == ContainerId }
                .measure(
                    Constraints(
                        minWidth = if (width != Constraints.Infinity) width else 0,
                        maxWidth = width,
                        minHeight = if (height != Constraints.Infinity) height else 0,
                        maxHeight = height,
                    ),
                )
        return layout(width, totalHeight) {
            place(
                totalHeight = totalHeight,
                width = width,
                leadingPlaceable = leadingPlaceable,
                trailingPlaceable = trailingPlaceable,
                textFieldPlaceable = textFieldPlaceable,
                labelPlaceable = labelPlaceable,
                placeholderPlaceable = placeholderPlaceable,
                supportingPlaceable = supportingPlaceable,
                containerPlaceable = borderPlaceable,
                animationProgress = animationProgress,
                singleLine = singleLine,
                density = density,
                layoutDirection = layoutDirection,
                paddingValues = paddingValues,
            )
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int = intrinsicHeight(measurables, width) { intrinsicMeasurable, w ->
        intrinsicMeasurable.maxIntrinsicHeight(w)
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int = intrinsicHeight(measurables, width) { intrinsicMeasurable, w ->
        intrinsicMeasurable.minIntrinsicHeight(w)
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int = intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
        intrinsicMeasurable.maxIntrinsicWidth(h)
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int = intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
        intrinsicMeasurable.minIntrinsicWidth(h)
    }

    private fun IntrinsicMeasureScope.intrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int,
    ): Int {
        val textFieldWidth =
            intrinsicMeasurer(measurables.fastFirst { it.layoutId == TextFieldId }, height)
        val labelWidth =
            measurables.fastFirstOrNull { it.layoutId == LabelId }
                ?.let { intrinsicMeasurer(it, height) } ?: 0
        val trailingWidth =
            measurables.fastFirstOrNull { it.layoutId == TrailingId }
                ?.let { intrinsicMeasurer(it, height) } ?: 0
        val leadingWidth =
            measurables.fastFirstOrNull { it.layoutId == LeadingId }
                ?.let { intrinsicMeasurer(it, height) } ?: 0
        val placeholderWidth =
            measurables.fastFirstOrNull { it.layoutId == PlaceholderId }
                ?.let { intrinsicMeasurer(it, height) } ?: 0
        return calculateWidth(
            leadingPlaceableWidth = leadingWidth,
            trailingPlaceableWidth = trailingWidth,
            textFieldPlaceableWidth = textFieldWidth,
            labelPlaceableWidth = labelWidth,
            placeholderPlaceableWidth = placeholderWidth,
            animationProgress = animationProgress,
            constraints = ZeroConstraints,
            density = density,
            paddingValues = paddingValues,
        )
    }

    private fun IntrinsicMeasureScope.intrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int,
    ): Int {
        var remainingWidth = width
        val leadingHeight =
            measurables
                .fastFirstOrNull { it.layoutId == LeadingId }
                ?.let {
                    remainingWidth =
                        remainingWidth.substractConstraintSafely(
                            it.maxIntrinsicWidth(Constraints.Infinity),
                        )
                    intrinsicMeasurer(it, width)
                } ?: 0

        val trailingHeight =
            measurables
                .fastFirstOrNull { it.layoutId == TrailingId }
                ?.let {
                    remainingWidth =
                        remainingWidth.substractConstraintSafely(
                            it.maxIntrinsicWidth(Constraints.Infinity),
                        )
                    intrinsicMeasurer(it, width)
                } ?: 0

        val labelHeight =
            measurables
                .fastFirstOrNull { it.layoutId == LabelId }
                ?.let { intrinsicMeasurer(it, lerp(remainingWidth, width, animationProgress)) } ?: 0
        val textFieldHeight =
            intrinsicMeasurer(measurables.fastFirst { it.layoutId == TextFieldId }, remainingWidth)

        val placeholderHeight =
            measurables
                .fastFirstOrNull { it.layoutId == PlaceholderId }
                ?.let { intrinsicMeasurer(it, remainingWidth) } ?: 0

        val supportingHeight =
            measurables
                .fastFirstOrNull { it.layoutId == SupportingId }
                ?.let { intrinsicMeasurer(it, width) } ?: 0

        return calculateHeight(
            leadingPlaceableHeight = leadingHeight,
            trailingPlaceableHeight = trailingHeight,
            textFieldPlaceableHeight = textFieldHeight,
            labelPlaceableHeight = labelHeight,
            placeholderPlaceableHeight = placeholderHeight,
            supportingPlaceableHeight = supportingHeight,
            animationProgress = animationProgress,
            constraints = ZeroConstraints,
            density = density,
            paddingValues = paddingValues,
        )
    }
}

private fun Int.substractConstraintSafely(from: Int): Int {
    if (this == Constraints.Infinity) {
        return this
    }
    return this - from
}

/**
 * Calculate the width of the [SparkTextField] given all elements that should be
 * placed inside
 */
private fun calculateWidth(
    leadingPlaceableWidth: Int,
    trailingPlaceableWidth: Int,
    textFieldPlaceableWidth: Int,
    labelPlaceableWidth: Int,
    placeholderPlaceableWidth: Int,
    animationProgress: Float,
    constraints: Constraints,
    density: Float,
    paddingValues: PaddingValues,
): Int {
    val middleSection = maxOf(
        textFieldPlaceableWidth,
        lerp(labelPlaceableWidth, 0, animationProgress),
        placeholderPlaceableWidth,
    )
    val wrappedWidth = leadingPlaceableWidth + middleSection + trailingPlaceableWidth

    // Actual LayoutDirection doesn't matter; we only need the sum
    val labelHorizontalPadding =
        (
            paddingValues.calculateLeftPadding(LayoutDirection.Ltr) +
                paddingValues.calculateRightPadding(LayoutDirection.Ltr)
            )
            .value *
            density
    val focusedLabelWidth =
        ((labelPlaceableWidth + labelHorizontalPadding) * animationProgress).roundToInt()
    return maxOf(wrappedWidth, focusedLabelWidth, constraints.minWidth)
}

/**
 * Calculate the height of the [SparkTextField] given all elements that should be placed inside.
 * This includes the supporting text, if it exists, even though this element is not "visually"
 * inside the text field.
 */
private fun calculateHeight(
    leadingPlaceableHeight: Int,
    trailingPlaceableHeight: Int,
    textFieldPlaceableHeight: Int,
    labelPlaceableHeight: Int,
    placeholderPlaceableHeight: Int,
    supportingPlaceableHeight: Int,
    animationProgress: Float,
    constraints: Constraints,
    density: Float,
    paddingValues: PaddingValues,
): Int {
    // middle section is defined as a height of the text field or placeholder ( whichever is
    // taller) plus 16.dp or half height of the label if it is taller, given that the label
    // is vertically centered to the top edge of the resulting text field's container
    val inputFieldHeight =
        maxOf(
            textFieldPlaceableHeight,
            placeholderPlaceableHeight,
            lerp(labelPlaceableHeight, 0, animationProgress),
        )
    val topPadding = paddingValues.calculateTopPadding().value * density
    val actualTopPadding = lerp(topPadding, max(topPadding, labelPlaceableHeight / 2f), animationProgress)
    val bottomPadding = paddingValues.calculateBottomPadding().value * density
    val middleSectionHeight = inputFieldHeight + bottomPadding + actualTopPadding

    return max(
        constraints.minHeight,
        maxOf(
            leadingPlaceableHeight,
            trailingPlaceableHeight,
            middleSectionHeight.roundToInt(),
        ) +
            supportingPlaceableHeight,
    )
}

/**
 * Places the provided text field, placeholder, label, optional leading and trailing icons inside
 * the [SparkTextField]
 */
private fun Placeable.PlacementScope.place(
    totalHeight: Int,
    width: Int,
    leadingPlaceable: Placeable?,
    trailingPlaceable: Placeable?,
    textFieldPlaceable: Placeable,
    labelPlaceable: Placeable?,
    placeholderPlaceable: Placeable?,
    supportingPlaceable: Placeable?,
    containerPlaceable: Placeable,
    animationProgress: Float,
    singleLine: Boolean,
    density: Float,
    layoutDirection: LayoutDirection,
    paddingValues: PaddingValues,
) {
    // place container
    containerPlaceable.place(IntOffset.Zero)

    // Most elements should be positioned w.r.t the text field's "visual" height, i.e., excluding
    // the supporting text on bottom
    val height = totalHeight - heightOrZero(supportingPlaceable)
    val topPadding = (paddingValues.calculateTopPadding().value * density).roundToInt()
    val startPadding =
        (paddingValues.calculateStartPadding(layoutDirection).value * density).roundToInt()

    // Single line text fields have text components centered vertically.
    // Multiline text fields have text components aligned to top with padding.
    fun calculateVerticalPosition(placeable: Placeable): Int =
        max(
            if (singleLine) {
                Alignment.CenterVertically.align(placeable.height, height)
            } else {
                topPadding
            },
            heightOrZero(labelPlaceable) / 2,
        )

    // placed center vertically and to the start edge horizontally
    leadingPlaceable?.placeRelative(
        0,
        if (singleLine) {
            Alignment.CenterVertically.align(leadingPlaceable.height, height)
        } else {
            topPadding
        },
    )

    // placed center vertically and to the end edge horizontally
    trailingPlaceable?.placeRelative(
        width - trailingPlaceable.width,
        if (singleLine) {
            Alignment.CenterVertically.align(trailingPlaceable.height, height)
        } else {
            topPadding
        },
    )

    // label position is animated
    // in single line text field label is centered vertically before animation starts
    labelPlaceable?.let {
        val startPositionY =
            if (singleLine) {
                Alignment.CenterVertically.align(it.height, height)
            } else {
                topPadding
            }
        val positionY = lerp(startPositionY, -(it.height / 2), animationProgress)
        val positionX =
            (
                if (leadingPlaceable == null) {
                    0f
                } else {
                    (widthOrZero(leadingPlaceable) - startPadding) * (1 - animationProgress) + startPadding
                }
                ).roundToInt() +
                startPadding
        it.placeRelative(positionX, positionY)
    }

    val textHorizontalPosition = widthOrZero(leadingPlaceable)

    textFieldPlaceable.placeRelative(
        textHorizontalPosition,
        calculateVerticalPosition(textFieldPlaceable),
    )

    // placed similar to the input text above
    placeholderPlaceable?.let {
        val placeholderVerticalPosition = if (singleLine) {
            Alignment.CenterVertically.align(it.height, height)
        } else {
            topPadding
        }
        it.placeRelative(widthOrZero(leadingPlaceable), placeholderVerticalPosition)
    }

    supportingPlaceable?.placeRelative(
        x = 0,
        y = height,
    )
}
