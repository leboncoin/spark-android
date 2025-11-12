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
package com.adevinta.spark.components.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tools.modifiers.ifNotNull
import com.adevinta.spark.tools.modifiers.ifTrue
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

internal val BadgeWithNoContentSize = 12.dp

private const val BADGE_MAX_COUNT = 99

@InternalSparkApi
@Composable
internal fun SparkBadge(
    badgeStyle: BadgeStyle,
    modifier: Modifier = Modifier,
    intent: BadgeIntent = BadgeIntent.Danger,
    hasStroke: Boolean = false,
    content: (@Composable () -> Unit)? = null,
) {
    val size = if (content != null) badgeStyle.size else BadgeWithNoContentSize
    val shape = SparkTheme.shapes.full
    val colors = intent.colors()

    Row(
        modifier = modifier
            .ifTrue(hasStroke) { border(2.dp, SparkTheme.colors.surface, shape).padding(2.dp) }
            .defaultMinSize(minWidth = size, minHeight = size)
            .background(color = colors.color, shape = shape)
            .clip(shape)
            .ifNotNull(content) { padding(horizontal = badgeStyle.contentPadding) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (content != null) {
            CompositionLocalProvider(
                LocalContentColor provides colors.onColor,
            ) {
                val style = badgeStyle.getTextStyle()
                    .copy(platformStyle = PlatformTextStyle(includeFontPadding = false))
                ProvideTextStyle(
                    value = style,
                    content = { content() },
                )
            }
        }
    }
}

/** Spark Badge.
 *
 * A badge is a visual indicator for numeric values such as tallies and scores.
 *
 * @param count to use inside the label
 * @param modifier the Modifier to be applied to this badge
 * @param badgeStyle style of the badge which defines its size
 * @param intent The [BadgeIntent] color to use
 * @param overflowCount defines the max count starting from which + is displayed
 * @param hasStroke whether a border should be drawn
 **/
@Composable
public fun Badge(
    count: Int,
    modifier: Modifier = Modifier,
    badgeStyle: BadgeStyle = BadgeStyle.Medium,
    intent: BadgeIntent = BadgeIntent.Danger,
    overflowCount: Int = BADGE_MAX_COUNT,
    hasStroke: Boolean = false,
) {
    val content: @Composable () -> Unit = {
        Text(
            text = if (count > overflowCount) "$overflowCount+" else "$count",
        )
    }
    val contentA11y = if (count > overflowCount) {
        stringResource(id = R.string.spark_exceed_max_badge_number_a11y)
    } else {
        pluralStringResource(id = R.plurals.spark_badge_a11y, count, count)
    }
    SparkBadge(
        badgeStyle = badgeStyle,
        modifier = modifier
            .semantics {
                contentDescription = contentA11y
            }
            .sparkUsageOverlay(),
        intent = intent,
        hasStroke = hasStroke,
        content = content,
    )
}

/**
 * Spark Badge.
 *
 * A badge is a visual indicator for numeric values such as tallies and scores.
 *
 * @param badgeStyle style of the badge which defines its size
 * @param modifier the Modifier to be applied to this badge
 * @param intent The [BadgeIntent] color to use
 * @param hasStroke whether a border should be drawn
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param content optional content to be rendered inside this badge
 **/
@Composable
public fun Badge(
    modifier: Modifier = Modifier,
    badgeStyle: BadgeStyle = BadgeStyle.Medium,
    intent: BadgeIntent = BadgeIntent.Danger,
    hasStroke: Boolean = false,
    contentDescription: String? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val contentA11y = contentDescription ?: stringResource(id = R.string.spark_badge_numberless_a11y)
    SparkBadge(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentA11y
            }
            .sparkUsageOverlay(),
        badgeStyle = badgeStyle,
        intent = intent,
        hasStroke = hasStroke,
        content = content,
    )
}

@Preview(
    group = "Badge",
    name = "Badge no stroke",
)
@Composable
internal fun BadgeNoStrokeIntentPreview() {
    PreviewTheme {
        BadgeIntent.entries.forEach { intent ->
            BadgeIntentPreview(intent, hasStroke = false)
        }
    }
}

@Preview(
    group = "Badge",
    name = "Badge with stroke",
)
@Composable
internal fun BadgeWithStrokeIntentPreview() {
    PreviewTheme {
        BadgeIntent.entries.forEach { intent ->
            BadgeIntentPreview(intent, hasStroke = true)
        }
    }
}

@Composable
private fun BadgeIntentPreview(intent: BadgeIntent, hasStroke: Boolean) {
    BadgeStyle.entries.forEach {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SparkTheme.colors.neutralContainer)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Badge(badgeStyle = it, intent = intent, hasStroke = hasStroke)
            Badge(badgeStyle = it, intent = intent, hasStroke = hasStroke) { Text("56k") }
            Badge(badgeStyle = it, intent = intent, count = 3, hasStroke = hasStroke)
            Badge(badgeStyle = it, intent = intent, count = 99, hasStroke = hasStroke)
            Badge(badgeStyle = it, intent = intent, count = 200, overflowCount = 99, hasStroke = hasStroke)
            Badge(badgeStyle = it, intent = intent, count = 99, overflowCount = 999, hasStroke = hasStroke)
            Badge(badgeStyle = it, intent = intent, count = 1000, overflowCount = 999, hasStroke = hasStroke)
        }
    }
}
