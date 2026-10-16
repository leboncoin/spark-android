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
package com.adevinta.spark.catalog.testing

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import com.adevinta.spark.catalog.examples.samples.buttons.ButtonsExamples
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SmokeTest {

    @get:Rule(order = 0)
    val componentActivityRule = ComponentActivityRule()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun filledButtonsExampleRendersEnabledButtons() {
        composeTestRule.setContent {
            CatalogTestTheme { FilledButtonsExample() }
        }

        val buttons = composeTestRule.onAllNodesWithText(FilledButtonText)
        assertThat(buttons.fetchSemanticsNodes()).isNotEmpty()
        buttons.onFirst().assertIsDisplayed().assertIsEnabled()
    }
}

internal class SmokeScreenshotTest {

    @get:Rule
    val paparazzi = catalogPaparazzi()

    @Test
    fun filledButtonsExample() = paparazzi.catalogSnapshot { FilledButtonsExample() }
}

private const val FilledButtonText = "Filled Button"

@Composable
private fun FilledButtonsExample() {
    val example = ButtonsExamples.first { it.id == "filled" }
    val snackbarHostState = remember { SnackbarHostState() }
    Column { example.content(this, snackbarHostState) }
}
