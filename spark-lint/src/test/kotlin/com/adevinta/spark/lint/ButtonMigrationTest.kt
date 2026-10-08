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

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Parity test for [mapToVariant]. Enumerates all 60 (intent, style) pairs and asserts the expected
 * variant. Expected values come directly from the mapping logic. They do not call ButtonStyleMapper.
 * If the mapping logic drifts, this test fails.
 */
@RunWith(JUnit4::class)
class ButtonMigrationTest {

    @Test
    fun `mapToVariant - intent Ai always returns Ai regardless of style`() {
        for (style in ButtonStyle.entries) {
            assertEquals("Ai/$style", ButtonVariant.Ai, mapToVariant(ButtonIntent.Ai, style))
        }
    }

    @Test
    fun `mapToVariant - style Underline always returns Underline for non-Ai intents`() {
        for (intent in ButtonIntent.entries.filter { it != ButtonIntent.Ai }) {
            assertEquals("$intent/Underline", ButtonVariant.Underline, mapToVariant(intent, ButtonStyle.Underline))
        }
    }

    @Test
    fun `mapToVariant - style Contrast always returns Contrast for non-Ai intents`() {
        for (intent in ButtonIntent.entries.filter { it != ButtonIntent.Ai }) {
            assertEquals("$intent/Contrast", ButtonVariant.Contrast, mapToVariant(intent, ButtonStyle.Contrast))
        }
    }

    @Test
    fun `mapToVariant - style Ghost always returns Ghost for non-Ai intents`() {
        for (intent in ButtonIntent.entries.filter { it != ButtonIntent.Ai }) {
            assertEquals("$intent/Ghost", ButtonVariant.Ghost, mapToVariant(intent, ButtonStyle.Ghost))
        }
    }

    @Test
    fun `mapToVariant - all 60 pairs exhaustive check`() {
        // Each triple is (intent, style, expectedVariant).
        // Expected values come directly from the mapping logic.
        val cases: List<Triple<ButtonIntent, ButtonStyle, ButtonVariant>> = listOf(
            // Ai intent - always Ai regardless of style
            Triple(ButtonIntent.Ai, ButtonStyle.Filled, ButtonVariant.Ai),
            Triple(ButtonIntent.Ai, ButtonStyle.Outlined, ButtonVariant.Ai),
            Triple(ButtonIntent.Ai, ButtonStyle.Tinted, ButtonVariant.Ai),
            Triple(ButtonIntent.Ai, ButtonStyle.Ghost, ButtonVariant.Ai),
            Triple(ButtonIntent.Ai, ButtonStyle.Contrast, ButtonVariant.Ai),
            Triple(ButtonIntent.Ai, ButtonStyle.Underline, ButtonVariant.Ai),

            // Main intent
            Triple(ButtonIntent.Main, ButtonStyle.Filled, ButtonVariant.Primary),
            Triple(ButtonIntent.Main, ButtonStyle.Outlined, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Main, ButtonStyle.Tinted, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Main, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Main, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Main, ButtonStyle.Underline, ButtonVariant.Underline),

            // Support intent
            Triple(ButtonIntent.Support, ButtonStyle.Filled, ButtonVariant.Secondary),
            Triple(ButtonIntent.Support, ButtonStyle.Outlined, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Support, ButtonStyle.Tinted, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Support, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Support, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Support, ButtonStyle.Underline, ButtonVariant.Underline),

            // Accent intent
            Triple(ButtonIntent.Accent, ButtonStyle.Filled, ButtonVariant.Boost),
            Triple(ButtonIntent.Accent, ButtonStyle.Outlined, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Accent, ButtonStyle.Tinted, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Accent, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Accent, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Accent, ButtonStyle.Underline, ButtonVariant.Underline),

            // Surface intent - Filled/Outlined/Tinted all map to Contrast
            Triple(ButtonIntent.Surface, ButtonStyle.Filled, ButtonVariant.Contrast),
            Triple(ButtonIntent.Surface, ButtonStyle.Outlined, ButtonVariant.Contrast),
            Triple(ButtonIntent.Surface, ButtonStyle.Tinted, ButtonVariant.Contrast),
            Triple(ButtonIntent.Surface, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Surface, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Surface, ButtonStyle.Underline, ButtonVariant.Underline),

            // Success intent
            Triple(ButtonIntent.Success, ButtonStyle.Filled, ButtonVariant.Success),
            Triple(ButtonIntent.Success, ButtonStyle.Outlined, ButtonVariant.Success),
            Triple(ButtonIntent.Success, ButtonStyle.Tinted, ButtonVariant.Success),
            Triple(ButtonIntent.Success, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Success, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Success, ButtonStyle.Underline, ButtonVariant.Underline),

            // Alert intent - Filled/Outlined/Tinted all map to Tertiary
            Triple(ButtonIntent.Alert, ButtonStyle.Filled, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Alert, ButtonStyle.Outlined, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Alert, ButtonStyle.Tinted, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Alert, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Alert, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Alert, ButtonStyle.Underline, ButtonVariant.Underline),

            // Danger intent
            Triple(ButtonIntent.Danger, ButtonStyle.Filled, ButtonVariant.Danger),
            Triple(ButtonIntent.Danger, ButtonStyle.Outlined, ButtonVariant.Danger),
            Triple(ButtonIntent.Danger, ButtonStyle.Tinted, ButtonVariant.Danger),
            Triple(ButtonIntent.Danger, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Danger, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Danger, ButtonStyle.Underline, ButtonVariant.Underline),

            // Info intent
            Triple(ButtonIntent.Info, ButtonStyle.Filled, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Info, ButtonStyle.Outlined, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Info, ButtonStyle.Tinted, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Info, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Info, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Info, ButtonStyle.Underline, ButtonVariant.Underline),

            // Neutral intent
            Triple(ButtonIntent.Neutral, ButtonStyle.Filled, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Neutral, ButtonStyle.Outlined, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Neutral, ButtonStyle.Tinted, ButtonVariant.Tertiary),
            Triple(ButtonIntent.Neutral, ButtonStyle.Ghost, ButtonVariant.Ghost),
            Triple(ButtonIntent.Neutral, ButtonStyle.Contrast, ButtonVariant.Contrast),
            Triple(ButtonIntent.Neutral, ButtonStyle.Underline, ButtonVariant.Underline),
        )

        assertEquals("Expected exactly 60 pairs", 60, cases.size)

        for ((intent, style, expected) in cases) {
            assertEquals("mapToVariant($intent, $style)", expected, mapToVariant(intent, style))
        }
    }
}
