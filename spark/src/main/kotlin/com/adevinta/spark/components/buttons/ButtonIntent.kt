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
package com.adevinta.spark.components.buttons

import androidx.compose.runtime.Composable
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.IntentColors

/**
 * ButtonIntent is used to define the intent of the buttons.
 */
public enum class ButtonIntent {

    /**
     * Main buttons are used for the most important actions.
     */
    Main {
        @Composable
        override fun colors(): IntentColor = IntentColors.Main.colors()
    },

    /**
     * Support buttons are used to highlight/accentuate actions.
     */
    Support {
        @Composable
        override fun colors(): IntentColor = IntentColors.Support.colors()
    },

    /**
     * Used to make button visually accentuated.
     */
    Accent {
        @Composable
        override fun colors(): IntentColor = IntentColors.Accent.colors()
    },

    /**
     * Actions on a color / image panel without on intent color.
     */
    Surface {
        @Composable
        override fun colors(): IntentColor = IntentColors.Surface.colors()
    },

    /**
     * Used for confirmation actions.
     */
    Success {
        @Composable
        override fun colors(): IntentColor = IntentColors.Success.colors()
    },

    /**
     * Used for warning actions.
     */
    Alert {
        @Composable
        override fun colors(): IntentColor = IntentColors.Alert.colors()
    },

    /**
     * Used for risky actions.
     */
    Danger {
        @Composable
        override fun colors(): IntentColor = IntentColors.Danger.colors()
    },

    /**
     * Used informational valuable actions.
     */
    Info {
        @Composable
        override fun colors(): IntentColor = IntentColors.Info.colors()
    },

    /**
     * Used for low or irrelevant actions.
     */
    Neutral {
        @Composable
        override fun colors(): IntentColor = IntentColors.Neutral.colors()
    },
    ;

    @Composable
    internal abstract fun colors(): IntentColor
}
