/*
 * Copyright (c) 2023-2024 Adevinta
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
package com.adevinta.spark.tools.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlin.reflect.KClass

/**
 * Creates a [PreviewParameterProvider] based on the classes of two existing providers.
 *
 * You can create your own easily with Kotlin delegation:
 * ```kotlin
 * class ExampleProvider : PreviewParameterProvider<Pair<Type1, Type2>>
 *    by compositeProvider(Type1ParameterProvider::class, Type2ParameterProvider::class)
 * ```
 */
public fun <T1 : Any, T2 : Any> compositeProvider(
    kClass1: KClass<out PreviewParameterProvider<T1>>,
    kClass2: KClass<out PreviewParameterProvider<T2>>,
): PreviewParameterProvider<Pair<T1, T2>> = CompositeParameterProvider(kClass1, kClass2)

private class CompositeParameterProvider<T1 : Any, T2 : Any>(
    kClass1: KClass<out PreviewParameterProvider<T1>>,
    kClass2: KClass<out PreviewParameterProvider<T2>>,
) : PreviewParameterProvider<Pair<T1, T2>> {

    private val provider1 = kClass1.java.getDeclaredConstructor().newInstance()
    private val provider2 = kClass2.java.getDeclaredConstructor().newInstance()

    override val values: Sequence<Pair<T1, T2>>
        get() = provider1.values union provider2.values
}

private infix fun <T1 : Any, T2 : Any> Sequence<T1>.union(
    sequence: Sequence<T2>,
): Sequence<Pair<T1, T2>> = flatMap { original -> sequence.map { original to it } }
