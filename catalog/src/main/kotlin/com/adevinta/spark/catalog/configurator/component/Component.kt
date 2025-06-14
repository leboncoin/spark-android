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
package com.adevinta.spark.catalog.configurator.component

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.examples.ExamplesSharedElementKey
import com.adevinta.spark.catalog.examples.ExamplesSharedElementType
import com.adevinta.spark.catalog.examples.component.ComponentOrigin
import com.adevinta.spark.catalog.model.Component
import com.adevinta.spark.catalog.model.Components
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.animations.LocalAnimatedVisibilityScope
import com.adevinta.spark.catalog.ui.animations.LocalSharedTransitionScope
import com.adevinta.spark.catalog.util.IssueUrl
import com.adevinta.spark.catalog.util.openUrl
import com.adevinta.spark.components.divider.HorizontalDivider
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.menu.DropdownMenu
import com.adevinta.spark.components.menu.DropdownMenuItem
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Computer
import com.adevinta.spark.icons.Link
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.Layout

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
public fun ConfiguratorComponentScreen(
    component: Component,
    configurator: Configurator,
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
        keyframes {
            durationMillis = 300
            initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
            targetBounds at 300
        }
    }
    with(LocalSharedTransitionScope.current) {
        Scaffold(
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    ExamplesSharedElementKey(
                        exampleId = component.id,
                        origin = ComponentOrigin.Configurator.name,
                        type = ExamplesSharedElementType.Background,
                    ),
                ),
                animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
//                boundsTransform = boundsTransform,
                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
            ),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = Layout.bodyMargin)
                    .skipToLookaheadSize()
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier.align(Alignment.End),
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    IconButtonGhost(
                        icon = SparkIcons.Link,
                        onClick = { expanded = true },
                        contentDescription = "Localized description",
                    )

                    ConfiguratorComponentMenu(
                        component = component,
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    )
                }

                with(configurator) {
                    this@Column.content(snackbarHostState)
                }
            }
        }
    }
}

@Composable
private fun ConfiguratorComponentMenu(
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
            onClick = { context.openUrl(component.guidelinesUrl) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Palette,
                    contentDescription = null,
                )
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.component_menu_dev_docs)) },
            onClick = { context.openUrl(component.docsUrl) },
            leadingIcon = {
                Icon(
                    sparkIcon = SparkIcons.Computer,
                    contentDescription = null,
                )
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.component_menu_source)) },
            onClick = { context.openUrl(component.sourceUrl) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Code,
                    contentDescription = null,
                )
            },
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text(stringResource(R.string.component_menu_issue)) },
            onClick = { context.openUrl(IssueUrl) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.BugReport,
                    contentDescription = null,
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfiguratorComponentMenuPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
    ) {
        ConfiguratorComponentMenu(
            component = Components.first(),
            expanded = true,
        ) {}
    }
}
