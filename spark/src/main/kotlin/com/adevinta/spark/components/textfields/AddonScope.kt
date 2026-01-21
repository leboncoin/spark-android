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
package com.adevinta.spark.components.textfields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.SparkButtonDefaults
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.icons.IconIntent
import com.adevinta.spark.components.icons.IconToggleButton
import com.adevinta.spark.components.menu.DropdownMenu
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.menu.DropdownMenuItemColumnScope
import com.adevinta.spark.components.spacer.Spacer
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.EyeFill
import com.adevinta.spark.icons.EyeOffFill
import com.adevinta.spark.icons.QuestionOutline
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.SparkColors
import kotlin.random.Random
import androidx.compose.material3.FilledTonalButton as MaterialButton

/**
 * Scope that provide pre-made addons for leading and trailing contents of [TextField].
 */
@Stable
public abstract class AddonScope {

    /**
     * A Button that can be used as trailing content for a [TextField]. The button is a custom one as a regular
     * one doesn't fit so it has a more limited api than a Spark button.
     */
    @ExperimentalSparkApi
    @Composable
    public fun Button(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        icon: SparkIcon? = null,
        isLoading: Boolean = false,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    ) {
        MaterialButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            elevation = ButtonDefaults.buttonElevation(),
            shape = SparkTheme.shapes.full,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = SparkTheme.colors.mainContainer,
            ),
            interactionSource = interactionSource,
        ) {
            Text(
                text = text,
                style = SparkTheme.typography.callout,
            )
            if (icon != null) {
                Spacer(Modifier.width(SparkButtonDefaults.IconSpacing))
                Icon(
                    sparkIcon = icon,
                    modifier = Modifier.size(SparkButtonDefaults.IconSize),
                    contentDescription = null, // button text should be enough for context
                )
            }
            AnimatedVisibility(visible = isLoading) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(SparkButtonDefaults.IconSize)) {
                        CircularProgressIndicator(
                            color = LocalContentColor.current,
                            strokeWidth = 2.dp,
                        )
                    }
                }
            }
        }
    }

    /**
     * An icon that can be used as leading or trailing content for a [TextField].
     * Its size is fixed to 16dp to not change the height of the TextField.
     * Its color is fixed to neutral.
     */
    @ExperimentalSparkApi
    @Composable
    public fun TextFieldIcon(
        icon: SparkIcon,
        contentDescription: String?,
        modifier: Modifier = Modifier,
    ) {
        Icon(
            modifier = modifier.size(16.dp),
            sparkIcon = icon,
            contentDescription = contentDescription,
            tint = IconIntent.Neutral.color(),
        )
    }

    /**
     * An icon ghost button that can be used as leading or trailing content for a [TextField].
     * It's color is fixed to neutral.
     */
    @ExperimentalSparkApi
    @Composable
    public fun TextFieldIconButton(
        onClick: () -> Unit,
        icon: SparkIcon,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier.requiredWidth(40.dp),
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
        ) {
            Icon(
                sparkIcon = icon,
                contentDescription = contentDescription,
                tint = IconIntent.Neutral.color(),
            )
        }
    }

    /**
     * An icon ghost toggle button that can be used as leading or trailing content for a [TextField].
     * It's color is fixed to neutral.
     */
    @Suppress("DEPRECATION") // we don't want to use the spark button for this yet
    @ExperimentalSparkApi
    @Composable
    public fun TextFieldIconToggleButton(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        unCheckedIcon: SparkIcon,
        checkedIcon: SparkIcon,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    ) {
        IconToggleButton(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier.requiredWidth(40.dp),
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
        ) {
            Icon(
                sparkIcon = if (checked) checkedIcon else unCheckedIcon,
                contentDescription = contentDescription,
                tint = IconIntent.Neutral.color(),
            )
        }
    }

    /**
     * A text that can be used as a prefix or suffix for a [TextField].
     * Only use [SparkColors.onSurface] color.
     */
    @ExperimentalSparkApi
    @Composable
    public fun TextFieldText(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = SparkTheme.colors.onSurface,
            style = SparkTheme.typography.body1,
            maxLines = 1,
        )
    }

    /**
     * A dropdown that can be used as a leading or trailing content for a [TextField] that show a dropdown on touch.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @ExperimentalSparkApi
    @Composable
    public fun Dropdown(
        expanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        onDismissRequest: () -> Unit,
        dropdownLabel: @Composable RowScope.() -> Unit,
        modifier: Modifier = Modifier,
        popupDropdownContent: @Composable DropdownMenuItemColumnScope.() -> Unit,
    ) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
            Surface(
                modifier = modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable),
                onClick = {},
                shape = SparkTheme.shapes.small,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    dropdownLabel()
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = Modifier.exposedDropdownSize(true),
                content = popupDropdownContent,
            )
        }
    }
}

internal object AddonScopeInstance : AddonScope()

@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun TextFieldWithDropdownPreview() {
    var expanded by remember { mutableStateOf(false) }
    PreviewTheme {
        TextField(
            value = "+33 0123456789",
            label = "Phone number",
            onValueChange = {},
            leadingContent = {
                Dropdown(
                    modifier = Modifier,
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    onDismissRequest = {
                        expanded = false
                    },
                    dropdownLabel = {
                        Canvas(
                            modifier = Modifier.size(width = 24.dp, height = 14.dp),
                        ) {
                            drawRect(color = Color.Blue)
                            drawRect(color = Color.White, topLeft = Offset(24.dp.toPx() / 3, 0f))
                            drawRect(color = Color.Red, topLeft = Offset(24.dp.toPx() / 3 * 2, 0f))
                        }
                        Text(text = "FR", style = SparkTheme.typography.body1)
                        SparkSelectTrailingIcon(expanded = expanded)
                    },
                ) {
                    repeat(4) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                            },
                            text = { Text(text = "Dropdown") },
                        )
                    }
                }
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithButtonPreview() {
    var isLoading by remember { mutableStateOf(false) }
    PreviewTheme {
        TextField(
            value = "AA-123-BB",
            label = "Plate number",
            onValueChange = {},
            trailingContent = {
                Button(
                    text = "Validate",
                    modifier = Modifier,
                    onClick = { isLoading = !isLoading },
                    isLoading = isLoading,
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithIconPreview() {
    PreviewTheme {
        TextField(
            value = "AA-123-BB",
            label = "Plate number",
            onValueChange = {},
            trailingContent = {
                TextFieldIcon(
                    icon = SparkIcons.QuestionOutline,
                    modifier = Modifier,
                    contentDescription = "",
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithIconButtonPreview() {
    PreviewTheme {
        var value by remember {
            mutableStateOf("AA-123-BB")
        }
        TextField(
            value = value,
            label = "Plate number",
            onValueChange = {},
            trailingContent = {
                TextFieldIconButton(
                    modifier = Modifier,
                    icon = SparkIcons.EyeOffFill,
                    contentDescription = "",
                    onClick = { value = Random.nextInt(0, 8000).toString() },
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithIconToggleButtonPreview() {
    PreviewTheme {
        var checked by remember {
            mutableStateOf(false)
        }
        TextField(
            value = "AA-123-BB",
            label = "Plate number",
            onValueChange = {},
            trailingContent = {
                TextFieldIconToggleButton(
                    modifier = Modifier,
                    checked = checked,
                    checkedIcon = SparkIcons.EyeFill,
                    unCheckedIcon = SparkIcons.EyeOffFill,
                    contentDescription = "",
                    onCheckedChange = { checked = it },
                )
            },
        )
        TextField(
            value = "AA-123-BB",
            label = "Plate number",
            onValueChange = {},
            trailingContent = {
                TextFieldIconToggleButton(
                    modifier = Modifier,
                    checked = !checked,
                    checkedIcon = SparkIcons.EyeFill,
                    unCheckedIcon = SparkIcons.EyeOffFill,
                    contentDescription = "",
                    onCheckedChange = { checked = it },
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldWithPrefixSuffixButtonPreview() {
    PreviewTheme {
        TextField(
            value = "www.adevinta.com",
            label = "Url",
            onValueChange = {},
            leadingContent = {
                TextFieldText(
                    text = "https://",
                )
            },
            trailingContent = {
                TextFieldText(
                    text = ".com",
                )
            },
        )
    }
}

@Preview
@Composable
private fun TextFieldMandatoryWithHelperPreview() {
    PreviewTheme {
        TextField(
            value = "AA-123-BB",
            label = "Helper",
            onValueChange = {},
            required = true,
            helper = "Help me please, this is mandatory",
        )
    }
}

@Preview
@Composable
private fun TextFieldWithCounterPreview() {
    PreviewTheme {
        val value by remember { mutableStateOf("AA-123-BB") }
        TextField(
            value = value,
            label = "Counter",
            onValueChange = {},
            counter = TextFieldCharacterCounter(
                count = value.length,
                maxCharacter = 20,
            ),
        )
    }
}

@Preview
@Composable
private fun TextFieldWithStatePreview() {
    PreviewTheme {
        val value by remember { mutableStateOf("AA-123-BB") }
        TextField(
            value = value,
            label = "State",
            onValueChange = {},
            state = TextFieldState.Success,
            stateMessage = "This field should contain 9 characters",
        )
    }
}
