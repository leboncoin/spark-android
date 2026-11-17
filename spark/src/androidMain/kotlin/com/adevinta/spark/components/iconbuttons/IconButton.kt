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
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.popover.PlainTooltip
import com.adevinta.spark.components.popover.TooltipBox
import com.adevinta.spark.components.progress.Spinner
import com.adevinta.spark.components.progress.SpinnerSize
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.GearOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tools.modifiers.minimumTouchTargetSize
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * Draws the icon button that the [IconButton] variants and the legacy icon buttons share.
 *
 * The caller chooses the colours. This function adds the content description, the tooltip, the
 * loading state, and the minimum touch target. For the behaviour that every icon button shares,
 * see [IconButton].
 *
 * @param icon The [SparkIcon] to draw at the centre of the icon button.
 * @param colors The [IconButtonColors] that resolve the container, content, and border colours in
 * the enabled and disabled states.
 * @param onClick The callback that runs when the user clicks the icon button. It does not run while
 * [isLoading] is `true`.
 * @param modifier The [Modifier] to apply to the icon button.
 * @param enabled Whether the icon button responds to user input. If `false`, it ignores input, draws
 * its disabled colours, and accessibility services report it as disabled. Default: `true`.
 * @param isLoading Whether the icon button shows its loading state. If `true`, a [Spinner] replaces
 * the [icon], the icon button ignores clicks, and screen readers announce the state "Loading" with
 * the content description. If `false`, the icon button shows the [icon]. Default: `false`.
 * @param size The [IconButtonSize] of the icon button: [IconButtonSize.Small] (32 dp),
 * [IconButtonSize.Medium] (44 dp), or [IconButtonSize.Large] (56 dp). Default:
 * [IconButtonSize.Medium].
 * @param border The [BorderStroke] to draw around the icon button. If `null`, the border comes from
 * [colors], and the icon button has no border when [colors] defines none.
 * @param contentDescription The localised text that screen readers announce and that the icon button
 * shows as its tooltip. A `null` or blank value shows no tooltip, and `null` also leaves the icon
 * button without an accessible name.
 * @param interactionSource The [MutableInteractionSource] that emits the [Interaction] events of the
 * icon button, such as press and focus. Pass your own `remember`ed instance to observe those events.
 * @param atEnd Whether an animated [icon] is drawn at the end of its animation. Changing the value
 * from `false` to `true` plays the animation of an animated vector icon. A static icon ignores the
 * value. Default: `false`.
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
    size: IconButtonSize = IconButtonDefaults.DefaultSize,
    border: BorderStroke? = null,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    atEnd: Boolean = false,
) {
    // The label lives on the clickable node, so it survives the Icon to Spinner swap.
    val content: @Composable () -> Unit = {
        Icon(
            sparkIcon = icon,
            contentDescription = null,
            size = size.iconSize,
            atEnd = atEnd,
        )
    }
    val loadingStateDescription = stringResource(R.string.spark_icon_button_loading_a11y)
    Box(modifier = modifier) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = TooltipAnchorPosition.Above,
            ),
            tooltip = {
                PlainTooltip {
                    Text(contentDescription.orEmpty())
                }
            },
            state = rememberTooltipState(),
            // Without a label there is nothing to show, so long press and hover show no tooltip.
            enableUserInput = !contentDescription.isNullOrBlank(),
        ) {
            Surface(
                onClick = { if (!isLoading) onClick() },
                modifier = Modifier
                    .minimumTouchTargetSize()
                    .sparkUsageOverlay()
                    .semantics {
                        role = Role.Button
                        contentDescription?.let { this.contentDescription = it }
                        if (isLoading) stateDescription = loadingStateDescription
                    },
                enabled = enabled,
                shape = IconButtonTokens.shape,
                color = colors.containerColor(enabled = enabled).value,
                contentColor = colors.contentColor(enabled).value,
                border = border ?: colors.borderColor(enabled)?.let { BorderStroke(IconButtonTokens.BorderWidth, it) },
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
                                // The button node carries the label and the loading state.
                                modifier = Modifier.clearAndSetSemantics { },
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

/**
 * Groups the rebranded icon button variants. Use an icon button for a supplementary action that
 * needs only an icon, such as an action in a toolbar or an image list.
 *
 * Each variant is an extension function of this object, so you call it as `IconButton.Primary(...)`.
 * All variants take the same parameters and behave the same way. They differ only in colour and
 * border.
 *
 * When you choose a variant, start from the importance of the action in the view:
 *
 * | Variant | Appearance | Use for |
 * |---------|------------|---------|
 * | [IconButton.Primary] | Main colour container | The one action of a view that matters most |
 * | [IconButton.Secondary] | Support colour container | Most actions |
 * | [IconButton.Tertiary] | Transparent container, outline border | Important actions that are not the likely next step |
 * | [IconButton.Boost] | Accent colour container | Conversion and monetisation actions, such as boosting an ad |
 * | [IconButton.Ai] | AI colour container (experimental) | AI-powered actions |
 * | [IconButton.Danger] | Transparent container, outline border, error colour icon | Destructive or irreversible actions |
 * | [IconButton.Success] | Transparent container, outline border, success colour icon | Positive confirmations |
 * | [IconButton.Contrast] | Surface colour container | Coloured or image backgrounds |
 * | [IconButton.Ghost] | No container, icon follows the content colour | The lowest priority actions |
 *
 * The following example shows a primary icon button that opens the settings:
 *
 * ```kotlin
 * IconButton.Primary(
 *     icon = LeboncoinIcons.GearOutline,
 *     onClick = { openSettings() },
 *     contentDescription = stringResource(R.string.settings),
 * )
 * ```
 *
 * Every variant provides the following accessibility behaviour:
 * - The content description is required. Screen readers announce it, and the icon button shows it
 *   as a tooltip on long press and on pointer hover. A blank value shows no tooltip.
 * - The role is `Button`.
 * - While `isLoading` is `true`, the content description stays on the icon button, screen readers
 *   announce the state "Loading", and the icon button ignores clicks.
 * - The layout reserves a touch target of at least 44 dp, even for [IconButtonSize.Small].
 *
 * For an action with a text label, use [com.adevinta.spark.components.buttons.Button].
 *
 * @see IconButtonSize
 */
@Immutable
public object IconButton

@Preview(
    group = "IconButtons",
    name = "IconButtons",
)
@Composable
private fun IconButtonPreview() {
    PreviewTheme {
        val icon = LeboncoinIcons.GearOutline
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
