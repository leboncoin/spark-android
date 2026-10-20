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
package com.adevinta.spark.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.Company
import com.adevinta.spark.components.image.User
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class AvatarDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun sizes() = paparazzi.sparkDocSnapshot {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar.User(username = "Spark", style = AvatarSize.SM)
            Avatar.User(username = "Spark", style = AvatarSize.MD)
            Avatar.User(username = "Spark", style = AvatarSize.XL)
        }
    }

    @Test
    fun variants() = paparazzi.sparkDocSnapshot {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar.User(username = "Spark", style = AvatarSize.XL)
            Avatar.Company(style = AvatarSize.XL)
        }
    }

    @Test
    fun letter() = paparazzi.sparkDocSnapshot {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar.User(username = "", style = AvatarSize.XL)
            Avatar.User(username = "Spark", style = AvatarSize.XL)
        }
    }

    @Test
    fun onlineIndicator() = paparazzi.sparkDocSnapshot {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar.User(username = "Spark", style = AvatarSize.XL, addon = { onlineIndicator() })
            Avatar.Company(style = AvatarSize.XL, addon = { onlineIndicator() })
        }
    }

    @Test
    fun addonBadge() = paparazzi.sparkDocSnapshot {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar.User(
                username = "Spark",
                style = AvatarSize.XL,
                addon = {
                    custom {
                        Box(
                            Modifier
                                .size(20.dp)
                                .background(SparkTheme.colors.error, CircleShape),
                        )
                    }
                },
            )
            Avatar.Company(
                style = AvatarSize.XL,
                addon = {
                    custom {
                        Box(
                            Modifier
                                .size(20.dp)
                                .background(SparkTheme.colors.error, CircleShape),
                        )
                    }
                },
            )
        }
    }
}
