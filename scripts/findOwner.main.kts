#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("https://maven.google.com")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.0.3")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.15.2")

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.file
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

// Print startup progress
println("🔧 Initializing file owner finder...")

// Create a shared Moshi instance for JSON serialization/deserialization
@OptIn(ExperimentalStdlibApi::class)
private val sharedMoshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

// Data classes for input/output
data class FilePathInput(val filepath: String)
data class FilePathInputList(val files: List<FilePathInput> = emptyList())

data class OwnerMapping(val filepath: String, val owner: String?)
data class OwnerMappingList(val mappings: List<OwnerMapping> = emptyList())

// Team ownership detection
class TeamOwnershipDetector {
    private val fileToTeamCache = ConcurrentHashMap<String, String?>() // file path -> team (cached lookups)
    private val backstageFileCache = ConcurrentHashMap<String, String?>() // directory path -> team from backstage file

    /**
     * Efficiently finds the team for a given file by walking up the directory hierarchy on-demand.
     * Uses caching to avoid repeated directory traversals and backstage file parsing.
     *
     * @param file The file to find team ownership for
     * @return The team name if found, null otherwise
     */
    fun getTeamForFile(file: File): String? {
        val filePath = file.absolutePath

        // Check if we've already cached the result for this exact file
        val cachedResult = fileToTeamCache[filePath]
        if (cachedResult != null) { // null means not cached, empty string means "no team found"
            return cachedResult.ifEmpty { null }
        }

        // Walk up the directory hierarchy until we find a backstage file or reach the root
        var currentDir = file.parentFile
        while (currentDir != null) {
            val dirPath = currentDir.absolutePath

            // Check if we've already parsed a backstage file for this directory
            val cachedTeam = backstageFileCache[dirPath]
            if (cachedTeam != null) {
                // Cache the result for the original file
                fileToTeamCache[filePath] = cachedTeam
                return cachedTeam.ifEmpty { null }
            }

            // Look for backstage.yml or backstage.yaml in this directory
            val backstageFile = listOf("backstage.yml", "backstage.yaml")
                .map { File(currentDir, it) }
                .find { it.exists() }

            if (backstageFile != null) {
                val team = parseBackstageFile(backstageFile)
                // Cache the result for this directory (even if null/empty)
                backstageFileCache[dirPath] = team ?: ""
                // Cache the result for the original file
                fileToTeamCache[filePath] = team ?: ""
                return team
            }

            currentDir = currentDir.parentFile
        }

        // No team found, cache empty result to avoid future lookups
        fileToTeamCache[filePath] = ""
        return null
    }

    private fun parseBackstageFile(file: File): String? {
        return runCatching {
            val content = file.readText()

            /**
             * Matches team ownership declarations in backstage.yml files.
             *
             * Pattern breakdown:
             * - `owner:` - Literal "owner:" key
             * - `\s*` - Optional whitespace after colon
             * - `group:default/` - Literal "group:default/" prefix
             * - `(.+)` - Capture group for the team name (one or more characters)
             *
             * Example matches:
             * - `owner: group:default/team-mobile` -> captures "team-mobile"
             * - `owner:group:default/platform-team` -> captures "platform-team"
             * - `  owner:  group:default/design-system  ` -> captures "design-system"
             *
             * Non-matches:
             * - `owner: user:john.doe` ✗ (not a group)
             * - `maintainer: group:default/team-web` ✗ (wrong key)
             */
            val regex = """owner:\s*group:default/(.+)""".toRegex()
            regex.find(content)?.groupValues?.get(1)?.trim()
        }.onFailure { e ->
            println("   ⚠️ Failed to parse ${file.name}: ${e.message}")
        }.getOrNull()
    }

    /**
     * Returns statistics about the current cache state for debugging/reporting.
     */
    fun getCacheStats(): CacheStats {
        val uniqueTeams = fileToTeamCache.values.filterNotNull().filterNot { it.isEmpty() }.toSet().size
        val cachedFiles = fileToTeamCache.size
        return CacheStats(cachedFiles, uniqueTeams)
    }
}

