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
class SparkScaffoldDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = SparkScaffoldDetector()
    override fun getIssues(): List<Issue> = listOf(
        SparkScaffoldDetector.UNUSED_PADDING_ISSUE,
        SparkScaffoldDetector.BAR_WITHOUT_INSETS_ISSUE,
    )

    private val sparkScaffoldStub = kotlin(
        """
            package com.adevinta.spark.components.scaffold

            import androidx.compose.foundation.layout.PaddingValues
            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Modifier

            fun Scaffold(
                modifier: Modifier = Modifier,
                topBar: @Composable () -> Unit = {},
                bottomBar: @Composable () -> Unit = {},
                content: @Composable (PaddingValues) -> Unit
            ) {}
            """,
    ).indented()

    private val paddingValuesStub = kotlin(
        """
            package androidx.compose.foundation.layout

            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Modifier

            class PaddingValues

            class WindowInsets {
                companion object
            }

            val WindowInsets.Companion.navigationBars: WindowInsets get() = WindowInsets()

            fun Modifier.padding(paddingValues: PaddingValues): Modifier = this
            fun Modifier.statusBarsPadding(): Modifier = this
            fun Modifier.windowInsetsPadding(insets: WindowInsets): Modifier = this

            @Composable
            fun Box(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {}
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

            interface Modifier {
                companion object : Modifier
            }
            """,
    ).indented()

    private val sparkComponentsStub = kotlin(
        """
            package com.adevinta.spark.components.appbar

            import androidx.compose.foundation.layout.WindowInsets
            import androidx.compose.runtime.Composable

            @Composable
            fun TopAppBar(title: @Composable () -> Unit, windowInsets: WindowInsets = WindowInsets()) {}
            """,
    ).indented()

    private val textStub = kotlin(
        """
            package com.adevinta.spark.components.text

            import androidx.compose.runtime.Composable

            @Composable
            fun Text(text: String) {}
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

    @Test
    fun barsWithoutInsets() {
        lint()
            .files(
                kotlin(
                    """
                package test.foo

                import com.adevinta.spark.components.scaffold.*
                import com.adevinta.spark.components.text.Text
                import androidx.compose.foundation.layout.Box
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                @Composable
                fun Recursive(): Unit = Recursive()

                @Composable
                fun Test() {
                    Scaffold(topBar = { Text("Title") }) { it }
                    Scaffold(bottomBar = { Box { Text("Actions") } }) { it }
                    Scaffold(topBar = { Recursive() }) { it }
                }
            """,
                ),
                sparkScaffoldStub,
                sparkComponentsStub,
                textStub,
                modifierStub,
                paddingValuesStub,
                composableStub,
            )
            .run()
            .expect(
                """
src/test/foo/test.kt:15: Warning: topBar content does not handle window insets: use a Spark app bar or apply a window insets padding modifier [SparkScaffoldBarWithoutInsets]
                    Scaffold(topBar = { Text("Title") }) { it }
                                      ~~~~~~~~~~~~~~~~~
src/test/foo/test.kt:16: Warning: bottomBar content does not handle window insets: use a Spark app bar or apply a window insets padding modifier [SparkScaffoldBarWithoutInsets]
                    Scaffold(bottomBar = { Box { Text("Actions") } }) { it }
                                         ~~~~~~~~~~~~~~~~~~~~~~~~~~~
src/test/foo/test.kt:17: Warning: topBar content does not handle window insets: use a Spark app bar or apply a window insets padding modifier [SparkScaffoldBarWithoutInsets]
                    Scaffold(topBar = { Recursive() }) { it }
                                      ~~~~~~~~~~~~~~~
0 errors, 3 warnings
            """,
            )
    }

    @Test
    fun barsHandlingInsets() {
        lint()
            .files(
                kotlin(
                    """
                package test.foo

                import com.adevinta.spark.components.appbar.TopAppBar
                import com.adevinta.spark.components.scaffold.*
                import com.adevinta.spark.components.text.Text
                import androidx.compose.foundation.layout.*
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                @Composable
                fun CustomTopBar() {
                    TopAppBar(title = { Text("Title") })
                }

                @Composable
                fun Test() {
                    Scaffold(topBar = { TopAppBar(title = { Text("Title") }) }) { it }
                    Scaffold(topBar = { CustomTopBar() }) { it }
                    Scaffold(topBar = { Box(Modifier.statusBarsPadding()) { Text("Title") } }) { it }
                    Scaffold(
                        bottomBar = { Box(Modifier.windowInsetsPadding(WindowInsets.navigationBars)) { Text("Actions") } },
                    ) { it }
                }
            """,
                ),
                sparkScaffoldStub,
                sparkComponentsStub,
                textStub,
                modifierStub,
                paddingValuesStub,
                composableStub,
            )
            .run()
            .expectClean()
    }

    @Test
    fun barsOnNonSparkScaffold() {
        lint()
            .files(
                kotlin(
                    """
                package androidx.compose.material3

                import androidx.compose.foundation.layout.PaddingValues
                import androidx.compose.runtime.Composable

                fun Scaffold(
                    topBar: @Composable () -> Unit = {},
                    content: @Composable (PaddingValues) -> Unit,
                ) {}
            """,
                ).indented(),
                kotlin(
                    """
                package test.foo

                import androidx.compose.material3.Scaffold
                import com.adevinta.spark.components.text.Text
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    Scaffold(topBar = { Text("Title") }) { }
                }
            """,
                ),
                textStub,
                modifierStub,
                paddingValuesStub,
                composableStub,
            )
            .run()
            .expectClean()
    }
}
