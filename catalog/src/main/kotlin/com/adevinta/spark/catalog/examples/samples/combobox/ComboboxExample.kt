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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SelectedChoice
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import kotlinx.collections.immutable.toImmutableList

private const val ComboBoxExampleSourceUrl = "$SampleSourceUrl/ComboBoxExample.kt"

internal data class Book(val id: Int, val title: Int)

internal val comboBoxSampleValues = listOf(
    Book(1, R.string.book_title_mockingbird),
    Book(2, R.string.book_title_war_peace),
    Book(3, R.string.book_title_idiot),
    Book(4, R.string.book_title_dorian_gray),
    Book(5, R.string.book_title_1984),
    Book(6, R.string.book_title_pride_prejudice),
)

private val MultipleComboBox = Example(
    id = "combobox-multiple",
    name = R.string.combobox_example_multiple_title,
    description = R.string.combobox_example_multiple_description,
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
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            value = newValue
            expanded = true // Keep dropdown open while typing
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = true,
        required = true,
        label = stringResource(R.string.combobox_example_multiple_label),
        placeholder = comboBoxSampleValues[0].title,
        helper = stringResource(R.string.combobox_example_multiple_helper),
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

private val FilteringComboBox = Example(
    id = "combobox-filtering",
    name = stringResource(R.string.combobox_example_filtering_title),
    description = stringResource(R.string.combobox_example_filtering_description),
    sourceUrl = ComboBoxExampleSourceUrl,
) {
    var value by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredBooks by remember(searchText) {
        derivedStateOf {
            if (searchText.isBlank()) {
                comboBoxSampleValues
            } else {
                comboBoxSampleValues.filter { book ->
                    book.title.contains(searchText, ignoreCase = true)
                }
            }
        }
    }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            value = newValue
            searchText = newValue // Update search text when value changes
            expanded = true // Keep dropdown open while typing
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = true,
        required = true,
        label = "Search books",
        placeholder = "Type to search...",
        helper = "Filter books by typing in the search box",
        dropdownContent = {
            if (filteredBooks.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No books found") },
                    onClick = { },
                    selected = false,
                )
            } else {
                filteredBooks.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book.title) },
                        onClick = {
                            value = book.title
                            expanded = false
                        },
                        selected = book.title == value,
                    )
                }
            }
        },
    )
}

private val SuggestionComboBox = Example(
    id = "combobox-suggestion",
    name = stringResource(R.string.combobox_example_suggestion_title),
    description = stringResource(R.string.combobox_example_suggestion_description),
    sourceUrl = ComboBoxExampleSourceUrl,
) {
    var value by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val suggestedBooks by remember(searchText) {
        derivedStateOf {
            if (searchText.isBlank()) {
                emptyList()
            } else {
                comboBoxSampleValues.filter { book ->
                    book.title.contains(searchText, ignoreCase = true)
                }.take(3) // Limit to top 3 suggestions
            }
        }
    }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            value = newValue
            searchText = newValue
            expanded = true // Keep dropdown open while typing
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = true,
        required = true,
        label = stringResource(R.string.combobox_example_suggestions_label),
        placeholder = stringResource(R.string.combobox_example_suggestions_placeholder),
        helper = stringResource(R.string.combobox_example_suggestions_helper),
        dropdownContent = {
            if (suggestedBooks.isEmpty()) {
                if (searchText.isNotBlank()) {
                    DropdownMenuItem(
                        text = { Text("No suggestions found") },
                        onClick = { },
                        selected = false,
                    )
                }
            } else {
                suggestedBooks.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book.title) },
                        onClick = {
                            value = book.title
                            expanded = false
                        },
                        selected = book.title == value,
                    )
                }
            }
        },
    )
}

public val ComboBoxExample: List<Example> = listOf(
    MultipleComboBox,
    FilteringComboBox,
    SuggestionComboBox,
)
