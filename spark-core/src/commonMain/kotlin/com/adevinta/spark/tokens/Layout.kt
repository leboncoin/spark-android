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
package com.adevinta.spark.tokens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrain
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text

public object Layout {

    public val windowSize: WindowSizeClass
        @Composable
        @ReadOnlyComposable
        get() = LocalWindowSizeClass.current

    public val bodyMargin: Dp
        @Composable get() = when {
            windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> 64.dp
            windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> 32.dp
            else -> 16.dp
        }

    public val gutter: Dp
        @Composable get() =
            when {
                windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> 24.dp
                windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> 16.dp
                else -> 8.dp
            }

    public val columns: Int
        @Composable get() =
            when {
                windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> 12
                windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> 8
                else -> 4
            }

    public val bodyMaxWidth: Dp
        @Composable get() = computeBodyMaxWidth(windowSize)
}

private fun computeBodyMaxWidth(windowSize: WindowSizeClass): Dp = when {
    // Not yet available in CMP
//    windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXTRA_LARGE_LOWER_BOUND) -> 1040.dp
//    windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND) -> 840.dp
    windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> 840.dp

    windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> Dp.Infinity

    else -> Dp.Infinity
}

/**
 * Support wide screen by making the content width max based on window size class, centered horizontally.
 */
@Suppress("ComposeModifierComposed") // WindowInsets.systemBars is a composable getter
public fun Modifier.bodyWidth(): Modifier = fillMaxWidth()
    .wrapContentWidth(align = Alignment.CenterHorizontally)
    .then(BodyMaxWidthElement)
    .composed {
        windowInsetsPadding(
            WindowInsets.systemBars
                .only(WindowInsetsSides.Horizontal),
        )
    }

private data object BodyMaxWidthElement : ModifierNodeElement<BodyMaxWidthNode>() {
    override fun create(): BodyMaxWidthNode = BodyMaxWidthNode()

    override fun update(node: BodyMaxWidthNode) = Unit

    override fun InspectorInfo.inspectableProperties() {
        name = "bodyMaxWidth"
    }
}

/**
 * Limits the width to [Layout.bodyMaxWidth], as `widthIn(max = Layout.bodyMaxWidth)` did, and passes the
 * constraints through when that width is infinite. It reads [LocalWindowSizeClass] in the measure phase, so a
 * change of the window size class invalidates the measurement without a recomposition.
 */
private class BodyMaxWidthNode :
    Modifier.Node(),
    LayoutModifierNode,
    CompositionLocalConsumerModifierNode {

    // The max width in pixels, or null when the max width is infinite.
    private fun Density.maxWidthPx(): Int? {
        val maxWidth = computeBodyMaxWidth(currentValueOf(LocalWindowSizeClass))
        return if (maxWidth.isFinite) maxWidth.roundToPx().coerceAtLeast(0) else null
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val maxWidthPx = maxWidthPx()
        val childConstraints = if (maxWidthPx != null) {
            constraints.constrain(Constraints(maxWidth = maxWidthPx))
        } else {
            constraints
        }
        val placeable = measurable.measure(childConstraints)
        return layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurable: IntrinsicMeasurable, height: Int): Int =
        constrainIntrinsicWidth(measurable.minIntrinsicWidth(height))

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurable: IntrinsicMeasurable, height: Int): Int =
        constrainIntrinsicWidth(measurable.maxIntrinsicWidth(height))

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurable: IntrinsicMeasurable, width: Int): Int =
        measurable.minIntrinsicHeight(width)

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurable: IntrinsicMeasurable, width: Int): Int =
        measurable.maxIntrinsicHeight(width)

    private fun Density.constrainIntrinsicWidth(width: Int): Int {
        val maxWidthPx = maxWidthPx() ?: return width
        return width.coerceIn(0, maxWidthPx)
    }
}

@Composable
@PreviewScreenSizes
internal fun LayoutPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        val columns = Layout.columns
        val bodyMargin = Layout.bodyMargin
        val gutter = Layout.gutter

        Box(Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns / 4),
                contentPadding = PaddingValues(
                    horizontal = bodyMargin,
                    vertical = gutter,
                ),
                horizontalArrangement = Arrangement.spacedBy(gutter),
                verticalArrangement = Arrangement.spacedBy(gutter),
                modifier = Modifier
                    .bodyWidth()
                    .fillMaxHeight()
                    .background(SparkTheme.colors.mainContainer),
            ) {
                items(
                    count = 20,
                ) {
                    Item()
                }
            }
        }
    }
}

@Composable
private fun Item() {
    Surface(
        modifier = Modifier
            .aspectRatio(2 / 3f),
        color = SparkTheme.colors.main,
        shape = SparkTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "",
                style = SparkTheme.typography.body2,
                textAlign = TextAlign.Center,
            )
        }
    }
}
