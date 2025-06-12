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

import android.app.UiModeManager
import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.DarkMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.LayoutDirection
import androidx.compose.ui.test.then
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.adevinta.spark.SparkFeatureFlag
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.configurator.ConfiguratorComponentsScreen
import com.adevinta.spark.catalog.examples.ComponentsScreen
import com.adevinta.spark.catalog.icons.IconDemoScreen
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.tabbar.CatalogTabBar
import com.adevinta.spark.catalog.tabbar.CatalogTabs
import com.adevinta.spark.catalog.themes.ColorMode
import com.adevinta.spark.catalog.themes.FontScaleMode.System
import com.adevinta.spark.catalog.themes.TextDirection
import com.adevinta.spark.catalog.themes.Theme
import com.adevinta.spark.catalog.themes.ThemeMode
import com.adevinta.spark.catalog.themes.ThemePicker
import com.adevinta.spark.catalog.themes.UserMode
import com.adevinta.spark.catalog.themes.themeprovider.ThemeProvider
import com.adevinta.spark.catalog.themes.themeprovider.leboncoin.LeboncoinTheme
import com.adevinta.spark.catalog.ui.BackdropScaffold
import com.adevinta.spark.catalog.ui.BackdropScaffoldDefaults
import com.adevinta.spark.catalog.ui.BackdropValue
import com.adevinta.spark.catalog.ui.rememberBackdropScaffoldState
import com.adevinta.spark.catalog.ui.shaders.colorblindness.ColorBlindNessType
import com.adevinta.spark.catalog.ui.shaders.colorblindness.shader
import com.adevinta.spark.tokens.asSparkColors
import kotlinx.coroutines.launch

@Composable
internal fun ComponentActivity.CatalogApp(
    theme: Theme,
    onThemeChange: (theme: Theme) -> Unit,
    components: List<Component>,
) {
    val themeProvider: ThemeProvider = LeboncoinTheme

    val useDark = (theme.themeMode == ThemeMode.System && isSystemInDarkTheme()) || theme.themeMode == ThemeMode.Dark

    // TODO(b/336693596): UIModeManager is not yet supported in preview
    val contrastLevel = if (isContrastAvailable()) {
        val uiModeManager = LocalContext.current.getSystemService<UiModeManager>()
        uiModeManager?.contrast ?: 0f
    } else {
        0f
    }

    val colors = if (theme.colorMode == ColorMode.Dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (useDark) {
            dynamicDarkColorScheme(LocalContext.current)
        } else {
            dynamicLightColorScheme(LocalContext.current)
        }.asSparkColors(useDark = true)
    } else {
        themeProvider.colors(
            useDarkColors = useDark,
            isPro = theme.userMode == UserMode.Pro,
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
            useLegacyStyle = theme.useLegacyTheme,
            useSparkTokensHighlighter = theme.highlightSparkTokens,
            useSparkComponentsHighlighter = theme.highlightSparkComponents,
        ),
    ) {
        val layoutDirection = when (theme.textDirection) {
            TextDirection.LTR -> LayoutDirection.Ltr
            TextDirection.RTL -> LayoutDirection.Rtl
            TextDirection.System -> LocalLayoutDirection.current
        }

        // Update the edge to edge configuration to match the theme
        // This is the same parameters as the default enableEdgeToEdge call, but we manually
        // resolve whether or not to show dark theme using uiState, since it can be different
        // than the configuration's dark theme value based on the user preference.
        DisposableEffect(useDark) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { useDark },
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim,
                    darkScrim,
                ) { useDark },
            )
            onDispose {}
        }
        // Shader for colorblindness demo
        val runtimeShader = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RuntimeShader(shader)
        } else {
            null
        }

        val colorBlindNessType = theme.colorBlindNessType
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            runtimeShader?.setFloatUniform("severity", theme.colorBlindNessSeverity)
            runtimeShader?.setIntUniform(
                "colorblindType",
                colorBlindNessType.ordinal,
            )
        }

        DeviceConfigurationOverride(
            override = DeviceConfigurationOverride.DarkMode(useDark)
                then DeviceConfigurationOverride.LayoutDirection(layoutDirection)
                then DeviceConfigurationOverride.FontScale(
                    theme.takeUnless { it.fontScaleMode == System }?.fontScale ?: LocalDensity.current.fontScale,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            colorBlindNessType != ColorBlindNessType.None
                        ) {
                            Modifier.graphicsLayer {
                                clip = true
                                renderEffect = RenderEffect
                                    .createRuntimeShaderEffect(
                                        /* shader = */ runtimeShader!!,
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

                BackdropScaffold(
                    scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed),
                    frontLayerScrimColor = Color.Unspecified,
                    headerHeight = BackdropScaffoldDefaults.HeaderHeight +
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                    peekHeight = BackdropScaffoldDefaults.PeekHeight + WindowInsets.statusBars.asPaddingValues()
                        .calculateTopPadding() - 4.dp,
                    backLayerBackgroundColor = SparkTheme.colors.mainContainer,
                    appBar = {
                        HomeTabBar(
                            modifier = Modifier
                                .requiredHeightIn(
                                    min =
                                    BackdropScaffoldDefaults.PeekHeight + WindowInsets.statusBars.asPaddingValues()
                                        .calculateTopPadding(),
                                )
                                .statusBarsPadding(),
                            tabSelected = homeScreenValues[pagerState.currentPage],
                            onTabSelected = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        it.ordinal,
                                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
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
            catalogScreens.map { it.name }
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
            onTabSelected = { newTab -> onTabSelected(catalogScreens[newTab.ordinal]) },
        )
    }
}

@Composable
private fun getInitialScreen(uri: Uri?): CatalogHomeScreen {
    val initialScreen = uri?.pathSegments?.let { segments ->
        when {
            "examples" in segments -> CatalogHomeScreen.Examples
            "configurator" in segments -> CatalogHomeScreen.Configurator
            "icons" in segments -> CatalogHomeScreen.Icons
            else -> null
        }
    }

    return initialScreen ?: CatalogHomeScreen.Examples
}

public enum class CatalogHomeScreen { Examples, Configurator, Icons }

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

internal const val AppBasePath = "spark://"
