/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.catalog.configurator.samples.image

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImageModelEqualityDelegate
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImageModelEqualityDelegate
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.decode.DataSource
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.crossfade
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarAddonItem
import com.adevinta.spark.components.image.AvatarAddonScope
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.Company
import com.adevinta.spark.components.image.User
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.google.accompanist.drawablepainter.rememberDrawablePainter

public val AvatarConfigurator: Configurator = Configurator(
    id = "avatar",
    name = "Avatar",
    description = "Avatar configuration",
    sourceUrl = "$SampleSourceUrl/image/AvatarExamples.kt",
) { _, _ ->
    AvatarSample()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ColumnScope.AvatarSample() {
    var variant by remember { mutableStateOf(AvatarVariant.User) }
    var style by remember { mutableStateOf(AvatarSize.SM) }
    var showOnlineIndicator by remember { mutableStateOf(false) }
    var imageState by remember { mutableStateOf(AvatarImageState.Empty) }
    var username by remember { mutableStateOf("Spark") }

    val context = LocalContext.current
    val drawable = getDrawable(context, R.drawable.img_narrow_image_configurator)!!
    val painter = rememberDrawablePainter(drawable)
    val imageRequest = ImageRequest.Builder(context)
        .crossfade(true)
        .data(imageState.ordinal)
        .build()

    val previewHandler = AsyncImagePreviewHandler { _, request ->
        when (imageState) {
            AvatarImageState.Success -> AsyncImagePainter.State.Success(
                painter = painter,
                result = SuccessResult(drawable.asImage(), request, DataSource.DISK),
            )

            AvatarImageState.Loading -> AsyncImagePainter.State.Loading(painter)

            AvatarImageState.Error -> AsyncImagePainter.State.Error(
                painter = painter,
                result = ErrorResult(drawable.asImage(), request, Throwable("")),
            )

            AvatarImageState.Empty -> AsyncImagePainter.State.Empty
        }
    }

    val addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = if (showOnlineIndicator) {
        { onlineIndicator() }
    } else {
        {}
    }

    CompositionLocalProvider(
        LocalAsyncImagePreviewHandler provides previewHandler,
        LocalAsyncImageModelEqualityDelegate provides AsyncImageModelEqualityDelegate.AllProperties,
        LocalInspectionMode provides true,
    ) {
        val avatarModifier = Modifier.align(Alignment.CenterHorizontally)
        when (variant) {
            AvatarVariant.User -> Avatar.User(
                username = username,
                modifier = avatarModifier,
                size = style,
                model = imageRequest,
                addon = addon,
            )

            AvatarVariant.Company -> Avatar.Company(
                modifier = avatarModifier,
                size = style,
                model = imageRequest,
                addon = addon,
            )
        }
    }

    ButtonGroup(
        title = "Variant",
        selectedOption = variant,
        onOptionSelect = { variant = it },
    )

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
    )

    ButtonGroup(
        title = "Image state",
        selectedOption = imageState,
        onOptionSelect = { imageState = it },
    )

    if (variant == AvatarVariant.User) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = { username = it },
            label = "Username",
            placeholder = "Enter a user name",
            helper = "The first letter shows in the Empty and Error image states only",
        )
    }

    SwitchLabelled(
        checked = showOnlineIndicator,
        onCheckedChange = { showOnlineIndicator = it },
    ) {
        Text(text = "Online indicator", modifier = Modifier.fillMaxWidth())
    }
}

private enum class AvatarVariant {
    User,
    Company,
}

private enum class AvatarImageState {
    Success,
    Loading,
    Error,
    Empty,
}

@Preview
@Composable
private fun AvatarSamplePreview() {
    PreviewTheme { AvatarSample() }
}
