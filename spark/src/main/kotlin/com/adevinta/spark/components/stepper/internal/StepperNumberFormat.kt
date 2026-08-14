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
package com.adevinta.spark.components.stepper.internal

import java.text.DecimalFormatSymbols
import java.text.NumberFormat

internal val integerNumberFormat: NumberFormat = NumberFormat.getIntegerInstance()

internal val groupingSeparator: Char = DecimalFormatSymbols.getInstance().groupingSeparator

internal fun String.stripGroupingSeparators(): String = replace(groupingSeparator.toString(), "")

internal fun Int.formatInteger(): String = integerNumberFormat.format(this)

/**
 * Builds the spoken state description for a stepper value. Joins the value and [suffix] with a
 * single space so a screen reader reads "5 kg", not "5kg" or "5null". Falls back to [emptyText]
 * when [value] is null.
 */
internal fun stepperStateDescription(value: Int?, suffix: String?, emptyText: String): String {
    if (value == null) return emptyText
    val trimmedSuffix = suffix?.trim().orEmpty()
    return if (trimmedSuffix.isEmpty()) value.toString() else "$value $trimmedSuffix"
}
