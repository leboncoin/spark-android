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
package com.adevinta.spark.tokens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.spacer.HorizontalSpacer
import com.adevinta.spark.icons.IdentityOutline
import com.adevinta.spark.icons.SparkIcons

/**
 * This function is used to set the current value of [LocalContentColor] to onSurface. Any [Text], Icons, or other
 * components included will be styled with this content color alpha that applies a medium emphasis which
 * is close to Black.
 *
 * This is the default emphasis applied in the whole theme.
 */
@Composable
public fun EmphasizeHigh(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides SparkTheme.colors.onSurface, content = content)
}

/**
 * This function is used to set the current value of [LocalContentColor] to onSurfaceVariant. Any [Text], Icons, or other
 * components included in this component's [content] will be styled with this content color alpha that applies a
 * medium emphasis which is close to Dark Gray.
 */
@Composable
public fun EmphasizeMedium(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides SparkTheme.colors.onSurface.dim1,
        content = content,
    )
}

/**
 * This function is used to set the current value of [LocalContentColor] to onSurface with a disabled alpha.
 * Any [Text], Icons, or other components included in this component's [content] will be styled with this content
 * color alpha that applies a disabled emphasis which is close to Gray or Light Gray.
 */
@Composable
public fun EmphasizeDisable(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides SparkTheme.colors.onSurface.dim3,
        content = content,
    )
}

public const val DisabledAlpha: Float = 0.38f

/**
 * Medium emphasis for text.
 */
@Composable
public fun EmphasizeDim1(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.dim1,
        content = content,
    )
}

/**
 * Medium emphasis for icons.
 */
@Composable
public fun EmphasizeDim2(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.dim2,
        content = content,
    )
}

/**
 * Medium emphasis.
 */
@Composable
public fun EmphasizeDim3(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.dim3,
        content = content,
    )
}

/**
 * To represent low elements.
 */
@Composable
public fun EmphasizeDim4(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.dim4,
        content = content,
    )
}

/**
 * Used for press and ripples.
 *
 * This should not be used on Android as the Material Ripple handles this already.
 */
@InternalSparkApi
@Composable
public fun EmphasizeDim5(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.dim5,
        content = content,
    )
}

@Composable
@Preview(
    group = "Tokens",
    name = "Emphasis",
)
internal fun EmphasePreview() {
    PreviewTheme {
        Text("Uses default alpha")
        EmphasizeHigh {
            Text("This Text uses the high value")
        }
        EmphasizeMedium {
            Text("Medium value provided for LocalContentAlpha")
            Text("This Text also uses the medium value")
            EmphasizeDisable {
                Text("This Text uses the disabled alpha now")
            }
        }
    }
}

@Composable
@Preview(
    group = "Tokens",
    name = "Dim",
)
internal fun DimPreview() {
    PreviewTheme {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(24.dp),
                sparkIcon = SparkIcons.IdentityOutline,
                contentDescription = "Favorite",
            )
            HorizontalSpacer(8.dp)
            Text("This Text uses the default alpha")
        }
        EmphasizeDim1 {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    sparkIcon = SparkIcons.IdentityOutline,
                    contentDescription = "Favorite",
                )
                HorizontalSpacer(8.dp)
                Text("This Text uses the Dim 1")
            }
        }
        EmphasizeDim2 {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    sparkIcon = SparkIcons.IdentityOutline,
                    contentDescription = "Favorite",
                )
                HorizontalSpacer(8.dp)
                Text("This Text uses the Dim 2")
            }
        }
        EmphasizeDim3 {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    sparkIcon = SparkIcons.IdentityOutline,
                    contentDescription = "Favorite",
                )
                HorizontalSpacer(8.dp)
                Text("This Text uses the Dim 3")
            }
        }
        EmphasizeDim4 {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    sparkIcon = SparkIcons.IdentityOutline,
                    contentDescription = "Favorite",
                )
                HorizontalSpacer(8.dp)
                Text("This Text uses the Dim 4")
            }
        }
        EmphasizeDim5 {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    sparkIcon = SparkIcons.IdentityOutline,
                    contentDescription = "Favorite",
                )
                HorizontalSpacer(8.dp)
                Text("This Text uses the Dim 5")
            }
        }
    }
}
