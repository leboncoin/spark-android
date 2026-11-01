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

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.tokens.contentColorFor
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tokens.disabled

internal object IconButtonDefaults {

    /**
     * The outlined icon button's border size
     */
    internal val OutlinedBorderSize = IconButtonTokens.BorderWidth

    /**
     * The default intent of IconButton
     */
    internal val DefaultIntent = IconButtonIntent.Main

    /**
     * The default size of IconButton
     */
    internal val DefaultSize = IconButtonSize.Medium

    /**
     * The [Color] for a PlainTooltipBox's container.
     */
    internal val TooltipContainerColor @Composable get() = SparkTheme.colors.surfaceInverse

    /**
     * The [Color] for the content within the PlainTooltipBox.
     */
    internal val TooltipContentColor @Composable get() = SparkTheme.colors.onSurfaceInverse

    /**
     * The [Shape] for a [com.adevinta.spark.components.popover.TooltipBox]'s container.
     */
    val TooltipContainerShape: Shape
        @Composable get() = SparkTheme.shapes.large

    @Composable
    fun filledIconButtonColors(
        intent: IntentColor,
        containerColor: Color = intent.color,
        contentColor: Color = intent.onColor,
        disabledContainerColor: Color = containerColor.dim3,
        disabledContentColor: Color = contentColor.dim3,
    ): IconButtonColors = IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun outlinedIconButtonColors(
        intent: IntentColor,
        containerColor: Color = Color.Transparent,
        contentColor: Color = intent.color,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = contentColor.dim3,
    ): IconButtonColors = IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun tintedIconButtonColors(
        intent: IntentColor,
        containerColor: Color = intent.containerColor,
        contentColor: Color = intent.onContainerColor,
        disabledContainerColor: Color = containerColor.dim3,
        disabledContentColor: Color = contentColor.dim3,
    ): IconButtonColors = IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun ghostIconButtonColors(
        intent: IntentColor,
        containerColor: Color = Color.Transparent,
        contentColor: Color = intent.color,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = contentColor.dim3,
    ): IconButtonColors = IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun ghostIconButtonColorsForSnackbar(
        color: Color,
        containerColor: Color = Color.Transparent,
        contentColor: Color = contentColorFor(backgroundColor = color),
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = contentColor.dim3,
    ): IconButtonColors = IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun contrastIconButtonColors(
        intent: IntentColor,
        containerColor: Color = SparkTheme.colors.surface,
        contentColor: Color = intent.color,
        disabledContainerColor: Color = containerColor.dim3,
    ): IconButtonColors = with(if (containerColor != contentColor) contentColor else SparkTheme.colors.onSurface) {
        IconButtonColors(
            containerColor = containerColor,
            contentColor = this,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = this.dim3,
        )
    }

