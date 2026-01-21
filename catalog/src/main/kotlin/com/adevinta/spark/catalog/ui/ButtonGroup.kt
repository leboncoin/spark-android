/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.catalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.highlight
import kotlin.enums.enumEntries

@Composable
private fun ButtonGroupLayout(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.semantics(mergeDescendants = true) { }) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            style = SparkTheme.typography.body2.highlight,
        )
        content()
    }
}

@Composable
internal inline fun <reified T : Enum<T>> ButtonGroup(
    title: String,
    selectedOption: T,
    crossinline onOptionSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = enumEntries<T>().map { it.name }
    val selectedIndex = options.indexOf(selectedOption.name)

    ButtonGroupLayout(
        title = title,
        modifier = modifier,
    ) {
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex,
            onSegmentSelected = { index ->
                onOptionSelect(enumValueOf<T>(options[index]))
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                SingleLine(option)
            }
        }
    }
}

@Composable
internal fun ButtonGroup(
    title: String,
    selectedOption: String,
    onOptionSelect: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = options.indexOf(selectedOption)

    ButtonGroupLayout(
        title = title,
        modifier = modifier,
    ) {
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex,
            onSegmentSelected = { index ->
                onOptionSelect(options[index])
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                SingleLine(option)
            }
        }
    }
}
