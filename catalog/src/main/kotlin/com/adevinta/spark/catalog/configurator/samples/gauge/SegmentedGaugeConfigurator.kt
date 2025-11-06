/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.catalog.configurator.samples.gauge

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.gauge.GaugeSize
import com.adevinta.spark.components.gauge.GaugeType
import com.adevinta.spark.components.gauge.SegmentedGauge

public val GaugesConfigurator: Configurator = Configurator(
    id = "gauge",
    name = "Segmented Gauge",
    description = "Segmented Gauge configuration",
    sourceUrl = "$SampleSourceUrl/GaugeSample.kt",
) {
    GaugeSample()
}

@Composable
private fun ColumnScope.GaugeSample() {
    var size by remember { mutableStateOf(GaugeSize.Medium) }
    var type by remember { mutableStateOf(GaugeType.NoData) }
//    var type by remember { mutableStateOf(GaugeSegment.Five) }

    SegmentedGauge(
        size = size,
        type = type,
        color = Color.Unspecified,
    )

    ButtonGroup(
        title = "Sizes",
        selectedOption = size,
        onOptionSelect = { size = it },
    )
//    ButtonGroup(
//        title = "Sizes",
//        selectedOption = size,
//        onOptionSelect = { size = it },
//    )

    DropdownEnum(
        title = "Types",
        selectedOption = type,
        onOptionSelect = { type = it },
    )
}

private enum class GaugeSegment {
    Three,
    Five;
}

@Preview
@Composable
private fun GaugeSamplePreview() {
    PreviewTheme { GaugeSample() }
}
