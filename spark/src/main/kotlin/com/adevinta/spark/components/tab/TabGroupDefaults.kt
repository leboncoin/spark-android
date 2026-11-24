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
package com.adevinta.spark.components.tab

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrain
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Contains default implementations and values used for TabRow.
 */
internal object TabGroupDefaults {
    /** Minimum width of a tab */
    val minTabWidth = 48.dp

    /**
     * Default indicator, which will be positioned at the bottom of the [TabGroup], on top of the
     * divider.
     *
     * @param modifier modifier for the indicator's layout
     * @param height height of the indicator
     * @param color color of the indicator
     */
    @Composable
    fun Indicator(
        modifier: Modifier = Modifier,
        height: Dp = TabDefaults.ActiveIndicatorHeight,
        color: Color = MainNavigationTabTokens.ActiveIndicatorColor,
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(height)
                .background(color = color),
        )
    }

    /**
     * [AnimationSpec] used when scrolling to a tab that is not fully visible.
     */
    internal val ScrollableTabRowScrollSpec: AnimationSpec<Float> = tween(
        durationMillis = 250,
        easing = FastOutSlowInEasing,
    )

    /** [AnimationSpec] used when an indicator is updating width and/or offset. */
    internal val TabRowIndicatorSpec: AnimationSpec<Dp> =
        tween(durationMillis = 250, easing = FastOutSlowInEasing)

    /**
     * [Modifier] that takes up all the available width inside the [TabGroup], and then animates
     * the offset of the indicator it is applied to, depending on the [currentTabPosition].
     *
     * @param currentTabPosition [TabPosition] of the currently selected tab. This is used to
     * calculate the offset of the indicator this modifier is applied to, as well as its width.
     */
    internal fun Modifier.tabIndicatorOffset(
        currentTabPosition: TabPosition,
    ): Modifier = fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .then(TabIndicatorOffsetElement(currentTabPosition))
}

private data class TabIndicatorOffsetElement(
    val currentTabPosition: TabPosition,
) : ModifierNodeElement<TabIndicatorOffsetNode>() {
    override fun create(): TabIndicatorOffsetNode = TabIndicatorOffsetNode(currentTabPosition)

    override fun update(node: TabIndicatorOffsetNode) {
        node.update(currentTabPosition)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
}

/**
 * Animates the offset and the width of the indicator toward the [TabPosition] of the selected tab. It does the work
 * of the former `offset { }.width(...)` chain with two `animateDpAsState` values. The offset is read in the
 * placement phase and the width in the measure phase, so neither value causes a recomposition.
 */
private class TabIndicatorOffsetNode(
    private var currentTabPosition: TabPosition,
) : Modifier.Node(),
    LayoutModifierNode {

    private var width = Animatable(currentTabPosition.width, Dp.VectorConverter)
    private var left = Animatable(currentTabPosition.left, Dp.VectorConverter)

    override val shouldAutoInvalidate: Boolean = false

    fun update(currentTabPosition: TabPosition) {
        this.currentTabPosition = currentTabPosition
        if (!isAttached) return
        // Like animateDpAsState, start an animation only for the value whose target changed.
        if (currentTabPosition.width != width.targetValue) {
            coroutineScope.launch { width.animateTo(currentTabPosition.width, TabGroupDefaults.TabRowIndicatorSpec) }
        }
        if (currentTabPosition.left != left.targetValue) {
            coroutineScope.launch { left.animateTo(currentTabPosition.left, TabGroupDefaults.TabRowIndicatorSpec) }
        }
    }

    override fun onReset() {
        width = Animatable(currentTabPosition.width, Dp.VectorConverter)
        left = Animatable(currentTabPosition.left, Dp.VectorConverter)
    }

    // Same constraints as Modifier.width(width): a fixed width inside the incoming constraints.
    private fun Density.widthConstraints(): Constraints {
        val widthPx = width.value.roundToPx().coerceAtLeast(0)
        return Constraints(minWidth = widthPx, maxWidth = widthPx)
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints.constrain(widthConstraints()))
        return layout(placeable.width, placeable.height) {
            placeable.placeRelativeWithLayer(x = left.value.roundToPx(), y = 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(measurable: IntrinsicMeasurable, height: Int): Int =
        widthConstraints().maxWidth

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(measurable: IntrinsicMeasurable, height: Int): Int =
        widthConstraints().maxWidth

    override fun IntrinsicMeasureScope.minIntrinsicHeight(measurable: IntrinsicMeasurable, width: Int): Int =
        measurable.minIntrinsicHeight(width)

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(measurable: IntrinsicMeasurable, width: Int): Int =
        measurable.maxIntrinsicHeight(width)
}
