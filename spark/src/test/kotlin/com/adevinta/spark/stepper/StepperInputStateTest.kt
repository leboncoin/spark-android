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

import androidx.compose.foundation.text.input.TextFieldState
import com.adevinta.spark.components.stepper.StepperInputState
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.NumberFormat
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class StepperInputStateTest {

    @Test
    fun `value is null for empty text`() {
        val state = StepperInputState(TextFieldState(""))
        assertNull(state.value)
    }

    @Test
    fun `value parses valid integer`() {
        val state = StepperInputState(TextFieldState("123"))
        assertEquals(123, state.value)
    }

    @Test
    fun `value parses negative integer`() {
        val state = StepperInputState(TextFieldState("-42"))
        assertEquals(-42, state.value)
    }

    @Test
    fun `value is null for lone minus`() {
        val state = StepperInputState(TextFieldState("-"))
        assertNull(state.value)
    }

    @Test
    fun `value is null for non-numeric text`() {
        val state = StepperInputState(TextFieldState("abc"))
        assertNull(state.value)
    }

    @Test
    fun `commitValue clamps and reformats`() {
        val state = StepperInputState(TextFieldState("999"))
        state.commitValue(0..100)
        assertEquals("100", state.textFieldState.text.toString())
        assertEquals(100, state.value)
    }

    @Test
    fun `commitValue formats with locale grouping`() {
        val state = StepperInputState(TextFieldState("12345"))
        state.commitValue(0..99999)
        val expected = NumberFormat.getIntegerInstance().format(12345)
        assertEquals(expected, state.textFieldState.text.toString())
    }

    @Test
    fun `commitValue clears empty to empty`() {
        val state = StepperInputState(TextFieldState(""))
        state.commitValue(0..100)
        assertEquals("", state.textFieldState.text.toString())
    }

    @Test
    fun `commitValue clears invalid to empty`() {
        val state = StepperInputState(TextFieldState("-"))
        state.commitValue(0..100)
        assertEquals("", state.textFieldState.text.toString())
    }

    @Test
    fun `increment from null snaps to range first`() {
        val state = StepperInputState(TextFieldState(""))
        state.increment(step = 1, range = 5..10)
        assertEquals(5, state.value)
    }

    @Test
    fun `increment from value`() {
        val state = StepperInputState(TextFieldState("5"))
        state.increment(step = 2, range = 0..10)
        assertEquals(7, state.value)
    }

    @Test
    fun `increment clamps to range max`() {
        val state = StepperInputState(TextFieldState("9"))
        state.increment(step = 2, range = 0..10)
        assertEquals(10, state.value)
    }

    @Test
    fun `decrement from null snaps to range first`() {
        val state = StepperInputState(TextFieldState(""))
        state.decrement(step = 1, range = 5..10)
        assertEquals(5, state.value)
    }

    @Test
    fun `decrement from value`() {
        val state = StepperInputState(TextFieldState("5"))
        state.decrement(step = 2, range = 0..10)
        assertEquals(3, state.value)
    }

    @Test
    fun `decrement clamps to range min`() {
        val state = StepperInputState(TextFieldState("1"))
        state.decrement(step = 2, range = 0..10)
        assertEquals(0, state.value)
    }

    @Test
    fun `value strips grouping separators before parsing`() {
        // The field stores formatted text like "1,234". The value property must
        // strip separators before calling toIntOrNull, otherwise it returns null
        // and any subsequent commitValue call would clear the field.
        val sep = NumberFormat.getIntegerInstance().let {
            java.text.DecimalFormatSymbols.getInstance().groupingSeparator
        }
        val state = StepperInputState(TextFieldState("1${sep}234"))
        assertEquals(1234, state.value)
    }

    @Test
    fun `commitValue clamps value below range minimum up to range first`() {
        // The existing clamp test only checks clamping above range.last.
        // This covers the symmetric lower-bound case.
        val state = StepperInputState(TextFieldState("-50"))
        state.commitValue(0..100)
        assertEquals("0", state.textFieldState.text.toString())
        assertEquals(0, state.value)
    }

    @Test
    fun `commitValue on value equal to range last leaves value unchanged`() {
        val state = StepperInputState(TextFieldState("100"))
        state.commitValue(0..100)
        assertEquals(100, state.value)
    }

    @Test
    fun `increment then decrement restores original formatted text`() {
        // Verifies that the round-trip through applyStep + formatInteger
        // produces consistent text, not a double-formatted artefact.
        val state = StepperInputState(TextFieldState("5"))
        state.increment(step = 2, range = 0..10)
        state.decrement(step = 2, range = 0..10)
        assertEquals("5", state.textFieldState.text.toString())
    }
}
