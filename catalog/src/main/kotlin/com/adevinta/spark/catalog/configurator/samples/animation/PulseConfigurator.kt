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
package com.adevinta.spark.catalog.configurator.samples.animation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.animation.pulse
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

public val PulseConfigurator: List<Configurator> = listOf(
    Configurator(
        id = "pulse",
        name = "Pulse",
        description = "Pulse animation configuration",
        sourceUrl = "$SampleSourceUrl/PulseConfigurator.kt",
    ) { snackbarHostState ->
        PulseSample(snackbarHostState)
    },
)

@Composable
private fun ColumnScope.PulseSample(snackbarHostState: SnackbarHostState) {
    var targetScale by remember { mutableFloatStateOf(2.2f) }
    var initialScale by remember { mutableFloatStateOf(1.0f) }
    var pulseColor by remember { mutableStateOf(PulseColor.Main) }
    var pulseShape by remember { mutableStateOf(PulseShape.Large) }
    var animationDuration by remember { mutableIntStateOf(1000) }
    var isEnabled by remember { mutableStateOf(true) }

    val firstLocale = LocalConfiguration.current.locales[0]

    // Quick and dirty way to restart the animation otherwise when we change the scales they'll become
    // synchronized with the alpha as they'll be restarted
    val coroutineScope = rememberCoroutineScope()
    val restartPulseOnChange = {
        coroutineScope.launch {
            isEnabled = false
            delay(50.milliseconds)
            isEnabled = true
        }
        Unit
    }

    // Preview area
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        ButtonFilled(
            modifier = Modifier.pulse(
                enabled = isEnabled,
                targetScale = targetScale,
                initialScale = initialScale,
                brush = pulseColor.brush,
                shape = pulseShape.shape,
                animationSpec = tween(animationDuration),
            ),
            text = "Pulsing Button",
            onClick = { },
            intent = ButtonIntent.Main,
        )
    }

    // Configuration controls
    SwitchLabelled(
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enable Pulse Animation",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    // Initial Scale Slider
    Text(text = "Initial Scale: ${String.format(locale= firstLocale , format = "%.1f", initialScale)}")

    Slider(
        value = initialScale,
        onValueChange = { initialScale = it },
        onValueChangeFinished = restartPulseOnChange,
        valueRange = 0.1f..5.0f,
        steps = 48, // 0.1 increments from 0.1 to 5.0
        intent = SliderIntent.Basic,
        modifier = Modifier.fillMaxWidth(),
    )

    // Target Scale Slider
    Text(text = "Target Scale: ${String.format(locale= firstLocale , format = "%.1f", targetScale)}")
    Slider(
        value = targetScale,
        onValueChange = { targetScale = it },
        onValueChangeFinished = restartPulseOnChange,
        valueRange = 0.1f..5.0f,
        steps = 48, // 0.1 increments from 0.1 to 5.0
        intent = SliderIntent.Basic,
        modifier = Modifier.fillMaxWidth(),
    )

    // Animation Duration Slider
    Text(text = "Animation Duration: ${animationDuration}ms")
    Slider(
        value = animationDuration.toFloat(),
        onValueChange = { animationDuration = it.toInt() },
        onValueChangeFinished = restartPulseOnChange,
        valueRange = 100f..5000f,
        steps = 48, // ~100ms increments from 100 to 5000
        intent = SliderIntent.Basic,
        modifier = Modifier.fillMaxWidth(),
    )

    DropdownEnum(
        title = "Pulse Color",
        selectedOption = pulseColor,
        onOptionSelect = { pulseColor = it },
    )

    DropdownEnum(
        title = "Pulse Shape",
        selectedOption = pulseShape,
        onOptionSelect = { pulseShape = it },
    )
}

@Preview
@Composable
private fun PulseSamplePreview() {
    PreviewTheme { PulseSample(SnackbarHostState()) }
}

private enum class PulseColor {
    Main {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.main)
    },
    MainContainer {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.mainContainer)
    },
    Accent {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.accent)
    },
    AccentContainer {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.accentContainer)
    },
    Basic {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.basic)
    },
    BasicContainer {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.basicContainer)
    },
    Success {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.success)
    },
    SuccessContainer {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.successContainer)
    },
    Alert {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.alert)
    },
    AlertContainer {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.alertContainer)
    },
    Error {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.error)
    },
    ErrorContainer {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.errorContainer)
    },
    OnSurface {
        override val brush: Brush
            @Composable
            get() = SolidColor(SparkTheme.colors.onSurface)
    },
    AI {
        override val brush: Brush
            @Composable
            get() {
                val galaxyColors = listOf(Color(0xFF26c0d0), Color(0xFF967be7) /*...*/)

                val brush = Brush.radialGradient(
                    colors = galaxyColors,
                )
                return brush
            }
    },
    ;

    @get:Composable
    abstract val brush: Brush
}

private enum class PulseShape {
    None {
        override val shape: Shape
            @Composable
            get() = SparkTheme.shapes.none
    },
    Small {
        override val shape: Shape
            @Composable
            get() = SparkTheme.shapes.small
    },
    Medium {
        override val shape: Shape
            @Composable
            get() = SparkTheme.shapes.medium
    },
    Large {
        override val shape: Shape
            @Composable
            get() = SparkTheme.shapes.large
    },
    ExtraLarge {
        override val shape: Shape
            @Composable
            get() = SparkTheme.shapes.extraLarge
    },
    Full {
        override val shape: Shape
            @Composable
            get() = SparkTheme.shapes.full
    },
    ;

    @get:Composable
    abstract val shape: Shape
}
