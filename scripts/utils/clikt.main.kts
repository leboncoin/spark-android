@file:DependsOn(
    "com.github.ajalt.clikt:clikt-jvm:5.1.0",
)


import com.github.ajalt.clikt.core.BaseCliktCommand
import com.github.ajalt.clikt.parameters.options.OptionWithValues
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path
import java.nio.file.Paths


fun BaseCliktCommand<*>.dryRun() = option("--dry-run", help = "Dry run").flag()
fun BaseCliktCommand<*>.verbose() = option("--verbose", "-v", help = "Verbose").flag()
fun BaseCliktCommand<*>.quiet() = option("-q", "--quiet", help = "Print errors only").flag()

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

/**
 * Repeatable `--input path` option. The name is derived from the filename without extension.
 * Usage: `--input spark.txt --input spark-icons.txt --input apk.txt`
 * In the command, call `.associateBy { it.nameWithoutExtension }` to get a `Map<String, Path>`.
 */
fun BaseCliktCommand<*>.inputs(help: String = "Input file (repeatable, name derived from filename)") =
    option("--input", help = help)
        .path(mustExist = true, mustBeReadable = true, canBeDir = false)
        .multiple()
