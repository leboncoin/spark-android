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
package com.adevinta.spark.catalog.configurator.samples.colorselector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ColorSelector
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.tokens.contentColorFor

public val ColorSelectorTestConfigurator: Configurator = Configurator(
    id = "color-selector-test",
    name = "Color Selector Test",
    description = "Test configurator for ColorSelector component",
    sourceUrl = "$SampleSourceUrl/ColorSelectorSamples.kt",
) {
    ColorSelectorTestSample(it)
}

@Composable
private fun ColumnScope.ColorSelectorTestSample(snackbarHostState: SnackbarHostState) {
    val defaultColor = SparkTheme.colors.main
    var selectedColor by remember { mutableStateOf(defaultColor) }
    var allowSameColorSelection by remember { mutableStateOf(false) }

    // Display the selected color
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp),
        color = selectedColor,
        shape = SparkTheme.shapes.full,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Selected Color Preview",
                color = if (contentColorFor(selectedColor) != Color.Unspecified) {
                    contentColorFor(selectedColor)
                } else if (selectedColor.luminance() >= 0.5) {
                    Color.Black
                } else {
                    Color.White
                },
                style = SparkTheme.typography.headline2,
            )
        }
    }

    ColorSelector(
        title = "Select Color",
        selectedColor = selectedColor,
        onColorSelected = { color ->
            if (color != null) {
                selectedColor = color
            }
        },
        allowSameColorSelection = allowSameColorSelection,
    )

    SwitchLabelled(
        checked = allowSameColorSelection,
        onCheckedChange = { allowSameColorSelection = it },
    ) {
        Text(
            text = "Allow selecting same color",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun ColorSelectorTestSamplePreview() {
    PreviewTheme { ColorSelectorTestSample(SnackbarHostState()) }
}
