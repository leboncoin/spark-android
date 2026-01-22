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

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.ui.shaders.colorblindness.ColorBlindSetting
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.slider.Slider
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.SparkAnimatedIcons
import com.adevinta.spark.icons.collapseExpand
import com.adevinta.spark.tokens.Layout
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.highlight
import com.composeunstyled.UnstyledDisclosure
import com.composeunstyled.UnstyledDisclosureHeading
import com.composeunstyled.UnstyledDisclosurePanel
import com.composeunstyled.rememberDisclosureState
import java.text.NumberFormat

@SuppressLint("MaterialComposableHasSparkReplacement")
@Composable
public fun ThemePicker(
    theme: Theme,
    onThemeChange: (theme: Theme) -> Unit,
    modifier: Modifier = Modifier,
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
        verticalArrangement = spacedBy(ItemSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Theme Settings Section
        item(
            key = "theme_settings_header",
            contentType = ThemePickerContentType.SectionHeader,
        ) {
            SectionHeader(title = stringResource(id = R.string.theme_picker_section_theme_settings))
        }
        item(
            key = "mode",
            contentType = ThemePickerContentType.ButtonGroup,
        ) {
            Column {
                val themeModes = ThemeMode.entries
                val themeModesLabel = themeModes.map { it.name }
                ButtonGroup(
                    title = stringResource(id = R.string.theme_picker_mode_title),
                    selectedOption = theme.themeMode.name,
                    onOptionSelect = { onThemeChange(theme.copy(themeMode = ThemeMode.valueOf(it))) },
                    options = themeModesLabel,
                )
                HelperText(text = stringResource(id = R.string.theme_picker_mode_helper))
            }
        }
        item(
            key = "theme_pro",
            contentType = ThemePickerContentType.SwitchGroup,
        ) {
            Column(
                verticalArrangement = spacedBy(RelatedItemSpacing),
            ) {
                Column {
                    val colorModes = ColorMode.entries
                    val colorModesLabel = colorModes.map { it.name }
                    ButtonGroup(
                        title = stringResource(id = R.string.theme_picker_theme_title),
                        selectedOption = theme.colorMode.name,
                        onOptionSelect = {
                            onThemeChange(theme.copy(colorMode = ColorMode.valueOf(it)))
                        },
                        options = colorModesLabel,
                    )
                    HelperText(text = stringResource(id = R.string.theme_picker_theme_helper))
                }
                AnimatedVisibility(
                    visible = theme.colorMode == ColorMode.Baseline,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Column {
                        val userModes = UserMode.entries
                        val userModesLabel = userModes.map { it.name }
                        ButtonGroup(
                            title = stringResource(id = R.string.theme_picker_theme_title),
                            selectedOption = theme.userMode.name,
                            onOptionSelect = { onThemeChange(theme.copy(userMode = UserMode.valueOf(it))) },
                            options = userModesLabel,
                        )
                        HelperText(text = stringResource(id = R.string.theme_picker_pro_theme_helper))
                    }
                }
            }
        }

        // Accessibility Section
        item(
            key = "accessibility_divider",
            contentType = ThemePickerContentType.Divider,
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = SectionDividerPadding),
            )
        }
        item(
            key = "accessibility_header",
            contentType = ThemePickerContentType.SectionHeader,
        ) {
            SectionHeader(title = stringResource(id = R.string.theme_picker_section_accessibility))
        }
        item(
            key = "font_scale",
            contentType = ThemePickerContentType.ButtonGroup,
        ) {
            Column(
                verticalArrangement = spacedBy(RelatedItemSpacing),
            ) {
                Column {
                    val fontScaleModes = FontScaleMode.entries
                    val fontModesLabel = fontScaleModes.map { it.name }
                    ButtonGroup(
                        title = stringResource(id = R.string.theme_picker_font_scale_title),
                        selectedOption = theme.fontScaleMode.name,
                        onOptionSelect = {
                            onThemeChange(theme.copy(fontScaleMode = FontScaleMode.valueOf(it)))
                        },
                        options = fontModesLabel,
                    )
                    HelperText(text = stringResource(id = R.string.theme_picker_font_scale_helper))
                }
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
        item(
            key = "text_direction",
            contentType = ThemePickerContentType.ButtonGroup,
        ) {
            Column {
                val textDirections = TextDirection.entries
                val textDirectionsLabel = textDirections.map { it.name }
                ButtonGroup(
                    title = stringResource(id = R.string.theme_picker_text_direction_title),
                    selectedOption = theme.textDirection.name,
                    onOptionSelect = {
                        onThemeChange(theme.copy(textDirection = TextDirection.valueOf(it)))
                    },
                    options = textDirectionsLabel,
                )
                HelperText(text = stringResource(id = R.string.theme_picker_text_direction_helper))
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item(
                key = "color_blindness",
                contentType = ThemePickerContentType.ColorBlindSetting,
            ) {
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
        val isContrastAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
        if (isContrastAvailable) {
            item(
                key = "contrast",
                contentType = ThemePickerContentType.ContrastSlider,
            ) {
                Column {
                    val contrastLevel = 1 + (uiModeManager?.contrast ?: 0f)
                    val level = remember { NumberFormat.getInstance().format(contrastLevel - 1) }
                    Text(
                        text = "Contrast level: $level",
                        modifier = Modifier.padding(bottom = SliderLabelSpacing),
                        style = SparkTheme.typography.body2.highlight,
                    )

                    androidx.compose.material3.Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .systemGestureExclusion(),
                        value = contrastLevel - 1f,
                        valueRange = 0f..1f,
                        steps = 9,
                        onValueChange = { },
                    )
                    HelperText(text = stringResource(id = R.string.theme_picker_contrast_helper))
                }
            }
        }

        // Developer Options Section
        item(
            key = "developer_divider",
            contentType = ThemePickerContentType.Divider,
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = SectionDividerPadding),
            )
        }
        item(
            key = "developer_options",
            contentType = ThemePickerContentType.SectionHeader,
        ) {
            val disclosureState = rememberDisclosureState(initiallyExpanded = false)
            UnstyledDisclosure(state = disclosureState) {
                UnstyledDisclosureHeading(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SparkTheme.shapes.small,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 16.dp),

                ) {
                    Text(
                        text = stringResource(id = R.string.theme_picker_section_developer_options),
                        style = SparkTheme.typography.headline2,
                    )
                    Icon(
                        sparkIcon = SparkAnimatedIcons.collapseExpand(),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        atEnd = disclosureState.expanded,
                    )
                }
                UnstyledDisclosurePanel(
                    enter = expandVertically(
                        spring(
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntSize.VisibilityThreshold,
                        ),
                    ),
                    exit = shrinkVertically(),

                ) {
                    Column(
                        verticalArrangement = spacedBy(ItemSpacing),
                        modifier = Modifier.padding(top = ItemSpacing),
                    ) {
                        HelperText(text = stringResource(id = R.string.theme_picker_developer_options_helper))

                        Column {
                            DropdownEnum(
                                title = stringResource(id = R.string.themepicker_navigation_label),
                                selectedOption = theme.navigationMode,
                                onOptionSelect = { onThemeChange(theme.copy(navigationMode = it)) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            HelperText(text = stringResource(id = R.string.theme_picker_navigation_helper))
                        }
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
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = SparkTheme.typography.headline2,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = SectionHeaderBottomPadding),
    )
}

@Composable
private fun HelperText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = SparkTheme.typography.caption,
        color = LocalContentColor.current.dim1,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = HelperTextTopPadding),
    )
}

@Composable
private fun FontScaleItem(
    enabled: Boolean,
    fontScale: Float,
    onValueChange: (textScale: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    fontScaleMin: Float = MinFontScale,
    fontScaleMax: Float = MaxFontScale,
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(SliderLabelSpacing),
    ) {
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

private enum class ThemePickerContentType {
    SectionHeader,
    Divider,
    ButtonGroup,
    SwitchGroup,
    ColorBlindSetting,
    ContrastSlider,
    DropdownGroup,
}

private val ThemePickerPadding = 16.dp
private val ItemSpacing = 12.dp
private val RelatedItemSpacing = 8.dp
private val SectionDividerPadding = 20.dp
private val SectionHeaderBottomPadding = 12.dp
private val SliderLabelSpacing = 8.dp
private val HelperTextTopPadding = 4.dp

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
