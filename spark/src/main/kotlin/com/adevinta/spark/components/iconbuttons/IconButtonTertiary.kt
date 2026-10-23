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
package com.adevinta.spark.components.iconbuttons

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.progress.Spinner
import com.adevinta.spark.icons.GearOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon

/**
 * Used for miscellaneous actions: the action is important, but may not be what the user is looking
 * to do right then. Often paired with a Primary or Secondary icon button.
 *
 * ![IconButton Tertiary](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonTertiary.png)
 *
 * @param icon a content to be drawn inside the IconButton
 * @param onClick called when this icon button is clicked
 * @param contentDescription text used by accessibility services to describe what this icon button
 * represents. This text should be localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon button
 * @param size one of the [IconButtonSize] values that sets width and height of the IconButton
 * @param enabled controls the enabled state of this icon button. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param isLoading show or hide a [Spinner] instead of the [icon] to indicate a
 * loading state
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this icon button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this icon button in different states.
 * @param atEnd Whether the animated vector should be rendered at the end of all its animations.
 */
@Suppress("UnusedReceiverParameter")
@Composable
public fun IconButton.Tertiary(
    icon: SparkIcon,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: IconButtonSize = IconButtonDefaults.DefaultSize,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    atEnd: Boolean = false,
) {
    SparkIconButton(
        icon = icon,
        colors = IconButtonDefaults.tertiaryIconButtonColors(),
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        size = size,
        contentDescription = contentDescription,
        interactionSource = interactionSource,
        atEnd = atEnd,
    )
}

@Preview(
    group = "IconButtons",
    name = "IconButton Tertiary",
)
@Composable
private fun IconButtonTertiaryPreview() {
    PreviewTheme {
        val icon = LeboncoinIcons.GearOutline
        val contentDescription = "Localized description"

        IconButton.Tertiary(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
        )

        IconButton.Tertiary(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
            enabled = false,
        )

        IconButton.Tertiary(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
            isLoading = true,
        )
    }
}