data class CacheStats(
    val cachedFiles: Int,
    val uniqueTeams: Int,
)

/**
 * Parses input file based on its format (CSV or JSON)
 */
fun parseInputFile(inputFile: File, inputFormat: InputFormat): List<String> {
    return when (inputFormat) {
        InputFormat.CSV -> parseCsvFile(inputFile)
        InputFormat.JSON -> parseJsonFile(inputFile)
    }
}

/**
 * Parses a CSV file containing filepaths
 */
fun parseCsvFile(file: File): List<String> {
    return try {
        file.readLines()
            .drop(1) // Skip header if present
            .map { line ->
                line.trim().split(",").firstOrNull()?.trim() ?: ""
            }
            .filter { it.isNotEmpty() }
    } catch (e: Exception) {
        println("❌ Error parsing CSV file: ${e.message}")
        emptyList()
    }
}

/**
 * Parses a JSON file containing filepaths
 */
@OptIn(ExperimentalStdlibApi::class)
fun parseJsonFile(file: File): List<String> {
    return try {
        val adapter = sharedMoshi.adapter<FilePathInputList>()
        val jsonContent = file.readText()
        val data = adapter.fromJson(jsonContent) ?: FilePathInputList(emptyList())
        data.files.map { it.filepath }
    } catch (e: Exception) {
        println("❌ Error parsing JSON file: ${e.message}")
        emptyList()
    }
}

/**
 * Writes output file based on the specified format
 */
fun writeOutputFile(mappings: List<OwnerMapping>, outputFile: File, outputFormat: OutputFormat) {
    when (outputFormat) {
        OutputFormat.CSV -> writeCsvFile(mappings, outputFile)
        OutputFormat.JSON -> writeJsonFile(mappings, outputFile)
    }
}

/**
 * Writes mappings to a CSV file
 */
fun writeCsvFile(mappings: List<OwnerMapping>, file: File) {
    try {
        val csvContent = buildString {
            appendLine("filepath,owner")
            mappings.forEach { mapping ->
                appendLine("${mapping.filepath},${mapping.owner ?: ""}")
            }
        }
        file.writeText(csvContent)
    } catch (e: Exception) {
        println("❌ Error writing CSV file: ${e.message}")
    }
}

/**
 * Writes mappings to a JSON file
 */
@OptIn(ExperimentalStdlibApi::class)
fun writeJsonFile(mappings: List<OwnerMapping>, file: File) {
    try {
        val data = OwnerMappingList(mappings)
        val adapter = sharedMoshi.adapter<OwnerMappingList>()
        val jsonContent = adapter.toJson(data)
        file.writeText(jsonContent)
    } catch (e: Exception) {
        println("❌ Error writing JSON file: ${e.message}")
    }
}

