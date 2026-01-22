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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.fileupload.FileUpload
import com.adevinta.spark.components.fileupload.PreviewFile
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.manualFileKitCoreInitialization
import org.junit.Rule
import org.junit.Test
import java.io.File

internal class FileUploadScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Phone,
    )

    @Test
    fun singleAndMultiple() {
        FileKit.manualFileKitCoreInitialization(paparazzi.context)
        paparazzi.sparkSnapshotNightMode {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SingleFileSample()
                MultipleFilesSample()
            }
        }
    }

    @Test
    fun previewFiles() {
        FileKit.manualFileKitCoreInitialization(paparazzi.context)
        paparazzi.sparkSnapshotNightMode {
            FilePreviewStatesExample()
        }
    }

    @Composable
    private fun SingleFileSample() {
        FileUpload.ButtonSingleSelect(
            onResult = {},
            label = "Upload ticket",
        )
    }

    @Composable
    private fun MultipleFilesSample() {
        FileUpload.Button(
            onResult = {},
            label = "Upload documents",
        )
    }

    @Composable
    private fun FilePreviewStatesExample() {
        Column {
            // Create mock files for different states
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
            val indeterminateFile = remember {
                UploadedFile(
                    file = PlatformFile(file = File("video.mp4")),
                    isLoading = true,
                )
            }
            val errorFile = remember {
                UploadedFile(
                    file = PlatformFile(file = File("large-file.zip")),
                    errorMessage = "File size exceeds maximum limit of 10MB",
                )
            }
            val disabledFile = remember {
                UploadedFile(
                    file = PlatformFile(file = File("archive.rar")),
                    enabled = false,
                )
            }

            PreviewFile(
                file = defaultFile,
                onClear = {},
                modifier = Modifier.fillMaxWidth(),
            )

            VerticalSpacer(16.dp)

            PreviewFile(
                file = progressFile,
                onClear = {},
                modifier = Modifier.fillMaxWidth(),
            )

            VerticalSpacer(16.dp)

            PreviewFile(
                file = indeterminateFile,
                onClear = {},
                modifier = Modifier.fillMaxWidth(),
            )

            VerticalSpacer(16.dp)

            PreviewFile(
                file = errorFile,
                onClear = {},
                modifier = Modifier.fillMaxWidth(),
            )

            VerticalSpacer(16.dp)

            PreviewFile(
                file = disabledFile,
                onClear = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
