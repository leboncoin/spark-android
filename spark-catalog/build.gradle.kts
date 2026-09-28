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
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spark.spotless)
}

android {
    namespace = "com.adevinta.spark.catalog"

    compileOptions.isCoreLibraryDesugaringEnabled = true
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "com.adevinta.spark.InternalSparkApi",
            "com.adevinta.spark.ExperimentalSparkApi",
        )
    }
}

dependencies {
    implementation(projects.spark)

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
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.metrics)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.appCompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material.motion)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)

    coreLibraryDesugaring(libs.desugarJdkLibs)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
