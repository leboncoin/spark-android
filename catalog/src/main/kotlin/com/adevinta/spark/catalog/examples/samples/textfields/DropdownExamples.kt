/*
 * Copyright (c) 2023-2025 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.textfields

import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuGroupItem
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.tags.TagIntent
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.Dropdown
import com.adevinta.spark.tokens.highlight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.res.stringResource
import com.adevinta.spark.catalog.R

private const val DropdownsExampleSourceUrl = "$SampleSourceUrl/DropdownExamples.kt"

private data class DropdownExampleGroup(val name: Int, val books: ImmutableList<Int>)

private val DropdownStubData = persistentListOf(
    DropdownExampleGroup(
        R.string.dropdown_example_group_best_sellers,
        persistentListOf(
            R.string.book_title_mockingbird,
            R.string.book_title_war_peace,
            R.string.book_title_idiot
        )
    ),
    DropdownExampleGroup(
        R.string.dropdown_example_group_novelties,
        persistentListOf(
            R.string.book_title_dorian_gray,
            R.string.book_title_1984,
            R.string.book_title_pride_prejudice
        )
    ),
)

public val DropdownsExamples: List<Example> = listOf(
    Example(
        id = "single-select",
        name = R.string.dropdown_example_single_select_title,
        description = R.string.dropdown_example_single_select_description,
        sourceUrl = DropdownsExampleSourceUrl,
    ) {
        SingleSelectDropdown()
    },
    Example(
        id = "multi-select",
        name = R.string.dropdown_example_multi_select_title,
        description = R.string.dropdown_example_multi_select_description,
        sourceUrl = DropdownsExampleSourceUrl,
    ) {
        MultiSelectDropdown()
    },
    Example(
        id = "custom-item",
        name = R.string.dropdown_example_custom_item_title,
        description = R.string.dropdown_example_custom_item_description,
        sourceUrl = DropdownsExampleSourceUrl,
    ) {
        CustomItemsDropdown()
    },
)

@Composable
private fun SingleSelectDropdown() {
    val singleSelectionFilter by remember {
        mutableStateOf(DropdownStubData)
    }

    var singleSelected: Int by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }
    Dropdown(
        modifier = Modifier.fillMaxWidth(),
        value = singleSelected,
        label = "Book",
        placeholder = "Pick a Book",
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        onDismissRequest = {
            expanded = false
        },
        dropdownContent = {
            singleSelectionFilter.forEach { (groupName, books) ->
                DropdownMenuGroupItem(
                    title = {
                        Text(stringResource(groupName))
                    },
                ) {
                    books.forEach { book ->
                        DropdownMenuItem(
                            text = { Text(stringResource(book)) },
                            onClick = {
                                singleSelected = book
                                expanded = false
                            },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun MultiSelectDropdown() {
    val multiSelectionFilter by remember {
        mutableStateOf(DropdownStubData)
    }

    var selectedItems by remember {
        mutableStateOf(
            listOf(
                "To Kill a Mockingbird",
                "War and Peace",
            ),
        )
    }
    val multiSelectedValues by remember(selectedItems.size) {
        derivedStateOf {
            val suffix = if (selectedItems.size > 1) ", +${selectedItems.size - 1}" else ""
            selectedItems.firstOrNull().orEmpty() + suffix
        }
    }
    var expanded by remember { mutableStateOf(false) }
    Dropdown(
        modifier = Modifier.fillMaxWidth(),
        value = multiSelectedValues,
        label = "Book",
        placeholder = "Pick a Book",
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        onDismissRequest = {
            expanded = false
        },
        dropdownContent = {
            multiSelectionFilter.forEach { (groupName, books) ->
                DropdownMenuGroupItem(
                    title = {
                        Text(stringResource(groupName))
                    },
                ) {
                    books.forEach { book ->
                        // This should be part of your model otherwise it's a huge work that done on
                        // each items but we're simplifying things since it's an example here.
                        val isSelected = book in selectedItems
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(book),
                                    style = if (isSelected) {
                                        SparkTheme.typography.body1.highlight
                                    } else {
                                        SparkTheme.typography.body1
                                    },
                                )
                            },
                            onClick = {
                                selectedItems = if (book in selectedItems) {
                                    selectedItems - book
                                } else {
                                    selectedItems + book
                                }
                                // Here we want to have the dropdown to stay expanded when we multiSelect but
                                // you could use the line below if in the case you want to have the same behaviour
                                // as the single selection.
                                // expanded = false
                            },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun CustomItemsDropdown() {
    val singleSelectionFilter by remember {
        mutableStateOf(DropdownStubData)
    }

    var singleSelected by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Dropdown(
        modifier = Modifier.fillMaxWidth(),
        value = singleSelected,
        label = "Book",
        placeholder = "Pick a Book",
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        onDismissRequest = {
            expanded = false
        },
        dropdownContent = {
            singleSelectionFilter.forEach { (groupName, books) ->
                DropdownMenuGroupItem(
                    title = {
                        Text(
                            text = stringResource(groupName),
                            style = SparkTheme.typography.body1.highlight,
                        )
                    },
                ) {
                    books.forEach { book ->
                        DropdownMenuItem(
                            text = { Text(stringResource(book)) },
                            onClick = {
                                singleSelected = book
                                expanded = false
                            },
                            trailingIcon = {
                                TagFilled(text = "New", intent = TagIntent.Basic)
                            },
                        )
                    }
                }
            }
        },
    )
}
