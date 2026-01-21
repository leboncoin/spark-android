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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.icons.IconPickerItem
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.ui.animations.AnimatedNullableVisibility
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.fileupload.FileExtensionStandard
import com.adevinta.spark.components.fileupload.FileUpload
import com.adevinta.spark.components.fileupload.FileUploadDefaultPreview
import com.adevinta.spark.components.fileupload.FileUploadDefaults
import com.adevinta.spark.components.fileupload.FileUploadType
import com.adevinta.spark.components.fileupload.ImageSource
import com.adevinta.spark.components.fileupload.PreviewFile
import com.adevinta.spark.components.fileupload.UploadedFile
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.stepper.StepperForm
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import io.github.vinceglb.filekit.path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
    var enabled by rememberSaveable { mutableStateOf(true) }
    var singleFile by remember { mutableStateOf<UploadedFile?>(null) }
    var multipleFiles by remember { mutableStateOf<ImmutableList<UploadedFile>>(persistentListOf()) }
    var pickerType by rememberSaveable { mutableStateOf(FileUploadPickerType.File) }
    var imageSource by rememberSaveable { mutableStateOf(ImageSource.Gallery) }
    var fileExtension by rememberSaveable { mutableStateOf(FileExtensionStandard.All) }
    var maxFiles: Int by rememberSaveable { mutableIntStateOf(0) }
    var clearIcon by remember { mutableStateOf<SparkIcon>(SparkIcons.Close) }

    // Track file states separately
    var fileStates by remember {
        mutableStateOf<Map<String, FileState>>(emptyMap())
    }

    val selectedType by remember { derivedStateOf { pickerType.toFileUploadType(imageSource, fileExtension) } }

    // Helper function to apply states to a file
    fun UploadedFile.applyState(globalEnabled: Boolean = true): UploadedFile {
        val state = fileStates[file.path]
        // If global enabled is false, override individual file enabled state
        val finalEnabled = if (!globalEnabled) false else (state?.enabled ?: this.enabled)
        return copy(
            enabled = finalEnabled,
            progress = state?.progress?.let { { it / 100f } } ?: this.progress,
            errorMessage = state?.errorMessage ?: this.errorMessage,
        )
    }

    // Helper function to get isLoading state for a file
    fun UploadedFile.getIsLoading(): Boolean {
        val state = fileStates[file.path]
        return state?.isLoading ?: false
    }

    FileUpload.ButtonSingleSelect(
        onResult = { file -> singleFile = file },
        label = "Select single file",
        modifier = Modifier.fillMaxWidth(),
        type = selectedType,
        enabled = enabled,
    )

    // Display preview for single file
    AnimatedNullableVisibility(
        value = singleFile,
    ) { file ->
        val fileWithState = file.applyState(globalEnabled = enabled)
        PreviewFile(
            file = fileWithState,
            onClear = { singleFile = null },
            progress = fileWithState.progress,
            errorMessage = fileWithState.errorMessage,
            enabled = fileWithState.enabled,
            onClick = {
                FileUploadDefaults.openFile(fileWithState)
            },
            clearIcon = clearIcon,
            isLoading = file.getIsLoading(),
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    FileUpload.Button(
        onResult = { files ->
            multipleFiles = multipleFiles
                .plus(files)
                .let {
                    if (maxFiles > 0) it.take(maxFiles) else it
                }.toImmutableList()
        },
        label = "Select multiple file",
        modifier = Modifier.fillMaxWidth(),
        type = selectedType,
        maxFiles = maxFiles.takeIf { it > 0 },
        enabled = enabled,
    )

    // Display preview for multiple files with applied states
    val filesWithStates by remember {
        derivedStateOf {
            multipleFiles.map { it.applyState(globalEnabled = enabled) }.toImmutableList()
        }
    }
    FileUploadDefaultPreview(
        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        files = filesWithStates,
        onClearFile = { file ->
            // Find original file by path since we're comparing files with applied states
            val originalFile = multipleFiles.find { it.file.path == file.file.path }
            if (originalFile != null) {
                multipleFiles = multipleFiles.filterNot { it == originalFile }.toImmutableList()
            }
        },
        onClick = { file ->
            FileUploadDefaults.openFile(file)
        },
        clearIcon = clearIcon,
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
        label = "Max files",
        helper = "Maximum number of files that can be selected. Use 0 for no limit (maximum 50 files allowed).",
    )

    ButtonGroup(
        title = "File type",
        selectedOption = pickerType,
        onOptionSelect = { pickerType = it },
    )

    // Show file extension selector only when File type is selected
    if (pickerType == FileUploadPickerType.File) {
        DropdownEnum(
            title = "File extension filter",
            selectedOption = fileExtension,
            onOptionSelect = { fileExtension = it },
        )
    }

    // Show ImageSource selector only when Image type is selected
    if (pickerType != FileUploadPickerType.File) {
        ButtonGroup(
            title = "Image source",
            selectedOption = imageSource,
            onOptionSelect = { imageSource = it },
        )
    }

    // Clear icon selector
    IconPickerItem(
        label = "Clear icon",
        selectedIcon = clearIcon,
        onIconSelected = { icon -> if (icon != null) clearIcon = icon },
    )

    // File state controls
    val allFiles = remember(singleFile, multipleFiles) {
        buildList {
            singleFile?.let { add(it) }
            addAll(multipleFiles)
        }
    }

    if (allFiles.isNotEmpty()) {
        VerticalSpacer(16.dp)
        Text(
            text = "File state controls",
            modifier = Modifier.fillMaxWidth(),
        )
        VerticalSpacer(8.dp)

        allFiles.forEach { file ->
            val filePath = file.file.path
            val currentState = fileStates[filePath] ?: FileState()
            val fileWithState = file.applyState()

            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                FileStateControls(
                    file = fileWithState,
                    fileName = file.name,
                    state = currentState,
                    onStateChange = { newState ->
                        fileStates = fileStates + (filePath to newState)
                    },
                )
            }
            VerticalSpacer(12.dp)
        }
    }
}

