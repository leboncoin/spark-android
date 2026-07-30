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
package com.adevinta.spark.catalog.scenarios

import androidx.compose.runtime.Composable
import com.adevinta.spark.catalog.scenarios.components.buttonScenarios
import com.adevinta.spark.catalog.scenarios.components.comboBoxScenarios
import com.adevinta.spark.catalog.scenarios.components.iconButtonScenarios
import com.adevinta.spark.catalog.scenarios.components.placeholderScenarios
import com.adevinta.spark.catalog.scenarios.components.ratingScenarios
import com.adevinta.spark.catalog.scenarios.components.stepperScenarios

/**
 * Registry of performance benchmark scenarios, keyed by id.
 *
 * Scenarios are self-contained composables split into per-component files under
 * [com.adevinta.spark.catalog.scenarios.components]. They do NOT reuse catalog examples.
 * Each scenario drives its own state so runs are deterministic, except the ComboBox filter one
 * which waits for real UiAutomator input.
 *
 * The id is also the trace section name suffix (`Spark::<id>`). Launch a scenario via
 * [com.adevinta.spark.catalog.BenchmarkActivity] with the `scenario_id` extra.
 */
internal val benchmarkScenarios: Map<String, @Composable () -> Unit> = buildMap {
    putAll(buttonScenarios)
    putAll(stepperScenarios)
    putAll(comboBoxScenarios)
    putAll(ratingScenarios)
    putAll(iconButtonScenarios)
    putAll(placeholderScenarios)
}
