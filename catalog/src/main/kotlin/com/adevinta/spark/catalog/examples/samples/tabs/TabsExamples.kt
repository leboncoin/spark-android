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
package com.adevinta.spark.catalog.examples.samples.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.badge.Badge
import com.adevinta.spark.components.tab.Tab
import com.adevinta.spark.components.tab.TabGroup
import com.adevinta.spark.icons.BellOnFill
import com.adevinta.spark.icons.BubbleTextOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.UserFill

private const val TabsExampleSourceUrl = "$SampleSourceUrl/TabExamples.kt"
public val TabsExamples: List<Example> = listOf(
    Example(
        id = "simple",
        name = "Simple tabs",
        description = "2 tabs displayed on screen",
        sourceUrl = TabsExampleSourceUrl,
    ) {
        TabSimpleSample()
    },
    Example(
        id = "badge",
        name = "Tabs with badge",
        description = "3 tabs and the middle one contains a badge",
        sourceUrl = TabsExampleSourceUrl,
    ) {
        TabWithBadgeSample()
    },
    Example(
        id = "scrollable",
        name = "Scrollable tabs",
        description = "Display 3 tabs with a long one that overflows to showcase the scrolling",
        sourceUrl = TabsExampleSourceUrl,
    ) {
        ScrollableTabsSample()
    },
    Example(
        id = "icon",
        name = "Icons tabs",
        description = "Tabs with no label, only icons",
        sourceUrl = TabsExampleSourceUrl,
    ) {
        IconsTabsSample()
    },
)

@Composable
private fun TabSimpleSample() {
    val tabs = mutableListOf(
        Pair("Home", null) to 0,
        Pair("Message", SparkIcons.BubbleTextOutline) to 0,
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    TabGroup(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, (tab, unread) ->
            Tab(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                enabled = true,
                icon = tab.second,
                text = tab.first,
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
}

@Composable
private fun TabWithBadgeSample() {
    val tabs = mutableListOf(
        Pair("Home", null) to 0,
        Pair("Message", SparkIcons.BubbleTextOutline) to 1,
        Pair("MIM", null) to 0,
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    TabGroup(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, (tab, unread) ->
            Tab(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                enabled = true,
                icon = tab.second,
                text = tab.first,
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
}

@Composable
private fun ScrollableTabsSample() {
    val tabs = mutableListOf(
        Pair("Home", null) to 0,
        Pair("Message", SparkIcons.BubbleTextOutline) to 1,
        Pair("Notifications", SparkIcons.BubbleTextOutline) to 0,
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    TabGroup(
        spacedEvenly = false,
        selectedTabIndex = selectedIndex,
    ) {
        tabs.forEachIndexed { index, (tab, unread) ->
            Tab(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                enabled = true,
                icon = tab.second,
                text = tab.first,
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
}

@Composable
private fun IconsTabsSample() {
    val tabs = mutableListOf(
        Triple(SparkIcons.BellOnFill, 0, "notifications"),
        Triple(SparkIcons.BubbleTextOutline, 1, "messages"),
        Triple(SparkIcons.UserFill, 0, "compte"),
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    TabGroup(
        selectedTabIndex = selectedIndex,
    ) {
        tabs.forEachIndexed { index, (tab, unread, contentDescription) ->
            Tab(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                enabled = true,
                icon = tab,
                text = null,
                contentDescription = contentDescription,
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
}
