/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.catalog.examples.samples.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.image.UserAvatar
import com.adevinta.spark.components.image.UserAvatarStyle
import com.adevinta.spark.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val UserAvatarExampleSourceUrl = "$SampleSourceUrl/image/UserAvatarExamples.kt"

public val UserAvatarExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "sizes",
        name = "Sizes",
        description = "User Avatar in SM, MD, and XL styles",
        sourceUrl = UserAvatarExampleSourceUrl,
    ) {
        UserAvatarSizes()
    },
    Example(
        id = "pro",
        name = "Regular vs Pro",
        description = "Regular avatar with a person icon and pro avatar with a building icon",
        sourceUrl = UserAvatarExampleSourceUrl,
    ) {
        UserAvatarPro()
    },
    Example(
        id = "online",
        name = "Online indicator",
        description = "Avatar with and without the online status dot",
        sourceUrl = UserAvatarExampleSourceUrl,
    ) {
        UserAvatarOnline()
    },
    Example(
        id = "placeholder",
        name = "Placeholder",
        description = "Avatar with no model shows a placeholder icon",
        sourceUrl = UserAvatarExampleSourceUrl,
    ) {
        UserAvatarPlaceholder()
    },
)

@Preview
@Composable
private fun ColumnScope.UserAvatarSizes() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("SM, MD, and XL sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UserAvatar(style = UserAvatarStyle.SM)
            UserAvatar(style = UserAvatarStyle.MD)
            UserAvatar(style = UserAvatarStyle.XL)
        }
    }
}

@Preview
@Composable
private fun ColumnScope.UserAvatarPro() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Person icon vs building icon")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UserAvatar(style = UserAvatarStyle.XL, isPro = false)
            UserAvatar(style = UserAvatarStyle.XL, isPro = true)
        }
    }
}

@Preview
@Composable
private fun ColumnScope.UserAvatarOnline() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Offline and online")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UserAvatar(style = UserAvatarStyle.XL, addon = {})
            UserAvatar(style = UserAvatarStyle.XL, addon = { onlineIndicator() })
        }
        Text("Online dot on all sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UserAvatar(style = UserAvatarStyle.SM, addon = { onlineIndicator() })
            UserAvatar(style = UserAvatarStyle.MD, addon = { onlineIndicator() })
            UserAvatar(style = UserAvatarStyle.XL, addon = { onlineIndicator() })
        }
    }
}

@Preview
@Composable
private fun ColumnScope.UserAvatarPlaceholder() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("No model, placeholder icon shown")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            UserAvatar(style = UserAvatarStyle.XL, model = null)
            UserAvatar(style = UserAvatarStyle.XL, model = null, isPro = true)
        }
    }
}
