/*
 * Copyright (c) 2026 Adevinta
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
import androidx.compose.foundation.layout.Row
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
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

internal class SegmentedControlScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun segmentedControl() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    HorizontalVariants()
                    VerticalVariants()
                    ContentTypesSection()
                    DisabledSection()
                    CustomSection()
                }
            }
        }
    }

    @Composable
    private fun HorizontalVariants() {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SegmentedControl.Horizontal(selectedIndex = 0, modifier = Modifier.weight(1f)) {
                singleLine("Day", selected = true, onClick = {})
                singleLine("Week", selected = false, onClick = {})
            }

            SegmentedControl.Horizontal(selectedIndex = 1, modifier = Modifier.weight(1f)) {
                singleLine("All", selected = false, onClick = {})
                singleLine("Active", selected = true, onClick = {})
                singleLine("Done", selected = false, onClick = {})
            }

            SegmentedControl.Horizontal(selectedIndex = 2, modifier = Modifier.weight(1f)) {
                singleLine("Mon", selected = false, onClick = {})
                singleLine("Tue", selected = false, onClick = {})
                singleLine("Wed", selected = true, onClick = {})
                singleLine("Thu", selected = false, onClick = {})
            }

            SegmentedControl.Horizontal(selectedIndex = 4, modifier = Modifier.weight(1f)) {
                singleLine("A", selected = false, onClick = {})
                singleLine("B", selected = false, onClick = {})
                singleLine("C", selected = false, onClick = {})
                singleLine("D", selected = false, onClick = {})
                singleLine("E", selected = true, onClick = {})
            }
        }
    }

    @Composable
    private fun VerticalVariants() {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Vertical", style = SparkTheme.typography.headline2)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("5 segments — Pill", style = SparkTheme.typography.body2)
                    SegmentedControl.Vertical(selectedIndex = 2, shape = SegmentedControlShape.Pill) {
                        singleLine("Option 1", selected = false, onClick = {})
                        singleLine("Option 2", selected = false, onClick = {})
                        singleLine("Option 3", selected = true, onClick = {})
                        singleLine("Option 4", selected = false, onClick = {})
                        singleLine("Option 5", selected = false, onClick = {})
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("8 segments — Rounded", style = SparkTheme.typography.body2)
                    SegmentedControl.Vertical(selectedIndex = 3, shape = SegmentedControlShape.Rounded) {
                        singleLine("1", selected = false, onClick = {})
                        singleLine("2", selected = false, onClick = {})
                        singleLine("3", selected = false, onClick = {})
                        singleLine("4", selected = true, onClick = {})
                        singleLine("5", selected = false, onClick = {})
                        singleLine("6", selected = false, onClick = {})
                        singleLine("7", selected = false, onClick = {})
                        singleLine("8", selected = false, onClick = {})
                    }
                }
            }
        }
    }

    @Composable
    private fun ContentTypesSection() {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Content types", style = SparkTheme.typography.headline2)

            SegmentedControl.Horizontal(selectedIndex = 0) {
                singleLine("Text", selected = true, onClick = {})
                twoLine("Title", "Subtitle", selected = false, onClick = {})
                icon(LeboncoinIcons.ShoppingCartOutline, contentDescription = "Cart", selected = false, onClick = {})
                iconText(LeboncoinIcons.ShoppingCartOutline, "Cart", selected = false, iconOnTop = true, onClick = {})
            }

            SegmentedControl.Horizontal(selectedIndex = 2) {
                number(1, selected = false, onClick = {})
                number(2, selected = false, onClick = {})
                number(3, selected = true, onClick = {})
                number(4, selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun DisabledSection() {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Disabled", style = SparkTheme.typography.headline2)

            SegmentedControl.Horizontal(selectedIndex = 1, enabled = false) {
                singleLine("Option 1", selected = false, onClick = {})
                singleLine("Option 2", selected = true, onClick = {})
                singleLine("Option 3", selected = false, onClick = {})
            }

            SegmentedControl.Vertical(selectedIndex = 1, enabled = false) {
                singleLine("Option 1", selected = false, onClick = {})
                singleLine("Option 2", selected = true, onClick = {})
                singleLine("Option 3", selected = false, onClick = {})
                singleLine("Option 4", selected = false, onClick = {})
            }
        }
    }

    @Composable
    private fun CustomSection() {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Custom indicator and segments", style = SparkTheme.typography.headline2)

            SegmentedControl.Horizontal(
                selectedIndex = 1,
                indicatorContent = { _, _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .background(SparkTheme.colors.alertContainer, SparkTheme.shapes.full),
                    )
                },
            ) {
                custom(selected = false, onClick = {}, rippleColor = SparkTheme.colors.alert) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SparkTheme.colors.errorContainer),
                        contentAlignment = Alignment.Center,
                    ) { Text("A", color = SparkTheme.colors.onErrorContainer) }
                }
                custom(selected = true, onClick = {}, rippleColor = SparkTheme.colors.alert) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SparkTheme.colors.successContainer),
                        contentAlignment = Alignment.Center,
                    ) { Text("B", color = SparkTheme.colors.onSuccessContainer) }
                }
                custom(selected = false, onClick = {}, rippleColor = SparkTheme.colors.alert) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SparkTheme.colors.infoContainer),
                        contentAlignment = Alignment.Center,
                    ) { Text("C", color = SparkTheme.colors.onInfoContainer) }
                }
            }
        }
    }
}
