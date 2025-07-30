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
package com.adevinta.spark.catalog.configurator

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.CatalogHomeScreen
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.examples.component.ComponentConfiguratorItem
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.themes.NavigationMode
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.catalog.ui.navigation.ChangeSelectedNavControllerOnPageChange
import com.adevinta.spark.catalog.ui.navigation.NavHostSpark
import com.adevinta.spark.catalog.util.TrackScrollJank
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Layout

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun ConfiguratorComponentsScreen(
    modifier: Modifier = Modifier,
    components: List<Component>,
    pagerState: PagerState,
    contentPadding: PaddingValues,
    navigationMode: NavigationMode,
) {
    SharedTransitionLayout {
        val navController = rememberNavController()

        ChangeSelectedNavControllerOnPageChange(
            pagerState = pagerState,
            catalogScreen = CatalogHomeScreen.Configurator,
            navController = navController,
        )
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHostSpark(
                modifier = modifier,
                navController = navController,
                startDestination = ConfiguratorsList,
                navigationMode = navigationMode,
                builder = {
                    configuratorsDestination(
                        navController = navController,
                        contentPadding = contentPadding,
                        components = components,
                    )
                },
            )
        }
    }
}

@Composable
internal fun ComponentsListScreen(
    components: List<Component>,
    contentPadding: PaddingValues,
    onConfiguratorSelected: (componentId: String, configuratorId: String) -> Unit,
) {
    val configuratorsComponents by remember(components) {
        mutableStateOf(components.filter { it.configurators.firstOrNull() != null })
    }
    val columns = Layout.columns / 2
    val state = rememberLazyGridState()
    TrackScrollJank(scrollableState = state, stateName = "configurator:component:grid")

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(contentPadding),
        columns = GridCells.Fixed(columns),
        state = state,
        contentPadding = PaddingValues(
            start = Layout.bodyMargin / 2 + contentPadding.calculateLeftPadding(
                LocalLayoutDirection.current,
            ),
            end = Layout.bodyMargin / 2 + contentPadding.calculateRightPadding(
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
                    text = stringResource(R.string.configurator_component_screen_title),
                    style = SparkTheme.typography.headline1,
                )
                Text(
                    text = stringResource(R.string.configurator_component_screen_description),
                    style = SparkTheme.typography.body2,
                )
            }
        }
        items(
            items = configuratorsComponents,
            key = { it.id },
            span = { GridItemSpan(1) },
            contentType = { ComponentsItemType.Component },
            itemContent = { component ->
                val configuratorCount = component.configurators.size
                ComponentConfiguratorItem(
                    component = component,
                    countIndicator = configuratorCount,
                    onClick = { selectedComponent, selectedConfigurator ->
                        val componentId = selectedComponent.id
                        onConfiguratorSelected(componentId, selectedConfigurator)
                    },
                )
            },
        )
    }
}

private enum class ComponentsItemType {
    Header,
    Component,
}
