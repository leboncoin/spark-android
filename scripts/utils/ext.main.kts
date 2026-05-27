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
// spotless marker for license header

inline fun <reified T : Any> Any?.cast(): T = this as T

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> = filterValues { it != null } as Map<K, V>

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K?, V>.filterNotNullKeys(): Map<K, V> = filterKeys { it != null } as Map<K, V>

fun String.toKebabCase() = "(?<=[a-zA-Z])[A-Z]".toRegex().replace(this) { "-${it.value}" }.lowercase()
fun String.toSnakeCase() = "(?<=.)[A-Z]".toRegex().replace(this, "_$0").lowercase()
fun String.toPascalCase() = split("_").joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
