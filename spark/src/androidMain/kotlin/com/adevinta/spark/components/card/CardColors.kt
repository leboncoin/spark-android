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
package com.adevinta.spark.components.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

/**
 * Represents the container and content colors used in a card in different states.
 *
 * - See [CardDefaults.cardColors] for the default colors used in a [Card].
 * - See [CardDefaults.elevatedCardColors] for the default colors used in a [ElevatedCard].
 * - See [CardDefaults.outlinedCardColors] for the default colors used in a [OutlinedCard].
 */
@Immutable
public class CardColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color,
    private val disabledContainerColor: Color,
    private val disabledContentColor: Color,
) {
    /**
     * Represents the container color for this card, depending on [enabled].
     *
     * @param enabled whether the card is enabled
     */
    @Composable
    internal fun containerColor(
        enabled: Boolean,
    ): State<Color> = rememberUpdatedState(if (enabled) containerColor else disabledContainerColor)

    /**
     * Represents the content color for this card, depending on [enabled].
     *
     * @param enabled whether the card is enabled
     */
    @Composable
    internal fun contentColor(
        enabled: Boolean,
    ): State<Color> = rememberUpdatedState(if (enabled) contentColor else disabledContentColor)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is CardColors) return false

        if (containerColor != other.containerColor) return false
        if (contentColor != other.contentColor) return false
        if (disabledContainerColor != other.disabledContainerColor) return false
        if (disabledContentColor != other.disabledContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }
}
