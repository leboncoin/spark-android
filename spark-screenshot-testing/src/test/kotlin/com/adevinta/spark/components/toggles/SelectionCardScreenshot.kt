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
package com.adevinta.spark.components.toggles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.adevinta.spark.tokens.highlight
import org.junit.Rule
import org.junit.Test

internal class SelectionCardScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Foldable,
    )

    @Test
    fun checkboxCardHorizontal() = paparazzi.sparkSnapshotNightMode {
        CheckboxCardStates(layout = SelectionCardLayout.Horizontal)
    }

    @Test
    fun checkboxCardVertical() = paparazzi.sparkSnapshotNightMode {
        CheckboxCardStates(layout = SelectionCardLayout.Vertical)
    }

    @Test
    fun radioButtonCardHorizontal() = paparazzi.sparkSnapshotNightMode {
        RadioButtonCardStates(layout = SelectionCardLayout.Horizontal)
    }

    @Test
    fun radioButtonCardVertical() = paparazzi.sparkSnapshotNightMode {
        RadioButtonCardStates(layout = SelectionCardLayout.Vertical)
    }

    @Test
    fun switchCardHorizontal() = paparazzi.sparkSnapshotNightMode {
        SwitchCardStates(layout = SelectionCardLayout.Horizontal)
    }

    @Test
    fun switchCardVertical() = paparazzi.sparkSnapshotNightMode {
        SwitchCardStates(layout = SelectionCardLayout.Vertical)
    }
}

/** One cell of the state grid. */
private data class CardState(
    val label: String,
    val selected: Boolean,
    val enabled: Boolean = true,
    val error: Boolean = false,
    val indeterminate: Boolean = false,
)

// Hovered and pressed are not covered here. The card draws both with the default ripple.
// Paparazzi renders a hovered card the same as a resting one.
// A pressed card aborts the test JVM in the layoutlib ripple animator.
private val commonStates = listOf(
    CardState(label = "Unselected", selected = false),
    CardState(label = "Selected", selected = true),
    CardState(label = "Disabled", selected = false, enabled = false),
    CardState(label = "Disabled selected", selected = true, enabled = false),
)

private val errorStates = listOf(
    CardState(label = "Error", selected = false, error = true),
    CardState(label = "Error selected", selected = true, error = true),
)

private val checkboxStates = commonStates +
    CardState(label = "Indeterminate", selected = true, indeterminate = true) +
    errorStates

private val radioButtonStates = commonStates + errorStates

@Composable
private fun CheckboxCardStates(layout: SelectionCardLayout) {
    StateGrid(checkboxStates) { state ->
        Checkbox.Card(
            state = when {
                state.indeterminate -> ToggleableState.Indeterminate
                state.selected -> ToggleableState.On
                else -> ToggleableState.Off
            },
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = state.enabled,
            error = state.error,
            layout = layout,
        ) { CardContent() }
    }
}

@Composable
private fun RadioButtonCardStates(layout: SelectionCardLayout) {
    StateGrid(radioButtonStates) { state ->
        RadioButton.Card(
            selected = state.selected,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = state.enabled,
            error = state.error,
            layout = layout,
        ) { CardContent() }
    }
}

@Composable
private fun SwitchCardStates(layout: SelectionCardLayout) {
    StateGrid(commonStates) { state ->
        Switch.Card(
            checked = state.selected,
            onCheckedChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = state.enabled,
            layout = layout,
        ) { CardContent() }
    }
}

/** Lays the states out two per row, each with its name above the card. */
@Composable
private fun StateGrid(states: List<CardState>, card: @Composable (CardState) -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        states.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEach { state ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(text = state.label, style = SparkTheme.typography.caption)
                        card(state)
                    }
                }
                // Keep a lone last card at half width.
                if (row.size == 1) Column(modifier = Modifier.weight(1f)) {}
            }
        }
    }
}

@Composable
private fun ColumnScope.CardContent() {
    Text(text = "Title", style = SparkTheme.typography.body1.highlight)
    Text(text = "Description of the option", style = SparkTheme.typography.body2)
}
