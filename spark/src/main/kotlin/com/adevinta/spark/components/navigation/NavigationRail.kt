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
package com.adevinta.spark.components.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.icons.House
import com.adevinta.spark.icons.MagnifierOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.Userfill
import androidx.compose.material3.NavigationRail as MaterialNavigationRail

@InternalSparkApi
@Composable
internal fun SparkNavigationRail(
    modifier: Modifier = Modifier,
    containerColor: Color = SparkTheme.colors.surface,
    contentColor: Color = contentColorFor(containerColor),
    header: @Composable (ColumnScope.() -> Unit)? = null,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    MaterialNavigationRail(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        header = header,
        windowInsets = windowInsets,
        content = content,
    )
}

/**
 * Spark bottom navigation rail.
 * Navigation rails provide access to main destinations in apps when using tablet and desktop screens.
 *
 * The navigation rail should be used to display three to seven app destinations and, optionally,
 * a FloatingActionButton or a logo header.
 * NavigationRail should contain multiple NavigationRailItems, each representing a singular destination.
 * @param modifier commentCountthe Modifier to be applied to this navigation rail
 * @param header commentCountoptional header that may hold a FloatingActionButton or a logo
 * @param content commentCountthe content of this navigation rail, typically 3-7 NavigationRailItems
 */
@ExperimentalSparkApi
@Composable
public fun NavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkNavigationRail(
        modifier = modifier,
        header = header,
        content = content,
    )
}

@Preview(
    group = "NavigationRail",
    name = "NavigationRail",
)
@Composable
internal fun NavigationRailPreview() {
    PreviewTheme {
        val items = mutableListOf(
            Pair("Home", SparkIcons.House),
            Pair("Search", SparkIcons.MagnifierOutline),
            Pair(null, SparkIcons.Userfill),
        )
        NavigationRail {
            items.forEach {
                NavigationRailItem(selected = true, onClick = { }, icon = it.second, label = it.first)
            }
        }
    }
}
