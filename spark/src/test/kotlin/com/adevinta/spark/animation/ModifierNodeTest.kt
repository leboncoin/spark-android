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
package com.adevinta.spark.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.tab.TabGroupDefaults.tabIndicatorOffset
import com.adevinta.spark.components.tab.TabPosition
import com.adevinta.spark.tokens.LocalWindowSizeClass
import com.adevinta.spark.tokens.bodyWidth
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ModifierNodeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    @Config(qualifiers = "w1200dp-h800dp")
    fun bodyWidth_followsWindowSizeClass() {
        var windowSizeClass by mutableStateOf(WindowSizeClass(minWidthDp = 840, minHeightDp = 480))
        composeTestRule.setContent {
            CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                Box(Modifier.width(1000.dp)) {
                    Box(
                        Modifier
                            .bodyWidth()
                            .fillMaxWidth()
                            .testTag(Tag),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag(Tag)
            .assertWidthIsEqualTo(840.dp)
            .assertLeftPositionInRootIsEqualTo(80.dp)

        windowSizeClass = WindowSizeClass(minWidthDp = 600, minHeightDp = 480)

        composeTestRule.onNodeWithTag(Tag)
            .assertWidthIsEqualTo(1000.dp)
            .assertLeftPositionInRootIsEqualTo(0.dp)
    }

    @Test
    fun tabIndicatorOffset_animatesToSelectedTab() {
        var tabPosition by mutableStateOf(TabPosition(left = 0.dp, width = 100.dp))
        composeTestRule.setContent {
            Box(Modifier.width(400.dp)) {
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPosition)
                        .height(2.dp)
                        .testTag(Tag),
                )
            }
        }

        composeTestRule.onNodeWithTag(Tag)
            .assertWidthIsEqualTo(100.dp)
            .assertLeftPositionInRootIsEqualTo(0.dp)

        tabPosition = TabPosition(left = 150.dp, width = 200.dp)

        composeTestRule.onNodeWithTag(Tag)
            .assertWidthIsEqualTo(200.dp)
            .assertLeftPositionInRootIsEqualTo(150.dp)
    }

    @Test
    fun shake_movesContentThenReturnsToRest() {
        val controller = ShakeController()
        composeTestRule.setContent {
            Box(Modifier.width(400.dp)) {
                Box(
                    Modifier
                        .shake(controller)
                        .size(50.dp)
                        .testTag(Tag),
                )
            }
        }
        composeTestRule.onNodeWithTag(Tag).assertLeftPositionInRootIsEqualTo(0.dp)

        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.runOnIdle {
            controller.shake(ShakeConfig(iterations = 2, intensity = 1_000f, translateX = 40f))
            Snapshot.sendApplyNotifications()
        }
        repeat(6) { composeTestRule.mainClock.advanceTimeByFrame() }
        val left = composeTestRule.onNodeWithTag(Tag).getUnclippedBoundsInRoot().left
        assertNotEquals(0.dp, left)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.onNodeWithTag(Tag).assertLeftPositionInRootIsEqualTo(0.dp)
    }

    @OptIn(ExperimentalSparkApi::class)
    @Test
    fun pulse_drawsAndToggles() {
        var enabled by mutableStateOf(true)
        composeTestRule.setContent {
            Box(
                Modifier
                    .pulse(brush = SolidColor(Color.Red), enabled = enabled)
                    .size(50.dp)
                    .testTag(Tag),
            )
        }
        composeTestRule.onNodeWithTag(Tag).assertIsDisplayed()

        enabled = false
        composeTestRule.onNodeWithTag(Tag).assertIsDisplayed()

        enabled = true
        composeTestRule.onNodeWithTag(Tag).assertIsDisplayed()
    }

    private companion object {
        const val Tag = "node"
    }
}
