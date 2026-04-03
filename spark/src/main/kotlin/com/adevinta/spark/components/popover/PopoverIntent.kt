/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.components.popover

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme

/**
 * TagIntent is used to define the intent of the tag.
 */
public enum class PopoverIntent {

    /**
     * Used to make UI component visually accentuated.
     */
    Surface {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.surface
    },

    /**
     * Main tags are used for the most important information.
     */
    Main {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.mainContainer
    },

    /**
     * Support tags are used to highlight information.
     */
    Support {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.supportContainer
    },

    /**
     * Used to make UI component visually accentuated.
     */
    Accent {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.accentContainer
    },

    /**
     * Used to match default color of such UI controls as toggles, Slider, etc.
     */
    @Deprecated(
        message = "Basic has been removed with the rebranding, use Support instead",
        replaceWith = ReplaceWith("PopoverIntent.Support"),
        level = DeprecationLevel.ERROR,
    )
    Basic {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.supportContainer
    },

    /**
     * Used for feedbacks that are positive.
     */
    Success {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.successContainer
    },

    /**
     * Used for feedbacks that are negative.
     */
    Alert {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.alertContainer
    },

    /**
     * Used for feedbacks that are negative and dangerous.
     */
    Error {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.errorContainer
    },

    /**
     * Used for feedbacks that are informative.
     */
    Info {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.infoContainer
    },

    /**
     * Used for feedbacks that are neutral.
     */
    Neutral {
        @Composable
        override fun containerColor(): Color = SparkTheme.colors.neutralContainer
    },
    ;

    @Composable
    internal abstract fun containerColor(): Color
}
