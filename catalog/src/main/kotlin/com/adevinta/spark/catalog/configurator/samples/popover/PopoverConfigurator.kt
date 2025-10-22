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
package com.adevinta.spark.catalog.configurator.samples.popover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.model.Configurator
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.ui.DropdownEnum
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.iconbuttons.IconButtonFilled
import com.adevinta.spark.components.image.Illustration
import com.adevinta.spark.components.image.Image
import com.adevinta.spark.components.popover.Popover
import com.adevinta.spark.components.popover.PopoverIntent
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.text.TextLinkButton
import com.adevinta.spark.components.toggles.SwitchLabelled
import com.adevinta.spark.icons.BurgerMenu
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.StoreCriteria
import com.adevinta.spark.tokens.highlight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public val PopoverConfigurator: Configurator = Configurator(
    id = "popover",
    name = "Popover",
    description = "Popover configuration",
    sourceUrl = "$SampleSourceUrl/PopoverSamples.kt",
) {
    PopoverSample()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.PopoverSample() {
    var isDismissButtonEnabled by remember { mutableStateOf(false) }
    var popoverContentExample by remember { mutableStateOf(PopoverContentExamples.TextList) }
    var popoverTriggerExample by remember { mutableStateOf(PopoverTriggerExamples.Button) }
    var intent by remember { mutableStateOf(PopoverIntent.Surface) }
    val popoverState = rememberTooltipState(isPersistent = false)
    val scope = rememberCoroutineScope()

    ConfiguredPopover(scope, intent, popoverState, isDismissButtonEnabled, popoverContentExample, popoverTriggerExample)

    VerticalSpacer(40.dp)

    SwitchLabelled(
        checked = isDismissButtonEnabled,
        onCheckedChange = {
            isDismissButtonEnabled = it
        },
    ) {
        Text(
            text = "Show dismiss icon",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Popover Content Example",
        selectedOption = popoverContentExample,
        onOptionSelect = {
            popoverContentExample = it
        },
    )
    ButtonGroup(
        title = "Popover Anchor",
        selectedOption = popoverTriggerExample,
        onOptionSelect = { popoverTriggerExample = it },
    )

    DropdownEnum(
        modifier = Modifier.fillMaxWidth(),
        title = "Popover Intent",
        selectedOption = intent,
        onOptionSelect = {
            intent = it
        },
    )
}

@Preview
@Composable
private fun PopOverSamplePreview() {
    PreviewTheme { PopoverSample() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfiguredPopover(
    scope: CoroutineScope,
    intent: PopoverIntent,
    popoverState: TooltipState,
    isDismissButtonEnabled: Boolean,
    popoverContentExample: PopoverContentExamples,
    popoverTriggerExample: PopoverTriggerExamples,
) {
    Popover(
        popoverState = popoverState,
        intent = intent,
        popover = {
            when (popoverContentExample) {
                PopoverContentExamples.TextList -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(5) { index ->
                        Text(text = "Text: $index")
                    }
                }

                PopoverContentExamples.Text -> Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Title",
                        style = SparkTheme.typography.headline1.highlight,
                    )
                    Text(
                        text = "Do you want to have this cookie now?",
                        style = SparkTheme.typography.body2.highlight,
                    )
                    TextLinkButton(
                        text = "Text Link",
                        onClick = {},
                    )
                }

                PopoverContentExamples.Image -> Image(
                    contentScale = ContentScale.Inside,
                    model = "https://images.unsplash.com/photo-1606041008023-472dfb5e530f",
                    contentDescription = null,
                )

                PopoverContentExamples.Illustration -> Illustration(
                    sparkIcon = SparkIcons.StoreCriteria,
                    contentDescription = null,

                    modifier = Modifier.size(100.dp),
                )
            }
        },
        isDismissButtonEnabled = isDismissButtonEnabled,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            when (popoverTriggerExample) {
                PopoverTriggerExamples.Button -> {
                    ButtonOutlined(
                        text = "Display Popover",
                        onClick = { scope.launch { popoverState.show() } },
                    )
                }

                PopoverTriggerExamples.Icon -> {
                    IconButtonFilled(
                        onClick = { scope.launch { popoverState.show() } },
                        icon = SparkIcons.BurgerMenu,
                        contentDescription = "Burger Menu",
                    )
                }
            }
        }
    }
}

private enum class PopoverContentExamples {
    TextList,
    Image,
    Illustration,
    Text,
}

private enum class PopoverTriggerExamples {
    Button,
    Icon,
}
