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
package com.adevinta.spark.catalog.examples.example

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.examples.ExamplesSharedElementKey
import com.adevinta.spark.catalog.examples.ExamplesSharedElementType
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.tokens.Layout

/**
 * Displays an example content based on the provided [Example] data model.
 *
 * @param example The [Example] data model containing the content to be displayed.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun Example(example: Example) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    val commonModifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(top = 32.dp)
        .padding(horizontal = Layout.bodyMargin)
        .imePadding()

    with(LocalSharedTransitionScope.current) {
        Scaffold(
            modifier = Modifier.sharedBounds(
                rememberSharedContentState(
                    ExamplesSharedElementKey(
                        exampleId = example.id,
                        origin = "component",
                        type = ExamplesSharedElementType.Background,
                    ),
                ),
                boundsTransform = { initialBounds, targetBounds ->
                    spring(dampingRatio = .8f, stiffness = 380f)
                },
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
            ),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) {
            Column(
                modifier = commonModifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                with(example) {
                    this@Column.content(snackbarHostState)
                }
            }
        }
    }
}
