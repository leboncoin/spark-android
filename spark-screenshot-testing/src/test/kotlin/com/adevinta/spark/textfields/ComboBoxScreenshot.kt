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
package com.adevinta.spark.textfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SelectedChoice
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class ComboBoxScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Phone,
        renderingMode = RenderingMode.NORMAL,

    )

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun singleChoice() {
        paparazzi.sparkSnapshot {
            FlowColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SingleChoiceComboBox(
                    modifier = Modifier.fillMaxWidth(),
                    state = rememberTextFieldState(),
                    expanded = false,
                    onExpandedChange = { },
                    onDismissRequest = { },
                    enabled = true,
                    required = true,
                    label = "Select a book",
                    placeholder = "Choose a book...",
                    helper = "You can search and select a book",
                ) {
                    comboBoxSampleValues.forEachIndexed { index, book ->
                        DropdownMenuItem(
                            text = { Text(book.title) },
                            onClick = {},
                            selected = index == 2,
                        )
                    }
                }

                SingleChoiceComboBox(
                    modifier = Modifier.fillMaxWidth(),
                    state = rememberTextFieldState(),
                    expanded = true,
                    onExpandedChange = { },
                    onDismissRequest = {},
                    enabled = true,
                    required = true,
                    label = "Select a book",
                    placeholder = "Choose a book...",
                    helper = "You can search and select a book",
                ) {
                    comboBoxSampleValues.forEachIndexed { index, book ->
                        DropdownMenuItem(
                            text = { Text(book.title) },
                            onClick = {},
                            selected = index == 2,
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun multiChoice() {
        paparazzi.sparkSnapshot {
            FlowColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                var selectedChoices by remember {
                    mutableStateOf(
                        persistentListOf(
                            SelectedChoice("1", "To Kill a Mockingbird"),
                            SelectedChoice("2", "War and Peace"),
                        ),
                    )
                }

                MultiChoiceComboBox(
                    modifier = Modifier.fillMaxWidth(),
                    state = rememberTextFieldState(),
                    expanded = false,
                    onExpandedChange = { },
                    onDismissRequest = { },
                    selectedChoices = selectedChoices,
                    onSelectedClick = { id ->
                        selectedChoices = selectedChoices.filter { it.id != id }.toPersistentList()
                    },
                    enabled = true,
                    required = true,
                    label = "Select books",
                    placeholder = "Choose books...",
                    helper = "You can select multiple books",
                ) {
                    comboBoxSampleValues.forEachIndexed { index, book ->
                        DropdownMenuItem(
                            text = { Text(book.title) },
                            onCheckedChange = {
                                val choice = SelectedChoice(index.toString(), book.title)
                                selectedChoices = if (selectedChoices.any { it.id == choice.id }) {
                                    selectedChoices.filter { it.id != choice.id }.toPersistentList()
                                } else {
                                    selectedChoices.add(choice)
                                }
                            },
                            checked = selectedChoices.any { it.label == book.title },
                        )
                    }
                }

                MultiChoiceComboBox(
                    modifier = Modifier.fillMaxWidth(),
                    state = rememberTextFieldState(),
                    expanded = true,
                    onExpandedChange = { },
                    onDismissRequest = { },
                    selectedChoices = selectedChoices,
                    onSelectedClick = { id ->
                        selectedChoices = selectedChoices.filter { it.id != id }.toPersistentList()
                    },
                    enabled = true,
                    required = true,
                    label = "Select books",
                    placeholder = "Choose books...",
                    helper = "You can select multiple books",
                ) {
                    comboBoxSampleValues.forEachIndexed { index, book ->
                        DropdownMenuItem(
                            text = { Text(book.title) },
                            onCheckedChange = {
                                val choice = SelectedChoice(index.toString(), book.title)
                                selectedChoices = if (selectedChoices.any { it.id == choice.id }) {
                                    selectedChoices.filter { it.id != choice.id }.toPersistentList()
                                } else {
                                    selectedChoices.add(choice)
                                }
                            },
                            checked = selectedChoices.any { it.label == book.title },
                        )
                    }
                }
            }
        }
    }

    companion object {
        private val comboBoxSampleValues = listOf(
            Book(1, "To Kill a Mockingbird"),
            Book(2, "War and Peace"),
            Book(3, "The Idiot"),
            Book(4, "A Picture of Dorian Gray"),
            Book(5, "1984"),
            Book(6, "Pride and Prejudice"),
        )
    }
}

private data class Book(val id: Int, val title: String)
