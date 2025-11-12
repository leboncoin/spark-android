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
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.provideDelegate
import kotlin.reflect.KProperty

internal fun Project.spark() = SparkProperties(this)

internal class SparkProperties private constructor(project: Project) {
    private val catalog by lazy(project.rootProject::getVersionsCatalog)
    val libraries by lazy { SparkLibraries(catalog) }
    val versions by lazy { SparkVersions(catalog) }
    val ciUnitTestVariant = project.providers.gradleProperty("spark.ci-unit-test.variant").orElse("release")

    companion object {
        private const val EXT_KEY = "com.adevinta.spark.SparkProperties"
        operator fun invoke(project: Project): SparkProperties = project.getOrCreateExtra(EXT_KEY, ::SparkProperties)
    }
}

@Suppress("HasPlatformType", "PropertyName")
internal class SparkVersions(catalog: VersionCatalog) {
    val `targetSdk` by catalog
    val `minCompileSdk` by catalog
    val `compileSdk` by catalog
    val `kotlin` by catalog

    private operator fun VersionCatalog.getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ) = findVersion(property.name).orElseThrow {
        IllegalStateException("Missing catalog version ${property.name}")
    }
}

@Suppress("HasPlatformType", "PropertyName")
internal class SparkLibraries(catalog: VersionCatalog) {
    val `androidx-compose-bom` by catalog
    val `dokka-android-documentation-plugin` by catalog
    val `kotlin-bom` by catalog
    val `ktlint-bom` by catalog

    private operator fun VersionCatalog.getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ) = findLibrary(property.name).orElseThrow {
        IllegalStateException("Missing catalog library ${property.name}")
    }
}
