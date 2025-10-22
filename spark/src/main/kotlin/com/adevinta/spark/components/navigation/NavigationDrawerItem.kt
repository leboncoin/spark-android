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
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.badge.Badge
import com.adevinta.spark.components.badge.BadgeStyle
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.HeartOutline
import com.adevinta.spark.icons.House
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.Userfill
import androidx.compose.material3.NavigationDrawerItem as MaterialNavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors as MaterialNavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults as MaterialNavigationDrawerItemDefaults

@InternalSparkApi
@Composable
internal fun SparkNavigationDrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: SparkIcon? = null,
    badge: (@Composable () -> Unit)? = null,
    shape: Shape = SparkTheme.shapes.full,
    colors: MaterialNavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MaterialNavigationDrawerItem(
        label = {
            Text(
                text = label,
                style = SparkTheme.typography.small,
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        icon = {
            icon?.let { Icon(sparkIcon = it, contentDescription = null, modifier = Modifier.size(16.dp)) }
        },
        badge = badge,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
    )
}

/**
 * Spark navigation drawer item.
 *
 * A [NavigationDrawerItem] represents a destination within drawers, either [ModalNavigationDrawer],
 * [PermanentNavigationDrawer] or [DismissibleNavigationDrawer].
 *
 * @param label text label for this item
 * @param selected whether this item is selected
 * @param onClick called when this item is clicked
 * @param modifier the [Modifier] to be applied to this item
 * @param icon optional icon for this item, typically an [Icon]
 * @param badge optional badge to show on this item from the end side
 * item in different states. See [NavigationDrawerItemDefaults.colors].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this item in different states.
 */
@ExperimentalSparkApi
@Composable
public fun NavigationDrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: SparkIcon? = null,
    badge: (@Composable () -> Unit)? = null,
    shape: Shape = SparkTheme.shapes.full,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkNavigationDrawerItem(
        label = label,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        badge = badge,
        shape = shape,
        interactionSource = interactionSource,
    )
}

private object NavigationDrawerItemDefaults {
    @Composable
    fun colors(
        selectedContainerColor: Color = SparkTheme.colors.supportContainer,
        unselectedContainerColor: Color = SparkTheme.colors.surface,
        selectedIconColor: Color = SparkTheme.colors.onSupportContainer,
        unselectedIconColor: Color = SparkTheme.colors.neutral,
        selectedTextColor: Color = SparkTheme.colors.onSupportContainer,
        unselectedTextColor: Color = SparkTheme.colors.neutral,
        selectedBadgeColor: Color = selectedTextColor,
        unselectedBadgeColor: Color = unselectedTextColor,
    ): MaterialNavigationDrawerItemColors = MaterialNavigationDrawerItemDefaults.colors(
        selectedContainerColor = selectedContainerColor,
        unselectedContainerColor = unselectedContainerColor,
        selectedIconColor = selectedIconColor,
        unselectedIconColor = unselectedIconColor,
        selectedTextColor = selectedTextColor,
        unselectedTextColor = unselectedTextColor,
        selectedBadgeColor = selectedBadgeColor,
        unselectedBadgeColor = unselectedBadgeColor,
    )
}

@Preview(
    group = "NavigationDrawerItem",
    name = "NavigationDrawerItem",
)
@Composable
internal fun NavigationDrawerItemPreview() {
    PreviewTheme {
        val items = mutableListOf(
            Pair("Home", SparkIcons.House),
            Pair("Favourite", SparkIcons.HeartOutline),
            Pair("Account", SparkIcons.Userfill),
        )
        items.forEachIndexed { index, tab ->
            NavigationDrawerItem(
                selected = index == 0,
                onClick = { },
                label = tab.first,
                icon = tab.second,
                badge = { Badge(badgeStyle = BadgeStyle.Medium) { Text("1") } },
            )
        }
        NavigationDrawerItem(
            selected = false,
            onClick = { },
            label = "Search",
        )
    }
}
