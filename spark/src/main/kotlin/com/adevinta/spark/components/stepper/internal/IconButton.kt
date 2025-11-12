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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.textfields.AnimationDuration
import com.adevinta.spark.components.textfields.DefaultSparkTextFieldColors
import com.adevinta.spark.components.textfields.animateBorderStrokeAsState
import com.adevinta.spark.components.textfields.sparkOutlinedTextFieldColors
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons

@Composable
internal fun IconButton(
    sparkIcon: SparkIcon,
    contentDescription: String,
    enabled: Boolean,
    colors: DefaultSparkTextFieldColors,
    shape: CornerBasedShape,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val borderStroke by animateBorderStrokeAsState(
        enabled = true,
        readOnly = false,
        state = null,
        interactionSource = interactionSource,
        colors = colors,
    )

    val containerColor by animateColorAsState(
        targetValue = colors.containerColor(enabled).value,
        animationSpec = tween(durationMillis = AnimationDuration),
    )
    val contentColor by animateColorAsState(
        targetValue = colors.trailingIconColor(enabled, interactionSource).value,
        animationSpec = tween(durationMillis = AnimationDuration),
    )
    Surface(
        onClick = {
            onClick()
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        },
        modifier = modifier
            .sizeIn(minWidth = 44.dp, minHeight = 44.dp)
            .focusProperties { canFocus = false }, // Focus is show by the parent
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = borderStroke,
    ) {
        Icon(
            sparkIcon = sparkIcon,
            contentDescription = contentDescription,
            modifier = Modifier.requiredSize(16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewIconButton() {
    PreviewTheme {
        IconButton(
            sparkIcon = SparkIcons.Plus,
            contentDescription = "",
            enabled = true,
            shape = SparkTheme.shapes.large,
            onClick = { },
            colors = sparkOutlinedTextFieldColors(),
        )
        IconButton(
            sparkIcon = SparkIcons.Plus,
            contentDescription = "",
            enabled = false,
            shape = SparkTheme.shapes.large,
            onClick = { },
            colors = sparkOutlinedTextFieldColors(),
        )
    }
}
