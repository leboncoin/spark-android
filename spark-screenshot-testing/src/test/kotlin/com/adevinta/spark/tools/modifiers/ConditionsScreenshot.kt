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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    fun conditions() = paparazzi.sparkSnapshot(drawBackground = true) {
        Column {
            Row {
                ConditionTestComposable(name = "ifTrue", value = true) {
                    ifTrue(true, it)
                }
                ConditionTestComposable(name = "ifTrue", value = false) {
                    ifTrue(false, it)
                }
            }

            Row {
                ConditionTestComposable(name = "ifFalse", value = false) {
                    ifFalse(false, it)
                }
                ConditionTestComposable(name = "ifFalse", value = true) {
                    ifFalse(true, it)
                }
            }

            Row {
                ConditionTestComposable(name = "ifNull", value = null) {
                    ifNull(null, it)
                }
                ConditionTestComposable(name = "ifNull", value = Unit) {
                    ifNull(Unit, it)
                }
            }

            Row {
                ConditionTestComposable(name = "ifNotNull", value = Unit) {
                    ifNotNull(Unit) { it() }
                }
                ConditionTestComposable(name = "ifNotNull", value = null) {
                    ifNotNull(null) { it() }
                }
            }
        }
    }
}

@Composable
private fun ConditionTestComposable(
    name: String,
    value: Any?,
    condition: Modifier.(chain: Modifier.() -> Modifier) -> Modifier,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Condition: $name",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Red)
                .rotate(90f)
                .condition({ background(Color.Green) }),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
