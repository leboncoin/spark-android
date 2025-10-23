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
package com.adevinta.spark.components.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Activity
import com.adevinta.spark.icons.IdentityCardOutline
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.StoreCriteria
import androidx.compose.material3.PermanentNavigationDrawer as MaterialPermanentNavigationDrawer

@Composable
internal fun SparkPermanentNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MaterialPermanentNavigationDrawer(
        drawerContent = drawerContent,
        modifier = modifier,
        content = content,
    )
}

/**
 * Spark navigation permanent drawer.
 * Navigation drawers provide ergonomic access to destinations in an app.
 * They’re often next to app content and affect the screen’s layout grid.
 *
 * The permanent navigation drawer is always visible and usually used for frequently switching destinations.
 * On mobile screens, use ModalNavigationDrawer instead.
 *
 * @param drawerContent commentCountcontent inside this drawer
 * @param modifier commentCountthe Modifier to be applied to this drawer
 * @param content commentCountcontent of the rest of the UI
 */
@ExperimentalSparkApi
@Composable
public fun PermanentNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SparkPermanentNavigationDrawer(
        drawerContent = drawerContent,
        modifier = modifier,
        content = content,
    )
}

@Preview(
    group = "PermanentNavigationDrawer",
    name = "PermanentNavigationDrawer",
)
@Composable
internal fun PermanentNavigationDrawerPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        // icons to mimic drawer destinations
        val items = listOf(SparkIcons.Activity, SparkIcons.IdentityCardOutline, SparkIcons.StoreCriteria)
        val selectedItem = remember { mutableStateOf(items[0]) }
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(Modifier.width(240.dp)) {
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = item,
                            label = "Item",
                            selected = item == selectedItem.value,
                            onClick = {
                                selectedItem.value = item
                            },
                            modifier = Modifier.padding(horizontal = 12.dp),
                        )
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Application content")
                }
            },
        )
    }
}
