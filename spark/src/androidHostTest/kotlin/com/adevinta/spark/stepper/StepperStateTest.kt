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

import com.adevinta.spark.components.stepper.StepperState
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class StepperStateTest {

    @Test
    fun `initial null value`() {
        val state = StepperState(initialValue = null, range = 0..10, step = 1)
        assertNull(state.value)
    }

    @Test
    fun `initial non-null value`() {
        val state = StepperState(initialValue = 5, range = 0..10, step = 1)
        assertEquals(5, state.value)
    }

    @Test
    fun `increment from null snaps to range first`() {
        val state = StepperState(initialValue = null, range = 2..10, step = 2)
        state.increment()
        assertEquals(2, state.value)
    }

    @Test
    fun `decrement from null snaps to range first`() {
        val state = StepperState(initialValue = null, range = 2..10, step = 2)
        state.decrement()
        assertEquals(2, state.value)
    }

    @Test
    fun `increment from value`() {
        val state = StepperState(initialValue = 4, range = 0..10, step = 2)
        state.increment()
        assertEquals(6, state.value)
    }

    @Test
    fun `decrement from value`() {
        val state = StepperState(initialValue = 4, range = 0..10, step = 2)
        state.decrement()
        assertEquals(2, state.value)
    }

    @Test
    fun `increment clamps to range max`() {
        val state = StepperState(initialValue = 9, range = 0..10, step = 2)
        state.increment()
        assertEquals(10, state.value)
    }

    @Test
    fun `decrement clamps to range min`() {
        val state = StepperState(initialValue = 1, range = 0..10, step = 2)
        state.decrement()
        assertEquals(0, state.value)
    }

    @Test
    fun `canIncrement true when below max`() {
        val state = StepperState(initialValue = 5, range = 0..10, step = 1)
        assertTrue(state.canIncrement)
    }

    @Test
    fun `canIncrement false when at max`() {
        val state = StepperState(initialValue = 10, range = 0..10, step = 1)
        assertFalse(state.canIncrement)
    }

    @Test
    fun `canDecrement true when above min`() {
        val state = StepperState(initialValue = 5, range = 0..10, step = 1)
        assertTrue(state.canDecrement)
    }

    @Test
    fun `canDecrement false when at min`() {
        val state = StepperState(initialValue = 0, range = 0..10, step = 1)
        assertFalse(state.canDecrement)
    }

    @Test
    fun `canIncrement true when value is null and range is non-empty`() {
        val state = StepperState(initialValue = null, range = 0..10, step = 1)
        assertTrue(state.canIncrement)
    }

    @Test
    fun `canDecrement true when value is null and range is non-empty`() {
        val state = StepperState(initialValue = null, range = 0..10, step = 1)
        assertTrue(state.canDecrement)
    }

    @Test
    fun `canIncrement false when value is null and range is empty`() {
        val state = StepperState(initialValue = null, range = 5..5, step = 1)
        assertFalse(state.canIncrement)
    }

    @Test
    fun `canDecrement false when value is null and range is empty`() {
        // Symmetric to the canIncrement empty-range case; the null branch checks
        // range.first < range.last, which is false for a single-element range.
        val state = StepperState(initialValue = null, range = 5..5, step = 1)
        assertFalse(state.canDecrement)
    }

    @Test
    fun `increment then decrement from null returns range first both times`() {
        // Both operations on a null value snap to range.first rather than
        // applying the step, so the value stays at range.first after each call.
        val state = StepperState(initialValue = null, range = 4..10, step = 2)
        state.increment()
        assertEquals(4, state.value)
        state.decrement()
        // Now value is 4 (non-null), so decrement applies -step and clamps.
        assertEquals(4, state.value)
    }

    @Test
    fun `value out of range on construction is not clamped by the state itself`() {
        // StepperState does not clamp initialValue — clamping is the caller's
        // responsibility. This test documents that contract so a future
        // "helpful" clamp in the constructor would break it intentionally.
        val state = StepperState(initialValue = 20, range = 0..10, step = 1)
        assertEquals(20, state.value)
    }
}
