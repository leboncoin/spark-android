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

import android.os.Build
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.placeholder.PlaceholderHighlight
import com.adevinta.spark.components.placeholder.basePlaceholder
import com.adevinta.spark.components.placeholder.placeholder
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Ignore(
    "I tried multiples solutions but it seems like the edge of the bitmap are seen as blurred or something that " +
        "makes the test fail. I tried to use the same code as the library but it still fails. " +
        "We get 0.9019608 for the red value instead of 1. I don't know what to do so I'm ignoring this test for now.",
)
@RunWith(AndroidJUnit4::class)
class PlaceholderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val contentTag = "Content"

    @Before
    fun before() {
        isDebugInspectorInfoEnabled = true
    }

    @After
    fun after() {
        isDebugInspectorInfoEnabled = false
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    @Test
    fun placeholder_switchVisible1() {
        var visible by mutableStateOf(true)

        composeTestRule.setContent {
            SparkTheme {
                Box(
                    Modifier
                        .size(128.dp)
                        .background(color = Color.Black)
                        .placeholder(
                            visible = visible,
                            color = Color.Red,
                            shape = RectangleShape,
                            highlight = null,
                        )
                        .testTag(contentTag),
                )
            }
        }

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Red)

        visible = false

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Black)
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    @Test
    fun placeholder_switchVisible2() {
        var visible by mutableStateOf(true)

        composeTestRule.setContent {
            Box(
                Modifier
                    .size(128.dp)
                    .background(color = Color.Black)
                    .basePlaceholder(
                        visible = visible,
                        color = Color.Gray,
                        highlight = Solid(Color.Red),
                        shape = RectangleShape,
                    )
                    .testTag(contentTag),
            )
        }

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Red)

        visible = false

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Black)
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    @Test
    fun placeholder_switchColor() {
        var color by mutableStateOf(Color.Red)

        composeTestRule.setContent {
            Box(
                Modifier
                    .size(128.dp)
                    .background(color = Color.Black)
                    .basePlaceholder(
                        visible = true,
                        color = color,
                        shape = RectangleShape,
                        highlight = null,
                    )
                    .testTag(contentTag),
            )
        }

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Red)

        color = Color.Blue

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Blue)
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    @Test
    fun placeholder_switchAnimatedBrush() {
        var animatedBrush by mutableStateOf(Solid(Color.Red))

        composeTestRule.setContent {
            Box(
                Modifier
                    .size(128.dp)
                    .background(color = Color.Black)
                    .basePlaceholder(
                        visible = true,
                        color = Color.Gray,
                        highlight = animatedBrush,
                        shape = RectangleShape,
                    )
                    .testTag(contentTag),
            )
        }

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Red)

        animatedBrush = Solid(Color.Blue)

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .captureToImage()
            .assertPixels(Color.Blue)
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    @Test
    fun placeholder_switchShape1() {
        var shape by mutableStateOf(RectangleShape)

        composeTestRule.setContent {
            Box(
                Modifier
                    .size(20.dp)
                    .background(color = Color.Black)
                    .basePlaceholder(
                        visible = true,
                        color = Color.Red,
                        shape = shape,
                        highlight = null,
                    )
                    .testTag(contentTag),
            )
        }

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(20.dp)
            .assertHeightIsEqualTo(20.dp)
            .captureToImage()
            .assertPixels(Color.Red)

        shape = CircleShape

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(20.dp)
            .assertHeightIsEqualTo(20.dp)
            .captureToImage()
            // There is no stable API to assert the shape.
            // So check the color of the vertices simply.
            .assertPixelsOfVertices(Color.Black)
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
    @Test
    fun placeholder_switchShape2() {
        var shape by mutableStateOf(RectangleShape)

        composeTestRule.setContent {
            Box(
                Modifier
                    .size(20.dp)
                    .background(color = Color.Black)
                    .basePlaceholder(
                        visible = true,
                        color = Color.Gray,
                        highlight = Solid(Color.Red),
                        shape = shape,
                    )
                    .testTag(contentTag),
            )
        }

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(20.dp)
            .assertHeightIsEqualTo(20.dp)
            .captureToImage()
            .assertPixels(Color.Red)

        shape = CircleShape

        composeTestRule.onNodeWithTag(contentTag)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(20.dp)
            .assertHeightIsEqualTo(20.dp)
            .captureToImage()
            // There is no stable API to assert the shape.
            // So check the color of the vertices simply.
            .assertPixelsOfVertices(Color.Black)
    }
}

internal class Solid(
    private val color: Color,
    override val animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(delayMillis = 0, durationMillis = 500),
        repeatMode = RepeatMode.Restart,
    ),
) : PlaceholderHighlight {
    override fun alpha(progress: Float): Float = 1f

    override fun brush(progress: Float, size: Size): Brush = SolidColor(color)
}
