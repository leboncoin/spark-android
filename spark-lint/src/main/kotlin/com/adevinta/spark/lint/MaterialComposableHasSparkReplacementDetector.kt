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
@file:Suppress("UnstableApiUsage", "ktlint:standard:max-line-length")

package com.adevinta.spark.lint

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.EnumSet

/**
 * Checks if a Composable has a Spark replacement.
 */
public class MaterialComposableHasSparkReplacementDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = METHOD_NAMES

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val packageName = (method.containingFile as? PsiJavaFile)?.packageName ?: return
        val replacement = REPLACEMENTS["$packageName.${method.name}"] ?: return
        method.reportUsage(context, node, replacement)
    }

    private fun PsiMethod.reportUsage(
        context: JavaContext,
        node: UCallExpression,
        replacement: String,
    ) = Incident(context)
        .issue(ISSUE).location(context.getNameLocation(node))
        .message("Composable $name has a Spark replacement that should be used instead: ${replacement.methodName()}")
        .fix(quickfixData(replacement))
        .report()

    private fun PsiMethod.quickfixData(replacement: String) = LintFix.create()
        .name("Replace $name with Spark's ${replacement.methodName()}")
        .replace().all().with(replacement.methodName()).imports(replacement).shortenNames()
        .robot(false)
        .independent(true)
        .build()

    internal companion object {
        val ISSUE = Issue.create(
            id = "MaterialComposableHasSparkReplacement",
            briefDescription = "A Spark replacement is available for this Composable",
            explanation = "Material or any other third party Composable should be replaced with a Spark Composable.",
            category = CORRECTNESS,
            priority = 8,
            severity = ERROR,
            implementation = Implementation(
                MaterialComposableHasSparkReplacementDetector::class.java,
                EnumSet.of(JAVA_FILE, TEST_SOURCES),
            ),
        )

        private val REPLACEMENTS: Map<String, String> = mapOf(
            //region Material3
            "androidx.compose.material3.AlertDialog" to "com.adevinta.spark.components.dialog.AlertDialog",
            "androidx.compose.material3.AssistChip" to "com.adevinta.spark.components.chips.Chip",
            "androidx.compose.material3.Badge" to "com.adevinta.spark.components.badge.Badge",
            "androidx.compose.material3.BadgedBox" to "com.adevinta.spark.components.badge.BadgedBox",
            "androidx.compose.material3.BottomAppBar" to "com.adevinta.spark.components.appbar.BottomAppBar",
            "androidx.compose.material3.BottomSheetScaffold" to
                "com.adevinta.spark.components.bottomsheet.scaffold.BottomSheetScaffold",
            "androidx.compose.material3.ModalBottomSheet" to "com.adevinta.spark.components.bottomsheet.BottomSheet",
            "androidx.compose.material3.Button" to "com.adevinta.spark.components.buttons.ButtonFilled",
            "androidx.compose.material3.Card" to "com.adevinta.spark.components.card.Card",
            "androidx.compose.material3.CenterAlignedTopAppBar" to "com.adevinta.spark.components.appbar.TopAppBar",
            "androidx.compose.material3.Checkbox" to "com.adevinta.spark.components.toggles.Checkbox",
            "androidx.compose.material3.CircularProgressIndicator" to
                "com.adevinta.spark.components.progress.CircularProgressIndicator",
            "androidx.compose.material3.contentColorFor" to "com.adevinta.spark.tokens.contentColorFor",
            "androidx.compose.material3.DismissibleDrawerSheet" to
                "com.adevinta.spark.components.drawer.DismissibleDrawerSheet",
            "androidx.compose.material3.DismissibleNavigationDrawer" to
                "com.adevinta.spark.components.drawer.DismissibleNavigationDrawer",
            "androidx.compose.material3.Divider" to "com.adevinta.spark.components.divider.HorizontalDivider",
            "androidx.compose.material3.DropdownMenu" to "com.adevinta.spark.components.menu.DropdownMenu",
            "androidx.compose.material3.DropdownMenuItem" to "com.adevinta.spark.components.menu.DropdownMenuItem",
            "androidx.compose.material3.ElevatedAssistChip" to "com.adevinta.spark.components.chips.Chip",
            "androidx.compose.material3.ElevatedButton" to "com.adevinta.spark.components.buttons.ButtonFilled",
            "androidx.compose.material3.ElevatedCard" to "com.adevinta.spark.components.cards.Card",
            "androidx.compose.material3.ElevatedFilterChip" to "com.adevinta.spark.components.chips.Chip",
            "androidx.compose.material3.ExtendedFloatingActionButton" to
                "com.adevinta.spark.components.buttons.FloatingActionButton",
            "androidx.compose.material3.FilledIconButton" to
                "com.adevinta.spark.components.iconbuttons.toggle.IconButtonFilled",
            "androidx.compose.material3.FilledIconToggleButton" to
                "com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonFilled",
            "androidx.compose.material3.FilledTonalButton" to "com.adevinta.spark.components.buttons.ButtonTinted",
            "androidx.compose.material3.FilledTonalIconButton" to
                "com.adevinta.spark.components.iconbuttons.IconButtonTinted",
            "androidx.compose.material3.FilledTonalIconToggleButton" to
                "com.adevinta.spark.components.iconbuttons.IconToggleButtonTinted",
            "androidx.compose.material3.FilterChip" to "com.adevinta.spark.components.chips.Chip",
            "androidx.compose.material3.FloatingActionButton" to
                "com.adevinta.spark.components.buttons.FloatingActionButton",
            "androidx.compose.material3.Icon" to "com.adevinta.spark.components.icons.Icon",
            "androidx.compose.material3.IconButton" to "com.adevinta.spark.components.icons.IconButton",
            "androidx.compose.material3.IconToggleButton" to "com.adevinta.spark.components.icons.IconToggleButton",
            "androidx.compose.material3.InputChip" to "com.adevinta.spark.components.chips.Chip",
            "androidx.compose.material3.LargeFloatingActionButton" to
                "com.adevinta.spark.components.buttons.FloatingActionButton",
            "androidx.compose.material3.LargeTopAppBar" to "com.adevinta.spark.components.appbar.TopAppBar",
            "androidx.compose.material3.LeadingIconTab" to "com.adevinta.spark.components.tab.Tab",
            "androidx.compose.material3.LinearProgressIndicator" to
                "com.adevinta.spark.components.progress.LinearProgressIndicator",
            "androidx.compose.material3.ListItem" to "com.adevinta.spark.components.list.ListItem",
            "androidx.compose.material3.MaterialTheme" to "com.adevinta.spark.SparkTheme",
            "androidx.compose.material3.MediumTopAppBar" to "com.adevinta.spark.components.appbar.TopAppBar",
            "androidx.compose.material3.ModalDrawerSheet" to "com.adevinta.spark.components.drawer.ModalDrawerSheet",
            "androidx.compose.material3.ModalNavigationDrawer" to
                "com.adevinta.spark.components.drawer.ModalNavigationDrawer",
            "androidx.compose.material3.NavigationBar" to "com.adevinta.spark.components.appbar.NavigationBar",
            "androidx.compose.material3.NavigationBarItem" to "com.adevinta.spark.components.appbar.NavigationBarItem",
            "androidx.compose.material3.NavigationDrawerItem" to
                "com.adevinta.spark.components.navigation.NavigationDrawerItem",
            "androidx.compose.material3.NavigationRail" to "com.adevinta.spark.components.navigation.NavigationRail",
            "androidx.compose.material3.NavigationRailItem" to
                "com.adevinta.spark.components.navigation.NavigationRailItem",
            "androidx.compose.material3.OutlinedButton" to "com.adevinta.spark.components.buttons.ButtonOutlined",
            "androidx.compose.material3.OutlinedCard" to "com.adevinta.spark.components.cards.Card",
            "androidx.compose.material3.OutlinedIconButton" to
                "com.adevinta.spark.components.iconbuttons.IconButtonOutlined",
            "androidx.compose.material3.OutlinedIconToggleButton" to
                "com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonOutlined",
            "androidx.compose.material3.OutlinedTextField" to "com.adevinta.spark.components.textfields.TextField",
            "androidx.compose.material3.PermanentDrawerSheet" to
                "com.adevinta.spark.components.navigation.PermanentDrawerSheet",
            "androidx.compose.material3.PlainTooltip" to "com.adevinta.spark.components.popover.PlainTooltip",
            "androidx.compose.material3.RadioButton" to "com.adevinta.spark.components.toggles.RadioButton",
            "androidx.compose.material3.RangeSlider" to "com.adevinta.spark.components.slider.RangeSlider",
            "androidx.compose.material3.rememberDrawerState" to
                "com.adevinta.spark.components.drawer.rememberDrawerState",
            "androidx.compose.material3.rememberTopAppBarState" to
                "com.adevinta.spark.components.appbar.rememberTopAppBarState",
            "androidx.compose.material3.Scaffold" to "com.adevinta.spark.components.scaffold.Scaffold",
            "androidx.compose.material3.ScrollableTabRow" to "com.adevinta.spark.components.tab.TabGroup",
            "androidx.compose.material3.Slider" to "com.adevinta.spark.components.slider.Slider",
            "androidx.compose.material3.SmallFloatingActionButton" to
                "com.adevinta.spark.components.buttons.FloatingActionButton",
            "androidx.compose.material3.Snackbar" to "com.adevinta.spark.components.snackbars.Snackbar",
            "androidx.compose.material3.SnackbarHost" to "com.adevinta.spark.components.snackbars.SnackbarHost",
            "androidx.compose.material3.SnackbarHostState" to
                "com.adevinta.spark.components.snackbars.SnackbarHostState",
            "androidx.compose.material3.Surface" to "com.adevinta.spark.components.surface.Surface",
            "androidx.compose.material3.Switch" to "com.adevinta.spark.components.toggles.Switch",
            "androidx.compose.material3.Tab" to "com.adevinta.spark.components.tab.Tab",
            "androidx.compose.material3.TabRow" to "com.adevinta.spark.components.tab.TabGroup",
            "androidx.compose.material3.Text" to "com.adevinta.spark.components.text.Text",
            "androidx.compose.material3.TextButton" to "com.adevinta.spark.components.buttons.ButtonGhost",
            "androidx.compose.material3.TooltipBox" to "com.adevinta.spark.components.popover.TooltipBox",
            "androidx.compose.material3.TopAppBar" to "com.adevinta.spark.components.appbar.TopAppBar",
            "androidx.compose.material3.TriStateCheckbox" to "com.adevinta.spark.components.toggles.Checkbox",
            "androidx.compose.material3.ripple" to "com.adevinta.spark.tokens.ripple",
            "androidx.compose.material3.HorizontalDivider" to "com.adevinta.spark.components.divider.HorizontalDivider",
            "androidx.compose.material3.VerticalDivider" to "com.adevinta.spark.components.divider.VerticalDivider",
            //endregion
            //region Material
            "androidx.compose.material.ripple.rememberRipple" to "com.adevinta.spark.tokens.rememberRipple",
            //endregion
            //region Foundation
            "androidx.compose.foundation.Image" to "com.adevinta.spark.components.image.Illustration",
            //endregion
            //region Coil
            "coil.compose.AsyncImage" to "com.adevinta.spark.components.image.Image",
            "coil.compose.SubcomposeAsyncImage" to "com.adevinta.spark.components.image.Image",
            //endregion
        )

        private val METHOD_NAMES = REPLACEMENTS.keys.map { it.methodName() }
        private fun String.methodName() = substringAfterLast(".")
    }
}
