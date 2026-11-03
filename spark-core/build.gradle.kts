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
plugins {
    alias(libs.plugins.spark.multiplatform)
    alias(libs.plugins.spark.multiplatform.library)
    alias(libs.plugins.spark.compose.multiplatform)
    alias(libs.plugins.spark.dokka)
    alias(libs.plugins.spark.publishing)
    alias(libs.plugins.spark.dependencyGuard)
}

kotlin {
    android {
        namespace = "com.adevinta.spark.core"
    }
    sourceSets {
        commonMain.dependencies {
            api(project.dependencies.platform(projects.sparkBom))
            api(projects.sparkIcons)
            api(projects.sparkAnnotation)
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.material.ripple)
            implementation(libs.compose.animation)
            implementation(libs.kotlinx.collections.immutable)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.accompanist.drawablepainter)
            implementation(libs.androidx.compose.animation.graphics)
            implementation(libs.androidx.appCompat)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.adevinta.spark.core"
    generateResClass = always
}
