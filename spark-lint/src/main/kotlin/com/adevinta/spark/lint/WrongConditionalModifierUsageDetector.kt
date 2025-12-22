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

import com.adevinta.spark.lint.extensions.sourceImplementation
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.findSelector
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.visitor.AbstractUastVisitor
import kotlin.collections.lastOrNull
import kotlin.jvm.java
import kotlin.text.endsWith

public class WrongConditionalModifierUsageDetector : Detector(), SourceCodeScanner {

    internal companion object {
        val ISSUE = Issue.create(
            id = "WrongConditionalModifierUsageDetector",
            briefDescription = "Static Modifier usage in a conditional Modifier",
            explanation = """
                When using conditional extension functions like 'ifTrue', 'ifFalse', \
                'ifNotNull', or 'ifNull' on Modifier, you should not use the static reference \
                to 'Modifier' inside the lambda. Instead, use the implicit 'this' receiver \
                or omit it, as it's already an extension on Modifier. \
                
                Incorrect:
                ```
                Modifier.ifTrue(condition) { Modifier.background(Color.Red) }
                ```
                
                Correct:
                ```
                Modifier..ifTrue(condition) { background(Color.Red) }
                ```
                """,
            category = CORRECTNESS,
            priority = 6,
            severity = ERROR,
            implementation = sourceImplementation<WrongConditionalModifierUsageDetector>(shouldRunOnTestSources = false),
        )

        private val CONDITIONAL_MODIFIERS = setOf(
            "ifTrue", "ifFalse", "ifNotNull", "ifNull",
        )
    }

    override fun getApplicableUastTypes(): List<Class<UCallExpression>> = listOf(UCallExpression::class.java)

    private fun quickFix() = fix().name("Remove 'Modifier.' prefix")
        .replace().text("Modifier.").with("")
        .autoFix()
        .build()

    private fun JavaContext.reportIssue(
        node: UElement,
        methodName: String,
    ) = report(
        issue = ISSUE,
        scope = node,
        location = getLocation(node),
        message = "Do not use the static `Modifier` reference in the lambda of `${methodName}`. " +
                "Use the implicit 'this' receiver instead",
        quickfixData = quickFix(),
    )

    override fun createUastHandler(context: JavaContext): UElementHandler = object : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            val methodName = node.methodName ?: return
            if (methodName !in CONDITIONAL_MODIFIERS) return

            val receiverType = node.receiverType?.canonicalText ?: return
            if (!receiverType.endsWith("Modifier")) return

            val lambdaArg = node.valueArguments.lastOrNull() as? ULambdaExpression ?: return
            lambdaArg.accept(
                object : AbstractUastVisitor() {
                    override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression): Boolean {
                        if (node.receiver.findSelector().asRenderString() != "Modifier") return super.visitQualifiedReferenceExpression(node)

                        context.reportIssue(node, methodName)
                        return true
                    }
                },
            )
        }
    }
}
