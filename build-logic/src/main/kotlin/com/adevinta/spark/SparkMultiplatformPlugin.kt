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
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
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
                if (isAndroidMultiplatformLibrary) {
                    androidLibrary {
                        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
                    }
                }
            }

            compilerOptions {
                if (hasSparkInternalAnnotations) {
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

            if (hasSparkInternalAnnotations) {
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

            explicitApi()

            sourceSets.apply {
                create("nonAndroidMain") { dependsOn(commonMain.get()) }
                jvmMain.get().dependsOn(getByName("nonAndroidMain"))
                // Add other / future source sets here as needed, e.g. jsMain, iosMain, etc.
            }
        }

        dependencies {
            add("commonMainImplementation", platform(spark().libraries.`kotlin-bom`))
        }
    }
}
