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
import com.adevinta.spark.catalog.examples.samples.dialog.modal.EdgeToEdgeExample
import com.adevinta.spark.catalog.examples.samples.dialog.modal.ModalSample
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl

private const val ModalsExampleSourceUrl = "$SampleSourceUrl/ModalExamples.kt"
public val DialogsExamples: List<Example> = listOf(
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
        id = "edge to edge test",
        name = "Edge to edge",
        description = "Showcase the modal component with no main and support button. \n This will hide the Bottom " +
            "App Bar and add a close button in the dialog layout",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        EdgeToEdgeExample()
    },
)
