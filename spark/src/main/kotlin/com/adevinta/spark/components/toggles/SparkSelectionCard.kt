/*
 * Copyright (c) 2026 Adevinta
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
package com.adevinta.spark.components.toggles

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.IntentColors
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.ripple
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

// LBCSPARK-746: default Spark ripple for hover and press, not the Figma States/Surface Hover fill
private val SelectionCardIndication: Indication = ripple()

/**
 * A selection card for a control with three states, such as a checkbox.
 *
 * The whole card is one tri-state toggleable target with [Role.Checkbox].
 * It exposes [ToggleableState] and never `Selected`.
 *
 * @param state the state of the card. Any state other than [ToggleableState.Off] draws the selected border.
 * @param onClick called when the user clicks the card.
 * @param modifier the [Modifier] applied to the card.
 * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
 * @param error whether the card draws the error border.
 * @param layout the position of the [toggle] relative to the [content].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
 * @param toggle the passive control, drawn with no click handler of its own.
 * @param content the content of the card.
 */
@InternalSparkApi
@Composable
internal fun SparkSelectionCard(
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
    layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
    interactionSource: MutableInteractionSource? = null,
    toggle: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    SelectionCardContainer(
        selected = state != ToggleableState.Off,
        selectionModifier = Modifier.triStateToggleable(
            state = state,
            interactionSource = interactionSource,
            indication = SelectionCardIndication,
            enabled = enabled,
            role = Role.Checkbox,
            onClick = onClick,
        ),
        modifier = modifier,
        enabled = enabled,
        error = error,
        layout = layout,
        toggle = toggle,
        content = content,
    )
}

/**
 * A selection card for a control with two states, such as a switch.
 *
 * The whole card is one toggleable target with [Role.Switch].
 * It exposes [ToggleableState] and never `Selected`. It has no error state.
 *
 * @param checked whether the card is checked. A checked card draws the selected border.
 * @param onCheckedChange called with the new value when the user clicks the card.
 * @param modifier the [Modifier] applied to the card.
 * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
 * @param layout the position of the [toggle] relative to the [content].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
 * @param toggle the passive control, drawn with no click handler of its own.
 * @param content the content of the card.
 */
@InternalSparkApi
@Composable
internal fun SparkSelectionCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
    interactionSource: MutableInteractionSource? = null,
    toggle: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    SelectionCardContainer(
        selected = checked,
        selectionModifier = Modifier.toggleable(
            value = checked,
            interactionSource = interactionSource,
            indication = SelectionCardIndication,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = onCheckedChange,
        ),
        modifier = modifier,
        enabled = enabled,
        error = false,
        layout = layout,
        toggle = toggle,
        content = content,
    )
}

/**
 * A selection card for a control picked from a group, such as a radio button.
 *
 * The whole card is one selectable target with [Role.RadioButton].
 * It exposes `Selected` and never [ToggleableState].
 * Put the cards of one group in a parent with `Modifier.selectableGroup()`.
 *
 * @param selected whether the card is selected. A selected card draws the selected border.
 * @param onClick called when the user clicks the card.
 * @param modifier the [Modifier] applied to the card.
 * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
 * @param error whether the card draws the error border.
 * @param layout the position of the [toggle] relative to the [content].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
 * @param toggle the passive control, drawn with no click handler of its own.
 * @param content the content of the card.
 */
@InternalSparkApi
@Composable
internal fun SparkSelectionCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
    layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
    interactionSource: MutableInteractionSource? = null,
    toggle: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    SelectionCardContainer(
        selected = selected,
        selectionModifier = Modifier.selectable(
            selected = selected,
            interactionSource = interactionSource,
            indication = SelectionCardIndication,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        modifier = modifier,
        enabled = enabled,
        error = error,
        layout = layout,
        toggle = toggle,
        content = content,
    )
}

/**
 * Draws the card. The [selectionModifier] owns the click target, the role and the state semantics.
 * The non-interactive [Surface] only draws the fill, the border and the shape, so it adds no semantics state.
 */
@Composable
private fun SelectionCardContainer(
    selected: Boolean,
    selectionModifier: Modifier,
    modifier: Modifier,
    enabled: Boolean,
    error: Boolean,
    layout: SelectionCardLayout,
    toggle: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = SparkTheme.shapes.small
    Surface(
        modifier = modifier
            .then(if (enabled) Modifier else Modifier.alpha(SparkTheme.colors.dim3))
            .clip(shape)
            .then(selectionModifier)
            .sparkUsageOverlay(),
        shape = shape,
        border = SelectionCardDefaults.border(selected = selected, error = error),
    ) {
        when (layout) {
            SelectionCardLayout.Horizontal -> Row(
                modifier = Modifier.padding(SelectionCardDefaults.ContentPadding),
                horizontalArrangement = Arrangement.spacedBy(SelectionCardDefaults.ToggleSpacing),
                verticalAlignment = Alignment.Top,
            ) {
                toggle()
                Column(
                    modifier = Modifier.weight(1f),
                    content = content,
                )
            }

            SelectionCardLayout.Vertical -> Column(
                modifier = Modifier.padding(SelectionCardDefaults.ContentPadding),
                verticalArrangement = Arrangement.spacedBy(SelectionCardDefaults.ToggleSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                toggle()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun SelectionCardPreviewContent(layout: SelectionCardLayout) {
    SparkSelectionCard(
        state = ToggleableState.On,
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        layout = layout,
        toggle = { SparkCheckbox(state = ToggleableState.On, onClick = null) },
    ) {
        Text("Checkbox selected")
    }
    SparkSelectionCard(
        state = ToggleableState.Off,
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        error = true,
        layout = layout,
        toggle = { SparkCheckbox(state = ToggleableState.Off, onClick = null, intent = IntentColors.Danger) },
    ) {
        Text("Checkbox in error")
    }
    SparkSelectionCard(
        selected = true,
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        layout = layout,
        toggle = { SparkRadioButton(selected = true, onClick = null) },
    ) {
        Text("Radio button selected")
    }
    SparkSelectionCard(
        checked = false,
        onCheckedChange = {},
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        layout = layout,
        toggle = { SparkSwitch(checked = false, onCheckedChange = null) },
    ) {
        Text("Switch disabled")
    }
}

@Preview(group = "Toggles", name = "SelectionCard Horizontal")
@Composable
private fun SelectionCardHorizontalPreview() {
    PreviewTheme {
        SelectionCardPreviewContent(layout = SelectionCardLayout.Horizontal)
    }
}

@Preview(group = "Toggles", name = "SelectionCard Vertical")
@Composable
private fun SelectionCardVerticalPreview() {
    PreviewTheme {
        SelectionCardPreviewContent(layout = SelectionCardLayout.Vertical)
    }
}
