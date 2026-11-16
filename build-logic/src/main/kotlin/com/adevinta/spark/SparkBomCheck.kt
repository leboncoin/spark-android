/*
 * Copyright (c) 2026 Adevinta
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

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * Fails when a module from [SparkPublication.publishedModules] is missing from the BOM constraints.
 *
 * To fix a failure, add the line that the message prints for each module to `spark-bom/build.gradle.kts`.
 */
public abstract class CheckBomTask : DefaultTask() {

    @get:Input
    public abstract val publishedModules: SetProperty<String>

    @get:Input
    public abstract val bomModules: SetProperty<String>

    @TaskAction
    public fun check() {
        val missing = publishedModules.get() - bomModules.get()
        if (missing.isNotEmpty()) {
            val lines = missing.sorted().joinToString("\n") {
                "    api(\"com.adevinta.spark:${it.removePrefix(":")}:\$version\")"
            }
            throw GradleException(
                "These modules from SparkPublication.publishedModules are missing from the BOM constraints " +
                    "in spark-bom/build.gradle.kts: ${missing.sorted().joinToString()}.\n" +
                    "Add these lines to the constraints block:\n$lines\n" +
                    "If a module must stay out of the BOM, add it to SparkBomCheck.excludedModules instead.",
            )
        }
    }
}

internal object SparkBomCheck {

    /** Published modules that stay out of the BOM. The BOM cannot list itself as a constraint. */
    private val excludedModules = setOf(":spark-bom")

    fun configureBomProject(bom: Project) = with(bom) {
        val checkBom = tasks.register<CheckBomTask>("checkBom") {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            description = "Checks that every published module is declared in the BOM."
            publishedModules.set(
                SparkPublication.publishedModules.filterNot { it in excludedModules },
            )
            bomModules.set(
                configurations.named("api").map { api -> api.dependencyConstraints.map { ":${it.name}" } },
            )
        }
        tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) { dependsOn(checkBom) }
    }
}
