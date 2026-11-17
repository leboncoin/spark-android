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
package com.adevinta.spark.catalog.examples

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.adevinta.spark.catalog.examples.component.Component
import com.adevinta.spark.catalog.examples.example.Example
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.navigation.DetailPlaceholder
import com.adevinta.spark.catalog.ui.navigation.openFrom
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
internal data class ExampleSection(val componentId: String) : NavKey

@Serializable
internal data class ExampleShowcase(val componentId: String, val exampleId: String) : NavKey

/**
 * Entries of the examples tab. The component list is the list pane, the examples of a component are the detail pane,
 * and one example is the extra pane.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun EntryProviderScope<NavKey>.examplesEntries(
    backStack: MutableList<NavKey>,
    components: ImmutableList<Component>,
    contentPadding: PaddingValues,
) {
    entry<ExamplesList>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = { DetailPlaceholder() }),
    ) {
        CompositionLocalProvider(LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current) {
            ComponentsListScreen(
                components = components,
                contentPadding = contentPadding,
                onExampleSectionClick = { id ->
                    backStack.openFrom(parent = ExamplesList, destination = ExampleSection(componentId = id))
                },
            )
        }
    }

    entry<ExampleSection>(
        metadata = ListDetailSceneStrategy.detailPane(),
    ) { exampleSection ->
        val componentId = exampleSection.componentId
        val component = components.firstOrNull { component -> component.id == componentId }
        if (component == null) {
            LaunchedEffect(Unit) { backStack.remove(exampleSection) }
            return@entry
        }
        CompositionLocalProvider(LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current) {
            Component(
                component = component,
                contentPadding = contentPadding,
                onExampleClick = { exampleId ->
                    backStack.openFrom(
                        parent = exampleSection,
                        destination = ExampleShowcase(componentId = componentId, exampleId = exampleId),
                    )
                },
            )
        }
    }

    entry<ExampleShowcase>(
        metadata = ListDetailSceneStrategy.extraPane(),
    ) { exampleShowcase ->
        val example = components
            .firstOrNull { component -> component.id == exampleShowcase.componentId }
            ?.examples
            ?.firstOrNull { it.id == exampleShowcase.exampleId }
        if (example == null) {
            LaunchedEffect(Unit) { backStack.remove(exampleShowcase) }
            return@entry
        }
        CompositionLocalProvider(LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current) {
            Example(
                example = example,
            )
        }
    }
}

internal data class ExamplesSharedElementKey(
    val exampleId: String,
    val origin: String,
    val type: ExamplesSharedElementType,
)

internal enum class ExamplesSharedElementType {
    Background,
    Illustration,
    Title,
}
