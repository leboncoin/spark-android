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
package com.adevinta.spark.catalog.examples.samples.fileupload

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SparkSampleSourceUrl
import com.adevinta.spark.components.fileupload.FileUpload
import com.adevinta.spark.components.fileupload.FileUploadDefaultPreview
import com.adevinta.spark.components.fileupload.FileUploadDefaults
import com.adevinta.spark.components.fileupload.PreviewFile
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.components.spacer.VerticalSpacer
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import java.io.File

private const val FileUploadExampleSourceUrl = "$SparkSampleSourceUrl/fileupload/FileUploadExamples.kt"

public val FileUploadExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "preview-states",
        name = "File Preview States",
        description = "Demonstrates all preview states: default, progress, indeterminate loading, error, and disabled",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        FilePreviewStatesExample()
    },
    Example(
        id = "single-file-upload",
        name = "Single File Upload",
        description = "Single file selection with preview",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        SingleFileUploadExample()
    },
    Example(
        id = "multiple-files-concatenated",
        name = "Multiple Files (Concatenated)",
        description = "Multiple file selection where new files are added to existing ones",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        MultipleFilesConcatenatedExample()
    },
    Example(
        id = "multiple-files-replaced",
        name = "Multiple Files (Replaced)",
        description = "Multiple file selection where new selection replaces existing files",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        MultipleFilesReplacedExample()
    },
)

@Composable
private fun FilePreviewStatesExample() {
    Column(modifier = Modifier.fillMaxWidth()) {
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

@Composable
private fun SingleFileUploadExample() {
    var selectedFile by rememberSaveable { mutableStateOf<UploadedFile?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUpload.ButtonSingleSelect(
            onResult = { file -> selectedFile = file },
            label = "Select a file",
            modifier = Modifier.fillMaxWidth(),
        )

        VerticalSpacer(16.dp)

        selectedFile?.let { file ->
            PreviewFile(
                file = file,
                onClear = { selectedFile = null },
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    FileUploadDefaults.openFile(file)
                },
            )
        }
    }
}

@Composable
private fun MultipleFilesConcatenatedExample() {
    var selectedFiles by remember {
        mutableStateOf<ImmutableSet<UploadedFile>>(persistentSetOf())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUpload.Button(
            onResult = { files ->
                // Concatenate: add new files to existing ones
                selectedFiles = selectedFiles.plus(files).toImmutableSet()
            },
            label = "Add files",
            modifier = Modifier.fillMaxWidth(),
        )

        VerticalSpacer(16.dp)

        if (selectedFiles.isNotEmpty()) {
            FileUploadDefaultPreview(
                files = selectedFiles.toImmutableList(),
                onClearFile = { file ->
                    selectedFiles = selectedFiles.filterNot { it == file }.toImmutableSet()
                },
                onClick = { file ->
                    FileUploadDefaults.openFile(file)
                },
            )
        }
    }
}

@Composable
private fun MultipleFilesReplacedExample() {
    var selectedFiles by remember {
        mutableStateOf<ImmutableList<UploadedFile>>(persistentListOf())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUpload.Button(
            onResult = { files ->
                // Replace: new selection replaces existing files
                selectedFiles = files
            },
            label = "Select files",
            modifier = Modifier.fillMaxWidth(),
        )

        VerticalSpacer(16.dp)

        if (selectedFiles.isNotEmpty()) {
            FileUploadDefaultPreview(
                files = selectedFiles,
                onClearFile = { file ->
                    selectedFiles = selectedFiles.filterNot { it == file }.toImmutableList()
                },
                onClick = { file ->
                    FileUploadDefaults.openFile(file)
                },
            )
        }
    }
}
