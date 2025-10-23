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
package com.adevinta.spark.catalog.examples.samples.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.R
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.snackbars.SnackbarIntent
import com.adevinta.spark.components.text.TextLink
import com.adevinta.spark.components.text.TextLinkButton
import com.adevinta.spark.icons.Chain
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.res.annotatedStringResource
import kotlinx.coroutines.launch

private const val TextLinksExampleSourceUrl = "$SampleSourceUrl/TextLinkExamples.kt"

public val TextLinksExamples: List<Example> = listOf(
    Example(
        id = "filled",
        name = "Link inside title",
        description = "Link inside title no icon",
        sourceUrl = TextLinksExampleSourceUrl,
    ) { snackbarHostState ->
        val scope = rememberCoroutineScope()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            TextLink(
                style = SparkTheme.typography.subhead,
                text = annotatedStringResource(id = R.string.spark_text_link_short_example_),
                lineHeight = 40.sp,
                onClickLabel = "Aller au site web",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "https://kotlinlang.org",
                            actionLabel = "Action",
                            duration = SnackbarDuration.Short,
                            intent = SnackbarIntent.Success,
                        )
                    }
                },
            )
        }
    },
    Example(
        id = "paragraph",
        name = "Link inside paragraph",
        description = "Link inside paragraph no icon",
        sourceUrl = TextLinksExampleSourceUrl,
    ) { snackbarHostState ->
        val scope = rememberCoroutineScope()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            TextLink(
                style = SparkTheme.typography.subhead,
                text = annotatedStringResource(id = R.string.spark_text_link_paragraph_example_),
                onClickLabel = "Voir les conditions Conditions générales d’utilisation",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Link Clicked",
                            actionLabel = "Action",
                            duration = SnackbarDuration.Short,
                        )
                    }
                },
            )
        }
    },
    Example(
        id = "button",
        name = "Entire text as link no icon",
        description = "Entire text as link no icon using Text Link Button",
        sourceUrl = TextLinksExampleSourceUrl,
    ) { snackbarHostState ->
        val scope = rememberCoroutineScope()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            TextLinkButton(
                text = "Try out Android Development",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Try out Android Development Clicked",
                            actionLabel = "Accéder au site",
                            duration = SnackbarDuration.Short,
                        )
                    }
                },
            )
        }
    },
    Example(
        id = "button-icon",
        name = "Entire text as link with icon",
        description = "Entire text as link with icon using Text Link Button",
        sourceUrl = TextLinksExampleSourceUrl,
    ) { snackbarHostState ->
        val scope = rememberCoroutineScope()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {

            TextLinkButton(
                text = "Try out Android Development",
                icon = SparkIcons.Chain,
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Try out Android Development Clicked",
                            actionLabel = "Action",
                            duration = SnackbarDuration.Short,
                        )
                    }
                },
            )
        }
    },
)
