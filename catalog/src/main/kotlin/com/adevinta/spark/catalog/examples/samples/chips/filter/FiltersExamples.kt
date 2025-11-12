/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.chips.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.SparkIcons
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private data class FilterName(val name: String, val content: ImmutableList<String>)

@Composable
internal fun ColumnScope.ChipFilter() {
    SingleFilter()
    VerticalSpacer(32.dp)
    Text(
        "Multiple Selection",
        style = SparkTheme.typography.headline2,
        color = SparkTheme.colors.mainVariant,
    )
    Text(
        "Multi Selection Filters can represent unions or intersections between multiple categories.",
        style = SparkTheme.typography.body2,
    )
    VerticalSpacer(16.dp)
    UnionFilter()
    VerticalSpacer(16.dp)
    InterFilter()
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SingleFilter() {
    val singleSelectionFilter by remember {
        mutableStateOf(
            persistentListOf(
                FilterName("Fruit", persistentListOf("🥝", "🍎", "🍇", "🍌")),
                FilterName("Vegetable", persistentListOf("🍆", "🧅", "🥔", "🌶", "🥕")),
            ),
        )
    }
    Text("Single selection", style = SparkTheme.typography.headline2, color = SparkTheme.colors.mainVariant)

    var singleSelected by remember { mutableStateOf("Fruit") }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
        modifier = Modifier.selectableGroup(),
    ) {
        singleSelectionFilter.forEach { filter ->
            ChipSelectable(
                modifier = Modifier.semantics {
                    role = Role.RadioButton
                },
                text = filter.name,
                selected = singleSelected == filter.name,
                onClick = { singleSelected = filter.name },
            )
        }
    }
    AnimatedContent(singleSelectionFilter.first { it.name == singleSelected }) {
        FlowRow(
            horizontalArrangement = spacedBy(8.dp),
        ) {
            it.content.forEach { content ->
                Text(text = content)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun UnionFilter() {
    Text(
        "Union",
        style = SparkTheme.typography.subhead,
        color = SparkTheme.colors.mainVariant,
    )
    Text(
        "The results shown on a union filter are all the elements that satisfy at least 1 of " +
            "the selected filters.",
        style = SparkTheme.typography.body2,
    )

    val unionSelectionFilter by remember {
        mutableStateOf(
            persistentListOf(
                FilterName("Animal", persistentListOf("🦍", "🦒", "🦌", "🐄", "🐕", "🐬")),
                FilterName("Flower", persistentListOf("🌺", "🌼", "🌻", "🪷")),
                FilterName("Tree", persistentListOf("🌳", "🌴", "🌲")),
            ),
        )
    }
    var unionSelected by remember { mutableStateOf(listOf("Animal", "Tree")) }
    FlowRow(
        modifier = Modifier.selectableGroup(),
        horizontalArrangement = spacedBy(8.dp),
    ) {
        unionSelectionFilter.forEach { filter ->
            val selected = filter.name in unionSelected
            ChipSelectable(
                text = filter.name,
                selected = selected,
                leadingIcon = if (selected) SparkIcons.Check else null,
                onClick = {
                    unionSelected = if (selected) {
                        unionSelected - filter.name
                    } else {
                        unionSelected + filter.name
                    }
                },
            )
        }
    }
    val multipleUnionSet = unionSelectionFilter.filter {
        it.name in unionSelected
    }.ifEmpty {
        unionSelectionFilter
    }.flatMap {
        it.content
    }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        multipleUnionSet.forEach { content ->
            Text(text = content)
        }
    }
    Text(
        "• When there is no element selected (no filter applied) all the results are shown.",
        style = SparkTheme.typography.body2,
    )
    Text(
        "• When all the elements are selected all the results are shown.\n",
        style = SparkTheme.typography.body2,
    )
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun InterFilter() {
    Text(
        "Intersection",
        style = SparkTheme.typography.subhead,
        color = SparkTheme.colors.mainVariant,
    )
    Text(
        "The results shown on a union filter are all the elements that satisfy all the selected filters.",
        style = SparkTheme.typography.body2,
    )
    val interSelectionFilter by remember {
        mutableStateOf(
            persistentListOf(
                FilterName(
                    "Land",
                    persistentListOf(
                        "🦍", "🦒", "🦌", "🐄", "🐕", "🐓", "🐿️", "🦧", "🦭", "🦆", "🐞", "🐢", "🦜",
                    ),
                ),
                FilterName(
                    "Sea",
                    persistentListOf("🐬", "🐳", "🦈", "🦭", "🦆", "🐡", "🐟", "🐢"),
                ),
                FilterName("Air", persistentListOf("🦆", "🐞", "🦜")),
                FilterName(
                    "Wild",
                    persistentListOf(
                        "🦍", "🦒", "🦌", "🐬", "🐿️", "🦧", "🐳", "🦈", "🦭", "🦆", "🐡", "🐟",
                        "🐞", "🐢", "🦜",
                    ),
                ),
                FilterName(
                    "Domestic",
                    persistentListOf("🐄", "🐕", "🐓", "🐢", "🦜"),
                ),
            ),
        )
    }
    var interSelected by remember { mutableStateOf(listOf("Land", "Wild")) }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        interSelectionFilter.forEach { filter ->
            val selected = filter.name in interSelected
            ChipSelectable(
                text = filter.name,
                selected = selected,
                leadingIcon = if (selected) SparkIcons.Check else null,
                onClick = {
                    interSelected = if (selected) {
                        interSelected - filter.name
                    } else {
                        interSelected + filter.name
                    }
                },
            )
        }
    }
    val multipleInterSet = if (interSelected.isEmpty()) {
        interSelectionFilter.flatMap { it.content }.toSet()
    } else {
        interSelectionFilter.filter {
            it.name in interSelected
        }.map {
            it.content.toSet()
        }.reduce { acc, contents ->
            acc.intersect(contents)
        }
    }
    FlowRow(
        horizontalArrangement = spacedBy(8.dp),
    ) {
        multipleInterSet.forEach { content ->
            Text(text = content)
        }
    }
    Text(
        "• When there is no element selected (no filter applied), all the results are shown.",
        style = SparkTheme.typography.body2,
    )
    Text(
        "• When all the elements are selected only shows the results that satisfy all the " +
            "filters (normally none of them).",
        style = SparkTheme.typography.body2,
    )
}
