#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("https://maven.google.com")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.0.3")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.15.2")

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.Abort
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.unique
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.meta
import kotlinx.html.option
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.select
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.unsafe
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

// Print startup progress
println("🔧 Initializing component usage analyzer...")

// Create a shared Moshi instance for JSON serialization/deserialization throughout the script
@OptIn(ExperimentalStdlibApi::class)
private val sharedMoshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

/**
 * Data class representing a component to search for.
 *
 * @property name The name of the component (e.g., "ButtonFilled")
 * @property fqn The fully qualified name of the component (e.g., "com.example.components.buttons.ButtonFilled")
 */
data class Component(val name: String, val fqn: String, val documentation: String, val componentGroup: String)
data class Components(val components: List<Component> = emptyList())

// Default exclusion patterns (can be overridden via command line)
val DEFAULT_EXCLUSION_PATTERNS = listOf(
    "**/build/**",
    "**/.gradle/**",
    "**/.idea/**",
    "**/generated/**",
    "**/tmp/**",
    "**/.git/**",
    "**/test/**",
    "**/androidTest/**",
)

// Data classes
data class ComponentUsage(
    val file: File,
    val lineNumber: Int,
    val context: String,
    val containingFunction: String? = null,
    val team: String? = null,
)

data class ComponentDefinition(
    val file: File,
    val lineNumber: Int,
    val dependencies: List<String> = emptyList(),
    val packageName: String = "",
)

data class CacheStats(
    val cachedFiles: Int,
    val uniqueTeams: Int,
)

// Data class to hold file analysis data for phase 1
data class AnalysisData(
    val file: File,
    val content: String,
    val lines: List<String>,
    val imports: Set<String>,
    val packageName: String,
    val team: String?,
)

data class AnalysisResult(
    val directUsages: ConcurrentHashMap<String, MutableList<ComponentUsage>> = ConcurrentHashMap(),
    val wrapperComponents: ConcurrentHashMap<String, String> = ConcurrentHashMap(), // wrapper -> component
    val wrapperUsages: ConcurrentHashMap<String, MutableList<ComponentUsage>> = ConcurrentHashMap(),
    val wrapperDefinitions: ConcurrentHashMap<String, ComponentDefinition> = ConcurrentHashMap(),
    val multiComponentWrappers: ConcurrentHashMap<String, List<String>> = ConcurrentHashMap(), // wrapper -> multiple components
    val teamUsages: ConcurrentHashMap<String, ConcurrentHashMap<String, Int>> = ConcurrentHashMap(), // team -> component -> count
) {
    fun addDirectUsage(
        component: String,
        file: File,
        lineNumber: Int,
        context: String,
        containingFunction: String? = null,
        team: String? = null,
    ) {
        directUsages.getOrPut(component) { mutableListOf() }
            .add(ComponentUsage(file, lineNumber, context, containingFunction, team))

        // Track team usage
        if (team != null) {
            teamUsages.getOrPut(team) { ConcurrentHashMap() }
                .merge(component, 1, Int::plus)
        }
    }

    fun addWrapperComponent(
        wrapperName: String,
        wrappedComponent: String,
        file: File,
        lineNumber: Int,
        packageName: String = "",
    ) {
        wrapperComponents[wrapperName] = wrappedComponent
        wrapperDefinitions[wrapperName] = ComponentDefinition(file, lineNumber, emptyList(), packageName)
    }

    fun addMultiComponentWrapper(
        wrapperName: String,
        wrappedComponents: List<String>,
        file: File,
        lineNumber: Int,
        packageName: String = "",
    ) {
        multiComponentWrappers[wrapperName] = wrappedComponents
        wrapperDefinitions[wrapperName] = ComponentDefinition(file, lineNumber, wrappedComponents, packageName)
    }

    fun addWrapperUsage(
        wrapperName: String,
        file: File,
        lineNumber: Int,
        context: String,
        containingFunction: String? = null,
        team: String? = null,
    ) {
        wrapperUsages.getOrPut(wrapperName) { mutableListOf() }
            .add(ComponentUsage(file, lineNumber, context, containingFunction, team))

        // Track team usage for wrapped components
        if (team != null) {
            val wrappedComponent = wrapperComponents[wrapperName]
            if (wrappedComponent != null) {
                teamUsages.getOrPut(team) { ConcurrentHashMap() }
                    .merge(wrappedComponent, 1, Int::plus)
            }

            val multiWrappedComponents = multiComponentWrappers[wrapperName]
            multiWrappedComponents?.forEach { component ->
                teamUsages.getOrPut(team) { ConcurrentHashMap() }
                    .merge(component, 1, Int::plus)
            }
        }
    }

    fun getEffectiveUsage(): Map<String, Int> {
        val effectiveUsage = mutableMapOf<String, Int>()

        // First, collect all wrapper definitions to identify which direct usages to subtract
        val wrapperDefinitions = mutableSetOf<String>()
        wrapperComponents.keys.forEach { wrapper ->
            wrapperDefinitions.add(wrapper.substringAfterLast('.'))
        }
        multiComponentWrappers.keys.forEach { wrapper ->
            wrapperDefinitions.add(wrapper.substringAfterLast('.'))
        }

        // Add direct usages, subtracting those that are wrapper definitions
        directUsages.forEach { (component, usages) ->
            val adjustedUsages = usages.count { usage ->
                // Check if this usage is a wrapper definition
                val containingFunction = usage.containingFunction
                !wrapperDefinitions.contains(containingFunction)
            }
            effectiveUsage.merge(component, adjustedUsages) { existing, new -> existing + new }
        }

        // Add single-component wrapper usages
        wrapperUsages.forEach { (wrapperName, usages) ->
            wrapperComponents[wrapperName]?.let { wrappedComponent ->
                effectiveUsage.merge(wrappedComponent, usages.size) { existing, new -> existing + new }
            }
        }

        // Add multi-component wrapper usages (distributed equally)
        wrapperUsages.forEach { (wrapperName, usages) ->
            multiComponentWrappers[wrapperName]?.takeIf { it.isNotEmpty() }?.let { wrappedComponents ->
                val usagePerComponent = usages.size / wrappedComponents.size
                wrappedComponents.forEach { component ->
                    effectiveUsage.merge(component, usagePerComponent) { existing, new -> existing + new }
                }
            }
        }

        return effectiveUsage
    }
}

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

// Parallel file discovery function with count tracking
suspend fun discoverKotlinFilesParallelWithCount(
    sourceDir: File,
    exclusionPatterns: List<String>,
    verbose: Boolean = false,
    counter: AtomicInteger? = null,
): List<File> {
    val cpuCores = Runtime.getRuntime().availableProcessors()

    // Get immediate subdirectories (first level children)
    // If we find more than 1 subdir we can parallelize the file discovery
    val topLevelDirs = sourceDir.listFiles()?.filter { it.isDirectory } ?: emptyList()
    val fileCounter = counter ?: AtomicInteger(0)

    return if (topLevelDirs.isEmpty()) {
        val filesInDir = mutableListOf<File>()
        sourceDir.walkTopDown()
            .onEnter {
                // Optimization: If a directory itself matches an exclusion pattern, don't descend into it.
                // This requires careful handling of how baseDir is used in shouldExcludeFile.
                // For now, we filter files, not directories, to keep it simpler.
                !shouldExcludeFile(it, sourceDir, exclusionPatterns)
            }
            .filter { it.isFile && it.extension == "kt" }
            .forEach { file ->
                val count = fileCounter.incrementAndGet()
                // Update discovery progress periodically
                print("\r   📊 Files discovered: $count")
                if (!shouldExcludeFile(file, sourceDir, exclusionPatterns)) {
                    filesInDir.add(file)
                }
            }
        println() // Add a newline after the progress updates
        filesInDir
    } else {
        // Split top-level directories into chunks based on CPU cores
        val dirChunks = if (topLevelDirs.size <= cpuCores) {
            // If we have fewer directories than cores, each gets its own chunk
            topLevelDirs.map { listOf(it) }
        } else {
            // Otherwise, distribute directories evenly across cores
            topLevelDirs.chunked((topLevelDirs.size + cpuCores - 1) / cpuCores)
        }

        if (verbose) {
            println("   📁 Processing ${topLevelDirs.size} top-level directories in ${dirChunks.size} parallel chunks")
        }

        // Process each chunk in parallel
        val results = supervisorScope {
            dirChunks.mapIndexed { chunkIndex, dirsInChunk ->
                async(Dispatchers.IO) {
                    try {
                        val chunkFiles = mutableListOf<File>()

                        dirsInChunk.forEach { dir ->
                            dir.walkTopDown()
                                .onEnter {
                                    !shouldExcludeFile(it, sourceDir, exclusionPatterns)
                                }
                                .filter { it.isFile && it.extension == "kt" }
                                .forEach { file ->
                                    val count = fileCounter.incrementAndGet()
                                    // Update discovery progress periodically
                                    print("\r   📊 Files discovered: $count")
                                    chunkFiles.add(file)
                                }
                        }
                        if (verbose) {
                            println("\n   ✅ Chunk ${chunkIndex + 1}/${dirChunks.size}: Found ${chunkFiles.size}")
                        }
                        chunkFiles
                    } catch (e: Exception) {
                        println("\n   ⚠️ Error processing chunk ${chunkIndex + 1}: ${e.message}")
                        emptyList()
                    }
                }
            }.awaitAll()
        }

        println() // Add a newline after the progress updates
        results.flatten()
    }
}

