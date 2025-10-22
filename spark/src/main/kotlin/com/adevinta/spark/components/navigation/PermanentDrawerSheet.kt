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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.icons.Activity
import com.adevinta.spark.icons.IdentityCardOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.StoreCriteria
import androidx.compose.material3.PermanentDrawerSheet as MaterialPermanentDrawerSheet

@InternalSparkApi
@Composable
internal fun SparkPermanentDrawerSheet(
    modifier: Modifier = Modifier,
    drawerShape: Shape = RectangleShape,
    drawerContainerColor: Color = SparkTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    drawerTonalElevation: Dp = 1.dp,
    windowInsets: WindowInsets = DrawerDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    MaterialPermanentDrawerSheet(
        modifier = modifier,
        drawerShape = drawerShape,
        drawerContainerColor = drawerContainerColor,
        drawerContentColor = drawerContentColor,
        drawerTonalElevation = drawerTonalElevation,
        windowInsets = windowInsets,
        content = content,
    )
}

/**
 * Content inside of a permanent navigation drawer.
 * @param modifier commentCountthe Modifier to be applied to this drawer's content
 * @param drawerContainerColor commentCountthe color used for the background of this drawer. Use Color.Transparent to have no color.
 * @param drawerContentColor commentCountthe preferred color for content inside this drawer. Defaults to either the matching content color
 * for drawerContainerColor, or to the current LocalContentColor if drawerContainerColor is not a color from the theme.
 * @param content commentCountcontent inside a permanent navigation drawer
 */
@ExperimentalSparkApi
@Composable
public fun PermanentDrawerSheet(
    modifier: Modifier = Modifier,
    drawerContainerColor: Color = SparkTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkPermanentDrawerSheet(
        modifier = modifier,
        drawerContainerColor = drawerContainerColor,
        drawerContentColor = drawerContentColor,
        content = content,
    )
}

@Preview(
    group = "PermanentDrawerSheet",
    name = "PermanentDrawerSheet",
)
@Composable
internal fun PermanentDrawerSheetPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        // icons to mimic drawer destinations
        val items = listOf(SparkIcons.Activity, SparkIcons.IdentityCardOutline, SparkIcons.StoreCriteria)
        val selectedItem = remember { mutableStateOf(items[0]) }
        PermanentDrawerSheet(Modifier.width(240.dp)) {
            Spacer(Modifier.height(12.dp))
            items.forEach { item ->
                NavigationDrawerItem(
                    icon = item,
                    label = "Item",
                    selected = item == selectedItem.value,
                    onClick = {
                        selectedItem.value = item
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        }
    }
}
