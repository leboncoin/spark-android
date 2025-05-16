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
package com.adevinta.spark.components.textfields

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.textfields.TextFieldState.Alert
import com.adevinta.spark.components.textfields.TextFieldState.Error
import com.adevinta.spark.components.textfields.TextFieldState.Success
import com.adevinta.spark.icons.AlertOutline
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WarningOutline

public typealias FormFieldStatus = TextFieldState

/**
 *   State of a [TextField] that's used to change the highlight color to either [Success], [Alert] or [Error].
 */
// @Deprecated(
//    "This name is now used by a Compose Api so we're renamming it to FormFieldStatus",
//    ReplaceWith("FormFieldStatus"),
// )
public enum class TextFieldState(public val icon: SparkIcon) {
    /**
     * Used for feedbacks that are positive.
     */
    Success(icon = SparkIcons.Check) {
        @Composable
        override fun color(): Color = SparkTheme.colors.success
    },

    /**
     * Used for feedbacks that are negative.
     */
    Alert(icon = SparkIcons.WarningOutline) {
        @Composable
        override fun color(): Color = SparkTheme.colors.alert
    },

    /**
     * Used for feedbacks that are negative and dangerous. (required field not filled or a wrong input)
     */
    Error(icon = SparkIcons.AlertOutline) {
        @Composable
        override fun color(): Color = SparkTheme.colors.error
    },
    ;

    @Composable
    internal abstract fun color(): Color
}
