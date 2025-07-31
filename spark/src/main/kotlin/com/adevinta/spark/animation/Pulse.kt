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
package com.adevinta.spark.animation

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.tokens.dim3

/**
 * Applies a pulsating effect that is drawn behind the composable element. This effect creates
 * a visual appearance where the background pulse scales up and fades out in a loop, simulating
 * a pulsating effect.
 *
 * @param targetScale The scale to which the pulse effect will grow during the animation.
 * @param initialScale The starting scale of the pulse effect.
 * @param color The color used to fill the pulse effect.
 * @param shape The shape of the pulse effect.
 * @param animationSpec The animation specification.
 *
 * @return A [Modifier] that applies the pulsating effect behind the composable element.
 */
@Composable
@ExperimentalSparkApi
public fun Modifier.pulse(
    targetScale: Float = 1.5f,
    initialScale: Float = 1f,
    color: Color = SparkTheme.colors.onSurface.dim3,
    shape: Shape = CircleShape,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(1200),
): Modifier = pulse(
    brush = SolidColor(color),
    targetScale = targetScale,
    initialScale = initialScale,
    shape = shape,
    animationSpec = animationSpec,
)

/**
 * Applies a pulsating effect that is drawn behind the composable element. This effect creates
 * a visual appearance where the background pulse scales up and fades out in a loop, simulating
 * a pulsating effect.
 *
 * @param targetScale The scale to which the pulse effect will grow during the animation.
 * @param initialScale The starting scale of the pulse effect.
 * @param brush The brush used to fill the pulse effect.
 * @param shape The shape of the pulse effect.
 * @param animationSpec The animation specification.
 *
 * @return A [Modifier] that applies the pulsating effect behind the composable element.
 */
@Composable
@ExperimentalSparkApi
public fun Modifier.pulse(
    brush: Brush,
    targetScale: Float = 1.5f,
    initialScale: Float = 1f,
    shape: Shape = CircleShape,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(1200),
): Modifier {
    val pulseTransition = rememberInfiniteTransition("PulseTransition")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = initialScale,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(animationSpec),
        label = "PulseScale",
    )
    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animationSpec),
        label = "PulseAlpha",
    )
    return this.drawBehind {
        val outline = shape.createOutline(size, layoutDirection, this)
        scale(pulseScale) {
            drawOutline(outline, brush, pulseAlpha)
        }
    }
}

@Composable
@Preview
private fun Pulser() {
    PreviewTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            var isValid by remember { mutableStateOf(true) }

            val intent = if (isValid) ButtonIntent.Success else ButtonIntent.Danger
            val pulseColor = if (isValid) SparkTheme.colors.successContainer else SparkTheme.colors.errorContainer
            ButtonTinted(
                modifier = Modifier
                    .align(Alignment.Center)
                    .pulse(
                        color = pulseColor,
                        shape = SparkTheme.shapes.large,
                        targetScale = 1.5f,
                    ),
                onClick = {
                    isValid = !isValid
                },
                intent = intent,
                text = "Vibing",
            )
        }
    }
}
