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
package com.adevinta.spark.textfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.InputOTP
import com.adevinta.spark.components.textfields.InputOTPType
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.adevinta.spark.sparkSnapshotNightMode
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.H_SCROLL
import org.junit.Rule
import org.junit.Test

internal class InputOTPScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet.copy(
            screenHeight = 2400, // Increased height to accommodate all content
        ),
        renderingMode = H_SCROLL,
    )

    @OptIn(ExperimentalLayoutApi::class)
    @Test
    fun allStatesAndTypes() {
        paparazzi.sparkSnapshotNightMode {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // States section
                Text("States")
                FlowColumn(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Empty
                    InputOTP(
                        value = "",
                        onValueChange = {},
                        length = 4,
                    )

                    // Filled
                    InputOTP(
                        value = "1234",
                        onValueChange = {},
                        length = 4,
                    )

                    // Error
                    InputOTP(
                        value = "1234",
                        onValueChange = {},
                        length = 4,
                        state = FormFieldStatus.Error,
                    )

                    // Success
                    InputOTP(
                        value = "1234",
                        onValueChange = {},
                        length = 4,
                        state = FormFieldStatus.Success,
                    )

                    // Disabled Empty
                    InputOTP(
                        value = "",
                        onValueChange = {},
                        length = 4,
                        enabled = false,
                    )

                    // Disabled Filled
                    InputOTP(
                        value = "1234",
                        onValueChange = {},
                        length = 4,
                        enabled = false,
                    )
                }

                // Note: Focused/Activated state with double blue border (4dp blue + 2dp white)
                // cannot be captured in static Paparazzi screenshots as it requires real focus interaction

                // Input types section
                Text("Input Types")
                FlowColumn(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Numeric
                    InputOTP(
                        value = "1234",
                        onValueChange = {},
                        length = 4,
                        inputType = InputOTPType.Numeric,
                    )

                    // Alphanumeric
                    InputOTP(
                        value = "AB12",
                        onValueChange = {},
                        length = 4,
                        inputType = InputOTPType.Alphanumeric,
                    )

                    // Alphabetic
                    InputOTP(
                        value = "ABCD",
                        onValueChange = {},
                        length = 4,
                        inputType = InputOTPType.Alphabetic,
                    )
                }

                // Lengths section
                Text("Lengths")
                FlowColumn(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // 4 digits partial
                    InputOTP(
                        value = "12",
                        onValueChange = {},
                        length = 4,
                    )
                }

                // 6 Digits section
                Text("6 Digits")
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // 6 digits with separator
                    InputOTP(
                        value = "123456",
                        onValueChange = {},
                        length = 6,
                        showSeparator = true,
                    )

                    // 6 digits without separator
                    InputOTP(
                        value = "123456",
                        onValueChange = {},
                        length = 6,
                        showSeparator = false,
                    )
                }

                // Add spacing at the bottom to ensure all content is visible in screenshots
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
