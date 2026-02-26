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
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ScaffoldPaddingDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ScaffoldPaddingDetector()
    override fun getIssues(): List<Issue> = listOf(ScaffoldPaddingDetector.ISSUE)

    private val sparkScaffoldStub = kotlin(
        """
            package com.adevinta.spark.components.scaffold

            import androidx.compose.foundation.layout.PaddingValues

            fun Scaffold(
                topBar: @Composable () -> Unit = {},
                bottomBar: @Composable () -> Unit = {},
                content: @Composable (PaddingValues) -> Unit
            ) {}
            """,
    ).indented()

    private val paddingValuesStub = kotlin(
        """
            package androidx.compose.foundation.layout

            class PaddingValues

            fun Modifier.padding(paddingValues: PaddingValues): Modifier = this
            """,
    ).indented()

    private val composableStub = kotlin(
        """
            package androidx.compose.runtime

            annotation class Composable
            """,
    ).indented()

    private val modifierStub = kotlin(
        """
            package androidx.compose.ui

            object Modifier {
                fun fillMaxSize(): Modifier = this
            }

            interface Modifier {
                companion object : Modifier
            }
            """,
    ).indented()

    @Test
    fun unreferencedParameters() {
        lint()
            .files(
                kotlin(
                    """
                package test.foo

                import com.adevinta.spark.components.scaffold.*
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                @Composable
                fun Test() {
                    Scaffold { /**/ }
                    Scaffold(Modifier) { /**/ }
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { /**/ }
                    Scaffold(Modifier, topBar = {}, bottomBar = {}, content = { /**/ })
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { _ -> /**/ }
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { innerPadding -> /**/ }
                }
            """,
                ),
                sparkScaffoldStub,
                modifierStub,
                paddingValuesStub,
                composableStub,
            )
            .run()
            .expect(
                """
src/test/foo/test.kt:10: Error: Content padding parameter it is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold { /**/ }
                             ~~~~~~~~
src/test/foo/test.kt:11: Error: Content padding parameter it is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold(Modifier) { /**/ }
                                       ~~~~~~~~
src/test/foo/test.kt:12: Error: Content padding parameter it is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { /**/ }
                                                                    ~~~~~~~~
src/test/foo/test.kt:13: Error: Content padding parameter it is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold(Modifier, topBar = {}, bottomBar = {}, content = { /**/ })
                                                                              ~~~~~~~~
src/test/foo/test.kt:14: Error: Content padding parameter _ is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { _ -> /**/ }
                                                                      ~
src/test/foo/test.kt:15: Error: Content padding parameter innerPadding is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { innerPadding -> /**/ }
                                                                      ~~~~~~~~~~~~
6 errors
            """,
            )
    }

    @Test
    fun unreferencedParameter_shadowedNames() {
        lint()
            .files(
                kotlin(
                    """
                package test.foo

                import com.adevinta.spark.components.scaffold.*
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                val foo = false

                @Composable
                fun Test() {
                    Scaffold {
                        foo.let {
                            // These `it`s refer to the `let`, not the `Scaffold`, so we
                            // should still report an error
                            it.let {
                                if (it) { /**/ } else { /**/ }
                            }
                        }
                    }
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { innerPadding ->
                        foo.let { innerPadding ->
                            // These `innerPadding`s refer to the `let`, not the `Scaffold`, so we
                            // should still report an error
                            innerPadding.let {
                                if (innerPadding) { /**/ } else { /**/ }
                            }
                        }
                    }
                }
            """,
                ),
                sparkScaffoldStub,
                modifierStub,
                paddingValuesStub,
                composableStub,
            )
            .run()
            .expect(
                """
src/test/foo/test.kt:12: Error: Content padding parameter it is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold {
                             ^
src/test/foo/test.kt:21: Error: Content padding parameter innerPadding is not used [UnusedSparkScaffoldPaddingParameter]
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { innerPadding ->
                                                                      ~~~~~~~~~~~~
2 errors
            """,
            )
    }

    @Test
    fun noErrors() {
        lint()
            .files(
                kotlin(
                    """
                package test.foo

                import com.adevinta.spark.components.scaffold.*
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                @Composable
                fun Test() {
                    Scaffold {
                        it
                    }
                    Scaffold(Modifier, topBar = {}, bottomBar = {}) { innerPadding ->
                        innerPadding
                    }
                }
        """,
                ),
                sparkScaffoldStub,
                modifierStub,
                paddingValuesStub,
                composableStub,
            )
            .run()
            .expectClean()
    }
}
