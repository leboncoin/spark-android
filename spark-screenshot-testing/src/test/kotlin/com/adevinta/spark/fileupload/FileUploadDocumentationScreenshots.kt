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
package com.adevinta.spark.fileupload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.components.fileupload.FileUpload
import com.adevinta.spark.components.fileupload.PreviewFile
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.manualFileKitCoreInitialization
import org.junit.Rule
import org.junit.Test
import java.io.File

@OptIn(InternalSparkApi::class)
internal class FileUploadDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun fileUploadButtons() {
        FileKit.manualFileKitCoreInitialization(paparazzi.context)
        paparazzi.sparkDocSnapshot {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                FileUpload.Button(
                    onResult = {},
                    label = "Upload documents",
                )
                FileUpload.ButtonSingleSelect(
                    onResult = {},
                    label = "Upload ticket",
                )
            }
        }
    }

    @Test
    fun fileUploadPreview() {
        FileKit.manualFileKitCoreInitialization(paparazzi.context)
        paparazzi.sparkDocSnapshot {
            val defaultFile = remember {
                UploadedFile(
                    file = PlatformFile(file = File("document.pdf")),
                )
            }
            val progressFile = remember {
                UploadedFile(
                    file = PlatformFile(file = File("image.jpg")),
                    progress = { 0.65f },
                )
            }
            val errorFile = remember {
                UploadedFile(
                    file = PlatformFile(file = File("large-file.zip")),
                    errorMessage = "File size exceeds maximum limit of 10MB",
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PreviewFile(
                    file = defaultFile,
                    onClear = {},
                    modifier = Modifier.fillMaxWidth(),
                )
                PreviewFile(
                    file = progressFile,
                    onClear = {},
                    modifier = Modifier.fillMaxWidth(),
                )
                PreviewFile(
                    file = errorFile,
                    onClear = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
