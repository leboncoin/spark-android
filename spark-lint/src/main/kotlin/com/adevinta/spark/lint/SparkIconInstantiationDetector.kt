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
@file:Suppress("UnstableApiUsage")

package com.adevinta.spark.lint

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
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
import java.util.EnumSet

public class SparkIconInstantiationDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableConstructorTypes(): List<String> = SPARK_ICON_SUBTYPES

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        Incident(context)
            .issue(ISSUE)
            .location(context.getLocation(node))
            .message(MESSAGE)
            .report()
    }

    internal companion object {
        private const val MESSAGE =
            "Do not instantiate SparkIcon directly. Use icons from `LeboncoinIcons` provided by the icon update process."

        private val SPARK_ICON_SUBTYPES = listOf(
            "com.adevinta.spark.icons.SparkIcon.DrawableRes",
            "com.adevinta.spark.icons.SparkIcon.Resource",
            "com.adevinta.spark.icons.SparkIcon.Vector",
            "com.adevinta.spark.icons.SparkIcon.AnimatedDrawableRes",
            "com.adevinta.spark.icons.SparkIcon.AnimatedPainter",
        )

        val ISSUE: Issue = Issue.create(
            id = "SparkIconInstantiation",
            briefDescription = "Direct SparkIcon instantiation is not allowed",
            explanation = "SparkIcon subtypes must not be constructed manually. " +
                "Use icons from `LeboncoinIcons` provided by the icon update process (Figma export -> spark-tokens -> automated PR).",
            category = CORRECTNESS,
            priority = 8,
            severity = ERROR,
            implementation = Implementation(
                SparkIconInstantiationDetector::class.java,
                EnumSet.of(JAVA_FILE, TEST_SOURCES),
            ),
        )
    }
}