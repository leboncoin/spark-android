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

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.PaletteTokens.Avocado100
import com.adevinta.spark.tokens.PaletteTokens.Avocado300
import com.adevinta.spark.tokens.PaletteTokens.Avocado50
import com.adevinta.spark.tokens.PaletteTokens.Avocado700
import com.adevinta.spark.tokens.PaletteTokens.Avocado900
import com.adevinta.spark.tokens.PaletteTokens.Banana100
import com.adevinta.spark.tokens.PaletteTokens.Banana300
import com.adevinta.spark.tokens.PaletteTokens.Banana50
import com.adevinta.spark.tokens.PaletteTokens.Banana500
import com.adevinta.spark.tokens.PaletteTokens.Banana800
import com.adevinta.spark.tokens.PaletteTokens.Banana900
import com.adevinta.spark.tokens.PaletteTokens.Blueberry100
import com.adevinta.spark.tokens.PaletteTokens.Blueberry200
import com.adevinta.spark.tokens.PaletteTokens.Blueberry300
import com.adevinta.spark.tokens.PaletteTokens.Blueberry50
import com.adevinta.spark.tokens.PaletteTokens.Blueberry700
import com.adevinta.spark.tokens.PaletteTokens.Blueberry800
import com.adevinta.spark.tokens.PaletteTokens.Blueberry900
import com.adevinta.spark.tokens.PaletteTokens.Cherry100
import com.adevinta.spark.tokens.PaletteTokens.Cherry300
import com.adevinta.spark.tokens.PaletteTokens.Cherry50
import com.adevinta.spark.tokens.PaletteTokens.Cherry600
import com.adevinta.spark.tokens.PaletteTokens.Cherry700
import com.adevinta.spark.tokens.PaletteTokens.Cherry900
import com.adevinta.spark.tokens.PaletteTokens.Clementin100
import com.adevinta.spark.tokens.PaletteTokens.Clementin300
import com.adevinta.spark.tokens.PaletteTokens.Clementin400
import com.adevinta.spark.tokens.PaletteTokens.Clementin50
import com.adevinta.spark.tokens.PaletteTokens.Clementin500
import com.adevinta.spark.tokens.PaletteTokens.Clementin600
import com.adevinta.spark.tokens.PaletteTokens.Clementin700
import com.adevinta.spark.tokens.PaletteTokens.Clementin900
import com.adevinta.spark.tokens.PaletteTokens.NightShade100
import com.adevinta.spark.tokens.PaletteTokens.NightShade300
import com.adevinta.spark.tokens.PaletteTokens.NightShade400
import com.adevinta.spark.tokens.PaletteTokens.NightShade50
import com.adevinta.spark.tokens.PaletteTokens.NightShade500
import com.adevinta.spark.tokens.PaletteTokens.NightShade700
import com.adevinta.spark.tokens.PaletteTokens.NightShade800
import com.adevinta.spark.tokens.PaletteTokens.NightShade900
import com.adevinta.spark.tokens.PaletteTokens.Plum100
import com.adevinta.spark.tokens.PaletteTokens.Plum200
import com.adevinta.spark.tokens.PaletteTokens.Plum300
import com.adevinta.spark.tokens.PaletteTokens.Plum50
import com.adevinta.spark.tokens.PaletteTokens.Plum500
import com.adevinta.spark.tokens.PaletteTokens.Plum700
import com.adevinta.spark.tokens.PaletteTokens.Plum800
import com.adevinta.spark.tokens.PaletteTokens.Plum900
import com.materialkolor.contrast.Contrast.ratioOfYs
import com.materialkolor.ktx.darken
import com.materialkolor.ktx.from
import com.materialkolor.ktx.lighten
import com.materialkolor.ktx.toColor
import com.materialkolor.ktx.toHct
import com.materialkolor.ktx.toneColor
import com.materialkolor.palettes.TonalPalette
import com.materialkolor.utils.ColorUtils.lstarFromY
import com.materialkolor.utils.ColorUtils.yFromLstar
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.reflect.KProperty0

public fun lightSparkColors(
    contrastLevel: Float = 1.36f,
    accent: Color = Plum500.darken(contrastLevel),
    onAccent: Color = Color.White,
    accentContainer: Color = Plum100.darken(contrastLevel),
    onAccentContainer: Color = Plum800,
    accentVariant: Color = Plum700,
    onAccentVariant: Color = Color.White,
    basic: Color = Blueberry800,
    onBasic: Color = Color.White,
    basicContainer: Color = Blueberry100.darken(contrastLevel),
    onBasicContainer: Color = Blueberry900,
    main: Color = Clementin500.darken(contrastLevel),
    onMain: Color = Color.White,
    mainContainer: Color = Clementin100.darken(contrastLevel),
    onMainContainer: Color = Clementin700,
    mainVariant: Color = Clementin600,
    onMainVariant: Color = Color.White,
    support: Color = Blueberry800,
    onSupport: Color = Color.White,
    supportContainer: Color = Blueberry100.darken(contrastLevel),
    onSupportContainer: Color = Blueberry900,
    supportVariant: Color = Blueberry700,
    onSupportVariant: Color = Color.White,
    success: Color = Avocado700,
    onSuccess: Color = Color.White,
    successContainer: Color = Avocado100.darken(contrastLevel),
    onSuccessContainer: Color = Avocado700,
    alert: Color = Banana500.darken(contrastLevel),
    onAlert: Color = NightShade900,
    alertContainer: Color = Banana100.darken(contrastLevel),
    onAlertContainer: Color = Banana800,
    error: Color = Cherry600,
    onError: Color = Color.White,
    errorContainer: Color = Cherry100.darken(contrastLevel),
    onErrorContainer: Color = Cherry700,
    info: Color = Blueberry700,
    onInfo: Color = Color.White,
    infoContainer: Color = Blueberry200.darken(contrastLevel),
    onInfoContainer: Color = Blueberry700,
    neutral: Color = NightShade700,
    onNeutral: Color = Color.White,
    neutralContainer: Color = NightShade100,
    onNeutralContainer: Color = NightShade700,
    background: Color = Color.White,
    onBackground: Color = Blueberry900,
    backgroundVariant: Color = Blueberry50,
    onBackgroundVariant: Color = Blueberry900,
    surface: Color = Color.White,
    onSurface: Color = Blueberry900,
    surfaceInverse: Color = NightShade800,
    onSurfaceInverse: Color = Color.White,
    outline: Color = NightShade400,
    outlineHigh: Color = NightShade900,
    scrim: Color = Color.Black,
    dimContent1: Float = .72f,
    dimContent2: Float = .56f,
    dimContent3: Float = .40f,
    dimContent4: Float = .16f,
    dimContent5: Float = .08f,
): SparkColors = SparkColors(
    accent = accent,
    onAccent = onAccent,
    accentContainer = accentContainer,
    onAccentContainer = onAccentContainer,
    accentVariant = accentVariant,
    onAccentVariant = onAccentVariant,
    basic = basic,
    onBasic = onBasic,
    basicContainer = basicContainer,
    onBasicContainer = onBasicContainer,
    main = main,
    onMain = onMain,
    mainContainer = mainContainer,
    onMainContainer = onMainContainer,
    mainVariant = mainVariant,
    onMainVariant = onMainVariant,
    support = support,
    onSupport = onSupport,
    supportContainer = supportContainer,
    onSupportContainer = onSupportContainer,
    supportVariant = supportVariant,
    onSupportVariant = onSupportVariant,
    success = success,
    onSuccess = onSuccess,
    successContainer = successContainer,
    onSuccessContainer = onSuccessContainer,
    alert = alert,
    onAlert = onAlert,
    alertContainer = alertContainer,
    onAlertContainer = onAlertContainer,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    info = info,
    onInfo = onInfo,
    infoContainer = infoContainer,
    onInfoContainer = onInfoContainer,
    neutral = neutral,
    onNeutral = onNeutral,
    neutralContainer = neutralContainer,
    onNeutralContainer = onNeutralContainer,
    background = background,
    onBackground = onBackground,
    backgroundVariant = backgroundVariant,
    onBackgroundVariant = onBackgroundVariant,
    surface = surface,
    onSurface = onSurface,
    surfaceInverse = surfaceInverse,
    onSurfaceInverse = onSurfaceInverse,
    surfaceTint = surface,
    outline = outline,
    outlineHigh = outlineHigh,
    scrim = scrim,
    dimContent1 = dimContent1,
    dimContent2 = dimContent2,
    dimContent3 = dimContent3,
    dimContent4 = dimContent4,
    dimContent5 = dimContent5,
)

