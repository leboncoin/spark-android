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
package com.adevinta.spark.catalog.configurator.samples.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.ColorSelector
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled

public val CardHighlightedConfigurator: Configurator = Configurator(
    id = "card-highlighted",
    name = "Card Highlighted",
    description = "Highlighted card variants configuration",
    sourceUrl = "$SampleSourceUrl/CardSamples.kt",
) {
    CardHighlightedSample()
}

@Composable
private fun ColumnScope.CardHighlightedSample() {
    var variant by rememberSaveable { mutableStateOf(HighlightedCardVariant.HighlightFlat) }
    var isInteractive by rememberSaveable { mutableStateOf(false) }
    var contentPadding by rememberSaveable { mutableStateOf(CardPaddingOption.Medium) }
    var cardText by rememberSaveable { mutableStateOf("Card content") }
    var cardTitle by rememberSaveable { mutableStateOf("Card Title") }
    var headingText by rememberSaveable { mutableStateOf("Heading") }
    var hasHeading by rememberSaveable { mutableStateOf(true) }
    val defaultColor = SparkTheme.colors.main
    var selectedColor by rememberSaveable(stateSaver = ColorSaver) {
        mutableStateOf(defaultColor)
    }
    var shapeOption by rememberSaveable { mutableStateOf(CardShape.Medium) }

    ConfiguredHighlightedCard(
        modifier = Modifier.fillMaxWidth(),
        variant = variant,
        isInteractive = isInteractive,
        contentPadding = contentPadding.paddingValues,
        cardTitle = cardTitle,
        cardText = cardText,
        headingText = headingText,
        hasHeading = hasHeading,
        headingColor = selectedColor,
        shapeOption = shapeOption,
    )

    ButtonGroup(
        title = "Variant",
        selectedOption = variant,
        onOptionSelect = { variant = it },
    )

    SwitchLabelled(
        checked = isInteractive,
        onCheckedChange = { isInteractive = it },
    ) {
        Text(
            text = "Interactive (onClick)",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    SwitchLabelled(
        checked = hasHeading,
        onCheckedChange = { hasHeading = it },
    ) {
        Text(
            text = "Show Heading",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Content Padding",
        selectedOption = contentPadding,
        onOptionSelect = { contentPadding = it },
    )

    DropdownEnum(
        title = "Shape",
        selectedOption = shapeOption,
        onOptionSelect = { shapeOption = it },
    )

    ColorSelector(
        title = "Heading Color",
        selectedColor = selectedColor,
        onColorSelected = {
            selectedColor = it ?: defaultColor
        },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = headingText,
        onValueChange = { headingText = it },
        label = "Heading Text",
        placeholder = "Enter heading text",
        enabled = hasHeading,
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = cardTitle,
        onValueChange = { cardTitle = it },
        label = "Card Title",
        placeholder = "Enter card title",
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = cardText,
        onValueChange = { cardText = it },
        label = "Card Text",
        placeholder = "Enter card text",
    )
}

private val ColorSaver = Saver<Color, Long>(
    save = { it.value.toLong() },
    restore = { Color(it.toULong()) },
)

@Composable
private fun ConfiguredHighlightedCard(
    variant: HighlightedCardVariant,
    isInteractive: Boolean,
    contentPadding: PaddingValues,
    cardTitle: String,
    cardText: String,
    headingText: String,
    hasHeading: Boolean,
    headingColor: Color,
    shapeOption: CardShape,
    modifier: Modifier = Modifier,
) {
    val shape = shapeOption.shape
    val onClick: (() -> Unit)? = if (isInteractive) {
        { /* Handle click */ }
    } else {
        null
    }

    when (variant) {
        HighlightedCardVariant.HighlightFlat -> {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = modifier,
            ) {
                if (onClick != null) {
                    Card.HighlightFlat(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onClick,
                        shape = shape,
                        colors = headingColor,
                        contentPadding = contentPadding,
                        heading = {
                            if (hasHeading) {
                                Text(
                                    text = headingText,
                                    style = SparkTheme.typography.caption,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        },
                    ) {
                        CardContent(cardTitle = cardTitle, cardText = cardText)
                    }
                } else {
                    Card.HighlightFlat(
                        modifier = Modifier.fillMaxWidth(),
                        shape = shape,
                        colors = headingColor,
                        contentPadding = contentPadding,
                        heading = {
                            if (hasHeading) {
                                Text(
                                    text = headingText,
                                    style = SparkTheme.typography.caption,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        },
                    ) {
                        CardContent(cardTitle = cardTitle, cardText = cardText)
                    }
                }
            }
        }

        HighlightedCardVariant.HighlightElevated -> {
            if (onClick != null) {
                Card.HighlightElevated(
                    modifier = modifier,
                    onClick = onClick,
                    shape = shape,
                    colors = headingColor,
                    contentPadding = contentPadding,
                    heading = {
                        if (hasHeading) {
                            Text(
                                text = headingText,
                                style = SparkTheme.typography.caption,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    },
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            } else {
                Card.HighlightElevated(
                    modifier = modifier,
                    shape = shape,
                    colors = headingColor,
                    contentPadding = contentPadding,
                    heading = {
                        if (hasHeading) {
                            Text(
                                text = headingText,
                                style = SparkTheme.typography.caption,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    },
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            }
        }
    }
}

@Composable
private fun CardContent(
    cardTitle: String,
    cardText: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = cardTitle,
            style = SparkTheme.typography.subhead,
        )
        Text(
            text = cardText,
            style = SparkTheme.typography.body1,
        )
    }
}

private enum class HighlightedCardVariant {
    HighlightFlat,
    HighlightElevated,
}





@Preview
@Composable
private fun CardHighlightedSamplePreview() {
    PreviewTheme { CardHighlightedSample() }
}
