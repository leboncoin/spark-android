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
package com.adevinta.spark.tools.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Reserves at least 44.dp in size to disambiguate touch interactions if the element would measure
 * smaller.
 *
 * https://m3.material.io/foundations/accessible-design/accessibility-basics#28032e45-c598-450c-b355-f9fe737b1cd8
 *
 * This uses the Spark recommended minimum size of 44.dp x 44.dp, which may not be the same as the
 * system enforced minimum size. The minimum clickable / touch target size (44.dp by default) is
 * controlled by the system via ViewConfiguration and automatically expanded at the touch input layer.
 *
 * This modifier is not needed for touch target expansion to happen. It only affects layout, to make
 * sure there is adequate space for touch target expansion.
 */
public fun Modifier.minimumTouchTargetSize(): Modifier = this then MinimumTouchTargetModifier()

private class MinimumTouchTargetModifier : ModifierNodeElement<MinimumTouchTargetModifierNode>() {

    override fun create() = MinimumTouchTargetModifierNode()
    override fun update(node: MinimumTouchTargetModifierNode) = Unit

    override fun InspectorInfo.inspectableProperties() {
        name = "minimumTouchTargetSize"
        properties["README"] = "Adds outer padding to measure at least 44.dp (default) in " +
            "size to disambiguate touch interactions if the element would measure smaller"
    }

    override fun hashCode(): Int = System.identityHashCode(this)
    override fun equals(other: Any?) = (other === this)
}

private class MinimumTouchTargetModifierNode :
    Modifier.Node(),
    LayoutModifierNode {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val size = minimumTouchTargetSize
        val placeable = measurable.measure(constraints)
        val enforcement = isAttached

        // Be at least as big as the minimum dimension in both dimensions
        val width = if (enforcement) {
            maxOf(placeable.width, size.width.roundToPx())
        } else {
            placeable.width
        }
        val height = if (enforcement) {
            maxOf(placeable.height, size.height.roundToPx())
        } else {
            placeable.height
        }

        return layout(width, height) {
            val centerX = ((width - placeable.width) / 2f).roundToInt()
            val centerY = ((height - placeable.height) / 2f).roundToInt()
            placeable.place(centerX, centerY)
        }
    }
}

private val minimumTouchTargetSize = DpSize(44.dp, 44.dp)
