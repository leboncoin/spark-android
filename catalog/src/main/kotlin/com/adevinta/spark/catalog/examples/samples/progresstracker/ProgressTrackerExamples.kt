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
package com.adevinta.spark.catalog.examples.samples.progresstracker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.progress.tracker.ProgressSizes
import com.adevinta.spark.components.progress.tracker.ProgressStep
import com.adevinta.spark.components.progress.tracker.ProgressTrackerColumn
import com.adevinta.spark.components.progress.tracker.ProgressTrackerIntent
import com.adevinta.spark.components.progress.tracker.ProgressTrackerRow
import kotlinx.collections.immutable.persistentListOf

private const val ProgressTrackerExampleSourceUrl = "$SampleSourceUrl/ProgressTrackerSamples.kt"

public val ProgressTrackerExamples: List<Example> = listOf(
    Example(
        id = "default",
        name = "Default",
        description = "Step indicator content defaults to step index, or checkmark icon when completed.",
        sourceUrl = ProgressTrackerExampleSourceUrl,
    ) {
        ProgressTrackerDefault()
    },
    Example(
        id = "controlled",
        name = "Controlled",
        description = "Use `onStepClick` param to control component's state.",
        sourceUrl = ProgressTrackerExampleSourceUrl,
    ) {
        ProgressTrackerControlled()
    },
    Example(
        id = "disabled",
        name = "Disabled",
        description = "Use `enabled` param on a `Step` to disable it.",
        sourceUrl = ProgressTrackerExampleSourceUrl,
    ) {
        ProgressTrackerDisabled()
    },
    Example(
        id = "sizes",
        name = "Sizes",
        description = "Use `size` param to set the size of the progress indicators. Only the large one is clickable.",
        sourceUrl = ProgressTrackerExampleSourceUrl,
    ) {
        ProgressTrackerSizes()
    },
    Example(
        id = "intent",
        name = "Intent",
        description = "Use `intent` param to set the color of the progress indicators.",
        sourceUrl = ProgressTrackerExampleSourceUrl,
    ) {
        ProgressTrackerColors()
    },
)

@PreviewLightDark
@Composable
private fun ColumnScope.ProgressTrackerDefault() {
    val selectedStep by remember { mutableIntStateOf(1) }
    val items = persistentListOf(
        ProgressStep("Lorem ipsume", true),
        ProgressStep("Lorem ipsume dolar sit amet", true),
        ProgressStep("Lorem ipsume", true),
    )
    ProgressTrackerRow(
        items = items,
        selectedStep = selectedStep,
    )
    ProgressTrackerColumn(
        items = persistentListOf(
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation.",
                true,
            ),
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.",
                true,
            ),
            ProgressStep("Lorem ipsume dolar sit amet", true),
        ),
        selectedStep = selectedStep,
    )
    ProgressTrackerRow(
        items = items,
        hasIndicatorContent = false,
        selectedStep = selectedStep,
    )
    ProgressTrackerColumn(
        items = persistentListOf(
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation.",
                true,
            ),
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.",
                true,
            ),
            ProgressStep("Lorem ipsume dolar sit amet", true),
        ),
        hasIndicatorContent = false,
        selectedStep = selectedStep,
    )
}

@Composable
@Preview
private fun ColumnScope.ProgressTrackerControlled() {
    var selectedStep by remember { mutableIntStateOf(0) }
    val items = persistentListOf(
        ProgressStep("Lorem ipsume", true),
        ProgressStep("Lorem ipsume dolar sit amet", true),
        ProgressStep("Lorem ipsume", false),
    )
    ProgressTrackerRow(
        items = items,
        onStepClick = {
            selectedStep = it
        },
        selectedStep = selectedStep,
    )
    ProgressTrackerColumn(
        items = persistentListOf(
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation.",
                true,
            ),
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.",
                true,
            ),
            ProgressStep("Lorem ipsume dolar sit amet", true),
        ),
        hasIndicatorContent = false,
        selectedStep = selectedStep,
        onStepClick = {
            selectedStep = it
        },
    )
}

@Composable
@Preview
private fun ColumnScope.ProgressTrackerDisabled() {
    var selectedStep by remember { mutableIntStateOf(0) }
    val items = persistentListOf(
        ProgressStep("Lorem ipsume", true),
        ProgressStep("Lorem ipsume dolar sit amet", false),
        ProgressStep("Lorem ipsume", true),
    )
    ProgressTrackerRow(
        items = items,
        onStepClick = {
            selectedStep = it
        },
        selectedStep = selectedStep,
    )
    ProgressTrackerColumn(
        items = persistentListOf(
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation.",
                true,
            ),
            ProgressStep(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                    "ut labore et dolore magna aliqua.",
                false,
            ),
            ProgressStep("Lorem ipsume dolar sit amet", true),
        ),
        selectedStep = selectedStep,
        onStepClick = {
            selectedStep = it
        },
    )
}

@Composable
@Preview
private fun ColumnScope.ProgressTrackerSizes() {
    val selectedStep by remember { mutableIntStateOf(1) }
    val items = persistentListOf(
        ProgressStep("", true),
        ProgressStep("", true),
        ProgressStep("", false),
    )
    for (size in ProgressSizes.entries) {
        ProgressTrackerRow(
            items = items,
            size = size,
            selectedStep = selectedStep,
        )
    }
    Row {
        for (size in ProgressSizes.entries) {
            ProgressTrackerColumn(
                items = persistentListOf(
                    ProgressStep("a", true),
                    ProgressStep("a", true),
                    ProgressStep("a", false),
                ),
                size = size,
                selectedStep = selectedStep,
            )
        }
    }
}

@Composable
@Preview
private fun ColumnScope.ProgressTrackerColors() {
    val hScrollState = rememberScrollState()

    val selectedStep by remember { mutableIntStateOf(1) }
    val items = persistentListOf(
        ProgressStep("", true),
        ProgressStep("", true),
        ProgressStep("", false),
    )
    for (intent in ProgressTrackerIntent.entries) {
        ProgressTrackerRow(
            items = items,
            intent = intent,
            selectedStep = selectedStep,
        )
    }
    Row(
        modifier = Modifier.horizontalScroll(hScrollState),
    ) {
        for (intent in ProgressTrackerIntent.entries) {
            ProgressTrackerColumn(
                items = persistentListOf(
                    ProgressStep("Step 1", true),
                    ProgressStep("Step 2", true),
                    ProgressStep("Step 3", false),
                ),
                intent = intent,
                selectedStep = selectedStep,
            )
        }
    }
}
