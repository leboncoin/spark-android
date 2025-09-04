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
package com.adevinta.spark.catalog.examples.samples.animation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.animation.pulse
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Layout

private const val AnimationsExampleSourceUrl = "$SampleSourceUrl/PulsesSamples.kt"
public val AnimationExamples: List<Example> = listOf(
    Example(
        id = "pulse-basic",
        name = "Basic Pulse",
        description = "A simple pulse animation on a button",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        BasicPulseExample()
    },
    Example(
        id = "pulse-colors",
        name = "Pulse Colors",
        description = "Different pulse colors for different intents",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        PulseColorsExample()
    },
    Example(
        id = "pulse-shapes",
        name = "Pulse Shapes",
        description = "Pulse animation with different shapes",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        PulseShapesExample()
    },
    Example(
        id = "pulse-timing",
        name = "Pulse Timing",
        description = "Different animation durations",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        PulseTimingExample()
    },
)

@Composable
private fun BasicPulseExample() {
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Basic pulse animation on a button",
            style = SparkTheme.typography.body2,
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            ButtonFilled(
                modifier = Modifier.pulse(
                    targetScale = 1.3f,
                    color = SparkTheme.colors.main,
                    shape = SparkTheme.shapes.large,
                ),
                text = "Pulsing Button",
                onClick = { },
                intent = ButtonIntent.Main,
            )
        }
    }
}

@Composable
private fun PulseColorsExample() {
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Pulse animations with different colors for different intents",
            style = SparkTheme.typography.body2,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ButtonFilled(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.success,
                        shape = SparkTheme.shapes.large,
                    ),
                text = "Success",
                onClick = { },
                intent = ButtonIntent.Success,
            )

            ButtonFilled(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.error,
                        shape = SparkTheme.shapes.large,
                    ),
                text = "Error",
                onClick = { },
                intent = ButtonIntent.Danger,
            )
            ButtonFilled(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.alert,
                        shape = SparkTheme.shapes.large,
                    ),
                text = "Warning",
                onClick = { },
                intent = ButtonIntent.Alert,
            )

            ButtonFilled(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.info,
                        shape = SparkTheme.shapes.large,
                    ),
                text = "Info",
                onClick = { },
                intent = ButtonIntent.Info,
            )
        }
    }
}

@Composable
private fun PulseShapesExample() {
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Pulse animations with different shapes",
            style = SparkTheme.typography.body2,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            ButtonFilled(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.3f,
                        shape = CircleShape,
                    ),
                text = "Circle",
                onClick = { },
                intent = ButtonIntent.Main,
            )

            ButtonFilled(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.3f,
                        shape = SparkTheme.shapes.medium,
                    ),
                text = "Rounded",
                onClick = { },
                intent = ButtonIntent.Main,
            )
        }

        ButtonFilled(
            modifier = Modifier
                .pulse(
                    targetScale = 1.3f,
                    shape = SparkTheme.shapes.large,
                ),
            text = "Large",
            onClick = { },
            intent = ButtonIntent.Main,
        )

        ButtonFilled(
            modifier = Modifier
                .pulse(
                    targetScale = 1.3f,
                    shape = SparkTheme.shapes.small,
                ),
            text = "Small",
            onClick = { },
            intent = ButtonIntent.Main,
        )
    }
}

@Composable
private fun PulseTimingExample() {
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Pulse animations with different timing",
            style = SparkTheme.typography.body2,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            ButtonTinted(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        animationSpec = tween(600),
                    ),
                text = "Fast",
                onClick = { },
                intent = ButtonIntent.Main,
            )

            ButtonTinted(
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        animationSpec = tween(1200),
                    ),
                text = "Normal",
                onClick = { },
                intent = ButtonIntent.Main,
            )
        }
        ButtonTinted(
            modifier = Modifier
                .pulse(
                    targetScale = 1.2f,
                    animationSpec = tween(1800),
                ),
            text = "Slow",
            onClick = { },
            intent = ButtonIntent.Main,
        )

        ButtonTinted(
            modifier = Modifier
                .pulse(
                    targetScale = 1.2f,
                    animationSpec = tween(2400),
                ),
            text = "Very Slow",
            onClick = { },
            intent = ButtonIntent.Main,
        )
    }
}
