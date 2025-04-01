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
package org.tamzi.convention.kotermate

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin for configuring instrumented tests
 */
class InstrumentedTestsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java")
            }

            // Configure test tasks
            tasks.withType<Test> {
                // Enable JUnit Platform (JUnit 5) support
                useJUnitPlatform()

                // Configure test execution options
                testLogging {
                    events("passed", "skipped", "failed")
                }

                // Fail the build on test failures
                ignoreFailures = false

                // Maximum number of test processes
                maxParallelForks =
                    (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1

                // Configure JVM arguments for tests
                jvmArgs = listOf("-Xmx1g", "-Xms512m")
            }

            // Add dependencies for instrumented tests
            dependencies {
                // JUnit 5
                add("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.9.2")
                add("testImplementation", "org.junit.jupiter:junit-jupiter-params:5.9.2")
                add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:5.9.2")

                // Mockito
                add("testImplementation", "org.mockito:mockito-core:5.3.1")
                add("testImplementation", "org.mockito:mockito-junit-jupiter:5.3.1")

                // AssertJ for fluent assertions
                add("testImplementation", "org.assertj:assertj-core:3.24.2")
            }
        }
    }
}