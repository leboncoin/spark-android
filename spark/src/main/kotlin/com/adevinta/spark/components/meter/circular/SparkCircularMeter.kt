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

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.R
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
    overrideContentDescription: String? = null,
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

    val context = LocalContext.current
    val semanticDescription = overrideContentDescription
        ?: buildSemanticDescription(content, normalizedProgress, context)

    CompositionLocalProvider(LocalCircularMeterStyling provides styling) {
        Box(
            modifier = modifier
                .sparkUsageOverlay()
                .size(size.diameter)
                .semantics(mergeDescendants = true) {
                    contentDescription = semanticDescription
                    progressBarRangeInfo = ProgressBarRangeInfo(
                        current = value.coerceIn(range),
                        range = range,
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
            content?.Content()
        }
    }
}

private fun buildSemanticDescription(
    content: CircularMeterContent?,
    progress: Float,
    context: Context,
): String {
    val percentText = context.getString(R.string.spark_meter_a11y, (progress * 100).toInt())
    return when (content) {
        is CircularMeterContent.Value -> "${content.text}, $percentText"
        is CircularMeterContent.ValueLabel -> "${content.text}, ${content.label}"
        is CircularMeterContent.Icon -> {
            val parts = listOfNotNull(
                content.contentDescription.takeIf { it.isNotEmpty() },
                content.label,
                percentText,
            )
            parts.joinToString(", ")
        }

        is CircularMeterContent.Image -> {
            val desc = content.contentDescription.takeIf { it.isNotEmpty() }
            if (desc != null) "$desc, $percentText" else percentText
        }

        is CircularMeterContent.Custom -> {
            val desc = content.contentDescription?.takeIf { it.isNotEmpty() }
            if (desc != null) "$desc, $percentText" else percentText
        }

        null -> percentText
    }
}
