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
package com.adevinta.spark.catalog.examples.samples.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.configurator.samples.bottomsheet.BottomSheetContentExamples
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SparkSampleSourceUrl
import com.adevinta.spark.components.bottomsheet.BottomSheet
import com.adevinta.spark.components.bottomsheet.DragHandle
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.image.Illustration
import com.adevinta.spark.components.image.Image
import com.adevinta.spark.components.list.ListItem
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.text.TextLinkButton
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.Store
import com.adevinta.spark.tokens.highlight
import kotlinx.coroutines.launch

private const val BottomSheetExampleSourceUrl = "$SparkSampleSourceUrl/bottomsheet/BottomSheetExamples.kt"

@OptIn(ExperimentalMaterial3Api::class)
public val BottomSheetExamples: List<Example> = listOf(
    Example(
        id = "list-content",
        name = "BottomSheet List Content",
        description = "BottomSheet List Content",
        sourceUrl = BottomSheetExampleSourceUrl,
    ) {
        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
        )
        VerticalSpacer(24.dp)
        ConfiguredBottomSheet(
            bottomSheetContentExample = BottomSheetContentExamples.List,
            isDragHandlerEnabled = true,
            bottomSheetState = bottomSheetState,
        )
    },
    Example(
        id = "list-content-no-handle",
        name = "No Handle List Content",
        description = "BottomSheet List Content, no drag handle",
        sourceUrl = BottomSheetExampleSourceUrl,
    ) {
        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
        )
        VerticalSpacer(24.dp)
        ConfiguredBottomSheet(
            bottomSheetContentExample = BottomSheetContentExamples.List,
            isDragHandlerEnabled = false,
            bottomSheetState = bottomSheetState,
        )
    },
    Example(
        id = "list-content-expanded",
        name = "Expanded List Content",
        description = "BottomSheet List Content fully expanded",
        sourceUrl = BottomSheetExampleSourceUrl,
    ) {
        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
        VerticalSpacer(24.dp)
        ConfiguredBottomSheet(
            bottomSheetContentExample = BottomSheetContentExamples.List,
            isDragHandlerEnabled = true,
            bottomSheetState = bottomSheetState,
        )
    },
    Example(
        id = "text-content",
        name = "BottomSheet Text Content",
        description = "BottomSheet Text Content",
        sourceUrl = BottomSheetExampleSourceUrl,
    ) {
        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
        )
        VerticalSpacer(24.dp)
        ConfiguredBottomSheet(
            bottomSheetContentExample = BottomSheetContentExamples.Text,
            isDragHandlerEnabled = true,
            bottomSheetState = bottomSheetState,
        )
    },
    Example(
        id = "image-content",
        name = "BottomSheet Image Content",
        description = "BottomSheet Image Content",
        sourceUrl = BottomSheetExampleSourceUrl,
    ) {
        val bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
        )
        VerticalSpacer(24.dp)
        ConfiguredBottomSheet(
            bottomSheetContentExample = BottomSheetContentExamples.Image,
            isDragHandlerEnabled = true,
            bottomSheetState = bottomSheetState,
        )
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfiguredBottomSheet(
    bottomSheetContentExample: BottomSheetContentExamples,
    isDragHandlerEnabled: Boolean,
    bottomSheetState: SheetState,
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val onDismissRequest: () -> Unit = { openBottomSheet = false }

    val onHideBottomSheetClicked: () -> Unit = {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                openBottomSheet = false
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ButtonFilled(
            size = ButtonSize.Large,
            text = "Show BottomSheet",
            onClick = { openBottomSheet = !openBottomSheet },
        )
    }
    if (openBottomSheet) {
        BottomSheet(
            content = {
                when (bottomSheetContentExample) {
                    BottomSheetContentExamples.Text -> TextContent()
                    BottomSheetContentExamples.Image -> ImageContent()
                    BottomSheetContentExamples.Illustration -> IllustrationContent()
                    BottomSheetContentExamples.List -> ListContent(onHideBottomSheetClicked)
                }
            },
//            applyTempStatusBarPadding = true,
//            dragHandle = if (isDragHandlerEnabled) {
//                { DragHandle() }
//            } else {
//                null
//            },
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListContent(onHideBottomSheetClicked: () -> Unit) {
    LazyColumn {
        stickyHeader {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ButtonFilled(
                    text = "Hide Bottom Sheet",
                    onClick = onHideBottomSheetClicked,
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

@Composable
private fun TextContent() {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Title",
            modifier = Modifier.padding(bottom = 16.dp),
            style = SparkTheme.typography.headline1.highlight,
        )
        Text(
            text = "Do you want to have this cookie now?",
            modifier = Modifier.padding(bottom = 16.dp),
            style = SparkTheme.typography.body2.highlight,
        )
        TextLinkButton(text = "Text Link", onClick = {})
    }
}

@Composable
private fun ImageContent() {
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(),
            model = "https://upload.wikimedia.org/wikipedia/commons/f/fd/Pink_flower.jpg",
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}

@Composable
private fun IllustrationContent() {
    Illustration(
        sparkIcon = SparkIcons.Store,
        contentDescription = null,
        modifier = Modifier.size(100.dp),
    )
}

public enum class BottomSheetContentExamples {
    Text,
    Image,
    Illustration,
    List,
}
