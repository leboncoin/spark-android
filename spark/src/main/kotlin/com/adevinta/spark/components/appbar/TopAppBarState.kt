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
package com.adevinta.spark.components.appbar

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import com.adevinta.spark.ExperimentalSparkApi
import androidx.compose.material3.rememberTopAppBarState as rememberMaterialTopAppBarState

@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
internal fun rememberSparkTopAppBarState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0.0f,
    initialContentOffset: Float = 0.0f,
): TopAppBarState = rememberMaterialTopAppBarState(
    initialHeightOffsetLimit = initialHeightOffsetLimit,
    initialHeightOffset = initialHeightOffset,
    initialContentOffset = initialContentOffset,
)

/**
 * Creates a TopAppBarState that is remembered across compositions.
 * @param initialHeightOffsetLimit the initial value for TopAppBarState.heightOffsetLimit, which represents the pixel limit that a top app bar is allowed to collapse when the scrollable content is scrolled
 * @param initialHeightOffset the initial value for TopAppBarState.heightOffset. The initial offset height offset should be between zero and initialHeightOffsetLimit.
 * @param initialContentOffset the initial value for TopAppBarState.contentOffset
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
public fun rememberTopAppBarState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f,
): TopAppBarState = rememberSparkTopAppBarState(
    initialHeightOffsetLimit = initialHeightOffsetLimit,
    initialHeightOffset = initialHeightOffset,
    initialContentOffset = initialContentOffset,
)
