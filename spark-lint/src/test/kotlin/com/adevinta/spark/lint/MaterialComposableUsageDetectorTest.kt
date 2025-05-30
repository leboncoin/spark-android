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

import com.adevinta.spark.lint.MaterialComposableUsageDetector.Companion.ISSUE
import com.adevinta.spark.lint.stubs.CoilComponentsStub
import com.adevinta.spark.lint.stubs.Composable
import com.adevinta.spark.lint.stubs.FoundationStub
import com.adevinta.spark.lint.stubs.MaterialComponentsStub
import com.adevinta.spark.lint.stubs.SparkComponentsStub
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
public class MaterialComposableUsageDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = MaterialComposableUsageDetector()
    override fun getIssues(): List<Issue> = listOf(ISSUE)

    private fun Issue.explanation(replacement: Pair<String, String>) =
        "${defaultSeverity.description}: Composable ${replacement.first} has a Spark replacement " +
            "that should be used instead: ${replacement.second} [$id]"

    @Test
    public fun materialUsages(): Unit = with(ISSUE) {
        lint().files(
            kotlin(
                """
                package foo
                import androidx.compose.material3.*
                import androidx.compose.runtime.*
                import androidx.compose.ui.*

                @Composable
                fun Test() {
                    Button()
                    ElevatedButton()
                    FilledTonalButton()
                    OutlinedButton()
                    TextButton()
                    OutlinedTextField()
                    FilledTextField()
                    CheckBox()
                    TriStateCheckbox()
                    RadioButton()
                    Switch()
                    Snackbar()
                    LinearProgressIndicator()
                    CircularProgressIndicator()
                    MaterialTheme()
                }
            """,
            ),
            MaterialComponentsStub,
            *Composable,
        )
            .allowMissingSdk()
            .run()
            .expect(
                """
                src/foo/test.kt:9: ${explanation("Button" to "ButtonFilled")}
                                    Button()
                                    ~~~~~~
                src/foo/test.kt:10: ${explanation("ElevatedButton" to "ButtonFilled")}
                                    ElevatedButton()
                                    ~~~~~~~~~~~~~~
                src/foo/test.kt:11: ${explanation("FilledTonalButton" to "ButtonTinted")}
                                    FilledTonalButton()
                                    ~~~~~~~~~~~~~~~~~
                src/foo/test.kt:12: ${explanation("OutlinedButton" to "ButtonOutlined")}
                                    OutlinedButton()
                                    ~~~~~~~~~~~~~~
                src/foo/test.kt:13: ${explanation("TextButton" to "ButtonGhost")}
                                    TextButton()
                                    ~~~~~~~~~~
                src/foo/test.kt:14: ${explanation("OutlinedTextField" to "TextField")}
                                    OutlinedTextField()
                                    ~~~~~~~~~~~~~~~~~
                src/foo/test.kt:15: ${explanation("FilledTextField" to "TextField")}
                                    FilledTextField()
                                    ~~~~~~~~~~~~~~~
                src/foo/test.kt:16: ${explanation("CheckBox" to "CheckBox")}
                                    CheckBox()
                                    ~~~~~~~~
                src/foo/test.kt:17: ${explanation("TriStateCheckbox" to "CheckBox")}
                                    TriStateCheckbox()
                                    ~~~~~~~~~~~~~~~~
                src/foo/test.kt:18: ${explanation("RadioButton" to "RadioButton")}
                                    RadioButton()
                                    ~~~~~~~~~~~
                src/foo/test.kt:19: ${explanation("Switch" to "Switch")}
                                    Switch()
                                    ~~~~~~
                src/foo/test.kt:20: ${explanation("Snackbar" to "Snackbar")}
                                    Snackbar()
                                    ~~~~~~~~
                src/foo/test.kt:21: ${explanation("LinearProgressIndicator" to "LinearProgressIndicator")}
                                    LinearProgressIndicator()
                                    ~~~~~~~~~~~~~~~~~~~~~~~
                src/foo/test.kt:22: ${explanation("CircularProgressIndicator" to "CircularProgressIndicator")}
                                    CircularProgressIndicator()
                                    ~~~~~~~~~~~~~~~~~~~~~~~~~
                src/foo/test.kt:23: ${explanation("MaterialTheme" to "SparkTheme")}
                                    MaterialTheme()
                                    ~~~~~~~~~~~~~
                15 errors, 0 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 9: Replace Button with Spark's ButtonFilled:
                @@ -3 +3
                + import com.adevinta.spark.components.buttons.ButtonFilled
                @@ -9 +10
                -                     Button()
                +                     ButtonFilled()
                Fix for src/foo/test.kt line 10: Replace ElevatedButton with Spark's ButtonFilled:
                @@ -3 +3
                + import com.adevinta.spark.components.buttons.ButtonFilled
                @@ -10 +11
                -                     ElevatedButton()
                +                     ButtonFilled()
                Fix for src/foo/test.kt line 11: Replace FilledTonalButton with Spark's ButtonTinted:
                @@ -3 +3
                + import com.adevinta.spark.components.buttons.ButtonTinted
                @@ -11 +12
                -                     FilledTonalButton()
                +                     ButtonTinted()
                Fix for src/foo/test.kt line 12: Replace OutlinedButton with Spark's ButtonOutlined:
                @@ -3 +3
                + import com.adevinta.spark.components.buttons.ButtonOutlined
                @@ -12 +13
                -                     OutlinedButton()
                +                     ButtonOutlined()
                Fix for src/foo/test.kt line 13: Replace TextButton with Spark's ButtonGhost:
                @@ -3 +3
                + import com.adevinta.spark.components.buttons.ButtonGhost
                @@ -13 +14
                -                     TextButton()
                +                     ButtonGhost()
                Fix for src/foo/test.kt line 14: Replace OutlinedTextField with Spark's TextField:
                @@ -3 +3
                + import com.adevinta.spark.components.textfields.TextField
                @@ -14 +15
                -                     OutlinedTextField()
                +                     TextField()
                Fix for src/foo/test.kt line 15: Replace FilledTextField with Spark's TextField:
                @@ -3 +3
                + import com.adevinta.spark.components.textfields.TextField
                @@ -15 +16
                -                     FilledTextField()
                +                     TextField()
                Fix for src/foo/test.kt line 16: Replace CheckBox with Spark's CheckBox:
                @@ -3 +3
                + import com.adevinta.spark.components.toggles.CheckBox
                Fix for src/foo/test.kt line 17: Replace TriStateCheckbox with Spark's CheckBox:
                @@ -3 +3
                + import com.adevinta.spark.components.toggles.CheckBox
                @@ -17 +18
                -                     TriStateCheckbox()
                +                     CheckBox()
                Fix for src/foo/test.kt line 18: Replace RadioButton with Spark's RadioButton:
                @@ -3 +3
                + import com.adevinta.spark.components.toggles.RadioButton
                Fix for src/foo/test.kt line 19: Replace Switch with Spark's Switch:
                @@ -3 +3
                + import com.adevinta.spark.components.toggles.Switch
                Fix for src/foo/test.kt line 20: Replace Snackbar with Spark's Snackbar:
                @@ -3 +3
                + import com.adevinta.spark.components.snackbars.Snackbar
                Fix for src/foo/test.kt line 21: Replace LinearProgressIndicator with Spark's LinearProgressIndicator:
                @@ -3 +3
                + import com.adevinta.spark.components.progress.LinearProgressIndicator
                Fix for src/foo/test.kt line 22: Replace CircularProgressIndicator with Spark's CircularProgressIndicator:
                @@ -3 +3
                + import com.adevinta.spark.components.progress.CircularProgressIndicator
                Fix for src/foo/test.kt line 23: Replace MaterialTheme with Spark's SparkTheme:
                @@ -3 +3
                + import com.adevinta.spark.SparkTheme
                @@ -23 +24
                -                     MaterialTheme()
                +                     SparkTheme()
                """.trimIndent(),
            )
    }

    @Test
    public fun foundationUsages(): Unit = with(ISSUE) {
        lint().files(
            kotlin(
                """
                package foo
                import androidx.compose.foundation.*
                import androidx.compose.runtime.*

                @Composable
                fun Test() {
                    Image()
                }
            """,
            ),
            *Composable,
            FoundationStub,
        )
            .allowMissingSdk()
            .run()
            .expect(
                """
                src/foo/test.kt:8: ${explanation("Image" to "Illustration")}
                                    Image()
                                    ~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 8: Replace Image with Spark's Illustration:
                @@ -3 +3
                + import com.adevinta.spark.components.image.Illustration
                @@ -8 +9
                -                     Image()
                +                     Illustration()
                """.trimIndent(),
            )
    }

    @Test
    public fun coilUsages(): Unit = with(ISSUE) {
        lint().files(
            kotlin(
                """
                package foo
                import coil.compose.AsyncImage
                import coil.compose.SubcomposeAsyncImage
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    AsyncImage()
                    SubcomposeAsyncImage()
                }
            """,
            ),
            *Composable,
            CoilComponentsStub,
        )
            .allowMissingSdk()
            .run()
            .expect(
                """
                src/foo/test.kt:9: ${explanation("AsyncImage" to "Image")}
                                    AsyncImage()
                                    ~~~~~~~~~~
                src/foo/test.kt:10: ${explanation("SubcomposeAsyncImage" to "Image")}
                                    SubcomposeAsyncImage()
                                    ~~~~~~~~~~~~~~~~~~~~
                2 errors, 0 warnings
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Fix for src/foo/test.kt line 9: Replace AsyncImage with Spark's Image:
                @@ -3 +3
                + import com.adevinta.spark.components.image.Image
                @@ -9 +10
                -                     AsyncImage()
                +                     Image()
                Fix for src/foo/test.kt line 10: Replace SubcomposeAsyncImage with Spark's Image:
                @@ -3 +3
                + import com.adevinta.spark.components.image.Image
                @@ -10 +11
                -                     SubcomposeAsyncImage()
                +                     Image()
                """.trimIndent(),
            )
    }

    @Test
    public fun noErrors() {
        lint().files(
            kotlin(
                """
                package foo
                import androidx.compose.runtime.*
                import com.adevinta.spark.*

                @Composable
                fun Test() {
                    ButtonFilled()
                    ButtonOutlined()
                    ButtonTinted()
                    ButtonGhost()
                    ButtonContrast()
                    TextField()
                    MultilineTextField()
                    SelectTextField()
                    CheckBox()
                    RadioButton()
                    Switch()
                    SparkTheme()
                    LinearProgressIndicator()
                    Snackbar()
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
