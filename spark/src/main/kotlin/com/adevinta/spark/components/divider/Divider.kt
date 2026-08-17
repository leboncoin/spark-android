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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
 */
@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .sparkUsageOverlay()
                .fillMaxWidth(),
        ) {
            MaterialHorizontalDivider(
                color = intent.color(),
                modifier = when (labelHorizontalAlignment) {
                    LabelHorizontalAlignment.Start -> Modifier.width(40.dp)

                    LabelHorizontalAlignment.Center,
                    LabelHorizontalAlignment.End,
                    -> Modifier.weight(1f).widthIn(min = 40.dp)
                },
            )

            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(horizontal = 16.dp),
            ) { label() }

            MaterialHorizontalDivider(
                color = intent.color(),
                modifier = when (labelHorizontalAlignment) {
                    LabelHorizontalAlignment.End -> Modifier.width(40.dp)

                    LabelHorizontalAlignment.Center,
                    LabelHorizontalAlignment.Start,
                    -> Modifier.weight(1f).widthIn(min = 40.dp)
                },
            )
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
 */
@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .sparkUsageOverlay()
                .fillMaxHeight(),
        ) {
            MaterialVerticalDivider(
                color = intent.color(),
                modifier = when (labelVerticalAlignment) {
                    LabelVerticalAlignment.Top -> Modifier.height(40.dp)

                    LabelVerticalAlignment.Center,
                    LabelVerticalAlignment.Bottom,
                    -> Modifier.weight(1f).heightIn(min = 40.dp)
                },
            )

            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(vertical = 16.dp),
            ) { label() }

            MaterialVerticalDivider(
                color = intent.color(),
                modifier = when (labelVerticalAlignment) {
                    LabelVerticalAlignment.Bottom -> Modifier.height(40.dp)

                    LabelVerticalAlignment.Center,
                    LabelVerticalAlignment.Top,
                    -> Modifier.weight(1f).heightIn(min = 40.dp)
                },
            )
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
        // text = "jdkdkskjdkkklnljxcljcxlcjvxxcljljxcsdksj\n\ndljjjcdljcjdljcljdsljfdld\nlkjkjisd\nsdsksjddsk\njdksdjslds",
        text = "label",
    )
}
