/*
 * Copyright (c) 2025 Adevinta
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

import com.android.tools.lint.detector.api.Category.Companion.A11Y
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.evaluateString
import java.util.EnumSet

public class TagContentDescriptionDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = METHOD_NAMES

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        // Ensure we're targeting the correct Tag Composable function.
        if (context.evaluator.getPackage(method)?.qualifiedName != SPARK_TAG_PACKAGE_NAME) {
            return
        }

        val textArgument: UExpression? = node.getArgumentForParameterName("text")
        val contentDescriptionArgument: UExpression? = node.getArgumentForParameterName("contentDescription")
        val contentArgument: UExpression? = node.getArgumentForParameterName("content")

        val textValue: String? = textArgument?.evaluateString()
        val contentDescriptionValue: String? = contentDescriptionArgument?.evaluateString()

        // Check for content slot in both named parameter and lambda form
        val hasContentSlot: Boolean = contentArgument != null || node.valueArguments.any { it is ULambdaExpression }

        if (textValue.isNullOrBlank() && contentDescriptionValue.isNullOrBlank() && !hasContentSlot) {
            method.reportUsage(context, node)
        }
    }

    // Helper to find argument by parameter name, which is more robust for Kotlin calls
    private fun UCallExpression.getArgumentForParameterName(name: String): UExpression? {
        // Get the method being called
        val method = resolve() ?: return null

        // Get the parameter list
        val parameters = method.parameterList.parameters

        // Find the index of the parameter with the given name
        val parameterIndex = parameters.indexOfFirst { it.name == name }
        if (parameterIndex == -1) return null

        // Get the argument at that index
        return valueArguments.getOrNull(parameterIndex)
    }

    private fun PsiMethod.reportUsage(
        context: JavaContext,
        node: UCallExpression,
    ) = Incident(context)
        .issue(ISSUE)
        .location(context.getNameLocation(node))
        .message("Tag $name is missing a contentDescription when no text label is provided for screen readers")
//        .fix(quickfixData(node))
        .report()

    // Doesn't work as expected since if we have other parameters it'll mess with the ordering
//    private fun quickfixData(node: UCallExpression) = LintFix.create()
//        .name("Add contentDescription parameter")
//        .replace()
//        .with("${node.methodName}(contentDescription = TODO(\"Add a description for screen readers\"))")
//        .robot(false)
//        .build()

    internal companion object {
        val ISSUE = Issue.create(
            id = "MissingContentDescriptionForIconOnlyTag",
            briefDescription = "Missing contentDescription for icon-only SparkTag",
            explanation = """
            When a SparkTag is used without a 'text' (or with a blank 'text'),
            it effectively acts as an icon. For accessibility, such tags must have a
            'contentDescription' provided.
            """.trimIndent(),
            category = A11Y,
            priority = 7,
            severity = ERROR,
            implementation = Implementation(
                TagContentDescriptionDetector::class.java,
                EnumSet.of(JAVA_FILE, TEST_SOURCES),
            ),
        )

        private val METHOD_NAMES: List<String> = listOf("TagFilled", "TagOutlined", "TagTinted")
        private const val SPARK_TAG_PACKAGE_NAME: String = "com.adevinta.spark.components.tags"
    }
}
