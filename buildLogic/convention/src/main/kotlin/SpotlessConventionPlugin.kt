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

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.util.Properties

/**
 * Convention plugin for code formatting using Spotless.
 * Loads configuration from external properties file and uses codequality.yml.
 */
class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")

            // Load configuration from properties file
            val configFile = rootProject.file("config/spotless/spotless.properties")
            val config = loadSpotlessConfig(configFile)

            extensions.configure<SpotlessExtension> {
                // Use codequality.yml instead of .editorconfig
                val codeQualityFile = rootProject.file("config/codequality.yml")
                if (codeQualityFile.exists()) {
                    logger.lifecycle(
                        "Using codequality.yml file at: ${codeQualityFile.absolutePath}"
                    )
                }

                kotlin {
                    target("**/*.kt")
                    targetExclude(
                        config.getProperty(
                            "kotlin.exclude",
                            "**/build/**/*.kt,**/generated/**/*.kt"
                        )
                    )
                    // Exclude all copyright template files
                    targetExclude("config/spotless/copyright.kt")
                    targetExclude("**/copyright.kt")

                    // Configure ktlint with version from config
                    val ktlintVersion = config.getProperty("ktlint.version", "1.5.0")
                    ktlint(ktlintVersion)
                        .editorConfigOverride(
                            mapOf(
                                // Ensure ktlint uses the .editorconfig file
                                "ij_kotlin_imports_layout" to "*, java.**, javax.**, kotlin.**, ^",
                                "ij_kotlin_allow_trailing_comma" to "false",
                                "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
                                "ktlint_standard_no-wildcard-imports" to "true",
                                // Disable final newline rule to match codequality.yml
                                "insert_final_newline" to "false"
                            )
                        )

                    // Apply license header if specified
                    val licenseHeaderPath = config.getProperty("license.header.path", "")
                    if (licenseHeaderPath.isNotEmpty()) {
                        val licenseHeaderFile = rootProject.file(licenseHeaderPath)
                        if (licenseHeaderFile.exists()) {
                            logger.lifecycle("Using license header from: $licenseHeaderPath")
                            licenseHeader(licenseHeaderFile.readText())
                        }
                    }
                }

                kotlinGradle {
                    target("**/*.gradle.kts")
                    targetExclude(
                        config.getProperty("kotlinGradle.exclude", "**/build/**/*.gradle.kts")
                    )
                    // Exclude copyright.kts files
                    targetExclude("config/spotless/copyright.kts")
                    targetExclude("**/copyright.kts")

                    ktlint(config.getProperty("ktlint.version", "1.5.0"))
                        .editorConfigOverride(
                            mapOf(
                                // Disable final newline rule to match codequality.yml
                                "insert_final_newline" to "false"
                            )
                        )
                }

                // Configure XML formatting if enabled
                if (config.getProperty("xml.format.enabled", "true").toBoolean()) {
                    format("xml") {
                        target("**/*.xml")
                        targetExclude(config.getProperty("xml.exclude", "**/build/**/*.xml"))
                        // Exclude copyright.xml files
                        targetExclude("config/spotless/copyright.xml")
                        targetExclude("**/copyright.xml")

                        leadingTabsToSpaces(config.getProperty("xml.indent.spaces", "4").toInt())
                        trimTrailingWhitespace()
                        // Note: We're NOT calling endWithNewline() here to avoid adding newlines
                        // The endWithNewline() method adds a newline when called, it doesn't take parameters
                    }
                }

                // Configure misc formats (markdown, gitignore, yaml, etc.)
                format("misc") {
                    target("**/*.md", "**/.gitignore", "**/*.yaml", "**/*.yml")
                    targetExclude(config.getProperty("misc.exclude", "**/build/**"))

                    trimTrailingWhitespace()
                    leadingTabsToSpaces(2)
                    // Note: We're NOT calling endWithNewline() here to avoid adding newlines
                    // The endWithNewline() method adds a newline when called, it doesn't take parameters
                }
            }
        }
    }

    private fun loadSpotlessConfig(configFile: File): Properties {
        val properties = Properties()
        if (configFile.exists()) {
            configFile.inputStream().use { properties.load(it) }
        }
        return properties
    }
}