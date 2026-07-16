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
package com.adevinta.spark.components.placeholder

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.LayoutDirection
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.LocalSparkColors
import com.adevinta.spark.tokens.LocalSparkShapes
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.contentColorFor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
 * A cross-fade transition will be applied to the content and placeholder UI when the [visible]
 * value changes. The transition uses a spring animation.
 *
 * You can provide a [PlaceholderHighlight] which runs a highlight animation on the placeholder.
 * The [shimmer] and [fade] implementations are provided for easy usage.
 *
 * You can find more information on the pattern at the Material Theming
 * [Placeholder UI](https://material.io/design/communication/launch-screen.html#placeholder-ui)
 * guidelines.
 *
 * @param visible whether the placeholder should be visible or not.
 * @param color the color used to draw the placeholder UI.
 * @param shape desired shape of the placeholder.
 * @param highlight optional highlight animation.
 */
internal fun Modifier.placeholder(
    visible: Boolean,
    color: Color,
    shape: Shape,
    highlight: PlaceholderHighlight? = null,
): Modifier = this then PlaceholderElement(
    visible = visible,
    color = color,
    shape = shape,
    highlight = highlight,
    useShimmerHighlight = false,
    useFullShape = false,
)

/**
 * Placeholder that holds the default values used by the public API.
 * Unresolved values (Color.Unspecified, null shape/highlight) are resolved from theme at draw time.
 */
internal fun Modifier.basePlaceholder(
    visible: Boolean,
    color: Color = Color.Unspecified,
    shape: Shape? = null,
    highlight: PlaceholderHighlight? = null,
    useShimmerHighlight: Boolean = false,
    useFullShape: Boolean = false,
): Modifier = this then PlaceholderElement(
    visible = visible,
    color = color,
    shape = shape,
    highlight = highlight,
    useShimmerHighlight = useShimmerHighlight,
    useFullShape = useFullShape,
)

internal class PlaceholderElement(
    val visible: Boolean,
    val color: Color,
    val shape: Shape?,
    val highlight: PlaceholderHighlight?,
    val useShimmerHighlight: Boolean,
    val useFullShape: Boolean,
) : ModifierNodeElement<PlaceholderNode>() {

    override fun create(): PlaceholderNode = PlaceholderNode(
        visible = visible,
        color = color,
        shape = shape,
        highlight = highlight,
        useShimmerHighlight = useShimmerHighlight,
        useFullShape = useFullShape,
    )

    override fun update(node: PlaceholderNode) {
        node.update(
            visible = visible,
            color = color,
            shape = shape,
            highlight = highlight,
            useShimmerHighlight = useShimmerHighlight,
            useFullShape = useFullShape,
        )
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "placeholder"
        value = visible
        properties["visible"] = visible
        properties["color"] = color
        if (shape != null) properties["shape"] = shape
        properties["highlight"] = highlight
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlaceholderElement) return false
        return visible == other.visible &&
            color == other.color &&
            shape == other.shape &&
            highlight == other.highlight &&
            useShimmerHighlight == other.useShimmerHighlight &&
            useFullShape == other.useFullShape
    }

    override fun hashCode(): Int {
        var result = visible.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + (shape?.hashCode() ?: 0)
        result = 31 * result + (highlight?.hashCode() ?: 0)
        result = 31 * result + useShimmerHighlight.hashCode()
        result = 31 * result + useFullShape.hashCode()
        return result
    }
}

