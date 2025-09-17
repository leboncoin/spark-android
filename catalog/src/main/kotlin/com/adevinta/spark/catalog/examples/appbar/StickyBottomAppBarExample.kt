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
package com.adevinta.spark.catalog.examples.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.appbar.BottomAppBar
import com.adevinta.spark.components.appbar.BottomAppBarSparkDefaults
import com.adevinta.spark.components.appbar.TopAppBar
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ArrowHorizontalDown
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.SparkIcons

/**
 * Example demonstrating BottomAppBar with sticky behavior and elevation
 * that responds to scrollable content, similar to TopAppBar behavior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Sticky BottomAppBar Example",
    showBackground = true,
)
@Composable
private fun StickyBottomAppBarExample() {
    PreviewTheme {
        // Create scroll behaviors for both top and bottom app bars
        val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                // Connect both scroll behaviors to the nested scroll system
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text("Sticky App Bars Demo") },
                    scrollBehavior = topAppBarScrollBehavior,
                    actions = {
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(
                                sparkIcon = SparkIcons.Check,
                                contentDescription = "Action",
                            )
                        }
                    },
                )
            },
            bottomBar = {
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                    actions = {
                        ButtonOutlined(
                            onClick = { /* Handle support action */ },
                            text = "Cancel",
                            modifier = Modifier.weight(1f),
                        )
                        ButtonFilled(
                            onClick = { /* Handle main action */ },
                            text = "Save",
                            modifier = Modifier.weight(1f),
                        )
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Sticky Bottom App Bar Demo",
                            style = SparkTheme.typography.headline1,
                        )
                        Text(
                            text = "Scroll to see the elevation effect on both app bars. " +
                                "The top app bar elevates when content scrolls under it. " +
                                "The bottom app bar elevates when content has been scrolled up from " +
                                "a scrolled-down position (using BottomAppBarState.contentOffset > 0), " +
                                "indicating there's content above to scroll back to.",
                            style = SparkTheme.typography.body2,
                        )
                        Text(
                            text = "Key Features:",
                            style = SparkTheme.typography.subhead,
                        )
                        Text(
                            text = "• Bottom app bar stays pinned at the bottom",
                            style = SparkTheme.typography.body2,
                        )
                        Text(
                            text = "• Elevates based on BottomAppBarState.contentOffset > 0",
                            style = SparkTheme.typography.body2,
                        )
                        Text(
                            text = "• Provides overlappedFraction property for advanced transitions",
                            style = SparkTheme.typography.body2,
                        )
                        Text(
                            text = "• Uses official Material3 BottomAppBarScrollBehavior API",
                            style = SparkTheme.typography.body2,
                        )
                        Text(
                            text = "• Smooth elevation animations",
                            style = SparkTheme.typography.body2,
                        )
                    }
                }
                items(50) { index ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        color = SparkTheme.colors.backgroundVariant,
                        shape = SparkTheme.shapes.medium,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Item ${index + 1}")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Example showing BottomAppBar with icon actions instead of buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "BottomAppBar with Icons",
    showBackground = true,
)
@Composable
private fun BottomAppBarWithIconsExample() {
    PreviewTheme {
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            bottomBar = {
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                    actions = {
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(
                                sparkIcon = SparkIcons.Check,
                                contentDescription = "Action 1",
                            )
                        }
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(
                                sparkIcon = SparkIcons.PenOutline,
                                contentDescription = "Action 2",
                            )
                        }
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(
                                sparkIcon = SparkIcons.ArrowHorizontalDown,
                                contentDescription = "Action 3",
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "BottomAppBar with Icon Actions",
                            style = SparkTheme.typography.headline1,
                        )
                        Text(
                            text = "This example shows a bottom app bar with icon actions. " +
                                "Scroll through the content to see the elevation effect when " +
                                "state.contentOffset > 0 (content has been scrolled up from scrolled-down position).",
                            style = SparkTheme.typography.body2,
                        )
                    }
                }
                items(30) { index ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        color = SparkTheme.colors.backgroundVariant,
                        shape = SparkTheme.shapes.medium,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text("Card ${index + 1}")
                                Text(
                                    text = "Swipe up to see elevation effect",
                                    style = SparkTheme.typography.caption,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Example demonstrating BottomAppBar behavior with minimal content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "BottomAppBar Minimal Content",
    showBackground = true,
)
@Composable
private fun BottomAppBarMinimalContentExample() {
    PreviewTheme {
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            bottomBar = {
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                    actions = {
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(
                                sparkIcon = SparkIcons.Check,
                                contentDescription = "Confirm",
                            )
                        }
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(
                                sparkIcon = SparkIcons.PenOutline,
                                contentDescription = "Edit",
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Minimal Content Example",
                            style = SparkTheme.typography.headline1,
                        )
                        Text(
                            text = "This example has minimal content. Notice that the bottom app bar " +
                                "doesn't elevate because state.contentOffset remains <= 0 " +
                                "(no scrolling up from scrolled-down position has occurred).",
                            style = SparkTheme.typography.body2,
                        )
                    }
                }
                items(5) { index ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        color = SparkTheme.colors.backgroundVariant,
                        shape = SparkTheme.shapes.medium,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Item ${index + 1}")
                        }
                    }
                }
            }
        }
    }
}
