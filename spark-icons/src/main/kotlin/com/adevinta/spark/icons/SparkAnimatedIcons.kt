/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.icons

import androidx.annotation.RestrictTo
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.adevinta.spark.icons.internal.animatePathAsState

/**
 * A collection of animated icons from Spark.
 *
 * This object provides access to animated icon implementations that use Compose's
 * animation system. Each icon is provided as a function that returns a [SparkIcon.AnimatedPainter], allowing for flexible
 * animation control through the `atEnd` parameter.
 *
 * These icons are designed to be Kotlin Multiplatform compatible and provide more flexibility
 * compared to traditional vector drawables.
 *
 * @see SparkIcon.AnimatedPainter
 * @see SparkIcon.AnimatedDrawableRes
 */
public object SparkAnimatedIcons

public fun SparkAnimatedIcons.collapseExpand(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter { expanded ->
    rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ) { _, _ ->
        val startPath = remember {
            addPathNodes(
                "M 12 7 C 12.47 7 13 7.2 13.33 7.52 L 21.67 15.25 C 22.11 15.65 22.11 16.3 21.67 16.7 C 21.24 17.1 20.54 17.1 20.1 16.7 L 13.266 10.373 C 12.844 9.982 12.422 9.591 12 9.2 L 12 7 M 12 7 L 12 9.2 C 12 9.2 12 9.2 12 9.2 C 11.651 9.523 11.302 9.846 10.953 10.169 L 3.9 16.7 C 3.46 17.1 2.76 17.1 2.33 16.7 L 2.33 16.7 C 1.89 16.3 1.89 15.65 2.33 15.25 L 10.67 7.52 C 10.967 7.232 11.401 7.042 11.849 7.006 C 11.899 7.002 11.949 7 12 7 M 12 14.78 L 12 14.78",
            )
        }
        val endPath = remember {
            addPathNodes(
                "M 12 14.78 C 12.378 14.431 12.756 14.082 13.134 13.733 L 20.1 7.3 C 20.54 6.9 21.24 6.9 21.67 7.3 C 22.11 7.7 22.11 8.36 21.67 8.77 L 13.33 16.47 C 13 16.81 12.5 17 12 17 L 12 14.78 M 12 14.78 L 12 17 C 12 17 12 17 12 17 C 11.5 17 11.01 16.8 10.67 16.47 L 2.33 8.77 C 1.89 8.37 1.89 7.71 2.33 7.3 L 2.33 7.3 C 2.76 6.9 3.46 6.9 3.9 7.3 L 10.853 13.721 C 11.236 14.074 11.618 14.427 12 14.78 C 12 14.78 12 14.78 12 14.78 M 12 14.78 L 12 14.78",
            )
        }

        val currentPath by animatePathAsState(if (expanded) startPath else endPath)

        Group {
            Path(
                pathData = currentPath,
                fill = SolidColor(Color.Black),
            )
        }
    }
}

