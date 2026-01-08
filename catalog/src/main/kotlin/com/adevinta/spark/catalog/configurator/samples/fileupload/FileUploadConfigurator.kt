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
package com.adevinta.spark.catalog.configurator.samples.fileupload

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.fileupload.FileUploadButton
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.components.stepper.StepperForm
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitMode.MultipleWithState
import io.github.vinceglb.filekit.dialogs.FileKitPickerState
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.size
import kotlinx.collections.immutable.toImmutableList

public val FileUploadConfigurator: Configurator = Configurator(
    id = "fileupload",
    name = "File upload",
    description = "File upload configuration",
    sourceUrl = "$SampleSourceUrl/FileUploadSamples.kt",
) {
    FileUploadSample()
}

@Composable
private fun ColumnScope.FileUploadSample() {
    var enabled by remember { mutableStateOf(true) }
    val files = remember { mutableStateSetOf<UploadedFile>() }
    var pickerType by remember { mutableStateOf(FileUploadPickerType.File) }
    var maxFiles: Int by remember { mutableIntStateOf(0) }

    val selectedType = pickerType.toKitType()

    val singleLauncher = rememberFilePickerLauncher(
        type = selectedType,
        title = "Select file",
    ) { pickedFile ->
        if (pickedFile == null) return@rememberFilePickerLauncher
        val newFile = UploadedFile(
            path = pickedFile.path,
            name = pickedFile.name,
            sizeBytes = pickedFile.size(),
            mimeType = pickedFile.mimeType()?.primaryType,
        )
        files.clear()
        files.add(newFile)
    }
    val multipleLauncher = rememberFilePickerLauncher(
        type = selectedType,
        mode = MultipleWithState(maxFiles.takeIf { it > 0 }),
        title = "Select file",
    ) { state ->
        when (state) {
            is FileKitPickerState.Started -> {
                // Show loading indicator
                files.clear()
            }

            is FileKitPickerState.Progress -> {
                // Update progress for: state.processed
                println("Processing: ${state.processed.size} / ${state.total}")
                // Handle selected file: state.result
                val newFiles = state.processed.map { file ->
                    UploadedFile(
                        path = file.path,
                        name = file.name,
                        sizeBytes = file.size(),
                        mimeType = file.mimeType()?.primaryType,
                    )
                }
                files.addAll(newFiles)
            }

            is FileKitPickerState.Completed -> {
                // Handle selected file: state.result
                val newFiles = state.result.map { file ->
                    UploadedFile(
                        path = file.path,
                        name = file.name,
                        sizeBytes = file.size(),
                        mimeType = file.mimeType()?.primaryType,
                    )
                }
                files.addAll(newFiles)
            }

            is FileKitPickerState.Cancelled -> {
                // Handle cancellation
                println("Selection cancelled")
            }
        }
    }

    FileUploadButton(
        files = files.toImmutableList(),
        onClick = {
            if (enabled) singleLauncher.launch()
        },
        onClearFile = { file -> files.remove(file) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        mode = FileKitMode.Single,
        label = "Select single file",
    )

    FileUploadButton(
        files = files.toImmutableList(),
        onClick = {
            if (enabled) multipleLauncher.launch()
        },
        onClearFile = { file -> files.remove(file) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        mode = FileKitMode.MultipleWithState(maxItems = maxFiles),
        label = "Select multiple file",
    )

    SwitchLabelled(
        checked = enabled,
        onCheckedChange = { enabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    StepperForm(
        value = maxFiles,
        range = 0..50,
        onValueChange = {
            maxFiles = it
        },
        flexible = true,
        label = "Label",
        helper = "Using 0 will be considered as not limiting the amount of files selectable, however not that " +
                "if putting any other value the extreme maximum can only be 50",
    )

    ButtonGroup(
        title = "File type",
        selectedOption = pickerType,
        onOptionSelect = { pickerType = it },
    )
}


private enum class FileUploadPickerType {
    File,
    Image,
    Video,
    ImageAndVideo,
}

private fun FileUploadPickerType.toKitType(): FileKitType = when (this) {
    FileUploadPickerType.File -> FileKitType.File()
    FileUploadPickerType.Image -> FileKitType.Image
    FileUploadPickerType.Video -> FileKitType.Video
    FileUploadPickerType.ImageAndVideo -> FileKitType.ImageAndVideo
}

@Preview
@Composable
private fun FileUploadSamplePreview() {
    PreviewTheme { FileUploadSample() }
}

