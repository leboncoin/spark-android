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
package com.adevinta.spark.components.buttons

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.adevinta.spark.icons.SparkIcon

internal enum class ButtonVariant {
    Ai,
    Primary,
    Secondary,
    Tertiary,
    Contrast,
    Ghost,
    Underline,
    Success,
    Danger,
    Boost,
}

internal object ButtonStyleMapper {

    @Suppress("DEPRECATION_ERROR")
    fun map(intent: ButtonIntent, style: ButtonStyle): ButtonVariant {
        if (intent == ButtonIntent.Ai) return ButtonVariant.Ai

        if (style == ButtonStyle.Underline) return ButtonVariant.Underline

        if (style == ButtonStyle.Contrast) return ButtonVariant.Contrast
        if (style == ButtonStyle.Ghost) return ButtonVariant.Ghost

        return when (intent) {
            ButtonIntent.Main -> mapMain(style)
            ButtonIntent.Support -> mapSupport(style)
            ButtonIntent.Accent -> mapAccent(style)
            ButtonIntent.Surface -> mapSurface(style)
            ButtonIntent.Success -> mapSuccess(style)
            ButtonIntent.Danger -> mapDanger(style)
            ButtonIntent.Info -> mapToTertiary(style)
            ButtonIntent.Alert -> mapToTertiary(style)
            ButtonIntent.Neutral -> mapToTertiary(style)
            ButtonIntent.Ai -> error("ButtonIntent.Ai is handled before reaching the when block")
            ButtonIntent.Basic -> mapSupport(style)
        }
    }

    private fun mapMain(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled -> ButtonVariant.Primary
        ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        else -> error("$style is handled before reaching mapMain")
    }

    private fun mapSupport(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled -> ButtonVariant.Secondary
        ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        else -> error("$style is handled before reaching mapSupport")
    }

    private fun mapAccent(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled -> ButtonVariant.Boost
        ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        else -> error("$style is handled before reaching mapAccent")
    }

    private fun mapSurface(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Contrast
        else -> error("$style is handled before reaching mapSurface")
    }

    private fun mapSuccess(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Success
        else -> error("$style is handled before reaching mapSuccess")
    }

    private fun mapDanger(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Danger
        else -> error("$style is handled before reaching mapDanger")
    }

    private fun mapToTertiary(style: ButtonStyle): ButtonVariant = when (style) {
        ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        else -> error("$style is handled before reaching mapToTertiary")
    }
}

@Composable
internal fun RouteToNewButton(
    variant: ButtonVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
    icon: SparkIcon? = null,
    iconSide: IconSide = IconSide.START,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    atEnd: Boolean = false,
    @SuppressLint("SlotReused") content: @Composable RowScope.() -> Unit,
) {
    when (variant) {
        ButtonVariant.Ai -> Button.Ai(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Primary -> Button.Primary(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Secondary -> Button.Secondary(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Tertiary -> Button.Tertiary(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Contrast -> Button.Contrast(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Ghost -> Button.Ghost(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Underline -> SparkButtonUnderlined(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Success -> Button.Success(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Danger -> Button.Danger(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )

        ButtonVariant.Boost -> Button.Boost(
            onClick = onClick,
            modifier = modifier,
            size = size,
            enabled = enabled,
            icon = icon,
            iconSide = iconSide,
            isLoading = isLoading,
            interactionSource = interactionSource,
            atEnd = atEnd,
            content = content,
        )
    }
}
