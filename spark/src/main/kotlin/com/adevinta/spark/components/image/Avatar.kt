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
package com.adevinta.spark.components.image

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.R
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Namespace for the avatar variants.
 *
 * An avatar is a picture that identifies a person or a company. When no image loads, it shows a
 * placeholder. An optional addon, for example an online indicator, sits on the avatar.
 *
 * ![Variants](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_AvatarDocumentationScreenshots_variants.png)
 */
@Immutable
public object Avatar

/**
 * The size of an avatar, from [XS] (24dp) to [XXXL] (128dp).
 *
 * Each size sets the avatar diameter and the matching online badge and stroke sizes.
 *
 * @param imageSize size of the image in [Dp]
 * @param badgeSize size of online badge in [Dp]
 * @param borderSize The indicator border size in [Dp]. Must be set explicitly because the border mechanism differs
 * from the Figma spec.
 */
public enum class AvatarSize(public val imageSize: Dp, public val badgeSize: Dp, public val borderSize: Dp) {
    XS(imageSize = 24.dp, badgeSize = 2.dp, borderSize = 1.dp),
    SM(imageSize = 32.dp, badgeSize = 8.dp, borderSize = 1.dp),
    MD(imageSize = 40.dp, badgeSize = 8.dp, borderSize = 1.dp),
    LG(imageSize = 56.dp, badgeSize = 16.dp, borderSize = 2.dp),
    XL(imageSize = 64.dp, badgeSize = 16.dp, borderSize = 2.dp),
    XXL(imageSize = 96.dp, badgeSize = 16.dp, borderSize = 2.dp),
    XXXL(imageSize = 128.dp, badgeSize = 16.dp, borderSize = 2.dp),
}

/**
 * Where [SparkAvatar] places its addon.
 */
internal enum class AvatarAddonPlacement {
    /** Centred on the circle rim at 45 degrees (bottom-right). The addon can overflow the avatar bounds. */
    Rim,

    /** Inside the avatar bounds, aligned to the bottom-end corner. The addon never overflows. */
    BottomEnd,
}

@InternalSparkApi
@Composable
internal fun SparkAvatar(
    shape: Shape,
    addonPlacement: AvatarAddonPlacement,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    // Useful to preview different states
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    size: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    Layout(
        modifier = modifier.sparkUsageOverlay(),
        content = {
            SparkImage(
                modifier = Modifier
                    .then(if (fillParentSize) Modifier.fillMaxSize() else Modifier.size(size.imageSize))
                    .aspectRatio(1f)
                    .clip(shape),
                model = model,
                transform = transform,
                contentDescription = stringResource(id = R.string.spark_user_avatar_content_description),
                contentScale = contentScale,
                emptyIcon = placeholder,
                errorIcon = placeholder,
            )
            AvatarAddonScopeImpl(size).addon(AvatarAddonItemImpl)
        },
    ) { measurables, constraints ->
        val avatarPlaceable = measurables[0].measure(constraints)
        val avatarWidth = avatarPlaceable.width
        val avatarHeight = avatarPlaceable.height

        val addonConstraints = when (addonPlacement) {
            AvatarAddonPlacement.Rim -> Constraints()
            AvatarAddonPlacement.BottomEnd -> Constraints(maxWidth = avatarWidth, maxHeight = avatarHeight)
        }
        val addonMeasurable = if (measurables.size > 1) measurables[1] else null
        val addonPlaceable = addonMeasurable?.measure(addonConstraints)
        val addonWidth = addonPlaceable?.width ?: 0
        val addonHeight = addonPlaceable?.height ?: 0

        val hasAddon = addonWidth > 0 || addonHeight > 0

        layout(avatarWidth, avatarHeight) {
            avatarPlaceable.placeRelative(0, 0)
            if (addonPlaceable == null) return@layout
            when (addonPlacement) {
                AvatarAddonPlacement.Rim -> {
                    val addonLeft: Int
                    val addonTop: Int
                    if (!hasAddon) {
                        addonLeft = 0
                        addonTop = 0
                    } else {
                        val radius = avatarWidth / 2f
                        // cos(45°) = sin(45°) = sqrt(2)/2; centre the addon on the bottom-right circumference point.
                        val diagonal = sqrt(2f) / 2f
                        val addonCenterX = (avatarWidth / 2f).roundToInt()
                        val addonCenterY = (avatarHeight / 2f).roundToInt()
                        addonLeft = ((addonCenterX - addonWidth / 2) + radius * diagonal).roundToInt()
                        addonTop = ((addonCenterY - addonHeight / 2) + radius * diagonal).roundToInt()
                    }
                    addonPlaceable.place(addonLeft, addonTop)
                }

                AvatarAddonPlacement.BottomEnd -> addonPlaceable.placeRelative(
                    x = avatarWidth - addonWidth,
                    y = avatarHeight - addonHeight,
                )
            }
        }
    }
}
