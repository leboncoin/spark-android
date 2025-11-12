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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.chips.ChipDefaults.LeadingIconEndSpacing
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.tags.TagDefaults.LeadingIconSize
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Accessories
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.contentColorFor
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tools.modifiers.SlotArea
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 * Base composable for Spark tag components that assemble the different building block of these components.
 *
 * @param colors The color scheme for the tag
 * @param modifier modifier for the tag container
 * @param border Optional border styling for the [TagOutlined]
 * @param leadingContent Optional composable content to display, most of the time an icon like on the overload
 * @param shape The shape of the tag (defaults to full rounded)
 * @param size The size variant of the tag
 * @param content The main content of the tag, usually a [Text]
 */
@InternalSparkApi
@Composable
internal fun BaseSparkTag(
    colors: TagColors,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    shape: Shape = SparkTheme.shapes.full,
    size: TagSize = TagSize.Medium,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier.sparkUsageOverlay(),
        shape = shape,
        color = colors.backgroundColor,
        contentColor = colors.contentColor.copy(1.0f),
        border = border,
    ) {
        ProvideTextStyle(
            value = SparkTheme.typography.caption.highlight,
        ) {
            Row(
                Modifier
                    .defaultMinSize(minHeight = size.minHeight.dp)
                    .padding(vertical = size.verticalPadding.dp, horizontal = HorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(LeadingIconEndSpacing, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    modifier = Modifier.invisibleSemantic(),
                    value = leadingContent,
                ) { leadContent ->
                    CompositionLocalProvider(
                        LocalContentColor provides colors.contentColor,
                    ) {
                        leadContent()
                    }
                }

                content()
            }
        }
    }
}

/**
 * Base composable for Spark tag components that assemble the different building block of these components.
 *
 * @param colors The color scheme for the tag
 * @param modifier modifier for the tag container
 * @param border Optional border styling for the [TagOutlined]
 * @param leadingIcon The spark icon shown at the start of the tag
 * @param tint The tint color for the icon. Use Color.Unspecified to not apply tint.
 * @param atEnd Whether the animated vector should be rendered at the end of all its animations.
 * @param content The main content of the tag, usually a [Text]
 */
@InternalSparkApi
@Composable
internal fun BaseSparkTag(
    colors: TagColors,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    leadingIcon: SparkIcon? = null,
    tint: Color? = null,
    atEnd: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val iconContent = leadingIcon?.let {
        @Composable {
            Icon(
                sparkIcon = it,
                modifier = Modifier.size(LeadingIconSize),
                contentDescription = null, // The tag is associated with a mandatory label so it's okay
                tint = tint ?: LocalContentColor.current,
                atEnd = atEnd,
            )
        }
    }
    BaseSparkTag(
        colors = colors,
        modifier = modifier,
        border = border,
        leadingContent = iconContent,
        content = content,
    )
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
    tint: Color? = null,
    atEnd: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    BaseSparkTag(
        colors = colors,
        modifier = modifier,
        border = border,
        leadingIcon = leadingIcon,
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

private val HorizontalPadding = 8.dp

@Preview(
    group = "Tags",
    name = "Tag",
)
@Composable
private fun SparkTagPreview() {
    PreviewTheme {
        val colors = TagDefaults.filledColors()
        BaseSparkTag(colors = colors, atEnd = false) {
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
