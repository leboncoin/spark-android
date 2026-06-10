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
package com.adevinta.spark.components.meter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.IntentColors

public enum class MeterIntent {

    Main {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Main.colors()
    },

    Support {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Support.colors()
    },

    Accent {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Accent.colors()
    },

    Success {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Success.colors()
    },

    Alert {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Alert.colors()
    },

    Danger {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Danger.colors()
    },

    Info {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Info.colors()
    },

    Neutral {
        @Composable
        @ReadOnlyComposable
        override fun colors(): IntentColor = IntentColors.Neutral.colors()
    },
    ;

    @Composable
    @ReadOnlyComposable
    internal abstract fun colors(): IntentColor
}
