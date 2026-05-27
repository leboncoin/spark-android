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
package com.adevinta.spark.segmentedcontrol

import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlShape
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class SegmentedControlDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun horizontal() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 1) {
            singleLine("All", selected = false, onClick = {})
            singleLine("Active", selected = true, onClick = {})
            singleLine("Done", selected = false, onClick = {})
        }
    }

    @Test
    fun horizontalContentTypes() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 0) {
            singleLine("Text", selected = true, onClick = {})
            twoLine("Title", "Subtitle", selected = false, onClick = {})
            icon(LeboncoinIcons.ShoppingCartOutline, selected = false, onClick = {})
            iconText(LeboncoinIcons.ShoppingCartOutline, "Cart", selected = false, onClick = {})
        }
    }

    @Test
    fun verticalRounded() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Vertical(selectedIndex = 2, shape = SegmentedControlShape.Rounded) {
            singleLine("Option 1", selected = false, onClick = {})
            singleLine("Option 2", selected = false, onClick = {})
            singleLine("Option 3", selected = true, onClick = {})
            singleLine("Option 4", selected = false, onClick = {})
            singleLine("Option 5", selected = false, onClick = {})
        }
    }

    @Test
    fun verticalPill() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Vertical(selectedIndex = 2, shape = SegmentedControlShape.Pill) {
            singleLine("Option 1", selected = false, onClick = {})
            singleLine("Option 2", selected = false, onClick = {})
            singleLine("Option 3", selected = true, onClick = {})
            singleLine("Option 4", selected = false, onClick = {})
            singleLine("Option 5", selected = false, onClick = {})
        }
    }
}