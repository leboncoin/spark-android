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
package com.adevinta.spark.progress.tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.progress.tracker.ProgressSizes
import com.adevinta.spark.components.progress.tracker.ProgressStep
import com.adevinta.spark.components.progress.tracker.ProgressTrackerColumn
import com.adevinta.spark.components.progress.tracker.ProgressTrackerRow
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.adevinta.spark.sparkSnapshotNightMode
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class ProgressTrackerDocScreenshot {

    @get:Rule
    val paparazzi = paparazziRule()

    @Test
    fun progressColumnShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Column {
                ProgressTrackerColumn(
                    modifier = Modifier.width(240.dp),
                    items = persistentListOf(
                        ProgressStep(
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                                " incididunt ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis" +
                                " nostrud exercitation.",
                            true,
                        ),
                        ProgressStep(
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod" +
                                " tempor incididunt ut labore et dolore magna aliqua.",
                            false,
                        ),
                        ProgressStep("Lorem ipsume dolar sit amet", true),
                    ),
                    onStepClick = {
                    },
                    selectedStep = 1,
                )
            }
        }
    }

    @Test
    fun progressRowShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Column {
                ProgressTrackerRow(
                    modifier = Modifier.width(240.dp),
                    items = persistentListOf(
                        ProgressStep("Lorem ipsume", true),
                        ProgressStep("Lorem ipsume dolar sit amet", false),
                        ProgressStep("Lorem ipsume", true),
                    ),
                    onStepClick = {
                    },
                    selectedStep = 1,
                )
            }
        }
    }

    @Test
    fun progressSizesShowcase() {
        paparazzi.sparkSnapshot {
            Column {
                ProgressSizes.entries.forEach {
                    ProgressTrackerRow(
                        modifier = Modifier.width(240.dp),
                        items = persistentListOf(
                            ProgressStep("Lorem ipsume", true),
                            ProgressStep("Lorem ipsume dolar sit amet", false),
                            ProgressStep("Lorem ipsume", true),
                        ),
                        size = it,
                        onStepClick = {
                        },
                        selectedStep = 1,
                    )
                }
            }
        }
    }
}