public fun SparkAnimatedIcons.likeHeart(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter { filled ->
    rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ) { _, _ ->
        val fillPath = remember {
            addPathNodes(
                "M 7.61 3 C 4.47 3 2 5.8 2 9.16 C 2.042 11.158 2.733 13.09 3.97 14.66 C 5.86 17.17 8.4 18.84 10.75 20.38 L 11.51 20.88 C 11.76 21.04 12.07 21.05 12.33 20.89 L 12.98 20.49 C 15.44 18.97 18.1 17.33 20.05 14.7 C 21.281 13.112 21.965 11.168 22 9.16 C 22 5.8 19.53 3.01 16.39 3.01 C 15.479 3.01 14.622 3.248 13.866 3.669 C 13.328 3.967 12.842 4.357 12.423 4.819 C 12.273 4.984 12.132 5.158 12 5.34 C 11.849 5.123 11.683 4.918 11.503 4.726 C 11.095 4.291 10.617 3.924 10.087 3.643 C 9.324 3.238 8.474 3.021 7.61 3.01 L 7.61 3 M 12 11.93 C 12 11.93 12 11.93 12 11.93 L 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 L 12 11.93 L 12 11.93 C 12 11.93 12 11.93 12 11.93 L 12 11.93 L 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93 C 12 11.93 12 11.93 12 11.93",
            )
        }
        val outlinePath = remember {
            addPathNodes(
                "M 7.72 5.12 C 5.72 5.12 4.02 6.92 4.02 9.25 C 4.062 10.78 4.603 12.256 5.56 13.45 C 7.2 15.62 9.41 17.07 11.74 18.59 L 11.845 18.655 C 11.88 18.677 11.915 18.698 11.95 18.72 L 12.02 18.68 C 14.47 17.16 16.78 15.74 18.45 13.49 C 18.99 12.75 19.97 11.25 19.97 9.25 C 19.98 6.91 18.27 5.12 16.28 5.12 C 15.546 5.14 14.834 5.378 14.236 5.804 C 13.638 6.229 13.18 6.823 12.92 7.51 C 12.76 7.9 12.4 8.15 11.99 8.15 C 11.58 8.15 11.22 7.89 11.06 7.51 C 10.801 6.825 10.344 6.232 9.748 5.806 C 9.152 5.381 8.442 5.142 7.71 5.12 L 7.72 5.12 M 12 5.11 C 13.04 3.83 14.56 3 16.28 3 L 16.28 3.02 C 19.49 3.02 22 5.85 22 9.25 C 21.963 11.258 21.279 13.202 20.05 14.79 C 18.12 17.37 15.49 18.99 13.1 20.47 L 13.08 20.47 L 12.45 20.86 C 12.12 21.06 11.71 21.05 11.39 20.84 L 10.66 20.35 L 10.64 20.35 C 8.35 18.86 5.84 17.22 3.97 14.75 C 2.737 13.174 2.045 11.241 2 9.24 C 2 5.85 4.51 3 7.72 3 C 8.546 3.01 9.359 3.204 10.099 3.57 C 10.84 3.935 11.489 4.461 12 5.11",
            )
        }

        val currentPath by animatePathAsState(
            if (filled) fillPath else outlinePath,
        )

        Path(
            pathData = currentPath,
            fill = SolidColor(Color.Black),
        )
    }
}

public fun SparkAnimatedIcons.searchIcon(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter { filled ->
    val animatedScale by animateFloatAsState(
        targetValue = if (filled) 0f else 1f,
        animationSpec = tween(durationMillis = 275, easing = FastOutSlowInEasing),
        label = "search_icon_scale",
    )
    val animatedRotation by animateFloatAsState(
        targetValue = if (filled) -90f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "search_icon_rotation",
    )

    rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ) { _, _ ->
        Path(
            pathData = addPathNodes(
                "M 10.5 3.95 C 8.763 3.95 7.096 4.641 5.868 5.868 C 4.641 7.096 3.95 8.763 3.95 10.5 C 3.95 12.237 4.641 13.904 5.868 15.132 C 7.096 16.359 8.763 17.05 10.5 17.05 C 12.237 17.05 13.904 16.359 15.132 15.132 C 16.359 13.904 17.05 12.237 17.05 10.5 C 17.05 8.763 16.359 7.096 15.132 5.868 C 13.904 4.641 12.237 3.95 10.5 3.95 M 2 10.5 C 2 8.247 2.896 6.083 4.49 4.49 C 6.083 2.896 8.247 2 10.5 2 C 12.753 2 14.917 2.896 16.51 4.49 C 18.104 6.083 19 8.247 19 10.5 C 19 12.753 18.104 14.917 16.51 16.51 C 14.917 18.104 12.753 19 10.5 19 C 8.247 19 6.083 18.104 4.49 16.51 C 2.896 14.917 2 12.753 2 10.5",
            ),
            fill = SolidColor(Color.Black),
            pathFillType = EvenOdd,
        )

        Path(
            pathData = addPathNodes(
                "M 15.13 15.13 C 15.313 14.948 15.562 14.846 15.82 14.846 C 16.078 14.846 16.327 14.948 16.51 15.13 L 21.71 20.33 C 21.879 20.47 21.996 20.663 22.042 20.877 C 22.088 21.092 22.061 21.315 21.965 21.513 C 21.869 21.71 21.71 21.869 21.513 21.965 C 21.315 22.061 21.092 22.088 20.877 22.042 C 20.663 21.996 20.47 21.879 20.33 21.71 L 15.13 16.51 C 14.948 16.327 14.846 16.078 14.846 15.82 C 14.846 15.562 14.948 15.313 15.13 15.13",
            ),
            fill = SolidColor(Color.Black),
            pathFillType = EvenOdd,
        )

        Group(
            scaleX = animatedScale,
            scaleY = animatedScale,
            rotation = animatedRotation,
            pivotX = 16f,
            pivotY = 16f,
        ) {
            Path(
                pathData = addPathNodes(
                    "M 15.75 10.5 C 15.75 11.892 15.196 13.228 14.212 14.212 C 13.228 15.196 11.892 15.75 10.5 15.75 C 9.108 15.75 7.772 15.196 6.788 14.212 C 5.804 13.228 5.25 11.892 5.25 10.5 C 5.25 9.108 5.804 7.772 6.788 6.788 C 7.772 5.804 9.108 5.25 10.5 5.25 C 11.892 5.25 13.228 5.804 14.212 6.788 C 15.196 7.772 15.75 9.108 15.75 10.5",
                ),
                fill = SolidColor(Color.Black),
            )
        }
    }
}