public fun darkSparkColors(
    contrastLevel: Float = 1.5f,
    accent: Color = Plum200,
    onAccent: Color = Plum900,
    accentContainer: Color = Plum700.lighten(contrastLevel),
    onAccentContainer: Color = Plum50,
    accentVariant: Color = Plum300,
    onAccentVariant: Color = Plum900,
    basic: Color = Blueberry200,
    onBasic: Color = Blueberry900,
    basicContainer: Color = Blueberry800.lighten(contrastLevel),
    onBasicContainer: Color = Blueberry50,
    main: Color = Clementin400,
    onMain: Color = Clementin900,
    mainContainer: Color = Clementin700.lighten(contrastLevel),
    onMainContainer: Color = Clementin50,
    mainVariant: Color = Clementin300,
    onMainVariant: Color = Clementin900,
    support: Color = Blueberry200,
    onSupport: Color = Blueberry900,
    supportContainer: Color = Blueberry800.lighten(contrastLevel),
    onSupportContainer: Color = Blueberry50,
    supportVariant: Color = Blueberry100,
    onSupportVariant: Color = Blueberry900,
    success: Color = Avocado300,
    onSuccess: Color = Avocado900,
    successContainer: Color = Avocado700.lighten(contrastLevel),
    onSuccessContainer: Color = Avocado50,
    alert: Color = Banana300,
    onAlert: Color = Banana900,
    alertContainer: Color = Banana800.lighten(contrastLevel),
    onAlertContainer: Color = Banana50,
    error: Color = Cherry300,
    onError: Color = Cherry900,
    errorContainer: Color = Cherry700.lighten(contrastLevel),
    onErrorContainer: Color = Cherry50,
    info: Color = Blueberry300,
    onInfo: Color = Blueberry900,
    infoContainer: Color = Blueberry700.lighten(contrastLevel),
    onInfoContainer: Color = Blueberry50,
    neutral: Color = NightShade300,
    onNeutral: Color = NightShade900,
    neutralContainer: Color = NightShade700.lighten(contrastLevel),
    onNeutralContainer: Color = NightShade50,
    background: Color = NightShade900,
    onBackground: Color = NightShade50,
    backgroundVariant: Color = Color.Black,
    onBackgroundVariant: Color = NightShade50,
    surface: Color = NightShade900,
    onSurface: Color = NightShade50,
    surfaceInverse: Color = NightShade50,
    onSurfaceInverse: Color = NightShade800,
    surfaceTint: Color = Color.White,
    outline: Color = NightShade500,
    outlineHigh: Color = NightShade100,
    scrim: Color = Color.Black,
    dimContent1: Float = .72f,
    dimContent2: Float = .56f,
    dimContent3: Float = .40f,
    dimContent4: Float = .16f,
    dimContent5: Float = .08f,
): SparkColors = SparkColors(
    accent = accent,
    onAccent = onAccent,
    accentContainer = accentContainer,
    onAccentContainer = onAccentContainer,
    accentVariant = accentVariant,
    onAccentVariant = onAccentVariant,
    basic = basic,
    onBasic = onBasic,
    basicContainer = basicContainer,
    onBasicContainer = onBasicContainer,
    main = main,
    onMain = onMain,
    mainContainer = mainContainer,
    onMainContainer = onMainContainer,
    mainVariant = mainVariant,
    onMainVariant = onMainVariant,
    support = support,
    onSupport = onSupport,
    supportContainer = supportContainer,
    onSupportContainer = onSupportContainer,
    supportVariant = supportVariant,
    onSupportVariant = onSupportVariant,
    success = success,
    onSuccess = onSuccess,
    successContainer = successContainer,
    onSuccessContainer = onSuccessContainer,
    alert = alert,
    onAlert = onAlert,
    alertContainer = alertContainer,
    onAlertContainer = onAlertContainer,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    info = info,
    onInfo = onInfo,
    infoContainer = infoContainer,
    onInfoContainer = onInfoContainer,
    neutral = neutral,
    onNeutral = onNeutral,
    neutralContainer = neutralContainer,
    onNeutralContainer = onNeutralContainer,
    background = background,
    onBackground = onBackground,
    backgroundVariant = backgroundVariant,
    onBackgroundVariant = onBackgroundVariant,
    surface = surface,
    onSurface = onSurface,
    surfaceInverse = surfaceInverse,
    onSurfaceInverse = onSurfaceInverse,
    surfaceTint = surfaceTint,
    outline = outline,
    outlineHigh = outlineHigh,
    scrim = scrim,
    dimContent1 = dimContent1,
    dimContent2 = dimContent2,
    dimContent3 = dimContent3,
    dimContent4 = dimContent4,
    dimContent5 = dimContent5,
)

