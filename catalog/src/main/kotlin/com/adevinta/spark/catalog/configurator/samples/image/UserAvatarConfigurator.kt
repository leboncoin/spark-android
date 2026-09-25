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
import com.adevinta.spark.components.image.UserAvatar
import com.adevinta.spark.components.image.UserAvatarStyle
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.google.accompanist.drawablepainter.rememberDrawablePainter

public val UserAvatarConfigurator: Configurator = Configurator(
    id = "user-avatar",
    name = "User Avatar",
    description = "User Avatar configuration",
    sourceUrl = "$SampleSourceUrl/UserAvatarSample.kt",
) { _, _ ->
    UserAvatarSample()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ColumnScope.UserAvatarSample() {
    var style by remember { mutableStateOf(UserAvatarStyle.SM) }
    var isPro by remember { mutableStateOf(false) }
    var showOnlineIndicator by remember { mutableStateOf(false) }
    var imageState by remember { mutableStateOf(UserAvatarImageState.Empty) }

    val context = LocalContext.current
    val drawable = getDrawable(context, R.drawable.img_narrow_image_configurator)!!
    val painter = rememberDrawablePainter(drawable)
    val imageRequest = ImageRequest.Builder(context)
        .crossfade(true)
        .data(imageState.ordinal)
        .build()

    val previewHandler = AsyncImagePreviewHandler { _, request ->
        when (imageState) {
            UserAvatarImageState.Success -> AsyncImagePainter.State.Success(
                painter = painter,
                result = SuccessResult(drawable.asImage(), request, DataSource.DISK),
            )

            UserAvatarImageState.Loading -> AsyncImagePainter.State.Loading(painter)

            UserAvatarImageState.Error -> AsyncImagePainter.State.Error(
                painter = painter,
                result = ErrorResult(drawable.asImage(), request, Throwable("")),
            )

            UserAvatarImageState.Empty -> AsyncImagePainter.State.Empty
        }
    }

    CompositionLocalProvider(
        LocalAsyncImagePreviewHandler provides previewHandler,
        LocalAsyncImageModelEqualityDelegate provides AsyncImageModelEqualityDelegate.AllProperties,
        LocalInspectionMode provides true,
    ) {
        UserAvatar(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = style,
            model = imageRequest,
            isPro = isPro,
            addon = if (showOnlineIndicator) {
                { onlineIndicator() }
            } else {
                {}
            },
        )
    }

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

    SwitchLabelled(
        checked = isPro,
        onCheckedChange = { isPro = it },
    ) {
        Text(text = "Pro", modifier = Modifier.fillMaxWidth())
    }

    SwitchLabelled(
        checked = showOnlineIndicator,
        onCheckedChange = { showOnlineIndicator = it },
    ) {
        Text(text = "Online indicator", modifier = Modifier.fillMaxWidth())
    }
}

private enum class UserAvatarImageState {
    Success,
    Loading,
    Error,
    Empty,
}

@Preview
@Composable
private fun UserAvatarSamplePreview() {
    PreviewTheme { UserAvatarSample() }
}
