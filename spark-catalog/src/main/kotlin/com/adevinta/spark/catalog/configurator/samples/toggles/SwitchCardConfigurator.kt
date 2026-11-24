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
package com.adevinta.spark.catalog.configurator.samples.toggles

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.ConfiguratorSourceUrl
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SelectionCardLayout
import com.adevinta.spark.components.toggles.Switch
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight

internal val SwitchCardConfigurator: Configurator = Configurator(
    id = "switch-card",
    name = "Switch card",
    description = "Switch card configuration",
    sourceUrl = "$ConfiguratorSourceUrl/toggles/SwitchCardConfigurator.kt",
) { _, _ ->
    SwitchCardSample()
}

/** Shows one switch card with controls for its layout, enabled state and toggle. Switch cards have no error state. */
@Composable
private fun ColumnScope.SwitchCardSample() {
    var layout by rememberSaveable { mutableStateOf(SelectionCardLayout.Horizontal) }
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var isToggleShown by rememberSaveable { mutableStateOf(true) }
    var isChecked by rememberSaveable { mutableStateOf(false) }

    // The card keeps its default switch when the toggle is shown. A null toggle removes it.
    Switch.Card(
        checked = isChecked,
        onCheckedChange = { isChecked = it },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled,
        layout = layout,
        toggle = { if (isToggleShown) switch() else null },
    ) {
        Text(
            text = stringResource(id = R.string.spark_catalog_configurator_component_selection_card_title),
            style = SparkTheme.typography.body1.highlight,
        )
        Text(
            text = stringResource(id = R.string.spark_catalog_configurator_component_selection_card_helper),
            style = SparkTheme.typography.body2,
            color = SparkTheme.colors.onSurface.dim1,
        )
    }

    ButtonGroup(
        title = stringResource(id = R.string.spark_catalog_configurator_component_selection_card_layout_label),
        selectedOption = layout,
        onOptionSelect = { layout = it },
    )
    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = stringResource(id = R.string.spark_catalog_configurator_component_screen_enabled_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    SwitchLabelled(
        checked = isToggleShown,
        onCheckedChange = { isToggleShown = it },
    ) {
        Text(
            text = stringResource(id = R.string.spark_catalog_configurator_component_selection_card_toggle_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SwitchCardSamplePreview() {
    PreviewTheme { SwitchCardSample() }
}
