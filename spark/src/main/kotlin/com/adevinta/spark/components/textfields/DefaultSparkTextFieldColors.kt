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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.dim2
import com.adevinta.spark.tokens.dim3

@Composable
internal fun sparkOutlinedTextFieldColors(
    textColor: Color = SparkTheme.colors.onSurface,
    disabledTextColor: Color = textColor.dim3,
    containerColor: Color = Color.Transparent,
    disabledContainerColor: Color = Color.Transparent,
    cursorColor: Color = SparkTheme.colors.onSurface,
    selectionColors: TextSelectionColors = LocalTextSelectionColors.current,
    focusedBorderColor: Color = SparkTheme.colors.outlineHigh,
    unfocusedBorderColor: Color = SparkTheme.colors.outlineHigh.dim2,
    disabledBorderColor: Color = SparkTheme.colors.outline,
    focusedLeadingContentColor: Color = SparkTheme.colors.neutral,
    unfocusedLeadingContentColor: Color = focusedLeadingContentColor,
    disabledLeadingContentColor: Color = focusedLeadingContentColor.dim3,
    focusedTrailingContentColor: Color = SparkTheme.colors.neutral,
    unfocusedTrailingContentColor: Color = focusedTrailingContentColor,
    disabledTrailingContentColor: Color = focusedTrailingContentColor.dim3,
    focusedLabelColor: Color = SparkTheme.colors.onSurface,
    unfocusedLabelColor: Color = focusedLabelColor.dim1,
    disabledLabelColor: Color = focusedLabelColor.dim3,
    placeholderColor: Color = SparkTheme.colors.onSurface.dim1,
    disabledPlaceholderColor: Color = SparkTheme.colors.onSurface.dim3,
    focusedSupportingTextColor: Color = SparkTheme.colors.onSurface.dim1,
    unfocusedSupportingTextColor: Color = focusedSupportingTextColor,
    disabledSupportingTextColor: Color = focusedSupportingTextColor.dim3,
): DefaultSparkTextFieldColors = DefaultSparkTextFieldColors(
    textColor = textColor,
    disabledTextColor = disabledTextColor,
    containerColor = containerColor,
    disabledContainerColor = disabledContainerColor,
    cursorColor = cursorColor,
    textSelectionColors = selectionColors,
    focusedIndicatorColor = focusedBorderColor,
    unfocusedIndicatorColor = unfocusedBorderColor,
    disabledIndicatorColor = disabledBorderColor,
    focusedLeadingContentColor = focusedLeadingContentColor,
    unfocusedLeadingContentColor = unfocusedLeadingContentColor,
    disabledLeadingContentColor = disabledLeadingContentColor,
    focusedTrailingContentColor = focusedTrailingContentColor,
    unfocusedTrailingContentColor = unfocusedTrailingContentColor,
    disabledTrailingContentColor = disabledTrailingContentColor,
    focusedLabelColor = focusedLabelColor,
    unfocusedLabelColor = unfocusedLabelColor,
    disabledLabelColor = disabledLabelColor,
    placeholderColor = placeholderColor,
    disabledPlaceholderColor = disabledPlaceholderColor,
    focusedSupportingTextColor = focusedSupportingTextColor,
    unfocusedSupportingTextColor = unfocusedSupportingTextColor,
    disabledSupportingTextColor = disabledSupportingTextColor,
)

/**
 * Represents the colors of the input text, container, and content (including label, placeholder,
 * leading and trailing icons) used in a text field in different states.
 *
 * See [TextFieldDefaults.defaultTextFieldColors] for the default colors used in [TextField].
 */
