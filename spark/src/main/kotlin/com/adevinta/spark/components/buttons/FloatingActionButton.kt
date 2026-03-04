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
package com.adevinta.spark.components.buttons

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import androidx.compose.material3.ExtendedFloatingActionButton as MaterialExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton as MaterialFloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton as MaterialLargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton as MaterialSmallFloatingActionButton

/**
 * <a href="https://m3.material.io/components/floating-action-button/overview" class="external" target="_blank">Material Design floating action button</a>.
 *
 * The FAB represents the most important action on a screen. It puts key actions within reach.
 *
 * ![FAB image](https://developer.android.com/images/reference/androidx/compose/material3/fab.png)
 *
 * FAB typically contains an icon, for a FAB with text and an icon, see
 * [ExtendedFloatingActionButton].
 *
 * @param onClick called when this FAB is clicked
 * @param icon icon to show inside the FAB
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this FAB
 * @param shape defines the shape of this FAB's container and shadow (when using [elevation])
 * @param containerColor the color used for the background of this FAB. Use [Color.Transparent] to
 * have no color.
 * @param contentColor the preferred color for content inside this FAB. Defaults to either the
 * matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation [FloatingActionButtonElevation] used to resolve the elevation for this FAB in
 * different states. This controls the size of the shadow below the FAB. Additionally, when the
 * container color is [ColorScheme.surface], this controls the amount of main color applied as an
 * overlay. See also: [Surface].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this FAB. You can create and pass in your own `remember`ed instance to observe [Interaction]s
 * and customize the appearance / behavior of this FAB in different states.
 */
@Composable
public fun FloatingActionButton(
    onClick: () -> Unit,
    icon: SparkIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MaterialFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = {
            Icon(icon, contentDescription)
        },
    )
}

/**
 * <a href="https://m3.material.io/components/floating-action-button/overview" class="external" target="_blank">Material Design small floating action button</a>.
 *
 * The FAB represents the most important action on a screen. It puts key actions within reach.
 *
 * ![Small FAB image](https://developer.android.com/images/reference/androidx/compose/material3/small-fab.png)
 *
 * @param onClick called when this FAB is clicked
 * @param icon icon to show inside the FAB
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this FAB
 * @param shape defines the shape of this FAB's container and shadow (when using [elevation])
 * @param containerColor the color used for the background of this FAB. Use [Color.Transparent] to
 * have no color.
 * @param contentColor the preferred color for content inside this FAB. Defaults to either the
 * matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation [FloatingActionButtonElevation] used to resolve the elevation for this FAB in
 * different states. This controls the size of the shadow below the FAB. Additionally, when the
 * container color is [ColorScheme.surface], this controls the amount of main color applied as an
 * overlay. See also: [Surface].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this FAB. You can create and pass in your own `remember`ed instance to observe [Interaction]s
 * and customize the appearance / behavior of this FAB in different states.
 */
@Composable
public fun SmallFloatingActionButton(
    onClick: () -> Unit,
    icon: SparkIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.smallShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MaterialSmallFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = {
            Icon(icon, contentDescription)
        },
    )
}

/**
 * <a href="https://m3.material.io/components/floating-action-button/overview" class="external" target="_blank">Material Design large floating action button</a>.
 *
 * The FAB represents the most important action on a screen. It puts key actions within reach.
 *
 * ![Large FAB image](https://developer.android.com/images/reference/androidx/compose/material3/large-fab.png)
 *
 * @param onClick called when this FAB is clicked
 * @param icon icon to show inside the FAB
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this FAB
 * @param shape defines the shape of this FAB's container and shadow (when using [elevation])
 * @param containerColor the color used for the background of this FAB. Use [Color.Transparent] to
 * have no color.
 * @param contentColor the preferred color for content inside this FAB. Defaults to either the
 * matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation [FloatingActionButtonElevation] used to resolve the elevation for this FAB in
 * different states. This controls the size of the shadow below the FAB. Additionally, when the
 * container color is [ColorScheme.surface], this controls the amount of main color applied as an
 * overlay. See also: [Surface].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this FAB. You can create and pass in your own `remember`ed instance to observe [Interaction]s
 * and customize the appearance / behavior of this FAB in different states.
 */
@Composable
public fun LargeFloatingActionButton(
    onClick: () -> Unit,
    icon: SparkIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.largeShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MaterialLargeFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = {
            Icon(
                icon,
                contentDescription,
                modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize),
            )
        },
    )
}

