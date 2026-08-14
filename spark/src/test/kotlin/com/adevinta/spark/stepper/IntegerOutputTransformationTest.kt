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
import com.adevinta.spark.components.stepper.internal.IntegerOutputTransformation
import com.adevinta.spark.components.stepper.internal.groupingSeparator
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.NumberFormat
import kotlin.test.assertEquals

/**
 * Tests for [IntegerOutputTransformation].
 *
 * This transformation has zero dedicated test coverage. It runs on every
 * render of the input field, so a formatting bug would surface visually on
 * every keystroke — high user-facing impact.
 *
 * The helper [applyOutputTransformation] mirrors the pattern used in
 * IntegerInputTransformationTest so both files stay consistent.
 */
@RunWith(RobolectricTestRunner::class)
class IntegerOutputTransformationTest {

    private val transformation = IntegerOutputTransformation()

    /**
     * Applies [IntegerOutputTransformation.transformOutput] to a [TextFieldState]
     * with the given [rawText] and returns the transformed output as a string.
     *
     * OutputTransformation is called by the framework during the draw phase.
     * We simulate it by invoking transformOutput directly inside a state.edit
     * block with the same TextFieldBuffer the framework would pass.
     */
    private fun applyOutputTransformation(rawText: String): String {
        val state = TextFieldState(rawText)
        // OutputTransformation operates on a TextFieldBuffer that mirrors the
        // underlying text. We call the internal function via edit { } to obtain
        // an equivalent buffer.
        state.edit {
            with(transformation) { transformOutput() }
        }
        return state.text.toString()
    }

    // region no-op cases — transformation must leave the buffer unchanged

    @Test
    fun `empty string is left unchanged`() {
        // toIntOrNull() returns null → early return, buffer untouched.
        assertEquals("", applyOutputTransformation(""))
    }

    @Test
    fun `lone minus sign is left unchanged`() {
        // "-".stripGroupingSeparators().toIntOrNull() → null → no replacement.
        assertEquals("-", applyOutputTransformation("-"))
    }

    @Test
    fun `non-numeric text is left unchanged`() {
        assertEquals("abc", applyOutputTransformation("abc"))
    }

    @Test
    fun `single digit is left unchanged`() {
        // "5" already formats to "5", so formatted == raw → no replacement.
        assertEquals("5", applyOutputTransformation("5"))
    }

    @Test
    fun `three-digit number below grouping threshold is left unchanged`() {
        assertEquals("999", applyOutputTransformation("999"))
    }

    // endregion

    // region grouping separator insertion

    @Test
    fun `four-digit number receives grouping separator`() {
        val sep = groupingSeparator
        val expected = "1${sep}000"
        assertEquals(expected, applyOutputTransformation("1000"))
    }

    @Test
    fun `seven-digit number receives two grouping separators`() {
        val expected = NumberFormat.getIntegerInstance().format(1_234_567)
        assertEquals(expected, applyOutputTransformation("1234567"))
    }

    @Test
    fun `already-formatted input is idempotent`() {
        // If the buffer already contains grouping separators the transformation
        // must not double-insert them or corrupt the value.
        val sep = groupingSeparator
        val alreadyFormatted = "1${sep}000"
        assertEquals(alreadyFormatted, applyOutputTransformation(alreadyFormatted))
    }

    // endregion

    // region negative numbers

    @Test
    fun `negative single digit is left unchanged`() {
        assertEquals("-5", applyOutputTransformation("-5"))
    }

    @Test
    fun `negative four-digit number receives grouping separator`() {
        val expected = NumberFormat.getIntegerInstance().format(-1000)
        assertEquals(expected, applyOutputTransformation("-1000"))
    }

    // endregion

    // region boundary values

    @Test
    fun `zero is left unchanged`() {
        assertEquals("0", applyOutputTransformation("0"))
    }

    @Test
    fun `Int MAX_VALUE formats without throwing`() {
        val expected = NumberFormat.getIntegerInstance().format(Int.MAX_VALUE)
        assertEquals(expected, applyOutputTransformation(Int.MAX_VALUE.toString()))
    }

    @Test
    fun `Int MIN_VALUE formats without throwing`() {
        val expected = NumberFormat.getIntegerInstance().format(Int.MIN_VALUE)
        assertEquals(expected, applyOutputTransformation(Int.MIN_VALUE.toString()))
    }

    // endregion
}
