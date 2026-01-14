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
package com.adevinta.spark.components.tags

import androidx.compose.runtime.Composable
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.IntentColors

/**
 * TagIntent is used to define the intent of the tag.
 */
public enum class TagIntent {
    /**
     * Used to match default color of such UI controls as toggles, Slider, etc.
     */
    Basic {
        @Composable
        override fun colors(): IntentColor = IntentColors.Basic.colors()
    },

    /**
     * Used to make UI component visually accentuated.
     */
    Accent {
        @Composable
        override fun colors(): IntentColor = IntentColors.Accent.colors()
    },

    /**
     * Main tags are used for the most important information.
     */
    Main {
        @Composable
        override fun colors(): IntentColor = IntentColors.Main.colors()
    },

    /**
     * Support tags are used to highlight information.
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
     * To display on a image or visually heavy surface, can only be used with [TagFilled].
     */
    Surface {
        @Composable
        override fun colors(): IntentColor = IntentColors.Surface.colors()
    },
    ;

    @Composable
    internal abstract fun colors(): IntentColor
}
