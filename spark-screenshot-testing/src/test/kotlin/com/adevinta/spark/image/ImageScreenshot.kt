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

import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
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
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.image.SparkImage
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.Tattoo
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.screenshot.testing.R
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.Rule
import org.junit.Test

@OptIn(InternalSparkApi::class)
internal class ImageScreenshot {

    private val imageSizes = listOf(48.dp, 100.dp, 200.dp)

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = RenderingMode.H_SCROLL,
    )

    @Test
    fun imagesInDifferentStates() {
        paparazzi.sparkSnapshot {
            ImageStates()
        }
    }

    @Test
    fun imageWithDifferentContentScales() {
        paparazzi.sparkSnapshot {
            ContentScaleShowcase()
        }
    }

    @OptIn(InternalSparkApi::class)
    @Composable
    private fun ImageStates() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Image Component",
                style = SparkTheme.typography.headline1,
            )

            val states = ImageState.entries
            val context = LocalContext.current
            val imageRequest = ImageRequest.Builder(context).data(Unit).build()
            val painter = painterResource(SparkIcons.Tattoo.drawableId)

            states.forEach { state ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        imageSizes.forEach { size ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                TestImage(
                                    size = size,
                                    state = state,
                                    painter = painter,
                                    imageRequest = imageRequest,
                                    drawable = context.getDrawable(SparkIcons.Tattoo.drawableId)!!,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    private fun TestImage(
        size: Dp,
        state: ImageState,
        painter: Painter,
        imageRequest: ImageRequest,
        drawable: Drawable,
    ) {
        val previewHandler = AsyncImagePreviewHandler { _, request ->
            when (state) {
                ImageState.Error -> AsyncImagePainter.State.Error(
                    painter = painter,
                    result = ErrorResult(drawable.asImage(), request, Throwable("")),
                )

                ImageState.Empty -> AsyncImagePainter.State.Empty
                ImageState.Loading -> AsyncImagePainter.State.Loading(painter)
                ImageState.Success -> AsyncImagePainter.State.Success(
                    painter = painter,
                    result = SuccessResult(drawable.asImage(), request, DataSource.DISK),
                )
            }
        }
        CompositionLocalProvider(
            LocalAsyncImagePreviewHandler provides previewHandler,
            LocalAsyncImageModelEqualityDelegate provides AsyncImageModelEqualityDelegate.AllProperties,
        ) {
            SparkImage(
                model = imageRequest,
                contentDescription = "Test image $state",
                modifier = Modifier
                    .size(size)
                    .clip(SparkTheme.shapes.medium),
            )
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun ContentScaleShowcase() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val contentScales = listOf(
                ContentScale.Crop to "Crop",
                ContentScale.Fit to "Fit",
                ContentScale.FillBounds to "FillBounds",
                ContentScale.FillHeight to "FillHeight",
                ContentScale.FillWidth to "FillWidth",
                ContentScale.Inside to "Inside",
                ContentScale.None to "None",
            )

            val imageTypes = listOf(
                R.drawable.spark_img_narrow_image_configurator to "Narrow Image",
                R.drawable.spark_img_wide_image_configurator to "Wide Image",
            )

            val context = LocalContext.current

            imageTypes.forEach { (imageRes, imageType) ->
                Text(
                    text = imageType,
                    style = SparkTheme.typography.headline2,
                    modifier = Modifier.padding(top = 16.dp),
                )
                val imageRequest = ImageRequest.Builder(context).data(imageRes).build()
                val painter = painterResource(imageRes)

                val state = AsyncImagePainter.State.Success(
                    painter,
                    SuccessResult(context.getDrawable(imageRes)!!.asImage(), imageRequest),
                )
//                val previewHandler = AsyncImagePreviewHandler { context.getDrawable(imageRes)!!.asImage() }

                Spacer(modifier = Modifier.size(8.dp))
                FlowRow(
                    horizontalArrangement = spacedBy(8.dp),
                ) {
                    contentScales.forEach { (contentScale, scaleName) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 16.dp),
                        ) {
                            Text(
                                text = scaleName,
                                style = SparkTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )

                            // Add a border to make the container bounds visible
                            Surface(
                                border = BorderStroke(1.dp, SparkTheme.colors.outline),
                                modifier = Modifier
                                    .width(200.dp)
                                    .aspectRatio(1f),
                            ) {
                                SparkImage(
                                    model = imageRes,
                                    contentDescription = "ContentScale $scaleName",
                                    contentScale = contentScale,
                                    modifier = Modifier.fillMaxSize(),
                                    transform = { state },
                                )
                            }
                        }
                    }
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
