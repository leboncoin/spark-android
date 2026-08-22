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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
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
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.image.Image
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.Tablet
import com.adevinta.spark.paparazziRule
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
        val context = LocalContext.current
        val painter = painterResource(LeboncoinIcons.Tablet.drawableId)
        val imageRequest = ImageRequest.Builder(context).data(Unit).build()
        val drawable = context.getDrawable(LeboncoinIcons.Tablet.drawableId)!!
        val previewHandler = AsyncImagePreviewHandler { _, request ->
            AsyncImagePainter.State.Success(
                painter = painter,
                result = SuccessResult(drawable.asImage(), request, DataSource.DISK),
            )
        }
        CompositionLocalProvider(
            LocalAsyncImagePreviewHandler provides previewHandler,
            LocalAsyncImageModelEqualityDelegate provides AsyncImageModelEqualityDelegate.AllProperties,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(48.dp, 100.dp, 200.dp).forEach { size ->
                    Image(
                        model = imageRequest,
                        contentDescription = null,
                        modifier = Modifier.size(size),
                    )
                }
            }
        }
    }
}
