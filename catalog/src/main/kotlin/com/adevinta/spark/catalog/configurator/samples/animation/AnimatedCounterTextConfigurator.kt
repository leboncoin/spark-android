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
package com.adevinta.spark.catalog.configurator.samples.animation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.animation.AnimatedCounterText
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.ColorSelector
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SourceUrl
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.slider.SliderIntent
import com.adevinta.spark.components.stepper.Stepper
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

private const val AnimationConfiguratorSourceUrl =
    "$SourceUrl/blob/main/catalog/src/main/kotlin/com/adevinta/spark/catalog/configurator/samples/animation"

internal val AnimatedCounterTextConfigurator: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "animated-counter-text",
        name = "AnimatedCounterText",
        description = "Animated counter configuration",
        sourceUrl = "$AnimationConfiguratorSourceUrl/AnimatedCounterTextConfigurator.kt",
    ) { _, _ ->
        AnimatedCounterTextSample()
    },
)

@Composable
private fun ColumnScope.AnimatedCounterTextSample() {
    var value by remember { mutableIntStateOf(120) }
    var step by remember { mutableStateOf(CounterStep.One) }
    var format by remember { mutableStateOf(CounterFormat.Plain) }
    var animation by remember { mutableStateOf(CounterAnimation.DefaultSpring) }
    var durationMs by remember { mutableIntStateOf(300) }
    var style by remember { mutableStateOf(CounterStyle.Display2) }
    var color by remember { mutableStateOf(Color.Unspecified) }
    val customTextState = rememberTextFieldState("€120")

    val firstLocale = LocalConfiguration.current.locales[0]

    val text = when (format) {
        CounterFormat.Plain -> String.format(locale = firstLocale, format = "%d", value)
        CounterFormat.Grouped -> String.format(locale = firstLocale, format = "%,d", value)
        CounterFormat.Percent -> String.format(locale = firstLocale, format = "%d%%", value)
        CounterFormat.Currency -> String.format(locale = firstLocale, format = "€%d", value)
        CounterFormat.Decimal -> String.format(locale = firstLocale, format = "%.2f", value / 100.0)
        CounterFormat.CustomText -> customTextState.text.toString()
    }

    val animationSpec: FiniteAnimationSpec<IntOffset> = remember(animation, durationMs) {
        when (animation) {
            CounterAnimation.DefaultSpring -> spring(visibilityThreshold = IntOffset.VisibilityThreshold)

            // Mirrors the internal StepperDefaults.textAnimationSpec.
            CounterAnimation.StepperSpring -> spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            )

            CounterAnimation.Bouncy -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            )

            CounterAnimation.LowStiffness -> spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            )

            CounterAnimation.Tween -> tween(durationMillis = durationMs)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        // One node announcing the full string, instead of one node per character.
        AnimatedCounterText(
            text = text,
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = text
                liveRegion = LiveRegionMode.Polite
            },
            style = style.style,
            color = color,
            animationSpec = animationSpec,
        )
    }

    Text(text = "Value")
    Stepper.Nudger(
        value = value,
        onValueChange = { value = it },
        range = -1000..10_000,
        step = step.value,
    )

    ButtonGroup(
        title = "Step",
        selectedOption = step,
        onOptionSelect = { step = it },
    )

    DropdownEnum(
        title = "Format",
        selectedOption = format,
        onOptionSelect = { format = it },
    )

    if (format == CounterFormat.CustomText) {
        TextField(
            state = customTextState,
            label = "Custom text",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DropdownEnum(
        title = "Animation spec",
        selectedOption = animation,
        onOptionSelect = { animation = it },
    )

    if (animation == CounterAnimation.Tween) {
        Text(text = "Duration: ${durationMs}ms")
        Slider(
            value = durationMs.toFloat(),
            onValueChange = { durationMs = it.roundToInt() },
            valueRange = 100f..1000f,
            steps = 17, // 50ms increments from 100 to 1000
            intent = SliderIntent.Support,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DropdownEnum(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
    )

    ColorSelector(
        title = "Color",
        selectedColor = color,
        onColorSelected = { color = it ?: Color.Unspecified },
    )
}

@Preview
@Composable
private fun AnimatedCounterTextSamplePreview() {
    PreviewTheme { AnimatedCounterTextSample() }
}

private enum class CounterStep(val value: Int) {
    One(1),
    Ten(10),
    Hundred(100),
}

private enum class CounterFormat {
    Plain,
    Grouped,
    Percent,
    Currency,
    Decimal,
    CustomText,
}

private enum class CounterAnimation {
    DefaultSpring,
    StepperSpring,
    Bouncy,
    LowStiffness,
    Tween,
}

private enum class CounterStyle {
    Display1 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.display1
    },
    Display2 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.display2
    },
    Display3 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.display3
    },
    Headline1 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.headline1
    },
    Headline2 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.headline2
    },
    Subhead {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.subhead
    },
    Body1 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.body1
    },
    Body2 {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.body2
    },
    Caption {
        override val style: TextStyle
            @Composable
            get() = SparkTheme.typography.caption
    },
    ;

    @get:Composable
    abstract val style: TextStyle
}