public fun SparkAnimatedIcons.bellShake(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter {
    val infiniteTransition = rememberInfiniteTransition(label = "bell_shake")
    val shakeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 800
                delayMillis = 5000
                0f at 0
                -15f at 100
                15f at 200
                -10f at 300
                10f at 400
                -5f at 500
                5f at 600
                0f at 800
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "shake_rotation",
    )

    val path = remember {
        addPathNodes(
            "M 40.78 13.86 L 40.78 13.87 C 38.93 12.01 36.68 10.64 34.22 9.86 C 34.12 7.23 31.97 5.13 29.32 5.13 C 26.66 5.13 24.5 7.23 24.41 9.86 C 21.96 10.64 19.7 12.01 17.85 13.86 C 14.81 16.92 13.1 21.06 13.1 25.36 C 13.1 31.91 12 35.52 10.86 37.62 C 9.95 39.29 9.5 41.35 10.26 43.19 C 11.09 45.25 13.06 46.24 15.27 46.24 L 22.46 46.24 C 22.96 47.42 23.75 48.46 24.77 49.25 C 26.07 50.25 27.67 50.8 29.31 50.8 C 30.96 50.8 32.56 50.25 33.86 49.25 C 34.88 48.45 35.68 47.42 36.16 46.24 C 38.56 46.24 40.96 46.24 43.36 46.24 C 45.56 46.24 47.54 45.24 48.38 43.19 C 49.13 41.35 48.68 39.29 47.78 37.61 C 46.63 35.51 45.53 31.91 45.53 25.36 C 45.53 21.06 43.83 16.92 40.78 13.86 M 37.44 17.19 C 35.28 15.02 32.36 13.81 29.32 13.81 C 26.27 13.81 23.35 15.02 21.19 17.19 C 19.03 19.36 17.82 22.29 17.82 25.36 C 17.82 32.41 16.63 36.86 15 39.86 C 14.48 40.82 14.59 41.34 14.62 41.41 C 14.66 41.43 14.84 41.52 15.27 41.52 L 43.35 41.52 C 43.78 41.52 43.97 41.43 44 41.42 L 44.01 41.4 C 44.04 41.34 44.16 40.82 43.63 39.86 C 42 36.86 40.81 32.4 40.81 25.36 C 40.81 22.3 39.6 19.36 37.44 17.19 Z",
        )
    }

    rememberVectorPainter(
        defaultWidth = 56.dp,
        defaultHeight = 56.dp,
        viewportWidth = 56f,
        viewportHeight = 56f,
        autoMirror = true,
    ) { _, _ ->
        Group(
            pivotX = 28f,
            pivotY = 20f,
            rotation = shakeRotation,
        ) {
            Path(
                pathData = path,
                fill = SolidColor(Color.Black),
                pathFillType = EvenOdd,
            )
        }
    }
}

