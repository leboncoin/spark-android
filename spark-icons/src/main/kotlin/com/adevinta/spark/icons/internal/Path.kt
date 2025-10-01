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
package com.adevinta.spark.icons.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.util.lerp

@Composable
internal fun animatePathAsState(
    path: String,
): State<List<PathNode>> = animatePathAsState(remember(path) { addPathNodes(path) })

@Composable
internal fun animatePathAsState(path: List<PathNode>): State<List<PathNode>> {
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
