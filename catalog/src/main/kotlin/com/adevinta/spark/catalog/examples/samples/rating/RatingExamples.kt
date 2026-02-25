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
package com.adevinta.spark.catalog.examples.samples.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.rating.RatingDefault
import com.adevinta.spark.components.rating.RatingDisplay
import com.adevinta.spark.components.rating.RatingFull
import com.adevinta.spark.components.rating.RatingInput
import com.adevinta.spark.components.rating.RatingLabelSide
import com.adevinta.spark.components.rating.RatingSimple
import com.adevinta.spark.components.rating.RatingSimpleLarge
import com.adevinta.spark.components.rating.RatingStar
import com.adevinta.spark.components.rating.RatingStarState
import com.adevinta.spark.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

public val RatingExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "display",
        name = "Rating Display",
        description = "Rating Display example that show every step value",
        sourceUrl = "$SampleSourceUrl/RatingDisplaySample.kt",
    ) {
        RatingDisplaySample()
    },
    Example(
        id = "display-medium",
        name = "Rating Display Medium",
        description = "Rating Display example in medium size that show every step value",
        sourceUrl = "$SampleSourceUrl/RatingDisplaySample.kt",
    ) {
        RatingDisplaySample(starSize = RatingDefault.StarSize)
    },
    Example(
        id = "input",
        name = "Rating Input",
        description = "Rating input example that le the user select a rating value",
        sourceUrl = "$SampleSourceUrl/RatingInputSample.kt",
    ) {
        RatingInputSample()
    },
    Example(
        id = "full",
        name = "Rating Full",
        description = "Rating Full for Polaris that highlight the different layout possible and the difference " +
            "in locale",
        sourceUrl = "$SampleSourceUrl/RatingFullSample.kt",
    ) {
        RatingFull()
    },
    Example(
        id = "simple",
        name = "Rating Simple",
        description = "Rating Simple for Polaris that highlight the different layout possible and the difference " +
            "in locale",
        sourceUrl = "$SampleSourceUrl/RatingSmallSample.kt",
    ) {
        RatingSimpleSample()
    },
    Example(
        id = "star",
        name = "Rating Star",
        description = "The star used for the rating component",
        sourceUrl = "$SampleSourceUrl/RatingStarSample.kt",
    ) {
        RatingStarSample()
    },
)

@Composable
private fun RatingDisplaySample(
    starSize: Dp = RatingDefault.SmallStarSize,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RatingDisplay(value = 0.5f, size = starSize)
        RatingDisplay(value = 1f, size = starSize)
        RatingDisplay(value = 1.5f, size = starSize)
        RatingDisplay(value = 2.1f, size = starSize)
        RatingDisplay(value = 2.5f, size = starSize)
        RatingDisplay(value = 3.2f, size = starSize)
        RatingDisplay(value = 3.666f, size = starSize)
        RatingDisplay(value = 4.1f, size = starSize)
        RatingDisplay(value = 4.26f, size = starSize)
        RatingDisplay(value = 5f, size = starSize)
    }
}

@Composable
private fun RatingInputSample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var rating by remember {
            mutableIntStateOf(2)
        }
        RatingInput(value = rating, onRatingChanged = { rating = it })
    }
}

@Composable
private fun RatingFull() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RatingFull(value = 1f, label = "Communication")
        RatingFull(value = 2.1f, label = "Communication", commentCount = 5)
        RatingFull(value = 3.999999f, commentCount = 5, locale = null)
        RatingFull(value = 4.2f)
        RatingFull(value = 5f)
    }
}

@Composable
private fun RatingSimpleSample() {
    val frenchLocale = Locale.FRANCE
    val germanLocale = Locale.GERMANY
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("French")
        RatingSimple(value = 3.0f, locale = frenchLocale, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.50f, commentCount = 12, locale = frenchLocale, labelSide = RatingLabelSide.End)
        RatingSimpleLarge(value = 4.50f, locale = frenchLocale, labelSide = RatingLabelSide.End)

        Text("US English")
        RatingSimple(value = 3.0f, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimple(value = 4.50f, commentCount = 12, locale = Locale.US, labelSide = RatingLabelSide.End)
        RatingSimpleLarge(value = 4.50f, locale = Locale.US, labelSide = RatingLabelSide.End)
    }
}

@Composable
private fun RatingStarSample() {
    val smallSize = RatingDefault.SmallStarSize
    val mediumSize = RatingDefault.StarSize
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RatingStar(enabled = true, size = smallSize)
        RatingStar(enabled = true, size = mediumSize)
        RatingStar(enabled = false, size = smallSize)
        RatingStar(enabled = false, size = mediumSize)
        RatingStar(state = RatingStarState(0.1), size = smallSize)
        RatingStar(state = RatingStarState(0.1), size = mediumSize)
        RatingStar(state = RatingStarState(0.3), size = smallSize)
        RatingStar(state = RatingStarState(0.3), size = mediumSize)
        RatingStar(state = RatingStarState(0.6), size = smallSize)
        RatingStar(state = RatingStarState(0.6), size = mediumSize)
        RatingStar(state = RatingStarState(0.8), size = smallSize)
        RatingStar(state = RatingStarState(0.8), size = mediumSize)
    }
}
