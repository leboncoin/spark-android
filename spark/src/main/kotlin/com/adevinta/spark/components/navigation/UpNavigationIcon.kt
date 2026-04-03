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
package com.adevinta.spark.components.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.adevinta.spark.R
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.icons.ArrowLeft
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay

@SuppressLint("MaterialComposableHasSparkReplacement") // We're wrapping the material component
@Composable
public fun UpNavigationIcon(
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(id = R.string.spark_back_arrow_content_description),
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier.sparkUsageOverlay(),
        onClick = onClick,
    ) {
        Icon(
            size = IconSize.Medium,
            sparkIcon = SparkIcons.ArrowLeft,
            contentDescription = contentDescription,
        )
    }
}
