/*
 * Copyright (c) 2026 Adevinta
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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import kotlinx.collections.immutable.ImmutableList

/**
 * High level file upload component for single file selection.
 *
 * This component provides a button to trigger file selection. To display selected files,
 * manage state yourself and use [PreviewFile] or [FileUploadList].
 *
 * @param onResult Callback invoked when a file is selected or cleared (null)
 * @param label Text label for the default button
 * @param modifier Modifier to be applied to the button
 * @param type Type of files to select (image, video, file, etc.)
 * @param title Optional title for the file picker dialog
 * @param directory Optional directory to open the picker in
 * @param dialogSettings Optional settings for the file picker dialog
 * @param enabled Whether the button is enabled
 * @param onClickLabel If provided, it'll be spoken in place of the default "double tap to activate".
 * @param buttonContent Composable lambda for custom button. Receives onClick callback.
 * Defaults to a filled button with the label.
 *
 * @sample com.adevinta.spark.components.fileupload.FileUploadSamples
 */
@Composable
internal fun FileUploadSingleButton(
    onResult: (UploadedFile?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Medium,
    icon: SparkIcon? = null,
    iconSide: IconSide = IconSide.START,
    type: FileUploadType = FileUploadType.File(),
    title: String? = null,
    directory: PlatformFile? = null,
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
    enabled: Boolean = true,
    onClickLabel: String? = null,
    buttonContent: @Composable (onClick: () -> Unit) -> Unit = { onClick ->
        ButtonFilled(
            modifier = Modifier
                .fillMaxWidth()
                .ifNotNull(onClickLabel) {
                    semantics { onClick(label = onClickLabel, action = null) }
                },
            onClick = onClick,
            text = label,
            intent = ButtonIntent.Basic,
            enabled = enabled,
        )
    },
) {
    val pattern = rememberFileUploadPattern(
        type = type,
        mode = FileUploadMode.Single,
        title = title,
        directory = directory,
        dialogSettings = dialogSettings,
        onFilesSelect = { files -> onResult(files.firstOrNull()) },
    )

    FileUploadPattern(
        pattern = pattern,
        modifier = modifier.sparkUsageOverlay(),
        content = buttonContent,
    )
}

/**
 * High level file upload component for multiple file selection.
 *
 * This component provides a button to trigger multiple file selection. To display selected files,
 * manage state yourself and use [PreviewFile] or [FileUploadList].
 *
 * @param onResult Callback invoked when files are selected
 * @param label Text label for the default button
 * @param modifier Modifier to be applied to the button
 * @param type Type of files to select (image, video, file, etc.)
 * @param maxFiles Maximum number of files that can be selected. If null, no limit.
 * @param title Optional title for the file picker dialog
 * @param directory Optional directory to open the picker in
 * @param dialogSettings Optional settings for the file picker dialog
 * @param enabled Whether the button is enabled
 * @param onClickLabel If provided, it'll be spoken in place of the default "double tap to activate".
 * @param buttonContent Composable lambda for custom button. Receives onClick callback.
 * Defaults to a filled button with the label.
 *
 * @sample com.adevinta.spark.components.fileupload.FileUploadSamples
 */
@Composable
internal fun FileUploadButton(
    onResult: (ImmutableList<UploadedFile>) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Medium,
    icon: SparkIcon? = null,
    iconSide: IconSide = IconSide.START,
    type: FileUploadType = FileUploadType.File(),
    maxFiles: Int? = null,
    title: String? = null,
    directory: PlatformFile? = null,
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
    enabled: Boolean = true,
    onClickLabel: String? = null,
    buttonContent: @Composable (onClick: () -> Unit) -> Unit = { onClick ->
        ButtonFilled(
            modifier = Modifier
                .fillMaxWidth()
                .ifNotNull(onClickLabel) {
                    semantics { onClick(label = onClickLabel, action = null) }
                },
            onClick = onClick,
            text = label,
            intent = ButtonIntent.Basic,
            enabled = enabled,
        )
    },
) {
    val pattern = rememberFileUploadPattern(
        type = type,
        mode = FileUploadMode.Multiple(maxFiles),
        title = title,
        directory = directory,
        dialogSettings = dialogSettings,
        onFilesSelect = onResult,
    )

    FileUploadPattern(
        pattern = pattern,
        modifier = modifier.sparkUsageOverlay(),
        content = buttonContent,
    )
}
