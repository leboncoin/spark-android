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
package com.adevinta.spark.catalog.configurator.samples.meter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.meter.Meter
import com.adevinta.spark.components.meter.MeterIntent
import com.adevinta.spark.components.meter.MeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private enum class MeterContentType { Value, ValueLabel, Custom }

public val MeterConfigurator: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "meter-circular",
        name = "Circular Meter",
        description = "Circular meter configuration",
        sourceUrl = "$SampleSourceUrl/MeterSamples.kt",
    ) { _, _ ->
        MeterSample()
    },
)

@Composable
private fun ColumnScope.MeterSample() {
    var progress by remember { mutableFloatStateOf(0.7f) }
    var intent by remember { mutableStateOf(MeterIntent.Support) }
    var size by remember { mutableStateOf(MeterSize.Large) }
    var contentType by remember { mutableStateOf(MeterContentType.ValueLabel) }

    val percentText = "${(progress * 100).toInt()}%"

    if (size == MeterSize.Small) {
        Meter.CircularSmall(
            progress = progress,
            intent = intent,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    } else {
        val content = when (contentType) {
            MeterContentType.Value -> CircularMeterContent.Value(percentText)
            MeterContentType.ValueLabel -> CircularMeterContent.ValueLabel(percentText, "Label")
            MeterContentType.Custom -> CircularMeterContent.Custom { Text(text = percentText) }
        }
        Meter.Circular(
            progress = progress,
            content = content,
            intent = intent,
            size = size,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }

    VerticalSpacer(16.dp)

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Intent",
        selectedOption = intent,
        onOptionSelect = { intent = it },
    )
    VerticalSpacer(8.dp)

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Size",
        selectedOption = size,
        onOptionSelect = { size = it },
    )
    VerticalSpacer(8.dp)

    if (size != MeterSize.Small) {
        DropdownEnum(
            modifier = Modifier.fillMaxWidth(),
            title = "Content",
            selectedOption = contentType,
            onOptionSelect = { contentType = it },
        )
        VerticalSpacer(8.dp)
    }

    Text(text = "Progress: $percentText")
    Slider(
        value = progress,
        onValueChange = { progress = it },
        enabled = true,
        steps = 9,
    )
}

@Preview
@Composable
private fun MeterSamplePreview() {
    PreviewTheme { MeterSample() }
}