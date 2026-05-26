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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Components
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.text.Text

/**
 * Activity that renders all Spark component examples in a single scrollable list.
 * Used exclusively by macrobenchmarks to capture baseline profile rules
 * for every component without navigating the full catalog UI.
 *
 * Launched via intent action: com.adevinta.spark.catalog.BENCHMARK
 */
internal class BenchmarkActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SparkTheme {
                BenchmarkComponentShowcase()
            }
        }
    }
}

@Composable
private fun BenchmarkComponentShowcase() {
    val snackbarHostState = remember { SnackbarHostState() }
    val allExamples = remember {
        Components.flatMap { component ->
            component.examples.map { example -> component.name to example }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("benchmark_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = allExamples,
            key = { (componentName, example) -> "$componentName-${example.id}" },
        ) { (componentName, example) ->
            BenchmarkExampleItem(componentName = componentName, example = example, snackbarHostState = snackbarHostState)
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