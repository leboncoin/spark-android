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

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.progress.Spinner
import com.adevinta.spark.icons.GearOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon

/**
 * An icon button with a surface colour container, for coloured or image backgrounds where the other
 * variants lack visibility.
 *
 * The container uses the surface colour, and the icon uses the on-surface colour. On a background
 * of the surface colour, the container blends in, so use another variant.
 * For the behaviour that every variant shares, see [IconButton].
 *
 * ![IconButton Contrast](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonContrast.png)
 *
 * @receiver The [IconButton] object that groups the variants. The function does not read it.
 * @param icon The [SparkIcon] to draw at the centre of the icon button.
 * @param onClick The callback that runs when the user clicks the icon button. It does not run while
 * [isLoading] is `true`.
 * @param contentDescription The localised text that screen readers announce and that the icon button
 * shows as its tooltip. Required, with no default. Load it with
 * [androidx.compose.ui.res.stringResource]. A blank value shows no tooltip.
 * @param modifier The [Modifier] to apply to the icon button.
 * @param size The [IconButtonSize] of the icon button: [IconButtonSize.Small] (32 dp),
 * [IconButtonSize.Medium] (44 dp), or [IconButtonSize.Large] (56 dp). Default:
 * [IconButtonSize.Medium].
 * @param enabled Whether the icon button responds to user input. If `false`, it ignores input, draws
 * its disabled colours, and accessibility services report it as disabled. Default: `true`.
 * @param isLoading Whether the icon button shows its loading state. If `true`, a [Spinner] replaces
 * the [icon], the icon button stays enabled but ignores clicks, and screen readers announce the
 * state "Loading" with the content description. If `false`, the icon button shows the [icon].
 * Default: `false`.
 * @param interactionSource The [MutableInteractionSource] that emits the [Interaction] events of the
 * icon button, such as press and focus. Pass your own `remember`ed instance to observe those events.
 * @param atEnd Whether an animated [icon] is drawn at the end of its animation. Changing the value
 * from `false` to `true` plays the animation of an animated vector icon. A static icon ignores the
 * value. Default: `false`.
 */
@Suppress("UnusedReceiverParameter")
@Composable
public fun IconButton.Contrast(
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
        colors = IconButtonDefaults.contrastRebrandedIconButtonColors(),
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
    name = "IconButton Contrast Rebranded",
)
@Composable
private fun IconButtonContrastRebrandedPreview() {
    PreviewTheme(
        color = { SparkTheme.colors.backgroundVariant },
    ) {
        val icon = LeboncoinIcons.GearOutline
        val contentDescription = "Localized description"

        IconButton.Contrast(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
        )

        IconButton.Contrast(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
            enabled = false,
        )

        IconButton.Contrast(
            icon = icon,
            onClick = {},
            contentDescription = contentDescription,
            isLoading = true,
        )
    }
}
