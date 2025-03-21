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
package com.adevinta.spark.components.iconbuttons.toggle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.buttons.ButtonShape
import com.adevinta.spark.components.iconbuttons.IconButtonColors
import com.adevinta.spark.components.iconbuttons.IconButtonDefaults
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.popover.PlainTooltip
import com.adevinta.spark.components.popover.TooltipBox
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.FavoriteFill
import com.adevinta.spark.icons.FavoriteOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.minimumTouchTargetSize
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * Icon toggle buttons help people take supplementary actions with a single tap. They’re used when a
 * compact toggle button is required, such as in a toolbar or image list.
 *
 * @param checked controls the check state of this icon toggle button. When `true`, this component will
 * show icons.checked, and `false` will show icons.unchecked
 * @param onCheckedChange responds to user interaction of checking/unchecking the icon toggle button
 * and changes @param checked by `true` or `false`
 * @param icons a content to be drawn inside the IconToggleButton,
 * should show one of [IconToggleButtonIcons] values that sets checked and unchecked
 * @param colors [IconButtonColors] that will be used to resolve the colors used for this icon
 * button in different states.
 * @param modifier the [Modifier] to be applied to this icon button
 * @param enabled controls the enabled state of this icon button. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape to be applied to the IconButton background. It should be one of [ButtonShape] values
 * @param size one of the [IconButtonSize] values that sets width and height of the IconButton
 * @param border an optional [BorderStroke] to be applied to the IconButton
 * @param contentDescription text used by accessibility services to describe what this icon button
 * represents. This text should be localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this icon button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this icon button in different states.
 */

@OptIn(ExperimentalMaterial3Api::class)
@InternalSparkApi
@Composable
internal fun SparkIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
    colors: IconButtonColors,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: ButtonShape = IconButtonDefaults.DefaultShape,
    size: IconButtonSize = IconButtonDefaults.DefaultSize,
    border: BorderStroke? = null,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val content: @Composable () -> Unit = {
        Icon(
            sparkIcon = if (checked) icons.checked else icons.unchecked,
            contentDescription = contentDescription,
            size = size.iconSize,
        )
    }

    Box(modifier = modifier) {
        TooltipBox(
            state = rememberTooltipState(),
            tooltip = { PlainTooltip { Text(contentDescription.orEmpty()) } },
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
        ) {
            Surface(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier
                    .minimumTouchTargetSize()
                    .sparkUsageOverlay()
                    .semantics { role = Role.Checkbox },
                enabled = enabled,
                shape = shape.shape,
                border = border,
                color = colors.containerColor(enabled).value,
                contentColor = colors.contentColor(enabled).value,
                interactionSource = interactionSource,
            ) {
                Box(
                    modifier = Modifier.size(size.height),
                    contentAlignment = Alignment.Center,
                ) {
                    content()
                }
            }
        }
    }
}

@Preview(
    group = "IconToggleButtons",
    name = "IconToggleButtons",
)
@Composable
private fun IconToggleButtonPreview() {
    PreviewTheme {
        val intent = IconButtonIntent.Basic
        var state by remember {
            mutableStateOf(false)
        }
        IconButtonSize.entries.forEach { size ->
            SparkIconToggleButton(
                checked = state,
                onCheckedChange = { state = !state },
                icons = IconToggleButtonIcons(SparkIcons.FavoriteOutline, SparkIcons.FavoriteFill),
                colors = IconButtonDefaults.filledIconButtonColors(intent = intent.colors()),
                size = size,
            )
        }
    }
}
