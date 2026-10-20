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
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.Company
import com.adevinta.spark.components.image.User
import com.adevinta.spark.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val AvatarExampleSourceUrl = "$SampleSourceUrl/image/AvatarExamples.kt"

public val AvatarExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "sizes",
        name = "Sizes",
        description = "User and Company avatars in SM, MD, and XL sizes",
        sourceUrl = AvatarExampleSourceUrl,
    ) {
        AvatarSizes()
    },
    Example(
        id = "variants",
        name = "User vs Company",
        description = "Circular user avatar with a letter and rounded company avatar with a building icon",
        sourceUrl = AvatarExampleSourceUrl,
    ) {
        AvatarVariants()
    },
    Example(
        id = "online",
        name = "Online indicator",
        description = "Avatar with and without the online status dot",
        sourceUrl = AvatarExampleSourceUrl,
    ) {
        AvatarOnline()
    },
    Example(
        id = "letter",
        name = "Letter",
        description = "User avatar with no image shows the first letter of the user name",
        sourceUrl = AvatarExampleSourceUrl,
    ) {
        AvatarLetter()
    },
)

@Preview
@Composable
private fun ColumnScope.AvatarSizes() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("User in SM, MD, and XL sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.User(username = "Spark", style = AvatarSize.SM)
            Avatar.User(username = "Spark", style = AvatarSize.MD)
            Avatar.User(username = "Spark", style = AvatarSize.XL)
        }
        Text("Company in SM, MD, and XL sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.Company(style = AvatarSize.SM)
            Avatar.Company(style = AvatarSize.MD)
            Avatar.Company(style = AvatarSize.XL)
        }
    }
}

@Preview
@Composable
private fun ColumnScope.AvatarVariants() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("User and Company")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.User(username = "Spark", style = AvatarSize.XL)
            Avatar.Company(style = AvatarSize.XL)
        }
    }
}

@Preview
@Composable
private fun ColumnScope.AvatarOnline() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Offline and online")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.User(username = "Spark", style = AvatarSize.XL)
            Avatar.User(username = "Spark", style = AvatarSize.XL, addon = { onlineIndicator() })
            Avatar.Company(style = AvatarSize.XL)
            Avatar.Company(style = AvatarSize.XL, addon = { onlineIndicator() })
        }
        Text("Online dot on the rim of the User avatar")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.User(username = "Spark", style = AvatarSize.SM, addon = { onlineIndicator() })
            Avatar.User(username = "Spark", style = AvatarSize.MD, addon = { onlineIndicator() })
            Avatar.User(username = "Spark", style = AvatarSize.XL, addon = { onlineIndicator() })
        }
        Text("Online dot inside the bottom-end corner of the Company avatar")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.Company(style = AvatarSize.SM, addon = { onlineIndicator() })
            Avatar.Company(style = AvatarSize.MD, addon = { onlineIndicator() })
            Avatar.Company(style = AvatarSize.XL, addon = { onlineIndicator() })
        }
    }
}

@Preview
@Composable
private fun ColumnScope.AvatarLetter() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Letter on SM, MD, and XL sizes")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.User(username = "Spark", style = AvatarSize.SM)
            Avatar.User(username = "Spark", style = AvatarSize.MD)
            Avatar.User(username = "Spark", style = AvatarSize.XL)
        }
        Text("Blank user name shows no letter")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Avatar.User(username = "", style = AvatarSize.XL)
            Avatar.User(username = "Spark", style = AvatarSize.XL)
        }
    }
}
