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
package com.adevinta.spark.catalog.icons

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.chips.ChipTinted
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.tab.Tab
import com.adevinta.spark.components.tab.TabGroup
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.toggles.SwitchIcons
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
internal fun IconExampleScreen(
    icon: SparkIcon,
    name: String,
    isAnimated: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        ) {
            var atEnd by remember { mutableStateOf(false) }
            var isRunning by remember { mutableStateOf(false) }

            // This is necessary just if you want to run the animation when the
            // component is displayed. Otherwise, you can remove it.
            LaunchedEffect(icon, isRunning) {
                while (isRunning) {
                    delay(3.seconds) // set here your delay between animations
                    atEnd = !atEnd
                }
            }
            if (isAnimated) {
                SwitchLabelled(
                    checked = isRunning,
                    onCheckedChange = { isRunning = !isRunning },
                ) {
                    Text(text = "Animate indefinitely")
                }
            }
            val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
                keyframes {
                    durationMillis = 300
                    initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
                    targetBounds at 300
                }
            }
            Icon(
                sparkIcon = icon,
                contentDescription = name,
                modifier = Modifier
                    .size(128.dp)
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "icon-$name"),
                        animatedVisibilityScope = animatedContentScope,
                        boundsTransform = boundsTransform,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                    ),
                atEnd = atEnd,
            )
            IconButtonFilled(
                icon = icon,
                contentDescription = name,
                onClick = {
                    atEnd = !atEnd
                },
                atEnd = atEnd,
            )
            ButtonFilled(onClick = { atEnd = !atEnd }, text = name, icon = icon, atEnd = atEnd)
            TagFilled(text = name, leadingIcon = icon, atEnd = atEnd)
            ChipTinted(
                onClick = { atEnd = !atEnd },
            ) {
                Text(text = name)
                Icon(sparkIcon = icon, contentDescription = name, atEnd = atEnd)
            }
            SwitchLabelled(
                checked = atEnd,
                onCheckedChange = { atEnd = !atEnd },
                icons = SwitchIcons(checked = icon, unchecked = SparkIcons.Close),
            ) {
                Text(text = name)
            }

            val tabs = mutableListOf(
                Pair("Home", null),
                Pair(name, icon),
            )
            var selectedIndex by remember { mutableIntStateOf(1) }
            TabGroup(
                selectedTabIndex = selectedIndex,
            ) {
                tabs.forEachIndexed { index, pair ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = pair.second,
                        text = pair.first,
                    )
                }
            }
        }
    }
}
