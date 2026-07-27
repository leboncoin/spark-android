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
package com.adevinta.spark.components.chips

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

public object ChipDefaults {
    /**
     * The padding between the elements in the chip.
     */
    private val HorizontalElementsPadding = 8.dp

    /**
     * The size of the leading icon in the chip.
     */
    internal val LeadingIconSize = 16.dp

    /**
     * Horizontal padding for chip content.
     */
    internal val ChipPadding = PaddingValues(horizontal = HorizontalElementsPadding)

    /**
     * Spacing between the leading icon and the label.
     */
    internal val LeadingIconEndSpacing = 4.dp

    /**
     * The outlined chip's border size
     */
    internal val BorderStrokeWidth = 1.dp

    /**
     * The dashed chip's border corner radius.
     */
    internal val DashedBorderRadius = 8.dp

    /**
     * The chip's default height
     */
    internal val MinHeight = 32.dp
}
