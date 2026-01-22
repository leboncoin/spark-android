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

import androidx.compose.foundation.layout.Box
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
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.icons.SparkIcons

public val SegmentedControlConfigurator: Configurator = Configurator(
    id = "segmented-control",
    name = "Segmented Control",
    description = "Segmented Control configuration",
    sourceUrl = "$SampleSourceUrl/SegmentedControlConfigurator.kt",
) {
    SegmentedControlSample(it)
}

@Composable
private fun ColumnScope.SegmentedControlSample(snackbarHostState: com.adevinta.spark.components.snackbars.SnackbarHostState) {
    var layoutType by remember { mutableStateOf(LayoutType.Horizontal) }
    var segmentCount by remember { mutableIntStateOf(3) }
    var selectedIndex by remember { mutableIntStateOf(1) }
    var title by remember { mutableStateOf("Filter") }
    var linkText by remember { mutableStateOf("Learn more") }
    var showTitle by remember { mutableStateOf(true) }
    var showLink by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    ConfigedSegmentedControl(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        layoutType = layoutType,
        segmentCount = segmentCount,
        selectedIndex = selectedIndex,
        onSegmentSelected = { selectedIndex = it },
        title = if (showTitle) title else null,
        linkText = if (showLink) linkText else null,
        onLinkClick = if (showLink) { {} } else null,
        enabled = enabled,
    )

    ButtonGroup(
        title = "Layout Type",
        selectedOption = layoutType,
        onOptionSelect = { newType ->
            layoutType = newType
            // Adjust segment count if needed
            val maxSegments = if (newType == LayoutType.Horizontal) 4 else 8
            if (segmentCount > maxSegments) {
                segmentCount = maxSegments
            }
            if (selectedIndex >= segmentCount) {
                selectedIndex = segmentCount - 1
            }
        },
    )

    val maxSegments = if (layoutType == LayoutType.Horizontal) 4 else 8
    val segmentCountOptions = (2..maxSegments).map { it.toString() }
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

    val selectedIndexOptions = (0 until segmentCount).map { it.toString() }
    ButtonGroup(
        title = "Selected Index",
        selectedOption = selectedIndex.toString(),
        onOptionSelect = { newIndex ->
            selectedIndex = newIndex.toInt()
        },
        options = selectedIndexOptions,
    )

    SwitchLabelled(
        checked = showTitle,
        onCheckedChange = { showTitle = it },
    ) {
        Text(text = "Show Title", modifier = Modifier.fillMaxWidth())
    }

    if (showTitle) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            label = "Title",
        )
    }

    SwitchLabelled(
        checked = showLink,
        onCheckedChange = { showLink = it },
    ) {
        Text(text = "Show Link", modifier = Modifier.fillMaxWidth())
    }

    if (showLink) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = linkText,
            onValueChange = { linkText = it },
            label = "Link Text",
        )
    }

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
    layoutType: LayoutType,
    segmentCount: Int,
    selectedIndex: Int,
    onSegmentSelected: (Int) -> Unit,
    title: String?,
    linkText: String?,
    onLinkClick: (() -> Unit)?,
    enabled: Boolean,
) {
    Surface(
        modifier = modifier,
        color = SparkTheme.colors.backgroundVariant,
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            when (layoutType) {
                LayoutType.Horizontal -> {
                    SegmentedControl.Horizontal(
                        selectedIndex = selectedIndex,
                        onSegmentSelect = onSegmentSelected,
                        title = title,
                        linkText = linkText,
                        onLinkClick = onLinkClick,
                        enabled = enabled,
                    ) {
                        repeat(segmentCount) { index ->
                            when (index % 4) {
                                0 -> SingleLine("Option ${index + 1}")
                                1 -> TwoLine("Title ${index + 1}", "Subtitle")
                                2 -> Icon(SparkIcons.ShoppingCartOutline)
                                3 -> IconText(SparkIcons.ShoppingCartOutline, "Item ${index + 1}")
                            }
                        }
                    }
                }

                LayoutType.Vertical -> {
                    SegmentedControl.Vertical(
                        selectedIndex = selectedIndex,
                        onSegmentSelect = onSegmentSelected,
                        title = title,
                        linkText = linkText,
                        onLinkClick = onLinkClick,
                        enabled = enabled,
                    ) {
                        repeat(segmentCount) { index ->
                            when (index % 4) {
                                0 -> SingleLine("Option ${index + 1}")
                                1 -> TwoLine("Title ${index + 1}", "Subtitle")
                                2 -> Icon(SparkIcons.ShoppingCartOutline)
                                3 -> IconText(SparkIcons.ShoppingCartOutline, "Item ${index + 1}")
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class LayoutType {
    Horizontal,
    Vertical,
}

@Preview
@Composable
private fun SegmentedControlSamplePreview() {
    PreviewTheme {
        SegmentedControlSample(com.adevinta.spark.components.snackbars.SnackbarHostState())
    }
}
