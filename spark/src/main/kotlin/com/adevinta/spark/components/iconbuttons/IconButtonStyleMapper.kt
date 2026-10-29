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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonStyle
import com.adevinta.spark.components.buttons.ButtonStyleMapper
import com.adevinta.spark.components.buttons.ButtonVariant
import com.adevinta.spark.icons.SparkIcon

private fun IconButtonIntent.toButtonIntent(): ButtonIntent = when (this) {
    IconButtonIntent.Accent -> ButtonIntent.Accent
    IconButtonIntent.Main -> ButtonIntent.Main
    IconButtonIntent.Support -> ButtonIntent.Support
    IconButtonIntent.Surface -> ButtonIntent.Surface
    IconButtonIntent.Success -> ButtonIntent.Success
    IconButtonIntent.Alert -> ButtonIntent.Alert
    IconButtonIntent.Danger -> ButtonIntent.Danger
    IconButtonIntent.Info -> ButtonIntent.Info
    IconButtonIntent.Neutral -> ButtonIntent.Neutral
    IconButtonIntent.Ai -> ButtonIntent.Ai
}

@Composable
private fun ButtonVariant.iconButtonColors(): IconButtonColors = when (this) {
    ButtonVariant.Ai -> IconButtonDefaults.aiIconButtonColors()
    ButtonVariant.Primary -> IconButtonDefaults.primaryIconButtonColors()
    ButtonVariant.Secondary -> IconButtonDefaults.secondaryIconButtonColors()
    ButtonVariant.Tertiary -> IconButtonDefaults.tertiaryIconButtonColors()
    ButtonVariant.Contrast -> IconButtonDefaults.contrastRebrandedIconButtonColors()
    ButtonVariant.Ghost -> IconButtonDefaults.ghostRebrandedIconButtonColors()
    ButtonVariant.Underline -> error("ButtonVariant.Underline is not available for icon buttons")
    ButtonVariant.Success -> IconButtonDefaults.successIconButtonColors()
    ButtonVariant.Danger -> IconButtonDefaults.dangerIconButtonColors()
    ButtonVariant.Boost -> IconButtonDefaults.boostIconButtonColors()
}

@Composable
internal fun RouteToRebrandedIconButton(
    intent: IconButtonIntent,
    style: ButtonStyle,
    icon: SparkIcon,
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    size: IconButtonSize,
    contentDescription: String?,
    interactionSource: MutableInteractionSource,
    atEnd: Boolean,
    modifier: Modifier = Modifier,
) {
    val variant = ButtonStyleMapper.map(intent.toButtonIntent(), style)
    // Calls the base, not IconButton.X: the legacy contentDescription is nullable and must stay so.
    SparkIconButton(
        icon = icon,
        colors = variant.iconButtonColors(),
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
