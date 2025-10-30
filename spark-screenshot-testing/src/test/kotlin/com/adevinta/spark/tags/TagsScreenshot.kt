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
package com.adevinta.spark.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.tags.TagFilled
import com.adevinta.spark.components.tags.TagHighlight
import com.adevinta.spark.components.tags.TagHighlightBadge
import com.adevinta.spark.components.tags.TagIntent
import com.adevinta.spark.components.tags.TagOutlined
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.FireFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.adevinta.spark.tags.TagsScreenshot.Style.Filled
import com.adevinta.spark.tags.TagsScreenshot.Style.Outlined
import com.adevinta.spark.tags.TagsScreenshot.Style.Tinted
import com.android.ide.common.rendering.api.SessionParams
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.V_SCROLL
import org.junit.Rule
import org.junit.Test
import java.util.Locale

internal class TagsScreenshot {

    private val values: List<String> = listOf(stubBody, stubShortBody, "")
    private val icon: List<SparkIcon?> = listOf(SparkIcons.FireFill, null)

    enum class Style {
        Tinted,
        Filled,
        Outlined,
    }

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = V_SCROLL,
    )

    @Test
    fun themesTags() {
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Tags()
            }
        }
    }

    @Test
    fun tagHighlight() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DefaultTestDevices.Phone.copy(
                locale = Locale.FRENCH.toLanguageTag(),
            ),
            renderingMode = SessionParams.RenderingMode.SHRINK,
        )
        paparazzi.sparkSnapshotNightMode {
            Surface(
                color = SparkTheme.colors.backgroundVariant,
            ) {
                TagHighlightPreview()
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun Tags() {
        FlowRow(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Center,
        ) {
            TagIntent.entries.forEach { intent ->
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = intent.name,
                            style = SparkTheme.typography.headline1,
                        )
                        Style.entries.forEach { style ->
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                values.forEach { value ->
                                    for (icon in icon) {
                                        // Invalid state to be skipped
                                        if (value.isBlank() && icon == null) continue
                                        Row {
                                            when (style) {
                                                Filled -> TagFilled(
                                                    text = value,
                                                    intent = intent,
                                                    leadingIcon = icon,
                                                )

                                                Outlined -> TagTinted(
                                                    text = value,
                                                    intent = intent,
                                                    leadingIcon = icon,
                                                )

                                                Tinted -> TagOutlined(
                                                    text = value,
                                                    intent = intent,
                                                    leadingIcon = icon,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalSparkApi::class)
    @Composable
    private fun TagHighlightPreview() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "TagHighlight",
                style = SparkTheme.typography.headline2,
            )
            TagHighlight()

            Text(
                text = "TagHighlightBadge",
                style = SparkTheme.typography.headline2,
            )
            TagHighlightBadge()
        }
    }

    companion object {
        private const val stubShortBody = "Lorem"
        private const val stubBody = "Lorem ipsum dolor sit"
    }
}
