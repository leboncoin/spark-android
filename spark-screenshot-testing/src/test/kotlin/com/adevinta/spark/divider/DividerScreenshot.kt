/*
 * Copyright (c) 2023 Adevinta
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
package com.adevinta.spark.divider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.divider.DividerIntent
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.divider.LabelHorizontalAlignment
import com.adevinta.spark.components.divider.LabelVerticalAlignment
import com.adevinta.spark.components.divider.VerticalDivider
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class DividerScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun divider() {
        paparazzi.sparkSnapshot {
            HorizontalDivider(intent = DividerIntent.Outline)
            HorizontalDivider(intent = DividerIntent.OutlineHigh)
            HorizontalDivider(
                label = { TextComposable() },
            )
            HorizontalDivider(
                label = { TextComposable() },
                labelHorizontalAlignment = LabelHorizontalAlignment.End,
            )
            HorizontalDivider(
                label = { TextComposable() },
                labelHorizontalAlignment = LabelHorizontalAlignment.Start,
            )
            // Regression: empty label must not leave stray 16.dp gaps (PR #1208 bug 1)
            HorizontalDivider(
                label = { Box {} },
            )
            // Regression: oversized label must not collapse the divider lines (PR #1208 bug 2)
            HorizontalDivider(
                label = { OverflowTextComposable() },
            )

            Row {
                VerticalDivider(
                    label = { TextComposable() },
                    labelVerticalAlignment = LabelVerticalAlignment.Top,
                )
                VerticalDivider(
                    label = { TextComposable() },
                    labelVerticalAlignment = LabelVerticalAlignment.Center,
                )
                VerticalDivider(
                    label = { TextComposable() },
                    labelVerticalAlignment = LabelVerticalAlignment.Bottom,
                )
                HorizontalSpacer(space = 16.dp)
                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    labelVerticalAlignment = LabelVerticalAlignment.Top,
                )
                HorizontalSpacer(space = 16.dp)
                VerticalDivider(modifier = Modifier.height(24.dp))
                HorizontalSpacer(space = 16.dp)
                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    labelVerticalAlignment = LabelVerticalAlignment.Bottom,
                )
                HorizontalSpacer(space = 16.dp)

                VerticalDivider(
                    labelVerticalAlignment = LabelVerticalAlignment.Bottom,
                )
            }
        }
    }
}

@Composable
private fun TextComposable(textOverflow: TextOverflow = TextOverflow.Ellipsis) {
    Text(
        textAlign = TextAlign.Center,
        overflow = textOverflow,
        style = SparkTheme.typography.body1,
        text = "label",
    )
}

@Composable
private fun OverflowTextComposable() {
    Text(
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = SparkTheme.typography.body1,
        text = "This label is intentionally very long to verify the divider lines keep their 40dp minimum",
    )
}
