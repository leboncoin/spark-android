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

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.meter.Meter
import com.adevinta.spark.components.meter.MeterIntent
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.VerifiedShieldFill
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.material3.RangeSlider as MaterialRangeSlider
import androidx.compose.material3.Slider as MaterialSlider

private enum class MeterContentType { Value, ValueLabel, Icon, Image, Custom }

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

@SuppressLint("MaterialComposableHasSparkReplacement")
@Composable
private fun ColumnScope.MeterSample() {
    var value by rememberSaveable { mutableFloatStateOf(70f) }
    var intent by rememberSaveable { mutableStateOf(MeterIntent.Support) }
    var size by rememberSaveable { mutableStateOf(CircleMeterSize.Large) }
    var contentType by rememberSaveable { mutableStateOf(MeterContentType.ValueLabel) }
    var range by remember { mutableStateOf(0f..100f) }
    var suffix by rememberSaveable { mutableStateOf("") }
    var animateEntrance by rememberSaveable { mutableStateOf(false) }
    var entranceKey by rememberSaveable { mutableIntStateOf(0) }

    val normalizedPercent = ((value - range.start) / (range.endInclusive - range.start) * 100)
        .toInt()
        .coerceIn(0, 100)
    val valueText = "${value.toInt()}"

    val displayValue = if (animateEntrance) {
        var animatedValue by remember(entranceKey) { mutableFloatStateOf(range.start) }
        LaunchedEffect(entranceKey) { animatedValue = value }
        animatedValue
    } else {
        value
    }

    if (size == CircleMeterSize.Small) {
        Meter.CircularSmall(
            value = displayValue,
            range = range,
            intent = intent,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    } else {
        val suffixOrNull = suffix.takeIf { it.isNotEmpty() }
        val content = when (contentType) {
            MeterContentType.Value -> CircularMeterContent.Value(valueText, valueSuffix = suffixOrNull)
            MeterContentType.ValueLabel -> CircularMeterContent.ValueLabel(
                valueText,
                valueSuffix = suffixOrNull,
                label = "of ${range.endInclusive.toInt()}",
            )
            MeterContentType.Icon -> CircularMeterContent.Icon(
                icon = LeboncoinIcons.VerifiedShieldFill,
                contentDescription = "$normalizedPercent%",
                label = "$normalizedPercent%",
            )
            MeterContentType.Image -> CircularMeterContent.Image(
                contentDescription = "$valueText of ${range.endInclusive.toInt()}",
                model = R.drawable.img_wide_image_configurator,
            )
            MeterContentType.Custom -> CircularMeterContent.Custom { Text(text = "$normalizedPercent%") }
        }
        Meter.Circular(
            value = displayValue,
            range = range,
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

    ButtonGroup(
        modifier = Modifier.fillMaxWidth(),
        title = "Size",
        selectedOption = size,
        onOptionSelect = { size = it },
    )

    AnimatedVisibility(size != CircleMeterSize.Small) {
        ButtonGroup(
            modifier = Modifier.fillMaxWidth(),
            title = "Content",
            selectedOption = contentType,
            onOptionSelect = { contentType = it },
        )
    }

    AnimatedVisibility(
        size != CircleMeterSize.Small &&
            (contentType == MeterContentType.Value || contentType == MeterContentType.ValueLabel),
    ) {
        TextField(
            value = suffix,
            onValueChange = { suffix = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Value suffix (e.g. €, %)",
        )
    }

    VerticalSpacer(8.dp)

    SwitchLabelled(
        checked = animateEntrance,
        onCheckedChange = {
            animateEntrance = it
            if (it) entranceKey++
        },
    ) {
        Text("Animate entrance (0 → value)")
    }

    VerticalSpacer(8.dp)

    Text(text = "Range: ${range.start.toInt()} - ${range.endInclusive.toInt()}")
    MaterialRangeSlider(
        value = (range.start / 200f)..(range.endInclusive / 200f),
        onValueChange = { range = (it.start * 200f)..(it.endInclusive * 200f) },
        enabled = true,
        steps = 0,
    )

    Text(text = "Value: $valueText ($normalizedPercent%)")
    MaterialSlider(
        value = (value - range.start) / (range.endInclusive - range.start),
        onValueChange = { value = range.start + it * (range.endInclusive - range.start) },
        enabled = true,
        steps = ((range.endInclusive - range.start).toInt() - 1).coerceAtLeast(0),
    )
}

@Preview
@Composable
private fun MeterSamplePreview() {
    PreviewTheme { MeterSample() }
}
