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
package com.adevinta.spark.components.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.navigation.NavigationDrawerItem
import com.adevinta.spark.icons.Family
import com.adevinta.spark.icons.HeartOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.MailBoxOpenOutline
import com.adevinta.spark.tokens.SparkShapes
import com.adevinta.spark.tokens.contentColorFor

@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@ExperimentalSparkApi
@Composable
internal fun SparkModalDrawerSheet(
    modifier: Modifier = Modifier,
    drawerShape: Shape = SparkShapes().large,
    drawerContainerColor: Color = SparkTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    drawerTonalElevation: Dp = SparkDrawerDefaults.ModalDrawerElevation,
    windowInsets: WindowInsets = SparkDrawerDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalDrawerSheet(
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
 * Spark ModalDrawerSheet.
 *
 * A ModalDrawerSheet is a Content inside of a modal navigation drawer.
 *
 * @param modifier the [Modifier] to be applied to this drawer's content
 * @param drawerShape defines the shape of this drawer's container
 * @param drawerContainerColor the color used for the background of this drawer. Use Color.Transparent to have no color.
 * @param drawerContentColor the preferred color for content inside this drawer. Defaults to either the matching content color for drawerContainerColor, or to the current LocalContentColor if drawerContainerColor is not a color from the theme.
 * @param drawerTonalElevation when drawerContainerColor is ColorScheme.surface, a translucent main color overlay is applied on top of the container. A higher tonal elevation value will result in a darker color in light theme and lighter color in dark theme. See also: Surface.
 * @param windowInsets a window insets for the sheet.
 * @param content content inside of a modal navigation drawer
 */
@ExperimentalSparkApi
@Composable
public fun ModalDrawerSheet(
    modifier: Modifier = Modifier,
    drawerShape: Shape = SparkShapes().large,
    drawerContainerColor: Color = SparkTheme.colors.surface,
    drawerContentColor: Color = contentColorFor(drawerContainerColor),
    drawerTonalElevation: Dp = SparkDrawerDefaults.ModalDrawerElevation,
    windowInsets: WindowInsets = SparkDrawerDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkModalDrawerSheet(
        modifier = modifier,
        drawerShape = drawerShape,
        drawerContainerColor = drawerContainerColor,
        drawerContentColor = drawerContentColor,
        drawerTonalElevation = drawerTonalElevation,
        windowInsets = windowInsets,
        content = content,
    )
}

@Preview(
    group = "Drawer",
    name = "ModalDrawerSheet",
)
@Composable
internal fun ModalDrawerSheetPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        // icons to mimic drawer destinations
        val items = listOf(LeboncoinIcons.HeartOutline, LeboncoinIcons.Family, LeboncoinIcons.MailBoxOpenOutline)
        val selectedItem = remember { mutableStateOf(items[0]) }

        ModalDrawerSheet {
            Spacer(Modifier.height(12.dp))
            items.forEach { item ->
                NavigationDrawerItem(
                    icon = item,
                    label = "item",
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
