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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

private const val STROKE_WIDTH_FRACTION = 0.42f
private const val AMPLITUDE_FRACTION = 0.14f
private const val MIN_STROKE_WIDTH_PX = 3f

/**
 * Builds the squiggle points for each text line, one inner list per line.
 *
 * The placeholder box is split into [lineCount] equal horizontal bands, one per text line. Each
 * band holds one squiggle centred on the band. The seed is derived purely from the integer-pixel
 * dimensions and the line index, so two placeholders with identical width, height and line count
 * always produce the same geometry with no per-frame randomness, and each line differs from the
 * others.
 *
 * - The first and last points of a line sit on the band centre line.
 * - Interior points are displaced vertically by a seeded random fraction of the amplitude.
 * - Segment count grows with the band aspect ratio, so wider placeholders have more waves.
 */
internal fun buildSquigglePoints(widthPx: Float, heightPx: Float, lineCount: Int): List<List<Offset>> {
    val lines = max(1, lineCount)
    val band = heightPx / lines
    val amplitude = band * AMPLITUDE_FRACTION
    return List(lines) { line ->
        val seed = widthPx.roundToInt().toLong() * 100_000L + heightPx.roundToInt().toLong() * 31L + line
        val rng = Random(seed)
        val centreY = band * (line + 0.5f)
        val segmentCount = max(2, (widthPx / band).roundToInt())
        List(segmentCount + 1) { i ->
            val x = i * widthPx / segmentCount
            val y = when (i) {
                0, segmentCount -> centreY
                else -> centreY + (rng.nextFloat() * 2f - 1f) * amplitude
            }
            Offset(x, y)
        }
    }
}

/**
 * Builds a smooth [Path] through the squiggle points using quadratic Bézier midpoint subdivision.
 *
 * The path holds one squiggle sub-path per text line. Each point acts as a Bézier control point;
 * the midpoint between it and its successor is the on-curve destination. Each line finishes with a
 * straight segment to its last point, which sits on the band centre line.
 */
internal fun buildSquigglePath(widthPx: Float, heightPx: Float, lineCount: Int): Path {
    val lines = buildSquigglePoints(widthPx, heightPx, lineCount)
    return Path().apply {
        for (points in lines) {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val midX = (points[i].x + points[i + 1].x) / 2f
                val midY = (points[i].y + points[i + 1].y) / 2f
                quadraticTo(points[i].x, points[i].y, midX, midY)
            }
            lineTo(points.last().x, points.last().y)
        }
    }
}

/**
 * Returns a [Stroke] proportional to one text line height, with rounded caps and joins to
 * reinforce the hand-drawn character of the squiggle.
 */
internal fun buildSquiggleStroke(lineHeightPx: Float): Stroke = Stroke(
    width = max(MIN_STROKE_WIDTH_PX, lineHeightPx * STROKE_WIDTH_FRACTION),
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
)
