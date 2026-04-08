#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("https://maven.google.com")
@file:DependsOn("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
@file:DependsOn("com.android.tools:sdk-common:31.1.1")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:Import("../utils/clikt.main.kts")
@file:Import("../utils/ext.main.kts")
@file:Import("../utils/files.main.kts")

import com.android.ide.common.vectordrawable.Svg2Vector
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import java.lang.System.lineSeparator
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.outputStream
import kotlin.io.path.readText

/**
 * SVG2AVD will compress SVG files and convert them to AVD.
 * - https://github.com/svg/svgo
 * - https://central.sonatype.com/artifact/com.android.tools/sdk-common
 */
class SVG2AVD : CliktCommand(
    name = "svg2avd.main.kts",
) {
    override fun help(context: Context) = "⚙️ svg2avd: Convert SVG files to Android Vector Drawables (AVD)."

    private val input: Path by option("-i", "--input", help = "SVG assets input directory")
        .path(mustExist = true, canBeFile = false, mustBeReadable = true)
        .prompt("SVG assets input directory")

    private val output: Path by option("-o", "--output", help = "AVD assets output directory")
        .path(mustExist = true, canBeFile = false, mustBeWritable = true)
        .defaultLazy { input }

    private val precision: Int? by option("-p", "--precision", help = "Set number of digits in the fractional part (svgo)")
        .int().check("must be a positive integer") { it >= 0 }

    private val prefix: String by option("-x", "--prefix", help = "XML file prefix")
        .default("")

    private val copyright: Path? by option("-c", "--copyright", help = "Copyright header for AVD")
        .path(mustExist = false, canBeDir = false, mustBeReadable = true)

    private val quiet by quiet()

    override fun run() = input
        .ifNotNull(precision) { compress(precision = it) }
        .filesWithExtension("svg")
        .toList().parallelStream().forEach { it.convert(output) }
        .also { if (!quiet) echo(lineSeparator() + "✅ " + output.absolutePathString()) }

    private fun Path.compress(precision: Int) = createTempDirectory().also {
        val svgo = exec(
            "svgo",
            "--folder=${absolutePathString()}",
            "--output=${it.absolutePathString()}",
            "--precision=$precision",
            "--multipass",
        )
        if (!quiet) echo(svgo + lineSeparator())
    }

    private fun Path.convert(output: Path) = output
        .resolve("$prefix${nameWithoutExtension.toSnakeCase()}.xml")
        .also { if (!quiet) echo("⚙️ Converting $name to $it") }
        .outputStream().use {
            copyright?.takeIf { it.isRegularFile() }?.readText()?.toByteArray()?.let(it::write)
            Svg2Vector.parseSvgToXml(this, it)
        }

    private fun exec(vararg cmd: String) = Runtime.getRuntime().exec((arrayOf("cmd", "/c").takeIf { isWindows() } ?: emptyArray()) + cmd).text()
    private fun Process.text() = apply { waitFor() }.inputStream.bufferedReader().use { it.readText().trim() }
    private fun <I : Any, O : Any?> I.ifNotNull(it: O?, block: I.(O) -> I): I = if (it != null) block(it) else this
    private fun isWindows() = System.getProperty("os.name").orEmpty().startsWith("windows", ignoreCase = true)
}

SVG2AVD().main(args)
