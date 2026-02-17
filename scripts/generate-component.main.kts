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
@file:DependsOn("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:4.2.0")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.prompt
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.varargValues
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Year
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

/**
 * Component Generator creates the complete file structure for new Spark components.
 */
class GenerateComponent : SuspendingCliktCommand(
    name = "generate-component.main.kts",
    help = "⚙️ Component Generator: Scaffold new Spark components",
) {
    private val componentName: String by option(

        name = "component-name",
        help = "Component name in PascalCase (e.g., Card)",
    ).prompt("Enter component name (PascalCase)")

    private val packageName: String by option(
        name = "package-name",
        help = "Package name (e.g., card)",
    ).prompt("Enter package name")
    private val variants: List<String>? by option(
        "--variants", "-v",
        help = "Variant names (can be specified multiple times, e.g., -v Elevated -v Outlined)",
    ).varargValues()

    private val dryRun by option(
        "--dry-run",
        help = "Preview what would be generated without creating files",
    ).flag(default = false)

    override suspend fun run() {
        // Validate inputs
        validateComponentName(componentName)
        validatePackageName(packageName)

        // Use variants list directly
        val variantList = variants?.filter { it.isNotEmpty() }.orEmpty()

        // Get project root (assuming script is in scripts/ directory)
        // Try to detect project root by looking for spotless directory
        val currentDir = Paths.get(System.getProperty("user.dir"))
        val projectRoot = if (currentDir.resolve("spotless").exists()) {
            currentDir
        } else if (currentDir.fileName.toString() == "scripts" && currentDir.parent?.resolve("spotless")?.exists() == true) {
            currentDir.parent
        } else {
            // Fallback: assume we're in project root
            currentDir
        } ?: currentDir

        // Read copyright template
        val copyrightTemplate = projectRoot
            .resolve("spotless")
            .resolve("spotless.kt")
            .readText()
        val copyright = copyrightTemplate.replace("\$YEAR", Year.now().value.toString())

        // Derive spark component name
        val sparkComponentName = "Spark$componentName"
        val componentNameLower = componentName.lowercase()
        val componentNameScreenshot = "${componentName}Screenshot"
        val componentNameConfigurator = "${componentName}Configurator"
        val componentNameExamples = "${componentName}Examples"
        val componentNameDefaults = "${componentName}Defaults"
        val componentNameSamples = "${componentName}Samples"

        // Generate variants content for Component.kt
        val variantsContent = if (variantList.isNotEmpty()) {
            variantList.joinToString("\n\n") { variant ->
                """    /**
     * $componentName $variant variant.
     *
     * TODO: Add description for $variant variant.
     *
     * @param modifier the Modifier to be applied to this $componentNameLower
     */
    @Composable
    public fun $variant(
        modifier: Modifier = Modifier,
    ) {
        $sparkComponentName(
            modifier = modifier,
        )
    }"""
            }
        } else {
            """    /**
     * $componentName component.
     *
     * TODO: Add component description.
     *
     * @param modifier the Modifier to be applied to this $componentNameLower
     */
    @Composable
    public fun Default(
        modifier: Modifier = Modifier,
    ) {
        $sparkComponentName(
            modifier = modifier,
        )
    }"""
        }

        // Generate screenshot table for Component.md
        val screenshotTable = if (variantList.isNotEmpty()) {
            val header = variantList.joinToString(" | ") { it }
            val separator = variantList.joinToString("|") { "---" }
            val lightRow = variantList.joinToString(" | ") { "![](../../images/...)" }
            val darkRow = variantList.joinToString(" | ") { "![](../../images/...)" }
            """
|       | $header |
|-------|$separator|
| Light | $lightRow |
| Dark  | $darkRow |
""".trimIndent()
        } else {
            """
| Light | Dark |
|-------|------|
| ![](../../images/...) | ![](../../images/...) |
""".trimIndent()
        }

        // Generate variants sections for Component.md
        val firstVariant = variantList.firstOrNull() ?: "Default"
        val variantsSections = if (variantList.isNotEmpty()) {
            variantList.joinToString("\n\n") { variant ->
                """
#### $componentName.$variant

TODO: Add description for $variant variant.

```kotlin
$componentName.$variant()
```

| Light | Dark |
|-------|------|
| ![](../../images/...) | ![](../../images/...) |
""".trimIndent()
            }
        } else {
            ""
        }

        // Generate examples content for ComponentExamples.kt
        val exampleDescriptionVarName = "${componentName}ExampleDescription"
        val exampleSourceUrlVarName = "${componentName}ExampleSourceUrl"
        val examplesContent = if (variantList.isNotEmpty()) {
            variantList.joinToString(",\n") { variant ->
                """    Example(
        id = "${variant.lowercase()}",
        name = "$variant $componentName",
        description = $exampleDescriptionVarName,
        sourceUrl = $exampleSourceUrlVarName,
    ) {
        $componentName.$variant()
    }"""
            }
        } else {
            """    Example(
        id = "default",
        name = "$componentName",
        description = $exampleDescriptionVarName,
        sourceUrl = $exampleSourceUrlVarName,
    ) {
        $componentName.Default()
    }"""
        }

        // Template mappings
        val templateMappings = mapOf(
            $$"$componentName" to componentName,
            $$"$componentNameLower" to componentNameLower,
            $$"$packageName" to packageName,
            $$"$sparkComponentName" to sparkComponentName,
            $$"$componentNameScreenshot" to componentNameScreenshot,
            $$"$componentNameConfigurator" to componentNameConfigurator,
            $$"$componentNameExamples" to componentNameExamples,
            $$"$componentNameDefaults" to componentNameDefaults,
            $$"$componentNameSamples" to componentNameSamples,
            $$"$componentNameExampleDescription" to exampleDescriptionVarName,
            $$"$componentNameExampleSourceUrl" to exampleSourceUrlVarName,
            $$"$copyright" to copyright,
            $$"$variantsContent" to variantsContent,
            $$"$screenshotTable" to screenshotTable,
            $$"$variantsSections" to variantsSections,
            $$"$firstVariant" to firstVariant,
            $$"$examplesContent" to examplesContent,
        )

        // File generation mappings: template file -> target path
        val filesToGenerate = listOf(
            "Component.kt.template" to projectRoot
                .resolve("spark")
                .resolve("src")
                .resolve("main")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("components")
                .resolve(packageName)
                .resolve("$componentName.kt"),
            "SparkComponent.kt.template" to projectRoot
                .resolve("spark")
                .resolve("src")
                .resolve("main")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("components")
                .resolve(packageName)
                .resolve("$sparkComponentName.kt"),
            "ComponentDefaults.kt.template" to projectRoot
                .resolve("spark")
                .resolve("src")
                .resolve("main")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("components")
                .resolve(packageName)
                .resolve("$componentNameDefaults.kt"),
            "Component.md.template" to projectRoot
                .resolve("spark")
                .resolve("src")
                .resolve("main")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("components")
                .resolve(packageName)
                .resolve("$componentName.md"),
            "ComponentScreenshot.kt.template" to projectRoot
                .resolve("spark-screenshot-testing")
                .resolve("src")
                .resolve("test")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("components")
                .resolve(packageName)
                .resolve("$componentNameScreenshot.kt"),
            "ComponentConfigurator.kt.template" to projectRoot
                .resolve("catalog")
                .resolve("src")
                .resolve("main")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("catalog")
                .resolve("configurator")
                .resolve("samples")
                .resolve(packageName)
                .resolve("$componentNameConfigurator.kt"),
            "ComponentExamples.kt.template" to projectRoot
                .resolve("catalog")
                .resolve("src")
                .resolve("main")
                .resolve("kotlin")
                .resolve("com")
                .resolve("adevinta")
                .resolve("spark")
                .resolve("catalog")
                .resolve("examples")
                .resolve("samples")
                .resolve(packageName)
                .resolve("$componentNameExamples.kt"),
        )

        // Template directory
        val templateDir = projectRoot
            .resolve("scripts")
            .resolve("templates")
            .resolve("component")

        // Validate all templates exist before starting
        val missingTemplates = mutableListOf<Path>()
        for ((templateFile, _) in filesToGenerate) {
            val templatePath = templateDir.resolve(templateFile)
            if (!templatePath.exists()) {
                missingTemplates.add(templatePath)
            }
        }

        if (missingTemplates.isNotEmpty()) {
            echo("❌ Missing template files:")
            missingTemplates.forEach { echo("   - ${it.absolutePathString()}") }
            throw UsageError("Cannot proceed: ${missingTemplates.size} template file(s) are missing")
        }

        if (dryRun) {
            echo("🔍 DRY RUN MODE - No files will be created\n")
        }

        // Generate files concurrently
        val generatedFiles = supervisorScope {
            filesToGenerate.map { (templateFile, targetPath) ->
                async(Dispatchers.IO) {
                    try {
                        val templatePath = templateDir.resolve(templateFile)

                        if (targetPath.exists()) {
                            echo("⚠️  File already exists: ${targetPath.absolutePathString()}")
                            echo("   Skipping...")
                            return@async null
                        }

                        // Read template
                        var content = templatePath.readText()

                        // Substitute variables
                        for ((key, value) in templateMappings) {
                            content = content.replace(key, value)
                        }

                        if (dryRun) {
                            echo("📄 Would create: ${targetPath.absolutePathString()}")
                            if (targetPath.exists()) {
                                echo("   ⚠️  File already exists - would be skipped")
                                null
                            } else {
                                targetPath
                            }
                        } else {
                            // Create parent directories
                            targetPath.parent?.createDirectories()

                            // Write file
                            targetPath.writeText(content)
                            echo("✅ Created: ${targetPath.absolutePathString()}")
                            targetPath
                        }
                    } catch (e: Exception) {
                        echo("❌ Error processing ${templateFile}: ${e.message}")
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

        if (dryRun) {
            if (generatedFiles.isEmpty()) {
                echo("\n⚠️  No new files would be generated (all files already exist).")
            } else {
                echo("\n🔍 Would generate ${generatedFiles.size} file(s) (dry-run mode).")
                echo("   Run without --dry-run to actually create the files.")
            }
        } else {
            if (generatedFiles.isEmpty()) {
                echo("\n⚠️  No files were generated.")
            } else {
                echo("\n✅ Successfully generated ${generatedFiles.size} file(s)!")
            }
        }
    }

    private fun validateComponentName(name: String) {
        if (name.isEmpty()) {
            throw UsageError("Component name cannot be empty")
        }
        if (!name.matches(Regex("^[A-Z][a-zA-Z0-9]*$"))) {
            throw UsageError("Component name must be a valid PascalCase identifier (e.g., Card, Button)")
        }
    }

    private fun validatePackageName(name: String) {
        if (name.isEmpty()) {
            throw UsageError("Package name cannot be empty")
        }
        if (!name.matches(Regex("^[a-z][a-z0-9_]*$"))) {
            throw UsageError("Package name must be a valid lowercase identifier (e.g., card, button)")
        }
    }
}

@OptIn(ExperimentalPathApi::class)
runBlocking {
    GenerateComponent().main(args)
}
