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
package com.adevinta.spark.components.tags

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.tags.TagDefaults.LeadingIconSize
import com.adevinta.spark.components.tags.TagDefaults.MinHeight
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Accessories
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.contentColorFor
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tools.modifiers.SlotArea
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@InternalSparkApi
@Composable
internal fun BaseSparkTag(
    colors: TagColors,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingIcon: SparkIcon? = null,
    contentDescription: String? = null,
    tint: Color? = null,
    atEnd: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.sparkUsageOverlay(),
        shape = SparkTheme.shapes.full,
        color = colors.backgroundColor,
        contentColor = colors.contentColor.copy(1.0f),
        border = border,
    ) {
        ProvideTextStyle(
            value = SparkTheme.typography.caption.highlight,
        ) {
            Row(
                Modifier
                    .defaultMinSize(minHeight = MinHeight)
                    .padding(vertical = VerticalPadding, horizontal = HorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(LeadingIconEndSpacing, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    modifier = Modifier.invisibleSemantic(),
                    visible = leadingIcon != null,
                ) {
                    if (leadingIcon != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides colors.contentColor,
                        ) {
                            Icon(
                                sparkIcon = leadingIcon,
                                modifier = Modifier.size(LeadingIconSize),
                                contentDescription = contentDescription,
                                tint = tint ?: LocalContentColor.current,
                                atEnd = atEnd,
                            )
                        }
                    } else {
                        // Placeholder so that the animation doesn't breaks when the icon disappears
                        Spacer(Modifier.size(LeadingIconSize))
                    }
                }

                ProvideTextStyle(value = SparkTheme.typography.caption.highlight) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun <T> ColumnScope.AnimatedVisibility(
    value: T?,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.(T) -> Unit,
) {
    // null until non-null then never null again
    var lastNonNullValueOrNull by remember { mutableStateOf(value) }
    lastNonNullValueOrNull = value ?: lastNonNullValueOrNull

    AnimatedVisibility(
        visible = value != null,
        modifier = modifier,
    ) {
        lastNonNullValueOrNull?.let {
            content(it)
        }
    }
}

@Composable
private fun <T> RowScope.AnimatedVisibility(
    value: T?,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.(T) -> Unit,
) {
    // null until non-null then never null again
    var lastNonNullValueOrNull by remember { mutableStateOf(value) }
    lastNonNullValueOrNull = value ?: lastNonNullValueOrNull

    AnimatedVisibility(
        visible = value != null,
        modifier = modifier,
    ) {
        lastNonNullValueOrNull?.let {
            content(it)
        }
    }
}

@InternalSparkApi
@Composable
internal fun SparkTag(
    colors: TagColors,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingIcon: SparkIcon? = null,
    contentDescription: String? = null,
    tint: Color? = null,
    atEnd: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    BaseSparkTag(
        colors = colors,
        modifier = modifier,
        border = border,
        leadingIcon = leadingIcon,
        contentDescription = contentDescription,
        tint = tint,
        atEnd = atEnd,
        content = content,
    )
}

@InternalSparkApi
@Composable
internal fun SparkTag(
    colors: TagColors,
    text: String,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingIcon: SparkIcon? = null,
    contentDescription: String? = null,
    tint: Color? = null,
    atEnd: Boolean = false,
) {
    require(text.isNotBlank() || leadingIcon != null) {
        "text can be blank only when there is an icon"
    }
    BaseSparkTag(
        colors = colors,
        modifier = modifier,
        border = border,
        leadingIcon = leadingIcon,
        tint = tint,
        atEnd = atEnd,
    ) {
        if (text.isNotBlank()) {
            Text(text = text)
        }
    }
}

@InternalSparkApi
@Composable
internal fun SparkTag(
    colors: TagColors,
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingIcon: SparkIcon? = null,
    tint: Color? = null,
    atEnd: Boolean = false,
) {
    require(text.isNotBlank() || leadingIcon != null) {
        "text can be blank only when there is an icon"
    }
    BaseSparkTag(
        colors = colors,
        modifier = modifier,
        border = border,
        leadingIcon = leadingIcon,
        tint = tint,
        atEnd = atEnd,
    ) {
        if (text.isNotBlank()) {
            Text(text = text)
        }
    }
}

@Immutable
public data class TagColors(val backgroundColor: Color, val contentColor: Color)

public object TagDefaults {
    internal val MinHeight = 20.dp

    /**
     * The outlined tag's border size
     */
    internal val OutlinedBorderSize = 1.dp

    /**
     * The size of a tag's leading icon
     */
    internal val LeadingIconSize = 12.dp

    @Composable
    @InternalSparkApi
    public fun tintedColors(
        intent: TagIntent = TagIntent.Neutral,
    ): TagColors {
        val backgroundColor = intent.colors().containerColor
        return TagColors(
            backgroundColor = backgroundColor,
            contentColor = contentColorFor(backgroundColor = backgroundColor),
        )
    }

    @Composable
    internal fun filledColors(
        intent: TagIntent = TagIntent.Basic,
    ): TagColors {
        val backgroundColor = intent.colors().color
        return TagColors(
            backgroundColor = backgroundColor,
            contentColor = contentColorFor(backgroundColor = backgroundColor),
        )
    }

    @Composable
    internal fun outlinedColors(
        intent: TagIntent = TagIntent.Neutral,
    ): TagColors {
        val contentColor = intent.colors().color
        return TagColors(
            backgroundColor = SparkTheme.colors.surface,
            contentColor = contentColor,
        )
    }
}

/**
 * The content padding used by a tag.
 * Used as start padding when there's leading icon, used as eng padding when there's no
 * trailing icon.
 */
private val LeadingIconEndSpacing = 4.dp

private val HorizontalPadding = 8.dp
private val VerticalPadding = 2.dp

@Preview(
    group = "Tags",
    name = "Tag",
)
@Composable
private fun SparkTagPreview() {
    PreviewTheme {
        val colors = TagDefaults.filledColors()
        BaseSparkTag(colors = colors) {
            SlotArea(color = LocalContentColor.current) {
                Text("À la une")
            }
        }
        BaseSparkTag(leadingIcon = SparkIcons.Accessories, colors = colors) {
            SlotArea(color = LocalContentColor.current) {
                Text("À la une")
            }
        }
        BaseSparkTag(leadingIcon = SparkIcons.Accessories, colors = colors) {
        }
    }
}
