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
package com.adevinta.spark.catalog.configurator.samples.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.slider.RangeSlider
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.Minus
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.highlight

public val SlidersConfigurator: Configurator = Configurator(
    id = "slider",
    name = "Slider",
    description = "Slider configuration",
    sourceUrl = "$SampleSourceUrl/SliderSamples.kt",
) {
    SliderSample()
}

@Composable
private fun SliderSample() {
    var enabled by remember { mutableStateOf(true) }
    var rounded by remember { mutableStateOf(true) }
    var intent by remember { mutableStateOf(SliderIntent.Basic) }
    val intents = SliderIntent.entries
    var expanded by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.75f) }
    var rangeProgress by remember { mutableStateOf(0.1f..0.5f) }
    var sliderSteps by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = "Slider", style = SparkTheme.typography.headline1)

        Slider(
            value = progress,
            intent = intent,
            onValueChange = { progress = it },
            enabled = enabled,
            rounded = rounded,
            valueRange = 0f..1f,
            steps = sliderSteps,
        )

        VerticalSpacer(8.dp)
        Text(text = "Range Slider", style = SparkTheme.typography.headline1)

        RangeSlider(
            intent = intent,
            value = rangeProgress,
            onValueChange = { rangeProgress = it },
            enabled = enabled,
            rounded = rounded,
            steps = sliderSteps,
        )

        SwitchLabelled(
            checked = enabled,
            onCheckedChange = { enabled = it },
        ) {
            Text(
                text = "Enabled",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        SwitchLabelled(
            checked = rounded,
            onCheckedChange = { rounded = it },
        ) {
            Text(
                text = "Rounded",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column {
            Text(
                text = "Number of Slider Steps",
                modifier = Modifier.padding(bottom = 8.dp),
                style = SparkTheme.typography.body2.highlight,
            )
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                IconButtonFilled(
                    icon = SparkIcons.Minus,
                    onClick = {
                        if (sliderSteps > 0) {
                            sliderSteps--
                        }
                    },
                )
                Text(
                    text = sliderSteps.toString(),
                    style = SparkTheme.typography.body2.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                    ),
                )

                IconButtonFilled(
                    icon = SparkIcons.Plus,
                    onClick = {
                        sliderSteps++
                    },
                )
            }
        }

        DropdownEnum(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.configurator_component_screen_intent_label),
            selectedOption = intent,
            onOptionSelect = {
                intent = it
            },
        )
    }
}

@Preview
@Composable
private fun SliderSamplePreview() {
    PreviewTheme { SliderSample() }
}
