/*
 * Copyright (c) 2026 Adevinta
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

import android.net.Uri
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.deeplink.BackStackMatcher
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.deeplink.DeepLinkUri
import androidx.navigation3.runtime.deeplink.UriDeepLinkMatcher
import androidx.navigation3.runtime.deeplink.withBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.adevinta.spark.catalog.AppBasePath
import com.adevinta.spark.catalog.configurator.ConfiguratorShowcase
import com.adevinta.spark.catalog.configurator.ConfiguratorsList
import com.adevinta.spark.catalog.examples.ExampleSection
import com.adevinta.spark.catalog.examples.ExampleShowcase
import com.adevinta.spark.catalog.examples.ExamplesList
import com.adevinta.spark.catalog.icons.IconShowcase
import com.adevinta.spark.catalog.icons.IconsList
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer

/**
 * The `spark://` deep links of the catalog. The URIs are the same as with Navigation 2.
 *
 * Navigation 3 does not read the activity intent, so [backStackFor] converts the intent URI into a back stack, and
 * [uriFor] converts a back stack entry back into a URI for the assist content.
 */
internal object CatalogDeepLinks {

    private val matchers: List<BackStackMatcher<NavKey, NavKey>> = listOf(
        matcher<ExamplesList>("examples") { listOf(it) },
        matcher<ExampleSection>("examples/component/{componentId}") { listOf(ExamplesList, it) },
        matcher<ExampleShowcase>("examples/component/detail/{componentId}/{exampleId}") {
            listOf(ExamplesList, ExampleSection(componentId = it.componentId), it)
        },
        matcher<ConfiguratorsList>("configurator") { listOf(it) },
        matcher<ConfiguratorShowcase>("configurator/detail/{componentId}/{configuratorId}") {
            listOf(ConfiguratorsList, it)
        },
        matcher<IconsList>("icons") { listOf(it) },
        matcher<IconShowcase>("icons/detail/{iconName}/{isIconAnimated}") { listOf(IconsList, it) },
    )

    /** Returns the back stack for a `spark://` [uri], or null when no destination matches it. */
    fun backStackFor(uri: Uri?): List<NavKey>? {
        if (uri == null) return null
        val request = DeepLinkRequest(uri = DeepLinkUri(uri.toString()))
        return matchers.firstNotNullOfOrNull { it.match(request) }?.backStack
    }

    /** Returns the `spark://` URI that opens [key]. */
    fun uriFor(key: NavKey): String? = when (key) {
        ExamplesList -> "${AppBasePath}examples"

        is ExampleSection -> "${AppBasePath}examples/component/${key.componentId.encoded()}"

        is ExampleShowcase ->
            "${AppBasePath}examples/component/detail/${key.componentId.encoded()}/${key.exampleId.encoded()}"

        ConfiguratorsList -> "${AppBasePath}configurator"

        is ConfiguratorShowcase ->
            "${AppBasePath}configurator/detail/${key.componentId.encoded()}/${key.configuratorId.encoded()}"

        IconsList -> "${AppBasePath}icons"

        is IconShowcase -> "${AppBasePath}icons/detail/${key.iconName.encoded()}/${key.isIconAnimated}"

        else -> null
    }

    private inline fun <reified T : NavKey> matcher(
        path: String,
        crossinline backStack: (key: T) -> List<NavKey>,
    ): BackStackMatcher<T, NavKey> = UriDeepLinkMatcher(
        uriPattern = DeepLinkUri("$AppBasePath$path"),
        serializer = serializer<T>(),
    ).withBackStack { matchResult -> backStack(matchResult.key) }

    private fun String.encoded(): String = Uri.encode(this)
}

/** Saves the back stacks of the catalog without reflection. Register every catalog [NavKey] here. */
internal val CatalogNavSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ExamplesList::class)
            subclass(ExampleSection::class)
            subclass(ExampleShowcase::class)
            subclass(ConfiguratorsList::class)
            subclass(ConfiguratorShowcase::class)
            subclass(IconsList::class)
            subclass(IconShowcase::class)
        }
    }
}
