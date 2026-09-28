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
package com.adevinta.spark.catalog.examples.samples.tokens.shapes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text

@Composable
internal fun ShapeSample() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Column {
            ShapeItem(
                shape = SparkTheme.shapes.none,
                text = "none",
            )
            ShapeItem(
                shape = SparkTheme.shapes.small,
                text = "small",
            )
            ShapeItem(
                shape = SparkTheme.shapes.large,
                text = "large",
            )
        }
        Column {
            ShapeItem(
                shape = SparkTheme.shapes.extraSmall,
                text = "extraSmall",
            )

            ShapeItem(
                shape = SparkTheme.shapes.medium,
                text = "medium",
            )

            ShapeItem(
                shape = SparkTheme.shapes.extraLarge,
                text = "extraLarge",
            )
        }
        ShapeItem(
            shape = SparkTheme.shapes.full,
            text = "full",
        )
    }
}

@Composable
private fun ShapeItem(
    shape: Shape,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(104.dp)
            .padding(8.dp),
        elevation = 6.dp,
        shape = shape,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = SparkTheme.typography.caption,
            )
        }
    }
}

@Preview
@Composable
private fun ShapeSamplePreview() {
    PreviewTheme {
        ShapeSample()
    }
}
