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
package com.adevinta.spark.components.meter.circular

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.animation.AnimatedCounterText
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.image.SparkImage
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.tokens.dim1

internal data class CircularMeterStyling(
    val indicatorColor: Color,
    val size: CircleMeterSize,
)

@Suppress("ComposeCompositionLocalUsage")
internal val LocalCircularMeterStyling = compositionLocalOf<CircularMeterStyling> {
    error("No CircularMeterStyling provided")
}

@Stable
public sealed interface CircularMeterContent {
    @Composable
    public fun Content(modifier: Modifier = Modifier)

    /**
     * Display a formatted value text centred in the ring.
     *
     * @param text The formatted value string (e.g. "70%").
     */
    public data class Value(
        val text: String,
        val valueSuffix: String? = null,
    ) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            val styling = LocalCircularMeterStyling.current
            AnimatedCounterText(
                text = text + valueSuffix.orEmpty(),
                style = styling.size.valueTextStyle,
                textAlign = TextAlign.Center,
                modifier = modifier,
            )
        }
    }

    /**
     * Display a formatted value with a label below it, both centred in the ring.
     *
     * @param text The formatted value string (e.g. "70%").
     * @param label Secondary label displayed below the value.
     */
    public data class ValueLabel(
        val text: String,
        val valueSuffix: String? = null,
        val label: String,
    ) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            val styling = LocalCircularMeterStyling.current
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
                AnimatedCounterText(
                    text = text + valueSuffix.orEmpty(),
                    style = styling.size.valueTextStyle,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = label,
                    style = styling.size.labelTextStyle,
                    color = LocalContentColor.current.dim1,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

    /**
     * Display an icon centred in the ring, optionally with a label below.
     *
     * @param icon The [SparkIcon] to render.
     * @param contentDescription Accessible description for the icon.
     * @param label Optional label displayed below the icon.
     */
    public data class Icon(
        val icon: SparkIcon,
        val contentDescription: String,
        val label: String? = null,
    ) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            val styling = LocalCircularMeterStyling.current
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
                Icon(
                    sparkIcon = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(styling.size.iconSize),
                    tint = styling.indicatorColor,
                )
                if (label != null) {
                    Text(
                        text = label,
                        style = styling.size.labelTextStyle,
                        color = LocalContentColor.current.dim1,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }

    /**
     * Display a composable image centred in the ring.
     *
     * @param contentDescription Accessible description for the image content.
     * @param model The image composable to render.
     */
    public data class Image(
        val contentDescription: String,
        val model: Any?,
    ) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            val styling = LocalCircularMeterStyling.current
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                SparkImage(
                    model = model,
                    modifier = Modifier
                        .padding(styling.size.strokeWidth * 2)
                        .fillMaxSize(1f)
                        .clip(SparkTheme.shapes.full),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }

    /**
     * Display fully custom content centred in the ring.
     *
     * @param contentDescription Optional accessible description for the custom content.
     * @param content The composable content to render.
     */
    public data class Custom(
        val contentDescription: String? = null,
        val content: @Composable BoxScope.() -> Unit,
    ) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}
