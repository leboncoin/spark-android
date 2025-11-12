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
package com.adevinta.spark.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
/**
 * A unified representation of different icon types used in the Spark design system.
 *
 * This sealed interface provides a type-safe way to handle various icon formats,
 * enabling consistent icon management across static and animated variants.
 * The different implementations support different rendering approaches and animation capabilities.
 *
 * It also allows spark components to enforce consumers to use the icons from the design system.
 */
public sealed interface SparkIcon {

    /**
     * An icon represented by an android drawable resource.
     *
     * It should be a vector drawable rather than a raster image/icon.
     *
     * @property drawableId The resource ID of the drawable to display
     */
    public data class DrawableRes(@androidx.annotation.DrawableRes val drawableId: Int) : SparkIcon

    /**
     * An icon represented by an android animated vector drawable.
     *
     * This supports Android's native animated vector drawables for complex
     * animations defined in XML resources.
     *
     * @property drawableId The resource ID of the animated drawable to display
     */
    @Deprecated(
        message = "AnimatedPainter will replace AnimatedDrawableRes",
        replaceWith = ReplaceWith(
            "SparkIcon.AnimatedPainter",
            "com.adevinta.spark.icons.SparkIcon.AnimatedPainter",
        ),
    )
    public data class AnimatedDrawableRes(@androidx.annotation.DrawableRes val drawableId: Int) : SparkIcon

    /**
     * An icon represented by a Compose [ImageVector].
     *
     * This is ideal for programmatically generated or Compose-native vector graphics
     * that don't require animation.
     *
     * @property imageVector The ImageVector containing the vector path data
     */
    public data class Vector(val imageVector: ImageVector) : SparkIcon

    /**
     * An icon represented by a composable painter provider.
     *
     * This enables custom animated icons through Compose's animation system,
     * providing maximum flexibility for complex animations and state-driven visuals.
     * Particularly useful for Kotlin Multiplatform compatibility.
     *
     * @property painterProvider A composable function that returns a [Painter], receiving an `atEnd` parameter to control animation state
     */
    public data class AnimatedPainter(val painterProvider: @Composable (atEnd: Boolean) -> Painter) : SparkIcon
}
