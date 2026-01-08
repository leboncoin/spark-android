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

package com.adevinta.spark.components.fileupload

import android.content.Context
import com.adevinta.spark.R
import kotlin.math.pow
import kotlin.math.roundToLong

internal fun Double.formatNumber(
    context: Context,
    decimals: Int
): String {
    val groupSeparator = context.getString(R.string.spark_groupSeparator).formatWithSpaceIfNeeded()
    val decimalSymbol = context.getString(R.string.spark_decimalSymbol)
    val rounded = formatWithDecimals(decimals)
    val parts = rounded.split('.')

    // Format the integer part
    val formattedIntegerPart = parts[0]
        .reversed()
        .chunked(3)
        .joinToString(groupSeparator)
        .reversed()

    // Format the decimal part
    val decimalPart = if (parts.size > 1) parts[1] else ""
    val formattedDecimalPart = if (decimals > 0) {
        val truncatedDecimals = decimalPart.padEnd(decimals, '0').substring(0, decimals)
        decimalSymbol + truncatedDecimals
    } else {
        ""
    }

    return formattedIntegerPart + formattedDecimalPart
}

/**
 * Returns the given [bytes] size in human-readable format.
 */
internal fun formatFileSize(context: Context, bytes: Long, decimals: Int): String {
    return when {
        bytes < 1024 -> {
            "$bytes ${context.getString(R.string.spark_byte_symbol)}"
        }
        bytes < 1_048_576 -> {
            "${(bytes / 1_024.0).formatNumber(context, decimals)} ${context.getString(R.string.spark_kilobyte_symbol)}"
        }
        bytes < 1.07374182E9 -> {
            "${(bytes / 1_048_576.0).formatNumber(context, decimals)} ${context.getString(R.string.spark_megabyte_symbol)}"
        }
        bytes < 1.09951163E12 -> {
            "${(bytes / 1.07374182E9).formatNumber(context, decimals)} ${context.getString(R.string.spark_gigabyte_symbol)}"
        }
        else -> {
            "${(bytes / 1.09951163E12).formatNumber(context, decimals)} ${context.getString(R.string.spark_terabyte_symbol)}"
        }
    }
}

private fun Double.formatWithDecimals(decimals: Int): String {
    val multiplier = 10.0.pow(decimals)
    val numberAsString = (this * multiplier).roundToLong().toString().padStart(decimals + 1, '0')
    val decimalIndex = numberAsString.length - decimals - 1
    val mainRes = numberAsString.substring(0..decimalIndex)
    val fractionRes = numberAsString.substring(decimalIndex + 1)
    return if (fractionRes.isEmpty()) {
        mainRes
    } else {
        "$mainRes.$fractionRes"
    }
}

/**
 * Workaround for Libres returning an empty string if it contains only a space.
 */
private fun String.formatWithSpaceIfNeeded(): String {
    return ifEmpty { " " }
}
