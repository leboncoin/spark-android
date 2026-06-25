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
package com.adevinta.spark.lint

import com.adevinta.spark.lint.SparkIconInstantiationDetector.Companion.ISSUE
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SparkIconInstantiationDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = SparkIconInstantiationDetector()
    override fun getIssues(): List<Issue> = listOf(ISSUE)

    private val sparkIconStub = kotlin(
        """
        package com.adevinta.spark.icons

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.graphics.painter.Painter
        import androidx.compose.ui.graphics.vector.ImageVector
        import org.jetbrains.compose.resources.DrawableResource

        sealed interface SparkIcon {
            data class DrawableRes(val drawableId: Int) : SparkIcon
            data class Resource(val res: DrawableResource) : SparkIcon
            data class Vector(val imageVector: ImageVector) : SparkIcon
            data class AnimatedDrawableRes(val drawableId: Int) : SparkIcon
            data class AnimatedPainter(val painterProvider: @Composable (atEnd: Boolean) -> Painter) : SparkIcon
        }
        """.trimIndent(),
    )

    private val leboncoinIconsStub = kotlin(
        """
        package com.adevinta.spark.icons

        import com.adevinta.spark.icons.SparkIcon.DrawableRes

        object LeboncoinIcons

        val LeboncoinIcons.Heart: DrawableRes get() = DrawableRes(0)
        """.trimIndent(),
    )

    private val composeStubs = arrayOf(
        kotlin(
            """
            package androidx.compose.runtime
            annotation class Composable
            """.trimIndent(),
        ),
        kotlin(
            """
            package androidx.compose.ui.graphics.painter
            abstract class Painter
            """.trimIndent(),
        ),
        kotlin(
            """
            package androidx.compose.ui.graphics.vector
            class ImageVector
            """.trimIndent(),
        ),
        kotlin(
            """
            package org.jetbrains.compose.resources
            class DrawableResource
            """.trimIndent(),
        ),
    )

    @Test
    fun drawableResInstantiation_flagged() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.SparkIcon

                fun test() {
                    val icon = SparkIcon.DrawableRes(123)
                }
                """.trimIndent(),
            ),
            sparkIconStub,
            *composeStubs,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:5: Error: Do not instantiate SparkIcon directly. Use icons from `LeboncoinIcons` provided by the icon update process. [SparkIconInstantiation]
                    val icon = SparkIcon.DrawableRes(123)
                               ~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }

    @Test
    fun resourceInstantiation_flagged() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.SparkIcon
                import org.jetbrains.compose.resources.DrawableResource

                fun test(res: DrawableResource) {
                    val icon = SparkIcon.Resource(res)
                }
                """.trimIndent(),
            ),
            sparkIconStub,
            *composeStubs,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:6: Error: Do not instantiate SparkIcon directly. Use icons from `LeboncoinIcons` provided by the icon update process. [SparkIconInstantiation]
                    val icon = SparkIcon.Resource(res)
                               ~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }

    @Test
    fun vectorInstantiation_flagged() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.SparkIcon
                import androidx.compose.ui.graphics.vector.ImageVector

                fun test(iv: ImageVector) {
                    val icon = SparkIcon.Vector(iv)
                }
                """.trimIndent(),
            ),
            sparkIconStub,
            *composeStubs,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:6: Error: Do not instantiate SparkIcon directly. Use icons from `LeboncoinIcons` provided by the icon update process. [SparkIconInstantiation]
                    val icon = SparkIcon.Vector(iv)
                               ~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }

    @Test
    fun animatedDrawableResInstantiation_flagged() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.SparkIcon

                fun test() {
                    val icon = SparkIcon.AnimatedDrawableRes(456)
                }
                """.trimIndent(),
            ),
            sparkIconStub,
            *composeStubs,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:5: Error: Do not instantiate SparkIcon directly. Use icons from `LeboncoinIcons` provided by the icon update process. [SparkIconInstantiation]
                    val icon = SparkIcon.AnimatedDrawableRes(456)
                               ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }

    @Test
    fun animatedPainterInstantiation_flagged() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.SparkIcon
                import androidx.compose.runtime.Composable
                import androidx.compose.ui.graphics.painter.Painter

                fun test() {
                    val icon = SparkIcon.AnimatedPainter { TODO() }
                }
                """.trimIndent(),
            ),
            sparkIconStub,
            *composeStubs,
        )
            .run()
            .expect(
                """
                src/foo/test.kt:7: Error: Do not instantiate SparkIcon directly. Use icons from `LeboncoinIcons` provided by the icon update process. [SparkIconInstantiation]
                    val icon = SparkIcon.AnimatedPainter { TODO() }
                               ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }

    @Test
    fun usingLeboncoinIcons_clean() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.LeboncoinIcons
                import com.adevinta.spark.icons.Heart
                import com.adevinta.spark.icons.SparkIcon

                fun test() {
                    val icon: SparkIcon = LeboncoinIcons.Heart
                }
                """.trimIndent(),
            ),
            sparkIconStub,
            leboncoinIconsStub,
            *composeStubs,
        )
            .run()
            .expectClean()
    }

    @Test
    fun passingSparkIconAsParameter_clean() {
        lint().files(
            kotlin(
                """
                package foo
                import com.adevinta.spark.icons.SparkIcon

                fun display(icon: SparkIcon) {}
                """.trimIndent(),
            ),
            sparkIconStub,
            *composeStubs,
        )
            .run()
            .expectClean()
    }
}
