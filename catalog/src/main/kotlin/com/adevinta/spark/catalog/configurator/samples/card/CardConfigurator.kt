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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.components.toggles.SwitchLabelled

public val CardConfigurator: Configurator = Configurator(
    id = "card",
    name = "Card",
    description = "Card configuration",
    sourceUrl = "$SampleSourceUrl/CardSamples.kt",
) {
    CardSample()
}

@Composable
private fun ColumnScope.CardSample() {
    var variant by rememberSaveable { mutableStateOf(CardVariant.Flat) }
    var isInteractive by rememberSaveable { mutableStateOf(false) }
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var contentPadding by rememberSaveable { mutableStateOf(PaddingOption.Medium) }
    var cardText by rememberSaveable { mutableStateOf("Card content") }
    var cardTitle by rememberSaveable { mutableStateOf("Card Title") }
    val defaultColor = SparkTheme.colors.surface
    val defaultBorderColor = SparkTheme.colors.surface
    var selectedColor by rememberSaveable(stateSaver = ColorSaver) {
        mutableStateOf(defaultColor)
    }
    var borderColor by rememberSaveable(stateSaver = ColorSaver) {
        mutableStateOf(defaultBorderColor)
    }
    var shapeOption by rememberSaveable { mutableStateOf(CardShape.Medium) }

    ConfiguredCard(
        modifier = Modifier.fillMaxWidth(),
        variant = variant,
        isInteractive = isInteractive,
        isEnabled = isEnabled,
        contentPadding = contentPadding.paddingValues,
        cardTitle = cardTitle,
        cardText = cardText,
        cardColor = selectedColor,
        borderColor = borderColor,
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
        checked = isEnabled,
        onCheckedChange = { isEnabled = it },
    ) {
        Text(
            text = "Enabled",
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
        title = "Card Color",
        selectedColor = selectedColor,
        onColorSelected = {
            selectedColor = it ?: defaultColor
        },
    )

    if (variant == CardVariant.Outlined) {
        ColorSelector(
            title = "Border Color",
            selectedColor = borderColor,
            onColorSelected = {
                borderColor = it ?: defaultBorderColor
            },
        )
    }

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
private fun ConfiguredCard(
    variant: CardVariant,
    isInteractive: Boolean,
    isEnabled: Boolean,
    contentPadding: PaddingValues,
    cardTitle: String,
    cardText: String,
    cardColor: Color,
    borderColor: Color,
    shapeOption: CardShape,
    modifier: Modifier = Modifier,
) {
    val shape = shapeOption.shape
    val onClick: (() -> Unit)? = if (isInteractive && isEnabled) {
        { /* Handle click */ }
    } else {
        null
    }

    when (variant) {
        CardVariant.Flat -> {
            if (onClick != null) {
                Card.Flat(
                    modifier = modifier,
                    onClick = onClick,
                    shape = shape,
                    colors = cardColor,
                    contentPadding = contentPadding,
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            } else {
                Card.Flat(
                    modifier = modifier,
                    shape = shape,
                    colors = cardColor,
                    contentPadding = contentPadding,
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            }
        }

        CardVariant.Elevated -> {
            if (onClick != null) {
                Card.Elevated(
                    modifier = modifier,
                    onClick = onClick,
                    shape = shape,
                    colors = cardColor,
                    contentPadding = contentPadding,
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            } else {
                Card.Elevated(
                    modifier = modifier,
                    shape = shape,
                    colors = cardColor,
                    contentPadding = contentPadding,
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            }
        }

        CardVariant.Outlined -> {
            if (onClick != null) {
                Card.Outlined(
                    modifier = modifier,
                    onClick = onClick,
                    shape = shape,
                    colors = cardColor,
                    borderColor = borderColor,
                    contentPadding = contentPadding,
                ) {
                    CardContent(cardTitle = cardTitle, cardText = cardText)
                }
            } else {
                Card.Outlined(
                    modifier = modifier,
                    shape = shape,
                    colors = cardColor,
                    borderColor = borderColor,
                    contentPadding = contentPadding,
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

private enum class CardVariant {
    Flat,
    Elevated,
    Outlined,
}

private enum class PaddingOption(val paddingValues: PaddingValues) {
    None(PaddingValues(0.dp)),
    Small(PaddingValues(8.dp)),
    Medium(PaddingValues(16.dp)),
    Large(PaddingValues(24.dp)),
    ExtraLarge(PaddingValues(32.dp)),
}

private enum class CardShape {
    None {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.none
    },
    ExtraSmall {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.extraSmall
    },
    Small {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.small
    },
    Medium {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.medium
    },
    Large {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.large
    },
    ExtraLarge {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.extraLarge
    },
    Full {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.full
    },
    ;

    abstract val shape: Shape
        @Composable get
}

@Preview
@Composable
private fun CardSamplePreview() {
    PreviewTheme { CardSample() }
}
