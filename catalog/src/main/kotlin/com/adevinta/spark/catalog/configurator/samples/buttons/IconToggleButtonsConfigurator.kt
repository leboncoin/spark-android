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
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonContrast
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonFilled
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonGhost
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonOutlined
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonTinted
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.CarFill
import com.adevinta.spark.icons.CarOutline
import com.adevinta.spark.icons.SparkIcons

public val IconToggleButtonsConfigurator: Configurator = Configurator(
    id = "icon-toggle-button",
    name = "IconToggleButton",
    description = "IconToggleButton configuration",
    sourceUrl = "$SampleSourceUrl/IconToggleButtonSamples.kt",
) {
    IconToggleButtonSample()
}

@Composable
private fun ColumnScope.IconToggleButtonSample() {
    var style by remember { mutableStateOf(IconToggleButtonStyle.Filled) }
    var isEnabled by remember { mutableStateOf(true) }
    var isChecked by remember { mutableStateOf(true) }
    var shape by remember { mutableStateOf(ButtonShape.Rounded) }
    var size by remember { mutableStateOf(IconButtonSize.Medium) }
    var intent by remember { mutableStateOf(IconButtonIntent.Main) }
    val icons by remember { mutableStateOf(IconToggleButtonIcons(SparkIcons.CarOutline, SparkIcons.CarFill)) }
    var contentDescription by remember { mutableStateOf("Content Description") }

    ConfiguredIconToggleButton(
        style = style,
        shape = shape,
        contentDescription = contentDescription,
        onCheckedChange = { isChecked = !isChecked },
        size = size,
        intent = intent,
        isChecked = isChecked,
        isEnabled = isEnabled,
        icons = icons,
    )

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
        onOptionSelect = {
            intent = it
        },
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
private fun IconToggleButtonSamplePreview() {
    PreviewTheme { IconToggleButtonSample() }
}

@Composable
private fun ConfiguredIconToggleButton(
    modifier: Modifier = Modifier,
    style: IconToggleButtonStyle,
    shape: ButtonShape,
    contentDescription: String?,
    onCheckedChange: (Boolean) -> Unit = {},
    size: IconButtonSize,
    intent: IconButtonIntent,
    isChecked: Boolean,
    isEnabled: Boolean,
    icons: IconToggleButtonIcons,
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
                IconToggleButtonStyle.Filled -> IconToggleButtonFilled(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onCheckedChange = onCheckedChange,
                    size = size,
                    shape = shape,
                    intent = intent,
                    checked = isChecked,
                    enabled = isEnabled,
                    icons = icons,
                )

                IconToggleButtonStyle.Outlined -> IconToggleButtonOutlined(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onCheckedChange = onCheckedChange,
                    size = size,
                    shape = shape,
                    intent = intent,
                    checked = isChecked,
                    enabled = isEnabled,
                    icons = icons,
                )

                IconToggleButtonStyle.Tinted -> IconToggleButtonTinted(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onCheckedChange = onCheckedChange,
                    size = size,
                    shape = shape,
                    intent = intent,
                    checked = isChecked,
                    enabled = isEnabled,
                    icons = icons,
                )

                IconToggleButtonStyle.Ghost -> IconToggleButtonGhost(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onCheckedChange = onCheckedChange,
                    size = size,
                    shape = shape,
                    intent = intent,
                    checked = isChecked,
                    enabled = isEnabled,
                    icons = icons,
                )

                IconToggleButtonStyle.Contrast -> IconToggleButtonContrast(
                    modifier = modifier,
                    contentDescription = contentDescription,
                    onCheckedChange = onCheckedChange,
                    size = size,
                    shape = shape,
                    intent = intent,
                    checked = isChecked,
                    enabled = isEnabled,
                    icons = icons,
                )
            }
        }
    }
}

private enum class IconToggleButtonStyle {
    Filled,
    Outlined,
    Tinted,
    Contrast,
    Ghost,
}
