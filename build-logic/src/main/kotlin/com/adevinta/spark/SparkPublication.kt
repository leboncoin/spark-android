/*
 * Copyright (c) 2025 Adevinta
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

import com.android.build.api.dsl.LibraryExtension
import nmcp.NmcpAggregationExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask
import org.jetbrains.kotlin.gradle.utils.named
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

internal object SparkPublication {

    fun configureRootProject(project: Project) = with(project) {
        apply(plugin = "com.gradleup.nmcp.aggregation")
        configure<NmcpAggregationExtension> {
            centralPortal {
                username = System.getenv("CENTRAL_PORTAL_USERNAME")
                password = System.getenv("CENTRAL_PORTAL_PASSWORD")
                publishingType = "AUTOMATIC"
                publishingTimeout = 20.minutes.toJavaDuration()
            }
        }
        dependencies {
            add("nmcpAggregation", project(":spark-bom"))
            add("nmcpAggregation", project(":spark-icons"))
            add("nmcpAggregation", project(":spark"))
        }
    }

    fun configureSubproject(project: Project) = with(project) {
        apply(plugin = "org.gradle.maven-publish")
        apply(plugin = "org.gradle.signing")
        apply(plugin = "com.gradleup.nmcp")

        configureRepository()
        registerPublication()
        configureSigning()
    }

    private fun Project.configureRepository() = configure<PublishingExtension> {
        repositories {
            mavenLocal {
                name = "Local"
                url = uri(rootProject.layout.buildDirectory.dir(".m2/repository"))
            }
        }
    }

    private fun Project.registerPublication() = configure<PublishingExtension> {
        publications {
            if (isKotlinMultiplatform) {
                // KMP auto-generates per-target publications (kotlinMultiplatform, android, jvm…).
                // Don't register a competing "maven" publication — just configure what KMP created.
                val dokkaJavadocJar = tasks.register<Jar>("dokkaHtmlJar") {
                    description = "A javadoc JAR containing Dokka HTML documentation"
                    from(tasks.named<DokkaGenerateTask>("dokkaGeneratePublicationHtml").flatMap { it.outputDirectory })
                    archiveClassifier.set("javadoc")
                }
                afterEvaluate {
                    publications.withType(MavenPublication::class.java) {
                        configurePom()
                    }
                    (publications.findByName("kotlinMultiplatform") as? MavenPublication)
                        ?.artifact(dokkaJavadocJar)
                }
            } else {
                register<MavenPublication>("maven") {
                    when {
                        isAndroidLibrary -> configureAndroidPublication(this)
                        isJavaPlatform -> from(components["javaPlatform"])
                        else -> TODO("Unsupported project type $this")
                    }
                    configurePom()
                }
            }
        }
    }

    private fun Project.configureAndroidPublication(publication: MavenPublication) {
        val dokkaJavadocJar = tasks.register<Jar>("dokkaHtmlJar") {
            description = "A javadoc JAR containing Dokka HTML documentation"
            from(tasks.named<DokkaGenerateTask>("dokkaGeneratePublicationHtml").flatMap { it.outputDirectory })
            archiveClassifier.set("javadoc")
        }
        configure<LibraryExtension> {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }
        // AGP creates software components during the afterEvaluate callback step...
        afterEvaluate {
            publication.from(components.getByName("release"))
            publication.artifact(dokkaJavadocJar)
        }
    }

    private fun MavenPublication.configurePom() = pom {
        name = "Spark"
        description = "Spark Design System"
        url = "https://github.com/leboncoin/spark-android"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        scm {
            url = "https://github.com/leboncoin/spark-android"
        }
        developers {
            developer {
                name = "Adevinta Engineers"
                email = "engineers@adevinta.com"
            }
        }
    }

    private fun Project.configureSigning() = configure<SigningExtension> signing@{
        val signingKey: String? by project
        val signingPassword: String? by project
        if (signingKey == null || signingPassword == null) return@signing
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(the<PublishingExtension>().publications)
    }
}
