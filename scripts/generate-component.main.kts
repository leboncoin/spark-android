#!/usr/bin/env kotlin

/*
 * Copyright (c) 2025 Adevinta
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
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.options.varargValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Year
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

private val SPARK_BASE = "com/adevinta/spark"

private fun Path.sparkComponentsPath(packageName: String): Path =
    resolve("spark/src/main/kotlin/$SPARK_BASE/components/$packageName")

private fun Path.screenshotComponentsPath(packageName: String): Path =
    resolve("spark-screenshot-testing/src/test/kotlin/$SPARK_BASE/components/$packageName")

private fun Path.catalogConfiguratorPath(packageName: String): Path =
    resolve("catalog/src/main/kotlin/$SPARK_BASE/catalog/configurator/samples/$packageName")

private fun Path.catalogExamplesPath(packageName: String): Path =
    resolve("catalog/src/main/kotlin/$SPARK_BASE/catalog/examples/samples/$packageName")

private fun detectProjectRoot(): Path {
    val currentDir = Paths.get(System.getProperty("user.dir"))
    return when {
        currentDir.resolve("spotless").exists() -> currentDir
        currentDir.fileName.toString() == "scripts" &&
            currentDir.parent?.resolve("spotless")?.exists() == true -> currentDir.parent!!
        else -> currentDir
    }
}

private fun String.applySubstitutions(substitutions: Map<String, String>): String =
    substitutions.entries.fold(this) { content, (key, value) -> content.replace(key, value) }

private fun validateTemplatesExist(templateDir: Path, templateFiles: List<String>): List<Path> =
    templateFiles.map { templateDir.resolve(it) }.filter { !it.exists() }

private data class ComponentNames(
    val componentName: String,
    val packageName: String,
) {
    val componentNameLower: String get() = componentName.lowercase()
    val sparkComponentName: String get() = "Spark$componentName"
    val componentNameScreenshot: String get() = "${componentName}Screenshot"
    val componentNameConfigurator: String get() = "${componentName}Configurator"
    val componentNameExamples: String get() = "${componentName}Examples"
    val componentNameDefaults: String get() = "${componentName}Defaults"
    val componentNameSamples: String get() = "${componentName}Samples"
    val exampleDescriptionVarName: String get() = "${componentName}ExampleDescription"
    val exampleSourceUrlVarName: String get() = "${componentName}ExampleSourceUrl"
}

private fun buildVariantsContent(names: ComponentNames, variantList: List<String>): String {
    val variantBlock = { variant: String ->
        """    /**
     * ${names.componentName} $variant variant.
     *
     * TODO: Add description for $variant variant.
     *
     * @param modifier the Modifier to be applied to this ${names.componentNameLower}
     */
    @Composable
    public fun $variant(
        modifier: Modifier = Modifier,
    ) {
        ${names.sparkComponentName}(
            modifier = modifier,
        )
    }"""
    }
    return if (variantList.isNotEmpty()) {
        variantList.joinToString("\n\n", transform = variantBlock)
    } else {
        variantBlock("Default")
            .replace("TODO: Add description for Default variant", "TODO: Add component description")
            .replace(" Default variant", " component")
    }
}

private fun buildScreenshotTable(variantList: List<String>): String {
    val placeholder = "![](../../images/...)"
    return if (variantList.isNotEmpty()) {
        val header = variantList.joinToString(" | ") { it }
        val separator = variantList.joinToString("|") { "---" }
        val row = variantList.joinToString(" | ") { placeholder }
        """
|       | $header |
|-------|$separator|
| Light | $row |
| Dark  | $row |
""".trimIndent()
    } else {
        """
