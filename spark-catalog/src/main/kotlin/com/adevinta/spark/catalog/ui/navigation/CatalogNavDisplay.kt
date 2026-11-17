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
package com.adevinta.spark.catalog.ui.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.rememberNavigationEventDispatcherOwner
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.CatalogHomeScreen
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.SparkCatalogActivity
import com.adevinta.spark.catalog.themes.NavigationMode
import com.adevinta.spark.catalog.util.TrackDisposableJank
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Layout

/**
 * Remembers the back stack of one catalog tab. The back stack starts at [root], or at the deep link of the
 * activity intent when that deep link belongs to this tab. It survives configuration changes and process death.
 */
@Composable
internal fun rememberCatalogBackStack(root: NavKey): NavBackStack<NavKey> {
    val deepLink = LocalActivity.current?.intent?.data
    val initialKeys = remember(root, deepLink) {
        CatalogDeepLinks.backStackFor(deepLink)?.takeIf { it.firstOrNull() == root } ?: listOf(root)
    }
    return rememberNavBackStack(CatalogNavSavedStateConfiguration, *initialKeys.toTypedArray())
}

/**
 * The [NavDisplay] of one catalog tab.
 *
 * Each tab of the home pager owns a back stack. The pager keeps the next page composed, so only the visible page
 * handles back events and provides the assist content of the activity.
 *
 * On wide windows, [ListDetailSceneStrategy][androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy]
 * shows the list, detail, and extra entries side by side. On narrow windows, each entry fills the tab.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun CatalogNavDisplay(
    catalogScreen: CatalogHomeScreen,
    pagerState: PagerState,
    backStack: NavBackStack<NavKey>,
    navigationMode: NavigationMode,
    modifier: Modifier = Modifier,
    entries: EntryProviderScope<NavKey>.() -> Unit,
) {
    val isVisiblePage by remember(pagerState, catalogScreen) {
        derivedStateOf { pagerState.currentPage == catalogScreen.ordinal }
    }
    val activity = LocalActivity.current as? SparkCatalogActivity
    LaunchedEffect(isVisiblePage, activity, backStack) {
        if (isVisiblePage) activity?.activeBackStack = backStack
    }
    NavigationTrackingSideEffect(backStack)

    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(
        // One back event removes one entry, as with Navigation 2.
        backNavigationBehavior = BackNavigationBehavior.PopLatest,
    )
    val dispatcherOwner = rememberNavigationEventDispatcherOwner(enabled = isVisiblePage)
    CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides dispatcherOwner) {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            contentAlignment = Alignment.Center,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(listDetailStrategy),
            sizeTransform = navigationMode.sizeTransform,
            transitionSpec = { navigationMode.enterTransition togetherWith navigationMode.exitTransition },
            popTransitionSpec = { navigationMode.popEnterTransition togetherWith navigationMode.popExitTransition },
            predictivePopTransitionSpec = {
                navigationMode.popEnterTransition togetherWith navigationMode.popExitTransition
            },
            entryProvider = entryProvider(builder = entries),
        )
    }
}

/**
 * Opens [destination] from the [parent] entry. Entries above [parent] are removed first, so that a selection in a
 * list pane replaces the detail pane instead of stacking a new one. On a single pane, [parent] is the top entry and
 * this is a plain push.
 */
internal fun MutableList<NavKey>.openFrom(parent: NavKey, destination: NavKey) {
    if (lastOrNull() == destination) return
    val parentIndex = lastIndexOf(parent)
    if (parentIndex >= 0) {
        while (lastIndex > parentIndex) removeAt(lastIndex)
    }
    add(destination)
}

/**
 * Grid columns of the component lists. A list can fill the tab or share it with a detail pane, so the values come
 * from the width of the list and not from the window. When the list fills the tab, the values are the same as with
 * the window size class.
 */
internal object ListColumns {
    fun isExpanded(width: Dp): Boolean = width >= WIDTH_DP_EXPANDED_LOWER_BOUND.dp

    fun count(width: Dp): Int = if (width >= WIDTH_DP_MEDIUM_LOWER_BOUND.dp) 4 else 2
}

/** Content of the detail pane while no detail entry is in the back stack. */
@Composable
internal fun DetailPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Layout.bodyMargin),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.spark_catalog_list_detail_placeholder),
            style = SparkTheme.typography.body1,
            color = SparkTheme.colors.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(backStack: List<NavKey>) {
    val destination = backStack.lastOrNull()?.let { it::class.simpleName }.toString()
    TrackDisposableJank(destination) { metricsHolder ->
        metricsHolder.state?.putState("Navigation", destination)
        onDispose {}
    }
}
