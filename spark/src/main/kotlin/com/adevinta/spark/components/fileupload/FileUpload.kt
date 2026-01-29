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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.icons.CameraVideo
import com.adevinta.spark.icons.CvOutline
import com.adevinta.spark.icons.FilePdfOutline
import com.adevinta.spark.icons.ImageOutline
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.ifNotNull
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import io.github.vinceglb.filekit.mimeType
import io.github.vinceglb.filekit.mimeType.MimeType
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.size
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

public object FileUpload {

    /**
     * High level file upload component for multiple file selection.
     *
     * This component provides a button to trigger multiple file selection. To display selected files,
     * manage state yourself and use [PreviewFile] or [FileUploadList].
     *
     * @param onResult Callback invoked when files are selected
     * @param label Text label for the default button
     * @param modifier Modifier to be applied to the button
     * @param size The size of the button. Only applies when using the default [buttonContent].
     * @param icon The optional icon to be displayed at the start or the end of the button container. Only applies when using the default [buttonContent].
     * @param iconSide If an icon is added, you can configure the side where is should be displayed, at the start
     * or end of the button. Only applies when using the default [buttonContent].
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
    @ExperimentalSparkApi
    @Composable
    public fun Button(
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
        FileUploadButton(
            onResult = onResult,
            label = label,
            modifier = modifier,
            size = size,
            icon = icon,
            iconSide = iconSide,
            type = type,
            maxFiles = maxFiles,
            title = title,
            directory = directory,
            dialogSettings = dialogSettings,
            enabled = enabled,
            onClickLabel = onClickLabel,
            buttonContent = buttonContent,
        )
    }

    /**
     * High level file upload component for single file selection.
     *
     * This component provides a button to trigger file selection. To display selected files,
     * manage state yourself and use [PreviewFile] or [FileUploadList].
     *
     * @param onResult Callback invoked when a file is selected or cleared (null)
     * @param label Text label for the default button
     * @param modifier Modifier to be applied to the button
     * @param size The size of the button. Only applies when using the default [buttonContent].
     * @param icon The optional icon to be displayed at the start or the end of the button container. Only applies when using the default [buttonContent].
     * @param iconSide If an icon is added, you can configure the side where is should be displayed, at the start
     * or end of the button. Only applies when using the default [buttonContent].
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
    @ExperimentalSparkApi
    @Composable
    public fun ButtonSingleSelect(
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
        FileUploadSingleButton(
            onResult = onResult,
            label = label,
            modifier = modifier,
            size = size,
            icon = icon,
            iconSide = iconSide,
            type = type,
            title = title,
            directory = directory,
            dialogSettings = dialogSettings,
            enabled = enabled,
            onClickLabel = onClickLabel,
            buttonContent = buttonContent,
        )
    }
}

/**
 * Represents a file that has been selected or uploaded within the Spark file upload components.
 *
 * This class wraps the underlying [PlatformFile] and provides additional state such as
 * upload progress, error messages, and UI interaction states to represent the uploaded state of this
 * specific selected file.
 *
 * @property file The underlying platform-specific file reference. System file information are contained here.
 * @property enabled Whether interaction with this file in the UI (e.g., removal) is enabled.
 * @property progress A lambda returning the upload progress as a float between 0.0 and 1.0.
 * If null, no progress indicator is shown.
 * @property isLoading Whether the upload is in indeterminate loading state.
 * @property errorMessage An optional error message to display if the upload failed.
 * @property name The display name of the file (e.g., "document.pdf").
 * @property sizeBytes The size of the file in bytes.
 * @property mimeType The detected [MimeType] of the file, if available.
 */
@Immutable
@Serializable
public data class UploadedFile(
    val file: PlatformFile,
    val enabled: Boolean = true,
    val progress: (() -> Float)? = null,
    val isLoading: Boolean = false,
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
 * public API of file upload components small and consistent with other Spark components.
 */
public object FileUploadDefaults {

    /**
     * Maps a [UploadedFile] to an icon representing its type.
     *
     * This is a best-effort mapping based on the mimeType or filename extension and is
     * intentionally simple. Consumers can override the icon used in the preview item if
     * they need more control.
     */
    public fun iconFor(
        mimeType: MimeType?,
        name: String,
    ): SparkIcon {
        val mimeType = mimeType?.primaryType.orEmpty()
        val lowerName = name.lowercase()

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

            mimeType.startsWith("video/") ||
                lowerName.endsWith(".mp4") ||
                lowerName.endsWith(".mov") ||
                lowerName.endsWith(".avi") ||
                lowerName.endsWith(".mkv") -> {
                SparkIcons.CameraVideo
            }

            else -> SparkIcons.CvOutline
        }
    }

    /**
     * Opens a file with the system's default application for that file type.
     *
     * This function uses FileKit's `openFileWithDefaultApplication` to open the file.
     * For example, a PDF file will open in the default PDF viewer, an image will open
     * in the default image viewer, etc.
     *
     * Note that Android, if you're opening files from custom locations (e.g., `FileKit.filesDir` or
     * `FileKit.cacheDir`), you may need to configure FileProvider in your app's
     * AndroidManifest.xml. See [FileKit documentation](https://filekit.mintlify.app/dialogs/open-file)
     * for details.
     *
     * @param file The uploaded file to open
     */
    public fun openFile(file: UploadedFile) {
        FileKit.openFileWithDefaultApplication(file.file)
    }
}

/**
 * Source selection for image/video picker.
 */
@Stable
public enum class ImageSource {
    /** Only camera picker */
    Camera,

