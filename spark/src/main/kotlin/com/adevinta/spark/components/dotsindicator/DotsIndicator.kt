/*
 * Copyright (c) 2024 Adevinta
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

package com.adevinta.spark.components.dotsindicator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.dotsindicator.Dot
import com.adevinta.spark.components.dotsindicator.DotsIndicatorIntent
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.dim3
import com.adevinta.spark.tokens.disabled

public object DotsIndicatorDefaults {
    public val size: DotsIndicatorSize = DotsIndicatorSize.Medium
    public val intent: DotsIndicatorIntent = DotsIndicatorIntent.Basic
}

public enum class DotsIndicatorSize(public val radius: Dp, public val selectedDotWidth: Dp) {
    Small(4.dp, 16.dp),
    Medium(8.dp, 32.dp),
    Large(16.dp, 64.dp),
}

@Composable
public fun DotsIndicator(
    numberOfDots: Int,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    selectedIndex: Int = 0,
    intent: DotsIndicatorIntent = DotsIndicatorIntent.Main,
    size: DotsIndicatorSize = DotsIndicatorDefaults.size,
) {

    // TODO needs to handle the correct colors for Disabled state
    val activeColor = if (isEnabled) intent.colors().color else intent.colors().color.dim3
    val inactiveColor = if (isEnabled) SparkTheme.colors.onSurface.dim3 else SparkTheme.colors.onSurface.dim3.disabled
    val radius = size.radius
    val selectedDotWidth = size.selectedDotWidth
    val animationDurationInMillis = 250
    val spacing = size.radius

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier,
    ) {
        for (i in 0 until numberOfDots) {
            val isSelected = i == selectedIndex
            Dot(
                isSelected = isSelected,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                radius = radius,
                selectedDotWidth = selectedDotWidth,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
@Preview()
private fun ScreenWithDotsIndicator() {
    val selectedIndex = remember { mutableIntStateOf(0) }
    val totalDots = remember { mutableIntStateOf(7) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            val dotConfigurations = listOf(
                Triple(false, DotsIndicatorSize.Large, DotsIndicatorIntent.Success),
                Triple(true, DotsIndicatorSize.Large, DotsIndicatorIntent.Accent),
                Triple(true, DotsIndicatorDefaults.size, DotsIndicatorIntent.Alert),
                Triple(true, DotsIndicatorSize.Small, DotsIndicatorIntent.Error),
                Triple(false, DotsIndicatorDefaults.size, DotsIndicatorIntent.Success),
            )

            dotConfigurations.forEach { config ->
                DotsIndicator(
                    isEnabled = config.first,
                    size = config.second,
                    intent = config.third,
                    numberOfDots = totalDots.intValue,
                    selectedIndex = selectedIndex.intValue,
                )
            }
            Spacer(modifier = Modifier.size(16.dp))

            Row {
                ButtonFilled(
                    intent = ButtonIntent.Accent,
                    onClick = { if (selectedIndex.intValue > 0) selectedIndex.intValue-- },
                    content = { Text(text = "Previous") },
                    modifier = Modifier.padding(end = 8.dp),
                )
                ButtonFilled(
                    intent = ButtonIntent.Accent,
                    onClick = { if (selectedIndex.intValue < totalDots.intValue - 1) selectedIndex.intValue++ },
                    content = { Text(text = "Next") },
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}
