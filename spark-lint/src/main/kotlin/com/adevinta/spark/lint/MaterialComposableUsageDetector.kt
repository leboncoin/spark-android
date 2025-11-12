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
public class MaterialComposableUsageDetector :
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
            id = "MaterialComposableUsageDetector",
            briefDescription = "A Spark replacement is available for this Composable",
            explanation = "Material or any other third party Composable should be replaced with a Spark Composable.",
            category = CORRECTNESS,
            priority = 8,
            severity = ERROR,
            implementation = Implementation(
                MaterialComposableUsageDetector::class.java,
                EnumSet.of(JAVA_FILE, TEST_SOURCES),
            ),
        )

        private val REPLACEMENTS: Map<String, String> = mapOf(
            //region Material3
            "androidx.compose.material3.Button" to "com.adevinta.spark.components.buttons.ButtonFilled",
            "androidx.compose.material3.ElevatedButton" to "com.adevinta.spark.components.buttons.ButtonFilled",
            "androidx.compose.material3.FilledTonalButton" to "com.adevinta.spark.components.buttons.ButtonTinted",
            "androidx.compose.material3.OutlinedButton" to "com.adevinta.spark.components.buttons.ButtonOutlined",
            "androidx.compose.material3.TextButton" to "com.adevinta.spark.components.buttons.ButtonGhost",
            "androidx.compose.material3.FilledTextField" to "com.adevinta.spark.components.textfields.TextField",
            "androidx.compose.material3.OutlinedTextField" to "com.adevinta.spark.components.textfields.TextField",
            "androidx.compose.material3.CheckBox" to "com.adevinta.spark.components.toggles.CheckBox",
            "androidx.compose.material3.TriStateCheckbox" to "com.adevinta.spark.components.toggles.CheckBox",
            "androidx.compose.material3.RadioButton" to "com.adevinta.spark.components.toggles.RadioButton",
            "androidx.compose.material3.Switch" to "com.adevinta.spark.components.toggles.Switch",
            "androidx.compose.material3.Snackbar" to "com.adevinta.spark.components.snackbars.Snackbar",
            "androidx.compose.material3.LinearProgressIndicator" to
                "com.adevinta.spark.components.progress.LinearProgressIndicator",
            "androidx.compose.material3.CircularProgressIndicator" to
                "com.adevinta.spark.components.progress.CircularProgressIndicator",
            "androidx.compose.material3.MaterialTheme" to "com.adevinta.spark.SparkTheme",
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
