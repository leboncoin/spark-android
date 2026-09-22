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
package com.adevinta.spark.catalog.configurator.samples.buttons

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.icons.IconPickerItem
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.Ai
import com.adevinta.spark.components.buttons.Boost
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.Contrast
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.buttons.Primary
import com.adevinta.spark.components.buttons.Secondary
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.components.buttons.Tertiary
import com.adevinta.spark.components.buttons.Text
import com.adevinta.spark.components.buttons.Underlined
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.SparkIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public val NewButtonsConfigurator: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "new-button",
        name = "New Button",
        description = "New Button API configuration",
        sourceUrl = "$SampleSourceUrl/ButtonSamples.kt",
    ) { _, _ ->
        NewButtonSample()
    },
)

@Composable
private fun ColumnScope.NewButtonSample() {
    var icon: SparkIcon? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var iconSide by remember { mutableStateOf(IconSide.START) }
    var style by remember { mutableStateOf(NewButtonStyle.Primary) }
    var size by remember { mutableStateOf(ButtonSize.Medium) }
    var buttonText by remember { mutableStateOf("Button") }

    ConfiguredNewButton(
        modifier = Modifier.fillMaxWidth(),
        style = style,
        buttonText = buttonText,
        onClick = { isLoading = !isLoading },
        isLoading = isLoading,
        size = size,
        isEnabled = isEnabled,
        icon = icon,
        iconSide = iconSide,
    )

    IconPickerItem(
        label = "With Icon",
        selectedIcon = icon,
        onIconSelected = { icon = it },
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
        title = "Icon side",
        selectedOption = iconSide,
        onOptionSelect = { iconSide = it },
    )
    DropdownEnum(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
    )
    ButtonGroup(
        title = "Button size",
        selectedOption = size,
        onOptionSelect = { size = it },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = buttonText,
        onValueChange = { buttonText = it },
        label = "Button text",
        placeholder = "Button",
    )
}

@Preview
@Composable
private fun NewButtonSamplePreview() {
    PreviewTheme { NewButtonSample() }
}

@Composable
private fun ConfiguredNewButton(
    modifier: Modifier = Modifier,
    style: NewButtonStyle,
    buttonText: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    size: ButtonSize,
    isEnabled: Boolean,
    icon: SparkIcon?,
    iconSide: IconSide,
) {
    when (style) {
        NewButtonStyle.Primary -> Button.Primary(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Secondary -> Button.Secondary(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Tertiary -> Button.Tertiary(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Boost -> Button.Boost(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Ai -> Button.Ai(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
        )

        NewButtonStyle.Danger -> Button.Danger(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Success -> Button.Success(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Contrast -> Button.Contrast(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Text -> Button.Text(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )

        NewButtonStyle.Underlined -> Button.Underlined(
            modifier = modifier,
            text = buttonText,
            onClick = onClick,
            isLoading = isLoading,
            size = size,
            enabled = isEnabled,
            icon = icon,
            iconSide = iconSide,
        )
    }
}

private enum class NewButtonStyle {
    Primary,
    Secondary,
    Tertiary,
    Boost,
    Ai,
    Danger,
    Success,
    Contrast,
    Text,
    Underlined,
}
