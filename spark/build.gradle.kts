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
    alias(libs.plugins.spark.library)
    alias(libs.plugins.spark.compose)
    alias(libs.plugins.spark.dokka)
    alias(libs.plugins.spark.publishing)
    alias(libs.plugins.spark.dependencyGuard)
}

android {
    namespace = "com.adevinta.spark"
    resourcePrefix = "spark_"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=com.adevinta.spark.InternalSparkApi",
            "-opt-in=com.adevinta.spark.ExperimentalSparkApi",
        )
    }
}

dependencies {
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.constraintlayout.compose)
    lintPublish(projects.sparkLint)
    lintChecks(libs.slack.lint.compose)

    api(projects.sparkIcons)

    implementation(libs.accompanist.drawablepainter)

    implementation(libs.androidx.appCompat.resources)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.savedstate)
    implementation(libs.androidx.window)

    api(libs.androidx.compose.animation.graphics)
    api(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.iconsCore)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.windowSizeClass)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.text)
    api(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.constraintsExplorer)
    implementation(libs.androidx.compose.ui.util)
    api(platform(libs.coil.bom))
    api(libs.coil.compose)
    api(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.test.truth)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.testParameterInjector)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.ui.testJUnit)
    testImplementation(libs.androidx.compose.ui.testManifest)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.test.truth)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.compose.ui.testJUnit)
    androidTestImplementation(libs.androidx.compose.ui.testManifest)
}
