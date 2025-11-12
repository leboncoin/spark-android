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

import com.adevinta.spark.lint.StringResourceAnnotationDetector.Companion.EMPTY_ANNOTATION_VARIABLE_ISSUE
import com.adevinta.spark.lint.StringResourceAnnotationDetector.Companion.UNKNOWN_ANNOTATION_ATTRIBUTE_NAME_ISSUE
import com.adevinta.spark.lint.StringResourceAnnotationDetector.Companion.UNSUPPORTED_ANNOTATION_ATTRIBUTE_VALUE_ISSUE
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode.Companion.SUPPRESSIBLE
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
public class StringResourceAnnotationDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = StringResourceAnnotationDetector()
    override fun getIssues(): List<Issue> = listOf(
        UNKNOWN_ANNOTATION_ATTRIBUTE_NAME_ISSUE,
        UNSUPPORTED_ANNOTATION_ATTRIBUTE_VALUE_ISSUE,
        EMPTY_ANNOTATION_VARIABLE_ISSUE,
    )

    @Test
    public fun `unknown attribute name`() = lint()
        .files(
            xml(
                "res/values/strings.xml",
                """
                    <resources>
                        <string name="example"><annotation unknown="neutral">Example</annotation></string>
                    </resources>
                 """,
            ),
        )
        .run()
        .expect(
            """
                res/values/strings.xml:3: Warning: Unknown annotation name [UnknownAnnotationAttributeNameDetector]
                                        <string name="example"><annotation unknown="neutral">Example</annotation></string>
                                                                           ~~~~~~~~~~~~~~~~~
                0 errors, 1 warnings
            """.trimIndent(),
        ).cleanup()

    @Test
    public fun `unknown attribute value`() = lint()
        .files(
            xml(
                "res/values/strings.xml",
                """
                    <resources>
                        <string name="example"><annotation color="unknown" typography="unknown">Example</annotation></string>
                    </resources>
                """,
            ),
        )
        .skipTestModes(SUPPRESSIBLE)
        .run()
        .expect(
            """
                res/values/strings.xml:3: Error: unknown is not a valid color value.
                Supported values are [main, support, success, alert, error, info, neutral, accent]. [UnsupportedAnnotationAttributeValueDetector]
                                        <string name="example"><annotation color="unknown" typography="unknown">Example</annotation></string>
                                                                           ~~~~~~~~~~~~~~~
                res/values/strings.xml:3: Error: unknown is not a valid typography value.
                Supported values are [display1, display2, display3, headline1, headline2, subhead, large, body1, body2, caption, small, callout]. [UnsupportedAnnotationAttributeValueDetector]
                                        <string name="example"><annotation color="unknown" typography="unknown">Example</annotation></string>
                                                                                           ~~~~~~~~~~~~~~~~~~~~
                2 errors, 0 warnings
            """.trimIndent(),
        ).cleanup()

    @Test
    public fun `known attributes`() = lint()
        .files(
            xml(
                "res/values/strings.xml",
                """
                    <resources>
                        <string name="example"><annotation color="neutral" typography="display1">Example</annotation></string>
                    </resources>
                """,
            ),
        )
        .run()
        .expectClean()
        .cleanup()

    @Test
    public fun `variable attribute name is allowed`() = lint()
        .files(
            xml(
                "res/values/strings.xml",
                """
                    <resources>
                        <string name="example"><annotation variable="foo" typography="display1">Example</annotation></string>
                    </resources>
                """,
            ),
        )
        .run()
        .expectClean()
        .cleanup()

    @Test
    public fun `annotation must not be empty when a variable is used`() = lint()
        .files(
            xml(
                "res/values/strings.xml",
                """
                    <resources>
                        <string name="valid_space"><annotation variable="foo"> </annotation></string>
                        <string name="valid_other"><annotation variable="foo">foo</annotation></string>
                        <string name="invalid"><annotation variable="foo"></annotation></string>
                        <string name="invalid_collapsed"><annotation variable="foo"/></string>
                    </resources>
                """,
            ),
        )
        .skipTestModes(SUPPRESSIBLE)
        .run()
        .expect(
            """
            res/values/strings.xml:5: Error: Empty annotation variable [EmptyAnnotationValueDetector]
                                    <string name="invalid"><annotation variable="foo"></annotation></string>
                                                           ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            res/values/strings.xml:6: Error: Empty annotation variable [EmptyAnnotationValueDetector]
                                    <string name="invalid_collapsed"><annotation variable="foo"/></string>
                                                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            2 errors, 0 warnings
            """.trimIndent(),
        )
        .cleanup()
}
