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
package com.adevinta.spark.catalog.examples.samples.dialog.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.dialog.ModalDefault
import com.adevinta.spark.components.dialog.ModalScaffold
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ImageFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.ThreeDotsVertical
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun ModalSample(
    paddingValues: PaddingValues = ModalDefault.DialogPadding,
    withButtons: Boolean = true,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var showDialog by rememberSaveable { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        ButtonFilled(
            onClick = { showDialog = true },
            text = "Show Modal",
        )

        if (showDialog) {
            val controller = LocalSoftwareKeyboardController.current
            val focusRequester = remember { FocusRequester() }
            val snackbarHostState = remember { SnackbarHostState() }

            ModalScaffold(
                onClose = { showDialog = false },
                contentPadding = paddingValues,
                inEdgeToEdge = true,
                mainButton = if (withButtons) {
                    { MainButton(it, coroutineScope, snackbarHostState) }
                } else {
                    null
                },
                supportButton = if (withButtons) {
                    { SupportButton(it, focusRequester, controller) }
                } else {
                    null
                },
                title = {
                    Text(text = "Title")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(sparkIcon = SparkIcons.ThreeDotsVertical, contentDescription = "")
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
}

@Composable
private fun SupportButton(
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
        text = "Alternative Action",
    )
}

@Composable
private fun MainButton(
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
        text = "Main Action",
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