// Main analyzer class
class ComponentAnalyzer(
    private val components: List<Component>,
    private val teamDetector: TeamOwnershipDetector,
    private val contextLines: Int = 3,
) {
    // Optimization: Use ConcurrentHashMap for caches to allow parallel access during file processing
    private val fileContentCache = ConcurrentHashMap<String, String>()
    private val importCache = ConcurrentHashMap<String, Set<String>>()
    private val packageCache = ConcurrentHashMap<String, String>()

    // Optimization: Compile regex patterns once
    /**
     * Matches @Composable function definitions with optional visibility modifiers.
     *
     * Pattern breakdown:
     * - `@Composable\s+` - Matches @Composable annotation followed by whitespace
     * - `(?:public\s+|private\s+|internal\s+)?` - Optional visibility modifier
     * - `fun\s+(\w+)\s*` - Function keyword, capture group for function name
     * - `\([^)]*\)\s*` - Parameter list (any characters except closing paren)
     * - `\{([^}]*(?:\{[^}]*\}[^}]*)*)\}` - Function body with nested braces support
     *
     * Example matches:
     * - `@Composable fun MyButton() { ButtonFilled() }`
     * - `@Composable private fun CustomCard(title: String) { ... }`
     * - `@Composable internal fun NestedComponent() { if (condition) { ButtonFilled() } }`
     */
    private val composableFunctionPattern: Pattern = Pattern.compile(
        """@Composable\s+(?:public\s+|private\s+|internal\s+)?fun\s+(\w+)\s*\([^)]*\)\s*\{([^}]*(?:\{[^}]*\}[^}]*)*)\}""",
        Pattern.DOTALL,
    )

    /**
     * Matches @Preview annotation with optional parameters.
     *
     * Pattern breakdown:
     * - `@Preview` - The Preview annotation
     * - `(?:\s*\([^)]*\))?` - Optional parameters in parentheses
     */
    private val previewAnnotationPattern: Pattern = Pattern.compile(
        """@Preview(?:\s*\([^)]*\))?""",
        Pattern.DOTALL
    )

    /**
     * Matches function definitions (with or without @Composable) for finding containing functions.
     * Enhanced to handle multi-line function declarations with annotations and modifiers.
     *
     * Pattern breakdown:
     * - `(?:(?:@\w+(?:\s*\([^)]*\))?\s+)*)` - Zero or more annotations (with optional parameters)
     * - `(?:public\s+|private\s+|internal\s+|external\s+|inline\s+|suspend\s+)*` - Zero or more modifiers
     * - `fun\s+(\w+)\s*\(` - Function keyword, capture group for function name, opening paren
     *
     * Example matches:
     * - `fun onCreate()` -> captures "onCreate"
     * - `@Composable fun MyButton()` -> captures "MyButton"
     * - `private fun helper()` -> captures "helper"
     * - Multi-line: `@Composable\n@ExperimentalSparkApi\nfun AwarenessCard()` -> captures "AwarenessCard"
     */
    private val functionPatternForContaining: Pattern =
        Pattern.compile(
            """(?:(?:@\w+(?:\s*\([^)]*\))?\s+)*)(?:public\s+|private\s+|internal\s+|external\s+|inline\s+|suspend\s+)*fun\s+(\w+)\s*\(""",
            Pattern.DOTALL
        )

    /**
     * Matches import statements to extract fully qualified class names.
     *
     * Pattern breakdown:
     * - `import\s+` - Import keyword followed by whitespace
     * - `([^\s\n]+)` - Capture group for the import path (non-whitespace characters)
     *
     * Example matches:
     * - `import com.adevinta.example.components.buttons.ButtonFilled` -> captures full path
     * - `import kotlinx.coroutines.*` -> captures "kotlinx.coroutines.*"
     * - `import androidx.compose.runtime.Composable` -> captures full path
     */
    private val importPatternRegex: Pattern = Pattern.compile("""import\s+([^\s\n]+)""")

    /**
     * Matches package declarations to extract the current file's package name.
     *
     * Pattern breakdown:
     * - `package\s+` - Package keyword followed by whitespace
     * - `([^\s\n]+)` - Capture group for the package name (non-whitespace characters)
     *
     * Example matches:
     * - `package com.example.myapp.ui` -> captures "com.example.myapp.ui"
     * - `package com.adevinta.example.catalog` -> captures "com.adevinta.example.catalog"
     */
    private val packagePatternRegex: Pattern = Pattern.compile("""package\s+([^\s\n]+)""")

    // Cache for compiled component patterns to avoid recompiling them for every file
    private val componentRegexCache = ConcurrentHashMap<String, Pattern>()

    /**
     * Gets or creates a cached regex pattern for matching component usage.
     *
     * Pattern breakdown:
     * - `(?<![a-zA-Z0-9_])` - Negative lookbehind: ensures component name is not preceded by word characters
     * - `$componentName` - The exact component name (e.g., "ButtonFilled")
     * - `\s*\(` - Optional whitespace followed by opening parenthesis
     *
     * This prevents false matches like "MyButtonFilled" when searching for "ButtonFilled".
     *
     * Example matches for "ButtonFilled":
     * - `ButtonFilled()` ✓
     * - `ButtonFilled (` ✓ (with space)
     * - `  ButtonFilled(text = "Click")` ✓
     * - `MyButtonFilled()` ✗ (prevented by negative lookbehind)
     * - `buttonFilled()` ✗ (case sensitive)
     *
     * @param componentName The name of the component to match
     * @return Compiled regex pattern for the component
     */
    private fun getComponentPattern(componentName: String): Pattern {
        return componentRegexCache.getOrPut(componentName) {
            Pattern.compile("""(?<![a-zA-Z0-9_])$componentName\s*\(""")
        }
    }

    private fun getCachedContent(file: File): String {
        return fileContentCache.getOrPut(file.absolutePath) { file.readText() }
    }


    private fun getCachedImports(file: File, content: String): Set<String> {
        return importCache.getOrPut(file.absolutePath) {
            extractImports(content)
        }
    }

    private fun getCachedPackage(file: File, content: String): String {
        return packageCache.getOrPut(file.absolutePath) {
            extractPackageName(content)
        }
    }

    suspend fun analyze(sourceFiles: List<File>, excludePreviews: Boolean): AnalysisResult {
        val result = AnalysisResult()
        val cpuCores = Runtime.getRuntime().availableProcessors()

        println("📁 Analyzing ${sourceFiles.size} Kotlin files using $cpuCores CPU cores...")
        val progressCounter = AtomicInteger(0)
        val totalFiles = sourceFiles.size.toDouble()

        // Phase 1: Find all direct component usages and wrapper definitions using coroutines
        println("🔍 Phase 1: Finding direct component usages and wrapper definitions...")
        val unifiedAnalysisTime = measureTimeMillis {
            supervisorScope {
                sourceFiles.map { file ->
                    async(Dispatchers.IO) {
                        try {
                            val currentProgress = progressCounter.incrementAndGet()
                            if (totalFiles > 0 && currentProgress % 100 == 0 || currentProgress == sourceFiles.size) { // Update progress periodically
                                val percentage = (currentProgress / totalFiles * 100).toInt()
                                print("\r   📊 Unified Analysis Progress: $percentage% ($currentProgress/${sourceFiles.size})")
                            }

                            val content = getCachedContent(file)
                            if (content.isEmpty()) return@async // Skip if file read failed or empty

                            val lines = content.lines() // Do this once
                            val imports = getCachedImports(file, content)
                            val currentPackageName = getCachedPackage(file, content)
                            val team = teamDetector.getTeamForFile(file)

                            val data = AnalysisData(
                                file = file,
                                content = content,
                                lines = lines,
                                imports = imports,
                                packageName = currentPackageName,
                                team = team,
                            )
                            analyzeFileForDirectUsages(
                                file = data.file,
                                content = data.content,
                                lines = data.lines,
                                imports = data.imports,
                                team = data.team,
                                excludePreviews= excludePreviews,
                                result = result,
                            )
                            analyzeFileForWrapperComponents(
                                file = data.file,
                                content = data.content,
                                imports = data.imports,
                                packageName = data.packageName,
                                result = result,
                            )
                        } catch (e: Exception) {
                            println("\n   ⚠️ Error analyzing file ${file.name}: ${e.message}")
                        }
                    }
                }.awaitAll()
            }
            println()
        }
        println("   ⏱️ Phase 1 completed in ${unifiedAnalysisTime}ms")

        // Phase 2: Find usages of wrapper components
        println("📊 Phase 2: Analyzing wrapper usages...")
        progressCounter.set(0)
        val phase2Time = measureTimeMillis {
            supervisorScope {
                sourceFiles.map { file ->
                    async(Dispatchers.IO) {
                        try {
                            val currentProgress = progressCounter.incrementAndGet()
                            if (totalFiles > 0 && currentProgress % 100 == 0 || currentProgress == sourceFiles.size) {
                                val percentage = (currentProgress / totalFiles * 100).toInt()
                                print("\r   📊 Wrapper Usage Analysis Progress: $percentage% ($currentProgress/${sourceFiles.size})")
                            }

                            val content = getCachedContent(file) // Content should be cached from previous pass
                            if (content.isEmpty()) return@async

                            val lines = content.lines() // Recalculate or retrieve if not passed
                            val currentPackageName = getCachedPackage(file, content) // Should be cached
                            val imports = getCachedImports(file, content) // Should be cached
                            val team = teamDetector.getTeamForFile(file) // Can be re-fetched

                            analyzeFileForWrapperUsages(file, content, lines, currentPackageName, imports, team, excludePreviews, result)
                        } catch (e: Exception) {
                            println("\n   ⚠️ Error analyzing wrapper usages in file ${file.name}: ${e.message}")
                        }
                    }
                }.awaitAll()
            }
            println()
        }
        println("   ⏱️ Phase 2 completed in ${phase2Time}ms")

        // Phase 3: Discover team ownership for all discovered components and wrappers
        println("👥 Phase 3: Discovering team ownership...")
        val allFilesToAnalyze = mutableSetOf<File>()

        // Collect all files that have component usages
        result.directUsages.values.flatten().forEach { usage -> allFilesToAnalyze.add(usage.file) }
        result.wrapperUsages.values.flatten().forEach { usage -> allFilesToAnalyze.add(usage.file) }

        // Collect all files that define wrapper components
        result.wrapperDefinitions.values.forEach { definition -> allFilesToAnalyze.add(definition.file) }

        val phase3Time = measureTimeMillis {
            println("   📂 Analyzing team ownership for ${allFilesToAnalyze.size} relevant files...")

            // Perform on-demand team discovery for these specific files
            val processedFiles = AtomicInteger(0)
            supervisorScope {
                allFilesToAnalyze.chunked(50).map { fileChunk ->
                    async(Dispatchers.IO) {
                        try {
                            fileChunk.forEach { file ->
                                teamDetector.getTeamForFile(file) // This will cache the result
                                val current = processedFiles.incrementAndGet()
                                if (current % 20 == 0 || current == allFilesToAnalyze.size) {
                                    print("\r   📊 Team discovery progress: $current/${allFilesToAnalyze.size} files")
                                }
                            }
                        } catch (e: Exception) {
                            println("\n   ⚠️ Error discovering team ownership: ${e.message}")
                        }
                    }
                }.awaitAll()
            }
            println()
        }
        println("   ⏱️ Phase 3 completed in ${phase3Time}ms")

        val totalAnalysisTime = unifiedAnalysisTime + phase2Time + phase3Time
        println("🚀 Total analysis time: ${totalAnalysisTime}ms (${totalAnalysisTime / 1000.0}s)")

        // Report team discovery statistics
        val cacheStats = teamDetector.getCacheStats()
        println("   📊 Team ownership: analyzed ${allFilesToAnalyze.size} files, found ${cacheStats.uniqueTeams} unique teams")

        return result
    }

    private fun analyzeFileForDirectUsages(
        file: File,
        content: String,
        lines: List<String>,
        imports: Set<String>,
        team: String?,
        excludePreviews: Boolean,
        result: AnalysisResult,
    ) {
        val availableComponents = getAvailableComponents(imports)

        availableComponents.forEach { (componentName, _) ->
            // Use word boundaries to ensure exact function name match
            val pattern = getComponentPattern(componentName)
            val matcher = pattern.matcher(content)

            var searchStart = 0
            while (matcher.find(searchStart)) {
                val lineNumber = content.substring(0, matcher.start()).count { it == '\n' } + 1
                val context = extractContext(lines, lineNumber)
                val containingFunction = findContainingFunction(content, matcher.start())

                // Skip if this usage is inside a preview composable and we're excluding previews
                if (excludePreviews && containingFunction?.isPreview == true) {
                    searchStart = matcher.end()
                    continue
                }

                // Skip if this is a function definition (not a function call)
                if (isFunctionDefinition(content, matcher.start(), componentName)) {
                    searchStart = matcher.end()
                    continue
                }

                result.addDirectUsage(
                    component = componentName,
                    file = file,
                    lineNumber = lineNumber,
                    context = context,
                    containingFunction = containingFunction?.name,
                    team = team,
                )
                searchStart = matcher.end()
            }
        }
    }

    private fun analyzeFileForWrapperComponents(
        file: File,
        content: String,
        imports: Set<String>,
        packageName: String,
        result: AnalysisResult,
    ) {
        val availableComponents = getAvailableComponents(imports)

        val matcher = composableFunctionPattern.matcher(content)
        while (matcher.find()) {
            val functionName = matcher.group(1)
            val functionBody = matcher.group(2)

            // Skip if this is already a known component
            if (components.any { it.name == functionName } || components.any { it.fqn == functionName }) {
                continue
            }

            // Find which components this function uses
            val usedComponents = mutableListOf<String>()
            val definedInPackage = packageName
            availableComponents.forEach { (componentName, _) ->
                // Check if the component is used *within* the body of this Composable function
                val compPattern = getComponentPattern(componentName)
                if (compPattern.matcher(functionBody).find()) {
                    usedComponents.add(componentName)
                }
            }

            if (usedComponents.isNotEmpty()) {
                val lineNumber = content.substring(0, matcher.start()).count { it == '\n' } + 1
                val wrapperFqn = if (definedInPackage.isNotEmpty()) "$definedInPackage.$functionName" else functionName

                if (usedComponents.size == 1) {
                    result.addWrapperComponent(
                        wrapperName = wrapperFqn,
                        wrappedComponent = usedComponents.first(),
                        file = file,
                        lineNumber = lineNumber,
                        packageName = definedInPackage,
                    )
                } else {
                    result.addMultiComponentWrapper(
                        wrapperName = wrapperFqn,
                        wrappedComponents = usedComponents,
                        file = file,
                        lineNumber = lineNumber,
                        packageName = definedInPackage,
                    )
                }
            }
        }
    }

    private fun analyzeFileForWrapperUsages(
        file: File,
        content: String,
        lines: List<String>,
        currentPackageName: String,
        imports: Set<String>,
        team: String?,
        excludePreviews: Boolean,
        result: AnalysisResult,
    ) {
        // We need all wrapper definitions to check for their usages.
        // This includes wrappers defined in any package.
        val allWrapperDefinitions = result.wrapperDefinitions.keys // FQNs of all discovered wrappers

        allWrapperDefinitions.forEach { wrapperFqn ->
            // Extract just the function name from the fully qualified name
            val wrapperShortName = wrapperFqn.substringAfterLast('.')
            val wrapperPackageName = wrapperFqn.substringBeforeLast('.', missingDelimiterValue = "")

            // Check if this wrapper is usable in the current file:
            // 1. Defined in the same package
            // 2. Imported explicitly (e.g., import com.example.MyWrapper)
            // 3. Imported via star import (e.g., import com.example.*)
            val isUsable = currentPackageName == wrapperPackageName ||
                    imports.contains(wrapperFqn) ||
                    imports.contains("$wrapperPackageName.*")

            if (!isUsable) {
                return@forEach // Skip this wrapper if it's not accessible
            }

            // Use word boundaries to ensure exact function name match
            val pattern = getComponentPattern(wrapperShortName)
            val matcher = pattern.matcher(content)
            var searchStart = 0
            while (matcher.find(searchStart)) {
                // Basic check to ensure it's not a self-reference if the wrapper is defined in this file
                // More robust would be to check if matcher.start() is within the definition of the wrapper itself.
                // For now, this is a simplification.
                val currentLineNumber = content.substring(0, matcher.start()).count { it == '\n' } + 1
                val isPotentiallySelfReference = result.wrapperDefinitions[wrapperFqn]?.file == file &&
                        currentLineNumber == result.wrapperDefinitions[wrapperFqn]?.lineNumber
                if (isPotentiallySelfReference) {
                    searchStart = matcher.end()
                    continue
                }

                val lineNumber = currentLineNumber
                val context = extractContext(lines, lineNumber)
                val containingFunction = findContainingFunction(content, matcher.start())

                // Skip if this usage is inside a preview composable and we're excluding previews
                if (excludePreviews && containingFunction?.isPreview == true) {
                    searchStart = matcher.end()
                    continue
                }

                result.addWrapperUsage(
                    wrapperName = wrapperFqn,
                    file = file,
                    lineNumber = lineNumber,
                    context = context,
                    containingFunction = containingFunction?.name,
                    team = team,
                )

                searchStart = matcher.end()
            }
        }
    }

    /**
     * Represents a containing function with information about whether it's a preview.
     */
    private data class ContainingFunction(
        val name: String,
        val isPreview: Boolean
    )

    /**
     * Checks if a component name match at the given position is actually a function definition
     * rather than a function call by looking backwards for the "fun" keyword.
     *
     * @param content The file content
     * @param position The position of the match
     * @param componentName The component name that was matched
     * @return true if this is a function definition, false if it's a function call
     */
    private fun isFunctionDefinition(content: String, position: Int, componentName: String): Boolean {
        // Look backwards from the position to find the start of the current statement/declaration
        // We need to account for multi-line function declarations with annotations

        var searchStart = position
        var linesChecked = 0
        val maxLinesToCheck = 10 // Reasonable limit for annotations + function declaration

        // Go backwards up to 10 lines to find potential function declaration start
        while (searchStart > 0 && linesChecked < maxLinesToCheck) {
            val prevLineEnd = content.lastIndexOf('\n', searchStart - 1)
            if (prevLineEnd == -1) break

            searchStart = prevLineEnd + 1
            linesChecked++

            // Get the text from this line start to the component name position
            val textBlock = content.substring(searchStart, position + componentName.length + 1)

            // Look for function declaration pattern that ends with our component name
            // This pattern looks for: (optional annotations and modifiers) + "fun" + componentName
            val functionDefPattern = Pattern.compile(
                """(?:^|\n)\s*(?:@\w+(?:\s*\([^)]*\))?\s+)*(?:public\s+|private\s+|internal\s+|external\s+|inline\s+|suspend\s+)*fun\s+$componentName\s*\(""",
                Pattern.DOTALL or Pattern.MULTILINE
            )

            if (functionDefPattern.matcher(textBlock).find()) {
                return true
            }
        }

        return false
    }

    private fun findContainingFunction(content: String, position: Int): ContainingFunction? {
        val beforePosition = content.substring(0, position)

        val matcher = functionPatternForContaining.matcher(beforePosition)

        var lastFunctionName: String? = null
        var lastFunctionStart = -1

        while (matcher.find()) {
            lastFunctionName = matcher.group(1)
            lastFunctionStart = matcher.start()
        }

        if (lastFunctionName == null) {
            return null
        }

        // Check if this function has a @Preview annotation
        // Look for @Preview in the text between the last function start and the previous function or file start
        val functionDeclarationText = if (lastFunctionStart > 0) {
            // Look at up to 10 lines before the function declaration to find annotations
            val lookbackStart = Math.max(0, beforePosition.lastIndexOf('\n', Math.max(0, lastFunctionStart - 200)))
            beforePosition.substring(lookbackStart, lastFunctionStart)
        } else {
            ""
        }

        val isPreview = previewAnnotationPattern.matcher(functionDeclarationText).find()

        return ContainingFunction(lastFunctionName, isPreview)
    }

    private fun extractContext(lines: List<String>, lineNumber: Int): String {
        val start = maxOf(0, lineNumber - contextLines - 1)
        val end = minOf(lines.size, lineNumber + contextLines)
        return lines.subList(start, end).joinToString("\n")
    }

    private fun extractImports(content: String): Set<String> {
        val imports = mutableSetOf<String>()
        val matcher = importPatternRegex.matcher(content)

        while (matcher.find()) {
            imports.add(matcher.group(1))
        }
        return imports
    }

    private fun extractPackageName(content: String): String {
        val matcher = packagePatternRegex.matcher(content)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            ""
        }
    }

    /**
     * Optimized version of getAvailableComponents that uses caching and more efficient lookups.
     * This function determines which components are available for use in a file based on its imports.
     *
     * @param imports Set of import statements in the file
     * @return Map of available component names to their fully qualified names
     */
    private fun getAvailableComponents(imports: Set<String>): List<Component> {
        // Fast path: if no imports, return all components (they could be in the same package)
        if (imports.isEmpty()) {
            return components
        }

        // Process star imports once and cache the results
        val starImportPrefixes = imports
            .filter { it.endsWith(".*") }
            .map { it.dropLast(1) }
            .toSet()

        // Fast path: check if we have a star import that covers all components
        // This is a common case for files that import all components
        if (starImportPrefixes.any { prefix ->
                components.all { component -> component.fqn.startsWith(prefix) }
            }) {
            return components
        }

        // Process direct imports
        val directImports = imports.filter { !it.endsWith(".*") }.toSet()

        // Build available components map efficiently
        val availableComponents = mutableMapOf<String, Component>()

        components.forEach { component ->
            // Direct import check (fastest)
            if (directImports.contains(component.fqn)) {
                availableComponents[component.name] = component
                return@forEach
            }

            // Star import check
            if (starImportPrefixes.any { prefix -> component.fqn.startsWith(prefix) }) {
                availableComponents[component.name] = component
            }
        }

        // If no specific imports found, assume all components might be available
        if (availableComponents.isEmpty()) {
            return components
        }

        return availableComponents.values.toList()
    }
}

