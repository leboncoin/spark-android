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
package com.adevinta.spark.catalog.examples.samples.progressbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.progressbar.Progressbar
import com.adevinta.spark.components.progressbar.ProgressbarIndeterminate
import com.adevinta.spark.components.progressbar.ProgressbarIntent
import com.adevinta.spark.components.spacer.VerticalSpacer

private const val ProgressbarExampleSourceUrl = "$SampleSourceUrl/ProgressbarSamples.kt"

public val ProgressbarExamples: List<Example> = listOf(
    Example(
        id = "determinate",
        name = R.string.progressbar_example_default_title,
        description = R.string.progressbar_example_description,
        sourceUrl = ProgressbarExampleSourceUrl,
    ) {
        Column {

            ProgressbarIntent.entries.forEach { intent ->
                Progressbar(
                    progress = { 0.5f },
                    modifier = Modifier.fillMaxWidth(),
                    intent = intent,
                    isRounded = true,
                )
                VerticalSpacer(8.dp)
            }
        }
    },
    Example(
        id = "indeterminate",
        name = R.string.progressbar_example_indeterminate_title,
        description = R.string.progressbar_example_description,
        sourceUrl = ProgressbarExampleSourceUrl,
    ) {
        Column {

            ProgressbarIntent.entries.forEach { intent ->
                ProgressbarIndeterminate(
                    intent = intent,
                    modifier = Modifier.fillMaxWidth(),
                    isRounded = false,
                )
                VerticalSpacer(8.dp)
            }
        }
    },
)
