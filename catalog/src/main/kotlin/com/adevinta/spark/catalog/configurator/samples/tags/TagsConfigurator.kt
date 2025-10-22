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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.icons.FilledTonalIconToggleButton
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.tags.TagIntent
import com.adevinta.spark.components.tags.TagOutlined
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.Dropdown
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.highlight

public val TagsConfigurator: Configurator = Configurator(
    id = "tag",
    name = "Tag",
    description = "Tag configuration",
    sourceUrl = "$SampleSourceUrl/TagSamples.kt",
) {
    TagSample()
}

@Suppress("DEPRECATION")
@Composable
private fun ColumnScope.TagSample() {
    var icon: SparkIcon? by remember { mutableStateOf(null) }
    var style by remember { mutableStateOf(TagStyle.Filled) }
    var intent by remember { mutableStateOf(TagIntent.Main) }
    var tagText by remember { mutableStateOf("Filled Tag") }

    ConfigedTag(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = style,
        tagText = tagText,
        intent = intent,
        icon = icon,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp),
    ) {
        Text(
            text = "With Icon",
            modifier = Modifier.weight(1f),
            style = SparkTheme.typography.body2.highlight,
        )
        FilledTonalIconToggleButton(
            checked = icon != null,
            onCheckedChange = {
                icon = if (it) SparkIcons.HeartFill else null
            },
        ) {
            Icon(
                sparkIcon = SparkIcons.HeartFill,
                contentDescription = null,
            )
        }
    }

    ButtonGroup(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
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
            intents.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        intent = it
                        expanded = false
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
    PreviewTheme { TagSample() }
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

private enum class TagStyle {
    Filled,
    Outlined,
    Tinted,
}
