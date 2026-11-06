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
package com.adevinta.spark.catalog

import android.content.res.Configuration
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.net.Uri
import android.os.Build
import android.view.ContextThemeWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkFeatureFlag
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.configurator.ConfiguratorComponentsScreen
import com.adevinta.spark.catalog.examples.ComponentsScreen
import com.adevinta.spark.catalog.icons.IconDemoScreen
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.tabbar.CatalogTabBar
import com.adevinta.spark.catalog.tabbar.CatalogTabs
import com.adevinta.spark.catalog.themes.CatalogMotion
import com.adevinta.spark.catalog.themes.ColorMode
import com.adevinta.spark.catalog.themes.FontScaleMode.System
import com.adevinta.spark.catalog.themes.TextDirection
import com.adevinta.spark.catalog.themes.Theme
import com.adevinta.spark.catalog.themes.ThemeMode
import com.adevinta.spark.catalog.themes.ThemePicker
import com.adevinta.spark.catalog.themes.themeprovider.ThemeProvider
import com.adevinta.spark.catalog.themes.themeprovider.leboncoin.LeboncoinTheme
import com.adevinta.spark.catalog.ui.BackdropScaffold
import com.adevinta.spark.catalog.ui.BackdropScaffoldDefaults
import com.adevinta.spark.catalog.ui.BackdropValue
import com.adevinta.spark.catalog.ui.rememberBackdropScaffoldState
import com.adevinta.spark.catalog.ui.shaders.colorblindness.ColorBlindNessType
import com.adevinta.spark.catalog.ui.shaders.colorblindness.shader
import com.adevinta.spark.tokens.asSparkColors
import com.adevinta.spark.tokens.contrastLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun ComponentActivity.CatalogApp(
    theme: Theme,
    onThemeChange: (theme: Theme) -> Unit,
    components: ImmutableList<Component>,
) {
    val themeProvider: ThemeProvider = LeboncoinTheme

    val useDark = theme.themeMode.isDark(isSystemDark = isSystemInDarkTheme())
    val contrastLevel = contrastLevel(LocalContext.current)

    val colors = if (theme.colorMode == ColorMode.Dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (useDark) {
            dynamicDarkColorScheme(LocalContext.current)
        } else {
            dynamicLightColorScheme(LocalContext.current)
        }.asSparkColors(useDark = useDark)
    } else {
        themeProvider.colors(
            useDarkColors = useDark,
            brandMode = theme.brandMode,
            versionMode = theme.versionMode,
            contrastLevel = contrastLevel,
        )
    }

    val shapes = themeProvider.shapes()
    val typography = themeProvider.typography()

    SparkTheme(
        colors = colors,
        shapes = shapes,
        typography = typography,
        sparkFeatureFlag = SparkFeatureFlag(
            useSparkTokensHighlighter = theme.highlightSparkTokens,
            useSparkComponentsHighlighter = theme.highlightSparkComponents,
            isContainingActivityEdgeToEdge = true,
            useRebrandedButtons = theme.useRebrandedButtons,
        ),
    ) {
        val layoutDirection = when (theme.textDirection) {
            TextDirection.LTR -> LayoutDirection.Ltr
            TextDirection.RTL -> LayoutDirection.Rtl
            TextDirection.System -> LocalLayoutDirection.current
        }

        val colorBlindNessType = theme.colorBlindNessType
        val colorBlindNessSeverity = theme.colorBlindNessSeverity
        val runtimeShader = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            colorBlindNessType != ColorBlindNessType.None
        ) {
            remember { RuntimeShader(shader) }
        } else {
            null
        }

        CatalogConfigurationOverride(
            isDarkMode = useDark,
            layoutDirection = layoutDirection,
            fontScale = theme.takeUnless { it.fontScaleMode == System }?.fontScale ?: LocalDensity.current.fontScale,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && runtimeShader != null) {
                            Modifier.graphicsLayer {
                                runtimeShader.setFloatUniform("severity", colorBlindNessSeverity)
                                runtimeShader.setIntUniform("colorblindType", colorBlindNessType.ordinal)
                                clip = true
                                renderEffect = RenderEffect
                                    .createRuntimeShaderEffect(
                                        /* shader = */ runtimeShader,
                                        /* uniformShaderName = */ "composable",
                                    )
                                    .asComposeRenderEffect()
                            }
                        } else {
                            Modifier
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                val coroutineScope = rememberCoroutineScope()
                val homeScreenValues = CatalogHomeScreen.entries
                val intent = LocalActivity.current?.intent
                val initialPage = getInitialScreen(intent?.data)

                val pagerState = rememberPagerState(
                    initialPage = initialPage.ordinal,
                    pageCount = { homeScreenValues.size },
                )

                ReportDrawnWhen { pagerState.layoutInfo.visiblePagesInfo.isNotEmpty() }

                BackdropScaffold(
                    scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
                    frontLayerScrimColor = Color.Unspecified,
                    headerHeight = BackdropScaffoldDefaults.HeaderHeight +
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                    peekHeight = BackdropScaffoldDefaults.PeekHeight +
                        WindowInsets.statusBars.asPaddingValues()
                            .calculateTopPadding() -
                        4.dp,
                    backLayerBackgroundColor = SparkTheme.colors.mainContainer,
                    appBar = {
                        HomeTabBar(
                            modifier = Modifier
                                .requiredHeightIn(
                                    min =
                                    BackdropScaffoldDefaults.PeekHeight +
                                        WindowInsets.statusBars.asPaddingValues()
                                            .calculateTopPadding(),
                                )
                                .statusBarsPadding(),
                            tabSelected = homeScreenValues[pagerState.currentPage],
                            onTabSelected = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        it.ordinal,
                                        animationSpec = CatalogMotion.TabPagerScroll,
                                    )
                                }
                            },
                        )
                    },
                    backLayerContent = {
                        ThemePicker(
                            theme = theme,
                            onThemeChange = { theme ->
                                coroutineScope.launch {
                                    onThemeChange(theme)
                                }
                            },
                        )
                    },
                    frontLayerContent = {
                        val insetsPadding = WindowInsets.navigationBars.asPaddingValues()
                        val innerPadding = PaddingValues(
                            top = 16.dp + insetsPadding.calculateTopPadding(),
                            end = insetsPadding.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = insetsPadding.calculateBottomPadding(),
                            start = insetsPadding.calculateStartPadding(LocalLayoutDirection.current),
                        )
                        HorizontalPager(
                            state = pagerState,
                            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                            beyondViewportPageCount = 1,
                            key = {
                                when (it) {
                                    0 -> CatalogHomeScreen.Examples
                                    1 -> CatalogHomeScreen.Configurator
                                    2 -> CatalogHomeScreen.Icons
                                    else -> CatalogHomeScreen.Examples
                                }.ordinal
                            },
                        ) {
                            when (homeScreenValues[it]) {
                                CatalogHomeScreen.Examples -> ComponentsScreen(
                                    components = components,
                                    pagerState = pagerState,
                                    contentPadding = innerPadding,
                                    navigationMode = theme.navigationMode,
                                )

                                CatalogHomeScreen.Configurator -> ConfiguratorComponentsScreen(
                                    components = components,
                                    pagerState = pagerState,
                                    contentPadding = innerPadding,
                                    navigationMode = theme.navigationMode,
                                )

                                CatalogHomeScreen.Icons -> IconDemoScreen(
                                    contentPadding = innerPadding,
                                    pagerState = pagerState,
                                    navigationMode = theme.navigationMode,
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

private fun isContrastAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

@Composable
private fun HomeTabBar(
    tabSelected: CatalogHomeScreen,
    onTabSelected: (CatalogHomeScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val catalogScreens by remember { mutableStateOf(CatalogHomeScreen.entries) }
    val catalogScreensName by remember {
        derivedStateOf {
            catalogScreens.map { it.name }.toImmutableList()
        }
    }
    CatalogTabBar(
        modifier = modifier
            .wrapContentWidth()
            .sizeIn(maxWidth = 500.dp),
    ) { tabBarModifier ->
        CatalogTabs(
            modifier = tabBarModifier,
            titles = catalogScreensName,
            tabSelected = tabSelected,
            onTabSelect = { newTab -> onTabSelected(catalogScreens[newTab.ordinal]) },
        )
    }
}

private fun getInitialScreen(uri: Uri?): CatalogHomeScreen {
    val initialScreen = when (uri?.host) {
        "examples" -> CatalogHomeScreen.Examples
        "configurator" -> CatalogHomeScreen.Configurator
        "icons" -> CatalogHomeScreen.Icons
        else -> null
    }

    return initialScreen ?: CatalogHomeScreen.Examples
}

internal enum class CatalogHomeScreen { Examples, Configurator, Icons }

internal const val AppBasePath = "spark://"

/** Applies the in-app dark mode, layout direction, and font scale to [content]; keeps `LocalActivity` resolvable. */
@Composable
private fun CatalogConfigurationOverride(
    isDarkMode: Boolean,
    layoutDirection: LayoutDirection,
    fontScale: Float,
    content: @Composable () -> Unit,
) {
    val baseContext = LocalContext.current
    val baseConfiguration = LocalConfiguration.current
    val configuration = remember(baseConfiguration, isDarkMode, layoutDirection, fontScale) {
        Configuration(baseConfiguration).apply {
            val nightMode = if (isDarkMode) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
            uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or nightMode

            val layoutDirectionMode = when (layoutDirection) {
                LayoutDirection.Ltr -> Configuration.SCREENLAYOUT_LAYOUTDIR_LTR
                LayoutDirection.Rtl -> Configuration.SCREENLAYOUT_LAYOUTDIR_RTL
            }
            screenLayout = (screenLayout and Configuration.SCREENLAYOUT_LAYOUTDIR_MASK.inv()) or layoutDirectionMode

            this.fontScale = fontScale
        }
    }
    val context = remember(baseContext, configuration) {
        ContextThemeWrapper(baseContext, 0).apply { applyOverrideConfiguration(configuration) }
    }

    CompositionLocalProvider(
        LocalContext provides context,
        LocalConfiguration provides configuration,
        LocalLayoutDirection provides layoutDirection,
        LocalDensity provides Density(LocalDensity.current.density, fontScale),
        content = content,
    )
}
