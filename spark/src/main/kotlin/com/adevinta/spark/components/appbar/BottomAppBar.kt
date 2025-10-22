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
@file:Suppress("DEPRECATION")

package com.adevinta.spark.components.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.BottomAppBarState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.FloatingActionButton
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.GearOutline
import com.adevinta.spark.icons.PenOutline
import com.adevinta.spark.icons.Plus
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.ElevationTokens
import com.adevinta.spark.tokens.applyTonalElevation
import com.adevinta.spark.tokens.contentColorFor

/**
 * Scroll behavior for BottomAppBar that implements a pinned behavior similar to TopAppBar
 * but with bottom-specific elevation logic. The bottom app bar stays pinned at the bottom
 * and shows elevation when content has been scrolled up from a scrolled-down position
 * (contentOffset > 0), indicating there's content above the current scroll position.
 *
 * Example usage with a scrollable screen:
 * ```kotlin
 * @OptIn(ExperimentalMaterial3Api::class)
 * @Composable
 * fun ScrollableScreenWithBottomBar() {
 *     // Create scroll behaviors for both bars if needed
 *     val topBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
 *     val bottomBarBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()
 *
 *     Scaffold(
 *         modifier = Modifier
 *             .fillMaxSize()
 *             // Connect both scroll behaviors
 *             .nestedScroll(topBarBehavior.nestedScrollConnection)
 *             .nestedScroll(bottomBarBehavior.nestedScrollConnection),
 *         topBar = {
 *             TopAppBar(
 *                 title = { Text("My App") },
 *                 scrollBehavior = topBarBehavior
 *             )
 *         },
 *         bottomBar = {
 *             BottomAppBar(
 *                 scrollBehavior = bottomBarBehavior,
 *                 actions = {
 *                     ButtonOutlined(
 *                         onClick = { /* Handle click */ },
 *                         text = "Cancel",
 *                         modifier = Modifier.weight(1f)
 *                     )
 *                     ButtonFilled(
 *                         onClick = { /* Handle click */ },
 *                         text = "Save",
 *                         modifier = Modifier.weight(1f)
 *                     )
 *                 }
 *             )
 *         }
 *     ) { padding ->
 *         LazyColumn(
 *             modifier = Modifier
 *                 .fillMaxSize()
 *                 .padding(padding),
 *             verticalArrangement = Arrangement.spacedBy(8.dp)
 *         ) {
 *             items(50) { index ->
 *                 ListItem(
 *                     headlineContent = { Text("Item ${index + 1}") }
 *                 )
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * This behavior uses the BottomAppBarState.contentOffset to determine elevation:
 * - contentOffset > 0: Content has been scrolled up from scrolled-down position, show elevation
 * - contentOffset <= 0: At the top or scrolling down, no elevation needed
 *
 * Additionally provides an overlappedFraction property (0.0 to 1.0+) that represents
 * how much content has been scrolled above the bottom app bar, similar to TopAppBar's
 * overlappedFraction but for bottom positioning.
 */
@ExperimentalMaterial3Api
public class SparkBottomAppBarScrollBehavior(
    override val state: BottomAppBarState,
    override val snapAnimationSpec: AnimationSpec<Float>?,
    override val flingAnimationSpec: DecayAnimationSpec<Float>?,
    public val canScroll: () -> Boolean = { true },
) : BottomAppBarScrollBehavior {

    override val isPinned: Boolean = true

    /**
     * A value that represents the percentage of content that has been scrolled above the bottom app bar.
     *
     * A `0.0` indicates that no content has been scrolled above the bottom app bar (at the top),
     * while values > 0.0 indicate increasing amounts of content scrolled above.
     *
     * This is the bottom app bar equivalent of TopAppBar's overlappedFraction.
     */
    public val overlappedFraction: Float
        get() = if (state.contentOffset > 0f) {
            // Normalize the contentOffset to a fraction
            // We use a reasonable maximum scroll distance for normalization
            (state.contentOffset / 1000f).coerceAtMost(1f)
        } else {
            0f
        }

    override var nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (!canScroll()) return Offset.Zero

            // Update the content offset to track scroll position
            // Negative contentOffset means we've scrolled down (content above)
            // Positive contentOffset means we've scrolled up (at or near top)
            if (consumed.y == 0f && available.y <= 0f) {
                // Reset the total content offset to zero when scrolling all the way down.
                // This will eliminate some float precision inaccuracies.
                state.contentOffset = 0f
            } else {
                state.contentOffset += consumed.y
            }

            return Offset.Zero
        }
    }
}

/**
 * <a href="https://m3.material.io/components/bottom-app-bar/overview" class="external" target="_blank">Material Design bottom app bar</a>.
 *
 * A bottom app bar displays navigation and key actions at the bottom of mobile screens.
 *
 * ![Bottom app bar image](https://developer.android.com/images/reference/androidx/compose/material3/bottom-app-bar.png)
 *
 * It can optionally display a [FloatingActionButton] embedded at the end of the BottomAppBar.
 *
 * Also see [NavigationBar].
 *
 * @param actions the icon content of this BottomAppBar. The default layout here is a [Row],
 * so content inside will be placed horizontally.
 * @param modifier the [Modifier] to be applied to this BottomAppBar
 * @param floatingActionButton optional floating action button at the end of this BottomAppBar
 * @param containerColor the color used for the background of this BottomAppBar. Use
 * [Color.Transparent] to have no color.
 * @param contentColor the preferred color for content inside this BottomAppBar. Defaults to either
 * the matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation when [containerColor] is [ColorScheme.surface], a translucent main color
 * overlay is applied on top of the container. A higher tonal elevation value will result in a
 * darker color in light theme and lighter color in dark theme. See also: [Surface].
 * @param contentPadding the padding applied to the content of this BottomAppBar
 * @param windowInsets a window insets that app bar will respect.
 * @param scrollBehavior a [BottomAppBarScrollBehavior] which holds scroll state and will be used to
 * determine the bottom app bar elevation. Elevation is shown when state.contentOffset > 0, indicating
 * there's content above the current scroll position to scroll back to. When using
 * [SparkBottomAppBarScrollBehavior], the overlappedFraction property enables smooth elevation
 * transitions with performance-optimized recomposition using derivedStateOf.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun BottomAppBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null,
    containerColor: Color = BottomAppBarDefaults.containerColor,
    contentColor: Color = androidx.compose.material3.contentColorFor(containerColor),
    elevation: Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
    scrollBehavior: SparkBottomAppBarScrollBehavior? = null,
): Unit = BottomAppBar(
    modifier = modifier,
    containerColor = containerColor,
    contentColor = contentColor,
    elevation = elevation,
    windowInsets = windowInsets,
    contentPadding = contentPadding,
    scrollBehavior = scrollBehavior,
) {
    actions()
    if (floatingActionButton != null) {
        Spacer(Modifier.weight(1f, true))
        Box(
            Modifier
                .fillMaxHeight()
                .padding(
                    top = FABVerticalPadding,
                    end = FABHorizontalPadding,
                ),
            contentAlignment = Alignment.TopStart,
        ) {
            floatingActionButton()
        }
    }
}

/**
 * <a href="https://m3.material.io/components/bottom-app-bar/overview" class="external" target="_blank">Material Design bottom app bar</a>.
 *
 * A bottom app bar displays navigation and key actions at the bottom of mobile screens.
 *
 * ![Bottom app bar image](https://developer.android.com/images/reference/androidx/compose/material3/bottom-app-bar.png)
 *
 * If you are interested in displaying a [FloatingActionButton], consider using another overload.
 *
 * Also see [NavigationBar].
 *
 * @param modifier the [Modifier] to be applied to this BottomAppBar
 * @param containerColor the color used for the background of this BottomAppBar. Use
 * [Color.Transparent] to have no color.
 * @param contentColor the preferred color for content inside this BottomAppBar. Defaults to either
 * the matching content color for [containerColor], or to the current [LocalContentColor] if
 * [containerColor] is not a color from the theme.
 * @param elevation when [containerColor] is [ColorScheme.surface], a translucent main color
 * overlay is applied on top of the container. A higher tonal elevation value will result in a
 * darker color in light theme and lighter color in dark theme. See also: [Surface].
 * @param contentPadding the padding applied to the content of this BottomAppBar
 * @param windowInsets a window insets that app bar will respect.
 * @param scrollBehavior a [BottomAppBarScrollBehavior] which holds scroll state and will be used to
 * determine the bottom app bar elevation. Elevation is shown when state.contentOffset > 0, indicating
 * there's content above the current scroll position to scroll back to. When using
 * [SparkBottomAppBarScrollBehavior], the overlappedFraction property enables smooth elevation
 * transitions with performance-optimized recomposition using derivedStateOf.
 * @param content the content of this BottomAppBar. The default layout here is a [Row],
 * so content inside will be placed horizontally.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun BottomAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color = BottomAppBarDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
    scrollBehavior: BottomAppBarScrollBehavior? = null,
    content: @Composable RowScope.() -> Unit,
) {
    // Obtain the elevation transition fraction using the overlappedFraction. This
    // ensures that the elevation will adjust whether the app bar behavior is pinned or scrolled.
    // This may potentially animate or interpolate a transition between the normal elevation and the
    // elevated state according to the app bar's scroll state.
    val elevationTransitionFraction by
        remember(scrollBehavior) {
            // derivedStateOf to prevent redundant recompositions when the content scrolls.
            derivedStateOf {
                val overlappingFraction = (scrollBehavior as? SparkBottomAppBarScrollBehavior)?.overlappedFraction ?: 0f
                if (overlappingFraction > 0.001f) 1f else 0f
            }
        }

    // Animate container color for scroll-based elevation effect
    val animatedContainerColor by animateColorAsState(
        targetValue = if (elevationTransitionFraction > 0f) {
            SparkTheme.colors.applyTonalElevation(
                backgroundColor = containerColor,
                elevation = ElevationTokens.Level5,
            )
        } else {
            containerColor
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "BottomAppBarContainerColor",
    )

    // Animate elevation
    val animatedElevation by animateDpAsState(
        targetValue = if (elevationTransitionFraction > 0f) {
            elevation + ElevationTokens.Level3
        } else {
            elevation
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "BottomAppBarElevation",
    )

    Surface(
        color = animatedContainerColor,
        contentColor = contentColor,
        elevation = animatedElevation,
        shape = SparkTheme.shapes.none,
        modifier = modifier,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .heightIn(min = ContainerHeight)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * Contains default values used for the bottom app bar implementations.
 */
public object BottomAppBarSparkDefaults {

    /**
     * Creates a [SparkBottomAppBarScrollBehavior] for [BottomAppBar]. This scroll behavior is designed to work
     * in conjunction with scrolled content to change the bottom app bar appearance as the content scrolls.
     * The bottom app bar will show elevation when BottomAppBarState.contentOffset > 0, indicating
     * there's content above the current scroll position to scroll back to.
     *
     * This implementation uses only the standard Material3 BottomAppBarScrollBehavior interface
     * and BottomAppBarState properties, with an additional overlappedFraction property for
     * more nuanced color transitions.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    public fun bottomAppBarScrollBehavior(
        state: BottomAppBarState = rememberBottomAppBarState(initialContentOffset = Float.POSITIVE_INFINITY),
        canScroll: () -> Boolean = { true },
        snapAnimationSpec: AnimationSpec<Float>? = null,
        flingAnimationSpec: DecayAnimationSpec<Float>? = null,
    ): SparkBottomAppBarScrollBehavior = remember(state, canScroll, snapAnimationSpec, flingAnimationSpec) {
        SparkBottomAppBarScrollBehavior(
            state = state,
            snapAnimationSpec = snapAnimationSpec,
            flingAnimationSpec = flingAnimationSpec,
            canScroll = canScroll,
        )
    }
}

// Padding minus IconButton's min touch target expansion
private val BottomAppBarHorizontalPadding = 16.dp - 12.dp
internal val BottomAppBarVerticalPadding = 16.dp - 12.dp

// Padding minus content padding
private val FABHorizontalPadding = 16.dp - BottomAppBarHorizontalPadding
private val FABVerticalPadding = 12.dp - BottomAppBarVerticalPadding
private val ContainerHeight = 80.0.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    group = "AppBar",
    name = "BottomAppBar",
)
@Composable
internal fun BottomAppBarPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(SparkIcons.GearOutline, contentDescription = "Localized description")
            }
        }

        BottomAppBar(
            actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        sparkIcon = SparkIcons.Check,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        SparkIcons.PenOutline,
                        contentDescription = "Localized description",
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* do something */ },
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    icon = SparkIcons.Plus,
                    contentDescription = "Localized description",
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    group = "AppBar",
    name = "BottomAppBar with Scroll Behavior",
)
@Composable
internal fun BottomAppBarScrollablePreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            bottomBar = {
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                    actions = {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(
                                sparkIcon = SparkIcons.Check,
                                contentDescription = "Action 1",
                            )
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(
                                sparkIcon = SparkIcons.PenOutline,
                                contentDescription = "Action 2",
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(50) { index ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        color = SparkTheme.colors.backgroundVariant,
                        shape = SparkTheme.shapes.medium,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Item ${index + 1}")
                        }
                    }
                }
            }
        }
    }
}
