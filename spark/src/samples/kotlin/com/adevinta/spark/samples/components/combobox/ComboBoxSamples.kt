/*
 * Copyright (c) 2023 Adevinta
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

package com.adevinta.spark.samples.components.combobox

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SelectedChoice
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

private data class Book(val id: Int, val title: String)

private val books = listOf(
    Book(1, "To Kill a Mockingbird"),
    Book(2, "War and Peace"),
    Book(3, "The Idiot"),
    Book(4, "A Picture of Dorian Gray"),
    Book(5, "1984"),
    Book(6, "Pride and Prejudice"),
)

/**
 * Demonstrates a [SingleChoiceComboBox] where the current selection can be cleared by clicking it again.
 */
@Composable
public fun SingleChoiceComboBoxSample() {
    val state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        required = true,
        label = "Select a book",
        placeholder = "Choose a book…",
        helper = "Click the selected item again to deselect it",
        dropdownContent = {
            books.forEach { book ->
                val isSelected = book.title == state.text.toString()
                DropdownMenuItem(
                    text = { Text(book.title) },
                    onClick = {
                        if (isSelected) state.clearText() else state.setTextAndPlaceCursorAtEnd(book.title)
                        expanded = false
                    },
                    selected = isSelected,
                )
            }
        },
    )
}

/**
 * Demonstrates a [SingleChoiceComboBox] with live text filtering — the list narrows as the user types.
 */
@Composable
public fun SingleChoiceComboBoxFilteringSample() {
    val state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredBooks by remember(searchText) {
        derivedStateOf {
            if (searchText.isBlank()) {
                books
            } else {
                books.filter { it.title.contains(searchText, ignoreCase = true) }
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { state.text }
            .debounce(300.milliseconds)
            .onEach { searchText = it.toString() }
            .collect()
    }

    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        required = true,
        label = "Search books",
        placeholder = "Type to filter…",
        dropdownContent = {
            filteredBooks.forEach { book ->
                DropdownMenuItem(
                    text = { Text(book.title) },
                    onClick = {
                        state.setTextAndPlaceCursorAtEnd(book.title)
                        expanded = false
                    },
                    selected = book.title == state.text.toString(),
                )
            }
        },
    )
}

/**
 * Demonstrates a [MultiChoiceComboBox] where several items can be selected simultaneously.
 * Selected items appear as chips inside the field and can be removed individually.
 */
@Composable
public fun MultiChoiceComboBoxSample() {
    val state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf(listOf(books[1].id, books[3].id)) }
    val selectedChoices by remember(selectedIds.size) {
        derivedStateOf {
            selectedIds
                .mapNotNull { id -> books.firstOrNull { it.id == id } }
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
        required = true,
        label = "Books to sell",
        placeholder = books[0].title,
        helper = "You can select multiple books at a time",
        selectedChoices = selectedChoices,
        onSelectedClick = { id -> selectedIds = selectedIds - id.toInt() },
    ) {
        books.fastForEach { (id, title) ->
            key(id) {
                this.DropdownMenuItem(
                    text = { Text(text = title) },
                    onCheckedChange = { checked ->
                        selectedIds = if (checked) selectedIds + id else selectedIds - id
                    },
                    checked = id in selectedIds,
                )
            }
        }
    }
}
