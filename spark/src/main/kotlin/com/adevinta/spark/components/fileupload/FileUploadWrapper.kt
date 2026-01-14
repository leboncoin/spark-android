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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import io.github.vinceglb.filekit.path
import kotlinx.collections.immutable.ImmutableList

/**
 * High-level wrapper composable that integrates file upload pattern with any component.
 *
 * This composable allows you to wrap any component (button, surface, image, etc.) with file upload
 * functionality. The pattern handles all file picking logic while you control the UI trigger.
 *
 * For displaying selected files, use [PreviewFile] or [FileUploadDefaultPreview] separately.
 *
 * @param pattern The [FileUploadPatternState] from [rememberFileUploadPattern]
 * @param modifier Modifier to be applied to the container
 * @param content Composable lambda that receives an onClick function to trigger file selection.
 *                This can be any component that accepts an onClick callback.
 *
 * @sample com.adevinta.spark.components.fileupload.FileUploadWrapperSamples
 */
@Composable
public fun FileUploadPattern(
    pattern: FileUploadPatternState,
    content: @Composable (onClick: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    content(pattern.launchPicker)
}

/**
 * Default preview implementation that displays uploaded files in a list.
 *
 * This function provides a standard preview layout that:
 * - Shows each file with its icon, name, and size
 * - Displays progress bars during upload
 * - Shows error messages with red border and warning icon
 * - Disables clear button during upload (when progress is active)
 *
 * @param files List of uploaded files to display
 * @param onClearFile Callback invoked when a file should be removed
 * @param modifier Modifier to be applied to the preview container
 * @param onClick Optional callback invoked when a file preview is clicked. Receives the clicked file.
 * @param clearIcon Icon to use for the clear button. Defaults to [SparkIcons.Close].
 * @param isLoading Optional function that determines if a file is in indeterminate loading state. Receives the file and returns true if loading.
 */
@Composable
public fun FileUploadDefaultPreview(
    files: ImmutableList<UploadedFile>,
    onClearFile: (UploadedFile) -> Unit,
    modifier: Modifier = Modifier,
    onClick: ((UploadedFile) -> Unit)? = null,
    clearIcon: SparkIcon = SparkIcons.Close,
    isLoading: ((UploadedFile) -> Boolean)? = null,
) {
    AnimatedVisibility(files.isNotEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Display all files
            files.forEach { file ->
                key(file.file.path) {
                    PreviewFile(
                        file = file,
                        onClear = { onClearFile(file) },
                        progress = file.progress,
                        errorMessage = file.errorMessage,
                        enabled = file.enabled,
                        onClick = onClick?.let { { it(file) } },
                        clearIcon = clearIcon,
                        isLoading = isLoading?.invoke(file) ?: false,
                    )
                }
            }
        }
    }
}
