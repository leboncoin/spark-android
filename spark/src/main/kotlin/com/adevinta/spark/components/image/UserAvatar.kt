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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.icons.BuildingCircle
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.UserCircleFill
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val AVATAR_PLACEHOLDER_ICON_SCALE = 1.2f

@InternalSparkApi
@Composable
internal fun SparkUserAvatar(
    modifier: Modifier = Modifier,
    // Useful to preview different states
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    style: UserAvatarStyle = UserAvatarStyle.SMALL,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    color: Color = Color.Unspecified,
    isPro: Boolean = false,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    val emptyIcon = @Composable {
        // The circle placeholder icons keep a 2dp safe area inside a 24dp viewport. Scale by 24/20
        // so the circle fills the avatar box and its rim aligns with the addon slot.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(AVATAR_PLACEHOLDER_ICON_SCALE),
        ) {
            ImageIconState(
                sparkIcon = if (isPro) LeboncoinIcons.BuildingCircle else LeboncoinIcons.UserCircleFill,
                color = color.takeOrElse { Color.Transparent },
                size = null,
            )
        }
    }
    Layout(
        modifier = modifier.sparkUsageOverlay(),
        content = {
            SparkImage(
                modifier = Modifier.then(if (fillParentSize) Modifier.fillMaxSize() else Modifier.size(style.imageSize))
                    .aspectRatio(1f),
                model = model,
                transform = transform,
                contentDescription = stringResource(id = R.string.spark_user_avatar_content_description),
                contentScale = contentScale,
                emptyIcon = emptyIcon,
                errorIcon = emptyIcon,
            )
            AvatarAddonScopeImpl(style).addon(AvatarAddonItemImpl)
        },
    ) { measurables, constraints ->
        val avatarPlaceable = measurables[0].measure(constraints)
        val avatarWidth = avatarPlaceable.width
        val avatarHeight = avatarPlaceable.height

        val addonMeasurable = if (measurables.size > 1) measurables[1] else null
        val addonPlaceable = addonMeasurable?.measure(Constraints())
        val addonWidth = addonPlaceable?.width ?: 0
        val addonHeight = addonPlaceable?.height ?: 0

        val hasAddon = addonWidth > 0 || addonHeight > 0
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

        layout(avatarWidth, avatarHeight) {
            avatarPlaceable.placeRelative(0, 0)
            addonPlaceable?.place(addonLeft, addonTop)
        }
    }
}

/**
 * A circular profile picture that identifies a user.
 *
 * When [model] is null or the load fails, it shows a fallback icon instead of a blank circle: a profile
 * silhouette, or a building icon for a pro account.
 *
 * ![Online indicator](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_UserAvatarDocumentationScreenshots_onlineIndicator.png)
 *
 * @param modifier applied to the avatar
 * @param style avatar diameter (32dp, 40dp, or 64dp) and matching online badge size
 * @param fillParentSize ignore [style] and fill the parent; use it when a fixed-size slot already
 * sets the avatar size
 * @param model image to load, for example the user photo URL; null shows the fallback icon
 * @param color background behind the fallback icon; match it to the surface behind the avatar, or
 * leave Color.Unspecified for a transparent background
 * @param isPro mark a professional account so the fallback shows a building icon; it shows only in
 * the fallback state
 * @param addon optional overlay badge slot; call [AvatarAddonScope.onlineIndicator] for the
 * standard presence dot, or [AvatarAddonScope.custom] for arbitrary content; defaults to empty
 **/
@Composable
public fun UserAvatar(
    modifier: Modifier = Modifier,
    style: UserAvatarStyle = UserAvatarStyle.SMALL,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    color: Color = Color.Unspecified,
    isPro: Boolean = false,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    SparkUserAvatar(
        modifier = modifier,
        style = style,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        isPro = isPro,
        color = color,
        addon = addon,
    )
}

/**
 * @param imageSize size of the image in [Dp]
 * @param badgeSize size of online badge in [Dp]
 * @param borderSize The indicator border size in [Dp]. Must be set explicitly because the border mechanism differs
 * from the Figma spec.
 */
public enum class UserAvatarStyle(public val imageSize: Dp, public val badgeSize: Dp, public val borderSize: Dp) {
    SMALL(imageSize = 32.dp, badgeSize = 8.dp, borderSize = 1.dp),
    MEDIUM(imageSize = 40.dp, badgeSize = 12.dp, borderSize = 2.dp),
    LARGE(imageSize = 64.dp, badgeSize = 16.dp, borderSize = 2.dp),
}

@Preview(
    group = "Images",
    name = "User Avatar",
)
@Composable
internal fun UserAvatarPreview() {
    PreviewTheme {
        SparkUserAvatar(
            style = UserAvatarStyle.LARGE,
            model = "",
            isPro = false,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.MEDIUM,
            model = "",
            isPro = false,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.SMALL,
            model = "",
            isPro = false,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.LARGE,
            model = "",
            isPro = true,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.LARGE,
            model = "",
            isPro = true,
            addon = { iconButton({}) },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.LARGE,
            model = "",
            isPro = true,
            addon = { iconButton({}, icon = LeboncoinIcons.PenOutline) },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.MEDIUM,
            model = "",
            isPro = true,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
        SparkUserAvatar(
            style = UserAvatarStyle.SMALL,
            model = "",
            isPro = true,
            addon = { onlineIndicator() },
            transform = { AsyncImagePainter.State.Empty },
        )
    }
}