// HTML Report Generator
class HtmlReportGenerator(private val components: List<Component>) {

    // Create a lookup map for component documentation
    private val componentDocMap = components.associateBy { it.name }

    private fun FlowContent.generateSummaryItems(result: AnalysisResult) {
        // Get all components from the original components list
        val allComponents = components.map { it.name }.toSet()

        // Get effective usage for existing components
        val effectiveUsage = result.getEffectiveUsage()

        // Create a map that includes all components, defaulting to 0 for unused ones
        val allComponentsWithUsage = allComponents.associateWith { component ->
            effectiveUsage[component] ?: 0
        }

        // Generate individual items (default view)
        div("individual-view summary-grid") {
            id = "individualView"
            allComponentsWithUsage.toList().sortedByDescending { it.second }.forEach { (component, count) ->
                val directCount = result.directUsages[component]?.size ?: 0
                val wrapperCount = getWrapperUsageCount(result, component)
                val componentInfo = componentDocMap[component]

                val classes = if (count == 0) "summary-item unused-component" else "summary-item"
                div(classes = classes) {
                    attributes["data-component"] = component
                    attributes["data-total"] = count.toString()
                    attributes["data-direct"] = directCount.toString()
                    attributes["data-wrapper"] = wrapperCount.toString()
                    attributes["data-group"] = componentInfo?.componentGroup ?: "other"

                    div("component-name") {
                        if (componentInfo?.documentation?.isNotBlank() == true) {
                            unsafe {
                                +"""<a href="${componentInfo.documentation}" target="_blank" class="component-link">$component</a>"""
                            }
                        } else {
                            +component
                        }
                    }
                    div("usage-count") { +count.toString() }
                    div("usage-label") { +"total usages" }
                    div("usage-breakdown") {
                        +"($directCount direct + $wrapperCount wrapper)"
                    }
                }
            }
        }

        // Generate grouped view
        div("grouped-view") {
            id = "groupedView"
            style = "display: none;"

            val groupedComponents = allComponentsWithUsage.entries.groupBy { (component, _) ->
                componentDocMap[component]?.componentGroup ?: "other"
            }

            groupedComponents.toList().sortedBy { it.first }.forEach { (group, componentsInGroup) ->
                div("component-group") {
                    attributes["data-group-name"] = group

                    h3("group-header") {
                        +group.replaceFirstChar { it.uppercase() }
                        span("group-count") {
                            +"(${componentsInGroup.size} components)"
                        }
                    }

                    div("group-items") {
                        componentsInGroup.sortedByDescending { it.value }.forEach { (component, count) ->
                            val directCount = result.directUsages[component]?.size ?: 0
                            val wrapperCount = getWrapperUsageCount(result, component)
                            val componentInfo = componentDocMap[component]

                            val classes = if (count == 0) "summary-item grouped-item unused-component" else "summary-item grouped-item"
                            div(classes = classes) {
                                attributes["data-component"] = component
                                attributes["data-total"] = count.toString()
                                attributes["data-direct"] = directCount.toString()
                                attributes["data-wrapper"] = wrapperCount.toString()
                                attributes["data-group"] = group

                                div("component-name") {
                                    if (componentInfo?.documentation?.isNotBlank() == true) {
                                        unsafe {
                                            +"""<a href="${componentInfo.documentation}" target="_blank" class="component-link">$component</a>"""
                                        }
                                    } else {
                                        +component
                                    }
                                }
                                div("usage-count") { +count.toString() }
                                div("usage-label") { +"total usages" }
                                div("usage-breakdown") {
                                    +"($directCount direct + $wrapperCount wrapper)"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getWrapperUsageCount(result: AnalysisResult, component: String): Int {
        var wrapperCount = 0

        // Single-component wrappers
        result.wrapperUsages.forEach { (wrapperName, usages) ->
            if (result.wrapperComponents[wrapperName] == component) {
                wrapperCount += usages.size
            }
        }

        // Multi-component wrappers
        result.wrapperUsages.forEach { (wrapperName, usages) ->
            val wrappedComponents = result.multiComponentWrappers[wrapperName]
            if (wrappedComponents != null && wrappedComponents.contains(component)) {
                wrapperCount += usages.size / wrappedComponents.size
            }
        }

        return wrapperCount
    }

    fun generateReport(result: AnalysisResult, outputFile: File, teamDetector: TeamOwnershipDetector) {
        val html = createHTML().html {
            head {
                meta(charset = "UTF-8")
                title("Component Usage Analysis Report")
                style {
                    +getReportStyles()
                }
                unsafe {
                    +"<script type=\"text/javascript\">"
                    +getReportJavaScript()
                    +"</script>"
                }
            }
            body {
                h1 { +"🎯 Component Usage Analysis Report" }

                // Global search filter
                div("search-container") {
                    input {
                        type = InputType.text
                        id = "globalSearch"
                        placeholder = "🔍 Search components, wrappers, or teams..."
                        classes = setOf("search-input")
                    }
                    button {
                        id = "clearSearch"
                        classes = setOf("clear-button")
                        +"Clear"
                    }
                    div("toggle-container") {
                        input {
                            type = InputType.checkBox
                            id = "showUnused"
                            checked = true
                        }
                        label {
                            htmlFor = "showUnused"
                            +"Show unused components"
                        }
                    }
                    div("toggle-container") {
                        input {
                            type = InputType.checkBox
                            id = "groupByCategory"
                            checked = false
                        }
                        label {
                            htmlFor = "groupByCategory"
                            +"Group by component category"
                        }
                    }
                }

                // Summary section
                div("summary") {
                    h2 { +"📊 Summary" }

                    // Sorting controls
                    div("sort-controls") {
                        label {
                            +"Sort by: "
                            select("sort-attribute") {
                                id = "sortAttribute"
                                option {
                                    value = "total"
                                    selected = true
                                    +"Total Usage"
                                }
                                option {
                                    value = "direct"
                                    +"Direct Usage"
                                }
                                option {
                                    value = "wrapper"
                                    +"Wrapper Usage"
                                }
                            }
                        }
                        label {
                            +"Order: "
                            select("sort-order") {
                                id = "sortOrder"
                                option {
                                    value = "desc"
                                    selected = true
                                    +"Descending"
                                }
                                option {
                                    value = "asc"
                                    +"Ascending"
                                }
                            }
                        }
                        button {
                            id = "sortButton"
                            +"Apply Sort"
                        }
                    }

                    div("summary-grid") {
                        id = "summaryGrid"
                        generateSummaryItems(result)
                    }
                }

                // Wrapper components section
                val allWrappers =
                    result.wrapperComponents + result.multiComponentWrappers.mapValues { it.value.joinToString(", ") }
                if (allWrappers.isNotEmpty()) {
                    div("section") {
                        h2("collapsible-header collapsed") {
                            +"🔗 Wrapper Components Detected"
                            span("toggle-icon") { +"▶" }
                        }
                        div("wrapper-list collapsible-content") {
                            // Sort wrappers by usage count (descending)
                            allWrappers.toList().sortedByDescending { (wrapper, _) ->
                                result.wrapperUsages[wrapper]?.size ?: 0
                            }.forEach { (wrapper, components) ->
                                val definition = result.wrapperDefinitions[wrapper]
                                val usageCount = result.wrapperUsages[wrapper]?.size ?: 0
                                val displayName = wrapper.substringAfterLast('.')
                                val packageName = definition?.packageName ?: ""
                                val ownerTeam =
                                    if (definition != null) teamDetector.getTeamForFile(definition.file) else null

                                div("wrapper-item") {
                                    div("wrapper-header") {
                                        span("wrapper-name") { +displayName }
                                        if (packageName.isNotEmpty()) {
                                            span("wrapper-package") { +"($packageName)" }
                                        }
                                        if (ownerTeam != null) {
                                            span("team-badge") { +"👥 $ownerTeam" }
                                        }
                                        span("wraps") { +"wraps" }
                                        span("component") { +components }
                                        span("usage-badge") { +"$usageCount usages" }
                                    }
                                    if (definition != null) {
                                        div("definition-location") {
                                            +"Defined in: ${definition.file.name}:${definition.lineNumber}"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Team usage section
                if (result.teamUsages.isNotEmpty()) {
                    div("section") {
                        h2("collapsible-header collapsed") {
                            +"👥 Usage by Team"
                            span("toggle-icon") { +"▶" }
                        }
                        div("collapsible-content") {

                            // Get all components
                            val allComponents = components.map { it.name }.toSet()

                            result.teamUsages.toList().sortedByDescending { (_, components) ->
                                components.values.sum()
                            }.forEach { (team, teamComponents) ->
                                div("team-section") {
                                    h3("team-header") {
                                        +"$team (${teamComponents.values.sum()} total usages)"
                                    }
                                    div("team-components") {
                                        // Show all components, including unused ones
                                        allComponents.sorted().forEach { component ->
                                            val count = teamComponents[component] ?: 0
                                            div(if (count == 0) "team-component-item unused-component" else "team-component-item") {
                                                span("component-name") { +component }
                                                span("usage-count-small") { +"$count" }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Detailed usage section
                div("section") {
                    h2("collapsible-header collapsed") {
                        +"📋 Detailed Usage Analysis"
                        span("toggle-icon") { +"▶" }
                    }
                    div("collapsible-content") {

                        result.directUsages.forEach { (component, usages) ->
                            div("component-section") {
                                h3("collapsible-header collapsed") {
                                    val componentInfo = componentDocMap[component]
                                    if (componentInfo?.documentation?.isNotBlank() == true) {
                                        unsafe {
                                            +"""<a href="${componentInfo.documentation}" target="_blank" class="component-link">$component</a> - Direct Usages (${usages.size})"""
                                        }
                                    } else {
                                        +"$component - Direct Usages (${usages.size})"
                                    }
                                    span("toggle-icon") { +"▶" }
                                }
                                div("collapsible-content") {
                                    usages.forEach { usage ->
                                        div("usage-item") {
                                            div("usage-header") {
                                                span("file-info") { +"${usage.file.name}:${usage.lineNumber}" }
                                                if (usage.team != null) {
                                                    span("team-badge-small") { +"👥 ${usage.team}" }
                                                }
                                            }
                                            pre("code-context") { +usage.context }
                                        }
                                    }
                                }
                            }
                        }

                        result.wrapperUsages.forEach { (wrapper, usages) ->
                            val components = result.wrapperComponents[wrapper]
                                ?: result.multiComponentWrappers[wrapper]?.joinToString(", ")
                                ?: "Unknown"
                            val displayName = wrapper.substringAfterLast('.')
                            val definition = result.wrapperDefinitions[wrapper]
                            val packageInfo = if (definition?.packageName?.isNotEmpty() == true) {
                                " (${definition.packageName})"
                            } else {
                                ""
                            }

                            div("component-section") {
                                h3("collapsible-header collapsed") {
                                    +"$displayName$packageInfo - Wrapper Usages (${usages.size}) → $components"
                                    span("toggle-icon") { +"▶" }
                                }
                                div("collapsible-content") {
                                    usages.forEach { usage ->
                                        div("usage-item wrapper-usage") {
                                            div("usage-header") {
                                                span("file-info") { +"${usage.file.name}:${usage.lineNumber}" }
                                                if (usage.team != null) {
                                                    span("team-badge-small") { +"👥 ${usage.team}" }
                                                }
                                            }
                                            pre("code-context") { +usage.context }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Footer
                div("footer") {
                    p { +"Generated by Component Usage Analyzer Script at ${java.time.LocalDateTime.now()}" }
                }
            }
        }

        outputFile.writeText(html, Charsets.UTF_8)
    }

    private fun getReportJavaScript(): String = """
        function globalSearch(searchTerm) {
            const term = searchTerm.toLowerCase().trim();
            const showUnused = document.getElementById('showUnused').checked;
            const groupByCategory = document.getElementById('groupByCategory').checked;

            // Reset all filters first
            document.querySelectorAll('.filtered-out').forEach(el => {
                el.classList.remove('filtered-out');
            });

            // If empty search term, just apply unused filter and return
            if (term === '') {
                applyUnusedFilter(showUnused, groupByCategory);
                return;
            }

            // Filter summary items (both individual and grouped views)
            document.querySelectorAll('.summary-item').forEach(item => {
                const componentName = item.getAttribute('data-component').toLowerCase();
                const componentGroup = item.getAttribute('data-group').toLowerCase();
                const count = parseInt(item.getAttribute('data-total'));
                
                const matchesSearch = componentName.includes(term) || componentGroup.includes(term);
                const shouldShow = matchesSearch && (showUnused || count > 0);
                
                if (!shouldShow) {
                    item.classList.add('filtered-out');
                }
            });

            // Handle group visibility in grouped view
            if (groupByCategory) {
                document.querySelectorAll('.component-group').forEach(group => {
                    const groupName = group.getAttribute('data-group-name').toLowerCase();
                    const hasVisibleItems = Array.from(group.querySelectorAll('.summary-item')).some(item => 
                        !item.classList.contains('filtered-out')
                    );
                    
                    if (groupName.includes(term)) {
                        // If group name matches, show the group and filter items by unused setting
                        group.classList.remove('filtered-out');
                        group.querySelectorAll('.summary-item').forEach(item => {
                            const count = parseInt(item.getAttribute('data-total'));
                            if (!showUnused && count === 0) {
                                item.classList.add('filtered-out');
                            } else {
                                item.classList.remove('filtered-out');
                            }
                        });
                    } else if (hasVisibleItems) {
                        // Group has matching items, show the group
                        group.classList.remove('filtered-out');
                    } else {
                        // No matching items in group, hide the group
                        group.classList.add('filtered-out');
                    }
                });
            }

            // Filter wrapper items
            document.querySelectorAll('.wrapper-item').forEach(item => {
                const wrapperName = item.querySelector('.wrapper-name')?.textContent?.toLowerCase() || '';
                const component = item.querySelector('.component')?.textContent?.toLowerCase() || '';
                const teamBadge = item.querySelector('.team-badge')?.textContent?.toLowerCase() || '';

                if (wrapperName.includes(term) || component.includes(term) || teamBadge.includes(term)) {
                    item.classList.remove('filtered-out');
                } else {
                    item.classList.add('filtered-out');
                }
            });

            // Filter team sections
            document.querySelectorAll('.team-section').forEach(section => {
                const teamHeader = section.querySelector('.team-header')?.textContent?.toLowerCase() || '';
                let hasMatchingComponent = false;

                // Filter individual team component items
                section.querySelectorAll('.team-component-item').forEach(item => {
                    const componentName = item.querySelector('.component-name')?.textContent?.toLowerCase() || '';
                    const count = parseInt(item.querySelector('.usage-count-small')?.textContent || '0');
                    if (componentName.includes(term) && (showUnused || count > 0)) {
                        item.classList.remove('filtered-out');
                        hasMatchingComponent = true;
                    } else {
                        item.classList.add('filtered-out');
                    }
                });

                // Show/hide the entire team section
                if (teamHeader.includes(term) || hasMatchingComponent) {
                    section.classList.remove('filtered-out');
                    // If team header matches, show all components in this team
                    if (teamHeader.includes(term)) {
                        section.querySelectorAll('.team-component-item').forEach(item => {
                            const count = parseInt(item.querySelector('.usage-count-small')?.textContent || '0');
                            if (showUnused || count > 0) {
                                item.classList.remove('filtered-out');
                            }
                        });
                    }
                } else {
                    section.classList.add('filtered-out');
                }
            });

            // Filter component sections in detailed analysis
            document.querySelectorAll('.component-section').forEach(section => {
                const sectionHeader = section.querySelector('h3')?.textContent?.toLowerCase() || '';
                let hasMatchingUsage = false;

                // Check individual usage items and filter them
                section.querySelectorAll('.usage-item').forEach(usage => {
                    const fileInfo = usage.querySelector('.file-info')?.textContent?.toLowerCase() || '';
                    const teamBadge = usage.querySelector('.team-badge-small')?.textContent?.toLowerCase() || '';
                    const codeContext = usage.querySelector('.code-context')?.textContent?.toLowerCase() || '';

                    if (fileInfo.includes(term) || teamBadge.includes(term) || codeContext.includes(term)) {
                        usage.classList.remove('filtered-out');
                        hasMatchingUsage = true;
                    } else {
                        usage.classList.add('filtered-out');
                    }
                });

                // Show/hide the entire section based on header match or if it has matching usage items
                if (sectionHeader.includes(term) || hasMatchingUsage) {
                    section.classList.remove('filtered-out');
                    // If header matches, show all usage items in this section
                    if (sectionHeader.includes(term)) {
                        section.querySelectorAll('.usage-item').forEach(usage => {
                            usage.classList.remove('filtered-out');
                        });
                    }
                } else {
                    section.classList.add('filtered-out');
                }
            });
        }

        function applyUnusedFilter(showUnused, groupByCategory) {
            if (!showUnused) {
                document.querySelectorAll('.unused-component').forEach(el => {
                    el.classList.add('filtered-out');
                });
            }
            
            // In grouped view, hide empty groups
            if (groupByCategory) {
                document.querySelectorAll('.component-group').forEach(group => {
                    const hasVisibleItems = Array.from(group.querySelectorAll('.summary-item')).some(item => 
                        !item.classList.contains('filtered-out')
                    );
                    if (!hasVisibleItems) {
                        group.classList.add('filtered-out');
                    }
                });
            }
        }

        function clearSearch() {
            document.getElementById('globalSearch').value = '';
            globalSearch('');
        }

        function toggleUnusedComponents() {
            const showUnused = document.getElementById('showUnused').checked;
            const groupByCategory = document.getElementById('groupByCategory').checked;
            const searchTerm = document.getElementById('globalSearch').value;
            
            if (searchTerm === '') {
                // No search term, just apply unused filter
                document.querySelectorAll('.filtered-out').forEach(el => {
                    el.classList.remove('filtered-out');
                });
                applyUnusedFilter(showUnused, groupByCategory);
            } else {
                // Re-run search with new unused setting
                globalSearch(searchTerm);
            }
        }

        function toggleGroupByCategory() {
            const groupByCategory = document.getElementById('groupByCategory').checked;
            const individualView = document.getElementById('individualView');
            const groupedView = document.getElementById('groupedView');
            const showUnused = document.getElementById('showUnused').checked;
            
            if (groupByCategory) {
                individualView.style.display = 'none';
                groupedView.style.display = 'block';
            } else {
                individualView.style.display = 'block';
                groupedView.style.display = 'none';
            }
            
            // Apply current filters to the new view
            const searchTerm = document.getElementById('globalSearch').value;
            if (searchTerm === '') {
                // No search term, just apply unused filter
                document.querySelectorAll('.filtered-out').forEach(el => {
                    el.classList.remove('filtered-out');
                });
                applyUnusedFilter(showUnused, groupByCategory);
            } else {
                // Re-run search with current settings
                globalSearch(searchTerm);
            }
        }

        function toggleSection(header) {
            // Toggle the collapsed class on the header
            header.classList.toggle('collapsed');
            
            // Find the content div that follows this header
            const content = header.nextElementSibling;
            if (content && content.classList.contains('collapsible-content')) {
                // Toggle content animation classes
                if (header.classList.contains('collapsed')) {
                    content.classList.remove('expanded');
                    content.classList.add('collapsed');
                } else {
                    content.classList.remove('collapsed');
                    content.classList.add('expanded');
                }
            }
            
            // Update the toggle icon
            const icon = header.querySelector('.toggle-icon');
            if (icon) {
                icon.textContent = header.classList.contains('collapsed') ? '▶' : '▼';
            }
        }

        function sortSummary() {
            const sortAttribute = document.getElementById('sortAttribute').value;
            const sortOrder = document.getElementById('sortOrder').value;
            const showUnused = document.getElementById('showUnused').checked;
            const groupByCategory = document.getElementById('groupByCategory').checked;

            if (groupByCategory) {
                // Sort items within each group
                document.querySelectorAll('.group-items').forEach(groupContainer => {
                    const items = Array.from(groupContainer.children);
                    
                    // Sort the items
                    items.sort((a, b) => {
                        const aValue = parseInt(a.getAttribute(`data-${'$'}{sortAttribute}`) || '0');
                        const bValue = parseInt(b.getAttribute(`data-${'$'}{sortAttribute}`) || '0');
                        return sortOrder === 'asc' ? aValue - bValue : bValue - aValue;
                    });

                    // Reappend the sorted items
                    items.forEach(item => groupContainer.appendChild(item));
                });
                
                // Sort groups themselves by total usage
                const groupedView = document.getElementById('groupedView');
                const groups = Array.from(groupedView.children);
                
                groups.sort((a, b) => {
                    const aTotal = Array.from(a.querySelectorAll('.summary-item')).reduce((sum, item) => {
                        return sum + parseInt(item.getAttribute('data-total') || '0');
                    }, 0);
                    const bTotal = Array.from(b.querySelectorAll('.summary-item')).reduce((sum, item) => {
                        return sum + parseInt(item.getAttribute('data-total') || '0');
                    }, 0);
                    return sortOrder === 'asc' ? aTotal - bTotal : bTotal - aTotal;
                });
                
                groups.forEach(group => groupedView.appendChild(group));
            } else {
                // Sort individual view
                const individualView = document.getElementById('individualView');
                const items = Array.from(individualView.children);

                // Sort the items
                items.sort((a, b) => {
                    const aValue = parseInt(a.getAttribute(`data-${'$'}{sortAttribute}`) || '0');
                    const bValue = parseInt(b.getAttribute(`data-${'$'}{sortAttribute}`) || '0');
                    return sortOrder === 'asc' ? aValue - bValue : bValue - aValue;
                });

                // Reappend the sorted items
                items.forEach(item => individualView.appendChild(item));
            }

            // Apply unused components filter
            applyUnusedFilter(showUnused, groupByCategory);
        }

        document.addEventListener('DOMContentLoaded', function() {
            // Search functionality
            const searchInput = document.getElementById('globalSearch');
            const clearButton = document.getElementById('clearSearch');
            const showUnusedCheckbox = document.getElementById('showUnused');

            searchInput.addEventListener('input', function() {
                globalSearch(this.value);
            });

            searchInput.addEventListener('keyup', function(event) {
                if (event.key === 'Escape') {
                    clearSearch();
                }
            });

            clearButton.addEventListener('click', clearSearch);
            showUnusedCheckbox.addEventListener('change', toggleUnusedComponents);
            
            // Group by category functionality
            const groupByCategoryCheckbox = document.getElementById('groupByCategory');
            groupByCategoryCheckbox.addEventListener('change', toggleGroupByCategory);

            // Sorting functionality
            document.getElementById('sortButton').addEventListener('click', sortSummary);
            document.getElementById('sortAttribute').addEventListener('change', sortSummary);
            document.getElementById('sortOrder').addEventListener('change', sortSummary);

            // Add click handlers for collapsible headers
            var collapsibleHeaders = document.querySelectorAll('.collapsible-header');
            collapsibleHeaders.forEach(function(header) {
                // Set initial state
                const content = header.nextElementSibling;
                if (content && content.classList.contains('collapsible-content')) {
                    if (header.classList.contains('collapsed')) {
                        content.classList.add('collapsed');
                        content.classList.remove('expanded');
                    } else {
                        content.classList.add('expanded');
                        content.classList.remove('collapsed');
                    }
                }
                
                // Add click handler
                header.addEventListener('click', function() {
                    toggleSection(header);
                });
            });
        });
    """.trimIndent()

    private fun getReportStyles(): String = """
        body { 
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; 
            margin: 0; 
            padding: 20px; 
            background: #f8f9fa; 
            line-height: 1.6;
        }
        h1 { 
            color: #2c3e50; 
            border-bottom: 3px solid #3498db; 
            padding-bottom: 10px; 
        }
        h2 { 
            color: #34495e; 
            margin-top: 30px; 
        }
        .collapsible-header {
            cursor: pointer;
            user-select: none;
            display: flex;
            align-items: center;
            justify-content: space-between;
            transition: all 0.2s ease;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 6px;
            margin-bottom: 10px;
            border: 1px solid #e9ecef;
        }
        .collapsible-header:hover {
            color: #2c3e50;
            background: #e9ecef;
        }
        .collapsible-header.collapsed {
            color: #7f8c8d;
            background: #f8f9fa;
        }
        .toggle-icon {
            font-size: 14px;
            transition: transform 0.2s ease;
            margin-left: 10px;
            color: #3498db;
        }
        .collapsible-content {
            padding: 10px;
            background: white;
            border-radius: 6px;
            margin-bottom: 20px;
            border: 1px solid #e9ecef;
            overflow: hidden;
            transition: opacity 0.3s ease, transform 0.3s ease;
        }
        
        .collapsible-content.collapsed {
            content-visibility: hidden;
            opacity: 0;
            transform: translateY(-10px);
        }
        
        .collapsible-content.expanded {
            content-visibility: visible;
            opacity: 1;
            transform: translateY(0);
        }
        h3 { 
            color: #2c3e50; 
            margin-bottom: 15px; 
        }
        .summary { 
            background: white; 
            padding: 20px; 
            border-radius: 8px; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
            margin-bottom: 30px; 
        }
        .sort-controls {
            margin-bottom: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 6px;
            display: flex;
            gap: 20px;
            align-items: center;
            flex-wrap: wrap;
        }
        .sort-controls label {
            display: flex;
            align-items: center;
            gap: 8px;
            font-weight: 500;
        }
        .sort-controls select {
            padding: 6px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background: white;
        }
        .sort-controls button {
            padding: 8px 16px;
            background: #3498db;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
        }
        .sort-controls button:hover {
            background: #2980b9;
        }
        .summary-grid { 
            display: grid; 
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); 
            gap: 15px; 
            margin-top: 15px; 
        }
        .summary-item { 
            text-align: center; 
            padding: 15px; 
            background: #ecf0f1; 
            border-radius: 6px; 
        }
        .component-name { 
            font-weight: bold; 
            color: #2c3e50; 
            margin-bottom: 5px; 
        }
        .component-link {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.2s ease;
        }
        .component-link:hover {
            color: #2980b9;
            text-decoration: underline;
        }
        .usage-count { 
            font-size: 24px; 
            font-weight: bold; 
            color: #3498db; 
        }
        .usage-label { 
            font-size: 12px; 
            color: #7f8c8d; 
        }
        .usage-breakdown {
            font-size: 11px;
            color: #95a5a6;
            margin-top: 2px;
        }
        .section { 
            background: white; 
            padding: 20px; 
            border-radius: 8px; 
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
            margin-bottom: 20px; 
        }
        .wrapper-list { 
            margin-top: 15px; 
        }
        .wrapper-item { 
            padding: 15px; 
            background: #f8f9fa; 
            border-left: 4px solid #e74c3c; 
            margin-bottom: 10px; 
            border-radius: 0 6px 6px 0; 
        }
        .wrapper-header { 
            display: flex; 
            align-items: center; 
            gap: 10px; 
            flex-wrap: wrap; 
        }
        .wrapper-name { 
            font-weight: bold; 
            color: #e74c3c; 
        }
        .wrapper-package {
            font-size: 12px;
            color: #7f8c8d;
            font-weight: normal;
            margin-left: 5px;
        }
        .wraps { 
            color: #7f8c8d; 
            font-style: italic; 
        }
        .component { 
            font-weight: bold; 
            color: #3498db; 
        }
        .usage-badge { 
            background: #3498db; 
            color: white; 
            padding: 2px 8px; 
            border-radius: 12px; 
            font-size: 12px; 
        }
        .team-badge {
            background: #9b59b6;
            color: white;
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 11px;
            margin-left: 8px;
        }
        .team-badge-small {
            background: #9b59b6;
            color: white;
            padding: 1px 6px;
            border-radius: 8px;
            font-size: 10px;
            margin-left: 8px;
        }
        .definition-location { 
            margin-top: 5px; 
            font-size: 12px; 
            color: #7f8c8d; 
        }
        .component-section { 
            margin-bottom: 30px; 
        }
        .usage-item { 
            margin: 10px 0; 
            padding: 15px; 
            background: #f8f9fa; 
            border-radius: 6px; 
            border-left: 4px solid #3498db; 
        }
        .wrapper-usage { 
            border-left-color: #e74c3c; 
        }
        .usage-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 8px;
        }
        .file-info { 
            font-weight: bold; 
            color: #2c3e50; 
        }
        .code-context { 
            background: white; 
            padding: 10px; 
            border-radius: 4px; 
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace; 
            font-size: 13px; 
            line-height: 1.4; 
            overflow-x: auto; 
            margin: 0; 
        }
        .team-section {
            margin-bottom: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 6px;
            border-left: 4px solid #9b59b6;
        }
        .team-header {
            color: #9b59b6;
            margin-bottom: 10px;
            font-size: 16px;
        }
        .team-components {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 8px;
        }
        .team-component-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 12px;
            background: white;
            border-radius: 4px;
            font-size: 14px;
        }
        .usage-count-small {
            background: #9b59b6;
            color: white;
            padding: 2px 6px;
            border-radius: 10px;
            font-size: 12px;
            font-weight: bold;
        }
        .footer {
            margin-top: 40px;
            padding: 20px;
            text-align: center;
            color: #7f8c8d;
            font-size: 12px;
        }
        .search-container {
            margin: 20px 0 30px 0;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            display: flex;
            gap: 15px;
            align-items: center;
        }
        .search-input {
            flex: 1;
            padding: 12px 16px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 16px;
            transition: border-color 0.2s ease;
        }
        .search-input:focus {
            outline: none;
            border-color: #3498db;
        }
        .clear-button {
            padding: 12px 20px;
            background: #95a5a6;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: background 0.2s ease;
        }
        .clear-button:hover {
            background: #7f8c8d;
        }
        .filtered-out {
            display: none !important;
        }
        .unused-component {
            opacity: 0.6;
            background: #f8f9fa;
            border: 1px dashed #ddd;
        }
        .toggle-container {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-left: 15px;
        }
        .toggle-container input[type="checkbox"] {
            width: 16px;
            height: 16px;
            cursor: pointer;
        }
        .toggle-container label {
            cursor: pointer;
            user-select: none;
            font-size: 14px;
            color: #2c3e50;
        }
        .component-group {
            margin-bottom: 25px;
            padding: 20px;
            background: white;
            border-radius: 8px;
            border: 1px solid #e9ecef;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .group-header {
            color: #2c3e50;
            margin-bottom: 15px;
            padding-bottom: 8px;
            border-bottom: 2px solid #3498db;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .group-count {
            font-size: 14px;
            color: #7f8c8d;
            font-weight: normal;
            margin-left: 10px;
        }
        .group-items {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }
        .grouped-item {
            margin: 0 !important;
        }
        .individual-view,
        .grouped-view {
            transition: opacity 0.3s ease;
        }
    """.trimIndent()
}

/**
 * Determines if a file should be excluded based on exclusion patterns.
 *
 * @param file The file to check
 * @param baseDir The base directory for relative path calculation
 * @param patterns List of glob patterns to match against
 * @return true if the file should be excluded, false otherwise
 */
fun shouldExcludeFile(file: File, baseDir: File, patterns: List<String>): Boolean {
    val relativePath = baseDir.toPath().relativize(file.toPath()).toString().replace("\\", "/")
    return patterns.any { pattern ->
        /**
         * Converts glob patterns to regex for file path matching.
         *
         * Pattern conversions:
         * - `**` followed by `/` -> `.*` followed by `/` - Matches any directory depth (zero or more directories)
         * - `*` -> `[^/]*` - Matches any characters except path separator within a directory
         * - `?` -> `[^/]` - Matches single character except path separator
         *
         * Example transformations:
         * - `**` + `/build/` + `**` -> `.*` + `/build/` + `.*` (matches any build directory at any depth)
         * - `*.kt` -> `[^/]*\.kt` (matches .kt files in current directory)
         * - `test?` + `/` + `**` -> `test[^/]` + `/` + `.*` (matches test1/, test2/, etc. directories)
         *
         * Example matches for pattern "double-star/build/double-star":
         * - `app/build/tmp/file.txt` ✓
         * - `module/submodule/build/classes/Main.class` ✓
         * - `src/main/kotlin/File.kt` ✗
         */
        val glob = pattern
            .replace("**/", ".*/") // Allow matching any directory depth
            .replace("*", "[^/]*")   // Match any character except path separator
            .replace("?", "[^/]")    // Match single character except path separator
        runCatching {
            Regex(glob).matches(relativePath)
        }.onFailure { e ->
            println("   ⚠️ Invalid regex pattern from glob '$pattern': ${e.message}")
        }.getOrDefault(false)
    }
}

/**
 * Parses components from a JSON file using Moshi.
 *
 * NOTE: We use Moshi instead of kotlinx.serialization because:
 * 1. It doesn't require compiler plugins, which aren't supported in .kts scripts
 * 2. It has better runtime reflection support for Kotlin data classes
 * 3. It's more flexible with JSON parsing and error handling
 */
@OptIn(ExperimentalStdlibApi::class)
fun parseComponentsFromJson(jsonFile: File): Components {
    return try {
        val adapter = sharedMoshi.adapter<Components>()
        val jsonContent = jsonFile.readText()
        adapter.fromJson(jsonContent) ?: Components(emptyList())
    } catch (e: Exception) {
        println("   ⚠️ Error parsing JSON file: ${e.message}")
        Components(emptyList())
    }
}

class ComponentUsageReport : SuspendingCliktCommand(
    name = "report-component-usage",
) {
    private val sourceDirs by argument(
        name = "SOURCE_DIRS",
        help = "Source directories to analyze (comma-separated paths)",
    ).file(
        canBeFile = false,
        canBeDir = true,
        mustExist = true,
        mustBeReadable = true,
    ).multiple()
        .unique()

    private val componentsJsonFile by argument(
        name = "COMPONENTS_FILE",
        help = "JSON file containing components definitions",
    ).file(
        mustExist = true,
        canBeDir = false,
        mustBeReadable = true,
    )

    private val outputDir by option(
        "--output-dir", "-o",
        help = "Output directory path (default: to current dir)",
        metavar = "DIR",
    ).file(
        canBeFile = false,
        mustExist = true,
        mustBeWritable = true,
    ).default(File("."))

    private val exportFormat by option(
        "--format", "-f",
        help = "Export format (html, json, csv, or all)",
        metavar = "FORMAT",
    ).enum<ExportFormat>().default(ExportFormat.HTML)

    private val excludePatterns by option(
        "--exclude", "-e",
        help = "Exclude patterns (comma-separated glob patterns)",
        metavar = "PATTERNS",
    ).convert { it.split(",").map { pattern -> pattern.trim() } }
        .default(DEFAULT_EXCLUSION_PATTERNS)

    private val verbose by option(
        "--verbose", "-v",
        help = "Enable verbose output",
    ).flag()

    private val contextLines by option(
        "--context-lines", "-l",
        help = "Number of context lines to show around each usage (default: 3)",
        metavar = "NUMBER",
    ).int()
        .restrictTo(min = 1, max = 20, clamp = true)
        .default(3)

    private val excludePreviews by option(
        "--exclude-previews",
        help = "Exclude component usages inside preview composables (default: true)",
    ).flag("--include-previews", default = true)

    override fun help(context: Context): String {
        return """
        Analyze component usage across Compose projects.

        This tool scans Kotlin source files to identify direct usage of Compose components
        and wrapper components, providing insights into adoption patterns and team usage.

        Usage: report-component-usage [OPTIONS] SOURCE_DIRS COMPONENTS_FILE

        Arguments:
            SOURCE_DIRS    Source directories to analyze (comma-separated)
            COMPONENTS_FILE JSON file containing components definitions

        Options:
            -o, --output-dir DIR    Output directory path (default: current directory)
            -f, --format FORMAT    Export format: html, json, csv, or all (default: html)
            -e, --exclude PATTERNS  Exclude patterns (comma-separated glob patterns)
            -v, --verbose        Enable verbose output
            -l, --context-lines NUMBER  Number of context lines to show (default: 3)

        The COMPONENTS_FILE should have the following structure:
        {
          "components": [
            {
              "name": "ComponentName1",
              "fqn": "com.adevinta.example.components.package.ComponentName1",
              "documentation": "...",
              "componentGroup": "..."
            },
            ...
          ]
        }
        """.trimIndent()
    }

    override suspend fun run() {
        analyzeComponentUsage()
    }

    private suspend fun analyzeComponentUsage() {
        echo("🚀 Component Usage Analyzer")
        echo("==================================")

        // Validate source directories
        val invalidDirs = sourceDirs.filter { !it.exists() }
        if (invalidDirs.isNotEmpty()) {
            echo("❌ Error: The following source directories do not exist:", err = true)
            invalidDirs.forEach { echo("  - ${it.path}", err = true) }
            throw Abort()
        }

        if (verbose) {
            echo("🔧 Verbose mode enabled")
            echo("📁 Source directories: ${sourceDirs.joinToString(", ") { it.absolutePath }}")
            echo("📁 Output directory: ${outputDir.absolutePath}")
            echo("📄 Export format: $exportFormat")
            echo("🚫 Exclusion patterns: ${excludePatterns.joinToString(", ")}")
        }

        // Find all Kotlin files (excluding specified patterns) - parallelized discovery
        if (verbose) {
            echo("🔍 Discovering Kotlin files using ${Runtime.getRuntime().availableProcessors()} CPU cores...")
        }
        var kotlinFiles: List<File>
        val totalDiscoveredFiles = AtomicInteger(0)
        val discoveryTime = measureTimeMillis {
            val allResults = supervisorScope {
                sourceDirs.map { sourceDir ->
                    async(Dispatchers.IO) {
                        try {
                            if (verbose) echo("   📂 Processing directory: ${sourceDir.name}")
                            discoverKotlinFilesParallelWithCount(
                                sourceDir = sourceDir,
                                exclusionPatterns = excludePatterns,
                                verbose = verbose,
                                counter = totalDiscoveredFiles,
                            )
                        } catch (e: Exception) {
                            echo("   ⚠️ Error processing directory ${sourceDir.name}: ${e.message}")
                            emptyList()
                        }
                    }
                }.awaitAll()
            }

            kotlinFiles = allResults.flatten()
        }
        echo("⏱️ File discovery completed in ${discoveryTime}ms")

        if (kotlinFiles.isEmpty()) {
            echo("⚠️  No Kotlin files found in the specified directories (after applying exclusions)")
            return
        }

        echo("📝 Found ${kotlinFiles.size} Kotlin files")

        // Initialize team detector for on-demand discovery
        val teamDetector = TeamOwnershipDetector()

        // Load components from JSON file
        val components = try {
            val components = parseComponentsFromJson(componentsJsonFile)
            components.components
        } catch (e: Exception) {
            echo("❌ Error parsing components JSON file: ${e.message}", err = true)
            throw Abort()
        }

        if (verbose) {
            echo("📄 Using ${components.size} components for analysis")
            echo("   Loaded from JSON file: ${componentsJsonFile.absolutePath}")
        }

        // Run analysis with coroutines
        val analyzer = ComponentAnalyzer(components, teamDetector, contextLines)
        val result = analyzer.analyze(kotlinFiles, excludePreviews)

        // Print console summary
        if (verbose) {
            printSummary(result, teamDetector)
        }

        // Create output directory if it doesn't exist
        outputDir.mkdirs()

        // Generate reports based on selected format
        when (exportFormat) {
            ExportFormat.HTML -> {
                echo("\n📊 Generating HTML report...")
                val htmlFile = File(outputDir, "component-usage-report.html")
                val reportGenerator = HtmlReportGenerator(components)
                reportGenerator.generateReport(result, htmlFile, teamDetector)
                echo("✅ HTML report generated: ${htmlFile.absolutePath}")
            }

            ExportFormat.JSON -> {
                echo("\n📊 Generating JSON report...")
                val jsonFile = File(outputDir, "component-usage-report.json")
                generateJsonReport(result, jsonFile)
                echo("✅ JSON report generated: ${jsonFile.absolutePath}")
            }

            ExportFormat.CSV -> {
                echo("\n📊 Generating CSV report...")
                val csvFile = File(outputDir, "component-usage-report.csv")
                generateCsvReport(result, csvFile, teamDetector)
                echo("✅ CSV report generated: ${csvFile.absolutePath}")
            }

            ExportFormat.ALL -> {
                echo("\n📊 Generating HTML, JSON, and CSV reports...")
                val htmlFile = File(outputDir, "component-usage-report.html")
                val jsonFile = File(outputDir, "component-usage-report.json")
                val csvFile = File(outputDir, "component-usage-report.csv")

                val reportGenerator = HtmlReportGenerator(components)
                reportGenerator.generateReport(result, htmlFile, teamDetector)
                generateJsonReport(result, jsonFile)
                generateCsvReport(result, csvFile, teamDetector)

                echo("✅ HTML report generated: ${htmlFile.absolutePath}")
                echo("✅ JSON report generated: ${jsonFile.absolutePath}")
                echo("✅ CSV report generated: ${csvFile.absolutePath}")
            }
        }

        if (verbose) {
            echo("\n🌐 Open the reports in your browser to view detailed results!")
        }
    }
}

enum class ExportFormat {
    HTML, JSON, CSV, ALL
}

// Add JSON report generation function
@OptIn(ExperimentalStdlibApi::class)
private fun generateJsonReport(result: AnalysisResult, outputFile: File) {
    val reportData = mapOf(
        "directUsages" to result.directUsages.mapValues { (_, usages) ->
            usages.map { usage ->
                mapOf(
                    "file" to usage.file.absolutePath,
                    "lineNumber" to usage.lineNumber,
                    "context" to usage.context,
                    "containingFunction" to usage.containingFunction,
                    "team" to usage.team,
                )
            }
        },
        "wrapperComponents" to result.wrapperComponents,
        "wrapperUsages" to result.wrapperUsages.mapValues { (_, usages) ->
            usages.map { usage ->
                mapOf(
                    "file" to usage.file.absolutePath,
                    "lineNumber" to usage.lineNumber,
                    "context" to usage.context,
                    "containingFunction" to usage.containingFunction,
                    "team" to usage.team,
                )
            }
        },
        "wrapperDefinitions" to result.wrapperDefinitions.mapValues { (_, definition) ->
            mapOf(
                "file" to definition.file.absolutePath,
                "lineNumber" to definition.lineNumber,
                "dependencies" to definition.dependencies,
                "packageName" to definition.packageName,
            )
        },
        "multiComponentWrappers" to result.multiComponentWrappers,
        "teamUsages" to result.teamUsages.mapValues { (_, components) ->
            components.mapValues { it.value }
        },
        "effectiveUsage" to result.getEffectiveUsage(),
    )

    val json = sharedMoshi.adapter<Map<String, Any>>().toJson(reportData)
    outputFile.writeText(json)
}

// Add CSV report generation functions
private fun generateCsvReport(result: AnalysisResult, outputFile: File, teamDetector: TeamOwnershipDetector) {
    try {
        val csvContent = buildString {
            // Component usage summary
            appendLine("Component Usage Summary")
            appendLine("component,total_usage,direct_usage,wrapper_usage,component_group")
            
            val effectiveUsage = result.getEffectiveUsage()
            effectiveUsage.toList().sortedByDescending { it.second }.forEach { (component, totalUsage) ->
                val directUsage = result.directUsages[component]?.size ?: 0
                val wrapperUsage = getWrapperUsageCount(result, component)
                appendLine("$component,$totalUsage,$directUsage,$wrapperUsage,")
            }
            
            appendLine() // Empty line for separation
            
            // Direct usages
            appendLine("Direct Usages")
            appendLine("component,file,line_number,containing_function,team,context")
            result.directUsages.forEach { (component, usages) ->
                usages.forEach { usage ->
                    val context = usage.context.replace("\n", " ").replace(",", ";")
                    appendLine("$component,${usage.file.absolutePath},${usage.lineNumber},${usage.containingFunction ?: ""},${usage.team ?: ""},$context")
                }
            }
            
            appendLine() // Empty line for separation
            
            // Wrapper components
            appendLine("Wrapper Components")
            appendLine("wrapper_name,package_name,wrapped_components,usage_count,owner_team,definition_file,definition_line")
            result.wrapperComponents.forEach { (wrapper, wrappedComponent) ->
                val wrapperUsages = result.wrapperUsages[wrapper]?.size ?: 0
                val displayName = wrapper.substringAfterLast('.')
                val definition = result.wrapperDefinitions[wrapper]
                val packageName = definition?.packageName ?: ""
                val ownerTeam = if (definition != null) teamDetector.getTeamForFile(definition.file) else null
                val definitionFile = definition?.file?.absolutePath ?: ""
                val definitionLine = definition?.lineNumber ?: 0
                appendLine("$displayName,$packageName,$wrappedComponent,$wrapperUsages,${ownerTeam ?: ""},$definitionFile,$definitionLine")
            }
            
            result.multiComponentWrappers.forEach { (wrapper, wrappedComponents) ->
                val wrapperUsages = result.wrapperUsages[wrapper]?.size ?: 0
                val displayName = wrapper.substringAfterLast('.')
                val definition = result.wrapperDefinitions[wrapper]
                val packageName = definition?.packageName ?: ""
                val ownerTeam = if (definition != null) teamDetector.getTeamForFile(definition.file) else null
                val definitionFile = definition?.file?.absolutePath ?: ""
                val definitionLine = definition?.lineNumber ?: 0
                appendLine("$displayName,$packageName,${wrappedComponents.joinToString(";")},$wrapperUsages,${ownerTeam ?: ""},$definitionFile,$definitionLine")
            }
            
            appendLine() // Empty line for separation
            
            // Wrapper usages
            appendLine("Wrapper Usages")
            appendLine("wrapper_name,file,line_number,containing_function,team,context")
            result.wrapperUsages.forEach { (wrapper, usages) ->
                val displayName = wrapper.substringAfterLast('.')
                usages.forEach { usage ->
                    val context = usage.context.replace("\n", " ").replace(",", ";")
                    appendLine("$displayName,${usage.file.absolutePath},${usage.lineNumber},${usage.containingFunction ?: ""},${usage.team ?: ""},$context")
                }
            }
            
            appendLine() // Empty line for separation
            
            // Team usage summary
            appendLine("Team Usage Summary")
            appendLine("team,component,usage_count")
            result.teamUsages.forEach { (team, components) ->
                components.forEach { (component, count) ->
                    appendLine("$team,$component,$count")
                }
            }
        }
        
        outputFile.writeText(csvContent)
    } catch (e: Exception) {
        println("❌ Error writing CSV file: ${e.message}")
    }
}

private fun getWrapperUsageCount(result: AnalysisResult, component: String): Int {
    var wrapperCount = 0

    // Single-component wrappers
    result.wrapperUsages.forEach { (wrapperName, usages) ->
        if (result.wrapperComponents[wrapperName] == component) {
            wrapperCount += usages.size
        }
    }

    // Multi-component wrappers
    result.wrapperUsages.forEach { (wrapperName, usages) ->
        val wrappedComponents = result.multiComponentWrappers[wrapperName]
        if (wrappedComponents != null && wrappedComponents.contains(component)) {
            wrapperCount += usages.size / wrappedComponents.size
        }
    }

    return wrapperCount
}

// Console output functions
fun printSummary(result: AnalysisResult, teamDetector: TeamOwnershipDetector) {
    println("\n" + "=".repeat(50))
    println("🎯 COMPONENT USAGE SUMMARY")
    println("=".repeat(50))

    val effectiveUsage = result.getEffectiveUsage()

    if (result.directUsages.isNotEmpty()) {
        println("\n📊 Direct Usages:")
        result.directUsages.forEach { (component, usages) ->
            println("  • $component: ${usages.size} direct usages")
        }
    }

    if (result.wrapperComponents.isNotEmpty() || result.multiComponentWrappers.isNotEmpty()) {
        println("\n🔗 Wrapper Components Found:")
        result.wrapperComponents.forEach { (wrapper, wrappedComponent) ->
            val wrapperUsages = result.wrapperUsages[wrapper]?.size ?: 0
            val displayName = wrapper.substringAfterLast('.')
            val definition = result.wrapperDefinitions[wrapper]
            val packageInfo = if (definition?.packageName?.isNotEmpty() == true) {
                " (${definition.packageName})"
            } else {
                ""
            }
            val ownerTeam = if (definition != null) {
                teamDetector.getTeamForFile(definition.file)
            } else null
            val teamInfo = if (ownerTeam != null) " [👥 $ownerTeam]" else ""
            println("  • $displayName$packageInfo$teamInfo (wraps $wrappedComponent): $wrapperUsages usages")
        }
        result.multiComponentWrappers.forEach { (wrapper, wrappedComponents) ->
            val wrapperUsages = result.wrapperUsages[wrapper]?.size ?: 0
            val displayName = wrapper.substringAfterLast('.')
            val definition = result.wrapperDefinitions[wrapper]
            val packageInfo = if (definition?.packageName?.isNotEmpty() == true) {
                " (${definition.packageName})"
            } else {
                ""
            }
            val ownerTeam = if (definition != null) {
                teamDetector.getTeamForFile(definition.file)
            } else null
            val teamInfo = if (ownerTeam != null) " [👥 $ownerTeam]" else ""
            println("  • $displayName$packageInfo$teamInfo (wraps ${wrappedComponents.joinToString(", ")}): $wrapperUsages usages")
        }
    }

    println("\n🎯 Total Effective Usage (sorted by usage count):")
    if (effectiveUsage.isEmpty()) {
        println("  No components found!")
    } else {
        effectiveUsage.toList().sortedByDescending { it.second }.forEach { (component, count) ->
            println("  • $component: $count total effective usages")
        }
    }

    if (result.teamUsages.isNotEmpty()) {
        println("\n👥 Usage by Team:")
        result.teamUsages.toList().sortedByDescending { (_, components) ->
            components.values.sum()
        }.forEach { (team, components) ->
            val totalUsages = components.values.sum()
            println("  • $team: $totalUsages total usages")
            components.toList().sortedByDescending { it.second }.take(3).forEach { (component, count) ->
                println("    - $component: $count usages")
            }
            if (components.size > 3) {
                println("    - ... and ${components.size - 3} more components")
            }
        }
    }

    println("\n" + "=".repeat(50))
}

// Main execution
runBlocking {
    ComponentUsageReport().main(args)
}
