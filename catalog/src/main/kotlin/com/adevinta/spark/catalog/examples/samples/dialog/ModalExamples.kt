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
package com.adevinta.spark.catalog.examples.samples.dialog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.examples.samples.dialog.alertdialog.AlertDialogLongTextSample
import com.adevinta.spark.catalog.examples.samples.dialog.alertdialog.AlertDialogSample
import com.adevinta.spark.catalog.examples.samples.dialog.alertdialog.AlertDialogSimpleSample
import com.adevinta.spark.catalog.examples.samples.dialog.alertdialog.AlertDialogWithIconSample
import com.adevinta.spark.catalog.examples.samples.dialog.modal.EdgeToEdgeExample
import com.adevinta.spark.catalog.examples.samples.dialog.modal.ModalSample
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl

private const val ModalsExampleSourceUrl = "$SampleSourceUrl/ModalExamples.kt"
public val DialogsExamples: List<Example> = listOf(
    Example(
        id = "alert-dialog-basic",
        name = "Alert Dialog - Basic",
        description = "Basic alert dialog with icon, title, text, and action buttons",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        AlertDialogSample()
    },
    Example(
        id = "alert-dialog-icon",
        name = "Alert Dialog - With Icon",
        description = "Alert dialog with custom icon for destructive actions",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        AlertDialogWithIconSample()
    },
    Example(
        id = "alert-dialog-simple",
        name = "Alert Dialog - Simple",
        description = "Simple alert dialog with only title, text, and confirm button",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        AlertDialogSimpleSample()
    },
    Example(
        id = "alert-dialog-long",
        name = "Alert Dialog - Long Text",
        description = "Alert dialog with longer text content for terms and conditions",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        AlertDialogLongTextSample()
    },
    Example(
        id = "modal",
        name = "Modal",
        description = "Showcase the modal component with different",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample()
    },
    Example(
        id = "no-padding",
        name = "Modal with no content padding",
        description = "Showcase the modal component with no start and end padding on the content placeholder",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample(PaddingValues(0.dp))
    },
    Example(
        id = "no-buttons",
        name = "Modal with no buttons",
        description = "Showcase the modal component with no main and support button. \n This will hide the Bottom " +
            "App Bar and add a close button in the dialog layout",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample(withButtons = false)
    },
    Example(
        id = "no-content",
        name = "Modal with no content",
        description = "Showcase the modal component with no text content. \n This will make sure that the Bottom " +
            "App Bar will not be elevated when there's no scroll available",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample(withContent = false)
    },

    Example(
        id = "edge to edge test",
        name = "Edge to edge",
        description = "Showcase the modal component with no main and support button. \n This will hide the Bottom " +
            "App Bar and add a close button in the dialog layout",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        EdgeToEdgeExample()
    },
)
