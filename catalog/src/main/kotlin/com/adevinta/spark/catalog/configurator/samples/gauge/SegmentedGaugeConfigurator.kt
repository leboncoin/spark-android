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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.ColorSelector
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.gauge.GaugeSize
import com.adevinta.spark.components.gauge.GaugeTypeNormal
import com.adevinta.spark.components.gauge.GaugeTypeShort
import com.adevinta.spark.components.gauge.SegmentedGauge
import com.adevinta.spark.components.gauge.SegmentedGaugeShort
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled

public val GaugesConfigurator: Configurator = Configurator(
    id = "gauge",
    name = "Segmented Gauge",
    description = "Segmented Gauge configuration",
    sourceUrl = "$SampleSourceUrl/GaugeSample.kt",
) {
    GaugeSample()
}

private val ColorSaver = Saver<Color, Long>(
    save = { it.value.toLong() },
    restore = { Color(it.toULong()) },
)

@Composable
private fun ColumnScope.GaugeSample() {
    var size by rememberSaveable { mutableStateOf(GaugeSize.Medium) }
    var type: SegmentedGaugeType by rememberSaveable { mutableStateOf(SegmentedGaugeType.Medium) }
    var selectedColor by rememberSaveable(stateSaver = ColorSaver) { mutableStateOf(Color.Unspecified) }
    var showIndicator by rememberSaveable { mutableStateOf(true) }
    val text = rememberTextFieldState("Description")
    SegmentedGauge(
        size = size,
        type = type.normalType,
        customColor = selectedColor,
        showIndicator = showIndicator,
    ) { Text(text = text.text.toString()) }

    SegmentedGaugeShort(
        size = size,
        type = type.shortType,
        customColor = selectedColor,
        showIndicator = showIndicator,
    ) { Text(text = text.text.toString()) }

    ButtonGroup(
        title = "Sizes",
        selectedOption = size,
        onOptionSelect = { size = it },
    )

    SwitchLabelled(
        checked = showIndicator,
        onCheckedChange = { showIndicator = it },
    ) {
        Text(text = "Show the indicator", modifier = Modifier.fillMaxWidth())
    }

    ColorSelector(
        title = "Color",
        selectedColor = selectedColor,
        onColorSelected = {
            selectedColor = it ?: Color.Unspecified
        },
    )

    DropdownEnum(
        title = "Types",
        selectedOption = type,
        onOptionSelect = { type = it },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        state = text,
        label = "Description",
        helper = "The description displayed either on the end side or the bottom side of the gauge depending on " +
            "the available size",
    )
}

private enum class SegmentedGaugeType(val normalType: GaugeTypeNormal?, val shortType: GaugeTypeShort?) {
    VeryHigh(GaugeTypeNormal.VeryHigh, GaugeTypeShort.VeryHigh),
    High(GaugeTypeNormal.High, GaugeTypeShort.VeryHigh),
    Medium(GaugeTypeNormal.Medium, GaugeTypeShort.Low),
    Low(GaugeTypeNormal.Low, GaugeTypeShort.Low),
    VeryLow(GaugeTypeNormal.VeryLow, GaugeTypeShort.VeryLow),
    NoData(null, null),
}

@Preview
@Composable
private fun GaugeSamplePreview() {
    PreviewTheme { GaugeSample() }
}
