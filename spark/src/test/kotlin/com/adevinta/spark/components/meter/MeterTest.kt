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
package com.adevinta.spark.components.meter

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MeterTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun circular_meter_displays_value_label_semantics() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    progress = 0.75f,
                    content = CircularMeterContent.ValueLabel("75%", "Complete"),
                    intent = MeterIntent.Support,
                    size = MeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf("75%, Complete, 75 percent"),
            ),
        ).assertExists()
    }

    @Test
    fun circular_small_renders_with_accessibility_description() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.CircularSmall(progress = 0.5f, intent = MeterIntent.Main)
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf("50 percent"),
            ),
        ).assertExists()
    }

    @Test
    fun progress_above_one_is_clamped() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    progress = 1.5f,
                    content = CircularMeterContent.Value("100%"),
                    intent = MeterIntent.Support,
                    size = MeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf("100%, 100 percent"),
            ),
        ).assertExists()
    }
}