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
package com.adevinta.spark.icons

import androidx.compose.ui.graphics.Color
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshot
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.google.testing.junit.testparameterinjector.TestParameterValuesProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
internal class IconsScreenshot {

    @get:Rule
    val paparazzi = paparazziRule()

    internal object SparkIconProvider : TestParameterValuesProvider() {
        override fun provideValues(context: Context) = Class.forName("com.adevinta.spark.icons.LeboncoinIconsKt")
            .methods
            .filter { SparkIcon::class.java.isAssignableFrom(it.returnType) }
            .map { value(it.invoke(null, LeboncoinIcons) as SparkIcon).withName(it.name.removePrefix("get")) }
    }

    @Test
    @Suppress("JUnitMalformedDeclaration")
    fun render(
        @TestParameter(valuesProvider = SparkIconProvider::class) icon: SparkIcon,
    ) = paparazzi.sparkSnapshot(drawBackground = false) {
        Icon(
            sparkIcon = icon,
            contentDescription = icon.toString(),
            size = IconSize.ExtraLarge,
            tint = Color.Black,
        )
    }
}
