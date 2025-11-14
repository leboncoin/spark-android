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
package com.adevinta.spark.catalog.examples.samples.gauge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.gauge.GaugeSize
import com.adevinta.spark.components.gauge.GaugeTypeNormal
import com.adevinta.spark.components.gauge.GaugeTypeShort
import com.adevinta.spark.components.gauge.SegmentedGauge
import com.adevinta.spark.components.gauge.SegmentedGaugeShort
import com.adevinta.spark.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val SegmentedGaugeExampleSourceUrl = "$SampleSourceUrl/SegmentedGaugeExamples.kt"

public val SegmentedGaugeExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "five-segment-types",
        name = "Five-Segment Gauge Types",
        description = "All available types for the five-segment gauge component",
        sourceUrl = SegmentedGaugeExampleSourceUrl,
    ) {
        FiveSegmentTypesExample()
    },
    Example(
        id = "three-segment-types",
        name = "Three-Segment Gauge Types",
        description = "All available types for the three-segment gauge component",
        sourceUrl = SegmentedGaugeExampleSourceUrl,
    ) {
        ThreeSegmentTypesExample()
    },
    Example(
        id = "sizes",
        name = "Gauge Sizes",
        description = "Different sizes available for gauge components",
        sourceUrl = SegmentedGaugeExampleSourceUrl,
    ) {
        SizesExample()
    },
    Example(
        id = "custom-colors",
        name = "Custom Colors",
        description = "Gauge components with custom colors instead of semantic colors",
        sourceUrl = SegmentedGaugeExampleSourceUrl,
    ) {
        CustomColorsExample()
    },
    Example(
        id = "price-level",
        name = "Price Level Indicator",
        description = "Using gauges to show price levels (fair, overpriced, great deal)",
        sourceUrl = SegmentedGaugeExampleSourceUrl,
    ) {
        PriceLevelExample()
    },
)

@Composable
private fun FiveSegmentTypesExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Five-Segment Gauge - All Types")
        SegmentedGauge(type = GaugeTypeNormal.VeryHigh) { Text("VeryHigh") }
        SegmentedGauge(type = GaugeTypeNormal.High) { Text("High") }
        SegmentedGauge(type = GaugeTypeNormal.Medium) { Text("Medium") }
        SegmentedGauge(type = GaugeTypeNormal.Low) { Text("Low") }
        SegmentedGauge(type = GaugeTypeNormal.VeryLow) { Text("VeryLow") }
        SegmentedGauge(type = null) { Text("No data") } // No indicator
    }
}

@Composable
private fun ThreeSegmentTypesExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Three-Segment Gauge - All Types")
        SegmentedGaugeShort(type = GaugeTypeShort.VeryHigh) { Text("VeryHigh") }
        SegmentedGaugeShort(type = GaugeTypeShort.Low) { Text("Low") }
        SegmentedGaugeShort(type = GaugeTypeShort.VeryLow) { Text("VeryLow") }
        SegmentedGaugeShort(type = null) { Text("No data") } // No indicator
    }
}

@Composable
private fun SizesExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Gauge Sizes")

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SegmentedGauge(size = GaugeSize.Small, type = GaugeTypeNormal.Medium) { Text("Small Medium") }
            SegmentedGaugeShort(size = GaugeSize.Small, type = GaugeTypeShort.Low) { Text("Small Low") }
        }

        Text("Medium Size")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SegmentedGauge(size = GaugeSize.Medium, type = GaugeTypeNormal.Medium) { Text("Medium Medium") }
            SegmentedGaugeShort(size = GaugeSize.Medium, type = GaugeTypeShort.Low) { Text("Medium Low") }
        }
    }
}

@Composable
private fun CustomColorsExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Custom Colors")
        SegmentedGauge(type = GaugeTypeNormal.High, customColor = Color.Red) { Text("High cutom red") }
        SegmentedGauge(type = GaugeTypeNormal.Medium, customColor = Color.Blue) { Text("Medium custom blue") }
        SegmentedGauge(type = GaugeTypeNormal.Low, customColor = Color.Green) { Text("Low custom green") }
        SegmentedGaugeShort(type = GaugeTypeShort.Low, customColor = Color.Magenta) { Text("Low custom magenta") }
    }
}

@Composable
private fun PriceLevelExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Price Level Indicators")
        SegmentedGauge(type = GaugeTypeNormal.Medium) { Text("Fair Price") }
        SegmentedGauge(type = GaugeTypeNormal.Low) { Text("Overpriced") }
        SegmentedGauge(type = GaugeTypeNormal.VeryHigh) { Text("Great Deal") }
    }
}
