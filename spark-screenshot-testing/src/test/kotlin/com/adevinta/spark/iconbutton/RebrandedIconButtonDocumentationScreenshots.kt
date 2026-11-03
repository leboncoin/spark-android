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
package com.adevinta.spark.iconbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.iconbuttons.Ai
import com.adevinta.spark.components.iconbuttons.Boost
import com.adevinta.spark.components.iconbuttons.Contrast
import com.adevinta.spark.components.iconbuttons.Danger
import com.adevinta.spark.components.iconbuttons.Ghost
import com.adevinta.spark.components.iconbuttons.IconButton
import com.adevinta.spark.components.iconbuttons.Primary
import com.adevinta.spark.components.iconbuttons.Secondary
import com.adevinta.spark.components.iconbuttons.Success
import com.adevinta.spark.components.iconbuttons.Tertiary
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class RebrandedIconButtonDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun iconButtonPrimary() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Primary(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Primary(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonSecondary() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Secondary(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Secondary(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonTertiary() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Tertiary(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Tertiary(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonBoost() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Boost(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Boost(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonAi() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Ai(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Ai(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonDanger() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Danger(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Danger(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonSuccess() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Success(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Success(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonContrast() = paparazzi.sparkDocSnapshot(
        color = { SparkTheme.colors.backgroundVariant },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Contrast(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Contrast(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonGhost() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton.Ghost(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = true,
            )
            IconButton.Ghost(
                icon = LeboncoinIcons.UserOutline,
                onClick = {},
                contentDescription = "Profile",
                enabled = false,
            )
        }
    }
}