/**
 * A color scheme holds all the named color parameters for a [SparkTheme] highly inspired by Material 3 ColorScheme.
 *
 * SparkColors are designed to be harmonious, ensure accessible text, and distinguish UI
 * elements and surfaces from one another. There are two built-in baseline schemes,
 * [lightSparkColors] and a [darkSparkColors], that can be used as-is or customized.
 *
 * To learn more about colors, see [Material Design colors](https://m3.material.io/styles/color/overview) and
 * [Spark colors](https://spark.adevinta.com/1186e1705/p/0879a9-colors/b/27d7a3).
 *
 * @property accent
 * @property onAccent
 * @property accentContainer
 * @property onAccentContainer
 * @property accentVariant
 * @property onAccentVariant
 * @property basic
 * @property onBasic
 * @property basicContainer
 * @property onBasicContainer
 * @property main The main color is the color displayed most frequently across your app’s
 * screens and components.
 * @property onMain Color used for text and icons displayed on top of the main color.
 * @property mainContainer The preferred tonal color of containers.
 * @property onMainContainer The color (and state variants) that should be used for content on
 * top of [mainContainer].
 * @property mainVariant Darker variation of [main] color.
 * @property onMainVariant Color used for text and icons displayed on top of the [mainVariant] color.
 * @property support The support color provides more ways to accent and distinguish your
 * product. Support colors are best for:
 * - Floating action buttons
 * - Selection controls, like checkboxes and radio buttons
 * - Highlighting selected text
 * - Links and headlines
 * @property onSupport Color used for text and icons displayed on top of the support color.
 * @property supportContainer A tonal color to be used in containers.
 * @property onSupportContainer The color (and state variants) that should be used for content on
 * top of [supportContainer].
 * @property supportVariant Darker variation of [support] color.
 * @property onSupportVariant Color used for text and icons displayed on top of the [supportVariant] color.
 * @property success An emphasis color used to provide a positive feedback.
 * @property onSuccess Color used for text and icons displayed on top of the [success] color.
 * @property successContainer The preferred tonal color of success containers.
 * @property onSuccessContainer Color used for text and icons displayed on top of the [successContainer] color.
 * @property alert An emphasis color used to provide a warning feedback.
 * @property onAlert Color used for text and icons displayed on top of the [alert] color.
 * @property alertContainer The preferred tonal color of warning containers.
 * @property onAlertContainer Color used for text and icons displayed on top of the [alertContainer] color.
 * @property error The error color is used to indicate errors in components, such as invalid text in
 * a text field.
 * @property onError Color used for text and icons displayed on top of the error color.
 * @property errorContainer The preferred tonal color of error containers.
 * @property onErrorContainer The color (and state variants) that should be used for content on
 * top of [errorContainer].
 * @property info An emphasis color used to provide an informative feedback.
 * @property onInfo Color used for text and icons displayed on top of the [info] color.
 * @property infoContainer The preferred tonal color of info containers.
 * @property onInfoContainer Color used for text and icons displayed on top of the [infoContainer] color.
 * @property neutral An emphasis color used to provide a neutral feedback.
 * @property onNeutral Color used for text and icons displayed on top of the [neutral] color.
 * @property neutralContainer The preferred tonal color of neutral containers.
 * @property onNeutralContainer Color used for text and icons displayed on top of the [neutralContainer] color.
 * @property background The background color that appears behind scrollable content.
 * @property onBackground Color used for text and icons displayed on top of the background color.
 * @property backgroundVariant The background color that appears behind scrollable content.
 * @property onBackgroundVariant Color used for text and icons displayed on top of the background color.
 * @property surface The surface color that affect surfaces of components, such as cards, sheets,
 * and menus.
 * @property onSurface Color used for text and icons displayed on top of the surface color.
 * @property surfaceInverse
 * @property onSurfaceInverse
 * @property surfaceTint This color will be used by components that apply tonal elevation and is
 * applied on top of [surface]. The higher the elevation the more this color is used.
 * @property outline Subtle color used for boundaries. Outline color role adds contrast for
 * accessibility purposes.
 * @property outlineHigh Utility color used for boundaries for decorative elements when strong
 * contrast is not required.
 * @property scrim Color of a scrim that obscures content. On Android platforms, the scrim color
 * and opacity is automatically handled by the system UI.
 * @property dim1 An alpha used to apply a medium emphasis to text.
 * @property dim2 An alpha used to apply a medium emphasis to icons.
 * @property dim3 An alpha used to apply a disabled emphasis to all components.
 * @property dim4 An alpha used to show a low element.
 * @property dim5 An alpha used to apply a pressed/ripple visual but should not be used on Android.
 */
