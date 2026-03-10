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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import com.android.resources.ScreenOrientation
import org.junit.Rule
import org.junit.Test

/**
 * Documentation screenshots for Card component, following Material Design's approach of showing
 * each variant with light and dark theme side by side.
 */
internal class CardDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C.copy(orientation = ScreenOrientation.LANDSCAPE),
    )

    @Test
    fun flatCard() = paparazzi.sparkDocSnapshot {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Box(Modifier.padding(16.dp)) {
                Card.Flat {
                    CardContent()
                }
            }
        }
    }

    @Test
    fun elevatedCard() = paparazzi.sparkDocSnapshot {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Box(Modifier.padding(16.dp)) {
                Card.Elevated {
                    CardContent()
                }
            }
        }
    }

    @Test
    fun outlinedCard() = paparazzi.sparkDocSnapshot {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Box(Modifier.padding(16.dp)) {
                Card.Outlined {
                    CardContent()
                }
            }
        }
    }

    @Test
    fun highlightFlatCard() = paparazzi.sparkDocSnapshot {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Box(Modifier.padding(16.dp)) {
                Card.HighlightFlat(
                    heading = { },
                ) {
                    CardContent()
                }
            }
        }
    }

    @Test
    fun highlightElevatedCard() = paparazzi.sparkDocSnapshot {
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Box(Modifier.padding(16.dp)) {
                Card.HighlightElevated(
                    heading = { },
                ) {
                    CardContent()
                }
            }
        }
    }
}

@Composable
private fun CardContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Title",
            style = SparkTheme.typography.headline2,
        )
        Text(
            text = "Subhead",
            style = SparkTheme.typography.body2,
        )
        Text(
            text = "Spark is a design system backed by open source code that helps teams build " +
                    "high-quality digital experiences. \uD83D\uDCA4",
            style = SparkTheme.typography.body1,
        )
    }
}
