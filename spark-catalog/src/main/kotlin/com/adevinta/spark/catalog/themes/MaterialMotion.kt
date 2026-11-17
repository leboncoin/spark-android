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
package com.adevinta.spark.catalog.themes

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

/*
 * Material motion patterns (shared axis, fade through, fade and elevation scale) built on Compose
 * animation primitives. The values follow the Material motion specification and match the
 * `io.github.fornewid:material-motion-compose-core` 2.0.1 transitions (Apache 2.0) that the
 * catalog used before.
 */

private const val MotionDuration = 300
private const val FadeInDuration = 150
private const val FadeOutDuration = 75

/** Part of a sequential transition that the outgoing content uses. */
private const val ProgressThreshold = 0.35f
private const val FadeEndThreshold = 0.3f

private val Int.forOutgoing: Int get() = (this * ProgressThreshold).toInt()
private val Int.forIncoming: Int get() = this - forOutgoing

internal fun materialSharedAxisXIn(forward: Boolean, slideDistance: Int): EnterTransition =
    slideInHorizontally(tween(MotionDuration, easing = FastOutSlowInEasing)) {
        if (forward) slideDistance else -slideDistance
    } + sharedAxisFadeIn()

internal fun materialSharedAxisXOut(forward: Boolean, slideDistance: Int): ExitTransition =
    slideOutHorizontally(tween(MotionDuration, easing = FastOutSlowInEasing)) {
        if (forward) -slideDistance else slideDistance
    } + sharedAxisFadeOut()

internal fun materialSharedAxisYIn(forward: Boolean, slideDistance: Int): EnterTransition =
    slideInVertically(tween(MotionDuration, easing = FastOutSlowInEasing)) {
        if (forward) slideDistance else -slideDistance
    } + sharedAxisFadeIn()

internal fun materialSharedAxisYOut(forward: Boolean, slideDistance: Int): ExitTransition =
    slideOutVertically(tween(MotionDuration, easing = FastOutSlowInEasing)) {
        if (forward) -slideDistance else slideDistance
    } + sharedAxisFadeOut()

internal fun materialSharedAxisZIn(forward: Boolean): EnterTransition = sharedAxisFadeIn() +
    scaleIn(
        animationSpec = tween(MotionDuration, easing = FastOutSlowInEasing),
        initialScale = if (forward) 0.8f else 1.1f,
    )

internal fun materialSharedAxisZOut(forward: Boolean): ExitTransition = sharedAxisFadeOut() +
    scaleOut(
        animationSpec = tween(MotionDuration, easing = FastOutSlowInEasing),
        targetScale = if (forward) 1.1f else 0.8f,
    )

internal fun materialFadeThroughIn(initialScale: Float = 0.92f): EnterTransition = sharedAxisFadeIn() +
    scaleIn(
        animationSpec = tween(
            durationMillis = MotionDuration.forIncoming,
            delayMillis = MotionDuration.forOutgoing,
            easing = LinearOutSlowInEasing,
        ),
        initialScale = initialScale,
    )

internal fun materialFadeThroughOut(): ExitTransition = sharedAxisFadeOut()

internal fun materialFadeIn(): EnterTransition =
    fadeIn(tween(durationMillis = (FadeInDuration * FadeEndThreshold).toInt(), easing = LinearEasing)) +
        scaleIn(
            animationSpec = tween(FadeInDuration, easing = FastOutSlowInEasing),
            initialScale = 0.8f,
        )

internal fun materialFadeOut(): ExitTransition = fadeOut(tween(FadeOutDuration, easing = LinearEasing))

internal fun materialElevationScaleIn(
    initialAlpha: Float = 0.85f,
    initialScale: Float = 0.85f,
): EnterTransition = fadeIn(
    animationSpec = tween(MotionDuration, easing = LinearEasing),
    initialAlpha = initialAlpha,
) + scaleIn(
    animationSpec = tween(MotionDuration, easing = FastOutSlowInEasing),
    initialScale = initialScale,
)

private fun sharedAxisFadeIn(): EnterTransition = fadeIn(
    tween(
        durationMillis = MotionDuration.forIncoming,
        delayMillis = MotionDuration.forOutgoing,
        easing = LinearOutSlowInEasing,
    ),
)

private fun sharedAxisFadeOut(): ExitTransition = fadeOut(
    tween(
        durationMillis = MotionDuration.forOutgoing,
        delayMillis = 0,
        easing = FastOutLinearInEasing,
    ),
)
