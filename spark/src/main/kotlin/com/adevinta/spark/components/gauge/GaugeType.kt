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

package com.adevinta.spark.components.gauge

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.SparkTheme

public enum class GaugeType(internal val index: Int) {
    VeryHigh(index = 4) {
        override val color: Color
            @Composable
            get() = SparkTheme.colors.success
    },
    High(index = 3) {
        override val color: Color
            @Composable
            get() = SparkTheme.colors.success
    },
    Medium(index = 2) {
        override val color: Color
            @Composable
            get() = SparkTheme.colors.neutral
    },
    Low(index = 1) {
        override val color: Color
            @Composable
            get() = SparkTheme.colors.alert
    },
    VeryLow(index = 0) {
        override val color: Color
            @Composable
            get() = SparkTheme.colors.error
    },
    NoData(index = 0) {
        override val color: Color
            @Composable
            get() = SparkTheme.colors.surface
    };

    @get:Composable
    public abstract val color: Color
}
