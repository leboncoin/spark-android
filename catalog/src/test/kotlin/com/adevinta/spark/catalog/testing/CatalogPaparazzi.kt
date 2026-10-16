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
package com.adevinta.spark.catalog.testing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.adevinta.spark.SparkTheme
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Keyboard
import com.android.resources.KeyboardState
import com.android.resources.Navigation
import com.android.resources.ScreenOrientation
import com.android.resources.ScreenRatio
import com.android.resources.ScreenSize
import com.android.resources.TouchScreen
import com.android.resources.Density as ResourceDensity

internal fun catalogPaparazzi(
    deviceConfig: DeviceConfig = CatalogTestDevices.Phone,
    renderingMode: SessionParams.RenderingMode = SessionParams.RenderingMode.SHRINK,
): Paparazzi = Paparazzi(
    deviceConfig = deviceConfig,
    renderingMode = renderingMode,
    maxPercentDifference = CatalogMaxPercentDifference,
    theme = CatalogPaparazziTheme,
    useDeviceResolution = true,
)

internal object CatalogTestDevices {
    /** Pixel 6 Pro, compact width (411 dp). */
    val Phone = DeviceConfig(
        screenHeight = 3120,
        screenWidth = 1440,
        xdpi = 512,
        ydpi = 512,
        orientation = ScreenOrientation.PORTRAIT,
        density = ResourceDensity.create(560),
        ratio = ScreenRatio.LONG,
        size = ScreenSize.NORMAL,
        keyboard = Keyboard.NOKEY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
        released = "October 28, 2021",
    )

    /** Medium width (700 dp by 1000 dp). */
    val Medium = DeviceConfig(
        screenHeight = 2000,
        screenWidth = 1400,
        xdpi = 320,
        ydpi = 320,
        orientation = ScreenOrientation.PORTRAIT,
        density = ResourceDensity.XHIGH,
        ratio = ScreenRatio.NOTLONG,
        size = ScreenSize.LARGE,
        keyboard = Keyboard.NOKEY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
    )

    val Foldable = DeviceConfig(
        screenHeight = 2480,
        screenWidth = 2200,
        xdpi = 420,
        ydpi = 420,
        orientation = ScreenOrientation.PORTRAIT,
        density = ResourceDensity.create(420),
        ratio = ScreenRatio.NOTLONG,
        size = ScreenSize.XLARGE,
        keyboard = Keyboard.NOKEY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
        released = "October 31, 2013",
    )

    /** Pixel C, expanded width (1280 dp). */
    val Tablet = DeviceConfig(
        screenHeight = 1800,
        screenWidth = 2560,
        xdpi = 308,
        ydpi = 308,
        orientation = ScreenOrientation.LANDSCAPE,
        density = ResourceDensity.XHIGH,
        ratio = ScreenRatio.NOTLONG,
        size = ScreenSize.XLARGE,
        keyboard = Keyboard.QWERTY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
        released = "December 8, 2015",
    )

    val all: List<DeviceConfig> = listOf(Phone, Medium, Foldable, Tablet)
}

internal fun Paparazzi.catalogSnapshot(
    name: String? = null,
    isDark: Boolean = false,
    isHighContrast: Boolean = false,
    fontScale: Float = 1f,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    content: @Composable () -> Unit,
): Unit = snapshot(name) {
    CatalogSnapshotContent(
        isDark = isDark,
        isHighContrast = isHighContrast,
        fontScale = fontScale,
        layoutDirection = layoutDirection,
        content = content,
    )
}

internal fun Paparazzi.catalogSnapshotDark(name: String? = null, content: @Composable () -> Unit) =
    catalogSnapshot(name = name, isDark = true, content = content)

internal fun Paparazzi.catalogSnapshotHighContrast(name: String? = null, content: @Composable () -> Unit) =
    catalogSnapshot(name = name, isHighContrast = true, content = content)

internal fun Paparazzi.catalogSnapshotLargeFont(name: String? = null, content: @Composable () -> Unit) =
    catalogSnapshot(name = name, fontScale = LargeFontScale, content = content)

internal fun Paparazzi.catalogSnapshotRtl(name: String? = null, content: @Composable () -> Unit) =
    catalogSnapshot(name = name, layoutDirection = LayoutDirection.Rtl, content = content)

@Composable
private fun CatalogSnapshotContent(
    isDark: Boolean,
    isHighContrast: Boolean,
    fontScale: Float,
    layoutDirection: LayoutDirection,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalInspectionMode provides true,
        LocalDensity provides Density(density = density.density, fontScale = fontScale),
        LocalLayoutDirection provides layoutDirection,
    ) {
        CatalogTestTheme(isDark = isDark, isHighContrast = isHighContrast) {
            // The outer box stops ComposeView from forcing the content to fill the screen.
            Box {
                Box(Modifier.background(SparkTheme.colors.surface)) {
                    content()
                }
            }
        }
    }
}

internal const val LargeFontScale: Float = 2f
private const val CatalogMaxPercentDifference: Double = 0.01
private const val CatalogPaparazziTheme: String = "android:Theme.MaterialComponent.Light.NoActionBar"
