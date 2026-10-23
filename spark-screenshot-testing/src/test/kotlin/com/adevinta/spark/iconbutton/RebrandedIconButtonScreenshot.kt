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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkFeatureFlagProvider
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.iconbuttons.Ai
import com.adevinta.spark.components.iconbuttons.Boost
import com.adevinta.spark.components.iconbuttons.Contrast
import com.adevinta.spark.components.iconbuttons.Danger
import com.adevinta.spark.components.iconbuttons.Ghost
import com.adevinta.spark.components.iconbuttons.IconButton
import com.adevinta.spark.components.iconbuttons.IconButtonContrast
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.components.iconbuttons.IconButtonOutlined
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.IconButtonTinted
import com.adevinta.spark.components.iconbuttons.Primary
import com.adevinta.spark.components.iconbuttons.Secondary
import com.adevinta.spark.components.iconbuttons.Success
import com.adevinta.spark.components.iconbuttons.Tertiary
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.UserOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test

internal class RebrandedIconButtonScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = V_SCROLL,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun allVariants() {
        paparazzi.sparkSnapshotNightMode {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButtonSize.entries.forEach { size ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButtonStates { enabled, isLoading ->
                            OnSurfaceVariants(size = size, enabled = enabled, isLoading = isLoading)
                        }
                        Surface(color = SparkTheme.colors.surfaceInverse) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                IconButtonStates { enabled, isLoading ->
                                    OnSurfaceInverseVariants(size = size, enabled = enabled, isLoading = isLoading)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Expected variant per intent and legacy style, from `ButtonStyleMapper.map`.
     * Columns: Filled, Outlined, Tinted, Ghost, Contrast.
     *
     * - Main: Primary, Tertiary, Tertiary, Ghost, Contrast
     * - Support: Secondary, Tertiary, Tertiary, Ghost, Contrast
     * - Accent: Boost, Tertiary, Tertiary, Ghost, Contrast
     * - Surface: Contrast, Contrast, Contrast, Ghost, Contrast
     * - Success: Success, Success, Success, Ghost, Contrast
     * - Danger: Danger, Danger, Danger, Ghost, Contrast
     * - Neutral: Tertiary, Tertiary, Tertiary, Ghost, Contrast (Info and Alert map the same way)
     * - Ai: Ai, Ai, Ai, Ai, Ai
     */
    private val legacyIntents = listOf(
        IconButtonIntent.Main,
        IconButtonIntent.Support,
        IconButtonIntent.Accent,
        IconButtonIntent.Surface,
        IconButtonIntent.Success,
        IconButtonIntent.Danger,
        IconButtonIntent.Neutral,
        IconButtonIntent.Ai,
    )

    @Test
    fun legacyIconButtonsWithFlag() {
        paparazzi.sparkSnapshotNightMode {
            SparkFeatureFlagProvider(useRebrandedButtons = true) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    legacyIntents.forEach { intent ->
                        LegacyIconButtons(intent = intent)
                    }
                }
            }
        }
    }

    @Composable
    private fun IconButtonStates(content: @Composable (enabled: Boolean, isLoading: Boolean) -> Unit) {
        content(true, false)
        content(false, false)
        content(true, true)
    }

    @Composable
    private fun OnSurfaceVariants(size: IconButtonSize, enabled: Boolean, isLoading: Boolean) {
        val icon = LeboncoinIcons.UserOutline
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton.Primary(
                icon = icon,
                onClick = {},
                contentDescription = "Primary",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Secondary(
                icon = icon,
                onClick = {},
                contentDescription = "Secondary",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Tertiary(
                icon = icon,
                onClick = {},
                contentDescription = "Tertiary",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Boost(
                icon = icon,
                onClick = {},
                contentDescription = "Boost",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Ai(
                icon = icon,
                onClick = {},
                contentDescription = "Ai",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Danger(
                icon = icon,
                onClick = {},
                contentDescription = "Danger",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Success(
                icon = icon,
                onClick = {},
                contentDescription = "Success",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    }

    @Composable
    private fun OnSurfaceInverseVariants(size: IconButtonSize, enabled: Boolean, isLoading: Boolean) {
        val icon = LeboncoinIcons.UserOutline
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton.Contrast(
                icon = icon,
                onClick = {},
                contentDescription = "Contrast",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
            IconButton.Ghost(
                icon = icon,
                onClick = {},
                contentDescription = "Ghost",
                size = size,
                enabled = enabled,
                isLoading = isLoading,
            )
        }
    }

    @Composable
    private fun LegacyIconButtons(intent: IconButtonIntent) {
        val icon = LeboncoinIcons.UserOutline
        val size = IconButtonSize.Medium
        // backgroundVariant keeps the Contrast fill (surface) and the Ghost content visible.
        Surface(color = SparkTheme.colors.backgroundVariant) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                IconButtonFilled(icon = icon, onClick = {}, intent = intent, size = size)
                IconButtonOutlined(icon = icon, onClick = {}, intent = intent, size = size)
                IconButtonTinted(icon = icon, onClick = {}, intent = intent, size = size)
                IconButtonGhost(icon = icon, onClick = {}, intent = intent, size = size)
                IconButtonContrast(icon = icon, onClick = {}, intent = intent, size = size)
            }
        }
    }
}
