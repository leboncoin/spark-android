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
    alias(libs.plugins.spark.application)
    alias(libs.plugins.spark.compose)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spark.spotless)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.adevinta.spark.catalog"
    defaultConfig.applicationId = "com.adevinta.spark.catalog"
    @Suppress("UnstableApiUsage")
    androidResources.localeFilters += setOf("en-rGB", "fr", "en-rXA", "ar-rXB")
    defaultConfig {
        versionName = version.toString()
        if (providers.environmentVariable("GITHUB_ACTION").isPresent) {
            versionName = version.toString().replace("SNAPSHOT", System.getenv("GITHUB_SHA").take(7))
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions.isCoreLibraryDesugaringEnabled = true

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true

        managedDevices {
            val phone = allDevices.create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6Api33") {
                device = "Pixel 6"
                apiLevel = 33
                systemImageSource = "aosp"
            }
            val tablet = allDevices.create<com.android.build.api.dsl.ManagedVirtualDevice>("pixelTabletApi33") {
                device = "Pixel Tablet"
                apiLevel = 33
                systemImageSource = "aosp"
            }
            val foldable = allDevices.create<com.android.build.api.dsl.ManagedVirtualDevice>("pixelFoldApi33") {
                device = "Pixel Fold"
                apiLevel = 33
                systemImageSource = "aosp"
            }
            groups.create("catalogDevices") {
                targetDevices += listOf(phone, tablet, foldable)
            }
        }
    }

    lint.baseline = file("lint-baseline.xml")

    val keystore = rootDir.resolve("keystore.properties")
        .takeIf { it.exists() }
        ?.let { Properties().apply { load(it.inputStream()) } }

    val debug = signingConfigs.getByName("debug")
    //noinspection WrongGradleMethod
    val release = signingConfigs.create("release") {
        if (keystore == null) return@create
        keyAlias = keystore.getProperty("keyAlias")
        keyPassword = keystore.getProperty("keyPassword")
        storeFile = file(keystore.getProperty("storeFile"))
        storePassword = keystore.getProperty("storePassword")
    }

    buildTypes.named("debug") {
        isPseudoLocalesEnabled = true
    }

    buildTypes.named("release") {
        signingConfig = if (keystore != null) release else debug
    }

    buildTypes.create("benchmark") {
        initWith(buildTypes.getByName("release"))
        matchingFallbacks += listOf("release")
        isDebuggable = false
        signingConfig = signingConfigs.getByName("debug")
        proguardFiles("benchmark-rules.pro")
    }
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "com.adevinta.spark.InternalSparkApi",
            "com.adevinta.spark.ExperimentalSparkApi",
        )
    }
}

// Paparazzi and Robolectric both load libandroid_runtime natively, so they cannot share a test JVM.
// https://github.com/cashapp/paparazzi/issues/1979
tasks.withType<Test>().configureEach {
    forkEvery = 1
}

dependencies {
    implementation(projects.spark)
    lintChecks(projects.sparkLint)
    lintChecks(libs.slack.lint.compose)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.accompanist.drawablepainter)
    implementation(libs.colorPicker)
    implementation(libs.unstyled.disclosure)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.tracing.perfetto)
    implementation(libs.androidx.tracing.perfetto.binary)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.metrics)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.material.motion)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)

    coreLibraryDesugaring(libs.desugarJdkLibs)

    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.profileinstaller)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.test.truth)
    testImplementation(libs.testParameterInjector)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.ui.testJUnit)
    testImplementation(libs.androidx.compose.ui.testManifest)
    testImplementation(libs.androidx.compose.ui.testJUnit.accessibility)
    testImplementation(libs.kotlinx.coroutines.test)

    // ui-test in the app pulls espresso and the runner, and the test APK reuses the app's copies.
    constraints {
        implementation(libs.androidx.espresso.core)
        implementation(libs.androidx.test.runner)
    }
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.testJUnit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.uiautomator)

    baselineProfile(project(":catalog:baselineprofile"))
}
