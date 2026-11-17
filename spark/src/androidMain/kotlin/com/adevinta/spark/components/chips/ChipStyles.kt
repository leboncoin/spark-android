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
package com.adevinta.spark.components.chips

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tokens.disabled
import com.adevinta.spark.tokens.transparent

public enum class ChipStyles {
    Outlined {
        @Composable
        override fun colors(intent: ChipIntent): ChipColors {
            val colors = intent.colors()
            val contentColor = colors.color
            return ChipColors(
                backgroundColor = colors.containerColor.transparent,
                contentColor = contentColor,
                disabledBackgroundColor = colors.containerColor.transparent,
                disabledContentColor = contentColor.disabled,
                selectedBackgroundColor = colors.containerColor,
                selectedContentColor = colors.onContainerColor,
                disabledSelectedBackgroundColor = colors.containerColor.disabled,
                disabledSelectedContentColor = colors.onContainerColor.disabled,
            )
        }
    },
    Tinted {
        @Composable
        override fun colors(intent: ChipIntent): ChipColors {
            val colors = intent.colors()
            val backgroundColor = colors.containerColor
            val contentColor = colors.onContainerColor
            return ChipColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                disabledBackgroundColor = backgroundColor.disabled,
                disabledContentColor = contentColor.disabled,
                selectedBackgroundColor = colors.color,
                selectedContentColor = colors.onColor,
                disabledSelectedBackgroundColor = colors.color.disabled,
                disabledSelectedContentColor = colors.onColor.disabled,
            )
        }
    },
    Dashed {
        @Composable
        override fun colors(intent: ChipIntent): ChipColors {
            val colors = intent.colors()
            val contentColor = colors.color
            return ChipColors(
                backgroundColor = colors.containerColor.transparent,
                contentColor = contentColor,
                disabledBackgroundColor = colors.containerColor.transparent,
                disabledContentColor = contentColor.dim3,
                selectedBackgroundColor = colors.containerColor,
                selectedContentColor = colors.onContainerColor,
                disabledSelectedBackgroundColor = colors.containerColor.disabled,
                disabledSelectedContentColor = colors.onContainerColor.disabled,
            )
        }
    },
    ;

    @Composable
    internal abstract fun colors(intent: ChipIntent): ChipColors
}

@Immutable
internal data class ChipColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
    val selectedBackgroundColor: Color,
    val selectedContentColor: Color,
    val disabledSelectedBackgroundColor: Color,
    val disabledSelectedContentColor: Color,
)

@Composable
public fun ChipStyles.border(intent: ChipIntent, enabled: Boolean): BorderStroke? {
    if (this != ChipStyles.Outlined) return null
    val colors = this.colors(intent = intent)
    val borderColor = if (enabled) colors.contentColor else colors.disabledContentColor
    return BorderStroke(width = ChipDefaults.BorderStrokeWidth, color = borderColor)
}
