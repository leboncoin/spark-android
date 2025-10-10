/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.icons

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.gifView
import com.adevinta.spark.paparazziRule
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

internal class AnimatedIconsScreenshot {

    @get:Rule
    val paparazzi = paparazziRule()

//    Blocked until https://github.com/cashapp/paparazzi/pull/1645 is merged and available
//    @Test
//    fun bellShake() {
//        val view = paparazzi.gifView {
//            Icon(
//                sparkIcon = SparkAnimatedIcons.bellShake(),
//                contentDescription = "Bell Shake Animation",
//                size = IconSize.ExtraLarge,
//                tint = Color.Black,
//                atEnd = true,
//            )
//        }
//
//        paparazzi.gif(view, start = 0, end = 800, fps = 60)
//    }

    @Test
    fun collapseExpand() {
        val view = paparazzi.gifView {
            var atEnd by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                atEnd = true
                delay(250)
                atEnd = false
            }
            Icon(
                sparkIcon = SparkAnimatedIcons.collapseExpand(),
                contentDescription = "Collapse Expand Animation",
                size = IconSize.ExtraLarge,
                tint = Color.Black,
                atEnd = atEnd,
            )
        }

        paparazzi.gif(view, start = 0, end = 500, fps = 60)
    }

    @Test
    fun likeHeart() {
        val view = paparazzi.gifView {
            var atEnd by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                atEnd = true
                delay(250)
                atEnd = false
            }
            Icon(
                sparkIcon = SparkAnimatedIcons.likeHeart(),
                contentDescription = "Like Heart Animation",
                size = IconSize.ExtraLarge,
                tint = Color.Black,
                atEnd = atEnd,
            )
        }

        paparazzi.gif(view, start = 0, end = 500, fps = 60)
    }

    @Test
    fun searchIcon() {
        val view = paparazzi.gifView {
            var atEnd by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                atEnd = true
                delay(300)
                atEnd = false
            }
            Icon(
                sparkIcon = SparkAnimatedIcons.searchIcon(),
                contentDescription = "Search Icon Animation",
                size = IconSize.ExtraLarge,
                tint = Color.Black,
                atEnd = atEnd,
            )
        }

        paparazzi.gif(view, start = 0, end = 600, fps = 60)
    }

    @Test
    fun addButton() {
        val view = paparazzi.gifView {
            var atEnd by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(300)
                atEnd = true
            }
            Icon(
                sparkIcon = SparkAnimatedIcons.addButton(),
                contentDescription = "Add Button Animation",
                size = IconSize.ExtraLarge,
                tint = Color.Black,
                atEnd = atEnd,
            )
        }

        paparazzi.gif(view, start = 0, end = 600, fps = 60)
    }

    @Test
    fun accountIcon() {
        val view = paparazzi.gifView {
            var atEnd by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                atEnd = true
                delay(300)
                atEnd = false
            }
            Icon(
                sparkIcon = SparkAnimatedIcons.accountIcon(),
                contentDescription = "Account Icon Animation",
                size = IconSize.ExtraLarge,
                tint = Color.Black,
                atEnd = atEnd,
            )
        }

        paparazzi.gif(view, start = 0, end = 600, fps = 60)
    }

    @Test
    fun messageIcon() {
        val view = paparazzi.gifView {
            var atEnd by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                atEnd = true
                delay(300)
                atEnd = false
            }
            Icon(
                sparkIcon = SparkAnimatedIcons.messageIcon(),
                contentDescription = "Message Icon Animation",
                size = IconSize.ExtraLarge,
                tint = Color.Black,
                atEnd = atEnd,
            )
        }

        paparazzi.gif(view, start = 0, end = 600, fps = 60)
    }
}
