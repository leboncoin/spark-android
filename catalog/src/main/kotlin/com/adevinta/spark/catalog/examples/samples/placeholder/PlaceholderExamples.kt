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
package com.adevinta.spark.catalog.examples.samples.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.placeholder.illustrationPlaceholder
import com.adevinta.spark.components.placeholder.placeholder
import com.adevinta.spark.components.placeholder.textPlaceholder
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text

private const val PlaceholderExampleSourceUrl = "$SampleSourceUrl/PlaceholderSamples.kt"

public val PlaceholderExamples: List<Example> = listOf(
    Example(
        id = "text",
        name = "Text Placeholder",
        description = "Placeholder for text content with fade animation",
        sourceUrl = PlaceholderExampleSourceUrl,
    ) {
        PlaceholderText()
    },
    Example(
        id = "rectangle",
        name = "Rectangle Placeholder",
        description = "Generic rectangular placeholder with fade animation",
        sourceUrl = PlaceholderExampleSourceUrl,
    ) {
        PlaceholderRectangle()
    },
    Example(
        id = "illustration",
        name = "Illustration Placeholder",
        description = "Placeholder for images and illustrations with shimmer animation",
        sourceUrl = PlaceholderExampleSourceUrl,
    ) {
        PlaceholderIllustration()
    },
    Example(
        id = "card",
        name = "Card Placeholder",
        description = "Complete card layout with multiple placeholder elements",
        sourceUrl = PlaceholderExampleSourceUrl,
    ) {
        PlaceholderCard()
    },
    Example(
        id = "list",
        name = "List Placeholder",
        description = "List of items with placeholder content",
        sourceUrl = PlaceholderExampleSourceUrl,
    ) {
        PlaceholderList()
    },
    Example(
        id = "loading-states",
        name = "Loading States",
        description = "Demonstrates toggling between loading and loaded states",
        sourceUrl = PlaceholderExampleSourceUrl,
    ) {
        PlaceholderLoadingStates()
    },
)

@Preview
@Composable
private fun ColumnScope.PlaceholderText() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Text placeholders with different lengths")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Short text",
                modifier = Modifier.textPlaceholder(true),
            )
            Text(
                "This is a longer text placeholder that demonstrates how the placeholder adapts to content length",
                modifier = Modifier.textPlaceholder(true),
            )
            Text(
                "Medium length text content",
                modifier = Modifier.textPlaceholder(true),
            )
        }

        Text("Mixed content with some real text")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Real title text")
            Text(
                "Loading description...",
                modifier = Modifier.textPlaceholder(true),
            )
            Text("Real footer text")
        }
    }
}

@Preview
@Composable
private fun ColumnScope.PlaceholderRectangle() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Rectangular placeholders for various UI elements")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Button placeholder
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .placeholder(true),
            )

            // Card placeholder
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .placeholder(true),
            )

            // Thin line placeholder (divider)
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .placeholder(true),
            )
        }

        Text("Different aspect ratios")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Square placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .placeholder(true),
            )

            // Wide rectangle
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .placeholder(true),
            )

            // Tall rectangle
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(120.dp)
                    .placeholder(true),
            )
        }
    }
}

@Preview
@Composable
private fun ColumnScope.PlaceholderIllustration() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Illustration placeholders with shimmer animation")

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Circular avatar placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = CircleShape,
                    ),
            )

            // Square image placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = SparkTheme.shapes.medium,
                    ),
            )

            // Rounded rectangle image placeholder
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 64.dp)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = SparkTheme.shapes.large,
                    ),
            )
        }

        Text("Different illustration shapes")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Small rounded
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = SparkTheme.shapes.small,
                    ),
            )

            // Medium rounded
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = SparkTheme.shapes.medium,
                    ),
            )

            // Large rounded
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = SparkTheme.shapes.large,
                    ),
            )

            // Extra large rounded
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .illustrationPlaceholder(
                        visible = true,
                        shape = SparkTheme.shapes.extraLarge,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun ColumnScope.PlaceholderCard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Complete card with placeholder content")

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = SparkTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Header with avatar and text
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .illustrationPlaceholder(
                                visible = true,
                                shape = CircleShape,
                            ),
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Loading name...",
                            modifier = Modifier
                                .width(120.dp)
                                .textPlaceholder(true),
                        )
                        Text(
                            "Loading subtitle...",
                            modifier = Modifier
                                .width(80.dp)
                                .textPlaceholder(true),
                        )
                    }
                }

                // Image placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .illustrationPlaceholder(
                            visible = true,
                            shape = SparkTheme.shapes.medium,
                        ),
                )

                // Content text
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Loading title text that is longer...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .textPlaceholder(true),
                    )
                    Text(
                        "Loading description content...",
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .textPlaceholder(true),
                    )
                    Text(
                        "Loading shorter text...",
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .textPlaceholder(true),
                    )
                }

                // Action buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .placeholder(true),
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .placeholder(true),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ColumnScope.PlaceholderList() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("List of items with placeholder content")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(5) { index ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SparkTheme.shapes.medium,
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .illustrationPlaceholder(
                                    visible = true,
                                    shape = SparkTheme.shapes.small,
                                ),
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                "Loading item ${index + 1} title...",
                                modifier = Modifier
                                    .fillMaxWidth(if (index % 2 == 0) 0.7f else 0.9f)
                                    .textPlaceholder(true),
                            )
                            Text(
                                "Loading description...",
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .textPlaceholder(true),
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .placeholder(true),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ColumnScope.PlaceholderLoadingStates() {
    var isLoading by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ButtonFilled(
            text = if (isLoading) "Stop Loading" else "Start Loading",
            onClick = { isLoading = !isLoading },
        )

        Text("Content that toggles between loading and loaded states")

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = SparkTheme.shapes.large,
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .illustrationPlaceholder(
                            visible = isLoading,
                            shape = CircleShape,
                        ),
                ) {
                    // Real content would go here
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SparkTheme.colors.main),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = if (isLoading) "Loading name..." else "John Doe",
                        modifier = Modifier.textPlaceholder(isLoading),
                    )
                    Text(
                        text = if (isLoading) "Loading status..." else "Online",
                        modifier = Modifier.textPlaceholder(isLoading),
                    )
                }

                Box(
                    modifier = Modifier.placeholder(isLoading),
                ) {
                    // Real icon would go here
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(SparkTheme.colors.success),
                    )
                }
            }
        }
    }
}
