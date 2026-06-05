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
package com.adevinta.spark.catalog.examples.samples.meter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.meter.Meter
import com.adevinta.spark.components.meter.MeterIntent
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val MeterExampleDescription = "Meter examples"
private const val MeterExampleSourceUrl = "$SampleSourceUrl/MeterSamples.kt"

public val MeterExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "circular",
        name = "Circular Meter",
        description = MeterExampleDescription,
        sourceUrl = MeterExampleSourceUrl,
    ) {
        CircularMeterExample()
    },
    Example(
        id = "circular-small",
        name = "Circular Small Meter",
        description = MeterExampleDescription,
        sourceUrl = MeterExampleSourceUrl,
    ) {
        CircularSmallMeterExample()
    },
)

@Composable
private fun ColumnScope.CircularMeterExample() {
    Meter.Circular(
        value = 70f,
        content = CircularMeterContent.ValueLabel("70%", label = "Label"),
        intent = MeterIntent.Support,
        size = CircleMeterSize.Large,
    )
}

@Composable
private fun ColumnScope.CircularSmallMeterExample() {
    Meter.CircularSmall(
        value = 70f,
        intent = MeterIntent.Support,
    )
}
