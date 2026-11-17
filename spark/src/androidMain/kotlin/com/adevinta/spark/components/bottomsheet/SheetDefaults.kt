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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

/**
 * Contains the default values used by [BottomSheet]
 * and [com.adevinta.spark.components.bottomsheet.scaffold.BottomSheetScaffold].
 */
@Stable
internal object SheetDefaults {

    /**
     * the default color of the container for the bottom sheet.
     */
    val ContainerColor: Color
        @Composable get() = SparkTheme.colors.surface

    /** The default color of the scrim overlay for background content. */
    val ScrimColor: Color
        @Composable get() = SparkTheme.colors.scrim

    val DragHandleHeight: Dp = 4.dp
    val DragHandleWidth: Dp = 32.dp
    val DragHandleTopPadding: Dp = 8.dp

    val ContentTopPadding: Dp = 24.dp
    val ContentTopPaddingNoHandle: Dp = 16.dp
}
