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

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlScope
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlShape
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tokens.transparent
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
        ) {
            SingleLine("Day", selected = selectedIndex1 == 0, onClick = { selectedIndex1 = 0 })
            SingleLine("Week", selected = selectedIndex1 == 1, onClick = { selectedIndex1 = 1 })
        }

        Text("3 Segments")
        var selectedIndex2 by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
        ) {
            SingleLine("Option 1", selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            SingleLine("Option 2", selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
            SingleLine("Option 3", selected = selectedIndex2 == 2, onClick = { selectedIndex2 = 2 })
        }

        Text("4 Segments")
        var selectedIndex3 by remember { mutableIntStateOf(2) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex3,
        ) {
            SingleLine("All", selected = selectedIndex3 == 0, onClick = { selectedIndex3 = 0 })
            SingleLine("Active", selected = selectedIndex3 == 1, onClick = { selectedIndex3 = 1 })
            SingleLine("Pending", selected = selectedIndex3 == 2, onClick = { selectedIndex3 = 2 })
            SingleLine("Done", selected = selectedIndex3 == 3, onClick = { selectedIndex3 = 3 })
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
            shape = SegmentedControlShape.Pill,
        ) {
            SingleLine("Option 1", selected = selectedIndex1 == 0, onClick = { selectedIndex1 = 0 })
            SingleLine("Option 2", selected = selectedIndex1 == 1, onClick = { selectedIndex1 = 1 })
            SingleLine("Option 3", selected = selectedIndex1 == 2, onClick = { selectedIndex1 = 2 })
            SingleLine("Option 4", selected = selectedIndex1 == 3, onClick = { selectedIndex1 = 3 })
            SingleLine("Option 5", selected = selectedIndex1 == 4, onClick = { selectedIndex1 = 4 })
        }

        Text("8 Segments")
        var selectedIndex2 by remember { mutableIntStateOf(4) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex2,
            shape = SegmentedControlShape.Rounded,
        ) {
            SingleLine("1", selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            SingleLine("2", selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
            SingleLine("3", selected = selectedIndex2 == 2, onClick = { selectedIndex2 = 2 })
            SingleLine("4", selected = selectedIndex2 == 3, onClick = { selectedIndex2 = 3 })
            SingleLine("5", selected = selectedIndex2 == 4, onClick = { selectedIndex2 = 4 })
            SingleLine("6", selected = selectedIndex2 == 5, onClick = { selectedIndex2 = 5 })
            SingleLine("7", selected = selectedIndex2 == 6, onClick = { selectedIndex2 = 6 })
            SingleLine("8", selected = selectedIndex2 == 7, onClick = { selectedIndex2 = 7 })
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
        ) {
            SingleLine("Text", selected = selectedIndex1 == 0, onClick = { selectedIndex1 = 0 })
            TwoLine("Title", "Subtitle", selected = selectedIndex1 == 1, onClick = { selectedIndex1 = 1 })
            Icon(LeboncoinIcons.ShoppingCartOutline, selected = selectedIndex1 == 2, onClick = { selectedIndex1 = 2 })
            IconText(
                LeboncoinIcons.ShoppingCartOutline,
                "Cart",
                selected = selectedIndex1 == 3,
                onClick = { selectedIndex1 = 3 },
            )
        }

        Text("Numbers")
        var selectedIndex2 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
        ) {
            Number(1, selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            Number(2, selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
            Number(3, selected = selectedIndex2 == 2, onClick = { selectedIndex2 = 2 })
            Number(4, selected = selectedIndex2 == 3, onClick = { selectedIndex2 = 3 })
        }

        // TODO: Custom segment type - pending rework
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
            title = "Filter",
            linkText = "Learn more",
            onLinkClick = { },
        ) {
            SingleLine("All", selected = selectedIndex == 0, onClick = { selectedIndex = 0 })
            SingleLine("Active", selected = selectedIndex == 1, onClick = { selectedIndex = 1 })
            SingleLine("Completed", selected = selectedIndex == 2, onClick = { selectedIndex = 2 })
        }

        var selectedIndex2 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
            title = "View Mode",
        ) {
            SingleLine("List", selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            SingleLine("Grid", selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
            SingleLine("Map", selected = selectedIndex2 == 2, onClick = { selectedIndex2 = 2 })
        }
    }
}

@Immutable
private data class EnergyRatingData(val text: String, val color: Color, val contentColor: Color)

private val EnergyRatingDataFake: ImmutableList<EnergyRatingData> = persistentListOf(
    EnergyRatingData("A", Color(0xFF009424), Color.White),
    EnergyRatingData("B", Color(0xFF3ACC31), Color.White),
    EnergyRatingData("C", Color(0xFFCDFD32), Color.Black),
    EnergyRatingData("D", Color(0xFFFBEA49), Color.Black),
    EnergyRatingData("E", Color(0xFFFCCB2F), Color.Black),
    EnergyRatingData("F", Color(0xFFFB9C34), Color.Black),
    EnergyRatingData("G", Color(0xFFFA1C1F), Color.White),
    EnergyRatingData("Vierge", Color.Unspecified, Color.Unspecified),
)

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
            shape = SegmentedControlShape.Pill,
            indicatorContent = { selectedIndex ->
                val data = EnergyRatingDataFake[selectedIndex]
                val transition = updateTransition(data, label = "indicator")
                val background = transition.animateColor(label = "indicatorBackground") { d ->
                    if (d.color.isSpecified) d.color else SparkTheme.colors.neutralContainer
                }
                val borderColor = transition.animateColor(label = "indicatorBorderColor") { d ->
                    if (d.color.isSpecified) {
                        SparkTheme.colors.outlineHigh.transparent
                    } else {
                        SparkTheme.colors.outlineHigh
                    }
                }
                val borderSize = transition.animateDp(label = "indicatorBorderSize") { d ->
                    if (d.color.isSpecified) 0.dp else 2.dp
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(SegmentedControlShape.Pill.shape)
                        .border(borderSize.value, borderColor.value, SegmentedControlShape.Pill.shape)
                        .drawBehind { drawRect(background.value) },
                )
            },
        ) {
            EnergyRatingDataFake.forEachIndexed { index, data ->
                if (data.color.isSpecified) {
                    DPE(
                        text = data.text,
                        color = data.color,
                        contentColor = data.contentColor,
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                    )
                } else {
                    SingleLine(data.text, selected = selectedIndex == index, onClick = { selectedIndex = index })
                }
            }
        }
    }
}

