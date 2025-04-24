/*
 * Copyright (c) 2025 Adevinta
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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.menu.SingleChoiceDropdownItemColumnScope
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.SingleChoiceDropdown
import kotlin.enums.enumEntries

@Composable
internal inline fun <reified T : Enum<T>> DropdownEnum(
    title: String,
    selectedOption: T,
    crossinline onOptionSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    SingleChoiceDropdown(
        modifier = modifier.fillMaxWidth(),
        value = selectedOption.name,
        label = title,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        onDismissRequest = {
            expanded = false
        },
        dropdownContent = {
            enumEntries<T>().forEach { enum ->
                DropdownEnumItem<T>(
                    enum = enum,
                    selectedOption = selectedOption,
                    onOptionSelect = {
                        onOptionSelect(it)
                        expanded = false
                    },
                )
            }
        },
    )
}

@Composable
private inline fun <reified T : Enum<T>> SingleChoiceDropdownItemColumnScope.DropdownEnumItem(
    enum: T,
    selectedOption: T,
    crossinline onOptionSelect: (T) -> Unit,
) {
    val selected = enum == selectedOption
    DropdownMenuItem(
        text = { Text(enum.name) },
        onClick = {
            onOptionSelect(enum)
        },
        selected = selected,
    )
}
