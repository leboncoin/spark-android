/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.catalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.util.cast
import com.adevinta.spark.catalog.util.splitCamelWithSpaces
import com.adevinta.spark.components.buttons.ButtonTinted
import com.adevinta.spark.components.dialog.ModalScaffold
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Order
import com.adevinta.spark.tokens.SparkColors
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType

/**
 * Represents a color option that can be either a SparkColors token or a custom color.
 */
@Stable
public sealed class ColorOption {
    public abstract val color: Color
    public abstract val name: String

    /**
     * A color option from SparkColors tokens.
     */
    @Stable
    public data class Token(override val color: Color, override val name: String) : ColorOption()

    /**
     * A custom color option.
     */
    @Stable
    public data class Custom(override val color: Color) : ColorOption() {
        override val name: String = "Custom"
    }
}

/**
 * Gets all SparkColors properties as ColorOption.Token instances.
 */
public fun List<List<KProperty1<SparkColors, Color>>>.toColorOptions(
    tokens: SparkColors,
): ImmutableList<ColorOption.Token> = this.flatMap { colorGroup ->
    colorGroup.map { property ->
        ColorOption.Token(property.get(tokens), property.name.splitCamelWithSpaces())
    }
}.toImmutableList()

/**
 * Combined data structure holding Spark color tokens, options, and lookup map for efficient access.
 */
@Stable
internal data class SparkColorTokensData(
    val colorTokenGroups: List<List<KProperty1<SparkColors, Color>>>,
    val colorOptions: ImmutableList<ColorOption>,
    val colorNameLookup: Map<Color, String>,
)

/**
 * A composable that displays a color selector with a preview and allows selecting colors
 * from SparkColors tokens or custom colors.
 *
 * @param title The title/label for the color selector
 * @param selectedColor The currently selected color
 * @param onColorSelected Callback when a color is selected. Returns the selected Color,
 * or null if the same color was selected (based on allowSameColorSelection), or nothing if dismissed.
 * @param modifier Modifier for the component
 * @param allowSameColorSelection If true, selecting the same color returns it. If false, returns null.
 */
@Composable
public fun ColorSelector(
    title: String,
    selectedColor: Color,
    onColorSelected: (Color?) -> Unit,
    modifier: Modifier = Modifier,
    allowSameColorSelection: Boolean = false,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val colors = SparkTheme.colors
    val sparkColorTokensData by rememberSparkColorTokens(colors, selectedColor)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(SparkTheme.shapes.medium)
            .clickable { showDialog = true }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = SparkTheme.typography.body2,
            modifier = Modifier.weight(1f),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val borderColor = if (selectedColor.luminance() >= 0.5) Color.Black else Color.White
            // Color preview
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(selectedColor, CircleShape)
                    .border(1.dp, borderColor, CircleShape),
            )
            // Color name if it's a token
            val colorName = remember(selectedColor) {
                sparkColorTokensData.colorNameLookup[selectedColor] ?: "Custom"
            }
            Text(
                text = colorName,
                style = SparkTheme.typography.body2,
                color = SparkTheme.colors.onSurface,
            )
        }
    }

    if (showDialog) {
        ColorPickerDialog(
            selectedColor = selectedColor,
            sparkColorTokensData = sparkColorTokensData,
            onColorSelected = { color ->
                showDialog = false
                if (color == null) {
                    // Dialog dismissed, do nothing
                    return@ColorPickerDialog
                }
                if (!allowSameColorSelection && color == selectedColor) {
                    onColorSelected(null)
                } else {
                    onColorSelected(color)
                }
            },
            onDismiss = { showDialog = false },
        )
    }
}

/**
 * Dialog for selecting colors from SparkColors tokens or custom colors.
 */
