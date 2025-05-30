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

import com.adevinta.spark.lint.TagContentDescriptionDetector.Companion.ISSUE
import com.adevinta.spark.lint.stubs.Composable
import com.adevinta.spark.lint.stubs.SparkComponentsStub
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
public class TagIconOnlyRequireDecriptionDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = TagContentDescriptionDetector()
    override fun getIssues(): List<Issue> = listOf(ISSUE)

    @Test
    public fun `test tag without text and content description should report error`() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.components.tags.*
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    TagFilled()
                    TagOutlined()
                    TagTinted()
                }
                """,
            ),
            *SparkComponentsStub,
            *Composable,
        )
            .allowMissingSdk()
            .run()
            .expect(
                """
src/foo/test.kt:8: Error: Tag TagFilled is missing a contentDescription when no text label is provided for screen readers [MissingContentDescriptionForIconOnlyTag]
                    TagFilled()
                    ~~~~~~~~~
src/foo/test.kt:9: Error: Tag TagOutlined is missing a contentDescription when no text label is provided for screen readers [MissingContentDescriptionForIconOnlyTag]
                    TagOutlined()
                    ~~~~~~~~~~~
src/foo/test.kt:10: Error: Tag TagTinted is missing a contentDescription when no text label is provided for screen readers [MissingContentDescriptionForIconOnlyTag]
                    TagTinted()
                    ~~~~~~~~~
3 errors
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 8: Add contentDescription parameter:
                @@ -8 +8
                -                     TagFilled()
                +                     TagFilled(contentDescription = TODO("Add a description for screen readers"))
                Fix for src/foo/test.kt line 9: Add contentDescription parameter:
                @@ -9 +9
                -                     TagOutlined()
                +                     TagOutlined(contentDescription = TODO("Add a description for screen readers"))
                Fix for src/foo/test.kt line 10: Add contentDescription parameter:
                @@ -10 +10
                -                     TagTinted()
                +                     TagTinted(contentDescription = TODO("Add a description for screen readers"))
                """.trimIndent(),
            )
    }

    @Test
    public fun `test tag with text should not report error`() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.components.tags.*
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    TagFilled(text = "Hello")
                    TagOutlined(text = "World")
                    TagTinted(text = "!")
                }
                """,
            ),
            *SparkComponentsStub,
            *Composable,
        )
            .allowMissingSdk()
            .run()
            .expectClean()
    }

    @Test
    public fun `test tag with content description should not report error`() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.components.tags.*
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    TagFilled(contentDescription = "Hello")
                    TagOutlined(contentDescription = "World")
                    TagTinted(contentDescription = "!")
                }
                """,
            ),
            *SparkComponentsStub,
            *Composable,
        )
            .allowMissingSdk()
            .run()
            .expectClean()
    }

    @Test
    public fun `test tag with empty text and content description should not report error`() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.components.tags.*
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    TagFilled(text = "", contentDescription = "Hello")
                    TagOutlined(text = "   ", contentDescription = "World")
                    TagTinted(text = null, contentDescription = "!")
                }
                """,
            ),
            *SparkComponentsStub,
            *Composable,
        )
            .allowMissingSdk()
            .run()
            .expectClean()
    }

    @Test
    public fun `test tag with text composable should not report error`() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.components.tags.*
                import androidx.compose.runtime.*
                import com.adevinta.spark.components.text.Text

                @Composable
                fun Test() {
                    TagFilled {
                        Text("Hello")
                    }
                    TagOutlined {
                        Text("World")
                    }
                    TagTinted {
                        Text("!")
                    }
                }
                """,
            ),
            *SparkComponentsStub,
            *Composable,
        )
            .allowMissingSdk()
            .run()
            .expectClean()
    }
}
