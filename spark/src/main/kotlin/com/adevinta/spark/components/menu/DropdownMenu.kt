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
package com.adevinta.spark.components.menu

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.textfields.MultiChoiceComboBox
import com.adevinta.spark.components.textfields.SingleChoiceComboBox
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.MailOutline
import com.adevinta.spark.icons.PenFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WheelOutline
import com.adevinta.spark.tokens.ElevationTokens
import com.adevinta.spark.tokens.EmphasizeDim1
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.SparkTypography
import com.adevinta.spark.tokens.ripple
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.DropdownMenu as MaterialDropdownMenu

/**
 * <a href="https://m3.material.io/components/menus/overview" class="external" target="_blank">Material Design dropdown menu</a>.
 *
 * Menus display a list of choices on a temporary surface. They appear when users interact with a
 * button, action, or other control.
 *
 * ![Dropdown menu image](https://developer.android.com/images/reference/androidx/compose/material3/menu.png)
 *
 * A [DropdownMenu] behaves similarly to a [Popup], and will use the position of the parent layout
 * to position itself on screen. Commonly a [DropdownMenu] will be placed in a [Box] with a sibling
 * that will be used as the 'anchor'. Note that a [DropdownMenu] by itself will not take up any
 * space in a layout, as the menu is displayed in a separate window, on top of other content.
 *
 * The [content] of a [DropdownMenu] will typically be [DropdownMenuItem]s, as well as custom
 * content. Using [DropdownMenuItem]s will result in a menu that matches the Material
 * specification for menus. Also note that the [content] is placed inside a scrollable [Column],
 * so using a [LazyColumn] as the root layout inside [content] is unsupported.
 *
 * [onDismissRequest] will be called when the menu should close for example when there is a
 * tap outside the menu, or when the back key is pressed.
 *
 * [DropdownMenu] changes its positioning depending on the available space, always trying to be
 * fully visible. It will try to expand horizontally, depending on layout direction, to the end of
 * its parent, then to the start of its parent, and then screen end-aligned. Vertically, it will
 * try to expand to the bottom of its parent, then from the top of its parent, and then screen
 * top-aligned. An [offset] can be provided to adjust the positioning of the menu for cases when
 * the layout bounds of its parent do not coincide with its visual bounds. Note the offset will
 * be applied in the direction in which the menu will decide to expand.
 *
 * @param expanded whether the menu is expanded or not
 * @param onDismissRequest called when the user requests to dismiss the menu, such as by tapping
 * outside the menu's bounds
 * @param offset [DpOffset] to be added to the position of the menu
 */
@Composable
public fun DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable DropdownMenuItemColumnScope.() -> Unit,
) {
    MaterialDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.sparkUsageOverlay(),
        offset = offset,
        scrollState = scrollState,
        properties = properties,
        shape = SparkTheme.shapes.small,
        containerColor = SparkTheme.colors.surface,
        tonalElevation = ElevationTokens.Level0,
        shadowElevation = ElevationTokens.Level2,
        border = null,
        content = {
            val scope = remember { DropdownMenuItemWrapper(this) }
            scope.content()
        },
    )
}

/**
 * Popup which contains content for Exposed Dropdown Menu. Should be used inside the content of
 * [ExposedDropdownMenuBox].
 *
 * @param expanded whether the menu is expanded
 * @param onDismissRequest called when the user requests to dismiss the menu, such as by tapping
 *   outside the menu's bounds
 * @param modifier the [Modifier] to be applied to this menu
 * @param scrollState a [ScrollState] used by the menu's content for items vertical scrolling
 * @param matchTextFieldWidth whether the menu's width should be forcefully constrained to match
 *   the width of the text field to which it's attached.
 * @param content the content of the menu
 */
@SuppressLint("ComposeUnstableReceiver")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ExposedDropdownMenuBoxScope.ExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchTextFieldWidth: Boolean = true,
    content: @Composable DropdownMenuItemColumnScope.() -> Unit,
) {
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.sparkUsageOverlay(),
        scrollState = scrollState,
        shape = SparkTheme.shapes.small,
        containerColor = SparkTheme.colors.surface,
        tonalElevation = ElevationTokens.Level0,
        shadowElevation = ElevationTokens.Level2,
        matchTextFieldWidth = matchTextFieldWidth,
        border = null,
        content = {
            val scope = remember { DropdownMenuItemWrapper(this) }
            scope.content()
        },
    )
}

/**
 * Popup which contains content for Exposed Dropdown Menu. Should be used inside the content of
 * [ExposedDropdownMenuBox].
 *
 * @param expanded whether the menu is expanded
 * @param onDismissRequest called when the user requests to dismiss the menu, such as by tapping
 *   outside the menu's bounds
 * @param modifier the [Modifier] to be applied to this menu
 * @param scrollState a [ScrollState] used by the menu's content for items vertical scrolling
 * @param matchTextFieldWidth whether the menu's width should be forcefully constrained to match
 *   the width of the text field to which it's attached.
 * @param content the content of the menu
 */
