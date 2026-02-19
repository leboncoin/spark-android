#!/usr/bin/env kotlin

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

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("https://maven.google.com")
@file:DependsOn("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:4.2.0")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path
import java.util.Locale
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.bufferedWriter
import kotlin.io.path.isRegularFile
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText
import kotlin.io.path.walk

/**
 * SparkIcons will create a Kotlin file containing all icons.
 */
class SparkIcons : CliktCommand(
    name = "spark-icons-kt.main.kts",
    help = "⚙️ LeboncoinIcons: Create a Kotlin file containing all icons",
) {

    val input: Path by option("-i", "--input", help = "AVD assets input")
        .path(mustExist = true, canBeFile = false, mustBeReadable = true)
        .prompt()

    val output: Path by option("-o", "--output", help = "Kotlin file output")
        .path(mustExist = false, canBeDir = false)
        .prompt()

    val copyright: Path? by option("-c", "--copyright", help = "Copyright header")
        .path(mustExist = false, canBeDir = false, mustBeReadable = true)

    val quiet by option("-q", "--quiet", help = "Print errors only")
        .flag(default = false)

    override fun run() {
        output.bufferedWriter().use {
            copyright?.takeIf { it.isRegularFile() }?.readText()?.let(it::write)
            it.write(
                """
                @file:Suppress("UnusedReceiverParameter", "unused", "ktlint")

                package com.adevinta.spark.icons

                import com.adevinta.spark.icons.SparkIcon.DrawableRes

                /**
                 * A collection of static icons from Spark for leboncoin.
                 *
                 * This object provides access to all static vector icons as drawable resources.
                 * Each icon is available as a property that returns a [SparkIcon.DrawableRes] or a [SparkIcon.Vector],
                 * ensuring type safety and consistency across the icon system.
                 *
                 * @see SparkIcon.DrawableRes
                 * @see SparkIcon.Vector
                 */
                public object LeboncoinIcons

                """.trimIndent(),
            )
            it.newLine()
            input.files()
                .map { it.normalize() }
                .sorted()
                .map { it.nameWithoutExtension.removePrefix("spark_icons_lbc_").toPascalCase() to it.nameWithoutExtension }
                .forEach { (name, resource) ->
                    it.write("""public val LeboncoinIcons.$name: DrawableRes get() = DrawableRes(R.drawable.$resource)""")
                    it.newLine()
                }
        }
        if (!quiet) echo("\n✅ " + output.absolutePathString())
    }
}

@OptIn(ExperimentalPathApi::class)
fun Path.files() = walk().filter { it: Path -> it.isRegularFile() }
fun Path.files(predicate: (Path) -> Boolean) = files().filter(predicate)
fun String.exec() = Runtime.getRuntime().exec(this).text()
fun Process.text() = apply { waitFor() }.inputStream.bufferedReader().use { it.readText().trim() }
fun String.toPascalCase(): String = split("_").joinToString(separator = "") { it.capitalize() }
fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
    else it.toString()
}

SparkIcons().main(args)
