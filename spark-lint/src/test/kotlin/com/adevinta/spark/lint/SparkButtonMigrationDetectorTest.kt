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
@file:Suppress("UnstableApiUsage")

package com.adevinta.spark.lint

import com.adevinta.spark.lint.SparkButtonMigrationDetector.Companion.ISSUE
import com.adevinta.spark.lint.stubs.Composables
import com.adevinta.spark.lint.stubs.SparkButtonStubs
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Detector tests for [SparkButtonMigrationDetector].
 *
 * One focused test per case:
 *  - One test per old button composable to confirm all five are detected.
 *  - Style-to-variant path coverage: every distinct (style, intent) → variant mapping exercised.
 *  - Intent forms: named, positional, absent (per-button default), unresolved.
 *  - Overloads: content-slot, text:String, text:AnnotatedString.
 *  - Argument variety: extra args (enabled, isLoading, atEnd) confirmed preserved; intent/shape dropped.
 *  - Nested composable: button inside an inner function.
 */
@RunWith(JUnit4::class)
class SparkButtonMigrationDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = SparkButtonMigrationDetector()
    override fun getIssues(): List<Issue> = listOf(ISSUE)

    // ── Per-button detection ────────────────────────────────────────────────

    /** ButtonFilled detected: content-slot overload, named Main intent → Button.Primary. */
    @Test
    fun buttonFilled_namedMain_contentSlot_rewritesToPrimary() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { }
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Primary. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { }
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Primary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Primary
                @@ -9 +11 @@
                -    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { }
                +    Button.Primary(onClick = {}) { }
                """.trimIndent(),
            )
    }

    /** Fixes are marked safe for bulk use, and each one imports both `Button` and its variant extension. */
    @Test
    fun multipleButtons_fixesApplyInBulk() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import com.adevinta.spark.components.buttons.ButtonOutlined
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { }
                    ButtonOutlined(onClick = {}, text = "Confirm")
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .verifyFixes()
            .robot(true)
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 10: Replace ButtonFilled with Button.Primary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -6,0 +8 @@
                +import com.adevinta.spark.components.buttons.Primary
                @@ -10 +12 @@
                -    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { }
                +    Button.Primary(onClick = {}) { }
                Autofix for src/foo/test.kt line 11: Replace ButtonOutlined with Button.Tertiary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -6,0 +8 @@
                +import com.adevinta.spark.components.buttons.Tertiary
                @@ -11 +13 @@
                -    ButtonOutlined(onClick = {}, text = "Confirm")
                +    Button.Tertiary(onClick = {}, text = "Confirm")
                """.trimIndent(),
            )
    }

    /** ButtonOutlined detected: text:String overload, absent intent (default Support) → Button.Tertiary. */
    @Test
    fun buttonOutlined_defaultIntent_textString_rewritesToTertiary() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonOutlined
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonOutlined(onClick = {}, text = "Confirm")
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:8: Warning: ButtonOutlined is an old style-based Spark button. Replace it with Button.Tertiary. [SparkButtonMigration]
                    ButtonOutlined(onClick = {}, text = "Confirm")
                    ~~~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 8: Replace ButtonOutlined with Button.Tertiary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -4,0 +6 @@
                +import com.adevinta.spark.components.buttons.Tertiary
                @@ -8 +10 @@
                -    ButtonOutlined(onClick = {}, text = "Confirm")
                +    Button.Tertiary(onClick = {}, text = "Confirm")
                """.trimIndent(),
            )
    }

    /** ButtonTinted detected: text:String overload, named Success intent → Button.Success. */
    @Test
    fun buttonTinted_namedSuccess_textString_rewritesToSuccess() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonTinted
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonTinted(onClick = {}, text = "Ok", intent = ButtonIntent.Success)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonTinted is an old style-based Spark button. Replace it with Button.Success. [SparkButtonMigration]
                    ButtonTinted(onClick = {}, text = "Ok", intent = ButtonIntent.Success)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonTinted with Button.Success:
                @@ -3,0 +4 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Success
                @@ -9 +11 @@
                -    ButtonTinted(onClick = {}, text = "Ok", intent = ButtonIntent.Success)
                +    Button.Success(onClick = {}, text = "Ok")
                """.trimIndent(),
            )
    }

    /** ButtonGhost detected: text:String overload, absent intent (default Main) → Button.Ghost. */
    @Test
    fun buttonGhost_defaultIntent_textString_rewritesToGhost() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonGhost
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonGhost(onClick = {}, text = "Cancel")
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:8: Warning: ButtonGhost is an old style-based Spark button. Replace it with Button.Ghost. [SparkButtonMigration]
                    ButtonGhost(onClick = {}, text = "Cancel")
                    ~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 8: Replace ButtonGhost with Button.Ghost:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -4,0 +6 @@
                +import com.adevinta.spark.components.buttons.Ghost
                @@ -8 +10 @@
                -    ButtonGhost(onClick = {}, text = "Cancel")
                +    Button.Ghost(onClick = {}, text = "Cancel")
                """.trimIndent(),
            )
    }

    /** ButtonContrast detected: content-slot overload, absent intent (default Main) → Button.Contrast. */
    @Test
    fun buttonContrast_defaultIntent_contentSlot_rewritesToContrast() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonContrast
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonContrast(onClick = {}) { }
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:8: Warning: ButtonContrast is an old style-based Spark button. Replace it with Button.Contrast. [SparkButtonMigration]
                    ButtonContrast(onClick = {}) { }
                    ~~~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 8: Replace ButtonContrast with Button.Contrast:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -4,0 +6 @@
                +import com.adevinta.spark.components.buttons.Contrast
                @@ -8 +10 @@
                -    ButtonContrast(onClick = {}) { }
                +    Button.Contrast(onClick = {}) { }
                """.trimIndent(),
            )
    }

    // ── Style-to-variant path coverage ─────────────────────────────────────

    /** ButtonFilled + Support → Button.Secondary. */
    @Test
    fun buttonFilled_namedSupport_textString_rewritesToSecondary() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, text = "Save", intent = ButtonIntent.Support)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Secondary. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, text = "Save", intent = ButtonIntent.Support)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Secondary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Secondary
                @@ -9 +11 @@
                -    ButtonFilled(onClick = {}, text = "Save", intent = ButtonIntent.Support)
                +    Button.Secondary(onClick = {}, text = "Save")
                """.trimIndent(),
            )
    }

    /** ButtonFilled + Accent → Button.Boost. */
    @Test
    fun buttonFilled_namedAccent_textString_rewritesToBoost() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, text = "Boost", intent = ButtonIntent.Accent)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Boost. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, text = "Boost", intent = ButtonIntent.Accent)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Boost:
                @@ -2,0 +3,2 @@
                +import com.adevinta.spark.components.buttons.Boost
                +import com.adevinta.spark.components.buttons.Button
                @@ -9 +11 @@
                -    ButtonFilled(onClick = {}, text = "Boost", intent = ButtonIntent.Accent)
                +    Button.Boost(onClick = {}, text = "Boost")
                """.trimIndent(),
            )
    }

    /** ButtonFilled + Danger → Button.Danger; extra args (enabled, isLoading, atEnd) are preserved and intent is dropped. */
    @Test
    fun buttonFilled_namedDanger_withExtraArgs_rewritesToDanger() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(
                        onClick = {},
                        text = "Delete",
                        intent = ButtonIntent.Danger,
                        enabled = false,
                        isLoading = true,
                        atEnd = true,
                    )
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Danger. [SparkButtonMigration]
                    ButtonFilled(
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Danger:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Danger
                @@ -9,8 +11 @@
                -    ButtonFilled(
                -        onClick = {},
                -        text = "Delete",
                -        intent = ButtonIntent.Danger,
                -        enabled = false,
                -        isLoading = true,
                -        atEnd = true,
                -    )
                +    Button.Danger(onClick = {}, text = "Delete", enabled = false, isLoading = true, atEnd = true)
                """.trimIndent(),
            )
    }

    /** ButtonFilled + Ai → Button.Ai (Ai intent always maps to Ai variant regardless of style). */
    @Test
    fun buttonFilled_namedAi_rewritesToAi() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, text = "Ask AI", intent = ButtonIntent.Ai)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Ai. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, text = "Ask AI", intent = ButtonIntent.Ai)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Ai:
                @@ -2,0 +3,2 @@
                +import com.adevinta.spark.components.buttons.Ai
                +import com.adevinta.spark.components.buttons.Button
                @@ -9 +11 @@
                -    ButtonFilled(onClick = {}, text = "Ask AI", intent = ButtonIntent.Ai)
                +    Button.Ai(onClick = {}, text = "Ask AI")
                """.trimIndent(),
            )
    }

    /** ButtonFilled + Surface → Button.Contrast (Surface intent maps Filled style to Contrast). */
    @Test
    fun buttonFilled_namedSurface_rewritesToContrast() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, text = "Surface", intent = ButtonIntent.Surface)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Contrast. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, text = "Surface", intent = ButtonIntent.Surface)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Contrast:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Contrast
                @@ -9 +11 @@
                -    ButtonFilled(onClick = {}, text = "Surface", intent = ButtonIntent.Surface)
                +    Button.Contrast(onClick = {}, text = "Surface")
                """.trimIndent(),
            )
    }

    // ── Intent-form coverage ────────────────────────────────────────────────

    /** Positional intent argument is resolved and produces the correct variant. */
    @Test
    fun buttonFilled_positionalIntent_textString_rewritesToSecondary() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import com.adevinta.spark.components.buttons.ButtonShape
                import com.adevinta.spark.components.buttons.ButtonSize
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled({}, "Hello", modifier = androidx.compose.ui.Modifier, size = ButtonSize.Medium, shape = ButtonShape.Rounded, intent = ButtonIntent.Support)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:11: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Secondary. [SparkButtonMigration]
                    ButtonFilled({}, "Hello", modifier = androidx.compose.ui.Modifier, size = ButtonSize.Medium, shape = ButtonShape.Rounded, intent = ButtonIntent.Support)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
    }

    /** Unresolved intent (local variable) reports a warning with no autofix. */
    @Test
    fun buttonFilled_unresolvedIntent_warnsWithNoFix() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    val myIntent = ButtonIntent.Main
                    ButtonFilled(onClick = {}, text = "Hello", intent = myIntent)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:10: Warning: ButtonFilled is an old style-based Spark button. The intent argument could not be resolved to a constant ButtonIntent; migrate this call by hand. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, text = "Hello", intent = myIntent)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs("")
    }

    // ── Overload coverage ───────────────────────────────────────────────────

    /** text:AnnotatedString overload: content lambda is synthesised as { Text(text = <expr>) }. */
    @Test
    fun buttonFilled_annotatedString_rewritesToTextLambda() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.ui.text.AnnotatedString
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, text = AnnotatedString("Hello"), intent = ButtonIntent.Main)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:10: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Primary. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, text = AnnotatedString("Hello"), intent = ButtonIntent.Main)
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 10: Replace ButtonFilled with Button.Primary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -6,0 +8,2 @@
                +import com.adevinta.spark.components.buttons.Primary
                +import com.adevinta.spark.components.text.Text
                @@ -10 +13 @@
                -    ButtonFilled(onClick = {}, text = AnnotatedString("Hello"), intent = ButtonIntent.Main)
                +    Button.Primary(onClick = {}) { Text(text = AnnotatedString("Hello")) }
                """.trimIndent(),
            )
    }

    /** Content-slot overload with a real body: the content lambda is kept verbatim. */
    @Test
    fun buttonFilled_contentSlotWithBody_preservesLambda() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { Text("myText") }
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Primary. [SparkButtonMigration]
                    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { Text("myText") }
                    ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Primary:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Primary
                @@ -9 +11 @@
                -    ButtonFilled(onClick = {}, intent = ButtonIntent.Main) { Text("myText") }
                +    Button.Primary(onClick = {}) { Text("myText") }
                """.trimIndent(),
            )
    }

    // ── Nested composable ───────────────────────────────────────────────────

    /**
     * A button nested inside another local composable still produces a warning at the
     * correct call-site location.
     */
    @Test
    fun buttonFilled_nestedInsideLocalComposable_correctLocation() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Screen() {
                    @Composable
                    fun InnerContent() {
                        ButtonFilled(onClick = {}, text = "Nested", intent = ButtonIntent.Main)
                    }
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:11: Warning: ButtonFilled is an old style-based Spark button. Replace it with Button.Primary. [SparkButtonMigration]
                        ButtonFilled(onClick = {}, text = "Nested", intent = ButtonIntent.Main)
                        ~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
    }

    // ── TextLinkButton (direct mapping to Button.Underlined) ──────────────────

    /** TextLinkButton text overload maps to Button.Underlined regardless of intent. */
    @Test
    fun textLinkButton_textString_rewritesToUnderlined() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.text.TextLinkButton
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    TextLinkButton(text = "Terms", onClick = {})
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:8: Warning: TextLinkButton is an old Spark button. Replace it with Button.Underlined. [SparkButtonMigration]
                    TextLinkButton(text = "Terms", onClick = {})
                    ~~~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 8: Replace TextLinkButton with Button.Underlined:
                @@ -2,0 +3,2 @@
                +import com.adevinta.spark.components.buttons.Button
                +import com.adevinta.spark.components.buttons.Underlined
                @@ -8 +10 @@
                -    TextLinkButton(text = "Terms", onClick = {})
                +    Button.Underlined(text = "Terms", onClick = {})
                """.trimIndent(),
            )
    }

    /** TextLinkButton ignores intent: an explicit intent still maps to Button.Underlined, intent dropped. */
    @Test
    fun textLinkButton_withIntent_rewritesToUnderlinedAndDropsIntent() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.text.TextLinkButton
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    TextLinkButton(text = "Terms", onClick = {}, intent = ButtonIntent.Success)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:9: Warning: TextLinkButton is an old Spark button. Replace it with Button.Underlined. [SparkButtonMigration]
                    TextLinkButton(text = "Terms", onClick = {}, intent = ButtonIntent.Success)
                    ~~~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace TextLinkButton with Button.Underlined:
                @@ -2,0 +3 @@
                +import com.adevinta.spark.components.buttons.Underlined
                @@ -3,0 +5 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -9 +11 @@
                -    TextLinkButton(text = "Terms", onClick = {}, intent = ButtonIntent.Success)
                +    Button.Underlined(text = "Terms", onClick = {})
                """.trimIndent(),
            )
    }

    /** TextLinkButton content-slot overload has no public Button.Underlined target, so it warns with no fix. */
    @Test
    fun textLinkButton_contentSlot_warnsWithNoFix() {
        lint().files(
            kotlin(
                """
                package foo

                import com.adevinta.spark.components.text.TextLinkButton
                import androidx.compose.runtime.Composable

                @Composable
                fun Test() {
                    TextLinkButton(onClick = {}) { Text("Terms") }
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expect(
                """
                src/foo/test.kt:8: Warning: TextLinkButton is an old Spark button. Replace it with Button.Underlined. [SparkButtonMigration]
                    TextLinkButton(onClick = {}) { Text("Terms") }
                    ~~~~~~~~~~~~~~
                0 errors, 1 warning
                """.trimIndent(),
            )
            .expectFixDiffs("")
    }

    // ── Argument binding ────────────────────────────────────────────────────

    private val callSiteStubs = kotlin(
        """
        package foo

        import androidx.compose.ui.Modifier
        import com.adevinta.spark.icons.SparkIcon

        class Dp
        val Int.dp: Dp get() = Dp()
        fun Modifier.fillMaxWidth(): Modifier = this
        fun Modifier.padding(vertical: Dp): Modifier = this
        fun stringResource(id: Int): String = ""
        object R {
            object string {
                const val accountemailpart_modern_edit_action_save = 1
                const val login_account_blocked_suggest_two_factor_activation_cta_later = 2
            }
        }
        class State(val submitEnabled: Boolean)
        object LeboncoinIcons {
            val ClockArrow: SparkIcon = object : SparkIcon {}
        }
        """,
    ).indented()

    /** Named arguments in a different order from the declaration each keep their own parameter. */
    @Test
    fun buttonFilled_namedArgumentsOutOfOrder_keepTheirParameters() {
        lint().files(
            kotlin(
                """
                package foo

                import androidx.compose.ui.Modifier
                import com.adevinta.spark.components.buttons.ButtonFilled
                import androidx.compose.runtime.Composable

                @Composable
                fun Test(state: State, onSaveClick: () -> Unit) {
                    ButtonFilled(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        text = stringResource(R.string.accountemailpart_modern_edit_action_save),
                        enabled = state.submitEnabled,
                        onClick = onSaveClick,
                    )
                }
                """,
            ).indented(),
            callSiteStubs,
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 9: Replace ButtonFilled with Button.Primary:
                @@ -3,0 +4 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -5,0 +7 @@
                +import com.adevinta.spark.components.buttons.Primary
                @@ -9,2 +11 @@
                -    ButtonFilled(
                -        modifier = Modifier
                +    Button.Primary(onClick = onSaveClick, text = stringResource(R.string.accountemailpart_modern_edit_action_save), modifier = Modifier
                @@ -12,5 +13 @@
                -            .padding(vertical = 16.dp),
                -        text = stringResource(R.string.accountemailpart_modern_edit_action_save),
                -        enabled = state.submitEnabled,
                -        onClick = onSaveClick,
                -    )
                +            .padding(vertical = 16.dp), enabled = state.submitEnabled)
                """.trimIndent(),
            )
    }

    /** Positional arguments bind to the declared parameter order and are emitted as named arguments. */
    @Test
    fun buttonFilled_positionalArguments_emittedAsNamed() {
        lint().files(
            kotlin(
                """
                package foo

                import androidx.compose.ui.Modifier
                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonSize
                import androidx.compose.runtime.Composable

                @Composable
                fun Test(modifier: Modifier) {
                    ButtonFilled({}, "Save", modifier, ButtonSize.Large)
                }
                """,
            ).indented(),
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 10: Replace ButtonFilled with Button.Primary:
                @@ -3,0 +4 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -6,0 +8 @@
                +import com.adevinta.spark.components.buttons.Primary
                @@ -10 +12 @@
                -    ButtonFilled({}, "Save", modifier, ButtonSize.Large)
                +    Button.Primary(onClick = {}, text = "Save", modifier = modifier, size = ButtonSize.Large)
                """.trimIndent(),
            )
    }

    /** A named `intent` after other named arguments selects the variant and is dropped. */
    @Test
    fun buttonFilled_namedSurfaceIntentAmongOtherArgs_rewritesToContrast() {
        lint().files(
            kotlin(
                """
                package foo

                import androidx.compose.ui.Modifier
                import com.adevinta.spark.components.buttons.ButtonFilled
                import com.adevinta.spark.components.buttons.ButtonIntent
                import androidx.compose.runtime.Composable

                @Composable
                fun Test(modifier: Modifier, onClick: () -> Unit) {
                    ButtonFilled(
                        onClick = onClick,
                        modifier = modifier,
                        intent = ButtonIntent.Surface,
                        icon = LeboncoinIcons.ClockArrow,
                        text = stringResource(id = R.string.login_account_blocked_suggest_two_factor_activation_cta_later),
                    )
                }
                """,
            ).indented(),
            callSiteStubs,
            *Composables,
            *SparkButtonStubs,
        ).run()
            .expectFixDiffs(
                """
                Autofix for src/foo/test.kt line 10: Replace ButtonFilled with Button.Contrast:
                @@ -3,0 +4 @@
                +import com.adevinta.spark.components.buttons.Button
                @@ -6,0 +8 @@
                +import com.adevinta.spark.components.buttons.Contrast
                @@ -10,7 +12 @@
                -    ButtonFilled(
                -        onClick = onClick,
                -        modifier = modifier,
                -        intent = ButtonIntent.Surface,
                -        icon = LeboncoinIcons.ClockArrow,
                -        text = stringResource(id = R.string.login_account_blocked_suggest_two_factor_activation_cta_later),
                -    )
                +    Button.Contrast(onClick = onClick, text = stringResource(id = R.string.login_account_blocked_suggest_two_factor_activation_cta_later), modifier = modifier, icon = LeboncoinIcons.ClockArrow)
                """.trimIndent(),
            )
    }

    /** Every constant intent passed by name after other arguments selects the mapped variant. */
    @Test
    fun buttonFilled_everyNamedIntent_selectsMappedVariant() {
        for (intent in ButtonIntent.entries) {
            val target = variantToCallee.getValue(mapToVariant(intent, ButtonStyle.Filled))
            lint().files(
                kotlin(
                    """
                    package foo

                    import com.adevinta.spark.components.buttons.ButtonFilled
                    import com.adevinta.spark.components.buttons.ButtonIntent
                    import androidx.compose.runtime.Composable

                    @Composable
                    fun Test() {
                        ButtonFilled(onClick = {}, enabled = false, intent = ButtonIntent.${intent.name}, text = "Go")
                    }
                    """,
                ).indented(),
                *Composables,
                *SparkButtonStubs,
            ).run()
                .expectContains("Replace it with $target.")
        }
    }
}
