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
package com.adevinta.spark.components.divider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider
import androidx.compose.material3.VerticalDivider as MaterialVerticalDivider

/**
 * LabelHorizontalAlignment is used to define the horizontal alignment of the label in the divider.
 * Alignment can be visible when Label is activated
 */
public enum class LabelHorizontalAlignment {
    Start,
    Center,
    End,
}

/**
 * LabelVerticalAlignment is used to define the vertical alignment of the label in the divider.
 * Alignment can be visible when Label is activated
 */
public enum class LabelVerticalAlignment {
    Top,
    Center,
    Bottom,
}

/**
 * HorizontalDivider Component.
 * A divider is a thin line that groups content in lists and layouts.
 *
 * @param modifier The modifier to be applied to the divider.
 * @param intent The intent defining the color of the divider.
 * @param label The optional label to be displayed on the divider.
 * @param labelHorizontalAlignment The horizontal alignment of the label.
 * ![Horizontal Divider](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.divider_DividerDocumentationScreenshots_horizontalDivider.png)
 */
@Suppress("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@Composable
public fun HorizontalDivider(
    modifier: Modifier = Modifier,
    intent: DividerIntent = DividerIntent.Outline,
    labelHorizontalAlignment: LabelHorizontalAlignment = LabelHorizontalAlignment.Center,
    label: @Composable (BoxScope.() -> Unit)? = null,
) {
    if (label == null) {
        MaterialHorizontalDivider(
            color = intent.color(),
            modifier = modifier
                .sparkUsageOverlay()
                .fillMaxWidth(),
        )
    } else {
        LabelledDivider(
            isHorizontal = true,
            isStartLineFixed = labelHorizontalAlignment == LabelHorizontalAlignment.Start,
            isEndLineFixed = labelHorizontalAlignment == LabelHorizontalAlignment.End,
            modifier = modifier
                .sparkUsageOverlay()
                .fillMaxWidth(),
        ) {
            MaterialHorizontalDivider(color = intent.color())
            Box(modifier = Modifier.padding(horizontal = 16.dp)) { label() }
            MaterialHorizontalDivider(color = intent.color())
        }
    }
}

/**
 * VerticalDivider Component.
 * A divider is a thin line that groups content in lists and layouts.
 *
 * @param modifier The modifier to be applied to the divider.
 * @param intent The intent defining the color of the divider.
 * @param label The optional label to be displayed on the divider.
 * @param labelVerticalAlignment The vertical alignment of the label.
 * ![Vertical Divider](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.divider_DividerDocumentationScreenshots_verticalDivider.png)
 */
@Suppress("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@Composable
public fun VerticalDivider(
    modifier: Modifier = Modifier,
    intent: DividerIntent = DividerIntent.Outline,
    labelVerticalAlignment: LabelVerticalAlignment = LabelVerticalAlignment.Center,
    label: @Composable (BoxScope.() -> Unit)? = null,
) {
    if (label == null) {
        MaterialVerticalDivider(
            color = intent.color(),
            modifier = modifier
                .sparkUsageOverlay()
                .fillMaxHeight(),
        )
    } else {
        LabelledDivider(
            isHorizontal = false,
            isStartLineFixed = labelVerticalAlignment == LabelVerticalAlignment.Top,
            isEndLineFixed = labelVerticalAlignment == LabelVerticalAlignment.Bottom,
            modifier = modifier
                .sparkUsageOverlay()
                .fillMaxHeight(),
        ) {
            MaterialVerticalDivider(color = intent.color())
            Box(modifier = Modifier.padding(vertical = 16.dp)) { label() }
            MaterialVerticalDivider(color = intent.color())
        }
    }
}

private val MinLineLength = 40.dp

/**
 * Lays out a line, a label and a second line along one axis, in that order.
 *
 * The label takes its wrapped size, capped so that each line keeps at least [MinLineLength]. The lines share the
 * remaining space. A fixed line is exactly [MinLineLength] long, and the other line takes the rest. When both lines
 * are flexible, they split the remaining space equally. The three children are centred on the cross axis. Odd
 * remainders round up, as ConstraintLayout did, so that the output stays pixel identical.
 *
 * When the bounded space is smaller than two minimum lines, the lines shrink so that the total never exceeds the
 * space. The horizontal layout follows the layout direction, so the first child is on the right in a right-to-left
 * locale.
 */
@Composable
internal fun LabelledDivider(
    isHorizontal: Boolean,
    isStartLineFixed: Boolean,
    isEndLineFixed: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val minLine = MinLineLength.roundToPx()
        val maxMain = if (isHorizontal) constraints.maxWidth else constraints.maxHeight
        val maxCross = if (isHorizontal) constraints.maxHeight else constraints.maxWidth
        val isBounded = maxMain != Constraints.Infinity

        fun constraintsFor(minMain: Int, maxMain: Int): Constraints = if (isHorizontal) {
            Constraints(minWidth = minMain, maxWidth = maxMain, maxHeight = maxCross)
        } else {
            Constraints(minHeight = minMain, maxHeight = maxMain, maxWidth = maxCross)
        }

        val labelPlaceable = measurables[1].measure(
            constraintsFor(
                minMain = 0,
                maxMain = if (isBounded) (maxMain - 2 * minLine).coerceAtLeast(0) else Constraints.Infinity,
            ),
        )
        val labelMain = if (isHorizontal) labelPlaceable.width else labelPlaceable.height

        // Space for the two lines. It is at least two minimum lines, unless the bounded space is smaller than that.
        val spare = if (isBounded) maxMain - labelMain else 2 * minLine
        val startMain: Int
        val endMain: Int
        when {
            isStartLineFixed -> {
                startMain = minLine.coerceAtMost(spare)
                endMain = spare - startMain
            }

            isEndLineFixed -> {
                endMain = minLine.coerceAtMost(spare)
                startMain = spare - endMain
            }

            else -> {
                startMain = (spare + 1) / 2
                endMain = spare - startMain
            }
        }

        val startPlaceable = measurables[0].measure(constraintsFor(minMain = startMain, maxMain = startMain))
        val endPlaceable = measurables[2].measure(constraintsFor(minMain = endMain, maxMain = endMain))

        val main = startMain + labelMain + endMain
        val cross = if (isHorizontal) {
            maxOf(startPlaceable.height, labelPlaceable.height, endPlaceable.height)
        } else {
            maxOf(startPlaceable.width, labelPlaceable.width, endPlaceable.width)
        }
        val width = constraints.constrainWidth(if (isHorizontal) main else cross)
        val height = constraints.constrainHeight(if (isHorizontal) cross else main)

        layout(width, height) {
            if (isHorizontal) {
                // placeRelative mirrors the position in a right-to-left layout, as ConstraintLayout did.
                startPlaceable.placeRelative(0, (height - startPlaceable.height + 1) / 2)
                labelPlaceable.placeRelative(startMain, (height - labelPlaceable.height + 1) / 2)
                endPlaceable.placeRelative(startMain + labelMain, (height - endPlaceable.height + 1) / 2)
            } else {
                startPlaceable.place((width - startPlaceable.width + 1) / 2, 0)
                labelPlaceable.place((width - labelPlaceable.width + 1) / 2, startMain)
                endPlaceable.place((width - endPlaceable.width + 1) / 2, startMain + labelMain)
            }
        }
    }
}

@Preview(
    group = "Dividers",
    name = "Divider",
)
@Composable
internal fun DividerPreview() {
    PreviewTheme {
        HorizontalDivider(intent = DividerIntent.Outline)
        HorizontalDivider(intent = DividerIntent.OutlineHigh)
        HorizontalDivider(
            label = { TextComposable() },
        )
        HorizontalDivider(
            label = { TextComposable() },
            labelHorizontalAlignment = LabelHorizontalAlignment.End,
        )
        HorizontalDivider(
            label = { TextComposable() },
            labelHorizontalAlignment = LabelHorizontalAlignment.Start,
        )

        Row {
            VerticalDivider(
                label = { TextComposable() },
                labelVerticalAlignment = LabelVerticalAlignment.Top,
            )
            VerticalDivider(
                label = { TextComposable() },
                labelVerticalAlignment = LabelVerticalAlignment.Center,
            )
            VerticalDivider(
                label = { TextComposable() },
                labelVerticalAlignment = LabelVerticalAlignment.Bottom,
            )
            HorizontalSpacer(space = 16.dp)
            VerticalDivider(
                modifier = Modifier.height(24.dp),
                labelVerticalAlignment = LabelVerticalAlignment.Top,
            )
            HorizontalSpacer(space = 16.dp)
            VerticalDivider(modifier = Modifier.height(24.dp))
            HorizontalSpacer(space = 16.dp)
            VerticalDivider(
                modifier = Modifier.height(24.dp),
                labelVerticalAlignment = LabelVerticalAlignment.Bottom,
            )
            HorizontalSpacer(space = 16.dp)
            VerticalDivider(modifier = Modifier.height(24.dp))
            HorizontalSpacer(space = 16.dp)
            VerticalDivider(
                modifier = Modifier.height(24.dp),
                labelVerticalAlignment = LabelVerticalAlignment.Bottom,
            )
            HorizontalSpacer(space = 16.dp)

            VerticalDivider(
                labelVerticalAlignment = LabelVerticalAlignment.Bottom,
            )
        }
    }
}

@Composable
private fun TextComposable(textOverflow: TextOverflow = TextOverflow.Ellipsis) {
    Text(
        textAlign = TextAlign.Center,
        overflow = textOverflow,
        style = SparkTheme.typography.body1,
        text = "label",
    )
}
