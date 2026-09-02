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
package com.adevinta.spark.placeholder

import com.adevinta.spark.components.placeholder.buildSquigglePoints
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Unit tests for [buildSquigglePoints].
 *
 * The function is pure and seeded only by the integer-pixel dimensions, so the tests run on the
 * JVM without Robolectric or a Compose rule.
 */
@RunWith(JUnit4::class)
class HandDrawnTextPlaceholderTest {

    @Test
    fun sameDimensionsProduceIdenticalPoints() {
        // Calling with the same size and line count twice must yield an equal list.
        val first = buildSquigglePoints(widthPx = 300f, heightPx = 16f, lineCount = 1)
        val second = buildSquigglePoints(widthPx = 300f, heightPx = 16f, lineCount = 1)
        assertThat(first).isEqualTo(second)
    }

    @Test
    fun differentWidthProducesDifferentPoints() {
        // A change in width alters the seed, so the resulting geometry must differ.
        val baseline = buildSquigglePoints(widthPx = 300f, heightPx = 16f, lineCount = 1)
        val narrower = buildSquigglePoints(widthPx = 280f, heightPx = 16f, lineCount = 1)
        assertThat(baseline).isNotEqualTo(narrower)
    }

    @Test
    fun differentHeightProducesDifferentPoints() {
        // A change in height alters the seed, so the resulting geometry must differ.
        val baseline = buildSquigglePoints(widthPx = 300f, heightPx = 16f, lineCount = 1)
        val taller = buildSquigglePoints(widthPx = 300f, heightPx = 20f, lineCount = 1)
        assertThat(baseline).isNotEqualTo(taller)
    }

    @Test
    fun lineCountControlsTheNumberOfLines() {
        // One inner list per text line, and each line differs from the others.
        val twoLines = buildSquigglePoints(widthPx = 300f, heightPx = 48f, lineCount = 2)
        assertThat(twoLines).hasSize(2)
        assertThat(twoLines[0]).isNotEqualTo(twoLines[1])
    }
}
