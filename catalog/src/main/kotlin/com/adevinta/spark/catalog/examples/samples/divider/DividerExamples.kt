/*
 * Copyright (c) 2023-2025 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.divider

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.divider.DividerIntent
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.divider.LabelHorizontalAlignment
import com.adevinta.spark.components.divider.LabelVerticalAlignment
import com.adevinta.spark.components.divider.VerticalDivider
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.highlight

private const val DividerExampleDescription = "Divider examples"
private const val DividerExampleSourceUrl = "$SampleSourceUrl/DividerSamples.kt"
private const val TextExample =
    "Lorem ipsum dolor sit amet consectetur adipiscing elit Ut et massa mi." +
        " Aliquam in hendrerit urna. Pellentesque sit amet sapien fringill."
private const val LongTextExample =
    "Lorem ipsum dolor sit amet consectetur adipiscing elit Ut et massa mi." +
        " Aliquam in hendrerit urna. Pellentesque sit amet sapien fringill." +
        " Lorem ipsum dolor sit amet consectetur adipiscing elit Ut et massa mi." +
        " Aliquam in hendrerit urna. Pellentesque sit amet sapien fringill" +
        " Lorem ipsum dolor sit amet consectetur adipiscing elit Ut et massa mi." +
        " Aliquam in hendrerit urna. Pellentesque sit amet sapien fringill"

public val DividerExamples: List<Example> = listOf(
    Example(
        id = "horizontal-no-label",
        name = "HorizontalDivider With Label",
        description = DividerExampleDescription,
        sourceUrl = DividerExampleSourceUrl,
    ) {
        DividerIntent.entries.forEach { intent ->

            LabelHorizontalAlignment.entries.forEach { alignment ->
                Text(
                    style = SparkTheme.typography.headline2,
                    text = "Title",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                VerticalSpacer(8.dp)
                Text(
                    style = SparkTheme.typography.body1,
                    text = TextExample,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                VerticalSpacer(16.dp)

                HorizontalDivider(
                    intent = intent,
                    label = {
                        Text(
                            textAlign = TextAlign.Center,
                            style = SparkTheme.typography.body1,
                            text = intent.name,
                        )
                    },
                    labelHorizontalAlignment = alignment,
                )
                VerticalSpacer(16.dp)
            }
        }
    },
    Example(
        id = "horizontal",
        name = "HorizontalDivider No Label",
        description = DividerExampleDescription,
        sourceUrl = DividerExampleSourceUrl,
    ) {
        DividerIntent.entries.forEach { intent ->
            Text(
                style = SparkTheme.typography.headline2,
                text = "Divider variant ${intent.name}",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            VerticalSpacer(8.dp)

            Text(
                style = SparkTheme.typography.body1,
                text = TextExample,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            VerticalSpacer(16.dp)
            HorizontalDivider(
                intent = intent,
            )
            VerticalSpacer(16.dp)
        }
    },
    Example(
        id = "vertical",
        name = "VerticalDivider with Label",
        description = DividerExampleDescription,
        sourceUrl = DividerExampleSourceUrl,
    ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier.horizontalScroll(scrollState),
        ) {
            DividerIntent.entries.forEach { intent ->
                LabelVerticalAlignment.entries.forEach { alignment ->

                    Text(
                        style = SparkTheme.typography.body2.highlight,
                        textAlign = TextAlign.Justify,
                        text = LongTextExample,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .width(100.dp),
                    )
                    HorizontalSpacer(8.dp)

                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        intent = intent,
                        label = {
                            Text(
                                textAlign = TextAlign.Center,
                                style = SparkTheme.typography.body1,
                                text = intent.name,
                            )
                        },
                        labelVerticalAlignment = alignment,
                    )
                    HorizontalSpacer(8.dp)
                }
            }
        }
    },
    Example(
        id = "vertical-no-label",
        name = "VerticalDivider No Label",
        description = DividerExampleDescription,
        sourceUrl = DividerExampleSourceUrl,
    ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier.horizontalScroll(scrollState),
        ) {
            DividerIntent.entries.forEach { intent ->

                Text(
                    style = SparkTheme.typography.body2.highlight,
                    textAlign = TextAlign.Justify,
                    text = LongTextExample,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .width(100.dp),
                )
                HorizontalSpacer(8.dp)

                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    intent = intent,
                )
                HorizontalSpacer(8.dp)
            }
            Text(
                style = SparkTheme.typography.body2.highlight,
                textAlign = TextAlign.Justify,
                text = LongTextExample,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(100.dp),
            )
            HorizontalSpacer(8.dp)
        }
    },
)
