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
package com.adevinta.spark.tokens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tools.preview.DevicePreviews

public object Layout {

    public val windowSize: WindowSizeClass
        @Composable get() = LocalWindowSizeClass.current

    public val bodyMargin: Dp
        @Composable get() = when (LocalWindowSizeClass.current.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 16.dp
            WindowWidthSizeClass.Medium -> 32.dp
            else -> 64.dp
        }

    public val gutter: Dp
        @Composable get() = when (LocalWindowSizeClass.current.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 8.dp
            WindowWidthSizeClass.Medium -> 16.dp
            else -> 24.dp
        }

    public val columns: Int
        @Composable get() = when (LocalWindowSizeClass.current.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 4
            WindowWidthSizeClass.Medium -> 8
            else -> 12
        }
}

/**
 * Supports wide screens by making the content width max 840dp, centered horizontally.
 */
@Suppress("ComposeModifierComposed") // WindowInsets.systemBars is internally a LocalComposition but we
// can't access it to use it in the Node API.
public fun Modifier.bodyWidth(): Modifier = fillMaxWidth()
    .composed {
        windowInsetsPadding(
            WindowInsets.systemBars
                .only(WindowInsetsSides.Horizontal),
        )
    }

@Composable
@DevicePreviews
internal fun LayoutPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        val columns = Layout.columns
        val bodyMargin = Layout.bodyMargin
        val gutter = Layout.gutter

        Box(Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns / 4),
                contentPadding = PaddingValues(
                    horizontal = bodyMargin,
                    vertical = gutter,
                ),
                // We minus 8.dp off the grid padding, as we use content padding on the items below
                horizontalArrangement = Arrangement.spacedBy(gutter),
                verticalArrangement = Arrangement.spacedBy(gutter),
                modifier = Modifier
                    .bodyWidth()
                    .fillMaxHeight()
                    .background(SparkTheme.colors.mainContainer),
            ) {
                items(
                    count = 20,
                ) {
                    Item()
                }
            }
        }
    }
}

@Composable
private fun Item() {
    Surface(
        modifier = Modifier
            .aspectRatio(2 / 3f),
        color = SparkTheme.colors.main,
        shape = SparkTheme.shapes.medium,
        elevation = 4.dp,
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "",
                style = SparkTheme.typography.body2,
                textAlign = TextAlign.Center,
            )
        }
    }
}
