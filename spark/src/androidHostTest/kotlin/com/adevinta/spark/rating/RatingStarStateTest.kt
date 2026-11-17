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
package com.adevinta.spark.rating

import com.adevinta.spark.components.rating.RatingStarState
import com.adevinta.spark.components.rating.RatingStarState.Empty
import com.adevinta.spark.components.rating.RatingStarState.Full
import com.adevinta.spark.components.rating.RatingStarState.Half
import org.junit.Test
import kotlin.test.assertEquals

class RatingStarStateTest {

    @Test
    fun testBooleanInput() {
        assertEquals(RatingStarState(true), Full)
        assertEquals(RatingStarState(false), Empty)
    }

    @Test
    fun testIntInput() {
        assertEquals(RatingStarState(1), Full)
        assertEquals(RatingStarState(0), Empty)
    }

    @Test
    fun testFloatRangeInput() {
        assertEquals(RatingStarState(0.0f), Empty)
        assertEquals(RatingStarState(0.1f), Empty)
        assertEquals(RatingStarState(0.25f), Empty)
        assertEquals(RatingStarState(0.3f), Half)
        assertEquals(RatingStarState(0.5f), Half)
        assertEquals(RatingStarState(0.75f), Half)
        assertEquals(RatingStarState(0.8f), Full)
        assertEquals(RatingStarState(1.0f), Full)
    }

    @Test
    fun testDoubleRangeInput() {
        assertEquals(RatingStarState(0.0), Empty)
        assertEquals(RatingStarState(0.1), Empty)
        assertEquals(RatingStarState(0.25), Empty)
        assertEquals(RatingStarState(0.3), Half)
        assertEquals(RatingStarState(0.5), Half)
        assertEquals(RatingStarState(0.75), Half)
        assertEquals(RatingStarState(0.8), Full)
        assertEquals(RatingStarState(1.0), Full)
    }
}
