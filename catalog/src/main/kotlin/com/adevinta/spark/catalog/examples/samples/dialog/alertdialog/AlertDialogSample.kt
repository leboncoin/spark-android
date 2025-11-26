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
package com.adevinta.spark.catalog.examples.samples.dialog.alertdialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.dialog.AlertDialog
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.icons.DeleteOutline
import com.adevinta.spark.icons.SparkIcons

@OptIn(ExperimentalSparkApi::class)
@Preview
@Composable
internal fun AlertDialogSample() {
    var showDialog by rememberSaveable { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dismiss
                showDialog = false
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                )
            },
            title = {
                Text("Alert Dialog Title")
            },
            text = {
                Text(
                    "This is the content of the alert dialog. It can contain " +
                        "information about an action that requires user confirmation.",
                )
            },
            confirmButton = {
                ButtonGhost(
                    text = "Confirm",
                    onClick = {
                        // Handle confirm
                        showDialog = false
                    },
                )
            },
            dismissButton = {
                ButtonGhost(
                    text = "Cancel",
                    onClick = {
                        // Handle dismiss
                        showDialog = false
                    },
                )
            },
        )
    }
}

@OptIn(ExperimentalSparkApi::class)
@Preview
@Composable
internal fun AlertDialogWithIconSample() {
    var showDialog by rememberSaveable { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dismiss
                showDialog = false
            },
            icon = {
                IconButtonFilled(
                    icon = SparkIcons.DeleteOutline,
                    onClick = {},
                    contentDescription = null,
                )
            },
            title = {
                Text("Delete Item")
            },
            text = {
                Text(
                    "Are you sure you want to delete this item? " +
                        "This action cannot be undone.",
                )
            },
            confirmButton = {
                ButtonGhost(
                    text = "Delete",
                    onClick = {
                        // Handle delete
                        showDialog = false
                    },
                )
            },
            dismissButton = {
                ButtonGhost(
                    text = "Cancel",
                    onClick = {
                        // Handle cancel
                        showDialog = false
                    },
                )
            },
        )
    }
}

@OptIn(ExperimentalSparkApi::class)
@Preview
@Composable
internal fun AlertDialogSimpleSample() {
    var showDialog by rememberSaveable { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dismiss
                showDialog = false
            },
            title = {
                Text("Simple Alert")
            },
            text = {
                Text("This is a simple alert dialog with only a title and message.")
            },
            confirmButton = {
                ButtonGhost(
                    text = "OK",
                    onClick = {
                        // Handle OK
                        showDialog = false
                    },
                )
            },
        )
    }
}

@OptIn(ExperimentalSparkApi::class)
@Preview
@Composable
internal fun AlertDialogLongTextSample() {
    var showDialog by rememberSaveable { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Handle dismiss
                showDialog = false
            },
            title = {
                Text("Terms and Conditions")
            },
            text = {
                Text(
                    "By using this application, you agree to our terms and conditions. " +
                        "This includes our privacy policy, data usage guidelines, and user " +
                        "conduct rules. Please read these documents carefully before proceeding. " +
                        "If you do not agree with any of these terms, please do not use the application.",
                )
            },
            confirmButton = {
                ButtonGhost(
                    text = "I Agree",
                    onClick = {
                        // Handle agreement
                        showDialog = false
                    },
                )
            },
            dismissButton = {
                ButtonGhost(
                    text = "Decline",
                    onClick = {
                        // Handle decline
                        showDialog = false
                    },
                )
            },
        )
    }
}
