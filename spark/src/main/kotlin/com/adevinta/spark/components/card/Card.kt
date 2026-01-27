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
package com.adevinta.spark.components.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.dim2
import com.adevinta.spark.tokens.dim4
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@Composable
internal fun SparkCard(
    modifier: Modifier = Modifier,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = colors.containerColor(enabled = true).value,
        contentColor = colors.contentColor(enabled = true).value,
        elevation = elevation.tonalElevation(enabled = true, interactionSource = null).value,
        border = border,
    ) {
        Column(content = content)
    }
}

@Composable
internal fun SparkCard(
    modifier: Modifier = Modifier,
    shape: Shape = SparkTheme.shapes.large,
    colors: Color = SparkTheme.colors.surface,
    borderColor: Color = SparkTheme.colors.outline,
    headingColor: Color = SparkTheme.colors.main,
    elevation: CardElevation = CardDefaults.cardElevation(),
    heading: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = CardDefaults.paddingValues(heading != null),
    content: @Composable ColumnScope.() -> Unit,
) {
    val border = if (borderColor != Color.Unspecified) {
        BorderStroke(1.dp, borderColor)
    } else null
    Surface(
        modifier = modifier.sparkUsageOverlay(),
        shape = shape,
        color = colors,
        elevation = elevation.tonalElevation(enabled = true, interactionSource = null).value,
        border = border,
    ) {
        Column {
            if (heading != null) {
                CardHeading(heading, headingColor, shape)
            }
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    }
}


@Composable
internal fun SparkCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = SparkTheme.shapes.large,
    color: Color = SparkTheme.colors.surface,
    borderColor: Color = SparkTheme.colors.outline,
    headingColor: Color = SparkTheme.colors.main,
    elevation: CardElevation = CardDefaults.cardElevation(),
    heading: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = CardDefaults.paddingValues(heading != null),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    val animatedBorderColor by animateColorAsState(borderColor)
    val animatedBackgroundColor by animateColorAsState(color)
    val border = if (borderColor != Color.Unspecified) {
        BorderStroke(1.dp, animatedBorderColor)
    } else null
    Surface(
        onClick = onClick,
        modifier = modifier.sparkUsageOverlay(),
        shape = shape,
        color = animatedBackgroundColor,
        interactionSource = interactionSource,
        elevation = elevation.tonalElevation(enabled = true, interactionSource = null).value,
        border = border,
    ) {
        Column {
            if (heading != null) {
                CardHeading(heading = heading, headingColor = headingColor, shape = shape)
            }
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    }
}

@Composable
private fun CardHeading(
    heading: @Composable (() -> Unit),
    headingColor: Color,
    shape: Shape,
) {
    val cornerSize = if(shape is CornerBasedShape) shape.topStart else CornerSize(16.dp)
    Box(
        modifier = Modifier
            .height(16.dp)
            .fillMaxWidth()
            .clip(CardHighlightShape(cornerSize))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        headingColor.dim4,
                        headingColor.dim2,
                    ),
                ),
            ),
    ) {
        heading()
    }
}

@Composable
internal fun SparkCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.containerColor(enabled).value,
        contentColor = colors.contentColor(enabled).value,
        elevation = elevation.tonalElevation(enabled, interactionSource).value,
        border = border,
        interactionSource = interactionSource,
    ) {
        Column(content = content)
    }
}

/**
 * Spark card.
 *
 * Cards contain content and actions that relate information about a subject.
 * Filled cards provide subtle separation from the background.
 * This has less emphasis than elevated or outlined cards.
 *
 * ![Card image](https://developer.android.com/images/reference/androidx/compose/material3/filled-card.png)
 *
 * @param modifier the Modifier to be applied to this card
 * @param shape the shape of this card
 * @param colors CardColors that will be used to resolve the colors used for this card in different states.
 * See [CardDefaults.cardColors]
 * @param border the border to draw around the container of this card
 * @param content content of the card
 * @deprecated Use [Card.Flat] for a simple card with default styling, or use [Card] with [CardColors] for
 * more customization. This function is deprecated in favor of the [Card] object variants.
 */
