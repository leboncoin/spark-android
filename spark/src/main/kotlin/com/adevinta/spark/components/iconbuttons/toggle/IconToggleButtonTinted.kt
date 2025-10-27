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

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.buttons.ButtonShape
import com.adevinta.spark.components.iconbuttons.IconButtonColors
import com.adevinta.spark.components.iconbuttons.IconButtonDefaults
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.HeartOutline
import com.adevinta.spark.icons.SparkIcons

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
 * @param modifier the [Modifier] to be applied to this icon button
 * @param intent one of [IconButtonIntent] values that will be used to determine [IconButtonColors] to be applied*
 * @param enabled controls the enabled state of this icon button. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape to be applied to the IconButton background. It should be one of [ButtonShape] values
 * @param size one of the [IconButtonSize] values that sets width and height of the IconButton
 * @param contentDescription text used by accessibility services to describe what this icon button
 * represents. This text should be localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this icon button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this icon button in different states.
 */

@ExperimentalSparkApi
@Composable
public fun IconToggleButtonTinted(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
    modifier: Modifier = Modifier,
    intent: IconButtonIntent = IconButtonDefaults.DefaultIntent,
    enabled: Boolean = true,
    shape: ButtonShape = IconButtonDefaults.DefaultShape,
    size: IconButtonSize = IconButtonDefaults.DefaultSize,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = IconButtonDefaults.tintedIconButtonColors(intent.colors())
    SparkIconToggleButton(
        icons = icons,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = colors,
        checked = checked,
        enabled = enabled,
        shape = shape,
        size = size,
        contentDescription = contentDescription,
        interactionSource = interactionSource,
    )
}

@Preview(
    group = "IconToggleButton",
    name = "IconToggleButton Tinted",
)
@Composable
private fun IconToggleButtonTintedPreview() {
    PreviewTheme {
        var isChecked by remember { mutableStateOf(false) }
        IconToggleButtonTinted(
            checked = isChecked,
            icons = IconToggleButtonIcons(SparkIcons.HeartOutline, SparkIcons.HeartFill),
            onCheckedChange = { isChecked = !isChecked },
        )
    }
}