@Stable
public class SparkColors(
    accent: Color,
    onAccent: Color,
    accentContainer: Color,
    onAccentContainer: Color,
    accentVariant: Color,
    onAccentVariant: Color,
    basic: Color,
    onBasic: Color,
    basicContainer: Color,
    onBasicContainer: Color,
    main: Color,
    onMain: Color,
    mainContainer: Color,
    onMainContainer: Color,
    mainVariant: Color,
    onMainVariant: Color,
    support: Color,
    onSupport: Color,
    supportContainer: Color,
    onSupportContainer: Color,
    supportVariant: Color,
    onSupportVariant: Color,
    success: Color,
    onSuccess: Color,
    successContainer: Color,
    onSuccessContainer: Color,
    alert: Color,
    onAlert: Color,
    alertContainer: Color,
    onAlertContainer: Color,
    error: Color,
    onError: Color,
    errorContainer: Color,
    onErrorContainer: Color,
    info: Color,
    onInfo: Color,
    infoContainer: Color,
    onInfoContainer: Color,
    neutral: Color,
    onNeutral: Color,
    neutralContainer: Color,
    onNeutralContainer: Color,
    background: Color,
    onBackground: Color,
    backgroundVariant: Color,
    onBackgroundVariant: Color,
    surface: Color,
    onSurface: Color,
    surfaceInverse: Color,
    onSurfaceInverse: Color,
    surfaceTint: Color,
    outline: Color,
    outlineHigh: Color,
    scrim: Color,
    dimContent1: Float,
    dimContent2: Float,
    dimContent3: Float,
    dimContent4: Float,
    dimContent5: Float,
) {
    @Order(2)
    public var accent: Color by mutableStateOf(accent, structuralEqualityPolicy())
        internal set
    public var onAccent: Color by mutableStateOf(onAccent, structuralEqualityPolicy())
        internal set
    public var accentContainer: Color by mutableStateOf(accentContainer, structuralEqualityPolicy())
        internal set
    public var onAccentContainer: Color by mutableStateOf(onAccentContainer, structuralEqualityPolicy())
        internal set
    public var accentVariant: Color by mutableStateOf(accentVariant, structuralEqualityPolicy())
        internal set
    public var onAccentVariant: Color by mutableStateOf(onAccentVariant, structuralEqualityPolicy())
        internal set

    @Order(3)
    public var basic: Color by mutableStateOf(basic, structuralEqualityPolicy())
        internal set
    public var onBasic: Color by mutableStateOf(onBasic, structuralEqualityPolicy())
        internal set
    public var basicContainer: Color by mutableStateOf(basicContainer, structuralEqualityPolicy())
        internal set
    public var onBasicContainer: Color by mutableStateOf(onBasicContainer, structuralEqualityPolicy())
        internal set

    @Order(0)
    public var main: Color by mutableStateOf(main, structuralEqualityPolicy())
        internal set
    public var onMain: Color by mutableStateOf(onMain, structuralEqualityPolicy())
        internal set
    public var mainContainer: Color by mutableStateOf(mainContainer, structuralEqualityPolicy())
        internal set
    public var onMainContainer: Color by mutableStateOf(onMainContainer, structuralEqualityPolicy())
        internal set
    public var mainVariant: Color by mutableStateOf(mainVariant, structuralEqualityPolicy())
        internal set
    public var onMainVariant: Color by mutableStateOf(onMainVariant, structuralEqualityPolicy())
        internal set

    @Order(1)
    public var support: Color by mutableStateOf(support, structuralEqualityPolicy())
        internal set
    public var onSupport: Color by mutableStateOf(onSupport, structuralEqualityPolicy())
        internal set
    public var supportContainer: Color by mutableStateOf(supportContainer, structuralEqualityPolicy())
        internal set
    public var onSupportContainer: Color by mutableStateOf(onSupportContainer, structuralEqualityPolicy())
        internal set
    public var supportVariant: Color by mutableStateOf(supportVariant, structuralEqualityPolicy())
        internal set
    public var onSupportVariant: Color by mutableStateOf(onSupportVariant, structuralEqualityPolicy())
        internal set

    @Order(4)
    public var background: Color by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    public var onBackground: Color by mutableStateOf(onBackground, structuralEqualityPolicy())
        internal set
    public var backgroundVariant: Color by mutableStateOf(backgroundVariant, structuralEqualityPolicy())
        internal set
    public var onBackgroundVariant: Color by mutableStateOf(onBackgroundVariant, structuralEqualityPolicy())
        internal set

    @Order(5)
    public var surface: Color by mutableStateOf(surface, structuralEqualityPolicy())
        internal set
    public var onSurface: Color by mutableStateOf(onSurface, structuralEqualityPolicy())
        internal set
    public var surfaceInverse: Color by mutableStateOf(surfaceInverse, structuralEqualityPolicy())
        internal set
    public var onSurfaceInverse: Color by mutableStateOf(onSurfaceInverse, structuralEqualityPolicy())
        internal set
    internal var surfaceTint: Color by mutableStateOf(surfaceTint, structuralEqualityPolicy())

    @Order(6)
    public var outline: Color by mutableStateOf(outline, structuralEqualityPolicy())
        internal set
    public var outlineHigh: Color by mutableStateOf(outlineHigh, structuralEqualityPolicy())
        internal set
    public var scrim: Color by mutableStateOf(scrim, structuralEqualityPolicy())
        internal set

    @Order(7)
    public var success: Color by mutableStateOf(success, structuralEqualityPolicy())
        internal set
    public var onSuccess: Color by mutableStateOf(onSuccess, structuralEqualityPolicy())
        internal set
    public var successContainer: Color by mutableStateOf(successContainer, structuralEqualityPolicy())
        internal set
    public var onSuccessContainer: Color by mutableStateOf(onSuccessContainer, structuralEqualityPolicy())
        internal set

    @Order(8)
    public var alert: Color by mutableStateOf(alert, structuralEqualityPolicy())
        internal set
    public var onAlert: Color by mutableStateOf(onAlert, structuralEqualityPolicy())
        internal set
    public var alertContainer: Color by mutableStateOf(alertContainer, structuralEqualityPolicy())
        internal set
    public var onAlertContainer: Color by mutableStateOf(onAlertContainer, structuralEqualityPolicy())
        internal set

    @Order(9)
    public var error: Color by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    public var onError: Color by mutableStateOf(onError, structuralEqualityPolicy())
        internal set
    public var errorContainer: Color by mutableStateOf(errorContainer, structuralEqualityPolicy())
        internal set
    public var onErrorContainer: Color by mutableStateOf(onErrorContainer, structuralEqualityPolicy())
        internal set

    @Order(10)
    public var info: Color by mutableStateOf(info, structuralEqualityPolicy())
        internal set
    public var onInfo: Color by mutableStateOf(onInfo, structuralEqualityPolicy())
        internal set
    public var infoContainer: Color by mutableStateOf(infoContainer, structuralEqualityPolicy())
        internal set
    public var onInfoContainer: Color by mutableStateOf(onInfoContainer, structuralEqualityPolicy())
        internal set

    @Order(11)
    public var neutral: Color by mutableStateOf(neutral, structuralEqualityPolicy())
        internal set
    public var onNeutral: Color by mutableStateOf(onNeutral, structuralEqualityPolicy())
        internal set
    public var neutralContainer: Color by mutableStateOf(neutralContainer, structuralEqualityPolicy())
        internal set
    public var onNeutralContainer: Color by mutableStateOf(onNeutralContainer, structuralEqualityPolicy())
        internal set

    public var dim1: Float by mutableFloatStateOf(dimContent1)
        internal set
    public var dim2: Float by mutableFloatStateOf(dimContent2)
        internal set
    public var dim3: Float by mutableFloatStateOf(dimContent3)
        internal set
    public var dim4: Float by mutableFloatStateOf(dimContent4)
        internal set
    public var dim5: Float by mutableFloatStateOf(dimContent5)
        internal set

    /**
     * Returns a copy of this Colors, optionally overriding some of the values.
     */
    public fun copy(
        accent: Color = this.accent,
        onAccent: Color = this.onAccent,
        accentContainer: Color = this.accentContainer,
        onAccentContainer: Color = this.onAccentContainer,
        accentVariant: Color = this.accentVariant,
        onAccentVariant: Color = this.onAccentVariant,
        basic: Color = this.basic,
        onBasic: Color = this.onBasic,
        basicContainer: Color = this.basicContainer,
        onBasicContainer: Color = this.onBasicContainer,
        main: Color = this.main,
        onMain: Color = this.onMain,
        mainContainer: Color = this.mainContainer,
        onMainContainer: Color = this.onMainContainer,
        mainVariant: Color = this.mainVariant,
        onMainVariant: Color = this.onMainVariant,
        support: Color = this.support,
        onSupport: Color = this.onSupport,
        supportContainer: Color = this.supportContainer,
        onSupportContainer: Color = this.onSupportContainer,
        supportVariant: Color = this.supportVariant,
        onSupportVariant: Color = this.onSupportVariant,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        backgroundVariant: Color = this.backgroundVariant,
        onBackgroundVariant: Color = this.onBackgroundVariant,
        surface: Color = this.surface,
        onSurface: Color = this.onSurface,
        surfaceInverse: Color = this.surfaceInverse,
        onSurfaceInverse: Color = this.onSurfaceInverse,
        surfaceTint: Color = this.surfaceTint,
        outline: Color = this.outline,
        outlineHigh: Color = this.outlineHigh,
        scrim: Color = this.scrim,
        success: Color = this.success,
        onSuccess: Color = this.onSuccess,
        successContainer: Color = this.successContainer,
        onSuccessContainer: Color = this.onSuccessContainer,
        alert: Color = this.alert,
        onAlert: Color = this.onAlert,
        alertContainer: Color = this.alertContainer,
        onAlertContainer: Color = this.onAlertContainer,
        error: Color = this.error,
        onError: Color = this.onError,
        errorContainer: Color = this.errorContainer,
        onErrorContainer: Color = this.onErrorContainer,
        info: Color = this.info,
        onInfo: Color = this.onInfo,
        infoContainer: Color = this.infoContainer,
        onInfoContainer: Color = this.onInfoContainer,
        neutral: Color = this.neutral,
        onNeutral: Color = this.onNeutral,
        neutralContainer: Color = this.neutralContainer,
        onNeutralContainer: Color = this.onNeutralContainer,
        dimContent1: Float = this.dim1,
        dimContent2: Float = this.dim2,
        dimContent3: Float = this.dim3,
        dimContent4: Float = this.dim4,
        dimContent5: Float = this.dim5,
    ): SparkColors = SparkColors(
        accent = accent,
        onAccent = onAccent,
        accentContainer = accentContainer,
        onAccentContainer = onAccentContainer,
        accentVariant = accentVariant,
        onAccentVariant = onAccentVariant,
        basic = basic,
        onBasic = onBasic,
        basicContainer = basicContainer,
        onBasicContainer = onBasicContainer,
        main = main,
        onMain = onMain,
        mainContainer = mainContainer,
        onMainContainer = onMainContainer,
        mainVariant = mainVariant,
        onMainVariant = onMainVariant,
        support = support,
        onSupport = onSupport,
        supportContainer = supportContainer,
        onSupportContainer = onSupportContainer,
        supportVariant = supportVariant,
        onSupportVariant = onSupportVariant,
        background = background,
        onBackground = onBackground,
        backgroundVariant = backgroundVariant,
        onBackgroundVariant = onBackgroundVariant,
        surface = surface,
        onSurface = onSurface,
        surfaceInverse = surfaceInverse,
        onSurfaceInverse = onSurfaceInverse,
        surfaceTint = surfaceTint,
        outline = outline,
        outlineHigh = outlineHigh,
        scrim = scrim,
        success = success,
        onSuccess = onSuccess,
        successContainer = successContainer,
        onSuccessContainer = onSuccessContainer,
        alert = alert,
        onAlert = onAlert,
        alertContainer = alertContainer,
        onAlertContainer = onAlertContainer,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        info = info,
        onInfo = onInfo,
        infoContainer = infoContainer,
        onInfoContainer = onInfoContainer,
        neutral = neutral,
        onNeutral = onNeutral,
        neutralContainer = neutralContainer,
        onNeutralContainer = onNeutralContainer,
        dimContent1 = dimContent1,
        dimContent2 = dimContent2,
        dimContent3 = dimContent3,
        dimContent4 = dimContent4,
        dimContent5 = dimContent5,
    )

    override fun toString(): String = buildString {
        append("SparkColors(")
        append("accent=$accent, ")
        append("onAccent=$onAccent, ")
        append("accentContainer=$accentContainer, ")
        append("onAccentContainer=$onAccentContainer, ")
        append("accentVariant=$accentVariant, ")
        append("onAccentVariant=$onAccentVariant, ")
        append("basic=$basic, ")
        append("onBasic=$onBasic, ")
        append("basicContainer=$basicContainer, ")
        append("onBasicContainer=$onBasicContainer, ")
        append("main=$main, ")
        append("onMain=$onMain, ")
        append("mainContainer=$mainContainer, ")
        append("onMainContainer=$onMainContainer, ")
        append("mainVariant=$mainVariant, ")
        append("onMainVariant=$onMainVariant, ")
        append("support=$support, ")
        append("onSupport=$onSupport, ")
        append("supportContainer=$supportContainer, ")
        append("onSupportContainer=$onSupportContainer, ")
        append("supportVariant=$supportVariant, ")
        append("onSupportVariant=$onSupportVariant, ")
        append("background=$background, ")
        append("onBackground=$onBackground, ")
        append("backgroundVariant=$backgroundVariant, ")
        append("onBackgroundVariant=$onBackgroundVariant, ")
        append("surface=$surface, ")
        append("onSurface=$onSurface, ")
        append("surfaceTint=$surfaceTint, ")
        append("outline=$outline, ")
        append("outlineHigh=$outlineHigh, ")
        append("scrim=$scrim, ")
        append("alert=$alert, ")
        append("onAlert=$onAlert, ")
        append("alertContainer=$alertContainer, ")
        append("onAlertContainer=$onAlertContainer, ")
        append("error=$error, ")
        append("onError=$onError, ")
        append("errorContainer=$errorContainer, ")
        append("onErrorContainer=$onErrorContainer, ")
        append("info=$info, ")
        append("onInfo=$onInfo, ")
        append("infoContainer=$infoContainer, ")
        append("onInfoContainer=$onInfoContainer, ")
        append("neutral=$neutral, ")
        append("onNeutral=$onNeutral, ")
        append("neutralContainer=$neutralContainer, ")
        append("onNeutralContainer=$onNeutralContainer, ")
        append("dim1=$dim1, ")
        append("dim2=$dim2, ")
        append("dim3=$dim3, ")
        append("dim4=$dim4, ")
        append("dim5=$dim5, ")
        append(")")
    }
}

