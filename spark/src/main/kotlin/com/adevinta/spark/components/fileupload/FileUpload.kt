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

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.icons.CvOutline
import com.adevinta.spark.icons.FilePdfOutline
import com.adevinta.spark.icons.ImageOutline
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberCameraPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.mimeType.MimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.launch

/**
 * Model representing a file selected by the user through a file picker.
 *
 * This model is intentionally simple and UI-focused. It is FileKit-agnostic so that
 * consumers can adapt their own file picking solutions by mapping their domain model
 * into [UploadedFile].
 */
@Immutable
public data class UploadedFile(
    val file: PlatformFile,
    val enabled: Boolean = true,
    val progress: (() -> Float)? = null,
    val errorMessage: String? = null,
) {
    public val name: String = file.name
    public val sizeBytes: Long = file.size()
    public val mimeType: MimeType? = file.mimeType()
}

/**
 * Defaults for the FileUpload components.
 *
 * This object centralises default text, icon mapping and layout behaviour to keep the
 * public API of [FileUploadButton] small and consistent with other Spark components.
 */
public object FileUploadDefaults {

    /**
     * Maps a [UploadedFile] to an icon representing its type.
     *
     * This is a best-effort mapping based on the mimeType or filename extension and is
     * intentionally simple. Consumers can override the icon used in the preview item if
     * they need more control.
     */
    public fun iconFor(file: UploadedFile): SparkIcon {
        val mimeType = file.mimeType?.primaryType.orEmpty()
        val lowerName = file.name.lowercase()

        return when {
            mimeType.startsWith("image/") ||
                    lowerName.endsWith(".png") ||
                    lowerName.endsWith(".jpg") ||
                    lowerName.endsWith(".jpeg") ||
                    lowerName.endsWith(".webp") -> {
                SparkIcons.ImageOutline
            }

            mimeType == "application/pdf" || lowerName.endsWith(".pdf") -> {
                SparkIcons.FilePdfOutline
            }

            else -> SparkIcons.CvOutline
        }
    }
}

/**
 * High level file upload component composed of a button and an optional list of file previews.
 *
 * This component is agnostic of the underlying file picker library. Consumers are expected to
 * trigger their file picker in [onClick] and update [files] when new files are selected.
 */
@Composable
public fun <PickerResult, ConsumedResult> FileUploadSingleButton(
    onResult: (UploadedFile?) -> Unit,
    mode: FileKitMode<PickerResult, ConsumedResult>,
    label: String,
    modifier: Modifier = Modifier,
    type: FileUploadType = FileUploadType.File(),
    camera: Boolean = false,
    title: String? = null,
    directory: PlatformFile? = null,
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
    enabled: Boolean = true,
    buttonContent: @Composable (onClick: () -> Unit) -> Unit = { onClick ->
        ButtonFilled(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            text = label,
            intent = ButtonIntent.Basic,
            enabled = enabled,
        )
    },
    preview: (@Composable ColumnScope.(UploadedFile?) -> Unit)?
    = { file ->
        file?.let {
            FileUploadPreviewList {
                PreviewFile(
                    file = file,
                    onClear = { onClearFile(file) },
                    enabled = file.enabled,
                )
            }
        }
    },
) {
    val bookmarkManager = rememberBookmark()
    val coroutineScope = rememberCoroutineScope()
    var userPickedFile by remember { mutableStateOf<PlatformFile?>(null) }
    var userPickedUploadFile by remember { mutableStateOf<UploadedFile?>(null) }

    val filePicker = rememberFilePickerLauncher(
        type = type.toFileKitType(),
        title = title,
    ) { pickedFile ->
        if (pickedFile == null) return@rememberFilePickerLauncher
        userPickedFile = pickedFile
        val newFile = UploadedFile(
            file = pickedFile,
        )
        onResult(newFile)
        userPickedUploadFile = newFile
        coroutineScope.launch {
            bookmarkManager.save(pickedFile)
        }
    }
    val cameraPicker = rememberCameraPickerLauncher() { newPhoto ->
        userPickedFile = newPhoto
        val newFile = UploadedFile(
            file = newPhoto,
        )
        onResult(newFile)
        userPickedUploadFile = newFile
        coroutineScope.launch {
            bookmarkManager.save(newPhoto)
        }
    }


    // Load the file when the screen is first composed
    LaunchedEffect(Unit) {
        userPickedFile = bookmarkManager.load()
    }

    Column(
        modifier = modifier,
    ) {
        buttonContent({ filePicker.launch() })
        preview?.invoke(this@Column, userPickedUploadFile)
    }
}

