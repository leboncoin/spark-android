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
package com.adevinta.spark.components.iconbuttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.buttons.ButtonShape
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.popover.PlainTooltip
import com.adevinta.spark.components.popover.TooltipBox
import com.adevinta.spark.components.progress.Spinner
import com.adevinta.spark.components.progress.SpinnerSize
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WheelOutline
import com.adevinta.spark.tools.modifiers.minimumTouchTargetSize
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * Icon buttons help people take supplementary actions with a single tap. They’re used when a
 * compact button is required, such as in a toolbar or image list.
 *
 * @param icon a content to be drawn inside the IconButton
 * @param colors [IconButtonColors] that will be used to resolve the colors used for this icon
 * button in different states.
 * @param onClick called when this icon button is clicked
 * @param modifier the [Modifier] to be applied to this icon button
 * @param enabled controls the enabled state of this icon button. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param isLoading show or hide a [Spinner] instead of the [icon] to indicate a
 * loading state
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
internal fun SparkIconButton(
    icon: SparkIcon,
    colors: IconButtonColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: ButtonShape = IconButtonDefaults.DefaultShape,
    size: IconButtonSize = IconButtonDefaults.DefaultSize,
    border: BorderStroke? = null,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    atEnd: Boolean = false,
) {
    val content: @Composable () -> Unit = {
        Icon(
            sparkIcon = icon,
            contentDescription = contentDescription,
            size = size.iconSize,
            atEnd = atEnd,
        )
    }
    Box(modifier = modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(contentDescription.orEmpty())
                }
            },
            state = rememberTooltipState(),
        ) {
            Surface(
                onClick = onClick,
                modifier = Modifier
                    .minimumTouchTargetSize()
                    .sparkUsageOverlay(),
                enabled = enabled,
                shape = shape.shape,
                color = colors.containerColor(enabled = enabled).value,
                contentColor = colors.contentColor(enabled).value,
                border = border,
                interactionSource = interactionSource,
            ) {
                Box(
                    modifier = Modifier.size(size.height),
                    contentAlignment = Alignment.Center,
                ) {
                    AnimatedContent(targetState = isLoading, label = "loadingAnimation") { isLoading ->
                        if (isLoading) {
                            Spinner(
                                size = when (size) {
                                    IconButtonSize.Small, IconButtonSize.Medium -> SpinnerSize.Small
                                    IconButtonSize.Large -> SpinnerSize.Medium
                                },
                            )
                        } else {
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    group = "IconButtons",
    name = "IconButtons",
)
@Composable
private fun IconButtonPreview() {
    PreviewTheme {
        val icon = SparkIcons.WheelOutline
        val contentDescription = "Localized description"

        IconButtonFilled(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
        )

        IconButtonFilled(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
        )
    }
}
