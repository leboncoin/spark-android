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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.meter.MeterDefaults
import com.adevinta.spark.components.meter.MeterIntent
import com.adevinta.spark.components.meter.MeterSize
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@InternalSparkApi
@Composable
internal fun SparkCircularMeter(
    progress: Float,
    intent: MeterIntent,
    size: MeterSize,
    content: CircularMeterContent?,
    modifier: Modifier = Modifier,
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val animatedProgress = animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = MeterDefaults.AnimationSpec,
        label = "MeterProgress",
    )

    val intentColors = intent.colors()
    val indicatorColor = intentColors.color
    val trackColor = intentColors.containerColor

    val styling = CircularMeterStyling(
        valueStyle = when (size) {
            MeterSize.Small -> SparkTheme.typography.body2.highlight
            MeterSize.Medium -> SparkTheme.typography.body2.highlight
            MeterSize.Large -> SparkTheme.typography.body1.highlight
            MeterSize.XLarge -> SparkTheme.typography.display3
        },
        labelStyle = when (size) {
            MeterSize.Small -> SparkTheme.typography.body2
            MeterSize.Medium -> SparkTheme.typography.small
            MeterSize.Large -> SparkTheme.typography.caption
            MeterSize.XLarge -> SparkTheme.typography.body2
        },
        valueColor = SparkTheme.colors.onSurface,
        labelColor = SparkTheme.colors.onSurface.dim1,
        indicatorColor = indicatorColor,
        iconSize = size.iconSize,
    )

    val semanticDescription = buildSemanticDescription(content, clampedProgress)

    CompositionLocalProvider(LocalCircularMeterStyling provides styling) {
        Box(
            modifier = modifier
                .sparkUsageOverlay()
                .size(size.diameter)
                .semantics { contentDescription = semanticDescription },
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress.value },
                modifier = Modifier.size(size.diameter),
                color = indicatorColor,
                trackColor = trackColor,
                strokeWidth = size.strokeWidth,
                strokeCap = StrokeCap.Round,
            )
            content?.Content()
        }
    }
}

private fun buildSemanticDescription(content: CircularMeterContent?, progress: Float): String {
    val percentText = "${(progress * 100).toInt()} percent"
    val label = when (content) {
        is CircularMeterContent.Value -> content.text
        is CircularMeterContent.ValueLabel -> "${content.text}, ${content.label}"
        is CircularMeterContent.Icon -> content.contentDescription
        is CircularMeterContent.Image -> ""
        is CircularMeterContent.Custom -> ""
        null -> ""
    }
    return if (label.isNotEmpty()) "$label, $percentText" else percentText
}
