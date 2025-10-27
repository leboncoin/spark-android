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
package com.adevinta.spark.components.image

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.icons.rememberSparkIconPainter
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.StoreCriteria
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import androidx.compose.foundation.Image as FoundationImage

@InternalSparkApi
@Composable
internal fun SparkIllustration(
    data: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    when (data) {
        is ImageBitmap -> FoundationImage(
            bitmap = data,
            contentDescription = contentDescription,
            modifier = modifier.sparkUsageOverlay(),
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )

        is ImageVector -> FoundationImage(
            imageVector = data,
            contentDescription = contentDescription,
            modifier = modifier.sparkUsageOverlay(),
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )

        is Painter -> FoundationImage(
            painter = data,
            contentDescription = contentDescription,
            modifier = modifier.sparkUsageOverlay(),
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )

        is SparkIcon -> FoundationImage(
            painter = rememberSparkIconPainter(data),
            modifier = modifier.sparkUsageOverlay(),
            contentDescription = contentDescription,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )
    }
}

/**
 * A composable that lays out and draws a given [ImageBitmap]. This will attempt to
 * size the composable according to the [ImageBitmap]'s given width and height. However, an
 * optional [Modifier] parameter can be provided to adjust sizing or draw additional content (ex.
 * background). Any unspecified dimension will leverage the [ImageBitmap]'s size as a minimum
 * constraint.
 *
 * @param bitmap The [ImageBitmap] to draw
 * @param contentDescription text used by accessibility services to describe what this image
 * represents. This should always be provided unless this image is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content (ex.
 * background)
 * @param alignment Optional alignment parameter used to place the [ImageBitmap] in the given
 * bounds defined by the width and height
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be used
 * if the bounds are a different size from the intrinsic size of the [ImageBitmap]
 * @param alpha Optional opacity to be applied to the [ImageBitmap] when it is rendered onscreen
 * @param colorFilter Optional ColorFilter to apply for the [ImageBitmap] when it is rendered
 * onscreen
 * @param filterQuality Sampling algorithm applied to the [bitmap] when it is scaled and drawn
 * into the destination. The default is [FilterQuality.Low] which scales using a bilinear
 * sampling algorithm
 */
@Composable
public fun Illustration(
    bitmap: ImageBitmap,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    SparkIllustration(
        data = bitmap,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
    )
}

/**
 * A composable that lays out and draws a given [ImageVector]. This will attempt to
 * size the composable according to the [ImageVector]'s given width and height. However, an
 * optional [Modifier] parameter can be provided to adjust sizing or draw additional content (ex.
 * background). Any unspecified dimension will leverage the [ImageVector]'s size as a minimum
 * constraint.
 *
 * @param imageVector The [ImageVector] to draw
 * @param contentDescription text used by accessibility services to describe what this image
 * represents. This should always be provided unless this image is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content (ex.
 * background)
 * @param alignment Optional alignment parameter used to place the [ImageVector] in the given
 * bounds defined by the width and height
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be used
 * if the bounds are a different size from the intrinsic size of the [ImageVector]
 * @param alpha Optional opacity to be applied to the [ImageVector] when it is rendered onscreen
 * @param colorFilter Optional ColorFilter to apply for the [ImageVector] when it is rendered
 * onscreen
 */
@Composable
public fun Illustration(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    SparkIllustration(
        data = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

/**
 * Creates a composable that lays out and draws a given [Painter]. This will attempt to size
 * the composable according to the [Painter]'s intrinsic size. However, an optional [Modifier]
 * parameter can be provided to adjust sizing or draw additional content (ex. background)
 *
 * **NOTE** a Painter might not have an intrinsic size, so if no LayoutModifier is provided
 * as part of the Modifier chain this might size the [Image] composable to a width and height
 * of zero and will not draw any content. This can happen for Painter implementations that
 * always attempt to fill the bounds like [ColorPainter]
 *
 * @param painter to draw
 * @param contentDescription text used by accessibility services to describe what this image
 * represents. This should always be provided unless this image is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content (ex.
 * background)
 * @param alignment Optional alignment parameter used to place the [Painter] in the given
 * bounds defined by the width and height.
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be used
 * if the bounds are a different size from the intrinsic size of the [Painter]
 * @param alpha Optional opacity to be applied to the [Painter] when it is rendered onscreen
 * the default renders the [Painter] completely opaque
 * @param colorFilter Optional colorFilter to apply for the [Painter] when it is rendered onscreen
 */
@Composable
public fun Illustration(
    painter: Painter?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    SparkIllustration(
        data = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

/**
 * Creates a composable that lays out and draws a given [android.graphics.drawable.Drawable]. This will attempt to size
 * the composable according to the [android.graphics.drawable.Drawable]'s intrinsic size. However, an
 * optional [Modifier] parameter can be provided to adjust sizing or draw additional content (ex. background)
 *
 * @param drawableRes to draw
 * @param contentDescription text used by accessibility services to describe what this image
 * represents. This should always be provided unless this image is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content (ex.
 * background)
 * @param alignment Optional alignment parameter used to place the [Painter] in the given
 * bounds defined by the width and height.
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be used
 * if the bounds are a different size from the intrinsic size of the [Painter]
 * @param alpha Optional opacity to be applied to the [Painter] when it is rendered onscreen
 * the default renders the [Painter] completely opaque
 * @param colorFilter Optional colorFilter to apply for the [Painter] when it is rendered onscreen
 */
@Composable
public fun Illustration(
    @DrawableRes drawableRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    val drawable = AppCompatResources.getDrawable(LocalContext.current, drawableRes)
    val painter = rememberDrawablePainter(drawable)

    SparkIllustration(
        data = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

/**
 * Creates a composable that lays out and draws a given [SparkIcon]. This will attempt to size
 * the composable according to the [SparkIcon]'s intrinsic size (should be already normalized but some exception may
 * still exist). However, an optional [Modifier] parameter can be provided to adjust sizing or draw additional
 * content (ex. background)
 *
 * @param sparkIcon to draw
 * @param contentDescription text used by accessibility services to describe what this image
 * represents. This should always be provided unless this image is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content (ex.
 * background)
 * @param alignment Optional alignment parameter used to place the [Painter] in the given
 * bounds defined by the width and height.
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be used
 * if the bounds are a different size from the intrinsic size of the [Painter]
 * @param alpha Optional opacity to be applied to the [Painter] when it is rendered onscreen
 * the default renders the [Painter] completely opaque
 * @param colorFilter Optional colorFilter to apply for the [Painter] when it is rendered onscreen
 */
@Composable
public fun Illustration(
    sparkIcon: SparkIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    SparkIllustration(
        data = sparkIcon,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

@Preview(
    group = "Images",
    name = "Illustration",
)
@Composable
internal fun IllustrationPreview() {
    PreviewTheme {
        Illustration(
            sparkIcon = SparkIcons.StoreCriteria,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )
    }
}
