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
package com.adevinta.spark.catalog.themes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.highlight

@Composable
internal fun SegmentedButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = SparkTheme.colors.onMainVariant,
    unSelectedColor: Color = LocalContentColor.current,
) {
    require(options.size >= 2) { "This composable requires at least 2 options" }
    require(options.size <= 5) { "This composable requires at most 5 options" }
    require(options.contains(selectedOption)) { "Invalid selected option [$selectedOption]" }

    val selectedIndex by remember(options, selectedOption) { mutableIntStateOf(options.indexOf(selectedOption)) }

    val backgroundProgress by animateFloatAsState(
        label = "Background Position Progress",
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
        ),
        targetValue = selectedIndex.toFloat(),
    )
    Layout(
        modifier = modifier
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .clip(
                shape = SparkTheme.shapes.full,
            )
            .selectableGroup()
            .border(
                width = 1.dp,
                color = SparkTheme.colors.outlineHigh,
                shape = SparkTheme.shapes.full,
            ),
        content = {
            options.forEachIndexed { index, option ->
                val selected = index == selectedIndex
                val textColor by animateColorAsState(
                    label = "Button text color",
                    targetValue = if (selected) selectedColor else unSelectedColor,
                )

                val shape = RoundedCornerShape(
                    topStartPercent = if (0 == index) 50 else 15,
                    bottomStartPercent = if (0 == index) 50 else 15,
                    topEndPercent = if (options.lastIndex == index) 50 else 15,
                    bottomEndPercent = if (options.lastIndex == index) 50 else 15,
                )

                Box(
                    modifier = Modifier
                        .layoutId(MultiSelectorOption.Option)
                        .clip(shape)
                        .selectable(selected) { onOptionSelect(option) }
                        .semantics { role = Role.RadioButton },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option,
                        color = textColor,
                        maxLines = 1,
                        style = SparkTheme.typography.body2.highlight,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                }
            }

            val startCornerShape by animateIntAsState(
                label = "Selector start shape",
                targetValue = if (options.first() == selectedOption) 50 else 15,
            )
            val endCornerShape by animateIntAsState(
                label = "Selector end shape",
                targetValue = if (options.last() == selectedOption) 50 else 15,
            )
            Box(
                modifier = Modifier
                    .layoutId(MultiSelectorOption.Background)
                    .clip(
                        shape = RoundedCornerShape(
                            topStartPercent = startCornerShape,
                            bottomStartPercent = startCornerShape,
                            topEndPercent = endCornerShape,
                            bottomEndPercent = endCornerShape,
                        ),
                    )
                    .background(SparkTheme.colors.mainVariant),
            )
        },
    ) { measurables, constraints ->
        val optionWidth = constraints.maxWidth / options.size
        val optionConstraints = Constraints.fixed(
            width = optionWidth,
            height = constraints.minHeight,
        )
        val optionPlaceables = measurables
            .filter { measurable -> measurable.layoutId == MultiSelectorOption.Option }
            .map { measurable -> measurable.measure(optionConstraints) }
        val backgroundPlaceable = measurables
            .first { measurable -> measurable.layoutId == MultiSelectorOption.Background }
            .measure(optionConstraints)
        layout(
            width = constraints.maxWidth,
            height = constraints.minHeight,
        ) {
            backgroundPlaceable.placeRelative(
                x = (backgroundProgress * optionWidth).toInt(),
                y = 0,
            )
            optionPlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = optionWidth * index,
                    y = 0,
                )
            }
        }
    }
}

private val animationSpec = spring<Int>()

@Stable
internal interface MultiSelectorState {
    val selectedIndex: Int
    fun selectOption(index: Int)
}

@Stable
internal class MultiSelectorStateImpl(options: List<String>, selectedOption: String) : MultiSelectorState {

    override val selectedIndex: Int
        get() = _selectedIndex

    private var _selectedIndex = options.indexOf(selectedOption)

    override fun selectOption(index: Int) {
        _selectedIndex = index
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MultiSelectorStateImpl

        return _selectedIndex == other._selectedIndex
    }

    override fun hashCode(): Int = _selectedIndex.hashCode()
}

@Composable
internal fun rememberMultiSelectorState(
    options: List<String>,
    selectedOption: String,
) = remember {
    MultiSelectorStateImpl(
        options,
        selectedOption,
    )
}

internal enum class MultiSelectorOption {
    Option,
    Background,
}

@PreviewLightDark
@Composable
private fun PreviewMultiSelector() {
    PreviewTheme {
        val options1 = listOf("Day", "Week", "Month")
        var selectedOption1 by remember {
            mutableStateOf(options1.first())
        }
        val options2 = listOf("Sit", "Amet", "Consectetur", "Elit", "Quis")
        var selectedOption2 by remember {
            mutableStateOf(options2.first())
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SegmentedButton(
                options = options1,
                selectedOption = selectedOption1,
                onOptionSelect = { option ->
                    selectedOption1 = option
                },
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
            )

            SegmentedButton(
                options = options2,
                selectedOption = selectedOption2,
                onOptionSelect = { option ->
                    selectedOption2 = option
                },
                modifier = Modifier
                    .padding(all = 16.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
            )
        }
    }
}
