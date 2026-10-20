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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarAddonItem
import com.adevinta.spark.components.image.AvatarAddonScope
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.Company
import com.adevinta.spark.components.image.User
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import org.junit.Rule
import org.junit.Test

internal class AvatarScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun avatarMatrix() {
        paparazzi.sparkSnapshot { AvatarMatrix() }
        paparazzi.sparkSnapshot(name = "dark", isDark = true) { AvatarMatrix() }
    }

    @Test
    fun avatarAddonPlacement() {
        val overflowingAddon: @Composable AvatarAddonScope.(AvatarAddonItem) -> Unit = {
            custom {
                Box(
                    Modifier
                        .size(20.dp)
                        .background(SparkTheme.colors.error, CircleShape),
                )
            }
        }
        paparazzi.sparkSnapshot {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AvatarSize.entries.forEach { size ->
                        Avatar.User(username = "Spark", style = size, addon = overflowingAddon)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AvatarSize.entries.forEach { size ->
                        Avatar.Company(style = size, addon = overflowingAddon)
                    }
                }
            }
        }
    }

    @Composable
    private fun AvatarMatrix() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Avatar",
                style = SparkTheme.typography.headline1,
            )

            listOf(false, true).forEach { isCompany ->
                listOf(false, true).forEach { isOnline ->
                    Text(
                        text = "${if (isCompany) "Company" else "User"} " +
                            if (isOnline) "Online" else "Offline",
                        style = SparkTheme.typography.headline2,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        AvatarSize.entries.forEach { size ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                if (isCompany) {
                                    Avatar.Company(
                                        style = size,
                                        addon = { if (isOnline) onlineIndicator() },
                                    )
                                } else {
                                    Avatar.User(
                                        username = "Spark",
                                        style = size,
                                        addon = { if (isOnline) onlineIndicator() },
                                    )
                                }
                                Text(
                                    text = size.name,
                                    style = SparkTheme.typography.caption,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
