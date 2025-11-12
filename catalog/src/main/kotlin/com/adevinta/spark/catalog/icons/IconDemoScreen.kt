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

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.adevinta.spark.catalog.AppBasePath
import com.adevinta.spark.catalog.CatalogHomeScreen
import com.adevinta.spark.catalog.MainActivity
import com.adevinta.spark.catalog.themes.NavigationMode
import com.adevinta.spark.catalog.ui.navigation.ChangeSelectedNavControllerOnPageChange
import com.adevinta.spark.catalog.ui.navigation.NavHostSpark
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.Serializable

@Serializable
public object IconsList

internal val IconsList.deepLinks: List<NavDeepLink>
    get() = listOf(
        navDeepLink<IconsList>(basePath = "${AppBasePath}icons"),
    )

@Composable
private fun MonitorPagerChanges(
    pagerState: PagerState,
    catalogScreen: CatalogHomeScreen,
    navController: androidx.navigation.NavController,
    activity: MainActivity,
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .filter { it == catalogScreen.ordinal }
            .collect { activity.activeNavController = navController }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun IconDemoScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    pagerState: PagerState,
    navigationMode: NavigationMode,
) {
    SharedTransitionLayout {
        val navController = rememberNavController()

        ChangeSelectedNavControllerOnPageChange(
            pagerState = pagerState,
            catalogScreen = CatalogHomeScreen.Icons,
            navController = navController,
        )

        NavHostSpark(
            modifier = modifier,
            navController = navController,
            startDestination = IconsList,
            navigationMode = navigationMode,
            builder = {
                iconsDemoDestination(
                    navController = navController,
                    contentPadding = contentPadding,
                    this@SharedTransitionLayout,
                )
            },
        )
    }
}
