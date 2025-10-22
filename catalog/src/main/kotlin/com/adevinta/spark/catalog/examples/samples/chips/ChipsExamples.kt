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
@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)

package com.adevinta.spark.catalog.examples.samples.chips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.examples.samples.chips.filter.ChipFilter
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.chips.Chip
import com.adevinta.spark.components.chips.ChipIntent
import com.adevinta.spark.components.chips.ChipOutlined
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.components.chips.ChipStyles
import com.adevinta.spark.components.chips.ChipStyles.Dashed
import com.adevinta.spark.components.chips.ChipStyles.Tinted
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenu
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.icons.CalendarTextOutline
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.ChevronHorizontalUp
import com.adevinta.spark.icons.EyeOutline
import com.adevinta.spark.icons.LetterOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.UserFill
import com.adevinta.spark.tokens.ElevationTokens
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider

private const val ChipsExampleSourceUrl = "$SampleSourceUrl/ChipsSamples.kt"

public val ChipsExamples: List<Example> = listOf(
    Example(
        id = "default",
        name = "Default",
        description = "Step indicator content defaults to step index, or checkmark icon when completed.",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipsDefault()
    },
    Example(
        id = "kind",
        name = "Kind",
        description = "",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipsKind()
    },
    Example(
        id = "intent",
        name = "Intent",
        description = "All intents are available",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipsIntent()
    },
    Example(
        id = "selectable",
        name = "Intent Selectable",
        description = "All intents are available and selectable",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipSelectablesIntent()
    },
    Example(
        id = "assist",
        name = "Assist",
        description = "Assist chips represent smart or automated actions that can span multiple apps, such as " +
            "opening a calendar event from the home screen.\n\n" +
            "Assist chips function as though the user asked an assistant to complete the action. They " +
            "should appear dynamically and contextually in a UI.\n\n" +
            "An alternative to assist chips is buttons, which should appear persistently and consistently.\n",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipAssist()
    },
    Example(
        id = "filter",
        name = "Filter",
        description = "Filter chips represent filters for a collection. They use tags or descriptive words to filter " +
            "content. They can be a good alternative to toggle buttons or checkboxes.\n" +
            "\n" +
            "Tapping on a filter chip activates it and appends a leading checkmark icon to the starting " +
            "edge of the chip label.",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipFilter()
    },
    Example(
        id = "input",
        name = "Input",
        description = "Input chips represent discrete pieces of information entered by a user, such as Gmail " +
            "contacts or filter options within a search field.\n" +
            "They enable user input and verify that input by converting text into chips.",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipInput()
    },
    Example(
        id = "suggestion",
        name = "Suggestion",
        description = "Suggestion chips help narrow a user’s intent by presenting dynamically generated suggestions," +
            " such as possible responses or search filters.",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipSuggestion()
    },
    Example(
        id = "selection",
        name = "Selection",
        description = "Chips can also add other extra rare usages like acting as a Select combined with the popover.",
        sourceUrl = ChipsExampleSourceUrl,
    ) {
        ChipSelection()
    },
)

@Preview
@Composable
private fun ColumnScope.ChipsDefault() {
    ChipOutlined(text = "default chip")
}