internal class PlaceholderNode(
    private var visible: Boolean,
    private var color: Color,
    private var shape: Shape?,
    private var highlight: PlaceholderHighlight?,
    private var useShimmerHighlight: Boolean,
    private var useFullShape: Boolean,
) : Modifier.Node(),
    DrawModifierNode,
    CompositionLocalConsumerModifierNode {

    private var crossfadeAlpha: Float = if (visible) 1f else 0f
    private var highlightProgress: Float = 0f

    private var crossfadeJob: Job? = null
    private var highlightJob: Job? = null

    private val paint = Paint()

    private var lastSize: Size? = null
    private var lastLayoutDirection: LayoutDirection? = null
    private var lastOutline: Outline? = null

    override fun onAttach() {
        startHighlightAnimation()
    }

    fun update(
        visible: Boolean,
        color: Color,
        shape: Shape?,
        highlight: PlaceholderHighlight?,
        useShimmerHighlight: Boolean,
        useFullShape: Boolean,
    ) {
        val visibleChanged = this.visible != visible
        val highlightChanged = this.highlight != highlight ||
            this.useShimmerHighlight != useShimmerHighlight

        this.color = color
        this.shape = shape
        this.highlight = highlight
        this.useShimmerHighlight = useShimmerHighlight
        this.useFullShape = useFullShape

        if (visibleChanged) {
            this.visible = visible
            startCrossfade()
        }

        if (highlightChanged) {
            startHighlightAnimation()
        }

        lastOutline = null
        invalidateDraw()
    }

    private fun startCrossfade() {
        crossfadeJob?.cancel()
        val start = crossfadeAlpha
        val target = if (visible) 1f else 0f
        if (start == target) return
        crossfadeJob = coroutineScope.launch {
            animate(
                initialValue = start,
                targetValue = target,
                animationSpec = spring(),
            ) { value, _ ->
                crossfadeAlpha = value
                invalidateDraw()
            }
        }
    }

    private fun startHighlightAnimation() {
        highlightJob?.cancel()
        highlightProgress = 0f
        if (!visible && crossfadeAlpha < 0.01f) return
        highlightJob = coroutineScope.launch {
            val spec = resolveHighlight().animationSpec ?: return@launch
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = spec,
            ) { value, _ ->
                highlightProgress = value
                invalidateDraw()
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val placeholderAlpha = crossfadeAlpha
        val contentAlpha = 1f - crossfadeAlpha

        if (contentAlpha in 0.01f..0.99f) {
            paint.alpha = contentAlpha
            withLayer(paint) {
                this@draw.drawContent()
            }
        } else if (contentAlpha >= 0.99f) {
            drawContent()
        }

        if (placeholderAlpha >= 0.01f) {
            val resolvedColor = resolveColor()
            val resolvedShape = resolveShape()
            val resolvedHighlight = resolveHighlight()

            if (placeholderAlpha in 0.01f..0.99f) {
                paint.alpha = placeholderAlpha
                withLayer(paint) {
                    lastOutline = drawPlaceholder(
                        shape = resolvedShape,
                        color = resolvedColor,
                        highlight = resolvedHighlight,
                        progress = highlightProgress,
                    )
                }
            } else {
                lastOutline = drawPlaceholder(
                    shape = resolvedShape,
                    color = resolvedColor,
                    highlight = resolvedHighlight,
                    progress = highlightProgress,
                )
            }
        }

        lastSize = size
        lastLayoutDirection = layoutDirection
    }

    private fun resolveColor(): Color {
        if (color.isSpecified) return color
        val colors = currentValueOf(LocalSparkColors)
        val surface = colors.surface
        val content = colors.contentColorFor(surface)
        return content.copy(alpha = 0.25f).compositeOver(surface)
    }

    private fun resolveShape(): Shape {
        shape?.let { return it }
        val shapes = currentValueOf(LocalSparkShapes)
        return if (useFullShape) shapes.full else shapes.small
    }

    private fun resolveHighlight(): PlaceholderHighlight {
        highlight?.let { return it }
        val surface = currentValueOf(LocalSparkColors).surface
        val highlightColor = surface.copy(alpha = 0.3f)
        return if (useShimmerHighlight) {
            PlaceholderHighlight.shimmer(
                highlightColor = highlightColor,
                animationSpec = PlaceholderDefaults.shimmerAnimationSpec,
            )
        } else {
            PlaceholderHighlight.fade(
                highlightColor = highlightColor,
                animationSpec = PlaceholderDefaults.fadeAnimationSpec,
            )
        }
    }

    private fun DrawScope.drawPlaceholder(
        shape: Shape,
        color: Color,
        highlight: PlaceholderHighlight?,
        progress: Float,
    ): Outline? {
        if (shape === RectangleShape) {
            drawRect(color = color)
            if (highlight != null) {
                drawRect(
                    brush = highlight.brush(progress, size),
                    alpha = highlight.alpha(progress),
                )
            }
            return null
        }

        val outline = lastOutline.takeIf {
            size == lastSize && layoutDirection == lastLayoutDirection
        } ?: shape.createOutline(size, layoutDirection, this)

        drawOutline(outline = outline, color = color)

        if (highlight != null) {
            drawOutline(
                outline = outline,
                brush = highlight.brush(progress, size),
                alpha = highlight.alpha(progress),
            )
        }

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
}

/**
 * Returns the value used as the `color` parameter value on [Modifier.placeholder].
 *
 * @param backgroundColor The current background color of the layout. Defaults to
 * `SparkTheme.colors.surface`.
 * @param contentColor The content color to be used on top of [backgroundColor].
 * @param contentAlpha The alpha component to set on [contentColor] when compositing the color
 * on top of [backgroundColor]. Defaults to `0.25f`.
 */
@Composable
public fun PlaceholderDefaults.color(
    backgroundColor: Color = SparkTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    contentAlpha: Float = 0.25f,
): Color = contentColor.copy(contentAlpha).compositeOver(backgroundColor)

/**
 * Returns the value used as the `highlightColor` parameter value of
 * [PlaceholderHighlight.Companion.fade].
 *
 * @param backgroundColor The current background color of the layout. Defaults to
 * `SparkTheme.colors.surface`.
 * @param alpha The alpha component to set on [backgroundColor]. Defaults to `0.3f`.
 */
@Composable
public fun PlaceholderDefaults.fadeHighlightColor(
    backgroundColor: Color = SparkTheme.colors.surface,
    alpha: Float = 0.3f,
): Color = backgroundColor.copy(alpha = alpha)

/**
 * Returns the value used as the `highlightColor` parameter value of
 * [PlaceholderHighlight.Companion.shimmer].
 *
 * @param backgroundColor The current background color of the layout. Defaults to
 * `SparkTheme.colors.surface`.
 * @param alpha The alpha component to set on [backgroundColor]. Defaults to `0.3f`.
 */
@Composable
public fun PlaceholderDefaults.shimmerHighlightColor(
    backgroundColor: Color = SparkTheme.colors.surface,
    alpha: Float = 0.3f,
): Color = backgroundColor.copy(alpha = alpha)
