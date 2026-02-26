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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.chips.Chip
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.CalendarOutline
import com.adevinta.spark.icons.Check

@Composable
@Preview
private fun ChipsKind() {
    var selected by remember { mutableStateOf(true) }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        Chip(text = "Assist", leadingIcon = SparkIcons.CalendarOutline, onClick = {})
        ChipSelectable(
            text = "Filter",
            leadingIcon = if (selected) SparkIcons.Check else null,
            onClick = { selected = !selected },
            selected = selected,
        )
        Chip(
            text = "Input",
            onClick = { },
            onClose = { },
            onCloseLabel = "Supprimer le filtre",
        )
        Chip(text = "Suggestion", onClick = { })
    }
}
