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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
 */
@Composable
public fun AnimatedCounterText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
    textAlign: TextAlign = TextAlign.Center,
) {
    Row(
        modifier = modifier.animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        text.mapIndexed { index, char ->
            AnimatedDigit(char = char, fullText = text, index = index)
        }.forEach { digit ->
            AnimatedContent(
                targetState = digit,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { -it } + fadeIn() togetherWith
                            slideOutVertically { it } + fadeOut()
                    } else {
                        slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "AnimatedDigit",
            ) { target ->
                Text(
                    text = "${target.char}",
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

private data class AnimatedDigit(val char: Char, val fullText: String, val index: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is AnimatedDigit) return false
        return char == other.char
    }

    override fun hashCode(): Int = char.hashCode()
}

private operator fun AnimatedDigit.compareTo(other: AnimatedDigit): Int = fullText.extractNumber().compareTo(
    other.fullText.extractNumber(),
)

private fun String.extractNumber(): Double = filter { it.isDigit() || it == '.' || it == '-' }
    .toDoubleOrNull() ?: 0.0
