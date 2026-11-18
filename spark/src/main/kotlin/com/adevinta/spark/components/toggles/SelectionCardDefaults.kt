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
package com.adevinta.spark.components.toggles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

/**
 * Default values for the selection cards of checkboxes, radio buttons and switches.
 */
internal object SelectionCardDefaults {

    /**
     * Space between the border of the card and its toggle and content.
     */
    val ContentPadding: PaddingValues = PaddingValues(16.dp)

    /**
     * Space between the toggle and the content.
     */
    val ToggleSpacing: Dp = 16.dp

    /**
     * Border width when the card is not selected.
     */
    val BorderWidth: Dp = 1.dp

    /**
     * Border width when the card is selected.
     */
    val SelectedBorderWidth: Dp = 2.dp

    /**
     * The border of the card. The error colour takes precedence over the selected colour.
     * The disabled state keeps the same border, since the whole card is dimmed once.
     */
    @Composable
    fun border(selected: Boolean, error: Boolean): BorderStroke {
        val width = if (selected) SelectedBorderWidth else BorderWidth
        val color = when {
            error -> SparkTheme.colors.error
            selected -> SparkTheme.colors.support
            else -> SparkTheme.colors.outline
        }
        return remember(width, color) { BorderStroke(width, color) }
    }
}
