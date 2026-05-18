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
package com.adevinta.spark.placeholder

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableValue
import androidx.compose.ui.platform.ValueElement
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.adevinta.spark.components.placeholder.PlaceholderHighlight
import com.adevinta.spark.components.placeholder.fade
import com.adevinta.spark.components.placeholder.placeholder
import com.adevinta.spark.components.placeholder.shimmer
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowPixelCopy

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    instrumentedPackages = [
        "androidx.loader.content",
    ],
    shadows = [ShadowPixelCopy::class],
    sdk = [33],
)
class PlaceholderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun before() {
        isDebugInspectorInfoEnabled = true
    }

    @After
    fun after() {
        isDebugInspectorInfoEnabled = false
    }

    @Test
    fun placeholder_inspectableParameter1() {
        val highlight = PlaceholderHighlight.shimmer(Color.Red)
        val modifier = Modifier.placeholder(
            visible = true,
            color = Color.Blue,
            highlight = highlight,
            shape = CircleShape,
        ) as InspectableValue

        assertThat(modifier.nameFallback).isEqualTo("placeholder")
        assertThat(modifier.valueOverride).isEqualTo(true)
        assertThat(modifier.inspectableElements.asIterable()).containsExactly(
            ValueElement("visible", true),
            ValueElement("color", Color.Blue),
            ValueElement("highlight", highlight),
            ValueElement("shape", CircleShape),
        )
    }

    @Test
    fun placeholder_inspectableParameter2() {
        val highlight = PlaceholderHighlight.fade(Color.Red)
        val modifier = Modifier.placeholder(
            visible = true,
            color = Color.Blue,
            highlight = highlight,
            shape = CircleShape,
        ) as InspectableValue

        assertThat(modifier.nameFallback).isEqualTo("placeholder")
        assertThat(modifier.valueOverride).isEqualTo(true)
        assertThat(modifier.inspectableElements.asIterable()).containsExactly(
            ValueElement("visible", true),
            ValueElement("color", Color.Blue),
            ValueElement("highlight", highlight),
            ValueElement("shape", CircleShape),
        )
    }
}
