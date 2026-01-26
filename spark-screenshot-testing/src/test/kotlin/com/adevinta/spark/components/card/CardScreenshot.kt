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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Flat Card")
            Card.Flat {
                Text("This is a flat card with default styling.")
            }

            Text("Elevated Card")
            Card.Elevated {
                Text("This is an elevated card with a drop shadow.")
            }

            Text("Outlined Card")
            Card.Outlined {
                Text("This is an outlined card with a border.")
            }
        }
    }

    @Test
    fun cardStates() = paparazzi.sparkSnapshot(drawBackground = false) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Enabled Interactive Card")
            Card.Flat(
                onClick = {},
            ) {
                Text("This card is enabled and clickable.")
            }

            Text("Disabled Interactive Card")
            Card.Flat(
                onClick = {},
            ) {
                Text("This card is disabled.")
            }
        }
    }

    @Test
    fun cardInteractive() = paparazzi.sparkSnapshot(drawBackground = false) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Interactive Flat Card")
            Card.Flat(
                onClick = {},
            ) {
                Text("Clickable flat card")
            }

            Text("Interactive Elevated Card")
            Card.Elevated(
                onClick = {},
            ) {
                Text("Clickable elevated card")
            }

            Text("Interactive Outlined Card")
            Card.Outlined(
                onClick = {},
            ) {
                Text("Clickable outlined card")
            }
        }
    }

    @Test
    fun cardShapes() = paparazzi.sparkSnapshot(drawBackground = false) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Card with default shape")
            Card.Flat {
                Text("Default medium shape")
            }

            Text("Card with custom padding")
            Card.Flat(
                contentPadding = PaddingValues(24.dp),
            ) {
                Text("Card with larger padding")
            }
        }
    }

    @Test
    fun cardContent() = paparazzi.sparkSnapshot(drawBackground = false) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card.Elevated {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Card Title",
                        style = com.adevinta.spark.SparkTheme.typography.headline4,
                    )
                    Text(
                        text = "Card content with multiple lines of text to demonstrate how cards can contain various types of content.",
                    )
                }
            }

            Card.Elevated {
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
