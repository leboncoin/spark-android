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
package com.adevinta.spark.components.iconbuttons

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.icons.IconSize

public enum class IconButtonSize {
    /**
     * Small button with min touch size still applied
     */
    Small {
        override val height: Dp = 32.dp
        override val iconSize: IconSize = IconSize.Small
    },

    /**
     * Medium button is the default button size (recommended).
     */
    Medium {
        override val height: Dp = 44.dp
        override val iconSize: IconSize = IconSize.Small
    },

    /**
     * Alternative large icon button size to give more emphasis to the action
     */
    Large {
        override val height: Dp = 56.dp
        override val iconSize: IconSize = IconSize.Medium
    },
    ;

    internal abstract val height: Dp
    internal abstract val iconSize: IconSize
}