@Deprecated(
    message = "Use Card.Flat for a simple card with default styling, or use Card with CardColors for more customization",
    replaceWith = ReplaceWith(
        "Card.Flat(modifier = modifier, shape = shape, content = content)",
        "com.adevinta.spark.components.card.Card",
    ),
    level = DeprecationLevel.WARNING,
)
@ExperimentalSparkApi
@Composable
public fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.cardColors(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        border = border,
        content = content,
    )
}

public object Card {

    @Composable
    public fun Elevated(
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.surface,
        contentPadding: PaddingValues = PaddingValues(16.dp),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            modifier = modifier,
            shape = shape,
            colors = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.elevatedCardElevation(),
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    public fun Elevated(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.surface,
        contentPadding: PaddingValues = PaddingValues(16.dp),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.elevatedCardElevation(),
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    public fun Outlined(
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.surface,
        borderColor: Color = SparkTheme.colors.outline,
        contentPadding: PaddingValues = PaddingValues(16.dp),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            modifier = modifier,
            shape = shape,
            colors = colors,
            borderColor = borderColor,
            elevation = CardDefaults.outlinedCardElevation(),
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    public fun Outlined(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.surface,
        borderColor: Color = SparkTheme.colors.outline,
        contentPadding: PaddingValues = PaddingValues(16.dp),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = colors,
            borderColor = borderColor,
            elevation = CardDefaults.outlinedCardElevation(),
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    public fun Flat(
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.surface,
        contentPadding: PaddingValues = PaddingValues(16.dp),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            modifier = modifier,
            shape = shape,
            colors = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.cardElevation(),
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    public fun Flat(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.surface,
        contentPadding: PaddingValues = PaddingValues(16.dp),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.cardElevation(),
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    public fun HighlightElevated(
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.main,
        contentPadding: PaddingValues = CardDefaults.paddingValues(true),
        heading: @Composable (() -> Unit) = { },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            modifier = modifier,
            shape = shape,
            headingColor = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.elevatedCardElevation(),
            contentPadding = contentPadding,
            heading = heading,
            content = content,
        )
    }

    @Composable
    public fun HighlightElevated(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.main,
        contentPadding: PaddingValues = CardDefaults.paddingValues(true),
        heading: @Composable (() -> Unit) = { },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            headingColor = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.elevatedCardElevation(),
            contentPadding = contentPadding,
            heading = heading,
            content = content,
        )
    }

    @Composable
    public fun HighlightFlat(
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.main,
        contentPadding: PaddingValues = CardDefaults.paddingValues(true),
        heading: @Composable (() -> Unit) = { },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            modifier = modifier,
            shape = shape,
            headingColor = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.cardElevation(),
            contentPadding = contentPadding,
            heading = heading,
            content = content,
        )
    }

    @Composable
    public fun HighlightFlat(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = SparkTheme.shapes.medium,
        colors: Color = SparkTheme.colors.main,
        contentPadding: PaddingValues = CardDefaults.paddingValues(true),
        heading: @Composable (() -> Unit) = { },
        content: @Composable ColumnScope.() -> Unit,
    ) {
        SparkCard(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            headingColor = colors,
            borderColor = Color.Unspecified,
            elevation = CardDefaults.cardElevation(),
            contentPadding = contentPadding,
            heading = heading,
            content = content,
        )
    }
}

/**
 * Spark card.
 *
 * Cards contain content and actions that relate information about a subject.
 * Filled cards provide subtle separation from the background.
 * This has less emphasis than elevated or outlined cards.
 *
 * ![Card image](https://developer.android.com/images/reference/androidx/compose/material3/filled-card.png)
 *
 * @param onClick called when this card is clicked
 * @param modifier the Modifier to be applied to this card
 * @param enabled controls the enabled state of this card.
 * When false, this component will not respond to user input,
 * and it will appear visually disabled and disabled to accessibility services.
 * @param shape the shape of this card
 * @param colors CardColors that will be used to resolve the colors used for this card in different states.
 * See [CardDefaults.cardColors]
 * @param border the border to draw around the container of this card
 * @param interactionSource the [MutableInteractionSource] representing the stream of Interactions for this card.
 * You can create and pass in your own remembered instance to observe
 * @param content content of the card
 * @deprecated Use [Card.Flat] with onClick for a clickable card with default styling, or use [Card] with onClick
 * and [CardColors] for more customization. This function is deprecated in favor of the [Card] object variants.
 */
@Deprecated(
    message = "Use Card.Flat with onClick for a clickable card with default styling, or use Card with onClick and CardColors for more customization",
    replaceWith = ReplaceWith(
        "Card.Flat(onClick = onClick, modifier = modifier, shape = shape, content = content)",
        "com.adevinta.spark.components.card.Card",
    ),
    level = DeprecationLevel.WARNING,
)
@ExperimentalSparkApi
@Composable
public fun Card(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.cardColors(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkCard(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * <a href="https://m3.material.io/components/cards/overview" class="external" target="_blank">Material Design outlined card</a>.
 *
 * Outlined cards contain content and actions that relate information about a subject.
 * They have a visual boundary around the container.
 * This can provide greater emphasis than the other types.
 *
 * This OutlinedCard does not handle input events - see the other OutlinedCard overloads if you want
 * a clickable or selectable OutlinedCard.
 *
 * ![Outlined card image](https://developer.android.com/images/reference/androidx/compose/material3/outlined-card.png)
 *
 * @param modifier the [Modifier] to be applied to this card
 * @param shape the shape of this card
 * @param colors [CardColors] that will be used to resolve the color(s) used for this card in
 * different states. See [CardDefaults.outlinedCardColors].
 * @param border the border to draw around the container of this card
 * @param content content of the card
 * @deprecated Use [Card.Outlined] instead. This function is deprecated in favor of the [Card] object variants.
 */
@Deprecated(
    message = "Use Card.Outlined instead",
    replaceWith = ReplaceWith(
        "Card.Outlined(modifier = modifier, shape = shape, content = content)",
        "com.adevinta.spark.components.card.Card",
    ),
    level = DeprecationLevel.WARNING,
)
@Composable
public fun OutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    border: BorderStroke = CardDefaults.outlinedCardBorder(),
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = CardDefaults.outlinedCardElevation(),
        border = border,
        content = content,
    )
}

/**
 * <a href="https://m3.material.io/components/cards/overview" class="external" target="_blank">Material Design outlined card</a>.
 *
 * Outlined cards contain content and actions that relate information about a subject.
 * They have a visual boundary around the container.
 * This can provide greater emphasis than the other types.
 *
 * This OutlinedCard handles click events, calling its [onClick] lambda.
 *
 * ![Outlined card image](https://developer.android.com/images/reference/androidx/compose/material3/outlined-card.png)
 *
 * @param onClick called when this card is clicked
 * @param modifier the [Modifier] to be applied to this card
 * @param enabled controls the enabled state of this card. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape the shape of this card
 * @param colors [CardColors] that will be used to resolve the color(s) used for this card in
 * different states. See [CardDefaults.outlinedCardColors].
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this card. You can create and pass in your own `remember`ed instance to observe
 * Interactions and customize the appearance / behavior of this card in different states.
 * @param content content of the card
 * @deprecated Use [Card.Outlined] with onClick instead. This function is deprecated in favor of the [Card] object variants.
 */
@Deprecated(
    message = "Use Card.Outlined with onClick instead",
    replaceWith = ReplaceWith(
        "Card.Outlined(onClick = onClick, modifier = modifier, shape = shape, content = content)",
        "com.adevinta.spark.components.card.Card",
    ),
    level = DeprecationLevel.WARNING,
)
@ExperimentalMaterial3Api
@Composable
public fun OutlinedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkCard(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = CardDefaults.outlinedCardElevation(),
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * Spark Elevated Card.
 *
 * Elevated cards contain content and actions that relate information about a subject. They have a
 * drop shadow, providing more separation from the background than filled cards, but less than
 * outlined cards.
 *
 * This ElevatedCard does not handle input events - see the other ElevatedCard overloads if you
 * want a clickable or selectable ElevatedCard.
 *
 * ![Elevated card image](https://developer.android.com/images/reference/androidx/compose/material3/elevated-card.png)
 *
 * @param modifier the [Modifier] to be applied to this card
 * @param shape the shape of this card
 * @param colors [CardColors] that will be used to resolve the color(s) used for this card in
 * different states. See [CardDefaults.elevatedCardColors].
 * @param content content of the card
 * @deprecated Use [Card.Elevated] instead. This function is deprecated in favor of the [Card] object variants.
 */
@Deprecated(
    message = "Use Card.Elevated instead",
    replaceWith = ReplaceWith(
        "Card.Elevated(modifier = modifier, shape = shape, content = content)",
        "com.adevinta.spark.components.card.Card",
    ),
    level = DeprecationLevel.WARNING,
)
@Composable
public fun ElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = CardDefaults.elevatedCardElevation(),
        content = content,
    )
}

/**
 * Spark Elevated Card.
 *
 * Elevated cards contain content and actions that relate information about a subject. They have a
 * drop shadow, providing more separation from the background than filled cards, but less than
 * outlined cards.
 *
 * This ElevatedCard handles click events, calling its [onClick] lambda.
 *
 * ![Elevated card image](https://developer.android.com/images/reference/androidx/compose/material3/elevated-card.png)
 *
 * @param onClick called when this card is clicked
 * @param modifier the [Modifier] to be applied to this card
 * @param enabled controls the enabled state of this card. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape the shape of this card
 * @param colors [CardColors] that will be used to resolve the color(s) used for this card in
 * different states. See [CardDefaults.elevatedCardColors].
 * @param interactionSource the [MutableInteractionSource] representing the stream of Interactions for this card.
 * You can create and pass in your own remembered instance to observe
 * @param content content of the card
 * @deprecated Use [Card.Elevated] with onClick instead. This function is deprecated in favor of the [Card] object variants.
 */
@Deprecated(
    message = "Use Card.Elevated with onClick instead",
    replaceWith = ReplaceWith(
        "Card.Elevated(onClick = onClick, modifier = modifier, shape = shape, content = content)",
        "com.adevinta.spark.components.card.Card",
    ),
    level = DeprecationLevel.WARNING,
)
@ExperimentalMaterial3Api
@Composable
public fun ElevatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SparkTheme.shapes.medium,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkCard(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = CardDefaults.elevatedCardElevation(),
        interactionSource = interactionSource,
        content = content,
    )
}

@Preview
@Composable
internal fun PreviewCard() {
    PreviewTheme(color = { SparkTheme.colors.backgroundVariant }) {
        Card.Flat {
            Text(
                text = "Card preview",
            )
        }
        Card.Outlined {
            Text(
                text = "Card preview",
            )
        }
        Card.Elevated {
            Text(
                text = "Card preview",
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewInteractiveCard() {
    PreviewTheme(color = { SparkTheme.colors.backgroundVariant }) {

        Card.Flat(
            onClick = {},
        ) {
            Text(
                text = "Card preview",
            )
        }

        Card.Outlined(
            onClick = {},
        ) {
            Text(
                text = "Card preview",
            )
        }
        Card.Elevated(
            onClick = {},
        ) {
            Text(
                text = "Card preview",
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewHighlightCard() {
    PreviewTheme(color = { SparkTheme.colors.backgroundVariant }) {
        Card.HighlightFlat {
            Text(
                text = "Card preview",
            )
        }

        Card.HighlightElevated(
            onClick = {},
        ) {
            Text(
                text = "Card preview",
            )
        }
    }
}
