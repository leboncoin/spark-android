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
package com.adevinta.spark

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import java.io.File
import java.net.URI
import java.time.Year

internal class SparkDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.dokka")

            when {
                this === rootProject -> configureRootProject()
                else -> configureSubProject()
            }

            dependencies {
                add("dokkaPlugin", spark().libraries.`dokka-android-documentation-plugin`)
            }
        }
    }

    private fun Project.configureRootProject() {
        val proj = this
        extensions.configure<DokkaExtension> {
            moduleName.set("Spark")
            dokkaPublications.named("html") {
                outputDirectory.set(proj.layout.buildDirectory.dir("dokka"))
            }
            pluginsConfiguration.named("html", DokkaHtmlPluginParameters::class.java) {
                fun File.recursiveAssets() = walk().filter(File::isFile)
                    // https://github.com/Kotlin/dokka/issues/3400
                    .filter { runCatching { URI(it.name) }.isSuccess }
                    .toList().toTypedArray()
                // https://kotlinlang.org/docs/dokka-html.html#customize-assets
                customAssets.from(
                    proj.file("art/logo-icon.svg"), // https://kotlinlang.org/docs/dokka-html.html#change-the-logo
                    *proj.rootDir.resolve("art").recursiveAssets(),
                    *proj.rootDir.resolve("spark-screenshot-testing/src/test/snapshots/images").recursiveAssets(),
                )
                footerMessage.set("© ${Year.now().value} Adevinta")
            }
        }
    }

    private fun Project.configureSubProject() {
        val proj = this
        extensions.configure<DokkaExtension> {
            dokkaSourceSets.configureEach {
                // Parse Module and Package docs
                // https://kotlinlang.org/docs/dokka-module-and-package-docs.html
                proj.projectDir.resolve("src").walk()
                    .filter { it.isFile && it.extension == "md" }.toList()
                    .let { includes.from(it) }

                // List of files or directories containing sample code (referenced with @sample tags)
                proj.projectDir.resolve("samples").walk()
                    .filter { it.isFile && it.extension == "kt" }.toList()
                    .let { samples.from(it) }

                // https://kotlinlang.org/docs/dokka-gradle.html#source-link-configuration
                sourceLink {
                    val url = "https://github.com/leboncoin/spark-android/tree/main/${proj.name}/src/main/kotlin"
                    localDirectory.set(proj.projectDir.resolve("src"))
                    remoteUrl(url)
                    remoteLineSuffix.set("#L")
                }

                // Only document public API surface; protected/internal/private members add noise
                documentedVisibilities(VisibilityModifier.Public)

                // Skip packages that have no documented items after applying the above filters
                skipEmptyPackages.set(true)
            }
            pluginsConfiguration.named("html", DokkaHtmlPluginParameters::class.java) {
                footerMessage.set("© ${Year.now().value} Adevinta")
            }
        }
    }
}
