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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.progressbar.Progressbar
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.EmphasizeDim1
import com.adevinta.spark.tokens.ripple

/**
 * Visual representation of a single uploaded file.
 *
 * Layout:
 * - Icon representing the file type.
 * - Filename as primary text.
 * - File size as secondary text.
 * - Clear button at the end to remove the file.
 */
@Composable
public fun PreviewFile(
    file: UploadedFile,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    progress: (() -> Float)? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SparkTheme.colors.surface,
        border = BorderStroke(1.dp, SparkTheme.colors.outline),
        shape = SparkTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PreviewFileIcon(file = file)

            HorizontalSpacer(8.dp)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = spacedBy(8.dp),
            ) {
                PreviewFileTexts(
                    file = file,
                    modifier = Modifier.fillMaxWidth(1f),
                )
                AnimatedVisibility(
                    visible = progress != null,
                ) {
                    Progressbar(
                        modifier = Modifier.fillMaxWidth(1f),
                        progress = progress!!,
                    )
                }
            }

            PreviewFileClearButton(onClear, enabled)
        }
    }
}

@Composable
private fun PreviewFileClearButton(
    onClear: () -> Unit,
    enabled: Boolean,
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
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            sparkIcon = SparkIcons.Close,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun PreviewFileIcon(
    file: UploadedFile,
) {
    Surface(
        color = SparkTheme.colors.neutralContainer,
        shape = SparkTheme.shapes.small,
        modifier = Modifier.size(36.dp),
    ) {
        Icon(
            sparkIcon = FileUploadDefaults.iconFor(file),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(6.dp),
        )
    }
}

@Composable
private fun PreviewFileTexts(
    file: UploadedFile,
    modifier: Modifier = Modifier,
) {
    val sizeLabel = file.sizeBytes?.let {
        formatFileSize(LocalContext.current, it, 2)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(8.dp),
    ) {
        Text(
            text = file.name,
            style = SparkTheme.typography.caption,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
            modifier = Modifier.weight(1f),
        )

        if (sizeLabel != null) {
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
}

@Preview
@Composable
private fun PreviewPreviewFile() {
    PreviewTheme {
        PreviewFile(
            file = UploadedFile(
                path = "path",
                name = "Namefile.doc",
                sizeBytes = 10243241,
                mimeType = "text/plain",
            ),
            enabled = true,
            onClear = {},
        )
        PreviewFile(
            file = UploadedFile(
                path = "path",
                name = "Namefile.doc",
                sizeBytes = 10243241,
                mimeType = "text/plain",
            ),
            enabled = false,
            onClear = {},
        )
        PreviewFile(
            file = UploadedFile(
                path = "path",
                name = "Namefile.doc",
                sizeBytes = 10243241,
                mimeType = "text/plain",
            ),
            enabled = true,
            progress = {0.5f},
            onClear = {},
        )
    }
}

