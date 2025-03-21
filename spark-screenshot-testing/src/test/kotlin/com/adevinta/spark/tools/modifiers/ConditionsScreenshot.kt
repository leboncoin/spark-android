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

package com.adevinta.spark.tools.modifiers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

class ConditionsScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun `test conditions`() = paparazzi.sparkSnapshot(drawBackground = true) {
        Column {
            Row {
                IfTrueTestComposable(value = true)
                IfTrueTestComposable(value = false)
                IfTrueTestComposable(
                    value = true,
                    modifier = Modifier.rotate(90f),
                )
                IfTrueTestComposable(
                    value = false,
                    modifier = Modifier.rotate(90f),
                )
            }

            Row {
                IfFalseTestComposable(value = false)
                IfFalseTestComposable(value = true)
                IfFalseTestComposable(
                    value = false,
                    modifier = Modifier.rotate(90f),
                )
                IfFalseTestComposable(
                    value = true,
                    modifier = Modifier.rotate(90f),
                )
            }

            Row {
                IfNullTestComposable(value = null)
                IfNullTestComposable(value = Unit)
                IfNullTestComposable(
                    value = null,
                    modifier = Modifier.rotate(90f),
                )
                IfNullTestComposable(
                    value = Unit,
                    modifier = Modifier.rotate(90f),
                )
            }

            Row {
                IfNotNullTestComposable(value = Unit)
                IfNotNullTestComposable(value = null)
                IfNotNullTestComposable(
                    value = Unit,
                    modifier = Modifier.rotate(90f),
                )
                IfNotNullTestComposable(
                    value = null,
                    modifier = Modifier.rotate(90f),
                )
            }
        }
    }
}


