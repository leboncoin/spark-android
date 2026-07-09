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
package com.adevinta.spark.catalog.configurator.samples.segmentedcontrol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.adevinta.spark.components.segmentedcontrol.SegmentedButtonItem
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlScope
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlShape
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlTokens
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import kotlinx.collections.immutable.toImmutableList

public val SegmentedControlConfigurator: Configurator = Configurator(
    id = "segmented-control",
    name = "Segmented Control",
    description = "Segmented Control configuration",
    sourceUrl = "$SampleSourceUrl/SegmentedControlConfigurator.kt",
) { snackbarHostState, _ ->
    SegmentedControlSample(snackbarHostState)
}

@Composable
private fun ColumnScope.SegmentedControlSample(
    snackbarHostState: com.adevinta.spark.components.snackbars.SnackbarHostState,
) {
    var segmentCount by remember { mutableIntStateOf(3) }
    var selectedIndex by remember { mutableIntStateOf(1) }
    var enabled by remember { mutableStateOf(true) }

    ConfigedSegmentedControl(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        segmentCount = segmentCount,
        selectedIndex = selectedIndex,
        onSegmentSelect = { selectedIndex = it },
        enabled = enabled,
    )

    val segmentCountOptions = (2..8).map { it.toString() }.toImmutableList()
    ButtonGroup(
        title = "Number of Segments",
        selectedOption = segmentCount.toString(),
        onOptionSelect = { newCount ->
            segmentCount = newCount.toInt()
            if (selectedIndex >= segmentCount) {
                selectedIndex = segmentCount - 1
            }
        },
        options = segmentCountOptions,
    )

    val selectedIndexOptions = (0 until segmentCount).map { it.toString() }.toImmutableList()
    ButtonGroup(
        title = "Selected Index",
        selectedOption = selectedIndex.toString(),
        onOptionSelect = { newIndex ->
            selectedIndex = newIndex.toInt()
        },
        options = selectedIndexOptions,
    )

    SwitchLabelled(
        checked = enabled,
        onCheckedChange = { enabled = it },
    ) {
        Text(text = "Enabled", modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun ConfigedSegmentedControl(
    modifier: Modifier = Modifier,
    segmentCount: Int,
    selectedIndex: Int,
    onSegmentSelect: (Int) -> Unit,
    enabled: Boolean,
) {
    Surface(
        modifier = modifier,
        color = SparkTheme.colors.backgroundVariant,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val segmentContent: @Composable SegmentedControlScope.(
                SegmentedButtonItem,
            ) -> Unit = remember(segmentCount, selectedIndex, onSegmentSelect) {
                {
                    repeat(segmentCount) { index ->
                        val selected = index == selectedIndex
                        val onClick = { onSegmentSelect(index) }
                        when (index % 4) {
                            0 -> singleLine("Option ${index + 1}", selected = selected, onClick = onClick)

                            1 -> twoLine("Title ${index + 1}", "Subtitle", selected = selected, onClick = onClick)

                            2 -> icon(
                                LeboncoinIcons.ShoppingCartOutline,
                                contentDescription = "Cart",
                                selected = selected,
                                onClick = onClick,
                            )

                            3 -> iconText(
                                LeboncoinIcons.ShoppingCartOutline,
                                "Item ${index + 1}",
                                selected = selected,
                                iconOnTop = true,
                                onClick = onClick,
                            )
                        }
                    }
                }
            }
            if (segmentCount <= SegmentedControlTokens.MaxHorizontalSegments) {
                SegmentedControl.Horizontal(
                    selectedIndex = selectedIndex,
                    enabled = enabled,
                    content = segmentContent,
                )
            }
            if (segmentCount >= SegmentedControlTokens.MinVerticalSegments) {
                SegmentedControl.Vertical(
                    selectedIndex = selectedIndex,
                    enabled = enabled,
                    shape = SegmentedControlShape.Rounded,
                    content = segmentContent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SegmentedControlSamplePreview() {
    PreviewTheme {
        SegmentedControlSample(com.adevinta.spark.components.snackbars.SnackbarHostState())
    }
}
