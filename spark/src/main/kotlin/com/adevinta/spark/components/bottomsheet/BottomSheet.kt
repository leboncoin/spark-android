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
package com.adevinta.spark.components.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetDefaults.ExpandedShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.bottomsheet.SheetDefaults.ContentTopPadding
import com.adevinta.spark.components.bottomsheet.SheetDefaults.ContentTopPaddingNoHandle
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.list.ListItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.CheckboxLabelled
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.contentColorFor
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlinx.coroutines.launch

/**
 * Modal bottom sheets are used as an alternative to inline menus or simple dialogs on mobile,
 * especially when offering a long list of action items, or when items require longer descriptions
 * and icons. Like dialogs, modal bottom sheets appear in front of app content, disabling all other
 * app functionality when they appear, and remaining on screen until confirmed, dismissed, or a
 * required action has been taken.
 *
 * @param onDismissRequest
 * @param modifier Optional [Modifier] for the bottom sheet.
 * @param showHandle Optional [Boolean] to show / hide handle, if handle is hidden it will fill all screen.
 *
 *
 * @param contentTopPadding The top padding for the content of the bottom sheet, does not apply to the handle.
 *
 * By default if showHandle is [Boolean.true]. contentTopPadding = [SheetDefaults.ContentTopPadding]
 * else contentTopPadding = [SheetDefaults.ContentTopPaddingNoHandle]
 *
 * If you want to have immersive BottomSheet, you can set contentTopPadding = 0.dp,
 * Beware you need to set your content top padding yourself
 * to avoid content to be hidden by the handle at least [SheetDefaults.ContentTopPadding]
 *
 *
 * @param sheetState the state of the bottom sheet.
 * @param content The content to be displayed inside the bottom sheet.
 */
@Composable
@ExperimentalMaterial3Api
public fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    dragHandle: @Composable (() -> Unit)? = {
        DragHandle()
    },
    applyTempStatusBarPadding: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    SparkModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier.sparkUsageOverlay(),
        sheetState = sheetState,
        content = content,
        applyTempStatusBarPadding = applyTempStatusBarPadding,
        dragHandle = dragHandle,
    )
}

/**
 * Modal bottom sheets are used as an alternative to inline menus or simple dialogs on mobile,
 * especially when offering a long list of action items, or when items require longer descriptions
 * and icons. Like dialogs, modal bottom sheets appear in front of app content, disabling all other
 * app functionality when they appear, and remaining on screen until confirmed, dismissed, or a
 * required action has been taken.
 **
 * A simple example of a modal bottom sheet looks like this:
 * @param contentTopPadding The top padding for the content of the bottom sheet, does not apply to the handle.
 * @param onDismissRequest Executes when the user clicks outside of the bottom sheet, after sheet
 * animates to [androidx.compose.material3.SheetValue.Hidden].
 * @param modifier Optional [Modifier] for the bottom sheet.
 * @param showHandle Optional [Boolean] to show / hide handle, if handle is hidden it will fill all screen.
 * @param sheetState The state of the bottom sheet.
 * @param shape The shape of the bottom sheet.
 * @param containerColor The color used for the background of this bottom sheet
 *
 * @param contentColor The preferred color for content inside this bottom sheet. Defaults to either
 * the matching content color for [containerColor],
 * or to the current [androidx.compose.material3.LocalContentColor] if [containerColor] is not a color from the theme.
 *
 * @param tonalElevation The tonal elevation of this bottom sheet.
 * @param scrimColor Color of the scrim that obscures content when the bottom sheet is open.
 * @param dragHandle Optional visual marker to swipe the bottom sheet.
 * @param content The content to be displayed inside the bottom sheet.
 */

@Composable
@ExperimentalMaterial3Api
internal fun SparkModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = ExpandedShape,
    containerColor: Color = SparkTheme.colors.surface,
    contentColor: Color = contentColorFor(containerColor),
    dragHandle: (@Composable () -> Unit)? = { DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    applyTempStatusBarPadding: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
//        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
//        contentWindowInsets = contentWindowInsets,
        properties = properties,
//        dragHandle = null,
        content = content,
    ) /*{
        Box {
            val systemBarTopInsets = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
            val additionalTopPadding = if (applyTempStatusBarPadding) systemBarTopInsets else 0.dp
            Column(
                modifier = Modifier.padding(
                    top = if (dragHandle != null) {
                        ContentTopPadding + additionalTopPadding
                    } else {
                        ContentTopPaddingNoHandle + additionalTopPadding
                    },
                ),
            ) {
                content()
            }
            if (dragHandle != null) {
                Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth()) {
                    dragHandle()
                }
            }
        }
    }*/
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(
    group = "BottomSheet",
    name = "ModalBottomSheet",
)
@Composable
private fun ModalBottomSheetSample() {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CheckboxLabelled(
            state = if (skipPartiallyExpanded) ToggleableState.On else ToggleableState.Off,
            onClick = { skipPartiallyExpanded = !skipPartiallyExpanded },
        ) {
            Text("Skip Partially Expanded State")
        }

        ButtonFilled(
            text = "Show Bottom Sheet",
            onClick = { openBottomSheet = !openBottomSheet },
        )
    }

    if (openBottomSheet) {
        BottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            LazyColumn {
                stickyHeader {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Green),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        // Note: If you provide logic outside of onDismissRequest to remove the sheet,
                        // you must additionally handle intended state cleanup, if any.
                        ButtonFilled(
                            modifier = Modifier.padding(24.dp),
                            text = "Hide Bottom Sheet",
                            onClick = {
                                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                    if (!bottomSheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                }
                            },
                        )
                    }
                }

                items(50) {
                    ListItem(
                        headlineContent = { Text("Item $it") },
                        leadingContent = {
                            Icon(
                                SparkIcons.LikeFill,
                                contentDescription = "Localized description",
                            )
                        },
                    )
                }
            }
        }
    }
}
