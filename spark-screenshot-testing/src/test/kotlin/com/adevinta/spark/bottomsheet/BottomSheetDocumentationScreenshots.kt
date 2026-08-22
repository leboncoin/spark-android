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
package com.adevinta.spark.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.bottomsheet.BottomSheet
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.NORMAL
import com.android.resources.ScreenOrientation
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = NORMAL,
        deviceConfig = DefaultTestDevices.DocPhone.copy(
            screenWidth = 720,
            screenHeight = 1000,
            orientation = ScreenOrientation.PORTRAIT,
        ),
    )

    @Test
    fun bottomSheetBody() = paparazzi.sparkSnapshot {
        val sheetState = remember {
            SheetState(
                skipPartiallyExpanded = true,
                positionalThreshold = { 0f },
                velocityThreshold = { 0f },
                initialValue = SheetValue.Expanded,
                skipHiddenState = true,
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Screen title",
                    style = SparkTheme.typography.headline1,
                )
                repeat(4) { index ->
                    Text(
                        text = "List item ${index + 1}",
                        style = SparkTheme.typography.body1,
                    )
                }
            }
            BottomSheet(
                onDismissRequest = {},
                sheetState = sheetState,
            ) {
                Text(
                    text = "Sheet title",
                    style = SparkTheme.typography.headline2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                Text(
                    text = "Sheet content goes here.",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}
