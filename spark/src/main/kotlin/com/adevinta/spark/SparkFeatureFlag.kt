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
package com.adevinta.spark

/**
 * Flags that will activate debugging features from Spark or features hidden to consumers.
 *
 * @property useSparkTokensHighlighter Highlight visually where the spark tokens are used or not. Setting it to true
 * makes the text in cursive, colors in red/green/blue and shapes in full cut corners.
 * @property useSparkComponentsHighlighter Highlight visually with an overlay where the spark components are used
 * or not. Setting it to true show an overlay on spark components.
 * @property isContainingActivityEdgeToEdge
 * @property useRebrandedShapes Use new button, chips, tags and textfield shapes.
 */
public data class SparkFeatureFlag(
    val useSparkTokensHighlighter: Boolean = false,
    val useSparkComponentsHighlighter: Boolean = false,
    val isContainingActivityEdgeToEdge: Boolean = false,
    val useRebrandedShapes: Boolean = false,
)
