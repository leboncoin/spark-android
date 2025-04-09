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
package com.adevinta.spark.catalog.examples.samples.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.snackbars.Snackbar
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.snackbars.SnackbarSparkVisuals
import com.adevinta.spark.components.snackbars.SnackbarStyle
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.TextLinkButton
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.SparkIcons
import kotlinx.coroutines.launch

private const val SnackbarExampleSourceUrl = "$SampleSourceUrl/SnackbarSamples.kt"

public val SnackbarExamples: List<Example> = listOf(
    Example(
        id = "filled",
        name = R.string.snackbar_example_filled_title,
        description = R.string.snackbar_example_filled_description ,
        sourceUrl = SnackbarExampleSourceUrl,
    ) {
        Snackbar(
            intent = SnackbarIntent.Info,
            withDismissAction = true,
            actionOnNewLine = true,
            style = SnackbarStyle.Filled,
            icon = SparkIcons.LikeFill,
            actionLabel = "Action",
        ) {
            Text("Simple message!")
        }
    },
    Example(
        id = "tinted",
        name = R.string.snackbar_example_tinted_title,
        description = R.string.snackbar_example_tinted_description,
        sourceUrl = SnackbarExampleSourceUrl,
    ) {
        Snackbar(
            intent = SnackbarIntent.Alert,
            withDismissAction = true,
            actionOnNewLine = false,
            style = SnackbarStyle.Tinted,
            icon = SparkIcons.LikeFill,
            actionLabel = "Action",
        ) {
            Text("Simple message!")
        }
        VerticalSpacer(8.dp)
    },
    Example(
        id = "host",
        name = R.string.snackbar_example_host_title,
        description = R.string.snackbar_example_host_description,
        sourceUrl = SnackbarExampleSourceUrl,
    ) { snackbarHostState ->
        val scope = rememberCoroutineScope()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            TextLinkButton(
                intent = ButtonIntent.Success,
                text = "Click me to open snackbar",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Hello Snackbar!",
                            actionLabel = "Action",
                        )
                    }
                },
            )
        }
    },

    Example(
        id = "host-action-long",
        name = R.string.snackbar_example_host_customised_title,
        description = R.string.snackbar_example_host_customised_description,
        sourceUrl = SnackbarExampleSourceUrl,
    ) { snackbarHostState ->
        val scope = rememberCoroutineScope()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            TextLinkButton(
                intent = ButtonIntent.Accent,
                text = "Click me to open snackbar",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            SnackbarSparkVisuals(
                                message = "Hello Snackbar!",
                                actionLabel = "Action",
                                duration = SnackbarDuration.Long,
                                intent = SnackbarIntent.Accent,
                            ),
                        )
                    }
                },
            )
        }
    },
)
