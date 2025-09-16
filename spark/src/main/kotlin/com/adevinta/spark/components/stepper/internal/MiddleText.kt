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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.stepper.StepperDefaults
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.DefaultSparkTextFieldColors
import com.adevinta.spark.components.textfields.sparkOutlinedTextFieldColors
import com.adevinta.spark.tokens.dim3
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MiddleText(
    value: Int,
    enabled: Boolean,
    colors: DefaultSparkTextFieldColors,
    modifier: Modifier = Modifier,
    suffix: String = "",
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides colors.selectionColors,
        LocalTextStyle provides SparkTheme.typography.body1,
        LocalContentColor provides if (enabled) SparkTheme.colors.onSurface else SparkTheme.colors.onSurface.dim3,
    ) {
        Box(
            modifier = modifier
                .widthIn(min = 48.dp),
            propagateMinConstraints = true,
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = 10.dp,
                    horizontal = 8.dp,
                ),
                horizontalArrangement = Arrangement.Center,
            ) {
                AnimatedContent(
                    targetState = value,
                    contentAlignment = Alignment.Center,
                    transitionSpec = {
                        // Compare the incoming number with the previous number.
                        if (targetState < initialState) {
                            // If the target number is larger, it slides up and fades in
                            // while the initial (smaller) number slides up and fades out.
                            slideInVertically(
                                StepperDefaults.textAnimationSpec,
                            ) { height -> height } +
                                fadeIn() togetherWith slideOutVertically(
                                    StepperDefaults.textAnimationSpec,
                                ) { height -> -height } +
                                fadeOut()
                        } else {
                            // If the target number is smaller, it slides down and fades in
                            // while the initial number slides down and fades out.
                            slideInVertically(
                                StepperDefaults.textAnimationSpec,
                            ) { height -> -height } +
                                fadeIn() togetherWith slideOutVertically(
                                    StepperDefaults.textAnimationSpec,
                                ) { height -> height } +
                                fadeOut()
                        }.using(
                            // Disable clipping since the faded slide-in/out should
                            // be displayed out of bounds.
                            SizeTransform(clip = false),
                        )
                    },
                ) { count ->
                    Text(
                        text = numberFormat.format(count),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
                if (suffix.isNotEmpty()) {
                    Text(
                        text = suffix,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }
    }
}

private val numberFormat: NumberFormat = NumberFormat.getIntegerInstance().apply {
    isParseIntegerOnly = true
}

@Preview
@Composable
private fun MiddleTextFieldPreview() {
    PreviewTheme {
        MiddleText(
            value = 1,
            enabled = true,
            suffix = "€",
            colors = sparkOutlinedTextFieldColors(),
        )
    }
}