public fun SparkAnimatedIcons.addButton(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter { filled ->
    val animatedScale by animateFloatAsState(
        targetValue = if (filled) 0f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "add_icon_scale",
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (filled) 0f else 1f,
        animationSpec = tween(durationMillis = 125, easing = FastOutSlowInEasing),
        label = "add_icon_alpha",
    )

    rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ) { _, _ ->
        val outlinePath = remember {
            addPathNodes(
                "M 7.16 2 C 5.792 2 4.479 2.544 3.511 3.511 C 2.544 4.479 2 5.792 2 7.16 L 2 16.84 C 2 17.524 2.136 18.194 2.393 18.815 C 2.65 19.435 3.028 20.005 3.511 20.489 C 4.479 21.456 5.792 22 7.16 22 L 16.84 22 C 17.524 22 18.194 21.864 18.815 21.607 C 19.435 21.35 20.005 20.972 20.489 20.489 C 21.456 19.521 22 18.208 22 16.84 L 22 7.16 C 22 6.476 21.864 5.806 21.607 5.185 C 21.35 4.565 20.972 3.995 20.489 3.511 C 19.521 2.544 18.208 2 16.84 2 Z M 4.02 7.16 C 4.02 6.728 4.107 6.315 4.266 5.94 C 4.424 5.564 4.654 5.226 4.938 4.941 C 5.221 4.657 5.559 4.427 5.935 4.267 C 6.311 4.108 6.725 4.02 7.16 4.02 C 8.773 4.02 10.387 4.02 12 4.02 C 13.613 4.02 15.227 4.02 16.84 4.02 C 17.273 4.02 17.685 4.107 18.06 4.266 C 18.436 4.424 18.774 4.654 19.059 4.938 C 19.343 5.221 19.573 5.559 19.733 5.935 C 19.892 6.311 19.98 6.725 19.98 7.16 C 19.98 8.773 19.98 10.387 19.98 12 C 19.98 13.613 19.98 15.227 19.98 16.84 C 19.98 17.273 19.892 17.685 19.734 18.06 C 19.576 18.436 19.346 18.774 19.063 19.059 C 18.779 19.343 18.441 19.573 18.065 19.733 C 17.689 19.892 17.275 19.98 16.84 19.98 C 15.227 19.98 13.613 19.98 12 19.98 C 10.387 19.98 8.773 19.98 7.16 19.98 C 6.328 19.98 5.528 19.649 4.94 19.06 C 4.743 18.864 4.576 18.644 4.44 18.408 C 4.303 18.172 4.198 17.918 4.128 17.655 C 4.057 17.391 4.02 17.117 4.02 16.84 C 4.02 15.227 4.02 13.613 4.02 12 C 4.02 10.387 4.02 8.773 4.02 7.16",
            )
        }
        val fillPath = remember {
            addPathNodes(
                "M 7 2 C 5.674 2 4.402 2.527 3.464 3.464 C 2.527 4.402 2 5.674 2 7 L 2 17 C 2 17.878 2.231 18.74 2.67 19.5 C 3.109 20.26 3.74 20.891 4.5 21.33 C 5.26 21.769 6.122 22 7 22 L 17 22 C 17.878 22 18.74 21.769 19.5 21.33 C 20.26 20.891 20.891 20.26 21.33 19.5 C 21.769 18.74 22 17.878 22 17 L 22 7 C 22 6.122 21.769 5.26 21.33 4.5 C 20.891 3.74 20.26 3.109 19.5 2.67 C 18.74 2.231 17.878 2 17 2 Z M 7.133 11.403 C 7.289 11.248 7.5 11.16 7.72 11.16 C 8.87 11.16 10.02 11.16 11.17 11.16 C 11.17 10.01 11.17 8.86 11.17 7.71 C 11.17 7.49 11.258 7.279 11.413 7.123 C 11.569 6.968 11.78 6.88 12 6.88 C 12.22 6.88 12.431 6.968 12.587 7.123 C 12.742 7.279 12.83 7.49 12.83 7.71 C 12.83 8.86 12.83 10.01 12.83 11.16 C 13.98 11.16 15.13 11.16 16.28 11.16 C 16.5 11.16 16.711 11.248 16.867 11.403 C 17.022 11.559 17.11 11.77 17.11 11.99 C 17.11 12.21 17.022 12.421 16.867 12.577 C 16.711 12.732 16.5 12.82 16.28 12.82 C 15.13 12.82 13.98 12.82 12.83 12.82 C 12.83 13.97 12.83 15.12 12.83 16.27 C 12.83 16.49 12.742 16.701 12.587 16.857 C 12.431 17.012 12.22 17.1 12 17.1 C 11.78 17.1 11.569 17.012 11.413 16.857 C 11.258 16.701 11.17 16.49 11.17 16.27 C 11.17 15.12 11.17 13.97 11.17 12.82 C 10.02 12.82 8.87 12.82 7.72 12.82 C 7.5 12.82 7.289 12.732 7.133 12.577 C 6.978 12.421 6.89 12.21 6.89 11.99 C 6.89 11.77 6.978 11.559 7.133 11.403",
            )
        }

        val currentPath by animatePathAsState(
            if (filled) fillPath else outlinePath,
        )

        Path(
            pathData = currentPath,
            fill = SolidColor(Color.Black),
            pathFillType = EvenOdd,
        )

        Group(
            scaleX = animatedScale,
            scaleY = animatedScale,
            pivotX = 12f,
            pivotY = 12f,
        ) {
            Path(
                pathData = addPathNodes(
                    "M 12 6.84 C 12.178 6.838 12.353 6.884 12.507 6.972 C 12.661 7.061 12.789 7.189 12.878 7.343 C 12.966 7.497 13.012 7.672 13.01 7.85 L 13.01 10.99 L 16.15 10.99 C 16.327 10.997 16.499 11.05 16.648 11.145 C 16.797 11.24 16.919 11.373 17 11.53 C 17.081 11.687 17.119 11.863 17.11 12.04 C 17.1 12.291 16.996 12.53 16.818 12.708 C 16.64 12.886 16.401 12.99 16.15 13 L 13.01 13 L 13.01 16.14 C 13.012 16.318 12.966 16.493 12.878 16.647 C 12.789 16.801 12.661 16.929 12.507 17.018 C 12.353 17.106 12.178 17.152 12 17.15 C 11.822 17.152 11.647 17.106 11.493 17.018 C 11.339 16.929 11.211 16.801 11.122 16.647 C 11.034 16.493 10.988 16.318 10.99 16.14 L 10.99 13 L 7.85 13 C 7.672 13.002 7.497 12.956 7.343 12.868 C 7.189 12.779 7.061 12.651 6.972 12.497 C 6.884 12.343 6.838 12.168 6.84 11.99 C 6.838 11.812 6.884 11.637 6.972 11.483 C 7.061 11.329 7.189 11.201 7.343 11.112 C 7.497 11.024 7.672 10.978 7.85 10.98 L 10.99 10.98 L 10.99 7.84 C 10.988 7.662 11.034 7.487 11.122 7.333 C 11.211 7.179 11.339 7.051 11.493 6.962 C 11.647 6.874 11.822 6.828 12 6.83 Z",
                ),
                fill = SolidColor(Color.Black),
                fillAlpha = animatedAlpha,
            )
        }
    }
}

