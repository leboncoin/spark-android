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
package com.adevinta.spark.lint.utils

import com.adevinta.spark.lint.extensions.hasImplicitItParameter
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtSimpleNameExpression
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.psi.psiUtil.isAncestor


internal class UnreferencedParameter(val name: String, public val parameter: KtParameter?)
private const val LAMBDA_IMPLICIT_PARAMETER_NAME = "it"

internal class LambdaParameterVisitor(private val lambda: KtLambdaExpression) {

    /**
     * Returns a list of [UnreferencedParameter]s inside [lambda]. Inner lambdas are checked to
     * ensure that they are not shadowing a parameter name, as a reference inside a shadowed lambda
     * will refer to that lambda's parameter, and not the outer parameter.
     *
     * If no parameters have been specified, but there is an implicit `it` parameter, this will
     * return a list containing an [UnreferencedParameter] with `it` as the name.
     */
    fun findUnreferencedParameters(): List<UnreferencedParameter> {
        // If there is an implicit `it` parameter, we only want to look for "it". There is no value
        // for parameter, since there is no corresponding declaration in the function literal.
        return if (lambda.hasImplicitItParameter) {
            if (isParameterReferenced(LAMBDA_IMPLICIT_PARAMETER_NAME)) {
                emptyList()
            } else {
                listOf(UnreferencedParameter(LAMBDA_IMPLICIT_PARAMETER_NAME, null))
            }
        } else {
            // Otherwise, look for all named, non-destructured parameters
            lambda.valueParameters
                // Ignore parameters with a destructuring declaration instead of a named parameter
                .filter { it.destructuringDeclaration == null }
                // Ignore referenced parameters
                .filterNot { isParameterReferenced(it.name!!) }
                // Return an UnreferencedParameters for each un-referenced parameter
                .map { UnreferencedParameter(it.name!!, it) }
        }
    }

    private fun isParameterReferenced(name: String): Boolean {
        val matchingReferences = references.filter { it.getReferencedName() == name }

        // Fast return if there is no reference
        if (matchingReferences.isEmpty()) return false

        // Find lambdas that shadow this parameter name, to make sure that they aren't shadowing
        // the references we are looking through
        val lambdasWithMatchingParameterName =
            innerLambdas.filter { innerLambda ->
                // If the lambda has an implicit it parameter, it will shadow the outer parameter if
                // the outer parameter also has an implicit it parameter (its name is "it").
                if (innerLambda.hasImplicitItParameter) {
                    name == LAMBDA_IMPLICIT_PARAMETER_NAME
                } else {
                    // Otherwise look to see if any of the parameters on the inner lambda have the
                    // same name
                    innerLambda.valueParameters
                        // Ignore parameters with a destructuring declaration instead of a named
                        // parameter
                        .filter { it.destructuringDeclaration == null }
                        .any { it.name == name }
                }
            }

        // The parameter is referenced if there is at least one reference that isn't shadowed by an
        // inner lambda
        return matchingReferences.any { reference ->
            lambdasWithMatchingParameterName.none { it.isAncestor(reference) }
        }
    }

    private val references by lazy {
        lambda.functionLiteral.collectDescendantsOfType<KtSimpleNameExpression>()
    }
    private val innerLambdas by lazy {
        lambda.functionLiteral.collectDescendantsOfType<KtLambdaExpression>()
    }
}