class FindOwnerCommand : SuspendingCliktCommand(
    name = "find-owner",
) {
    private val inputFile by argument(
        name = "INPUT_FILE",
        help = "Input file containing filepaths (CSV or JSON format)",
    ).file(
        mustExist = true,
        canBeDir = false,
        mustBeReadable = true,
    )

    private val outputFile by argument(
        name = "OUTPUT_FILE",
        help = "Output file for owner mappings",
    ).file(
        canBeDir = false,
        mustBeWritable = true,
    )

    private val inputFormat by option(
        "--input-format", "-i",
        help = "Input file format (csv or json)",
        metavar = "FORMAT",
    ).enum<InputFormat>().default(InputFormat.CSV)

    private val outputFormat by option(
        "--output-format", "-o",
        help = "Output file format (csv or json)",
        metavar = "FORMAT",
    ).enum<OutputFormat>().default(OutputFormat.CSV)

    private val verbose by option(
        "--verbose", "-v",
        help = "Enable verbose output",
    ).flag()

    override fun help(context: Context): String {
        return """
        Find team ownership for files by looking up the directory hierarchy.

        This tool takes a list of filepaths and finds the team owner for each file
        by walking up the directory tree looking for backstage.yml or backstage.yaml files.

        Usage: find-owner [OPTIONS] INPUT_FILE OUTPUT_FILE

        Arguments:
            INPUT_FILE    Input file containing filepaths (CSV or JSON format)
            OUTPUT_FILE   Output file for owner mappings

        Options:
            -i, --input-format FORMAT   Input file format: csv or json (default: csv)
            -o, --output-format FORMAT  Output file format: csv or json (default: csv)
            -v, --verbose              Enable verbose output

        Input CSV format:
            filepath
            /path/to/file1.kt
            /path/to/file2.kt
            ...

        Input JSON format:
            {
              "files": [
                {"filepath": "/path/to/file1.kt"},
                {"filepath": "/path/to/file2.kt"}
              ]
            }

        Output CSV format:
            filepath,owner
            /path/to/file1.kt,team-mobile
            /path/to/file2.kt,platform-team
            ...

        Output JSON format:
            {
              "mappings": [
                {"filepath": "/path/to/file1.kt", "owner": "team-mobile"},
                {"filepath": "/path/to/file2.kt", "owner": "platform-team"}
              ]
            }
        """.trimIndent()
    }

    override suspend fun run() {
        findOwners()
    }

    private suspend fun findOwners() {
        echo("🚀 File Owner Finder")
        echo("====================")

        if (verbose) {
            echo("📁 Input file: ${inputFile.absolutePath}")
            echo("📁 Output file: ${outputFile.absolutePath}")
            echo("📄 Input format: $inputFormat")
            echo("📄 Output format: $outputFormat")
        }

        // Parse input file
        echo("📖 Parsing input file...")
        val filepaths = parseInputFile(inputFile, inputFormat)
        
        if (filepaths.isEmpty()) {
            echo("❌ No filepaths found in input file")
            throw Abort()
        }

        echo("📝 Found ${filepaths.size} filepaths to process")

        // Initialize team detector
        val teamDetector = TeamOwnershipDetector()

        // Process filepaths in parallel
        echo("🔍 Finding team ownership for files...")
        val progressCounter = AtomicInteger(0)
        val totalFiles = filepaths.size.toDouble()

        val mappings = supervisorScope {
            filepaths.map { filepath ->
                async(Dispatchers.IO) {
                    try {
                        val currentProgress = progressCounter.incrementAndGet()
                        if (totalFiles > 0 && currentProgress % 100 == 0 || currentProgress == filepaths.size) {
                            val percentage = (currentProgress / totalFiles * 100).toInt()
                            print("\r   📊 Progress: $percentage% ($currentProgress/${filepaths.size})")
                        }

                        val file = File(filepath)
                        val owner = teamDetector.getTeamForFile(file)
                        OwnerMapping(filepath, owner)
                    } catch (e: Exception) {
                        if (verbose) {
                            echo("   ⚠️ Error processing filepath $filepath: ${e.message}")
                        }
                        OwnerMapping(filepath, null)
                    }
                }
            }.awaitAll()
        }

        println() // Add newline after progress updates

        // Write output file
        echo("💾 Writing output file...")
        writeOutputFile(mappings, outputFile, outputFormat)

        // Print summary
        val foundOwners = mappings.count { it.owner != null }
        val uniqueTeams = mappings.mapNotNull { it.owner }.toSet().size

        echo("✅ Processing completed!")
        echo("📊 Summary:")
        echo("  • Total files processed: ${mappings.size}")
        echo("  • Files with owners found: $foundOwners")
        echo("  • Unique teams found: $uniqueTeams")
        echo("  • Output file: ${outputFile.absolutePath}")

        if (verbose) {
            val cacheStats = teamDetector.getCacheStats()
            echo("  • Cache stats: ${cacheStats.cachedFiles} files cached, ${cacheStats.uniqueTeams} unique teams")
        }
    }
}

enum class InputFormat {
    CSV, JSON
}

enum class OutputFormat {
    CSV, JSON
}

// Main execution
runBlocking {
    FindOwnerCommand().main(args)
} 