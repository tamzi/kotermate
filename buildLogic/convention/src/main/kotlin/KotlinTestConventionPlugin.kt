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
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
/**
 * Convention plugin for Kotlin testing.
 * Configures both unit and integration testing with separate source sets and tasks.
 *
 * Features:
 * - Sets up separate source sets for unit and integration tests
 * - Configures JUnit 5 for testing
 * - Adds standard test dependencies
 * - Configures test execution with optimized settings
 * - Sets up JaCoCo for test coverage reporting
 * - Provides separate tasks for running unit and integration tests
 */
class KotlinTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply necessary plugins
            apply<JacocoPlugin>()
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            // Create integration test source set
            val sourceSets = extensions.getByType<SourceSetContainer>()
            val integrationTest =
                sourceSets.create("integrationTest") {
                    compileClasspath += sourceSets["main"].output + sourceSets["test"].output
                    runtimeClasspath += sourceSets["main"].output + sourceSets["test"].output
                }

            // Configure dependencies
            configureDependencies(this, integrationTest)

            // Configure integration test task
            configureIntegrationTestTask(this, integrationTest)

            // Configure standard test task for unit tests
            configureUnitTestTask(this)

            // Configure JaCoCo for test coverage
            configureJacoco(this)

            // Make 'check' task depend on integration tests
            tasks.named("check") {
                dependsOn("integrationTest")
            }
        }
    }

    private fun configureDependencies(
        project: Project,
        integrationTest: SourceSet
    ) {
        project.dependencies {
            // Get the version catalog
            val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Standard test dependencies
            add("testImplementation", kotlin("test"))
            add("testImplementation", kotlin("test-junit5"))

            // Integration test dependencies - same as test but in different configuration
            add("integrationTestImplementation", kotlin("test"))
            add("integrationTestImplementation", kotlin("test-junit5"))
            add(
                "integrationTestImplementation",
                project.extensions.getByType<SourceSetContainer>()["main"].output
            )

            // Add extended testing bundle for both test types
            libs.findBundle("testing-extended").ifPresent {
                add("testImplementation", it)
                add("integrationTestImplementation", it)
            }

            // Add Kotest for both test types
            libs.findBundle("kotest").ifPresent {
                add("testImplementation", it)
                add("integrationTestImplementation", it)
            }

            // Add coroutines test
            libs.findLibrary("kotlinx-coroutines-test").ifPresent {
                add("testImplementation", it)
                add("integrationTestImplementation", it)
            }

            // Add mockk for mocking
            libs.findLibrary("mockk").ifPresent {
                add("testImplementation", it)
                add("integrationTestImplementation", it)
            }

            // Add testcontainers for integration tests
            libs.findBundle("testcontainers").ifPresent {
                add("integrationTestImplementation", it)
            }

            // Add JUnit Jupiter API and Engine
            libs.findLibrary("junit-jupiter-api").ifPresent {
                add("testImplementation", it)
                add("integrationTestImplementation", it)
            }

            libs.findLibrary("junit-jupiter-engine").ifPresent {
                add("testRuntimeOnly", it)
                add("integrationTestRuntimeOnly", it)
            }
        }
    }

    private fun configureIntegrationTestTask(
        project: Project,
        integrationTest: SourceSet
    ) {
        project.tasks.register<Test>("integrationTest") {
            description = "Runs integration tests."
            group = "verification"

            testClassesDirs = integrationTest.output.classesDirs
            classpath = integrationTest.runtimeClasspath

            // Always run integration tests after unit tests
            shouldRunAfter("test")

            // Use JUnit 5
            useJUnitPlatform()

            // Configure test execution logging
            testLogging {
                events("passed", "skipped", "failed")
                showExceptions = true
                showCauses = true
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
            }

            // Generate HTML test reports
            reports {
                html.required.set(true)
                junitXml.required.set(true)
            }
        }
    }

    private fun configureUnitTestTask(project: Project) {
        project.tasks.withType<Test>().configureEach {
            // Skip integration tests for the standard test task
            if (this.name == "test") {
                filter {
                    excludeTestsMatching("*IT")
                    excludeTestsMatching("*IntegrationTest")
                }
            }

            // Use JUnit 5
            useJUnitPlatform()

            // Enable parallel test execution for faster builds
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

            // Set memory constraints for the test process
            jvmArgs = listOf("-Xmx1g", "-Xms256m")

            // Configure test execution logging
            testLogging {
                events("passed", "skipped", "failed")
                showExceptions = true
                showCauses = true
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
                showStandardStreams = false // Set to true for verbose output
            }

            // Generate HTML test reports
            reports {
                html.required.set(true)
                junitXml.required.set(true)
            }
        }
    }

    private fun configureJacoco(project: Project) {
        project.tasks.withType<JacocoReport>().configureEach {
            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(false)
            }
        }
    }
}