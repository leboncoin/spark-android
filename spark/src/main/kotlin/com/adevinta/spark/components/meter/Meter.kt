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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.components.meter.circular.SparkCircularMeter
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.VerifiedShieldFill
import com.adevinta.spark.tokens.highlight

public object Meter {

    /**
     * A circular meter displaying progress with content inside the ring.
     *
     * @param value Current value within [range].
     * @param content Content displayed inside the ring.
     * @param modifier Modifier applied to the meter.
     * @param range The value range (e.g. `0f..100f` for percentage, `0f..200f` for euros).
     * @param intent Color intent for the indicator and track.
     * @param size Size of the meter (Medium, Large, or XLarge).
     * @param contentDescription Overrides the accessibility announcement for the entire meter.
     */
    @Composable
    public fun Circular(
        value: Float,
        content: CircularMeterContent,
        modifier: Modifier = Modifier,
        range: ClosedFloatingPointRange<Float> = MeterDefaults.Range,
        intent: MeterIntent = MeterDefaults.Intent,
        size: CircleMeterSize = MeterDefaults.Size,
        contentDescription: String? = null,
    ) {
        SparkCircularMeter(
            value = value,
            range = range,
            intent = intent,
            size = size,
            content = content,
            modifier = modifier,
            overrideContentDescription = contentDescription,
        )
    }

    /**
     * A small circular meter (24dp) that renders the progress value text outside the ring.
     *
     * @param value Current value within [range].
     * @param modifier Modifier applied to the meter row.
     * @param range The value range (defaults to `0f..100f`).
     * @param intent Color intent for the indicator and track.
     */
    @Composable
    public fun CircularSmall(
        value: Float,
        modifier: Modifier = Modifier,
        range: ClosedFloatingPointRange<Float> = MeterDefaults.Range,
        intent: MeterIntent = MeterDefaults.Intent,
    ) {
        val normalizedProgress = ((value - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)
        val percentDescription = stringResource(
            R.string.spark_meter_a11y,
            (normalizedProgress * 100).toInt(),
        )
        Row(
            modifier = modifier.semantics(mergeDescendants = true) {
                this.contentDescription = percentDescription
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = value.coerceIn(range),
                    range = range,
                )
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            SparkCircularMeter(
                value = value,
                range = range,
                intent = intent,
                size = CircleMeterSize.Small,
                content = null,
                modifier = Modifier.clearAndSetSemantics {},
            )
            Text(
                text = "${(normalizedProgress * 100).toInt()}%",
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
            value = 70f,
            content = CircularMeterContent.ValueLabel("70%", label = "Label"),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
        Meter.Circular(
            value = 50f,
            content = CircularMeterContent.Value("50%"),
            intent = MeterIntent.Main,
            size = CircleMeterSize.Medium,
        )
        Meter.Circular(
            value = 100f,
            content = CircularMeterContent.ValueLabel("100%", label = "Complete"),
            intent = MeterIntent.Success,
            size = CircleMeterSize.XLarge,
        )
        Meter.Circular(
            value = 0f,
            content = CircularMeterContent.Icon(LeboncoinIcons.VerifiedShieldFill, "100%", label = "Complete"),
            intent = MeterIntent.Success,
            size = CircleMeterSize.XLarge,
        )
        Meter.Circular(
            value = 23f,
            content = CircularMeterContent.Image(
                "100%", model = com.adevinta.spark.icons.R.drawable.spark_icons_baby,
            ),
            intent = MeterIntent.Success,
            size = CircleMeterSize.XLarge,
        )
        Meter.CircularSmall(value = 30f, intent = MeterIntent.Info)
    }
}