@Immutable
internal data class DefaultSparkTextFieldColors(
    private val textColor: Color,
    private val disabledTextColor: Color,
    private val containerColor: Color,
    private val disabledContainerColor: Color,
    private val cursorColor: Color,
    private val textSelectionColors: TextSelectionColors,
    private val focusedIndicatorColor: Color,
    private val unfocusedIndicatorColor: Color,
    private val disabledIndicatorColor: Color,
    private val focusedLeadingContentColor: Color,
    private val unfocusedLeadingContentColor: Color,
    private val disabledLeadingContentColor: Color,
    private val focusedTrailingContentColor: Color,
    private val unfocusedTrailingContentColor: Color,
    private val disabledTrailingContentColor: Color,
    private val focusedLabelColor: Color,
    private val unfocusedLabelColor: Color,
    private val disabledLabelColor: Color,
    private val placeholderColor: Color,
    private val disabledPlaceholderColor: Color,
    private val focusedSupportingTextColor: Color,
    private val unfocusedSupportingTextColor: Color,
    private val disabledSupportingTextColor: Color,
) {
    /**
     * Represents the color used for the leading icon of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun leadingIconColor(
        enabled: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        return rememberUpdatedState(
            when {
                !enabled -> disabledLeadingContentColor
                focused -> focusedLeadingContentColor
                else -> unfocusedLeadingContentColor
            },
        )
    }

    /**
     * Represents the color used for the trailing icon of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun trailingIconColor(
        enabled: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        return rememberUpdatedState(
            when {
                !enabled -> disabledTrailingContentColor
                focused -> focusedTrailingContentColor
                else -> unfocusedTrailingContentColor
            },
        )
    }

    /**
     * Represents the color used for the border indicator of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param readOnly whether the text field's value can't be edited
     * @param state whether the text field's current value is in error, success or alert
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun indicatorColor(
        enabled: Boolean,
        readOnly: Boolean,
        state: TextFieldState?,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> disabledIndicatorColor
            state != null -> state.color()
            focused && !readOnly -> focusedIndicatorColor
            else -> unfocusedIndicatorColor
        }
        return if (enabled) {
            animateColorAsState(targetValue, tween(durationMillis = 150), label = "Indicator Color")
        } else {
            rememberUpdatedState(targetValue)
        }
    }

    /**
     * Represents the container color for this text field.
     */
    @Composable
    internal fun containerColor(enabled: Boolean): State<Color> = if (enabled) {
        animateColorAsState(containerColor, tween(durationMillis = 150), label = "Container Color")
    } else {
        rememberUpdatedState(disabledContainerColor)
    }

    /**
     * Represents the color used for the placeholder of this text field.
     *
     * @param enabled whether the text field is enabled
     */
    @Composable
    internal fun placeholderColor(
        enabled: Boolean,
    ): State<Color> = rememberUpdatedState(if (enabled) placeholderColor else disabledPlaceholderColor)

    /**
     * Represents the color used for the label of this text field.
     *
     * @param enabled whether the text field is enabled
     * @param interactionSource the [InteractionSource] of this text field. Helps to determine if
     * the text field is in focus or not
     */
    @Composable
    internal fun labelColor(
        enabled: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> disabledLabelColor
            focused -> focusedLabelColor
            else -> unfocusedLabelColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    internal fun textColor(
        enabled: Boolean,
    ): State<Color> = rememberUpdatedState(if (enabled) textColor else disabledTextColor)

    @Composable
    internal fun supportingTextColor(
        enabled: Boolean,
        state: FormFieldStatus?,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        return rememberUpdatedState(
            when {
                !enabled -> disabledSupportingTextColor
                state == FormFieldStatus.Error -> state.color()
                focused -> focusedSupportingTextColor
                else -> unfocusedSupportingTextColor
            },
        )
    }

    /**
     * Represents the color used for the cursor of this text field.
     *
     * @param state whether the text field's current value is in error, success or alert
     */
    @Composable
    internal fun cursorColor(state: TextFieldState?): State<Color> = rememberUpdatedState(state?.color() ?: cursorColor)

    /**
     * Represents the colors used for text selection in this text field.
     */
    internal val selectionColors: TextSelectionColors
        @Composable get() = textSelectionColors

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is DefaultSparkTextFieldColors) return false

        if (textColor != other.textColor) return false
        if (disabledTextColor != other.disabledTextColor) return false
        if (cursorColor != other.cursorColor) return false
        if (textSelectionColors != other.textSelectionColors) return false
        if (focusedIndicatorColor != other.focusedIndicatorColor) return false
        if (unfocusedIndicatorColor != other.unfocusedIndicatorColor) return false
        if (disabledIndicatorColor != other.disabledIndicatorColor) return false
        if (focusedLeadingContentColor != other.focusedLeadingContentColor) return false
        if (unfocusedLeadingContentColor != other.unfocusedLeadingContentColor) return false
        if (disabledLeadingContentColor != other.disabledLeadingContentColor) return false
        if (focusedTrailingContentColor != other.focusedTrailingContentColor) return false
        if (unfocusedTrailingContentColor != other.unfocusedTrailingContentColor) return false
        if (disabledTrailingContentColor != other.disabledTrailingContentColor) return false
        if (containerColor != other.containerColor) return false
        if (focusedLabelColor != other.focusedLabelColor) return false
        if (unfocusedLabelColor != other.unfocusedLabelColor) return false
        if (disabledLabelColor != other.disabledLabelColor) return false
        if (placeholderColor != other.placeholderColor) return false
        if (disabledPlaceholderColor != other.disabledPlaceholderColor) return false
        if (focusedSupportingTextColor != other.focusedSupportingTextColor) return false
        if (unfocusedSupportingTextColor != other.unfocusedSupportingTextColor) return false
        return disabledSupportingTextColor == other.disabledSupportingTextColor
    }

    override fun hashCode(): Int {
        var result = textColor.hashCode()
        result = 31 * result + disabledTextColor.hashCode()
        result = 31 * result + cursorColor.hashCode()
        result = 31 * result + textSelectionColors.hashCode()
        result = 31 * result + focusedIndicatorColor.hashCode()
        result = 31 * result + unfocusedIndicatorColor.hashCode()
        result = 31 * result + disabledIndicatorColor.hashCode()
        result = 31 * result + focusedLeadingContentColor.hashCode()
        result = 31 * result + unfocusedLeadingContentColor.hashCode()
        result = 31 * result + disabledLeadingContentColor.hashCode()
        result = 31 * result + focusedTrailingContentColor.hashCode()
        result = 31 * result + unfocusedTrailingContentColor.hashCode()
        result = 31 * result + disabledTrailingContentColor.hashCode()
        result = 31 * result + containerColor.hashCode()
        result = 31 * result + focusedLabelColor.hashCode()
        result = 31 * result + unfocusedLabelColor.hashCode()
        result = 31 * result + disabledLabelColor.hashCode()
        result = 31 * result + placeholderColor.hashCode()
        result = 31 * result + disabledPlaceholderColor.hashCode()
        result = 31 * result + focusedSupportingTextColor.hashCode()
        result = 31 * result + unfocusedSupportingTextColor.hashCode()
        result = 31 * result + disabledSupportingTextColor.hashCode()
        return result
    }
}
