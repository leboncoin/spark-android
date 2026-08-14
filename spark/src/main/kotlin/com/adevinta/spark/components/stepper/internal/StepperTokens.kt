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

package com.adevinta.spark.components.stepper.internal

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.iconbuttons.IconButtonTokens
import com.adevinta.spark.components.textfields.TextFieldTokens

@Immutable
public object StepperTokens {

    public val borderColor: Color
        @Composable @ReadOnlyComposable
        get() = SparkTheme.colors.outline
    public val borderFocusedColor: Color
        @Composable @ReadOnlyComposable
        get() = SparkTheme.colors.outlineHigh

    public val borderThickness: Dp
        @Composable @ReadOnlyComposable
        get() = 1.dp
    public val borderFocusedThickness: Dp
        @Composable @ReadOnlyComposable
        get() = 2.dp

    @Immutable
    public object Input {
        public val inputShape: Shape
            @Composable @ReadOnlyComposable
            get() = SparkTheme.shapes.none
        public val decrementShape: Shape
            @Composable @ReadOnlyComposable
            get() = TextFieldTokens.shape.copy(topEnd = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))
        public val incrementShape: Shape
            @Composable @ReadOnlyComposable
            get() = TextFieldTokens.shape.copy(topStart = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    }

    @Immutable
    public object Nudger {
        public val buttonsShape: Shape
            @Composable @ReadOnlyComposable
            get() = IconButtonTokens.resolveFullShape(SparkTheme.shapes.full)
    }

    /**
     * The default min height applied to an [OutlinedTextField]. Note that you can override it by
     * applying Modifier.heightIn directly on a text field.
     */
    public val MinHeight: Dp = 44.dp

    /**
     * The default min width applied to an [OutlinedTextField]. Note that you can override it by
     * applying Modifier.widthIn directly on a text field.
     */
    public val MinWidth: Dp = 56.dp
}
