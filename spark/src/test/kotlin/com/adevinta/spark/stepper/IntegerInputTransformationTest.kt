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
import androidx.compose.foundation.text.input.insert
import androidx.compose.ui.text.input.KeyboardType
import com.adevinta.spark.components.stepper.internal.IntegerInputTransformation
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.DecimalFormatSymbols
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class IntegerInputTransformationTest {

    private fun applyTransformation(
        initialText: String = "",
        insertText: String,
        allowNegative: Boolean = true,
    ): String {
        val transformation = IntegerInputTransformation(allowNegative)
        val state = TextFieldState(initialText)
        state.edit {
            insert(length, insertText)
            with(transformation) { transformInput() }
        }
        return state.text.toString()
    }

    @Test
    fun `digits accepted`() {
        assertEquals("123", applyTransformation(insertText = "123"))
    }

    @Test
    fun `letters rejected`() {
        assertEquals("", applyTransformation(insertText = "abc"))
    }

    @Test
    fun `mixed content rejected`() {
        assertEquals("", applyTransformation(insertText = "12a3"))
    }

    @Test
    fun `minus accepted when allowNegative`() {
        assertEquals("-", applyTransformation(insertText = "-", allowNegative = true))
    }

    @Test
    fun `minus with digits accepted when allowNegative`() {
        assertEquals("-5", applyTransformation(insertText = "-5", allowNegative = true))
    }

    @Test
    fun `minus rejected when not allowNegative`() {
        assertEquals("", applyTransformation(insertText = "-", allowNegative = false))
    }

    @Test
    fun `minus with digits rejected when not allowNegative`() {
        assertEquals("", applyTransformation(insertText = "-5", allowNegative = false))
    }

    @Test
    fun `multiple minus signs rejected`() {
        assertEquals("", applyTransformation(insertText = "--5"))
    }

    @Test
    fun `minus not at position 0 rejected`() {
        assertEquals("1", applyTransformation(initialText = "1", insertText = "-"))
    }

    @Test
    fun `grouping separator accepted`() {
        val sep = DecimalFormatSymbols.getInstance().groupingSeparator
        val input = "1${sep}234"
        assertEquals(input, applyTransformation(insertText = input))
    }

    @Test
    fun `empty string accepted`() {
        assertEquals("", applyTransformation(insertText = ""))
    }

    @Test
    fun `keyboard type is Number`() {
        val transformation = IntegerInputTransformation(allowNegative = true)
        assertEquals(KeyboardType.Number, transformation.keyboardOptions.keyboardType)
    }
}
