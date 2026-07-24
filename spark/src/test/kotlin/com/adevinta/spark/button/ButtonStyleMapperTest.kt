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
package com.adevinta.spark.button

import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonStyle
import com.adevinta.spark.components.buttons.ButtonStyleMapper
import com.adevinta.spark.components.buttons.ButtonVariant
import org.junit.Assert.assertEquals
import org.junit.Test

class ButtonStyleMapperTest {

    private val mapper = ButtonStyleMapper

    @Test
    fun `ai intent with any style maps to Ai`() {
        ButtonStyle.entries.forEach { style ->
            assertEquals(ButtonVariant.Ai, mapper.map(ButtonIntent.Ai, style))
        }
    }

    @Test
    fun `main filled maps to Primary`() {
        assertEquals(ButtonVariant.Primary, mapper.map(ButtonIntent.Main, ButtonStyle.Filled))
    }

    @Test
    fun `support filled maps to Secondary`() {
        assertEquals(ButtonVariant.Secondary, mapper.map(ButtonIntent.Support, ButtonStyle.Filled))
    }

    @Test
    fun `accent filled maps to Boost`() {
        assertEquals(ButtonVariant.Boost, mapper.map(ButtonIntent.Accent, ButtonStyle.Filled))
    }

    @Test
    fun `main outlined maps to Tertiary`() {
        assertEquals(ButtonVariant.Tertiary, mapper.map(ButtonIntent.Main, ButtonStyle.Outlined))
    }

    @Test
    fun `main tinted maps to Tertiary`() {
        assertEquals(ButtonVariant.Tertiary, mapper.map(ButtonIntent.Main, ButtonStyle.Tinted))
    }

    @Test
    fun `support outlined maps to Tertiary`() {
        assertEquals(ButtonVariant.Tertiary, mapper.map(ButtonIntent.Support, ButtonStyle.Outlined))
    }

    @Test
    fun `info filled maps to Tertiary`() {
        assertEquals(ButtonVariant.Tertiary, mapper.map(ButtonIntent.Info, ButtonStyle.Filled))
    }

    @Test
    fun `alert ghost maps to Ghost`() {
        assertEquals(ButtonVariant.Ghost, mapper.map(ButtonIntent.Alert, ButtonStyle.Ghost))
    }

    @Test
    fun `neutral filled maps to Tertiary`() {
        assertEquals(ButtonVariant.Tertiary, mapper.map(ButtonIntent.Neutral, ButtonStyle.Filled))
    }

    @Test
    fun `accent ghost maps to Ghost`() {
        assertEquals(ButtonVariant.Ghost, mapper.map(ButtonIntent.Accent, ButtonStyle.Ghost))
    }

    @Test
    fun `main ghost maps to Ghost`() {
        assertEquals(ButtonVariant.Ghost, mapper.map(ButtonIntent.Main, ButtonStyle.Ghost))
    }

    @Test
    fun `support ghost maps to Ghost`() {
        assertEquals(ButtonVariant.Ghost, mapper.map(ButtonIntent.Support, ButtonStyle.Ghost))
    }

    @Test
    fun `surface filled maps to Contrast`() {
        assertEquals(ButtonVariant.Contrast, mapper.map(ButtonIntent.Surface, ButtonStyle.Filled))
    }

    @Test
    fun `surface outlined maps to Contrast`() {
        assertEquals(ButtonVariant.Contrast, mapper.map(ButtonIntent.Surface, ButtonStyle.Outlined))
    }

    @Test
    fun `surface tinted maps to Contrast`() {
        assertEquals(ButtonVariant.Contrast, mapper.map(ButtonIntent.Surface, ButtonStyle.Tinted))
    }

    @Test
    fun `any intent with contrast style maps to Contrast`() {
        val intents = listOf(
            ButtonIntent.Main,
            ButtonIntent.Support,
            ButtonIntent.Surface,
            ButtonIntent.Success,
            ButtonIntent.Danger,
            ButtonIntent.Info,
            ButtonIntent.Alert,
            ButtonIntent.Neutral,
            ButtonIntent.Accent,
        )
        intents.forEach { intent ->
            assertEquals(
                "Expected Contrast for $intent + Contrast",
                ButtonVariant.Contrast,
                mapper.map(intent, ButtonStyle.Contrast),
            )
        }
    }

    @Test
    fun `any intent with underline maps to Underline`() {
        ButtonIntent.entries
            .filter { it != ButtonIntent.Ai }
            .forEach { intent ->
                assertEquals(
                    "Expected Underline for $intent + Underline",
                    ButtonVariant.Underline,
                    mapper.map(intent, ButtonStyle.Underline),
                )
            }
    }

    @Test
    fun `success with filled outlined tinted maps to Success`() {
        val styles = listOf(ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted)
        styles.forEach { style ->
            assertEquals(
                "Expected Success for Success + $style",
                ButtonVariant.Success,
                mapper.map(ButtonIntent.Success, style),
            )
        }
    }

    @Test
    fun `danger with filled outlined tinted maps to Danger`() {
        val styles = listOf(ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted)
        styles.forEach { style ->
            assertEquals(
                "Expected Danger for Danger + $style",
                ButtonVariant.Danger,
                mapper.map(ButtonIntent.Danger, style),
            )
        }
    }

    @Test
    @Suppress("DEPRECATION_ERROR")
    fun `basic filled maps to same as support filled`() {
        assertEquals(
            mapper.map(ButtonIntent.Support, ButtonStyle.Filled),
            mapper.map(ButtonIntent.Basic, ButtonStyle.Filled),
        )
    }
}
