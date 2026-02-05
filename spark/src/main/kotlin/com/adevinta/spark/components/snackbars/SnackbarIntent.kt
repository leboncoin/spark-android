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
package com.adevinta.spark.components.snackbars

import androidx.compose.runtime.Composable
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.IntentColors
import com.adevinta.spark.icons.AlertFill
import com.adevinta.spark.icons.InfoFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.ValidFill
import com.adevinta.spark.icons.WarningFill

/**
 * SnackbarIntent is used to define the visual style and semantic meaning of a Snackbar.
 * Each intent corresponds to a specific color palette from the Spark theme.
 */
public enum class SnackbarIntent {

    /**
     * This intent is used for information such as validation or success.
     */
    Success {
        @Composable
        override fun colors(): IntentColor = IntentColors.Success.colors()
        override val icon: SparkIcon = SparkIcons.ValidFill
    },

    /**
     * This intent is used for information such as warnings or alerts.
     */
    Alert {
        @Composable
        override fun colors(): IntentColor = IntentColors.Alert.colors()
        override val icon: SparkIcon = SparkIcons.WarningFill
    },

    /**
     * This intent is used for information such as Danger or Error.
     */
    Error {
        @Composable
        override fun colors(): IntentColor = IntentColors.Danger.colors()
        override val icon: SparkIcon = SparkIcons.AlertFill
    },

    /**
     * This intent is used for informational content.
     */
    Info {
        @Composable
        override fun colors(): IntentColor = IntentColors.Info.colors()
        override val icon: SparkIcon = SparkIcons.InfoFill
    },
    ;

    @Composable
    internal abstract fun colors(): IntentColor

    internal abstract val icon: SparkIcon
}
