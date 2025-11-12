/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.catalog.configurator.samples.modal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.examples.samples.dialog.modal.ModalSample
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.dialog.ModalDefault
import com.adevinta.spark.components.dialog.ModalScaffold
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.ImageFill
import com.adevinta.spark.icons.MoreMenuVertical
import com.adevinta.spark.icons.SparkIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public val ModalConfigurator: Configurator = Configurator(
    id = "modal",
    name = "Modal",
    description = "Modal configuration",
    sourceUrl = "$SampleSourceUrl/ModalSample.kt",
) {
    ModalSample()
}

@Composable
internal fun ColumnScope.ModalSample() {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var withPadding by remember { mutableStateOf(true) }
    var mainButtonText by rememberSaveable { mutableStateOf("Main Action") }
    var supportButtonText by rememberSaveable { mutableStateOf("Alternative Action") }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    ButtonFilled(
        onClick = { showDialog = true },
        text = "Show Modal",
    )

    SwitchLabelled(
        checked = withPadding,
        onCheckedChange = { withPadding = it },
    ) {
        Text(
            text = "With Padding content",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = mainButtonText,
        label = "Main Button Text",
        helper = "An empty string make hte button disappear",
        onValueChange = { mainButtonText = it },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = supportButtonText,
        label = "Support Button Text",
        helper = "An empty string make hte button disappear",
        onValueChange = {
            supportButtonText = it
        },
    )

    if (showDialog) {
        val controller = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }

        ModalScaffold(
            onClose = { showDialog = false },
            contentPadding = if (withPadding) ModalDefault.DialogPadding else PaddingValues(0.dp),
            mainButton = if (mainButtonText.isNotBlank()) {
                { MainButton(mainButtonText, it, coroutineScope, snackbarHostState) }
            } else {
                null
            },
            supportButton = if (supportButtonText.isNotBlank()) {
                { SupportButton(supportButtonText, it, focusRequester, controller) }
            } else {
                null
            },
            title = {
                Text(text = "Title")
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                }
                IconButton(onClick = { }) {
                    Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                }
                IconButton(onClick = { }) {
                    Icon(sparkIcon = SparkIcons.MoreMenuVertical, contentDescription = "")
                }
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(text = exampleContent)
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.focusRequester(focusRequester),
                )
            }
        }
    }
}

@Composable
private fun SupportButton(
    supportButtonText: String,
    modifier: Modifier,
    focusRequester: FocusRequester,
    controller: SoftwareKeyboardController?,
) {
    ButtonOutlined(
        modifier = modifier,
        onClick = {
            focusRequester.requestFocus()
            controller?.show()
        },
        text = supportButtonText,
    )
}

@Composable
private fun MainButton(
    mainButtonText: String,
    modifier: Modifier,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    ButtonFilled(
        modifier = modifier,
        onClick = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Snackbar",
                    actionLabel = "Action",
                    duration = SnackbarDuration.Short,
                )
            }
        },
        text = mainButtonText,
    )
}

private val exampleContent = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula " +
    "eget dolor. " +
    "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur " +
    "ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. " +
    "\n\nNulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, " +
    "vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. " +
    "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. " +
    "Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. " +
    "\n\nAenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante," +
    " dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius " +
    "laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. " +
    "Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. " +
    "Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. " +
    "\n\nAenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante," +
    " dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius " +
    "laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. " +
    "Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. " +
    "Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. " +
    "\n\nAenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante," +
    " dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius " +
    "laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. " +
    "Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. " +
    "\n\nMaecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet " +
    "adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, " +
    "lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis " +
    "faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. " +
    "Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. " +
    "Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,"

@Preview
@Composable
private fun ModalSamplePreview() {
    SparkTheme {
        ModalSample()
    }
}
