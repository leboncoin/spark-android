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

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.meter.circular.CircleMeterSize
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
    fun circular_value_announces_formatted_value_as_state_description() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    value = 75f,
                    content = CircularMeterContent.Value(),
                    intent = MeterIntent.Support,
                    size = CircleMeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "75%"),
        ).assertExists()
    }

    @Test
    fun circular_value_label_announces_value_and_label() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    value = 75f,
                    content = CircularMeterContent.ValueLabel(label = "Complete"),
                    intent = MeterIntent.Support,
                    size = CircleMeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "75%, Complete"),
        ).assertExists()
    }

    @Test
    fun circular_small_renders_with_accessibility_description() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.CircularSmall(value = 50f, intent = MeterIntent.Main)
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
    fun value_above_range_is_clamped() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    value = 150f,
                    content = CircularMeterContent.Value(),
                    intent = MeterIntent.Support,
                    size = CircleMeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "100%"),
        ).assertExists()
    }

    @Test
    fun circular_small_custom_range_announces_correct_percent() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.CircularSmall(
                    value = 50f,
                    range = 0f..200f,
                    intent = MeterIntent.Main,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription,
                listOf("25 percent"),
            ),
        ).assertExists()
    }

    @Test
    fun progress_bar_range_info_exposes_value_and_range() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    value = 120f,
                    range = 0f..200f,
                    content = CircularMeterContent.Value(formatValue = { "€${it.toInt()}" }),
                    intent = MeterIntent.Main,
                    size = CircleMeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo(current = 120f, range = 0f..200f)),
        ).assertExists()
    }

    @Test
    fun circular_value_with_suffix_announces_value_and_suffix() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    value = 120f,
                    range = 0f..200f,
                    content = CircularMeterContent.Value(formatValue = { "${it.toInt()}" }),
                    suffix = " euros",
                    intent = MeterIntent.Main,
                    size = CircleMeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "120 euros"),
        ).assertExists()
    }

    @Test
    fun circular_value_no_double_announcement_on_non_hundred_range() {
        composeTestRule.setContent {
            PreviewTheme {
                Meter.Circular(
                    value = 70f,
                    range = 0f..79f,
                    content = CircularMeterContent.Value(),
                    intent = MeterIntent.Support,
                    size = CircleMeterSize.Large,
                )
            }
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "70%"),
        ).assertExists()
    }
}
