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

import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tools.modifiers.ifTrue
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Applies a pulsating effect that is drawn behind the composable element. This effect creates
 * a visual appearance where the background pulse scales up and fades out in a loop, simulating
 * a pulsating effect.
 *
 * @param targetScale The scale to which the pulse effect will grow during the animation.
 * @param initialScale The starting scale of the pulse effect. Default to the component size. If you want to draw the
 * pulse on a component with a transparent background then you should set this to be inferior than 1f otherwise you
 * will get a flashing effect at the start of each pulse
 * @param color The color used to fill the pulse effect.
 * @param shape The shape of the pulse effect.
 * @param animationSpec The animation specification.
 *
 * @return A [Modifier] that applies the pulsating effect behind the composable element.
 */
@Composable
@ExperimentalSparkApi
public fun Modifier.pulse(
    enabled: Boolean = true,
    targetScale: Float = 1.5f,
    initialScale: Float = 1f,
    color: Color = SparkTheme.colors.onSurface.dim3,
    shape: Shape = CircleShape,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(1000),
): Modifier =
    pulse(
        enabled = enabled,
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
    enabled: Boolean = true,
    targetScale: Float = 1.5f,
    initialScale: Float = 1f,
    shape: Shape = CircleShape,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(1000),
): Modifier = ifTrue(enabled) {
    this then PulseElement(
        brush = brush,
        targetScale = targetScale,
        initialScale = initialScale,
        shape = shape,
        animationSpec = animationSpec,
    )
}

private data class PulseElement(
    val brush: Brush,
    val targetScale: Float,
    val initialScale: Float,
    val shape: Shape,
    val animationSpec: DurationBasedAnimationSpec<Float>,
) : ModifierNodeElement<PulseNode>() {
    override fun create(): PulseNode = PulseNode(brush, targetScale, initialScale, shape, animationSpec)

    override fun update(node: PulseNode) {
        node.update(brush, targetScale, initialScale, shape, animationSpec)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "pulse"
        properties["brush"] = brush
        properties["targetScale"] = targetScale
        properties["initialScale"] = initialScale
        properties["shape"] = shape
        properties["animationSpec"] = animationSpec
    }
}

/**
 * Draws the pulse behind the content. The frame loop follows the former `rememberInfiniteTransition` with two
 * `animateFloat` children: both values share one start time, a change of the scale values restarts only the
 * scale animation, a change of the spec alone has no effect, and a zero duration scale shows the end values.
 */
private class PulseNode(
    private var brush: Brush,
    private var targetScale: Float,
    private var initialScale: Float,
    private var shape: Shape,
    private var animationSpec: DurationBasedAnimationSpec<Float>,
) : Modifier.Node(),
    DrawModifierNode {

    private lateinit var scaleAnimation: PulseAnimation
    private lateinit var alphaAnimation: PulseAnimation

    override val shouldAutoInvalidate: Boolean = false

    fun update(
        brush: Brush,
        targetScale: Float,
        initialScale: Float,
        shape: Shape,
        animationSpec: DurationBasedAnimationSpec<Float>,
    ) {
        val scaleChanged = initialScale != this.initialScale || targetScale != this.targetScale
        this.brush = brush
        this.targetScale = targetScale
        this.initialScale = initialScale
        this.shape = shape
        this.animationSpec = animationSpec
        if (scaleChanged && isAttached) {
            scaleAnimation.updateValues(initialScale, targetScale, animationSpec)
        }
        invalidateDraw()
    }

    override fun onAttach() {
        scaleAnimation = PulseAnimation(initialScale, targetScale, animationSpec)
        alphaAnimation = PulseAnimation(1f, 0f, animationSpec)
        coroutineScope.launch { runPulse() }
    }

    private suspend fun runPulse() {
        val context = currentCoroutineContext()
        var startTimeNanos = AnimationConstants.UnspecifiedTime
        var durationScale = 1f
        while (true) {
            withInfiniteAnimationFrameNanos { frameTimeNanos ->
                val currentDurationScale = context.durationScale
                if (startTimeNanos == AnimationConstants.UnspecifiedTime || durationScale != currentDurationScale) {
                    startTimeNanos = frameTimeNanos
                    scaleAnimation.reset()
                    alphaAnimation.reset()
                    durationScale = currentDurationScale
                }
                if (durationScale == 0f) {
                    scaleAnimation.skipToEnd()
                    alphaAnimation.skipToEnd()
                } else {
                    val playTimeNanos = ((frameTimeNanos - startTimeNanos) / durationScale).toLong()
                    scaleAnimation.onPlayTimeChanged(playTimeNanos)
                    alphaAnimation.onPlayTimeChanged(playTimeNanos)
                }
                invalidateDraw()
            }
            if (durationScale == 0f) {
                snapshotFlow { context.durationScale }.first { it > 0f }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val outline = shape.createOutline(size, layoutDirection, this)
        val alpha = alphaAnimation.value
        scale(scaleAnimation.value) {
            drawOutline(outline, brush, alpha)
        }
        drawContent()
    }
}

private val CoroutineContext.durationScale: Float
    get() {
        val scale = this[MotionDurationScale]?.scaleFactor ?: 1f
        check(scale >= 0f) { "negative scale factor" }
        return scale
    }

/**
 * Same timing as `InfiniteTransition.TransitionAnimationState`: a new animation starts from play time 0 on the
 * next frame.
 */
private class PulseAnimation(
    initialValue: Float,
    targetValue: Float,
    animationSpec: DurationBasedAnimationSpec<Float>,
) {
    private var animation = createAnimation(initialValue, targetValue, animationSpec)
    private var startOnTheNextFrame = false
    private var playTimeNanosOffset = 0L

    var value: Float = initialValue
        private set

    fun updateValues(initialValue: Float, targetValue: Float, animationSpec: DurationBasedAnimationSpec<Float>) {
        animation = createAnimation(initialValue, targetValue, animationSpec)
        startOnTheNextFrame = true
    }

    fun onPlayTimeChanged(playTimeNanos: Long) {
        if (startOnTheNextFrame) {
            startOnTheNextFrame = false
            playTimeNanosOffset = playTimeNanos
        }
        value = animation.getValueFromNanos(playTimeNanos - playTimeNanosOffset)
    }

    fun skipToEnd() {
        value = animation.targetValue
        startOnTheNextFrame = true
    }

    fun reset() {
        startOnTheNextFrame = true
    }

    private fun createAnimation(
        initialValue: Float,
        targetValue: Float,
        animationSpec: DurationBasedAnimationSpec<Float>,
    ) = TargetBasedAnimation(
        animationSpec = infiniteRepeatable(animationSpec),
        typeConverter = Float.VectorConverter,
        initialValue = initialValue,
        targetValue = targetValue,
    )
}

@Composable
@Preview
private fun Pulser() {
    PreviewTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            var isValid by remember { mutableStateOf(true) }

            val pulseColor = if (isValid) SparkTheme.colors.successContainer else SparkTheme.colors.errorContainer
            val modifier = Modifier
                .align(Alignment.Center)
                .pulse(
                    color = pulseColor,
                    shape = SparkTheme.shapes.large,
                    targetScale = 1.5f,
                )
            val onClick = { isValid = !isValid }
            if (isValid) {
                Button.Success(onClick = onClick, text = "Vibing", modifier = modifier)
            } else {
                Button.Danger(onClick = onClick, text = "Vibing", modifier = modifier)
            }
        }
    }
}
