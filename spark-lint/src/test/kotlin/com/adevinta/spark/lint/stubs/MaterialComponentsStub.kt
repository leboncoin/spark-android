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
package com.adevinta.spark.lint.stubs

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin

// Simplified Material.kt stubs
internal val MaterialComponentsStub = kotlin(
    """
    package androidx.compose.material3
    import androidx.compose.runtime.Composable

    @Composable
    fun AlertDialog() {}
    @Composable
    fun AssistChip() {}
    @Composable
    fun Badge() {}
    @Composable
    fun BadgedBox() {}
    @Composable
    fun BottomAppBar() {}
    @Composable
    fun BottomSheetScaffold() {}
    @Composable
    fun ModalBottomSheet() {}
    @Composable
    fun Button() {}
    @Composable
    fun Card() {}
    @Composable
    fun CenterAlignedTopAppBar() {}
    @Composable
    fun Checkbox() {}
    @Composable
    fun CircularProgressIndicator() {}
    @Composable
    fun contentColorFor() {}
    @Composable
    fun DismissibleDrawerSheet() {}
    @Composable
    fun DismissibleNavigationDrawer() {}
    @Composable
    fun Divider() {}
    @Composable
    fun DropdownMenu() {}
    @Composable
    fun DropdownMenuItem() {}
    @Composable
    fun ElevatedAssistChip() {}
    @Composable
    fun ElevatedButton() {}
    @Composable
    fun ElevatedCard() {}
    @Composable
    fun ElevatedFilterChip() {}
    @Composable
    fun ExtendedFloatingActionButton() {}
    @Composable
    fun FilledIconButton() {}
    @Composable
    fun FilledIconToggleButton() {}
    @Composable
    fun FilledTonalButton() {}
    @Composable
    fun FilledTonalIconButton() {}
    @Composable
    fun FilledTonalIconToggleButton() {}
    @Composable
    fun FilterChip() {}
    @Composable
    fun FloatingActionButton() {}
    @Composable
    fun Icon() {}
    @Composable
    fun IconButton() {}
    @Composable
    fun IconToggleButton() {}
    @Composable
    fun InputChip() {}
    @Composable
    fun LargeFloatingActionButton() {}
    @Composable
    fun LargeTopAppBar() {}
    @Composable
    fun LeadingIconTab() {}
    @Composable
    fun LinearProgressIndicator() {}
    @Composable
    fun ListItem() {}
    @Composable
    fun MaterialTheme() {}
    @Composable
    fun MediumTopAppBar() {}
    @Composable
    fun ModalDrawerSheet() {}
    @Composable
    fun ModalNavigationDrawer() {}
    @Composable
    fun NavigationBar() {}
    @Composable
    fun NavigationBarItem() {}
    @Composable
    fun NavigationDrawerItem() {}
    @Composable
    fun NavigationRail() {}
    @Composable
    fun NavigationRailItem() {}
    @Composable
    fun OutlinedButton() {}
    @Composable
    fun OutlinedCard() {}
    @Composable
    fun OutlinedIconButton() {}
    @Composable
    fun OutlinedIconToggleButton() {}
    @Composable
    fun OutlinedTextField() {}
    @Composable
    fun PermanentDrawerSheet() {}
    @Composable
    fun PlainTooltip() {}
    @Composable
    fun RadioButton() {}
    @Composable
    fun RangeSlider() {}
    @Composable
    fun rememberDrawerState() {}
    @Composable
    fun rememberTopAppBarState() {}
    @Composable
    fun Scaffold() {}
    @Composable
    fun ScrollableTabRow() {}
    @Composable
    fun Slider() {}
    @Composable
    fun SmallFloatingActionButton() {}
    @Composable
    fun Snackbar() {}
    @Composable
    fun SnackbarHost() {}
    @Composable
    fun SnackbarHostState() {}
    @Composable
    fun Surface() {}
    @Composable
    fun Switch() {}
    @Composable
    fun Tab() {}
    @Composable
    fun TabRow() {}
    @Composable
    fun Text() {}
    @Composable
    fun TextButton() {}
    @Composable
    fun TooltipBox() {}
    @Composable
    fun TopAppBar() {}
    @Composable
    fun TriStateCheckbox() {}
    @Composable
    fun ripple() {}
    @Composable
    fun HorizontalDivider() {}
    @Composable
    fun VerticalDivider() {}

    """.trimIndent(),
)
