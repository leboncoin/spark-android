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
package com.adevinta.spark.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class CardScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun cardVariants() = paparazzi.sparkSnapshotNightMode(drawBackground = true) {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Row {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Card.Flat {
                        Text("This is a flat card with default styling.")
                    }

                    Card.Elevated {
                        Text("This is an elevated card with a drop shadow.")
                    }

                    Card.Outlined {
                        Text("This is an outlined card with a border.")
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Card.HighlightFlat {
                        Text("This is a flat card with a highlight banner.")
                    }
                    Card.HighlightElevated {
                        Text("This is an elevated card with a highlight banner.")
                    }
                }
            }
        }
    }

    @Test
    fun cardShapes() = paparazzi.sparkSnapshot(drawBackground = false) {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Row {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Card.Flat {
                        Text("Default medium shape")
                    }

                    Card.Flat(
                        shape = SparkTheme.shapes.large,
                    ) {
                        Text("Card with large shape")
                    }

                    Card.Flat(
                        shape = SparkTheme.shapes.extraSmall,
                    ) {
                        Text("Card with extraSmall shape")
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Card.HighlightFlat {
                        Text("Default medium shape")
                    }

                    Card.HighlightFlat(
                        shape = SparkTheme.shapes.large,
                    ) {
                        Text("Card with large shape")
                    }

                    Card.HighlightFlat(
                        shape = SparkTheme.shapes.extraSmall,
                    ) {
                        Text("Card with extraSmall shape")
                    }
                }
            }
        }
    }

    @Test
    fun cardColors() = paparazzi.sparkSnapshotNightMode(drawBackground = false) {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Card.Elevated(
                    colors = SparkTheme.colors.accentContainer,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Card Title",
                            style = SparkTheme.typography.headline2,
                        )
                        Text(
                            text = "Card content with multiple lines of text to demonstrate how cards can contain " +
                                "various types of content.",
                        )
                    }
                }

                Card.HighlightElevated(
                    colors = SparkTheme.colors.accentContainer,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Card with horizontal layout")
                    }
                }
            }
        }
    }
}
