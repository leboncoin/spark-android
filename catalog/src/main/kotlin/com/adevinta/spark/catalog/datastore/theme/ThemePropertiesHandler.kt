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
package com.adevinta.spark.catalog.datastore.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.adevinta.spark.catalog.themes.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

internal class ThemePropertiesHandler(context: Context) {

    private val datastore = context.datastore

    val properties: Flow<Theme> = datastore.data
        .map(ThemeProperties::toTheme)
        .catch { exception ->
            when (exception) {
                is IOException -> emit(
                    ThemeProperties.DEFAULT.copy(
                        fontScale = context.resources.configuration.fontScale,
                    ).toTheme(),
                )

                else -> throw exception
            }
        }

    suspend fun updateProperties(theme: Theme) {
        datastore.updateData { theme.toDataStoreThemeProperties() }
    }

    companion object {
        private val Context.datastore: DataStore<ThemeProperties> by dataStore(
            fileName = "theme_properties.pb",
            serializer = ThemePropertiesSerializer,
        )
    }
}

@Composable
internal fun Flow<Theme>.collectAsStateWithDefault(context: Context): State<Theme> = collectAsState(
    initial = ThemeProperties.DEFAULT.copy(
        fontScale = context.resources.configuration.fontScale,
    ).toTheme(),
)

private fun ThemeProperties.toTheme(): Theme = Theme(
    fontScale = fontScale,
    userMode = userMode,
    themeMode = themeMode,
    colorMode = colorMode,
    fontScaleMode = fontScaleMode,
    colorBlindNessType = colorBlindNessType,
    colorBlindNessSeverity = colorBlindNessSeverity,
    navigationMode = navigationMode,
    textDirection = textDirection,
    highlightSparkComponents = highlightSparkComponents,
    highlightSparkTokens = highlightSparkTokens,
)

private fun Theme.toDataStoreThemeProperties(): ThemeProperties = ThemeProperties(
    fontScale = fontScale,
    userMode = userMode,
    themeMode = themeMode,
    colorMode = colorMode,
    fontScaleMode = fontScaleMode,
    colorBlindNessType = colorBlindNessType,
    colorBlindNessSeverity = colorBlindNessSeverity,
    navigationMode = navigationMode,
    textDirection = textDirection,
    highlightSparkComponents = highlightSparkComponents,
    highlightSparkTokens = highlightSparkTokens,
)
