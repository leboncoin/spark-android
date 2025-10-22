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
package com.adevinta.spark.catalog.examples.samples.dialog.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.dialog.ModalScaffold
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ImageFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.ThreeDotsVertical

@Composable
internal fun EdgeToEdgeExample() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var showDialog by rememberSaveable { mutableStateOf(false) }
        ButtonFilled(
            onClick = { showDialog = true },
            text = "Show Modal",
        )

        if (showDialog) {
            WindowInsets.displayCutout.only(
                WindowInsetsSides.Horizontal,
            )
            ModalScaffold(
                inEdgeToEdge = true,
                onClose = { showDialog = false },
                contentPadding = PaddingValues(0.dp),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SparkTheme.colors.error),
                        textAlign = TextAlign.Center,
                        text = "ModalScaffold",
                        style = SparkTheme.typography.display3,
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(sparkIcon = SparkIcons.ThreeDotsVertical, contentDescription = "")
                    }
                },
            ) { innerPadding ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(SparkTheme.colors.accentContainer),
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .background(SparkTheme.colors.accent),
                    ) {
                        Text(
                            text = "content",
                            textAlign = TextAlign.Center,
                            style = SparkTheme.typography.display3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2000.dp),
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
//                            item {
//                                Spacer(modifier = Modifier.windowInsetsStartWidth(displayCutout))
//                            }
                            repeat(25) {
                                item {
                                    Surface(
                                        modifier = Modifier
                                            .size(150.dp)
                                            .background(SparkTheme.colors.successContainer),
                                    ) {
                                    }
                                }
                            }
//                            item {
//                                Spacer(modifier = Modifier.windowInsetsEndWidth(WindowInsets.systemBars))
//                            }
                        }
                        VerticalSpacer(100.dp)
                    }
                }
            }
        }
    }
}
