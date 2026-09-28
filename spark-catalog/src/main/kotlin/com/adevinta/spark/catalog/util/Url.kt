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
@file:Suppress("ktlint:standard:max-line-length")

package com.adevinta.spark.catalog.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

internal fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
}

internal const val GuidelinesUrl: String = "https://spark.adevinta.com"
internal const val ComponentGuidelinesUrl: String = "https://spark.adevinta.com/1186e1705"
internal const val StyleGuidelinesUrl: String = "https://m3.material.io/styles"
internal const val ReleasesUrl: String = "https://github.com/leboncoin/spark-android/releases"
internal const val DocsUrl: String = "https://adevinta.github.io/spark-android"
internal const val SourceUrl: String = "https://github.com/leboncoin/spark-android"
internal const val SparkSourceUrl: String = "https://github.com/leboncoin/spark-android/tree/main/spark/src/main"

internal const val SampleSourceUrl: String = "https://github.com/leboncoin/spark-android/blob/main/catalog/src/main/kotlin/com/adevinta/spark/catalog/examples/samples"

internal const val PackageSummaryUrl: String = "https://adevinta.github.io/spark-android/spark"

internal const val IssueUrl: String = "https://github.com/leboncoin/spark-android/issues?q=is%3Aissue+is%3Aopen+sort%3Aupdated-desc"
internal const val LicensesUrl: String = "https://github.com/leboncoin/spark-android/blob/main/LICENSE"