public fun SparkAnimatedIcons.accountIcon(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter { filled ->
    rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ) { _, _ ->
        // Head path morphing
        val headOutlinePath = remember {
            addPathNodes(
                "M 6.77 7.03 C 6.77 4.26 9.09 2 12 2 C 14.91 2 17.23 4.27 17.23 7.03 C 17.23 9.79 14.91 12.06 12 12.06 C 9.09 12.06 6.77 9.79 6.77 7.03 M 12 4.19 C 10.4 4.19 9.07 5.45 9.07 7.03 C 9.07 8.61 10.4 9.87 12 9.87 C 13.6 9.87 14.93 8.61 14.93 7.03 C 14.93 5.45 13.6 4.19 12 4.19",
            )
        }
        val headFillPath = remember {
            addPathNodes(
                "M 6.91 7 C 6.91 4.24 9.19 2 12 2 C 14.81 2 17.09 4.24 17.09 7 C 17.09 9.76 14.81 12 12 12 C 9.19 12 6.91 9.76 6.91 7 M 12 7.03 C 12 7.03 12 7.03 12 7.03 C 12 7.03 12 7.03 12 7.03 C 12 7.03 12 7.03 12 7.03 C 12 7.03 12 7.03 12 7.03",
            )
        }

        val currentHeadPath by animatePathAsState(
            if (filled) headFillPath else headOutlinePath,
        )

        // Body path morphing
        val bodyOutlinePath = remember {
            addPathNodes(
                "M 18.59 16.01 C 19.28 16.671 19.855 17.436 20.295 18.272 C 20.695 19.031 20.984 19.848 21.15 20.7 C 21.27 21.3 20.85 21.87 20.23 21.98 C 19.936 22.039 19.63 21.98 19.379 21.815 C 19.128 21.651 18.953 21.393 18.89 21.1 C 18.625 19.745 17.947 18.505 16.95 17.55 C 15.62 16.273 13.844 15.562 12 15.57 C 10.15 15.57 8.37 16.28 7.05 17.55 C 6.053 18.505 5.375 19.745 5.11 21.1 C 4.99 21.7 4.39 22.09 3.77 21.98 C 3.576 21.951 3.393 21.87 3.24 21.747 C 3.088 21.623 2.971 21.461 2.902 21.277 C 2.834 21.092 2.816 20.893 2.85 20.7 C 3.203 18.912 4.097 17.274 5.41 16.01 C 7.069 14.408 9.301 13.47 11.655 13.386 C 11.77 13.382 11.885 13.38 12 13.38 C 14.327 13.38 16.549 14.217 18.26 15.708 C 18.372 15.806 18.482 15.906 18.59 16.01",
            )
        }
        val bodyFillPath = remember {
            addPathNodes(
                "M 18.23 14.89 C 19.17 15.602 19.927 16.527 20.439 17.59 C 20.95 18.652 21.201 19.821 21.17 21 C 21.17 21.55 20.71 22 20.15 22 C 19.983 22 19.815 22 19.648 22 C 19.254 22 18.859 22 18.465 22 C 17.803 22 17.141 22 16.479 22 C 15.169 22 13.859 22 12.549 22 C 11.202 22 9.855 22 8.507 22 C 7.249 22 5.991 22 4.732 22 C 4.438 22 4.144 22 3.85 22 C 3.551 22 3.28 21.872 3.093 21.668 C 2.93 21.491 2.83 21.256 2.83 21 C 2.811 19.836 3.068 18.684 3.579 17.638 C 4.09 16.592 4.84 15.681 5.77 14.98 C 7.5 13.64 9.77 12.99 11.99 12.97 C 11.99 12.97 11.99 12.97 11.99 12.97 C 14.21 12.95 16.48 13.57 18.23 14.9 C 18.23 14.897 18.23 14.893 18.23 14.89",
            )
        }

        val currentBodyPath by animatePathAsState(
            if (filled) bodyFillPath else bodyOutlinePath,
        )

        Path(
            pathData = currentBodyPath,
            fill = SolidColor(Color.Black),
            pathFillType = EvenOdd,
        )

        Group(
            pivotX = 12f,
            pivotY = 17f,
        ) {
            Path(
                pathData = currentHeadPath,
                fill = SolidColor(Color.Black),
            )
        }
    }
}

