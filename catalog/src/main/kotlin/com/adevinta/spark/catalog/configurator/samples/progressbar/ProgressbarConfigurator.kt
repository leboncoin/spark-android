/*
 * Copyright (c) 2023 Adevinta
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
package com.adevinta.spark.catalog.configurator.samples.progressbar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.progressbar.Progressbar
import com.adevinta.spark.components.progressbar.ProgressbarIndeterminate
import com.adevinta.spark.components.progressbar.ProgressbarIntent
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.textfields.TextFieldState
import com.adevinta.spark.components.toggles.SwitchLabelled

public val ProgressbarConfigurator: Configurator = Configurator(
    id = "progressbar",
    name = "Progressbar",
    description = "Progressbar configuration",
    sourceUrl = "$SampleSourceUrl/ProgressbarSamples.kt",
) {
    ProgressbarSample()
}

@Composable
private fun ColumnScope.ProgressbarSample() {
    var isRounded by remember { mutableStateOf(false) }
    var intent by remember { mutableStateOf(ProgressbarIntent.Main) }
    var progress by remember { mutableFloatStateOf(0.5f) }
    val state: TextFieldState? by remember { mutableStateOf(null) }
    val stateMessageText by remember { mutableStateOf("State Message") }

    Text(text = "Progressbar")

    Progressbar(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
        intent = intent,
        isRounded = isRounded,
    )

    Text(text = "ProgressbarIndeterminate")

    ProgressbarIndeterminate(
        intent = intent,
        modifier = Modifier.fillMaxWidth(),
        isRounded = isRounded,
    )

    VerticalSpacer(8.dp)

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.configurator_component_screen_intent_label),
        selectedOption = intent,
        onOptionSelect = {
            intent = it
        },
    )
    VerticalSpacer(8.dp)

    SwitchLabelled(
        checked = isRounded,
        onCheckedChange = {
            isRounded = it
        },
    ) {
        Text(
            text = stringResource(id = R.string.configurator_component_screen_rounded_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    VerticalSpacer(8.dp)

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = progress.toString(),
        onValueChange = { progress = it.toFloat() },
        label = "Change progress",
        helper = "Type to change progress",
        state = state,
        keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number),
        stateMessage = stateMessageText,
    )

    VerticalSpacer(8.dp)
    Text(
        text = "Slide to change progress",
        modifier = Modifier.fillMaxWidth(),
    )

    Slider(
        value = progress,
        onValueChange = { value ->
            progress = value
        },
        enabled = true,
        steps = 7,
    )
}

@Preview
@Composable
private fun ProgressbarSamplePreview() {
    PreviewTheme { ProgressbarSample() }
}
