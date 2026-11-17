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
package com.adevinta.spark.components.badge

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.highlight

/**
 * @param size size of the badge
 * @param contentPadding padding to use for badge content
 */
public enum class BadgeStyle(public val size: Dp, public val contentPadding: Dp) {
    Small(size = smallBadgeSize, contentPadding = smallBadgeContentPadding),
    Medium(size = mediumBadgeSize, contentPadding = mediumBadgeContentPadding),
}

@Composable
internal fun BadgeStyle.getTextStyle() = when (this) {
    BadgeStyle.Small -> SparkTheme.typography.small
    BadgeStyle.Medium -> SparkTheme.typography.caption
}.highlight

private val mediumBadgeSize = 24.dp
private val mediumBadgeContentPadding = 8.dp
private val smallBadgeSize = 14.dp
private val smallBadgeContentPadding = 4.dp
