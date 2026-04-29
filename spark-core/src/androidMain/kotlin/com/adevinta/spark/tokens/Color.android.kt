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
package com.adevinta.spark.tokens

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.FloatRange
import androidx.core.content.getSystemService
import com.adevinta.spark.ExperimentalSparkApi

/**
 * Contrast level is an API available in the [UiModeManager] starting Android 34.
 */
public val isContrastLevelAvailable: Boolean = Build.VERSION.SDK_INT >= 34

/**
 * Retrieve the contrast level of the device. This value can be used to adjust the colors of the theme.
 *
 * It is available on Android 34 and above. On lower versions, the fallback value will be returned.
 *
 * The contrast level is a float value between -1.0 and 1.0. The default value is 0.0.
 * A value of -1.0 means that the contrast is set to the minimum value, and a value of 1.0 means that the contrast is
 * set to the maximum value.
 *
 * @param context The context to use to retrieve the UiModeManager.
 * @param fallback The fallback value to return if the contrast level is not available.
 * @return The contrast level of the device or the fallback if not available.
 *
 * @see isContrastLevelAvailable
 * @see UiModeManager.getContrast
 */
@ExperimentalSparkApi
@FloatRange(from = -1.0, to = 1.0)
public fun contrastLevel(
    context: Context,
    fallback: () -> Float = { 0f },
): Float = if (isContrastLevelAvailable) {
    val uiModeManager = context.getSystemService<UiModeManager>()
    uiModeManager?.contrast ?: fallback()
} else {
    fallback()
}
