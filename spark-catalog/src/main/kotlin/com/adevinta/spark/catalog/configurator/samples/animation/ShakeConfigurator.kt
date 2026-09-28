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

import androidx.compose.animation.core.Spring
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.animation.ShakeConfig
import com.adevinta.spark.animation.rememberShakeController
import com.adevinta.spark.animation.shake
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SourceUrl
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.Primary
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent
import com.adevinta.spark.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

private const val AnimationConfiguratorSourceUrl =
    "$SourceUrl/blob/main/catalog/src/main/kotlin/com/adevinta/spark/catalog/configurator/samples/animation"

internal val ShakeConfigurator: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "shake",
        name = "Shake",
        description = "Shake animation configuration",
        sourceUrl = "$AnimationConfiguratorSourceUrl/ShakeConfigurator.kt",
    ) { _, _ ->
        ShakeSample()
    },
)

@Composable
private fun ColumnScope.ShakeSample() {
    val defaultPreset = ShakePreset.Shake
    var preset by remember { mutableStateOf(defaultPreset) }
    var iterations by remember { mutableIntStateOf(defaultPreset.iterations) }
    var intensity by remember { mutableStateOf(defaultPreset.intensity) }
    var translateX by remember { mutableFloatStateOf(defaultPreset.translateX) }
    var translateY by remember { mutableFloatStateOf(defaultPreset.translateY) }
    var rotate by remember { mutableFloatStateOf(defaultPreset.rotate) }
    var rotateX by remember { mutableFloatStateOf(defaultPreset.rotateX) }
    var rotateY by remember { mutableFloatStateOf(defaultPreset.rotateY) }
    var scaleX by remember { mutableFloatStateOf(defaultPreset.scaleX) }
    var scaleY by remember { mutableFloatStateOf(defaultPreset.scaleY) }

    val shakeController = rememberShakeController()
    val firstLocale = LocalConfiguration.current.locales[0]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Button.Primary(
            onClick = {
                // Build the config on every tap: its default trigger timestamp makes each tap a new key,
                // so the shake replays even when no control has changed.
                shakeController.shake(
                    ShakeConfig(
                        iterations = iterations,
                        intensity = intensity.stiffness,
                        rotate = rotate,
                        rotateX = rotateX,
                        rotateY = rotateY,
                        scaleX = scaleX,
                        scaleY = scaleY,
                        translateX = translateX,
                        translateY = translateY,
                    ),
                )
            },
            text = "Shake me",
            modifier = Modifier.shake(shakeController),
        )
    }

    DropdownEnum(
        title = "Preset",
        selectedOption = preset,
        onOptionSelect = {
            preset = it
            iterations = it.iterations
            intensity = it.intensity
            translateX = it.translateX
            translateY = it.translateY
            rotate = it.rotate
            rotateX = it.rotateX
            rotateY = it.rotateY
            scaleX = it.scaleX
            scaleY = it.scaleY
        },
    )

    LabelledSlider(
        label = "Iterations: $iterations",
        value = iterations.toFloat(),
        onValueChange = { iterations = it.roundToInt() },
        valueRange = 0f..12f,
        steps = 11, // 1 increments from 0 to 12
    )

    DropdownEnum(
        title = "Intensity (stiffness ${String.format(locale = firstLocale, format = "%.0f", intensity.stiffness)})",
        selectedOption = intensity,
        onOptionSelect = { intensity = it },
    )

    LabelledSlider(
        label = "Translate X: ${String.format(locale = firstLocale, format = "%.0f", translateX)} px",
        value = translateX,
        onValueChange = { translateX = it },
        valueRange = -60f..60f,
        steps = 23, // 5 px increments from -60 to 60
    )

    LabelledSlider(
        label = "Translate Y: ${String.format(locale = firstLocale, format = "%.0f", translateY)} px",
        value = translateY,
        onValueChange = { translateY = it },
        valueRange = -60f..60f,
        steps = 23, // 5 px increments from -60 to 60
    )

    LabelledSlider(
        label = "Rotate: ${String.format(locale = firstLocale, format = "%.0f", rotate)}°",
        value = rotate,
        onValueChange = { rotate = it },
        valueRange = -90f..90f,
        steps = 35, // 5 degree increments from -90 to 90
    )

    LabelledSlider(
        label = "Rotate X: ${String.format(locale = firstLocale, format = "%.0f", rotateX)}°",
        value = rotateX,
        onValueChange = { rotateX = it },
        valueRange = -45f..45f,
        steps = 17, // 5 degree increments from -45 to 45
    )

    LabelledSlider(
        label = "Rotate Y: ${String.format(locale = firstLocale, format = "%.0f", rotateY)}°",
        value = rotateY,
        onValueChange = { rotateY = it },
        valueRange = -45f..45f,
        steps = 17, // 5 degree increments from -45 to 45
    )

    // Scale is capped at 0.5: the shake multiplies it by a value in [-1, 1], so 1 or more flips the target.
    LabelledSlider(
        label = "Scale X: ${String.format(locale = firstLocale, format = "%.2f", scaleX)}",
        value = scaleX,
        onValueChange = { scaleX = it },
        valueRange = 0f..0.5f,
        steps = 9, // 0.05 increments from 0 to 0.5
    )

    LabelledSlider(
        label = "Scale Y: ${String.format(locale = firstLocale, format = "%.2f", scaleY)}",
        value = scaleY,
        onValueChange = { scaleY = it },
        valueRange = 0f..0.5f,
        steps = 9, // 0.05 increments from 0 to 0.5
    )
}

@Composable
private fun LabelledSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
) {
    Text(text = label)
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        intent = SliderIntent.Support,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun ShakeSamplePreview() {
    PreviewTheme { ShakeSample() }
}

private enum class ShakeIntensity(val stiffness: Float) {
    VeryLow(Spring.StiffnessVeryLow),
    Soft(100f),
    Low(Spring.StiffnessLow),
    MediumLow(Spring.StiffnessMediumLow),
    Medium(1_000f),
    MediumHigh(2_000f),
    High(Spring.StiffnessHigh),
    Max(100_000f),
}

private enum class ShakePreset(
    val iterations: Int,
    val intensity: ShakeIntensity,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val rotate: Float = 0f,
    val rotateX: Float = 0f,
    val rotateY: Float = 0f,
    val scaleX: Float = 0f,
    val scaleY: Float = 0f,
) {
    Shake(iterations = 4, intensity = ShakeIntensity.MediumHigh, translateX = 40f, rotateY = 15f),
    Vibrate(iterations = 8, intensity = ShakeIntensity.Max, translateX = -15f),
    Scale(iterations = 1, intensity = ShakeIntensity.Soft, scaleX = 0.5f),
    Rotate(iterations = 2, intensity = ShakeIntensity.Medium, rotate = 90f),
}
