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
package com.adevinta.spark.components.meter.circular

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.components.meter.MeterDefaults
import com.adevinta.spark.components.meter.MeterIntent
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@InternalSparkApi
@Composable
internal fun SparkCircularMeter(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    intent: MeterIntent,
    size: CircleMeterSize,
    content: CircularMeterContent?,
    modifier: Modifier = Modifier,
    suffix: String? = null,
) {
    val normalizedProgress = ((value - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)

    val animatedProgress = animateFloatAsState(
        targetValue = normalizedProgress,
        animationSpec = MeterDefaults.AnimationSpec,
        label = "MeterProgress",
    )

    val intentColors = intent.colors()
    val indicatorColor = intentColors.color
    val trackColor = intentColors.containerColor

    val styling = CircularMeterStyling(
        indicatorColor = indicatorColor,
        size = size,
    )

    val coercedValue = value.coerceIn(range)

    CompositionLocalProvider(LocalCircularMeterStyling provides styling) {
        Box(
            modifier = modifier
                .sparkUsageOverlay()
                .size(size.diameter)
                .semantics(mergeDescendants = true) {
                    when (content) {
                        is CircularMeterContent.Value ->
                            stateDescription = content.formatValue(coercedValue) + suffix.orEmpty()

                        is CircularMeterContent.ValueLabel -> {
                            val label = if (content.label.isNotBlank()) ", ${content.label}" else ""
                            stateDescription = content.formatValue(coercedValue) + label + suffix.orEmpty()
                        }

                        else -> if (suffix != null) {
                            stateDescription = "${(normalizedProgress * 100).toInt()}%$suffix"
                        }
                    }
                    progressBarRangeInfo = ProgressBarRangeInfo(
                        current = coercedValue,
                        range = range.start..range.endInclusive,
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress.value },
                modifier = Modifier
                    .size(size.diameter)
                    .clearAndSetSemantics {},
                color = indicatorColor,
                trackColor = trackColor,
                strokeWidth = size.strokeWidth,
                strokeCap = StrokeCap.Round,
            )
            Box(modifier = Modifier.clearAndSetSemantics {}) {
                content?.Content(value = coercedValue)
            }
        }
    }
}
