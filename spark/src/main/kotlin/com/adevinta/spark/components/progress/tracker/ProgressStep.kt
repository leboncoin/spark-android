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
package com.adevinta.spark.components.progress.tracker

import androidx.compose.ui.text.AnnotatedString
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons

/**
 * Step in the [ProgressTrackerRow] & [ProgressTrackerColumn] component.
 * Each step has a label and a enabled state.
 *
 * @property label The label of the step to be displayed in the progress tracker. [AnnotatedString]s will be styled but
 * other class inheriting from [CharSequence] will be displayed as plain text.
 * @property enabled Indicate whether the step is enabled or disabled. Disabled steps won't be interactive.
 * @property icon The icon displayed when an icon is no yet considered as done by the user.
 * @property doneIcon The icon displayed when an icon is considered as done by the user
 */
public data class ProgressStep(
    public val label: CharSequence,
    public val enabled: Boolean,
    public val icon: SparkIcon? = null,
    public val doneIcon: SparkIcon = SparkIcons.Check,
)