@Composable
@Preview
private fun ColumnScope.ChipsKind() {
    var selected by remember { mutableStateOf(true) }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        Chip(text = "Assist", leadingIcon = SparkIcons.CalendarTextOutline, onClick = {})
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

@Composable
@Preview
private fun ColumnScope.ChipsIntent() {
    ChipStyles.entries.forEach { style ->
        FlowRow(
            horizontalArrangement = spacedBy(8.dp),
        ) {
            ChipIntent.entries.forEach {
                Chip(
                    text = it.name,
                    onClick = {},
                    style = style,
                    intent = it,
                    onClose = { },
                    onCloseLabel = "Close",
                )
            }
        }
    }
}

@Composable
@Preview
private fun ColumnScope.ChipSelectablesIntent() {
    ChipStyles.entries.forEach { style ->
        FlowRow(
            horizontalArrangement = spacedBy(8.dp),
        ) {
            ChipIntent.entries.forEach {
                var selected by remember { mutableStateOf(false) }
                ChipSelectable(
                    text = it.name,
                    selected = selected,
                    onClick = { selected = !selected },
                    style = style,
                    intent = it,
                )
            }
        }
    }
}

@Composable
@Preview
private fun ColumnScope.ChipAssist() {
    val nowDate by remember {
        mutableStateOf(LocalDate.now())
    }
    Surface(
        modifier = Modifier.widthIn(max = 320.dp),
        color = SparkTheme.colors.main,
        border = BorderStroke(2.dp, SparkTheme.colors.onMain),
        shape = SparkTheme.shapes.small,
        elevation = ElevationTokens.Level1,
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(SparkTheme.colors.main, SparkTheme.colors.supportVariant),
                        center = Offset(0f, 0f),
                        radius = 1000f,
                    ),
                    shape = SparkTheme.shapes.medium,
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.heightIn(min = 128.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Passed event".uppercase(),
                    style = SparkTheme.typography.small,
                    fontFamily = FontFamily.Monospace,
                )

                Column {
                    Text(
                        text = "Google I/O ${nowDate.year}",
                        style = SparkTheme.typography.headline1,
                    )
                    Text(
                        text = "google for Developers",
                        style = SparkTheme.typography.body1,
                    )
                }
            }

            MaterialHorizontalDivider(color = LocalContentColor.current)
            Column {
                Text(
                    text = nowDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                    style = SparkTheme.typography.small,
                )
                FlowRow(
                    horizontalArrangement = spacedBy(8.dp),
                ) {
                    Chip(
                        text = "Add to Calendar",
                        leadingIcon = SparkIcons.CalendarTextOutline,
                        onClick = {},
                        intent = ChipIntent.Surface,
                    )
                    Chip(
                        text = "Join the meet",
                        leadingIcon = SparkIcons.EyeOutline,
                        onClick = {},
                        intent = ChipIntent.Surface,
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ChipInput() {
    var value by remember { mutableStateOf("Third") }
    var tags by remember { mutableStateOf(listOf("First", "Second")) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value = it },
        label = "Value",
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Go,
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                if (value.isNotBlank() && value !in tags) {
                    tags = tags + value
                    value = ""
                }
            },
        ),
    )

    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        tags.forEach { input ->
            Chip(
                text = input,
                style = Dashed,
                intent = ChipIntent.Neutral,
                onClick = {},
                onClose = { tags = tags - input },
                onCloseLabel = "Remove $input from the list",
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ChipSuggestion() {
    var value by remember { mutableStateOf("") }
    var focused by remember { mutableStateOf(false) }
    var showSuggestion by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = focused) {
        if (!focused) {
            delay(200)
            showSuggestion = false
        }
    }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) {
                    showSuggestion = true
                    focused = true
                } else {
                    focused = false
                }
            },
        value = value,
        onValueChange = { value = it },
        label = "From",
        leadingContent = {
            TextFieldIcon(
                icon = SparkIcons.LetterOutline,
                contentDescription = null,
            )
        },
    )

    AnimatedVisibility(
        visible = showSuggestion,
    ) {
        FlowRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = spacedBy(8.dp),
        ) {
            Chip(
                text = "john.doe@email.com",
                style = if (value == "john.doe@email.com") Tinted else Dashed,
                intent = ChipIntent.Neutral,
                leadingIcon = SparkIcons.UserFill,
                onClick = {
                    value = "john.doe@email.com"
                },
            )
            Chip(
                text = "jane.doe@email.com",
                style = if (value == "jane.doe@email.com") Tinted else Dashed,
                intent = ChipIntent.Neutral,
                leadingIcon = SparkIcons.UserFill,
                onClick = {
                    value = "jane.doe@email.com"
                },
            )
        }
    }
    SelectionContainer {
        Text(
            text = "Suggestion chips can offer quick-reply options in a chat or email app, or It can help " +
                "the user start a search.",
        )
    }
}

@Composable
private fun ChipSelection() {
    var value by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val animals by remember { mutableStateOf(listOf("Cat", "Dog", "Bird", "Snake")) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart),
    ) {
        ChipSelectable(
            onClick = {
                expanded = true
            },
            selected = value in animals,
            onClose = if (value in animals) {
                {
                    value = "animals"
                }
            } else {
                null
            },
            onCloseLabel = "Remove filter",
        ) {
            Text(text = value.ifEmpty { "Animals" })
            Icon(
                sparkIcon = SparkIcons.ChevronHorizontalUp,
                modifier = Modifier.size(12.dp),
                contentDescription = null,
                tint = LocalContentColor.current,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            animals.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = { value = it },
                )
            }
        }
    }
}
