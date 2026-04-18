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
package com.adevinta.spark.components.icons

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import com.adevinta.spark.icons.SparkIcon
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.jetbrains.compose.resources.painterResource

/**
 * A [Painter] that can be used to draw a [SparkIcon]. This will use the correct painter for whatever type the SparkIcon
 * use.
 * @param sparkIcon the icon to draw.
 * @param atEnd Whether the animated vector should be rendered at the end of all its animations.
 */
@Suppress("DEPRECATION")
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
public actual fun rememberSparkIconPainter(sparkIcon: SparkIcon, atEnd: Boolean): Painter = when (sparkIcon) {
    is SparkIcon.Vector -> rememberVectorPainter(sparkIcon.imageVector)

    is SparkIcon.DrawableRes -> rememberDrawablePainter(getDrawable(LocalContext.current, sparkIcon.drawableId))

    is SparkIcon.AnimatedDrawableRes -> {
        val icon = AnimatedImageVector.animatedVectorResource(sparkIcon.drawableId)
        rememberAnimatedVectorPainter(icon, atEnd)
    }

    is SparkIcon.AnimatedPainter -> sparkIcon.painterProvider(atEnd)

    is SparkIcon.Resource -> painterResource(sparkIcon.res)
}