| Light | Dark |
|-------|------|
| $placeholder | $placeholder |
""".trimIndent()
    }
}

private fun buildVariantsSections(componentName: String, variantList: List<String>): String =
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

private fun buildTemplateMappings(
    names: ComponentNames,
    copyright: String,
    variantsContent: String,
    screenshotTable: String,
    variantsSections: String,
    firstVariant: String,
    examplesContent: String,
): Map<String, String> = mapOf(
    $$"$componentName" to names.componentName,
    $$"$componentNameLower" to names.componentNameLower,
    $$"$packageName" to names.packageName,
    $$"$sparkComponentName" to names.sparkComponentName,
    $$"$componentNameScreenshot" to names.componentNameScreenshot,
    $$"$componentNameConfigurator" to names.componentNameConfigurator,
    $$"$componentNameExamples" to names.componentNameExamples,
    $$"$componentNameDefaults" to names.componentNameDefaults,
    $$"$componentNameSamples" to names.componentNameSamples,
    $$"$componentNameExampleDescription" to names.exampleDescriptionVarName,
    $$"$componentNameExampleSourceUrl" to names.exampleSourceUrlVarName,
    $$"$copyright" to copyright,
    $$"$variantsContent" to variantsContent,
    $$"$screenshotTable" to screenshotTable,
    $$"$variantsSections" to variantsSections,
    $$"$firstVariant" to firstVariant,
    $$"$examplesContent" to examplesContent,
)

private fun buildExamplesContent(names: ComponentNames, variantList: List<String>): String {
    val exampleBlock = { id: String, displayName: String, call: String ->
        """    Example(
        id = "$id",
        name = "$displayName",
        description = ${names.exampleDescriptionVarName},
        sourceUrl = ${names.exampleSourceUrlVarName},
    ) {
        $call
    }"""
    }
    return if (variantList.isNotEmpty()) {
        variantList.joinToString(",\n") { variant ->
            exampleBlock(variant.lowercase(), "$variant ${names.componentName}", "${names.componentName}.$variant()")
        }
    } else {
        exampleBlock("default", names.componentName, "${names.componentName}.Default()")
    }
}

/**
 * Component Generator creates the complete file structure for new Spark components.
 */
class GenerateComponent : SuspendingCliktCommand(
    name = "generate-component.main.kts",
) {
    private val componentName: String by option(
        help = "Component name in PascalCase (e.g., Card)",
    ).prompt("Enter component name (PascalCase)")
        .validate { name ->
            if (name.isEmpty()) {
                throw UsageError("Component name cannot be empty")
            }
            if (!name.matches(Regex("^[A-Z][a-zA-Z0-9]*$"))) {
                throw UsageError("Component name must be a valid PascalCase identifier (e.g., Card, Button)")
            }
        }

    private val packageName: String by option(
        help = "Package name (e.g., card)",
    ).prompt("Enter package name")
        .validate { name ->
            if (name.isEmpty()) {
                throw UsageError("Package name cannot be empty")
            }
            if (!name.matches(Regex("^[a-z][a-z0-9_]*$"))) {
                throw UsageError("Package name must be a valid lowercase identifier (e.g., card, button)")
            }
        }
    private val variants: List<String>? by option(
        "--variants", "-v",
        help = "Variant names (can be specified multiple times, e.g., -v Elevated -v Outlined)",
    ).varargValues()

    private val dryRun by option(
        "--dry-run",
        help = "Preview what would be generated without creating files",
    ).flag(default = false)

    override suspend fun run() {
        val variantList = variants?.filter { it.isNotEmpty() }.orEmpty()
        val projectRoot = detectProjectRoot()

        val copyright = projectRoot
            .resolve("spotless/spotless.kt")
            .readText()
            .replace($$"$YEAR", Year.now().value.toString())

        val names = ComponentNames(componentName, packageName)

        val variantsContent = buildVariantsContent(names, variantList)
        val screenshotTable = buildScreenshotTable(variantList)
        val firstVariant = variantList.firstOrNull() ?: "Default"
        val variantsSections = buildVariantsSections(names.componentName, variantList)
        val examplesContent = buildExamplesContent(names, variantList)

        val templateMappings = buildTemplateMappings(
            names = names,
            copyright = copyright,
            variantsContent = variantsContent,
            screenshotTable = screenshotTable,
            variantsSections = variantsSections,
            firstVariant = firstVariant,
            examplesContent = examplesContent,
        )

        val componentsPath = projectRoot.sparkComponentsPath(names.packageName)
        val templateDir = projectRoot.resolve("scripts/templates/component")
        val filesToGenerate = listOf(
            "Component.kt.template" to componentsPath.resolve("${names.componentName}.kt"),
            "SparkComponent.kt.template" to componentsPath.resolve("${names.sparkComponentName}.kt"),
            "ComponentDefaults.kt.template" to componentsPath.resolve("${names.componentNameDefaults}.kt"),
            "Component.md.template" to componentsPath.resolve("${names.componentName}.md"),
            "ComponentScreenshot.kt.template" to projectRoot.screenshotComponentsPath(names.packageName)
                .resolve("${names.componentNameScreenshot}.kt"),
            "ComponentConfigurator.kt.template" to projectRoot.catalogConfiguratorPath(names.packageName)
                .resolve("${names.componentNameConfigurator}.kt"),
            "ComponentExamples.kt.template" to projectRoot.catalogExamplesPath(names.packageName)
                .resolve("${names.componentNameExamples}.kt"),
        )
        val missingTemplates = validateTemplatesExist(templateDir, filesToGenerate.map { it.first })

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

                        val content = templatePath.readText().applySubstitutions(templateMappings)

                        if (dryRun) {
                            echo("📄 Would create: ${targetPath.absolutePathString()}")
                            targetPath
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

        echo(buildSummaryMessage(dryRun, generatedFiles.size))
    }
}

private fun buildSummaryMessage(dryRun: Boolean, generatedCount: Int): String {
    val prefix = "\n"
    return when {
        generatedCount == 0 && dryRun -> "⚠️  No new files would be generated (all files already exist)."
        generatedCount == 0 -> "⚠️  No files were generated."
        dryRun -> "🔍 Would generate $generatedCount file(s) (dry-run mode).\n   Run without --dry-run to actually create the files."
        else -> "✅ Successfully generated $generatedCount file(s)!"
    }.let { prefix + it }
}

@OptIn(ExperimentalPathApi::class)
runBlocking {
    GenerateComponent().main(args)
}
