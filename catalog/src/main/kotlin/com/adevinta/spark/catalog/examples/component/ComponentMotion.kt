/*
 * Copyright (c) 2026 Adevinta
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
package com.adevinta.spark.catalog.examples.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first

/**
 * Bounds of the component card to component screen transform, shared by the container and its
 * title so that both arrive together. A straight path rather than an arc: an arc moves each corner
 * of a rectangle on its own curve, so the title drifted away from the card it belongs to.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
internal val ComponentBoundsTransform = BoundsTransform { _, _ ->
    tween(durationMillis = 500, easing = FastOutSlowInEasing)
}

/**
 * Bounds of the example item to example screen transform.
 */
internal val ComponentContentSpring: SpringSpec<Rect> = spring(dampingRatio = .8f, stiffness = 380f)

/**
 * Slide of the example items on the component screen. [staggerIndex] delays the slide by 50 ms per
 * item, capped at 5 steps so that a long list does not queue.
 */
internal fun componentSlideSpec(staggerIndex: Int = 0): FiniteAnimationSpec<IntOffset> =
    tween(durationMillis = 500, delayMillis = minOf(staggerIndex, 5) * 50)

/**
 * Returns `true` once this destination has reached [EnterExitState.Visible] a first time, so that
 * entrance animations do not replay when the user comes back to it.
 */
@Composable
internal fun AnimatedVisibilityScope.rememberHasEntered(): Boolean {
    var hasEntered by rememberSaveable { mutableStateOf(false) }
    if (!hasEntered) {
        LaunchedEffect(transition) {
            snapshotFlow { transition.currentState }.first { it == EnterExitState.Visible }
            hasEntered = true
        }
    }
    return hasEntered
}

/**
 * Room around the clipped bounds for the shadow of an elevated card: twice the pressed elevation
 * (Level3, 6 dp), which covers the blur and the downward offset of the shadow.
 */
private val ShadowOutset: Dp = 12.dp

/**
 * Reads the animated [radius] when the overlay clip path is built, in the draw phase, so the
 * screens do not recompose on every frame of the shared transition.
 *
 * The clip is [ShadowOutset] larger than the bounds on every side, with a radius that grows by
 * the same amount, so the shadow of the card keeps its soft edge while the card is drawn in the
 * overlay. The content itself stays bounded by its own layout and shape.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
internal class AnimatedRoundedClip(private val radius: () -> Dp) : SharedTransitionScope.OverlayClip {
    private val path = Path()

    override fun getClipPath(
        sharedContentState: SharedTransitionScope.SharedContentState,
        bounds: Rect,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Path {
        val outset = with(density) { ShadowOutset.toPx() }
        val cornerRadius = with(density) { radius().toPx() } + outset
        path.rewind()
        path.addRoundRect(RoundRect(bounds.inflate(outset), CornerRadius(cornerRadius)))
        return path
    }
}
