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
package com.adevinta.spark.components.icons

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme

public enum class IconIntent {
    /**
     * Used to match default color of such UI controls as toggles, Slider, etc.
     */
    @Deprecated(
        message = "Basic has been removed with the rebranding, use Support instead",
        replaceWith = ReplaceWith("IconIntent.Support"),
        level = DeprecationLevel.ERROR,
    )
    Basic {
        @Composable
        override fun color(): Color = SparkTheme.colors.support
    },

    /**
     * Used to make icons visually accentuated.
     */
    Accent {
        @Composable
        override fun color(): Color = SparkTheme.colors.accent
    },

    /**
     * Used for the most important information.
     */
    Main {
        @Composable
        override fun color(): Color = SparkTheme.colors.main
    },

    /**
     * Used to highlight/accentuate.
     */
    Support {
        @Composable
        override fun color(): Color = SparkTheme.colors.support
    },

    /**
     * Icon on a color / image panel without on intent color.
     */
    Surface {
        @Composable
        override fun color(): Color = SparkTheme.colors.surface
    },

    /**
     * Used for confirmation.
     */
    Success {
        @Composable
        override fun color(): Color = SparkTheme.colors.success
    },

    /**
     * Used for warning.
     */
    Alert {
        @Composable
        override fun color(): Color = SparkTheme.colors.alert
    },

    /**
     * Used to alert.
     */
    Danger {
        @Composable
        override fun color(): Color = SparkTheme.colors.error
    },

    /**
     * Used for low importance information.
     */
    Neutral {
        @Composable
        override fun color(): Color = SparkTheme.colors.neutral
    },

    /**
     * Used for low importance information.
     */
    Current {
        @Composable
        public override fun color(): Color = LocalContentColor.current
    },

    /**
     * To apply no tint and use original icon tint.
     */
    Unspecified {
        @Composable
        override fun color(): Color = Color.Unspecified
    },
    ;

    @Composable
    public abstract fun color(): Color
}
