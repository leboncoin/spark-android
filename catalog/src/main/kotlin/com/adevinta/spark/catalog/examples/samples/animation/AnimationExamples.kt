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

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.animation.AnimatedCounterText
import com.adevinta.spark.animation.ShakeConfig
import com.adevinta.spark.animation.pulse
import com.adevinta.spark.animation.rememberShakeController
import com.adevinta.spark.animation.shake
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.Primary
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.components.buttons.Tertiary
import com.adevinta.spark.components.iconbuttons.IconButtonOutlined
import com.adevinta.spark.components.meter.Meter
import com.adevinta.spark.components.meter.MeterIntent
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.components.stepper.Stepper
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.Minus
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.tokens.Layout
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val AnimationsExampleSourceUrl = "$SampleSourceUrl/animation/AnimationExamples.kt"
public val AnimationExamples: ImmutableList<Example> = persistentListOf(
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
    Example(
        id = "shake-error",
        name = "Shake on error",
        description = "Shake a text field when the user submits it empty",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        ShakeErrorExample()
    },
    Example(
        id = "shake-presets",
        name = "Shake presets",
        description = "Shake, vibrate, scale, and rotate configurations",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        ShakePresetsExample()
    },
    Example(
        id = "shake-retrigger",
        name = "Interrupt a shake",
        description = "Trigger a new shake while the previous one is still running",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        ShakeRetriggerExample()
    },
    Example(
        id = "counter-basic",
        name = "Animated counter",
        description = "Digits slide in when a value goes up or down",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        CounterBasicExample()
    },
    Example(
        id = "counter-formatted",
        name = "Formatted values",
        description = "Animated counters for integers, percentages, and prices",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        CounterFormattedExample()
    },
    Example(
        id = "counter-components",
        name = "Counter in components",
        description = "Components that animate their value with an animated counter",
        sourceUrl = AnimationsExampleSourceUrl,
    ) {
        CounterComponentsExample()
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
            Button.Primary(
                onClick = { },
                text = "Pulsing Button",
                modifier = Modifier.pulse(
                    targetScale = 1.3f,
                    color = SparkTheme.colors.main,
                    shape = SparkTheme.shapes.large,
                ),
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
            Button.Success(
                onClick = { },
                text = "Success",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.success,
                        shape = SparkTheme.shapes.large,
                    ),
            )

            Button.Danger(
                onClick = { },
                text = "Error",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.error,
                        shape = SparkTheme.shapes.large,
                    ),
            )
            Button.Tertiary(
                onClick = { },
                text = "Warning",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.alert,
                        shape = SparkTheme.shapes.large,
                    ),
            )

            Button.Tertiary(
                onClick = { },
                text = "Info",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        color = SparkTheme.colors.info,
                        shape = SparkTheme.shapes.large,
                    ),
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
            Button.Primary(
                onClick = { },
                text = "Circle",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.3f,
                        shape = CircleShape,
                    ),
            )

            Button.Primary(
                onClick = { },
                text = "Rounded",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.3f,
                        shape = SparkTheme.shapes.medium,
                    ),
            )
        }

        Button.Primary(
            onClick = { },
            text = "Large",
            modifier = Modifier
                .pulse(
                    targetScale = 1.3f,
                    shape = SparkTheme.shapes.large,
                ),
        )

        Button.Primary(
            onClick = { },
            text = "Small",
            modifier = Modifier
                .pulse(
                    targetScale = 1.3f,
                    shape = SparkTheme.shapes.small,
                ),
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
            Button.Tertiary(
                onClick = { },
                text = "Fast",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        animationSpec = tween(600),
                    ),
            )

            Button.Tertiary(
                onClick = { },
                text = "Normal",
                modifier = Modifier
                    .pulse(
                        targetScale = 1.2f,
                        animationSpec = tween(1200),
                    ),
            )
        }
        Button.Tertiary(
            onClick = { },
            text = "Slow",
            modifier = Modifier
                .pulse(
                    targetScale = 1.2f,
                    animationSpec = tween(1800),
                ),
        )

        Button.Tertiary(
            onClick = { },
            text = "Very Slow",
            modifier = Modifier
                .pulse(
                    targetScale = 1.2f,
                    animationSpec = tween(2400),
                ),
        )
    }
}

@Composable
private fun ShakeErrorExample() {
    val controller = rememberShakeController()
    val state = rememberTextFieldState()
    var showError by remember { mutableStateOf(false) }
    val isError = showError && state.text.isBlank()
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Submit the empty field to see it shake. The error message carries the meaning, " +
                "the shake only draws the eye to it.",
            style = SparkTheme.typography.body2,
        )

        TextField(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .shake(controller),
            label = "Email",
            status = if (isError) FormFieldStatus.Error else null,
            statusMessage = if (isError) "Enter your email to continue" else null,
        )

        Button.Primary(
            onClick = {
                if (state.text.isBlank()) {
                    showError = true
                    controller.shake(
                        ShakeConfig(
                            iterations = 4,
                            intensity = 2_000f,
                            translateX = 30f,
                        ),
                    )
                }
            },
            text = "Submit",
        )
    }
}

