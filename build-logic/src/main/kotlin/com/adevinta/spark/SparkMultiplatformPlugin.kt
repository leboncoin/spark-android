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
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

@OptIn(ExperimentalKotlinGradlePluginApi::class)
public class SparkMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            jvm()
            if (isAndroid) {
                androidTarget()
                if (isAndroidMultiplaformLibrary) {
                    androidLibrary {
                        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
                    }
                }
            }
//            @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
//            wasmJs {
//                browser()
//                binaries.executable()
//            }

            // We don't need to build an iOS x64 framework
            // iosX64()
            // iosArm64()
            // iosSimulatorArm64()

            compilerOptions {
                // Only add opt-in flags for modules that actually have these annotations
                if (project.name != "spark-icons") {
                    freeCompilerArgs.addAll(
                        listOf(
                            "-Xexpect-actual-classes",
                            "-opt-in=com.adevinta.spark.InternalSparkApi",
                            "-opt-in=com.adevinta.spark.ExperimentalSparkApi",
                        ),
                    )
                }
                allWarningsAsErrors.set(true)
            }

            if (project.name != "spark-icons") {
                sourceSets.all {
                    languageSettings.optIn("com.adevinta.spark.InternalSparkApi")
                    languageSettings.optIn("com.adevinta.spark.ExperimentalSparkApi")
                }
            }

            metadata {
                compilations.configureEach {
                    if (name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME) {
                        compileTaskProvider.configure {
                            // We replace the default library names with something more unique (the project path).
                            // This allows us to avoid the annoying issue of `duplicate library name: foo_commonMain`
                            // https://youtrack.jetbrains.com/issue/KT-57914
                            val projectPath = this@with.path.substring(1).replace(":", "_")
                            this as KotlinCompileCommon
                            moduleName.set("${projectPath}_commonMain")
                        }
                    }
                }
            }

//            configureSpotless()
//            configureKotlin()
            explicitApi()

//            addKotlinBom()
        }
    }
}

public fun Project.addKspDependencyForAllTargets(dependencyNotation: Any): Unit =
    addKspDependencyForAllTargets("", dependencyNotation)

public fun Project.addKspTestDependencyForAllTargets(dependencyNotation: Any): Unit =
    addKspDependencyForAllTargets("Test", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets.asSequence().filter { target ->
            // Don't add KSP for common target, only final platforms
            target.platformType != KotlinPlatformType.common
        }.forEach { target ->
            add(
                "ksp${target.targetName.capitalized()}$configurationNameSuffix",
                dependencyNotation,
            )
        }
    }
}
