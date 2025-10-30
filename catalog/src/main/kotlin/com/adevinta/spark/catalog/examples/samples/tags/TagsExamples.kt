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
package com.adevinta.spark.catalog.examples.samples.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.tags.TagHighlight
import com.adevinta.spark.components.tags.TagHighlightBadge
import com.adevinta.spark.components.tags.TagIntent
import com.adevinta.spark.components.tags.TagOutlined
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.icons.Booster
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons

private const val TagsExampleDescription = "Tags examples"
private const val TagsExampleSourceUrl = "$SampleSourceUrl/TagSamples.kt"

@OptIn(ExperimentalLayoutApi::class)
public val TagsExamples: List<Example> = listOf(
    Example(
        id = "filled",
        name = "Filled Tag",
        description = TagsExampleDescription,
        sourceUrl = TagsExampleSourceUrl,
    ) {
        TagSample(
            tag = { text, intent, leadingIcon ->
                TagFilled(
                    text = text,
                    intent = intent,
                    leadingIcon = leadingIcon,
                )
            },
        )
    },
    Example(
        id = "tinted",
        name = "Tinted Tag",
        description = TagsExampleDescription,
        sourceUrl = TagsExampleSourceUrl,
    ) {
        TagSample(
            tag = { text, intent, leadingIcon ->
                TagTinted(
                    text = text,
                    intent = intent,
                    leadingIcon = leadingIcon,
                )
            },
        )
    },
    Example(
        id = "outlined",
        name = "Outlined Tag",
        description = TagsExampleDescription,
        sourceUrl = TagsExampleSourceUrl,
    ) {
        TagSample(
            tag = { text, intent, leadingIcon ->
                TagOutlined(
                    text = text,
                    intent = intent,
                    leadingIcon = leadingIcon,
                )
            },
        )
    },
    Example(
        id = "highlight",
        name = "Tag \"Highlight\"",
        description = "The Highlight tag allows you to temporarily highlight new features of the product. It draws " +
            "users' attention to new features or important updates.",
        sourceUrl = TagsExampleSourceUrl,
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TagHighlight()
            TagHighlightBadge()
        }
    },
    Example(
        id = "layout",
        name = "Tag layouts",
        description = "Showcase how to layout tags sao that they don't clip on parent width but can go to a new " +
            "line if they don't fit",
        sourceUrl = TagsExampleSourceUrl,
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4,
        ) {
            TagFilled(text = "Tag 1", intent = TagIntent.Main)
            TagFilled(text = "Tag longer 2", intent = TagIntent.Accent)
            TagFilled(text = "Tag a bit longer 3", intent = TagIntent.Info)
            TagTinted(text = "Tag way more longer 4", intent = TagIntent.Main)
            TagTinted(text = "Tag small 5", intent = TagIntent.Main)
            TagOutlined(text = "Tag 6", intent = TagIntent.Main)
        }
    },
)

@Composable
private fun TagSample(
    tag: @Composable (
        text: String,
        intent: TagIntent,
        leadingIcon: SparkIcon?,
    ) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val icon = SparkIcons.Booster
        val tagText = "available"

        tag(
            /* text = */ tagText,
            /* intent = */TagIntent.Main,
            /* leadingIcon = */ null,
        )
        tag(
            /* text = */ tagText,
            /* intent = */TagIntent.Main,
            /* leadingIcon = */ icon,
        )
        tag(
            /* text = */ "",
            /* intent = */TagIntent.Main,
            /* leadingIcon = */ icon,
        )
    }
}
