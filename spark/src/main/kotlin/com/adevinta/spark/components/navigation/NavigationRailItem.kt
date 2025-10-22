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

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.House
import com.adevinta.spark.icons.MagnifierOutline
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.Userfill
import com.adevinta.spark.tokens.highlight
import androidx.compose.material3.NavigationRailItem as MaterialNavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults as MaterialNavigationRailItemDefaults

@InternalSparkApi
@Composable
internal fun SparkNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: SparkIcon,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    alwaysShowLabel: Boolean = true,
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MaterialNavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(sparkIcon = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        },
        modifier = modifier,
        enabled = enabled,
        label = label?.let {
            { Text(text = it, style = SparkTheme.typography.caption.highlight) }
        },
        alwaysShowLabel = alwaysShowLabel,
        colors = colors,
        interactionSource = interactionSource,
    )
}

/**
 * Spark navigation rail item.
 *
 * A [NavigationRailItem] represents a destination within a [NavigationRail].
 *
 * Navigation rails provide access to main destinations in apps when using tablet and desktop
 * screens.
 *
 * The text label is always shown (if it exists) when selected. Showing text labels if not selected
 * is controlled by [alwaysShowLabel].
 *
 * @param selected whether this item is selected
 * @param onClick called when this item is clicked
 * @param icon icon for this item, typically an [Icon]
 * @param modifier the [Modifier] to be applied to this item
 * @param enabled controls the enabled state of this item. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param label optional text label for this item
 * @param alwaysShowLabel whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 * @param colors [NavigationRailItemColors] that will be used to resolve the colors used for this
 * item in different states. See [NavigationRailItemDefaults.colors].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this item in different states.
 */
@ExperimentalSparkApi
@Composable
public fun NavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: SparkIcon,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    alwaysShowLabel: Boolean = true,
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkNavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = colors,
        interactionSource = interactionSource,
    )
}

public object NavigationRailItemDefaults {
    @Composable
    public fun colors(
        selectedIconColor: Color = SparkTheme.colors.onSupportContainer,
        selectedTextColor: Color = SparkTheme.colors.onSurface,
        indicatorColor: Color = SparkTheme.colors.supportContainer,
        unselectedIconColor: Color = SparkTheme.colors.neutral,
        unselectedTextColor: Color = SparkTheme.colors.neutral,
    ): NavigationRailItemColors = MaterialNavigationRailItemDefaults.colors(
        selectedIconColor = selectedIconColor,
        selectedTextColor = selectedTextColor,
        indicatorColor = indicatorColor,
        unselectedIconColor = unselectedIconColor,
        unselectedTextColor = unselectedTextColor,
    )
}

@Preview(
    group = "NavigationRailItem",
    name = "NavigationRailItem",
)
@Composable
internal fun NavigationRailItemPreview() {
    PreviewTheme {
        val items = mutableListOf(
            Pair("Home", SparkIcons.House),
            Pair("Search", SparkIcons.MagnifierOutline),
            Pair("Account", SparkIcons.Userfill),
        )
        items.forEach {
            NavigationRailItem(selected = true, onClick = { }, icon = it.second, label = it.first)
            NavigationRailItem(selected = true, onClick = { }, icon = it.second, label = null)
        }
    }
}
