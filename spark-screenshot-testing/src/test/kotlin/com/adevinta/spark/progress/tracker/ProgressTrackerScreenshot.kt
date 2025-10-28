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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.progress.tracker.ProgressSizes
import com.adevinta.spark.components.progress.tracker.ProgressStep
import com.adevinta.spark.components.progress.tracker.ProgressTrackerIntent
import com.adevinta.spark.components.progress.tracker.ProgressTrackerRow
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.AddFill
import com.adevinta.spark.icons.SparkAnimatedIcons
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.addButton
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

internal class ProgressTrackerScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        // fixme: fix to make it portrait while #1190 is not released
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = V_SCROLL,
    )

    @Test
    fun themesProgressTrackerRows() {
        paparazzi.sparkSnapshotNightMode {
            Row()
        }
    }

    @OptIn(ExperimentalLayoutApi::class, ExperimentalSparkApi::class)
    @Composable
    private fun Row() {
        FlowRow(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Center,
        ) {
            val selectedStep = 1
            ProgressTrackerIntent.entries.forEach { intent ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = intent.name,
                        style = SparkTheme.typography.headline1,
                    )
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ProgressSizes.entries.forEach { size ->
                            ProgressTrackerRow(
                                modifier = Modifier.width(240.dp),
                                items = persistentListOf(
                                    ProgressStep("Lorem ipsume", true, doneIcon = SparkIcons.AddFill),
                                    ProgressStep(
                                        "Lorem ipsume dolar sit amet",
                                        true,
                                        icon = SparkAnimatedIcons.addButton(),
                                    ),
                                    ProgressStep("Lorem ipsume", false),
                                    ProgressStep("Lorem ipsume", false),
                                    ProgressStep("Lorem ipsume", false),
                                ),
                                intent = intent,
                                size = size,
                                onStepClick = {
                                },
                                selectedStep = selectedStep,
                            )
                        }
                    }
                }
            }
        }
    }
}
