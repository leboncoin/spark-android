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
package com.adevinta.spark.catalog.examples.samples.tokens.colors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.cast
import com.adevinta.spark.catalog.util.splitCamelWithSpaces
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Order
import com.adevinta.spark.tokens.SparkColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType

@Composable
internal fun ColorSample() {
    val tokensColorsGroups by rememberColorTokens(SparkTheme.colors)

    tokensColorsGroups.forEach { tokens ->
        Row {
            tokens.forEach { token ->
                ColorItem(token)
            }
        }
    }
}

@Composable
private fun RowScope.ColorItem(color: KProperty1<SparkColors, Color>) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .heightIn(104.dp)
            .weight(1f),
        color = color.get(SparkTheme.colors),
        shape = SparkTheme.shapes.extraLarge,
        border = BorderStroke(2.dp, SparkTheme.colors.onBackground),
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = color.name.splitCamelWithSpaces(),
                style = SparkTheme.typography.body2,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Avoid computing these tokens on each recomposition but do it only when colors changes.
 */
@Composable
private fun rememberColorTokens(colors: SparkColors): State<List<List<KProperty1<SparkColors, Color>>>> {
    val result = remember(colors) { mutableStateOf<List<List<KProperty1<SparkColors, Color>>>>(emptyList()) }

    LaunchedEffect(colors) {
        val tokens = withContext(Dispatchers.Default) {
            colors::class.declaredMemberProperties
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

@Preview
@Composable
private fun ColorSamplePreview() {
    SparkTheme {
        ColorSample()
    }
}
