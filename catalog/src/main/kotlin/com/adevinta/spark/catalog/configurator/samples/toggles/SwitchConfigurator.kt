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
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.ContentSide
import com.adevinta.spark.components.toggles.Switch
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.components.toggles.ToggleIntent

public val SwitchConfigurator: Configurator = Configurator(
    id = "switch",
    name = "Switch",
    description = "Switch configuration",
    sourceUrl = "$SampleSourceUrl/SwitchSamples.kt",
) {
    SwitchSample()
}

@Composable
private fun ColumnScope.SwitchSample() {
    var isEnabled by remember { mutableStateOf(true) }
    var contentSide by remember { mutableStateOf(ContentSide.End) }
    var label: String? by remember { mutableStateOf(null) }
    var state by remember { mutableStateOf(false) }
    var intent by remember { mutableStateOf(ToggleIntent.Main) }
    val onClick = { checked: Boolean -> state = checked }
    ConfigedSwitch(
        label = label,
        onClick = onClick,
        checked = state,
        isEnabled = isEnabled,
        intent = intent,
        contentSide = contentSide,
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
    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.configurator_component_screen_intent_label),
        selectedOption = intent,
        onOptionSelect = {
            intent = it
        },
    )
    ButtonGroup(
        title = stringResource(id = R.string.configurator_component_toggle_content_side_label),
        selectedOption = contentSide,
        onOptionSelect = { contentSide = it },
    )
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
private fun SwitchSamplePreview() {
    PreviewTheme { SwitchSample() }
}

@Composable
private fun ConfigedSwitch(
    modifier: Modifier = Modifier,
    label: String?,
    onClick: (Boolean) -> Unit,
    checked: Boolean,
    isEnabled: Boolean,
    contentSide: ContentSide,
    intent: ToggleIntent,
) {
    if (label.isNullOrBlank().not()) {
        SwitchLabelled(
            modifier = modifier,
            enabled = isEnabled,
            checked = checked,
            onCheckedChange = onClick,
            contentSide = contentSide,
            intent = intent,
        ) { Text(text = label!!) }
    } else {
        Switch(
            modifier = modifier,
            enabled = isEnabled,
            checked = checked,
            onCheckedChange = onClick,
            intent = intent,
        )
    }
}
