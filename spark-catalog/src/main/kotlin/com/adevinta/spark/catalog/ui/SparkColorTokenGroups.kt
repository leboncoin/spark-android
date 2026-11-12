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
package com.adevinta.spark.catalog.ui

import androidx.compose.ui.graphics.Color
import com.adevinta.spark.tokens.SparkColors
import kotlin.reflect.KProperty1

/**
 * The [SparkColors] tokens shown in the catalog, grouped by token family and ordered like the specs.
 *
 * The groups are listed by hand so the catalog does not need `kotlin-reflect`. Content colors (`on...`)
 * and dims are left out on purpose. Add a new color family here when it is added to [SparkColors].
 */
internal val sparkColorTokenGroups: List<List<KProperty1<SparkColors, Color>>> = listOf(
    listOf(SparkColors::main, SparkColors::mainContainer, SparkColors::mainVariant),
    listOf(SparkColors::support, SparkColors::supportContainer, SparkColors::supportVariant),
    listOf(SparkColors::accent, SparkColors::accentContainer, SparkColors::accentVariant),
    listOf(SparkColors::background, SparkColors::backgroundVariant),
    listOf(SparkColors::surface, SparkColors::surfaceDark, SparkColors::surfaceInverse),
    listOf(SparkColors::outline, SparkColors::outlineHigh),
    listOf(SparkColors::success, SparkColors::successContainer),
    listOf(SparkColors::alert, SparkColors::alertContainer),
    listOf(SparkColors::error, SparkColors::errorContainer),
    listOf(SparkColors::info, SparkColors::infoContainer),
    listOf(SparkColors::neutral, SparkColors::neutralContainer),
    listOf(SparkColors::ai, SparkColors::aiContainer),
    listOf(SparkColors::focus, SparkColors::focusContainer),
    listOf(SparkColors::new, SparkColors::newContainer),
    listOf(SparkColors::business, SparkColors::businessContainer),
    listOf(SparkColors::service, SparkColors::serviceContainer),
    listOf(SparkColors::scrim),
)