    /**
     * The colours of [IconButton.Primary], matching the rebranded `Button.Primary`.
     */
    @Composable
    fun primaryIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = SparkTheme.colors.main,
        contentColor = SparkTheme.colors.onMain,
        disabledContainerColor = SparkTheme.colors.main.disabled,
        disabledContentColor = SparkTheme.colors.onMain.disabled,
    )

    /**
     * The colours of [IconButton.Secondary], matching the rebranded `Button.Secondary`.
     */
    @Composable
    fun secondaryIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = SparkTheme.colors.support,
        contentColor = SparkTheme.colors.onSupport,
        disabledContainerColor = SparkTheme.colors.support.disabled,
        disabledContentColor = SparkTheme.colors.onSupport.disabled,
    )

    /**
     * The colours of [IconButton.Tertiary], matching the rebranded `Button.Tertiary`. The border uses
     * the outline colour, not the content colour.
     */
    @Composable
    fun tertiaryIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = Color.Transparent,
        contentColor = SparkTheme.colors.onSurface,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = SparkTheme.colors.onSurface.disabled,
        borderColor = SparkTheme.colors.outline,
        disabledBorderColor = SparkTheme.colors.outline.disabled,
    )

    /**
     * The colours of [IconButton.Boost], matching the rebranded `Button.Boost`.
     */
    @Composable
    fun boostIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = SparkTheme.colors.accent,
        contentColor = SparkTheme.colors.onAccent,
        disabledContainerColor = SparkTheme.colors.accent.disabled,
        disabledContentColor = SparkTheme.colors.onAccent.disabled,
    )

    /**
     * The colours of [IconButton.Ghost], matching the rebranded `Button.Ghost`. The content follows
     * [LocalContentColor] so that the button adapts to the surface it sits on.
     */
    @Composable
    fun ghostRebrandedIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = Color.Transparent,
        contentColor = LocalContentColor.current,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = LocalContentColor.current.disabled,
    )

    /**
     * The colours of [IconButton.Contrast], matching the rebranded `Button.Contrast`.
     */
    @Composable
    fun contrastRebrandedIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = SparkTheme.colors.surface,
        contentColor = SparkTheme.colors.onSurface,
        disabledContainerColor = SparkTheme.colors.surface.disabled,
        disabledContentColor = SparkTheme.colors.onSurface.disabled,
    )

    /**
     * The colours of [IconButton.Danger], matching the rebranded `Button.Danger`. The border uses the
     * outline colour, not the content colour.
     */
    @Composable
    fun dangerIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = Color.Transparent,
        contentColor = SparkTheme.colors.error,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = SparkTheme.colors.error.disabled,
        borderColor = SparkTheme.colors.outline,
        disabledBorderColor = SparkTheme.colors.outline.disabled,
    )

    /**
     * The colours of [IconButton.Success], matching the rebranded `Button.Success`. The border uses
     * the outline colour, not the content colour.
     */
    @Composable
    fun successIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = Color.Transparent,
        contentColor = SparkTheme.colors.success,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = SparkTheme.colors.success.disabled,
        borderColor = SparkTheme.colors.outline,
        disabledBorderColor = SparkTheme.colors.outline.disabled,
    )

    /**
     * The colours of [IconButton.Ai], matching the rebranded `Button.Ai`.
     */
    @Composable
    fun aiIconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = SparkTheme.colors.ai,
        contentColor = SparkTheme.colors.onAi,
        disabledContainerColor = SparkTheme.colors.ai.disabled,
        disabledContentColor = SparkTheme.colors.onAi.disabled,
    )
}

/**
 * Represents the container, content and border colors used in an icon button in different states.
 *
 * A `null` [borderColor] or [disabledBorderColor] means the icon button draws no border in that
 * state.
 */
@Immutable
// FIXME: Copy from MD. Remove once MD version is updated and constructor is no longer internal
internal class IconButtonColors(
    private val containerColor: Color,
    private val contentColor: Color,
    private val disabledContainerColor: Color,
    private val disabledContentColor: Color,
    private val borderColor: Color? = null,
    private val disabledBorderColor: Color? = null,
) {
    /**
     * Represents the container color for this icon button, depending on [enabled].
     *
     * @param enabled whether the icon button is enabled
     */
    @Composable
    internal fun containerColor(
        enabled: Boolean,
    ): State<Color> = rememberUpdatedState(if (enabled) containerColor else disabledContainerColor)

    /**
     * Represents the content color for this icon button, depending on [enabled].
     *
     * @param enabled whether the icon button is enabled
     */
    @Composable
    internal fun contentColor(
        enabled: Boolean,
    ): State<Color> = rememberUpdatedState(if (enabled) contentColor else disabledContentColor)

    /**
     * Represents the border color for this icon button, depending on [enabled]. Returns `null` when
     * the icon button has no border in that state.
     *
     * @param enabled whether the icon button is enabled
     */
    internal fun borderColor(
        enabled: Boolean,
    ): Color? = if (enabled) borderColor else disabledBorderColor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is IconButtonColors) return false

        if (containerColor != other.containerColor) return false
        if (contentColor != other.contentColor) return false
        if (disabledContainerColor != other.disabledContainerColor) return false
        if (disabledContentColor != other.disabledContentColor) return false
        if (borderColor != other.borderColor) return false
        if (disabledBorderColor != other.disabledBorderColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + disabledBorderColor.hashCode()

        return result
    }
}
