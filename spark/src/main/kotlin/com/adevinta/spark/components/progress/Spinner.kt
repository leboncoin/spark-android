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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * Spinners provide a visual clue that an action is processing awaiting a course of change or a result.
 * @param intentColor The [IntentColor] to use to draw the spinner
 * @param modifier the [Modifier] to be applied to this component
 * @param size one of the [SpinnerSize]s that defines the size of the component
 */
@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@InternalSparkApi
@Composable
internal fun SparkSpinner(
    intentColor: IntentColor,
    modifier: Modifier = Modifier,
    size: SpinnerSize = SpinnerDefaults.Size,
) {
    androidx.compose.material3.CircularProgressIndicator(
        color = intentColor.color,
        trackColor = intentColor.containerColor,
        modifier = modifier.size(size.dp),
    )
}

/**
 * Spinners provide a visual clue that an action is processing awaiting a course of change or a result.
 * @param intent The [SpinnerIntent] colors that will be used to draw the spinner
 * @param modifier the [Modifier] to be applied to this component
 * @param size one of the [SpinnerSize]s that defines the size of the component
 */
@Composable
public fun Spinner(
    modifier: Modifier = Modifier,
    intent: SpinnerIntent = SpinnerIntent.Current,
    size: SpinnerSize = SpinnerDefaults.Size,
) {
    SparkSpinner(
        intentColor = intent.colors(),
        modifier = modifier.sparkUsageOverlay(),
        size = size,
    )
}

@Preview(
    group = "Spinner",
    name = "Spinner Medium",
)
@Composable
internal fun PreviewSpinnerMedium() {
    PreviewTheme {
        SpinnerPreview(size = SpinnerSize.Medium)
    }
}

@Preview(
    group = "Spinner",
    name = "Spinner Small",
)
@Composable
internal fun PreviewSpinnerSmall() {
    PreviewTheme {
        SpinnerPreview(size = SpinnerSize.Small)
    }
}

@Composable
private fun SpinnerPreview(size: SpinnerSize) {
    SpinnerIntent.entries.forEach { intent ->
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Spinner(intent = intent, size = size)
        }
    }
}
