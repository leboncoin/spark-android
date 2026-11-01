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
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.UReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression

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
        val params = method.parameterList.parameters
        val args = argumentsByParameterName(context, node, method)

        val directVariant = directVariantForOldComposable[calleeName]
        val message: String
        val fix: LintFix?
        if (directVariant != null) {
            // TextLinkButton and similar always map to one callee, whatever the intent.
            val targetCallee = variantToCallee[directVariant] ?: return
            message = "`$calleeName` is an old Spark button. Replace it with `$targetCallee`."
            fix = args?.let { buildFix(context, node, calleeName, targetCallee, params, it) }
        } else {
            val style = oldComposableToStyle[calleeName] ?: return
            val intent = resolveIntent(calleeName, args)
            if (intent != null) {
                val variant = mapToVariant(intent, style)
                val targetCallee = variantToCallee[variant] ?: return
                message = "`$calleeName` is an old style-based Spark button. Replace it with `$targetCallee`."
                fix = args?.let { buildFix(context, node, calleeName, targetCallee, params, it) }
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
     * (for example, a variable, a function call, or a `when` expression), or when [args] is null
     * because the argument binding could not be trusted.
     */
    private fun resolveIntent(calleeName: String, args: Map<String, UExpression>?): ButtonIntent? {
        if (args == null) return null
        val arg = args["intent"]
            ?: return defaultIntentForOldComposable[calleeName] // absent → use per-button default
        return resolveIntentFromExpression(arg) // present → resolve or return null (unresolved)
    }

    /**
     * Resolves a [UExpression] to a [ButtonIntent] enum constant.
     *
     * Returns null when the expression is anything other than a direct reference to a
     * [ButtonIntent] enum entry (for example, a variable, a call, or a conditional expression).
     */
    private fun resolveIntentFromExpression(expr: UExpression): ButtonIntent? {
        val ref = expr as? UReferenceExpression ?: return null
        // The CLI resolves to a light PsiEnumConstant, the IDE can resolve to the Kotlin KtEnumEntry.
        val entryName = when (val resolved = ref.resolve()) {
            is PsiEnumConstant -> resolved.takeIf { it.containingClass?.name == INTENT_CLASS_NAME }?.name
            is KtEnumEntry -> resolved.takeIf { it.enumClassName() == INTENT_CLASS_NAME }?.name
            else -> unresolvedIntentEntryName(ref)
        } ?: return null
        return ButtonIntent.entries.firstOrNull { it.name == entryName }
    }

    private fun KtEnumEntry.enumClassName(): String? =
        PsiTreeUtil.getParentOfType(this, KtClassOrObject::class.java)?.name

    /** Reads `ButtonIntent.<Entry>` from source when the reference does not resolve to a known enum form. */
    private fun unresolvedIntentEntryName(ref: UReferenceExpression): String? {
        val qualified = ref as? UQualifiedReferenceExpression ?: return null
        val receiverText = qualified.receiver.sourcePsi?.text ?: return null
        if (receiverText != INTENT_CLASS_NAME && !receiverText.endsWith(".$INTENT_CLASS_NAME")) return null
        return (qualified.selector as? USimpleNameReferenceExpression)?.identifier
    }

    /**
     * Binds each argument of [node] to the name of the [method] parameter it fills.
     *
     * Returns null when the binding is not trustworthy: an argument maps to a parameter that [method]
     * does not declare, or a named argument maps to a parameter with a different name. This happens
     * when the IDE resolves overloads inconsistently, and rewriting with that binding would move
     * values into the wrong parameters.
     */
    private fun argumentsByParameterName(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod,
    ): Map<String, UExpression>? {
        val paramNames = method.parameterList.parameters.map { it.name }.toSet()
        val mapping = context.evaluator.computeArgumentMapping(node, method)
        if (mapping.size != node.valueArgumentCount) return null
        val result = mutableMapOf<String, UExpression>()
        for ((arg, param) in mapping) {
            if (param.name !in paramNames) return null
            val sourceName = (arg.sourcePsi?.parent as? KtValueArgument)?.getArgumentName()?.asName?.asString()
            if (sourceName != null && sourceName != param.name) return null
            result[param.name] = arg
        }
        return result
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
        params: Array<PsiParameter>,
        args: Map<String, UExpression>,
    ): LintFix? {
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

        // Emit arguments in parameter order, looked up by parameter name. Parameters left at their
        // default value have no argument and are omitted.
        for (param in params) {
            val paramName = param.name
            val arg = args[paramName] ?: continue

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
            // Variants are extension functions on `Button`, so each one needs its own import.
            add("com.adevinta.spark.components.buttons.${targetCallee.removePrefix("Button.")}")
            if (isAnnotatedStringOverload) add("com.adevinta.spark.components.text.Text")
        }

        return LintFix.create()
            .name("Replace $calleeName with $targetCallee")
            .sharedName("Replace old Spark buttons with Button.<Variant>")
            .replace()
            .range(context.getLocation(node))
            .with(newText)
            .imports(*imports.toTypedArray())
            .reformat(true)
            .autoFix()
            .build()
    }

    internal companion object {
        /** New callees that have no content-slot overload, so a content-slot call cannot autofix. */
        private val CALLEES_WITHOUT_CONTENT_OVERLOAD = setOf("Button.Underlined")

        private const val INTENT_CLASS_NAME = "ButtonIntent"

        val ISSUE: Issue = Issue.create(
            id = "SparkButtonMigration",
            briefDescription = "Old Spark button usage",
            explanation = """
                The style-based Spark button composables (`ButtonFilled`, `ButtonOutlined`, `ButtonTinted`, \
                `ButtonGhost`, `ButtonContrast`) and `TextLinkButton` are deprecated. Replace them with the \
                new `Button.<Variant>` API (for example `Button.Primary`, `Button.Secondary`, \
                `Button.Underlined`), which encodes the visual variant directly in the composable name \
                rather than via a `style` parameter.
            """,
            category = CORRECTNESS,
            priority = 6,
            severity = WARNING,
            implementation = sourceImplementation<SparkButtonMigrationDetector>(),
        )
    }
}
