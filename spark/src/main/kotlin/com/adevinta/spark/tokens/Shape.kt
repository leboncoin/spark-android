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
package com.adevinta.spark.tokens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import androidx.compose.material3.Shapes as Material3Shapes

/**
 *
 * Components are grouped into shape categories based on their size. These categories provide a
 * way to change multiple component values at once, by changing the category’s values.
 * SparkShapes categories include:
 * - Small components
 * - Medium components
 * - Large components
 *
 * @param none A shape style with 4 same-sized corners whose size are equal to [RectangleShape].
 * By default app bars, navigation bars, banners, full-screen dialogs, and navigation rails use this shape.
 * @param extraSmall A shape style with 4 same-sized corners whose size are bigger than
 * [RectangleShape] and smaller than [SparkShapes.small]. By default, autocomplete menu, select menu,
 * snackbars, standard menu, and text fields use this shape.
 * @param small A shape style with 4 same-sized corners whose size are bigger than
 * [SparkShapes.extraSmall] and smaller than [SparkShapes.medium]. By default, chips use this shape.
 * @param medium A shape style with 4 same-sized corners whose size are bigger than [SparkShapes.small]
 * and smaller than [SparkShapes.large]. By default, cards and small FABs use this shape.
 * @param large A shape style with 4 same-sized corners whose size are bigger than [SparkShapes.medium]
 * and smaller than [SparkShapes.extraLarge]. By default, extended FABs, FABs, and navigation drawers use
 * this shape.
 * @param extraLarge A shape style with 4 same-sized corners whose size are bigger than
 * [SparkShapes.large] and smaller than [CircleShape]. By default, large FABs, bottom sheets, dialogs, and time picker
 * use this shape.
 * @param full A shape style with 4 same-sized corners whose size are equal to [CircleShape]. By default, large badges,
 * buttons, icon buttons, sliders, switches, and search bar use this shape.
 */
@Immutable
public data class SparkShapes(
    @Order(0) val none: CornerBasedShape = RoundedCornerShape(0.dp),
    @Order(1) val extraSmall: CornerBasedShape = RoundedCornerShape(4.0.dp),
    @Order(2) val small: CornerBasedShape = RoundedCornerShape(8.0.dp),
    @Order(3) val medium: CornerBasedShape = RoundedCornerShape(12.0.dp),
    @Order(4) val large: CornerBasedShape = RoundedCornerShape(16.0.dp),
    @Order(5) val extraLarge: CornerBasedShape = RoundedCornerShape(28.0.dp),
    @Order(6) val full: CornerBasedShape = CircleShape,
)

public fun sparkShapes(
    none: CornerBasedShape = RoundedCornerShape(0.dp),
    extraSmall: CornerBasedShape = RoundedCornerShape(4.0.dp),
    small: CornerBasedShape = RoundedCornerShape(8.0.dp),
    medium: CornerBasedShape = RoundedCornerShape(12.0.dp),
    large: CornerBasedShape = RoundedCornerShape(16.0.dp),
    extraLarge: CornerBasedShape = RoundedCornerShape(28.0.dp),
    full: CornerBasedShape = CircleShape,
): SparkShapes = SparkShapes(
    none = none,
    extraSmall = extraSmall,
    small = small,
    medium = medium,
    large = large,
    extraLarge = extraLarge,
    full = full,
)

public fun SparkShapes.asMaterial3Shapes(): Material3Shapes = Material3Shapes(
    extraSmall = extraSmall,
    small = small,
    medium = medium,
    large = large,
    extraLarge = extraLarge,
)

/**
 * Extension property to get a [CornerBasedShape] with a bottom start of 0.dp
 */
public val CornerBasedShape.highlight: CornerBasedShape
    get() = this.copy( bottomStart= CornerSize(0.dp))

/**
 * CompositionLocal used to specify the default shapes for the surfaces.
 */
internal val LocalSparkShapes = staticCompositionLocalOf { SparkShapes() }

@Preview(
    group = "Tokens",
    name = "Shapes",
)
@Composable
internal fun ShapePreview() {
    PreviewTheme {
        Row {
            Column {
                ShapeItem(
                    modifier = Modifier,
                    shape = SparkTheme.shapes.none,
                    text = "none",
                )
                ShapeItem(
                    modifier = Modifier,
                    shape = SparkTheme.shapes.small,
                    text = "small",
                )
                ShapeItem(
                    modifier = Modifier,
                    shape = SparkTheme.shapes.large,
                    text = "large",
                )
            }
            Column {
                ShapeItem(
                    modifier = Modifier,
                    shape = SparkTheme.shapes.extraSmall,
                    text = "extraSmall",
                )

                ShapeItem(
                    modifier = Modifier,
                    shape = SparkTheme.shapes.medium,
                    text = "medium",
                )

                ShapeItem(
                    modifier = Modifier,
                    shape = SparkTheme.shapes.extraLarge,
                    text = "extraLarge",
                )
            }
        }
        ShapeItem(
            modifier = Modifier,
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
            .size(96.dp)
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
