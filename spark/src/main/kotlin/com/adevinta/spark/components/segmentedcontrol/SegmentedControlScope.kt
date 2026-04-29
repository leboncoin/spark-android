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
package com.adevinta.spark.components.segmentedcontrol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.icons.SparkIcon

/**
 * Whether the segment currently being composed is selected.
 *
 * Available inside any segment content block — including [SegmentedControlScope.Custom] —
 * so callers can adapt icons, text weight, or colours to the selection state.
 */
public val LocalSegmentSelected: ProvidableCompositionLocal<Boolean> = compositionLocalOf { false }

/**
 * Scope interface for adding segments to a [SegmentedControl].
 *
 * Inside any content block you can read [LocalSegmentSelected] to adapt the
 * visual appearance based on whether the segment is currently selected.
 */
@Stable
public interface SegmentedControlScope {

    @Composable
    public fun SingleLine(
        text: String,
        modifier: Modifier = Modifier,
    )

    @Composable
    public fun TwoLine(
        title: String,
        subtitle: String,
        modifier: Modifier = Modifier,
    )

    @Composable
    public fun Icon(
        icon: SparkIcon,
        modifier: Modifier = Modifier,
    )

    @Composable
    public fun IconText(
        icon: SparkIcon,
        text: String,
        modifier: Modifier = Modifier,
    )

    @Composable
    public fun Number(
        number: Int,
        modifier: Modifier = Modifier,
    )

    @Composable
    public fun Custom(
        selectedBackgroundColor: Color,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    )
}
