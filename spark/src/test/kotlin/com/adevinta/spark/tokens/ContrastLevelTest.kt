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
package com.adevinta.spark.tokens

import android.app.UiModeManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class ContrastLevelTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    @Config(sdk = [33])
    fun `contrastLevel returns fallback on API 33`() {
        val fallbackValue = 0.5f
        val result = contrastLevel(context) { fallbackValue }
        assertEquals(fallbackValue, result)
    }

    @Test
    @Config(sdk = [34])
    fun `contrastLevel returns system contrast on API 34`() {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val shadow = shadowOf(uiModeManager)
        shadow.setContrast(0.75f)

        val result = contrastLevel(context) { 0f }
        assertEquals(0.75f, result)
    }

    @Test
    @Config(sdk = [34])
    fun `contrastLevel returns fallback when UiModeManager returns null contrast on API 34`() {
        val fallbackValue = -0.5f
        val result = contrastLevel(context) { fallbackValue }
        assertEquals(true, result in -1f..1f)
    }

    @Test
    @Config(sdk = [34])
    fun `contrastLevel uses default fallback of 0f`() {
        val result = contrastLevel(context)
        assertEquals(true, result in -1f..1f)
    }
}
