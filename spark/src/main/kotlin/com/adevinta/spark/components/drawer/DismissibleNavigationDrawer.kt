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
package com.adevinta.spark.components.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.navigation.NavigationDrawerItem
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Family
import com.adevinta.spark.icons.HeartOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.MailBoxOpenOutline
import kotlinx.coroutines.launch
import androidx.compose.material3.rememberDrawerState as rememberMaterialDrawerState

@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
internal fun SparkDismissibleNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    DismissibleNavigationDrawer(
        drawerContent = drawerContent,
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        content = content,
    )
}

/**
 * Spark DismissibleNavigationDrawer.
 *
 * A DismissibleNavigationDrawer provide ergonomic access to destinations in an app. They’re often next to app content and affect the screen’s layout grid.
 *
 * ![DismissibleNavigationDrawer image](https://developer.android.com/images/reference/androidx/compose/material3/navigation-drawer.png)
 *
 * @param drawerContent content inside this drawer
 * @param modifier the [Modifier] to be applied to this drawer
 * @param drawerState state of the drawer
 * @param gesturesEnabled whether or not the drawer can be interacted by gestures
 * @param content content of the rest of the UI
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalSparkApi
@Composable
public fun DismissibleNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    SparkDismissibleNavigationDrawer(
        drawerContent = drawerContent,
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        content = content,
    )
}

@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@ExperimentalMaterial3Api
@Composable
public fun rememberDrawerState(
    initialValue: DrawerValue,
    confirmStateChange: (DrawerValue) -> Boolean = { true },
): DrawerState = rememberMaterialDrawerState(
    initialValue = initialValue,
    confirmStateChange = confirmStateChange,
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    group = "Drawer",
    name = "DismissibleNavigationDrawer",
)
@Composable
internal fun AlertDialogPreview() {
    PreviewTheme(
        padding = PaddingValues(0.dp),
    ) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        // icons to mimic drawer destinations
        val items = listOf(LeboncoinIcons.HeartOutline, LeboncoinIcons.Family, LeboncoinIcons.MailBoxOpenOutline)
        val selectedItem = remember { mutableStateOf(items[0]) }

        DismissibleNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DismissibleDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = item,
                            label = "item",
                            selected = item == selectedItem.value,
                            onClick = {
                                scope.launch { drawerState.close() }
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
                    Text(text = if (drawerState.isClosed) ">>> Swipe >>>" else "<<< Swipe <<<")
                    Spacer(Modifier.height(20.dp))
                    ButtonFilled(onClick = { scope.launch { drawerState.open() } }) {
                        Text("Click to open")
                    }
                }
            },
        )
    }
}
