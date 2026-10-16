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

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.adevinta.spark.catalog.R
import com.adevinta.spark.components.snackbars.SnackbarHostState
import kotlinx.coroutines.launch

/**
 * Opens [url] in a browser and returns `false` when no app on the device can open it.
 */
public fun Context.openUrl(url: String): Boolean = try {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    true
} catch (_: ActivityNotFoundException) {
    false
}

@Composable
internal fun rememberUrlOpener(snackbarHostState: SnackbarHostState): (url: String) -> Unit {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val failureMessage = stringResource(R.string.url_open_failed)
    return remember(context, snackbarHostState, coroutineScope, failureMessage) {
        { url ->
            if (!context.openUrl(url)) {
                coroutineScope.launch { snackbarHostState.showSnackbar(failureMessage) }
            }
        }
    }
}

public const val GuidelinesUrl: String = "https://spark.adevinta.com"
public const val ComponentGuidelinesUrl: String = "https://spark.adevinta.com/1186e1705"
public const val StyleGuidelinesUrl: String = "https://m3.material.io/styles"
public const val ReleasesUrl: String = "https://github.com/leboncoin/spark-android/releases"
public const val DocsUrl: String = "https://adevinta.github.io/spark-android"
public const val SourceUrl: String = "https://github.com/leboncoin/spark-android"
public const val SparkSourceUrl: String = "https://github.com/leboncoin/spark-android/tree/main/spark/src/main"

public const val SampleSourceUrl: String = "https://github.com/leboncoin/spark-android/blob/main/catalog/src/main/kotlin/com/adevinta/spark/catalog/examples/samples"

public const val PackageSummaryUrl: String = "https://adevinta.github.io/spark-android/spark"

public const val IssueUrl: String = "https://github.com/leboncoin/spark-android/issues?q=is%3Aissue+is%3Aopen+sort%3Aupdated-desc"
public const val LicensesUrl: String = "https://github.com/leboncoin/spark-android/blob/main/LICENSE"
