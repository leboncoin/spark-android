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
package com.adevinta.spark.catalog.configurator.samples.tags

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.icons.IconPickerItem
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.tags.TagIntent
import com.adevinta.spark.components.tags.TagOutlined
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.Dropdown
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.icons.SparkIcon
import kotlinx.coroutines.launch

public val TagsConfigurator: Configurator = Configurator(
    id = "tag",
    name = "Tag",
    description = "Tag configuration",
    sourceUrl = "$SampleSourceUrl/TagSamples.kt",
) { snackbarHostState, _ ->
    TagSample(snackbarHostState)
}

@Suppress("DEPRECATION")
@Composable
private fun ColumnScope.TagSample(snackbarHostState: SnackbarHostState) {
    var icon: SparkIcon? by remember { mutableStateOf(null) }
    var style by remember { mutableStateOf(TagStyle.Filled) }
    var intent by remember { mutableStateOf(TagIntent.Main) }
    var tagText by remember { mutableStateOf("Filled Tag") }
    val coroutineScope = rememberCoroutineScope()

    ConfigedTag(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = style,
        tagText = tagText,
        intent = intent,
        icon = icon,
    )

    IconPickerItem(
        label = "With Icon",
        selectedIcon = icon,
        onIconSelected = { icon = it },
    )

    ButtonGroup(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { newStyle ->
            // Check if current intent is invalid for new style
            if (intent == TagIntent.Surface && newStyle != TagStyle.Filled) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Tag Intent Surface can only be used with TagFilled",
                        intent = SnackbarIntent.Error,
                    )
                }
                // Reset intent to a valid one
                intent = TagIntent.Main
            }
            style = newStyle
        },
    )

    val intents = TagIntent.entries
    var expanded by remember { mutableStateOf(false) }
    Dropdown(
        modifier = Modifier.fillMaxWidth(),
        value = intent.name,
        label = "Intent",
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        onDismissRequest = {
            expanded = false
        },
        dropdownContent = {
            intents.forEach { newIntent ->
                DropdownMenuItem(
                    text = { Text(newIntent.name) },
                    onClick = {
                        // Check if the selected intent is invalid for current style
                        if (newIntent == TagIntent.Surface && style != TagStyle.Filled) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Tag Intent Surface can only be used with TagFilled",
                                    intent = SnackbarIntent.Error,
                                )
                            }
                            expanded = false
                        } else {
                            intent = newIntent
                            expanded = false
                        }
                    },
                )
            }
        },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = tagText,
        onValueChange = { tagText = it },
        label = "Tag text",
        placeholder = "Vérifier les Disponibilité",
    )
}

@Preview
@Composable
private fun TagSamplePreview() {
    PreviewTheme { TagSample(SnackbarHostState()) }
}

@Composable
private fun ConfigedTag(
    modifier: Modifier = Modifier,
    style: TagStyle,
    tagText: String,
    intent: TagIntent,
    icon: SparkIcon?,
) {
    Surface(
        modifier = modifier,
        color = SparkTheme.colors.backgroundVariant,
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
        ) {
            // Only render if the combination is valid
            if (intent != TagIntent.Surface || style == TagStyle.Filled) {
                when (style) {
                    TagStyle.Filled -> TagFilled(
                        text = tagText,
                        intent = intent,
                        leadingIcon = icon,
                    )

                    TagStyle.Outlined -> TagOutlined(
                        text = tagText,
                        intent = intent,
                        leadingIcon = icon,
                    )

                    TagStyle.Tinted -> TagTinted(
                        text = tagText,
                        intent = intent,
                        leadingIcon = icon,
                    )
                }
            }
        }
    }
}

private enum class TagStyle {
    Filled,
    Outlined,
    Tinted,
}
