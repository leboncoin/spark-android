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
package com.adevinta.spark.components.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.bottomsheet.SheetDefaults.DragHandleHeight
import com.adevinta.spark.components.bottomsheet.SheetDefaults.DragHandleTopPadding
import com.adevinta.spark.components.bottomsheet.SheetDefaults.DragHandleWidth
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * The optional visual marker placed on top of a bottom sheet to indicate it may be dragged.
 */
@Composable
public fun DragHandle(
    modifier: Modifier = Modifier,
    color: Color = SparkTheme.colors.outline.dim1,
) {
    val dragHandleDescription = stringResource(id = R.string.spark_drag_handle_a11y)
    Surface(
        modifier = modifier
            .padding(vertical = DragHandleTopPadding)
            .semantics { contentDescription = dragHandleDescription }
            .sparkUsageOverlay(),
        color = color,
        shape = SparkTheme.shapes.full,
    ) {
        Box(
            Modifier.size(
                width = DragHandleWidth,
                height = DragHandleHeight,
            ),
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewDragHandle() {
    PreviewTheme {
        Box(modifier = Modifier.padding(4.dp)) {
            DragHandle()
        }
    }
}
