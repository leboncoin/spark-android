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
@file:SuppressLint("UseSdkSuppress")

package com.adevinta.spark.placeholder

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toPixelMap
import com.google.common.truth.Truth.assertThat

/**
 * Asserts that the color at a specific pixel in the bitmap at ([x], [y]) is [expected].
 */
fun PixelMap.assertPixelColor(expected: Color, x: Int, y: Int, tolerance: Float = 0.02f) {
    val color = this[x, y]
    assertThat(color.red).isWithin(tolerance).of(expected.red)
    assertThat(color.green).isWithin(tolerance).of(expected.green)
    assertThat(color.blue).isWithin(tolerance).of(expected.blue)
    assertThat(color.alpha).isWithin(tolerance).of(expected.alpha)
}

/**
 * Asserts that the colors at specific pixels in the vertices of the bitmap are [expected].
 */
fun ImageBitmap.assertPixelsOfVertices(expected: Color) {
    toPixelMap().run {
        assertPixelColor(expected, 0, 0)
        assertPixelColor(expected, 0, height - 1)
        assertPixelColor(expected, width - 1, 0)
        assertPixelColor(expected, width - 1, height - 1)
    }
}

/**
 * Asserts that all of the pixels in this image are of the [expected] color.
 */
fun ImageBitmap.assertPixels(expected: Color, tolerance: Float = 0.001f) {
    toPixelMap().run {
        for (x in 0 until width) {
            for (y in 0 until height) {
                assertPixelColor(expected, x, y, tolerance)
            }
        }
    }
}
