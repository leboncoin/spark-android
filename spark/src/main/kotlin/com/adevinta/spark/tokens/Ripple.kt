/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.tokens

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.RippleDefaults
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.unit.Dp
import com.adevinta.spark.SparkTheme

/**
 * Creates a Ripple using the provided values and values inferred from the theme.
 *
 * A Ripple is a Material implementation of [Indication] that expresses different [Interaction]s
 * by drawing ripple animations and state layers.
 *
 * A Ripple responds to [PressInteraction.Press] by starting a new ripple animation and
 * responds to other [Interaction]s by showing a fixed state layer with varying alpha values
 * depending on the [Interaction].
 *
 * [SparkTheme] provides Ripples using [androidx.compose.foundation.LocalIndication], so a Ripple
 * will be used as the default [Indication] inside components such as
 * [androidx.compose.foundation.clickable] and [androidx.compose.foundation.indication], in
 * addition to Material provided components that use a Ripple as well.
 *
 * You can also explicitly create a Ripple and provide it to custom components to change
 * the parameters from the default, such as to create an unbounded ripple with a fixed size.
 *
 * To create a Ripple with a manually defined color that can change over time, see the other
 * [ripple] overload with a [ColorProducer] parameter. This will avoid unnecessary recompositions
 * when changing the color and preserve existing ripple state when the color changes.
 *
 * @param bounded If true, ripples are clipped by the bounds of the target layout. Unbounded
 * ripples always animate from the target layout center, bounded ripples animate from the touch
 * position.
 * @param radius The radius for the ripple. If [Dp.Unspecified] is provided, then the size will be
 * calculated based on the target layout size.
 * @param color The color of the ripple. This color is usually the same color used by the text or
 * iconography in the component. This color will then have [RippleDefaults.RippleAlpha]
 * applied to calculate the final color used to draw the ripple. If [Color.Unspecified] is
 * provided, the color used will be [LocalContentColor] instead.
 */
@Stable
public fun ripple(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
): IndicationNodeFactory {
    return if (radius == Dp.Unspecified && color == Color.Unspecified) {
        if (bounded) return DefaultBoundedRipple else DefaultUnboundedRipple
    } else {
        RippleNodeFactory(bounded, radius, color)
    }
}

/**
 * Creates a Ripple using the provided values and values inferred from the theme.
 *
 * A Ripple is a Material implementation of [Indication] that expresses different [Interaction]s
 * by drawing ripple animations and state layers.
 *
 * A Ripple responds to [PressInteraction.Press] by starting a new ripple animation and
 * responds to other [Interaction]s by showing a fixed state layer with varying alpha values
 * depending on the [Interaction].
 *
 * [SparkTheme] provides Ripples using [androidx.compose.foundation.LocalIndication], so a Ripple
 * will be used as the default [Indication] inside components such as
 * [androidx.compose.foundation.clickable] and [androidx.compose.foundation.indication], in
 * addition to Material provided components that use a Ripple as well.
 *
 * You can also explicitly create a Ripple and provide it to custom components to change
 * the parameters from the default, such as to create an unbounded ripple with a fixed size.
 *
 * To create a Ripple with a static color, see the [ripple] overload with a [Color] parameter. This
 * overload is optimized for Ripples that have dynamic colors that change over time, to reduce
 * unnecessary recompositions.
 *
 * @param color The color of the ripple. This color is usually the same color used by the text or
 * iconography in the component. This color will then have [RippleDefaults.RippleAlpha]
 * applied to calculate the final color used to draw the ripple. If you are creating this
 * [ColorProducer] outside of composition (where it will be automatically remembered), make sure
 * that its instance is stable (such as by remembering the object that holds it), or remember the
 * returned [ripple] object to make sure that ripple nodes are not being created each recomposition.
 * @param bounded If true, ripples are clipped by the bounds of the target layout. Unbounded
 * ripples always animate from the target layout center, bounded ripples animate from the touch
 * position.
 * @param radius The radius for the ripple. If [Dp.Unspecified] is provided, then the size will be
 * calculated based on the target layout size.
 */
