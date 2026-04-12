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
@file:DependsOn("org.jetbrains.kotlin:kotlin-stdlib:2.3.20")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:Import("../utils/clikt.main.kts")
@file:Import("../utils/ext.main.kts")
@file:Import("../utils/files.main.kts")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.isRegularFile
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText

/**
 * Generates LeboncoinIcons Kotlin files for KMP source sets.
 *
 * Given an input directory containing drawable XML files, generates:
 * - `androidMain/kotlin/.../LeboncoinIcons.android.kt` using `DrawableRes(R.drawable.x)`
 * - `nonAndroidMain/kotlin/.../LeboncoinIcons.nonandroid.kt` using `Resource(Res.drawable.x)`
 */
class LeboncoinIcons : CliktCommand(
    name = "spark-icons-kt.main.kts",
) {
    override fun help(context: Context) = "⚙️ LeboncoinIcons: Generate KMP icon Kotlin files for all source sets"

    private val input: Path by option("-i", "--in", help = "AVD assets input directory")
        .path(mustExist = true, mustBeReadable = true, canBeFile = false)
        .required()

    private val outputDir: Path by option("-o", "--output-dir", help = "Output directory (e.g. spark-icons/src)")
        .path(mustExist = true, canBeFile = false)
        .required()

    private val copyright: Path? by option("-c", "--copyright", help = "Copyright header file")
        .path(mustExist = false, canBeDir = false, mustBeReadable = true)

    private val quiet by quiet()

    private val packagePath = "com/adevinta/spark/icons"

    override fun run() {
        val icons = input.files()
            .filter { it.nameWithoutExtension.startsWith("spark_icons_lbc_") }
            .map { it.normalize() }
            .sorted()
            .map { it.nameWithoutExtension.removePrefix("spark_icons_lbc_").toPascalCase() to it.nameWithoutExtension }
            .toList()

        val copyrightHeader = copyright?.takeIf { it.isRegularFile() }?.readText() ?: ""

        generateFile(
            subPath = "androidMain/kotlin/$packagePath/LeboncoinIcons.android.kt",
            importLine = "import com.adevinta.spark.icons.SparkIcon.DrawableRes",
            icons = icons,
            copyrightHeader = copyrightHeader,
        ) { name, resource -> """public val LeboncoinIcons.$name: DrawableRes get() = DrawableRes(R.drawable.$resource)""" }

        generateFile(
            subPath = "nonAndroidMain/kotlin/$packagePath/LeboncoinIcons.nonandroid.kt",
            importLine = "import com.adevinta.spark.icons.SparkIcon.Resource",
            icons = icons,
            copyrightHeader = copyrightHeader,
        ) { name, resource -> """public val LeboncoinIcons.$name: Resource get() = Resource(Res.drawable.$resource)""" }

        if (!quiet) {
            echo("\n✅ Generated ${icons.size} icon properties in:")
            echo("   - ${outputDir.resolve("androidMain/kotlin/$packagePath/LeboncoinIcons.android.kt").absolutePathString()}")
            echo("   - ${outputDir.resolve("nonAndroidMain/kotlin/$packagePath/LeboncoinIcons.nonandroid.kt").absolutePathString()}")
        }
    }

    private fun generateFile(
        subPath: String,
        importLine: String,
        icons: List<Pair<String, String>>,
        copyrightHeader: String,
        declarationFor: (name: String, resource: String) -> String,
    ) {
        val output = outputDir.resolve(subPath)
        output.parent.createDirectories()
        output.bufferedWriter().use { writer ->
            writer.write(copyrightHeader)
            writer.write(
                """
                @file:Suppress("UnusedReceiverParameter", "unused", "ktlint")

                package com.adevinta.spark.icons

                $importLine

                """.trimIndent(),
            )
            writer.newLine()
            icons.forEach { (name, resource) ->
                writer.write(declarationFor(name, resource))
                writer.newLine()
            }
        }
    }
}

LeboncoinIcons().main(args)
