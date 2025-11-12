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
package com.adevinta.spark.res

import android.text.Annotation
import android.util.Log
import androidx.compose.ui.text.SpanStyle
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.SparkTypography

/**
 * Represents a set of annotations supported by spark that can be used in a string resource.
 */
public object SparkStringAnnotations {

    /**
     * Given a string representing an annotation key and a string representing an annotation value, returns the corresponding [SpanStyle].
     */
    public fun toSpanStyle(
        annotation: Annotation,
        colors: SparkColors,
        typography: SparkTypography,
    ): SpanStyle? = when (annotation.key) {
        "color" -> annotation.value.toColorSpanStyle(colors)
        "typography" -> annotation.value.toTypographySpanStyle(typography)
        else -> null.also { _ ->
            Log.d("StringResources", "Annotation $this is not supported by spark")
        }
    }

    /**
     * Given a string representing annotation value of a spark color, returns the corresponding [SpanStyle] with the color token.
     */
    private fun String.toColorSpanStyle(token: SparkColors): SpanStyle? = when (this) {
        "main" -> token.main
        "support" -> token.support
        "success" -> token.success
        "alert" -> token.alert
        "error" -> token.error
        "info" -> token.info
        "neutral" -> token.neutral
        "accent" -> token.accent
        else -> null.also { _ ->
            Log.d("StringResources", "Spark color annotation : $this is not supported")
        }
    }?.let(::SpanStyle)

    /**
     * Given a string representing annotation value of a spark typography, returns the corresponding [SpanStyle] with the typography token.
     */
    private fun String.toTypographySpanStyle(token: SparkTypography): SpanStyle? = when (this) {
        "display1" -> token.display1
        "display2" -> token.display2
        "display3" -> token.display3
        "headline1" -> token.headline1
        "headline2" -> token.headline2
        "subhead" -> token.subhead
        "large" -> token.body1
        "body1" -> token.body1
        "body2" -> token.body2
        "caption" -> token.caption
        "small" -> token.small
        "callout" -> token.callout
        else -> null.also { _ ->
            Log.d("StringResources", "Spark typography annotation : $this is not supported")
        }
    }?.toSpanStyle()
}
