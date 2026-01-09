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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.progressbar.Progressbar
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WarningOutline
import com.adevinta.spark.tokens.EmphasizeDim1
import com.adevinta.spark.tokens.ripple
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.mimeType.MimeType
import java.io.File

/**
 * Visual representation of a single uploaded file.
 *
 * Layout:
 * - Icon representing the file type.
 * - Filename as primary text.
 * - File size as secondary text.
 * - Clear button at the end to remove the file.
 *
 * @param file The uploaded file to display
 * @param onClear Callback invoked when the clear button is clicked
 * @param modifier Modifier to be applied to the component
 * @param progress Optional progress indicator (0.0 to 1.0)
 * @param errorMessage Optional error message to display
 * @param enabled Whether the clear button is enabled
 * @param clearContentDescription Content description for the clear button. If null, defaults to "Remove ${file.name}"
 */
@Composable
public fun PreviewFile(
    file: UploadedFile,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    progress: (() -> Float)? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    clearContentDescription: String? = null,
) {
    val hasError = errorMessage != null
    val borderColor by animateColorAsState(
        if (hasError) SparkTheme.colors.error else SparkTheme.colors.outline,
    )
    val borderSize by animateDpAsState(if (hasError) 2.dp else 1.dp)
    val opacity by animateFloatAsState(if (enabled) 1f else SparkTheme.colors.dim3)

    // Disable clear button if progress is active (during loading)
    val isClearEnabled = enabled && progress == null

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = opacity
            },
        color = SparkTheme.colors.surface,
        border = BorderStroke(borderSize, borderColor),
        shape = SparkTheme.shapes.small,
    ) {
        ProvideTextStyle(SparkTheme.typography.caption) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PreviewFileIcon(file.mimeType, file.name, error = hasError)

                HorizontalSpacer(8.dp)

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    PreviewFileTexts(
                        file = file,
                        modifier = Modifier.fillMaxWidth(1f),
                    )

                    // Show progress bar during upload
                    AnimatedVisibility(
                        visible = progress != null,
                    ) {
                        Progressbar(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 8.dp),
                            progress = progress
                                ?: { 100f }, // backup to 100 otherwise we would crash since the progress
                            // is still in use for the time we hide it
                        )
                    }

                    // Show error message if present
                    AnimatedVisibility(visible = hasError) {
                        Text(
                            text = errorMessage ?: "",
                            color = SparkTheme.colors.error,
                        )
                    }
                }

                PreviewFileClearButton(
                    onClear = onClear,
                    enabled = isClearEnabled,
                    contentDescription = clearContentDescription ?: "Remove ${file.name}",
                )
            }
        }
    }
}

@Composable
private fun PreviewFileClearButton(
    onClear: () -> Unit,
    enabled: Boolean,
    contentDescription: String,
) {
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .size(36.dp)
            .clip(SparkTheme.shapes.full)
            .clickable(
                onClick = onClear,
                enabled = enabled,
                role = Role.Button,
                interactionSource = null,
                indication = ripple(
                    bounded = false,
                    radius = 40.dp / 2,
                ),
            )
            .semantics {
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            sparkIcon = SparkIcons.Close,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun PreviewFileIcon(
    mimeType: MimeType?,
    name: String,
    error: Boolean = false,
) {
    val icon = if (error) {
        SparkIcons.WarningOutline
    } else {
        FileUploadDefaults.iconFor(mimeType, name)
    }
    val containerColor by animateColorAsState(
        if (error) SparkTheme.colors.errorContainer else SparkTheme.colors.neutralContainer,
    )
    val contentColor by animateColorAsState(
        if (error) SparkTheme.colors.error else SparkTheme.colors.onSurface,
    )

    Surface(
        color = containerColor,
        shape = SparkTheme.shapes.small,
        modifier = Modifier.size(36.dp),
    ) {
        Icon(
            sparkIcon = icon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(6.dp),
            tint = contentColor,
        )
    }
}

@Composable
private fun PreviewFileTexts(
    file: UploadedFile,
    modifier: Modifier = Modifier,
) {
    val sizeLabel = formatFileSize(LocalContext.current, file.sizeBytes, 2)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(8.dp),
    ) {
        Text(
            text = file.name,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
            modifier = Modifier.weight(1f),
        )

        EmphasizeDim1 {
            Text(
                text = sizeLabel,
                style = SparkTheme.typography.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPreviewFile(
    @PreviewParameter(PreviewFileParameterProvider::class) state: PreviewFileState,
) {
    PreviewTheme {
        PreviewFile(
            file = state.file,
            onClear = {},
            progress = state.progress,
            errorMessage = state.errorMessage,
            enabled = state.enabled,
        )
    }
}

private data class PreviewFileState(
    val file: UploadedFile,
    val progress: (() -> Float)? = null,
    val errorMessage: String? = null,
    val enabled: Boolean = true,
)

private class PreviewFileParameterProvider : PreviewParameterProvider<PreviewFileState> {
    override val values: Sequence<PreviewFileState> = sequenceOf(
        PreviewFileState(
            file = UploadedFile(
                file = PlatformFile(file = File("image.png")),
            ),
        ),
        PreviewFileState(
            file = UploadedFile(
                file = PlatformFile(file = File("document.pdf")),
            ),
            progress = { 0.4f },
        ),
        PreviewFileState(
            file = UploadedFile(
                file = PlatformFile(file = File("video.mp4")),
            ),
            errorMessage = "File too large",
        ),
        PreviewFileState(
            file = UploadedFile(
                file = PlatformFile(file = File("other_file.txt")),
            ),
            enabled = false,
        ),
    )
}