    /** Only (system) gallery picker */
    Gallery,
}

/**
 * Type of files that can be selected for upload.
 */
@Stable
public sealed interface FileUploadType {
    /**
     * Interface for file types that can be sourced from different locations (e.g., Camera or Gallery).
     *
     * This is used by types like [FileUploadType.Image] and [FileUploadType.ImageAndVideo] to
     * specify where the media should be captured or selected from.
     *
     * @property source The origin from which the media should be retrieved.
     */
    public sealed interface HasMultipleSource : FileUploadType {
        public val source: ImageSource
    }

    /**
     * Image files only.
     *
     * @param source How to select images: camera, gallery
     */
    public data class Image(override val source: ImageSource = ImageSource.Gallery) : HasMultipleSource

    /**
     * Video files only
     */
    public data object Video : FileUploadType

    /**
     * Both image and video files
     *
     * @param source How to select images: camera, gallery
     */
    public data class ImageAndVideo(override val source: ImageSource = ImageSource.Gallery) : HasMultipleSource

    /**
     * Generic file selection with optional extension filter.
     *
     * @param extensions Optional set of file extensions to filter (e.g., setOf("pdf", "doc")), you can get a sample
     * of available extensions in [FileExtensionStandard].
     *
     * @see FileExtensionStandard
     */
    public data class File(val extensions: Set<String>? = null) : FileUploadType {
        public constructor(vararg extensions: String) : this(extensions.toSet())
        public constructor(extensions: List<String>) : this(extensions.toSet())
        public constructor(extension: String) : this(setOf(extension))
    }
}

/**
 * Mode for file upload: single file or multiple files.
 */
@Stable
public sealed interface FileUploadMode {
    /** Single file selection mode */
    public data object Single : FileUploadMode

    /**
     * Multiple file selection mode.
     *
     * @param maxFiles Maximum number of files that can be selected. If null, max to 50.
     */
    public data class Multiple(val maxFiles: Int? = null) : FileUploadMode
}

internal fun FileUploadType.toFileKitType(): FileKitType = when (this) {
    is FileUploadType.Image -> FileKitType.Image
    is FileUploadType.Video -> FileKitType.Video
    is FileUploadType.ImageAndVideo -> FileKitType.ImageAndVideo
    is FileUploadType.File -> FileKitType.File(extensions)
}