/**
 * Converts a [SparkColors] instance to a Material 3 [ColorScheme].
 *
 * This function maps the Spark color properties to their corresponding Material 3 color roles.
 * Note: Some Material 3 color roles might not have a direct equivalent in SparkColors and are
 * mapped to the closest available Spark color. For example, [ColorScheme.tertiary] colors
 * are mapped to [SparkColors.support] colors, and various surface container colors are mapped
 * to the base surface color.
 *
 * @return A [ColorScheme] representing the Material 3 color scheme derived from the
 * [SparkColors] instance.
 */
public fun SparkColors.asMaterial3Colors(): ColorScheme = ColorScheme(
    primary = main,
    onPrimary = onMain,
    primaryContainer = mainContainer,
    onPrimaryContainer = onMainContainer,
    inversePrimary = mainVariant,
    secondary = support,
    onSecondary = onSupport,
    secondaryContainer = supportContainer,
    onSecondaryContainer = onSupportContainer,
    tertiary = support,
    onTertiary = onSupport,
    tertiaryContainer = supportContainer,
    onTertiaryContainer = onSupportContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = backgroundVariant,
    onSurfaceVariant = onBackgroundVariant,
    surfaceTint = surfaceTint,
    inverseSurface = surfaceInverse,
    inverseOnSurface = onSurfaceInverse,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = outlineHigh,
    scrim = scrim,
    surfaceBright = surface,
    surfaceDim = surface,
    surfaceContainer = surface,
    surfaceContainerHigh = surface,
    surfaceContainerHighest = surface,
    surfaceContainerLow = surface,
    surfaceContainerLowest = surface,
)

