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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.BubbleTextOutline
import com.adevinta.spark.icons.HeartOutline
import com.adevinta.spark.icons.MagnifierOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.UserOutline

internal val SmallBadgeWithContentOffset = 4.dp
internal val MediumBadgeWithContentOffset = 8.dp
internal val BadgeWithNoContentOffset = 4.dp

@InternalSparkApi
@Composable
internal fun SparkBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val anchorId = "anchor"
    val badgeId = "badge"
    Layout(
        {
            Box(
                modifier = Modifier.layoutId(anchorId),
                contentAlignment = Alignment.Center,
                content = content,
            )
            Box(
                modifier = Modifier.layoutId(badgeId),
                content = badge,
            )
        },
        modifier = modifier,
    ) { measurables, constraints ->

        val badgePlaceable = measurables.first { it.layoutId == badgeId }.measure(
            // Measure with loose constraints for height as we don't want the text to take up more
            // space than it needs.
            constraints.copy(minHeight = 0),
        )

        val anchorPlaceable = measurables.first { it.layoutId == anchorId }.measure(constraints)

        val hasContent = badgePlaceable.width > BadgeWithNoContentSize.roundToPx()
        val offset = when {
            hasContent &&
                badgePlaceable.width >= MediumBadgeWithContentOffset.roundToPx() -> MediumBadgeWithContentOffset

            hasContent -> SmallBadgeWithContentOffset
            else -> BadgeWithNoContentOffset
        }.roundToPx()

        val firstBaseline = anchorPlaceable[FirstBaseline]
        val lastBaseline = anchorPlaceable[LastBaseline]
        val totalWidth = anchorPlaceable.width
        val totalHeight = anchorPlaceable.height

        layout(
            totalWidth,
            totalHeight,
            // Provide custom baselines based only on the anchor content to avoid default baseline
            // calculations from including by any badge content.
            mapOf(
                FirstBaseline to firstBaseline,
                LastBaseline to lastBaseline,
            ),
        ) {
            anchorPlaceable.placeRelative(0, 0)
            val badgeX = anchorPlaceable.width - offset
            val badgeY = -badgePlaceable.height / 2
            badgePlaceable.placeRelative(badgeX, badgeY)
        }
    }
}

/**
 * Spark BadgedBox.
 *
 * A badge represents dynamic information such as a number of pending requests in a navigation bar.
 *
 * ![BadgedBox image](https://developer.android.com/images/reference/androidx/compose/material3/badge.png)
 *
 * @param badge the badge to be displayed - typically a Badge
 * @param modifier the Modifier to be applied to this BadgedBox
 * @param content the anchor to which this badge will be positioned
 **/
@ExperimentalSparkApi
@Composable
public fun BadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    SparkBadgedBox(
        badge = badge,
        modifier = modifier,
        content = content,
    )
}

@Preview(
    group = "Badge",
    name = "BadgedBox No Stroke",
)
@Composable
internal fun BadgedBoxNoStrokePreview() {
    PreviewTheme {
        BadgeIntent.values().forEach { intent ->
            BadgeStyle.values().forEach { style ->
                BadgedBoxIntentPreview(
                    style,
                    intent,
                    hasStroke = false,
                )
            }
        }
    }
}

@Preview(
    group = "Badge",
    name = "BadgedBox With Stroke",
)
@Composable
internal fun BadgedBoxWithStrokePreview() {
    PreviewTheme {
        BadgeIntent.values().forEach { intent ->
            BadgeStyle.values().forEach { style ->
                BadgedBoxIntentPreview(
                    style,
                    intent,
                    hasStroke = true,
                )
            }
        }
    }
}

@Composable
private fun BadgedBoxIntentPreview(
    style: BadgeStyle,
    intent: BadgeIntent,
    hasStroke: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        BadgedBox(
            badge = {
                Badge(count = 1, badgeStyle = style, intent = intent, hasStroke = hasStroke)
            },
        ) {
            Icon(
                SparkIcons.HeartOutline,
                modifier = Modifier.size(24.dp),
                contentDescription = "Favorite",
            )
        }
        BadgedBox(
            badge = {
                Badge(count = 100, badgeStyle = style, intent = intent, hasStroke = hasStroke)
            },
        ) {
            Icon(
                SparkIcons.BubbleTextOutline,
                modifier = Modifier.size(24.dp),
                contentDescription = "Notifications",
            )
        }
        BadgedBox(
            badge = {
                Badge(count = 1000, badgeStyle = style, intent = intent, overflowCount = 999, hasStroke = hasStroke)
            },
        ) {
            Icon(
                SparkIcons.MagnifierOutline,
                modifier = Modifier.size(24.dp),
                contentDescription = "Search",
            )
        }
        BadgedBox(
            badge = {
                Badge(badgeStyle = style, intent = intent, hasStroke = hasStroke)
            },
        ) {
            Icon(
                SparkIcons.UserOutline,
                modifier = Modifier.size(24.dp),
                contentDescription = "Notifications",
            )
        }
    }
}
