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
package com.adevinta.spark.components.progress.tracker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

public class ProgressStepTest {

    @Test
    public fun progressStep_dataModelAndCompatibility() {
        val stepWithSublabel = ProgressStep(
            label = "Primary",
            enabled = true,
            sublabel = "Secondary",
        )
        assertEquals("Primary", stepWithSublabel.label)
        assertEquals("Secondary", stepWithSublabel.sublabel)
        assertEquals("Secondary", stepWithSublabel.secondaryLabel)

        val legacyConstructed = ProgressStep(
            label = "Primary",
            enabled = true,
        )
        assertEquals("Primary", legacyConstructed.label)
        assertNull(legacyConstructed.sublabel)
        assertNull(legacyConstructed.secondaryLabel)
    }
}
