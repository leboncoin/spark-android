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
import java.util.Properties

plugins {
    id("com.adevinta.spark.android-application")
    id("com.adevinta.spark.android-compose")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.adevinta.spark.catalog"
    defaultConfig.applicationId = "com.adevinta.spark.catalog"
    defaultConfig.resourceConfigurations.addAll(
        setOf("en-rGB", "fr", "de"),
    )
    defaultConfig {
        versionName = version.toString()
        if (providers.environmentVariable("GITHUB_ACTION").isPresent) {
            versionName = version.toString().replace("SNAPSHOT", System.getenv("GITHUB_SHA").take(7))
        }
    }

    compileOptions.isCoreLibraryDesugaringEnabled = true

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=com.adevinta.spark.InternalSparkApi",
            "-opt-in=com.adevinta.spark.ExperimentalSparkApi",
        )
    }

    val keystore = rootProject.file("keystore.properties")
        .takeIf { it.exists() }
        ?.let { Properties().apply { load(it.inputStream()) } }

    val debug by signingConfigs.getting
    val release by signingConfigs.creating {
        if (keystore == null) return@creating
        keyAlias = keystore.getProperty("keyAlias")
        keyPassword = keystore.getProperty("keyPassword")
        storeFile = file(keystore.getProperty("storeFile"))
        storePassword = keystore.getProperty("storePassword")
    }

    buildTypes.named("release") {
        signingConfig = if (keystore != null) release else debug
    }
}

dependencies {
    implementation(projects.spark)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.accompanist.drawablepainter)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.graphics.shapes)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material.motion)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)

    coreLibraryDesugaring(libs.desugarJdkLibs)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