@Composable
private fun ShakePresetsExample() {
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Each button builds its shake configuration on tap, so every tap animates again",
            style = SparkTheme.typography.body2,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var isValid by remember { mutableStateOf(true) }
            val shakeController = rememberShakeController()
            val onShakeClick = {
                shakeController.shake(
                    if (isValid) {
                        ShakeConfig(
                            iterations = 4,
                            intensity = 2_000f,
                            rotateY = 15f,
                            translateX = 40f,
                        )
                    } else {
                        ShakeConfig(
                            iterations = 4,
                            intensity = 1_000f,
                            rotateX = -20f,
                            translateY = 20f,
                        )
                    },
                )
                isValid = !isValid
            }
            // The new API encodes the intent in the composable name, so the variant switches with the state.
            if (isValid) {
                Button.Success(
                    onClick = onShakeClick,
                    text = "Shake me",
                    modifier = Modifier.shake(shakeController),
                )
            } else {
                Button.Danger(
                    onClick = onShakeClick,
                    text = "Shake me",
                    modifier = Modifier.shake(shakeController),
                )
            }

            val vibrateController = rememberShakeController()
            Button.Danger(
                onClick = {
                    vibrateController.shake(
                        ShakeConfig(
                            iterations = 8,
                            intensity = 100_000f,
                            translateX = -15f,
                        ),
                    )
                },
                text = "Vibrate me",
                modifier = Modifier.shake(vibrateController),
            )

            val scaleController = rememberShakeController()
            Button.Tertiary(
                onClick = {
                    scaleController.shake(
                        ShakeConfig(
                            iterations = 1,
                            intensity = 100f,
                            scaleX = 0.5f,
                        ),
                    )
                },
                text = "Scale me",
                modifier = Modifier.shake(scaleController),
            )

            val rotateController = rememberShakeController()
            Button.Tertiary(
                onClick = {
                    rotateController.shake(
                        ShakeConfig(
                            iterations = 2,
                            intensity = 1_000f,
                            rotate = 90f,
                        ),
                    )
                },
                text = "Rotate me",
                modifier = Modifier.shake(rotateController),
            )
        }
    }
}

@Composable
private fun ShakeRetriggerExample() {
    val controller = rememberShakeController()
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Tap Shake again while the square moves: the new shake restarts from the current position " +
                "instead of waiting for the first one to end",
            style = SparkTheme.typography.body2,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .shake(controller)
                    .size(96.dp)
                    .background(
                        color = SparkTheme.colors.mainContainer,
                        shape = SparkTheme.shapes.large,
                    ),
            )

            Button.Primary(
                onClick = {
                    controller.shake(
                        ShakeConfig(
                            iterations = 10,
                            intensity = Spring.StiffnessLow,
                            translateX = 30f,
                        ),
                    )
                },
                text = "Shake",
            )
        }
    }
}

@Composable
private fun CounterBasicExample() {
    var count by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Changed digits slide in from the top when the value goes up, and from the bottom " +
                "when it goes down",
            style = SparkTheme.typography.body2,
        )

        CounterControls(
            onDecrease = { count-- },
            onIncrease = { count++ },
        ) {
            AccessibleCounterText(text = count.toString())
        }
    }
}

@Composable
private fun CounterFormattedExample() {
    var value by remember { mutableIntStateOf(49) }
    val firstLocale = LocalConfiguration.current.locales[0]
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "One value formatted as an integer, a percentage, and a price. Only the characters " +
                "that change animate",
            style = SparkTheme.typography.body2,
        )

        CounterControls(
            onDecrease = { value = (value - 1).coerceAtLeast(0) },
            onIncrease = { value = (value + 1).coerceAtMost(100) },
            decreaseEnabled = value > 0,
            increaseEnabled = value < 100,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AccessibleCounterText(
                    text = String.format(locale = firstLocale, format = "%d", value),
                )
                AccessibleCounterText(
                    text = String.format(locale = firstLocale, format = "%d%%", value),
                )
                AccessibleCounterText(
                    text = String.format(locale = firstLocale, format = "€%d", value),
                )
            }
        }
    }
}

@Composable
private fun CounterComponentsExample() {
    var value by remember { mutableIntStateOf(40) }
    Column(
        modifier = Modifier.padding(Layout.bodyMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Stepper and Meter animate their value with an animated counter and provide their own " +
                "accessibility semantics",
            style = SparkTheme.typography.body2,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Stepper.Nudger(
                value = value,
                onValueChange = { value = it },
                range = 0..100,
                step = 5,
            )

            Meter.Circular(
                value = value.toFloat(),
                content = CircularMeterContent.ValueLabel(label = "Progress"),
                intent = MeterIntent.Support,
                size = CircleMeterSize.Large,
            )
        }
    }
}

@Composable
private fun CounterControls(
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
    decreaseEnabled: Boolean = true,
    increaseEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButtonOutlined(
            icon = LeboncoinIcons.Minus,
            onClick = onDecrease,
            enabled = decreaseEnabled,
            contentDescription = "Decrease",
        )
        content()
        IconButtonOutlined(
            icon = LeboncoinIcons.Plus,
            onClick = onIncrease,
            enabled = increaseEnabled,
            contentDescription = "Increase",
        )
    }
}

/**
 * [AnimatedCounterText] renders one text node per character, so TalkBack would read the value digit by
 * digit. Replace those nodes with a single description and announce each change politely.
 */
@Composable
private fun AccessibleCounterText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = SparkTheme.typography.headline1,
) {
    AnimatedCounterText(
        text = text,
        modifier = modifier.clearAndSetSemantics {
            contentDescription = text
            liveRegion = LiveRegionMode.Polite
        },
        style = style,
    )
}
