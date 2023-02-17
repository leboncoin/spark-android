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

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import org.intellij.lang.annotations.Language
import java.util.Locale

/**
 * Source: https://github.com/androidx/androidx/blob/androidx-main/compose/lint/common-test/src/main/java/androidx/compose/lint/test/Stubs.kt
 */

/**
 * Utility for creating a [kotlin] and corresponding [bytecode] stub, to try and make it easier to
 * configure everything correctly.
 *
 * @param filename name of the Kotlin source file, with extension - e.g. "Test.kt". These should
 * be unique across a test.
 * @param filepath directory structure matching the package name of the Kotlin source file. E.g.
 * if the source has `package foo.bar`, this should be `foo/bar`. If this does _not_ match, lint
 * will not be able to match the generated classes with the source file, and so won't print them
 * to console.
 * @param source Kotlin source for the bytecode
 * @param bytecode generated bytecode that will be used in tests. Leave empty to generate the
 * bytecode for [source].
 *
 * @return a pair of kotlin test file, to bytecode test file
 */
fun kotlinAndBytecodeStub(
    filename: String,
    filepath: String,
    checksum: Long,
    @Language("kotlin") source: String,
    vararg bytecode: String,
): KotlinAndBytecodeStub {
    val filenameWithoutExtension = filename.substringBefore(".").lowercase(Locale.ROOT)
    val kotlin = TestFiles.kotlin(source).to("$filepath/$filename")
    val bytecodeStub = TestFiles.bytecode(
        "libs/$filenameWithoutExtension.jar",
        kotlin,
        checksum,
        *bytecode,
    )
    return KotlinAndBytecodeStub(kotlin, bytecodeStub)
}

class KotlinAndBytecodeStub(
    val kotlin: TestFile,
    val bytecode: TestFile,
)

/**
 * Utility for creating a [bytecode] stub, to try and make it easier to configure everything
 * correctly.
 *
 * @param filename name of the Kotlin source file, with extension - e.g. "Test.kt". These should
 * be unique across a test.
 * @param filepath directory structure matching the package name of the Kotlin source file. E.g.
 * if the source has `package foo.bar`, this should be `foo/bar`. If this does _not_ match, lint
 * will not be able to match the generated classes with the source file, and so won't print them
 * to console.
 * @param source Kotlin source for the bytecode
 * @param bytecode generated bytecode that will be used in tests. Leave empty to generate the
 * bytecode for [source].
 */
fun bytecodeStub(
    filename: String,
    filepath: String,
    checksum: Long,
    @Language("kotlin") source: String,
    vararg bytecode: String,
): TestFile = kotlinAndBytecodeStub(filename, filepath, checksum, source, *bytecode).bytecode
