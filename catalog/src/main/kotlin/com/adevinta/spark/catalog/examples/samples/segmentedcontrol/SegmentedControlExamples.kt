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
import com.adevinta.spark.icons.Building
import com.adevinta.spark.icons.CalendarCheckFill
import com.adevinta.spark.icons.CalendarDotOutline
import com.adevinta.spark.icons.CalendarTextOutline
import com.adevinta.spark.icons.EuroSymbol
import com.adevinta.spark.icons.House
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.List
import com.adevinta.spark.icons.PaperMapFill
import com.adevinta.spark.icons.ParasolOutline
import com.adevinta.spark.icons.PeopleCriteria
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.icons.StarFill
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.disabled
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
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("2 Segments")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex1,
        ) {
            iconText(
                text = "Day",
                icon = LeboncoinIcons.CalendarCheckFill,
                selected = selectedIndex1 == 0,
                iconOnTop = false,
                onClick = { selectedIndex1 = 0 },
            )
            iconText(
                text = "Week",
                icon = LeboncoinIcons.CalendarDotOutline,
                selected = selectedIndex1 == 1,
                iconOnTop = false,
                onClick = { selectedIndex1 = 1 },
            )
        }

        Text("3 Segments")
        var selectedIndex2 by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex2,
        ) {
            singleLine("Option 1", selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            singleLine("Option 2", selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
            singleLine("Option 3", selected = selectedIndex2 == 2, onClick = { selectedIndex2 = 2 })
        }

        Text("4 Segments")
        var selectedIndex3 by remember { mutableIntStateOf(2) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex3,
        ) {
            singleLine("All", selected = selectedIndex3 == 0, onClick = { selectedIndex3 = 0 })
            singleLine("Active", selected = selectedIndex3 == 1, onClick = { selectedIndex3 = 1 })
            singleLine("Pending", selected = selectedIndex3 == 2, onClick = { selectedIndex3 = 2 })
            singleLine("Done", selected = selectedIndex3 == 3, onClick = { selectedIndex3 = 3 })
        }
    }
}

@Composable
private fun MultiRowExample() {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("5 Segments")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex1,
            shape = SegmentedControlShape.Pill,
        ) {
            singleLine("Option 1", selected = selectedIndex1 == 0, onClick = { selectedIndex1 = 0 })
            singleLine("Option 2", selected = selectedIndex1 == 1, onClick = { selectedIndex1 = 1 })
            singleLine("Option 3", selected = selectedIndex1 == 2, onClick = { selectedIndex1 = 2 })
            singleLine("Option 4", selected = selectedIndex1 == 3, onClick = { selectedIndex1 = 3 })
            singleLine("Option 5", selected = selectedIndex1 == 4, onClick = { selectedIndex1 = 4 })
        }

        Text("8 Segments")
        var selectedIndex2 by remember { mutableIntStateOf(4) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex2,
            shape = SegmentedControlShape.Rounded,
        ) {
            singleLine("1", selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            singleLine("2", selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
            singleLine("3", selected = selectedIndex2 == 2, onClick = { selectedIndex2 = 2 })
            singleLine("4", selected = selectedIndex2 == 3, onClick = { selectedIndex2 = 3 })
            singleLine("5", selected = selectedIndex2 == 4, onClick = { selectedIndex2 = 4 })
            singleLine("6", selected = selectedIndex2 == 5, onClick = { selectedIndex2 = 5 })
            singleLine("7", selected = selectedIndex2 == 6, onClick = { selectedIndex2 = 6 })
            singleLine("8", selected = selectedIndex2 == 7, onClick = { selectedIndex2 = 7 })
        }
    }
}

