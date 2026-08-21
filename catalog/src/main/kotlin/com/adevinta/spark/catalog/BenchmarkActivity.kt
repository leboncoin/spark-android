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
package com.adevinta.spark.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Components
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.scenarios.benchmarkScenarios
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.text.Text

/**
 * Activity that renders Spark component examples for macrobenchmarks.
 *
 * Modes:
 * - All components (no extras): renders every example in a scrollable Column.
 *   Used for baseline profile generation.
 * - Single component (extras: EXTRA_COMPONENT_ID, EXTRA_EXAMPLE_INDEX):
 *   renders one example at a time. Used for per-component benchmarks.
 *
 * Launched via intent action: com.adevinta.spark.catalog.BENCHMARK
 */
internal class BenchmarkActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scenarioId = intent.getStringExtra(EXTRA_SCENARIO_ID)
        val componentId = intent.getStringExtra(EXTRA_COMPONENT_ID)
        val exampleIndex = intent.getIntExtra(EXTRA_EXAMPLE_INDEX, 0)

        setContent {
            SparkTheme {
                when {
                    scenarioId != null -> ScenarioShowcase(scenarioId)
                    componentId != null -> SingleExampleShowcase(componentId, exampleIndex)
                    else -> AllComponentsShowcase()
                }
            }
        }
    }

    companion object {
        const val EXTRA_COMPONENT_ID = "component_id"
        const val EXTRA_EXAMPLE_INDEX = "example_index"
        const val EXTRA_SCENARIO_ID = "scenario_id"
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScenarioShowcase(scenarioId: String) {
    val scenario = remember(scenarioId) {
        benchmarkScenarios[scenarioId]
            ?: error("Unknown benchmark scenario: $scenarioId")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true }
            .testTag("benchmark_scenario"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        trace("Spark::$scenarioId") {
            scenario()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SingleExampleShowcase(componentId: String, exampleIndex: Int) {
    val snackbarHostState = remember { SnackbarHostState() }
    val component = remember { Components.first { it.id == componentId } }
    val example = remember { component.examples[exampleIndex] }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true }
            .testTag("benchmark_example"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        trace("Spark::${component.name}") {
            BenchmarkExampleItem(
                componentName = component.name,
                example = example,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AllComponentsShowcase() {
    val snackbarHostState = remember { SnackbarHostState() }
    val allExamples = remember {
        Components.flatMap { component ->
            component.examples.map { example -> component.name to example }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .semantics { testTagsAsResourceId = true }
            .testTag("benchmark_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        allExamples.forEach { (componentName, example) ->
            key("$componentName-${example.id}") {
                trace("Spark::$componentName") {
                    BenchmarkExampleItem(
                        componentName = componentName,
                        example = example,
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }
}

@Composable
private fun BenchmarkExampleItem(
    componentName: String,
    example: Example,
    snackbarHostState: SnackbarHostState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "$componentName - ${example.name}",
            style = SparkTheme.typography.body2,
        )
        example.content(this, snackbarHostState)
    }
}
