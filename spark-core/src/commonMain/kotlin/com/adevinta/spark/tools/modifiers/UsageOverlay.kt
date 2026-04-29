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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.LocalSparkFeatureFlag
import com.adevinta.spark.SparkTheme

/**
 * A [Modifier.Element] that adds a draw layer to identify spark components easily.
 */
@InternalSparkApi
@Stable
public fun Modifier.sparkUsageOverlay(overlayColor: Color = Color.Red): Modifier =
    this then SparkUsageOverlayElement(overlayColor)

private class SparkUsageOverlay(var overlayColor: Color = Color.Red) :
    Modifier.Node(),
    DrawModifierNode,
    CompositionLocalConsumerModifierNode {
    override fun ContentDrawScope.draw() {
        drawContent()
        if (currentValueOf(LocalSparkFeatureFlag).useSparkComponentsHighlighter) {
            drawRect(color = overlayColor, alpha = 0.5f)
        }
    }
}

private class SparkUsageOverlayElement(val overlayColor: Color) : ModifierNodeElement<SparkUsageOverlay>() {
    override fun create() = SparkUsageOverlay(overlayColor)
    override fun update(node: SparkUsageOverlay) {
        node.overlayColor = overlayColor
    }
    override fun hashCode() = System.identityHashCode(this)
    override fun equals(other: Any?) = (other === this)
    override fun InspectorInfo.inspectableProperties() {
        name = "Spark Component"
    }
}

/**
 * A Composable that hides its content and draw a dashed border to identify slot areas.
 */
@InternalSparkApi
@Composable
public fun SlotArea(
    shape: Shape = SparkTheme.shapes.extraLarge,
    color: Color = Color.Red,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .dashedBorder(width = 1.dp, shape = shape, color = color)
            .drawWithContent { /* hide the content */ },
        content = content,
    )
}
