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
package com.adevinta.spark.catalog.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import kotlin.math.pow

/**
 * Draws a vertical gradient scrim in the foreground.
 *
 * @param color The color of the gradient scrim.
 * @param decay The exponential decay to apply to the gradient. Defaults to `3.0f` which is
 * a cubic decay.
 * @param numStops The number of color stops to draw in the gradient. Higher numbers result in
 * the higher visual quality at the cost of draw performance. Defaults to `16`.
 */
internal fun Modifier.drawForegroundGradientScrim(
    color: Color,
    decay: Float = 3f,
    numStops: Int = 16,
    startY: Float = 0f,
    endY: Float = 1f,
): Modifier = composed {
    val colors = remember(color, numStops) {
        val baseAlpha = color.alpha
        List(numStops) { i ->
            val x = i * 1f / (numStops - 1)
            val opacity = x.pow(decay)
            color.copy(alpha = baseAlpha * opacity)
        }
    }

    drawWithContent {
        drawContent()
        drawRect(
            topLeft = Offset(x = 0f, y = startY * size.height),
            size = size.copy(height = (endY - startY) * size.height),
            brush = Brush.verticalGradient(colors = colors),
        )
    }
}

@Preview
@Composable
private fun GradientScrimPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier.size(100.dp)
                .background(SparkTheme.colors.surfaceInverse)
                .drawForegroundGradientScrim(SparkTheme.colors.surface),
        )
        Box(
            modifier = Modifier.size(100.dp)
                .background(SparkTheme.colors.surfaceInverse)
                .drawForegroundGradientScrim(
                    color = SparkTheme.colors.surface,
                    decay = 1.5f,
                ),
        )
    }
}
