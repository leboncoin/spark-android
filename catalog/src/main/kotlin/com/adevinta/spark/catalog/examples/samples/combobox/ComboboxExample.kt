/*
 * Copyright (c) 2023-2024 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.combobox

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SelectedChoice
import kotlinx.collections.immutable.toImmutableList

private const val ComboBoxExampleSourceUrl = "$SampleSourceUrl/ComboBoxExample.kt"

internal data class Book(val id: Int, val title: String)

internal val comboBoxSampleValues = listOf(
    Book(1, "To Kill a Mockingbird"),
    Book(2, "War and Peace"),
    Book(3, "The Idiot"),
    Book(4, "A Picture of Dorian Gray"),
    Book(5, "1984"),
    Book(6, "Pride and Prejudice but it is an extremely long title"),
)

private val MultipleComboBox = Example(
    id = "combobox-multiple",
    name = "Combobox with multiple selection",
    description = "Sample of addons provided by Spark through the AddonScope api",
    sourceUrl = ComboBoxExampleSourceUrl,
) {
    var value by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedBooksId by remember {
        mutableStateOf(
            listOf(
                comboBoxSampleValues[1].id,
                comboBoxSampleValues[3].id,
            ),
        )
    }
    val selectedChoices by remember(selectedBooksId.size) {
        derivedStateOf {
            selectedBooksId.mapNotNull { id -> comboBoxSampleValues.firstOrNull { it.id == id } }
                .map { SelectedChoice(it.id.toString(), it.title) }
                .toImmutableList()
        }
    }
    MultiChoiceComboBox(
        value = value,
        onValueChange = { newValue -> value = newValue },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = true,
        required = true,
        label = "Books to sell",
        placeholder = comboBoxSampleValues[0].title,
        helper = "You can select multiple books at a time",
        selectedChoices = selectedChoices,
        onSelectedClick = { id ->
            selectedBooksId = selectedBooksId - id.toInt()
        },
    ) {
        comboBoxSampleValues.fastForEach { (id, title) ->
            key(id) {
                this.DropdownMenuItem(
                    text = { Text(text = title) },
                    onCheckedChange = {
                        selectedBooksId = if (it) {
                            selectedBooksId + id
                        } else {
                            selectedBooksId - id
                        }
                    },
                    checked = id in selectedBooksId,
                )
            }
        }
    }
}

public val ComboBoxExample: List<Example> = listOf(
    MultipleComboBox,
)