@Composable
private fun ContentTypesExample() {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Mixed Content Types")
        var selectedIndex1 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex1,
        ) {
            singleLine(text = "Text", selected = selectedIndex1 == 0, onClick = { selectedIndex1 = 0 })
            twoLine(
                title = "Title",
                subtitle = "Subtitle",
                selected = selectedIndex1 == 1,
                onClick = { selectedIndex1 = 1 },
            )
            icon(
                icon = LeboncoinIcons.ShoppingCartOutline,
                contentDescription = "cart",
                selected = selectedIndex1 == 2,
                onClick = { selectedIndex1 = 2 },
            )
            iconText(
                LeboncoinIcons.ShoppingCartOutline,
                "Cart",
                selected = selectedIndex1 == 3,
                onClick = { selectedIndex1 = 3 },
                iconOnTop = true,
            )
        }

        Text("Single line")
        var selectedIndex2 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(selectedIndex = selectedIndex2) {
            singleLine(text = "Je vends", selected = selectedIndex2 == 0, onClick = { selectedIndex2 = 0 })
            singleLine(text = "Je donne", selected = selectedIndex2 == 1, onClick = { selectedIndex2 = 1 })
        }

        Text("Two line")
        var selectedIndex3 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(selectedIndex = selectedIndex3) {
            twoLine(
                title = "Offre",
                subtitle = "Je vends",
                selected = selectedIndex3 == 0,
                onClick = { selectedIndex3 = 0 },
            )
            twoLine(
                title = "Demande",
                subtitle = "Je recherche",
                selected = selectedIndex3 == 1,
                onClick = { selectedIndex3 = 1 },
            )
        }

        Text("Icon")
        var selectedIndex4 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(selectedIndex = selectedIndex4) {
            icon(
                icon = LeboncoinIcons.List,
                contentDescription = "List",
                selected = selectedIndex4 == 0,
                onClick = { selectedIndex4 = 0 },
            )
            icon(
                icon = LeboncoinIcons.PaperMapFill,
                contentDescription = "Map",
                selected = selectedIndex4 == 1,
                onClick = { selectedIndex4 = 1 },
            )
        }

        Text("Icon Text")
        var selectedIndex5 by remember { mutableIntStateOf(0) }
        SegmentedControl.Vertical(selectedIndex = selectedIndex5) {
            iconText(
                icon = LeboncoinIcons.House,
                text = "Vente immobilière",
                selected = selectedIndex5 == 0,
                onClick = { selectedIndex5 = 0 },
                iconOnTop = true,
            )
            iconText(
                icon = LeboncoinIcons.Building,
                text = "Location",
                selected = selectedIndex5 == 1,
                onClick = { selectedIndex5 = 1 },
                iconOnTop = true,
            )
            iconText(
                icon = LeboncoinIcons.ParasolOutline,
                text = "Location Saisonnière",
                selected = selectedIndex5 == 2,
                onClick = { selectedIndex5 = 2 },
                iconOnTop = true,
            )
            iconText(
                icon = LeboncoinIcons.PeopleCriteria,
                text = "Colocation",
                selected = selectedIndex5 == 3,
                onClick = { selectedIndex5 = 3 },
                iconOnTop = true,
            )
        }

        Text("Numbers")
        var selectedIndex6 by remember { mutableIntStateOf(0) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedIndex6,
        ) {
            number(1, selected = selectedIndex6 == 0, onClick = { selectedIndex6 = 0 })
            number(2, selected = selectedIndex6 == 1, onClick = { selectedIndex6 = 1 })
            number(3, selected = selectedIndex6 == 2, onClick = { selectedIndex6 = 2 })
            number(4, selected = selectedIndex6 == 3, onClick = { selectedIndex6 = 3 })
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
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Energy Rating Scale")
        var selectedIndex by remember { mutableIntStateOf(7) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex,
            shape = SegmentedControlShape.Pill,
            indicatorContent = { selectedIndex, enabled ->
                val data = EnergyRatingDataFake[selectedIndex]
                val transition = updateTransition(data, label = "indicator")
                val background = transition.animateColor(label = "indicatorBackground") { d ->
                    if (d.color.isSpecified) {
                        if (enabled) d.color else d.color.disabled
                    } else {
                        if (enabled) SparkTheme.colors.neutralContainer else SparkTheme.colors.surface
                    }
                }
                val borderColor = transition.animateColor(label = "indicatorBorderColor") { d ->
                    if (d.color.isSpecified) {
                        SparkTheme.colors.outlineHigh.transparent
                    } else {
                        if (enabled) SparkTheme.colors.outlineHigh else SparkTheme.colors.outlineHigh.disabled
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
                    singleLine(data.text, selected = selectedIndex == index, onClick = { selectedIndex = index })
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
    custom(
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
        ) {
            singleLine("All", selected = selectedIndex == 0, onClick = { selectedIndex = 0 })
            singleLine("Active", selected = selectedIndex == 1, onClick = { selectedIndex = 1 })
            singleLine("Archived", selected = selectedIndex == 2, onClick = { selectedIndex = 2 })
        }

        Text("Sort By")
        var selectedSortIndex by remember { mutableIntStateOf(1) }
        SegmentedControl.Horizontal(
            selectedIndex = selectedSortIndex,
        ) {
            iconText(
                LeboncoinIcons.EuroSymbol,
                "Price",
                selected = selectedSortIndex == 0,
                onClick = { selectedSortIndex = 0 },
                iconOnTop = true,
            )
            iconText(
                LeboncoinIcons.CalendarTextOutline,
                "Date",
                selected = selectedSortIndex == 1,
                onClick = { selectedSortIndex = 1 },
                iconOnTop = true,
            )
            iconText(
                LeboncoinIcons.StarFill,
                "Rating",
                selected = selectedSortIndex == 2,
                onClick = { selectedSortIndex = 2 },
                iconOnTop = true,
            )
        }
    }
}
