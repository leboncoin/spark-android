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
package com.adevinta.spark.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme

internal data class IntentColor(
    val color: Color,
    val onColor: Color,
    val containerColor: Color,
    val onContainerColor: Color,
)

internal enum class IntentColors {
    /**
     * Used to match default color of such UI controls as toggles, Slider, etc.
     */
    @Deprecated(
        message = "Basic has been removed with the rebranding, use Support instead",
        replaceWith = ReplaceWith("IntentColors.Support"),
        level = DeprecationLevel.ERROR,
    )
    Basic {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.support,
            onColor = SparkTheme.colors.onSupport,
            containerColor = SparkTheme.colors.supportContainer,
            onContainerColor = SparkTheme.colors.onSupportContainer,
        )
    },

    /**
     * Used to make UI component visually accentuated.
     */
    Accent {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.accent,
            onColor = SparkTheme.colors.onAccent,
            containerColor = SparkTheme.colors.accentContainer,
            onContainerColor = SparkTheme.colors.onAccentContainer,
        )
    },

    /**
     * Used for the most important information.
     */
    Main {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.main,
            onColor = SparkTheme.colors.onMain,
            containerColor = SparkTheme.colors.mainContainer,
            onContainerColor = SparkTheme.colors.onMainContainer,
        )
    },

    /**
     * Used to highlight information.
     */
    Support {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.support,
            onColor = SparkTheme.colors.onSupport,
            containerColor = SparkTheme.colors.supportContainer,
            onContainerColor = SparkTheme.colors.onSupportContainer,
        )
    },

    /**
     * Used for feedbacks that are positive.
     */
    Success {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.success,
            onColor = SparkTheme.colors.onSuccess,
            containerColor = SparkTheme.colors.successContainer,
            onContainerColor = SparkTheme.colors.onSuccessContainer,
        )
    },

    /**
     * Used for feedbacks that are negative.
     */
    Alert {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.alert,
            onColor = SparkTheme.colors.onAlert,
            containerColor = SparkTheme.colors.alertContainer,
            onContainerColor = SparkTheme.colors.onAlertContainer,
        )
    },

    /**
     * Used for first level information
     */
    Danger {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.error,
            onColor = SparkTheme.colors.onError,
            containerColor = SparkTheme.colors.errorContainer,
            onContainerColor = SparkTheme.colors.onErrorContainer,
        )
    },

    /**
     * Used to give information with no emphasis
     */
    Info {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.info,
            onColor = SparkTheme.colors.onInfo,
            containerColor = SparkTheme.colors.infoContainer,
            onContainerColor = SparkTheme.colors.onInfoContainer,
        )
    },

    /**
     * Used for feedbacks that are neutral.
     */
    Neutral {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.neutral,
            onColor = SparkTheme.colors.onNeutral,
            containerColor = SparkTheme.colors.neutralContainer,
            onContainerColor = SparkTheme.colors.onNeutralContainer,
        )
    },

    /**
     * Badge on a color / image panel without on intent color.
     */
    Surface {
        @Composable
        override fun colors() = IntentColor(
            color = SparkTheme.colors.surface,
            onColor = SparkTheme.colors.onSurface,
            containerColor = SparkTheme.colors.surface,
            onContainerColor = SparkTheme.colors.onSurface,
        )
    },
    ;

    @Composable
    internal abstract fun colors(): IntentColor
}
