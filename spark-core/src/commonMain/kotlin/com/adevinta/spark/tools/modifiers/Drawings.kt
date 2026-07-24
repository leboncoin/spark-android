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
package com.adevinta.spark.tools.modifiers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSimple
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme

/**
 * Modify element to add border with appearance specified with a [border] and a [shape], pad the
 * content by the [BorderStroke.width] and clip it.
 *
 * @param border [BorderStroke] class that specifies border appearance, such as size and color
 * @param shape shape of the border
 */
public fun Modifier.dashedBorder(border: BorderStroke, shape: Shape = RectangleShape): Modifier =
    dashedBorder(width = border.width, brush = border.brush, shape = shape)

/**
 * Returns a [Modifier] that adds border with appearance specified with [width], [color] and a
 * [shape], pads the content by the [width] and clips it.
 *
 * @param width width of the border. Use [Dp.Hairline] for a hairline border.
 * @param color color to paint the border with
 * @param shape shape of the border
 */
public fun Modifier.dashedBorder(width: Dp, color: Color, shape: Shape = RectangleShape): Modifier =
    dashedBorder(width = width, brush = SolidColor(color), shape = shape)

/**
 * Add a dashed border on a Composable
 */
private fun Modifier.dashedBorder(width: Dp, brush: Brush, shape: Shape): Modifier = drawWithCache {
    val on = 2.0.dp.toPx()
    val off = 2.0.dp.toPx()
    val outline: Outline = shape.createOutline(size, layoutDirection, this)
    var insetOutline: Outline? = null // outline used for roundrect/generic shapes
    var stroke: Stroke? = null // stroke to draw border for all outline types
    var pathClip: Path? = null // path to clip roundrect/generic shapes
    var inset = 0f // inset to translate before drawing the inset outline
    // path to draw generic shapes or roundrects with different corner radii
    var insetPath: Path? = null
    val borderSize = if (width == Dp.Hairline) 1f else width.toPx()

    if (borderSize > 0 && size.minDimension > 0f) {
        if (outline is Outline.Rectangle) {
            stroke = Stroke(
                borderSize,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(on, off),
                ),
            )
        } else {
            // Multiplier to apply to the border size to get a stroke width that is
            // large enough to cover the corners while not being too large to overly
            // square off the internal shape. The resultant shape will be
            // clipped to the desired shape. Any value lower will show artifacts in
            // the corners of shapes. A value too large will always square off
            // the internal shape corners. For example, for a rounded rect border
            // a large multiplier will always have squared off edges within the
            // inner section of the stroke, however, having a smaller multiplier
            // will still keep the rounded effect for the inner section of the
            // border
            val strokeWidth = 1.2f * borderSize
            inset = borderSize - strokeWidth / 2
            val insetSize = Size(
                size.width - inset * 2,
                size.height - inset * 2,
            )
            insetOutline = shape.createOutline(insetSize, layoutDirection, this)
            stroke = Stroke(
                strokeWidth,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(on, off),
                ),
            )
            pathClip = if (outline is Outline.Rounded) {
                Path().apply { addRoundRect(outline.roundRect) }
            } else if (outline is Outline.Generic) {
                outline.path
            } else {
                // should not get here because we check for Outline.Rectangle
                // above
                null
            }

            insetPath =
                if (insetOutline is Outline.Rounded &&
                    !insetOutline.roundRect.isSimple
                ) {
                    // Rounded rect with non equal corner radii needs a path
                    // to be pre-translated
                    Path().apply {
                        addRoundRect(insetOutline.roundRect)
                        translate(Offset(inset, inset))
                    }
                } else if (insetOutline is Outline.Generic) {
                    // Generic paths must be created and pre-translated
                    Path().apply {
                        addPath(insetOutline.path, Offset(inset, inset))
                    }
                } else {
                    // Drawing a round rect with equal corner radii without
                    // usage of a path
                    null
                }
        }
    }

    onDrawWithContent {
        drawContent()
        // Only draw the border if a have a valid stroke parameter. If we have
        // an invalid border size we will just draw the content
        if (stroke != null) {
            if (insetOutline != null && pathClip != null) {
                val isSimpleRoundRect = insetOutline is Outline.Rounded &&
                    insetOutline.roundRect.isSimple
                withTransform(
                    {
                        clipPath(pathClip)
                        // we are drawing the round rect not as a path so we must
                        // translate ourselves othe
                        if (isSimpleRoundRect) {
                            translate(inset, inset)
                        }
                    },
                ) {
                    if (isSimpleRoundRect) {
                        // If we don't have an insetPath then we are drawing
                        // a simple round rect with the corner radii all identical
                        val rrect = insetOutline.roundRect
                        drawRoundRect(
                            brush = brush,
                            topLeft = Offset(rrect.left, rrect.top),
                            size = Size(rrect.width, rrect.height),
                            cornerRadius = rrect.topLeftCornerRadius,
                            style = stroke,
                        )
                    } else if (insetPath != null) {
                        drawPath(insetPath, brush, style = stroke)
                    }
                }
                // Clip rect to ensure the stroke does not extend the bounds
                // of the composable.
                clipRect {
                    // Draw a hairline stroke to cover up non-anti-aliased pixels
                    // generated from the clip
                    if (isSimpleRoundRect) {
                        val rrect = (outline as Outline.Rounded).roundRect
                        drawRoundRect(
                            brush = brush,
                            alpha = 0f,
                            topLeft = Offset(rrect.left, rrect.top),
                            size = Size(rrect.width, rrect.height),
                            cornerRadius = rrect.topLeftCornerRadius,
                            style = Stroke(
                                Stroke.HairlineWidth,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(on, off),
                                ),
                            ),
                        )
                    } else {
                        drawPath(
                            pathClip,
                            brush = brush,
                            alpha = 0f,
                            style = Stroke(
                                Stroke.HairlineWidth,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(on, off),
                                ),
                            ),
                        )
                    }
                }
            } else {
                // Rectangular border fast path
                val strokeWidth = stroke.width
                val halfStrokeWidth = strokeWidth / 2
                drawRect(
                    brush = brush,
                    topLeft = Offset(halfStrokeWidth, halfStrokeWidth),
                    size = Size(
                        size.width - strokeWidth,
                        size.height - strokeWidth,
                    ),
                    style = stroke,
                )
            }
        }
    }
}

@Preview
@Composable
private fun DashedBorderPreview() {
    PreviewTheme {
        val modifier = Modifier.dashedBorder(2.dp, shape = SparkTheme.shapes.small, color = SparkTheme.colors.main)
        Box(
            modifier = modifier
                .size(100.dp)
                .background(Color.White),
        )
    }
}
