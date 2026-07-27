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
@file:Suppress("ComposeModifierComposed")

package com.adevinta.spark.components.placeholder

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.LayoutDirection
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.contentColorFor

/**
 * Contains default values used by [Modifier.placeholder] and [PlaceholderHighlight].
 */
@Stable
public object PlaceholderDefaults {
    /**
     * The default [InfiniteRepeatableSpec] to use for [fade].
     */
    public val fadeAnimationSpec: InfiniteRepeatableSpec<Float> by lazy {
        infiniteRepeatable(
            animation = tween(delayMillis = 200, durationMillis = 600),
            repeatMode = RepeatMode.Reverse,
        )
    }

    /**
     * The default [InfiniteRepeatableSpec] to use for [shimmer].
     */
    public val shimmerAnimationSpec: InfiniteRepeatableSpec<Float> by lazy {
        infiniteRepeatable(
            animation = tween(durationMillis = 1700, delayMillis = 200),
            repeatMode = RepeatMode.Restart,
        )
    }
}

/**
 * Draws some skeleton UI which is typically used whilst content is 'loading'.
 *
 * To customize the color and shape of the placeholder, you can use the foundation version of
 * [Modifier.placeholder], along with the values provided by [PlaceholderDefaults].
 *
 * A cross-fade transition will be applied to the content and placeholder UI when the [visible]
 * value changes. The transition can be customized via the [contentFadeTransitionSpec] and
 * [placeholderFadeTransitionSpec] parameters.
 *
 * You can provide a [PlaceholderHighlight] which runs an highlight animation on the placeholder.
 * The [shimmer] and [fade] implementations are provided for easy usage.
 *
 * You can find more information on the pattern at the Material Theming
 * [Placeholder UI](https://material.io/design/communication/launch-screen.html#placeholder-ui)
 * guidelines.
 *
 * @param visible whether the placeholder should be visible or not.
 * @param color the color used to draw the placeholder UI. If [Color.Unspecified] is provided,
 * the placeholder will use [PlaceholderDefaults.color].
 * @param shape desired shape of the placeholder. If null is provided the placeholder
 * will use the small shape set in [SparkTheme.shapes].
 * @param highlight optional highlight animation.
 * @param placeholderFadeTransitionSpec The transition spec to use when fading the placeholder
 * on/off screen. The boolean parameter defined for the transition is [visible].
 * @param contentFadeTransitionSpec The transition spec to use when fading the content
 * on/off screen. The boolean parameter defined for the transition is [visible].
 */
internal fun Modifier.placeholder(
    visible: Boolean,
    color: Color,
    shape: Shape,
    highlight: PlaceholderHighlight? = null,
    placeholderFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = {
        spring()
    },
    contentFadeTransitionSpec: @Composable Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float> = { spring() },
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "placeholder"
        value = visible
        properties["visible"] = visible
        properties["color"] = color
        properties["highlight"] = highlight
        properties["shape"] = shape
    },
) {
    // FIXME-scott.rayapoulle.ext (08-40-2024): This Modifier needs to be migrated to the Modifier.Node api
    // Values used for caching purposes
    val lastSize = remember { Ref<Size>() }
    val lastLayoutDirection = remember { Ref<LayoutDirection>() }
    val lastOutline = remember { Ref<Outline>() }

    var highlightProgress: Float by remember { mutableFloatStateOf(0f) }

    val transitionState = remember { MutableTransitionState(visible) }.apply {
        targetState = visible
    }
    val transition = rememberTransition(transitionState, "placeholder_crossfade")

    val placeholderAlpha by transition.animateFloat(
        transitionSpec = placeholderFadeTransitionSpec,
        label = "placeholder_fade",
        targetValueByState = { placeholderVisible -> if (placeholderVisible) 1f else 0f },
    )
    val contentAlpha by transition.animateFloat(
        transitionSpec = contentFadeTransitionSpec,
        label = "content_fade",
        targetValueByState = { placeholderVisible -> if (placeholderVisible) 0f else 1f },
    )

    // Run the optional animation spec and update the progress if the placeholder is visible
    val animationSpec = highlight?.animationSpec
    if (animationSpec != null && (visible || placeholderAlpha >= 0.01f)) {
        val infiniteTransition = rememberInfiniteTransition()
        highlightProgress = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = animationSpec,
            label = "",
        ).value
    }

    val paint = remember { Paint() }
    remember(color, shape, highlight) {
        drawWithContent {
            if (contentAlpha in 0.01f..0.99f) {
                // If the content alpha is between 1% and 99%, draw it in a layer with
                // the alpha applied
                paint.alpha = contentAlpha
                withLayer(paint) {
                    with(this@drawWithContent) {
                        drawContent()
                    }
                }
            } else if (contentAlpha >= 0.99f) {
                // If the content alpha is > 99%, draw it with no alpha
                drawContent()
            }

            if (placeholderAlpha in 0.01f..0.99f) {
                // If the placeholder alpha is between 1% and 99%, draw it in a layer with
                // the alpha applied
                paint.alpha = placeholderAlpha
                withLayer(paint) {
                    lastOutline.value = drawPlaceholder(
                        shape = shape,
                        color = color,
                        highlight = highlight,
                        progress = highlightProgress,
                        lastOutline = lastOutline.value,
                        lastLayoutDirection = lastLayoutDirection.value,
                        lastSize = lastSize.value,
                    )
                }
            } else if (placeholderAlpha >= 0.99f) {
                // If the placeholder alpha is > 99%, draw it with no alpha
                lastOutline.value = drawPlaceholder(
                    shape = shape,
                    color = color,
                    highlight = highlight,
                    progress = highlightProgress,
                    lastOutline = lastOutline.value,
                    lastLayoutDirection = lastLayoutDirection.value,
                    lastSize = lastSize.value,
                )
            }

            lastSize.value = size
            lastLayoutDirection.value = layoutDirection
        }
    }
}

