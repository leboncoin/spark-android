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
package com.adevinta.spark.components.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.BellFill
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

/**
 * Documentation screenshots for Badge component, following Material Design's approach of showing
 * each variant with light and dark theme side by side.
 */
internal class BadgeDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun badgeCount() = paparazzi.sparkDocSnapshot {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Badge(count = 1)
            Badge(count = 42)
            Badge(count = 999, overflowCount = 99)
        }
    }

    @Test
    fun badgeDot() = paparazzi.sparkDocSnapshot {
        Badge(content = null)
    }

    @Test
    fun badgeOverIcon() = paparazzi.sparkDocSnapshot {
        BadgedBox(
            badge = { Badge(count = 5) },
        ) {
            Icon(
                sparkIcon = LeboncoinIcons.BellFill,
                contentDescription = null,
            )
        }
    }
}
