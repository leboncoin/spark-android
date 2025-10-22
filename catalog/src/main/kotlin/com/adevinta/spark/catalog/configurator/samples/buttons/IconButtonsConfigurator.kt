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
package com.adevinta.spark.catalog.configurator.samples.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonShape
import com.adevinta.spark.components.iconbuttons.IconButtonContrast
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonOutlined
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.IconButtonTinted
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons

public val IconButtonsConfigurator: Configurator = Configurator(
    id = "icon-button",
    name = "IconButton",
    description = "IconButton configuration",
    sourceUrl = "$SampleSourceUrl/IconButtonSamples.kt",
) {
    IconButtonSample()
}

@Composable
private fun ColumnScope.IconButtonSample() {
    var style by remember { mutableStateOf(IconButtonStyle.Filled) }
    val icon: SparkIcon by remember { mutableStateOf(SparkIcons.HeartFill) }
    var isLoading by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var shape by remember { mutableStateOf(ButtonShape.Pill) }
    var size by remember { mutableStateOf(IconButtonSize.Medium) }
    var intent by remember { mutableStateOf(IconButtonIntent.Main) }
    var contentDescription by remember { mutableStateOf("Content Description") }

    ConfiguredIconButton(
        style = style,
        shape = shape,
        contentDescription = contentDescription,
        onClick = { },
        size = size,
        intent = intent,
        isEnabled = isEnabled,
        isLoading = isLoading,
        icon = icon,
    )

    SwitchLabelled(
        checked = isLoading,
        onCheckedChange = { isLoading = it },
    ) {
        Text(
            text = "Loading",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    ButtonGroup(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
    )

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Intent",
        selectedOption = intent,
        onOptionSelect = { intent = it },
    )

    ButtonGroup(
        title = "Shape",
        selectedOption = shape,
        onOptionSelect = { shape = it },
    )
    ButtonGroup(
        title = "Size",
        selectedOption = size,
        onOptionSelect = { size = it },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = contentDescription,
        onValueChange = {
            contentDescription = it
        },
        label = "Content Description",
        placeholder = "Content Description",
    )
}

@Preview
@Composable
private fun IconButtonSamplePreview() {
    PreviewTheme { IconButtonSample() }
}

@Composable
private fun ConfiguredIconButton(
    modifier: Modifier = Modifier,
    style: IconButtonStyle,
    shape: ButtonShape,
    contentDescription: String?,
    onClick: () -> Unit = {},
    size: IconButtonSize,
    intent: IconButtonIntent,
    isEnabled: Boolean,
    isLoading: Boolean,
    icon: SparkIcon,
) {
    val containerColor by animateColorAsState(
        targetValue = if (intent != IconButtonIntent.Surface) {
            Color.Transparent
        } else {
            SparkTheme.colors.surfaceInverse
        },
        label = "Button container color",
    )
    Surface(
        color = containerColor,
    ) {
        Box(
            modifier = Modifier.padding(4.dp),
        ) {
            when (style) {
                IconButtonStyle.Filled -> IconButtonFilled(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onClick = onClick,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = isEnabled,
                    isLoading = isLoading,
                    icon = icon,
                )

                IconButtonStyle.Outlined -> IconButtonOutlined(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onClick = onClick,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = isEnabled,
                    isLoading = isLoading,
                    icon = icon,
                )

                IconButtonStyle.Tinted -> IconButtonTinted(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onClick = onClick,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = isEnabled,
                    isLoading = isLoading,
                    icon = icon,
                )

                IconButtonStyle.Ghost -> IconButtonGhost(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onClick = onClick,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = isEnabled,
                    isLoading = isLoading,
                    icon = icon,
                )

                IconButtonStyle.Contrast -> IconButtonContrast(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onClick = onClick,
                    size = size,
                    shape = shape,
                    intent = intent,
                    enabled = isEnabled,
                    isLoading = isLoading,
                    icon = icon,
                )
            }
        }
    }
}

private enum class IconButtonStyle {
    Filled,
    Outlined,
    Tinted,
    Contrast,
    Ghost,
}
