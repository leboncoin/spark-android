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
package com.adevinta.spark.catalog.configurator.samples.typography

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import com.adevinta.spark.catalog.examples.samples.tokens.typography.typographyTokens
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.SingleChoiceDropdown
import com.adevinta.spark.components.textfields.TextField

public val TypographyConfigurator: Configurator = Configurator(
    id = "typography",
    name = "Typography",
    description = "Typography token configuration",
    sourceUrl = "$SampleSourceUrl/TokensExamples.kt",
) { _, _ ->
    TypographySample()
}

@Composable
private fun ColumnScope.TypographySample() {
    val tokens = typographyTokens()
    var selectedName by rememberSaveable { mutableStateOf(tokens.first().first) }
    var text = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }

    SingleChoiceDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = selectedName,
        label = "Typography token",
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        onDismissRequest = { expanded = false },
        dropdownContent = {
            tokens.fastForEach { (name, _) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        selectedName = name
                        expanded = false
                    },
                    selected = selectedName == name,
                )
            }
        },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        state = text,
        label = "Preview text",
    )

    val selectedStyle = tokens.first { it.first == selectedName }.second
    Text(
        text = text.text.toString(),
        style = selectedStyle,
    )
}

@Composable
@Preview
private fun TypographySamplePreview() {
    PreviewTheme {
        TypographySample()
    }
}
