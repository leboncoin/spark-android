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
package com.adevinta.spark.catalog.model

import androidx.annotation.StringRes
import com.adevinta.spark.catalog.R
import com.adevinta.spark.icons.Color
import com.adevinta.spark.icons.Digicode
import com.adevinta.spark.icons.Jewels
import com.adevinta.spark.icons.LinensType
import com.adevinta.spark.icons.Metro
import com.adevinta.spark.icons.Pieces
import com.adevinta.spark.icons.Size
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.SpeedIndicator

public enum class Category(
    @StringRes internal val label: Int,
    internal val icon: SparkIcon,
    internal val displayOrder: Int
) {
    Foundation(R.string.category_foundation, SparkIcons.Color, 0),
    Actions(R.string.category_actions, SparkIcons.Digicode, 1),
    DataInputs(R.string.category_datainputs, SparkIcons.Size, 2),
    Indicators(R.string.category_indicators, SparkIcons.SpeedIndicator, 3),
    Layout(R.string.category_layout, SparkIcons.Pieces, 4),
    Navigation(R.string.category_navigation, SparkIcons.Metro, 5),
    Overlays(R.string.category_overlays, SparkIcons.LinensType, 6),
    VisualAssets(R.string.category_visualassets, SparkIcons.Jewels, 7),
}
