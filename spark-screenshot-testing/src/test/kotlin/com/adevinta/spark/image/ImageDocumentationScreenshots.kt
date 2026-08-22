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
package com.adevinta.spark.image

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.image.Image
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.Tablet
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.screenshot.testing.R
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoilApi::class)
internal class ImageDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun imageStates() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StateImage(ImageState.Success, "Loaded")
            StateImage(ImageState.Error, "Error")
            StateImage(ImageState.Empty, "Empty")
            StateImage(ImageState.Loading, "Loading")
        }
    }

    @Test
    fun contentScaleCrop() = contentScaleSnapshot(ContentScale.Crop)

    @Test
    fun contentScaleFit() = contentScaleSnapshot(ContentScale.Fit)

    @Test
    fun contentScaleFillBounds() = contentScaleSnapshot(ContentScale.FillBounds)

    @Test
    fun contentScaleFillHeight() = contentScaleSnapshot(ContentScale.FillHeight)

    @Test
    fun contentScaleFillWidth() = contentScaleSnapshot(ContentScale.FillWidth)

    @Test
    fun contentScaleInside() = contentScaleSnapshot(ContentScale.Inside)

    @Test
    fun contentScaleNone() = contentScaleSnapshot(ContentScale.None)

    private fun contentScaleSnapshot(contentScale: ContentScale) = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ContentScaleImage(R.drawable.spark_img_narrow_image_configurator, "Narrow Image", contentScale)
            ContentScaleImage(R.drawable.spark_img_wide_image_configurator, "Wide Image", contentScale)
        }
    }

    @SuppressLint("LocalContextGetResourceValueCall")
    @Composable
    private fun StateImage(state: ImageState, label: String) {
        val context = LocalContext.current
        val painter = painterResource(R.drawable.spark_img_narrow_image_configurator)
        val imageRequest = ImageRequest.Builder(context).data(Unit).build()
        val drawable = context.getDrawable(R.drawable.spark_img_narrow_image_configurator)!!
        val previewHandler = AsyncImagePreviewHandler { _, request ->
            when (state) {
                ImageState.Success -> AsyncImagePainter.State.Success(
                    painter = painter,
                    result = SuccessResult(drawable.asImage(), request, DataSource.DISK),
                )

                ImageState.Error -> AsyncImagePainter.State.Error(
                    painter = painter,
                    result = ErrorResult(drawable.asImage(), request, Throwable("")),
                )

                ImageState.Empty -> AsyncImagePainter.State.Empty

                ImageState.Loading -> AsyncImagePainter.State.Loading(painter)
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = SparkTheme.typography.caption)
            CompositionLocalProvider(
                LocalAsyncImagePreviewHandler provides previewHandler,
                LocalAsyncImageModelEqualityDelegate provides AsyncImageModelEqualityDelegate.AllProperties,
            ) {
                Image(
                    model = imageRequest,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp),
                )
            }
        }
    }

    @SuppressLint("LocalContextGetResourceValueCall")
    @Composable
    private fun ContentScaleImage(
        @DrawableRes imageRes: Int,
        label: String,
        contentScale: ContentScale,
    ) {
        val context = LocalContext.current
        val painter = painterResource(imageRes)
        val imageRequest = ImageRequest.Builder(context).data(imageRes).build()
        val drawable = context.getDrawable(imageRes)!!
        val previewHandler = AsyncImagePreviewHandler { _, request ->
            AsyncImagePainter.State.Success(
                painter = painter,
                result = SuccessResult(drawable.asImage(), request, DataSource.DISK),
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = SparkTheme.typography.caption)
            CompositionLocalProvider(
                LocalAsyncImagePreviewHandler provides previewHandler,
                LocalAsyncImageModelEqualityDelegate provides AsyncImageModelEqualityDelegate.AllProperties,
            ) {
                Surface(
                    border = BorderStroke(1.dp, SparkTheme.colors.outline),
                    modifier = Modifier.size(96.dp),
                ) {
                    Image(
                        model = imageRequest,
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    private enum class ImageState {
        Loading,
        Empty,
        Error,
        Success,
    }
}
