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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImagePainter
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.icons.BuildingCircle
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.UserCircleFill

@InternalSparkApi
@Composable
internal fun SparkUserAvatar(
    modifier: Modifier = Modifier,
    // Useful to preview different states
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    size: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    color: Color = Color.Unspecified,
    isPro: Boolean = false,
    letter: Char? = null,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    val emptyIcon = @Composable {
        if (letter != null) {
            AvatarLetterState(letter = letter, size = size)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.5f), // Scale to fill the 2dp safe padding we have with icons but that we don't want here
            ) {
                ImageIconState(
                    sparkIcon = if (isPro) LeboncoinIcons.BuildingCircle else LeboncoinIcons.UserCircleFill,
                    // Color.Unspecified crashes Paint.setColor on device (invalid colour-space id). Resolve it here.
                    color = color.takeOrElse { Color.Transparent },
                    size = null,
                )
            }
        }
    }
    SparkAvatar(
        shape = SparkTheme.shapes.full,
        addonPlacement = AvatarAddonPlacement.Rim,
        placeholder = emptyIcon,
        modifier = modifier,
        transform = transform,
        size = size,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        addon = addon,
    )
}

/**
 * A circular profile picture that identifies a user.
 *
 * When [model] is null or the load fails, it shows a fallback instead of a blank circle: the [letter] when
 * given, otherwise a profile silhouette, or a building icon for a pro account.
 *
 * ![Online indicator](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_AvatarDocumentationScreenshots_onlineIndicator.png)
 *
 * ![Letter](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_AvatarDocumentationScreenshots_letter.png)
 *
 * @param modifier applied to the avatar
 * @param style avatar diameter, from [AvatarSize.XS] (24dp) to [AvatarSize.XXXL] (128dp), and
 * matching online badge size; defaults to [AvatarSize.SM] (32dp)
 * @param contentScale how the loaded image fits the avatar bounds; defaults to [ContentScale.Fit]
 * @param fillParentSize ignore [style] and fill the parent; use it when a fixed-size slot already
 * sets the avatar size
 * @param model image to load, for example the user photo URL; null shows the fallback icon
 * @param color background behind the fallback icon; match it to the surface behind the avatar, or
 * leave Color.Unspecified for a transparent background; the letter fallback ignores it
 * @param isPro mark a professional account so the fallback shows a building icon; it shows only in
 * the fallback state, and [letter] takes precedence over it
 * @param isOnline show the online indicator on the avatar rim; defaults to false
 * @param letter first letter of the user name to show on a neutral circle in the fallback state; it
 * renders as given, so pass the case you want; null shows the fallback icon
 *
 * @deprecated Use [Avatar.User] for a person or [Avatar.Company] for a company.
 **/
@Deprecated(
    message = "Use Avatar.User for a person or Avatar.Company for a company",
    level = DeprecationLevel.WARNING,
)
@Composable
public fun UserAvatar(
    modifier: Modifier = Modifier,
    style: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    color: Color = Color.Unspecified,
    isPro: Boolean = false,
    isOnline: Boolean = false,
    letter: Char? = null,
) {
    SparkUserAvatar(
        modifier = modifier,
        size = style,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        isPro = isPro,
        letter = letter,
        color = color,
        addon = {
            if (isOnline) onlineIndicator()
        },
    )
}

@Preview(
    group = "Images",
    name = "User Avatar",
)
@Composable
internal fun UserAvatarPreview() {
    PreviewTheme {
        SparkUserAvatar(
            size = AvatarSize.XL,
            model = "",
            isPro = false,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.MD,
            model = "",
            isPro = false,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.SM,
            model = "",
            isPro = false,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.XL,
            model = "",
            isPro = true,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.XL,
            model = "",
            isPro = true,
            addon = { iconButton({}) },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.XL,
            model = "",
            isPro = true,
            addon = { iconButton({}, icon = LeboncoinIcons.PenOutline) },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.MD,
            model = "",
            isPro = true,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            size = AvatarSize.SM,
            model = "",
            isPro = true,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        AvatarSize.entries.forEach { style ->
            SparkUserAvatar(
                size = style,
                model = "",
                letter = 'S',
                addon = { onlineIndicator() },
                transform = { AsyncImagePainter.State.Empty },
            )
        }
    }
}
