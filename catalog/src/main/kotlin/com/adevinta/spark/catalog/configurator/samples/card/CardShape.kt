/*
 * Copyright (c) 2026 Adevinta
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

package com.adevinta.spark.catalog.configurator.samples.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

import com.adevinta.spark.SparkTheme

internal enum class CardShape {
    None {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.none
    },
    ExtraSmall {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.extraSmall
    },
    Small {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.small
    },
    Medium {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.medium
    },
    Large {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.large
    },
    ExtraLarge {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.extraLarge
    },
    Full {
        override val shape: Shape
            @Composable get() = SparkTheme.shapes.full
    },
    ;

    abstract val shape: Shape
        @Composable get
}
