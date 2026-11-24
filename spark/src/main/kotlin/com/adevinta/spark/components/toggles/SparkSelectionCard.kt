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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.ripple
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * A selection card for a control with three states, such as a checkbox.
 *
 * The whole card is one tri-state toggleable target with [Role.Checkbox].
 * It exposes [ToggleableState] and never `Selected`.
 *
 * @param state the state of the card. Any state other than [ToggleableState.Off] draws the selected border.
 * @param onClick called when the user clicks the card.
 * @param style the resolved card tokens of the control.
 * @param toggle the passive control. The last expression must be a call on [CheckboxCardScope].
 * @param modifier the [Modifier] applied to the card.
 * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
 * @param error the error message that screen readers read aloud, or null when the card has no error.
 * @param layout the position of the [toggle] relative to the [content].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
 * @param content the content of the card.
 */
@InternalSparkApi
@Composable
internal fun SparkSelectionCard(
    state: ToggleableState,
    onClick: () -> Unit,
    style: SelectionCardStyle,
    toggle: @Composable CheckboxCardScope.() -> SelectionCardItem?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: String? = null,
    layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val isError = error != null
    SelectionCardContainer(
        scope = remember(state, isError) { CheckboxCardScopeImpl(state = state, error = isError) },
        toggle = toggle,
        selected = state != ToggleableState.Off,
        selectionModifier = Modifier.triStateToggleable(
            state = state,
            interactionSource = interactionSource,
            indication = SelectionCardIndication,
            enabled = enabled,
            role = Role.Checkbox,
            onClick = onClick,
        ),
        style = style,
        modifier = modifier,
        enabled = enabled,
        error = error,
        layout = layout,
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
 * @param style the resolved card tokens of the control.
 * @param toggle the passive control. The last expression must be a call on [SwitchCardScope].
 * @param modifier the [Modifier] applied to the card.
 * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
 * @param layout the position of the [toggle] relative to the [content].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
 * @param content the content of the card.
 */
@InternalSparkApi
@Composable
internal fun SparkSelectionCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    style: SelectionCardStyle,
    toggle: @Composable SwitchCardScope.() -> SelectionCardItem?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    SelectionCardContainer(
        scope = remember(checked) { SwitchCardScopeImpl(checked = checked) },
        toggle = toggle,
        selected = checked,
        selectionModifier = Modifier.toggleable(
            value = checked,
            interactionSource = interactionSource,
            indication = SelectionCardIndication,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = onCheckedChange,
        ),
        style = style,
        modifier = modifier,
        enabled = enabled,
        error = null,
        layout = layout,
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
 * @param style the resolved card tokens of the control.
 * @param toggle the passive control. The last expression must be a call on [RadioButtonCardScope].
 * @param modifier the [Modifier] applied to the card.
 * @param enabled whether the card reacts to clicks. A disabled card is dimmed once, as a whole.
 * @param error the error message that screen readers read aloud, or null when the card has no error.
 * @param layout the position of the [toggle] relative to the [content].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for the card interactions.
 * @param content the content of the card.
 */
@InternalSparkApi
@Composable
internal fun SparkSelectionCard(
    selected: Boolean,
    onClick: () -> Unit,
    style: SelectionCardStyle,
    toggle: @Composable RadioButtonCardScope.() -> SelectionCardItem?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: String? = null,
    layout: SelectionCardLayout = SelectionCardLayout.Horizontal,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val isError = error != null
    SelectionCardContainer(
        scope = remember(selected, isError) { RadioButtonCardScopeImpl(selected = selected, error = isError) },
        toggle = toggle,
        selected = selected,
        selectionModifier = Modifier.selectable(
            selected = selected,
            interactionSource = interactionSource,
            indication = SelectionCardIndication,
            enabled = enabled,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        style = style,
        modifier = modifier,
        enabled = enabled,
        error = error,
        layout = layout,
        content = content,
    )
}

/**
 * Draws the card. The [selectionModifier] owns the click target, the role and the state semantics.
 * When [error] is not null, the same node also carries it as the error semantics.
 * The non-interactive [Surface] only draws the fill, the border and the shape, so it adds no semantics state.
 * The border draws inside the bounds of the card, so its animated width never moves the content.
 * The [scope] carries the state of the card to the [toggle].
 * The container is generic over the scope, so each card offers only its own control in the [toggle] slot.
 */
// Consumers do not change the layout at runtime, so the content slot is never moved between the two branches.
@Suppress("ContentSlotReused")
@Composable
private fun <S : SelectionCardScope> SelectionCardContainer(
    scope: S,
    toggle: @Composable S.() -> SelectionCardItem?,
    selected: Boolean,
    style: SelectionCardStyle,
    enabled: Boolean,
    error: String?,
    layout: SelectionCardLayout,
    modifier: Modifier = Modifier,
    selectionModifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = style.shape
    Surface(
        modifier = modifier
            .then(if (enabled) Modifier else Modifier.alpha(style.disabledAlpha))
            .clip(shape)
            .then(selectionModifier)
            .ifNotNull(error) { semantics { error(it) } }
            .sparkUsageOverlay(),
        shape = shape,
        border = style.border(selected = selected, error = error != null),
    ) {
        when (layout) {
            SelectionCardLayout.Horizontal -> Row(
                modifier = Modifier.padding(style.contentPadding),
                horizontalArrangement = Arrangement.spacedBy(style.horizontalStartSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                scope.toggle()
                Column(
                    modifier = Modifier.weight(1f),
                    content = content,
                )
            }

            SelectionCardLayout.Vertical -> Column(
                modifier = Modifier.padding(style.contentPadding),
                verticalArrangement = Arrangement.spacedBy(style.verticalTopSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                scope.toggle()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
            }
        }
    }
}

/**
 * The value that a call on a selection card scope returns.
 * The toggle slot must end with such a call, or with `null` to draw no control.
 */
public interface SelectionCardItem

/**
 * The receiver of the toggle slot of a selection card.
 * The card is the only click target, so every control in this scope is passive.
 */
@Stable
public interface SelectionCardScope {

    /**
     * Draws your own passive control in the toggle slot.
     * Use it only when the default control of the card does not fit.
     * Do not add a click handler or a role to [content]. The card already owns them.
     *
     * @param modifier the [Modifier] applied to the box around [content].
     * @param content the content drawn in the toggle slot.
     */
    @Composable
    public fun custom(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    ): SelectionCardItem
}

/**
 * The receiver of the toggle slot of [Checkbox.Card].
 */
@Stable
public interface CheckboxCardScope : SelectionCardScope {
    /**
     * Draws a passive checkbox that follows the state and the error of the card.
     *
     * @param modifier the [Modifier] applied to the checkbox.
     */
    @Composable
    public fun checkbox(
        modifier: Modifier = Modifier,
    ): SelectionCardItem
}

/**
 * The receiver of the toggle slot of [Switch.Card].
 */
@Stable
public interface SwitchCardScope : SelectionCardScope {
    /**
     * Draws a passive switch that follows the checked state of the card.
     *
     * @param modifier the [Modifier] applied to the switch.
     */
    @Composable
    public fun switch(
        modifier: Modifier = Modifier,
    ): SelectionCardItem
}

/**
 * The receiver of the toggle slot of [RadioButton.Card].
 */
@Stable
public interface RadioButtonCardScope : SelectionCardScope {
    /**
     * Draws a passive radio button that follows the selected state and the error of the card.
     *
     * @param modifier the [Modifier] applied to the radio button.
     */
    @Composable
    public fun radioButton(
        modifier: Modifier = Modifier,
    ): SelectionCardItem
}

private object SelectionCardItemImpl : SelectionCardItem

@Suppress("ComposeContentEmitterReturningValues")
private abstract class SelectionCardScopeImpl : SelectionCardScope {

    @Suppress("ContentEmission")
    @Composable
    override fun custom(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ): SelectionCardItem {
        Box(modifier = modifier) { content() }
        return SelectionCardItemImpl
    }
}

// The card dims itself once when disabled, so each control below draws enabled.
// The card owns the click target, the role and the state semantics, so each control has no click handler.
// `invisibleSemantic()` hides the control from screen readers, so they do not announce the state twice.
@Suppress("ComposeContentEmitterReturningValues")
private class CheckboxCardScopeImpl(private val state: ToggleableState, private val error: Boolean) :
    SelectionCardScopeImpl(),
    CheckboxCardScope {

    @Suppress("ContentEmission")
    @Composable
    override fun checkbox(modifier: Modifier): SelectionCardItem {
        Checkbox.Standalone(
            state = state,
            onClick = null,
            modifier = modifier.invisibleSemantic(),
            error = error,
        )
        return SelectionCardItemImpl
    }
}

@Suppress("ComposeContentEmitterReturningValues")
private class RadioButtonCardScopeImpl(private val selected: Boolean, private val error: Boolean) :
    SelectionCardScopeImpl(),
    RadioButtonCardScope {

    @Suppress("ContentEmission")
    @Composable
    override fun radioButton(modifier: Modifier): SelectionCardItem {
        RadioButton.Standalone(
            selected = selected,
            onClick = null,
            modifier = modifier.invisibleSemantic(),
            error = error,
        )
        return SelectionCardItemImpl
    }
}

@Suppress("ComposeContentEmitterReturningValues")
private class SwitchCardScopeImpl(private val checked: Boolean) :
    SelectionCardScopeImpl(),
    SwitchCardScope {

    @Suppress("ContentEmission")
    @Composable
    override fun switch(modifier: Modifier): SelectionCardItem {
        Switch.Standalone(
            checked = checked,
            onCheckedChange = null,
            modifier = modifier.invisibleSemantic(),
        )
        return SelectionCardItemImpl
    }
}

@Composable
private fun ColumnScope.SelectionCardPreviewContent(layout: SelectionCardLayout) {
    SparkSelectionCard(
        state = ToggleableState.On,
        onClick = {},
        style = CheckboxTokens.Card.style,
        modifier = Modifier.fillMaxWidth(),
        layout = layout,
        toggle = { checkbox() },
    ) {
        Text("Checkbox selected")
        Text("Checkbox selected")
    }
    SparkSelectionCard(
        state = ToggleableState.Off,
        onClick = {},
        style = CheckboxTokens.Card.style,
        modifier = Modifier.fillMaxWidth(),
        error = "Select at least one option",
        layout = layout,
        toggle = { checkbox() },
    ) {
        Text("Checkbox in error")
        Text("Checkbox in error")
    }
    SparkSelectionCard(
        selected = true,
        onClick = {},
        style = RadioButtonTokens.Card.style,
        modifier = Modifier.fillMaxWidth(),
        layout = layout,
        toggle = { radioButton() },
    ) {
        Text("Radio button selected")
        Text("Radio button selected")
    }
    SparkSelectionCard(
        checked = true,
        onCheckedChange = {},
        style = SwitchTokens.Card.style,
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        layout = layout,
        toggle = { switch() },
    ) {
        Text("Switch disabled")
        Text("Switch disabled")
    }
}

// The card uses the default Spark ripple for hover and press, not the Figma States/Surface Hover fill.
private val SelectionCardIndication: Indication = ripple()

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
