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
package com.adevinta.spark.components.stepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

/**
 * Documentation screenshots for the Stepper component, following Material Design's approach of
 * showing each variant with light and dark theme side by side.
 */
internal class StepperDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun nudger() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Stepper.Nudger(value = 3, onValueChange = {}, suffix = "kg")
            Stepper.Nudger(value = null, onValueChange = {})
            Stepper.Nudger(value = 3, onValueChange = {}, enabled = false)
        }
    }

    @Test
    fun nudgerForm() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Stepper.NudgerForm(
                modifier = Modifier.width(IntrinsicSize.Min),
                value = 3,
                onValueChange = {},
                label = "Quantity",
                helper = "Helper text",
                required = true,
            )
            Stepper.NudgerForm(
                modifier = Modifier.width(IntrinsicSize.Min),
                value = 3,
                onValueChange = {},
                label = "Quantity",
                helper = "Helper text",
                status = FormFieldStatus.Error,
                statusMessage = "Error message",
            )
        }
    }

    @Test
    fun input() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Stepper.Input(value = 3, onValueChange = {}, range = 0..10, suffix = "kg")
            Stepper.Input(value = null, onValueChange = {}, range = 0..10)
            Stepper.Input(value = 3, onValueChange = {}, range = 0..10, enabled = false)
            Stepper.Input(value = 3, onValueChange = {}, range = 0..10, status = FormFieldStatus.Error)
        }
    }

    @Test
    fun inputForm() = paparazzi.sparkDocSnapshot {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Stepper.InputForm(
                modifier = Modifier.width(IntrinsicSize.Min),
                value = 3,
                onValueChange = {},
                label = "Quantity",
                helper = "Helper text",
                range = 0..10,
                required = true,
            )
            Stepper.InputForm(
                modifier = Modifier.width(IntrinsicSize.Min),
                value = 3,
                onValueChange = {},
                label = "Quantity",
                helper = "Error",
                range = 0..10,
                status = FormFieldStatus.Error,
                statusMessage = "Error message",
            )
        }
    }
}
