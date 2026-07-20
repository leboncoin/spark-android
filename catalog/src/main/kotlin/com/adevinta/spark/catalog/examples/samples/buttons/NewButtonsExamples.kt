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
import com.adevinta.spark.components.buttons.Ai
import com.adevinta.spark.components.buttons.Boost
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.Contrast
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.buttons.Primary
import com.adevinta.spark.components.buttons.Secondary
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.components.buttons.Tertiary
import com.adevinta.spark.components.buttons.Text
import com.adevinta.spark.components.buttons.Underlined
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val NewButtonsExampleDescription = "New Button examples"
private const val NewButtonsExampleSourceUrl = "$SampleSourceUrl/ButtonSamples.kt"

public val NewButtonsExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "primary",
        name = "Primary Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Primary(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "secondary",
        name = "Secondary Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Secondary(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "tertiary",
        name = "Tertiary Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Tertiary(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "boost",
        name = "Boost Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Boost(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "ai",
        name = "AI Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, _, _, isLoading ->
            Button.Ai(
                onClick = onClick,
                text = text,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "danger",
        name = "Danger Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Danger(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "success",
        name = "Success Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Success(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "contrast-new",
        name = "Contrast Button (new)",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Contrast(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "text",
        name = "Text Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Text(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
    Example(
        id = "underlined",
        name = "Underlined Button",
        description = NewButtonsExampleDescription,
        sourceUrl = NewButtonsExampleSourceUrl,
    ) {
        NewButtonSample { onClick, text, enabled, icon, iconSide, isLoading ->
            Button.Underlined(
                onClick = onClick,
                text = text,
                enabled = enabled,
                icon = icon,
                iconSide = iconSide,
                isLoading = isLoading,
            )
        }
    },
)

@Composable
private fun NewButtonSample(
    button: @Composable (
        onClick: () -> Unit,
        text: String,
        enabled: Boolean,
        icon: SparkIcon?,
        iconSide: IconSide,
        isLoading: Boolean,
    ) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val icon = LeboncoinIcons.HeartFill
        var isLoading by remember { mutableStateOf(false) }
        val buttonText = "Button"

        button(
            /* onClick = */ { isLoading = !isLoading },
            /* text = */ buttonText,
            /* enabled = */ true,
            /* icon = */ null,
            /* iconSide = */ IconSide.START,
            /* isLoading = */ isLoading,
        )
        button(
            /* onClick = */ { isLoading = !isLoading },
            /* text = */ buttonText,
            /* enabled = */ true,
            /* icon = */ icon,
            /* iconSide = */ IconSide.START,
            /* isLoading = */ isLoading,
        )
        button(
            /* onClick = */ { isLoading = !isLoading },
            /* text = */ buttonText,
            /* enabled = */ true,
            /* icon = */ icon,
            /* iconSide = */ IconSide.END,
            /* isLoading = */ isLoading,
        )
        button(
            /* onClick = */ { isLoading = !isLoading },
            /* text = */ buttonText,
            /* enabled = */ false,
            /* icon = */ icon,
            /* iconSide = */ IconSide.START,
            /* isLoading = */ isLoading,
        )
    }
}
