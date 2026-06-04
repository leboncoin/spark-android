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
package com.adevinta.spark.components.meter

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.components.meter.circular.SparkCircularMeter
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.highlight

public object Meter {

    /**
     * A circular meter displaying progress with content inside the ring.
     *
     * Content is a [CircularMeterContent] subtype — pick the variant that fits:
     * [CircularMeterContent.Value], [CircularMeterContent.ValueLabel],
     * [CircularMeterContent.Icon], [CircularMeterContent.Image], or [CircularMeterContent.Custom].
     *
     * @param progress Progress value between 0.0 and 1.0.
     * @param content Content displayed inside the ring.
     * @param modifier Modifier applied to the meter.
     * @param intent Colour intent for the indicator and track.
     * @param size Size of the meter (Medium, Large, or XLarge).
     */
    @Composable
    public fun Circular(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        content: CircularMeterContent,
        modifier: Modifier = Modifier,
        intent: MeterIntent = MeterDefaults.Intent,
        size: MeterSize = MeterDefaults.Size,
    ) {
        SparkCircularMeter(
            progress = progress,
            intent = intent,
            size = size,
            content = content,
            modifier = modifier,
        )
    }

    /**
     * A small circular meter (24dp) that renders the progress value text outside the ring.
     *
     * @param progress Progress value between 0.0 and 1.0.
     * @param modifier Modifier applied to the meter row.
     * @param intent Colour intent for the indicator and track.
     */
    @Composable
    public fun CircularSmall(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        modifier: Modifier = Modifier,
        intent: MeterIntent = MeterDefaults.Intent,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            SparkCircularMeter(
                progress = progress,
                intent = intent,
                size = MeterSize.Small,
                content = null,
            )
            Text(
                text = "${(progress.coerceIn(0f, 1f) * 100).toInt()}%",
                style = SparkTheme.typography.body2.highlight,
                color = SparkTheme.colors.onSurface,
                textAlign = TextAlign.Start,
                maxLines = 1,
            )
        }
    }
}

@Preview(
    group = "Meter",
    name = "Circular",
)
@Composable
private fun PreviewCircularMeter() {
    PreviewTheme {
        Meter.Circular(
            progress = 0.7f,
            content = CircularMeterContent.ValueLabel("70%", "Label"),
            intent = MeterIntent.Support,
            size = MeterSize.Large,
        )
        Meter.Circular(
            progress = 0.5f,
            content = CircularMeterContent.Value("50%"),
            intent = MeterIntent.Main,
            size = MeterSize.Medium,
        )
        Meter.Circular(
            progress = 1.0f,
            content = CircularMeterContent.ValueLabel("100%", "Complete"),
            intent = MeterIntent.Success,
            size = MeterSize.XLarge,
        )
        Meter.CircularSmall(progress = 0.3f, intent = MeterIntent.Info)
    }
}