@Composable
private fun ColorPickerDialog(
    selectedColor: Color,
    sparkColorTokensData: SparkColorTokensData,
    onColorSelected: (Color?) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedCustomColor by remember(selectedColor) {
        mutableStateOf(if (selectedColor !in sparkColorTokensData.colorNameLookup) selectedColor else Color.Unspecified)
    }
    val controller = rememberColorPickerController()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    var isCustomPickerVisible by remember { mutableStateOf(selectedColor !in sparkColorTokensData.colorNameLookup) }
    ModalScaffold(
        title = { Text(stringResource(R.string.color_picker_title)) },
        onClose = onDismiss,
        mainButton = {
            ButtonTinted(
                modifier = it,
                text = stringResource(R.string.color_picker_select_custom),
                onClick = {
                    onColorSelected(selectedCustomColor)
                },
            )
        },
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = it,
            state = gridState,
            modifier = Modifier.fillMaxWidth(),
        ) {
            stickyHeader {
                // SparkColors tokens grid
                Text(
                    text = stringResource(R.string.color_picker_tokens_title),
                    style = SparkTheme.typography.body1,
                )
            }
            items(sparkColorTokensData.colorOptions) { colorOption ->
                when (colorOption) {
                    is ColorOption.Token -> {
                        ColorTokenItem(
                            colorOption = colorOption,
                            isSelected = colorOption.color == selectedColor,
                            onClick = { onColorSelected(colorOption.color) },
                        )
                    }

                    is ColorOption.Custom -> {
                        CustomColorItem(
                            isSelected = isCustomPickerVisible,
                            onClick = {
                                isCustomPickerVisible = true
                                coroutineScope.launch {
                                    gridState.animateScrollToItem(sparkColorTokensData.colorOptions.size + 1)
                                }
                            },
                            color = selectedCustomColor,
                        )
                    }
                }
            }
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                Column {
                    if (isCustomPickerVisible) {
                        // Custom color picker
                        Text(
                            text = stringResource(R.string.color_picker_custom_title),
                            style = SparkTheme.typography.body1,
                        )

                        HsvColorPicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp)
                                .padding(10.dp),
                            controller = controller,
                            initialColor = selectedCustomColor,
                            onColorChanged = { colorEnvelope: ColorEnvelope ->
                                // do something
                                selectedCustomColor = colorEnvelope.color
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * Item for displaying a SparkColors token.
 */
@Composable
private fun ColorTokenItem(
    colorOption: ColorOption.Token,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colorOption.color, SparkTheme.shapes.full)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) SparkTheme.colors.outlineHigh else SparkTheme.colors.outline,
                    shape = SparkTheme.shapes.full,
                ),
        )
        Text(
            text = colorOption.name,
            style = SparkTheme.typography.caption,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}

/**
 * Item for custom color option.
 */
@Composable
private fun CustomColorItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, SparkTheme.shapes.full)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) SparkTheme.colors.main else SparkTheme.colors.outline,
                    shape = SparkTheme.shapes.full,
                ),
        )
        Text(
            text = stringResource(R.string.color_picker_custom),
            style = SparkTheme.typography.caption,
        )
    }
}

/**
 * Avoid computing these tokens on each recomposition but do it only when colors changes.
 */
@Composable
internal fun rememberSparkColorTokens(colors: SparkColors, selectedColor: Color): State<SparkColorTokensData> {
    val result = remember(colors) {
        mutableStateOf(
            SparkColorTokensData(
                emptyList(),
                emptyList<ColorOption>().toImmutableList(),
                emptyMap(),
            ),
        )
    }

    LaunchedEffect(colors) {
        val tokens = withContext(Dispatchers.Default) {
            val colorTokenGroups = colors::class.declaredMemberProperties
                .filterNot { field -> field.hasAnnotation<Deprecated>() }
                // Remove dims and any non color tokens
                .filter { it.returnType == Color::class.starProjectedType }
                // Cast the type otherwise we get a star type instead of Long
                .map { it.cast<KProperty1<SparkColors, Color>>() }
                // Convert property to always return a Color type
                .map { it.asColorProperty() }
                // Remove content colors
                .filterNot { it.name.startsWith("on") }
                // Use the same order than the one in the specs
                .sortedBy { it.findAnnotation<Order>()?.value ?: Int.MAX_VALUE }
                // Group by token name like "main", "mainContainer" or "mainVariant"
                .groupBy { it.name.takeWhile { char -> !char.isUpperCase() } }
                .values.toList()

            val colorOptions = colorTokenGroups.flatMap { colorGroup ->
                colorGroup.map { property ->
                    ColorOption.Token(property.get(colors), property.name.splitCamelWithSpaces()) as ColorOption
                }
            }.toMutableList()
                .also {
                    it.add(ColorOption.Custom(selectedColor))
                }.toImmutableList()

            val colorNameLookup = colorOptions.associate { it.color to it.name }

            SparkColorTokensData(colorTokenGroups, colorOptions, colorNameLookup)
        }
        result.value = tokens
    }

    return result
}

/**
 * @return [this] property value as [Color] even when the compiler (or R8) inlines the value as [ULong] in release mode.
 */
private fun KProperty1<SparkColors, Color>.asColorProperty(): KProperty1<SparkColors, Color> =
    object : KProperty1<SparkColors, Color> by this {
        @Suppress("USELESS_IS_CHECK")
        override fun get(receiver: SparkColors): Color = when (val any: Any = this@asColorProperty.get(receiver)) {
            // in debug builds
            is Color -> any

            // in release builds
            is Long -> Color(any.toULong())

            else -> error("Unexpected type: ${any::class}")
        }
    }
