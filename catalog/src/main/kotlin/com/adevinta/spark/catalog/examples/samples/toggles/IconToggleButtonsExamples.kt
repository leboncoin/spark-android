/*
 * Copyright (c) 2023 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.toggles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonContrast
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonFilled
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonGhost
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonOutlined
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonTinted
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons

private const val IconToggleButtonsExampleDescription = "Icon Toggle Button examples"
private const val IconToggleButtonsExampleSourceUrl = "$SampleSourceUrl/IconToggleButtonSamples.kt"
public val IconToggleButtonsExamples: List<Example> = listOf(
    Example(
        id = "filled",
        name = "Filled Icon Toggle Button",
        description = IconToggleButtonsExampleDescription,
        sourceUrl = IconToggleButtonsExampleSourceUrl,
    ) {
        IconToggleButtonSample(
            button = { _, enabled, icon, contentDescription ->
                IconToggleButtonFilled(
                    checked = true,
                    enabled = enabled,
                    onCheckedChange = {},
                    icons = IconToggleButtonIcons(icon, icon),
                    contentDescription = contentDescription,
                )
            },
        )
    },
    Example(
        id = "tinted",
        name = "Tinted Icon Toggle Button",
        description = IconToggleButtonsExampleDescription,
        sourceUrl = IconToggleButtonsExampleSourceUrl,
    ) {
        IconToggleButtonSample(
            button = { _, enabled, icon, contentDescription ->
                IconToggleButtonTinted(
                    checked = true,
                    enabled = enabled,
                    onCheckedChange = {},
                    icons = IconToggleButtonIcons(icon, icon),
                    contentDescription = contentDescription,
                )
            },
        )
    },
    Example(
        id = "outlined",
        name = "Outlined Icon Toggle Button",
        description = IconToggleButtonsExampleDescription,
        sourceUrl = IconToggleButtonsExampleSourceUrl,
    ) {
        IconToggleButtonSample(
            button = { _, enabled, icon, contentDescription ->
                IconToggleButtonOutlined(
                    checked = true,
                    enabled = enabled,
                    onCheckedChange = {},
                    icons = IconToggleButtonIcons(icon, icon),
                    contentDescription = contentDescription,
                )
            },
        )
    },
    Example(
        id = "ghost",
        name = "Ghost Icon Toggle Button",
        description = IconToggleButtonsExampleDescription,
        sourceUrl = IconToggleButtonsExampleSourceUrl,
    ) {
        IconToggleButtonSample(
            button = { _, enabled, icon, contentDescription ->
                IconToggleButtonGhost(
                    checked = true,
                    enabled = enabled,
                    onCheckedChange = {},
                    icons = IconToggleButtonIcons(icon, icon),
                    contentDescription = contentDescription,
                )
            },
        )
    },
    Example(
        id = "contrast",
        name = "Contrast Icon Toggle Button",
        description = IconToggleButtonsExampleDescription,
        sourceUrl = IconToggleButtonsExampleSourceUrl,
    ) {
        IconToggleButtonSample(
            button = { _, enabled, icon, contentDescription ->
                IconToggleButtonContrast(
                    checked = true,
                    enabled = enabled,
                    onCheckedChange = {},
                    icons = IconToggleButtonIcons(icon, icon),
                    contentDescription = contentDescription,
                )
            },
        )
    },
)

@Composable
private fun IconToggleButtonSample(
    button: @Composable (
        onClick: () -> Unit,
        enabled: Boolean,
        icon: SparkIcon,
        contentDescription: String?,
    ) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val icon = SparkIcons.HeartFill
        val contentDescription = "Localized Content Description"
        button(
            /* onClick = */ { },
            /* enabled = */ true,
            /* icon = */ icon,
            /* contentDescription = */ contentDescription,
        )
        button(
            /* onClick = */ { },
            /* enabled = */ false,
            /* icon = */ icon,
            /* contentDescription = */ contentDescription,
        )
    }
}
