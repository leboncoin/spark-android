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
package com.adevinta.spark.catalog.examples.samples.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.iconbuttons.Ai
import com.adevinta.spark.components.iconbuttons.Boost
import com.adevinta.spark.components.iconbuttons.Contrast
import com.adevinta.spark.components.iconbuttons.Danger
import com.adevinta.spark.components.iconbuttons.Ghost
import com.adevinta.spark.components.iconbuttons.IconButton
import com.adevinta.spark.components.iconbuttons.Primary
import com.adevinta.spark.components.iconbuttons.Secondary
import com.adevinta.spark.components.iconbuttons.Success
import com.adevinta.spark.components.iconbuttons.Tertiary
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val RebrandedIconButtonsExampleDescription = "Rebranded Icon Button examples"
private const val RebrandedIconButtonsExampleSourceUrl = "$SampleSourceUrl/IconButtonSamples.kt"

public val RebrandedIconButtonsExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "primary",
        name = "Primary Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Primary(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "secondary",
        name = "Secondary Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Secondary(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "tertiary",
        name = "Tertiary Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Tertiary(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "boost",
        name = "Boost Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Boost(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "ai",
        name = "AI Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Ai(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "danger",
        name = "Danger Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Danger(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "success",
        name = "Success Icon Button",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Success(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "contrast-rebranded",
        name = "Contrast Icon Button (rebranded)",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Contrast(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "ghost-rebranded",
        name = "Ghost Icon Button (rebranded)",
        description = RebrandedIconButtonsExampleDescription,
        sourceUrl = RebrandedIconButtonsExampleSourceUrl,
    ) {
        RebrandedIconButtonSample { onClick, enabled, icon, contentDescription, isLoading ->
            IconButton.Ghost(
                icon = icon,
                onClick = onClick,
                contentDescription = contentDescription,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
)

@Composable
private fun RebrandedIconButtonSample(
    button: @Composable (
        onClick: () -> Unit,
        enabled: Boolean,
        icon: SparkIcon,
        contentDescription: String,
        isLoading: Boolean,
    ) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val icon = LeboncoinIcons.HeartFill
        val contentDescription = "Localized Content Description"
        var isLoading by remember { mutableStateOf(false) }
        button(
            /* onClick = */ { isLoading = !isLoading },
            /* enabled = */ true,
            /* icon = */ icon,
            /* contentDescription = */ contentDescription,
            /* isLoading = */ isLoading,
        )
        button(
            /* onClick = */ { isLoading = !isLoading },
            /* enabled = */ false,
            /* icon = */ icon,
            /* contentDescription = */ contentDescription,
            /* isLoading = */ isLoading,
        )
    }
}
