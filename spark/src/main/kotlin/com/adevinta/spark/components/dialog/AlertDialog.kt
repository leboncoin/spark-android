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
package com.adevinta.spark.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.icons.CopyFill
import com.adevinta.spark.icons.LeboncoinIcons

@ExperimentalSparkApi
@Composable
internal fun SparkAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = shape,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        tonalElevation = tonalElevation,
        properties = properties,
    )
}

/**
 * Spark AlertDialog.
 *
 * An AlertDialog is a simple dialog for alerting about something
 *
 * ![AlertDialog image](https://developer.android.com/images/reference/androidx/compose/material/dialogs.png)
 *
 * @param modifier the [Modifier] to be applied to this dialog.
 * @param onDismissRequest called when the user tries to dismiss the Dialog by clicking outside or pressing the back button. This is not called when the dismiss button is clicked.
 * @param confirmButton button which is meant to confirm a proposed action, thus resolving what triggered the dialog. The dialog does not set up any events for this button so they need to be set up by the caller.
 * @param dismissButton button which is meant to dismiss the dialog. The dialog does not set up any events for this button so they need to be set up by the caller.
 * @param icon optional icon that will appear above the title or above the text, in case a title was not provided.
 * @param title title which should specify the purpose of the dialog. The title is not mandatory, because there may be sufficient information inside the text.
 * @param text text which presents the details regarding the dialog's purpose.
 * @param shape defines the shape of this dialog's container
 * @param containerColor the color used for the background of this dialog. Use Color.Transparent to have no color.
 * @param iconContentColor the content color used for the icon.
 * @param titleContentColor the content color used for the title.
 * @param textContentColor the content color used for the text.
 * @param tonalElevation when containerColor is ColorScheme.surface, a translucent main color overlay is applied on top of the container. A higher tonal elevation value will result in a darker color in light theme and lighter color in dark theme. See also: Surface.
 * @param properties typically platform specific properties to further configure the dialog.
 */
@ExperimentalSparkApi
@Composable
public fun AlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
) {
    SparkAlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        icon = icon,
        title = title,
        text = text,
        shape = shape,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        tonalElevation = tonalElevation,
        properties = properties,
    )
}

@Preview(
    group = "Dialog",
    name = "AlertDialog",
)
@Composable
internal fun AlertDialogPreview() {
    PreviewTheme {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
            },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Title",
                )
            },
            text = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "This area typically contains the supportive text " +
                        "which presents the details regarding the Dialog's purpose.",
                )
            },
            confirmButton = {
                ButtonGhost(
                    onClick = {
                    },
                    text = "Confirm",
                    icon = LeboncoinIcons.CopyFill,
                    iconSide = IconSide.START,
                )
            },
            dismissButton = {
                ButtonGhost(
                    onClick = {
                    },
                    text = "Dismiss",
                    icon = LeboncoinIcons.CopyFill,
                    iconSide = IconSide.START,
                )
            },
        )
    }
}
