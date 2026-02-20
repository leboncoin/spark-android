/*
 * Copyright (c) 2024 Adevinta
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
package com.adevinta.spark.snackbar

import com.adevinta.spark.components.snackbars.SnackbarSparkVisuals
import com.adevinta.spark.icons.BellOnOutline
import com.adevinta.spark.icons.LeboncoinIcons

internal val stubShortBody = "Lorem ipsum dolor sit amet"
internal val stubBody =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi lacus dolor, " +
        "pulvinar eu nulla sit amet, iaculis interdum."
internal val stubAction = "Action"

internal val BodySnackbar = SnackbarSparkVisuals(stubShortBody)
internal val BodyActionSnackbar = SnackbarSparkVisuals(stubShortBody, actionLabel = stubAction)
internal val BodyIconActionSnackbar = SnackbarSparkVisuals(
    stubShortBody,
    actionLabel = stubAction,
    icon = LeboncoinIcons.BellOnOutline,
)
internal val BodyIconSnackbar = SnackbarSparkVisuals(stubShortBody, icon = LeboncoinIcons.BellOnOutline)
internal val BodyIconActionNewLineSnackbar = SnackbarSparkVisuals(
    stubBody,
    actionLabel = stubAction,
    withDismissAction = true,
    icon = LeboncoinIcons.BellOnOutline,
)
internal val BodyTitleSnackbar = SnackbarSparkVisuals(stubBody)
internal val BodyTitleActionSnackbar = SnackbarSparkVisuals(
    stubBody,
    actionLabel = stubAction,
    withDismissAction = false,
)

internal val data = listOf(
    BodySnackbar,
    BodyActionSnackbar,
    BodyIconActionSnackbar,
    BodyIconSnackbar,
    BodyIconActionNewLineSnackbar,
    BodyTitleSnackbar,
    BodyTitleActionSnackbar,
)