/**
 * Converts a Material [ColorScheme] to a [SparkColors] instance.
 *
 * This function adapts the color values from a Material Design ColorScheme
 * to the structure and naming conventions used by SparkColors.
 * The following tokens [SparkColors.mainVariant], [SparkColors.accentVariant],
 * [SparkColors.supportVariant] and their on counterparts have no equivalent in Material so
 * their are juste derivative of their principal token with a different tone that needs to
 * be different for each (90/10 or 10/99).
 * It handles both light and dark themes by adjusting the color tones accordingly.
 *
 * @param useDark Whether to generate colors for a dark theme as the tone used for
 * variants is different in a dark theme
 *
 * @return A [SparkColors] populated with color values derived from the provided [ColorScheme].
 */
public fun ColorScheme.asSparkColors(useDark: Boolean): SparkColors = if (useDark) {
    darkSparkColors(
        main = primary,
        onMain = onPrimary,
        mainContainer = primaryContainer,
        onMainContainer = onPrimaryContainer,
        mainVariant = primary.adjustColorToMaterialTone(90f),
        onMainVariant = primary.adjustColorToMaterialTone(10f),
        accent = secondary,
        onAccent = onSecondary,
        accentContainer = secondaryContainer,
        onAccentContainer = onSecondaryContainer,
        accentVariant = secondary.adjustColorToMaterialTone(90f),
        onAccentVariant = secondary.adjustColorToMaterialTone(10f),
        support = tertiary,
        onSupport = onTertiary,
        supportContainer = tertiaryContainer,
        onSupportContainer = onTertiaryContainer,
        supportVariant = tertiary.adjustColorToMaterialTone(90f),
        onSupportVariant = tertiary.adjustColorToMaterialTone(10f),
        basic = tertiary,
        onBasic = onTertiary,
        basicContainer = tertiaryContainer,
        onBasicContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        backgroundVariant = surfaceVariant,
        onBackgroundVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        surfaceInverse = inverseSurface,
        onSurfaceInverse = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineHigh = outlineVariant,
        scrim = scrim,
    )
} else {
    lightSparkColors(
        main = primary,
        onMain = onPrimary,
        mainContainer = primaryContainer,
        onMainContainer = onPrimaryContainer,
        mainVariant = primary.adjustColorToMaterialTone(10f),
        onMainVariant = primary.adjustColorToMaterialTone(99f),
        accent = secondary,
        onAccent = onSecondary,
        accentContainer = secondaryContainer,
        onAccentContainer = onSecondaryContainer,
        accentVariant = secondary.adjustColorToMaterialTone(10f),
        onAccentVariant = secondary.adjustColorToMaterialTone(99f),
        support = tertiary,
        onSupport = onTertiary,
        supportContainer = tertiaryContainer,
        onSupportContainer = onTertiaryContainer,
        supportVariant = tertiary.adjustColorToMaterialTone(10f),
        onSupportVariant = tertiary.adjustColorToMaterialTone(99f),
        basic = tertiary,
        onBasic = onTertiary,
        basicContainer = tertiaryContainer,
        onBasicContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        backgroundVariant = surfaceVariant,
        onBackgroundVariant = onSurfaceVariant,
        surfaceInverse = inverseSurface,
        onSurfaceInverse = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineHigh = outlineVariant,
        scrim = scrim,
    )
}

private fun Color.adjustColorToMaterialTone(tone: Float): Color {
    val palette = TonalPalette.from(this)
    return palette.toneColor(tone.roundToInt())
}


