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
package com.adevinta.spark.components.meter

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.VerifiedShieldFill
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class MeterScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.Tablet,
    )

    @Test
    fun default() {
        paparazzi.sparkSnapshotNightMode {
            Row(horizontalArrangement = spacedBy(4.dp)) {
                MeterIntent.entries.forEach { intent ->
                    Column(verticalArrangement = spacedBy(4.dp)) {
                        Meter.CircularSmall(
                            value = 50f,
                            intent = intent,
                        )
                        Meter.Circular(
                            value = 70f,
                            content = CircularMeterContent.Value(),
                            intent = intent,
                            size = CircleMeterSize.Medium,
                        )
                        Meter.Circular(
                            value = 50f,
                            content = CircularMeterContent.ValueLabel(label = "Label"),
                            intent = intent,
                            size = CircleMeterSize.Large,
                        )
                        Meter.Circular(
                            value = 3f,
                            range = 0f..4f,
                            content = CircularMeterContent.Icon(
                                icon = LeboncoinIcons.VerifiedShieldFill,
                                contentDescription = "Label",
                            ),
                            intent = intent,
                            size = CircleMeterSize.XLarge,
                        )
                        Meter.Circular(
                            value = 50f,
                            content = CircularMeterContent.Image(
                                model = Color.Red, // should show the error state
                                contentDescription = "Label",
                            ),
                            intent = intent,
                            size = CircleMeterSize.XLarge,
                        )
                    }
                }
            }
        }
    }
}
