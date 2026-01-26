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
package com.adevinta.spark.catalog.examples.samples.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.LikeOutline
import com.adevinta.spark.icons.SparkIcons
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.adevinta.spark.components.text.Text as SparkText

private const val CardExampleSourceUrl = "$SampleSourceUrl/CardSamples.kt"

public val CardExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "flat",
        name = "Flat Card",
        description = "Basic filled card with default styling",
        sourceUrl = CardExampleSourceUrl,
    ) {
        CardFlat()
    },
    Example(
        id = "elevated",
        name = "Elevated Card",
        description = "Card with elevation and drop shadow",
        sourceUrl = CardExampleSourceUrl,
    ) {
        CardElevated()
    },
    Example(
        id = "outlined",
        name = "Outlined Card",
        description = "Card with border outline",
        sourceUrl = CardExampleSourceUrl,
    ) {
        CardOutlined()
    },
    Example(
        id = "interactive",
        name = "Interactive Card",
        description = "Clickable card with onClick handler",
        sourceUrl = CardExampleSourceUrl,
    ) {
        CardInteractive()
    },
    Example(
        id = "content",
        name = "Card Content",
        description = "Cards with different content types",
        sourceUrl = CardExampleSourceUrl,
    ) {
        CardContent()
    },
    Example(
        id = "states",
        name = "Card States",
        description = "Enabled and disabled card states",
        sourceUrl = CardExampleSourceUrl,
    ) {
        CardStates()
    },
)

@Preview
@Composable
private fun ColumnScope.CardFlat() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SparkText(
            text = "Flat Card",
            style = SparkTheme.typography.subhead,
        )
        Card.Flat {
            SparkText(
                text = "This is a flat card with default styling. It provides subtle separation from the background.",
            )
        }
        Card.Flat(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(24.dp),
        ) {
            SparkText(
                text = "Flat card with custom padding",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "You can customize the content padding to adjust spacing.",
            )
        }
    }
}

@Preview
@Composable
private fun ColumnScope.CardElevated() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SparkText(
            text = "Elevated Card",
            style = SparkTheme.typography.subhead,
        )
        Card.Elevated {
            SparkText(
                text = "This is an elevated card with a drop shadow. It provides more separation from the background than flat cards.",
            )
        }
        Card.Elevated(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        ) {
            SparkText(
                text = "Elevated card with content",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "Elevated cards are useful for drawing attention to important content.",
            )
        }
    }
}

@Preview
@Composable
private fun ColumnScope.CardOutlined() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SparkText(
            text = "Outlined Card",
            style = SparkTheme.typography.subhead,
        )
        Card.Outlined {
            SparkText(
                text = "This is an outlined card with a border. It provides clear visual separation from the background.",
            )
        }
        Card.Outlined(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        ) {
            SparkText(
                text = "Outlined card example",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "Outlined cards can provide greater emphasis than other card types.",
            )
        }
    }
}

@Preview
@Composable
private fun ColumnScope.CardInteractive() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SparkText(
            text = "Interactive Cards",
            style = SparkTheme.typography.subhead,
        )
        Card.Flat(
            onClick = {},
        ) {
            SparkText(
                text = "Clickable flat card",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "Tap to interact with this card.",
            )
        }
        Card.Elevated(
            onClick = {},
        ) {
            SparkText(
                text = "Clickable elevated card",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "Interactive cards respond to user input.",
            )
        }
        Card.Outlined(
            onClick = {},
        ) {
            SparkText(
                text = "Clickable outlined card",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "All card variants can be made interactive.",
            )
        }
    }
}

@Preview
@Composable
private fun ColumnScope.CardContent() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SparkText(
            text = "Card Content Examples",
            style = SparkTheme.typography.subhead,
        )
        Card.Elevated {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SparkText(
                    text = "Card with text content",
                    style = SparkTheme.typography.subhead,
                )
                SparkText(
                    text = "Cards can contain various types of content including text, images, and interactive elements.",
                )
            }
        }
        Card.Elevated {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        sparkIcon = SparkIcons.LikeOutline,
                        contentDescription = "Heart icon",
                        modifier = Modifier.size(24.dp),
                    )
                    SparkText(
                        text = "Card with icon and text",
                        style = SparkTheme.typography.subhead,
                    )
                }
                SparkText(
                    text = "You can combine different elements within a card.",
                )
            }
        }
        Card.Elevated {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SparkText(
                    text = "Card with button",
                    style = SparkTheme.typography.subhead,
                )
                SparkText(
                    text = "Cards can include interactive elements like buttons.",
                )
                ButtonFilled(
                    text = "Action",
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun ColumnScope.CardStates() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SparkText(
            text = "Card States",
            style = SparkTheme.typography.subhead,
        )
        Card.Flat(
            onClick = {},
        ) {
            SparkText(
                text = "Enabled interactive card",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "This card is enabled and can be interacted with.",
            )
        }
        Card.Elevated(
            onClick = {},
        ) {
            SparkText(
                text = "Enabled elevated card",
                style = SparkTheme.typography.subhead,
            )
            SparkText(
                text = "Interactive cards can be enabled or disabled.",
            )
        }
    }
}
