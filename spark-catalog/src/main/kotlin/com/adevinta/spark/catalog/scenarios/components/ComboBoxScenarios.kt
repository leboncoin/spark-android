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
@file:OptIn(kotlinx.coroutines.FlowPreview::class)

package com.adevinta.spark.catalog.scenarios.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.menu.NoContentItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration.Companion.milliseconds

internal val comboBoxScenarios: Map<String, @Composable () -> Unit> = mapOf(
    "combobox-filter" to { ComboBoxFilterScenario() },
)

/** Real input: type to filter, then select an option. Driven by UiAutomator. */
@Composable
private fun ComboBoxFilterScenario() {
    val books = remember {
        listOf(
            "To Kill a Mockingbird", "War and Peace", "The Idiot",
            "A Picture of Dorian Gray", "1984", "Pride and Prejudice",
            "Brave New World", "Crime and Punishment", "The Great Gatsby", "Moby Dick",
        )
    }
    val state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val filtered by remember(query) {
        derivedStateOf {
            if (query.isBlank()) books else books.filter { it.contains(query, ignoreCase = true) }
        }
    }
    LaunchedEffect(state) {
        snapshotFlow { state.text }
            .debounce(100.milliseconds)
            .collect { query = it.toString() }
    }
    SingleChoiceComboBox(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        label = "Search books",
        placeholder = "Type to search...",
        dropdownContent = {
            if (filtered.isEmpty()) {
                NoContentItem(text = "No books found")
            } else {
                filtered.forEach { title ->
                    DropdownMenuItem(
                        text = { Text(title) },
                        onClick = {
                            state.setTextAndPlaceCursorAtEnd(title)
                            expanded = false
                        },
                        selected = title == state.text.toString(),
                    )
                }
            }
        },
    )
}
