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
package com.adevinta.spark.catalog.themes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.saveable.Saver
import androidx.navigation.NavBackStackEntry
import com.adevinta.spark.catalog.ui.shaders.colorblindness.ColorBlindNessType
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

public data class Theme(
    val themeMode: ThemeMode = ThemeMode.System,
    val colorMode: ColorMode = ColorMode.Baseline,
    val userMode: UserMode = UserMode.Part,
    val fontScale: Float = 1.0f,
    val fontScaleMode: FontScaleMode = FontScaleMode.System,
    val textDirection: TextDirection = TextDirection.System,
    val colorBlindNessType: ColorBlindNessType = ColorBlindNessType.None,
    val colorBlindNessSeverity: Float = 0.5f,
    val navigationMode: NavigationMode = NavigationMode.Default,
    val highlightSparkComponents: Boolean = false,
    val highlightSparkTokens: Boolean = false,
    val useLegacyTheme: Boolean = false,
)

/**
 * A class for defining layout directions.
 *
 * A layout direction can be left-to-right (LTR) or right-to-left (RTL).
 */
public enum class TextDirection {
    System,

    /** Horizontal layout direction is from Left to Right. */
    LTR,

    /** Horizontal layout direction is from Right to Left. */
    RTL,
}

/**
 * Determines what color scheme should be used when viewing the catalog in the Google Material 3
 * theme.
 */
public enum class ColorMode(public val label: String) {
    /**
     * The baseline light/dark colors schemes.
     *
     * This is the default behavior, and the fallback if dynamic colors are not available on the
     * current device.
     */
    Baseline("Baseline"),

    /**
     * Build a color scheme from the dynamic colors taken from the Android System.
     *
     * If the dynamic colors are not available, the baseline color scheme will be used as a fallback.
     */
    Dynamic("Dynamic (Android 12+)"),
}

public enum class FontScaleMode(public val label: String) {
    System("System"),
    Custom("Custom"),
}

public enum class ThemeMode {
    System,
    Light,
    Dark,
}

public enum class UserMode {
    Part,
    Pro,
}

public enum class NavigationMode(
    public val enterTransition: EnterTransition = fadeIn(animationSpec = tween(700)),
    public val exitTransition: ExitTransition = fadeOut(animationSpec = tween(700)),
    public val popEnterTransition: EnterTransition = enterTransition,
    public val popExitTransition: ExitTransition = exitTransition,
    public val sizeTransform:
    (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)? =
        null,
) {
    Default,
    SharedAxisX(
        enterTransition = materialSharedAxisXIn(forward = true, slideDistance = 150),
        exitTransition = materialSharedAxisXOut(forward = true, slideDistance = 150),
        popEnterTransition = materialSharedAxisXIn(forward = false, slideDistance = 150),
        popExitTransition = materialSharedAxisXOut(forward = false, slideDistance = 150),
    ),
    SharedAxisY(
        enterTransition = materialSharedAxisYIn(forward = true, slideDistance = 150),
        exitTransition = materialSharedAxisYOut(forward = true, slideDistance = 150),
        popEnterTransition = materialSharedAxisYIn(forward = false, slideDistance = 150),
        popExitTransition = materialSharedAxisYOut(forward = false, slideDistance = 150),
    ),
    SharedAxisZ(
        enterTransition = materialSharedAxisZIn(forward = true),
        exitTransition = materialSharedAxisZOut(forward = true),
        popEnterTransition = materialSharedAxisZIn(forward = false),
        popExitTransition = materialSharedAxisZOut(forward = false),
    ),
    FadeThrough(
        enterTransition = materialFadeThroughIn(),
        exitTransition = materialFadeThroughOut(),
    ),
}

public val ThemeSaver: Saver<Theme, Map<String, Int>> = Saver(
    save = { theme ->
        mapOf(
            ThemeModeKey to theme.themeMode.ordinal,
            ColorModeKey to theme.colorMode.ordinal,
            UserModeKey to theme.userMode.ordinal,
            FontScaleKey to theme.fontScale.toInt(),
            ColorBlindTypeKey to theme.colorBlindNessType.ordinal,
            ColorBlindTypeSeverityKey to theme.colorBlindNessSeverity.toInt(),
            NavigationModeKey to theme.navigationMode.ordinal,
            TextDirectionKey to theme.textDirection.ordinal,
            HighlightSparkComponentsKey to if (theme.highlightSparkComponents) 1 else 0,
            HighlightSparkTokensKey to if (theme.highlightSparkTokens) 1 else 0,
            UseLegacyThemeKey to if (theme.useLegacyTheme) 1 else 0,
        )
    },
    restore = { map ->
        Theme(
            themeMode = ThemeMode.entries[map.getValue(ThemeModeKey)],
            colorMode = ColorMode.entries[map.getValue(ColorModeKey)],
            userMode = UserMode.entries[map.getValue(UserModeKey)],
            fontScale = map.getValue(FontScaleKey).toFloat(),
            colorBlindNessType = ColorBlindNessType.entries[map.getValue(ColorBlindTypeKey)],
            colorBlindNessSeverity = map.getValue(ColorBlindTypeSeverityKey).toFloat(),
            navigationMode = NavigationMode.entries[map.getValue(NavigationModeKey)],
            textDirection = TextDirection.entries[map.getValue(TextDirectionKey)],
            highlightSparkComponents = map.getValue(HighlightSparkComponentsKey) == 1,
            highlightSparkTokens = map.getValue(HighlightSparkTokensKey) == 1,
            useLegacyTheme = map.getValue(UseLegacyThemeKey) == 1,
        )
    },
)

public const val MinFontScale: Float = 0.4f
public const val MaxFontScale: Float = 2f

private const val ThemeModeKey = "themeMode"
private const val ColorModeKey = "colorMode"
private const val UserModeKey = "userMode"
private const val FontScaleKey = "fontScale"
private const val ColorBlindTypeKey = "colorBlindType"
private const val ColorBlindTypeSeverityKey = "colorBlindTypeSeverity"
private const val NavigationModeKey = "navigationMode"
private const val TextDirectionKey = "textDirection"
private const val HighlightSparkComponentsKey = "highlightSparkComponents"
private const val HighlightSparkTokensKey = "highlightSparkTokens"
private const val UseLegacyThemeKey = "useLegacyTheme"
