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
package com.adevinta.spark.components.progress

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.IntentColors

/**
 * SpinnerIntent is used to define the intent of the chip.
 */
public enum class SpinnerIntent {
    /**
     * Used to make UI component visually accentuated.
     */
    Accent {
        @Composable
        override fun colors(): IntentColor = IntentColors.Accent.colors()
    },

    /**
     * Used for the most important information.
     */
    Main {
        @Composable
        override fun colors(): IntentColor = IntentColors.Main.colors()
    },

    /**
     * Used to highlight information.
     */
    Support {
        @Composable
        override fun colors(): IntentColor = IntentColors.Support.colors()
    },

    /**
     * Used for feedbacks that are positive.
     */
    Success {
        @Composable
        override fun colors(): IntentColor = IntentColors.Success.colors()
    },

    /**
     * Used for feedbacks that are negative.
     */
    Alert {
        @Composable
        override fun colors(): IntentColor = IntentColors.Alert.colors()
    },

    /**
     * Used for feedbacks that are negative and dangerous.
     */
    Danger {
        @Composable
        override fun colors(): IntentColor = IntentColors.Danger.colors()
    },

    /**
     * Used for feedbacks that are informative.
     */
    Info {
        @Composable
        override fun colors(): IntentColor = IntentColors.Info.colors()
    },

    /**
     * Used for feedbacks that are neutral.
     */
    Neutral {
        @Composable
        override fun colors(): IntentColor = IntentColors.Neutral.colors()
    },

    /**
     * Spinner on a color / image panel without on intent color.
     */
    Surface {
        @Composable
        override fun colors(): IntentColor = IntentColors.Surface.colors()
    },

    /**
     * Spinner taking color of LocalContentColor set by the nearest CompositionLocalProvider
     */
    Current {
        @Composable
        override fun colors(): IntentColor = IntentColor(
            color = LocalContentColor.current,
            onColor = Color.Transparent,
            containerColor = Color.Transparent,
            onContainerColor = Color.Transparent,
        )
    },
    ;

    @Composable
    internal abstract fun colors(): IntentColor
}
