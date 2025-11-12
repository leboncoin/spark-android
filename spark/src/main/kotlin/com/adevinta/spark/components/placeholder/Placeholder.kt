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
@file:Suppress("ComposeModifierComposed") // These modifiers will be forked so the
// work will be done when it happens

package com.adevinta.spark.components.placeholder

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme

/**
 * Draws some skeleton UI which is typically used whilst content is 'loading'.
 *
 * To customize the shape of the placeholder, you can use the clip modifier after this one if it match your needs.
 *
 * A cross-fade transition will be applied to the content and placeholder UI when the [visible]
 * value changes.
 *
 * This placeholder display a fade animation.
 *
 * You can find more information on the pattern at the Material Theming
 * [Placeholder UI](https://material.io/design/communication/launch-screen.html#placeholder-ui)
 * guidelines.
 *
 * @param visible whether the placeholder should be visible or not.
 */
public fun Modifier.placeholder(visible: Boolean): Modifier = basePlaceholder(visible = visible)

/**
 * Draws some skeleton UI which is typically used whilst content is 'loading'.
 *
 * The shape is not customizable as it's meant to display the same style for each text placeholders.
 *
 * A cross-fade transition will be applied to the content and placeholder UI when the [visible]
 * value changes.
 *
 * This placeholder display a fade animation.
 *
 * You can find more information on the pattern at the Material Theming
 * [Placeholder UI](https://material.io/design/communication/launch-screen.html#placeholder-ui)
 * guidelines.
 *
 * @param visible whether the placeholder should be visible or not.
 */
public fun Modifier.textPlaceholder(visible: Boolean): Modifier = composed {
    basePlaceholder(
        visible = visible,
        shape = SparkTheme.shapes.full,
    )
}

/**
 * Draws a skeleton UI for illustrations which is typically used whilst a image is 'loading'.
 *
 * To customize the shape of the placeholder, you can use the shape parameter.
 *
 * A cross-fade transition will be applied to the content and placeholder UI when the [visible]
 * value changes.
 *
 * This placeholder display a shimmer animation.
 *
 * You can find more information on the pattern at the Material Theming
 * [Placeholder UI](https://material.io/design/communication/launch-screen.html#placeholder-ui)
 * guidelines.
 *
 * @param visible whether the placeholder should be visible or not.
 * @param shape desired shape of the placeholder. If null is provided the placeholder
 * will use the small shape set in [SparkTheme.shapes].
 */
public fun Modifier.illustrationPlaceholder(
    visible: Boolean,
    shape: Shape,
): Modifier = composed {
    basePlaceholder(
        visible = visible,
        highlight = PlaceholderHighlight.shimmer(),
        shape = shape,
    )
}

@PreviewLightDark
@Composable
internal fun PreviewPlaceHolder() {
    PreviewTheme {
        Column(verticalArrangement = spacedBy(4.dp)) {
            Text("Text Placeholder")
            Text("Text Placeholder", modifier = Modifier.textPlaceholder(true))
            Text("Text Placeholder with longer text", modifier = Modifier.textPlaceholder(true))
            Text("Text short", modifier = Modifier.textPlaceholder(true))
        }
        Column(verticalArrangement = spacedBy(4.dp)) {
            Text("Default Placeholder")
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .placeholder(true),
            )
        }
        Column(verticalArrangement = spacedBy(4.dp)) {
            Text("Illustration Placeholder")
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .illustrationPlaceholder(visible = true, shape = SparkTheme.shapes.extraLarge),
            )
        }
    }
}