/**
 * The Material color system contains pairs of colors that are typically used for the background
 * and content color inside a component. For example, a [ButtonFilled] typically uses `main` for its
 * background, and `onMain` for the color of its content (usually text or iconography).
 *
 * This function tries to match the provided [backgroundColor] to a 'background' color in this
 * [SparkColors], and then will return the corresponding color used for content. For example, when
 * [backgroundColor] is [SparkColors.main], this will return [SparkColors.onMain].
 *
 * If [backgroundColor] does not match a background color in the theme, this will return
 * [Color.Unspecified].
 *
 * @return the matching content color for [backgroundColor]. If [backgroundColor] is not present in
 * the theme's [SparkColors], then returns [Color.Unspecified].
 *
 * @see contentColorFor
 */
public fun SparkColors.contentColorFor(backgroundColor: Color): Color = when (backgroundColor) {
    accent -> onAccent
    accentContainer -> onAccentContainer
    accentVariant -> onAccentVariant
    basic -> onBasic
    basicContainer -> onBasicContainer
    main -> onMain
    mainContainer -> onMainContainer
    mainVariant -> onMainVariant
    support -> onSupport
    supportContainer -> onSupportContainer
    supportVariant -> onSupportVariant
    background -> onBackground
    backgroundVariant -> onBackgroundVariant
    surface -> onSurface
    surfaceInverse -> onSurfaceInverse
    success -> onSuccess
    successContainer -> onSuccessContainer
    alert -> onAlert
    alertContainer -> onAlertContainer
    error -> onError
    errorContainer -> onErrorContainer
    info -> onInfo
    infoContainer -> onInfoContainer
    neutral -> onNeutral
    neutralContainer -> onNeutralContainer
    else -> Color.Unspecified
}

/**
 * The Material color system contains pairs of colors that are typically used for the background
 * and content color inside a component. For example, a [ButtonFilled] typically uses `main` for its
 * background, and `onMain` for the color of its content (usually text or iconography).
 *
 * This function tries to match the provided [backgroundColor] to a 'background' color in this
 * [SparkColors], and then will return the corresponding color used for content. For example, when
 * [backgroundColor] is [SparkColors.main], this will return [SparkColors.onMain].
 *
 * If [backgroundColor] does not match a background color in the theme, this will return
 * the current value of [LocalContentColor] as a best-effort color.
 *
 * @return the matching content color for [backgroundColor]. If [backgroundColor] is not present in
 * the theme's [Colors], then returns the current value of [LocalContentColor].
 *
 * @see SparkColors.contentColorFor
 */
@Composable
@ReadOnlyComposable
public fun contentColorFor(
    backgroundColor: Color,
): Color = SparkTheme.colors.contentColorFor(backgroundColor).takeOrElse {
    LocalContentColor.current
}

/**
 * Extension property to get a [Color] with dim1(a medium emphasis to text) applied
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.dim1: Color
    @Composable get() = this.copy(alpha = SparkTheme.colors.dim1)

/**
 * Extension property to get a [Color] with dim2(a medium emphasis to icons) applied
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.dim2: Color
    @Composable get() = this.copy(alpha = SparkTheme.colors.dim2)

/**
 * Extension property to get a [Color] with dim3(disabled emphasis to all components) applied
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.dim3: Color
    @Composable get() = this.copy(alpha = SparkTheme.colors.dim3)

/**
 * Extension property to get a [Color] with dim4(low element) applied
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.dim4: Color
    @Composable get() = this.copy(alpha = SparkTheme.colors.dim4)

/**
 * Extension property to get a [Color] with dim5(pressed/ripple visual but should not be used on Android) applied
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.dim5: Color
    @Composable get() = this.copy(alpha = SparkTheme.colors.dim5)

/**
 * Extension property to get a [Color] with dim3(disabled element) applied composite over SparkTheme.colors.surface
 * to prevent the color being transparent
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.disabled: Color
    @Composable get() = this.dim3.compositeOver(SparkTheme.colors.surface)

/**
 * Extension property to get a [Color] that apply an alpha of zero to the color.
 * This is useful when you want to animate fro ma transparent color to a colored one
 * since using  [Color.Transparent] will start with a black background.
 */
@get:SuppressLint("ComposeUnstableReceiver") // https://github.com/slackhq/compose-lints/issues/326
public val Color.transparent: Color
    @Composable get() = this.copy(alpha = 0f)

/**
 * Updates the internal values of the given [SparkColors] with values from the [other] [SparkColors]. This
 * allows efficiently updating a subset of [SparkColors], without recomposing every composable that
 * consumes values from [LocalSparkColors].
 *
 * Because [SparkColors] is very wide-reaching, and used by many expensive composables in the
 * hierarchy, providing a new value to [LocalSparkColors] causes every composable consuming
 * [LocalSparkColors] to recompose, which is prohibitively expensive in cases such as animating one
 * color in the theme. Instead, [SparkColors] is internally backed by [mutableStateOf], and this
 * function mutates the internal state of [this] to match values in [other]. This means that any
 * changes will mutate the internal state of [this], and only cause composables that are reading
 * the specific changed value to recompose.
 */
internal fun SparkColors.updateColorsFrom(other: SparkColors) {
    accent = other.accent
    onAccent = other.onAccent
    accentContainer = other.accentContainer
    onAccentContainer = other.onAccentContainer
    accentVariant = other.accentVariant
    onAccentVariant = other.onAccentVariant
    basic = other.basic
    onBasic = other.onBasic
    basicContainer = other.basicContainer
    onBasicContainer = other.onBasicContainer
    main = other.main
    onMain = other.onMain
    mainContainer = other.mainContainer
    onMainContainer = other.onMainContainer
    mainVariant = other.mainVariant
    onMainVariant = other.onMainVariant
    support = other.support
    onSupport = other.onSupport
    supportContainer = other.supportContainer
    onSupportContainer = other.onSupportContainer
    supportVariant = other.supportVariant
    onSupportVariant = other.onSupportVariant
    background = other.background
    onBackground = other.onBackground
    backgroundVariant = other.backgroundVariant
    onBackgroundVariant = other.onBackgroundVariant
    surface = other.surface
    onSurface = other.onSurface
    surfaceInverse = other.surfaceInverse
    onSurfaceInverse = other.onSurfaceInverse
    surfaceTint = other.surfaceTint
    outline = other.outline
    outlineHigh = other.outlineHigh
    scrim = other.scrim
    success = other.success
    onSuccess = other.onSuccess
    successContainer = other.successContainer
    onSuccessContainer = other.onSuccessContainer
    alert = other.alert
    onAlert = other.onAlert
    alertContainer = other.alertContainer
    onAlertContainer = other.onAlertContainer
    error = other.error
    onError = other.onError
    errorContainer = other.errorContainer
    onErrorContainer = other.onErrorContainer
    info = other.info
    onInfo = other.onInfo
    infoContainer = other.infoContainer
    onInfoContainer = other.onInfoContainer
    neutral = other.neutral
    onNeutral = other.onNeutral
    neutralContainer = other.neutralContainer
    onNeutralContainer = other.onNeutralContainer
    dim1 = other.dim1
    dim2 = other.dim2
    dim3 = other.dim3
    dim4 = other.dim4
    dim5 = other.dim5
}

