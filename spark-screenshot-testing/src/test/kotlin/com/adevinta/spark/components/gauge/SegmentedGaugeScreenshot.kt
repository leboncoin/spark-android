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
@file:OptIn(ExperimentalSparkApi::class)

package com.adevinta.spark.components.gauge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

class SegmentedGaugeScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun segmentedGauge_all_states() = paparazzi.sparkSnapshotNightMode {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Five-segment gauge - All types
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SegmentedGauge(type = GaugeTypeNormal.VeryHigh) { Text("VeryHigh") }
                    SegmentedGauge(type = GaugeTypeNormal.High) { Text("High") }
                    SegmentedGauge(type = GaugeTypeNormal.Medium) { Text("Medium") }
                    SegmentedGauge(type = GaugeTypeNormal.Low) { Text("Low") }
                    SegmentedGauge(type = GaugeTypeNormal.VeryLow) { Text("VeryLow") }
                }
                SegmentedGauge(type = null) { Text("No data") } // No indicator
            }

            // Three-segment gauge - All types
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SegmentedGaugeShort(type = GaugeTypeShort.VeryHigh) { Text("VeryHigh") }
                    SegmentedGaugeShort(type = GaugeTypeShort.Low) { Text("Low") }
                    SegmentedGaugeShort(type = GaugeTypeShort.VeryLow) { Text("VeryLow") }
                }
                SegmentedGaugeShort(type = null) { Text("No data") } // No indicator
            }

            // Different sizes
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SegmentedGauge(size = GaugeSize.Small, type = GaugeTypeNormal.Medium) { Text("Small gauge") }
                    SegmentedGauge(size = GaugeSize.Medium, type = GaugeTypeNormal.Medium) { Text("Medium gauge") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SegmentedGaugeShort(size = GaugeSize.Small, type = GaugeTypeShort.Low) { Text("Small gauge") }
                    SegmentedGaugeShort(size = GaugeSize.Medium, type = GaugeTypeShort.Low) { Text("Medium gauge") }
                }
            }

            // Custom colors
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SegmentedGauge(type = GaugeTypeNormal.High, customColor = Color.Red) { Text("Custom color") }
                SegmentedGauge(type = GaugeTypeNormal.Medium, customColor = Color.Blue) { Text("Custom color") }
                SegmentedGauge(type = GaugeTypeNormal.Low, customColor = Color.Green) { Text("Custom color") }
                SegmentedGaugeShort(type = GaugeTypeShort.Low, customColor = Color.Magenta) { Text("Custom color") }
            }
        }
    }
}
