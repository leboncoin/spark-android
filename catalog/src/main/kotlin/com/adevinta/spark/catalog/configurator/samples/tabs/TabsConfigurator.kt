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
package com.adevinta.spark.catalog.configurator.samples.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.badge.Badge
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.tab.Tab
import com.adevinta.spark.components.tab.TabGroup
import com.adevinta.spark.components.tab.TabIntent
import com.adevinta.spark.components.tab.TabSize
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.SingleChoiceDropdown
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.MessageOutline
import com.adevinta.spark.icons.Minus
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.Order
import com.adevinta.spark.tokens.bodyWidth
import com.adevinta.spark.tokens.highlight
import kotlin.random.Random
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

public val TabsConfigurator: Configurator = Configurator(
    id = "tab",
    name = "Tab",
    description = "Tab configuration",
    sourceUrl = "$SampleSourceUrl/TabSamples.kt",
) {
    TabSample()
}

@Composable
private fun ColumnScope.TabSample() {
    var isEnabled by remember { mutableStateOf(true) }
    var isTabsSpacedEvenely by remember { mutableStateOf(true) }
    var tabSize by remember { mutableStateOf(TabSize.Medium) }
    var intent by remember { mutableStateOf(TabIntent.Main) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    val defaultShape = SparkTheme.shapes.large
    var selectedShape by remember { mutableStateOf(defaultShape) }
    var shapeDropdownExpanded by remember { mutableStateOf(false) }

    val tabs =
        remember { mutableStateListOf(Pair("Home", null) to 0, Pair("Message", SparkIcons.MessageOutline) to 120) }

    TabGroup(
        spacedEvenly = isTabsSpacedEvenely,
        modifier = Modifier.bodyWidth(),
        selectedTabIndex = selectedIndex,
        intent = intent,
    ) {
        tabs.forEachIndexed { index, (tab, unread) ->
            Tab(
                intent = intent,
                text = tab.first,
                icon = tab.second,
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                enabled = isEnabled,
                size = tabSize,
                shape = selectedShape,
                trailingContent = {
                    if (unread > 0) {
                        Badge(count = unread)
                    } else {
                        Unit
                    }
                },
            )
        }
    }

    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = isTabsSpacedEvenely,
        onCheckedChange = { isTabsSpacedEvenely = it },
    ) {
        Text(
            text = "Spaced Evenly",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.configurator_component_screen_intent_label),
        selectedOption = intent,
        onOptionSelect = {
            intent = it
        },
    )

    ButtonGroup(
        title = "Sizes",
        selectedOption = tabSize,
        onOptionSelect = { tabSize = it },
    )

    SingleChoiceDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = selectedShape.propertyName(),
        label = "Shape",
        expanded = shapeDropdownExpanded,
        onExpandedChange = { shapeDropdownExpanded = !shapeDropdownExpanded },
        onDismissRequest = { shapeDropdownExpanded = false },
        dropdownContent = {
            shapes.forEach { shape ->
                DropdownMenuItem(
                    text = { Text(shape.propertyName()) },
                    onClick = {
                        selectedShape = shape
                        shapeDropdownExpanded = false
                    },
                    selected = selectedShape == shape,
                )
            }
        },
    )
    Column {
        Text(
            text = "Number of tabs",
            modifier = Modifier.padding(bottom = 8.dp),
            style = SparkTheme.typography.body2.highlight,
        )
        Row {
            Spacer(modifier = Modifier.padding(start = 16.dp))
            IconButtonFilled(
                icon = SparkIcons.Minus,
                onClick = {
                    if (tabs.size > 1) {
                        tabs.removeAt(tabs.lastIndex)
                    }
                },
            )
            Spacer(modifier = Modifier.padding(start = 16.dp))

            IconButtonFilled(
                icon = SparkIcons.Plus,
                onClick = {
                    val randomString = ('A'..'z').map { it }.shuffled().subList(0, 5).joinToString("")
                    tabs.add(Pair(randomString, null) to Random.nextInt(3))
                },
            )
        }
    }
}

private val shapes: List<CornerBasedShape>
    @Composable
    get() = listOf(
        SparkTheme.shapes.none,
        SparkTheme.shapes.extraSmall,
        SparkTheme.shapes.small,
        SparkTheme.shapes.medium,
        SparkTheme.shapes.large,
        SparkTheme.shapes.extraLarge,
        SparkTheme.shapes.full,
    )

@Suppress("ReplaceGetOrSet")
@Composable
private fun CornerBasedShape.propertyName(): String = SparkTheme.shapes::class
    .declaredMemberProperties
    .sortedBy { it.findAnnotation<Order>()?.value ?: Int.MAX_VALUE }
    .get(shapes.indexOf(this))
    .name

@Preview
@Composable
private fun TabSamplePreview() {
    PreviewTheme { TabSample() }
}
