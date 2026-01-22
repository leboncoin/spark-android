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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.adevinta.spark.catalog.util.openUrl
import com.adevinta.spark.components.divider.DividerIntent
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenu
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Computer
import com.adevinta.spark.icons.Link
import com.adevinta.spark.icons.SparkIcons
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
    BoundsTransform { initialBounds, targetBounds ->
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
                    placeholderSize = SharedTransitionScope.PlaceholderSize.AnimatedSize,
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
                                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                    ),
                                )
                            }
                            Box {
                                var expanded by remember { mutableStateOf(false) }
                                IconButtonGhost(
                                    icon = SparkIcons.Link,
                                    onClick = { expanded = true },
                                    contentDescription = stringResource(R.string.component_menu_links),
                                )
                                ComponentQuickActionsMenu(
                                    component = component,
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                )
                            }
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
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), intent = DividerIntent.Outline)
                        Text(
                            text = stringResource(id = R.string.examples) + " ${component.examples.size}",
                            style = SparkTheme.typography.headline2,
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
                                        placeholderSize = SharedTransitionScope.PlaceholderSize.AnimatedSize,
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .animateEnterExit(
                                        enter = materialFadeThroughIn() +
                                            slideInVertically(
                                                tween(durationMillis = 500, delayMillis = (index - 1) * 50),
                                            ) { it } +
                                            materialElevationScaleIn(),
                                        exit = materialFadeThroughOut(),
                                    ),
                            )
                        }
                    } else {
                        item {
                            EmptyExamplesState(
                                component = component,
                                modifier = Modifier.animateEnterExit(
                                    enter = materialFadeThroughIn(),
                                    exit = materialFadeThroughOut(),
                                ),
                            )
                            Spacer(modifier = Modifier.height(ComponentPadding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentQuickActionsMenu(
    component: Component,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.component_menu_guidlines)) },
            onClick = {
                context.openUrl(component.guidelinesUrl)
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Palette,
                    contentDescription = null,
                )
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.component_menu_dev_docs)) },
            onClick = {
                context.openUrl(component.docsUrl)
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    sparkIcon = SparkIcons.Computer,
                    contentDescription = null,
                )
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.component_menu_source)) },
            onClick = {
                context.openUrl(component.sourceUrl)
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Code,
                    contentDescription = null,
                )
            },
        )
    }
}

@Composable
private fun EmptyExamplesState(
    component: Component,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            sparkIcon = SparkIcons.Computer,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = SparkTheme.colors.onSurface.copy(alpha = 0.38f),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.no_examples_title),
                style = SparkTheme.typography.subhead,
            )
            Text(
                text = stringResource(R.string.no_examples_description),
                style = SparkTheme.typography.body2,
            )
        }
        if (component.configurators.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.no_examples_configurator_hint),
                style = SparkTheme.typography.body2,
                color = SparkTheme.colors.main,
            )
        }
    }
}

private val ComponentIconSize = 108.dp
private val ComponentIconVerticalPadding = 42.dp
private val ComponentPadding = 16.dp
private val ComponentDescriptionPadding = 32.dp
private val ExampleItemPadding = 8.dp
