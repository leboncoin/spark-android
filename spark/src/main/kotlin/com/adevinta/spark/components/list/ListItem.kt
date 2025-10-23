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
package com.adevinta.spark.components.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcons
import androidx.compose.material3.ListItem as MaterialListItem

@ExperimentalSparkApi
@Composable
internal fun SparkListItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
) {
    MaterialListItem(
        headlineContent = headlineContent,
        modifier = modifier,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    )
}

/**
 * Lists are continuous, vertical indexes of text or images.
 *
 * This component can be used to achieve the list item templates existing in the spec. One-line list
 * items have a singular line of headline content. Two-line list items additionally have either
 * supporting or overline content. Three-line list items have either both supporting and overline
 * content, or extended (two-line) supporting text.
 *
 * @param headlineContent the headline content of the list item
 * @param modifier [Modifier] to be applied to the list item
 * @param overlineContent the content displayed above the headline content
 * @param supportingContent the supporting content of the list item
 * @param leadingContent the leading content of the list item
 * @param trailingContent the trailing meta text, icon, switch or checkbox
 * @param colors [ListItemColors] that will be used to resolve the background and content color for
 * this list item in different states. See [ListItemDefaults.colors]
 * @param tonalElevation the tonal elevation of this list item
 * @param shadowElevation the shadow elevation of this list item
 */
@ExperimentalSparkApi
@Composable
public fun ListItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
) {
    SparkListItem(
        headlineContent = headlineContent,
        modifier = modifier,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    )
}

@Preview(
    group = "List",
    name = "One Line",
)
@Composable
internal fun OneLineListItemPreview() {
    PreviewTheme {
        Column {
            ListItem(
                headlineContent = { Text("One line list item with 24x24 icon") },
                leadingContent = {
                    Icon(
                        sparkIcon = SparkIcons.HeartFill,
                        contentDescription = "Localized description",
                    )
                },
            )
            HorizontalDivider()
        }
    }
}

@Preview(
    group = "List",
    name = "Two Line",
)
@Composable
internal fun TwoLineListItemPreview() {
    PreviewTheme {
        Column {
            ListItem(
                headlineContent = { Text("Two line list item with trailing") },
                supportingContent = { Text("Secondary text") },
                trailingContent = { Text("meta") },
                leadingContent = {
                    Icon(
                        sparkIcon = SparkIcons.HeartFill,
                        contentDescription = "Localized description",
                    )
                },
            )
            HorizontalDivider()
        }
    }
}

@Preview(
    group = "List",
    name = "Three Line",
)
@Composable
internal fun ThreeLineListItemPreview() {
    PreviewTheme {
        Column {
            ListItem(
                headlineContent = { Text("Three line list item") },
                overlineContent = { Text("OVERLINE") },
                supportingContent = { Text("Secondary text") },
                leadingContent = {
                    Icon(
                        sparkIcon = SparkIcons.HeartFill,
                        contentDescription = "Localized description",
                    )
                },
                trailingContent = { Text("meta") },
            )
            HorizontalDivider()
        }
    }
}

@Preview(
    group = "List",
    name = "Three Line Extended Content",
)
@Composable
internal fun ThreeLineContentListItemPreview() {
    PreviewTheme {
        Column {
            ListItem(
                headlineContent = { Text("Three line list item") },
                supportingContent = {
                    Text("Secondary text that is long and perhaps goes onto another line")
                },
                leadingContent = {
                    Icon(
                        sparkIcon = SparkIcons.HeartFill,
                        contentDescription = "Localized description",
                    )
                },
                trailingContent = { Text("meta") },
            )
            HorizontalDivider()
        }
    }
}