@Composable
private fun SegmentedControlScope.DPE(
    text: String,
    color: Color,
    contentColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Custom(
        selected = selected,
        onClick = onClick,
        rippleColor = color,
    ) {
        val transition = updateTransition(selected, label = "dpeLabel")
        val labelColor by transition.animateColor(label = "labelColor") {
            if (it) contentColor else SparkTheme.colors.onSurface.dim1
        }
        val labelProgress by transition.animateFloat(label = "labelProgress") { if (it) 1f else 0f }
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
            title = "Show",
        ) {
            SingleLine("All", selected = selectedIndex == 0, onClick = { selectedIndex = 0 })
            SingleLine("Active", selected = selectedIndex == 1, onClick = { selectedIndex = 1 })
            SingleLine("Archived", selected = selectedIndex == 2, onClick = { selectedIndex = 2 })
        }

        Text("Sort By")
        var selectedSortIndex by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedSortIndex,
        ) {
            IconText(
                LeboncoinIcons.ShoppingCartOutline,
                "Price",
                selected = selectedSortIndex == 0,
                onClick = { selectedSortIndex = 0 },
            )
            IconText(
                LeboncoinIcons.ShoppingCartOutline,
                "Date",
                selected = selectedSortIndex == 1,
                onClick = { selectedSortIndex = 1 },
            )
            IconText(
                LeboncoinIcons.ShoppingCartOutline,
                "Rating",
                selected = selectedSortIndex == 2,
                onClick = { selectedSortIndex = 2 },
            )
        }
    }
}
