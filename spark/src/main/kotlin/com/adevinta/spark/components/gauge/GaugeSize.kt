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

/**
 * Defines the available sizes for the SegmentedGauge component.
 *
 * Note that each parameters
 *
 * @property width The width of each segment in density-independent pixels.
 * @property height The height of each segment in density-independent pixels.
 * @property indicatorSize The size of the indicator in density-independent pixels.
 */
public enum class GaugeSize(
    internal val width: Int,
    internal val height: Int,
    internal val indicatorSize: Int,
) {
    /**
     * Medium size gauge with larger segments and indicator.
     */
    Medium(width = 34, height = 12, indicatorSize = 16),

    /**
     * Small size gauge with compact segments and indicator.
     */
    Small(width = 24, height = 8, indicatorSize = 12);
}
