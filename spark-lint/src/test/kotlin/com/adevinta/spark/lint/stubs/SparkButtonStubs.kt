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
package com.adevinta.spark.lint.stubs

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin

/**
 * Full-signature button stubs for Phase 4.2 detector tests.
 *
 * [SparkComponentsStubs] keeps its zero-arg stubs for existing tests.
 * These stubs add the full parameter lists that the detector needs.
 *
 * Old API: ButtonFilled, ButtonOutlined, ButtonTinted, ButtonGhost, ButtonContrast.
 * Each old button has three overloads: content-slot, text: String, text: AnnotatedString.
 *
 * New API: Button.Primary, Button.Secondary, Button.Tertiary, Button.Contrast,
 * Button.Ghost, Button.Success, Button.Danger, Button.Boost, Button.Ai, Button.Underlined.
 * Each new button has two overloads: content-slot, text: String.
 */
internal val SparkButtonStubs: Array<TestFile> = arrayOf(

    // Compose foundation types not already stubbed in existing files

    kotlin(
        """
        package androidx.compose.foundation.layout

        interface RowScope
        """.trimIndent(),
    ),

    kotlin(
        """
        package androidx.compose.foundation.interaction

        class MutableInteractionSource
        """.trimIndent(),
    ),

    kotlin(
        """
        package androidx.compose.ui.text

        class AnnotatedString(val text: String)
        """.trimIndent(),
    ),

    // SparkIcon stub

    kotlin(
        """
        package com.adevinta.spark.icons

        interface SparkIcon
        """.trimIndent(),
    ),

    // Button supporting types

    kotlin(
        """
        package com.adevinta.spark.components.buttons

        enum class ButtonStyle { Filled, Outlined, Tinted, Ghost, Contrast, Underline }

        enum class ButtonIntent { Main, Support, Accent, Surface, Success, Alert, Danger, Info, Neutral, Ai }

        enum class ButtonSize { Small, Medium, Large }

        enum class IconSide { START, END }

        enum class ButtonShape { Square, Pill, Rounded }

        object Button
        """.trimIndent(),
    ),

    // Old buttons: full signatures (3 overloads per button, 5 buttons)
    // Overload 1 (content-slot): onClick, modifier, size, shape, intent, enabled,
    //   icon, iconSide, isLoading, interactionSource, atEnd, content
    // Overload 2 (text: String): same as content-slot, text after onClick, no content
    // Overload 3 (text: AnnotatedString): like String overload but no shape parameter

    kotlin(
        """
        package com.adevinta.spark.components.buttons

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.foundation.layout.RowScope
        import androidx.compose.foundation.interaction.MutableInteractionSource
        import androidx.compose.ui.text.AnnotatedString
        import com.adevinta.spark.icons.SparkIcon

        // ButtonFilled - default intent: Main

        @Composable
        fun ButtonFilled(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun ButtonFilled(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        @Composable
        fun ButtonFilled(
            onClick: () -> Unit,
            text: AnnotatedString,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // ButtonOutlined - default intent: Support

        @Composable
        fun ButtonOutlined(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Support,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun ButtonOutlined(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Support,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        @Composable
        fun ButtonOutlined(
            onClick: () -> Unit,
            text: AnnotatedString,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Support,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // ButtonTinted - default intent: Main

        @Composable
        fun ButtonTinted(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun ButtonTinted(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        @Composable
        fun ButtonTinted(
            onClick: () -> Unit,
            text: AnnotatedString,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // ButtonGhost - default intent: Main

        @Composable
        fun ButtonGhost(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun ButtonGhost(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        @Composable
        fun ButtonGhost(
            onClick: () -> Unit,
            text: AnnotatedString,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // ButtonContrast - default intent: Main

        @Composable
        fun ButtonContrast(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun ButtonContrast(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            shape: ButtonShape = ButtonShape.Rounded,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        @Composable
        fun ButtonContrast(
            onClick: () -> Unit,
            text: AnnotatedString,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Main,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}
        """.trimIndent(),
    ),

    // New buttons: Button.X extension functions (2 overloads per variant, 10 variants)
    // Parameter order: onClick, [text,] modifier, size, enabled, icon, iconSide,
    //   isLoading, interactionSource, atEnd, [content]
    // No intent, no shape, no AnnotatedString overload.

    kotlin(
        """
        package com.adevinta.spark.components.buttons

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.foundation.layout.RowScope
        import androidx.compose.foundation.interaction.MutableInteractionSource
        import com.adevinta.spark.icons.SparkIcon

        // Button.Primary

        @Composable
        fun Button.Primary(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Primary(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Secondary

        @Composable
        fun Button.Secondary(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Secondary(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Tertiary

        @Composable
        fun Button.Tertiary(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Tertiary(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Contrast

        @Composable
        fun Button.Contrast(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Contrast(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Ghost

        @Composable
        fun Button.Ghost(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Ghost(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Success

        @Composable
        fun Button.Success(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Success(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Danger

        @Composable
        fun Button.Danger(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Danger(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Boost

        @Composable
        fun Button.Boost(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Boost(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Ai

        @Composable
        fun Button.Ai(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
            content: @Composable RowScope.() -> Unit,
        ) {}

        @Composable
        fun Button.Ai(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}

        // Button.Underlined (text overload only; no public content-slot overload)

        @Composable
        fun Button.Underlined(
            onClick: () -> Unit,
            text: String,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            atEnd: Boolean = false,
        ) {}
        """.trimIndent(),
    ),

    // TextLinkButton: text overload and content-slot overload, in package components.text

    kotlin(
        """
        package com.adevinta.spark.components.text

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.foundation.layout.RowScope
        import androidx.compose.foundation.interaction.MutableInteractionSource
        import com.adevinta.spark.components.buttons.ButtonIntent
        import com.adevinta.spark.components.buttons.ButtonSize
        import com.adevinta.spark.components.buttons.IconSide
        import com.adevinta.spark.icons.SparkIcon

        @Composable
        fun TextLinkButton(
            text: String,
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Surface,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
        ) {}

        @Composable
        fun TextLinkButton(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            size: ButtonSize = ButtonSize.Medium,
            intent: ButtonIntent = ButtonIntent.Surface,
            enabled: Boolean = true,
            icon: SparkIcon? = null,
            iconSide: IconSide = IconSide.START,
            isLoading: Boolean = false,
            interactionSource: MutableInteractionSource = MutableInteractionSource(),
            content: @Composable RowScope.() -> Unit,
        ) {}
        """.trimIndent(),
    ),
)
