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
package com.adevinta.spark.catalog.examples.samples.popover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.popover.Popover
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.highlight
import kotlinx.coroutines.launch

private const val PopoverExampleDescription = "Popover examples"
private const val PopoverExampleSourceUrl = "$SampleSourceUrl/PopoverSamples.kt"

@OptIn(ExperimentalMaterial3Api::class)
public val PopoverExamples: List<Example> = listOf(
    Example(
        id = "default",
        name = "Popover",
        description = PopoverExampleDescription,
        sourceUrl = PopoverExampleSourceUrl,
    ) {
        PopoverSample(
            button = { _, _, _, _ ->
                val state = rememberTooltipState(isPersistent = true)
                val scope = rememberCoroutineScope()

                Popover(
                    popover = {
                        Column {
                            Text(
                                text = "Title",
                                modifier = Modifier.padding(bottom = 16.dp),
                                style = SparkTheme.typography.headline1.highlight,
                            )
                            Text(
                                text = "Do you want to have this cookie now?",
                                modifier = Modifier.padding(bottom = 16.dp),
                                style = SparkTheme.typography.body2.highlight,
                            )
                            Text(
                                text = "Text Link",
                                textDecoration = TextDecoration.Underline,
                                style = SparkTheme.typography.body1.highlight
                                    .copy(color = SparkTheme.colors.accent),
                            )
                        }
                    },
                    isDismissButtonEnabled = true,
                    popoverState = state,
                ) {
                    ButtonOutlined(
                        text = "Display Popover",
                        onClick = { scope.launch { state.show() } },
                    )
                }
            },
        )
    },
)

@Composable
private fun PopoverSample(
    button: @Composable (
        onClick: () -> Unit,
        enabled: Boolean,
        icon: SparkIcon,
        contentDescription: String?,
    ) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val icon = SparkIcons.HeartFill
        val contentDescription = "Localized Content Description"
        button(
            /* onClick = */ { },
            /* enabled = */ true,
            /* icon = */ icon,
            /* contentDescription = */ contentDescription,
        )
        button(
            /* onClick = */ { },
            /* enabled = */ false,
            /* icon = */ icon,
            /* contentDescription = */ contentDescription,
        )
    }
}