///**
// * High level file upload component composed of a button and an optional list of file previews.
// *
// * This component is agnostic of the underlying file picker library. Consumers are expected to
// * trigger their file picker in [onClick] and update [files] when new files are selected.
// */
//@Composable
//public fun <PickerResult, ConsumedResult> FileUploadMultipleButton(
//    onResult: (UploadedFile?) -> Unit,
//    mode: FileKitMode<PickerResult, ConsumedResult>,
//    label: String,
//    modifier: Modifier = Modifier,
//    type: FileKitType = FileKitType.File(),
//    title: String? = null,
//    directory: PlatformFile? = null,
//    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
//    enabled: Boolean = true,
//    buttonContent: @Composable (onClick: () -> Unit) -> Unit = { onClick ->
//        ButtonFilled(
//            modifier = Modifier.fillMaxWidth(),
//            onClick = onClick,
//            text = label,
//            intent = ButtonIntent.Basic,
//            enabled = enabled,
//        )
//    },
//    preview: (
//    @Composable ColumnScope.(List<UploadedFile>) -> Unit)? = { files ->
//        if (files.isNotEmpty()) {
//            Spacer(modifier = Modifier.height(8.dp))
//            FileUploadPreviewList(
//                files = files,
//                onClearFile = onClearFile,
//                mode = mode,
//            )
//        }
//    },
//) {
//    val bookmarkManager = rememberBookmark()
//    val coroutineScope = rememberCoroutineScope()
//    var userPickedFile by remember { mutableStateOf<PlatformFile?>(null) }
//    var userPickedUploadFile by remember { mutableStateOf<PlatformFile?>(null) }
//
//    val picker = rememberFilePickerLauncher(
//        type = type,
//        title = title,
//    ) { pickedFile ->
//        if (pickedFile == null) return@rememberFilePickerLauncher
//        val newFile = UploadedFile(
//            path = pickedFile.path,
//            name = pickedFile.name,
//            sizeBytes = pickedFile.size(),
//            mimeType = pickedFile.mimeType()?.primaryType,
//        )
//        onResult(newFile)
//        coroutineScope.launch {
//            coroutineScope.launch {
//                bookmarkManager.save(dir)
//            }
//        }
//    }
//
//
//    // Load the file when the screen is first composed
//    LaunchedEffect(Unit) {
//        userPickedFile = bookmarkManager.load()
//    }
//
//    Column(
//        modifier = modifier,
//    ) {
//        buttonContent({ picker.launch() })
//        ButtonFilled(
//            modifier = Modifier.fillMaxWidth(),
//            onClick = { picker.launch() },
//            text = label,
//            intent = ButtonIntent.Basic,
//            enabled = enabled,
//        )
//        preview?.let { it.invoke(this@Column, userPickedFile) }
//    }
//}

@Stable
public sealed interface FileUploadType {
    public data class Image(val camera: Boolean = false) : FileUploadType
    public data object Video : FileUploadType
    public data object ImageAndVideo : FileUploadType
    public data class File(
        val extensions: Set<String>? = null,
    ) : FileUploadType {
        public constructor(vararg extensions: String) : this(extensions.toSet())
        public constructor(extensions: List<String>) : this(extensions.toSet())
        public constructor(extension: String) : this(setOf(extension))
    }

    internal fun toFileKitType(): FileKitType {
        return when (this) {
            is Image -> FileKitType.Image
            is Video -> FileKitType.Video
            is ImageAndVideo -> FileKitType.ImageAndVideo
            is File -> FileKitType.File(extensions)
        }
    }
}

@Composable
private fun FileUploadPreviewList(
    items: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items()
    }
}