public fun SparkAnimatedIcons.messageIcon(): SparkIcon.AnimatedPainter = SparkIcon.AnimatedPainter { filled ->
    rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ) { _, _ ->
        val startPath = remember {
            addPathNodes(
                "M 2 6.18 C 2 3.84 3.9 2 6.21 2 L 17.71 2 C 18.461 2 19.199 2.198 19.85 2.573 C 20.501 2.949 21.041 3.489 21.417 4.14 C 21.792 4.791 21.99 5.529 21.99 6.28 L 21.99 20.98 C 21.989 21.231 21.895 21.474 21.726 21.66 C 21.557 21.847 21.325 21.964 21.075 21.99 C 20.824 22.016 20.573 21.948 20.37 21.8 L 16.71 19.06 L 6.3 19.06 C 5.169 19.06 4.082 18.612 3.281 17.813 C 2.479 17.015 2.025 15.931 2.02 14.8 C 2.01 12.28 2 8.77 2.01 6.17 Z M 6.08 13.64 C 6.08 14.21 6.54 14.66 7.1 14.66 L 13.63 14.66 C 13.899 14.66 14.142 14.559 14.323 14.393 C 14.436 14.29 14.524 14.161 14.581 14.015 C 14.626 13.899 14.65 13.773 14.65 13.64 C 14.65 13.07 14.19 12.62 13.63 12.62 L 7.521 12.62 L 7.31 12.62 C 7.24 12.62 7.17 12.62 7.1 12.62 C 6.53 12.62 6.08 13.08 6.08 13.64 M 16.9 8.2 L 7.1 8.2 C 6.884 8.201 6.674 8.133 6.499 8.006 C 6.324 7.88 6.194 7.701 6.127 7.496 C 6.06 7.291 6.06 7.069 6.127 6.864 C 6.194 6.659 6.324 6.48 6.499 6.354 C 6.674 6.227 6.884 6.159 7.1 6.16 L 16.9 6.16 C 17.46 6.16 17.92 6.61 17.92 7.18 C 17.92 7.465 17.808 7.72 17.624 7.904 C 17.44 8.088 17.185 8.2 16.9 8.2 M 16.9 11.39 L 7.1 11.39 C 6.884 11.391 6.674 11.323 6.499 11.196 C 6.324 11.07 6.194 10.891 6.127 10.686 C 6.06 10.481 6.06 10.259 6.127 10.054 C 6.194 9.849 6.324 9.67 6.499 9.544 C 6.674 9.417 6.884 9.349 7.1 9.35 L 16.9 9.35 C 17.46 9.35 17.92 9.8 17.92 10.37 C 17.92 10.655 17.808 10.91 17.624 11.094 C 17.44 11.277 17.185 11.39 16.9 11.39 M 10.365 13.635 C 10.365 13.635 10.365 13.635 10.365 13.635 L 10.365 13.635 C 10.365 13.635 10.365 13.635 10.365 13.635 C 10.365 13.635 10.365 13.635 10.365 13.635 L 10.365 13.635 C 10.365 13.635 10.365 13.635 10.365 13.635 L 10.365 13.635",
            )
        }
        val endPath = remember {
            addPathNodes(
                "M 2 6.18 C 2 3.84 3.9 2 6.22 2 L 17.72 2 C 18.507 2 19.244 2.213 19.879 2.585 C 20.513 2.957 21.043 3.487 21.415 4.121 C 21.787 4.756 22 5.493 22 6.28 L 22 13.63 C 22 16.08 22 18.53 22 20.98 C 22 21.37 21.78 21.72 21.44 21.89 C 21.1 22.06 20.68 22.03 20.38 21.79 L 16.71 19.06 L 6.3 19.06 C 5.12 19.06 4.053 18.585 3.279 17.816 C 2.505 17.047 2.025 15.985 2.02 14.81 C 2.013 11.933 2.007 9.057 2 6.18 Z M 4.05 14.8 C 4.05 16.03 5.05 17.03 6.3 17.03 L 17.05 17.03 C 17.27 17.03 17.48 17.1 17.65 17.23 C 18.423 17.807 19.197 18.383 19.97 18.96 C 19.97 14.733 19.97 10.507 19.97 6.28 C 19.97 5.04 18.97 4.03 17.72 4.03 L 6.22 4.03 L 6.22 4.03 C 5 4.03 4.04 5 4.03 6.18 C 4.03 8.78 4.03 12.28 4.05 14.8 M 16.9 8.2 L 12 8.2 C 10.367 8.2 8.733 8.2 7.1 8.2 C 6.913 8.2 6.738 8.149 6.587 8.06 C 6.436 7.971 6.309 7.844 6.22 7.693 C 6.131 7.542 6.08 7.367 6.08 7.18 C 6.08 6.62 6.54 6.17 7.1 6.17 L 16.9 6.17 C 17.46 6.17 17.92 6.62 17.92 7.18 C 17.92 7.74 17.46 8.2 16.9 8.2 C 16.9 8.2 16.9 8.2 16.9 8.2 M 16.9 11.38 L 12 11.38 C 10.367 11.38 8.733 11.38 7.1 11.38 C 6.913 11.38 6.738 11.33 6.587 11.243 C 6.436 11.155 6.309 11.03 6.22 10.881 C 6.131 10.731 6.08 10.557 6.08 10.37 C 6.08 9.81 6.54 9.35 7.1 9.35 L 16.9 9.35 C 17.46 9.35 17.92 9.81 17.92 10.37 C 17.92 10.93 17.46 11.38 16.9 11.38 C 16.9 11.38 16.9 11.38 16.9 11.38 M 6.08 13.63 C 6.08 13.07 6.54 12.62 7.1 12.62 L 13.63 12.62 C 14.19 12.62 14.65 13.07 14.65 13.63 C 14.65 14.19 14.19 14.65 13.63 14.65 L 7.1 14.65 C 6.54 14.65 6.08 14.19 6.08 13.63 L 6.08 13.63",
            )
        }

        val currentPath by animatePathAsState(
            if (filled) startPath else endPath,
        )

        Path(
            pathData = currentPath,
            fill = SolidColor(Color.Black),
            pathFillType = EvenOdd,
        )
    }
}

