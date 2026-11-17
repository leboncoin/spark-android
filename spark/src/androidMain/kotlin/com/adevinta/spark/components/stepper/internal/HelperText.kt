/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.components.stepper.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.TextFieldDefault
import com.adevinta.spark.tools.modifiers.invisibleSemantic

@Composable
internal fun supportText(
    helper: String?,
    status: FormFieldStatus?,
    stateMessage: String?,
    stateIcon: @Composable ((Modifier) -> Unit)?,
): (@Composable () -> Unit)? = if (stateMessage != null && status != null) {
    {
        SupportingText(
            text = stateMessage,
            statusIcon = stateIcon,
        )
    }
} else if (helper != null) {
    {
        SupportingText(
            text = helper,
            statusIcon = stateIcon,
        )
    }
} else {
    null
}

@Composable
private fun SupportingText(
    text: String,
    statusIcon: @Composable ((Modifier) -> Unit)?,
) {
    Row(
        modifier = Modifier.fillMaxWidth().invisibleSemantic(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        statusIcon?.invoke(
            Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterVertically),
        )
        Text(
            modifier = Modifier
                .weight(1f, fill = true)
                .clearAndSetSemantics { },
            text = text,
        )
    }
}

@Preview
@Composable
private fun PreviewSupportText() {
    PreviewTheme {
        supportText(
            helper = "Helper",
            status = null,
            stateMessage = "helper",
            stateIcon = null,
        )?.invoke()
        supportText(
            helper = "Helper",
            status = FormFieldStatus.Error,
            stateMessage = "error",
            stateIcon = TextFieldDefault.getStatusIcon(FormFieldStatus.Error),
        )?.invoke()
        supportText(
            helper = "Helper",
            status = FormFieldStatus.Alert,
            stateMessage = "alert",
            stateIcon = TextFieldDefault.getStatusIcon(FormFieldStatus.Alert),
        )?.invoke()
        supportText(
            helper = "Helper",
            status = FormFieldStatus.Success,
            stateMessage = "success",
            stateIcon = TextFieldDefault.getStatusIcon(FormFieldStatus.Success),
        )?.invoke()
    }
}
