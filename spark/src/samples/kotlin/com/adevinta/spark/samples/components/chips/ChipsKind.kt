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
package com.adevinta.spark.samples.components.chips

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.chips.Chip
import com.adevinta.spark.components.chips.ChipIntent
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.components.chips.ChipStyles
import com.adevinta.spark.icons.CalendarOutline
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.LeboncoinIcons

/**
 * Demonstrates the four chip kinds: Assist, Filter, Input, and Suggestion.
 */
@Composable
@Preview
public fun ChipsKind() {
    var selected by remember { mutableStateOf(true) }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        Chip(text = "Assist", leadingIcon = LeboncoinIcons.CalendarOutline, onClick = {})
        ChipSelectable(
            text = "Filter",
            leadingIcon = if (selected) LeboncoinIcons.Check else null,
            onClick = { selected = !selected },
            selected = selected,
        )
        Chip(
            text = "Input",
            onClick = { },
            onClose = { },
            onCloseLabel = "Remove filter",
        )
        Chip(text = "Suggestion", onClick = { })
    }
}

/**
 * Demonstrates single-selection filter chips using RadioButton semantics and [selectableGroup].
 * Only one chip can be selected at a time.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
public fun ChipSingleSelectionSample() {
    val filters = listOf("Fruit", "Vegetable")
    var selected by remember { mutableStateOf("Fruit") }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
        modifier = Modifier.selectableGroup(),
    ) {
        filters.forEach { filter ->
            ChipSelectable(
                modifier = Modifier.semantics { role = Role.RadioButton },
                text = filter,
                selected = selected == filter,
                onClick = { selected = filter },
            )
        }
    }
}

/**
 * Demonstrates multiple-selection filter chips where several chips can be toggled independently.
 * A checkmark icon appears when a chip is selected.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
public fun ChipMultipleSelectionSample() {
    val filters = listOf("Animal", "Flower", "Tree")
    var selectedFilters by remember { mutableStateOf(listOf("Animal", "Tree")) }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        filters.forEach { filter ->
            val isSelected = filter in selectedFilters
            ChipSelectable(
                text = filter,
                selected = isSelected,
                leadingIcon = if (isSelected) LeboncoinIcons.Check else null,
                onClick = {
                    selectedFilters = if (isSelected) {
                        selectedFilters - filter
                    } else {
                        selectedFilters + filter
                    }
                },
            )
        }
    }
}

/**
 * Demonstrates input chips (dashed style) that can be removed by clicking the close button.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
public fun ChipInputSample() {
    var tags by remember { mutableStateOf(listOf("First", "Second", "Third")) }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        tags.forEach { tag ->
            Chip(
                text = tag,
                style = ChipStyles.Dashed,
                intent = ChipIntent.Neutral,
                onClick = {},
                onClose = { tags = tags - tag },
                onCloseLabel = "Remove $tag from the list",
            )
        }
    }
}
