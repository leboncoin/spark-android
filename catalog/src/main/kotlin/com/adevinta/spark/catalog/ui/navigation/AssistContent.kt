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

import android.app.assist.AssistContent
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.core.net.toUri
import androidx.navigation.NavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination
import com.adevinta.spark.catalog.CatalogHomeScreen
import com.adevinta.spark.catalog.MainActivity
import com.adevinta.spark.catalog.configurator.ConfiguratorShowcase
import com.adevinta.spark.catalog.configurator.ConfiguratorsList
import com.adevinta.spark.catalog.configurator.deepLinks
import com.adevinta.spark.catalog.examples.ExampleSection
import com.adevinta.spark.catalog.examples.ExampleShowcase
import com.adevinta.spark.catalog.examples.ExamplesList
import com.adevinta.spark.catalog.examples.deepLinks
import com.adevinta.spark.catalog.icons.IconShowcase
import com.adevinta.spark.catalog.icons.IconsList
import com.adevinta.spark.catalog.icons.deepLinks
import kotlinx.coroutines.flow.filter

/**
 * To be used inside [android.app.Activity.onProvideAssistContent]. Uses the [NavController] to get the
 * [NavController.currentBackStackEntry]. From it, iterates over `allDeepLinkUriPatterns` and if the current
 * destination has a deep link that matches any of them, it tries to construct a web uri from it, combining the
 * deepLinkUriPatter along with the destination's nav arguments and the backStackEntry's arguments.
 *
 * In the recents screen, if the current destination has a deep link, the system allows the user to copy the URL of it.
 *
 * Context:
 *  [Recents URL sharing](https://developer.android.com/guide/components/activities/recents#url-sharing)
 *  [onProvideAssistContent](https://developer.android.com/reference/android/app/Activity#onProvideAssistContent(android.app.assist.AssistContent))
 */
internal fun NavController.provideAssistContent(outContent: AssistContent) {
    val backStackEntry: NavBackStackEntry? = currentBackStackEntry
    val navDestination: NavDestination? = backStackEntry?.destination
    val allDeepLinkUriPatterns = listOf<List<NavDeepLink>>(
        ExamplesList.deepLinks,
        ExampleSection.deepLinks,
        ExampleShowcase.deepLinks,
        ConfiguratorsList.deepLinks,
        ConfiguratorShowcase.deepLinks,
        IconsList.deepLinks,
        IconShowcase.deepLinks,
    ).flatten().mapNotNull { it.uriPattern }
    for (deepLinkUriPattern in allDeepLinkUriPatterns) {
        if (navDestination?.hasDeepLink(deepLinkUriPattern.toUri()) != true) {
            continue
        }
        val webUri: String? = constructWebUriOrNull(deepLinkUriPattern, backStackEntry, navDestination)
        if (webUri != null) {
            outContent.webUri = webUri.toUri()
            break
        }
    }
}

/**
 * For a given [deepLinkUriPattern], try to construct a uri with any placeholders inside it filled in. If there are no
 * placeholders, the uri is returned as-is.
 * Takes the [navDestination]'s [NavDestination.arguments], and for each one of them, tries to find the value that
 * exists inside the [backStackEntry]'s [NavBackStackEntry.arguments] which is for the same argument.
 * If there are placeholders, but not all of them can be filled in, we return null.
 * e.g. for "https://example.com/{id}/{name}?extra={extra}" and a [NavBackStackEntry.arguments] list like
 *  {id: "1", name: "John", extra: "extraValue"}, the result would be "https://example.com/1/John?extra=extraValue"
 */
private fun constructWebUriOrNull(
    deepLinkUriPattern: String,
    backStackEntry: NavBackStackEntry,
    navDestination: NavDestination,
): String? {
    if (!deepLinkUriPattern.contains("{")) {
        // If no parameters exist in the deep link, just use it as-is
        return deepLinkUriPattern
    }
    val backStackEntryArguments: Bundle = backStackEntry.arguments ?: return null
    val argumentNameToRealValueList = navDestination.arguments.map { (argumentName: String, navArgument: NavArgument) ->
        val serializedTypeValue =
            navArgument.type.serializeAsValue(navArgument.type[backStackEntryArguments, argumentName])
        argumentName to serializedTypeValue
    }
    val deepLinkWithPlaceholdersFilled =
        argumentNameToRealValueList.fold(initial = deepLinkUriPattern) { acc, (argumentName: String, value: String) ->
            acc.replace("{$argumentName}", value)
        }
    return deepLinkWithPlaceholdersFilled.takeIf { !it.contains("{") }
}

/**
 * Since we have one NavController per tab we need to change the selected controller in the activity when the
 * [androidx.compose.foundation.pager.PagerState.currentPage] change as the screens are not compose on page selection.
 */
@Composable
internal fun ChangeSelectedNavControllerOnPageChange(
    pagerState: PagerState,
    catalogScreen: CatalogHomeScreen,
    navController: NavController,
) {
    val activity = LocalActivity.current as MainActivity

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .filter { it == catalogScreen.ordinal }
            .collect { activity.activeNavController = navController }
    }
}
