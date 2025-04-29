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
package com.adevinta.spark.components.rating

import android.icu.text.NumberFormat
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.highlight
import java.lang.String.format
import java.util.Locale

/**
 * Component that displays rating of an user with a star in the following form:
 *  - ★ 3,4
 *  - ★ 3,4 (5)
 * @param value rating value as a float, should be between 1.0 and 5.0
 * @param modifier to apply
 * @param commentCount the count of comments are associated with this rating
 */
@InternalSparkApi
@Composable
internal fun SparkRatingSmall(
    @FloatRange(from = 0.0, to = 5.0)
    value: Float,
    labelSide: RatingLabelSide,
    size: RatingSize,
    modifier: Modifier = Modifier,
    commentCount: Int? = null,
    locale: Locale = firstLocale(),
) {
    if (value !in 1f..5f) return
    val contentDescription: String = if (commentCount != null) {
        pluralStringResource(
            id = R.plurals.spark_rating_with_comments_a11y,
            commentCount,
            value,
            commentCount,
        )
    } else {
        stringResource(id = R.string.spark_rating_a11y, value)
    }

    val labelText = formattedRatingValue(locale, value)

    val label = remember {
        movableContentOf {
            Text(
                text = labelText,
                textAlign = TextAlign.Center,
                style = if (size == RatingSize.Large) {
                    SparkTheme.typography.display3
                } else {
                    SparkTheme.typography.caption.highlight
                },
            )
        }
    }

    Row(
        modifier = modifier.semantics(mergeDescendants = true) {
            this.contentDescription = contentDescription
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(4.dp),
    ) {
        if (labelSide == RatingLabelSide.Start) {
            label()
        }
        RatingStar(
            enabled = true,
            size = if (size == RatingSize.Large) 24.dp else RatingDefault.SmallStarSize,
        )
        if (labelSide == RatingLabelSide.End) {
            label()
        }
        if (commentCount != null) {
            Text(
                text = format(locale, "(%d)", commentCount),
                textAlign = TextAlign.Center,
                style = SparkTheme.typography.caption,
            )
        }
        if (size == RatingSize.Large) {
            Text(
                text = "/5",
                modifier = Modifier.align(Alignment.Bottom),
                textAlign = TextAlign.Center,
                style = SparkTheme.typography.body1.highlight,
            )
        }
    }
}

@Composable
internal fun formattedRatingValue(locale: Locale, value: Float): String = remember(locale, value) {
    val numberFormat = NumberFormat.getInstance(locale)
    numberFormat.maximumFractionDigits = 1
    numberFormat.format(value)
}

/**
 * Component that displays a compressed version of user rating
 *
 * @param value the rating value included between [1..5]
 * @param modifier to be applied to this rating
 * @param commentCount number of collected ratings
 * @param locale the locale to use to format the rating value
 * @param labelSide the side of the label
 */
@Composable
@ExperimentalSparkApi
public fun RatingSimple(
    @FloatRange(from = 0.0, to = 5.0)
    value: Float,
    modifier: Modifier = Modifier,
    commentCount: Int? = null,
    locale: Locale = firstLocale(),
    labelSide: RatingLabelSide = RatingDefault.LabelSide,
) {
    SparkRatingSmall(
        value = value,
        modifier = modifier,
        commentCount = commentCount,
        locale = locale,
        labelSide = labelSide,
        size = RatingSize.Medium,
    )
}

/**
 * This function displays a large simple version of user rating.
 *
 * @param value The rating value included between [1..5].
 * @param modifier The modifier to be applied to this rating.
 * @param locale The locale to use to format the rating value.
 * @param labelSide The side of the label.
 */
@Composable
@ExperimentalSparkApi
public fun RatingSimpleLarge(
    @FloatRange(from = 0.0, to = 5.0)
    value: Float,
    modifier: Modifier = Modifier,
    locale: Locale = firstLocale(),
    labelSide: RatingLabelSide = RatingDefault.LabelSide,
) {
    SparkRatingSmall(
        value = value,
        modifier = modifier,
        commentCount = null,
        locale = locale,
        labelSide = labelSide,
        size = RatingSize.Large,
    )
}

public enum class RatingLabelSide {
    Start,
    End,
}

internal enum class RatingSize {
    Medium,
    Large,
}

@Composable
internal fun firstLocale(): Locale = LocalConfiguration.current.locales[0]

@Composable
@Preview(
    group = "Ratings",
    name = "RatingSmall",
)
internal fun RatingSmallPreview() {
    val frenchLocale = Locale.FRANCE
    PreviewTheme {
        RatingSimple(value = 3.0f, locale = frenchLocale, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.50f, locale = frenchLocale, labelSide = RatingLabelSide.End)

        RatingSimple(value = 3.0f, commentCount = 8, locale = frenchLocale, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.50f, commentCount = 12, locale = frenchLocale, labelSide = RatingLabelSide.End)
        RatingSimpleLarge(value = 4.50f, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.50f, commentCount = 12, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.51f, commentCount = 12, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.501f, commentCount = 12, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimple(value = 3.01f, commentCount = 12, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimple(value = 3.001f, commentCount = 12, locale = Locale.US, labelSide = RatingLabelSide.End)
    }
}
