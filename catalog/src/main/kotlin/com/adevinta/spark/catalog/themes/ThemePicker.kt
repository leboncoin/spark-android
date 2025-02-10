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

import android.app.UiModeManager
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.ui.shaders.colorblindness.ColorBlindSetting
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.tokens.Layout
import com.adevinta.spark.tokens.highlight
import java.text.NumberFormat

@Composable
public fun ThemePicker(
    modifier: Modifier = Modifier,
    theme: Theme,
    onThemeChange: (theme: Theme) -> Unit,
) {
    val uiModeManager = LocalContext.current.getSystemService<UiModeManager>()

    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
            .add(
                WindowInsets(
                    top = ThemePickerPadding,
                    bottom = ThemePickerPadding,
                    left = Layout.bodyMargin,
                    right = Layout.bodyMargin,
                ),
            )
            .asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(ThemePickerPadding),
    ) {
        item {
            val themeModes = ThemeMode.entries
            val themeModesLabel = themeModes.map { it.name }
            ButtonGroup(
                title = stringResource(id = R.string.theme_picker_mode_title),
                selectedOption = theme.themeMode.name,
                onOptionSelect = { onThemeChange(theme.copy(themeMode = ThemeMode.valueOf(it))) },
                options = themeModesLabel,
            )
        }
        item {
            Column {
                val colorModes = ColorMode.entries
                val colorModesLabel = colorModes.map { it.name }
                ButtonGroup(
                    title = stringResource(id = R.string.theme_picker_theme_title),
                    selectedOption = theme.colorMode.name,
                    onOptionSelect = { onThemeChange(theme.copy(colorMode = ColorMode.valueOf(it))) },
                    options = colorModesLabel,
                )
                AnimatedVisibility(
                    visible = theme.colorMode == ColorMode.Brand,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                ) {
                    DropdownEnum(
                        title = stringResource(id = R.string.brand),
                        selectedOption = theme.brandMode,
                        onOptionSelect = { onThemeChange(theme.copy(brandMode = it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = ThemePickerPadding),
                    )
                }
                AnimatedVisibility(
                    visible = theme.colorMode == ColorMode.Brand,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    SwitchLabelled(
                        checked = theme.userMode == UserMode.Pro,
                        onCheckedChange = { checked ->
                            onThemeChange(theme.copy(userMode = if (checked) UserMode.Pro else UserMode.Part))
                        },
                    ) {
                        Text(
                            text = stringResource(id = R.string.pro),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }

        val isContrastAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
        if (isContrastAvailable) {
            item {
                Column {
                    val contrastLevel = 1 + (uiModeManager?.contrast ?: 0f)
                    val level = remember { NumberFormat.getInstance().format(contrastLevel - 1) }
                    Text(
                        text = "Contrast level: $level",
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = SparkTheme.typography.body2.highlight,
                    )

                    androidx.compose.material3.Slider(
                        modifier = Modifier.systemGestureExclusion(),
                        value = contrastLevel - 1f,
                        valueRange = -1f..1f,
                        steps = 9,
                        onValueChange = { },
                    )
                }
            }
        }

        item {
            val textDirections = TextDirection.entries
            val textDirectionsLabel = textDirections.map { it.name }
            ButtonGroup(
                title = stringResource(id = R.string.theme_picker_text_direction_title),
                selectedOption = theme.textDirection.name,
                onOptionSelect = { onThemeChange(theme.copy(textDirection = TextDirection.valueOf(it))) },
                options = textDirectionsLabel,
            )
        }
        item {
            Column {
                val fontScaleModes = FontScaleMode.entries
                val fontModesLabel = fontScaleModes.map { it.name }
                ButtonGroup(
                    title = stringResource(id = R.string.theme_picker_font_scale_title),
                    selectedOption = theme.fontScaleMode.name,
                    onOptionSelect = { onThemeChange(theme.copy(fontScaleMode = FontScaleMode.valueOf(it))) },
                    options = fontModesLabel,
                )
                var fontScale by remember { mutableFloatStateOf(theme.fontScale) }
                AnimatedVisibility(
                    visible = theme.fontScaleMode == FontScaleMode.Custom,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    FontScaleItem(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = theme.fontScaleMode == FontScaleMode.Custom,
                        fontScale = fontScale,
                        onValueChange = { fontScale = it },
                        onValueChangeFinished = { onThemeChange(theme.copy(fontScale = fontScale)) },
                    )
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item {
                Column {
                    ColorBlindSetting(
                        colorBlindNessType = theme.colorBlindNessType,
                        severity = theme.colorBlindNessSeverity,
                        onTypeChange = {
                            onThemeChange(theme.copy(colorBlindNessType = it))
                        },
                        onSeverityChange = { onThemeChange(theme.copy(colorBlindNessSeverity = it)) },
                    )
                }
            }
        }
        item {
            DropdownEnum(
                title = stringResource(id = R.string.themepicker_navigation_label),
                selectedOption = theme.navigationMode,
                onOptionSelect = { onThemeChange(theme.copy(navigationMode = it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ThemePickerPadding),
            )
        }
        item {
            SwitchLabelled(
                checked = theme.useLegacyTheme,
                onCheckedChange = { checked ->
                    onThemeChange(theme.copy(useLegacyTheme = checked))
                },
            ) {
                Text(
                    text = "Use LegacyTheme",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        item {
            SwitchLabelled(
                checked = theme.highlightSparkComponents,
                onCheckedChange = { checked ->
                    onThemeChange(theme.copy(highlightSparkComponents = checked))
                },
            ) {
                Text(
                    text = "Highlight Spark Components",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        item {
            SwitchLabelled(
                checked = theme.highlightSparkTokens,
                onCheckedChange = { checked ->
                    onThemeChange(theme.copy(highlightSparkTokens = checked))
                },
            ) {
                Text(
                    text = "Highlight Spark Tokens",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun FontScaleItem(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    fontScale: Float,
    fontScaleMin: Float = MinFontScale,
    fontScaleMax: Float = MaxFontScale,
    onValueChange: (textScale: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
) {
    Column(modifier = modifier) {
        Slider(
            enabled = enabled,
            value = fontScale,
            steps = 10,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            valueRange = fontScaleMin..fontScaleMax,
        )
        Text(
            text = stringResource(id = R.string.scale, fontScale),
            style = SparkTheme.typography.body2.highlight,
        )
    }
}

@Preview
@Composable
private fun ThemePickerPreview() {
    PreviewTheme {
        ThemePicker(
            theme = Theme(),
            onThemeChange = {},
        )
    }
}

private val ThemePickerPadding = 16.dp
