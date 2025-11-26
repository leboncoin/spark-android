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
package com.adevinta.spark.catalog.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.util.TrackScrollJank
import com.adevinta.spark.catalog.util.splitCamelWithSpaces
import com.adevinta.spark.components.buttons.ButtonGhost
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.components.chips.ChipStyles
import com.adevinta.spark.components.dialog.ModalScaffold
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.DeleteFill
import com.adevinta.spark.icons.Search
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons

@ExperimentalSparkApi
@Composable
public fun IconPickerDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onIconSelected: (SparkIcon?) -> Unit,
    modifier: Modifier = Modifier,
    currentIcon: SparkIcon? = null,
    allowNullSelection: Boolean = true,
) {
    if (!showDialog) return

    val context = LocalContext.current
    var icons: List<NamedAsset> by remember {
        mutableStateOf(emptyList())
    }
    LaunchedEffect(Unit) {
        icons = getAllIconsRes(context)
    }
    var query: String by rememberSaveable { mutableStateOf("") }
    var showIcons by rememberSaveable { mutableStateOf(true) }
    var showAnimatedIcons by rememberSaveable { mutableStateOf(true) }

    val filteredIcons by remember(query, showIcons, showAnimatedIcons) {
        derivedStateOf {
            if (query.isEmpty()) {
                icons
            } else {
                icons.filter { it.name.contains(query, ignoreCase = true) }
            }.filterNot {
                !showIcons && it is NamedAsset.Icon
            }.filterNot {
                !showAnimatedIcons && it is NamedAsset.AnimatedIcon
            }
        }
    }

    ModalScaffold(
        modifier = modifier,
        title = { Text(stringResource(R.string.icon_picker_title)) },
        onClose = onDismissRequest,
        mainButton = null, // No main button since selection happens on icon click
        supportButton = if (allowNullSelection) {
            { modifier ->
                ButtonGhost(
                    text = stringResource(R.string.icon_picker_clear),
                    onClick = {
                        onIconSelected(null)
                        onDismissRequest()
                    },
                    modifier = modifier,
                )
            }
        } else {
            null
        },
    ) { paddingValues ->

        val state = rememberLazyGridState()
        TrackScrollJank(scrollableState = state, stateName = "icon-picker:grid")
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            columns = GridCells.Adaptive(minSize = 60.dp),
            verticalArrangement = spacedBy(16.dp),
            horizontalArrangement = spacedBy(16.dp),
        ) {
            stickyHeader {
                Column(
                    verticalArrangement = spacedBy(8.dp),

                ) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = stringResource(id = R.string.icons_screen_search_helper),
                        leadingContent = {
                            Icon(sparkIcon = SparkIcons.Search, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(
                                modifier = Modifier.clickable { query = "" },
                                sparkIcon = SparkIcons.DeleteFill,
                                contentDescription = "Clear",
                            )
                        },
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = spacedBy(8.dp),
                    ) {
                        ChipSelectable(
                            selected = showIcons,
                            text = stringResource(R.string.icons_filter_icon),
                            onClick = { showIcons = !showIcons },
                            style = ChipStyles.Tinted,
                            leadingIcon = if (showIcons) SparkIcons.Check else null,
                        )
                        ChipSelectable(
                            selected = showAnimatedIcons,
                            text = stringResource(R.string.icons_filter_icon_animated),
                            onClick = { showAnimatedIcons = !showAnimatedIcons },
                            style = ChipStyles.Tinted,
                            leadingIcon = if (showAnimatedIcons) SparkIcons.Check else null,
                        )
                    }
                }
            }
            items(
                items = filteredIcons,
                key = { it.name },
                contentType = { it.sparkIcon is SparkIcon.AnimatedPainter },
            ) { asset ->
                val sparkIcon = asset.sparkIcon
                val iconName = asset.name
                val isSelected = sparkIcon == currentIcon

                Column(
                    modifier = Modifier
                        .clip(SparkTheme.shapes.small)
                        .clickable {
                            onIconSelected(sparkIcon)
                            onDismissRequest()
                        }
                        .background(
                            if (isSelected) SparkTheme.colors.mainContainer else Color.Transparent,
                            SparkTheme.shapes.small,
                        )
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = spacedBy(4.dp),
                ) {
                    Icon(
                        sparkIcon = sparkIcon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        atEnd = true,
                    )
                    Text(
                        text = iconName.splitCamelWithSpaces(),
                        style = SparkTheme.typography.caption,
                        textAlign = TextAlign.Center,
                    )
                    if (isSelected) {
                        Icon(
                            sparkIcon = SparkIcons.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = SparkTheme.colors.main,
                        )
                    }
                }
            }
        }
    }
}
