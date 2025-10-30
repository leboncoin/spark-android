/*
 * Copyright (c) 2025 Adevinta
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tools.modifiers.invisibleSemantic

/**
 * The Highlight tag is integrated into a card. It highlights new content and is placed at the top of the card
 * so that users see it first.
 * This component usages should remain exceptional and it is important to prioritize the elements highlighted in
 * order to avoid visual overload.
 *
 * Ensure that this component is being used with a End of life Remote Config so that it doesn't remain in
 * production indefinitely.
 *
 * @param modifier Optional modifier for the badge container
 *
 * @see TagHighlightBadge
 */
@ExperimentalSparkApi
@Composable
public fun TagHighlight(
    modifier: Modifier = Modifier,
) {
    BaseSparkTag(
        colors = TagDefaults.filledColors(TagIntent.Main),
        modifier = modifier,
        shape = SparkTheme.shapes.small.highlight,
        size = TagSize.Large,
    ) {
        Text(
            text = stringResource(R.string.spark_tag_highlight_new_label),
            overflow = TextOverflow.Clip,
        )
    }
}

/**
 * The Highlight tag “Badge” is positioned as close as possible to the element to be highlighted, whether in a menu
 * for a new entry or in an Adcard.
 * This component usages should remain exceptional and it is important to prioritize the elements highlighted in
 * order to avoid visual overload.
 *
 *  Ensure that this component is being used with a End of life Remote Config so that it doesn't remain in
 *  production indefinitely.
 *
 * @param modifier Optional modifier for the badge container
 *
 * @see TagHighlight
 */
@ExperimentalSparkApi
@Composable
public fun TagHighlightBadge(
    modifier: Modifier = Modifier,
) {
    BaseSparkTag(
        colors = TagColors(backgroundColor = Color.Transparent, contentColor = SparkTheme.colors.onSurface),
        modifier = modifier,
        shape = SparkTheme.shapes.large,
        leadingContent = {
            Spacer(
                Modifier
                    .size(8.dp)
                    .background(SparkTheme.colors.main, SparkTheme.shapes.full)
                    .invisibleSemantic(),
            )
        },
    ) {
        Text(
            text = stringResource(R.string.spark_tag_highlight_new_label),
            overflow = TextOverflow.Clip,
        )
    }
}

@Preview
@Preview(locale = "fr")
@Composable
private fun PreviewTagHighlight() {
    PreviewTheme {
        TagHighlight()
        TagHighlightBadge()
    }
}
