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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.examples.ExamplesSharedElementKey
import com.adevinta.spark.catalog.examples.ExamplesSharedElementType
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.drawForegroundGradientScrim
import com.adevinta.spark.components.badge.Badge
import com.adevinta.spark.components.badge.BadgeIntent
import com.adevinta.spark.components.badge.BadgeStyle
import com.adevinta.spark.components.card.CardDefaults
import com.adevinta.spark.components.card.ElevatedCard
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.image.Illustration
import com.adevinta.spark.components.menu.DropdownMenu
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WheelOutline
import com.adevinta.spark.tokens.applyTonalElevation
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import soup.compose.material.motion.animation.materialFadeIn
import soup.compose.material.motion.animation.materialFadeOut

@Composable
public fun ComponentConfiguratorItem(
    component: Component,
    onClick: (component: Component, configuratorId: String) -> Unit,
    countIndicator: Int = 0,
) {
    var expanded by remember { mutableStateOf(false) }
    val singleContent = countIndicator == 1

    Box {
        ComponentItem(
            modifier = Modifier.semantics {
                onClick(
                    label = if (singleContent) {
                        "Aller au configurateur"
                    } else {
                        "Voir les configurateurs disponibles"
                    },
                    action = null,
                )
            },
            component = component,
            countIndicator = countIndicator,
            origin = ComponentOrigin.Configurator,
            onClick = {
                if (singleContent) {
                    val firstConfiguratorId = component.configurators.first().id
                    onClick(component, firstConfiguratorId)
                } else {
                    // Show a menu for all other options
                    expanded = true
                }
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            component.configurators.fastForEachIndexed { i, configurator ->
                DropdownMenuItem(
                    modifier = Modifier.semantics { onClick("Aller au configurateur", null) },
                    text = { Text(configurator.name) },
                    onClick = {
                        onClick(component, configurator.id)
                        expanded = false
                    },
                    leadingIcon = { Icon(SparkIcons.WheelOutline, contentDescription = null) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
public fun ComponentItem(
    modifier: Modifier = Modifier,
    component: Component,
    onClick: () -> Unit,
    origin: ComponentOrigin = ComponentOrigin.Example,
    countIndicator: Int = 0,
) {
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
        keyframes {
            durationMillis = 500
            initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
            targetBounds at 500
        }
    }
    with(LocalSharedTransitionScope.current) {
        val cardRadius by animatedVisibilityScope.transition
            .animateDp(label = "card radius") { enterExit ->
                when (enterExit) {
                    EnterExitState.PreEnter -> 0.dp
                    EnterExitState.Visible -> 12.dp
                    EnterExitState.PostExit -> 12.dp
                }
            }
        ElevatedCard(
            onClick = onClick,
            modifier = modifier
                .semantics(mergeDescendants = true) {}
                .height(ComponentItemHeight)
                .padding(ComponentItemOuterPadding)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        ExamplesSharedElementKey(
                            exampleId = component.id,
                            origin = origin.name,
                            type = ExamplesSharedElementType.Background,
                        ),
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
//                    boundsTransform = boundsTransform,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(cardRadius)),
                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                ),
            shape = SparkTheme.shapes.medium,
            colors = CardDefaults.elevatedCardColors(containerColor = SparkTheme.colors.surface),
        ) {
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopStart),
            ) {
                val tint = ColorFilter.tint(LocalContentColor.current).takeIf { component.tintIcon }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawForegroundGradientScrim(
                            color = SparkTheme.colors.applyTonalElevation(SparkTheme.colors.surface, 1.dp),
                        ),
                ) {
                    Illustration(
                        modifier = Modifier
                            .fillMaxSize()
                            .sharedElement(
                                rememberSharedContentState(
                                    ExamplesSharedElementKey(
                                        exampleId = component.id,
                                        origin = origin.name,
                                        type = ExamplesSharedElementType.Illustration,
                                    ),
                                ),
                                boundsTransform = { initialBounds, targetBounds ->
                                    spring(dampingRatio = .8f, stiffness = 380f)
                                },
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                        drawableRes = component.illustration,
                        contentDescription = null,
                        colorFilter = tint,
                    )
                }
                Text(
                    text = component.name,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(ComponentItemInnerPadding)
                        .sharedBounds(
                            rememberSharedContentState(
                                key = ExamplesSharedElementKey(
                                    exampleId = component.id,
                                    origin = origin.name,
                                    type = ExamplesSharedElementType.Title,
                                ),
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                        ),
                    style = SparkTheme.typography.body2,
                )

                if (countIndicator > 1) {
                    with(animatedVisibilityScope) {
                        Badge(
                            modifier = Modifier
                                .invisibleSemantic()
                                .align(Alignment.BottomEnd)
                                .padding(ComponentItemInnerPadding)
                                .animateEnterExit(
                                    enter = materialFadeIn(),
                                    exit = materialFadeOut(),
                                ),
                            count = countIndicator,
                            intent = BadgeIntent.Basic,
                            badgeStyle = BadgeStyle.Small,
                        )
                    }
                }
            }
        }
    }
}

public enum class ComponentOrigin {
    Example,
    Configurator;
}

private val ComponentItemHeight = 180.dp
private val ComponentItemOuterPadding = 4.dp
private val ComponentItemInnerPadding = 16.dp

@PreviewLightDark
@Composable
private fun ComponentItemPreview() {
    PreviewTheme {
        ComponentItem(
            component = Component(
                id = "colors",
                name = "Tokens",
                description = R.string.component_tokens_description,
                guidelinesUrl = "https://www.google.com/#q=constituto",
                docsUrl = "https://www.google.com/#q=dictas",
                sourceUrl = "http://www.bing.com/search?q=inani",
                examples = listOf(),
                configurators = emptyList(),
            ),
            countIndicator = 3,
            onClick = {},
        )
        ComponentItem(
            component = Component(
                id = "colors",
                name = "Tokens",
                description = R.string.component_tokens_description,
                illustration = R.drawable.illu_component_tokens,
                tintIcon = false,
                guidelinesUrl = "https://www.google.com/#q=constituto",
                docsUrl = "https://www.google.com/#q=dictas",
                sourceUrl = "http://www.bing.com/search?q=inani",
                examples = listOf(),
                configurators = emptyList(),
            ),
            onClick = {},
        )
    }
}
