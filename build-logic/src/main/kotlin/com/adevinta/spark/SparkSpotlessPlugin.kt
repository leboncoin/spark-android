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

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal class SparkSpotlessPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.diffplug.spotless")

            val ktlint = spark().libraries.`ktlint-bom`.get().version
            configure<SpotlessExtension> {
                val licenseHeader = rootProject.file("spotless/spotless.kt")
                format("misc") {
                    target("**/*.md", "**/.gitignore")
                    endWithNewline()
                }
                kotlin {
                    target("src/**/*.kt")
                    ktlint(ktlint.toString())
                    trimTrailingWhitespace()
                    endWithNewline()
                    licenseHeaderFile(licenseHeader)
                    targetExclude("spotless/*.kt")
                }
                kotlinGradle {
                    ktlint(ktlint.toString())
                    trimTrailingWhitespace()
                    endWithNewline()
                    licenseHeaderFile(
                        licenseHeader,
                        "(import |plugins|pluginManagement|rootProject|dependencyResolutionManagement|//)",
                    )
                }
            }
        }
    }
}
