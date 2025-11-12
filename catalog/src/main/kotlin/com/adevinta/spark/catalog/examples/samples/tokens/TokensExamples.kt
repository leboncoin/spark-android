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
package com.adevinta.spark.catalog.examples.samples.tokens

import com.adevinta.spark.catalog.examples.samples.tokens.colors.ColorSample
import com.adevinta.spark.catalog.examples.samples.tokens.elevation.ElevationCustomSample
import com.adevinta.spark.catalog.examples.samples.tokens.elevation.ElevationSample
import com.adevinta.spark.catalog.examples.samples.tokens.shapes.ShapeCustomSample
import com.adevinta.spark.catalog.examples.samples.tokens.shapes.ShapeSample
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val TokensExampleSourceUrl = "$SampleSourceUrl/TokensExamples.kt"
public val TokensExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "colors",
        name = "Colors",
        description = "Showcase the color tokens with their container and on color variants",
        sourceUrl = TokensExampleSourceUrl,
    ) {
        ColorSample()
    },
    Example(
        id = "shapes",
        name = "Shapes",
        description = "Showcase the shape tokens",
        sourceUrl = TokensExampleSourceUrl,
    ) {
        ShapeSample()
    },
    Example(
        id = "shapes-showcase",
        name = "Shapes showcase",
        description = "Showcase the shape tokens",
        sourceUrl = TokensExampleSourceUrl,
    ) {
        ShapeCustomSample()
    },
    Example(
        id = "elevation",
        name = "Elevations",
        description = "Showcase the elevation tokens",
        sourceUrl = TokensExampleSourceUrl,
    ) {
        ElevationSample()
    },
    Example(
        id = "elevation-showcase",
        name = "Elevation showcase",
        description = "Showcase the elevation tokens",
        sourceUrl = TokensExampleSourceUrl,
    ) {
        ElevationCustomSample()
    },
)
