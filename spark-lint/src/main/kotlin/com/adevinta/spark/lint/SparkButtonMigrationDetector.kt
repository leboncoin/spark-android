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
@file:Suppress("UnstableApiUsage")

package com.adevinta.spark.lint

import com.adevinta.spark.lint.extensions.sourceImplementation
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiEnumConstant
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UNamedExpression
import org.jetbrains.uast.UReferenceExpression

/**
 * Reports calls to old style-based Spark button composables that should be replaced with the new
 * `Button.<Variant>` API.
 */
public class SparkButtonMigrationDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> =
        (oldComposableToStyle.keys + directVariantForOldComposable.keys).distinct()

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val packageName = (method.containingFile as? PsiJavaFile)?.packageName ?: return
        val calleeName = node.methodName ?: method.name
        if (packageName != oldComposablePackage[calleeName]) return
        val params = node.resolve()?.parameterList?.parameters

        val directVariant = directVariantForOldComposable[calleeName]
        val message: String
        val fix: LintFix?
        if (directVariant != null) {
            // TextLinkButton and similar always map to one callee, whatever the intent.
            val targetCallee = variantToCallee[directVariant] ?: return
            message = "`$calleeName` is an old Spark button. Replace it with `$targetCallee`."
            fix = buildFix(context, node, calleeName, targetCallee, params)
        } else {
            val style = oldComposableToStyle[calleeName] ?: return
            val intent = resolveIntent(node, calleeName, params)
            if (intent != null) {
                val variant = mapToVariant(intent, style)
                val targetCallee = variantToCallee[variant] ?: return
                message = "`$calleeName` is an old style-based Spark button. Replace it with `$targetCallee`."
                fix = buildFix(context, node, calleeName, targetCallee, params)
            } else {
                message =
                    "`$calleeName` is an old style-based Spark button. The `intent` argument could not be resolved to a constant `ButtonIntent`; migrate this call by hand."
                fix = null
            }
        }
        val incident = Incident(context)
            .issue(ISSUE)
            .location(context.getNameLocation(node))
            .message(message)
        if (fix != null) incident.fix(fix)
        incident.report()
    }

    /**
     * Resolves the [ButtonIntent] for a call to one of the old button composables.
     *
     * Returns null when the `intent` argument is present but is not a constant enum reference
     * (for example, a variable, a function call, or a `when` expression).
     */
    private fun resolveIntent(
        node: UCallExpression,
        calleeName: String,
        params: Array<PsiParameter>?,
    ): ButtonIntent? {
        val arg = findIntentArgument(node, params)
            ?: return defaultIntentForOldComposable[calleeName] // absent → use per-button default
        return resolveIntentFromExpression(arg) // present → resolve or return null (unresolved)
    }

    /**
     * Finds the argument bound to the `intent` parameter in [node].
     *
     * Binds by parameter, not by raw argument index: UAST omits defaulted optional parameters from
     * `valueArguments`, so a positional index into that list does not align with the parameter list.
     * `getArgumentForParameter` handles named args, positional args, and returns null when the
     * parameter uses its default value. This handles all three overloads where `intent` appears at
     * different positions without relying on a hard-coded index.
     *
     * Returns null when no argument for `intent` is present in the call (the caller relies on the
     * default parameter value).
     */
    private fun findIntentArgument(node: UCallExpression, params: Array<PsiParameter>?): UExpression? {
        if (params == null) return null
        val intentIndex = params.indexOfFirst { it.name == "intent" }
        if (intentIndex < 0) return null
        val arg = node.getArgumentForParameter(intentIndex) ?: return null
        return if (arg is UNamedExpression) arg.expression else arg
    }

    /**
     * Resolves a [UExpression] to a [ButtonIntent] enum constant.
     *
     * Returns null when the expression is anything other than a direct reference to a
     * [ButtonIntent] enum entry (for example, a variable, a call, or a conditional expression).
     */
    private fun resolveIntentFromExpression(expr: UExpression): ButtonIntent? {
        val ref = expr as? UReferenceExpression ?: return null
        val enumConstant = ref.resolve() as? PsiEnumConstant ?: return null
        if (enumConstant.containingClass?.name != "ButtonIntent") return null
        return ButtonIntent.entries.firstOrNull { it.name == enumConstant.name }
    }

    /**
     * Builds a [LintFix] that rewrites the old button call to the new `Button.<Variant>` API using
     * named arguments.
     *
     * Handles three overloads:
     * - Content-slot overload: the content lambda is preserved verbatim as a trailing lambda.
     * - `text: String` overload: the string is emitted as a named `text` argument.
     * - `text: AnnotatedString` overload: the expression is wrapped in a `{ Text(text = ...) }`
     *   trailing lambda, because the new API has no AnnotatedString overload.
     *
     * The fix drops `intent` and `shape` (absent from the new API), preserves all other explicitly
     * passed arguments as named args, and appends the content lambda as a trailing lambda block.
     * Returns null for any call where argument source text cannot be resolved.
     */
    private fun buildFix(
        context: JavaContext,
        node: UCallExpression,
        calleeName: String,
        targetCallee: String,
        params: Array<PsiParameter>?,
    ): LintFix? {
        if (params == null) return null

        // Identify overload via the resolved parameter list
        val textParam = params.firstOrNull { it.name == "text" }
        val isContentOverload = params.any { it.name == "content" }
        // The target has no content-slot overload, so a content-slot call cannot migrate automatically.
        if (isContentOverload && targetCallee in CALLEES_WITHOUT_CONTENT_OVERLOAD) return null
        val isStringOverload = textParam?.type?.canonicalText == "java.lang.String"
        val isAnnotatedStringOverload = textParam != null && !isStringOverload
        if (!isContentOverload && !isStringOverload && !isAnnotatedStringOverload) return null

        val namedArgs = mutableListOf<String>()
        var trailingLambdaText: String? = null
        var annotatedStringTextExpr: String? = null

        // Iterate the parameter list and bind each parameter to its argument through
        // `getArgumentForParameter`. This aligns named and positional arguments correctly and
        // returns null for any parameter left at its default value, which is then omitted.
        for ((index, param) in params.withIndex()) {
            val rawArg = node.getArgumentForParameter(index) ?: continue
            val arg = if (rawArg is UNamedExpression) rawArg.expression else rawArg
            val paramName = param.name

            // Drop parameters that do not exist in the new API
            if (paramName == "intent" || paramName == "shape") continue

            val valueText = arg.sourcePsi?.text ?: return null

            when {
                // AnnotatedString text becomes the body of a synthesised content lambda
                isAnnotatedStringOverload && paramName == "text" -> annotatedStringTextExpr = valueText

                // Content lambda is emitted as a trailing lambda, not a named arg
                paramName == "content" -> trailingLambdaText = valueText

                else -> namedArgs.add("$paramName = $valueText")
            }
        }

        // For the AnnotatedString overload, synthesise a content lambda that wraps the text in
        // Text(). The new API has no AnnotatedString overload, so the text must go through the content slot.
        if (isAnnotatedStringOverload) {
            val textExpr = annotatedStringTextExpr ?: return null
            trailingLambdaText = "{ Text(text = $textExpr) }"
        }

        val newText = buildString {
            append(targetCallee)
            append('(')
            append(namedArgs.joinToString(", "))
            append(')')
            if (trailingLambdaText != null) {
                append(' ')
                append(trailingLambdaText)
            }
        }

        val imports = buildList {
            add("com.adevinta.spark.components.buttons.Button")
            if (isAnnotatedStringOverload) add("com.adevinta.spark.components.text.Text")
        }

        return LintFix.create()
            .name("Replace $calleeName with $targetCallee")
            .replace()
            .range(context.getLocation(node))
            .with(newText)
            .imports(*imports.toTypedArray())
            .shortenNames()
            .reformat(true)
            .build()
    }

    internal companion object {
        /** New callees that have no content-slot overload, so a content-slot call cannot autofix. */
        private val CALLEES_WITHOUT_CONTENT_OVERLOAD = setOf("Button.Underlined")

        val ISSUE: Issue = Issue.create(
            id = "SparkButtonMigration",
            briefDescription = "Old Spark button should be replaced with the new `Button.<Variant>` API",
            explanation = """
                The style-based Spark button composables (`ButtonFilled`, `ButtonOutlined`, `ButtonTinted`, \
                `ButtonGhost`, `ButtonContrast`) and `TextLinkButton` are deprecated. Replace them with the \
                new `Button.<Variant>` API (for example `Button.Primary`, `Button.Secondary`, \
                `Button.Underlined`), which encodes the visual variant directly in the composable name \
                rather than via a `style` parameter.
            """.trimIndent(),
            category = CORRECTNESS,
            priority = 6,
            severity = WARNING,
            implementation = sourceImplementation<SparkButtonMigrationDetector>(),
        )
    }
}
