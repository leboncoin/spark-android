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

// Simplified Spark.kt stubs
internal val SparkComponentsStubs = arrayOf(
    kotlin(
        """
    package com.adevinta.spark.components.buttons
    import androidx.compose.runtime.Composable

    @Composable
    fun ButtonFilled() {}

    @Composable
    fun ButtonTinted() {}

    @Composable
    fun ButtonOutlined() {}

    @Composable
    fun ButtonGhost() {}

    @Composable
    fun ButtonContrast() {}

    @Composable
    fun FloatingActionButton() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.textfields
    import androidx.compose.runtime.Composable

    @Composable
    fun TextField() {}

    @Composable
    fun DropDown() {}

    @Composable
    fun MultilineTextField() {}

    @Composable
    fun Combobox() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.toggles
    import androidx.compose.runtime.Composable

    @Composable
    fun Checkbox() {}

    @Composable
    fun RadioButton() {}

    @Composable
    fun Switch() {}

    @Composable
    fun CheckboxLabelled() {}

    @Composable
    fun RadioButtonLabelled() {}

    @Composable
    fun SwitchLabelled() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.snackbars
    import androidx.compose.runtime.Composable

    @Composable
    fun Snackbar() {}

    @Composable
    fun SnackbarHost() {}

    class SnackbarHostState
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.progress
    import androidx.compose.runtime.Composable

    @Composable
    fun LinearProgressIndicator() {}

    @Composable
    fun CircularProgressIndicator() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark
    import androidx.compose.runtime.Composable

    @Composable
    fun SparkTheme() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.image
    import androidx.compose.runtime.Composable

    @Composable
    fun Image() {}

    @Composable
    fun Illustration() {}

    @Composable
    fun UserAvatar() {}

    @Composable
    fun AsyncImage() {}

    @Composable
    fun SubcomposeAsyncImage() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.icons
    import androidx.compose.runtime.Composable

    @Composable
    fun Icon() {}

    @Composable
    fun IconButton() {}

    @Composable
    fun IconButtonFilled() {}

    @Composable
    fun IconButtonTinted() {}

    @Composable
    fun IconButtonOutlined() {}

    @Composable
    fun IconToggleButton() {}

    @Composable
    fun IconToggleButtonFilled() {}

    @Composable
    fun IconToggleButtonTinted() {}

    @Composable
    fun IconToggleButtonOutlined() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.divider
    import androidx.compose.runtime.Composable

    @Composable
    fun HorizontalDivider() {}

    @Composable
    fun VerticalDivider() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.text
    import androidx.compose.runtime.Composable

    @Composable
    fun Text() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.tags
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier

    @Composable
    fun TagFilled(
        text: String? = null,
        contentDescription: String? = null,
        modifier: Modifier = Modifier
    ) {}

    @Composable
    fun TagFilled(
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {}

    @Composable
    fun TagOutlined(
        text: String? = null,
        contentDescription: String? = null,
        modifier: Modifier = Modifier
    ) {}

    @Composable
    fun TagOutlined(
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {}

    @Composable
    fun TagTinted(
        text: String? = null,
        contentDescription: String? = null,
        modifier: Modifier = Modifier
    ) {}

    @Composable
    fun TagTinted(
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.dialog
    import androidx.compose.runtime.Composable

    @Composable
    fun AlertDialog() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.chips
    import androidx.compose.runtime.Composable

    @Composable
    fun Chip() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.badge
    import androidx.compose.runtime.Composable

    @Composable
    fun Badge() {}

    @Composable
    fun BadgedBox() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.appbar
    import androidx.compose.runtime.Composable

    @Composable
    fun BottomAppBar() {}

    @Composable
    fun TopAppBar() {}

    @Composable
    fun NavigationBar() {}

    @Composable
    fun NavigationBarItem() {}

    @Composable
    fun rememberTopAppBarState() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.bottomsheet.scaffold
    import androidx.compose.runtime.Composable

    @Composable
    fun BottomSheetScaffold() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.bottomsheet
    import androidx.compose.runtime.Composable

    @Composable
    fun BottomSheet() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.card
    import androidx.compose.runtime.Composable

    @Composable
    fun Card() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.tokens
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.graphics.Color

    @Composable
    fun contentColorFor(color: Color = Color.Unspecified): Color = Color.Unspecified

    @Composable
    fun ripple() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.drawer
    import androidx.compose.runtime.Composable

    @Composable
    fun DismissibleDrawerSheet() {}

    @Composable
    fun DismissibleNavigationDrawer() {}

    @Composable
    fun ModalDrawerSheet() {}

    @Composable
    fun ModalNavigationDrawer() {}

    fun rememberDrawerState() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.menu
    import androidx.compose.runtime.Composable

    @Composable
    fun DropdownMenu() {}

    @Composable
    fun DropdownMenuItem() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.tab
    import androidx.compose.runtime.Composable

    @Composable
    fun Tab() {}

    @Composable
    fun TabGroup() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.list
    import androidx.compose.runtime.Composable

    @Composable
    fun ListItem() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.navigation
    import androidx.compose.runtime.Composable

    @Composable
    fun NavigationDrawerItem() {}

    @Composable
    fun NavigationRail() {}

    @Composable
    fun NavigationRailItem() {}

    @Composable
    fun PermanentDrawerSheet() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.popover
    import androidx.compose.runtime.Composable

    @Composable
    fun PlainTooltip() {}

    @Composable
    fun TooltipBox() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.slider
    import androidx.compose.runtime.Composable

    @Composable
    fun RangeSlider() {}

    @Composable
    fun Slider() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.scaffold
    import androidx.compose.runtime.Composable

    @Composable
    fun Scaffold() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.surface
    import androidx.compose.runtime.Composable

    @Composable
    fun Surface() {}
        """.trimIndent(),
    ),
)
