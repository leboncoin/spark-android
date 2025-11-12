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
package com.adevinta.spark.catalog.configurator.samples.divider

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.divider.DividerIntent
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.divider.LabelHorizontalAlignment
import com.adevinta.spark.components.divider.LabelVerticalAlignment
import com.adevinta.spark.components.divider.VerticalDivider
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.TextField

public val DividerConfigurator: Configurator = Configurator(
    id = "divider",
    name = "Divider",
    description = "Divider configuration",
    sourceUrl = "$SampleSourceUrl/DividerSamples.kt",
) {
    DividerSample()
}

@Composable
private fun ColumnScope.DividerSample() {
    var intent by remember { mutableStateOf(DividerIntent.Outline) }
    var hLabelAlignments by remember { mutableStateOf(LabelHorizontalAlignment.Center) }
    var vLabelAlignments by remember { mutableStateOf(LabelVerticalAlignment.Center) }
    var labelText by remember { mutableStateOf("label") }

    ButtonGroup(
        title = "Divider Intent",
        selectedOption = intent,
        onOptionSelect = { intent = it },
    )

    Text(
        text = "HorizontalDivider",
        style = SparkTheme.typography.headline2,
    )

    HorizontalDivider(intent = intent)
    HorizontalDivider(
        intent = intent,
        label = labelText.takeUnless { it.isBlank() }?.let { { TextComposable(label = it) } },
        labelHorizontalAlignment = hLabelAlignments,
    )
    HorizontalDivider(
        modifier = Modifier
            .width(24.dp)
            .align(Alignment.CenterHorizontally),
        intent = intent,
    )

    Text(
        text = "VerticalDivider",
        style = SparkTheme.typography.headline2,
    )

    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        horizontalArrangement = spacedBy(16.dp),
    ) {
        VerticalDivider(
            modifier = Modifier.height(200.dp),
            intent = intent,
        )
        VerticalDivider(
            modifier = Modifier
                .height(200.dp),
            intent = intent,
            label = labelText.takeUnless { it.isBlank() }?.let { { TextComposable(label = it) } },
            labelVerticalAlignment = vLabelAlignments,
        )
        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .align(Alignment.CenterVertically),
            intent = intent,
        )
    }

    ButtonGroup(
        title = "HorizontalDivider Label Alignment",
        selectedOption = hLabelAlignments,
        onOptionSelect = { hLabelAlignments = it },
    )

    ButtonGroup(
        title = "VerticalDivider Label Alignment",
        selectedOption = vLabelAlignments,
        onOptionSelect = { vLabelAlignments = it },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = labelText,
        onValueChange = { labelText = it },
        label = "Change label",
        helper = "Divider label",
    )
}

@Composable
@Preview
private fun DividerSamplePreview() {
    PreviewTheme {
        DividerSample()
    }
}

@Composable
private fun TextComposable(label: String = "label") {
    Text(
        modifier = Modifier,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        style = SparkTheme.typography.body1,
        text = label,
    )
}
