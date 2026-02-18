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
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.progressbar.Progressbar
import com.adevinta.spark.components.progressbar.ProgressbarIndeterminate
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WarningOutline
import com.adevinta.spark.tokens.EmphasizeDim1
import com.adevinta.spark.tokens.ripple
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.mimeType.MimeType
import java.io.File

/**
 * Visual representation of a single uploaded file.
 *
 * Layout:
 * - Icon representing the file type or error state.
 * - Filename as primary text (with middle ellipsis if needed).
 * - File size as secondary text.
 * - Progress indicator (optional, linear or indeterminate) - shown when [UploadedFile.progress] or [UploadedFile.isLoading] is set.
 * - Error message (optional) - shown when [UploadedFile.errorMessage] is set.
 * - Clear button at the end to remove the file.
 *
 * The component automatically displays progress, loading states, and error messages based on the [file]'s properties.
 * Progress and loading states disable the clear button automatically.
 *
 * @param file The uploaded file to display. The file's [UploadedFile.progress], [UploadedFile.isLoading],
 * [UploadedFile.errorMessage], and [UploadedFile.enabled] properties control the component's visual state.
 * @param onClear Callback invoked when the clear button is clicked.
 * @param modifier Modifier to be applied to the component.
 * @param clearContentDescription Content description for the clear button. If null, defaults to "Remove ${file.name}".
 * @param onClick Optional callback invoked when the file preview is clicked. If null, the preview is not clickable.
 * @param clearIcon Icon to use for the clear button. Defaults to [SparkIcons.Close].
 */
@InternalSparkApi
@Composable
public fun PreviewFile(
    file: UploadedFile,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    clearContentDescription: String? = null,
    onClick: (() -> Unit)? = null,
    clearIcon: SparkIcon = SparkIcons.Close,
) {
    val hasError = file.errorMessage != null
    val borderColor by animateColorAsState(
        if (hasError) SparkTheme.colors.error else SparkTheme.colors.outline,
    )
    val borderSize by animateDpAsState(if (hasError) 2.dp else 1.dp)
    val opacity by animateFloatAsState(if (file.enabled) 1f else SparkTheme.colors.dim3)

    val progress = file.progress
    val isLoading = file.isLoading
    val errorMessage = file.errorMessage

    // Disable clear button if progress or loading is active
    val isClearEnabled = file.enabled && progress == null && !isLoading

    Surface(
        modifier = modifier
            .sparkUsageOverlay()
            .fillMaxWidth()
            .graphicsLayer {
                alpha = opacity
            }
            .ifNotNull(onClick) {
                clickable(
                    onClick = it,
                    enabled = file.enabled,
                    role = Role.Button,
                )
            }
            .semantics(mergeDescendants = true) {
                errorMessage?.let { error(it) }
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
                        visible = progress != null || isLoading,
                    ) {
                        if (isLoading) {
                            ProgressbarIndeterminate(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 8.dp),
                            )
                        } else {
                            val animatedProgress by
                                animateFloatAsState(
                                    // backup to 100 otherwise we would crash since the progress
                                    // is still in use for the time we hide it
                                    targetValue = progress?.invoke() ?: 100f,
                                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                                )
                            Progressbar(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 8.dp),
                                progress = { animatedProgress },
                            )
                        }
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
                    clearIcon = clearIcon,
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
    clearIcon: SparkIcon,
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
            sparkIcon = clearIcon,
            contentDescription = null,
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

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(SparkTheme.shapes.small)
            .drawBehind {
                drawRect(containerColor)
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            sparkIcon = icon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
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
        )
    }
}

private data class PreviewFileState(val file: UploadedFile, val enabled: Boolean = true)

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
                progress = { 0.4f },
            ),
        ),
        PreviewFileState(
            file = UploadedFile(
                file = PlatformFile(file = File("video.mp4")),
                errorMessage = "File too large",
            ),
        ),
        PreviewFileState(
            file = UploadedFile(
                file = PlatformFile(file = File("other_file.txt")),
            ),
            enabled = false,
        ),
    )
}
