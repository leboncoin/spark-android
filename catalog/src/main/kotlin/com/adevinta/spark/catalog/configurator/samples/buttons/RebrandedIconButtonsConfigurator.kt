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
import com.adevinta.spark.SparkFeatureFlagProvider
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.icons.IconPickerItem
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.iconbuttons.Ai
import com.adevinta.spark.components.iconbuttons.Boost
import com.adevinta.spark.components.iconbuttons.Contrast
import com.adevinta.spark.components.iconbuttons.Danger
import com.adevinta.spark.components.iconbuttons.Ghost
import com.adevinta.spark.components.iconbuttons.IconButton
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.Primary
import com.adevinta.spark.components.iconbuttons.Secondary
import com.adevinta.spark.components.iconbuttons.Success
import com.adevinta.spark.components.iconbuttons.Tertiary
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public val RebrandedIconButtonsConfigurator: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "rebranded-icon-button",
        name = "Rebranded IconButton",
        description = "Rebranded IconButton API configuration",
        sourceUrl = "$SampleSourceUrl/IconButtonSamples.kt",
    ) { _, _ ->
        RebrandedIconButtonSample()
    },
)

@Composable
private fun ColumnScope.RebrandedIconButtonSample() {
    var variant by remember { mutableStateOf(RebrandedIconButtonVariant.Primary) }
    var icon: SparkIcon? by remember { mutableStateOf(LeboncoinIcons.HeartFill) }
    var isLoading by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    var size by remember { mutableStateOf(IconButtonSize.Medium) }
    var contentDescription by remember { mutableStateOf("Content Description") }

    SparkFeatureFlagProvider(useRebrandedButtons = true) {
        ConfiguredRebrandedIconButton(
            variant = variant,
            icon = icon ?: LeboncoinIcons.HeartFill,
            contentDescription = contentDescription,
            onClick = { isLoading = !isLoading },
            size = size,
            isEnabled = isEnabled,
            isLoading = isLoading,
        )
    }

    IconPickerItem(
        label = "Icon",
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

    DropdownEnum(
        title = "Variant",
        selectedOption = variant,
        onOptionSelect = { variant = it },
    )

    ButtonGroup(
        title = "Size",
        selectedOption = size,
        onOptionSelect = { size = it },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = contentDescription,
        onValueChange = { contentDescription = it },
        label = "Content Description",
        placeholder = "Content Description",
    )
}

@Preview
@Composable
private fun RebrandedIconButtonSamplePreview() {
    PreviewTheme { RebrandedIconButtonSample() }
}

@Composable
private fun ConfiguredRebrandedIconButton(
    variant: RebrandedIconButtonVariant,
    icon: SparkIcon,
    contentDescription: String,
    onClick: () -> Unit,
    size: IconButtonSize,
    isEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    // Contrast uses a surface container, so it needs a non-surface background to stand out.
    val containerColor by animateColorAsState(
        targetValue = if (variant == RebrandedIconButtonVariant.Contrast) {
            SparkTheme.colors.backgroundVariant
        } else {
            Color.Transparent
        },
        label = "Icon button background color",
    )
    Surface(color = containerColor) {
        Box(modifier = Modifier.padding(4.dp)) {
            when (variant) {
                RebrandedIconButtonVariant.Primary -> IconButton.Primary(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Secondary -> IconButton.Secondary(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Tertiary -> IconButton.Tertiary(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Boost -> IconButton.Boost(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Ai -> IconButton.Ai(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Danger -> IconButton.Danger(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Success -> IconButton.Success(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Contrast -> IconButton.Contrast(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )

                RebrandedIconButtonVariant.Ghost -> IconButton.Ghost(
                    icon = icon,
                    onClick = onClick,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    size = size,
                    enabled = isEnabled,
                    isLoading = isLoading,
                )
            }
        }
    }
}

private enum class RebrandedIconButtonVariant {
    Primary,
    Secondary,
    Tertiary,
    Boost,
    Ai,
    Danger,
    Success,
    Contrast,
    Ghost,
}
