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
package com.adevinta.spark.catalog.ui.navigation

import android.app.assist.AssistContent
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey

/**
 * To be used inside [android.app.Activity.onProvideAssistContent]. Sets the `spark://` deep link of the top entry of
 * the visible tab back stack as the web URI of the assist content.
 *
 * In the recents screen, if the current destination has a deep link, the system allows the user to copy the URL of it.
 *
 * Context:
 *  [Recents URL sharing](https://developer.android.com/guide/components/activities/recents#url-sharing)
 *  [onProvideAssistContent](https://developer.android.com/reference/android/app/Activity#onProvideAssistContent(android.app.assist.AssistContent))
 */
internal fun List<NavKey>.provideAssistContent(outContent: AssistContent) {
    val key = lastOrNull() ?: return
    val webUri = CatalogDeepLinks.uriFor(key) ?: return
    outContent.webUri = webUri.toUri()
}
