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
package com.adevinta.spark.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.adevinta.spark.core.R

@Composable
internal actual fun nunitoFontFamily(): FontFamily = FontFamily(
    Font(resId = R.font.nunito_sans_regular, weight = FontWeight.Normal),
    Font(resId = R.font.nunito_sans_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(resId = R.font.nunito_sans_semi_bold, weight = FontWeight.SemiBold),
    Font(resId = R.font.nunito_sans_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(resId = R.font.nunito_sans_bold, weight = FontWeight.Bold),
    Font(resId = R.font.nunito_sans_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
)
