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
package com.adevinta.spark.segmentedcontrol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

internal class SegmentedControlScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = V_SCROLL,
    )

    @Test
    fun singleRowSegments() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                SingleRowSegments()
            }
        }
    }

    @Test
    fun multiRowSegments() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                MultiRowSegments()
            }
        }
    }

    @Test
    fun contentTypes() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                ContentTypes()
            }
        }
    }

    @Test
    fun withTitleAndLink() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                WithTitleAndLink()
            }
        }
    }

    @Test
    fun disabled() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Disabled()
            }
        }
    }

    @Composable
    private fun SingleRowSegments() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Single row - 2 segments",
                style = SparkTheme.typography.headline2,
            )
            var selectedIndex1 by remember { mutableIntStateOf(0) }
            SegmentedControl.Horizontal(
                selectedIndex = selectedIndex1,
                onSegmentSelect = { selectedIndex1 = it },
            ) {
                SingleLine("Option 1")
                SingleLine("Option 2")
            }

            Text(
                text = "Single row - 3 segments",
                style = SparkTheme.typography.headline2,
            )
            var selectedIndex2 by remember { mutableIntStateOf(1) }
            SegmentedControl.Horizontal(
                selectedIndex = selectedIndex2,
                onSegmentSelect = { selectedIndex2 = it },
            ) {
                SingleLine("Option 1")
                SingleLine("Option 2")
                SingleLine("Option 3")
            }

            Text(
                text = "Single row - 4 segments",
                style = SparkTheme.typography.headline2,
            )
            var selectedIndex3 by remember { mutableIntStateOf(2) }
            SegmentedControl.Horizontal(
                selectedIndex = selectedIndex3,
                onSegmentSelect = { selectedIndex3 = it },
            ) {
                SingleLine("Option 1")
                SingleLine("Option 2")
                SingleLine("Option 3")
                SingleLine("Option 4")
            }
        }
    }

    @Composable
    private fun MultiRowSegments() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Multi-row - 5 segments",
                style = SparkTheme.typography.headline2,
            )
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

            Text(
                text = "Multi-row - 8 segments",
                style = SparkTheme.typography.headline2,
            )
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
    private fun ContentTypes() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Content Types",
                style = SparkTheme.typography.headline2,
            )

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
        }
    }

    @Composable
    private fun WithTitleAndLink() {
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
        }
    }

    @Composable
    private fun Disabled() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Disabled",
                style = SparkTheme.typography.headline2,
            )

            SegmentedControl.Horizontal(
                selectedIndex = 1,
                onSegmentSelect = { },
                enabled = false,
            ) {
                SingleLine("Option 1")
                SingleLine("Option 2")
                SingleLine("Option 3")
            }
        }
    }
}
