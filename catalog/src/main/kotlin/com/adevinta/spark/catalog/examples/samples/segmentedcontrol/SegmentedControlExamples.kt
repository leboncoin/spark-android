/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.segmentedcontrol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.icons.SparkIcons
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val SegmentedControlExampleSourceUrl = "$SampleSourceUrl/SegmentedControlExamples.kt"

public val SegmentedControlExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "horizontal-basic",
        name = "Horizontal - Basic",
        description = "Basic horizontal segmented control with 2-4 segments",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        HorizontalBasicExample()
    },
    Example(
        id = "vertical-basic",
        name = "Vertical - Basic",
        description = "Basic vertical segmented control with 5-8 segments",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        VerticalBasicExample()
    },
    Example(
        id = "content-types",
        name = "Content Types",
        description = "Different segment content types: SingleLine, TwoLine, Icon, IconText, Number, Custom",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        ContentTypesExample()
    },
    Example(
        id = "with-title-link",
        name = "With Title and Link",
        description = "Segmented control with optional title and link",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        WithTitleAndLinkExample()
    },
    Example(
        id = "custom-colors",
        name = "Custom Colors - Energy Rating",
        description = "Using custom colors for value scales like energy ratings",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        CustomColorsExample()
    },
    Example(
        id = "real-world-filter",
        name = "Real-world: Filter",
        description = "Example of using segmented control for filtering content",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        FilterExample()
    },
)

@Composable
private fun HorizontalBasicExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("2 Segments")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex1,
            onSegmentSelect = { selectedIndex1 = it },
        ) {
            SingleLine("Day")
            SingleLine("Week")
        }

        Text("3 Segments")
        var selectedIndex2 by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
            onSegmentSelect = { selectedIndex2 = it },
        ) {
            SingleLine("Option 1")
            SingleLine("Option 2")
            SingleLine("Option 3")
        }

        Text("4 Segments")
        var selectedIndex3 by remember { mutableIntStateOf(2) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex3,
            onSegmentSelect = { selectedIndex3 = it },
        ) {
            SingleLine("All")
            SingleLine("Active")
            SingleLine("Pending")
            SingleLine("Done")
        }
    }
}

@Composable
private fun VerticalBasicExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("5 Segments")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex1,
            onSegmentSelect = { selectedIndex1 = it },
        ) {
            SingleLine("Option 1")
            SingleLine("Option 2")
            SingleLine("Option 3")
            SingleLine("Option 4")
            SingleLine("Option 5")
        }

        Text("8 Segments")
        var selectedIndex2 by remember { mutableIntStateOf(4) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex2,
            onSegmentSelect = { selectedIndex2 = it },
        ) {
            SingleLine("1")
            SingleLine("2")
            SingleLine("3")
            SingleLine("4")
            SingleLine("5")
            SingleLine("6")
            SingleLine("7")
            SingleLine("8")
        }
    }
}

@Composable
private fun ContentTypesExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Mixed Content Types")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex1,
            onSegmentSelect = { selectedIndex1 = it },
        ) {
            SingleLine("Text")
            TwoLine("Title", "Subtitle")
            Icon(SparkIcons.ShoppingCartOutline)
            IconText(SparkIcons.ShoppingCartOutline, "Cart")
        }

        Text("Numbers")
        var selectedIndex2 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
            onSegmentSelect = { selectedIndex2 = it },
        ) {
            Number(1)
            Number(2)
            Number(3)
            Number(4)
        }

        Text("Custom Content")
        var selectedIndex3 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex3,
            onSegmentSelect = { selectedIndex3 = it },
        ) {
            SingleLine("Standard")
            Custom(selectedBackgroundColor = Color(0xFF4CAF50)) {
                Column {
                    Text("Premium")
                    Text("★", style = com.adevinta.spark.SparkTheme.typography.caption)
                }
            }
        }
    }
}

@Composable
private fun WithTitleAndLinkExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var selectedIndex by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex,
            onSegmentSelect = { selectedIndex = it },
            title = "Filter",
            linkText = "Learn more",
            onLinkClick = { },
        ) {
            SingleLine("All")
            SingleLine("Active")
            SingleLine("Completed")
        }

        var selectedIndex2 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
            onSegmentSelect = { selectedIndex2 = it },
            title = "View Mode",
        ) {
            SingleLine("List")
            SingleLine("Grid")
            SingleLine("Map")
        }
    }
}

@Composable
private fun CustomColorsExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Energy Rating Scale")
        listOf(
            Color(0xFF4CAF50), // A - Green
            Color(0xFF8BC34A), // B - Light Green
            Color(0xFFCDDC39), // C - Yellow Green
            Color(0xFFFFEB3B), // D - Yellow
            Color(0xFFFFC107), // E - Amber
            Color(0xFFFF9800), // F - Orange
            Color(0xFFF44336), // G - Red
        )

        var selectedIndex by remember { mutableIntStateOf(3) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex,
            onSegmentSelect = { selectedIndex = it },
        ) {
            SingleLine("A")
            SingleLine("B")
            SingleLine("C")
            SingleLine("D")
            SingleLine("E")
            SingleLine("F")
            SingleLine("G")
        }
    }
}

@Composable
private fun FilterExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Content Filter")
        var selectedIndex by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex,
            onSegmentSelect = { selectedIndex = it },
            title = "Show",
        ) {
            SingleLine("All")
            SingleLine("Active")
            SingleLine("Archived")
        }

        Text("Sort By")
        var selectedSortIndex by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedSortIndex,
            onSegmentSelect = { selectedSortIndex = it },
        ) {
            IconText(SparkIcons.ShoppingCartOutline, "Price")
            IconText(SparkIcons.ShoppingCartOutline, "Date")
            IconText(SparkIcons.ShoppingCartOutline, "Rating")
        }
    }
}
