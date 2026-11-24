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
package com.adevinta.spark.components.chips

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.width
import com.adevinta.spark.PreviewTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class ChipSizeAnimationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun null_spec_measures_the_wide_width_on_the_next_frame() {
        setChipAndGrowMaxWidth { modifier ->
            Chip(onClick = {}, modifier = modifier, sizeAnimationSpec = null) {
                Spacer(Modifier.width(LabelWidth))
            }
        }

        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.onNodeWithTag(ChipTag).assertWidthIsEqualTo(NaturalChipWidth)

        composeTestRule.mainClock.advanceTimeBy(SettleTimeMillis)
        composeTestRule.onNodeWithTag(ChipTag).assertWidthIsEqualTo(NaturalChipWidth)
    }

    @Test
    fun default_spec_does_not_reach_the_wide_width_on_the_next_frame() {
        setChipAndGrowMaxWidth { modifier ->
            Chip(onClick = {}, modifier = modifier) {
                Spacer(Modifier.width(LabelWidth))
            }
        }

        composeTestRule.mainClock.advanceTimeByFrame()
        val frameWidth = composeTestRule.onNodeWithTag(ChipTag).getUnclippedBoundsInRoot().width
        assertTrue(
            frameWidth < NaturalChipWidth,
            "Expected the chip to be narrower than $NaturalChipWidth on the next frame, but it was $frameWidth",
        )

        composeTestRule.mainClock.advanceTimeBy(SettleTimeMillis)
        composeTestRule.onNodeWithTag(ChipTag).assertWidthIsEqualTo(NaturalChipWidth)
    }

    /**
     * Shows [chip] in a box whose max width starts at [NarrowMaxWidth], settles it, then grows the max width to
     * [WideMaxWidth] without advancing the clock.
     */
    private fun setChipAndGrowMaxWidth(chip: @Composable (Modifier) -> Unit) {
        var maxWidth by mutableStateOf(NarrowMaxWidth)
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            PreviewTheme {
                Box(modifier = Modifier.widthIn(max = maxWidth)) {
                    chip(Modifier.testTag(ChipTag))
                }
            }
        }
        composeTestRule.mainClock.advanceTimeBy(SettleTimeMillis)
        composeTestRule.onNodeWithTag(ChipTag).assertWidthIsEqualTo(NarrowMaxWidth)

        composeTestRule.runOnIdle {
            maxWidth = WideMaxWidth
            // Without this, the write reaches the recomposer one frame late, and the next frame still lays out
            // the narrow width.
            Snapshot.sendApplyNotifications()
        }
    }

    private companion object {
        const val ChipTag = "chip"
        const val SettleTimeMillis = 1_000L
        val LabelWidth: Dp = 200.dp

        // The label plus the 8dp horizontal chip padding on each side.
        val NaturalChipWidth: Dp = LabelWidth + 16.dp
        val NarrowMaxWidth: Dp = 60.dp
        val WideMaxWidth: Dp = 240.dp
    }
}
