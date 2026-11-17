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
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.adevinta.spark.catalog.CatalogHomeScreen
import com.adevinta.spark.catalog.themes.NavigationMode
import com.adevinta.spark.catalog.ui.navigation.CatalogNavDisplay
import com.adevinta.spark.catalog.ui.navigation.rememberCatalogBackStack
import kotlinx.serialization.Serializable

@Serializable
internal object IconsList : NavKey

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun IconDemoScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    pagerState: PagerState,
    navigationMode: NavigationMode,
) {
    SharedTransitionLayout {
        val backStack = rememberCatalogBackStack(root = IconsList)
        CatalogNavDisplay(
            modifier = modifier,
            catalogScreen = CatalogHomeScreen.Icons,
            pagerState = pagerState,
            backStack = backStack,
            navigationMode = navigationMode,
            entries = {
                iconsDemoEntries(
                    backStack = backStack,
                    contentPadding = contentPadding,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            },
        )
    }
}