/**
 * <a href="https://m3.material.io/components/extended-fab/overview" class="external" target="_blank">Material Design extended floating action button</a>.
 *
 * Extended FABs help people take main actions. They're wider than FABs to accommodate a text
 * label and larger target area.
 *
 * ![Extended FAB image](https://developer.android.com/images/reference/androidx/compose/material3/extended-fab.png)
 *
 * The other extended floating action button overload supports a text label and icon.
 *
 * @param onClick called when this FAB is clicked
 * @param modifier the [Modifier] to be applied to this FAB
 * @param shape defines the shape of this FAB's container and shadow (when using [elevation])
 * @param containerColor the color used for the background of this FAB. Use [Color.Transparent] to
 * have no color.
 * @param contentColor the preferred color for content inside this FAB. Defaults to either the
 * matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation [FloatingActionButtonElevation] used to resolve the elevation for this FAB in
 * different states. This controls the size of the shadow below the FAB. Additionally, when the
 * container color is [ColorScheme.surface], this controls the amount of main color applied as an
 * overlay. See also: [Surface].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this FAB. You can create and pass in your own `remember`ed instance to observe [Interaction]s
 * and customize the appearance / behavior of this FAB in different states.
 * @param content the content of this FAB, typically a [Text] label
 */
@Composable
public fun ExtendedFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.extendedFabShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    MaterialExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * <a href="https://m3.material.io/components/extended-fab/overview" class="external" target="_blank">Material Design extended floating action button</a>.
 *
 * Extended FABs help people take main actions. They're wider than FABs to accommodate a text
 * label and larger target area.
 *
 * ![Extended FAB image](https://developer.android.com/images/reference/androidx/compose/material3/extended-fab.png)
 *
 * The other extended floating action button overload is for FABs without an icon.
 *
 * Default content description for accessibility is extended from the extended fabs icon. For custom
 * behavior, you can provide your own via `Modifier.semantics`.
 *
 * @param text label displayed inside this FAB
 * @param icon icon to show inside the FAB
 * @param onClick called when this FAB is clicked
 * @param modifier the [Modifier] to be applied to this FAB
 * @param expanded controls the expansion state of this FAB. In an expanded state, the FAB will show
 * both the [icon] and [text]. In a collapsed state, the FAB will show only the [icon].
 * @param shape defines the shape of this FAB's container and shadow (when using [elevation])
 * @param containerColor the color used for the background of this FAB. Use [Color.Transparent] to
 * have no color.
 * @param contentColor the preferred color for content inside this FAB. Defaults to either the
 * matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation [FloatingActionButtonElevation] used to resolve the elevation for this FAB in
 * different states. This controls the size of the shadow below the FAB. Additionally, when the
 * container color is [ColorScheme.surface], this controls the amount of main color applied as an
 * overlay. See also: [Surface].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this FAB. You can create and pass in your own `remember`ed instance to observe [Interaction]s
 * and customize the appearance / behavior of this FAB in different states.
 */
@Composable
public fun ExtendedFloatingActionButton(
    text: @Composable () -> Unit,
    icon: SparkIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    shape: Shape = FloatingActionButtonDefaults.extendedFabShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    MaterialExtendedFloatingActionButton(
        text = text,
        icon = {
            Icon(
                sparkIcon = icon,
                contentDescription = null,
            )
        },
        onClick = onClick,
        modifier = modifier,
        expanded = expanded,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
    )
}

@Preview(
    group = "Buttons",
    name = "DropdownMenu",
)
@Composable
internal fun DropdownMenuPreview() {
    PreviewTheme {
        Text("Large Floating Action button")

        LargeFloatingActionButton(
            onClick = { /* do something */ },
            icon = SparkIcons.PenOutline,
            contentDescription = "Localized description",
        )

        Text("Floating Action button")

        FloatingActionButton(
            onClick = { /* do something */ },
            icon = SparkIcons.PenOutline,
            contentDescription = "Localized description",
        )

        Text("Small Floating Action button")

        SmallFloatingActionButton(
            onClick = { /* do something */ },
            icon = SparkIcons.PenOutline,
            contentDescription = "Localized description",
        )

        Text("Extended Floating Action button")

        ExtendedFloatingActionButton(onClick = { /* do something */ }) {
            Text(text = "Extended FAB")
        }

        var expandedFab by remember {
            mutableStateOf(false)
        }

        ExtendedFloatingActionButton(
            icon = SparkIcons.PenOutline,
            text = { Text(text = "Extended FAB") },
            onClick = { expandedFab = !expandedFab },
            expanded = expandedFab,
        )
    }
}
