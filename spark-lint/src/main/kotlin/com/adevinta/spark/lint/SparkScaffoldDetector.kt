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
import org.jetbrains.uast.UBlockExpression
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElementOfType
import org.jetbrains.uast.visitor.AbstractUastVisitor

/** Reports misuses of Spark's `Scaffold` slots: unused `content` padding and bars that ignore window insets. */
public class SparkScaffoldDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = listOf("Scaffold")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (!method.isInPackageName(SCAFFOLD_PACKAGE)) return

        computeKotlinArgumentMapping(node, method).orEmpty().forEach { (argument, parameter) ->
            val lambda = argument as? ULambdaExpression ?: return@forEach
            when (parameter.name) {
                "content" -> checkContentPadding(context, node, lambda)
                TOP_BAR, BOTTOM_BAR -> checkBarInsets(context, node, lambda, parameter.name)
            }
        }
    }

    private fun checkContentPadding(context: JavaContext, node: UCallExpression, content: ULambdaExpression) {
        val contentPaddingParameter = content.findUnreferencedParameters().firstOrNull() ?: return

        val location = contentPaddingParameter.parameter
            ?.let { context.getLocation(it) }
            ?: context.getLocation(content)

        context.report(
            issue = UNUSED_PADDING_ISSUE,
            scope = node,
            location = location,
            message = "Content padding parameter `${contentPaddingParameter.name}` is not used",
        )
    }

    private fun checkBarInsets(context: JavaContext, node: UCallExpression, bar: ULambdaExpression, slotName: String) {
        if (bar.isEmpty()) return
        if (bar.body.calls().any { it.handlesInsets(visited = mutableSetOf()) }) return

        context.report(
            issue = BAR_WITHOUT_INSETS_ISSUE,
            scope = node,
            location = context.getLocation(bar),
            message = "`$slotName` content does not handle window insets: use ${SUGGESTED_BARS.getValue(slotName)} " +
                "or apply a window insets padding modifier",
        )
    }

    private fun UCallExpression.handlesInsets(visited: MutableSet<PsiMethod>): Boolean {
        // Unresolvable calls are unknown: assume compliant rather than risk a false positive.
        val method = resolve() ?: return true
        if (method.takesWindowInsets()) return true
        if (method.isInPackageName(LAYOUT_PACKAGE) && method.name in INSETS_PADDING_MODIFIERS) return true

        val body = method.toUElementOfType<UMethod>()?.uastBody ?: return false
        if (!visited.add(method)) return false
        return body.calls().any { it.handlesInsets(visited) }
    }

    public companion object Companion {
        private const val SCAFFOLD_PACKAGE = "com.adevinta.spark.components.scaffold"
        private const val LAYOUT_PACKAGE = "androidx.compose.foundation.layout"
        private const val WINDOW_INSETS = "$LAYOUT_PACKAGE.WindowInsets"

        private const val TOP_BAR = "topBar"
        private const val BOTTOM_BAR = "bottomBar"

        private val SUGGESTED_BARS = mapOf(
            TOP_BAR to "`TopAppBar`, `CenterAlignedTopAppBar`, `MediumTopAppBar` or `LargeTopAppBar`",
            BOTTOM_BAR to "`BottomAppBar` or `NavigationBar`",
        )

        private val INSETS_PADDING_MODIFIERS = setOf(
            "navigationBarsPadding",
            "statusBarsPadding",
            "systemBarsPadding",
            "safeDrawingPadding",
            "safeContentPadding",
            "safeGesturesPadding",
        )

        // Spark app bars and inset modifiers such as `windowInsetsPadding` all take a `WindowInsets`.
        private fun PsiMethod.takesWindowInsets(): Boolean =
            parameterList.parameters.any { it.type.canonicalText == WINDOW_INSETS }

        private fun ULambdaExpression.isEmpty(): Boolean = (body as? UBlockExpression)?.expressions.isNullOrEmpty()

        private fun UElement.calls(): List<UCallExpression> = buildList {
            accept(
                object : AbstractUastVisitor() {
                    override fun visitCallExpression(node: UCallExpression): Boolean {
                        add(node)
                        return false
                    }
                },
            )
        }

        private val IMPLEMENTATION = sourceImplementation<SparkScaffoldDetector>(shouldRunOnTestSources = false)

        public val UNUSED_PADDING_ISSUE: Issue =
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
                implementation = IMPLEMENTATION,
            ).setAndroidSpecific(true)

        public val BAR_WITHOUT_INSETS_ISSUE: Issue =
            Issue.create(
                id = "SparkScaffoldBarWithoutInsets",
                briefDescription = "`Scaffold` bar slot does not handle window insets",
                explanation = "Spark app bars (`TopAppBar`, `BottomAppBar`, `NavigationBar`, …) apply " +
                    "the system bars window insets. Content placed in `Scaffold`'s `topBar` or " +
                    "`bottomBar` without one of them, nor a window insets padding modifier, is drawn " +
                    "behind the status or navigation bar in edge-to-edge.",
                category = Category.CORRECTNESS,
                priority = 5,
                severity = Severity.ERROR,
                implementation = IMPLEMENTATION,
            ).setAndroidSpecific(true)
    }
}
