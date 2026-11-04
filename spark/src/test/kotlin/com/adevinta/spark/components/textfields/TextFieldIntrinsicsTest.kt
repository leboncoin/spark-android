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
package com.adevinta.spark.components.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.ShoppingCartOutline
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TextFieldIntrinsicsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun intrinsic_height_with_a_width_narrower_than_the_addons_lays_out_empty() {
        composeTestRule.setContent {
            PreviewTheme {
                // IntrinsicSize.Min queries the field's intrinsic height for the 0dp width it is given.
                Column(modifier = Modifier.width(0.dp).height(IntrinsicSize.Min)) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = "Label",
                        placeholder = "Placeholder",
                        leadingContent = {
                            Icon(sparkIcon = LeboncoinIcons.ShoppingCartOutline, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(sparkIcon = LeboncoinIcons.ShoppingCartOutline, contentDescription = null)
                        },
                        modifier = Modifier.testTag("field"),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("field").assertWidthIsEqualTo(0.dp)
    }
}
