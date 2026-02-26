/*
 * Copyright (c) 2026 Adevinta
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

import com.adevinta.spark.lint.extensions.findUnreferencedParameters
import com.adevinta.spark.lint.extensions.isInPackageName
import com.adevinta.spark.lint.extensions.sourceImplementation
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.computeKotlinArgumentMapping
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.ULambdaExpression
import kotlin.collections.filter
import kotlin.collections.filterIsInstance
import kotlin.collections.firstOrNull
import kotlin.collections.orEmpty
import kotlin.let

public class ScaffoldPaddingDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = listOf("Scaffold")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (!method.isInPackageName("com.adevinta.spark.components.scaffold")) return

        val contentArgument =
            computeKotlinArgumentMapping(node, method)
                .orEmpty()
                .filter { (_, parameter) -> parameter.name == "content" }
                .keys
                .filterIsInstance<ULambdaExpression>()
                .firstOrNull() ?: return

        val contentPaddingParameter =
            contentArgument.findUnreferencedParameters().firstOrNull() ?: return

        val location = contentPaddingParameter.parameter
            ?.let { context.getLocation(it) }
            ?: context.getLocation(contentArgument)

        context.report(
            issue = ISSUE,
            scope = node,
            location = location,
            message = "Content padding parameter `${contentPaddingParameter.name}` is not used",
        )
    }

    public companion object Companion {
        public val ISSUE: Issue =
            Issue.create(
                id = "UnusedSparkScaffoldPaddingParameter",
                briefDescription = "Unused `Scaffold`'s `content` padding",
                explanation = "The `content` lambda in `Scaffold` has a padding parameter " +
                    "which will include any inner padding for the content due to app bars. If this " +
                    "parameter is ignored, then content may be obscured by the app bars resulting in " +
                    "visual issues or elements that can't be interacted with.",
                category = Category.CORRECTNESS,
                priority = 3,
                severity = Severity.ERROR,
                implementation = sourceImplementation<ScaffoldPaddingDetector>(shouldRunOnTestSources = false),
            )
    }
}
