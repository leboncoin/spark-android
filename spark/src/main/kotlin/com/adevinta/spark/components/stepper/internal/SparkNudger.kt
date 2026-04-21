/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.components.stepper.internal

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.iconbuttons.IconButtonTokens
import com.adevinta.spark.components.stepper.StepperDefaults
import com.adevinta.spark.components.stepper.applyStep
import com.adevinta.spark.components.stepper.canDecrement
import com.adevinta.spark.components.stepper.canIncrement
import com.adevinta.spark.components.stepper.stepperSemantics
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.Minus
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.tools.modifiers.ifTrue
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Composable
internal fun SparkNudger(
    value: Int?,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..10,
    suffix: String = "",
    step: Int = 1,
    enabled: Boolean = true,
    flexible: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    testTag: String? = null,
    allowSemantics: Boolean = true,
) {
    stepperInputValidator(step, range)
    val colors = StepperDefaults.stepperColors()

    val canDecrement = enabled && canDecrement(value, range)
    val canIncrement = enabled && canIncrement(value, range)

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .ifTrue(allowSemantics) {
                stepperSemantics(
                    value = value,
                    onValueChange = onValueChange,
                    range = range,
                    step = step,
                    suffix = suffix,
                    enabled = enabled,
                )
            }
            .focusable(
                enabled = enabled,
                interactionSource = interactionSource,
            )
            .sparkUsageOverlay(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RepeatableIconButton(
            modifier = Modifier
                .fillMaxHeight()
                .generateStepperTestTag(testTag, "Decrement"),
            sparkIcon = LeboncoinIcons.Minus,
            contentDescription = "", // handled by semantics modifier
            enabled = canDecrement,
            colors = colors,
            shape = IconButtonTokens.resolveFullShape(SparkTheme.shapes.large) as CornerBasedShape,
            interactionSource = interactionSource,
            onClick = { onValueChange(applyStep(value, -step, range)) },
        )

        MiddleText(
            modifier = Modifier
                .then(
                    if (flexible) {
                        Modifier.weight(1.0f)
                    } else {
                        Modifier.widthIn(min = 48.dp)
                    },
                )
                .fillMaxHeight()
                .invisibleSemantic(),
            value = value,
            suffix = suffix,
            enabled = enabled,
            colors = colors,
        )

        RepeatableIconButton(
            modifier = Modifier
                .fillMaxHeight()
                .generateStepperTestTag(testTag, "Increment"),
            sparkIcon = LeboncoinIcons.Plus,
            contentDescription = "", // handled by semantics modifier
            enabled = canIncrement,
            colors = colors,
            shape = IconButtonTokens.resolveFullShape(SparkTheme.shapes.large) as CornerBasedShape,
            interactionSource = interactionSource,
            onClick = { onValueChange(applyStep(value, step, range)) },
        )
    }
}

@Preview
@Composable
private fun PreviewSparkNudger() {
    PreviewTheme {
        SparkNudger(
            value = 1234,
            onValueChange = {},
        )
        SparkNudger(
            value = null,
            onValueChange = {},
        )
        SparkNudger(
            value = 1234,
            onValueChange = {},
            enabled = false,
        )
    }
}
