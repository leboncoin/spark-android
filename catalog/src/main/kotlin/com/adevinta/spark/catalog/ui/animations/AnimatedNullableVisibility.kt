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
package com.adevinta.spark.catalog.ui.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.buttons.ButtonFilled

/**
 * Animated visibility composable that shows/hides content based on a nullable value.
 *
 * Unlike regular [AnimatedVisibility], this composable remembers the last non-null value
 * to ensure the content remains available during exit animations. This is particularly
 * useful when you want to animate the hiding of content that depends on a value that
 * becomes null, as the content would otherwise disappear immediately without animation.
 *
 * The visibility is controlled by whether [value] is null or not:
 * - When [value] becomes non-null, the content appears with the [enter] animation
 * - When [value] becomes null, the content disappears with the [exit] animation
 * - During the exit animation, the last non-null value is used for the content
 *
 * @param value The nullable value that controls visibility. When non-null, content is shown.
 * @param modifier The modifier to be applied to the animation container.
 * @param enter The [EnterTransition] used when the content appears. Defaults to fade + expand.
 * @param exit The [ExitTransition] used when the content disappears. Defaults to shrink + fade.
 * @param label The label used for debugging purposes.
 * @param content The content to show, receiving the non-null value as a parameter.
 *
 */
@Composable
public fun <T> AnimatedNullableVisibility(
    value: T?,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    label: String = "AnimatedVisibility",
    content: @Composable AnimatedVisibilityScope.(T) -> Unit,
) {
    // null until non-null then never null again
    var lastNonNullValueOrNull by remember { mutableStateOf(value) }
    lastNonNullValueOrNull = value ?: lastNonNullValueOrNull

    AnimatedVisibility(
        visible = value != null,
        modifier = modifier,
        enter = enter,
        exit = exit,
        label = label,
    ) {
        lastNonNullValueOrNull?.let {
            content(it)
        }
    }
}

/**
 * Column-scoped animated visibility composable that shows/hides content based on a nullable value.
 *
 * This is a [ColumnScope] extension of [AnimatedNullableVisibility] optimized for vertical layouts.
 * The default animations are tuned for vertical expansion/contraction.
 *
 * @param value The nullable value that controls visibility. When non-null, content is shown.
 * @param modifier The modifier to be applied to the animation container.
 * @param enter The [EnterTransition] used when the content appears. Defaults to fade + expand vertically.
 * @param exit The [ExitTransition] used when the content disappears. Defaults to fade + shrink vertically.
 * @param label The label used for debugging purposes.
 * @param content The content to show, receiving the non-null value as a parameter.
 *
 * @see AnimatedNullableVisibility
 */
@Composable
public fun <T> ColumnScope.AnimatedNullableVisibility(
    value: T?,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandVertically(),
    exit: ExitTransition = fadeOut() + shrinkVertically(),
    label: String = "AnimatedVisibility",
    content: @Composable AnimatedVisibilityScope.(T) -> Unit,
) {
    // null until non-null then never null again
    var lastNonNullValueOrNull by remember { mutableStateOf(value) }
    lastNonNullValueOrNull = value ?: lastNonNullValueOrNull

    AnimatedVisibility(
        visible = value != null,
        modifier = modifier,
        enter = enter,
        exit = exit,
        label = label,
    ) {
        lastNonNullValueOrNull?.let {
            content(it)
        }
    }
}

/**
 * Row-scoped animated visibility composable that shows/hides content based on a nullable value.
 *
 * This is a [RowScope] extension of [AnimatedNullableVisibility] optimized for horizontal layouts.
 * The default animations are tuned for horizontal expansion/contraction.
 *
 * @param value The nullable value that controls visibility. When non-null, content is shown.
 * @param modifier The modifier to be applied to the animation container.
 * @param enter The [EnterTransition] used when the content appears. Defaults to fade + expand horizontally.
 * @param exit The [ExitTransition] used when the content disappears. Defaults to fade + shrink horizontally.
 * @param label The label used for debugging purposes.
 * @param content The content to show, receiving the non-null value as a parameter.
 *
 * @see AnimatedNullableVisibility
 */
@Composable
public fun <T> RowScope.AnimatedNullableVisibility(
    value: T?,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandHorizontally(),
    exit: ExitTransition = fadeOut() + shrinkHorizontally(),
    label: String = "AnimatedVisibility",
    content: @Composable AnimatedVisibilityScope.(T) -> Unit,
) {
    // null until non-null then never null again
    var lastNonNullValueOrNull by remember { mutableStateOf(value) }
    lastNonNullValueOrNull = value ?: lastNonNullValueOrNull

    AnimatedVisibility(
        visible = value != null,
        modifier = modifier,
        enter = enter,
        exit = exit,
        label = label,
    ) {
        lastNonNullValueOrNull?.let {
            content(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAnimatedNullableVisibility() {
    var message by remember { mutableStateOf<String?>(null) }
    var counter by remember { mutableStateOf<Int?>(null) }
    var isRowVisible by remember { mutableStateOf<String?>(null) }

    PreviewTheme {
        // Basic AnimatedNullableVisibility example
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Basic Animation",
                    style = SparkTheme.typography.display2,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ButtonFilled(
                        text = "Show Message",
                        onClick = { message = "Hello, World! 👋" },
                    )

                    ButtonFilled(
                        text = "Hide Message",
                        onClick = { message = null },
                    )
                }

                AnimatedNullableVisibility(
                    value = message,
                    modifier = Modifier.fillMaxWidth(),
                ) { msg ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Blue.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp),
                            )
                            .padding(16.dp),
                    ) {
                        Text(
                            text = msg,
                            style = SparkTheme.typography.body1,
                        )
                    }
                }
            }
        }

        // ColumnScope AnimatedNullableVisibility example
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Column Animation",
                    style = SparkTheme.typography.display2,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ButtonFilled(
                        text = "Increment",
                        onClick = { counter = (counter ?: 0) + 1 },
                    )

                    ButtonFilled(
                        text = "Reset",
                        onClick = { counter = null },
                    )
                }

                AnimatedNullableVisibility(
                    value = counter,
                ) { count ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Green.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp),
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Count: $count",
                            style = SparkTheme.typography.body1,
                        )
                    }
                }
            }
        }

        // RowScope AnimatedNullableVisibility example
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Row Animation",
                    style = SparkTheme.typography.display2,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ButtonFilled(
                        text = "Show Status",
                        onClick = { isRowVisible = "Status: Active 🟢" },
                    )

                    ButtonFilled(
                        text = "Hide Status",
                        onClick = { isRowVisible = null },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Status:")

                    AnimatedNullableVisibility(
                        value = isRowVisible,
                    ) { status ->
                        Box(
                            modifier = Modifier
                                .background(
                                    Color.Yellow.copy(alpha = 0.1f),
                                    RoundedCornerShape(16.dp),
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = status,
                                style = SparkTheme.typography.body1,
                            )
                        }
                    }
                }
            }
        }
    }
}
