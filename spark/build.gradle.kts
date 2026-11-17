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
plugins {
    alias(libs.plugins.spark.multiplatform)
    alias(libs.plugins.spark.multiplatform.library)
    alias(libs.plugins.spark.compose.multiplatform)
    // Android-KMP modules create lint tasks only with the standalone lint plugin.
    alias(libs.plugins.android.lint)
    alias(libs.plugins.spark.publishing)
    alias(libs.plugins.spark.dependencyGuard)
    alias(libs.plugins.spark.dokka)
    alias(libs.plugins.spark.spotless)
    alias(libs.plugins.baselineprofile)
}

kotlin {
    android {
        namespace = "com.adevinta.spark"
        androidResources {
            resourcePrefix = "spark_"
        }
        optimization {
            consumerKeepRules.file("consumer-rules.pro")
        }
        withHostTest {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(project.dependencies.platform(projects.sparkBom))
            api(projects.sparkCore)
            api(projects.sparkIcons)

            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.material3.adaptive)
            api(libs.compose.ui)
            api(libs.compose.ui.tooling.preview)
            api(project.dependencies.platform(libs.coil.bom))
            api(libs.coil.compose)
            api(libs.kotlinx.collections.immutable)
            // FileKit for file picker dialogs in the configurator
            api(libs.filekit)
            api(libs.filekit.dialogs)
        }
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.accompanist.drawablepainter)

            implementation(libs.androidx.appCompat.resources)
            implementation(libs.androidx.core)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.savedstate)
            implementation(libs.androidx.window)

            api(libs.androidx.compose.animation.graphics)
            api(libs.androidx.compose.ui.text)
            implementation(libs.androidx.compose.ui.util)
        }
        getByName("androidHostTest").dependencies {
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.junit)
            implementation(libs.kotlin.test)
            implementation(libs.androidx.test.truth)
            implementation(libs.androidx.test.runner)
            implementation(libs.testParameterInjector)
            implementation(libs.robolectric)
            implementation(libs.androidx.compose.ui.testJUnit)
            implementation(libs.androidx.compose.ui.testManifest)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.junit)
            implementation(libs.kotlin.test)
            implementation(libs.androidx.test.truth)
            implementation(libs.androidx.test.runner)
            implementation(libs.androidx.compose.ui.testJUnit)
            implementation(libs.androidx.compose.ui.testManifest)
        }
    }
}

dependencies {
    lintPublish(projects.sparkLint)
    lintChecks(projects.sparkLint)
    lintChecks(libs.slack.lint.compose)

    androidRuntimeClasspath(libs.compose.ui.tooling)

    baselineProfile(project(":catalog:baselineprofile"))
}

// Baseline Profile Gradle plugin configuration.
baselineProfile {

    // Filters the generated profile rules.
    filter {
        include("com.adevinta.spark.**")
        exclude("com.adevinta.spark.catalog.**")
        exclude("com.adevinta.spark.icons.**")
    }
}
