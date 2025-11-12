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
package com.adevinta.spark

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.RenderExtension
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import com.android.resources.Keyboard
import com.android.resources.KeyboardState
import com.android.resources.Navigation
import com.android.resources.ScreenOrientation
import com.android.resources.ScreenRatio
import com.android.resources.ScreenSize
import com.android.resources.TouchScreen

fun paparazziRule(
    renderingMode: SessionParams.RenderingMode = SessionParams.RenderingMode.SHRINK,
    deviceConfig: DeviceConfig = DefaultTestDevices.Phone,
    renderExtensions: Set<RenderExtension> = setOf(),
) = Paparazzi(
    deviceConfig = deviceConfig,
    renderingMode = renderingMode,
    renderExtensions = renderExtensions,
    maxPercentDifference = MaxPercentDifference,
    theme = PaparazziTheme,
    useDeviceResolution = true,
)

/**
 * Defaults devices configuration taken from NiA
 */
object DefaultTestDevices {
    /**
     * Pixel 6 Pro
     */
    val Phone = DeviceConfig(
        screenHeight = 3120,
        screenWidth = 1440,
        xdpi = 512,
        ydpi = 512,
        orientation = ScreenOrientation.PORTRAIT,
        density = Density.create(560),
        ratio = ScreenRatio.LONG,
        size = ScreenSize.NORMAL,
        keyboard = Keyboard.NOKEY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
        released = "October 28, 2021",
    )
    val Foldable = DeviceConfig(
        screenHeight = 2480,
        screenWidth = 2200,
        xdpi = 420,
        ydpi = 420,
        orientation = ScreenOrientation.PORTRAIT,
        density = Density.create(420),
        ratio = ScreenRatio.NOTLONG,
        size = ScreenSize.XLARGE,
        keyboard = Keyboard.NOKEY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
        released = "October 31, 2013",
    )

    /**
     * Pixel C
     */
    val Tablet = DeviceConfig(
        screenHeight = 1800,
        screenWidth = 2560,
        xdpi = 308,
        ydpi = 308,
        orientation = ScreenOrientation.LANDSCAPE,
        density = Density.XHIGH,
        ratio = ScreenRatio.NOTLONG,
        size = ScreenSize.XLARGE,
        keyboard = Keyboard.QWERTY,
        touchScreen = TouchScreen.FINGER,
        keyboardState = KeyboardState.SOFT,
        softButtons = true,
        navigation = Navigation.NONAV,
        released = "December 8, 2015",
    )
    internal val devices = listOf(Phone, Foldable, Tablet)
}

data class TestDeviceSpecs(val width: Int, val height: Int, val dpi: Int)

internal const val MaxPercentDifference: Double = 0.01
internal const val PaparazziTheme: String = "android:Theme.MaterialComponent.Light.NoActionBar"