/**
 * Returns the new background [Color] to use, representing the original background [color] with an
 * overlay corresponding to [elevation] applied. The overlay will only be applied to
 * [ColorScheme.surface].
 */
@InternalSparkApi
public fun SparkColors.applyTonalElevation(
    backgroundColor: Color,
    elevation: Dp,
): Color = if (backgroundColor == surface) {
    surfaceColorAtElevation(elevation)
} else {
    backgroundColor
}

/**
 * Computes the surface tonal color at different elevation levels e.g. surface1 through surface5.
 *
 * @param elevation Elevation value used to compute alpha of the color overlay layer.
 *
 * @return the [ColorScheme.surface] color with an alpha of the [ColorScheme.surfaceTint] color
 * overlaid on top of it.

 */
public fun SparkColors.surfaceColorAtElevation(
    elevation: Dp,
): Color {
    if (elevation > 0.dp && surface.luminance() >= 0.5) return surface
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return surfaceTint.copy(alpha = alpha).compositeOver(surface)
}

/**
 * CompositionLocal used to pass [SparkColors] down the tree.
 *
 * Setting the value here is typically done as part of [SparkTheme], which will
 * automatically handle efficiently updating any changed colors without causing unnecessary
 * recompositions, using [SparkColors.updateColorsFrom].
 * To retrieve the current value of this CompositionLocal, use [SparkTheme.colors].
 */
@Suppress("CompositionLocalAllowlist") // We need it to get access to our color like Material
internal val LocalSparkColors = staticCompositionLocalOf { lightSparkColors() }

/**
 * A [SparkColors] implementation which sets all colors to [debugColor] to help highlighting the usage
 * of [SparkTheme.colors] to more easily detect where they're not used.
 */
public fun debugColors(
    debugColor: Color = Color.Magenta,
    onDebugColor: Color = Color.Green,
): SparkColors = SparkColors(
    accent = debugColor,
    onAccent = onDebugColor,
    accentContainer = debugColor,
    onAccentContainer = onDebugColor,
    accentVariant = debugColor,
    onAccentVariant = onDebugColor,
    basic = debugColor,
    onBasic = onDebugColor,
    basicContainer = debugColor,
    onBasicContainer = onDebugColor,
    main = debugColor,
    onMain = onDebugColor,
    mainContainer = debugColor,
    onMainContainer = onDebugColor,
    mainVariant = debugColor,
    onMainVariant = onDebugColor,
    support = debugColor,
    onSupport = onDebugColor,
    supportContainer = debugColor,
    onSupportContainer = onDebugColor,
    supportVariant = debugColor,
    onSupportVariant = onDebugColor,
    success = debugColor,
    onSuccess = onDebugColor,
    successContainer = debugColor,
    onSuccessContainer = onDebugColor,
    alert = debugColor,
    onAlert = onDebugColor,
    alertContainer = debugColor,
    onAlertContainer = onDebugColor,
    error = debugColor,
    onError = onDebugColor,
    errorContainer = debugColor,
    onErrorContainer = onDebugColor,
    info = debugColor,
    onInfo = onDebugColor,
    infoContainer = debugColor,
    onInfoContainer = onDebugColor,
    neutral = debugColor,
    onNeutral = onDebugColor,
    neutralContainer = debugColor,
    onNeutralContainer = onDebugColor,
    background = Color.Blue,
    onBackground = onDebugColor,
    backgroundVariant = Color.Blue,
    onBackgroundVariant = onDebugColor,
    surface = Color.Blue,
    onSurface = onDebugColor,
    surfaceInverse = Color.Blue,
    onSurfaceInverse = onDebugColor,
    surfaceTint = debugColor,
    outline = debugColor,
    outlineHigh = debugColor,
    scrim = debugColor,
    dimContent1 = .72f,
    dimContent2 = .56f,
    dimContent3 = .40f,
    dimContent4 = .16f,
    dimContent5 = .08f,
)

@Preview(
    group = "Tokens",
    name = "Colors",
    device = "spec:width=1280dp,height=800dp,dpi=240",
)
@Preview(
    group = "Tokens",
    name = "Colors Dark",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    device = "spec:width=1280dp,height=800dp,dpi=240",
)
@Composable
private fun ColorPreview() {
    PreviewTheme {
        Colors()
    }
}

@Composable
private fun Colors() {
    Row {
        previewColors.forEach { column ->
            Column {
                column.forEach { row ->
                    Row {
                        row.forEach { color ->
                            ColorItem(color)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorItem(color: KProperty0<Color>) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .size(104.dp),
        color = color.get(),
        shape = SparkTheme.shapes.extraLarge,
        border = BorderStroke(2.dp, SparkTheme.colors.onBackground),
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = color.name,
                style = SparkTheme.typography.body2,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val previewColors
    @Composable get() = with(SparkTheme.colors) {
        listOf(
            listOf(
                listOf(::main, ::mainContainer, ::mainVariant),
                listOf(::support, ::supportContainer, ::supportVariant),
                listOf(::accent, ::accentContainer, ::accentVariant),
                listOf(::basic, ::basicContainer),
            ),
            listOf(
                listOf(::success, ::successContainer),
                listOf(::alert, ::alertContainer),
                listOf(::error, ::errorContainer),
                listOf(::info, ::infoContainer),
                listOf(::neutral, ::neutralContainer),
            ),
            listOf(
                listOf(::background, ::backgroundVariant),
                listOf(::surface, ::surfaceInverse),
                listOf(::outline, ::outlineHigh),
            ),
        )
    }