@SuppressLint("ComposeUnstableReceiver")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ExposedDropdownMenuBoxScope.SingleChoiceExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchTextFieldWidth: Boolean = true,
    content: @Composable SingleChoiceDropdownItemColumnScope.() -> Unit,
) {
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .selectableGroup()
            .sparkUsageOverlay(),
        scrollState = scrollState,
        shape = SparkTheme.shapes.small,
        containerColor = SparkTheme.colors.surface,
        tonalElevation = ElevationTokens.Level0,
        shadowElevation = ElevationTokens.Level2,
        matchTextFieldWidth = matchTextFieldWidth,
        border = null,
        content = {
            val scope = remember { SingleChoiceDropdownItemWrapper(this) }
            scope.content()
        },
    )
}

/**
 * Popup which contains content for Exposed Dropdown Menu. Should be used inside the content of
 * [ExposedDropdownMenuBox].
 *
 * @param expanded whether the menu is expanded
 * @param onDismissRequest called when the user requests to dismiss the menu, such as by tapping
 *   outside the menu's bounds
 * @param modifier the [Modifier] to be applied to this menu
 * @param scrollState a [ScrollState] used by the menu's content for items vertical scrolling
 * @param matchTextFieldWidth whether the menu's width should be forcefully constrained to match
 *   the width of the text field to which it's attached.
 * @param content the content of the menu
 */
@SuppressLint("ComposeUnstableReceiver")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ExposedDropdownMenuBoxScope.MultipleChoiceExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchTextFieldWidth: Boolean = true,
    content: @Composable MultiChoiceDropdownItemColumnScope.() -> Unit,
) {
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.sparkUsageOverlay(),
        scrollState = scrollState,
        shape = SparkTheme.shapes.small,
        containerColor = SparkTheme.colors.surface,
        tonalElevation = ElevationTokens.Level0,
        shadowElevation = ElevationTokens.Level2,
        matchTextFieldWidth = matchTextFieldWidth,
        border = null,
        content = {
            val scope = remember { MultiChoiceDropdownItemWrapper(this) }
            scope.content()
        },
    )
}

/** Scope for the children of a [DropdownMenu] */
public interface DropdownMenuItemColumnScope : ColumnScope

/** Scope for the children of a [SingleChoiceComboBox] */
public interface SingleChoiceDropdownItemColumnScope : ColumnScope

/** Scope for the children of a [MultiChoiceComboBox] */
public interface MultiChoiceDropdownItemColumnScope : ColumnScope

private class DropdownMenuItemWrapper(scope: ColumnScope) :
    DropdownMenuItemColumnScope,
    ColumnScope by scope

private class SingleChoiceDropdownItemWrapper(scope: ColumnScope) :
    SingleChoiceDropdownItemColumnScope,
    ColumnScope by scope

private class MultiChoiceDropdownItemWrapper(scope: ColumnScope) :
    MultiChoiceDropdownItemColumnScope,
    ColumnScope by scope

@Composable
private fun SparkDropdownMenuItem(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp,
    ),
) {
    val colors = MenuDefaults.itemColors()
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Preferred min and max width used during the intrinsic measurement.
            .sizeIn(
                minWidth = 112.dp,
                maxWidth = 280.dp,
                minHeight = 48.dp,
            )
            .padding(contentPadding)
            .sparkUsageOverlay(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProvideTextStyle(SparkTheme.typography.body1) {
            if (leadingIcon != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.leadingIconColor(enabled),
                ) {
                    Box(Modifier.defaultMinSize(minWidth = 24.dp)) {
                        leadingIcon()
                    }
                }
            }
            CompositionLocalProvider(LocalContentColor provides colors.textColor(enabled).value) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(
                            start = if (leadingIcon != null) 8.dp else 0.dp,
                            end = if (trailingIcon != null) 8.dp else 0.dp,
                        ),
                ) {
                    text()
                }
            }
            if (trailingIcon != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.trailingIconColor(enabled),
                ) {
                    Box(Modifier.defaultMinSize(minWidth = 24.dp)) {
                        trailingIcon()
                    }
                }
            }
        }
    }
}

/**
 * <a href="https://m3.material.io/components/menus/overview" class="external" target="_blank">Material Design dropdown menu</a> item.
 *
 * Menus display a list of choices on a temporary surface. They appear when users interact with a
 * button, action, or other control.
 *
 * ![Dropdown menu image](https://developer.android.com/images/reference/androidx/compose/material3/menu.png)
 *
 * @param text text of the menu item
 * @param onClick called when this menu item is clicked
 * @param modifier the [Modifier] to be applied to this menu item
 * @param leadingIcon optional leading icon to be displayed at the beginning of the item's text
 * @param trailingIcon optional trailing icon to be displayed at the end of the item's text. This
 * trailing icon slot can also accept [Text] to indicate a keyboard shortcut.
 * @param enabled controls the enabled state of this menu item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param contentPadding the padding applied to the content of this menu item
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this menu item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this menu item in different states.
 */
