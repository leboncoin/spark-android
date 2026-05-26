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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlShape
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
    fun customSegmentAndIndicator() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                CustomSegmentAndIndicator()
            }
        }
    }

    @Test
    fun verticalPillShape() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                VerticalPillShape()
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
    private fun CustomSegmentAndIndicator() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Custom segments with custom indicator", style = SparkTheme.typography.headline2)
            SegmentedControl.Horizontal(
                selectedIndex = 1,
                indicatorContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .background(SparkTheme.colors.warningContainer, SparkTheme.shapes.full),
                    )
                },
            ) {
                Custom(selected = false, onClick = {}, rippleColor = SparkTheme.colors.warning) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SparkTheme.colors.errorContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("A", color = SparkTheme.colors.onErrorContainer)
                    }
                }
                Custom(selected = true, onClick = {}, rippleColor = SparkTheme.colors.warning) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SparkTheme.colors.successContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("B", color = SparkTheme.colors.onSuccessContainer)
                    }
                }
                Custom(selected = false, onClick = {}, rippleColor = SparkTheme.colors.warning) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SparkTheme.colors.infoContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("C", color = SparkTheme.colors.onInfoContainer)
                    }
                }
            }
        }
    }

    @Composable
    private fun VerticalPillShape() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Vertical - Pill shape", style = SparkTheme.typography.headline2)
            SegmentedControl.Vertical(
                selectedIndex = 2,
                shape = SegmentedControlShape.Pill,
            ) {
                SingleLine("Option 1", selected = false, onClick = {})
                SingleLine("Option 2", selected = false, onClick = {})
                SingleLine("Option 3", selected = true, onClick = {})
                SingleLine("Option 4", selected = false, onClick = {})
                SingleLine("Option 5", selected = false, onClick = {})
            }

            Text(text = "Vertical - Rounded shape (default)", style = SparkTheme.typography.headline2)
            SegmentedControl.Vertical(
                selectedIndex = 2,
                shape = SegmentedControlShape.Rounded,
            ) {
                SingleLine("Option 1", selected = false, onClick = {})
                SingleLine("Option 2", selected = false, onClick = {})
                SingleLine("Option 3", selected = true, onClick = {})
                SingleLine("Option 4", selected = false, onClick = {})
                SingleLine("Option 5", selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun SingleRowSegments() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Single row - 2 segments", style = SparkTheme.typography.headline2)
            SegmentedControl.Horizontal(selectedIndex = 0) {
                SingleLine("Option 1", selected = true, onClick = {})
                SingleLine("Option 2", selected = false, onClick = {})
            }

            Text(text = "Single row - 3 segments", style = SparkTheme.typography.headline2)
            SegmentedControl.Horizontal(selectedIndex = 1) {
                SingleLine("Option 1", selected = false, onClick = {})
                SingleLine("Option 2", selected = true, onClick = {})
                SingleLine("Option 3", selected = false, onClick = {})
            }

            Text(text = "Single row - 4 segments", style = SparkTheme.typography.headline2)
            SegmentedControl.Horizontal(selectedIndex = 2) {
                SingleLine("Option 1", selected = false, onClick = {})
                SingleLine("Option 2", selected = false, onClick = {})
                SingleLine("Option 3", selected = true, onClick = {})
                SingleLine("Option 4", selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun MultiRowSegments() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Multi-row - 5 segments", style = SparkTheme.typography.headline2)
            SegmentedControl.Vertical(selectedIndex = 0) {
                SingleLine("Option 1", selected = true, onClick = {})
                SingleLine("Option 2", selected = false, onClick = {})
                SingleLine("Option 3", selected = false, onClick = {})
                SingleLine("Option 4", selected = false, onClick = {})
                SingleLine("Option 5", selected = false, onClick = {})
            }

            Text(text = "Multi-row - 8 segments", style = SparkTheme.typography.headline2)
            SegmentedControl.Vertical(selectedIndex = 4) {
                SingleLine("1", selected = false, onClick = {})
                SingleLine("2", selected = false, onClick = {})
                SingleLine("3", selected = false, onClick = {})
                SingleLine("4", selected = false, onClick = {})
                SingleLine("5", selected = true, onClick = {})
                SingleLine("6", selected = false, onClick = {})
                SingleLine("7", selected = false, onClick = {})
                SingleLine("8", selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun ContentTypes() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Content Types", style = SparkTheme.typography.headline2)

            SegmentedControl.Horizontal(selectedIndex = 0) {
                SingleLine("Text", selected = true, onClick = {})
                TwoLine("Title", "Subtitle", selected = false, onClick = {})
                Icon(LeboncoinIcons.ShoppingCartOutline, selected = false, onClick = {})
                IconText(LeboncoinIcons.ShoppingCartOutline, "Cart", selected = false, onClick = {})
            }

            SegmentedControl.Horizontal(selectedIndex = 0) {
                Number(1, selected = true, onClick = {})
                Number(2, selected = false, onClick = {})
                Number(3, selected = false, onClick = {})
                Number(4, selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun WithTitleAndLink() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SegmentedControl.Horizontal(
                selectedIndex = 1,
                title = "Filter",
                linkText = "Learn more",
                onLinkClick = {},
            ) {
                SingleLine("All", selected = false, onClick = {})
                SingleLine("Active", selected = true, onClick = {})
                SingleLine("Completed", selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun Disabled() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Disabled", style = SparkTheme.typography.headline2)

            SegmentedControl.Horizontal(
                selectedIndex = 1,
                enabled = false,
            ) {
                SingleLine("Option 1", selected = false, onClick = {})
                SingleLine("Option 2", selected = true, onClick = {})
                SingleLine("Option 3", selected = false, onClick = {})
            }
        }
    }
}
