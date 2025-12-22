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

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode.Companion.FULLY_QUALIFIED
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.text.trimIndent

@RunWith(JUnit4::class)
class WrongConditionalModifierUsageDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = WrongConditionalModifierUsageDetector()
    override fun getIssues(): List<Issue> = listOf(WrongConditionalModifierUsageDetector.ISSUE)

    private val kotlinModifierStub = kotlin(
        """
            package androidx.compose.ui

            object Modifier {
                fun background(color: Any): Modifier = this
                fun padding(dp: Int): Modifier = this
                fun fillMaxWidth(): Modifier = this
                fun clickable(onClick: () -> Unit): Modifier = this
            }

            interface Modifier {
                companion object : Modifier
            }
            """,
    ).indented()

    private val modifierExtensionsStub = kotlin(
        """
            package com.adevinta.spark.tools.modifiers

            import androidx.compose.ui.Modifier

            inline fun Modifier.ifTrue(value: Boolean, chain: Modifier.() -> Modifier): Modifier {}
            inline fun Modifier.ifFalse(value: Boolean, chain: Modifier.() -> Modifier): Modifier {}
            inline fun <T : Any> Modifier.ifNotNull(value: T?, chain: Modifier.(T) -> Modifier): Modifier {}
            inline fun <T : Any> Modifier.ifNull(value: T?, chain: Modifier.() -> Modifier): Modifier {}
        """,
    ).indented()

    @Test
    fun testNoErrorWhenUsingReceiverCorrectly() {
        lint()
            .files(
                kotlinModifierStub,
                modifierExtensionsStub,
                kotlin(
                    """
                    package fr.leboncoin

                    import androidx.compose.ui.Modifier
                    import com.adevinta.spark.tools.modifiers.ifTrue
                    import com.adevinta.spark.tools.modifiers.ifFalse
                    import com.adevinta.spark.tools.modifiers.ifNotNull
                    import com.adevinta.spark.tools.modifiers.ifNull

                    fun correctUsage() {

                        Modifier
                            .ifTrue { background(42) }
                            .ifTrue { this.background(42) }
                            .ifFalse { background(42).padding(10) }
                            .ifNotNull { padding(5) }
                            .ifNull { padding(5) }
                    }
                    """.trimIndent(),
                ),
            )
            .run()
            .expectClean()
    }

    @Test
    fun testErrorWhenUsingStaticModifierDirectly() {
        lint().files(
            kotlinModifierStub,
            modifierExtensionsStub,
            kotlin(
                """
                package fr.leboncoin

                import androidx.compose.ui.Modifier
                import com.adevinta.spark.tools.modifiers.ifFalse
                import com.adevinta.spark.tools.modifiers.ifNotNull
                import com.adevinta.spark.tools.modifiers.ifNull
                import com.adevinta.spark.tools.modifiers.ifTrue

                fun incorrectUsage() {
                    Modifier
                        .ifFalse { Modifier.background(42) }
                        .ifNotNull { Modifier.padding(10)}
                        .ifNull { Modifier.padding(5) }
                        .ifTrue { Modifier.background(42) }
                }
                """.trimIndent(),
            ),
        )
            .testModes(FULLY_QUALIFIED)
            .run()
            .expect(
                """
                src/fr/leboncoin/test.kt:11: Error: Do not use the static Modifier reference in the lambda of ifFalse. Use the implicit 'this' receiver instead [WrongConditionalModifierUsageDetector]
                        .ifFalse { androidx.compose.ui.Modifier.background(42) }
                                   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                src/fr/leboncoin/test.kt:12: Error: Do not use the static Modifier reference in the lambda of ifNotNull. Use the implicit 'this' receiver instead [WrongConditionalModifierUsageDetector]
                        .ifNotNull { androidx.compose.ui.Modifier.padding(10)}
                                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                src/fr/leboncoin/test.kt:13: Error: Do not use the static Modifier reference in the lambda of ifNull. Use the implicit 'this' receiver instead [WrongConditionalModifierUsageDetector]
                        .ifNull { androidx.compose.ui.Modifier.padding(5) }
                                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                src/fr/leboncoin/test.kt:14: Error: Do not use the static Modifier reference in the lambda of ifTrue. Use the implicit 'this' receiver instead [WrongConditionalModifierUsageDetector]
                        .ifTrue { androidx.compose.ui.Modifier.background(42) }
                                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                4 errors, 0 warnings
                """.trimIndent(),
            )
    }

    @Test
    fun testErrorWhenUsingModifierDirectlyWithChaining() {
        lint().files(
            kotlinModifierStub,
            modifierExtensionsStub,
            kotlin(
                """
                package fr.leboncoin

                import androidx.compose.ui.Modifier
                import com.adevinta.spark.tools.modifiers.ifTrue

                fun incorrectUsage() {
                    Modifier.ifTrue {
                        Modifier.background(42).padding(10)
                    }
                }
                """.trimIndent(),
            ),
        )
            .testModes(FULLY_QUALIFIED)
            .run()
            .expect(
                """
                src/fr/leboncoin/test.kt:8: Error: Do not use the static Modifier reference in the lambda of ifTrue. Use the implicit 'this' receiver instead [WrongConditionalModifierUsageDetector]
                        androidx.compose.ui.Modifier.background(42).padding(10)
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }
}
