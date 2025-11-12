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

import com.adevinta.spark.components.stepper.internal.stepperInputValidator
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class StepperInputValidatorTest {

    @Test
    fun `stepperInputValidator should throw exception when step is zero`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            stepperInputValidator(step = 0, range = 0..10)
        }
        assertEquals("A step can only be a positive integer, but was 0", exception.message)
    }

    @Test
    fun `stepperInputValidator should throw exception when step is not positive`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            stepperInputValidator(step = -1, range = 0..10)
        }
        assertEquals("A step can only be a positive integer, but was -1", exception.message)
    }

    @Test
    fun `stepperInputValidator should throw exception when range start is not multiple of step`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            stepperInputValidator(step = 2, range = 1..10)
        }
        assertEquals("The min range must be a multiple of the step, but has 1  remaining", exception.message)
    }

    @Test
    fun `stepperInputValidator should throw exception when range end is not multiple of step`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            stepperInputValidator(step = 2, range = 0..9)
        }
        assertEquals("The max range must be a multiple of the step, but has 1  remaining", exception.message)
    }

    @Test
    fun `stepperInputValidator should pass with all ranges possible when step 1`() {
        stepperInputValidator(step = 1, range = 1..10)
        stepperInputValidator(step = 1, range = 0..10)
    }

    @Test
    fun `stepperInputValidator should pass with valid inputs`() {
        stepperInputValidator(step = 2, range = 0..10)
    }
}
