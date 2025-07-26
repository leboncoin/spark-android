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
package com.adevinta.spark.catalog.configurator.samples.toggles

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.RadioButton
import com.adevinta.spark.components.toggles.RadioButtonLabelled
import com.adevinta.spark.components.toggles.SwitchLabelled

public val RadioButtonConfigurator: Configurator = Configurator(
    id = "radio-button",
    name = "RadioButton",
    description = "RadioButton configuration",
    sourceUrl = "$SampleSourceUrl/RadioButtonSamples.kt",
) {
    RadioButtonSample()
}

@Composable
private fun ColumnScope.RadioButtonSample() {
    var isEnabled by remember { mutableStateOf(true) }
    var label: String? by remember { mutableStateOf(null) }
    var selected by remember { mutableStateOf(false) }
    val onClick = { selected = !selected }

    ConfiguredRadioButton(
        label = label,
        onClick = onClick,
        selected = selected,
        isEnabled = isEnabled,
    )
    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = {
            isEnabled = it
        },
    ) {
        Text(
            text = stringResource(id = R.string.configurator_component_screen_enabled_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = label.orEmpty(),
        onValueChange = {
            label = it
        },
        label = stringResource(id = R.string.configurator_component_screen_textfield_label),
        placeholder = stringResource(id = R.string.configurator_component_toggle_placeholder_label),
    )
}

@Preview
@Composable
private fun RadioButtonSamplePreview() {
    PreviewTheme { RadioButtonSample() }
}

@Composable
private fun ConfiguredRadioButton(
    modifier: Modifier = Modifier,
    label: String?,
    onClick: () -> Unit,
    selected: Boolean,
    isEnabled: Boolean,
) {
    if (label.isNullOrBlank().not()) {
        RadioButtonLabelled(
            modifier = modifier,
            enabled = isEnabled,
            selected = selected,
            onClick = onClick,
        ) { Text(text = label) }
    } else {
        RadioButton(
            modifier = modifier,
            enabled = isEnabled,
            selected = selected,
            onClick = onClick,
        )
    }
}
