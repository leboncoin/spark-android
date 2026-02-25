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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Contains predefined [Dp] values for elevation levels.
 */
public object ElevationTokens {
    public val Level0: Dp = 0.0.dp
    public val Level1: Dp = 1.0.dp
    public val Level2: Dp = 3.0.dp
    public val Level3: Dp = 6.0.dp
    public val Level4: Dp = 8.0.dp
    public val Level5: Dp = 12.0.dp
}

/**
 * Animates the [Dp] value of [this] between [from] and [to] [Interaction]s, to [target]. The
 * [AnimationSpec] used depends on the values for [from] and [to], see
 * [ElevationDefaults.incomingAnimationSpecForInteraction] and
 * [ElevationDefaults.outgoingAnimationSpecForInteraction] for more details.
 *
 * @param target the [Dp] target elevation for this component, corresponding to the elevation
 * desired for the [to] state.
 * @param from the previous [Interaction] that was used to calculate elevation. `null` if there
 * was no previous [Interaction], such as when the component is in its default state.
 * @param to the [Interaction] that this component is moving to, such as [PressInteraction.Press]
 * when this component is being pressed. `null` if this component is moving back to its default
 * state.
 */
internal suspend fun Animatable<Dp, *>.animateElevation(
    target: Dp,
    from: Interaction? = null,
    to: Interaction? = null,
) {
    val spec = when {
        // Moving to a new state
        to != null -> ElevationDefaults.incomingAnimationSpecForInteraction(to)

        // Moving to default, from a previous state
        from != null -> ElevationDefaults.outgoingAnimationSpecForInteraction(from)

        // Loading the initial state, or moving back to the baseline state from a disabled /
        // unknown state, so just snap to the final value.
        else -> null
    }
    if (spec != null) animateTo(target, spec) else snapTo(target)
}

/**
 * Contains default [AnimationSpec]s used for animating elevation between different [Interaction]s.
 *
 * Typically you should use [animateElevation] instead, which uses these [AnimationSpec]s
 * internally. [animateElevation] in turn is used by the defaults for cards and buttons.
 *
 * @see animateElevation
 */
private object ElevationDefaults {
    /**
     * Returns the [AnimationSpec]s used when animating elevation to [interaction], either from a
     * previous [Interaction], or from the default state. If [interaction] is unknown, then
     * returns `null`.
     *
     * @param interaction the [Interaction] that is being animated to
     */
    fun incomingAnimationSpecForInteraction(interaction: Interaction): AnimationSpec<Dp>? = when (interaction) {
        is PressInteraction.Press -> DefaultIncomingSpec
        is DragInteraction.Start -> DefaultIncomingSpec
        is HoverInteraction.Enter -> DefaultIncomingSpec
        is FocusInteraction.Focus -> DefaultIncomingSpec
        else -> null
    }

    /**
     * Returns the [AnimationSpec]s used when animating elevation away from [interaction], to the
     * default state. If [interaction] is unknown, then returns `null`.
     *
     * @param interaction the [Interaction] that is being animated away from
     */
    fun outgoingAnimationSpecForInteraction(interaction: Interaction): AnimationSpec<Dp>? = when (interaction) {
        is PressInteraction.Press -> DefaultOutgoingSpec
        is DragInteraction.Start -> DefaultOutgoingSpec
        is HoverInteraction.Enter -> HoveredOutgoingSpec
        is FocusInteraction.Focus -> DefaultOutgoingSpec
        else -> null
    }
}

private val OutgoingSpecEasing: Easing = CubicBezierEasing(0.40f, 0.00f, 0.60f, 1.00f)

private val DefaultIncomingSpec = TweenSpec<Dp>(
    durationMillis = 120,
    easing = FastOutSlowInEasing,
)

private val DefaultOutgoingSpec = TweenSpec<Dp>(
    durationMillis = 150,
    easing = OutgoingSpecEasing,
)

private val HoveredOutgoingSpec = TweenSpec<Dp>(
    durationMillis = 120,
    easing = OutgoingSpecEasing,
)
