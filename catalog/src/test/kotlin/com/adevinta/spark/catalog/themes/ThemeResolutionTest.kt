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
package com.adevinta.spark.catalog.themes

import android.content.res.Configuration
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors
import com.google.common.truth.Truth.assertThat
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(TestParameterInjector::class)
internal class ThemeResolutionTest {

    @Test
    fun dynamicColorsFollowTheResolvedDarkMode(
        @TestParameter themeMode: ThemeMode,
        @TestParameter isSystemDark: Boolean,
    ) {
        val expectDark = themeMode == ThemeMode.Dark || (themeMode == ThemeMode.System && isSystemDark)

        val colors = resolveSparkColors(
            themeMode = themeMode,
            colorMode = ColorMode.Dynamic,
            isSystemDark = isSystemDark,
            dynamicColorScheme = { useDark -> if (useDark) DynamicDark else DynamicLight },
            baselineColors = Baseline,
        )

        val expectedScheme = if (expectDark) DynamicDark else DynamicLight
        val expectedDefaults = if (expectDark) darkSparkColors() else lightSparkColors()
        assertThat(colors.surface).isEqualTo(expectedScheme.surface)
        assertThat(colors.success).isEqualTo(expectedDefaults.success)
    }

    @Test
    fun usesBaselineColorsWhenColorModeIsBaselineOrDynamicIsNotSupported(
        @TestParameter themeMode: ThemeMode,
        @TestParameter colorMode: ColorMode,
        @TestParameter isSystemDark: Boolean,
    ) {
        val colors = resolveSparkColors(
            themeMode = themeMode,
            colorMode = colorMode,
            isSystemDark = isSystemDark,
            dynamicColorScheme = if (colorMode == ColorMode.Baseline) {
                { useDark -> if (useDark) DynamicDark else DynamicLight }
            } else {
                null
            },
            baselineColors = Baseline,
        )

        assertThat(colors).isSameInstanceAs(Baseline)
    }

    @Test
    fun usesDarkThemeOnlyForDarkModeOrDarkSystem(
        @TestParameter themeMode: ThemeMode,
        @TestParameter isSystemDark: Boolean,
    ) {
        val expected = themeMode == ThemeMode.Dark || (themeMode == ThemeMode.System && isSystemDark)

        assertThat(useDarkTheme(themeMode, isSystemDark)).isEqualTo(expected)
    }

    private companion object {
        val DynamicLight = lightColorScheme(surface = Color(0xFFFFF0F0))
        val DynamicDark = darkColorScheme(surface = Color(0xFF100000))
        val Baseline = lightSparkColors()
    }
}

@RunWith(RobolectricTestRunner::class)
internal class SystemDarkTest {

    @Test
    fun readsNightModeFromConfigurationUiMode() {
        assertThat(isSystemDark(configuration(Configuration.UI_MODE_NIGHT_YES))).isTrue()
        assertThat(isSystemDark(configuration(Configuration.UI_MODE_NIGHT_NO))).isFalse()
        assertThat(isSystemDark(configuration(Configuration.UI_MODE_NIGHT_UNDEFINED))).isFalse()
    }

    private fun configuration(night: Int) = Configuration().apply {
        uiMode = Configuration.UI_MODE_TYPE_NORMAL or night
    }
}