@Stable
public fun ripple(
    color: ColorProducer,
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
): IndicationNodeFactory = RippleNodeFactory(bounded, radius, color)

@Stable
private class RippleNodeFactory private constructor(
    private val bounded: Boolean,
    private val radius: Dp,
    private val colorProducer: ColorProducer?,
    private val color: Color,
) : IndicationNodeFactory {
    constructor(
        bounded: Boolean,
        radius: Dp,
        colorProducer: ColorProducer,
    ) : this(bounded, radius, colorProducer, Color.Unspecified)

    constructor(
        bounded: Boolean,
        radius: Dp,
        color: Color,
    ) : this(bounded, radius, null, color)

    override fun create(interactionSource: InteractionSource): DelegatableNode {
        val colorProducer = colorProducer ?: ColorProducer { color }
        return DelegatingThemeAwareRippleNode(interactionSource, bounded, radius, colorProducer)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RippleNodeFactory) return false

        if (bounded != other.bounded) return false
        if (radius != other.radius) return false
        if (colorProducer != other.colorProducer) return false
        return color == other.color
    }

    override fun hashCode(): Int {
        var result = bounded.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + colorProducer.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private class DelegatingThemeAwareRippleNode(
    private val interactionSource: InteractionSource,
    private val bounded: Boolean,
    private val radius: Dp,
    private val color: ColorProducer,
) : DelegatingNode(),
    CompositionLocalConsumerModifierNode,
    ObserverModifierNode {
    private var rippleNode: DelegatableNode? = null

    override fun onAttach() {
        updateConfiguration()
    }

    override fun onObservedReadsChanged() {
        updateConfiguration()
    }

    /**
     * Handles [LocalRippleConfiguration] changing between null / non-null. Changes to
     * [RippleConfiguration.color] and [RippleConfiguration.rippleAlpha] are handled as part of
     * the ripple definition.
     */
    private fun updateConfiguration() {
        observeReads {
            val configuration = currentValueOf(LocalRippleConfiguration)
            if (configuration == null) {
                removeRipple()
            } else {
                if (rippleNode == null) attachNewRipple()
            }
        }
    }

    private fun attachNewRipple() {
        val calculateColor = ColorProducer {
            val userDefinedColor = color()
            if (userDefinedColor.isSpecified) {
                userDefinedColor
            } else {
                // If this is null, the ripple will be removed, so this should always be non-null in
                // normal use
                val rippleConfiguration = currentValueOf(LocalRippleConfiguration)
                if (rippleConfiguration?.color?.isSpecified == true) {
                    rippleConfiguration.color
                } else {
                    currentValueOf(LocalContentColor)
                }
            }
        }

        val calculateRippleAlpha = {
            // If this is null, the ripple will be removed, so this should always be non-null in
            // normal use
            val rippleConfiguration = currentValueOf(LocalRippleConfiguration)
            rippleConfiguration?.rippleAlpha ?: RippleAlpha
        }

        rippleNode = delegate(
            createRippleModifierNode(
                interactionSource,
                bounded,
                radius,
                calculateColor,
                calculateRippleAlpha,
            ),
        )
    }

    private fun removeRipple() {
        rippleNode?.let { undelegate(it) }
    }
}

private val DefaultBoundedRipple = RippleNodeFactory(
    bounded = true,
    radius = Dp.Unspecified,
    color = Color.Unspecified,
)
private val DefaultUnboundedRipple = RippleNodeFactory(
    bounded = false,
    radius = Dp.Unspecified,
    color = Color.Unspecified,
)

/**
 * Represents the default [RippleAlpha] that will be used for a ripple to indicate different
 * states.
 */
internal val RippleAlpha: RippleAlpha = RippleAlpha(
    pressedAlpha = 0.36f,
    focusedAlpha = 0.24f,
    draggedAlpha = 0.16f,
    hoveredAlpha = 0.24f,
)
