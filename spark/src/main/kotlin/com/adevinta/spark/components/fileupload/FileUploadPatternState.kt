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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.compose.rememberCameraPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * State holder for file upload pattern that manages file picking logic.
 *
 * This state provides launchers for different picker types and tracks selected files.
 * It handles bookmark persistence internally and supports both single and multiple file modes.
 *
 * @property launchFilePicker Function to launch the file/gallery picker
 * @property launchCameraPicker Function to launch the camera picker
 * @property launchGalleryPicker Function to launch the gallery picker (same as launchFilePicker for images)
 * @property launchPicker Main launcher function that handles source selection based on [FileUploadType]
 * @property selectedFiles Currently selected files
 * @property isSingleMode Whether this pattern is in single file mode
 * @property maxFiles Maximum number of files allowed (null means no limit), only relevant for multiple mode
 */
@Stable
public class FileUploadPatternState internal constructor(
    public val launchFilePicker: () -> Unit,
    public val launchCameraPicker: () -> Unit,
    public val launchGalleryPicker: () -> Unit,
    public val launchPicker: () -> Unit,
    public val isSingleMode: Boolean,
    public val maxFiles: Int? = null,
)

/**
 * Creates and remembers a [FileUploadPatternState] for handling file uploads.
 *
 * This function sets up the file picker launchers and manages the selected files state.
 * It handles bookmark persistence and supports both single and multiple file selection modes.
 *
 * @param type Type of files to select (image, video, file, etc.)
 * @param mode Selection mode: single file or multiple files with optional max limit
 * @param title Optional title for the file picker dialog
 * @param directory Optional directory to open the picker in
 * @param dialogSettings Optional settings for the file picker dialog
 * @param onFilesSelected Callback invoked when files are selected
 * @return [FileUploadPatternState] that can be used to launch pickers and access selected files
 */
@Composable
public fun rememberFileUploadPattern(
    onFilesSelected: (ImmutableList<UploadedFile>) -> Unit,
    type: FileUploadType = FileUploadType.File(),
    mode: FileUploadMode = FileUploadMode.Single,
    title: String? = null,
    directory: PlatformFile? = null,
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
): FileUploadPatternState {
    // Use key to ensure each instance has independent state
    var selectedFiles by remember { mutableStateOf<ImmutableList<UploadedFile>>(persistentListOf()) }

    val isSingleMode = mode is FileUploadMode.Single
    val maxFiles = if (mode is FileUploadMode.Multiple) mode.maxFiles else null

    // File picker launcher (for gallery/file selection)
    val filePicker = if (isSingleMode) {
        // Single mode: callback receives PlatformFile? directly
        rememberFilePickerLauncher(
            type = type.toFileKitType(),
            title = title,
            directory = directory,
            dialogSettings = dialogSettings,
        ) { pickedFile ->
            if (pickedFile == null) return@rememberFilePickerLauncher
            val newFile = UploadedFile(file = pickedFile)
            selectedFiles = persistentListOf(newFile)
            onFilesSelected(selectedFiles)
        }
    } else {
        rememberFilePickerLauncher(
            type = type.toFileKitType(),
            mode = FileKitMode.Multiple(maxItems = maxFiles),
            title = title,
            directory = directory,
            dialogSettings = dialogSettings,
        ) { result ->
            val newFiles = result?.map { file ->
                UploadedFile(file = file)
            }.orEmpty().toImmutableList()
            selectedFiles = newFiles
            onFilesSelected(selectedFiles)
        }
    }

    // Camera picker launcher
    val cameraPicker = rememberCameraPickerLauncher { newPhoto ->
        if (newPhoto == null) return@rememberCameraPickerLauncher

        val newFile = UploadedFile(file = newPhoto)
        val updatedFiles = if (isSingleMode) {
            listOf(newFile)
        } else {
            selectedFiles + newFile
        }.toImmutableList()
        selectedFiles = updatedFiles
        onFilesSelected(updatedFiles)
    }

    // Determine which picker to launch based on type and source
    val launchPicker: () -> Unit = {
        when (type) {
            is FileUploadType.HasMultipleSource -> when (type.source) {
                ImageSource.Camera -> cameraPicker.launch()
                ImageSource.Gallery -> filePicker.launch()
            }

            is FileUploadType.File -> filePicker.launch()
        }
    }

    val launchFilePicker: () -> Unit = { filePicker.launch() }
    val launchCameraPicker: () -> Unit = { cameraPicker.launch() }
    val launchGalleryPicker: () -> Unit = { filePicker.launch() }

    // Create state object that updates when selectedFiles changes
    // The functions are recreated each time to ensure they capture current state
    return remember(selectedFiles.size, isSingleMode, maxFiles) {
        FileUploadPatternState(
            launchFilePicker = launchFilePicker,
            launchCameraPicker = launchCameraPicker,
            launchGalleryPicker = launchGalleryPicker,
            launchPicker = launchPicker,
            isSingleMode = isSingleMode,
            maxFiles = maxFiles,
        )
    }
}
