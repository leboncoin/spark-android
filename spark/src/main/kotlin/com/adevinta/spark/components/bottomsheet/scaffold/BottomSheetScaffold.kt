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
package com.adevinta.spark.components.bottomsheet.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.bottomsheet.DragHandle
import com.adevinta.spark.components.bottomsheet.SheetDefaults.ContentTopPadding
import com.adevinta.spark.components.bottomsheet.SheetDefaults.ContentTopPaddingNoHandle
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.list.ListItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

/**
 *  BottomSheetScaffold is a composable that implements a scaffold with a bottom sheet.
 *  It is a wrapper around [androidx.compose.material3.BottomSheetScaffold].
 *  It provides a way to display a bottom sheet that can be swiped up and down.
 *  The scaffold can have a top bar, a screen content, a bottom sheet content, and a drag handle.
 *  The scaffold can also have a snackbar host.
 *  @param sheetContent The content of the bottom sheet.
 *  @param modifier The modifier to apply to this layout.
 *  @param scaffoldState The state of the scaffold.
 *  @param showHandle Whether to show the drag handle.
 *  @param sheetContentTopPadding The top padding of the sheet content.
 *  @param screenContentPadding The padding of the screen content.
 *  @param sheetSwipeEnabled Whether the sheet can be swiped.
 *  @param topBar The top bar composable.
 *  @param snackbarHost The snackbar host composable.
 *  @param content The content of the screen.
 *
 *  @sample com.adevinta.spark.components.bottomsheet.scaffold.BottomSheetPreview
 */
@Composable
@ExperimentalMaterial3Api
public fun BottomSheetScaffold(
    sheetContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetPeekHeight: Dp = BottomSheetDefaults.SheetPeekHeight,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetSwipeEnabled: Boolean = true,
    sheetDragHandle: @Composable (() -> Unit)? = { DragHandle() },
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (androidx.compose.material3.SnackbarHostState) -> Unit = {
        androidx.compose.material3.SnackbarHost(it)
    },
    content: @Composable (PaddingValues) -> Unit,
) {
    androidx.compose.material3.BottomSheetScaffold(
        sheetPeekHeight = sheetPeekHeight,
        sheetContent = {
            Box {
                Box(
                    modifier = Modifier.padding(
                        top = if (sheetDragHandle != null) ContentTopPadding else ContentTopPaddingNoHandle,
                    ),
                ) {
                    sheetContent()
                }
                if (sheetDragHandle != null) {
                    Box(contentAlignment = TopCenter, modifier = Modifier.fillMaxWidth()) {
                        DragHandle()
                    }
                }
            }
        },
        modifier = modifier.sparkUsageOverlay(),
        scaffoldState = scaffoldState,
        sheetDragHandle = null,
        sheetSwipeEnabled = sheetSwipeEnabled,
        topBar = topBar,
        snackbarHost = snackbarHost,
    ) {
        content(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(
    group = "BottomSheet",
    name = "BottomSheetScaffold",
)
@Composable
internal fun BottomSheetPreview() {
    PreviewTheme(padding = PaddingValues()) {
        BottomSheetScaffold(
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(700.dp),
                ) {
                    Text(
                        modifier = Modifier.align(TopCenter),
                        text = "Sheet Content",
                    )
                }
            },
            modifier = Modifier.fillMaxSize(),
            scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberStandardBottomSheetState(),
            ),
        ) {
            LazyColumn {
                stickyHeader {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Green),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Screen Content",
                        )
                    }
                }
                items(50) {
                    ListItem(
                        headlineContent = { Text("Item $it") },
                        leadingContent = {
                            Icon(
                                SparkIcons.HeartFill,
                                contentDescription = "Localized description",
                            )
                        },
                    )
                }
            }
        }
    }
}
