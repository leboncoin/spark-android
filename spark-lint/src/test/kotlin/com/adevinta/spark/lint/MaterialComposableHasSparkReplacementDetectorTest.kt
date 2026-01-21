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
package com.adevinta.spark.lint

import com.adevinta.spark.lint.MaterialComposableHasSparkReplacementDetector.Companion.ISSUE
import com.adevinta.spark.lint.stubs.CoilComponentsStub
import com.adevinta.spark.lint.stubs.Composables
import com.adevinta.spark.lint.stubs.FoundationStub
import com.adevinta.spark.lint.stubs.MaterialComponentsStub
import com.adevinta.spark.lint.stubs.SparkComponentsStubs
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
public class MaterialComposableHasSparkReplacementDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = MaterialComposableHasSparkReplacementDetector()
    override fun getIssues(): List<Issue> = listOf(ISSUE)

    private fun Issue.explanation(replacement: Pair<String, String>) =
        "${defaultSeverity.description}: Composable ${replacement.first} has a Spark replacement " +
            "that should be used instead: ${replacement.second} [$id]"

    @Test
    public fun materialUsages(): Unit = with(ISSUE) {
        lint().files(
            kotlin(
                """
                package foo
                import androidx.compose.material3.*
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                @Composable
                fun Test() {
                AlertDialog()
                AssistChip()
                Badge()
                BadgedBox()
                BottomAppBar()
                BottomSheetScaffold()
                ModalBottomSheet()
                Button()
                Card()
                CenterAlignedTopAppBar()
                Checkbox()
                CircularProgressIndicator()
                contentColorFor()
                DismissibleDrawerSheet()
                DismissibleNavigationDrawer()
                Divider()
                DropdownMenu()
                DropdownMenuItem()
                ElevatedAssistChip()
                ElevatedButton()
                ElevatedCard()
                ElevatedFilterChip()
                ExtendedFloatingActionButton()
                FilledIconButton()
                FilledIconToggleButton()
                FilledTonalButton()
                FilledTonalIconButton()
                FilledTonalIconToggleButton()
                FilterChip()
                FloatingActionButton()
                Icon()
                IconButton()
                IconToggleButton()
                InputChip()
                LargeFloatingActionButton()
                LargeTopAppBar()
                LeadingIconTab()
                LinearProgressIndicator()
                ListItem()
                MaterialTheme()
                MediumTopAppBar()
                ModalDrawerSheet()
                ModalNavigationDrawer()
                NavigationBar()
                NavigationBarItem()
                NavigationDrawerItem()
                NavigationRail()
                NavigationRailItem()
                OutlinedButton()
                OutlinedCard()
                OutlinedIconButton()
                OutlinedIconToggleButton()
                OutlinedTextField()
                PermanentDrawerSheet()
                PlainTooltip()
                RadioButton()
                RangeSlider()
                rememberDrawerState()
                rememberTopAppBarState()
                Scaffold()
                ScrollableTabRow()
                Slider()
                SmallFloatingActionButton()
                Snackbar()
                SnackbarHost()
                SnackbarHostState()
                Surface()
                Switch()
                Tab()
                TabRow()
                Text()
                TextButton()
                TooltipBox()
                TopAppBar()
                TriStateCheckbox()
                ripple()
                HorizontalDivider()
                VerticalDivider()
                }
            """,
            ),
            MaterialComponentsStub,
            *Composables,
        )
            .run()
            .expect(
                """
               src/foo/test.kt:9: Error: Composable AlertDialog has a Spark replacement that should be used instead: AlertDialog [MaterialComposableHasSparkReplacement]
                               AlertDialog()
                               ~~~~~~~~~~~
               src/foo/test.kt:10: Error: Composable AssistChip has a Spark replacement that should be used instead: Chip [MaterialComposableHasSparkReplacement]
                               AssistChip()
                               ~~~~~~~~~~
               src/foo/test.kt:11: Error: Composable Badge has a Spark replacement that should be used instead: Badge [MaterialComposableHasSparkReplacement]
                               Badge()
                               ~~~~~
               src/foo/test.kt:12: Error: Composable BadgedBox has a Spark replacement that should be used instead: BadgedBox [MaterialComposableHasSparkReplacement]
                               BadgedBox()
                               ~~~~~~~~~
               src/foo/test.kt:13: Error: Composable BottomAppBar has a Spark replacement that should be used instead: BottomAppBar [MaterialComposableHasSparkReplacement]
                               BottomAppBar()
                               ~~~~~~~~~~~~
               src/foo/test.kt:14: Error: Composable BottomSheetScaffold has a Spark replacement that should be used instead: BottomSheetScaffold [MaterialComposableHasSparkReplacement]
                               BottomSheetScaffold()
                               ~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:15: Error: Composable ModalBottomSheet has a Spark replacement that should be used instead: BottomSheet [MaterialComposableHasSparkReplacement]
                               ModalBottomSheet()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:16: Error: Composable Button has a Spark replacement that should be used instead: ButtonFilled [MaterialComposableHasSparkReplacement]
                               Button()
                               ~~~~~~
               src/foo/test.kt:17: Error: Composable Card has a Spark replacement that should be used instead: Card [MaterialComposableHasSparkReplacement]
                               Card()
                               ~~~~
               src/foo/test.kt:18: Error: Composable CenterAlignedTopAppBar has a Spark replacement that should be used instead: TopAppBar [MaterialComposableHasSparkReplacement]
                               CenterAlignedTopAppBar()
                               ~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:19: Error: Composable Checkbox has a Spark replacement that should be used instead: Checkbox [MaterialComposableHasSparkReplacement]
                               Checkbox()
                               ~~~~~~~~
               src/foo/test.kt:20: Error: Composable CircularProgressIndicator has a Spark replacement that should be used instead: CircularProgressIndicator [MaterialComposableHasSparkReplacement]
                               CircularProgressIndicator()
                               ~~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:21: Error: Composable contentColorFor has a Spark replacement that should be used instead: contentColorFor [MaterialComposableHasSparkReplacement]
                               contentColorFor()
                               ~~~~~~~~~~~~~~~
               src/foo/test.kt:22: Error: Composable DismissibleDrawerSheet has a Spark replacement that should be used instead: DismissibleDrawerSheet [MaterialComposableHasSparkReplacement]
                               DismissibleDrawerSheet()
                               ~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:23: Error: Composable DismissibleNavigationDrawer has a Spark replacement that should be used instead: DismissibleNavigationDrawer [MaterialComposableHasSparkReplacement]
                               DismissibleNavigationDrawer()
                               ~~~~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:24: Error: Composable Divider has a Spark replacement that should be used instead: HorizontalDivider [MaterialComposableHasSparkReplacement]
                               Divider()
                               ~~~~~~~
               src/foo/test.kt:25: Error: Composable DropdownMenu has a Spark replacement that should be used instead: DropdownMenu [MaterialComposableHasSparkReplacement]
                               DropdownMenu()
                               ~~~~~~~~~~~~
               src/foo/test.kt:26: Error: Composable DropdownMenuItem has a Spark replacement that should be used instead: DropdownMenuItem [MaterialComposableHasSparkReplacement]
                               DropdownMenuItem()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:27: Error: Composable ElevatedAssistChip has a Spark replacement that should be used instead: Chip [MaterialComposableHasSparkReplacement]
                               ElevatedAssistChip()
                               ~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:28: Error: Composable ElevatedButton has a Spark replacement that should be used instead: ButtonFilled [MaterialComposableHasSparkReplacement]
                               ElevatedButton()
                               ~~~~~~~~~~~~~~
               src/foo/test.kt:29: Error: Composable ElevatedCard has a Spark replacement that should be used instead: Card [MaterialComposableHasSparkReplacement]
                               ElevatedCard()
                               ~~~~~~~~~~~~
               src/foo/test.kt:30: Error: Composable ElevatedFilterChip has a Spark replacement that should be used instead: Chip [MaterialComposableHasSparkReplacement]
                               ElevatedFilterChip()
                               ~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:31: Error: Composable ExtendedFloatingActionButton has a Spark replacement that should be used instead: FloatingActionButton [MaterialComposableHasSparkReplacement]
                               ExtendedFloatingActionButton()
                               ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:32: Error: Composable FilledIconButton has a Spark replacement that should be used instead: IconButtonFilled [MaterialComposableHasSparkReplacement]
                               FilledIconButton()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:33: Error: Composable FilledIconToggleButton has a Spark replacement that should be used instead: IconToggleButtonFilled [MaterialComposableHasSparkReplacement]
                               FilledIconToggleButton()
                               ~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:34: Error: Composable FilledTonalButton has a Spark replacement that should be used instead: ButtonTinted [MaterialComposableHasSparkReplacement]
                               FilledTonalButton()
                               ~~~~~~~~~~~~~~~~~
               src/foo/test.kt:35: Error: Composable FilledTonalIconButton has a Spark replacement that should be used instead: IconButtonTinted [MaterialComposableHasSparkReplacement]
                               FilledTonalIconButton()
                               ~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:36: Error: Composable FilledTonalIconToggleButton has a Spark replacement that should be used instead: IconToggleButtonTinted [MaterialComposableHasSparkReplacement]
                               FilledTonalIconToggleButton()
                               ~~~~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:37: Error: Composable FilterChip has a Spark replacement that should be used instead: Chip [MaterialComposableHasSparkReplacement]
                               FilterChip()
                               ~~~~~~~~~~
               src/foo/test.kt:38: Error: Composable FloatingActionButton has a Spark replacement that should be used instead: FloatingActionButton [MaterialComposableHasSparkReplacement]
                               FloatingActionButton()
                               ~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:39: Error: Composable Icon has a Spark replacement that should be used instead: Icon [MaterialComposableHasSparkReplacement]
                               Icon()
                               ~~~~
               src/foo/test.kt:40: Error: Composable IconButton has a Spark replacement that should be used instead: IconButton [MaterialComposableHasSparkReplacement]
                               IconButton()
                               ~~~~~~~~~~
               src/foo/test.kt:41: Error: Composable IconToggleButton has a Spark replacement that should be used instead: IconToggleButton [MaterialComposableHasSparkReplacement]
                               IconToggleButton()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:42: Error: Composable InputChip has a Spark replacement that should be used instead: Chip [MaterialComposableHasSparkReplacement]
                               InputChip()
                               ~~~~~~~~~
               src/foo/test.kt:43: Error: Composable LargeFloatingActionButton has a Spark replacement that should be used instead: FloatingActionButton [MaterialComposableHasSparkReplacement]
                               LargeFloatingActionButton()
                               ~~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:44: Error: Composable LargeTopAppBar has a Spark replacement that should be used instead: TopAppBar [MaterialComposableHasSparkReplacement]
                               LargeTopAppBar()
                               ~~~~~~~~~~~~~~
               src/foo/test.kt:45: Error: Composable LeadingIconTab has a Spark replacement that should be used instead: Tab [MaterialComposableHasSparkReplacement]
                               LeadingIconTab()
                               ~~~~~~~~~~~~~~
               src/foo/test.kt:46: Error: Composable LinearProgressIndicator has a Spark replacement that should be used instead: LinearProgressIndicator [MaterialComposableHasSparkReplacement]
                               LinearProgressIndicator()
                               ~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:47: Error: Composable ListItem has a Spark replacement that should be used instead: ListItem [MaterialComposableHasSparkReplacement]
                               ListItem()
                               ~~~~~~~~
               src/foo/test.kt:48: Error: Composable MaterialTheme has a Spark replacement that should be used instead: SparkTheme [MaterialComposableHasSparkReplacement]
                               MaterialTheme()
                               ~~~~~~~~~~~~~
               src/foo/test.kt:49: Error: Composable MediumTopAppBar has a Spark replacement that should be used instead: TopAppBar [MaterialComposableHasSparkReplacement]
                               MediumTopAppBar()
                               ~~~~~~~~~~~~~~~
               src/foo/test.kt:50: Error: Composable ModalDrawerSheet has a Spark replacement that should be used instead: ModalDrawerSheet [MaterialComposableHasSparkReplacement]
                               ModalDrawerSheet()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:51: Error: Composable ModalNavigationDrawer has a Spark replacement that should be used instead: ModalNavigationDrawer [MaterialComposableHasSparkReplacement]
                               ModalNavigationDrawer()
                               ~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:52: Error: Composable NavigationBar has a Spark replacement that should be used instead: NavigationBar [MaterialComposableHasSparkReplacement]
                               NavigationBar()
                               ~~~~~~~~~~~~~
               src/foo/test.kt:53: Error: Composable NavigationBarItem has a Spark replacement that should be used instead: NavigationBarItem [MaterialComposableHasSparkReplacement]
                               NavigationBarItem()
                               ~~~~~~~~~~~~~~~~~
               src/foo/test.kt:54: Error: Composable NavigationDrawerItem has a Spark replacement that should be used instead: NavigationDrawerItem [MaterialComposableHasSparkReplacement]
                               NavigationDrawerItem()
                               ~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:55: Error: Composable NavigationRail has a Spark replacement that should be used instead: NavigationRail [MaterialComposableHasSparkReplacement]
                               NavigationRail()
                               ~~~~~~~~~~~~~~
               src/foo/test.kt:56: Error: Composable NavigationRailItem has a Spark replacement that should be used instead: NavigationRailItem [MaterialComposableHasSparkReplacement]
                               NavigationRailItem()
                               ~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:57: Error: Composable OutlinedButton has a Spark replacement that should be used instead: ButtonOutlined [MaterialComposableHasSparkReplacement]
                               OutlinedButton()
                               ~~~~~~~~~~~~~~
               src/foo/test.kt:58: Error: Composable OutlinedCard has a Spark replacement that should be used instead: Card [MaterialComposableHasSparkReplacement]
                               OutlinedCard()
                               ~~~~~~~~~~~~
               src/foo/test.kt:59: Error: Composable OutlinedIconButton has a Spark replacement that should be used instead: IconButtonOutlined [MaterialComposableHasSparkReplacement]
                               OutlinedIconButton()
                               ~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:60: Error: Composable OutlinedIconToggleButton has a Spark replacement that should be used instead: IconToggleButtonOutlined [MaterialComposableHasSparkReplacement]
                               OutlinedIconToggleButton()
                               ~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:61: Error: Composable OutlinedTextField has a Spark replacement that should be used instead: TextField [MaterialComposableHasSparkReplacement]
                               OutlinedTextField()
                               ~~~~~~~~~~~~~~~~~
               src/foo/test.kt:62: Error: Composable PermanentDrawerSheet has a Spark replacement that should be used instead: PermanentDrawerSheet [MaterialComposableHasSparkReplacement]
                               PermanentDrawerSheet()
                               ~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:63: Error: Composable PlainTooltip has a Spark replacement that should be used instead: PlainTooltip [MaterialComposableHasSparkReplacement]
                               PlainTooltip()
                               ~~~~~~~~~~~~
               src/foo/test.kt:64: Error: Composable RadioButton has a Spark replacement that should be used instead: RadioButton [MaterialComposableHasSparkReplacement]
                               RadioButton()
                               ~~~~~~~~~~~
               src/foo/test.kt:65: Error: Composable RangeSlider has a Spark replacement that should be used instead: RangeSlider [MaterialComposableHasSparkReplacement]
                               RangeSlider()
                               ~~~~~~~~~~~
               src/foo/test.kt:66: Error: Composable rememberDrawerState has a Spark replacement that should be used instead: rememberDrawerState [MaterialComposableHasSparkReplacement]
                               rememberDrawerState()
                               ~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:67: Error: Composable rememberTopAppBarState has a Spark replacement that should be used instead: rememberTopAppBarState [MaterialComposableHasSparkReplacement]
                               rememberTopAppBarState()
                               ~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:68: Error: Composable Scaffold has a Spark replacement that should be used instead: Scaffold [MaterialComposableHasSparkReplacement]
                               Scaffold()
                               ~~~~~~~~
               src/foo/test.kt:69: Error: Composable ScrollableTabRow has a Spark replacement that should be used instead: TabGroup [MaterialComposableHasSparkReplacement]
                               ScrollableTabRow()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:70: Error: Composable Slider has a Spark replacement that should be used instead: Slider [MaterialComposableHasSparkReplacement]
                               Slider()
                               ~~~~~~
               src/foo/test.kt:71: Error: Composable SmallFloatingActionButton has a Spark replacement that should be used instead: FloatingActionButton [MaterialComposableHasSparkReplacement]
                               SmallFloatingActionButton()
                               ~~~~~~~~~~~~~~~~~~~~~~~~~
               src/foo/test.kt:72: Error: Composable Snackbar has a Spark replacement that should be used instead: Snackbar [MaterialComposableHasSparkReplacement]
                               Snackbar()
                               ~~~~~~~~
               src/foo/test.kt:73: Error: Composable SnackbarHost has a Spark replacement that should be used instead: SnackbarHost [MaterialComposableHasSparkReplacement]
                               SnackbarHost()
                               ~~~~~~~~~~~~
               src/foo/test.kt:74: Error: Composable SnackbarHostState has a Spark replacement that should be used instead: SnackbarHostState [MaterialComposableHasSparkReplacement]
                               SnackbarHostState()
                               ~~~~~~~~~~~~~~~~~
               src/foo/test.kt:75: Error: Composable Surface has a Spark replacement that should be used instead: Surface [MaterialComposableHasSparkReplacement]
                               Surface()
                               ~~~~~~~
               src/foo/test.kt:76: Error: Composable Switch has a Spark replacement that should be used instead: Switch [MaterialComposableHasSparkReplacement]
                               Switch()
                               ~~~~~~
               src/foo/test.kt:77: Error: Composable Tab has a Spark replacement that should be used instead: Tab [MaterialComposableHasSparkReplacement]
                               Tab()
                               ~~~
               src/foo/test.kt:78: Error: Composable TabRow has a Spark replacement that should be used instead: TabGroup [MaterialComposableHasSparkReplacement]
                               TabRow()
                               ~~~~~~
               src/foo/test.kt:79: Error: Composable Text has a Spark replacement that should be used instead: Text [MaterialComposableHasSparkReplacement]
                               Text()
                               ~~~~
               src/foo/test.kt:80: Error: Composable TextButton has a Spark replacement that should be used instead: ButtonGhost [MaterialComposableHasSparkReplacement]
                               TextButton()
                               ~~~~~~~~~~
               src/foo/test.kt:81: Error: Composable TooltipBox has a Spark replacement that should be used instead: TooltipBox [MaterialComposableHasSparkReplacement]
                               TooltipBox()
                               ~~~~~~~~~~
               src/foo/test.kt:82: Error: Composable TopAppBar has a Spark replacement that should be used instead: TopAppBar [MaterialComposableHasSparkReplacement]
                               TopAppBar()
                               ~~~~~~~~~
               src/foo/test.kt:83: Error: Composable TriStateCheckbox has a Spark replacement that should be used instead: Checkbox [MaterialComposableHasSparkReplacement]
                               TriStateCheckbox()
                               ~~~~~~~~~~~~~~~~
               src/foo/test.kt:84: Error: Composable ripple has a Spark replacement that should be used instead: ripple [MaterialComposableHasSparkReplacement]
                               ripple()
                               ~~~~~~
               src/foo/test.kt:85: Error: Composable HorizontalDivider has a Spark replacement that should be used instead: HorizontalDivider [MaterialComposableHasSparkReplacement]
                               HorizontalDivider()
                               ~~~~~~~~~~~~~~~~~
               src/foo/test.kt:86: Error: Composable VerticalDivider has a Spark replacement that should be used instead: VerticalDivider [MaterialComposableHasSparkReplacement]
                               VerticalDivider()
                               ~~~~~~~~~~~~~~~
               78 errors
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 9: Replace AlertDialog with Spark's AlertDialog:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.dialog.AlertDialog
                Fix for src/foo/test.kt line 10: Replace AssistChip with Spark's Chip:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.chips.Chip
                @@ -10 +11 @@
                -                AssistChip()
                +                Chip()
                Fix for src/foo/test.kt line 11: Replace Badge with Spark's Badge:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.badge.Badge
                Fix for src/foo/test.kt line 12: Replace BadgedBox with Spark's BadgedBox:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.badge.BadgedBox
                Fix for src/foo/test.kt line 13: Replace BottomAppBar with Spark's BottomAppBar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.BottomAppBar
                Fix for src/foo/test.kt line 14: Replace BottomSheetScaffold with Spark's BottomSheetScaffold:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.bottomsheet.scaffold.BottomSheetScaffold
                Fix for src/foo/test.kt line 15: Replace ModalBottomSheet with Spark's BottomSheet:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.bottomsheet.BottomSheet
                @@ -15 +16 @@
                -                ModalBottomSheet()
                +                BottomSheet()
                Fix for src/foo/test.kt line 16: Replace Button with Spark's ButtonFilled:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.ButtonFilled
                @@ -16 +17 @@
                -                Button()
                +                ButtonFilled()
                Fix for src/foo/test.kt line 17: Replace Card with Spark's Card:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.card.Card
                Fix for src/foo/test.kt line 18: Replace CenterAlignedTopAppBar with Spark's TopAppBar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.TopAppBar
                @@ -18 +19 @@
                -                CenterAlignedTopAppBar()
                +                TopAppBar()
                Fix for src/foo/test.kt line 19: Replace Checkbox with Spark's Checkbox:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.toggles.Checkbox
                Fix for src/foo/test.kt line 20: Replace CircularProgressIndicator with Spark's CircularProgressIndicator:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.progress.CircularProgressIndicator
                Fix for src/foo/test.kt line 21: Replace contentColorFor with Spark's contentColorFor:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.tokens.contentColorFor
                Fix for src/foo/test.kt line 22: Replace DismissibleDrawerSheet with Spark's DismissibleDrawerSheet:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.drawer.DismissibleDrawerSheet
                Fix for src/foo/test.kt line 23: Replace DismissibleNavigationDrawer with Spark's DismissibleNavigationDrawer:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.drawer.DismissibleNavigationDrawer
                Fix for src/foo/test.kt line 24: Replace Divider with Spark's HorizontalDivider:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.divider.HorizontalDivider
                @@ -24 +25 @@
                -                Divider()
                +                HorizontalDivider()
                Fix for src/foo/test.kt line 25: Replace DropdownMenu with Spark's DropdownMenu:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.menu.DropdownMenu
                Fix for src/foo/test.kt line 26: Replace DropdownMenuItem with Spark's DropdownMenuItem:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.menu.DropdownMenuItem
                Fix for src/foo/test.kt line 27: Replace ElevatedAssistChip with Spark's Chip:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.chips.Chip
                @@ -27 +28 @@
                -                ElevatedAssistChip()
                +                Chip()
                Fix for src/foo/test.kt line 28: Replace ElevatedButton with Spark's ButtonFilled:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.ButtonFilled
                @@ -28 +29 @@
                -                ElevatedButton()
                +                ButtonFilled()
                Fix for src/foo/test.kt line 29: Replace ElevatedCard with Spark's Card:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.cards.Card
                @@ -29 +30 @@
                -                ElevatedCard()
                +                Card()
                Fix for src/foo/test.kt line 30: Replace ElevatedFilterChip with Spark's Chip:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.chips.Chip
                @@ -30 +31 @@
                -                ElevatedFilterChip()
                +                Chip()
                Fix for src/foo/test.kt line 31: Replace ExtendedFloatingActionButton with Spark's FloatingActionButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.FloatingActionButton
                @@ -31 +32 @@
                -                ExtendedFloatingActionButton()
                +                FloatingActionButton()
                Fix for src/foo/test.kt line 32: Replace FilledIconButton with Spark's IconButtonFilled:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.iconbuttons.toggle.IconButtonFilled
                @@ -32 +33 @@
                -                FilledIconButton()
                +                IconButtonFilled()
                Fix for src/foo/test.kt line 33: Replace FilledIconToggleButton with Spark's IconToggleButtonFilled:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonFilled
                @@ -33 +34 @@
                -                FilledIconToggleButton()
                +                IconToggleButtonFilled()
                Fix for src/foo/test.kt line 34: Replace FilledTonalButton with Spark's ButtonTinted:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.ButtonTinted
                @@ -34 +35 @@
                -                FilledTonalButton()
                +                ButtonTinted()
                Fix for src/foo/test.kt line 35: Replace FilledTonalIconButton with Spark's IconButtonTinted:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.iconbuttons.IconButtonTinted
                @@ -35 +36 @@
                -                FilledTonalIconButton()
                +                IconButtonTinted()
                Fix for src/foo/test.kt line 36: Replace FilledTonalIconToggleButton with Spark's IconToggleButtonTinted:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.iconbuttons.IconToggleButtonTinted
                @@ -36 +37 @@
                -                FilledTonalIconToggleButton()
                +                IconToggleButtonTinted()
                Fix for src/foo/test.kt line 37: Replace FilterChip with Spark's Chip:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.chips.Chip
                @@ -37 +38 @@
                -                FilterChip()
                +                Chip()
                Fix for src/foo/test.kt line 38: Replace FloatingActionButton with Spark's FloatingActionButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.FloatingActionButton
                Fix for src/foo/test.kt line 39: Replace Icon with Spark's Icon:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.icons.Icon
                Fix for src/foo/test.kt line 40: Replace IconButton with Spark's IconButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.icons.IconButton
                Fix for src/foo/test.kt line 41: Replace IconToggleButton with Spark's IconToggleButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.icons.IconToggleButton
                Fix for src/foo/test.kt line 42: Replace InputChip with Spark's Chip:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.chips.Chip
                @@ -42 +43 @@
                -                InputChip()
                +                Chip()
                Fix for src/foo/test.kt line 43: Replace LargeFloatingActionButton with Spark's FloatingActionButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.FloatingActionButton
                @@ -43 +44 @@
                -                LargeFloatingActionButton()
                +                FloatingActionButton()
                Fix for src/foo/test.kt line 44: Replace LargeTopAppBar with Spark's TopAppBar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.TopAppBar
                @@ -44 +45 @@
                -                LargeTopAppBar()
                +                TopAppBar()
                Fix for src/foo/test.kt line 45: Replace LeadingIconTab with Spark's Tab:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.tab.Tab
                @@ -45 +46 @@
                -                LeadingIconTab()
                +                Tab()
                Fix for src/foo/test.kt line 46: Replace LinearProgressIndicator with Spark's LinearProgressIndicator:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.progress.LinearProgressIndicator
                Fix for src/foo/test.kt line 47: Replace ListItem with Spark's ListItem:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.list.ListItem
                Fix for src/foo/test.kt line 48: Replace MaterialTheme with Spark's SparkTheme:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.SparkTheme
                @@ -48 +49 @@
                -                MaterialTheme()
                +                SparkTheme()
                Fix for src/foo/test.kt line 49: Replace MediumTopAppBar with Spark's TopAppBar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.TopAppBar
                @@ -49 +50 @@
                -                MediumTopAppBar()
                +                TopAppBar()
                Fix for src/foo/test.kt line 50: Replace ModalDrawerSheet with Spark's ModalDrawerSheet:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.drawer.ModalDrawerSheet
                Fix for src/foo/test.kt line 51: Replace ModalNavigationDrawer with Spark's ModalNavigationDrawer:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.drawer.ModalNavigationDrawer
                Fix for src/foo/test.kt line 52: Replace NavigationBar with Spark's NavigationBar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.NavigationBar
                Fix for src/foo/test.kt line 53: Replace NavigationBarItem with Spark's NavigationBarItem:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.NavigationBarItem
                Fix for src/foo/test.kt line 54: Replace NavigationDrawerItem with Spark's NavigationDrawerItem:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.navigation.NavigationDrawerItem
                Fix for src/foo/test.kt line 55: Replace NavigationRail with Spark's NavigationRail:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.navigation.NavigationRail
                Fix for src/foo/test.kt line 56: Replace NavigationRailItem with Spark's NavigationRailItem:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.navigation.NavigationRailItem
                Fix for src/foo/test.kt line 57: Replace OutlinedButton with Spark's ButtonOutlined:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.ButtonOutlined
                @@ -57 +58 @@
                -                OutlinedButton()
                +                ButtonOutlined()
                Fix for src/foo/test.kt line 58: Replace OutlinedCard with Spark's Card:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.cards.Card
                @@ -58 +59 @@
                -                OutlinedCard()
                +                Card()
                Fix for src/foo/test.kt line 59: Replace OutlinedIconButton with Spark's IconButtonOutlined:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.iconbuttons.IconButtonOutlined
                @@ -59 +60 @@
                -                OutlinedIconButton()
                +                IconButtonOutlined()
                Fix for src/foo/test.kt line 60: Replace OutlinedIconToggleButton with Spark's IconToggleButtonOutlined:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonOutlined
                @@ -60 +61 @@
                -                OutlinedIconToggleButton()
                +                IconToggleButtonOutlined()
                Fix for src/foo/test.kt line 61: Replace OutlinedTextField with Spark's TextField:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.textfields.TextField
                @@ -61 +62 @@
                -                OutlinedTextField()
                +                TextField()
                Fix for src/foo/test.kt line 62: Replace PermanentDrawerSheet with Spark's PermanentDrawerSheet:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.navigation.PermanentDrawerSheet
                Fix for src/foo/test.kt line 63: Replace PlainTooltip with Spark's PlainTooltip:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.popover.PlainTooltip
                Fix for src/foo/test.kt line 64: Replace RadioButton with Spark's RadioButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.toggles.RadioButton
                Fix for src/foo/test.kt line 65: Replace RangeSlider with Spark's RangeSlider:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.slider.RangeSlider
                Fix for src/foo/test.kt line 66: Replace rememberDrawerState with Spark's rememberDrawerState:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.drawer.rememberDrawerState
                Fix for src/foo/test.kt line 67: Replace rememberTopAppBarState with Spark's rememberTopAppBarState:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.rememberTopAppBarState
                Fix for src/foo/test.kt line 68: Replace Scaffold with Spark's Scaffold:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.scaffold.Scaffold
                Fix for src/foo/test.kt line 69: Replace ScrollableTabRow with Spark's TabGroup:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.tab.TabGroup
                @@ -69 +70 @@
                -                ScrollableTabRow()
                +                TabGroup()
                Fix for src/foo/test.kt line 70: Replace Slider with Spark's Slider:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.slider.Slider
                Fix for src/foo/test.kt line 71: Replace SmallFloatingActionButton with Spark's FloatingActionButton:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.FloatingActionButton
                @@ -71 +72 @@
                -                SmallFloatingActionButton()
                +                FloatingActionButton()
                Fix for src/foo/test.kt line 72: Replace Snackbar with Spark's Snackbar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.snackbars.Snackbar
                Fix for src/foo/test.kt line 73: Replace SnackbarHost with Spark's SnackbarHost:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.snackbars.SnackbarHost
                Fix for src/foo/test.kt line 74: Replace SnackbarHostState with Spark's SnackbarHostState:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.snackbars.SnackbarHostState
                Fix for src/foo/test.kt line 75: Replace Surface with Spark's Surface:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.surface.Surface
                Fix for src/foo/test.kt line 76: Replace Switch with Spark's Switch:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.toggles.Switch
                Fix for src/foo/test.kt line 77: Replace Tab with Spark's Tab:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.tab.Tab
                Fix for src/foo/test.kt line 78: Replace TabRow with Spark's TabGroup:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.tab.TabGroup
                @@ -78 +79 @@
                -                TabRow()
                +                TabGroup()
                Fix for src/foo/test.kt line 79: Replace Text with Spark's Text:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.text.Text
                Fix for src/foo/test.kt line 80: Replace TextButton with Spark's ButtonGhost:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.buttons.ButtonGhost
                @@ -80 +81 @@
                -                TextButton()
                +                ButtonGhost()
                Fix for src/foo/test.kt line 81: Replace TooltipBox with Spark's TooltipBox:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.popover.TooltipBox
                Fix for src/foo/test.kt line 82: Replace TopAppBar with Spark's TopAppBar:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.appbar.TopAppBar
                Fix for src/foo/test.kt line 83: Replace TriStateCheckbox with Spark's Checkbox:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.toggles.Checkbox
                @@ -83 +84 @@
                -                TriStateCheckbox()
                +                Checkbox()
                Fix for src/foo/test.kt line 84: Replace ripple with Spark's ripple:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.tokens.ripple
                Fix for src/foo/test.kt line 85: Replace HorizontalDivider with Spark's HorizontalDivider:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.divider.HorizontalDivider
                Fix for src/foo/test.kt line 86: Replace VerticalDivider with Spark's VerticalDivider:
                @@ -5,0 +6 @@
                +import com.adevinta.spark.components.divider.VerticalDivider
                """.trimIndent(),
            )
    }

    @Test
    public fun foundationUsages(): Unit = with(ISSUE) {
        lint().files(
            kotlin(
                """
                package foo
                import androidx.compose.foundation.*
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    Image()
                }
            """,
            ),
            *Composables,
            FoundationStub,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:8: ${explanation("Image" to "Illustration")}
                                    Image()
                                    ~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 8: Replace Image with Spark's Illustration:
                @@ -3 +3
                + import com.adevinta.spark.components.image.Illustration
                @@ -8 +9
                -                     Image()
                +                     Illustration()
                """.trimIndent(),
            )
    }

    @Test
    public fun coilUsages(): Unit = with(ISSUE) {
        lint().files(
            kotlin(
                """
                package foo
                import coil.compose.AsyncImage
                import coil.compose.SubcomposeAsyncImage
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    AsyncImage()
                    SubcomposeAsyncImage()
                }
            """,
            ),
            *Composables,
            CoilComponentsStub,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:9: ${explanation("AsyncImage" to "Image")}
                                    AsyncImage()
                                    ~~~~~~~~~~
                src/foo/test.kt:10: ${explanation("SubcomposeAsyncImage" to "Image")}
                                    SubcomposeAsyncImage()
                                    ~~~~~~~~~~~~~~~~~~~~
                2 errors, 0 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 9: Replace AsyncImage with Spark's Image:
                @@ -3 +3
                + import com.adevinta.spark.components.image.Image
                @@ -9 +10
                -                     AsyncImage()
                +                     Image()
                Fix for src/foo/test.kt line 10: Replace SubcomposeAsyncImage with Spark's Image:
                @@ -3 +3
                + import com.adevinta.spark.components.image.Image
                @@ -10 +11
                -                     SubcomposeAsyncImage()
                +                     Image()
                """.trimIndent(),
            )
    }

    @Test
    public fun noErrors() {
        lint().files(
            kotlin(
                """
                package foo
                import androidx.compose.runtime.*
                import com.adevinta.spark.*
                import com.adevinta.spark.components.appbar.*
                import com.adevinta.spark.components.badge.*
                import com.adevinta.spark.components.bottomsheet.*
                import com.adevinta.spark.components.bottomsheet.scaffold.*
                import com.adevinta.spark.components.buttons.*
                import com.adevinta.spark.components.card.*
                import com.adevinta.spark.components.chips.*
                import com.adevinta.spark.components.dialog.*
                import com.adevinta.spark.components.divider.*
                import com.adevinta.spark.components.drawer.*
                import com.adevinta.spark.components.icons.*
                import com.adevinta.spark.components.image.*
                import com.adevinta.spark.components.list.*
                import com.adevinta.spark.components.menu.*
                import com.adevinta.spark.components.navigation.*
                import com.adevinta.spark.components.popover.*
                import com.adevinta.spark.components.progress.*
                import com.adevinta.spark.components.scaffold.*
                import com.adevinta.spark.components.slider.*
                import com.adevinta.spark.components.snackbars.*
                import com.adevinta.spark.components.surface.*
                import com.adevinta.spark.components.tab.*
                import com.adevinta.spark.components.tags.*
                import com.adevinta.spark.components.text.*
                import com.adevinta.spark.components.textfields.*
                import com.adevinta.spark.components.toggles.*
                import com.adevinta.spark.tokens.*

                @Composable
                fun Test() {
                   AlertDialog()
                   Chip()
                   Badge()
                   BadgedBox()
                   BottomAppBar()
                   BottomSheetScaffold()
                   BottomSheet()
                   ButtonFilled()
                   Card()
                   TopAppBar()
                   Checkbox()
                   CircularProgressIndicator()
                   contentColorFor()
                   DismissibleDrawerSheet()
                   DismissibleNavigationDrawer()
                   HorizontalDivider()
                   DropdownMenu()
                   DropdownMenuItem()
                   Chip()
                   ButtonFilled()
                   Card()
                   Chip()
                   FloatingActionButton()
                   IconButtonFilled()
                   IconToggleButtonFilled()
                   ButtonTinted()
                   IconButtonTinted()
                   IconToggleButtonTinted()
                   Chip()
                   FloatingActionButton()
                   Icon()
                   IconButton()
                   IconToggleButton()
                   Chip()
                   FloatingActionButton()
                   TopAppBar()
                   Tab()
                   LinearProgressIndicator()
                   ListItem()
                   SparkTheme()
                   TopAppBar()
                   ModalDrawerSheet()
                   ModalNavigationDrawer()
                   NavigationBar()
                   NavigationBarItem()
                   NavigationDrawerItem()
                   NavigationRail()
                   NavigationRailItem()
                   ButtonOutlined()
                   Card()
                   IconButtonOutlined()
                   IconToggleButtonOutlined()
                   TextField()
                   PermanentDrawerSheet()
                   PlainTooltip()
                   RadioButton()
                   RangeSlider()
                   rememberDrawerState()
                   rememberTopAppBarState()
                   Scaffold()
                   TabGroup()
                   Slider()
                   FloatingActionButton()
                   Snackbar()
                   SnackbarHost()
                   SnackbarHostState()
                   Surface()
                   Switch()
                   Tab()
                   TabGroup()
                   Text()
                   ButtonGhost()
                   TooltipBox()
                   TopAppBar()
                   Checkbox()
                   ripple()
                   HorizontalDivider()
                   VerticalDivider()
                   Image()
                   AsyncImage()
                   SubcomposeAsyncImage()
                }
                """.trimIndent(),
            ),
            *Composables,
            *SparkComponentsStubs,
        )
            .run()
            .expectClean()
    }
}
