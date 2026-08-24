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
package com.adevinta.spark.iconbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.iconbuttons.IconButtonContrast
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.iconbuttons.IconButtonOutlined
import com.adevinta.spark.components.iconbuttons.IconButtonTinted
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class IconButtonDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun iconButtonFilled() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonFilled(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = true,
            )
            IconButtonFilled(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonOutlined() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonOutlined(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = true,
            )
            IconButtonOutlined(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonTinted() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonTinted(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = true,
            )
            IconButtonTinted(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonContrast() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonContrast(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = true,
            )
            IconButtonContrast(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = false,
            )
        }
    }

    @Test
    fun iconButtonGhost() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButtonGhost(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = true,
            )
            IconButtonGhost(
                onClick = {},
                icon = LeboncoinIcons.UserOutline,
                enabled = false,
            )
        }
    }
}
