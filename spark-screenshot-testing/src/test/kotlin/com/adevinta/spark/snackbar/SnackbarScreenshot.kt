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
package com.adevinta.spark.snackbar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.components.snackbars.Snackbar
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.snackbars.SnackbarStyle
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

internal class SnackbarScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = V_SCROLL,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun snackbarIntentsFilledShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Column {
                SnackbarIntent.entries.forEach {
                    Snackbar(
                        style = SnackbarStyle.Filled,
                        intent = it,
                        actionOnNewLine = true,
                        withDismissAction = true,
                        icon = SparkIcons.HeartFill,
                        actionLabel = "Action",
                    ) {
                        Text("Lorem ipsum dolor sit amet")
                    }
                }
            }
        }
    }

    @Test
    fun snackbarIntentsTintedShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Column {
                SnackbarIntent.entries.forEach {
                    Snackbar(
                        style = SnackbarStyle.Tinted,
                        intent = it,
                        withDismissAction = true,
                        icon = SparkIcons.HeartFill,
                        actionOnNewLine = true,
                        actionLabel = "Action",
                    ) {
                        Text("Lorem ipsum dolor sit amet")
                    }
                }
            }
        }
    }
}
