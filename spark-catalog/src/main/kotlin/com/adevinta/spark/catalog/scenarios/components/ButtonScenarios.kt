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
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import kotlinx.coroutines.delay

internal val buttonScenarios: Map<String, @Composable () -> Unit> = mapOf(
    "button-static" to { ButtonStaticScenario() },
    "button-intent" to { ButtonIntentScenario() },
    "button-loading" to { ButtonLoadingScenario() },
)

@Composable
private fun ButtonStaticScenario() {
    ButtonFilled(onClick = {}, text = "Static button")
}

/** Cycles the button intent on a fixed cadence to stress recomposition on colour change. */
@Composable
private fun ButtonIntentScenario() {
    val intents = ButtonIntent.entries
    var index by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(200)
            index = (index + 1) % intents.size
        }
    }
    ButtonFilled(onClick = {}, intent = intents[index], text = "Intent ${intents[index]}")
}

/** Toggles the loading flag to stress the spinner swap. */
@Composable
private fun ButtonLoadingScenario() {
    var loading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(400)
            loading = !loading
        }
    }
    ButtonFilled(onClick = {}, isLoading = loading, text = "Loading button")
}
