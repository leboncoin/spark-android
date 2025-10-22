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
package com.adevinta.spark.components.appbar

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.ThreeDotsVertical
import com.adevinta.spark.tokens.contentColorFor

@Composable
internal fun SparkNavigationBar(
    elevation: Dp,
    modifier: Modifier = Modifier,
    containerColor: Color = SparkTheme.colors.surface,
    contentColor: Color = contentColorFor(containerColor),
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(NavigationBarHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(NavigationBarItemHorizontalPadding),
            content = content,
        )
    }
}

/**
 * <a href="https://m3.material.io/components/navigation-bar/overview" class="external" target="_blank">Material Design bottom navigation bar</a>.
 *
 * Navigation bars offer a persistent and convenient way to switch between main destinations in
 * an app.
 *
 * ![Navigation bar image](https://developer.android.com/images/reference/androidx/compose/material3/navigation-bar.png)
 *
 * [NavigationBar] should contain three to five [NavigationBarItem]s, each representing a singular
 * destination.

 *
 * See [NavigationBarItem] for configuration specific to each item, and not the overall
 * [NavigationBar] component.
 *
 * @param modifier the [Modifier] to be applied to this navigation bar
 * @param elevation when [containerColor] is [ColorScheme.surface], a translucent main color
 * overlay is applied on top of the container. A higher tonal elevation value will result in a
 * darker color in light theme and lighter color in dark theme. See also: [Surface].
 * @param windowInsets a window insets of the navigation bar.
 * @param content the content of this navigation bar, typically 3-5 [NavigationBarItem]s
 */
@ExperimentalSparkApi
@Composable
public fun NavigationBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    elevation: Dp = NavigationBarDefaults.Elevation,
    content: @Composable RowScope.() -> Unit,
) {
    SparkNavigationBar(
        modifier = modifier,
        windowInsets = windowInsets,
        elevation = elevation,
        content = content,
    )
}

/**
 * Material Design navigation bar item.
 *
 * Navigation bars offer a persistent and convenient way to switch between main destinations in
 * an app.
 *
 * The recommended configuration for a [NavigationBarItem] depends on how many items there are
 * inside a [NavigationBar]:
 *
 * - Three destinations: Display icons and text labels for all destinations.
 * - Four destinations: Active destinations display an icon and text label. Inactive destinations
 * display icons, and text labels are recommended.
 * - Five destinations: Active destinations display an icon and text label. Inactive destinations
 * use icons, and use text labels if space permits.
 *
 * A [NavigationBarItem] always shows text labels (if it exists) when selected. Showing text
 * labels if not selected is controlled by [alwaysShowLabel].
 *
 * @param selected whether this item is selected
 * @param onClick called when this item is clicked
 * @param icon icon for this item
 * @param modifier the [Modifier] to be applied to this item
 * @param enabled controls the enabled state of this item. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param label optional text label for this item
 * @param alwaysShowLabel whether to always show the label for this item. If `false`, the label will
 * only be shown when this item is selected.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this item in different states.
 */
@Composable
public fun RowScope.NavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: SparkIcon,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(sparkIcon = icon, contentDescription = null) // already using the label as description
        },
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(),
        interactionSource = interactionSource,
    )
}

private val NavigationBarHeight: Dp = 80.dp
private val NavigationBarItemHorizontalPadding: Dp = 8.dp

@Preview(
    group = "AppBar",
    name = "NavigationBar",
)
@Composable
internal fun PreviewNavigationBar() {
    PreviewTheme(
        padding = PaddingValues(start = 0.dp, top = 16.dp, end = 0.dp, bottom = 0.dp),
    ) {
        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf("Songs", "Artists", "Playlists")

        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(SparkIcons.ThreeDotsVertical, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                )
            }
        }
    }
}
