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
@file:Suppress("DEPRECATION")

package com.adevinta.spark.components.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.FloatingActionButton
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WheelOutline
import com.adevinta.spark.tokens.contentColorFor

/**
 * <a href="https://m3.material.io/components/bottom-app-bar/overview" class="external" target="_blank">Material Design bottom app bar</a>.
 *
 * A bottom app bar displays navigation and key actions at the bottom of mobile screens.
 *
 * ![Bottom app bar image](https://developer.android.com/images/reference/androidx/compose/material3/bottom-app-bar.png)
 *
 * It can optionally display a [FloatingActionButton] embedded at the end of the BottomAppBar.
 *
 * Also see [NavigationBar].
 *
 * @param actions the icon content of this BottomAppBar. The default layout here is a [Row],
 * so content inside will be placed horizontally.
 * @param modifier the [Modifier] to be applied to this BottomAppBar
 * @param floatingActionButton optional floating action button at the end of this BottomAppBar
 * @param containerColor the color used for the background of this BottomAppBar. Use
 * [Color.Transparent] to have no color.
 * @param contentColor the preferred color for content inside this BottomAppBar. Defaults to either
 * the matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation when [containerColor] is [ColorScheme.surface], a translucent main color
 * overlay is applied on top of the container. A higher tonal elevation value will result in a
 * darker color in light theme and lighter color in dark theme. See also: [Surface].
 * @param contentPadding the padding applied to the content of this BottomAppBar
 * @param windowInsets a window insets that app bar will respect.
 */
@Composable
public fun BottomAppBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null,
    containerColor: Color = BottomAppBarDefaults.containerColor,
    contentColor: Color = androidx.compose.material3.contentColorFor(containerColor),
    elevation: Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
): Unit = BottomAppBar(
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    elevation = elevation,
    windowInsets = windowInsets,
    contentPadding = contentPadding,
) {
    actions()
    if (floatingActionButton != null) {
        Spacer(Modifier.weight(1f, true))
        Box(
            Modifier
                .fillMaxHeight()
                .padding(
                    top = FABVerticalPadding,
                    end = FABHorizontalPadding,
                ),
            contentAlignment = Alignment.TopStart,
        ) {
            floatingActionButton()
        }
    }
}

/**
 * <a href="https://m3.material.io/components/bottom-app-bar/overview" class="external" target="_blank">Material Design bottom app bar</a>.
 *
 * A bottom app bar displays navigation and key actions at the bottom of mobile screens.
 *
 * ![Bottom app bar image](https://developer.android.com/images/reference/androidx/compose/material3/bottom-app-bar.png)
 *
 * If you are interested in displaying a [FloatingActionButton], consider using another overload.
 *
 * Also see [NavigationBar].
 *
 * @param modifier the [Modifier] to be applied to this BottomAppBar
 * @param containerColor the color used for the background of this BottomAppBar. Use
 * [Color.Transparent] to have no color.
 * @param contentColor the preferred color for content inside this BottomAppBar. Defaults to either
 * the matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation when [containerColor] is [ColorScheme.surface], a translucent main color
 * overlay is applied on top of the container. A higher tonal elevation value will result in a
 * darker color in light theme and lighter color in dark theme. See also: [Surface].
 * @param contentPadding the padding applied to the content of this BottomAppBar
 * @param windowInsets a window insets that app bar will respect.
 * @param content the content of this BottomAppBar. The default layout here is a [Row],
 * so content inside will be placed horizontally.
 */
@Composable
public fun BottomAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color = BottomAppBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = SparkTheme.shapes.none,
        modifier = modifier,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .heightIn(min = ContainerHeight)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

// Padding minus IconButton's min touch target expansion
private val BottomAppBarHorizontalPadding = 16.dp - 12.dp
internal val BottomAppBarVerticalPadding = 16.dp - 12.dp

// Padding minus content padding
private val FABHorizontalPadding = 16.dp - BottomAppBarHorizontalPadding
private val FABVerticalPadding = 12.dp - BottomAppBarVerticalPadding
private val ContainerHeight = 80.0.dp

@Preview(
    group = "AppBar",
    name = "BottomAppBar",
)
@Composable
internal fun BottomAppBarPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(SparkIcons.WheelOutline, contentDescription = "Localized description")
            }
        }

        BottomAppBar(
            actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        sparkIcon = SparkIcons.Check,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        SparkIcons.PenOutline,
                        contentDescription = "Localized description",
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* do something */ },
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    icon = SparkIcons.Plus,
                    contentDescription = "Localized description",
                )
            },
        )
    }
}
