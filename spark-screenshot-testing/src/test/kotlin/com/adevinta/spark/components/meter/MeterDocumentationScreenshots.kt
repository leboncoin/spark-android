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

import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.meter.circular.CircleMeterSize
import com.adevinta.spark.components.meter.circular.CircularMeterContent
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.VerifiedShieldFill
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.screenshot.testing.R
import com.adevinta.spark.sparkDocSnapshot
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import org.junit.Rule
import org.junit.Test

internal class MeterDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun circular() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.ValueLabel(label = "Label"),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun circularSmall() = paparazzi.sparkDocSnapshot {
        Meter.CircularSmall(value = 70f, intent = MeterIntent.Support)
    }

    @Test
    fun contentValue() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.Value(),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun contentValueLabel() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.ValueLabel(label = "Complete"),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun contentIcon() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 75f,
            content = CircularMeterContent.Icon(
                icon = LeboncoinIcons.VerifiedShieldFill,
                contentDescription = "Verified",
                label = "Verified",
            ),
            intent = MeterIntent.Success,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun contentImage() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 50f,
            content = CircularMeterContent.Image(
                model = R.drawable.spark_img_narrow_image_configurator,
                contentDescription = "Profile photo",
            ),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun sizeMedium() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.Value(),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Medium,
        )
    }

    @Test
    fun sizeLarge() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.ValueLabel(label = "Label"),
            intent = MeterIntent.Support,
            size = CircleMeterSize.Large,
        )
    }

    @Test
    fun sizeXLarge() = paparazzi.sparkDocSnapshot {
        Meter.Circular(
            value = 70f,
            content = CircularMeterContent.ValueLabel(label = "Label"),
            intent = MeterIntent.Support,
            size = CircleMeterSize.XLarge,
        )
    }
}
