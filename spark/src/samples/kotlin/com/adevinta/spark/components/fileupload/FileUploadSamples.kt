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
package com.adevinta.spark.components.fileupload

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
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.icons.ImageOutline
import com.adevinta.spark.icons.LeboncoinIcons
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import java.io.File

/**
 * Demonstrates single file selection: shows a button and a preview of the selected file.
 * Tapping the preview clears the selection.
 */
@OptIn(InternalSparkApi::class)
@Composable
public fun FileUploadSamples() {
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
                onClick = { FileUploadDefaults.openFile(file) },
            )
        }
    }
}

/**
 * Demonstrates multiple file selection: new files are appended to the existing list.
 * Each file can be individually removed from the list.
 */
@OptIn(InternalSparkApi::class)
@Composable
public fun FileUploadMultipleSamples() {
    var selectedFiles by remember {
        mutableStateOf<ImmutableSet<UploadedFile>>(persistentSetOf())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUpload.Button(
            onResult = { files ->
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
                onClick = { file -> FileUploadDefaults.openFile(file) },
            )
        }
    }
}

/**
 * Demonstrates customizing the upload button with a different style via [buttonContent].
 */
@OptIn(InternalSparkApi::class)
@Composable
public fun FileUploadCustomButtonSamples() {
    var selectedFile by rememberSaveable { mutableStateOf<UploadedFile?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUpload.ButtonSingleSelect(
            onResult = { file -> selectedFile = file },
            label = "Select file",
            modifier = Modifier.fillMaxWidth(),
            buttonContent = { onClick ->
                ButtonTinted(
                    onClick = onClick,
                    text = "Upload with Tinted Button",
                    icon = LeboncoinIcons.ImageOutline,
                    iconSide = IconSide.START,
                )
            },
        )

        VerticalSpacer(16.dp)

        selectedFile?.let { file ->
            PreviewFile(
                file = file,
                onClear = { selectedFile = null },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Demonstrates all [PreviewFile] states: default, progress, indeterminate loading, error, and disabled.
 */
@OptIn(InternalSparkApi::class)
@Composable
public fun FilePreviewStatesSamples() {
    val defaultFile = remember { UploadedFile(file = PlatformFile(file = File("document.pdf"))) }
    val progressFile = remember { UploadedFile(file = PlatformFile(file = File("image.jpg")), progress = { 0.65f }) }
    val loadingFile = remember { UploadedFile(file = PlatformFile(file = File("video.mp4")), isLoading = true) }
    val errorFile = remember {
        UploadedFile(
            file = PlatformFile(file = File("large-file.zip")),
            errorMessage = "File size exceeds maximum limit of 10MB",
        )
    }
    val disabledFile = remember { UploadedFile(file = PlatformFile(file = File("archive.rar")), enabled = false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        PreviewFile(file = defaultFile, onClear = {}, modifier = Modifier.fillMaxWidth())
        VerticalSpacer(16.dp)
        PreviewFile(file = progressFile, onClear = {}, modifier = Modifier.fillMaxWidth())
        VerticalSpacer(16.dp)
        PreviewFile(file = loadingFile, onClear = {}, modifier = Modifier.fillMaxWidth())
        VerticalSpacer(16.dp)
        PreviewFile(file = errorFile, onClear = {}, modifier = Modifier.fillMaxWidth())
        VerticalSpacer(16.dp)
        PreviewFile(file = disabledFile, onClear = {}, modifier = Modifier.fillMaxWidth())
    }
}

/**
 * Demonstrates using [FileUploadPattern] to wrap any custom composable with file-picking logic.
 */
@OptIn(InternalSparkApi::class)
@Composable
public fun FileUploadWrapperSamples() {
    var selectedFiles by remember {
        mutableStateOf<ImmutableList<UploadedFile>?>(null)
    }
    val pattern = rememberFileUploadPattern(
        onFilesSelect = { files -> selectedFiles = files },
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        FileUploadPattern(pattern = pattern) { onClick ->
            ButtonTinted(
                onClick = onClick,
                text = "Pick files",
                icon = LeboncoinIcons.ImageOutline,
                iconSide = IconSide.START,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(16.dp)

        selectedFiles?.let { files ->
            FileUploadList(
                files = files,
                onClearFile = { file ->
                    selectedFiles = selectedFiles?.filterNot { it == file }?.toImmutableList()
                },
                onClick = { file -> FileUploadDefaults.openFile(file) },
            )
        }
    }
}
