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
@file:OptIn(FlowPreview::class)

package com.adevinta.spark.catalog.examples.samples.combobox

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.menu.NoContentItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SelectedChoice
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

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
    var state = rememberTextFieldState()
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
        state = state,
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

private val SingleSelectionWithUnselect = Example(
    id = "combobox-single-unselect",
    name = "Single selection with unselect",
    description = "Example of a SingleChoiceComboBox that allows unselecting the current selection",
    sourceUrl = ComboBoxExampleSourceUrl,
) {
    var state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        enabled = true,
        required = false,
        label = "Select a book",
        placeholder = "Choose a book...",
        helper = "You can unselect by clicking the current selection",
        dropdownContent = {
            comboBoxSampleValues.forEach { book ->
                val isBookSelected = book.title == state.text
                DropdownMenuItem(
                    text = { Text(book.title) },
                    onClick = {
                        if (isBookSelected) {
                            state.clearText()
                        } else {
                            state.setTextAndPlaceCursorAtEnd(book.title)
                        }
                        expanded = false
                    },
                    selected = isBookSelected,
                )
            }
        },
    )
}

private val FilteringComboBox = Example(
    id = "combobox-filtering",
    name = "Combobox with filtering",
    description = "Example of a SingleChoiceComboBox with filtering functionality",
    sourceUrl = ComboBoxExampleSourceUrl,
) {
    var state = rememberTextFieldState()
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

    LaunchedEffect(Unit) {
        snapshotFlow { state.text }
            .debounce(300.milliseconds)
            .onEach { queryText ->
                searchText = queryText.toString()
            }.collect()
    }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
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
                NoContentItem(text = stringResource(R.string.combobox_example_no_books_found_label))
            } else {
                filteredBooks.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book.title) },
                        onClick = {
                            state.setTextAndPlaceCursorAtEnd(book.title)
                            expanded = false
                        },
                        selected = book.title == state.text,
                    )
                }
            }
        },
    )
}

private val SuggestionComboBox = Example(
    id = "combobox-suggestion",
    name = "Combobox with suggestions",
    description = "Example of a SingleChoiceComboBox with suggestion/autocomplete functionality",
    sourceUrl = ComboBoxExampleSourceUrl,
) {
    var state = rememberTextFieldState()
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

    var showDropdown by remember { mutableStateOf(false) }
    val expanded by remember {
        derivedStateOf {
            showDropdown && suggestedBooks.isNotEmpty()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { state.text }
            .collectLatest { queryText ->
                searchText = queryText.toString()
            }
    }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        expanded = expanded,
        onExpandedChange = {
            showDropdown = it
        },
        onDismissRequest = {
            searchText = ""
            showDropdown = false
        },
        enabled = true,
        required = true,
        label = "Search with suggestions",
        placeholder = "Type to see suggestions...",
        helper = "Get book suggestions as you type",
        dropdownContent = {
            suggestedBooks.forEach { book ->
                DropdownMenuItem(
                    text = { Text(book.title) },
                    onClick = {
                        state.setTextAndPlaceCursorAtEnd(book.title)
                        searchText = ""
                        showDropdown = false
                    },
                    selected = book.title == state.text,
                )
            }
        },
    )
}

public val ComboBoxExample: List<Example> = listOf(
    MultipleComboBox,
    SingleSelectionWithUnselect,
    FilteringComboBox,
    SuggestionComboBox,
)
