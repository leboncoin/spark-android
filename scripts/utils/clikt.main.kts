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

@file:DependsOn(
    "com.github.ajalt.clikt:clikt-jvm:5.1.0",
)


import com.github.ajalt.clikt.core.BaseCliktCommand
import com.github.ajalt.clikt.parameters.options.OptionWithValues
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path
import java.nio.file.Paths


fun BaseCliktCommand<*>.dryRun() = option("--dry-run", help = "Dry run").flag()
fun BaseCliktCommand<*>.verbose() = option("--verbose", "-v", help = "Verbose").flag()
fun BaseCliktCommand<*>.prettyPrint() = option("--pretty-print", "--pretty", help = "Pretty print").flag()

fun BaseCliktCommand<*>.projectDir(): OptionWithValues<Path, Path, Path> = option(
    "--project-dir",
    help = "The project directory. Defaults to the current working directory.",
)
    .path(mustExist = true, canBeFile = false)
    .defaultLazy { Paths.get("").toAbsolutePath() }

fun BaseCliktCommand<*>.input(help: String = "Input file") = option("-i", "--in", help = help)
    .path(mustExist = true, mustBeReadable = true, canBeDir = false)

fun BaseCliktCommand<*>.output(help: String = "Output file") = option("-o", "--out", help = help)
    .path(mustExist = false, canBeDir = false)