// TODO: Replace with internal annotation when core its moved to core module
// @InternalSparkApi
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
public data class AnimatedIconMetadata(
    val name: String,
    val iconProvider: () -> SparkIcon.AnimatedPainter,
    val navigationLabel: String? = null,
    val animateAtEnd: Boolean = false,
)

// TODO: Replace with internal annotation when core its moved to core module
// @InternalSparkApi
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
public val allAnimatedIconsMetadata: List<AnimatedIconMetadata> = listOf(
    AnimatedIconMetadata(
        name = "CollapseExpand",
        iconProvider = SparkAnimatedIcons::collapseExpand,
        navigationLabel = "Paramètres",
        animateAtEnd = true,
    ),
    AnimatedIconMetadata(
        name = "LikeHeart",
        iconProvider = SparkAnimatedIcons::likeHeart,
        navigationLabel = "Favoris",
        animateAtEnd = true,
    ),
    AnimatedIconMetadata(
        name = "Search",
        iconProvider = SparkAnimatedIcons::searchIcon,
        navigationLabel = "Rechercher",
        animateAtEnd = false,
    ),
    AnimatedIconMetadata(
        name = "BellShake",
        iconProvider = SparkAnimatedIcons::bellShake,
        navigationLabel = "Notifications",
        animateAtEnd = true,
    ),
    AnimatedIconMetadata(
        name = "AddButton",
        iconProvider = SparkAnimatedIcons::addButton,
        navigationLabel = "Publier",
        animateAtEnd = true,
    ),
    AnimatedIconMetadata(
        name = "Account",
        iconProvider = SparkAnimatedIcons::accountIcon,
        navigationLabel = "Compte",
        animateAtEnd = true,
    ),
    AnimatedIconMetadata(
        name = "Message",
        iconProvider = SparkAnimatedIcons::messageIcon,
        navigationLabel = "Messages",
        animateAtEnd = false,
    ),
)

// TODO: Replace with internal annotation when core its moved to core module
// @InternalSparkApi
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
public val allAnimatedIcons: Set<SparkIcon.AnimatedPainter> = allAnimatedIconsMetadata
    .map { it.iconProvider() }
    .toSet()
