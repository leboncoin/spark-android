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
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonContrast
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonFilled
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonGhost
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonOutlined
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonTinted
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.UserFill
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSparkApi::class)
internal class IconToggleButtonDocumentationScreenshots {

    private val icons = IconToggleButtonIcons(
        checked = LeboncoinIcons.UserOutline,
        unchecked = LeboncoinIcons.UserFill,
    )

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun iconToggleButtonFilled() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconToggleButtonFilled(
                checked = true,
                onCheckedChange = {},
                icons = icons,
            )
            IconToggleButtonFilled(
                checked = false,
                onCheckedChange = {},
                icons = icons,
            )
        }
    }

    @Test
    fun iconToggleButtonOutlined() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconToggleButtonOutlined(
                checked = true,
                onCheckedChange = {},
                icons = icons,
            )
            IconToggleButtonOutlined(
                checked = false,
                onCheckedChange = {},
                icons = icons,
            )
        }
    }

    @Test
    fun iconToggleButtonTinted() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconToggleButtonTinted(
                checked = true,
                onCheckedChange = {},
                icons = icons,
            )
            IconToggleButtonTinted(
                checked = false,
                onCheckedChange = {},
                icons = icons,
            )
        }
    }

    @Test
    fun iconToggleButtonContrast() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconToggleButtonContrast(
                checked = true,
                onCheckedChange = {},
                icons = icons,
            )
            IconToggleButtonContrast(
                checked = false,
                onCheckedChange = {},
                icons = icons,
            )
        }
    }

    @Test
    fun iconToggleButtonGhost() = paparazzi.sparkDocSnapshot {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconToggleButtonGhost(
                checked = true,
                onCheckedChange = {},
                icons = icons,
            )
            IconToggleButtonGhost(
                checked = false,
                onCheckedChange = {},
                icons = icons,
            )
        }
    }
}
