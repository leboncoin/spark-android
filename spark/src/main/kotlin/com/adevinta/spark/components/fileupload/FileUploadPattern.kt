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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.chips.ChipDashed
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitCameraType
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.compose.rememberCameraPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * High-level wrapper composable that integrates file upload pattern with any component.
 *
 * This composable allows you to wrap any component (button, surface, image, etc.) with file upload
 * functionality. The pattern handles all file picking logic while you control the UI trigger.
 *
 * For displaying selected files, use [PreviewFile] or [FileUploadList] separately.
 *
 * @param pattern The [FileUploadPatternState] from [rememberFileUploadPattern]
 * @param modifier Modifier to be applied to the container
 * @param content Composable lambda that receives an onClick function to trigger file selection.
 *                This can be any component that accepts an onClick callback.
 *
 * @sample com.adevinta.spark.components.fileupload.FileUploadWrapperSamples
 */
@ExperimentalSparkApi
@Composable
public fun FileUploadPattern(
    pattern: FileUploadPatternState,
    modifier: Modifier = Modifier,
    content: @Composable (onClick: () -> Unit) -> Unit,
) {
    content(pattern.launchPicker)
}

/**
 * State holder for file upload pattern that manages file picking logic.
 *
 * This state provides launchers for different picker types.
 * It abstracts the complexity of handling different selection modes (single vs. multiple)
 * and source types like camera or system file picker.
 *
 * @property launchFilePicker Function to launch the file picker.
 * @property launchCameraPicker Function to launch the camera picker.
 * @property launchGalleryPicker Function to launch the gallery picker (using the system file picker
 * filtered for images).
 * @property launchPicker Main launcher function that handles source selection based on [FileUploadType].
 * @property isSingleMode Whether this pattern is in single file mode.
 * @property maxFiles Maximum number of files allowed (null means no limit), only relevant for multiple mode.
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
 * Creates and remembers a [FileUploadPatternState] to manage file upload logic within a Composable.
 *
 * This function initializes the necessary launchers for picking files from the system or capturing
 * media via the camera. It abstracts the complexity of handling different selection modes
 * (single vs. multiple) and source types.
 *
 * @param onFilesSelect Callback invoked when files are successfully selected or captured.
 * Returns an empty list if the selection is cancelled or an error occurs.
 * @param type The category of files to be selected (e.g., Image, Video, or generic File).
 * This also determines the available sources like Camera or Gallery.
 * @param mode The selection strategy: [FileUploadMode.Single] for one file or
 * [FileUploadMode.Multiple] for several files with an optional limit.
 * @param title An optional title displayed in the file picker system dialog.
 * @param directory An optional initial directory for the file picker to open in.
 * @param dialogSettings Specific configuration for the picker dialog, primarily for desktop platforms.
 * @return A [FileUploadPatternState] used to trigger the pickers and query the current selection configuration.
 */
@ExperimentalSparkApi
@Composable
public fun rememberFileUploadPattern(
    onFilesSelect: (ImmutableList<UploadedFile>) -> Unit,
    type: FileUploadType = FileUploadType.File(),
    mode: FileUploadMode = FileUploadMode.Single,
    title: String? = null,
    directory: PlatformFile? = null,
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
): FileUploadPatternState {
    val isSingleMode = mode is FileUploadMode.Single
    val maxFiles = if (mode is FileUploadMode.Multiple) mode.maxFiles else null

    // File picker launcher (for gallery/file selection)
    val fileKitType = type.toFileKitType()
    val filePicker = if (isSingleMode) {
        // Single mode: callback receives PlatformFile? directly
        rememberFilePickerLauncher(
            type = fileKitType,
            title = title,
            directory = directory,
            dialogSettings = dialogSettings,
        ) { pickedFile ->
            if (pickedFile == null) return@rememberFilePickerLauncher
            val newFile = UploadedFile(file = pickedFile)
            onFilesSelect(persistentListOf(newFile))
        }
    } else {
        rememberFilePickerLauncher(
            type = fileKitType,
            mode = FileKitMode.Multiple(maxItems = maxFiles),
            title = title,
            directory = directory,
            dialogSettings = dialogSettings,
        ) { result ->
            val newFiles = result?.map { file ->
                UploadedFile(file = file)
            }.orEmpty().toImmutableList()
            onFilesSelect(newFiles)
        }
    }

    // Camera picker launcher
    val cameraPicker = rememberCameraPickerLauncher { newPhoto ->
        if (newPhoto == null) return@rememberCameraPickerLauncher

        val newFile = UploadedFile(file = newPhoto)
        onFilesSelect(persistentListOf(newFile))
    }

    val launchPicker: () -> Unit = {
        when (type) {
            is FileUploadType.HasMultipleSource -> when (type.source) {
                ImageSource.Camera -> cameraPicker.launch(type = FileKitCameraType.Photo)
                ImageSource.Gallery -> filePicker.launch()
            }

            else -> filePicker.launch()
        }
    }

    return remember(isSingleMode, maxFiles, type) {
        FileUploadPatternState(
            launchFilePicker = { filePicker.launch() },
            launchCameraPicker = { cameraPicker.launch() },
            launchGalleryPicker = { filePicker.launch() },
            launchPicker = launchPicker,
            isSingleMode = isSingleMode,
            maxFiles = maxFiles,
        )
    }
}

@Preview
@Composable
private fun FileUploadPatternPreview() {
    PreviewTheme {
        val pattern = rememberFileUploadPattern(
            onFilesSelect = {},
        )
        // Use Chip as this is not a prebuild FileUpload component
        FileUploadPattern(
            pattern = pattern,
        ) { onClick ->
            ChipDashed(
                onClick = onClick,
                text = "Upload File",
            )
        }
    }
}
