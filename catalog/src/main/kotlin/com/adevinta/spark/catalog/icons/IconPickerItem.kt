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
package com.adevinta.spark.catalog.icons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ArrowRight
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.dim3

@ExperimentalSparkApi
@Composable
public fun IconPickerItem(
    label: String,
    selectedIcon: SparkIcon?,
    onIconSelected: (SparkIcon?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPicker by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = { showPicker = true },
        shape = SparkTheme.shapes.small,
        color = SparkTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = SparkTheme.typography.body1,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (selectedIcon != null /*&& selectedIconName != null*/) {
                    Icon(
                        sparkIcon = selectedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
//                    Text(
//                        text = selectedIconName.splitCamelWithSpaces(),
//                        style = SparkTheme.typography.body2,
//                        color = SparkTheme.colors.onSurface,
//                    )
                } else {
                    Text(
                        text = stringResource(R.string.icon_picker_no_icon_selected),
                        style = SparkTheme.typography.body2,
                        color = SparkTheme.colors.onSurface.dim3,
                    )
                }

                Icon(
                    sparkIcon = SparkIcons.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = SparkTheme.colors.onSurface.dim3,
                )
            }
        }
    }

    IconPickerDialog(
        showDialog = showPicker,
        onDismissRequest = { showPicker = false },
        onIconSelected = onIconSelected,
    )
}
