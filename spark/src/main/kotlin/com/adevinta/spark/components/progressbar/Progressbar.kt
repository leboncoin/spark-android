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
package com.adevinta.spark.components.progressbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.dim4
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.LinearProgressIndicator as MaterialProgressbar

@InternalSparkApi
@Composable
internal fun SparkProgressbar(
    progress: () -> Float,
    isIndeterminate: Boolean,
    isRounded: Boolean,
    intent: ProgressbarIntent,
    modifier: Modifier = Modifier,
) {
    val color = intent.colors().color
    val trackColor = SparkTheme.colors.onBackground.dim4
    if (isIndeterminate) {
        MaterialProgressbar(
            modifier = modifier.sparkUsageOverlay(),
            color = color,
            trackColor = trackColor,
            strokeCap = if (isRounded) StrokeCap.Round else StrokeCap.Butt,
        )
    } else {
        MaterialProgressbar(
            modifier = modifier.sparkUsageOverlay(),
            progress = progress,
            color = color,
            trackColor = trackColor,
            strokeCap = if (isRounded) StrokeCap.Round else StrokeCap.Butt,
        )
    }
}

/**
 * Determinate Progressbar
 *
 * Progressbar express a specified progress.
 *
 * @param progress the progress of this progress indicator, where 0.0 represents no progress and 1.0
 * represents full progress. Values outside of this range are coerced into the range.
 * @param intent The intent color for the Progressbar.
 * @param modifier Modifier to be applied to the Progressbar
 * @param isRounded Controls the border shape of the progressbar. When `true`,
 * this progressbar will have rounded border shape, & the default is rounded
 */

@Composable
public fun Progressbar(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    intent: ProgressbarIntent = ProgressbarIntent.Basic,
    isRounded: Boolean = true,
) {
    SparkProgressbar(
        progress = progress,
        isIndeterminate = false,
        intent = intent,
        modifier = modifier,
        isRounded = isRounded,
    )
}

/**
 * Intermediate Progressbar
 *
 * Progressbar express an unspecified wait time or display the duration of a process.
 *
 * @param intent The intent color for the Progressbar.
 * @param modifier Modifier to be applied to the Progressbar
 * @param isRounded Controls the border shape of the progressbar. When `true`,
 * this progressbar will have rounded border shape, & the default is rounded
 */

@Composable
public fun ProgressbarIndeterminate(
    modifier: Modifier = Modifier,
    intent: ProgressbarIntent = ProgressbarIntent.Basic,
    isRounded: Boolean = true,
) {
    SparkProgressbar(
        progress = { 0f },
        isIndeterminate = true,
        intent = intent,
        modifier = modifier,
        isRounded = isRounded,
    )
}

@Preview(
    group = "Progressbar",
    name = "Progressbar",
)
@Composable
private fun PreviewProgressbar() {
    PreviewTheme {
        Progressbar(
            modifier = Modifier.fillMaxWidth(),
            progress = { 0.5f },
            isRounded = true,
        )
        Progressbar(
            modifier = Modifier.fillMaxWidth(),
            progress = { 0.5f },
            isRounded = false,
        )
        ProgressbarIndeterminate(
            modifier = Modifier.fillMaxWidth(),
            isRounded = false,
        )
    }
}
