/*
 * Copyright (c) 2023-2026 Adevinta
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
@file:Suppress("DEPRECATION")

package com.adevinta.spark.components.toggles

import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.IntentColors
import com.adevinta.spark.tokens.disabled

@Composable
internal fun IntentColors.toCheckboxDefaultsColors(checked: Boolean): CheckboxColors = with(this.colors()) {
    CheckboxDefaults.colors(
        checkedColor = this.color,
        checkmarkColor = this.onColor,
        uncheckedColor = if (this@toCheckboxDefaultsColors == IntentColors.Danger) this.color else UncheckedColor,
        disabledCheckedColor = this.color.disabled,
        disabledUncheckedColor = UncheckedColor.disabled,
    )
}

@Composable
internal fun IntentColors.toSwitchDefaultsColors(): SwitchColors = with(this.colors()) {
    SwitchDefaults.colors(
        checkedTrackColor = this.color,
    )
}

@Composable
internal fun IntentColors.toRadioButtonDefaultsColors(): RadioButtonColors = with(this.colors()) {
    RadioButtonDefaults.colors(
        selectedColor = this.color,
        unselectedColor = if (this@toRadioButtonDefaultsColors == IntentColors.Danger) this.color else UncheckedColor,
        disabledSelectedColor = this.color.disabled,
        disabledUnselectedColor = SparkTheme.colors.outline.disabled,
    )
}

private val UncheckedColor
    @Composable get() = SparkTheme.colors.outline
