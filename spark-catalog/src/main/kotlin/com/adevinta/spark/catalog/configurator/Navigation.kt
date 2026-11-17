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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.adevinta.spark.catalog.configurator.component.ConfiguratorComponentScreen
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.navigation.DetailPlaceholder
import com.adevinta.spark.catalog.ui.navigation.openFrom
import kotlinx.serialization.Serializable

@Serializable
internal object ConfiguratorsList : NavKey

@Serializable
internal data class ConfiguratorShowcase(val componentId: String, val configuratorId: String) : NavKey

/** Entries of the configurator tab. The component list is the list pane and one configurator is the detail pane. */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun EntryProviderScope<NavKey>.configuratorsEntries(
    backStack: MutableList<NavKey>,
    components: List<Component>,
    contentPadding: PaddingValues,
) {
    entry<ConfiguratorsList>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = { DetailPlaceholder() }),
    ) {
        CompositionLocalProvider(LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current) {
            ComponentsListScreen(
                components = components,
                contentPadding = contentPadding,
                onConfiguratorSelected = { id, index ->
                    backStack.openFrom(
                        parent = ConfiguratorsList,
                        destination = ConfiguratorShowcase(componentId = id, configuratorId = index),
                    )
                },
            )
        }
    }

    entry<ConfiguratorShowcase>(
        metadata = ListDetailSceneStrategy.detailPane(),
    ) { configuratorShowcase ->
        val componentId = configuratorShowcase.componentId
        val configuratorId = configuratorShowcase.configuratorId
        val component =
            components.firstOrNull { component ->
                component.configurators.firstOrNull() != null &&
                    component.id == componentId
            }
        val configurator = component?.configurators?.firstOrNull { it.id == configuratorId }
        if (component == null || configurator == null) {
            LaunchedEffect(Unit) { backStack.remove(configuratorShowcase) }
            return@entry
        }
        CompositionLocalProvider(LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current) {
            ConfiguratorComponentScreen(
                component = component,
                configurator = configurator,
            )
        }
    }
}
