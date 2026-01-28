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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SparkSampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.fileupload.FileUpload
import com.adevinta.spark.components.fileupload.FileUploadDefaults
import com.adevinta.spark.components.fileupload.FileUploadList
import com.adevinta.spark.components.fileupload.PreviewFile
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.icons.ImageOutline
import com.adevinta.spark.icons.SparkIcons
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay
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
    Example(
        id = "button-customization",
        name = "Button Customization",
        description = "Demonstrates button customization with different sizes, icons, and icon positions",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        ButtonCustomizationExample()
    },
    Example(
        id = "custom-button-content",
        name = "Custom Button Content",
        description = "Shows how to use custom button components instead of the default button",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        CustomButtonContentExample()
    },
    Example(
        id = "upload-progress-error",
        name = "Upload Progress & Error Handling",
        description = "Demonstrates handling upload progress, loading states, and error scenarios",
        sourceUrl = FileUploadExampleSourceUrl,
    ) {
        UploadProgressErrorExample()
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
            FileUploadList(
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
            FileUploadList(
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

@Composable
private fun ButtonCustomizationExample() {
    var selectedFile1 by rememberSaveable { mutableStateOf<UploadedFile?>(null) }
    var selectedFile2 by rememberSaveable { mutableStateOf<UploadedFile?>(null) }
    var selectedFile3 by rememberSaveable { mutableStateOf<UploadedFile?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Medium button with icon at end
        FileUpload.ButtonSingleSelect(
            onResult = { file -> selectedFile2 = file },
            label = "Medium with icon at end",
            modifier = Modifier.fillMaxWidth(),
            size = ButtonSize.Medium,
            icon = SparkIcons.ImageOutline,
            iconSide = IconSide.END,
        )

        VerticalSpacer(16.dp)

        // Large button with icon at start
        FileUpload.ButtonSingleSelect(
            onResult = { file -> selectedFile3 = file },
            label = "Large with icon",
            modifier = Modifier.fillMaxWidth(),
            size = ButtonSize.Large,
            icon = SparkIcons.ImageOutline,
            iconSide = IconSide.START,
        )

        VerticalSpacer(16.dp)

        selectedFile1?.let { file ->
            PreviewFile(
                file = file,
                onClear = { selectedFile1 = null },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        selectedFile2?.let { file ->
            VerticalSpacer(8.dp)
            PreviewFile(
                file = file,
                onClear = { selectedFile2 = null },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        selectedFile3?.let { file ->
            VerticalSpacer(8.dp)
            PreviewFile(
                file = file,
                onClear = { selectedFile3 = null },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CustomButtonContentExample() {
    var selectedFile1 by rememberSaveable { mutableStateOf<UploadedFile?>(null) }
    var selectedFile2 by rememberSaveable { mutableStateOf<UploadedFile?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Using ButtonTinted as custom button content
        FileUpload.ButtonSingleSelect(
            onResult = { file -> selectedFile1 = file },
            label = "Select file",
            modifier = Modifier.fillMaxWidth(),
            buttonContent = { onClick ->
                ButtonTinted(
                    onClick = onClick,
                    text = "Upload with Tinted Button",
                    icon = SparkIcons.ImageOutline,
                    iconSide = IconSide.START,
                )
            },
        )

        VerticalSpacer(16.dp)

        // Using ButtonOutlined as custom button content
        FileUpload.ButtonSingleSelect(
            onResult = { file -> selectedFile2 = file },
            label = "Select file",
            modifier = Modifier.fillMaxWidth(),
            buttonContent = { onClick ->
                ButtonOutlined(
                    onClick = onClick,
                    text = "Upload with Outlined Button",
                    icon = SparkIcons.ImageOutline,
                    iconSide = IconSide.END,
                )
            },
        )

        VerticalSpacer(16.dp)

        selectedFile1?.let { file ->
            PreviewFile(
                file = file,
                onClear = { selectedFile1 = null },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        selectedFile2?.let { file ->
            VerticalSpacer(8.dp)
            PreviewFile(
                file = file,
                onClear = { selectedFile2 = null },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun UploadProgressErrorExample() {
    var uploadedFile by rememberSaveable { mutableStateOf<UploadedFile?>(null) }
    var uploadProgress by rememberSaveable { mutableFloatStateOf(0f) }
    var uploadError by rememberSaveable { mutableStateOf<String?>(null) }

    // Simulate upload progress when a new file is selected
    LaunchedEffect(uploadedFile?.file?.path) {
        val file = uploadedFile ?: return@LaunchedEffect
        // Only start upload simulation if file doesn't already have progress/error state
        if (file.progress == null && !file.isLoading && file.errorMessage == null) {
            uploadProgress = 0f
            uploadError = null

            // Simulate upload progress
            for (i in 0..100 step 10) {
                delay(150)
                uploadProgress = i / 100f
                uploadedFile = file.copy(
                    progress = { uploadProgress },
                    isLoading = false,
                    errorMessage = null,
                )
            }

            // Simulate error at 80% progress (for demonstration purposes)
            delay(200)
            uploadedFile = file.copy(
                progress = null,
                isLoading = false,
                errorMessage = "Upload failed: Network error. Please try again.",
            )
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUpload.ButtonSingleSelect(
            onResult = { file ->
                if (file != null) {
                    // Reset state when new file is selected
                    uploadedFile = file
                    uploadError = null
                    uploadProgress = 0f
                } else {
                    uploadedFile = null
                    uploadError = null
                }
            },
            label = "Select file to upload",
            modifier = Modifier.fillMaxWidth(),
            icon = SparkIcons.ImageOutline,
            iconSide = IconSide.START,
        )

        VerticalSpacer(16.dp)

        uploadedFile?.let { file ->
            PreviewFile(
                file = file,
                onClear = {
                    uploadedFile = null
                    uploadProgress = 0f
                    uploadError = null
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
