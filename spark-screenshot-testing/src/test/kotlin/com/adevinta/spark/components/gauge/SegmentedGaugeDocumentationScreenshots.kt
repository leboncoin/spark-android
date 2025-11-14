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
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

class SegmentedGaugeDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun segmentedGauge_five_segment_types() = paparazzi.sparkSnapshotNightMode {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SegmentedGauge(type = GaugeTypeNormal.VeryHigh)
                SegmentedGauge(type = GaugeTypeNormal.High)
                SegmentedGauge(type = GaugeTypeNormal.Medium)
                SegmentedGauge(type = GaugeTypeNormal.Low)
                SegmentedGauge(type = GaugeTypeNormal.VeryLow)
                SegmentedGauge(type = null)
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SegmentedGaugeShort(type = GaugeTypeShort.VeryHigh)
                SegmentedGaugeShort(type = GaugeTypeShort.Low)
                SegmentedGaugeShort(type = GaugeTypeShort.VeryLow)
                SegmentedGaugeShort(type = null)
            }
        }
    }

    @Test
    fun segmentedGauge_sizes() = paparazzi.sparkSnapshotNightMode {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SegmentedGauge(size = GaugeSize.Small, type = GaugeTypeNormal.Medium)
                SegmentedGauge(size = GaugeSize.Medium, type = GaugeTypeNormal.Medium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SegmentedGaugeShort(size = GaugeSize.Small, type = GaugeTypeShort.Low)
                SegmentedGaugeShort(size = GaugeSize.Medium, type = GaugeTypeShort.Low)
            }
        }
    }

    @Test
    fun segmentedGauge_custom_colors() = paparazzi.sparkSnapshotNightMode {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SegmentedGauge(type = GaugeTypeNormal.High, customColor = SparkTheme.colors.accent)
                SegmentedGauge(type = GaugeTypeNormal.Medium, customColor = Color(0xFF2196F3)) // Blue
                SegmentedGauge(type = GaugeTypeNormal.Low, customColor = Color(0xFF4CAF50)) // Green
            }
        }
    }
}
