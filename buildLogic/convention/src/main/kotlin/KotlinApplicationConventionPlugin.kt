/*
 * Copyright 2025 kotermate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tamzi.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Convention plugin for Kotlin applications.
 * Applies standard configurations for Kotlin JVM applications in the project.
 *
 * Features:
 * - Configures Kotlin JVM with Java 23 toolchain
 * - Sets up strict compiler options
 * - Adds standard Kotlin dependencies
 * - Configures JUnit 5 for testing
 * - Sets up optimized JVM arguments for application execution
 * - Configures application distribution with necessary resources
 * - Adds metadata to JAR manifests
 */
class KotlinApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply the necessary plugins
            apply<ApplicationPlugin>()
            apply(plugin = "org.jetbrains.kotlin.jvm")

            // Configure Kotlin
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(21) // Use Java 21 toolchain

                // Configure Kotlin compiler options
                compilerOptions {
                    freeCompilerArgs.add("-Xjsr305=strict") // JSR 305 annotations support
                    allWarningsAsErrors.set(true) // Treat warnings as errors
                }
            }

            // Add standard dependencies
            dependencies {
                // Add test dependencies
                add("testImplementation", kotlin("test"))
                add("testImplementation", kotlin("test-junit5"))

                // Add implementation dependencies
                add("implementation", kotlin("stdlib"))
                add("implementation", kotlin("reflect"))
            }

            // Configure test tasks
            tasks.withType<Test>().configureEach {
                useJUnitPlatform() // Use JUnit 5 for tests

                // Configure test execution
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }

            // Configure application packaging
            extensions.configure<JavaApplication> {
                // Set default JVM arguments for the application
                // These can be overridden in individual projects if needed
                applicationDefaultJvmArgs =
                    listOf(
                        "-Xmx2g", // Maximum heap size
                        "-Xms512m", // Initial heap size
                        "-XX:+UseG1GC", // Use G1 Garbage Collector
                        "-XX:MaxGCPauseMillis=200" // Target max GC pause time
                    )

                // Set application main class if not specified in individual projects
                // Projects should override this as needed
                // mainClass.set("org.tamzi.MainKt")
            }

            // Configure JAR manifest
            tasks.withType<Jar>().configureEach {
                manifest {
                    attributes(
                        mapOf(
                            "Implementation-Title" to project.name,
                            "Implementation-Version" to project.version
                        )
                    )
                }
            }
        }
    }
}