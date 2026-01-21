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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.tokens.ElevationTokens
import com.adevinta.spark.tokens.animateElevation

@Composable
internal fun animateStepElevation(
    enabled: Boolean,
    style: ProgressStyles,
    interactionSource: InteractionSource,
): State<Dp> {
    if (style == ProgressStyles.Outlined) {
        return remember { mutableStateOf(ElevationTokens.Level0) }
    }
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is HoverInteraction.Enter -> {
                    interactions.add(interaction)
                }

                is HoverInteraction.Exit -> {
                    interactions.remove(interaction.enter)
                }

                is FocusInteraction.Focus -> {
                    interactions.add(interaction)
                }

                is FocusInteraction.Unfocus -> {
                    interactions.remove(interaction.focus)
                }

                is PressInteraction.Press -> {
                    interactions.add(interaction)
                }

                is PressInteraction.Release -> {
                    interactions.remove(interaction.press)
                }

                is PressInteraction.Cancel -> {
                    interactions.remove(interaction.press)
                }
            }
        }
    }

    val interaction = interactions.lastOrNull()

    val target = if (!enabled) {
        0.dp
    } else {
        when (interaction) {
            is PressInteraction.Press -> ElevationTokens.Level2
            is HoverInteraction.Enter -> ElevationTokens.Level2
            is FocusInteraction.Focus -> ElevationTokens.Level2
            else -> ElevationTokens.Level0
        }
    }

    val animatable = remember { Animatable(target, Dp.VectorConverter) }

    if (!enabled) {
        // No transition when moving to a disabled state
        LaunchedEffect(target) { animatable.snapTo(target) }
    } else {
        LaunchedEffect(target) {
            val lastInteraction = when (animatable.targetValue) {
                ElevationTokens.Level1 -> PressInteraction.Press(Offset.Zero)
                ElevationTokens.Level2 -> FocusInteraction.Focus()
                else -> null
            }
            animatable.animateElevation(
                from = lastInteraction,
                to = interaction,
                target = target,
            )
        }
    }

    return animatable.asState()
}
