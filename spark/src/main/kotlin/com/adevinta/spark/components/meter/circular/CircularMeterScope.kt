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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon

internal data class CircularMeterStyling(
    val valueStyle: TextStyle,
    val labelStyle: TextStyle,
    val valueColor: Color,
    val labelColor: Color,
    val indicatorColor: Color,
    val iconSize: Dp,
)

@Suppress("ComposeCompositionLocalUsage")
internal val LocalCircularMeterStyling = compositionLocalOf<CircularMeterStyling> {
    error("No CircularMeterStyling provided")
}

public sealed interface CircularMeterContent {
    @Composable
    public fun Content(modifier: Modifier = Modifier)

    /**
     * Display a formatted value text centred in the ring.
     *
     * @param text The formatted value string (e.g. "70%").
     */
    @Suppress("ComposeUnstableReceiver")
    public data class Value(val text: String) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            val styling = LocalCircularMeterStyling.current
            Text(
                text = text,
                style = styling.valueStyle,
                color = styling.valueColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
    @Suppress("ComposeUnstableReceiver")
    public data class ValueLabel(val text: String, val label: String) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            val styling = LocalCircularMeterStyling.current
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
                Text(
                    text = text,
                    style = styling.valueStyle,
                    color = styling.valueColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = label,
                    style = styling.labelStyle,
                    color = styling.labelColor,
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
    @Suppress("ComposeUnstableReceiver")
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
                    modifier = Modifier.size(styling.iconSize),
                    tint = styling.indicatorColor,
                )
                if (label != null) {
                    Text(
                        text = label,
                        style = styling.labelStyle,
                        color = styling.labelColor,
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
     * @param model The image composable to render.
     */
    @Suppress("ComposeUnstableReceiver")
    public data class Image(val model: @Composable BoxScope.() -> Unit) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                model()
            }
        }
    }

    /**
     * Display fully custom content centred in the ring.
     *
     * @param content The composable content to render.
     */
    @Suppress("ComposeUnstableReceiver")
    public data class Custom(val content: @Composable BoxScope.() -> Unit) : CircularMeterContent {
        @Composable
        override fun Content(modifier: Modifier) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}
