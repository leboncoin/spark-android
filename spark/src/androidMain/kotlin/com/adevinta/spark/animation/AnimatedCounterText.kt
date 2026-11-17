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
package com.adevinta.spark.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import com.adevinta.spark.components.text.Text

/**
 * A text composable that animates value changes with a per-character vertical slide transition.
 *
 * Each character animates independently: unchanged characters stay still, while changed
 * characters slide in from the top (increasing) or bottom (decreasing). This produces an
 * odometer/ticker effect where only the digits that change are animated.
 *
 * @param text The current display text (e.g. "70%", "€120").
 * @param modifier Modifier applied to the row container.
 * @param style Text style for rendering each character.
 * @param color Text color.
 * @param textAlign Text alignment within the available space.
 * @param animationSpec Slide animation spec for each character transition.
 */
@Composable
public fun AnimatedCounterText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
    textAlign: TextAlign = TextAlign.Center,
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(visibilityThreshold = IntOffset.VisibilityThreshold),
) {
    val number = text.extractNumber()
    var previousNumber by remember { mutableDoubleStateOf(number) }
    val increasing = number >= previousNumber
    SideEffect { previousNumber = number }

    Row(
        modifier = modifier.animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        text.forEachIndexed { index, char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (increasing) {
                        slideInVertically(animationSpec) { -it } + fadeIn() togetherWith
                            slideOutVertically(animationSpec) { it } + fadeOut()
                    } else {
                        slideInVertically(animationSpec) { it } + fadeIn() togetherWith
                            slideOutVertically(animationSpec) { -it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "AnimatedDigit",
            ) { target ->
                Text(
                    text = "$target",
                    style = style,
                    color = color,
                    textAlign = textAlign,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private fun String.extractNumber(): Double = filter { it.isDigit() || it == '.' || it == '-' }
    .toDoubleOrNull() ?: 0.0
