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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.SparkAnimatedIcons
import com.adevinta.spark.tokens.ripple
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Draws a vertical gradient scrim in the foreground.
 *
 * @param color The color of the gradient scrim.
 * @param decay The exponential decay to apply to the gradient. Defaults to `3.0f` which is
 * a cubic decay.
 * @param numStops The number of color stops to draw in the gradient. Higher numbers result in
 * the higher visual quality at the cost of draw performance. Defaults to `16`.
 */
public fun Modifier.drawForegroundGradientScrim(
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

@OptIn(ExperimentalSparkApi::class)
@Composable
private fun FavoriteButton(
    favorite: Boolean,
    modifier: Modifier = Modifier,
    onFavoriteChanged: (favorite: Boolean) -> Unit,
) {
    val backgroundColor = SparkTheme.colors.background
    val backgroundRadius = 32.dp / 2
    Box(
        modifier =
        modifier
            .minimumInteractiveComponentSize()
            .toggleable(
                value = favorite,
                onValueChange = onFavoriteChanged,
                role = Role.Checkbox,
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication =
                ripple(
                    bounded = false,
                    radius = backgroundRadius,
                ),
            )
            .background(SparkTheme.colors.alertContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.drawBehind {
                drawCircle(
                    color = backgroundColor,
                    radius = backgroundRadius.toPx(),
                )
            },
            painter = SparkAnimatedIcons.likeHeart(favorite),
            tint = Color.Unspecified,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun PreviewFavoriteButton() {
    PreviewTheme(
        color = { SparkTheme.colors.backgroundVariant },
    ) {
        var favorite by remember { mutableStateOf(false) }
        FavoriteButton(
            favorite = favorite,
            modifier = Modifier.size(200.dp),
            onFavoriteChanged = { favorite = it },
        )
    }
}

@Composable
public fun SparkAnimatedIcons.likeHeart(favorite: Boolean): Painter {
    val favoriteState = if (favorite) FavoriteState.Favorite else FavoriteState.UnFavorite
    val miniHeart1State = updateFavoriteState(favoriteState, 116.milliseconds)
    val miniHeart2State = updateFavoriteState(favoriteState, 116.milliseconds * 2)
    val miniHeart3State = updateFavoriteState(favoriteState, 116.milliseconds * 3)
    val outlineColor = LocalContentColor.current
    val fillColor = SparkTheme.colors.error
    val scaleYProgress by animateFloatAsState(
        targetValue = if (!favorite) 0.9999f else 1f,
        animationSpec = if (!favorite) {
            snap()
        } else {
            keyframes {
                durationMillis = 400
                1f at 0 using FastOutSlowInEasing
                1.5f at 100 using FastOutSlowInEasing
                1.75f at 200 using FastOutSlowInEasing
                0.82f at 300 using FastOutSlowInEasing
                0.9999f at 400 using FastOutSlowInEasing
            }
        },
        label = "favorite-scale-y",
    )
    val scaleXProgress by animateFloatAsState(
        targetValue = if (!favorite) 0.9999f else 1f,
        animationSpec = if (!favorite) {
            snap()
        } else {
            keyframesWithSpline {
                durationMillis = 400
                1f at 0 using FastOutSlowInEasing
                1.6f at 100 using FastOutSlowInEasing
                1.6f at 200 using FastOutSlowInEasing
                1.3f at 200 using FastOutSlowInEasing
                0.9999f at 300 using FastOutSlowInEasing
            }
        },
        label = "favorite-scale-x",
    )
    val animationColor by animateColorAsState(
        targetValue = if (!favorite) outlineColor else fillColor,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "favorite-color",
    )

    return rememberVectorPainter(
        defaultWidth = 40.dp,
        defaultHeight = 40.dp,
        viewportWidth = 40f,
        viewportHeight = 40f,
        autoMirror = true,
    ) { _, _ ->
        val outlineHeartPath = remember {
            PathParser().parsePathString(
                "M 7.72 5.12 C 5.72 5.12 4.02 6.92 4.02 9.25 C 4.062 10.78 4.603 12.256 5.56 13.45 C 7.2 15.62 9.41 17.07 11.74 18.59 L 11.845 18.655 C 11.88 18.677 11.915 18.698 11.95 18.72 L 12.02 18.68 C 14.47 17.16 16.78 15.74 18.45 13.49 C 18.99 12.75 19.97 11.25 19.97 9.25 C 19.98 6.91 18.27 5.12 16.28 5.12 C 15.546 5.14 14.834 5.378 14.236 5.804 C 13.638 6.229 13.18 6.823 12.92 7.51 C 12.76 7.9 12.4 8.15 11.99 8.15 C 11.58 8.15 11.22 7.89 11.06 7.51 C 10.801 6.825 10.344 6.232 9.748 5.806 C 9.152 5.381 8.442 5.142 7.71 5.12 L 7.72 5.12 M 12 5.11 C 13.04 3.83 14.56 3 16.28 3 L 16.28 3.02 C 19.49 3.02 22 5.85 22 9.25 C 21.963 11.258 21.279 13.202 20.05 14.79 C 18.12 17.37 15.49 18.99 13.1 20.47 L 13.08 20.47 L 12.45 20.86 C 12.12 21.06 11.71 21.05 11.39 20.84 L 10.66 20.35 L 10.64 20.35 C 8.35 18.86 5.84 17.22 3.97 14.75 C 2.737 13.174 2.045 11.241 2 9.24 C 2 5.85 4.51 3 7.72 3 C 8.546 3.01 9.359 3.204 10.099 3.57 C 10.84 3.935 11.489 4.461 12 5.11",
            )
                .toNodes()
        }
        val fillHeartPath = remember {
            PathParser().parsePathString(
                "M 7.61 3 C 4.47 3 2 5.8 2 9.16 C 2.042 11.158 2.733 13.09 3.97 14.66 C 5.86 17.17 8.4 18.84 10.75 20.38 L 11.51 20.88 C 11.76 21.04 12.07 21.05 12.33 20.89 L 12.98 20.49 C 15.44 18.97 18.1 17.33 20.05 14.7 C 21.281 13.112 21.965 11.168 22 9.16 C 22 5.8 19.53 3.01 16.39 3.01 C 15.479 3.01 14.622 3.248 13.866 3.669 C 13.328 3.967 12.842 4.357 12.423 4.819 C 12.273 4.984 12.132 5.158 12 5.34 C 11.849 5.123 11.683 4.918 11.503 4.726 C 11.095 4.291 10.617 3.924 10.087 3.643 C 9.324 3.238 8.474 3.021 7.61 3.01 L 7.61 3 M 12 11.93 C 12 11.93 12 11.93 12 11.93 L 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 L 12 11.93 L 12 11.93 C 12 11.93 12 11.93 12 11.93 L 12 11.93 L 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93",
            )
                .toNodes()
        }
        val fillMiniHeart1Path = remember {
            PathParser().parsePathString(
                "M31.34 28C30.03 28 29 29.17 29 30.57c0 1.14.58 1.97.82 2.29a10.55 10.55 0 0 0 2.83 2.38l.31.2c.1.08.24.08.34.01l.28-.17a9.91 9.91 0 0 0 2.94-2.4c.27-.37.81-1.19.81-2.32 0-1.4-1.03-2.56-2.33-2.56-.75 0-1.4.38-1.83.97a2.26 2.26 0 0 0-1.83-.97Z",
            )
                .toNodes()
        }
        val fillMiniHeart2Path = remember {
            PathParser().parsePathString(
                "M8.99 13c-2.51 0-4.49 2.33-4.49 5.13 0 2.3 1.12 3.95 1.58 4.59 1.5 2.1 3.54 3.48 5.43 4.76l.6.41c.2.14.45.15.65.01.18-.1.35-.22.53-.33 1.96-1.27 4.1-2.64 5.65-4.82a7.97 7.97 0 0 0 1.56-4.62c0-2.8-1.98-5.13-4.49-5.13a4.3 4.3 0 0 0-3.5 1.94A4.3 4.3 0 0 0 8.98 13Z",
            )
                .toNodes()
        }

        val fillMiniHeart3Path = remember {
            PathParser().parsePathString(
                "M6.93 34C5 34 3.5 35.7 3.5 37.76c0 1.69.85 2.9 1.2 3.37 1.16 1.53 2.71 2.55 4.15 3.49l.46.3c.16.1.35.1.5.01l.4-.25c1.5-.93 3.13-1.93 4.32-3.53a5.7 5.7 0 0 0 1.2-3.39c0-2.05-1.52-3.76-3.43-3.76-1.1 0-2.06.56-2.69 1.42A3.32 3.32 0 0 0 6.93 34Z",
            )
                .toNodes()
        }

        Group(
            pivotX = 12f,
            pivotY = 12f,
            translationX = 8f,
            translationY = 8f,
            scaleX = scaleXProgress,
            scaleY = scaleYProgress,
        ) {
            val currentPath by animatePathAsState(
                if (!favorite) outlineHeartPath else fillHeartPath,
            )
            Group(
                pivotX = 12f,
                pivotY = 12f,
                scaleX = 0.7f,
                scaleY = 0.7f,
            ) {
                Path(
                    pathData = currentPath,
                    fill = SolidColor(animationColor),
                )
            }
        }

        Group(
            pivotX = 32.665f,
            pivotY = 31.75f,
            scaleX = miniHeart1State.scale,
            scaleY = miniHeart1State.scale,
            translationY = miniHeart1State.translationY,
        ) {
            Path(
                pathData = fillMiniHeart1Path,
                fillAlpha = miniHeart1State.opacity,
                fill = SolidColor(fillColor),
            )
        }
        Group(
            pivotX = 12.5f,
            pivotY = 20.5f,
            scaleX = miniHeart2State.scale,
            scaleY = miniHeart2State.scale,
            translationY = miniHeart2State.translationY,
        ) {
            Path(
                pathData = fillMiniHeart2Path,
                fillAlpha = miniHeart2State.opacity,
                fill = SolidColor(fillColor),
            )
        }
        Group(
            pivotX = 14.61f,
            pivotY = 39.5f,
            scaleX = miniHeart3State.scale,
            scaleY = miniHeart3State.scale,
            translationY = miniHeart3State.translationY + -5,
        ) {
            Path(
                pathData = fillMiniHeart3Path,
                fillAlpha = miniHeart3State.opacity,
                fill = SolidColor(fillColor),
            )
        }
    }
}

private class FavoriteData(opacity: State<Float>, translationY: State<Float>, scale: State<Float>) {
    val opacity by opacity
    val translationY by translationY
    val scale by scale
}

internal enum class FavoriteState { Favorite, UnFavorite }

// Create a Transition and return its animation values.
@Composable
private fun updateFavoriteState(favoriteState: FavoriteState, delay: Duration): FavoriteData {
    val transition = updateTransition(favoriteState, label = "favorite state")
    val delayMilli = delay.inWholeMilliseconds.toInt()
    val scale = transition.animateFloat(
        label = "scale",
        transitionSpec = {
            if (FavoriteState.UnFavorite isTransitioningTo FavoriteState.Favorite) {
                keyframesWithSpline {
                    durationMillis = 616
                    0f at 0
                    0f at 600 + delayMilli using CubicBezierEasing(.34f, 0f, 0f, 1f)
                }
            } else {
                snap()
            }
        },
    ) { state ->
        when (state) {
            FavoriteState.Favorite -> 1f
            FavoriteState.UnFavorite -> 0f
        }
    }
    val translationY = transition.animateFloat(
        label = "translationY",
        transitionSpec = {
            if (FavoriteState.UnFavorite isTransitioningTo FavoriteState.Favorite) {
                keyframesWithSpline {
                    durationMillis = 616
                    0f at 0 + delayMilli
                    -10f at 600 + delayMilli using CubicBezierEasing(.34f, 0f, 0f, 1f)
                }
            } else {
                snap()
            }
        },
    ) { state ->
        when (state) {
            FavoriteState.Favorite -> -10f
            FavoriteState.UnFavorite -> 0f
        }
    }
    val opacity = transition.animateFloat(
        label = "opacity",
        transitionSpec = {
            if (FavoriteState.UnFavorite isTransitioningTo FavoriteState.Favorite) {
                keyframesWithSpline {
                    durationMillis = 616
                    1f at 0 + delayMilli
                    1f at 0 + delayMilli
                    0f at 600 + delayMilli using SnapEasing
//                    0f at 616 using FastOutSlowInEasing
                }
            } else {
                snap()
            }
        },
    ) { state ->
        when (state) {
            FavoriteState.Favorite -> 0.0001f
            FavoriteState.UnFavorite -> 0f
        }
    }
    return remember(transition) { FavoriteData(opacity, translationY, scale) }
}

private val SnapEasing: CubicBezierEasing = CubicBezierEasing(1f, -0.01f, .74f, 1f)

@Composable
public fun animatePathAsState(
    path: String,
): State<List<PathNode>> = animatePathAsState(remember(path) { addPathNodes(path) })

@Composable
public fun animatePathAsState(path: List<PathNode>): State<List<PathNode>> {
    var from by remember { mutableStateOf(path) }
    var to by remember { mutableStateOf(path) }
    val fraction = remember { Animatable(0f) }

    LaunchedEffect(path) {
        if (to != path) {
            from = to
            to = path
            fraction.snapTo(0f)
            fraction.animateTo(1f)
        }
    }

    return remember {
        derivedStateOf {
            if (canMorph(from, to)) {
                lerp(from, to, fraction.value)
            } else {
                to
            }
        }
    }
}

// Paths can morph if same size and same node types at same positions.
private fun canMorph(from: List<PathNode>, to: List<PathNode>): Boolean {
    if (from.size != to.size) {
        return false
    }

    for (i in from.indices) {
        if (from[i].javaClass != to[i].javaClass) {
            return false
        }
    }

    return true
}

// Assume paths can morph (see [canMorph]). If not, will throw.
private fun lerp(
    fromPath: List<PathNode>,
    toPath: List<PathNode>,
    fraction: Float,
): List<PathNode> = fromPath.mapIndexed { i, from ->
    val to = toPath[i]
    lerp(from, to, fraction)
}

private fun lerp(from: PathNode, to: PathNode, fraction: Float): PathNode = when (from) {
    PathNode.Close -> {
        to as PathNode.Close
        from
    }

    is PathNode.RelativeMoveTo -> {
        to as PathNode.RelativeMoveTo
        PathNode.RelativeMoveTo(
            lerp(from.dx, to.dx, fraction),
            lerp(from.dy, to.dy, fraction),
        )
    }

    is PathNode.MoveTo -> {
        to as PathNode.MoveTo
        PathNode.MoveTo(
            lerp(from.x, to.x, fraction),
            lerp(from.y, to.y, fraction),
        )
    }

    is PathNode.RelativeLineTo -> {
        to as PathNode.RelativeLineTo
        PathNode.RelativeLineTo(
            lerp(from.dx, to.dx, fraction),
            lerp(from.dy, to.dy, fraction),
        )
    }

    is PathNode.LineTo -> {
        to as PathNode.LineTo
        PathNode.LineTo(
            lerp(from.x, to.x, fraction),
            lerp(from.y, to.y, fraction),
        )
    }

    is PathNode.RelativeHorizontalTo -> {
        to as PathNode.RelativeHorizontalTo
        PathNode.RelativeHorizontalTo(
            lerp(from.dx, to.dx, fraction),
        )
    }

    is PathNode.HorizontalTo -> {
        to as PathNode.HorizontalTo
        PathNode.HorizontalTo(
            lerp(from.x, to.x, fraction),
        )
    }

    is PathNode.RelativeVerticalTo -> {
        to as PathNode.RelativeVerticalTo
        PathNode.RelativeVerticalTo(
            lerp(from.dy, to.dy, fraction),
        )
    }

    is PathNode.VerticalTo -> {
        to as PathNode.VerticalTo
        PathNode.VerticalTo(
            lerp(from.y, to.y, fraction),
        )
    }

    is PathNode.RelativeCurveTo -> {
        to as PathNode.RelativeCurveTo
        PathNode.RelativeCurveTo(
            lerp(from.dx1, to.dx1, fraction),
            lerp(from.dy1, to.dy1, fraction),
            lerp(from.dx2, to.dx2, fraction),
            lerp(from.dy2, to.dy2, fraction),
            lerp(from.dx3, to.dx3, fraction),
            lerp(from.dy3, to.dy3, fraction),
        )
    }

    is PathNode.CurveTo -> {
        to as PathNode.CurveTo
        PathNode.CurveTo(
            lerp(from.x1, to.x1, fraction),
            lerp(from.y1, to.y1, fraction),
            lerp(from.x2, to.x2, fraction),
            lerp(from.y2, to.y2, fraction),
            lerp(from.x3, to.x3, fraction),
            lerp(from.y3, to.y3, fraction),
        )
    }

    is PathNode.RelativeReflectiveCurveTo -> {
        to as PathNode.RelativeReflectiveCurveTo
        PathNode.RelativeReflectiveCurveTo(
            lerp(from.dx1, to.dx1, fraction),
            lerp(from.dy1, to.dy1, fraction),
            lerp(from.dx2, to.dx2, fraction),
            lerp(from.dy2, to.dy2, fraction),
        )
    }

    is PathNode.ReflectiveCurveTo -> {
        to as PathNode.ReflectiveCurveTo
        PathNode.ReflectiveCurveTo(
            lerp(from.x1, to.x1, fraction),
            lerp(from.y1, to.y1, fraction),
            lerp(from.x2, to.x2, fraction),
            lerp(from.y2, to.y2, fraction),
        )
    }

    is PathNode.RelativeQuadTo -> {
        to as PathNode.RelativeQuadTo
        PathNode.RelativeQuadTo(
            lerp(from.dx1, to.dx1, fraction),
            lerp(from.dy1, to.dy1, fraction),
            lerp(from.dx2, to.dx2, fraction),
            lerp(from.dy2, to.dy2, fraction),
        )
    }

    is PathNode.QuadTo -> {
        to as PathNode.QuadTo
        PathNode.QuadTo(
            lerp(from.x1, to.x1, fraction),
            lerp(from.y1, to.y1, fraction),
            lerp(from.x2, to.x2, fraction),
            lerp(from.y2, to.y2, fraction),
        )
    }

    is PathNode.RelativeReflectiveQuadTo -> {
        to as PathNode.RelativeReflectiveQuadTo
        PathNode.RelativeReflectiveQuadTo(
            lerp(from.dx, to.dx, fraction),
            lerp(from.dy, to.dy, fraction),
        )
    }

    is PathNode.ReflectiveQuadTo -> {
        to as PathNode.ReflectiveQuadTo
        PathNode.ReflectiveQuadTo(
            lerp(from.x, to.x, fraction),
            lerp(from.y, to.y, fraction),
        )
    }

    is PathNode.RelativeArcTo -> TODO("Support for RelativeArcTo not implemented yet")
    is PathNode.ArcTo -> TODO("Support for ArcTo not implemented yet")
}

@Preview
@Composable
private fun GradientScrimPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(SparkTheme.colors.surfaceInverse)
                .drawForegroundGradientScrim(SparkTheme.colors.surface),
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(SparkTheme.colors.surfaceInverse)
                .drawForegroundGradientScrim(
                    color = SparkTheme.colors.surface,
                    decay = 1.5f,
                ),
        )
    }
}
