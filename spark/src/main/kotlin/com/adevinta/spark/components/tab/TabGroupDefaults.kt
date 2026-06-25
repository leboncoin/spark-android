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

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

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
    @Suppress("ComposeModifierComposed") // Fork from M3 Tab that will be removed once the api is stable
    internal fun Modifier.tabIndicatorOffset(
        currentTabPosition: TabPosition,
    ): Modifier =
        composed(
            inspectorInfo =
            debugInspectorInfo {
                name = "tabIndicatorOffset"
                value = currentTabPosition
            },
        ) {
            val currentTabWidth by
                animateDpAsState(
                    targetValue = currentTabPosition.width,
                    animationSpec = TabRowIndicatorSpec,
                )
            val indicatorOffset by
                animateDpAsState(
                    targetValue = currentTabPosition.left,
                    animationSpec = TabRowIndicatorSpec,
                )
            fillMaxWidth()
                .wrapContentSize(Alignment.BottomStart)
                .offset { IntOffset(x = indicatorOffset.roundToPx(), y = 0) }
                .width(currentTabWidth)
        }
}
