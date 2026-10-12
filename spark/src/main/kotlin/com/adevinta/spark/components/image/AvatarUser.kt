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
package com.adevinta.spark.components.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.highlight

@InternalSparkApi
@Composable
internal fun SparkAvatarUser(
    username: String,
    modifier: Modifier = Modifier,
    // Useful to preview different states
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    style: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    val letter = usernameInitial(username)
    SparkAvatar(
        shape = SparkTheme.shapes.full,
        addonPlacement = AvatarAddonPlacement.Rim,
        placeholder = {
            if (letter != null) {
                AvatarLetterState(letter = letter, style = style)
            } else {
                Surface(
                    color = SparkTheme.colors.neutral,
                    modifier = Modifier.fillMaxSize(),
                    content = {},
                )
            }
        },
        modifier = modifier,
        transform = transform,
        style = style,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        addon = addon,
    )
}

/**
 * A circular picture that identifies a user.
 *
 * When [model] is null or the image does not load, the avatar shows the first letter of [username]
 * on a neutral circle. Accessibility services announce the avatar as a profile photo, not the letter.
 *
 * ![Sizes](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_AvatarDocumentationScreenshots_sizes.png)
 *
 * ![Letter](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.image_AvatarDocumentationScreenshots_letter.png)
 *
 * @param username name of the user. The placeholder shows its first non-whitespace character in upper
 * case. A blank value shows the neutral circle with no letter.
 * @param modifier applied to the avatar
 * @param style avatar diameter, from [AvatarSize.XS] (24dp) to [AvatarSize.XXXL] (128dp), and the
 * matching online badge size. The default is [AvatarSize.SM] (32dp).
 * @param contentScale how the loaded image fits the avatar bounds. The default is [ContentScale.Fit].
 * @param fillParentSize when true, the avatar ignores [style] and fills the parent. Use it when a
 * fixed-size slot already sets the avatar size.
 * @param model image to load, for example the URL of the user photo. Null shows the letter placeholder.
 * @param addon optional slot on the circle rim at the bottom right. The addon stays at the bottom right
 * in a right-to-left layout. Call [AvatarAddonScope.onlineIndicator] for the standard presence dot, or
 * [AvatarAddonScope.custom] for other content. The default is empty.
 */
@Suppress("UnusedReceiverParameter")
@Composable
public fun Avatar.User(
    username: String,
    modifier: Modifier = Modifier,
    style: AvatarSize = AvatarSize.SM,
    contentScale: ContentScale = ContentScale.Fit,
    fillParentSize: Boolean = false,
    model: Any? = null,
    addon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {},
) {
    SparkAvatarUser(
        username = username,
        modifier = modifier,
        style = style,
        contentScale = contentScale,
        fillParentSize = fillParentSize,
        model = model,
        addon = addon,
    )
}

private fun usernameInitial(username: String): Char? = username.firstOrNull { !it.isWhitespace() }?.uppercaseChar()

@Composable
internal fun AvatarLetterState(letter: Char, style: AvatarSize) {
    val density = LocalDensity.current
    Surface(
        color = SparkTheme.colors.neutral,
        contentColor = SparkTheme.colors.onNeutral,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(contentAlignment = Alignment.Center) {
            // The letter is a graphic in a fixed-size circle, so font scaling would clip it.
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 1f)) {
                Text(
                    text = letter.toString(),
                    style = style.letterTextStyle,
                    color = SparkTheme.colors.onNeutral,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

internal val AvatarSize.letterTextStyle: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        AvatarSize.XS -> SparkTheme.typography.body2.highlight
        AvatarSize.SM -> SparkTheme.typography.headline2
        AvatarSize.MD -> SparkTheme.typography.display3
        AvatarSize.LG -> SparkTheme.typography.display2
        AvatarSize.XL -> SparkTheme.typography.display2
        AvatarSize.XXL -> SparkTheme.typography.display1
        AvatarSize.XXXL -> SparkTheme.typography.display1.copy(fontSize = 64.sp)
    }

@Preview(
    group = "Images",
    name = "Avatar User",
)
@Composable
private fun AvatarUserPreview() {
    PreviewTheme {
        AvatarSize.entries.forEach { size ->
            SparkAvatarUser(
                username = "spark",
                style = size,
                model = "",
                transform = { AsyncImagePainter.State.Empty },
                addon = { onlineIndicator() },
            )
        }
        SparkAvatarUser(
            username = "",
            style = AvatarSize.XL,
            model = "",
            transform = { AsyncImagePainter.State.Empty },
        )
    }
}
