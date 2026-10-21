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

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tokens.ElevationTokens

/**
 * Marker interface returned by every [AvatarAddonScope] addon function.
 *
 * The return type makes sure that only the addon functions of [AvatarAddonScope] go in the addon
 * block of an avatar. Callers must not implement this interface directly.
 */
public interface AvatarAddonItem

/**
 * DSL scope for declaring an addon inside an avatar.
 *
 * Methods on this scope are composable: they emit UI directly when called and return an
 * [AvatarAddonItem] marker that callers discard. This mirrors [SegmentedControlScope], with one
 * deviation: the avatar supports a single addon rather than a list, so only the last emitted node
 * is laid out. [Avatar.User] centres the addon on the circle rim at the bottom right, so the addon
 * can go outside the avatar bounds. [Avatar.Company] puts the addon at the bottom-end corner, with
 * 25% of the addon outside the avatar bounds.
 */
@Stable
public interface AvatarAddonScope {

    /**
     * Emits the standard online presence indicator: a filled [SparkTheme.colors.success] circle
     * inside a border ring in [SparkTheme.colors.surface].
     *
     * The indicator size derives from the enclosing [AvatarSize]. At [AvatarSize.XS] the
     * indicator draws nothing.
     */
    @Composable
    public fun onlineIndicator(): AvatarAddonItem

    /**
     * Emits a 32dp circular button with an elevated surface and a 16dp [icon], for an action on
     * the avatar such as an edit.
     *
     * The button size does not change with the enclosing [AvatarSize].
     *
     * @param onClick called when the user clicks the button.
     * @param modifier applied to the button.
     * @param icon icon in the button; defaults to [LeboncoinIcons.Plus].
     */
    @Composable
    public fun iconButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: SparkIcon = LeboncoinIcons.Plus,
    ): AvatarAddonItem

    /**
     * Emits arbitrary composable content as the avatar addon.
     *
     * @param content Composable content to place in the addon slot.
     */
    @Composable
    public fun custom(content: @Composable () -> Unit): AvatarAddonItem
}

// Internal so SparkAvatar (same module, different file) can pass it as the phantom parameter.
internal object AvatarAddonItemImpl : AvatarAddonItem

// Suppress ComposeContentEmitterReturningValues to mirror SegmentedControlScopeImpl: scope
// methods emit UI and return the marker so the slot type enforces only recognised builders.
@SuppressLint("ComposeContentEmitterReturningValues")
internal class AvatarAddonScopeImpl(private val size: AvatarSize) : AvatarAddonScope {

    @Suppress("ContentEmission")
    @Composable
    override fun onlineIndicator(): AvatarAddonItem {
        OnlineIndicator(size)
        return AvatarAddonItemImpl
    }

    @Suppress("ContentEmission")
    @Composable
    override fun iconButton(
        onClick: () -> Unit,
        modifier: Modifier,
        icon: SparkIcon,
    ): AvatarAddonItem {
        IconButton(onClick, modifier, icon)
        return AvatarAddonItemImpl
    }

    @Suppress("ContentEmission")
    @Composable
    override fun custom(content: @Composable () -> Unit): AvatarAddonItem {
        content()
        return AvatarAddonItemImpl
    }
}

@Composable
private fun OnlineIndicator(avatarSize: AvatarSize) {
    if (avatarSize == AvatarSize.XS) return
    val indicatorColor = SparkTheme.colors.success
    val outline = SparkTheme.colors.surface
    Canvas(modifier = Modifier.size(avatarSize.badgeSize)) {
        val halfBadge = size.width / 2f
        val borderPx = avatarSize.borderSize.toPx()
        drawCircle(
            color = outline,
            radius = halfBadge,
        )
        // Draw the filled indicator dot inside the outline-coloured ring.
        drawCircle(
            color = indicatorColor,
            radius = halfBadge - borderPx,
        )
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: SparkIcon = LeboncoinIcons.Plus,
) {
    val indicatorColor = SparkTheme.colors.success
    Surface(
        modifier = modifier
            .size(32.dp)
            .semantics {
                role = Role.Button
            },
        elevation = ElevationTokens.Level2,
        shape = SparkTheme.shapes.full,
        onClick = onClick,
    ) {
        Icon(
            sparkIcon = icon,
            contentDescription = null,
            modifier = Modifier.requiredSize(16.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewAddons() {
    PreviewTheme(color = { SparkTheme.colors.backgroundVariant }) {
        Avatar.User(username = "Spark", size = AvatarSize.SM, addon = { onlineIndicator() })
        Avatar.User(username = "Spark", size = AvatarSize.MD, addon = { onlineIndicator() })
        Avatar.User(username = "Spark", size = AvatarSize.XL, addon = { onlineIndicator() })
        Avatar.User(username = "Spark", size = AvatarSize.XL, addon = { iconButton({}) })
        Avatar.User(
            username = "Spark",
            size = AvatarSize.XL,
            addon = { iconButton({}, icon = LeboncoinIcons.PenOutline) },
        )
    }
}
