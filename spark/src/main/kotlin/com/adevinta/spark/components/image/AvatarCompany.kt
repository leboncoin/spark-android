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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import coil3.compose.AsyncImagePainter
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.BuildingSquare
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.tokens.dim2
import kotlin.math.min
import kotlin.math.roundToInt

@InternalSparkApi
@Composable
internal fun SparkAvatarCompany(
    modifier: Modifier = Modifier,
    // Useful to preview different states
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    style: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    SparkAvatar(
        shape = SparkTheme.shapes.small,
        addonPlacement = AvatarAddonPlacement.BottomEnd,
        placeholder = { CompanyAvatarPlaceholder() },
        modifier = modifier,
        transform = transform,
        style = style,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        addon = addon,
    )
}

/**
 * A rounded-square picture that identifies a company, for example a professional seller.
 *
 * When [model] is null or the image does not load, the avatar shows a building icon on a neutral
 * container with a thin border.
 *
 * The addon is inside the avatar bounds, at the bottom-end corner. It moves to the bottom-start
 * corner in a right-to-left layout.
 *
 * ![Addon](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_AvatarDocumentationScreenshots_addonBadge.png)
 *
 * @param modifier applied to the avatar
 * @param style avatar size, from [AvatarSize.XS] (24dp) to [AvatarSize.XXXL] (128dp), and the
 * matching online badge size; the default is [AvatarSize.SM] (32dp)
 * @param contentScale how the loaded image fits the avatar bounds; the default is [ContentScale.Fit]
 * @param fillParentSize when true, the avatar ignores [style] and fills the parent; use it when a
 * fixed-size slot already sets the avatar size
 * @param model image to load, for example the company logo URL; null shows the building icon
 * @param addon optional badge slot; call [AvatarAddonScope.onlineIndicator] for the standard
 * presence dot, or [AvatarAddonScope.custom] for other content; the default is empty
 */
@Suppress("UnusedReceiverParameter")
@Composable
public fun Avatar.Company(
    modifier: Modifier = Modifier,
    style: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    SparkAvatarCompany(
        modifier = modifier,
        style = style,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        addon = addon,
    )
}

@Composable
private fun CompanyAvatarPlaceholder() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = SparkTheme.shapes.small,
        color = SparkTheme.colors.neutralContainer,
        border = BorderStroke(width = 1.dp, color = SparkTheme.colors.outline.dim2),
    ) {
        Icon(
            sparkIcon = LeboncoinIcons.BuildingSquare,
            contentDescription = null, // SparkImage handles the content description.
            modifier = Modifier.paddingFromAvatarSize(),
            tint = SparkTheme.colors.neutral,
        )
    }
}

// Figma sets a 4dp inset on the 64dp avatar. Scale it with the measured size so that it also fits fillParentSize.
private fun Modifier.paddingFromAvatarSize(): Modifier = layout { measurable, constraints ->
    val inset = (min(constraints.maxWidth, constraints.maxHeight) / 16f).roundToInt()
    val placeable = measurable.measure(constraints.offset(horizontal = -inset * 2, vertical = -inset * 2))
    layout(placeable.width + inset * 2, placeable.height + inset * 2) {
        placeable.placeRelative(inset, inset)
    }
}

@Preview(
    group = "Images",
    name = "Company Avatar",
)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompanyAvatarPreview() {
    PreviewTheme {
        listOf(false, true).forEach { withAddon ->
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AvatarSize.entries.forEach { size ->
                    SparkAvatarCompany(
                        style = size,
                        model = "",
                        transform = { AsyncImagePainter.State.Empty },
                        addon = { if (withAddon) onlineIndicator() },
                    )
                }
            }
        }
    }
}
