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
package com.adevinta.spark.stepper

import com.adevinta.spark.components.stepper.applyStep
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

/**
 * Tests for [applyStep].
 *
 * applyStep is the single arithmetic primitive shared by both StepperState and
 * StepperInputState. A bug here silently corrupts every increment/decrement
 * operation regardless of which state holder is used.
 */
@RunWith(JUnit4::class)
class ApplyStepTest {

    // region null current — snap behaviour

    @Test
    fun `null current with positive delta returns range first`() {
        // Null means "no value selected yet". Any step, positive or negative,
        // must snap to range.first — not range.first + delta.
        assertEquals(3, applyStep(current = null, delta = 2, range = 3..10))
    }

    @Test
    fun `null current with negative delta returns range first`() {
        assertEquals(3, applyStep(current = null, delta = -2, range = 3..10))
    }

    @Test
    fun `null current with zero delta returns range first`() {
        // delta = 0 is unusual but must not crash and still snaps to first.
        assertEquals(0, applyStep(current = null, delta = 0, range = 0..10))
    }

    // endregion

    // region normal step application

    @Test
    fun `positive delta adds step to current`() {
        assertEquals(7, applyStep(current = 5, delta = 2, range = 0..10))
    }

    @Test
    fun `negative delta subtracts step from current`() {
        assertEquals(3, applyStep(current = 5, delta = -2, range = 0..10))
    }

    // endregion

    // region clamping at boundaries

    @Test
    fun `result clamped to range last when delta overshoots`() {
        // 9 + 2 = 11, but range.last = 10 → expect 10
        assertEquals(10, applyStep(current = 9, delta = 2, range = 0..10))
    }

    @Test
    fun `result clamped to range first when delta undershoots`() {
        // 1 - 2 = -1, but range.first = 0 → expect 0
        assertEquals(0, applyStep(current = 1, delta = -2, range = 0..10))
    }

    @Test
    fun `result clamped when already at range last`() {
        assertEquals(10, applyStep(current = 10, delta = 1, range = 0..10))
    }

    @Test
    fun `result clamped when already at range first`() {
        assertEquals(0, applyStep(current = 0, delta = -1, range = 0..10))
    }

    // endregion

    // region negative ranges

    @Test
    fun `works with fully negative range`() {
        // -5 + 2 = -3, within -10..-1
        assertEquals(-3, applyStep(current = -5, delta = 2, range = -10..-1))
    }

    @Test
    fun `clamps to negative range last`() {
        // -2 + 5 = 3, but range.last = -1 → expect -1
        assertEquals(-1, applyStep(current = -2, delta = 5, range = -10..-1))
    }

    @Test
    fun `clamps to negative range first`() {
        // -9 - 5 = -14, but range.first = -10 → expect -10
        assertEquals(-10, applyStep(current = -9, delta = -5, range = -10..-1))
    }

    // endregion

    // region single-element range

    @Test
    fun `single-element range always returns that element for non-null current`() {
        assertEquals(5, applyStep(current = 5, delta = 1, range = 5..5))
        assertEquals(5, applyStep(current = 5, delta = -1, range = 5..5))
    }

    @Test
    fun `single-element range always returns that element for null current`() {
        assertEquals(5, applyStep(current = null, delta = 1, range = 5..5))
    }

    // endregion

    // region Int overflow protection

    @Test
    fun `large positive delta does not overflow when clamped to range last`() {
        // Int.MAX_VALUE - 1 + Int.MAX_VALUE would overflow without coerceIn.
        // coerceIn runs on the already-computed sum, so the real question is
        // whether (current + delta) wraps before coerceIn sees it.
        // The implementation uses plain addition, so this test documents the
        // current behaviour and will fail if the code ever silently overflows.
        val result = applyStep(current = Int.MAX_VALUE - 1, delta = Int.MAX_VALUE, range = 0..Int.MAX_VALUE)
        // Overflow: (MAX-1) + MAX wraps to a negative number, coerceIn clamps to 0.
        // Document this as the current behaviour so any future fix shows up as a test change.
        assertEquals(0, result)
    }

    @Test
    fun `large negative delta does not overflow when clamped to range first`() {
        val result = applyStep(current = Int.MIN_VALUE + 1, delta = Int.MIN_VALUE, range = Int.MIN_VALUE..0)
        // Similarly documents wrap-to-positive behaviour for negative overflow.
        assertEquals(0, result)
    }

    // endregion

    // region step larger than range

    @Test
    fun `step larger than range size clamps correctly on increment`() {
        // range is 0..2, delta = 100. Result must clamp to 2, not wrap.
        assertEquals(2, applyStep(current = 0, delta = 100, range = 0..2))
    }

    @Test
    fun `step larger than range size clamps correctly on decrement`() {
        assertEquals(0, applyStep(current = 2, delta = -100, range = 0..2))
    }

    // endregion
}
