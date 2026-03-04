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

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * Inspired by https://github.com/slackhq/slack-gradle-plugin
 */
internal object SparkUnitTests {
    private const val GLOBAL_CI_UNIT_TEST_TASK_NAME = "globalCiUnitTest"
    private const val CI_UNIT_TEST_TASK_NAME = "ciUnitTest"
    private const val COMPILE_CI_UNIT_TEST_NAME = "compileCiUnitTest"
    private const val LOG = "SparkUnitTests:"

    fun configureRootProject(project: Project): TaskProvider<Task> =
        project.tasks.register(GLOBAL_CI_UNIT_TEST_TASK_NAME) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            description = "Global lifecycle task to run all ciUnitTest tasks."
        }

    fun configureSubproject(project: Project) {
        val globalTask = project.rootProject.tasks.named(GLOBAL_CI_UNIT_TEST_TASK_NAME)
        project.pluginManager.withPlugin("com.android.base") {
            createAndroidCiUnitTestTask(project, globalTask)
        }
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            createJvmCiUnitTestTask(project, globalTask)
        }
        configureTestTasks(project)
    }

    private fun createJvmCiUnitTestTask(
        project: Project,
        globalTask: TaskProvider<Task>,
    ) {
        project.logger.debug("$LOG Creating CI unit test tasks for project '$project'")
        val ciUnitTest = project.tasks.register(CI_UNIT_TEST_TASK_NAME) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            dependsOn("test")
        }
        globalTask.configure { dependsOn(ciUnitTest) }
        project.tasks.register(COMPILE_CI_UNIT_TEST_NAME) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            dependsOn("testClasses")
        }
    }

    private fun createAndroidCiUnitTestTask(
        project: Project,
        globalTask: TaskProvider<Task>,
    ) {
        val variant = project.spark().ciUnitTestVariant.get().capitalized()
        val variantUnitTestTaskName = "test${variant}UnitTest"
        val variantCompileUnitTestTaskName = "compile${variant}UnitTestSources"
        project.logger.debug("$LOG Creating CI unit test tasks for project '$project' and variant '$variant'")
        val ciUnitTest = project.tasks.register(CI_UNIT_TEST_TASK_NAME) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            dependsOn(variantUnitTestTaskName)
        }
        globalTask.configure { dependsOn(ciUnitTest) }
        project.tasks.register(COMPILE_CI_UNIT_TEST_NAME) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            dependsOn(variantCompileUnitTestTaskName)
        }
    }

    private fun configureTestTasks(project: Project) {
        project.tasks.withType<Test>().configureEach {
            // Bump max heap space for paparazzi
            // https://github.com/cashapp/paparazzi/issues/915
            maxHeapSize = "1g"
            testLogging {
                showStandardStreams = true
                showStackTraces = true
                exceptionFormat = FULL
                events(STARTED, PASSED, FAILED, SKIPPED)
            }
            reports {
                // Disable HTML reports until Paparazzi supports Gradle 9.3+ APIs
                // https://github.com/cashapp/paparazzi/issues/2182
                html.required.set(false)
            }
        }
    }
}
