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

/** Local copy of `ButtonStyle` from the `spark` module, whose type is internal and not visible here. */
internal enum class ButtonStyle {
    Filled,
    Outlined,
    Tinted,
    Ghost,
    Contrast,
    Underline,
}

/** Local copy of `ButtonIntent` from the `spark` module, whose type is internal and not visible here. */
internal enum class ButtonIntent {
    Main,
    Support,
    Accent,
    Surface,
    Success,
    Alert,
    Danger,
    Info,
    Neutral,
    Ai,
}

/** Local copy of `ButtonVariant` from the `spark` module, whose type is internal and not visible here. */
internal enum class ButtonVariant {
    Ai,
    Primary,
    Secondary,
    Tertiary,
    Contrast,
    Ghost,
    Underline,
    Success,
    Danger,
    Boost,
}

/**
 * Maps a [ButtonIntent] and [ButtonStyle] pair to the target [ButtonVariant].
 *
 * This is a copy of `ButtonStyleMapper.map` in the `spark` module. spark-lint cannot call that mapper directly
 * because it is internal to the spark module. [ButtonMigrationTest] guards that this copy stays in sync.
 */
internal fun mapToVariant(intent: ButtonIntent, style: ButtonStyle): ButtonVariant {
    if (intent == ButtonIntent.Ai) return ButtonVariant.Ai
    if (style == ButtonStyle.Underline) return ButtonVariant.Underline
    if (style == ButtonStyle.Contrast) return ButtonVariant.Contrast
    if (style == ButtonStyle.Ghost) return ButtonVariant.Ghost
    return when (intent) {
        ButtonIntent.Main -> when (style) {
            ButtonStyle.Filled -> ButtonVariant.Primary
            ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        }

        ButtonIntent.Support -> when (style) {
            ButtonStyle.Filled -> ButtonVariant.Secondary
            ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        }

        ButtonIntent.Accent -> when (style) {
            ButtonStyle.Filled -> ButtonVariant.Boost
            ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        }

        ButtonIntent.Surface -> when (style) {
            ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Contrast
        }

        ButtonIntent.Success -> when (style) {
            ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Success
        }

        ButtonIntent.Danger -> when (style) {
            ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Danger
        }

        ButtonIntent.Info, ButtonIntent.Alert, ButtonIntent.Neutral -> when (style) {
            ButtonStyle.Filled, ButtonStyle.Outlined, ButtonStyle.Tinted -> ButtonVariant.Tertiary
        }

        ButtonIntent.Ai -> error("ButtonIntent.Ai is handled before reaching the when block")
    }
}

/**
 * Maps old button composable simple names to the [ButtonStyle] each one hardcodes.
 *
 * Keys are the unqualified composable names as they appear in source (for example `"ButtonFilled"`).
 */
internal val oldComposableToStyle: Map<String, ButtonStyle> = mapOf(
    "ButtonFilled" to ButtonStyle.Filled,
    "ButtonOutlined" to ButtonStyle.Outlined,
    "ButtonTinted" to ButtonStyle.Tinted,
    "ButtonGhost" to ButtonStyle.Ghost,
    "ButtonContrast" to ButtonStyle.Contrast,
)

/**
 * Maps a [ButtonVariant] to the fully-qualified callee string used in the new `Button.<Variant>` API.
 *
 * All callees live in package `com.adevinta.spark.components.buttons`. The map is total over
 * [ButtonVariant] to match [mapToVariant]. [mapToVariant] never returns [ButtonVariant.Underline]
 * because no style button uses [ButtonStyle.Underline]. [directVariantForOldComposable] reaches it
 * for `TextLinkButton`.
 */
internal val variantToCallee: Map<ButtonVariant, String> = mapOf(
    ButtonVariant.Ai to "Button.Ai",
    ButtonVariant.Primary to "Button.Primary",
    ButtonVariant.Secondary to "Button.Secondary",
    ButtonVariant.Tertiary to "Button.Tertiary",
    ButtonVariant.Contrast to "Button.Contrast",
    ButtonVariant.Ghost to "Button.Ghost",
    ButtonVariant.Underline to "Button.Underlined",
    ButtonVariant.Success to "Button.Success",
    ButtonVariant.Danger to "Button.Danger",
    ButtonVariant.Boost to "Button.Boost",
)

/**
 * Maps each old button composable name to the [ButtonIntent] applied when the caller omits the
 * `intent` argument, matching the default parameter values in the source composables.
 *
 * `ButtonOutlined` defaults to [ButtonIntent.Support]. All other old buttons default to
 * [ButtonIntent.Main].
 */
internal val defaultIntentForOldComposable: Map<String, ButtonIntent> = mapOf(
    "ButtonFilled" to ButtonIntent.Main,
    "ButtonOutlined" to ButtonIntent.Support,
    "ButtonTinted" to ButtonIntent.Main,
    "ButtonGhost" to ButtonIntent.Main,
    "ButtonContrast" to ButtonIntent.Main,
)

/**
 * Maps each old composable name to the package that declares it. The detector reports a call only
 * when its package matches, so a function with the same name from another package is not flagged.
 *
 * The five style buttons live in `com.adevinta.spark.components.buttons`. `TextLinkButton` lives in
 * `com.adevinta.spark.components.text`.
 */
internal val oldComposablePackage: Map<String, String> = mapOf(
    "ButtonFilled" to "com.adevinta.spark.components.buttons",
    "ButtonOutlined" to "com.adevinta.spark.components.buttons",
    "ButtonTinted" to "com.adevinta.spark.components.buttons",
    "ButtonGhost" to "com.adevinta.spark.components.buttons",
    "ButtonContrast" to "com.adevinta.spark.components.buttons",
    "TextLinkButton" to "com.adevinta.spark.components.text",
)

/**
 * Maps an old composable that always migrates to one variant, whatever its intent or style, to that
 * [ButtonVariant]. `TextLinkButton` always becomes `Button.Underlined` under the rebranded flag, so
 * the target does not depend on the `intent` argument. The style and intent model in [mapToVariant]
 * does not apply to these composables.
 */
internal val directVariantForOldComposable: Map<String, ButtonVariant> = mapOf(
    "TextLinkButton" to ButtonVariant.Underline,
)
