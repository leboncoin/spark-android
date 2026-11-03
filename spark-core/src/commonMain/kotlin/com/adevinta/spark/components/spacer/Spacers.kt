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
package com.adevinta.spark.components.spacer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.foundation.layout.Spacer as FoundationSpacer

/**
 * Component that represents an empty space layout, whose size can be defined using
 * [Modifier.width], [Modifier.height] and [Modifier.size] modifiers.
 *
 * @param modifier modifiers to set to this spacer
 */
@Composable
@NonRestartableComposable
public fun Spacer(modifier: Modifier = Modifier) {
    FoundationSpacer(modifier = modifier)
}

/**
 * Component that represents an empty vertical space layout and to be used in a [ColumnScope].
 *
 * @param space the vertical space taken by this Composable
 */
@Suppress("ModifierMissing") // We consider it not needed for spacers
@Composable
public fun ColumnScope.VerticalSpacer(space: Dp) {
    Spacer(
        modifier = Modifier
            .height(space)
            .sparkUsageOverlay(Color.Green),
    )
}

/**
 * Component that represents an empty horizontal space layout and to be used in a [RowScope].
 *
 * @param space the horizontal space taken by this Composable
 */
@Suppress("ModifierMissing")
@Composable
public fun RowScope.HorizontalSpacer(space: Dp) {
    Spacer(
        modifier = Modifier
            .width(space)
            .sparkUsageOverlay(Color.Green),
    )
}
