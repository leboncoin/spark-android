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

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString.Annotation
import androidx.compose.ui.text.AnnotatedString.Range
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.StringAnnotation
import androidx.compose.ui.unit.TextUnit
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.SparkTypography

/**
 * Represents a set of annotations supported by spark that can be used in a string resource.
 */
public object SparkStringAnnotations {

    /**
     * Given a string representing an annotation key and a string representing an annotation value, returns the corresponding [SpanStyle].
     */
    public fun toStyleAnnotation(
        annotation: Range<StringAnnotation>,
        colors: SparkColors,
        typography: SparkTypography,
    ): Range<out Annotation> = when (annotation.tag) {
        "color" -> annotation.toColorSpanStyle(colors)
        "typography" -> annotation.toTypographySpanStyle(typography)
        "variable" -> annotation.toTypographySpanStyle(typography)
        else -> annotation.also { _ ->
            Log.d("StringResources", "Annotation ${annotation.tag} is not supported by spark")
        }
    }

    /**
     * Given a string representing annotation value of a spark color, returns the corresponding [SpanStyle] with the color token.
     */
    private fun Range<StringAnnotation>.toColorSpanStyle(token: SparkColors): Range<SpanStyle> {
        val color = when (this.item.value) {
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
        }
        return Range(
            item = SpanStyle(
                color = color ?: Color.Unspecified,
            ),
            start = start,
            end = end,
            tag = tag,
        )
    }

    /**
     * Given a string representing annotation value of a spark typography, returns the corresponding [SpanStyle] with the typography token.
     */
    private fun Range<StringAnnotation>.toTypographySpanStyle(token: SparkTypography): Range<SpanStyle> {
        val typography = when (this.item.value) {
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
        }
        return Range(
            item = SpanStyle(
                color = Color.Unspecified,
                fontSize = typography?.fontSize ?: TextUnit.Unspecified,
                fontWeight = typography?.fontWeight,
                fontStyle = typography?.fontStyle,
                fontSynthesis = typography?.fontSynthesis,
                fontFamily = typography?.fontFamily,
                fontFeatureSettings = typography?.fontFeatureSettings,
                letterSpacing = typography?.letterSpacing ?: TextUnit.Unspecified,
                baselineShift = typography?.baselineShift,
                textGeometricTransform = typography?.textGeometricTransform,
                localeList = typography?.localeList,
                background = typography?.background ?: Color.Unspecified,
                textDecoration = typography?.textDecoration,
                shadow = typography?.shadow,
                platformStyle = typography?.platformStyle?.spanStyle,
                drawStyle = typography?.drawStyle,
            ),
            start = start,
            end = end,
            tag = tag,
        )
    }
}