/**
 * Placeholder that holds the default values used by the public api
 */
internal fun Modifier.basePlaceholder(
    visible: Boolean,
    color: Color = Color.Unspecified,
    shape: Shape? = null,
    highlight: PlaceholderHighlight? = null,
): Modifier = composed {
    placeholder(
        visible = visible,
        highlight = highlight ?: PlaceholderHighlight.fade(),
        color = if (color.isSpecified) color else PlaceholderDefaults.color(),
        shape = shape ?: SparkTheme.shapes.small,
    )
}

private fun DrawScope.drawPlaceholder(
    shape: Shape,
    color: Color,
    highlight: PlaceholderHighlight?,
    progress: Float,
    lastOutline: Outline?,
    lastLayoutDirection: LayoutDirection?,
    lastSize: Size?,
): Outline? {
    // shortcut to avoid Outline calculation and allocation
    if (shape === RectangleShape) {
        // Draw the initial background color
        drawRect(color = color)

        if (highlight != null) {
            drawRect(
                brush = highlight.brush(progress, size),
                alpha = highlight.alpha(progress),
            )
        }
        // We didn't create an outline so return null
        return null
    }

    // Otherwise we need to create an outline from the shape
    val outline = lastOutline.takeIf {
        size == lastSize && layoutDirection == lastLayoutDirection
    } ?: shape.createOutline(size, layoutDirection, this)

    // Draw the placeholder color
    drawOutline(outline = outline, color = color)

    if (highlight != null) {
        drawOutline(
            outline = outline,
            brush = highlight.brush(progress, size),
            alpha = highlight.alpha(progress),
        )
    }

    // Return the outline we used
    return outline
}

private inline fun DrawScope.withLayer(
    paint: Paint,
    drawBlock: DrawScope.() -> Unit,
) = drawIntoCanvas { canvas ->
    canvas.saveLayer(size.toRect(), paint)
    drawBlock()
    canvas.restore()
}

/**
 * Returns the value used as the the `color` parameter value on [Modifier.placeholder].
 *
 * @param backgroundColor The current background color of the layout. Defaults to
 * `SparkTheme.colors.surface`.
 * @param contentColor The content color to be used on top of [backgroundColor].
 * @param contentAlpha The alpha component to set on [contentColor] when compositing the color
 * on top of [backgroundColor]. Defaults to `0.1f`.
 */
@Composable
public fun PlaceholderDefaults.color(
    backgroundColor: Color = SparkTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    contentAlpha: Float = 0.25f,
): Color = contentColor.copy(contentAlpha).compositeOver(backgroundColor)

/**
 * Returns the value used as the the `highlightColor` parameter value of
 * [PlaceholderHighlight.Companion.fade].
 *
 * @param backgroundColor The current background color of the layout. Defaults to
 * `SparkTheme.color.surface`.
 * @param alpha The alpha component to set on [backgroundColor]. Defaults to `0.3f`.
 */
@Composable
public fun PlaceholderDefaults.fadeHighlightColor(
    backgroundColor: Color = SparkTheme.colors.surface,
    alpha: Float = 0.3f,
): Color = backgroundColor.copy(alpha = alpha)

/**
 * Returns the value used as the the `highlightColor` parameter value of
 * [PlaceholderHighlight.Companion.shimmer].
 *
 * @param backgroundColor The current background color of the layout. Defaults to
 * `SparkTheme.colors.surface`.
 * @param alpha The alpha component to set on [backgroundColor]. Defaults to `0.75f`.
 */
@Composable
public fun PlaceholderDefaults.shimmerHighlightColor(
    backgroundColor: Color = SparkTheme.colors.surface,
    alpha: Float = 0.3f,
): Color = backgroundColor.copy(alpha = alpha)