@Deprecated(
    message = "DropdownMenuItem is now scoped and can't be used as a Standalone component, migrate to " +
        "the variant that has DropdownMenuItemColumnScope as a receiver",
    replaceWith = ReplaceWith(
        "DropdownMenuItemColumnScope.DropdownMenuItem(text, onClick, modifier, leadingIcon, trailingIcon, enabled, contentPadding, interactionSource)",
    ),
)
@Composable
public fun DropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkDropdownMenuItem(
        text = text,
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onClick,
            interactionSource = interactionSource,
            indication = ripple(true),
        ),
        contentPadding = contentPadding,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
    )
}

/**
 * <a href="https://m3.material.io/components/menus/overview" class="external" target="_blank">Material Design dropdown menu</a> item.
 *
 * Menus display a list of choices on a temporary surface. They appear when users interact with a
 * button, action, or other control.
 *
 * ![Dropdown menu image](https://developer.android.com/images/reference/androidx/compose/material3/menu.png)
 *
 * @param text text of the menu item
 * @param onClick called when this menu item is clicked
 * @param modifier the [Modifier] to be applied to this menu item
 * @param leadingIcon optional leading icon to be displayed at the beginning of the item's text
 * @param trailingIcon optional trailing icon to be displayed at the end of the item's text. This
 * trailing icon slot can also accept [Text] to indicate a keyboard shortcut.
 * @param enabled controls the enabled state of this menu item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param contentPadding the padding applied to the content of this menu item
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this menu item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this menu item in different states.
 */
@Suppress("UnusedReceiverParameter") // Used as namespace
@Composable
public fun DropdownMenuItemColumnScope.DropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkDropdownMenuItem(
        text = text,
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onClick,
            interactionSource = interactionSource,
            indication = ripple(true),
        ),
        contentPadding = contentPadding,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
    )
}

/**
 * <a href="https://m3.material.io/components/menus/overview" class="external" target="_blank">Material Design dropdown menu</a> item.
 *
 * Menus display a list of choices on a temporary surface. They appear when users interact with a
 * button, action, or other control.
 *
 * ![Dropdown menu image](https://developer.android.com/images/reference/androidx/compose/material3/menu.png)
 *
 * @param text text of the menu item
 * @param selected - whether this item is selected or not
 * @param onClick called when this menu item is clicked
 * @param modifier the [Modifier] to be applied to this menu item
 * @param leadingIcon optional leading icon to be displayed at the beginning of the item's text
 * @param enabled controls the enabled state of this menu item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param contentPadding the padding applied to the content of this menu item
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this menu item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this menu item in different states.
 */
@Suppress("UnusedReceiverParameter") // Used as namespace
@Composable
public fun SingleChoiceDropdownItemColumnScope.DropdownMenuItem(
    text: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkDropdownMenuItem(
        text = text,
        modifier = modifier
            .selectable(
                enabled = enabled,
                selected = selected,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple(true),
            )
            .semantics { role = Role.RadioButton },
        contentPadding = contentPadding,
        leadingIcon = leadingIcon,
        trailingIcon = if (selected) {
            @Composable {
                Icon(
                    sparkIcon = SparkIcons.Check,
                    contentDescription = null, // Semantics handled in the Row
                    modifier = Modifier.size(24.dp),
                )
            }
        } else {
            null
        },
        enabled = enabled,
    )
}

/**
 * <a href="https://m3.material.io/components/menus/overview" class="external" target="_blank">Material Design dropdown menu</a> item.
 *
 * Menus display a list of choices on a temporary surface. They appear when users interact with a
 * button, action, or other control.
 *
 * ![Dropdown menu image](https://developer.android.com/images/reference/androidx/compose/material3/menu.png)
 *
 * @param text text of the menu item
 * @param checked whether this item is checked or not
 * @param onCheckedChange callback to be invoked when the item is clicked. therefore the change of
 *   checked state in requested.
 * @param modifier the [Modifier] to be applied to this menu item
 * @param leadingIcon optional leading icon to be displayed at the beginning of the item's text
 * @param enabled controls the enabled state of this menu item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param contentPadding the padding applied to the content of this menu item
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this menu item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this menu item in different states.
 */
