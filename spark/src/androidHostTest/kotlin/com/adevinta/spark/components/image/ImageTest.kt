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
package com.adevinta.spark.components.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tools.SparkExceptionHandler
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val reported = mutableListOf<Throwable>()
    private val recordingHandler = SparkExceptionHandler { reported += it }

    @Test
    fun zero_sized_constraints_are_not_reported() {
        setImage(width = 0.dp, height = 0.dp, imageModifier = Modifier.fillMaxSize())

        assertEquals(emptyList<Throwable>(), reported)
    }

    @Test
    fun zero_width_constraint_is_not_reported() {
        setImage(width = 0.dp, height = 100.dp, imageModifier = Modifier.fillMaxSize())

        assertEquals(emptyList<Throwable>(), reported)
    }

    @Test
    fun zero_height_constraint_is_not_reported() {
        setImage(width = 100.dp, height = 0.dp, imageModifier = Modifier.fillMaxSize())

        assertEquals(emptyList<Throwable>(), reported)
    }

    @Test
    fun defined_size_is_not_reported() {
        setImage(width = 100.dp, height = 100.dp, imageModifier = Modifier.fillMaxSize())

        assertEquals(emptyList<Throwable>(), reported)
    }

    @Test
    fun missing_minimum_size_is_reported() {
        setImage(width = 100.dp, height = 100.dp, imageModifier = Modifier.wrapContentSize())

        assertEquals(
            listOf(
                "Image must have a minimum width but has minWidth: 0 for model: null",
                "Image must have a minimum height but has minHeight: 0 for model: null",
            ),
            reported.map { it.message }.distinct(),
        )
    }

    private fun setImage(width: Dp, height: Dp, imageModifier: Modifier) {
        composeTestRule.setContent {
            SparkTheme(exceptionHandler = recordingHandler) {
                Box(modifier = Modifier.size(width = width, height = height)) {
                    Image(model = null, contentDescription = null, modifier = imageModifier)
                }
            }
        }
        composeTestRule.waitForIdle()
    }
}
