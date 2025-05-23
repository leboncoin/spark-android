/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.tokens.shapes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.SingleChoiceDropdown
import com.adevinta.spark.tokens.Order
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

@Composable
internal fun ColumnScope.ShapeCustomSample() {
    val defaultValue = SparkTheme.shapes.none

    var selectedShape by remember { mutableStateOf(defaultValue) }
    var expanded by remember { mutableStateOf(false) }

    SingleChoiceDropdown(
        modifier = Modifier.fillMaxWidth(),
        value = shapes.first { it == selectedShape }.propertyName(),
        label = "Shape",
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        onDismissRequest = { expanded = false },
        dropdownContent = {
            shapes.forEach { shape ->
                DropdownMenuItem(
                    text = { Text(shape.propertyName()) },
                    onClick = {
                        selectedShape = shape
                        expanded = false
                    },
                    selected = selectedShape == shape,
                )
            }
        },
    )

    ShapeItem(selectedShape)
}

@Composable
private fun ColumnScope.ShapeItem(shape: CornerBasedShape) {
    Surface(
        modifier = Modifier
            .size(104.dp)
            .padding(8.dp)
            .align(Alignment.CenterHorizontally),
        elevation = 6.dp,
        shape = shape,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "Shape",
                style = SparkTheme.typography.caption,
            )
        }
    }
}

private val shapes: List<CornerBasedShape>
    @Composable
    get() = listOf(
        SparkTheme.shapes.none,
        SparkTheme.shapes.extraSmall,
        SparkTheme.shapes.small,
        SparkTheme.shapes.medium,
        SparkTheme.shapes.large,
        SparkTheme.shapes.extraLarge,
    )

@Suppress("ReplaceGetOrSet")
@Composable
private fun CornerBasedShape.propertyName(): String = SparkTheme.shapes::class
    .declaredMemberProperties
    .sortedBy { it.findAnnotation<Order>()?.value ?: Int.MAX_VALUE }
    .get(shapes.indexOf(this))
    .name

@Preview
@Composable
private fun ShapeCustomSamplePreview() {
    PreviewTheme {
        ShapeCustomSample()
    }
}
