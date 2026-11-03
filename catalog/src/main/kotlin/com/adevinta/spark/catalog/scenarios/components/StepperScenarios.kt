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
package com.adevinta.spark.catalog.scenarios.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.stepper.Stepper
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

internal val stepperScenarios: Map<String, @Composable () -> Unit> = mapOf(
    "stepper-nudger-animate" to { StepperAnimateScenario() },
    "stepper-input-animate" to { StepperInputAnimateScenario() },
)

/** Increments and decrements to trigger the value text slide animation. */
@Composable
private fun StepperAnimateScenario() {
    var value by remember { mutableIntStateOf(0) }
    var goingUp by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(300.milliseconds)
            if (value >= 10) goingUp = false
            if (value <= 0) goingUp = true
            value += if (goingUp) 1 else -1
        }
    }
    Stepper.Nudger(value = value, onValueChange = { value = it }, modifier = Modifier, range = 0..10)
}

/** Increments and decrements to trigger the value text slide animation. */
@Composable
private fun StepperInputAnimateScenario() {
    var value by remember { mutableIntStateOf(0) }
    var goingUp by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(300.milliseconds)
            if (value >= 10) goingUp = false
            if (value <= 0) goingUp = true
            value += if (goingUp) 1 else -1
        }
    }
    Stepper.Input(
        value = value,
        onValueChange = {
            if (it != null) {
                value = it
            }
        },
        modifier = Modifier,
        range = 0..10,
    )
}
