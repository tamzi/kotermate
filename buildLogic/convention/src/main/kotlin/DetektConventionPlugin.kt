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

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import java.io.File

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            // Apply the Detekt plugin if it hasn't been applied already.
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            // Add the detekt-formatting plugin dependency
            dependencies {
                "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
            }

            // Configure Detekt for the project.
            extensions.configure<DetektExtension> {
                // Only use the configuration from codequality.yml
                config.setFrom(files("$rootDir/config/codequality.yml"))

                // Only set baseline if the file exists
                val baselineFile = File("$rootDir/config/detekt/baseline.xml")
                if (baselineFile.exists()) {
                    baseline = baselineFile
                    logger.lifecycle("Using detekt baseline file: ${baselineFile.absolutePath}")
                } else {
                    logger.lifecycle(
                        "No detekt baseline file found at: ${baselineFile.absolutePath}"
                    )
                }

                // Set buildUponDefaultConfig to false to ensure only our config is used
                buildUponDefaultConfig = false

                // Enable auto-correction of detected issues.
                autoCorrect = true

                // Enable parallel execution for better performance
                parallel = true

                // Log the configuration being used
                logger.lifecycle(
                    "Detekt using configuration from: $rootDir/config/codequality.yml"
                )
            }

            // Configure report generation on the tasks
            tasks.withType<Detekt>().configureEach {
                reports {
                    html.required.set(true)
                    html.outputLocation.set(layout.buildDirectory.file("reports/detekt.html"))
                    xml.required.set(false)
                    txt.required.set(false)
                    md.required.set(false)
                }

                // Set JVM target version directly
                jvmTarget = "21"
            }

            // Configure baseline creation task
            tasks.withType<DetektCreateBaselineTask>().configureEach {
                // Set JVM target version directly
                jvmTarget = "21"
            }
        }
    }
}