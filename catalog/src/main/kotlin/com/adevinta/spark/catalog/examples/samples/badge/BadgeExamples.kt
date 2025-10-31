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
package com.adevinta.spark.catalog.examples.samples.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.badge.Badge
import com.adevinta.spark.components.badge.BadgeIntent
import com.adevinta.spark.components.badge.BadgeStyle
import com.adevinta.spark.components.badge.BadgedBox
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.MailOutline
import com.adevinta.spark.icons.SparkIcons

private const val BadgeExampleSourceUrl = "$SampleSourceUrl/BadgeSamples.kt"

public val BadgeExamples: List<Example> = listOf(
    Example(
        id = "default",
        name = "Default Badge",
        description = "Basic badge with numeric content",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        BadgeDefault()
    },
    Example(
        id = "styles",
        name = "Badge Styles",
        description = "Different badge sizes and styles",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        BadgeStyles()
    },
    Example(
        id = "intents",
        name = "Badge Intents",
        description = "Badge with different color intents",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        BadgeIntents()
    },
    Example(
        id = "overflow",
        name = "Badge Overflow",
        description = "Badge with overflow handling for large numbers",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        BadgeOverflow()
    },
    Example(
        id = "badged-box",
        name = "Badged Box",
        description = "Badge positioned over other components using BadgedBox",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        BadgeBadgedBox()
    },
    Example(
        id = "stroke",
        name = "Badge with Stroke",
        description = "Badge with border stroke",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        BadgeWithStroke()
    },
)

@Preview
@Composable
private fun ColumnScope.BadgeDefault() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Basic numeric badge")
        Badge(count = 5)

        Text("Badge with custom content")
        Badge {
            Text("New")
        }

        Text("Empty badge (dot indicator)")
        Badge()
    }
}

@Preview
@Composable
private fun ColumnScope.BadgeStyles() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Different badge sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Badge(count = 1, badgeStyle = BadgeStyle.Small)
            Badge(count = 2, badgeStyle = BadgeStyle.Medium)
        }

        Text("Different styles with content")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Badge(badgeStyle = BadgeStyle.Small) { Text("S") }
            Badge(badgeStyle = BadgeStyle.Medium) { Text("M") }
        }
    }
}

@Preview
@Composable
private fun ColumnScope.BadgeIntents() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Badge with different intents")
        BadgeIntent.entries.forEach { intent ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(count = 1, intent = intent)
                Badge(count = 5, intent = intent)
                Badge(intent = intent) { Text("New") }
            }
        }
    }
}

@Preview
@Composable
private fun ColumnScope.BadgeOverflow() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Badge overflow behavior")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Badge(count = 99)
            Badge(count = 100)
            Badge(count = 1000, overflowCount = 999)
        }

        Text("Custom overflow count")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Badge(count = 50, overflowCount = 25)
            Badge(count = 100, overflowCount = 50)
        }
    }
}

@Preview
@Composable
private fun ColumnScope.BadgeBadgedBox() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Badge positioned over icons and buttons")

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BadgedBox(badge = { Badge(count = 5) }) {
                Icon(SparkIcons.MailOutline, contentDescription = "Mail")
            }

            BadgedBox(badge = { Badge() }) {
                Text(text = "Notifications")
            }
        }

        Text("Badge positioned over surface")
        BadgedBox(
            badge = { Badge(count = 3, badgeStyle = BadgeStyle.Medium) },
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("Content with badge overlay")
            }
        }
    }
}

@Preview
@Composable
private fun ColumnScope.BadgeWithStroke() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Badge with stroke (border)")

        BadgeIntent.entries.forEach { intent ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(count = 1, intent = intent, hasStroke = true)
                Badge(count = 5, intent = intent, hasStroke = true)
                Badge(intent = intent, hasStroke = true) { Text("New") }
            }
        }
    }
}
