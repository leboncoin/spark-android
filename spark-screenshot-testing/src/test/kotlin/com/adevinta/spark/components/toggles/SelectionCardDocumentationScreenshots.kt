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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

/**
 * Documentation screenshots for the selection cards of Checkbox, RadioButton and Switch.
 * Each image shows the light and the dark theme side by side.
 */
internal class SelectionCardDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun horizontal() = paparazzi.sparkDocSnapshot {
        Checkbox.Card(
            state = ToggleableState.On,
            onClick = {},
            modifier = Modifier.width(CardWidth),
            layout = SelectionCardLayout.Horizontal,
        ) {
            CardText(title = "Home delivery", description = "Delivered to your door in 3 days")
        }
    }

    @Test
    fun vertical() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RadioButton.Card(
                selected = true,
                onClick = {},
                modifier = Modifier.width(VerticalCardWidth),
                layout = SelectionCardLayout.Vertical,
            ) {
                CardText(title = "Monthly", description = "Billed monthly")
            }
            RadioButton.Card(
                selected = false,
                onClick = {},
                modifier = Modifier.width(VerticalCardWidth),
                layout = SelectionCardLayout.Vertical,
            ) {
                CardText(title = "Yearly", description = "Billed yearly")
            }
        }
    }

    @Test
    fun checkbox_group() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.width(CardWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Checkbox.Card(state = ToggleableState.On, onClick = {}) {
                CardText(title = "Home delivery", description = "Delivered to your door")
            }
            Checkbox.Card(state = ToggleableState.Off, onClick = {}) {
                CardText(title = "Pickup point", description = "Collect it near you")
            }
            Checkbox.Card(state = ToggleableState.On, onClick = {}) {
                CardText(title = "Hand delivery", description = "Meet the seller")
            }
        }
    }

    @Test
    fun radio_group() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier
                .width(CardWidth)
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RadioButton.Card(selected = false, onClick = {}) {
                CardText(title = "Standard", description = "3 to 5 days")
            }
            RadioButton.Card(selected = true, onClick = {}) {
                CardText(title = "Express", description = "Next day")
            }
            RadioButton.Card(selected = false, onClick = {}) {
                CardText(title = "Same day", description = "Before 8 pm")
            }
        }
    }

    @Test
    fun switch_list() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.width(CardWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Switch.Card(checked = true, onCheckedChange = {}) {
                CardText(title = "Messages", description = "Notify me of new messages")
            }
            Switch.Card(checked = false, onCheckedChange = {}) {
                CardText(title = "Saved searches", description = "Notify me of new ads")
            }
            Switch.Card(checked = true, onCheckedChange = {}) {
                CardText(title = "Price drops", description = "Notify me when a price drops")
            }
        }
    }

    @Test
    fun states() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.width(CardWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Checkbox.Card(state = ToggleableState.Off, onClick = {}) {
                CardText(title = "Unselected", description = "The default state")
            }
            Checkbox.Card(state = ToggleableState.On, onClick = {}) {
                CardText(title = "Selected", description = "The user picked this option")
            }
            Checkbox.Card(state = ToggleableState.On, onClick = {}, enabled = false) {
                CardText(title = "Disabled", description = "The user cannot change it")
            }
            Checkbox.Card(state = ToggleableState.Off, onClick = {}, error = "Select an option") {
                CardText(title = "Error", description = "The choice is not valid")
            }
        }
    }

    @Test
    fun empty_toggle_with_helper_text() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.width(CardWidth),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // With no toggle, the helper text must say how many options the user can pick.
            Text(
                text = "Select one or more options",
                style = SparkTheme.typography.body2,
                color = SparkTheme.colors.onSurface.dim1,
            )
            Checkbox.Card(state = ToggleableState.On, onClick = {}, toggle = { null }) {
                CardText(title = "Home delivery", description = "Delivered to your door")
            }
            Checkbox.Card(state = ToggleableState.Off, onClick = {}, toggle = { null }) {
                CardText(title = "Pickup point", description = "Collect it near you")
            }
        }
    }
}

private val CardWidth = 320.dp
private val VerticalCardWidth = 140.dp

@Composable
private fun CardText(title: String, description: String) {
    Text(text = title, style = SparkTheme.typography.body1.highlight)
    Text(
        text = description,
        style = SparkTheme.typography.body2,
        color = SparkTheme.colors.onSurface.dim1,
    )
}
