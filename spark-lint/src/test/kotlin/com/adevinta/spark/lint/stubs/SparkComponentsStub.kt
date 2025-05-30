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
package com.adevinta.spark.lint.stubs

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin

// Simplified Spark.kt stubs
internal val SparkComponentsStub = arrayOf(
    kotlin(
        """
    package com.adevinta.spark.components.buttons
    import androidx.compose.runtime.Composable

    @Composable
    fun ButtonFilled() {}

    @Composable
    fun ButtonTinted() {}

    @Composable
    fun ButtonOutlined() {}

    @Composable
    fun ButtonGhost() {}

    @Composable
    fun ButtonContrast() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.textfields
    import androidx.compose.runtime.Composable

    @Composable
    fun TextField() {}

    @Composable
    fun DropDown() {}

    @Composable
    fun MultilineTextField() {}

    @Composable
    fun Combobox() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.toggles
    import androidx.compose.runtime.Composable

    @Composable
    fun CheckBox() {}

    @Composable
    fun RadioButton() {}

    @Composable
    fun Switch() {}

    @Composable
    fun CheckboxLabelled() {}

@Composable
    fun RadioButtonLabelled() {}

    @Composable
    fun SwitchLabelled() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.snackbars
    import androidx.compose.runtime.Composable

    @Composable
    fun Snackbar() {}

    @Composable
    fun Snackbarhost() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.snackbars
    import androidx.compose.runtime.Composable

    @Composable
    fun Snackbar() {}

    @Composable
    fun Snackbarhost() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.progress
    import androidx.compose.runtime.Composable

    @Composable
    fun LinearProgressIndicator() {}

    @Composable
    fun CircularProgressIndicator() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark
    import androidx.compose.runtime.Composable

    @Composable
    fun SparkTheme() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.image
    import androidx.compose.runtime.Composable

    @Composable
    fun Image() {}

    @Composable
    fun Illustration() {}

    @Composable
    fun UserAvatar() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.icons
    import androidx.compose.runtime.Composable

    @Composable
    fun Icon() {}

    @Composable
    fun IconButton() {}

    @Composable
    fun FilledIconButton() {}

    @Composable
    fun FilledTonalIconButton() {}

    @Composable
    fun OutlinedIconButton() {}

    @Composable
    fun IconToggleButton() {}

    @Composable
    fun FilledIconToggleButton() {}

    @Composable
    fun FilledTonalIconToggleButton() {}

    @Composable
    fun OutlinedIconToggleButton() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.divider
    import androidx.compose.runtime.Composable

    @Composable
    fun HorizontalDivider() {}

    @Composable
    fun VerticalDivider() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.text
    import androidx.compose.runtime.Composable

    @Composable
    fun Text() {}
        """.trimIndent(),
    ),
    kotlin(
        """
    package com.adevinta.spark.components.tags
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier

    @Composable
    fun TagFilled(
        text: String? = null,
        contentDescription: String? = null,
        modifier: Modifier = Modifier
    ) {}

    @Composable
    fun TagFilled(
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {}

    @Composable
    fun TagOutlined(
        text: String? = null,
        contentDescription: String? = null,
        modifier: Modifier = Modifier
    ) {}

    @Composable
    fun TagOutlined(
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {}

    @Composable
    fun TagTinted(
        text: String? = null,
        contentDescription: String? = null,
        modifier: Modifier = Modifier
    ) {}

    @Composable
    fun TagTinted(
        contentDescription: String? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {}
        """.trimIndent(),
    ),
)