private data class FileState(
    val enabled: Boolean = true,
    val progress: Int? = null, // 0-100
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)

@Composable
private fun ColumnScope.FileStateControls(
    file: UploadedFile,
    fileName: String,
    state: FileState,
    onStateChange: (FileState) -> Unit,
) {
    var errorMessageText by rememberSaveable(state.errorMessage) {
        mutableStateOf(state.errorMessage ?: "")
    }
    var progressValue by rememberSaveable(state.progress) {
        mutableIntStateOf(state.progress ?: 0)
    }

    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        // File name as heading
        Text(
            text = fileName,
            style = SparkTheme.typography.headline2,
            modifier = Modifier.fillMaxWidth(),
        )
        VerticalSpacer(12.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Controls",
                    style = SparkTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                SwitchLabelled(
                    checked = state.enabled,
                    onCheckedChange = { enabled ->
                        onStateChange(state.copy(enabled = enabled))
                    },
                ) {
                    Text(
                        text = "Enabled",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                SwitchLabelled(
                    checked = state.progress != null,
                    enabled = state.enabled && !state.isLoading,
                    onCheckedChange = { hasProgress ->
                        onStateChange(
                            state.copy(
                                progress = if (hasProgress) progressValue else null,
                                errorMessage = if (hasProgress) null else state.errorMessage,
                                isLoading = if (hasProgress) false else state.isLoading,
                            ),
                        )
                    },
                ) {
                    Text(
                        text = "Show loading (progress)",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (state.progress != null) {
                    Text(
                        text = "Progress: $progressValue%",
                        style = SparkTheme.typography.body2,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    )
                    Slider(
                        value = progressValue.toFloat(),
                        onValueChange = { newValue ->
                            val intValue = newValue.toInt()
                            progressValue = intValue
                            onStateChange(state.copy(progress = intValue))
                        },
                        valueRange = 0f..100f,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                SwitchLabelled(
                    checked = state.isLoading,
                    enabled = state.enabled && state.progress == null,
                    onCheckedChange = { isLoading ->
                        onStateChange(
                            state.copy(
                                isLoading = isLoading,
                                errorMessage = if (isLoading) null else state.errorMessage,
                            ),
                        )
                    },
                ) {
                    Text(
                        text = "Show indeterminate loading",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                SwitchLabelled(
                    checked = state.errorMessage != null,
                    enabled = state.enabled,
                    onCheckedChange = { hasError ->
                        onStateChange(
                            state.copy(
                                errorMessage = if (hasError) {
                                    errorMessageText.takeIf { it.isNotBlank() }
                                        ?: "Error message"
                                } else {
                                    null
                                },
                                progress = if (hasError) null else state.progress,
                            ),
                        )
                    },
                ) {
                    Text(
                        text = "Show error",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = errorMessageText,
                        onValueChange = { newText ->
                            errorMessageText = newText
                            onStateChange(state.copy(errorMessage = newText.takeIf { it.isNotBlank() }))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Error message",
                    )
                }
            }
        }
    }
}

private enum class FileUploadPickerType {
    File,
    Image,
    Video,
    ImageAndVideo,
}

private fun FileUploadPickerType.toFileUploadType(
    imageSource: ImageSource = ImageSource.Gallery,
    fileExtension: FileExtensionStandard = FileExtensionStandard.All,
): FileUploadType =
    when (this) {
        FileUploadPickerType.File -> FileUploadType.File(
            extensions = fileExtension.extensions.takeIf {
                it.isNotEmpty()
            },
        )

        FileUploadPickerType.Image -> FileUploadType.Image(source = imageSource)

        FileUploadPickerType.Video -> FileUploadType.Video(source = imageSource)

        FileUploadPickerType.ImageAndVideo -> FileUploadType.ImageAndVideo(source = imageSource)
    }

@Preview
@Composable
private fun FileUploadSamplePreview() {
    PreviewTheme { FileUploadSample() }
}
