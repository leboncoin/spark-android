/*
 * Copyright (c) 2026 Adevinta
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

package com.adevinta.spark.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme

internal class CardHighlightShape(private val cornerSize: CornerSize) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Generic(
            path = drawTicketPath(cornerSize = cornerSize, size = size, density = density),
        )
    }
}

private fun drawTicketPath(cornerSize: CornerSize, size: Size, density: Density): Path {
    return Path().apply {
        val cornerRadius = cornerSize.toPx(size, density)
        // Top left arc
        arcTo(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = cornerRadius,
                bottom = cornerRadius,
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = 90.0f,
            forceMoveTo = false,
        )
        lineTo(x = size.width - cornerRadius, y = 0f)
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = 0f,
                right = size.width,
                bottom = cornerRadius,
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = 90.0f,
            forceMoveTo = false,
        )
        lineTo(x = size.width, y = size.height - cornerRadius)
        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius*2,
                top = size.height - cornerRadius / 2,
                right = size.width,
                bottom = size.height + cornerRadius / 2,
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false,
        )
        lineTo(x = cornerRadius, y = size.height - cornerRadius / 2)
        // Bottom left arc
        arcTo(
            rect = Rect(
                left = 0f,
                top = size.height - cornerRadius / 2,
                right = cornerRadius*2,
                bottom = size.height + cornerRadius / 2,
            ),
            startAngleDegrees = -90.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false,
        )
        lineTo(x = 0f, y = cornerRadius)

        close()
    }
}

@Preview
@Composable
private fun PreviewHighlightCardShape() {
    PreviewTheme {
        Box(
            modifier = Modifier.height(32.dp)
                .clip(SparkTheme.shapes.small)
                .background(SparkTheme.colors.accent)
        ) {
            Box( modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(8.dp)
                .clip(CardHighlightShape(CornerSize(8.dp)))
                .background(SparkTheme.colors.main)
            )
        }

        Box(
            modifier = Modifier.height(48.dp)
                .clip(SparkTheme.shapes.large)
                .background(SparkTheme.colors.accent)
        ) {
            Box( modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(16.dp)
                .clip(CardHighlightShape(CornerSize(16.dp)))
                .background(SparkTheme.colors.main)
            )
        }

        Box(
            modifier = Modifier.height(64.dp)
                .clip(SparkTheme.shapes.extraLarge)
                .background(SparkTheme.colors.accent)

        ) {
            Box( modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(28.dp)
                .clip(CardHighlightShape(CornerSize(28.dp)))
                .background(SparkTheme.colors.main)
            )
        }
    }
}
