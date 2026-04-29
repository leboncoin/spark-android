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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.segmentedcontrol.LocalSegmentSelected
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlScope
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlShape
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val SegmentedControlExampleSourceUrl = "$SampleSourceUrl/SegmentedControlExamples.kt"

public val SegmentedControlExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "single-row",
        name = "Single Row (2-4 segments)",
        description = "Single-select segmented control with adaptive single-row layout",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        SingleRowExample()
    },
    Example(
        id = "multi-row",
        name = "Multi-Row (5-8 segments)",
        description = "Single-select segmented control with adaptive multi-row layout",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        MultiRowExample()
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
        id = "energy-rating",
        name = "Energy Rating Scale",
        description = "Using segmented control for value scales like energy ratings",
        sourceUrl = SegmentedControlExampleSourceUrl,
    ) {
        EnergyRatingExample()
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
private fun SingleRowExample() {
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
private fun MultiRowExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("5 Segments")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex1,
            onSegmentSelect = { selectedIndex1 = it },
            shape = SegmentedControlShape.Pill,
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
            shape = SegmentedControlShape.Pill,
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
            Icon(LeboncoinIcons.ShoppingCartOutline)
            IconText(LeboncoinIcons.ShoppingCartOutline, "Cart")
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
                    Text("★", style = SparkTheme.typography.caption)
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
private fun EnergyRatingExample() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Energy Rating Scale")
        var selectedIndex by remember { mutableIntStateOf(7) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex,
            onSegmentSelect = { selectedIndex = it },
            shape = SegmentedControlShape.Pill,
        ) {
            DPE(
                "A",
                color = Color(0xFF009424),
                contentColor = Color.White,
            )
            DPE(
                "B",
                color = Color(0xFF3ACC31),
                contentColor = Color.White,
            )
            DPE(
                "C",
                color = Color(0xFFCDFD32),
                contentColor = Color.Black,
            )
            DPE(
                "D",
                color = Color(0xFFFBEA49),
                contentColor = Color.Black,
            )
            DPE(
                "E",
                color = Color(0xFFFCCB2F),
                contentColor = Color.Black,
            )
            DPE(
                "F",
                color = Color(0xFFFB9C34),
                contentColor = Color.Black,
            )
            DPE(
                "G",
                color = Color(0xFFFA1C1F),
                contentColor = Color.White,
            )
            SingleLine("Vierge")
        }
    }
}

@Composable
private fun SegmentedControlScope.DPE(
    text: String,
    color: Color,
    contentColor: Color,
) {
    Custom(
        selectedBackgroundColor = color,
    ) {
        val selected = LocalSegmentSelected.current
        val labelColor by animateColorAsState(
            if (selected) contentColor else SparkTheme.colors.onSurface.dim1,
        )

        val labelProgress by animateFloatAsState(if (selected) 1f else 0f)
        val textStyle = lerp(
            SparkTheme.typography.body2,
            SparkTheme.typography.body2.highlight,
            labelProgress,
        )
        Text(text, color = labelColor, style = textStyle)
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
            IconText(LeboncoinIcons.ShoppingCartOutline, "Price")
            IconText(LeboncoinIcons.ShoppingCartOutline, "Date")
            IconText(LeboncoinIcons.ShoppingCartOutline, "Rating")
        }
    }
}
