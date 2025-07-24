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

package com.adevinta.spark.components.toggles

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.AlarmOffFill
import com.adevinta.spark.icons.AlarmOnFill
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.minimumTouchTargetSize
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
@InternalSparkApi
internal fun SparkSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    intent: ToggleIntent = ToggleIntent.Basic,
    icons: SwitchIcons? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    // Icon isn't focusable, no need for content description
    val icon: (@Composable () -> Unit)? = icons?.let {
        {
            Icon(
                sparkIcon = if (checked) it.checked else it.unchecked,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
            )
        }
    }

    MaterialSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        interactionSource = interactionSource,
        enabled = enabled,
        thumbContent = icon,
        colors = intent.toSwitchDefaultsColors(),
        modifier = modifier
            .minimumTouchTargetSize()
            .padding(horizontal = 8.dp)
            .sparkUsageOverlay(),
    )
}

/**
 *
 * Switch component allows the user to activate or deactivate the state of an element or concept.
 * It is usually used as an element to add services, activate functionalities or adjust settings.
 * It is also used to control binary options (On/Off or True/False).
 *
 * @param checked whether or not this component is checked
 * @param onCheckedChange callback to be invoked when Switch is being clicked, therefore the change of checked state is requested. If null, then this is passive and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to  switch layout
 * @param enabled whether the component is enabled or grayed out
 * @param intent The [ToggleIntent] to use to draw the component
 * @param icons represents the pair of icons to use for check/unchecked states, you can use [SwitchDefaults.icons] if you want to use the default ones.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 */
@Deprecated(
    message = "Intent usages for Switch as been deprecated in favour of just using Basic",
    replaceWith = ReplaceWith(
        "Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier, enabled = enabled, error = false, interactionSource = interactionSource)",
    ),
)
@Composable
public fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    intent: ToggleIntent,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icons: SwitchIcons? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        intent = intent,
        icons = icons,
        interactionSource = interactionSource,
    )
}

/**
 *
 * Switch component allows the user to activate or deactivate the state of an element or concept.
 * It is usually used as an element to add services, activate functionalities or adjust settings.
 * It is also used to control binary options (On/Off or True/False).
 *
 * @param checked whether or not this component is checked
 * @param onCheckedChange callback to be invoked when Switch is being clicked, therefore the change of checked state is requested. If null, then this is passive and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to  switch layout
 * @param enabled whether the component is enabled or grayed out
 * @param icons represents the pair of icons to use for check/unchecked states, you can use [SwitchDefaults.icons] if you want to use the default ones.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 */
@Composable
public fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icons: SwitchIcons? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    SparkSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        icons = icons,
        interactionSource = interactionSource,
    )
}

/**
 *
 * Switches are the preferred way to adjust settings. They're used to control binary options – think On/Off or True/False.
 *
 *  - Toggle a single item on or off.
 *  - Immediately activate or deactivate something.
 *
 * @see [SparkSwitch] if you require color customization between states. Be aware that this is still an internal composable so if you need such state contact the Spark team
 *
 * @param checked whether or not this component is checked
 * @param onCheckedChange callback to be invoked when Switch is being clicked, therefore the change of checked state is requested. If null, then this is passive and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to the layout of the switch layout
 * @param enabled whether the component is enabled or grayed out
 * @param intent The [ToggleIntent] to use to draw the component
 * @param icons represents the pair of icons to use for check/unchecked states
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 * @param contentSide The side where we want to show the label, default to [ContentSide.Start].
 * @param content The content displayed before the switch, usually a Text composable shown at the start.
 */
@Deprecated(
    message = "Intent usages for SwitchLabelled as been deprecated in favour of just using Basic with a end " +
        "content side",
    replaceWith = ReplaceWith(
        "SwitchLabelled(checked = checked, onCheckedChange = onCheckedChange, " +
            "modifier = modifier, enabled = enabled, error = false, interactionSource = interactionSource, " +
            "contentSide = contentSide, content = content)",
    ),
)
@Composable
public fun SwitchLabelled(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    intent: ToggleIntent,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icons: SwitchIcons? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentSide: ContentSide = ContentSide.Start,
    content: @Composable RowScope.() -> Unit,
) {
    SparkToggleLabelledContainer(
        state = ToggleableState(checked),
        toggle = {
            SparkSwitch(
                checked = checked,
                onCheckedChange = null,
                interactionSource = interactionSource,
                enabled = enabled,
                intent = intent,
                icons = icons,
                modifier = Modifier.minimumTouchTargetSize(),
            )
        },
        role = Role.Switch,
        onClick = if (onCheckedChange != null) {
            { onCheckedChange(!checked) }
        } else {
            null
        },
        modifier = modifier.selectableGroup(),
        enabled = enabled,
        contentSide = contentSide,
        content = content,
    )
}

/**
 *
 * Switches are the preferred way to adjust settings. They're used to control binary options – think On/Off or True/False.
 *
 *  - Toggle a single item on or off.
 *  - Immediately activate or deactivate something.
 *
 * @see [SparkSwitch] if you require color customization between states. Be aware that this is still an internal composable so if you need such state contact the Spark team
 *
 * @param checked whether or not this component is checked
 * @param onCheckedChange callback to be invoked when Switch is being clicked, therefore the change of checked state is requested. If null, then this is passive and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to the layout of the switch layout
 * @param enabled whether the component is enabled or grayed out
 * @param icons represents the pair of icons to use for check/unchecked states
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 * @param content The content displayed before the switch, usually a Text composable shown at the start.
 */
@Composable
public fun SwitchLabelled(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icons: SwitchIcons? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    SparkToggleLabelledContainer(
        state = ToggleableState(checked),
        toggle = {
            SparkSwitch(
                checked = checked,
                onCheckedChange = null,
                interactionSource = interactionSource,
                enabled = enabled,
                icons = icons,
                modifier = Modifier.minimumTouchTargetSize(),
            )
        },
        role = Role.Switch,
        onClick = if (onCheckedChange != null) {
            { onCheckedChange(!checked) }
        } else {
            null
        },
        modifier = modifier.selectableGroup(),
        enabled = enabled,
        content = content,
    )
}

/**
 * @property checked icon to be used for the thumb in checked state
 * @property unchecked icon to be used for the thumb in unchecked state
 */
public data class SwitchIcons(val checked: SparkIcon = SparkIcons.Check, val unchecked: SparkIcon = SparkIcons.Close)

@Preview(
    group = "Toggles",
    name = "SwitchLabelled",
)
@Composable
private fun AllStatesSwitchLabelledPreview() {
    val text = "This is an example of a multi-line text which is very long and in which the user should read " +
        "all the information."
    val icons = SwitchIcons(
        checked = SparkIcons.AlarmOnFill,
        unchecked = SparkIcons.AlarmOffFill,
    )
    PreviewTheme {
        SwitchLabelled(
            enabled = true,
            checked = true,
            onCheckedChange = {},
            icons = icons,
        ) { Text(text = "Label") }
        SwitchLabelled(
            enabled = true,
            checked = false,
            onCheckedChange = {},
            icons = icons,
        ) { Text("Label") }
        SwitchLabelled(
            enabled = false,
            checked = true,
            onCheckedChange = {},
            icons = icons,
        ) { Text(text) }
        SwitchLabelled(
            enabled = false,
            checked = false,
            onCheckedChange = {},
            icons = icons,
        ) { Text(text) }
    }
}
