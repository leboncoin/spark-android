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
@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.adevinta.spark.catalog.examples

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.AppBasePath
import com.adevinta.spark.catalog.CatalogHomeScreen
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.examples.component.ComponentItem
import com.adevinta.spark.catalog.model.Category
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.themes.NavigationMode
import com.adevinta.spark.catalog.ui.animations.AnimatedNullableVisibility
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.catalog.ui.navigation.ChangeSelectedNavControllerOnPageChange
import com.adevinta.spark.catalog.ui.navigation.NavHostSpark
import com.adevinta.spark.catalog.util.TrackScrollJank
import com.adevinta.spark.components.chips.ChipOutlined
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.components.chips.ChipStyles
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tokens.Layout
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
public object ExamplesList

internal val ExamplesList.deepLinks: List<NavDeepLink>
    get() = listOf(
        navDeepLink<ExamplesList>(basePath = "${AppBasePath}examples"),
    )

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun ComponentsScreen(
    modifier: Modifier = Modifier,
    components: ImmutableList<Component>,
    pagerState: PagerState,
    contentPadding: PaddingValues,
    navigationMode: NavigationMode,
) {
    SharedTransitionLayout {
        val navController = rememberNavController()
        ChangeSelectedNavControllerOnPageChange(
            pagerState = pagerState,
            catalogScreen = CatalogHomeScreen.Examples,
            navController = navController,
        )
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHostSpark(
                modifier = modifier,
                navController = navController,
                startDestination = ExamplesList,
                navigationMode = navigationMode,
                builder = {
                    examplesDestination(
                        navController = navController,
                        contentPadding = contentPadding,
                        components = components,
                    )
                },
            )
        }
    }
}

private enum class GroupStrategy {
    None,
    Alphabetically,
    Category,
}

private data class Header(
    val label: String,
    val icon: SparkIcon? = null,
    val category: Category? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComponentsListScreen(
    components: ImmutableList<Component>,
    onExampleSectionClick: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    var selectedGroupStrategy by rememberSaveable { mutableStateOf(GroupStrategy.None) }

    val examplesComponents by remember(components) {
        mutableStateOf(components.filter { it.examples.isNotEmpty() })
    }
    val context = LocalContext.current
    val groupedComponents: Map<Header?, List<Component>> by remember {
        derivedStateOf {
            when (selectedGroupStrategy) {
                GroupStrategy.None ->
                    mapOf(null to examplesComponents.sortedBy { it.name })

                GroupStrategy.Alphabetically ->
                    examplesComponents
                        .groupBy { Header(it.name.first().toString(), null) }
                        .mapValues { (_, components) -> components.sortedBy { it.name } }
                        .toSortedMap(compareBy { it?.label })

                GroupStrategy.Category ->
                    examplesComponents
                        .groupBy { Header(context.getString(it.category.label), it.category.icon, it.category) }
                        .mapValues { (_, components) -> components.sortedBy { it.name } }
                        .toSortedMap(compareBy { it?.category?.displayOrder ?: Int.MAX_VALUE })
            }
        }
    }
    val columns = Layout.columns / 2
    val state = rememberLazyGridState()
    TrackScrollJank(scrollableState = state, stateName = "example:component:grid")
    Column {
        LazyRow(
            contentPadding = TopAppBarDefaults.windowInsets.asPaddingValues(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                ChipSelectable(
                    text = "Alphabetically",
                    style = ChipStyles.Outlined,
                    selected = selectedGroupStrategy == GroupStrategy.Alphabetically,
                    onClick = {
                        selectedGroupStrategy = if (selectedGroupStrategy == GroupStrategy.Alphabetically) {
                            GroupStrategy.None
                        } else {
                            GroupStrategy.Alphabetically
                        }
                    },
                )
            }
            item {
                ChipSelectable(
                    text = "Category",
                    style = ChipStyles.Outlined,
                    selected = selectedGroupStrategy == GroupStrategy.Category,
                    onClick = {
                        selectedGroupStrategy = if (selectedGroupStrategy == GroupStrategy.Category) {
                            GroupStrategy.None
                        } else {
                            GroupStrategy.Category
                        }
                    },
                )
            }
            item {
                ChipOutlined(
                    text = "Filtres",
                )
            }
        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(contentPadding),
            columns = GridCells.Fixed(columns),
            state = state,
            verticalArrangement = Arrangement.spacedBy(Layout.gutter),
            horizontalArrangement = Arrangement.spacedBy(Layout.gutter),
            contentPadding = PaddingValues(
                start = Layout.bodyMargin /
                        2 +
                        contentPadding.calculateLeftPadding(
                            LocalLayoutDirection.current,
                        ),
                end = Layout.bodyMargin /
                        2 +
                        contentPadding.calculateRightPadding(
                            LocalLayoutDirection.current,
                        ),
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
            ),
        ) {
            item(
                key = -2,
                contentType = ComponentsItemType.Header,
                span = { GridItemSpan(columns) },
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = Layout.bodyMargin / 2,
                        vertical = Layout.gutter,
                    ),
                    verticalArrangement = Arrangement.Absolute.spacedBy(4.dp),
                ) {
                    Text(
                        text = stringResource(R.string.examples_component_screen_title),
                        style = SparkTheme.typography.headline1,
                    )
                    Text(
                        text = stringResource(R.string.examples_component_screen_description),
                        style = SparkTheme.typography.body2,
                    )
                }
            }
            groupedComponents.forEach { (header, components) ->

                header?.let { // Only render header if it exists (not for None strategy)
                    stickyHeader(key = header.hashCode()) {
                        Surface(
                            Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .animateItem(),
                        ) {
                            // Render header with icon and label
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                AnimatedNullableVisibility(
                                    header.icon,
                                ) {
                                    Surface(
                                        color = SparkTheme.colors.mainContainer,
                                        modifier = Modifier.size(56.dp),
                                        shape = SparkTheme.shapes.full,
                                    ) {
                                        Icon(it, contentDescription = null)
                                    }
                                }
                                Text(
                                    header.label,
                                    style = SparkTheme.typography.display3,
                                )
                            }
                        }
                    }
                }
                items(
                    items = components,
                    key = { it.id },
                    span = { GridItemSpan(1) },
                    contentType = { ComponentsItemType.Component },
                    itemContent = { component ->
                        ComponentItem(
                            modifier = Modifier
                                .semantics { onClick("Aller aux exemples", null) }
                                .animateItem(),
                            component = component,
                            countIndicator = component.examples.size,
                            onClick = {
                                onExampleSectionClick(component.id)
                            },
                        )
                    },
                )
                item(span = { GridItemSpan(maxLineSpan) }) {
                    VerticalSpacer(16.dp)
                }
            }
        }
    }
}

private enum class ComponentsItemType {
    Header,
    Component,
}