@Suppress("UnusedReceiverParameter") // Used as namespace
@Composable
public fun MultiChoiceDropdownItemColumnScope.DropdownMenuItem(
    text: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkDropdownMenuItem(
        text = text,
        modifier = modifier
            .toggleable(
                enabled = enabled,
                value = checked,
                onValueChange = onCheckedChange,
                interactionSource = interactionSource,
                indication = ripple(true),
            ),
        contentPadding = contentPadding,
        leadingIcon = leadingIcon,
        trailingIcon = if (checked) {
            @Composable {
                Icon(
                    sparkIcon = SparkIcons.Check,
                    contentDescription = null, // Semantics handled in the Row
                    modifier = Modifier.size(24.dp),
                )
            }
        } else {
            null
        },
        enabled = enabled,
    )
}

/**
 * Represents the text color for a menu item, depending on its [enabled] state.
 *
 * @param enabled whether the menu item is enabled
 */
@Composable
private fun MenuItemColors.textColor(
    enabled: Boolean,
): State<Color> = rememberUpdatedState(if (enabled) textColor else disabledTextColor)

/**
 * Represents the leading icon color for a menu item, depending on its [enabled] state.
 *
 * @param enabled whether the menu item is enabled
 */
@Composable
private fun MenuItemColors.leadingIconColor(enabled: Boolean): Color =
    if (enabled) leadingIconColor else disabledLeadingIconColor

/**
 * Represents the trailing icon color for a menu item, depending on its [enabled] state.
 *
 * @param enabled whether the menu item is enabled
 */
@Composable
private fun MenuItemColors.trailingIconColor(enabled: Boolean): Color =
    if (enabled) trailingIconColor else disabledTrailingIconColor

/**
 * A group of [DropdownMenuItem] with a [title] label to describe all items from this group.
 *
 *
 * Like in [DropdownMenu] the items are stacks vertically.
 *
 * @param title The title label of the group, it's styled in [SparkTypography.body1] and colored in emphasis [SparkColors.dim1]
 * @param modifier The modifier to be applied to the Group.
 */
@Composable
@ExperimentalSparkApi
public fun DropdownMenuGroupItem(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable DropdownMenuItemColumnScope.() -> Unit,
) {
    Column(modifier = modifier.sparkUsageOverlay()) {
        SectionHeadline {
            title()
        }
        val scope = remember { DropdownMenuItemWrapper(this) }
        scope.content()
    }
}

@Composable
private inline fun SectionHeadline(
    crossinline content: @Composable () -> Unit,
) {
    ProvideTextStyle(
        value = SparkTheme.typography.body2.copy(fontStyle = FontStyle.Italic),
    ) {
        EmphasizeDim1 {
            content()
        }
    }
}

@Preview(
    group = "Menu",
    name = "DropdownMenuItem",
)
@Composable
private fun DropdownMenuItemPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
        contentPadding = 0.dp,
    ) {
        SparkDropdownMenuItem(
            text = { Text("Edit") },
            leadingIcon = {
                Icon(
                    SparkIcons.PenFill,
                    contentDescription = null,
                )
            },
        )
        SparkDropdownMenuItem(
            text = { Text("Save") },
        )
        SparkDropdownMenuItem(
            text = { Text("Settings") },
            enabled = false,
            leadingIcon = {
                Icon(
                    SparkIcons.WheelOutline,
                    contentDescription = null,
                )
                HorizontalDivider()
                SparkDropdownMenuItem(
                    text = { Text("Send Feedback") },
                    leadingIcon = {
                        Icon(
                            SparkIcons.MailOutline,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = { Text("F11", textAlign = TextAlign.Center) },
                )
            },
            trailingIcon = { Text("F11", textAlign = TextAlign.Center) },
        )
        SparkDropdownMenuItem(
            text = { Text("Send Feedback Send Feedback Send Feedback Send Feedback Send Feedback Send Feed") },
            leadingIcon = {
                Icon(
                    SparkIcons.MailOutline,
                    contentDescription = null,
                )
            },
            trailingIcon = { Text("F11", textAlign = TextAlign.Center) },
        )
    }
}

@Preview(
    group = "Menu",
    name = "DropdownMenuGroupItem",
)
@Composable
private fun DropdownMenuGroupItemPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
        contentPadding = 0.dp,
    ) {
        DropdownMenuGroupItem(
            title = { Text("Logiciel") },
        ) {
            SparkDropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = {
                    Icon(
                        SparkIcons.PenFill,
                        contentDescription = null,
                    )
                },
            )
            SparkDropdownMenuItem(
                text = { Text("Save") },
            )
            SparkDropdownMenuItem(
                text = { Text("Settings") },
                enabled = false,
                leadingIcon = {
                    Icon(
                        SparkIcons.WheelOutline,
                        contentDescription = null,
                    )
                },
            )
        }
        SparkDropdownMenuItem(
            text = { Text("Edit") },
            leadingIcon = {
                Icon(
                    SparkIcons.PenFill,
                    contentDescription = null,
                )
            },
        )
        SparkDropdownMenuItem(
            text = { Text("Save") },
        )
    }
}
