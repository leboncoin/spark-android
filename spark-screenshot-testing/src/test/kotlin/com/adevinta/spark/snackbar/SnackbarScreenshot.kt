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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.snackbars.Snackbar
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule
import org.junit.Test

internal class SnackbarScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SessionParams.RenderingMode.H_SCROLL,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    private val shortTitle = "Title"
    private val longTitle = "This is a very long title that should test how the layout handles extended text"
    private val shortMessage = "Short message"
    private val longMessage = "This is a very long message that should test how the layout handles extended " +
        "text content and wrapping behavior"

    @Test
    fun snackbarIntentsShowcase() {
        paparazzi.sparkSnapshotNightMode {
            Column {
                SnackbarIntent.entries.forEach {
                    Snackbar(
                        intent = it,
                        title = "Title",
                        actionLabel = "Action",
                        onDismissClick = {},
                    ) {
                        Text("Lorem ipsum dolor sit amet")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun snackbarTitleLayoutVariations() {
        paparazzi.sparkSnapshotNightMode {
            FlowColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Short title + Short message variations
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    onDismissClick = {},
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                    onDismissClick = {},
                ) {
                    Text(shortMessage)
                }

                // Short title + Long message variations
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    onDismissClick = {},
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                    onDismissClick = {},
                ) {
                    Text(longMessage)
                }

                // Long title + Short message variations
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    onDismissClick = {},
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                    onDismissClick = {},
                ) {
                    Text(shortMessage)
                }

                // Long title + Long message variations
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    onDismissClick = {},
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    actionLabel = "Action",
                    onActionClick = {},
                    onDismissClick = {},
                ) {
                    Text(longMessage)
                }

                // Action label length variations
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    actionLabel = "Short",
                    onActionClick = {},
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = shortTitle,
                    actionLabel = "This is a very long action label",
                    onActionClick = {},
                ) {
                    Text(shortMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    actionLabel = "Short",
                    onActionClick = {},
                ) {
                    Text(longMessage)
                }
                Snackbar(
                    intent = SnackbarIntent.Info,
                    title = longTitle,
                    actionLabel = "This is a very long action label",
                    onActionClick = {},
                ) {
                    Text(longMessage)
                }
            }
        }
    }
}
