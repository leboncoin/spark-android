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
package com.adevinta.spark.catalog.examples.component

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.examples.ExamplesSharedElementKey
import com.adevinta.spark.catalog.examples.ExamplesSharedElementType
import com.adevinta.spark.catalog.examples.example.ExampleItem
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.catalog.util.TrackScrollJank
import com.adevinta.spark.components.image.Image
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Layout
import soup.compose.material.motion.animation.materialElevationScaleIn
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
public fun Component(
    component: Component,
    contentPadding: PaddingValues,
    onExampleClick: (exampleId: String) -> Unit,
) {
    val ltr = LocalLayoutDirection.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
        keyframes {
            durationMillis = 500
            initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
            targetBounds at 500
        }
    }
    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "component:list")
    val origin = ComponentOrigin.Example.name
    with(LocalSharedTransitionScope.current) {
        val cardRadius by animatedVisibilityScope.transition
            .animateDp(label = "card radius") { enterExit ->
                when (enterExit) {
                    EnterExitState.PreEnter -> 12.dp
                    EnterExitState.Visible -> 0.dp
                    EnterExitState.PostExit -> 0.dp
                }
            }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        ExamplesSharedElementKey(
                            exampleId = component.id,
                            origin = origin,
                            type = ExamplesSharedElementType.Background,
                        ),
                    ),
//                    boundsTransform = boundsTransform,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    animatedVisibilityScope = animatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(cardRadius)),
                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                ),
        ) {
            with(animatedVisibilityScope) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(contentPadding),
                    contentPadding = PaddingValues(
                        start = Layout.bodyMargin + contentPadding.calculateLeftPadding(ltr),
                        end = Layout.bodyMargin + contentPadding.calculateRightPadding(ltr),
                        top = contentPadding.calculateTopPadding(),
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                    state = state,
                    verticalArrangement = Arrangement.spacedBy(ExampleItemPadding),
                ) {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = component.name,
                                    style = SparkTheme.typography.headline1,
                                    modifier = Modifier.sharedBounds(
                                        rememberSharedContentState(
                                            ExamplesSharedElementKey(
                                                exampleId = component.id,
                                                origin = origin,
                                                type = ExamplesSharedElementType.Title,
                                            ),
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                                    ),
                                )
                                Text(
                                    text = stringResource(id = R.string.description),
                                    style = SparkTheme.typography.body1,
                                    modifier = Modifier
                                        .renderInSharedTransitionScopeOverlay(
                                            renderInOverlay = { false },
                                            zIndexInOverlay = 1f,
                                        )
                                        .animateEnterExit(
                                            enter = materialFadeThroughIn(),
                                            exit = materialFadeThroughOut(),
                                        ),
                                )
                            }
                            Image(
                                modifier = Modifier
                                    .width(64.dp)
                                    .aspectRatio(1f)
                                    .sharedElement(
                                        rememberSharedContentState(
                                            ExamplesSharedElementKey(
                                                exampleId = component.id,
                                                origin = origin,
                                                type = ExamplesSharedElementType.Illustration,
                                            ),
                                        ),
                                        boundsTransform = { initialBounds, targetBounds ->
                                            spring(dampingRatio = .8f, stiffness = 380f)
                                        },
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
                                model = component.illustration,
                                contentScale = ContentScale.Fit,
                                contentDescription = null,
                            )
                        }

                        Spacer(modifier = Modifier.height(ComponentPadding))
                        Text(
                            text = stringResource(id = component.description),
                            style = SparkTheme.typography.body2,
                            modifier = Modifier.skipToLookaheadSize(),
                        )
                        Spacer(modifier = Modifier.height(ComponentDescriptionPadding))
                    }
                    item {
                        Text(
                            text = stringResource(id = R.string.examples),
                            style = SparkTheme.typography.body1,
                        )
                        Spacer(modifier = Modifier.height(ComponentPadding))
                    }
                    if (component.examples.isNotEmpty()) {
                        itemsIndexed(component.examples) { index, example ->
                            ExampleItem(
                                example = example,
                                onClick = onExampleClick,
                                modifier = Modifier
                                    .sharedBounds(
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
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .animateEnterExit(
                                        enter = materialFadeThroughIn() + slideInVertically(
                                            tween(durationMillis = 500, delayMillis = (index - 1) * 50),
                                        ) { it } + materialElevationScaleIn(),
                                        exit = materialFadeThroughOut(),
                                    ),
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = stringResource(id = R.string.no_examples),
                                style = SparkTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.height(ComponentPadding))
                        }
                    }
                }
            }
        }
    }
}

private val ComponentIconSize = 108.dp
private val ComponentIconVerticalPadding = 42.dp
private val ComponentPadding = 16.dp
private val ComponentDescriptionPadding = 32.dp
private val ExampleItemPadding = 8.dp
