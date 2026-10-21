/*
 * Copyright (c) 2026 Adevinta
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
package com.adevinta.spark.catalog.util

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.testing.CatalogTestTheme
import com.adevinta.spark.catalog.testing.ComponentActivityRule
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
internal class UrlTest {

    @get:Rule(order = 0)
    val componentActivityRule = ComponentActivityRule()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val application = ApplicationProvider.getApplicationContext<Application>()

    @Before
    fun failWhenNoActivityHandlesTheIntent() {
        shadowOf(application).checkActivities(true)
    }

    private fun activity(): ComponentActivity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()

    @Test
    fun openUrlReturnsFalseWhenNoBrowserIsInstalled() {
        assertThat(activity().openUrl(TestUrl)).isFalse()
    }

    @Test
    fun openUrlStartsTheBrowserWhenOneIsInstalled() {
        val browser = ComponentName("com.example.browser", "com.example.browser.BrowserActivity")
        with(shadowOf(application.packageManager)) {
            addActivityIfNotPresent(browser)
            addIntentFilterForActivity(
                browser,
                IntentFilter(Intent.ACTION_VIEW).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addDataScheme("https")
                },
            )
        }

        val activity = activity()
        assertThat(activity.openUrl(TestUrl)).isTrue()
        assertThat(shadowOf(activity).nextStartedActivity.dataString).isEqualTo(TestUrl)
    }

    @Test
    fun urlOpenerShowsSnackbarWhenNoBrowserIsInstalled() {
        lateinit var openUrl: (String) -> Unit
        composeTestRule.setContent {
            CatalogTestTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                openUrl = rememberUrlOpener(snackbarHostState)
                SnackbarHost(snackbarHostState)
            }
        }

        composeTestRule.runOnIdle { openUrl(TestUrl) }

        composeTestRule.onNodeWithText(application.getString(R.string.url_open_failed)).assertIsDisplayed()
    }
}

private const val TestUrl = "https://spark.adevinta.com"
