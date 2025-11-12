/*
 * Copyright (c) 2024 Adevinta
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

import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.VALUES
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.TextFormat.RAW
import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.forEach
import org.w3c.dom.Element
import org.w3c.dom.Node

public class StringResourceAnnotationDetector : ResourceXmlDetector() {

    override fun appliesTo(folderType: ResourceFolderType): Boolean = folderType == VALUES
    override fun getApplicableElements(): Collection<String> = listOf(TAG_ANNOTATION)

    private class Entry(val context: XmlContext, val element: Element, val node: Node)

    override fun visitElement(context: XmlContext, element: Element) {
        element.attributes.forEach { attribute ->
            ATTRIBUTE_HANDLERS.getValue(attribute.nodeName).invoke(Entry(context, element, attribute))
        }
    }

    internal companion object {
        private const val TAG_ANNOTATION = "annotation"

        private fun checkColor(it: Entry) {
            if (it.node.nodeValue in SUPPORTED_COLOR_VALUES) return
            Incident(it.context)
                .issue(UNSUPPORTED_ANNOTATION_ATTRIBUTE_VALUE_ISSUE)
                .at(it.node)
                .message(
                    """
                    ${it.node.nodeValue} is not a valid `color` value.
                    Supported values are $SUPPORTED_COLOR_VALUES.
                    """.trimIndent(),
                )
                .report()
        }

        private fun checkTypography(it: Entry) {
            if (it.node.nodeValue in SUPPORTED_TYPOGRAPHY_VALUES) return
            Incident(it.context)
                .issue(UNSUPPORTED_ANNOTATION_ATTRIBUTE_VALUE_ISSUE)
                .at(it.node)
                .message(
                    """
                    ${it.node.nodeValue} is not a valid `typography` value.
                    Supported values are $SUPPORTED_TYPOGRAPHY_VALUES.
                """,
                )
                .report()
        }

        private fun checkVariable(it: Entry) {
            if (it.element.textContent.isNotEmpty()) return
            Incident(it.context)
                .issue(EMPTY_ANNOTATION_VARIABLE_ISSUE)
                .at(it.element)
                .message(EMPTY_ANNOTATION_VARIABLE_ISSUE.getBriefDescription(RAW))
                .report()
        }

        private fun reportUnknown(it: Entry) = Incident(it.context)
            .issue(UNKNOWN_ANNOTATION_ATTRIBUTE_NAME_ISSUE)
            .at(it.node)
            .message(UNKNOWN_ANNOTATION_ATTRIBUTE_NAME_ISSUE.getBriefDescription(RAW))
            .report()

        private val ATTRIBUTE_HANDLERS: Map<String, Entry.() -> Unit> =
            mapOf(
                "color" to ::checkColor,
                "typography" to ::checkTypography,
                "variable" to ::checkVariable,
            ).withDefault { ::reportUnknown }

        // NOTE: It would be great to have these values come from `:spark` directly (see SparkStringAnnotations), but we would need to add extra dependencies.
        private val SUPPORTED_COLOR_VALUES = listOf(
            "main",
            "support",
            "success",
            "alert",
            "error",
            "info",
            "neutral",
            "accent",
        )

        private val SUPPORTED_TYPOGRAPHY_VALUES = listOf(
            "display1",
            "display2",
            "display3",
            "headline1",
            "headline2",
            "subhead",
            "large",
            "body1",
            "body2",
            "caption",
            "small",
            "callout",
        )

        val UNKNOWN_ANNOTATION_ATTRIBUTE_NAME_ISSUE = Issue.create(
            id = "UnknownAnnotationAttributeNameDetector",
            briefDescription = "Unknown annotation name",
            explanation = """
                This annotation attribute name is not supported and won't be parsed.
                Supported names are ${ATTRIBUTE_HANDLERS.keys}.
            """.trimIndent(),
            category = CORRECTNESS,
            priority = 5,
            severity = WARNING,
            implementation = Implementation(
                StringResourceAnnotationDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE,
            ),
        )

        val UNSUPPORTED_ANNOTATION_ATTRIBUTE_VALUE_ISSUE = Issue.create(
            id = "UnsupportedAnnotationAttributeValueDetector",
            briefDescription = "Unsupported annotation value",
            explanation = "This annotation attribute value is not supported and won't be parsed",
            category = CORRECTNESS,
            priority = 8,
            severity = ERROR,
            implementation = Implementation(
                StringResourceAnnotationDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE,
            ),
        )

        val EMPTY_ANNOTATION_VARIABLE_ISSUE = Issue.create(
            id = "EmptyAnnotationValueDetector",
            briefDescription = "Empty annotation variable",
            explanation =
            """
                Empty annotation variable will prevent the variable from being injected.
                Some content (like a space character) needs to be added.
            """.trimIndent(),
            category = CORRECTNESS,
            priority = 8,
            severity = ERROR,
            implementation = Implementation(
                StringResourceAnnotationDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE,
            ),
        )
    }
}
