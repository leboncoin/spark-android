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
package com.adevinta.spark.catalog.configurator.samples.snackbar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.snackbars.Snackbar
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.snackbars.SnackbarSparkVisuals
import com.adevinta.spark.components.snackbars.SnackbarStyle
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.FlashlightFill
import com.adevinta.spark.icons.SparkIcons
import kotlinx.coroutines.launch

public val SnackbarConfigurator: Configurator = Configurator(
    id = "snackbar",
    name = "Snackbar",
    description = "Snackbar configuration",
    sourceUrl = "$SampleSourceUrl/SnackbarSamples.kt",
) {
    SnackbarSample(it)
}

@Composable
private fun ColumnScope.SnackbarSample(snackbarHostState: SnackbarHostState) {
    var withDismissAction by remember { mutableStateOf(false) }
    var isIconEnabled by remember { mutableStateOf(false) }
    var style by remember { mutableStateOf(SnackbarStyle.Filled) }
    var actionOnNewLine by remember { mutableStateOf(false) }
    var intent by remember { mutableStateOf(SnackbarIntent.Basic) }
    var actionText by remember { mutableStateOf("Action") }
    var contentText by remember { mutableStateOf("Just a snackbar") }
    val scope = rememberCoroutineScope()

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.configurator_component_screen_intent_label),
        selectedOption = intent,
        onOptionSelect = {
            intent = it
        },
    )

    SwitchLabelled(
        checked = actionOnNewLine,
        onCheckedChange = {
            actionOnNewLine = it
        },
    ) {
        Text(
            text = "Action on new line",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = withDismissAction,
        onCheckedChange = {
            withDismissAction = it
        },
    ) {
        Text(
            text = "Dismiss icon enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    SwitchLabelled(
        checked = isIconEnabled,
        onCheckedChange = {
            isIconEnabled = it
        },
    ) {
        Text(
            text = "Icon enabled",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    ButtonGroup(
        title = "Style",
        selectedOption = style,
        onOptionSelect = { style = it },
    )
    Snackbar(
        intent = intent,
        withDismissAction = withDismissAction,
        actionOnNewLine = actionOnNewLine,
        style = style,
        icon = if (isIconEnabled) SparkIcons.FlashlightFill else null,
        actionLabel = actionText,
    ) {
        Text(contentText)
    }

    ButtonTinted(
        modifier = Modifier.fillMaxWidth(),
        size = ButtonSize.Medium,
        onClick = {
            scope.launch {
                snackbarHostState.showSnackbar(
                    SnackbarSparkVisuals(
                        intent = intent,
                        withDismissAction = withDismissAction,
                        actionOnNewLine = actionOnNewLine,
                        style = style,
                        icon = if (isIconEnabled) SparkIcons.FlashlightFill else null,
                        actionLabel = actionText,
                        message = contentText,
                        duration = SnackbarDuration.Short,
                    ),
                )
            }
        },
    ) {
        Text("Launch Snackbar")
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = actionText,
        onValueChange = { actionText = it },
        label = "Change Action Label",
        stateMessage = actionText,
    )
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = contentText,
        onValueChange = { contentText = it },
        label = "Change Text Content",
        stateMessage = contentText,
    )
}

@Preview
@Composable
private fun SnackbarSamplePreview() {
    PreviewTheme { SnackbarSample(SnackbarHostState()) }
}
