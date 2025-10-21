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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.ifTrue
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import androidx.compose.material3.Icon as MaterialIcon

/**
 * Icon component that draws [sparkIcon] using [tint], defaulting to [LocalContentColor]. For a
 * clickable icon, see [IconButton].
 *
 * @param sparkIcon [SparkIcon] to draw inside this Icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier optional [Modifier] for this Icon
 * @param tint to be applied to [sparkIcon]. If no tint is provided, then a default is used.
 * @param size one of [IconSize] to be applied as size of the icon.
 * If no size is provided the default [IconSize.Medium] is used.
 * @param atEnd Whether the animated vector should be rendered at the end of all its animations.
 */
@Composable
public fun Icon(
    sparkIcon: SparkIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = IconDefaults.intent.color(),
    size: IconSize? = null,
    atEnd: Boolean = false,
) {
    MaterialIcon(
        painter = rememberSparkIconPainter(sparkIcon, atEnd),
        contentDescription = contentDescription,
        modifier = modifier
            .ifNotNull(size) {
                size(it.size)
            },
        tint = tint,
    )
}

/**
 * Icon component that draws [imageVector] using [tint], defaulting to [LocalContentColor]. For a
 * clickable icon, see [IconButton].
 *
 * @param imageVector [ImageVector] to draw inside this icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier optional [Modifier] for this Icon
 * @param tint to be applied to [imageVector]. If no intent is provided, then a default is used.
 * @param size one of [IconSize] to be applied as size of the icon.
 * If no size is provided the default [IconSize.Medium] is used.
 */
@Composable
public fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = IconDefaults.intent.color(),
    size: IconSize? = null,
) {
    MaterialIcon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier
            .ifNotNull(size) {
                size(it.size)
            },
        tint = tint,
    )
}

/**
 * Icon component that draws [bitmap] using [tint], defaulting to [LocalContentColor]. For a
 * clickable icon, see [IconButton].
 *
 * @param bitmap [ImageBitmap] to draw inside this icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier optional [Modifier] for this Icon
 * @param tint to be applied to [bitmap]. If no intent is provided, then a default is used
 * @param size one of [IconSize] to be applied as size of the icon.
 * If no size is provided the default [IconSize.Medium] is used.
 */
@Composable
public fun Icon(
    bitmap: ImageBitmap,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = IconDefaults.intent.color(),
    size: IconSize? = null,
) {
    MaterialIcon(
        bitmap = bitmap,
        contentDescription = contentDescription,
        modifier = modifier
            .ifNotNull(size) {
                size(it.size)
            },
        tint = tint,
    )
}

/**
 * Icon component that draws [painter] using [tint], defaulting to [LocalContentColor]. For a
 * clickable icon, see [IconButton].
 *
 * @param painter [Painter] to draw inside this icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier optional [Modifier] for this Icon
 * @param tint to be applied to [painter]. If no intent is provided, then a default is used
 * @param size one of [IconSize] to be applied as size of the icon.
 * If no size is provided the default [IconSize.Medium] is used.
 */
@Composable
public fun Icon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = IconDefaults.intent.color(),
    size: IconSize? = null,
) {
    MaterialIcon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .ifNotNull(size) {
                size(it.size)
            },
        tint = tint,
    )
}

/**
 * A [Painter] that can be used to draw a [SparkIcon]. This will use the correct painter for whatever type the SparkIcon
 * use.
 * @param sparkIcon the icon to draw.
 * @param atEnd Whether the animated vector should be rendered at the end of all its animations.
 */
@Suppress("DEPRECATION")
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
public fun rememberSparkIconPainter(sparkIcon: SparkIcon, atEnd: Boolean = false): Painter = when (sparkIcon) {
    is SparkIcon.Vector -> rememberVectorPainter(sparkIcon.imageVector)
    is SparkIcon.DrawableRes -> rememberDrawablePainter(getDrawable(LocalContext.current, sparkIcon.drawableId))
    is SparkIcon.AnimatedDrawableRes -> {
        val icon = AnimatedImageVector.animatedVectorResource(sparkIcon.drawableId)
        rememberAnimatedVectorPainter(icon, atEnd)
    }
    is SparkIcon.AnimatedPainter -> sparkIcon.painterProvider(atEnd)
}

@PreviewLightDark
@Composable
private fun IconPreview() {
    PreviewTheme {
        IconSize.entries.map { it to IconIntent.entries.toTypedArray() }.forEach { (size, intents) ->
            LazyRow {
                items(
                    intents.count(),
                    itemContent = { index ->
                        Box(
                            modifier = Modifier.ifTrue(intents[index] == IconIntent.Surface) {
                                background(SparkTheme.colors.neutralContainer)
                            },
                        ) {
                            Icon(
                                sparkIcon = SparkIcons.Check,
                                tint = intents[index].color(),
                                contentDescription = "Done",
                                size = size,
                            )
                        }
                    },
                )
            }
        }
    }
}
