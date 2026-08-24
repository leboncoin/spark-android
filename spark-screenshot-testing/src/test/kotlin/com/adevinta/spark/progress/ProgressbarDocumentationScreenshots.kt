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
package com.adevinta.spark.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.progressbar.Progressbar
import com.adevinta.spark.components.progressbar.ProgressbarIndeterminate
import com.adevinta.spark.components.progressbar.ProgressbarIntent
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class ProgressbarDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun progressbar() = paparazzi.sparkDocSnapshot {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Progressbar(
                progress = { 0f },
                modifier = Modifier.fillMaxWidth(),
                intent = ProgressbarIntent.Support,
            )
            Progressbar(
                progress = { 0.25f },
                modifier = Modifier.fillMaxWidth(),
                intent = ProgressbarIntent.Support,
            )
            Progressbar(
                progress = { 0.75f },
                modifier = Modifier.fillMaxWidth(),
                intent = ProgressbarIntent.Support,
            )
            Progressbar(
                progress = { 1f },
                modifier = Modifier.fillMaxWidth(),
                intent = ProgressbarIntent.Support,
            )
        }
    }

    @Test
    fun progressbarIndeterminate() = paparazzi.sparkDocSnapshot {
        ProgressbarIndeterminate(
            modifier = Modifier.fillMaxWidth(),
            intent = ProgressbarIntent.Support,
        )
    }
}
