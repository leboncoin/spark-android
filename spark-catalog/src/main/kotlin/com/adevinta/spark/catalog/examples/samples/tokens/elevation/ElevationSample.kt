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
package com.adevinta.spark.catalog.examples.samples.tokens.elevation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.ElevationTokens

@Composable
internal fun ColumnScope.ElevationSample() {
    val tokensElevationsGroups by remember {
        mutableStateOf(
            listOf(
                "Level0" to ElevationTokens.Level0,
                "Level1" to ElevationTokens.Level1,
                "Level2" to ElevationTokens.Level2,
                "Level3" to ElevationTokens.Level3,
                "Level4" to ElevationTokens.Level4,
                "Level5" to ElevationTokens.Level5,
            ),
        )
    }

    tokensElevationsGroups.forEach { (tokenName, elevation) ->
        ElevationItem(tokenName, elevation)
    }
}

@Composable
private fun ColumnScope.ElevationItem(tokenName: String, elevation: Dp) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .width(104.dp)
            .heightIn(min = 104.dp)
            .align(Alignment.CenterHorizontally),
        elevation = elevation,
        shape = SparkTheme.shapes.extraLarge,
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = tokenName,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun ElevationSamplePreview() {
    PreviewTheme {
        ElevationSample()
    }
}
