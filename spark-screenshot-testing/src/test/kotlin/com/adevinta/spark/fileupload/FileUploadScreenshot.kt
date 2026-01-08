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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.fileupload.FileUploadButton
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import org.junit.Rule
import org.junit.Test

internal class FileUploadScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Phone,
    )

    @Test
    fun singleAndMultiple() {
        paparazzi.sparkSnapshot {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SingleFileSample()
                MultipleFilesSample()
            }
        }
    }

    @Composable
    private fun SingleFileSample() {
        val files = listOf(
            UploadedFile(
                path = "1",
                name = "Ticket.pdf",
                sizeBytes = 256_000,
                mimeType = "application/pdf",
            ),
        )
        FileUploadButton(
            files = files,
            onClick = {},
            onClearFile = {},
            label = "Upload ticket",
        )
    }

    @Composable
    private fun MultipleFilesSample() {
        val files = listOf(
            UploadedFile(
                path = "1",
                name = "Photo-1.jpg",
                sizeBytes = 512_000,
                mimeType = "image/jpeg",
            ),
            UploadedFile(
                path = "2",
                name = "Terms-and-conditions-long-name-document.pdf",
                sizeBytes = 3_072_000,
                mimeType = "application/pdf",
            ),
        )
        FileUploadButton(
            files = files,
            onClick = {},
            onClearFile = {},
            multiple = true,
            label = "Upload documents",
        )
    }
}

