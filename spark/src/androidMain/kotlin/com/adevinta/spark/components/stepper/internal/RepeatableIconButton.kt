/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.components.stepper.internal

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.adevinta.spark.components.textfields.DefaultSparkTextFieldColors
import com.adevinta.spark.icons.SparkIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

internal const val INITIAL_DELAY_MS = 400L
internal const val MIN_DELAY_MS = 50L
internal const val DECAY_MS = 30L

/**
 * While [interactionSource] reports a held press and [enabled] is true, repeats [onClick] with an
 * accelerating delay and haptic feedback. Stops on release, disable, or leaving composition.
 */
@Composable
internal fun RepeatOnLongPress(
    interactionSource: InteractionSource,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentOnClick by rememberUpdatedState(onClick)
    val currentEnabled by rememberUpdatedState(enabled)

    LaunchedEffect(interactionSource) {
        snapshotFlow { isPressed && currentEnabled }.collectLatest { active ->
            if (!active) return@collectLatest
            var currentDelay = INITIAL_DELAY_MS
            delay(currentDelay)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            while (true) {
                currentOnClick()
                currentDelay = (currentDelay - DECAY_MS).coerceAtLeast(MIN_DELAY_MS)
                delay(currentDelay)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
            }
        }
    }
}

@Composable
internal fun RepeatableIconButton(
    sparkIcon: SparkIcon,
    contentDescription: String,
    enabled: Boolean,
    colors: DefaultSparkTextFieldColors,
    shape: CornerBasedShape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val hapticFeedback = LocalHapticFeedback.current
    val internalInteractionSource = remember { MutableInteractionSource() }

    RepeatOnLongPress(
        interactionSource = internalInteractionSource,
        enabled = enabled,
        onClick = onClick,
    )

    IconButton(
        sparkIcon = sparkIcon,
        contentDescription = contentDescription,
        enabled = enabled,
        colors = colors,
        shape = shape,
        modifier = modifier,
        interactionSource = internalInteractionSource,
        onClick = {
            onClick()
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        },
    )
}
